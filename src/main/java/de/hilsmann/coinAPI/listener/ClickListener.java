package de.hilsmann.coinAPI.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickListener implements Listener {

	@EventHandler
	public void onInventoryListener(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getCurrentItem() != null) {
			if (e.getView().getTitle().startsWith("§9§lCoin Top")) {
				Player p = (Player) e.getWhoClicked();
				e.setCancelled(true);
			}
		}
	}
}
