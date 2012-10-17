package me.Destro168.Commands;

import me.Destro168.Configs.SpellConfig;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpellCE implements CommandExecutor
{
	SpellConfig util = new SpellConfig();
	RpgPlayer rpgPlayer;
	RpgMessageLib msgLib;
	
	public SpellCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable declarations.
		Player player = (Player) sender;
		msgLib = new RpgMessageLib(player);
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		//Only let active players use this command.
		if (rpgPlayer.getPlayerConfigFile().getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		//Without an argument, return.
		if (args[0].equals(""))
			return msgLib.helpSpell();
		
		//Set the spell.
		if (args[0].equalsIgnoreCase("bind") || args[0].equalsIgnoreCase("set"))
		{
			if (args[1].equals(""))
				return msgLib.errorInvalidCommand();
			
			//Variable declaration.
			int intArg1 = getSpellNumber(args[1]);
			
			if (intArg1 < 0)
				return true;
			
			//Remove the item from other spell binds if true.
			for (int i = 0; i < rpgPlayer.getPlayerConfigFile().getRpgClass().getSpellBook().size(); i++)
			{
				if (rpgPlayer.getPlayerConfigFile().getSpellBind(i) == player.getItemInHand().getTypeId())
					rpgPlayer.getPlayerConfigFile().setSpellBind(i, 999);
			}
			
			if (rpgPlayer.getPlayerConfigFile().getSpellLevel(intArg1) < 1)
			{
				msgLib.standardError("You must level the skill up before you can bind it.");
				return true;
			}
			
			//Set the spell bind.
			rpgPlayer.getPlayerConfigFile().setSpellBind(intArg1, player.getItemInHand().getTypeId());
			
			//Send a success message to the player.
			msgLib.standardMessage("Successfully bound " + rpgPlayer.getPlayerConfigFile().getRpgClass().getSpell(intArg1).getName() + " to item: " + player.getItemInHand().getType());
		}
		
		//List all spells.
		else if (args[0].equalsIgnoreCase("list"))
		{
			msgLib.standardHeader("Spells List");
			
			msgLib.standardMessage("Current Spell Points",String.valueOf(rpgPlayer.getPlayerConfigFile().getSpellPoints()));
			
			String[] msg = new String[8];
			
			for (int i = 0; i < rpgPlayer.getPlayerConfigFile().getRpgClass().getSpellBook().size(); i++)
			{
				msg[0] = "[N]: ";
				msg[1] = rpgPlayer.getPlayerConfigFile().getRpgClass().getSpell(i).getName();
				
				msg[2] = " [L]: ";
				msg[3] = String.valueOf(rpgPlayer.getPlayerConfigFile().getSpellLevel(i));
				
				if (rpgPlayer.getPlayerConfigFile().getSpellLevel(i) > 0)
				{
					msg[4] = " [MC]: ";
					msg[5] = String.valueOf(rpgPlayer.getPlayerConfigFile().getRpgClass().getSpell(i).getManaCost().get(i));
				}
				else
				{
					msg[4] = " [MC]: ";
					msg[5] = "0";
				}
				
				msg[6] = " [D]: ";
				msg[7] = rpgPlayer.getPlayerConfigFile().getRpgClass().getSpell(i).getDescription();
				
				msgLib.standardMessage(msg);
			}
		}
		
		else if (args[0].equalsIgnoreCase("upgrade"))
		{
			//If the player doesn't have enough spell points tell them.
			if (rpgPlayer.getPlayerConfigFile().getSpellPoints() < 1 && !perms.isAdmin())
			{
				msgLib.standardError("You don't have enough spell points");
				return true;
			}
			
			//Get the spell number by using the function getSpellNumber().
			int intArg1 = getSpellNumber(args[1]);
			
			//Check to make sure that the spell specialized is proper.
			if (intArg1 < 0 || intArg1 > SpellConfig.SPELL_TIERS)
				return msgLib.errorInvalidCommand();
			
			if (rpgPlayer.getPlayerConfigFile().getSpellLevel(intArg1) >= 5)
			{
				msgLib.standardError("This skill is already maxed");
				return true;
			}
			
			//Increaese the spell level.
			rpgPlayer.getPlayerConfigFile().setSpellLevel(intArg1, rpgPlayer.getPlayerConfigFile().getSpellLevel(intArg1) + 1);
			
			//Decrease player spell points.
			rpgPlayer.getPlayerConfigFile().setSpellPoints(rpgPlayer.getPlayerConfigFile().getSpellPoints() - 1);
			
			//Return success.
			return msgLib.successCommand();
		}
		
		else if (args[0].equalsIgnoreCase("reset"))
		{
			for (int i = 0; i < rpgPlayer.getPlayerConfigFile().getRpgClass().getSpellBook().size(); i++)
			{
				if (rpgPlayer.getPlayerConfigFile().getSpellBind(i) == player.getItemInHand().getTypeId())
					rpgPlayer.getPlayerConfigFile().setSpellBind(i, 999);
			}
			
			return msgLib.successCommand();
		}
		
		else if (args[0].equalsIgnoreCase("autocast"))
		{
			if (args[1].equals("on"))
				rpgPlayer.getPlayerConfigFile().setAutoCast(true);
			else if (args[1].equals("off"))
				rpgPlayer.getPlayerConfigFile().setAutoCast(false);
			else
			{
				if (rpgPlayer.getPlayerConfigFile().getAutoCast() == false)
					rpgPlayer.getPlayerConfigFile().setAutoCast(true);
				else
					rpgPlayer.getPlayerConfigFile().setAutoCast(false);
			}
			
			msgLib.successCommand();
		}
		
		return true;
    }
	
	private int getSpellNumber(String spellArgument)
	{
		int intArg1 = -1;
		int spellCount = rpgPlayer.getPlayerConfigFile().getRpgClass().getSpellBook().size();
		
		try
		{
			intArg1 = Integer.valueOf(spellArgument);
			intArg1--; //Convert input down by 1 due to user input.
		}
		catch (NumberFormatException e)
		{
			//For all of the player spells.
			for (int i = 0; i < spellCount; i++)
			{
				//If the spell argument is equal to the spell name, then we store that index.
				if (spellArgument.equalsIgnoreCase(rpgPlayer.getPlayerConfigFile().getRpgClass().getSpell(i).getName()))
				{
					intArg1 = i;
					break;
				}
			}
		}
		
		//Check to make sure the spell is in the right range.
		if (intArg1 < 0 || intArg1 > spellCount)
		{
			msgLib.standardError("You have entered an invalid spell.");
			return -1;
		}
		
		return intArg1;
	}
}










