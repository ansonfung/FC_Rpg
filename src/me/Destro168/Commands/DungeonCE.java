package me.Destro168.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;
import me.Destro168.events.DungeonEvent;

public class DungeonCE implements CommandExecutor
{
	RpgMessageLib msgLib;
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
		Player player = (Player) sender;
		msgLib = new RpgMessageLib(player);
		ArgParser fap = new ArgParser(args);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		DungeonEvent dungeon;
		int dungeonNumber = 0;
		
		//Check permissions.
		if (perms.isAdmin() == false)
			return true;
		
		//Convert input into a dungeon number.
		try
		{
			dungeonNumber = Integer.valueOf(fap.getArg(1)) - 1;
		}
		catch (NumberFormatException e)
		{
			return msgLib.helpDungeon();
		}
		
		//Store dungeon for convenient use.
		dungeon = FC_Rpg.dungeon[FC_Rpg.trueDungeonNumbers.get(dungeonNumber)];
		
		//If the new sub command is used, ...
		if (fap.getArg(0).equalsIgnoreCase("stop") || fap.getArg(0).equalsIgnoreCase("end"))
		{
			dungeon.end(false);
			return msgLib.successCommand();
		}
		
		else if (fap.getArg(0).equalsIgnoreCase("init") || fap.getArg(0).equalsIgnoreCase("start") || fap.getArg(0).equalsIgnoreCase("begin"))
		{
			//Start the dungeon
			dungeon.initialize(dungeonNumber);
			
			return msgLib.successCommand();
		}
		
		else if (fap.getArg(0).equalsIgnoreCase("check"))
		{
			//Start the dungeon
			dungeon.checkMobDeath(null);
			return msgLib.successCommand();
		}
		
		else if (fap.getArg(0).equalsIgnoreCase("kick"))
		{
			//Start the dungeon
			if (fap.getArg(1).equals(""))
				return msgLib.errorInvalidCommand();
			
			Player p2 = Bukkit.getServer().getPlayer(fap.getArg(1));
			
			if (p2 == null)
				return msgLib.errorPlayerNotOnline();
			
			dungeon.removeDungeoneer(player, p2, true);
			
			return true;
		}
		
		//Dungeon help.
		return msgLib.helpDungeon();
    }
}
















