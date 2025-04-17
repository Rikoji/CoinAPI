package de.hilsmann.coinAPI.GUI;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import de.hilsmann.coinAPI.API.CoinAPI;
import de.hilsmann.coinAPI.CoinMain;
import de.hilsmann.coinAPI.util.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TopInventory {

    // Konstanten für die Item-Positionen
    private static final int FIRST_PLACE_SLOT = 9;
    private static final int SECOND_PLACE_SLOT = 18;
    private static final int THIRD_PLACE_SLOT = 27;

    // Erstellt ein Item mit einem bestimmten Material und einer bestimmten Position
    private static ItemStack createItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    // Fügt die Platzierungen (1., 2., 3.) zum Inventar hinzu
    private static void addPlaceItems(Inventory inv) {
        inv.setItem(FIRST_PLACE_SLOT, createItem(Material.GOLD_BLOCK, "§6§lErster Platz", Arrays.asList(" ")));
        inv.setItem(SECOND_PLACE_SLOT, createItem(Material.IRON_BLOCK, "§7§lZweiter Platz", Arrays.asList(" ")));
        inv.setItem(THIRD_PLACE_SLOT, createItem(Material.COPPER_BLOCK, "§c§lDritter Platz", Arrays.asList(" ")));
    }

    // Fügt einen Spieler in das Inventar ein (mit Name, Platzierung und Coins)
    private static void addPlayerToInventory(Inventory inv, int slot, UUID uuid) {
        ItemStack playerSkull = SkullCreator.itemFromUuid(uuid);
        //TODO ITEM FROM MAP
        ItemMeta meta = playerSkull.getItemMeta();
        if (meta != null) {
            meta.setLore(Arrays.asList("§7Kontostand: §e" + CoinAPI.getCoins(uuid.toString())));
            meta.setDisplayName("§7§l" + CoinAPI.getNameFrom(uuid.toString()) + " - §6§o" + CoinAPI.getCoins(uuid.toString()) + " Coins");
            playerSkull.setItemMeta(meta);
        }
        inv.setItem(slot, playerSkull);
    }

    public static Inventory getInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, "§9§lCoin Top");

        // Creating a frame
        ItemStack filler = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", Arrays.asList());
        for (int i : GuiFunctions.invFrame) {
            inv.setItem(i, filler);
        }

        // Adding podium (1., 2., 3.)
        addPlaceItems(inv);

        // Top Plätze definieren
        int[] podiumSlots = {10, 19, 28};
        List<String> topPlayerList = CoinMain.topPlayerList;
        int index = 0;

        // 1. 2. und 3. Platz setzen
        for (int i = 0; i < podiumSlots.length && index < topPlayerList.size(); ) {
            UUID uuid = UUID.fromString(topPlayerList.get(index));
            index++; // Index immer erhöhen, um Endlosschleifen zu verhindern

            if (isExcludedUUID(uuid)) {
                continue;
            }

            addPlayerToInventory(inv, podiumSlots[i], uuid);
            i++; // Nur bei erfolgreichem Setzen den Slot-Index erhöhen
        }

        // Restliche Spieler auf freie Slots verteilen
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null) {
                continue;
            }

            if (index >= topPlayerList.size()) {
                break;
            }

            UUID uuid = UUID.fromString(topPlayerList.get(index));
            index++; // Index immer erhöhen

            if (isExcludedUUID(uuid)) {
                continue;
            }

            addPlayerToInventory(inv, i, uuid);
        }

        // Fill empty slots with RED_STAINED_GLASS_PANE
        ItemStack fillerRed = createItem(Material.RED_STAINED_GLASS_PANE, " ", Arrays.asList());
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, fillerRed);
            }
        }

        return inv;
    }

    /**
     * Überprüft, ob die UUID ausgeschlossen ist.
     */
    public static boolean isExcludedUUID(UUID uuid) {
        return uuid.toString().equalsIgnoreCase("2966eed3-ce23-40f6-a784-42d6c6c165ee") ||
                uuid.toString().equalsIgnoreCase("e0349cab-b6f2-4465-9084-4df6631ae4d0") ||
                uuid.toString().equalsIgnoreCase("c29a20a7-8e73-4393-9e4a-c41aae04c0e5");
    }

}
