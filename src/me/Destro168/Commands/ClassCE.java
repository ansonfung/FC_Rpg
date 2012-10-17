package me.Destro168.Commands;

import java.text.DecimalFormat;

import me.Destro168.Configs.GeneralConfig;
import me.Destro168.Configs.PlayerFileConfig;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.TimeUtils.DateManager;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCE implements CommandExecutor
{
	public ClassCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable declarations.
		Player player = (Player) sender;
		RpgMessageLib msgLib;
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		RpgPlayer rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		PlayerFileConfig rpgPlayerFile = new PlayerFileConfig(player.getName());
        DecimalFormat df = new DecimalFormat("#.#");
		DateManager dm = new DateManager();
		GeneralConfig co = new GeneralConfig();
        
		msgLib = new RpgMessageLib(player);
		
		//Only let active players use this command.
		if (rpgPlayer.getPlayerConfigFile().getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		if (args[0].equals(""))
		{
			return msgLib.helpClass();
		}
		
		//Let the player add stat points.
		if (args[0].equalsIgnoreCase("spec"))
		{
			try
			{
				if (args[1].equalsIgnoreCase("attack"))
				{
					//Send success message to user
					if (rpgPlayer.useStats(1, Integer.valueOf(args[2])) == true)
						msgLib.standardMessage("Successfully used stat points!");
				}
				else if (args[1].equalsIgnoreCase("constitution") || args[1].equalsIgnoreCase("health"))
				{
					//Send success message to user
					if (rpgPlayer.useStats(2, Integer.valueOf(args[2])) == true)
						msgLib.standardMessage("Successfully used stat points!");
				}
				else if (args[1].equalsIgnoreCase("magic"))
				{
					//Send success message to user
					if (rpgPlayer.useStats(3, Integer.valueOf(args[2])) == true)
						msgLib.standardMessage("Successfully used stat points!");
				}
				else if (args[1].equalsIgnoreCase("intelligence") || args[1].equalsIgnoreCase("mana"))
				{
					//Send success message to user
					if (rpgPlayer.useStats(4, Integer.valueOf(args[2])) == true)
						msgLib.standardMessage("Successfully used stat points!");
				}
				else
					return msgLib.errorInvalidCommand();
			}
			catch (NumberFormatException e)
			{
				return msgLib.help();
			}
		}
		
		//Let the player view their stat points, experience, just all statistics really.
		else if (args[0].equalsIgnoreCase("view"))
		{
			//Check to see if we are viewing somebody elses's file
			if (!args[1].equals(""))
			{
				//If so load the file
				rpgPlayerFile = new PlayerFileConfig(args[1]);
				
				//Also set the rpgPlayer.
				if (Bukkit.getServer().getPlayer(args[1]) != null)
					rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
				else
					rpgPlayer = null;
			}
			
			//If rpgPlayer isn't null, then...
			if (rpgPlayer != null)
				rpgPlayer.updateTimePlayed(); //update time played.
			
			//Begin displaying stats.
			msgLib.standardHeader("Character Information Sheet");
			
			msgLib.standardMessage("Name",rpgPlayerFile.getName());
			msgLib.standardMessage("Time Played",String.valueOf(dm.getTimeStringFromTimeInteger(rpgPlayerFile.getSecondsPlayed())));
			msgLib.standardMessage("Class",FC_Rpg.classConfig.getRpgClass(rpgPlayerFile.getCombatClass()).getName());
			msgLib.standardMessage("Class Level",String.valueOf(rpgPlayerFile.getClassLevel()));
			msgLib.standardMessage("Class Experience",df.format(rpgPlayerFile.getClassExperience()) +
					" of " + df.format(rpgPlayerFile.getLevelUpAmount()) +
					" (" + df.format(rpgPlayerFile.getRequiredExpPercent()) + "% Gathered)");
			msgLib.standardMessage("Stat points",String.valueOf(rpgPlayerFile.getStats()));
			msgLib.standardMessage("Lifetime Mob Kills",String.valueOf(rpgPlayerFile.getLifetimeMobKills()));
			
			
			if (rpgPlayerFile.isDonator())
				msgLib.standardHeader("Stats ~ " + co.getDonatorBonusStatPercent() * 100 + "% Donator Bonus!");
			else
				msgLib.standardHeader("Stats");
			
			String curHealth = "";
			String maxHealth = "";
			String curMana = "";
			String maxMana = "";
			
			//Begin formulating the stat display message.
			if (rpgPlayer == null)
			{
				msgLib.standardMessage(getMessageArray("[Attack] Base: ", rpgPlayerFile.getAttack()));
				msgLib.standardMessage(getMessageArray("[Constitution] Base: ", rpgPlayerFile.getConstitution()));
				msgLib.standardMessage(getMessageArray("[Magic] Base: ", rpgPlayerFile.getMagic()));
				msgLib.standardMessage(getMessageArray("[Intelligence] Base: ", rpgPlayerFile.getIntelligence()));
				
				curHealth = df.format(rpgPlayerFile.getCurHealthFile());
				maxHealth = df.format(rpgPlayerFile.getMaxHealthFile());
				curMana = df.format(rpgPlayerFile.getCurManaFile());
				maxMana = df.format(rpgPlayerFile.getMaxManaFile());
			}
			else
			{
				msgLib.standardMessage(getMessageArray("[Attack] Base: ", rpgPlayerFile.getAttack(), " Total: ", rpgPlayer.getTotalAttack()));
				msgLib.standardMessage(getMessageArray("[Constitution] Base: ", rpgPlayerFile.getConstitution(), " Total: ", rpgPlayer.getTotalConstitution()));
				msgLib.standardMessage(getMessageArray("[Magic] Base: ", rpgPlayerFile.getMagic(), " Total: ", rpgPlayer.getTotalMagic()));
				msgLib.standardMessage(getMessageArray("[Intelligence] Base: ", rpgPlayerFile.getIntelligence(), " Total: ", rpgPlayer.getTotalIntelligence()));
				
				curHealth = df.format(rpgPlayer.getCurHealth());
				maxHealth = df.format(rpgPlayer.getMaxHealth());
				curMana = df.format(rpgPlayer.getCurMana());
				maxMana = df.format(rpgPlayer.getMaxMana());
			}
			
			msgLib.standardMessage(getMessageArray("[Health]: (", curHealth, "/", maxHealth, ")"));
			msgLib.standardMessage(getMessageArray("[Mana]: (", curMana, "/", maxMana, ")"));
			
			//Display what the manual allocation setting is currently.
			if (rpgPlayerFile.getManualAllocation() == true)
				msgLib.standardMessage("- Allocation State", "Stats are automatically distributed.");
			else
				msgLib.standardMessage("- Allocation State", "Stats must be manually distributed.");
		}
		
		//Let the player view their stat points, experience, just all statistics really.
		else if (args[0].equalsIgnoreCase("allocate"))
		{
			if (!args[1].equals(""))
			{
				if (args[1].equalsIgnoreCase("on"))
				{
					msgLib.standardMessage("Auto stat allocation enabled.");
					rpgPlayer.getPlayerConfigFile().setAutomaticAllocation(false);
					return true;
				}
				else if (args[1].equalsIgnoreCase("off"))
				{
					msgLib.standardMessage("Auto stat allocation disabled.");
					rpgPlayer.getPlayerConfigFile().setAutomaticAllocation(true);
					return true;
				}
			}
			
			if (rpgPlayer.getPlayerConfigFile().getManualAllocation() == true)
			{
				msgLib.standardMessage("Auto stat allocation enabled.");
				rpgPlayer.getPlayerConfigFile().setAutomaticAllocation(false);
				return true;
			}
			else
			{
				msgLib.standardMessage("Auto stat allocation disabled.");
				rpgPlayer.getPlayerConfigFile().setAutomaticAllocation(true);
				return true;
			}
		}
		
		//Attempt to switch class for players with class change tickets.
		else if (args[0].equals("switch"))
		{
			if (rpgPlayer.hasClassChangeTicket() == false)
				return msgLib.errorNoPermission();
		
			int classNumber = -1;
			
			try
			{
				classNumber = Integer.valueOf(args[1]);
			}
			catch (NumberFormatException e)
			{
				msgLib.standardMessage("Invalid class number.");
				return true;
			}
			
			//Make sure the new class is different from current class.
			if (rpgPlayer.getPlayerConfigFile().getCombatClass() == classNumber)
			{
				msgLib.standardMessage("You can't switch to the class you are already in.");
				return true;
			}
			
			//Make sure that the user is only picking from 0-4 (remember getClassNumber returns proper version).
			if (classNumber == -1 || classNumber > FC_Rpg.classConfig.getRpgClasses().length)
				return msgLib.errorInvalidCommand();
			
			//Put in class number minus one to prevent anomolies.
			rpgPlayer.switchClass(classNumber);
			
			//Message success.
			return msgLib.successCommand();
		}
		
		return true;
	}
	
	private String[] getMessageArray(String p1, int p2)
	{
		String[] messageArray = new String[2];
		
		messageArray[0] = p1;
		messageArray[1] = String.valueOf(p2);
		
		return messageArray;
	}
	
	private String[] getMessageArray(String p1, int p2, String p3, int p4)
	{
		String[] messageArray = new String[4];
		
		messageArray[0] = p1;
		messageArray[1] = String.valueOf(p2);
		messageArray[2] = p3;
		messageArray[3] = String.valueOf(p4);
		
		return messageArray;
	}
	
	private String[] getMessageArray(String p1, String p2, String p3, String p4, String p5)
	{
		String[] messageArray = new String[5];
		
		messageArray[0] = p1;
		messageArray[1] = p2;
		messageArray[2] = p3;
		messageArray[3] = p4;
		messageArray[4] = p5;
		
		return messageArray;
	}
}




















