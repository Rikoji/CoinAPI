package de.hilsmann.coinAPI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import de.hilsmann.coinAPI.API.CoinAPI;
import de.hilsmann.coinAPI.MySQL.MySQL;
import de.hilsmann.coinAPI.commands.CMD_coin;
import de.hilsmann.coinAPI.commands.CMD_crystal;
import de.hilsmann.coinAPI.commands.CMD_gem;
import de.hilsmann.coinAPI.listener.ClickListener;
import de.hilsmann.coinAPI.listener.JoinListeners;
import de.hilsmann.coinAPI.util.CC;
import de.hilsmann.coinAPI.util.MCSW;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class CoinMain extends JavaPlugin {

	public static CoinMain instance;

	public static ArrayList<String> toplist = new ArrayList<>();
	public static ArrayList<String> topPlayerList;
	public static String version = "1.0_02";

	public void onEnable() {
		instance = this;

		MCSW.println(CC.YELLOW + "CoinAPI v" + version + CC.GREEN + " wird aktiviert...");
		MCSW.println(CC.GREEN_BOLD + "   _____      _                  _____ _____ ");
		MCSW.println(CC.GREEN_BOLD + "  / ____|    (_)           /\\   |  __ \\_   _|");
		MCSW.println(CC.GREEN_BOLD + " | |     ___  _ _ __      /  \\  | |__) || |  ");
		MCSW.println(CC.GREEN_BOLD + " | |    / _ \\| | '_ \\    / /\\ \\ |  ___/ | |  ");
		MCSW.println(CC.GREEN_BOLD + " | |___| (_) | | | | |  / ____ \\| |    _| |_ ");
		MCSW.println(CC.GREEN_BOLD + "  \\_____\\___/|_|_| |_| /_/    \\_\\_|   |_____|");
		MCSW.println(CC.GREEN_BOLD + "                                             ");
		MCSW.println(CC.YELLOW + "CoinAPI v" + version + CC.GREEN + " erfolgreich aktiviert!");
		Bukkit.getPluginManager().registerEvents(new JoinListeners(), this);
		Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
		saveDefaultConfig();
        try {
            MySQL.connect();
        } catch (Exception e) {
			System.out.println("Error creating a connection to the database: " + e.getMessage());
		}
        MySQL.createTable();
		loadCommands();
		createTop();

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, CoinMain::createTop, 4350, 4350);

	}

	public void onDisable() {
		MCSW.println(CC.YELLOW + "CoinAPI v" + version + CC.RED + " wird deaktiviert...");
		MCSW.println(CC.RED_BOLD + "   _____      _                  _____ _____ ");
		MCSW.println(CC.RED_BOLD + "  / ____|    (_)           /\\   |  __ \\_   _|");
		MCSW.println(CC.RED_BOLD + " | |     ___  _ _ __      /  \\  | |__) || |  ");
		MCSW.println(CC.RED_BOLD + " | |    / _ \\| | '_ \\    / /\\ \\ |  ___/ | |  ");
		MCSW.println(CC.RED_BOLD + " | |___| (_) | | | | |  / ____ \\| |    _| |_ ");
		MCSW.println(CC.RED_BOLD + "  \\_____\\___/|_|_| |_| /_/    \\_\\_|   |_____|");
		MCSW.println(CC.RED_BOLD + "                                             ");
		MCSW.println(CC.YELLOW + "CoinAPI v" + version + CC.RED + " erfolgreich deaktiviert!");
		MySQL.disconnect();
		instance = null;
	}

	public void loadCommands() {
		Objects.requireNonNull(getCommand("coin")).setExecutor(new CMD_coin());
		Objects.requireNonNull(getCommand("gem")).setExecutor(new CMD_gem());
		Objects.requireNonNull(getCommand("crystal")).setExecutor(new CMD_crystal());
	}

	public static void createTop() {
		try {
			topPlayerList = (ArrayList<String>) CoinAPI.getTopPlayers();
			String s0 = "§4#Staat §7-§c " + CoinAPI.getNameFrom(topPlayerList.get(0)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(0));
			String s1 = "§6#1 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(1)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(1));
			String s2 = "§7#2 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(2)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(2));
			String s3 = "§c#3 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(3)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(3));
			String s4 = "§e#4 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(4)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(4));
			String s5 = "§e#5 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(5)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(5));
			String s6 = "§e#6 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(6)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(6));
			String s7 = "§e#7 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(7)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(7));
			String s8 = "§e#8 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(8)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(8));
			String s9 = "§e#9 §7- §a" + CoinAPI.getNameFrom(topPlayerList.get(9)) + "§7 - §b" + CoinAPI.getCoins(topPlayerList.get(9));


			toplist.clear();
			toplist.add(s0);
			toplist.add(s1);
			toplist.add(s2);
			toplist.add(s3);
			toplist.add(s4);
			toplist.add(s5);
			toplist.add(s6);
			toplist.add(s7);
			toplist.add(s8);
			toplist.add(s9);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
