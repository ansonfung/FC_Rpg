package me.Destro168.Commands;

import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonatorCE implements CommandExecutor
{
	public DonatorCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
	{
		//Variable declarations.
		Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		RpgPlayer rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		
		//Only let active players use this command.
		if (rpgPlayer.getPlayerConfigFile().getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		if (rpgPlayer.getPlayerConfigFile().isDonator())
		{
			if (args[0].equals(""))
			{
				//Display the players donation information.
				msgLib.standardHeader("Donator Information");
				msgLib.standardMessage("Thank you for donating!");
				msgLib.standardMessage("Donation Perks End On: " + ChatColor.YELLOW + FC_Rpg.dfm.format(rpgPlayer.getPlayerConfigFile().getDonatorTime()));
				msgLib.helpDonator();
			}
			else if (args[0].equals("respecialize"))
			{
				rpgPlayer.respecAll();
				msgLib.standardMessage("Successfully refunded stat and spell points. Remember to use them!");
			}
		}
		else
		{
			return msgLib.errorNoPermission();
		}
		
		return true;
	}
}
