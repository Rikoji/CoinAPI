package de.hilsmann.coinAPI.commands;

import de.hilsmann.coinAPI.API.CoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_crystal implements CommandExecutor {
	  public final boolean isValidZahl(String code) {
		    return code.matches("0123456789");
		  }
		  
		  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			  if(sender instanceof Player) {
				  final Player p = (Player)sender;
				  
				    if (args.length == 0) {
					      p.sendMessage("§6§o"+ p.getDisplayName() + " §7§odu hast " + "§b§o" + CoinAPI.getCrystals(p.getUniqueId().toString()) + " §3§oKristalle!");
					    } else if (args[0].equalsIgnoreCase("add")) {
					      if (p.hasPermission("coin.admin")) {
					        String name = args[1];
					        String coin = args[2];
					        Player t = Bukkit.getPlayer(name);
					        if (t != null) {
					          if (coin != null)
					            try {
					              int preisint = Integer.parseInt(coin);
					              CoinAPI.addCrystals(t.getUniqueId().toString(), preisint);
					              p.sendMessage("§7§oDem Spieler §a§o" + t.getName() + " §b§o" + preisint + " §3§oKristalle §7§ogegeben!");
					              t.sendMessage("§a§oDir wurden §b§o" + preisint + " §3§oKristalle §7§ogegeben!");
					            } catch (NumberFormatException exception) {
					              p.sendMessage("§c§oDie Zahl muss aus Nummern bestehen. (0123456789)");
					            }  
					        } else {
					          p.sendMessage("§c§oDer Spieler ist nicht Online!");
					        } 
					      } else {
					        p.sendMessage("§c§oDen Befehl d§rfen nur Admins nutzen!");
					      } 
					    } else if (args[0].equalsIgnoreCase("show")) {
					      String name = args[1];
					      Player t = Bukkit.getPlayer(name);
					      if (t != null) {
					        p.sendMessage("§7§oDer Spieler §6§o" + t.getName() + " §7§obesitzt§b§o " + CoinAPI.getCrystals(t.getUniqueId().toString()) + " §3§oKristalle" );
					      } else {
					        p.sendMessage("§c§oDer Spieler ist nicht Online!");
					      } 
					    }
			  }else {
				  final CommandSender p = sender;
				  
				    if (args.length == 0) {
					      p.sendMessage("Bitte nutze als konsole kristall add/remove");
					    } else if (args[0].equalsIgnoreCase("add")) {
					      if (p.hasPermission("coin.admin")) {
					        String name = args[1];
					        String coin = args[2];
					        Player t = Bukkit.getPlayer(name);
					        if (t != null) {
					          if (coin != null)
					            try {
					              int preisint = Integer.parseInt(coin);
					              CoinAPI.addCrystals(t.getUniqueId().toString(), preisint);
					              p.sendMessage("§7§oDem Spieler §a§o" + t.getName() + " §b§o" + preisint + " §3§oKristalle §7§ogegeben!");
					              t.sendMessage("§a§oDir wurden §b§o" + preisint + " §3§oKristalle §7§ogegeben!");
					            } catch (NumberFormatException exception) {
					              p.sendMessage("§c§oDie Zahl muss aus Nummern bestehen. (0123456789)");
					            }  
					        } else {
					          p.sendMessage("§c§oDer Spieler ist nicht Online!");
					        } 
					      } else {
					        p.sendMessage("§c§oDen Befehl d§rfen nur Admins nutzen!");
					      } 
					    } else if (args[0].equalsIgnoreCase("show")) {
					      String name = args[1];
					      Player t = Bukkit.getPlayer(name);
					      if (t != null) {
					        p.sendMessage("§7§oDer Spieler §6§o" + t.getName() + " §7§obesitzt§b§o " + CoinAPI.getCrystals(t.getUniqueId().toString()) + " §3§oKristalle" );
					      } else {
					        p.sendMessage("§c§oDer Spieler ist nicht Online!");
					      } 
					    }
			  }
		    return false;
		  }
		  
}
