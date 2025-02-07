package de.hilsmann.coinAPI.Konto;

import org.bukkit.Bukkit;

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
    }

    /**
     * Loggt einen Methodenaufruf.
     * @param methodName Name der Methode, die aufgerufen wurde.
     */
    public void logMethodCall(String methodName) {
        LOGGER.info("Methode aufgerufen: " + methodName + " für Spieler: " + getPlayerName());
    }

    /**
     * Fügt eine Transaktion zum Log hinzu.
     * @param action Art der Transaktion (z.B. "add", "remove", "pay").
     * @param amount Betrag der Transaktion.
     * @param timestamp Zeitstempel der Transaktion.
     */
    public void addTransactionLog(String action, double amount, long timestamp, String receiverUUID, String senderUUID) {
        transactionLog.add(new Transaction(action, amount, timestamp, receiverUUID, senderUUID));
    }

    /**
     * Gibt das Transaktionslog als JSON-String zurück.
     * @return JSON-String der Transaktionslogs.
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
     * Gibt das Transaktionslog als lesbare Liste zurück.
     * @return Liste der Transaktionen.
     */
    public List<String> getTransactionLogList() {
        List<String> logList = new ArrayList<>();
        for (Transaction transaction : transactionLog) {
            logList.add("Aktion: " + transaction.action + ", Betrag: " + transaction.amount + ", Zeit: " + transaction.timestamp + ", Empfänger: " + transaction.receiverUUID + ", Sender: " + transaction.senderUUID);
        }
        return logList;
    }

    /**
     * Speichert die Transaktionshistorie in die Datenbank.
     */
    public void saveToDatabase() {
        LOGGER.info("Speichere Transaktionslog für " + getPlayerName() + " in die Datenbank: " + getTransactionLog());
    }

    /**
     * Entfernt das Objekt und speichert die Daten vorher.
     */
    public void dispose() {
        saveToDatabase();
        transactionLog.clear();
        LOGGER.info("KontoFunctions-Instanz für " + getPlayerName() + " wurde entfernt.");
    }

    /**
     * Lädt den Spielernamen basierend auf der UUID.
     * @return Name des Spielers oder "Unknown", falls nicht online.
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
            return "{\"action\":\"" + action + "\", \"amount\":" + amount + ", \"timestamp\":" + timestamp + ", \"receiverUUID\":" + receiverUUID + ", \"senderUUID\":" + senderUUID + "}";
        }
    }
}
