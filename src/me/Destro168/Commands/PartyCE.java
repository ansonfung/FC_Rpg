package me.Destro168.Commands;

import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCE implements CommandExecutor
{
	public PartyCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable declarations.
		Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		RpgPlayer rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		boolean success = true;
		
		//Only let active players use this command.
		if (rpgPlayer.getPlayerConfigFile().getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		if (args[0].equals(""))
		{
			return msgLib.helpParty();
		}
		
		if (args[0].equalsIgnoreCase("list"))
		{
			msgLib.standardMessage(FC_Rpg.partyManager.getPartyList());
		}
		else if (args[0].equalsIgnoreCase("create"))
		{
			if (args[1].equals(""))
			{
				args[1] = "No Name! Rename!";
			}
			
			success = FC_Rpg.partyManager.createParty(player.getName(), args[1]);
			
	    	if (success == false)
	    		msgLib.standardError("Failed to create a new party.");
		}
		else if (args[0].equalsIgnoreCase("close"))
		{
			success = FC_Rpg.partyManager.closeParty(player.getName());
			
			if (success == false)
				msgLib.standardError("Failed to close your party.");
			else
				msgLib.standardMessage("Successfully closed your party!");
		}
		else if (args[0].equalsIgnoreCase("open"))
		{
			success = FC_Rpg.partyManager.openParty(player.getName());
			
			if (success == false)
				msgLib.standardError("Failed to open your party.");
			else
				msgLib.standardMessage("Successfully opened your party!");
		}
		else if (args[0].equalsIgnoreCase("join"))
		{
			if (args[1].equals(""))
			{
				msgLib.standardError("You must specify a party to join!");
			}
			else
			{
				//Remove the member from any old parties
				FC_Rpg.partyManager.removeMemberFromAllParties(player.getName());
				
				//Add to the new party
				success = FC_Rpg.partyManager.addMember(player.getName(), args[1]);
				
				if (success == false)
					msgLib.standardError("Failed to join party.");
				else
					msgLib.standardMessage("Successfully joined party");
			}
		}
		else if (args[0].equalsIgnoreCase("leave"))
		{
			//Remove the member from any old parties
			success = FC_Rpg.partyManager.removeMemberFromAllParties(player.getName());
			
			if (success == false)
				msgLib.standardError("Failed to leave any parties.");
			else
				msgLib.standardMessage("Successfully left any and all parties.");
		}
		else if (args[0].equalsIgnoreCase("view"))
		{
			if (args[1].equals(""))
			{
				return msgLib.errorInvalidCommand();
			}
			else
			{
				success = FC_Rpg.partyManager.viewPartyInfo(args[1], player);
				
				if (success == false)
					msgLib.standardError("Failed to view any parties.");
			}
		}
		
		return true;
    }
}
