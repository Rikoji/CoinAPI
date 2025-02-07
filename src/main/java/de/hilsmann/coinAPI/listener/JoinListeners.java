package de.hilsmann.coinAPI.listener;

import de.hilsmann.coinAPI.API.CoinAPI;
import de.hilsmann.coinAPI.Konto.KontoFunctions;
import de.hilsmann.coinAPI.Konto.KontoManager;
import de.hilsmann.coinAPI.Konto.KontoType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String playerUUID = p.getUniqueId().toString();
		int coins = CoinAPI.getCoins(playerUUID);

		if (!p.hasPlayedBefore()) {
			if (coins == -999999999) {
				CoinAPI.createCoin(playerUUID, p.getName(), 100, 5, 0);
				KontoFunctions konto = KontoManager.getKonto(UUID.fromString(playerUUID));
				konto.addTransactionLog("Startgeld",100, System.currentTimeMillis(), playerUUID, KontoType.SERVER.getDisplayName());
			} else {
				CoinAPI.createCoin(playerUUID, p.getName(), coins, CoinAPI.getGems(playerUUID), CoinAPI.getCrystals(playerUUID));
			}
		} else {
			CoinAPI.createCoin(playerUUID, p.getName(), coins, CoinAPI.getGems(playerUUID), CoinAPI.getCrystals(playerUUID));
		}
	}
}
