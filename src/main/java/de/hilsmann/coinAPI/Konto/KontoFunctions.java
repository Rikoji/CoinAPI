package de.hilsmann.coinAPI.Konto;

import de.hilsmann.coinAPI.MySQL.MySQL;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class KontoFunctions {
    private static final Logger LOGGER = Logger.getLogger(KontoFunctions.class.getName());
    private final UUID playerUUID;
    private final List<Transaction> transactionLog;

    public KontoFunctions(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.transactionLog = new ArrayList<>();
        loadFromDatabase(); // Daten beim Erstellen laden
    }

    /**
     * Fügt eine Transaktion hinzu und speichert diese direkt in der Datenbank.
     */
    public void addTransactionLog(String action, double amount, long timestamp, String receiverUUID, String senderUUID) {
        Transaction transaction = new Transaction(action, amount, timestamp, receiverUUID, senderUUID);
        transactionLog.add(transaction);
        saveTransactionToDatabase(transaction);
    }

    /**
     * Speichert eine einzelne Transaktion in die Datenbank.
     */
    private void saveTransactionToDatabase(Transaction transaction) {
        String sql = "INSERT INTO transactions (playerUUID, action, amount, timestamp, receiverUUID, senderUUID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = MySQL.con.prepareStatement(sql)) {
            st.setString(1, playerUUID.toString());
            st.setString(2, transaction.action);
            st.setDouble(3, transaction.amount);
            st.setLong(4, transaction.timestamp);
            st.setString(5, transaction.receiverUUID);
            st.setString(6, transaction.senderUUID);
            st.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning("Fehler beim Speichern der Transaktion für " + getPlayerName() + ": " + e.getMessage());
        }
    }

    /**
     * Lädt alle Transaktionen eines Spielers aus der Datenbank.
     */
    private void loadFromDatabase() {
        String sql = "SELECT * FROM transactions WHERE playerUUID = ?";
        try (PreparedStatement st = MySQL.con.prepareStatement(sql)) {
            st.setString(1, playerUUID.toString());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String action = rs.getString("action");
                double amount = rs.getDouble("amount");
                long timestamp = rs.getLong("timestamp");
                String receiverUUID = rs.getString("receiverUUID");
                String senderUUID = rs.getString("senderUUID");

                transactionLog.add(new Transaction(action, amount, timestamp, receiverUUID, senderUUID));
            }
        } catch (SQLException e) {
            LOGGER.warning("Fehler beim Laden des Transaktionslogs für " + getPlayerName() + ": " + e.getMessage());
        }
    }

    /**
     * Gibt das Transaktionslog als lesbare Liste zurück.
     */
    public List<String> getTransactionLogList() {
        List<String> logList = new ArrayList<>();
        for (Transaction transaction : transactionLog) {
            logList.add("Aktion: " + transaction.action +
                    ", Betrag: " + transaction.amount +
                    ", Zeit: " + transaction.timestamp +
                    ", Empfänger: " + transaction.receiverUUID +
                    ", Sender: " + transaction.senderUUID);
        }
        return logList;
    }

    /**
     * Gibt das Transaktionslog als JSON-String zurück.
     */
    public String getTransactionLog() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < transactionLog.size(); i++) {
            sb.append(transactionLog.get(i).toJson());
            if (i < transactionLog.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Entfernt das Objekt und sichert die Daten vorher.
     */
    public void dispose() {
        transactionLog.clear();
        LOGGER.info("KontoFunctions-Instanz für " + getPlayerName() + " wurde entfernt.");
    }

    /**
     * Holt den Namen des Spielers.
     */
    private String getPlayerName() {
        return Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    /**
     * Interne Klasse zur Speicherung einer Transaktion.
     */
    private static class Transaction {
        private final String action;
        private final double amount;
        private final long timestamp;
        private final String receiverUUID;
        private final String senderUUID;

        public Transaction(String action, double amount, long timestamp, String receiverUUID, String senderUUID) {
            this.action = action;
            this.amount = amount;
            this.timestamp = timestamp;
            this.receiverUUID = receiverUUID;
            this.senderUUID = senderUUID;
        }

        public String toJson() {
            return "{\"action\":\"" + action + "\", \"amount\":" + amount + ", \"timestamp\":" + timestamp + ", \"receiverUUID\":\"" + receiverUUID + "\", \"senderUUID\":\"" + senderUUID + "\"}";
        }
    }
}
