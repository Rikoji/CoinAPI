package de.hilsmann.coinAPI.commands;

import de.hilsmann.coinAPI.API.CoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_crystal implements CommandExecutor {

	private static void sendMessageToPlayer(Player p, String message) {
		p.sendMessage(message);
	}

	private static void handlePlayerNotOnline(CommandSender sender, String playerName) {
		sender.sendMessage("§c§oDer Spieler " + playerName + " ist nicht online!");
	}

	private void showPlayerCrystals(CommandSender sender, String playerName) {
		Player target = Bukkit.getPlayer(playerName);
		if (target != null) {
			sender.sendMessage("§7§oDer Spieler §6§o" + target.getName() + " §7§obesitzt§b§o "
					+ CoinAPI.getCrystals(target.getUniqueId().toString()) + " §3§oKristalle");
		} else {
			handlePlayerNotOnline(sender, playerName);
		}
	}

	private void addCrystalsToPlayer(CommandSender sender, String playerName, String crystalAmount) {
		if (!(sender instanceof Player) || sender.hasPermission("coin.admin")) {
			Player target = Bukkit.getPlayer(playerName);
			if (target == null) {
				handlePlayerNotOnline(sender, playerName);
				return;
			}

			try {
				int crystals = Integer.parseInt(crystalAmount);
				CoinAPI.addCrystals(target.getUniqueId().toString(), crystals);
				sender.sendMessage("§7§oDem Spieler §6§o" + target.getName() + " §7§owurden §b§o" + crystals + " §3§oKristalle §7§ogegeben!");
				sendMessageToPlayer(target, "§a§oDir wurden §b§o" + crystals + " §3§oKristalle §7§ogegeben!");
			} catch (NumberFormatException e) {
				sender.sendMessage("§c§oDie Zahl muss aus Nummern bestehen. §6§o(0123456789)");
			}
		} else {
			sender.sendMessage("§c§oDen Befehl dürfen nur Admins nutzen!");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				sendMessageToPlayer(p, "§6§o" + p.getDisplayName() + " §7§odu hast §b§o"
						+ CoinAPI.getCrystals(p.getUniqueId().toString()) + " §3§oKristalle!");
			} else {
				sender.sendMessage("§c§oBitte nutze den Befehl korrekt: '/kristall add/show'");
			}
			return true;
		}

		if (args.length >= 2) {
			switch (args[0].toLowerCase()) {
				case "show":
					showPlayerCrystals(sender, args[1]);
					break;

				case "add":
					if (args.length == 3) {
						addCrystalsToPlayer(sender, args[1], args[2]);
					} else {
						sender.sendMessage("§c§oVerwendung: /kristall add <Spieler> <Menge>");
					}
					break;

				default:
					sender.sendMessage("§c§oUngültiger Befehl. Nutze '/kristall show <Spieler>' oder '/kristall add <Spieler> <Menge>'");
					break;
			}
			return true;
		}

		sender.sendMessage("§c§oUngültige Verwendung des Befehls. Nutze '/kristall', '/kristall show <Spieler>' oder '/kristall add <Spieler> <Menge>'");
		return true;
	}
}