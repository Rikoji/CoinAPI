package de.hilsmann.coinAPI.commands;

import de.hilsmann.coinAPI.API.CoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_gem implements CommandExecutor {

	private static void sendMessageToPlayer(Player p, String message) {
		p.sendMessage(message);
	}

	private static void sendMessageToTargetPlayer(Player target, String message) {
		if (target != null) {
			target.sendMessage(message);
		}
	}

	private static void handlePlayerNotOnline(CommandSender sender, String playerName) {
		sender.sendMessage("§c§oDer Spieler " + playerName + " ist nicht online!");
	}

	private void showPlayerGems(CommandSender sender, String playerName) {
		Player target = Bukkit.getPlayer(playerName);
		if (target != null) {
			sender.sendMessage("§7§oDer Spieler §6§o" + target.getName() + " §7§obesitzt§b§o "
					+ CoinAPI.getGems(target.getUniqueId().toString()) + " §a§oGems");
		} else {
			handlePlayerNotOnline(sender, playerName);
		}
	}

	private void addGemsToPlayer(CommandSender sender, String playerName, String gemAmount) {
		if (!(sender instanceof Player) || sender.hasPermission("coin.admin")) {
			Player target = Bukkit.getPlayer(playerName);
			if (target == null) {
				handlePlayerNotOnline(sender, playerName);
				return;
			}

			try {
				int gems = Integer.parseInt(gemAmount);
				CoinAPI.addGems(target.getUniqueId().toString(), gems);
				sender.sendMessage("§7§oDem Spieler §6§o" + target.getName() + " §7§owurden §b§o" + gems + " §a§oGems §7§ogegeben!");
				sendMessageToTargetPlayer(target, "§a§oDir wurden §b§o" + gems + " §a§oGems gegeben!");
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
						+ CoinAPI.getGems(p.getUniqueId().toString()) + " §a§oGems!");
			} else {
				sender.sendMessage("§c§oDieser Befehl kann nur von einem Spieler ohne Argumente genutzt werden.");
			}
			return true;
		}

		if (args.length >= 2) {
			switch (args[0].toLowerCase()) {
				case "show":
					showPlayerGems(sender, args[1]);
					break;

				case "add":
					if (args.length == 3) {
						addGemsToPlayer(sender, args[1], args[2]);
					} else {
						sender.sendMessage("§c§oVerwendung: /gem add <Spieler> <Menge>");
					}
					break;

				default:
					sender.sendMessage("§c§oUngültiger Befehl. Nutze '/gem show <Spieler>' oder '/gem add <Spieler> <Menge>'");
					break;
			}
			return true;
		}

		sender.sendMessage("§c§oUngültige Verwendung des Befehls. Nutze '/gem', '/gem show <Spieler>' oder '/gem add <Spieler> <Menge>'");
		return true;
	}
}