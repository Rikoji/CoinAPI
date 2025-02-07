package de.hilsmann.coinAPI.Konto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KontoManager {
    private static final Map<UUID, KontoFunctions> kontoMap = new HashMap<>();

    /**
     * Holt oder erstellt ein Konto für den angegebenen Spieler.
     * @param playerUUID UUID des Spielers.
     * @return Die KontoFunctions-Instanz des Spielers.
     */
    public static KontoFunctions getKonto(UUID playerUUID) {
        return kontoMap.computeIfAbsent(playerUUID, KontoFunctions::new);
    }

    /**
     * Entfernt ein Konto aus der Verwaltung und speichert es vorher.
     * @param playerUUID UUID des Spielers.
     */
    public static void removeKonto(UUID playerUUID) {
        if (kontoMap.containsKey(playerUUID)) {
            KontoFunctions konto = kontoMap.get(playerUUID);
            konto.dispose(); // Speichert Daten und leert das Log
            kontoMap.remove(playerUUID); // Entfernt das Konto aus der Verwaltung
        }
    }

    /**
     * Prüft, ob ein Konto für einen Spieler existiert.
     * @param playerUUID UUID des Spielers.
     * @return True, wenn ein Konto existiert, sonst false.
     */
    public static boolean hasKonto(UUID playerUUID) {
        return kontoMap.containsKey(playerUUID);
    }
}

