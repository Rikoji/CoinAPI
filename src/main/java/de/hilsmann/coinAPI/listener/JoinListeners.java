package de.hilsmann.coinAPI.listener;

import de.hilsmann.coinAPI.API.CoinAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String playerUUID = p.getUniqueId().toString();
		int coins = CoinAPI.getCoins(playerUUID);

		if (!p.hasPlayedBefore()) {
			if (coins == -999999999) {
				CoinAPI.createCoin(playerUUID, p.getName(), 100, 5, 0);
			} else {
				CoinAPI.createCoin(playerUUID, p.getName(), coins, CoinAPI.getGems(playerUUID), CoinAPI.getCrystals(playerUUID));
			}
		} else {
			CoinAPI.createCoin(playerUUID, p.getName(), coins, CoinAPI.getGems(playerUUID), CoinAPI.getCrystals(playerUUID));
		}
	}
}
