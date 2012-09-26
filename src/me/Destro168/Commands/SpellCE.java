package me.Destro168.Commands;

import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;
import me.Destro168.Util.SpellUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpellCE implements CommandExecutor
{
	SpellUtil util = new SpellUtil();
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
		if (rpgPlayer.getIsActive() == false)
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
			for (int i = 0; i < SpellUtil.CLASS_SPELL_COUNT; i++)
			{
				if (rpgPlayer.getSpellBind(i) == player.getItemInHand().getTypeId())
					rpgPlayer.setSpellBind(i, 999);
			}
			
			if (rpgPlayer.getSpellLevel(intArg1) < 1)
			{
				msgLib.standardError("You must level the skill up before you can bind it.");
				return true;
			}
			
			//Set the spell bind.
			rpgPlayer.setSpellBind(intArg1, player.getItemInHand().getTypeId());
			
			//Send a success message to the player.
			msgLib.standardMessage("Successfully bound " + util.getSpellName(rpgPlayer.getCombatClass(), intArg1) + " to item: " + player.getItemInHand().getType());
		}
		
		//List all spells.
		else if (args[0].equalsIgnoreCase("list"))
		{
			msgLib.standardHeader("Spells List");
			
			msgLib.standardMessage("Current Spell Points",String.valueOf(rpgPlayer.getSpellPoints()));
			
			String[] msg = new String[6];
			
			for (int i = 0; i < SpellUtil.CLASS_SPELL_COUNT; i++)
			{
				msg[0] = "Spell Name: ";
				msg[1] = util.getSpellName(rpgPlayer.getCombatClass(), i);
				msg[2] = " - Level: ";
				msg[3] = String.valueOf(rpgPlayer.getSpellLevel(i));
				
				if (rpgPlayer.getSpellLevel(i) > 0)
				{
					msg[4] = " - Mana Cost: ";
					msg[5] = String.valueOf(util.getSpellManaCost(rpgPlayer.getCombatClass(), i, rpgPlayer.getSpellLevel(i) - 1));
				}
				else
				{
					msg[4] = " - Mana Cost: ";
					msg[5] = "0";
				}
				
				msgLib.standardMessage(msg);
				msgLib.standardMessage(util.getSpellName(rpgPlayer.getCombatClass(), i),util.getSpellDescription(rpgPlayer.getCombatClass(), i));
			}
		}
		
		else if (args[0].equalsIgnoreCase("upgrade"))
		{
			//If the player doesn't have enough spell points tell them.
			if (rpgPlayer.getSpellPoints() < 1 && !perms.isAdmin())
			{
				msgLib.standardError("You don't have enough spell points");
				return true;
			}
			
			//Get the spell number by using the function getSpellNumber().
			int intArg1 = getSpellNumber(args[1]);
			
			//Check to make sure that the spell specialized is proper.
			if (intArg1 < 0 || intArg1 > SpellUtil.TIER_COUNT)
				return msgLib.errorInvalidCommand();
			
			if (rpgPlayer.getSpellLevel(intArg1) >= 5)
			{
				msgLib.standardError("This skill is already maxed");
				return true;
			}
			
			//Increaese the spell level.
			rpgPlayer.setSpellLevel(intArg1, rpgPlayer.getSpellLevel(intArg1) + 1);
			
			//Decrease player spell points.
			rpgPlayer.setSpellPoints(rpgPlayer.getSpellPoints() - 1);
			
			//Return success.
			return msgLib.successCommand();
		}
		
		else if (args[0].equalsIgnoreCase("reset"))
		{
			for (int i = 0; i < SpellUtil.CLASS_SPELL_COUNT; i++)
			{
				if (rpgPlayer.getSpellBind(i) == player.getItemInHand().getTypeId())
					rpgPlayer.setSpellBind(i, 999);
			}
			
			return msgLib.successCommand();
		}
		
		else if (args[0].equalsIgnoreCase("autocast"))
		{
			if (args[1].equals("on"))
				rpgPlayer.setAutoCast(true);
			else if (args[1].equals("off"))
				rpgPlayer.setAutoCast(false);
			else
			{
				if (rpgPlayer.getAutoCast() == false)
					rpgPlayer.setAutoCast(true);
				else
					rpgPlayer.setAutoCast(false);
			}
			
			msgLib.successCommand();
		}
		
		return true;
    }
	
	private int getSpellNumber(String spellArgument)
	{
		int intArg1 = -1;
		
		try
		{
			intArg1 = Integer.valueOf(spellArgument);
			intArg1--; //Convert input down by 1 due to user input.
		}
		catch (NumberFormatException e)
		{
			//Check by the spell name.
			for (int i = 0; i < SpellUtil.CLASS_SPELL_COUNT; i++)
			{
				if (spellArgument.equalsIgnoreCase(util.getSpellName(rpgPlayer.getCombatClass(), i)))
				{
					intArg1 = i;
					break;
				}
			}
		}
		
		//Check to make sure the spell is in the right range.
		if (intArg1 < 0 || intArg1 > SpellUtil.CLASS_SPELL_COUNT)
		{
			msgLib.standardError("You have entered an invalid spell.");
			return -1;
		}
		
		return intArg1;
	}
}










