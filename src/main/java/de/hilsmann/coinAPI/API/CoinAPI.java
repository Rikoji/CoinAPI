package de.hilsmann.coinAPI.API;

import de.hilsmann.coinAPI.Konto.KontoFunctions;
import de.hilsmann.coinAPI.Konto.KontoManager;
import de.hilsmann.coinAPI.MySQL.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CoinAPI {

    private static Optional<Integer> getValueFromDatabase(String uuid, String column) {
        try (PreparedStatement st = MySQL.con.prepareStatement("SELECT " + column + " FROM coins WHERE UUID = ?")) {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static int getCoins(String uuid) {
        return getValueFromDatabase(uuid, "coins").orElse(-999999999);
    }

    public static String getNameFrom(String uuid) {
        try (PreparedStatement st = MySQL.con.prepareStatement("SELECT name FROM coins WHERE UUID = ?")) {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Kein Spieler gefunden";
    }

    public static void setName(String uuid, String name) {
        if (getCoins(uuid) == -999999999) {
            createNewEntry(uuid, name, 0, 0, 0);
        } else {
            updateDatabase(uuid, name, 0, 0, 0);
        }
    }

    public static int getGems(String uuid) {
        return getValueFromDatabase(uuid, "gems").orElse(-999999999);
    }

    public static int getCrystals(String uuid) {
        return getValueFromDatabase(uuid, "crystals").orElse(-999999999);
    }

    public static void createCoin(String uuid, String name, int coins, int gems, int crystals) {
        if (getCoins(uuid) == -999999999) {
            createNewEntry(uuid, name, coins, gems, crystals);
        } else {
            updateDatabase(uuid, name, coins, gems, crystals);
        }
    }

    private static void updateDatabase(String uuid, String name, int coins, int gems, int crystals) {
        String update = "UPDATE coins SET name = ?, coins = ?, gems = ?, crystals = ? WHERE UUID = ?";
        try (PreparedStatement st = MySQL.con.prepareStatement(update)) {
            st.setString(1, name);
            st.setInt(2, coins);
            st.setInt(3, gems);
            st.setInt(4, crystals);
            st.setString(5, uuid);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createNewEntry(String uuid, String name, int coins, int gems, int crystals) {
        String insert = "INSERT INTO coins (UUID, name, coins, gems, crystals) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = MySQL.con.prepareStatement(insert)) {
            st.setString(1, uuid);
            st.setString(2, name);
            st.setInt(3, coins);
            st.setInt(4, gems);
            st.setInt(5, crystals);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setCoins(String uuid, int coins) {
        updateCoinsInDatabase(uuid, coins, "coins");
    }

    public static void setGems(String uuid, int gems) {
        updateCoinsInDatabase(uuid, gems, "gems");
    }

    public static void setCrystals(String uuid, int crystals) {
        updateCoinsInDatabase(uuid, crystals, "crystals");
    }

    private static void updateCoinsInDatabase(String uuid, int value, String column) {
        String update = "UPDATE coins SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement st = MySQL.con.prepareStatement(update)) {
            st.setInt(1, value);
            st.setString(2, uuid);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTopPlayers() {
        List<String> result = new ArrayList<>();
        String query = "SELECT UUID FROM coins ORDER BY coins DESC LIMIT 20";

        try (PreparedStatement st = MySQL.con.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                result.add(rs.getString("UUID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void addCoins(String uuid, int coins) {
        setCoins(uuid, getCoins(uuid) + coins);
    }

    public static void removeCoins(String uuid, int coins) {
        setCoins(uuid, getCoins(uuid) - coins);
    }

    public static void transactCoins(String senderUUID, String receiverUUID, int coins){
        addCoins(receiverUUID, coins);
        removeCoins(senderUUID, coins);

        KontoFunctions kontoSender = KontoManager.getKonto(UUID.fromString(senderUUID));
        KontoFunctions kontoReceiver = KontoManager.getKonto(UUID.fromString(receiverUUID));

        kontoSender.addTransactionLog("Überweisung", -coins, System.currentTimeMillis(), receiverUUID, senderUUID);
        kontoReceiver.addTransactionLog("Überweisung", coins, System.currentTimeMillis(), receiverUUID, senderUUID);
    }

    public static void addGems(String uuid, int gems) {
        setGems(uuid, getGems(uuid) + gems);
    }

    public static void removeGems(String uuid, int gems) {
        setGems(uuid, getGems(uuid) - gems);
    }

    public static void addCrystals(String uuid, int crystals) {
        setCrystals(uuid, getCrystals(uuid) + crystals);
    }

    public static void removeCrystals(String uuid, int crystals) {
        setCrystals(uuid, getCrystals(uuid) - crystals);
    }
}