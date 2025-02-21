package de.hilsmann.coinAPI;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

import de.hilsmann.coinAPI.API.CoinAPI;
import de.hilsmann.coinAPI.MySQL.MySQL;
import de.hilsmann.coinAPI.commands.CMD_coin;
import de.hilsmann.coinAPI.commands.CMD_crystal;
import de.hilsmann.coinAPI.commands.CMD_gem;
import de.hilsmann.coinAPI.listener.ClickListener;
import de.hilsmann.coinAPI.listener.JoinListeners;
import de.hilsmann.coinAPI.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class CoinMain extends JavaPlugin {

	public static CoinMain instance;
	public static ArrayList<String> toplist = new ArrayList<>();
	public static ArrayList<String> topPlayerList;
	public static String version = "1.0_02";

	// Statischer Logger
	public static final Logger logger = Logger.getLogger("CoinAPI");

	@Override
	public void onEnable() {
		instance = this;

		logger.info(CC.YELLOW + "CoinAPI v" + version + CC.GREEN + " wird aktiviert...");
		logger.info(CC.GREEN_BOLD + "   _____      _                  _____ _____ ");
		logger.info(CC.GREEN_BOLD + "  / ____|    (_)           /\\   |  __ \\_   _|");
		logger.info(CC.GREEN_BOLD + " | |     ___  _ _ __      /  \\  | |__) || |  ");
		logger.info(CC.GREEN_BOLD + " | |    / _ \\| | '_ \\    / /\\ \\ |  ___/ | |  ");
		logger.info(CC.GREEN_BOLD + " | |___| (_) | | | | |  / ____ \\| |    _| |_ ");
		logger.info(CC.GREEN_BOLD + "  \\_____\\___/|_|_| |_| /_/    \\_\\_|   |_____|");
		logger.info(CC.GREEN_BOLD + "                                             ");
		logger.info(CC.YELLOW + "CoinAPI v" + version + CC.GREEN + " erfolgreich aktiviert!");

		Bukkit.getPluginManager().registerEvents(new JoinListeners(), this);
		Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
		saveDefaultConfig();

		try {
			MySQL.connect();
		} catch (Exception e) {
			logger.severe("Error creating a connection to the database: " + e.getMessage());
		}

		MySQL.createTable();
		loadCommands();
		createTop();

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, CoinMain::createTop, 4350, 4350);
	}

	@Override
	public void onDisable() {
		logger.info(CC.YELLOW + "CoinAPI v" + version + CC.RED + " wird deaktiviert...");
		logger.info(CC.RED_BOLD + "   _____      _                  _____ _____ ");
		logger.info(CC.RED_BOLD + "  / ____|    (_)           /\\   |  __ \\_   _|");
		logger.info(CC.RED_BOLD + " | |     ___  _ _ __      /  \\  | |__) || |  ");
		logger.info(CC.RED_BOLD + " | |    / _ \\| | '_ \\    / /\\ \\ |  ___/ | |  ");
		logger.info(CC.RED_BOLD + " | |___| (_) | | | | |  / ____ \\| |    _| |_ ");
		logger.info(CC.RED_BOLD + "  \\_____\\___/|_|_| |_| /_/    \\_\\_|   |_____|");
		logger.info(CC.RED_BOLD + "                                             ");
		logger.info(CC.YELLOW + "CoinAPI v" + version + CC.RED + " erfolgreich deaktiviert!");
		MySQL.disconnect();
		instance = null;
	}

	public void loadCommands() {
		Objects.requireNonNull(getCommand("coin")).setExecutor(new CMD_coin());
		Objects.requireNonNull(getCommand("gem")).setExecutor(new CMD_gem());
		Objects.requireNonNull(getCommand("crystal")).setExecutor(new CMD_crystal());
	}

	public static void createTop() {
		topPlayerList = (ArrayList<String>) CoinAPI.getTopPlayers();

		toplist.clear();

		int limit = Math.min(topPlayerList.size(), 10); // Setzt die Grenze auf 10 oder die tatsächliche Größe der Liste.

		for (int i = 0; i < limit; i++) {
			String formatted = String.format("§%s#%d §7- §a%s§7 - §b%s",
					(i == 0 ? "4" : (i == 1 ? "6" : "e")),  // Farbcodierung je nach Rang
					i + 1,  // Rangnummer
					CoinAPI.getNameFrom(topPlayerList.get(i)),
					CoinAPI.getCoins(topPlayerList.get(i)));

			toplist.add(formatted);
		}
	}

	public static CoinMain getInstance(){
		return instance;
	}
}
