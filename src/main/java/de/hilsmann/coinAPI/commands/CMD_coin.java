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
  public final boolean isValidZahl(String code) {
    return code.matches("0123456789");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	  if(sender instanceof Player) {
		  Player p = (Player) sender;
    if (args.length == 0) {
      p.sendMessage("§6§o"+ p.getDisplayName() + " §7§odu hast " + "§6§o" + CoinAPI.getCoins(p.getUniqueId().toString()) + " §7§oCoins!");
    }else if(args.length == 2){
    	if (args[0].equalsIgnoreCase("show")) {
    	      String name = args[1];
    	      Player t = Bukkit.getPlayer(name);
    	      if (t != null) {
    	        p.sendMessage("§7§oDer Spieler §6§o" + t.getName() + " §7§obesitzt§6§o " + CoinAPI.getCoins(t.getUniqueId().toString()) + " §7§oCoins" );
    	      } else {
    	        p.sendMessage("§c§oDer Spieler ist nicht Online!");
    	      } 
    	    }
    }else if(args.length == 3){
    	if (args[0].equalsIgnoreCase("add")) {
  	      if (p.hasPermission("coin.admin")) {
  	        String name = args[1];
  	        String coin = args[2];
  	        try{
  	        	Player t = Bukkit.getPlayer(name);
  	        	if (name != null) {
  	  	          if (coin != null)
  	  	            try {
  	  	              int preisint = Integer.parseInt(coin);
  	  	              CoinAPI.addCoins(t.getUniqueId().toString(), preisint);
  	  	              p.sendMessage("§7§oDem Spieler §6§o" + t.getName() + " §6§o" + preisint + " §7§ogegeben!");
  	  	              t.sendMessage("§a§oDir wurden §6§o" + preisint + " §a§oCoins gegeben!");
  	  	            } catch (NumberFormatException exception) {
  	  	              p.sendMessage("§c§oDie Zahl muss aus Nummern bestehen. (0123456789)");
  	  	            }  
  	  	        } else {
  	  	          p.sendMessage("§c§oDer Spieler ist nicht Online!");
  	  	        }
  	        } catch(NullPointerException e) {
	            	p.sendMessage("Nicht Online");
	            }
  	         
  	      } else {
  	        p.sendMessage("§c§oDen Befehl d§rfen nur Admins nutzen!");
  	      } 
  	    }else if (args[0].equalsIgnoreCase("pay")) {
  	            String name = args[1];
  	            String coin = args[2];
  	            try{
  	            Player t = Bukkit.getPlayer(name);
  	  	        if (name != null) {
  	  	          if (coin != null && !coin.startsWith("-")){
  	  	            try {
  	  	              int preisint = Integer.parseInt(coin);
  	  	              if (CoinAPI.getCoins(p.getUniqueId().toString()) >= preisint) {
  	  	                CoinAPI.addCoins(t.getUniqueId().toString(), preisint);
  	  	                CoinAPI.removeCoins(p.getUniqueId().toString(), preisint);
  	  	                p.sendMessage("§7§oDu hast dem Spieler §6§o" + t.getName() + " §7§ogerade §6§o" + preisint + 
  	  	                    " §7§oCoins gezahlt!");
  	  	                t.sendMessage("§6§o" + p.getName() +" §a§ohat dir §6§o" + preisint + " §a§oCoins gezahlt!");
  	  	              } else {
  	  	                p.sendMessage("§c§oDu hast nicht gen§gend Coins");
  	  	              }
  	  	            } catch (NumberFormatException exception) {
  	  	              p.sendMessage("§c§oDie Zahl muss aus Nummern bestehen. §6§o(0123456789)");
  	  	            }  
  	  	        } else {
  	  	          p.sendMessage("§c§oVersuchst du einen negativen Betrag zu zahlen?");
  	  	        }
  	  	        }else{
  	  	      	  p.sendMessage("§c§oDieser Spieler ist nicht online.");
  	  	        }
  	            } catch(NullPointerException e) {
  	            	e.printStackTrace();
  	            	p.sendMessage("Nicht Online");
  	            }

  	      }
    }else if(args.length == 1) {
    	if(args[0].equalsIgnoreCase("top")) {
    		//p.sendTitle("§6§lCoin Top", "§7Zeigt TOP 10 Konten", 5, 40, 5);
			p.openInventory(TopInventory.getInv(p));
    }
    }
	  }else {
		  CommandSender p = sender;
		    if (args.length == 0) {
		        p.sendMessage("§6Bitte nutze die richtige Command-Syntax");
		      }else if(args.length == 2){
		      	if (args[0].equalsIgnoreCase("show")) {
		      	      String name = args[1];
		      	      Player t = Bukkit.getPlayer(name);
		      	      if (t != null) {
		      	        p.sendMessage("§7§oDer Spieler §6§o" + t.getName() + " §7§obesitzt§6§o " + CoinAPI.getCoins(t.getUniqueId().toString()) + " §7§oCoins" );
		      	      } else {
		      	        p.sendMessage("§c§oDer Spieler ist nicht Online!");
		      	      } 
		      	    }
		      }else if(args.length == 3){
		      	if (args[0].equalsIgnoreCase("add")) {
		    	      if (p.hasPermission("coin.admin")) {
		    	        String name = args[1];
		    	        String coin = args[2];
		    	        try{
		    	        	Player t = Bukkit.getPlayer(name);
		    	        	if (name != null) {
		    	  	          if (coin != null)
		    	  	            try {
		    	  	              int preisint = Integer.parseInt(coin);
		    	  	              CoinAPI.addCoins(t.getUniqueId().toString(), preisint);
		    	  	              p.sendMessage("§7§oDem Spieler §6§o" + t.getName() + " §6§o" + preisint + " §7§ogegeben!");
		    	  	              t.sendMessage("§a§oDir wurden §6§o" + preisint + " §a§oCoins gegeben!");
		    	  	            } catch (NumberFormatException exception) {
		    	  	              p.sendMessage("§c§oDie Zahl muss aus Nummern bestehen. (0123456789)");
		    	  	            }  
		    	  	        } else {
		    	  	          p.sendMessage("§c§oDer Spieler ist nicht Online!");
		    	  	        }
		    	        } catch(NullPointerException e) {
		  	            	p.sendMessage("Nicht Online");
		  	            }
		    	         
		    	      } else {
		    	        p.sendMessage("§c§oDen Befehl d§rfen nur Admins nutzen!");
		    	      } 
		    	    }
		      }else if(args.length == 1) {
		      	if(args[0].equalsIgnoreCase("top")) {
		      		p.sendMessage(CoinMain.toplist.get(0));
		  			p.sendMessage(CoinMain.toplist.get(1));
		  			p.sendMessage(CoinMain.toplist.get(2));
		  			p.sendMessage(CoinMain.toplist.get(3));
		  			p.sendMessage(CoinMain.toplist.get(4));
		  			p.sendMessage(CoinMain.toplist.get(5));
		  			p.sendMessage(CoinMain.toplist.get(6));
		  			p.sendMessage(CoinMain.toplist.get(7));
		  			p.sendMessage(CoinMain.toplist.get(8));
		  			p.sendMessage(CoinMain.toplist.get(9));
		      }
		      }
	  }
    return false;
  }
  
}