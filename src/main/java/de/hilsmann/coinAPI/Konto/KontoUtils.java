package de.hilsmann.coinAPI.Konto;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KontoUtils {

    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    public static void sendTransactionLogMessages(Player p, int count, String filter) {
        UUID uuid = p.getUniqueId();
        List<String> transactionLogs = getTransactionList(uuid, count, filter); // Alle Transaktionen abrufen

        // Falls die gewünschte Anzahl größer ist als die Liste, alle senden
        if (count > transactionLogs.size()) {
            count = transactionLogs.size();
        }

        // Falls die Liste leer ist, den Spieler informieren
        if (transactionLogs.isEmpty()) {
            p.sendMessage("Du hast keine Transaktionen.");
            return;
        }

        // Transaktionen an den Spieler senden
        p.sendMessage("Deine letzten " + count + " Transaktionen:");
        for (String log : transactionLogs) {
            p.sendMessage(log);
        }
    }

    public static List<String> getTransactionList(UUID uuid, int count, String filter) {
        List<String> transactionList = KontoManager.getKonto(uuid).getTransactionLogList();
        List<String> filteredList = new ArrayList<>();

        for (String transaction : transactionList) {
            if (filter == null || transaction.contains(filter)) {
                filteredList.add(replaceUUIDsWithNames(transaction));
            }
        }

        // Rückgabe der letzten "count" Einträge
        int fromIndex = Math.max(filteredList.size() - count, 0);
        return filteredList.subList(fromIndex, filteredList.size());
    }

    /**
     * Ersetzt UUIDs in einem String durch den Spielernamen, falls vorhanden.
     * @param input Der Eingabestring, der UUIDs enthalten könnte.
     * @return Der String mit ersetzten Spielernamen.
     */
    private static String replaceUUIDsWithNames(String input) {
        System.out.println(input);
        Matcher matcher = UUID_PATTERN.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String uuidStr = matcher.group();
            UUID uuid = UUID.fromString(uuidStr);
            String playerName = getPlayerName(uuid);
            matcher.appendReplacement(result, playerName);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static String getPlayerName(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return (player.getName() != null) ? player.getName() : "Anonym";
    }
}
