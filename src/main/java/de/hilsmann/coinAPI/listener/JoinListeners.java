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
		if (!p.hasPlayedBefore()) {
			if(CoinAPI.getCoins(e.getPlayer().getUniqueId().toString()) <= -999999999) {
				CoinAPI.createCoin(p.getUniqueId().toString(), p.getName(), 100, 5, 0);
			} else {
				CoinAPI.createCoin(p.getUniqueId().toString(), p.getName(), CoinAPI.getCoins(p.getUniqueId().toString()), CoinAPI.getGems(p.getUniqueId().toString()), CoinAPI.getCrystals(p.getUniqueId().toString()));
			}
		}else {
			CoinAPI.createCoin(p.getUniqueId().toString(), p.getName(), CoinAPI.getCoins(p.getUniqueId().toString()), CoinAPI.getGems(p.getUniqueId().toString()), CoinAPI.getCrystals(p.getUniqueId().toString()));
		}
	}
}
