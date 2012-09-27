package me.Destro168.Commands;

import me.Destro168.Configs.ConfigOverlord;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCE implements CommandExecutor
{
	RpgMessageLib msgLib;
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
		Player player = (Player) sender;
		msgLib = new RpgMessageLib(player);
		ArgParser fap = new ArgParser(args);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		Player target;
		boolean isHappening = FC_Rpg.pvp.isHappening();
		
		//If the new sub command is used, ...
		if (fap.getArg(0).equals("new"))
		{
			//End existing pvp events.
			if (isHappening == true)
				endPvpEvent();
			
			ConfigOverlord co = new ConfigOverlord();
			
			if (co.getPvpArenaReward() > -1)
				FC_Rpg.pvp.begin();	//Start a new one.
			else
				msgLib.standardMessage("You must set the pvp configuration up as well as put the reward at 0 or more first.");
			
			return true;
		}
		
		else if (isHappening == false)
		{
			if (fap.getArg(0).equals(""))
				return msgLib.helpPvp();
			else
				return msgLib.standardMessage("Command was blocked because no event is currently happening.");
		}
		
		//If the end sub command is used, ...
		else if (fap.getArg(0).equals("end") || fap.getArg(0).equals("stop"))
		{
			//End existing pvp events.
			endPvpEvent();
			return true;
		}
		
		//If the join command is used, ...
		if (fap.getArg(0).equals("join"))
		{
			//Add the player
			if (FC_Rpg.pvp.addPvper(player) == true)
				FC_Rpg.bLib.standardBroadcast(player.getName() + " Has Joined The " + ChatColor.RED + "Arena");
			
			return true;
		}
		
		//If the leave command is used, ...
		else if (fap.getArg(0).equals("leave"))
		{
			if (FC_Rpg.pvp.removePvper(player,player,true) == true)
				return msgLib.successCommand();
			
			return true;
		}
		
		//If the leave command is used, ...
		else if (fap.getArg(0).equals("kick"))
		{
			//Ensure an admin is performing the command.
			if (perms.isAdmin() == false)
				return msgLib.errorNoPermission();
			
			//Store the target.
			target = Bukkit.getServer().getPlayer(fap.getArg(1));
			
			//Only kick if the target is online.
			if (target == null)
			{
				msgLib.standardMessage("Player isn't online");
				return true;
			}
			
			//Attempt to remove.
			FC_Rpg.pvp.removePvper(player,target,true);
			
			return true;
		}
		
		//If the list command is used, ...
		else if (fap.getArg(0).equals("list"))
		{
			msgLib.standardHeader("Red Team:");
			
			for (Player redPlayer : FC_Rpg.pvp.getRedTeam())
				msgLib.standardMessage(redPlayer.getName());
			
			msgLib.standardHeader("Yellow Team:");
			
			for (Player yellowPlayer : FC_Rpg.pvp.getYellowTeam())
				msgLib.standardMessage(yellowPlayer.getName());
			
			return true;
		}
		
		return msgLib.helpPvp();
    }
	
	private void endPvpEvent()
	{
		FC_Rpg.pvp.end(true);
		FC_Rpg.bLib.standardBroadcast("Pvp Events Ended By Admin.");
	}
}
