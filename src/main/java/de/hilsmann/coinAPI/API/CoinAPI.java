package de.hilsmann.coinAPI.API;

import de.hilsmann.coinAPI.Konto.KontoFunctions;
import de.hilsmann.coinAPI.Konto.KontoManager;
import de.hilsmann.coinAPI.MySQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CoinAPI {

    private static final Map<String, Integer> coinCache = new ConcurrentHashMap<>();
    private static final Map<String, Integer> gemCache = new ConcurrentHashMap<>();
    private static final Map<String, Integer> crystalCache = new ConcurrentHashMap<>();

    private static final Set<String> pendingSync = ConcurrentHashMap.newKeySet();
    private static boolean dbAvailable = true;
    private static Plugin plugin;

    public static void init(Plugin mainPlugin) {
        plugin = mainPlugin;
        startHealthCheckScheduler();

        // Lade temporäre Backups, falls vorhanden
        loadBackupData();
    }

    private static void loadBackupData() {
        Map<String, Object> backupData = CacheBackupManager.loadBackup();

        if (!backupData.isEmpty()) {
            System.out.println("Lade Cache-Backup-Daten...");

            coinCache.putAll(CacheBackupManager.extractMap(backupData.get("coins")));
            gemCache.putAll(CacheBackupManager.extractMap(backupData.get("gems")));
            crystalCache.putAll(CacheBackupManager.extractMap(backupData.get("crystals")));
            pendingSync.addAll(CacheBackupManager.extractSet(backupData.get("pendingSync")));
        }
    }



    private static void startHealthCheckScheduler() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, CoinAPI::attemptSync, 20L, 20L * 60); // alle 60 Sekunden
    }

    private static Optional<Integer> getValueFromDatabase(String uuid, String column) {
        try (PreparedStatement st = MySQL.con.prepareStatement("SELECT " + column + " FROM coins WHERE UUID = ?")) {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt(column));
            }
        } catch (SQLException e) {
            dbAvailable = false;
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static void updateDatabase(String uuid, int value, String column) throws SQLException {
        String update = "UPDATE coins SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement st = MySQL.con.prepareStatement(update)) {
            st.setInt(1, value);
            st.setString(2, uuid);
            st.executeUpdate();
        }
    }

    // ---------------- Coins ----------------

    public static int getCoins(String uuid) {
        if (!dbAvailable) {
            return coinCache.getOrDefault(uuid, 0);
        }
        return getValueFromDatabase(uuid, "coins").orElseGet(() -> coinCache.getOrDefault(uuid, 0));
    }

    public static void setCoins(String uuid, int coins) {
        int oldCoins = getCoins(uuid);
        coinCache.put(uuid, coins);

        if (dbAvailable) {
            try {
                updateDatabase(uuid, coins, "coins");
            } catch (Exception e) {
                dbAvailable = false;
                pendingSync.add(uuid);
            }
        } else {
            pendingSync.add(uuid);
        }

        Bukkit.getPluginManager().callEvent(new CoinChangeEvent(uuid, "coins", oldCoins, coins));
    }

    public static void addCoins(String uuid, int coins) {
        setCoins(uuid, getCoins(uuid) + coins);
    }

    public static void removeCoins(String uuid, int coins) {
        setCoins(uuid, getCoins(uuid) - coins);
    }

    // ---------------- Gems ----------------

    public static int getGems(String uuid) {
        if (!dbAvailable) {
            return gemCache.getOrDefault(uuid, 0);
        }
        return getValueFromDatabase(uuid, "gems").orElseGet(() -> gemCache.getOrDefault(uuid, 0));
    }

    public static void setGems(String uuid, int gems) {
        int oldGems = getGems(uuid);
        gemCache.put(uuid, gems);

        if (dbAvailable) {
            try {
                updateDatabase(uuid, gems, "gems");
            } catch (Exception e) {
                dbAvailable = false;
                pendingSync.add(uuid);
            }
        } else {
            pendingSync.add(uuid);
        }

        Bukkit.getPluginManager().callEvent(new CoinChangeEvent(uuid, "gems", oldGems, gems));
    }

    public static void addGems(String uuid, int gems) {
        setGems(uuid, getGems(uuid) + gems);
    }

    public static void removeGems(String uuid, int gems) {
        setGems(uuid, getGems(uuid) - gems);
    }

    // ---------------- Crystals ----------------

    public static int getCrystals(String uuid) {
        if (!dbAvailable) {
            return crystalCache.getOrDefault(uuid, 0);
        }
        return getValueFromDatabase(uuid, "crystals").orElseGet(() -> crystalCache.getOrDefault(uuid, 0));
    }

    public static void setCrystals(String uuid, int crystals) {
        int oldCrystals = getCrystals(uuid);
        crystalCache.put(uuid, crystals);

        if (dbAvailable) {
            try {
                updateDatabase(uuid, crystals, "crystals");
            } catch (Exception e) {
                dbAvailable = false;
                pendingSync.add(uuid);
            }
        } else {
            pendingSync.add(uuid);
        }

        Bukkit.getPluginManager().callEvent(new CoinChangeEvent(uuid, "crystals", oldCrystals, crystals));
    }

    public static void addCrystals(String uuid, int crystals) {
        setCrystals(uuid, getCrystals(uuid) + crystals);
    }

    public static void removeCrystals(String uuid, int crystals) {
        setCrystals(uuid, getCrystals(uuid) - crystals);
    }

    public static List<String> getTopPlayers() {
        List<String> result = new ArrayList<>();

        if (dbAvailable) {
            String query = "SELECT UUID FROM coins ORDER BY coins DESC LIMIT 20";
            try (PreparedStatement st = MySQL.con.prepareStatement(query);
                 ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    result.add(rs.getString("UUID"));
                }
            } catch (SQLException e) {
                dbAvailable = false;
                System.out.println("Fehler beim Abrufen der Top-Spieler. Fallback auf Cache.");
            }
        }

        // Fallback auf Cache, wenn DB nicht verfügbar
        if (!dbAvailable) {
            result.addAll(coinCache.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(20)
                    .map(Map.Entry::getKey)
                    .toList());
        }

        return result;
    }

    private static final Map<String, String> nameCache = new ConcurrentHashMap<>();

    public static String getNameFrom(String uuid) {
        if (!dbAvailable) {
            return nameCache.getOrDefault(uuid, "Unbekannter Spieler");
        }

        try (PreparedStatement st = MySQL.con.prepareStatement("SELECT name FROM coins WHERE UUID = ?")) {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                nameCache.put(uuid, name); // Cache aktualisieren
                return name;
            }
        } catch (SQLException e) {
            dbAvailable = false;
            System.out.println("Datenbank nicht verfügbar, Fallback auf Cache für UUID: " + uuid);
        }

        return nameCache.getOrDefault(uuid, "Unbekannter Spieler");
    }

    public static void transactCoins(String senderUUID, String receiverUUID, int coins) {
        if (coins <= 0) {
            System.out.println("Ungültiger Transaktionsbetrag: " + coins);
            return;
        }

        int senderCoins = getCoins(senderUUID);
        int receiverCoins = getCoins(receiverUUID);

        if (senderCoins < coins) {
            System.out.println("Sender hat nicht genügend Coins. Transaktion abgebrochen.");
            return;
        }

        // Transaktion durchführen
        setCoins(senderUUID, senderCoins - coins);
        setCoins(receiverUUID, receiverCoins + coins);

        // Konto-Log aktualisieren
        KontoFunctions senderKonto = KontoManager.getKonto(UUID.fromString(senderUUID));
        KontoFunctions receiverKonto = KontoManager.getKonto(UUID.fromString(receiverUUID));

        long timestamp = System.currentTimeMillis();

        senderKonto.addTransactionLog("Überweisung an " + receiverUUID, -coins, timestamp, receiverUUID, senderUUID);
        receiverKonto.addTransactionLog("Überweisung von " + senderUUID, coins, timestamp, receiverUUID, senderUUID);

        // Events für beide Spieler feuern
        Bukkit.getPluginManager().callEvent(new CoinChangeEvent(senderUUID, "coins", senderCoins, senderCoins - coins));
        Bukkit.getPluginManager().callEvent(new CoinChangeEvent(receiverUUID, "coins", receiverCoins, receiverCoins + coins));

        System.out.println("Transaktion abgeschlossen: " + coins + " Coins von " + senderUUID + " zu " + receiverUUID);
    }

    public static void createCoin(String uuid, String name, int coins, int gems, int crystals) {
        if (getCoins(uuid) == -999999999) {
            // Neuer Eintrag
            try {
                String insert = "INSERT INTO coins (UUID, name, coins, gems, crystals) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement st = MySQL.con.prepareStatement(insert)) {
                    st.setString(1, uuid);
                    st.setString(2, name);
                    st.setInt(3, coins);
                    st.setInt(4, gems);
                    st.setInt(5, crystals);
                    st.executeUpdate();
                }
                // Cache aktualisieren
                coinCache.put(uuid, coins);
                gemCache.put(uuid, gems);
                crystalCache.put(uuid, crystals);
                nameCache.put(uuid, name);
            } catch (SQLException e) {
                dbAvailable = false;
                System.out.println("Datenbank nicht verfügbar, Fallback auf Cache für UUID: " + uuid);
                coinCache.put(uuid, coins);
                gemCache.put(uuid, gems);
                crystalCache.put(uuid, crystals);
                nameCache.put(uuid, name);
                pendingSync.add(uuid);
            }
        } else {
            // Konto aktualisieren
            setCoins(uuid, coins);
            setGems(uuid, gems);
            setCrystals(uuid, crystals);
        }
    }

    public static boolean exists(String uuid) {
        if (!dbAvailable) {
            return coinCache.containsKey(uuid);
        }

        String query = "SELECT 1 FROM coins WHERE UUID = ?";
        try (PreparedStatement st = MySQL.con.prepareStatement(query)) {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            dbAvailable = false;
            return coinCache.containsKey(uuid);
        }
    }


    // ---------------- Synchronisation ----------------

    private static boolean checkDatabaseConnection() {
        try (PreparedStatement ps = MySQL.con.prepareStatement("SELECT 1")) {
            ps.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void attemptSync() {
        if (!dbAvailable && checkDatabaseConnection()) {
            dbAvailable = true;
            System.out.println("DB wieder verfügbar, starte Synchronisation...");

            for (String uuid : pendingSync) {
                try {
                    updateDatabase(uuid, coinCache.getOrDefault(uuid, 0), "coins");
                    updateDatabase(uuid, gemCache.getOrDefault(uuid, 0), "gems");
                    updateDatabase(uuid, crystalCache.getOrDefault(uuid, 0), "crystals");
                    pendingSync.remove(uuid);
                } catch (SQLException e) {
                    System.out.println("Fehler beim Synchronisieren von UUID: " + uuid);
                }
            }

            // Backup-Datei löschen nach erfolgreicher Synchronisation
            CacheBackupManager.deleteBackup();
            System.out.println("Backup-Datei erfolgreich gelöscht.");
        }
    }


    public static void dispose() {
        CacheBackupManager.saveBackup(coinCache, gemCache, crystalCache, pendingSync);
        System.out.println("CoinAPI Cache-Daten für Fallback gespeichert.");
    }

}
