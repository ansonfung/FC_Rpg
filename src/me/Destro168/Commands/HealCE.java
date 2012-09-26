package me.Destro168.Commands;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCE implements CommandExecutor 
{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		
		if (args[0].equals(""))
			args[0] = player.getName();
		
		if (Bukkit.getServer().getPlayer(args[0]) != null)
		{
			FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[0])).healFull();
			
			//Restore health and food
			player.setHealth(20);
			player.setFoodLevel(20);
			
			msgLib.standardMessage("Successfully healed " + args[0]);
		}
		else
		{
			msgLib.errorPlayerNotOnline();
			return true;
		}
		
		return true;
    }
}
