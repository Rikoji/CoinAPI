package de.hilsmann.coinAPI.API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class CacheBackupManager {

    private static final File backupFile = new File("plugins/CoinAPI/cache_backup.json");
    private static final Gson gson = new Gson();

    public static void saveBackup(Map<String, Integer> coins, Map<String, Integer> gems, Map<String, Integer> crystals, Set<String> pendingSync) {
        Map<String, Object> backupData = new HashMap<>();
        backupData.put("coins", coins);
        backupData.put("gems", gems);
        backupData.put("crystals", crystals);
        backupData.put("pendingSync", new ArrayList<>(pendingSync)); // Speichern als Liste

        try (FileWriter writer = new FileWriter(backupFile)) {
            gson.toJson(backupData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> loadBackup() {
        if (!backupFile.exists()) {
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(backupFile)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static Map<String, Integer> extractMap(Object object) {
        Type type = new TypeToken<Map<String, Integer>>() {}.getType();
        return gson.fromJson(gson.toJson(object), type);
    }

    public static Set<String> extractSet(Object object) {
        Type type = new TypeToken<List<String>>() {}.getType(); // Lade als Liste
        List<String> list = gson.fromJson(gson.toJson(object), type);
        return new HashSet<>(list); // Konvertiere zu Set
    }

    public static void deleteBackup() {
        if (backupFile.exists()) {
            backupFile.delete();
        }
    }
}
