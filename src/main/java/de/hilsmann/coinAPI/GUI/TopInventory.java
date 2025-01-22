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
    public static Inventory getInv(Player p) {

        ItemStack is;
        ItemMeta ism;

        Inventory inv = Bukkit.createInventory(null, 54, "§9§lCoin Top");
        ItemStack f = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fm = f.getItemMeta();
        fm.setDisplayName(" ");
        f.setItemMeta(fm);

        ItemStack fr = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta frm = fr.getItemMeta();
        frm.setDisplayName(" ");
        fr.setItemMeta(frm);

        int i;
        for (i = 0; i < GuiFunctions.invFrame.length; i++) {
            inv.setItem(GuiFunctions.invFrame[i], f);
        }

        is = new ItemStack(Material.GOLD_BLOCK);
        ism = is.getItemMeta();
        ism.setDisplayName("§6§lErster Platz");
        ism.setLore(Arrays.asList(" "));
        is.setItemMeta(ism);
        inv.setItem(9, is);

        is = new ItemStack(Material.IRON_BLOCK);
        ism = is.getItemMeta();
        ism.setDisplayName("§7§lZweiter Platz");
        ism.setLore(Arrays.asList(" "));
        is.setItemMeta(ism);
        inv.setItem(18, is);

        is = new ItemStack(Material.COPPER_BLOCK);
        ism = is.getItemMeta();
        ism.setDisplayName("§c§lDritter Platz");
        ism.setLore(Arrays.asList(" "));
        is.setItemMeta(ism);
        inv.setItem(27, is);

        int kopf = 1;
        List<String> kopfList = CoinMain.topPlayerList;
        for(int ip = 0; ip < inv.getSize(); ip++) {
            if(inv.getItem(ip) != null) {
                continue;
            }else {
                UUID uuid = UUID.fromString(kopfList.get(kopf));
                ItemStack kopfIs = SkullCreator.itemFromUUID(uuid);
                //ItemStack kopfIs = SkullCreator.itemFromName(CoinAPI.getNameFrom(uuid));
                ism = kopfIs.getItemMeta();
                ism.setLore(Arrays.asList("§7Kontostand: §e" + CoinAPI.getCoins(String.valueOf(uuid))));
                if (kopf == 1) {
                    ism.setDisplayName("§6§l♯1 " + CoinAPI.getNameFrom(String.valueOf(uuid)));
                    kopfIs.setItemMeta(ism);
                    inv.setItem(10, kopfIs);
                    kopf++;
                    ip--;
                    continue;
                }else if (kopf == 2) {
                    ism.setDisplayName("§7§l♯2 " + CoinAPI.getNameFrom(String.valueOf(uuid)));
                    kopfIs.setItemMeta(ism);
                    inv.setItem(19, kopfIs);
                    kopf++;
                    ip--;
                    continue;
                }else if (kopf == 3) {
                    ism.setDisplayName("§c§l♯3 " + CoinAPI.getNameFrom(String.valueOf(uuid)));
                    kopfIs.setItemMeta(ism);
                    inv.setItem(28, kopfIs);
                    kopf++;
                    ip--;
                    continue;
                }
                ism.setDisplayName("§7§l♯" + kopf + " " + CoinAPI.getNameFrom(String.valueOf(uuid)));
                kopfIs.setItemMeta(ism);
                inv.setItem(ip, kopfIs);
                if (kopfList.size() - 1 == kopf) {
                    break;
                } else {
                    kopf++;
                }
            }
        }

        for(int i2 = 0; i2 < inv.getSize(); i2++) {
            if(inv.getItem(i2) == null) {
                inv.setItem(i2, fr);
            }
        }

        return inv;
    }
}
