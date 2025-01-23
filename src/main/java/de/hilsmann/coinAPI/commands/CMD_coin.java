package de.hilsmann.coinAPI.commands;

import de.hilsmann.coinAPI.API.CoinAPI;
import de.hilsmann.coinAPI.CoinMain;
import de.hilsmann.coinAPI.GUI.TopInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_coin implements CommandExecutor {

	private static void sendMessageToPlayer(Player p, String message) {
		p.sendMessage(message);
	}

	private static void sendMessageToTargetPlayer(Player target, String message) {
		if (target != null) {
			target.sendMessage(message);
		}
	}

	private static void handlePlayerNotOnline(Player p, String playerName) {
		sendMessageToPlayer(p, "§c§oDer Spieler " + playerName + " ist nicht Online!");
	}

	private void showPlayerCoins(Player p, String playerName) {
		Player target = Bukkit.getPlayer(playerName);
		if (target != null) {
			sendMessageToPlayer(p, "§7§oDer Spieler §6§o" + target.getName() + " §7§obesitzt§6§o "
					+ CoinAPI.getCoins(target.getUniqueId().toString()) + " §7§oCoins");
		} else {
			handlePlayerNotOnline(p, playerName);
		}
	}

	private void addCoinsToPlayer(Player p, String playerName, String coinAmount) {
		if (!p.hasPermission("coin.admin")) {
			sendMessageToPlayer(p, "§c§oDen Befehl dürfen nur Admins nutzen!");
			return;
		}

		Player target = Bukkit.getPlayer(playerName);
		if (target == null) {
			handlePlayerNotOnline(p, playerName);
			return;
		}

		try {
			int coins = Integer.parseInt(coinAmount);
			CoinAPI.addCoins(target.getUniqueId().toString(), coins);
			sendMessageToPlayer(p, "§7§oDem Spieler §6§o" + target.getName() + " §7§owurden §6§o" + coins + " §7§ogegeben!");
			sendMessageToTargetPlayer(target, "§a§oDir wurden §6§o" + coins + " §a§oCoins gegeben!");
		} catch (NumberFormatException e) {
			sendMessageToPlayer(p, "§c§oDie Zahl muss aus Nummern bestehen. (0123456789)");
		}
	}

	private void removeCoinsFromPlayer(Player p, String playerName, String coinAmount) {
		if (!p.hasPermission("coin.admin")) {
			sendMessageToPlayer(p, "§c§oDen Befehl dürfen nur Admins nutzen!");
			return;
		}

		Player target = Bukkit.getPlayer(playerName);
		if (target == null) {
			handlePlayerNotOnline(p, playerName);
			return;
		}

		try {
			int coins = Integer.parseInt(coinAmount);
			CoinAPI.addCoins(target.getUniqueId().toString(), -coins);
			sendMessageToPlayer(p, "§7§oDem Spieler §6§o" + target.getName() + " §7§owurden §6§o" + coins + " §7§oabgezogen!");
			sendMessageToTargetPlayer(target, "§c§oDir wurden §6§o" + coins + " §a§oCoins abgezogen!");
		} catch (NumberFormatException e) {
			sendMessageToPlayer(p, "§c§oDie Zahl muss aus Nummern bestehen. (0123456789)");
		}
	}

	private void payCoinsToPlayer(Player p, String playerName, String coinAmount) {
		if (coinAmount == null || coinAmount.isEmpty()) {
			sendMessageToPlayer(p, "§c§oDu musst eine Anzahl von Coins angeben.");
			return;
		}

		Player target = Bukkit.getPlayer(playerName);
		if (target == null) {
			sendMessageToPlayer(p, "§c§oDieser Spieler ist nicht online.");
			return;
		}

		try {
			int coins = Integer.parseInt(coinAmount);
			if (coins <= 0) {
				sendMessageToPlayer(p, "§c§oVersuchst du einen negativen Betrag zu zahlen?");
				return;
			}

			if (CoinAPI.getCoins(p.getUniqueId().toString()) >= coins) {
				CoinAPI.addCoins(target.getUniqueId().toString(), coins);
				CoinAPI.removeCoins(p.getUniqueId().toString(), coins);
				sendMessageToPlayer(p, "§7§oDu hast dem Spieler §6§o" + target.getName() + " §7§ogerade §6§o" + coins + " §7§oCoins gezahlt!");
				sendMessageToTargetPlayer(target, "§6§o" + p.getName() + " §a§ohat dir §6§o" + coins + " §a§oCoins gezahlt!");
			} else {
				sendMessageToPlayer(p, "§c§oDu hast nicht genügend Coins.");
			}
		} catch (NumberFormatException e) {
			sendMessageToPlayer(p, "§c§oDie Zahl muss aus Nummern bestehen. §6§o(0123456789)");
		}
	}

	private void showTopList(Player p) {
		for (int i = 0; i < Math.min(10, CoinMain.toplist.size()); i++) {
			sendMessageToPlayer(p, CoinMain.toplist.get(i));
		}
		p.openInventory(TopInventory.getInv(p));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (args.length == 0) {
				sendMessageToPlayer(p, "§6§o" + p.getDisplayName() + " §7§odu hast §6§o" + CoinAPI.getCoins(p.getUniqueId().toString()) + " §7§oCoins!");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("top")) {
					showTopList(p);
				} else {
					sendMessageToPlayer(p, "§c§oTODO Hilfetext");
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("show")) {
					showPlayerCoins(p, args[1]);
				}else {
					switch (args[0].toLowerCase()) {
						case "pay":
							sendMessageToPlayer(p, "§c§oDu musst eine Menge von Coins angeben. Beispiel: '/coin pay <Spieler> <Menge>'");
							break;

						case "add":
							sendMessageToPlayer(p, "§c§oDu musst eine Menge von Coins angeben. Beispiel: '/coin add <Spieler> <Menge>'");
							break;

						default:
							sendMessageToPlayer(p, "§c§oUngültiger Befehl. Nutze '/coin pay <Spieler> <Menge>' oder '/coin show <Spieler>'");
							break;
					}
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("add")) {
					addCoinsToPlayer(p, args[1], args[2]);
				}else if (args[0].equalsIgnoreCase("remove")){
					removeCoinsFromPlayer(p, args[1], args[2]);
			    }else if (args[0].equalsIgnoreCase("pay")) {
					payCoinsToPlayer(p, args[1], args[2]);
				} else {

					switch (args[0].toLowerCase()) {
						case "add":
							sendMessageToPlayer(p, "§c§oUngültiger Befehl. Nutze '/coin add <Spieler> <Menge>'");
							break;
						case "pay":
							sendMessageToPlayer(p, "§c§oUngültiger Befehl. Nutze '/coin pay <Spieler> <Menge>'");
							break;
						case "remove":
							sendMessageToPlayer(p, "§c§oUngültiger Befehl. Nutze '/coin remove <Spieler> <Menge>'");
							break;
						default:
							sendMessageToPlayer(p, "§c§oTODO Hilfetext");
							break;
					}
				}
			}
		} else {
			// Wenn der Befehl von der Konsole oder einem anderen CommandSender ausgeführt wird
			if (args.length == 0) {
				sender.sendMessage("§6Bitte nutze die richtige Command-Syntax");
			} else if (args.length == 2 && args[0].equalsIgnoreCase("show")) {
				showPlayerCoins((Player) sender, args[1]);
			} else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
				addCoinsToPlayer((Player) sender, args[1], args[2]);
			} else if (args.length == 3 && args[0].equalsIgnoreCase("pay")) {
				payCoinsToPlayer((Player) sender, args[1], args[2]);
			}
		}
		return true;
	}
}
