package me.Destro168.FC_Rpg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.Configs.DungeonConfig;
import me.Destro168.Configs.FaqConfig;
import me.Destro168.Configs.GroupConfig;
import me.Destro168.Configs.PlayerConfig;
import me.Destro168.Configs.SpellConfig;
import me.Destro168.Configs.WarpConfig;
import me.Destro168.Configs.WorldConfig;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.LoadedObjects.Group;
import me.Destro168.LoadedObjects.RpgClass;
import me.Destro168.TimeUtils.DateManager;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;
import me.Destro168.events.DungeonEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandGod implements CommandExecutor
{
	private CommandSender sender;
	private ColouredConsoleSender console;
	private Player player;
	private RpgPlayer rpgPlayer;
	private RpgMessageLib msgLib;
	private FC_RpgPermissions perms;
	private String[] args;
	private boolean isActive = false;
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args2)
    {
		if (initialize(sender, args2) == false)
		{
			msgLib.standardMessage("Failed to initialize key variables for continued command execution.");
			return true;
		}
		
		else if (command.getName().equalsIgnoreCase("class"))
		{
			ClassCE cmd = new ClassCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("donator"))
		{
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			DonatorCE cmd = new DonatorCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("dungeon"))
		{
			DungeonCE cmd = new DungeonCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("d9"))
			return msgLib.helpDungeonDefinition();
		
		else if (command.getName().equalsIgnoreCase("faq"))
		{
			FaqCE cmd = new FaqCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("g") || command.getName().equalsIgnoreCase("h") || command.getName().equalsIgnoreCase("gh") || command.getName().equalsIgnoreCase("hg"))
		{
			QuickCE cmd = new QuickCE();
			return cmd.execute(command.getName());
		}
		
		else if (command.getName().equalsIgnoreCase("hat"))
		{
			HatCE cmd = new HatCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("job"))
		{
			JobCE cmd = new JobCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase("list"))
		{
			ListCE cmd = new ListCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("party"))
		{
			PartyCE cmd = new PartyCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("pvp"))
		{
			PvpCE cmd = new PvpCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("reset"))
		{
			ResetCE cmd = new ResetCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("rpg"))
		{
			RpgCE cmd = new RpgCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("spell"))
		{
			SpellCE cmd = new SpellCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("modify"))
		{
			ModifyCE cmd = new ModifyCE();
			return cmd.execute();
		}

		else if (command.getName().equalsIgnoreCase("w"))
		{
			WarpCE cmd = new WarpCE();
			return cmd.execute();
		}

		else if (command.getName().equalsIgnoreCase("buff"))
		{
			BuffCE cmd = new BuffCE();
			return cmd.execute();
		}
		
		return true;
    }
	
	private boolean initialize(CommandSender sender_, String[] args2)
	{
		//Variable Declarations/Initializations
		ArgParser ap = new ArgParser(args2);
		sender = sender_;
		args = ap.getArgs();
		
		//Assign key variables based on command input and arguments.
		if (sender instanceof Player)
		{
			player = (Player) sender;
			rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
			perms = new FC_RpgPermissions(player);
			msgLib = new RpgMessageLib(player);
			isActive = rpgPlayer.getPlayerConfig().getIsActive();
			console = null;
		}
		else if (sender instanceof ColouredConsoleSender)
		{
			console = (ColouredConsoleSender) sender;
			perms = new FC_RpgPermissions(true);
			msgLib = new RpgMessageLib(console);
			isActive = true;
			player = null;
		}
		else
		{
			FC_Rpg.plugin.getLogger().info("Unknown command sender, returning ban command.");
			return false;
		}
		
		return true;
	}
	
	public class ClassCE
	{
		public ClassCE() { }
		
		public boolean execute()
	    {
			//Variable declarations.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpClass();
			if (args[0].equalsIgnoreCase("spec"))
				return specSubCommand();
			else if (args[0].equalsIgnoreCase("view"))
				return viewSubCommand();
			else if (args[0].equalsIgnoreCase("list"))
				return listSubCommand();
			else if (args[0].equalsIgnoreCase("allocate"))
				return allocateSubCommand();
			else if (args[0].equalsIgnoreCase("switch"))
				return switchSubCommand();
			
			return true;
		}
		
		//Let the player add stat points.
		private boolean specSubCommand()
		{
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			try
			{
				if (args[1].equalsIgnoreCase("attack") || args[1].equalsIgnoreCase("strength"))
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
				return msgLib.helpRpg();
			}
			
			return true;
		}
		
		//Let the player view their stat points, experience, just all statistics really.
		private boolean viewSubCommand()
		{
			//Variable Declarations
			PlayerConfig rpgPlayerFile;
			DateManager dm = new DateManager();
			
			String curHealth = "";
			String maxHealth = "";
			String curMana = "";
			String maxMana = "";
			String baseKeyWord = " Base: ";
			String totalKeyWord = " Total: ";
			
			//Check to see if we are viewing somebody elses's file
			if (!args[1].equalsIgnoreCase(""))
			{
				
				//If so load the file
				rpgPlayerFile = new PlayerConfig(args[1]);
				
				//Also set the rpgPlayer.
				if (Bukkit.getServer().getPlayer(args[1]) != null)
					rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
				else
					rpgPlayer = null;
			}
			else
			{
				if (console != null)
					return msgLib.standardError("The Console Must Enter A Player Name To Use This Command.");
				
				//If so load the file
				rpgPlayerFile = rpgPlayer.getPlayerConfig();
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
			msgLib.standardMessage("Class Experience",FC_Rpg.df.format(rpgPlayerFile.getClassExperience()) +
					" of " + FC_Rpg.df.format(rpgPlayerFile.getLevelUpAmount()) +
					" (" + FC_Rpg.df.format(rpgPlayerFile.getRequiredExpPercent()) + "% Gathered)");
			msgLib.standardMessage("Stat points",String.valueOf(rpgPlayerFile.getStats()));
			msgLib.standardMessage("Lifetime Mob Kills",String.valueOf(rpgPlayerFile.getLifetimeMobKills()));
			
			if (rpgPlayerFile.isDonator())
				msgLib.standardHeader("Stats ~ " + FC_Rpg.generalConfig.getDonatorBonusStatPercent() * 100 + "% Donator Bonus!");
			else
				msgLib.standardHeader("Stats");
			
			List<String> attackDisplay = getMessageList(baseKeyWord, rpgPlayerFile.getAttack());
			List<String> constDisplay = getMessageList(baseKeyWord, rpgPlayerFile.getConstitution());
			List<String> magicDisplay = getMessageList(baseKeyWord, rpgPlayerFile.getMagic());
			List<String> intelligenceDisplay = getMessageList(baseKeyWord, rpgPlayerFile.getIntelligence());
			
			//Begin formulating the stat display message.
			if (rpgPlayer != null)
			{
				attackDisplay.add(totalKeyWord);
				constDisplay.add(totalKeyWord);
				magicDisplay.add(totalKeyWord);
				intelligenceDisplay.add(totalKeyWord);
				
				attackDisplay.add(String.valueOf(rpgPlayer.getTotalAttack()));
				constDisplay.add(String.valueOf(rpgPlayer.getTotalConstitution()));
				magicDisplay.add(String.valueOf(rpgPlayer.getTotalMagic()));
				intelligenceDisplay.add(String.valueOf(rpgPlayer.getTotalIntelligence()));
				
				curHealth = FC_Rpg.df.format(rpgPlayer.getCurHealth());
				maxHealth = FC_Rpg.df.format(rpgPlayer.getMaxHealth());
				curMana = FC_Rpg.df.format(rpgPlayer.getCurMana());
				maxMana = FC_Rpg.df.format(rpgPlayer.getMaxMana());
			}
			else
			{
				curHealth = FC_Rpg.df.format(rpgPlayerFile.getCurHealthFile());
				maxHealth = FC_Rpg.df.format(rpgPlayerFile.getMaxHealthFile());
				curMana = FC_Rpg.df.format(rpgPlayerFile.getCurManaFile());
				maxMana = FC_Rpg.df.format(rpgPlayerFile.getMaxManaFile());
			}
			
			msgLib.standardMessage("[Attack]",attackDisplay);
			msgLib.standardMessage("[Constitution]",constDisplay);
			msgLib.standardMessage("[Magic]",magicDisplay);
			msgLib.standardMessage("[Intelligence]",intelligenceDisplay);
			
			msgLib.standardMessage(getMessageArray("[Health]: (", curHealth, "/", maxHealth, ")"));
			msgLib.standardMessage(getMessageArray("[Mana]: (", curMana, "/", maxMana, ")"));
			
			//Display what the manual allocation setting is currently.
			if (rpgPlayerFile.getManualAllocation() == true)
				msgLib.standardMessage("- Allocation State", "Stats are automatically distributed.");
			else
				msgLib.standardMessage("- Allocation State", "Stats must be manually distributed.");
			
			return true;
		}
		
		private boolean listSubCommand()
		{
			RpgClass[] classes = FC_Rpg.classConfig.getRpgClasses();
			
			msgLib.standardHeader("Server Classes List");
			
			for (int i = 0; i < classes.length; i++)
				msgLib.standardMessage("#" + i,classes[i].getName());
			
			return true;
		}
		
		//Let the player view their stat points, experience, just all statistics really.
		private boolean allocateSubCommand()
		{
			if (console != null)
				return msgLib.standardError("The Console Must Enter A Player Name To Use This Command.");
			
			if (!args[1].equalsIgnoreCase(""))
			{
				if (args[1].equalsIgnoreCase("on"))
				{
					msgLib.standardMessage("Auto stat allocation enabled.");
					rpgPlayer.getPlayerConfig().setAutomaticAllocation(false);
					return true;
				}
				else if (args[1].equalsIgnoreCase("off"))
				{
					msgLib.standardMessage("Auto stat allocation disabled.");
					rpgPlayer.getPlayerConfig().setAutomaticAllocation(true);
					return true;
				}
			}
			
			if (rpgPlayer.getPlayerConfig().getManualAllocation() == true)
			{
				msgLib.standardMessage("Auto stat allocation enabled.");
				rpgPlayer.getPlayerConfig().setAutomaticAllocation(false);
				return true;
			}
			else
			{
				msgLib.standardMessage("Auto stat allocation disabled.");
				rpgPlayer.getPlayerConfig().setAutomaticAllocation(true);
				return true;
			}
		}
		
		//Attempt to switch class for players with class change tickets.
		private boolean switchSubCommand()
		{
			//Only consoles can use the switch command.
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			//Switch to the new class.
			rpgPlayer.switchClass(args[1]);
			
			//Message success.
			return msgLib.successCommand();
		}
		
		private List<String> getMessageList(String p1, int p2)
		{
			List<String> messageList = new ArrayList<String>();
			
			messageList.add(p1);
			messageList.add(String.valueOf(p2));
			
			return messageList;
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

	public class DonatorCE
	{
		public DonatorCE() { }
		
		public boolean execute()
		{
			//Prevent console from using donator command.
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (rpgPlayer.getPlayerConfig().isDonator() == false)
				return msgLib.errorNoPermission();
			
			if (args[0].equalsIgnoreCase(""))
			{
				//Display the players donation information.
				msgLib.standardHeader("Donator Information");
				msgLib.standardMessage("Thank you for donating!");
				msgLib.standardMessage("Donation Perks End On: " + ChatColor.YELLOW + FC_Rpg.dfm.format(rpgPlayer.getPlayerConfig().getDonatorTime()));
				msgLib.helpDonator();
			}
			else if (args[0].equalsIgnoreCase("respecialize"))
			{
				rpgPlayer.respecAll();
				msgLib.standardMessage("Successfully refunded stat and spell points. Remember to use them!");
			}
			
			return true;
		}
	}
	
	public class DungeonCE
	{
		int dungeonNumber = 0;
		DungeonEvent dungeon;
		DungeonConfig dc = new DungeonConfig();
		Location pLoc = player.getLocation();
		
		public boolean execute()
	    {
			//Only admins and console can use dungeon command.
			if (perms.isAdmin() == false)
				return msgLib.errorNoPermission();
			
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpDungeon();
			
			//Commands that require only an argument.
			if (args[0].equalsIgnoreCase("list"))
			{
				for (int i = 0; i < FC_Rpg.dungeonEventArray.length; i++)
				{
					updateDungeonInfo(i);
					msgLib.standardMessage("Dungeon: " + i + " [N]: " +  dungeon.getDungeonName() + " [S]: " + dungeon.isHappening());
				}
				
				return true;
			}
			
			//Commands that require a dungeon number
			if (args[1] == null) 
				return msgLib.errorInvalidCommand();
			else
				try { dungeonNumber = Integer.valueOf(args[1]); } catch (NumberFormatException e) { }
			
			if (args[0].equalsIgnoreCase("name"))
			{
				if (dungeonNumber == -1 || args[2] == null)
					return msgLib.errorInvalidCommand();
				
				dc.setName(dungeonNumber, args[2]);
				return msgLib.successCommand();
			}
			
			//Commands that require an existing dungeon.
			updateDungeonInfo(dungeonNumber);
			
			if (args[0].equalsIgnoreCase("info"))
			{
				//If the dungeon number is -1 we return.
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				msgLib.standardHeader("The Dungeon: " + FC_Rpg.dungeonEventArray[dungeonNumber].getDungeonName());
				msgLib.standardMessage("In Use?",String.valueOf(FC_Rpg.dungeonEventArray[dungeonNumber].isHappening()));
				msgLib.standardMessage("Current Phase",String.valueOf(FC_Rpg.dungeonEventArray[dungeonNumber].getPhase()));
				msgLib.standardMessage("Lowest Level",String.valueOf(FC_Rpg.dungeonEventArray[dungeonNumber].getLowestLevel()));
				
				//Attempt to list partcipants.
				int pSize = FC_Rpg.dungeonEventArray[dungeonNumber].getParticipants().size();
				
				if (pSize > 0)
				{
					String[] names = new String[pSize];
					List<Player> p =  FC_Rpg.dungeonEventArray[dungeonNumber].getParticipants();
					
					for (int i = 0; i < pSize; i++)
						names[i] = p.get(i).getName();
					
					msgLib.standardMessage("Participants: ",names);
				}
				else
					msgLib.standardMessage("Participants","Currently Empty.");
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("end"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				dungeon.end(false);
				return true;
			}
			else if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("init"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				dungeon.initialize(dungeonNumber);
				return true;
			}
			else if (args[0].equalsIgnoreCase("check"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				//Check if there are any mobs left in a dungeon.
				dungeon.checkMobDeath(null);
				return msgLib.successCommand();
			}
			else if (args[0].equalsIgnoreCase("kick"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				if (args[2].equalsIgnoreCase(""))
					return msgLib.errorInvalidCommand();
				
				Player p2 = Bukkit.getServer().getPlayer(args[2]);
				
				if (p2 == null)
					return msgLib.errorPlayerNotOnline();
				
				dungeon.removeDungeoneer(player, p2, true);
				
				return true;
			}
			
			//Commands can't be used by console
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (args[0].equalsIgnoreCase("ranges"))
			{
				Location loc1 = FC_Rpg.sv.getBlockLoc1(player);
				Location loc2 = FC_Rpg.sv.getBlockLoc2(player);
				int index;
				
				if (loc1 == null || loc2 == null)
					return msgLib.errorInvalidSelection();
				
				index = dc.setRange1(dungeonNumber, loc1.getWorld().getName(), loc1.getX(), loc1.getY(), loc1.getZ(), loc1.getYaw(), loc1.getPitch());
				dc.setRange2(dungeonNumber, index, loc2.getWorld().getName(), loc2.getX(), loc2.getY(), loc2.getZ(), loc2.getYaw(), loc2.getPitch());
				
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("lobby"))
			{
				dc.setLobby(dungeonNumber, pLoc.getWorld().getName(), pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw());
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("playerStart"))
			{
				dc.setStart(dungeonNumber, pLoc.getWorld().getName(), pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw());
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("playerEnd"))
			{
				dc.setExit(dungeonNumber, pLoc.getWorld().getName(), pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw());
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("boss"))
			{
				dc.setBossSpawn(dungeonNumber, pLoc.getWorld().getName(), pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw());
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("treasure"))
			{
				dc.setTreasureChest(dungeonNumber, pLoc.getWorld().getName(), pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw());
				return msgLib.successCommand();
			}
			
			//Commands that require a second part.
			if (args[2].equalsIgnoreCase(""))
				return msgLib.helpDungeon();
			
			if (args[0].equalsIgnoreCase("cost"))
			{
				try { dc.setCost(dungeonNumber, Double.valueOf(args[2])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("lmin"))
			{
				try { dc.setLevelMin(dungeonNumber, Integer.valueOf(args[2])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("lmax"))
			{
				try { dc.setLevelMax(dungeonNumber, Integer.valueOf(args[2])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("spawncount"))
			{
				try { dc.setSpawnCount(dungeonNumber, Integer.valueOf(args[2])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
				return msgLib.successCommand();
			}
			
			//Commands that require a third part.
			if (args[3].equalsIgnoreCase(""))
				return msgLib.helpDungeonDefinition();
			else if (args[0].equalsIgnoreCase("spawnchance"))
				dc.setSpawnChance(dungeonNumber, Integer.valueOf(args[2]), Integer.valueOf(args[3]));
			else
				return msgLib.helpDungeonDefinition();
			
			//Dungeon help.
			return msgLib.successCommand();
	    }
		
		private boolean updateDungeonInfo(int newDungeonNumber)
		{
			//Convert dungeon number.
			dungeonNumber = newDungeonNumber;
			
			if (!FC_Rpg.trueDungeonNumbers.containsKey(dungeonNumber))
				return false;
			
			//Store dungeon for convenient use.
			dungeon = FC_Rpg.dungeonEventArray[FC_Rpg.trueDungeonNumbers.get(dungeonNumber)];
			
			return true;
		}
	}
	
	public class FaqCE
	{
		FaqConfig fc = new FaqConfig();
		
		public FaqCE() { }
			
		public boolean execute()
	    {
			if (args[0].equalsIgnoreCase("add"))
				return addSubCommand();
			else if (args[0].equalsIgnoreCase("del"))
				return delSubCommand();
			else if (args[0].equalsIgnoreCase("eProperty"))
				return editPropertySubCommand();
			else if (args[0].equalsIgnoreCase("eLine"))
				return editLineSubCommand();
			else
				return displaySubTopic();
	    }
		
		// /faq add [displayTag] 
		private boolean addSubCommand()
		{
			if (args[1].equalsIgnoreCase(""))
				return msgLib.errorInvalidCommand();
			
			fc.addNewFaq(args[1]);
			fc.editFaqProperties(args[1], "header", args[1]);
			fc.editFaqProperties(args[1], "displayTag", args[1]);
			fc.editFaqLines(args[1], 0, 1, "Example");
			fc.editFaqLines(args[1], 0, 2, "Example");
			
			return msgLib.standardMessage("Success! Remember to use the /faq edit (good luck) command on this faq to set it up!!!");
		}
		
		// /faq del [displayTag] <line> <half>
		private boolean delSubCommand()
		{
			if (args[1].equalsIgnoreCase(""))
				return msgLib.errorInvalidCommand();
			
			if (!args[2].equalsIgnoreCase(""))
			{
				if (args[3].equalsIgnoreCase("1"))
					fc.editFaqLines(args[1], Integer.valueOf(args[2]), 1, null);
				else if (args[3].equalsIgnoreCase("2"))
					fc.editFaqLines(args[1], Integer.valueOf(args[2]), 2, null);
				else
				{
					fc.editFaqLines(args[1], Integer.valueOf(args[2]), 1, null);
					fc.editFaqLines(args[1], Integer.valueOf(args[2]), 2, null);
				}
			}
			else
				fc.setFaqNull(args[1]);
			
			return msgLib.successCommand();
		}
		
		// /faq eProperty [displayTag] [modifiable] [new value]
		private boolean editPropertySubCommand()
		{
			fc.editFaqProperties(args[1], args[2], args[3]);
			
			return msgLib.successCommand();
		}
		
		// /faq eLine [displayTag] <line> <half> [new value]
		private boolean editLineSubCommand()
		{
			boolean success = false;
			
			ArgParser ap = new ArgParser(args);
			ap.setLastArg(4);
			try { success = fc.editFaqLines(args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), ap.getFinalArg()); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
			
			if (success == true)
				return msgLib.successCommand();
			else
				return msgLib.errorInvalidCommand();
		}
		
		//Called with no subcommand.
		private boolean displaySubTopic()
		{
			String tag = "";
			String faqFirstHalf = "";
			String faqSecondHalf = "";
			int breakPoint1 = 0;
			int breakPoint2 = 0;
			int count = 0;
			
			//We attempt to display faqs and parse based on faq.
			for (int i = 0; i < 10000; i++)
			{
				//Store the tag that we are currently parsing.
				tag = fc.getDisplayTag(i);
				
				//If the tag isn't null...
				if (fc.getDisplayTag(i) != null)
				{
					//Also if the argument entered is equal to the tag...
					if (args[0].equalsIgnoreCase(tag))
					{
						//We prase through the faqs for that tag now.
						for (int j = 0; j < 10000; j++)
						{
							//Store first and second half of the faq.
							try
							{
								faqFirstHalf = fc.getFaqList(i, 1).get(j);
								faqSecondHalf = fc.getFaqList(i, 2).get(j);
								
								if (faqFirstHalf != null && faqSecondHalf != null)
								{
									if (count == 0)
										msgLib.standardHeader(fc.getHeader(i));
									
									if (!(faqSecondHalf.equalsIgnoreCase("[empty]")))
										msgLib.standardMessage("#" + count + ": " + faqFirstHalf,faqSecondHalf);
									else
										msgLib.standardMessage("#" + count + ": " + faqFirstHalf);
									
									count++;
								}
								else
								{
									breakPoint2++;
									
									if (breakPoint2 > 50)
										break;
								}
							}
							catch (IndexOutOfBoundsException e) { }
						}
					}
				}
				else
				{
					breakPoint1++;
					
					if (breakPoint1 > 50)
						break;
				}
			}
			
			if (count == 0)
				return msgLib.helpFaq();
			
			return true;
		}
	}
	
	public class QuickCE
	{
		public boolean execute(String commandName)
		{
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (perms.isAdmin() == false)
				return msgLib.errorNoPermission();
			
			if (args[0].equalsIgnoreCase(""))
				args[0] = player.getName();
			
			if (Bukkit.getServer().getPlayer(args[0]) == null)
				return msgLib.errorPlayerNotOnline();
			
			if (commandName.equalsIgnoreCase("g"))
			{
				quick_Gamemode();
			}
			else if (commandName.equalsIgnoreCase("h"))
			{
				quick_Heal();
			}
			else if (commandName.equalsIgnoreCase("gh") || commandName.equalsIgnoreCase("hg"))
			{
				quick_Heal();
				quick_Gamemode();
			}
			
			return true;
		}
		
		private void quick_Gamemode()
	    {
			if (player.getGameMode() == GameMode.SURVIVAL)
			{
				player.setGameMode(GameMode.CREATIVE);
				msgLib.standardMessage("Successfully set gamemode creative for " + args[0]);
			}
			else
			{
				player.setGameMode(GameMode.SURVIVAL);
				msgLib.standardMessage("Successfully set gamemode survival for " + args[0]);
			}
	    }
		
		private void quick_Heal()
	    {
			FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[0])).healFull();
			
			//Restore health and food
			player.setHealth(20);
			player.setFoodLevel(20);
			
			msgLib.standardMessage("Successfully healed " + args[0]);
	    }
	}

	public class HatCE
	{
		public HatCE() { }
		
		public boolean execute()
	    {
			//Prevent console from using hat command.
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (rpgPlayer.getPlayerConfig().isDonator() || perms.commandHat())
			{
				if (player.getInventory().getHelmet() != null)
					Bukkit.getServer().getWorld(player.getWorld().getName()).dropItem(player.getLocation(), player.getInventory().getHelmet());
				
				player.getInventory().setHelmet(player.getItemInHand());
				player.setItemInHand(null);
				
				return true;
			}
			
			return msgLib.errorNoPermission();
	    }
	}
	
	public class JobCE
	{
		public JobCE() { }
		
		public boolean execute()
	    {
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Return help on empty sub commands.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpJob();
			
			if (args[0].equalsIgnoreCase("view"))
			{
				PlayerConfig playerFile = null;
				
				//Pick the target.
				if (!args[1].equalsIgnoreCase(""))
				{
					if (Bukkit.getServer().getPlayer(args[1]) != null)
					{
						rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
						playerFile = rpgPlayer.getPlayerConfig();
					}
					else
					{
						playerFile = new PlayerConfig(args[1]);
					}
				}
				else
				{
					//Console can't view job information about itself.
					if (console != null)
						return msgLib.errorConsoleCantUseCommand();
					
					playerFile = rpgPlayer.getPlayerConfig();
				}
				
				//Send the information.
				msgLib.standardHeader("Job Information Sheet");
				msgLib.standardMessage("Name", playerFile.getName());
				msgLib.standardMessage("Job Rank",String.valueOf(playerFile.getJobRank()));
				msgLib.standardMessage("Job Promotion Cost",String.valueOf(playerFile.getPromotionCost()));
			}
			else if (args[0].equalsIgnoreCase("promote"))
			{
				//Console can't promote itself.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				//Make sure the player isn't max job rank first
				if (rpgPlayer.getPlayerConfig().getJobRank() < 6)
				{
					//Make sure that players can only be promoted with the proper job rank.
					if (rpgPlayer.getPlayerConfig().getClassLevel() < 20 && rpgPlayer.getPlayerConfig().getJobRank() == 1)
					{
						msgLib.standardError("You need level 20+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfig().getClassLevel() < 40 && rpgPlayer.getPlayerConfig().getJobRank() == 2)
					{
						msgLib.standardError("You need level 40+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfig().getClassLevel() < 60 && rpgPlayer.getPlayerConfig().getJobRank() == 3)
					{
						msgLib.standardError("You need level 60+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfig().getClassLevel() < 80 && rpgPlayer.getPlayerConfig().getJobRank() == 4)
					{
						msgLib.standardError("You need level 80+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfig().getClassLevel() < 100 && rpgPlayer.getPlayerConfig().getJobRank() == 5)
					{
						msgLib.standardError("You need level 100 for the final promotion. Good luck :)");
						return true;
					}
					
					//If the player can afford a promotion
					if (FC_Rpg.economy.getBalance(player.getName()) > rpgPlayer.getPlayerConfig().getPromotionCost())
					{
						//Take away money from the player.
						FC_Rpg.economy.bankWithdraw(player.getName(), rpgPlayer.getPlayerConfig().getPromotionCost());
						
						//Give them the promotion
						rpgPlayer.getPlayerConfig().setJobRank(rpgPlayer.getPlayerConfig().getJobRank() + 1);
						
						//Announce the promotion
						FC_Rpg.bLib.standardBroadcast(player.getName() + " is now Job Rank [" + rpgPlayer.getPlayerConfig().getJobRank() + "]");
					}
					else
					{
						msgLib.standardError("You need $" + String.valueOf(rpgPlayer.getPlayerConfig().getPromotionCost() - FC_Rpg.economy.getBalance(player.getName())) + " for promotion");
					}
				}
				else
				{
					return msgLib.errorMaxJob();
				}
			}
			
			return true;
		}
	}
	
	public class ListCE
	{
		public ListCE() { }
		
		public boolean execute()
	    {
			GroupConfig conf = new GroupConfig();
			List<Group> groups = conf.getGroups();
			List<String> groupMembers = new ArrayList<String>();
			
			//Begin displaying the information.
			msgLib.standardHeader("Total Connected Players: " + ChatColor.GREEN + ChatColor.BOLD + Bukkit.getOnlinePlayers().length + ChatColor.GRAY + ChatColor.BOLD + "/" +
					ChatColor.RED + ChatColor.BOLD + Bukkit.getServer().getMaxPlayers());
			
			Player[] onlinePlayersArray = Bukkit.getServer().getOnlinePlayers();
			Map<Integer, Player> onlinePlayersMap = new HashMap<Integer, Player>();
			
			for (int i = 0; i < onlinePlayersArray.length; i++) 
				onlinePlayersMap.put(i,onlinePlayersArray[i]);
			
			for (int i = 0; i < groups.size(); i++)
			{
				groupMembers.clear();
				
				for (int j = 0; j < onlinePlayersArray.length; j++)
				{
					if (onlinePlayersMap.containsKey(j))
					{
						perms = new FC_RpgPermissions(onlinePlayersMap.get(j));
						
						if (perms.inGroup(groups.get(i).getName()))
						{
							groupMembers.add(onlinePlayersMap.get(j).getName());
							onlinePlayersMap.remove(j);
						}
					}
				}
				
				if (groupMembers.size() > 0)
					msgLib.standardMessage(groups.get(i).getDisplay(), specialConversion(groupMembers));
			}
			
			return true;
		}
		
		protected String specialConversion(List<String> msg)
		{
			//Variable Declarations
			String message = "";
			int alternateState = 0;
			
			//We want to alternate the colors for the standard  message.
			for (int i = 0; i < msg.size(); i++)
			{
				if (msg.get(i) != null)
				{
					if (alternateState == 0)
					{
						message += ChatColor.WHITE + "" + msg.get(i);
						alternateState = 1;
					}
					else if (alternateState == 1)
					{
						message += ChatColor.GRAY + ", " + msg.get(i);
						alternateState = 2;
					}
					else if (alternateState == 2)
					{
						message += ChatColor.WHITE + ", " + msg.get(i);
						alternateState = 1;
					}
				}
			}
			
			return message;
		}
	}
	
	public class PartyCE
	{
		public PartyCE() { }
		
		public boolean execute()
	    {
			//Variable declarations
			boolean success = true;
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			if (args[0].equalsIgnoreCase(""))
			{
				return msgLib.helpParty();
			}
			
			if (args[0].equalsIgnoreCase("list"))
			{
				msgLib.standardMessage(FC_Rpg.partyManager.getPartyList());
			}
			else if (args[0].equalsIgnoreCase("create"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equalsIgnoreCase(""))
					args[1] = "No Name! Rename!";
				
				success = FC_Rpg.partyManager.createParty(player.getName(), args[1]);
				
		    	if (success == false)
		    		msgLib.standardError("Failed to create a new party.");
			}
			else if (args[0].equalsIgnoreCase("close"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				success = FC_Rpg.partyManager.closeParty(player.getName());
				
				if (success == false)
					msgLib.standardError("Failed to close your party.");
				else
					msgLib.standardMessage("Successfully closed your party!");
			}
			else if (args[0].equalsIgnoreCase("open"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				success = FC_Rpg.partyManager.openParty(player.getName());
				
				if (success == false)
					msgLib.standardError("Failed to open your party.");
				else
					msgLib.standardMessage("Successfully opened your party!");
			}
			else if (args[0].equalsIgnoreCase("join"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equalsIgnoreCase(""))
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
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				//Remove the member from any old parties
				success = FC_Rpg.partyManager.removeMemberFromAllParties(player.getName());
				
				if (success == false)
					msgLib.standardError("Failed to leave any parties.");
				else
					msgLib.standardMessage("Successfully left any and all parties.");
			}
			else if (args[0].equalsIgnoreCase("view"))
			{
				if (args[1].equalsIgnoreCase(""))
				{
					return msgLib.errorInvalidCommand();
				}
				else
				{
					success = FC_Rpg.partyManager.viewPartyInfo(args[1], msgLib);
					
					if (success == false)
						msgLib.standardError("Failed to view any parties.");
				}
			}
			
			return true;
	    }
	}
	
	public class PvpCE
	{
		public boolean execute()
	    {
			Player target;
			boolean isHappening = FC_Rpg.pvp.isHappening();
			
			//If the new sub command is used, ...
			if (args[0].equalsIgnoreCase("new"))
			{
				//Only admins can use this command.
				if (perms.isAdmin() == false)
					return true;
				
				//End existing pvp events.
				if (isHappening == true)
					endPvpEvent();
				
				FC_Rpg.pvp.begin();	//Start a new one.
				
				return true;
			}
			
			else if (isHappening == false)
			{
				if (args[0].equalsIgnoreCase(""))
					return msgLib.helpPvp();
				else
					return msgLib.standardMessage("Command was blocked because no event is currently happening.");
			}
			
			//If the end sub command is used, ...
			else if (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("stop"))
			{
				//Only admins can use this command.
				if (perms.isAdmin() == false)
					return true;
				
				//End existing pvp events.
				endPvpEvent();
				return true;
			}
			
			//If the join command is used, ...
			if (args[0].equalsIgnoreCase("join"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				//Add the player
				if (FC_Rpg.pvp.addPvper(player) == true)
					FC_Rpg.bLib.standardBroadcast(player.getName() + " Has Joined The " + ChatColor.RED + "Arena");
				
				return true;
			}
			
			//If the leave command is used, ...
			else if (args[0].equalsIgnoreCase("leave"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (FC_Rpg.pvp.removePvper(player,player,true) == true)
					return msgLib.successCommand();
				
				return true;
			}
			
			//If the leave command is used, ...
			else if (args[0].equalsIgnoreCase("kick"))
			{
				//Ensure an admin is performing the command.
				if (perms.isAdmin() == false)
					return msgLib.errorNoPermission();
				
				//Store the target.
				target = Bukkit.getServer().getPlayer(args[1]);
				
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
			else if (args[0].equalsIgnoreCase("list"))
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
	
	public class ResetCE
	{
		public ResetCE() { }
		
		public boolean execute()
	    {
			//Variable declarations
			PlayerConfig rpgPlayerFile;
			Player target;
			
			if (args[0].equalsIgnoreCase(""))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				args[0] = player.getName();
			}
			
			rpgPlayerFile = new PlayerConfig(args[0]);
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Store target.
			target = Bukkit.getServer().getPlayer(args[0]);
			
			if (perms.isAdmin())
			{
				//Set to inactive
				rpgPlayer.getPlayerConfig().setDefaults();
				
				//Stop the players tasks.
				FC_Rpg.rpgManager.unregisterRpgPlayer(player);
				
				//Send confirmation message.
				msgLib.standardMessage("Successfully reset",rpgPlayer.getPlayerConfig().getName());
				
				if (target != null)
					FC_Rpg.rpgManager.unregisterRpgPlayer(target);
			}
			else
			{
				//We want to return if the player is attempting to reset somebody else.
				if (!rpgPlayerFile.getName().equalsIgnoreCase(player.getName()))
					return true;
				
				//Set to inactive
				rpgPlayerFile.setDefaults();
				
				//Send confirmation message.
				msgLib.standardMessage("You have successfully reset yourself.");
				
				//Unregister
				FC_Rpg.rpgManager.unregisterRpgPlayer(player);
			}
			
			return true;
		}
	}
	
	public class ModifyCE
	{
		PlayerConfig playerFile;
		
		public ModifyCE() { }
		
		public boolean execute()
		{
			//THE ALMIGHTTY MODIFY COMMAND.
			String name = args[0];
			String modifable = args[1];
			int intArg2;
			
			if (args[2].equalsIgnoreCase(""))
				return msgLib.helpModify();
			else
				try { intArg2 = Integer.valueOf(args[2]); } catch (NumberFormatException e) { intArg2 = -1; }
			
			// modify [name] [modifiable] [new value]
			if (name.equalsIgnoreCase("") && modifable.equalsIgnoreCase(""))
				return msgLib.helpModify();
			
			/********************
			 * Set up playerfile
			 ********************/
			
			if (FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(name)) != null)
				playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(name)).getPlayerConfig();
			else
				playerFile = new PlayerConfig(name);
			
			if (playerFile == null)
				return msgLib.errorPlayerNotFound();
			
			if (playerFile.getIsActive() != true)
				return msgLib.errorCreateCharacter();
			
			//Evaluate command parts.
			if (modifable.equalsIgnoreCase("prefix"))
				playerFile.setCustomPrefix(args[2]);
			else
			{
				if (intArg2 == -1)
					return msgLib.errorInvalidCommand();
				
				if (modifable.equalsIgnoreCase("strength") || modifable.equalsIgnoreCase("attack"))
					playerFile.setAttack(intArg2);
				else if (modifable.equalsIgnoreCase("magic"))
					playerFile.setMagic(intArg2);
				else if (modifable.equalsIgnoreCase("constitution") || modifable.equalsIgnoreCase("health"))
					playerFile.setConstitution(intArg2);
				else if (modifable.equalsIgnoreCase("intelligence") || modifable.equalsIgnoreCase("mana"))
					playerFile.setIntelligence(intArg2);
				else if (modifable.equalsIgnoreCase("all"))
				{
					playerFile.setAttack(intArg2);
					playerFile.setMagic(intArg2);
					playerFile.setConstitution(intArg2);
					playerFile.setIntelligence(intArg2);
				}
				else if (modifable.equalsIgnoreCase("stat") || modifable.equalsIgnoreCase("stats"))
					playerFile.setStats(intArg2);
				else if (modifable.equalsIgnoreCase("addLevel") || modifable.equalsIgnoreCase("addLevels"))
					playerFile.addLevels(intArg2);
				else if (modifable.equalsIgnoreCase("level"))
					playerFile.setClassLevel(intArg2);
				else if (modifable.equalsIgnoreCase("exp"))
					playerFile.setClassExperience(intArg2);
				else if (modifable.equalsIgnoreCase("addExp") || modifable.equalsIgnoreCase("addExperience"))
					playerFile.addOfflineClassExperience(intArg2, true);
				else if (modifable.equalsIgnoreCase("class"))
				{
					if (intArg2 >= 0 && intArg2 < FC_Rpg.classConfig.getRpgClasses().length)
						playerFile.setCombatClass(intArg2);
				}
				else if (modifable.equalsIgnoreCase("jobRank"))
					playerFile.setJobRank(intArg2);
				else if (modifable.equalsIgnoreCase("spellPoint") || modifable.equalsIgnoreCase("spellPoints"))
					playerFile.setSpellPoints(intArg2);
				else if (modifable.equalsIgnoreCase("addsecond") || modifable.equalsIgnoreCase("addseconds"))
					playerFile.setSecondsPlayed(playerFile.getSecondsPlayed() + intArg2);
				else if (modifable.equalsIgnoreCase("setsecond") || modifable.equalsIgnoreCase("setseconds"))
					playerFile.setSecondsPlayed(intArg2);
				
				else if (modifable.equalsIgnoreCase("donator") || modifable.equalsIgnoreCase("d"))
				{
					playerFile.offlineSetDonator(intArg2);
					FC_Rpg.bLib.standardBroadcast("Thank you for donating " + args[0] + "!!!");
				}
				else if (modifable.equalsIgnoreCase("ticket") || modifable.equalsIgnoreCase("tickets"))
					playerFile.setClassChangeTickets(intArg2);
				else
					return msgLib.errorInvalidCommand();
				
				//Update the players health and mana
				playerFile.calculateHealthAndManaOffline();
			}
			
			return msgLib.successCommand();
		}
	}
	
	public class RpgCE 
	{
		public RpgCE() { }
		
		public boolean execute()
	    {
			//Variable declarations.
			int eventLength = 3600;
			
			//We want to send help on empty commands.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpRpg();
			else if (args[0].equalsIgnoreCase("admin"))
				return msgLib.helpAdmin();
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Handle admin commands with /rpg.
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
			//Handle admin events
			if (args[0].equalsIgnoreCase("event"))
			{
				if (args[1].equalsIgnoreCase(""))
					args[1] = "";
				
				if (args[1].equalsIgnoreCase("exp"))
				{
					//Set the exp multiplier up high
					FC_Rpg.eventExpMultiplier = 2;
					
					//Announce the start of the event.
					FC_Rpg.bLib.standardBroadcast("Double Experience Event For One Hour Started!");
					
					FC_Rpg.tid3 = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
					{
						@Override
						public void run()
						{
							FC_Rpg.eventExpMultiplier = 1;
							FC_Rpg.bLib.standardBroadcast("Double Experience Event Has Ended!");
						}
					} , eventLength * 20);
				}
				else if (args[1].equalsIgnoreCase("loot"))
				{
					FC_Rpg.eventCashMultiplier = 2;
					
					FC_Rpg.bLib.standardBroadcast("Double Money Event For One Hour Started!");
					
					FC_Rpg.tid4 = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
					{
						public void run()
						{
							FC_Rpg.eventCashMultiplier = 1;
							FC_Rpg.bLib.standardBroadcast("Double Money Event Has Ended!");
						}
					} , eventLength * 20);
				}
				else if (args[1].equalsIgnoreCase("off"))
				{
					Bukkit.getServer().getScheduler().cancelTask(FC_Rpg.tid3);
					Bukkit.getServer().getScheduler().cancelTask(FC_Rpg.tid4);
					
					FC_Rpg.eventExpMultiplier = 1;
					FC_Rpg.eventCashMultiplier = 1;
					
					FC_Rpg.bLib.standardBroadcast("All Bonus Events Have Been Stopped By An Admin!");
				}
			}
			
			//Handle super fast uber teleport command.
			else if (args[0].equalsIgnoreCase("go"))
				teleportAdmin(args);
			
			//List worlds
			else if (args[0].equalsIgnoreCase("listworld") || args[0].equalsIgnoreCase("listworlds"))
			{
				int count = 0;
				String[] msg = new String[2];
				
				//Give a header
				msgLib.standardHeader("World List");
				
				//List all the worlds.
				for (World world : Bukkit.getServer().getWorlds())
				{
					msg[0] = "#" + count + ": ";
					msg[1] = world.getName();
					
					msgLib.standardMessage(msg);
					count++;
				}
			}
			
			//Teleport to different worlds.
			else if (args[0].equalsIgnoreCase("tpworld"))
			{
				//Teleport the player to the world they specify if it exists.
				for (World world : Bukkit.getServer().getWorlds())
				{
					if (world.getName().contains(args[1]))
					{
						Location tpLoc = FC_Rpg.worldConfig.getWorldSpawn(args[1]);
						
						if (tpLoc != null)
							player.teleport(tpLoc);
					}
				}
			}

			/*******************************
			//Owner only commmands.
			*******************************/
			
			//Handle wall generation command that will create a wall with signs!
			else if (args[0].equalsIgnoreCase("wall"))
			{
				if (perms.isOwner())
				{
					createBoard();
					msgLib.standardMessage("Successfully generated wall.");
				}
				else
				{
					return msgLib.helpOwner();
				}
			}
			
			//Handle setting world spawns.
			else if (args[0].equalsIgnoreCase("spawn")) //rpg spawn [here] OR //rpg spawn [worldname] [x] [y] [z] [yaw] [pitch]
			{
				if (perms.isOwner())
				{
					WorldConfig wm = new WorldConfig();
					
					if (args[1].equalsIgnoreCase("here"))
					{
						wm.setWorldSpawn(player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
								player.getLocation().getYaw(), player.getLocation().getPitch());
					}
					else
					{
						//Make sure all arguments are valid.
						for (int i = 1; i < 7; i++)
							if (args[i].equalsIgnoreCase("")) return msgLib.helpOwner();
						
						try
						{
							wm.setWorldSpawn(args[1], Double.valueOf(args[2]), Double.valueOf(args[3]), Double.valueOf(args[4]), Float.valueOf(args[5]), Float.valueOf(args[6]));
						}
						catch (NumberFormatException e)
						{
							return msgLib.errorInvalidCommand();
						}
					}
					
					
					msgLib.standardMessage("Successfully changed world spawn.");
				}
				else
				{
					return msgLib.helpOwner();
				}
			}
			
			else if (args[0].equalsIgnoreCase("levelone"))
			{
				WorldConfig worldConfig = new WorldConfig();
				
				worldConfig.setLevelOne(player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
						player.getLocation().getYaw(), player.getLocation().getPitch());
				
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("tp"))
			{
				if (perms.isOwner() == false)
					return msgLib.errorNoPermission();
				
				if (args[1].equalsIgnoreCase(""))
					return msgLib.errorInvalidCommand();
				
				if (Bukkit.getServer().getPlayer(args[1]) != null)
				{
					player.teleport(Bukkit.getServer().getPlayer(args[1]));
					return msgLib.successCommand();
				}
				else
					return msgLib.errorPlayerNotFound();
			}
			
			else if (args[0].equalsIgnoreCase("sudo"))
			{
				if (perms.isOwner() == false)
					return msgLib.errorNoPermission();
				
				if (args[1].equalsIgnoreCase(""))	//Name
					return msgLib.errorInvalidCommand();
				
				if (args[2].equalsIgnoreCase(""))	//Command to send.
					return msgLib.errorInvalidCommand();
				
				//Variable Declarations
				ArgParser targetArgs = new ArgParser(args);
				targetArgs.setLastArg(2);
				NameMatcher nm = new NameMatcher();
				String name = nm.getNameByMatch(args[1]);
				
				if (Bukkit.getServer().getPlayer(name) != null)
				{
					Bukkit.getServer().getPlayer(name).chat(targetArgs.getFinalArg());
					return msgLib.successCommand();
				}
				else
					return msgLib.errorPlayerNotFound();
			}
			
			//Show admin help.
			else if (args[0].equalsIgnoreCase("admin"))
			{
				return msgLib.helpAdmin();
			}
			
			//Show owner help.
			else if (args[0].equalsIgnoreCase("owner"))
			{
				return msgLib.helpOwner();
			}
			
			//Save configuration
			FC_Rpg.plugin.saveConfig();
			
			//Return true;
			return true;
		}
		
		public void createBoard()
		{
			Location playerLocation = player.getLocation();
			Location blockLocation;
			World playerWorld = player.getWorld();
			Block block;
			Sign sign;
			
			//Store player location
			double x = playerLocation.getX();
			double y = playerLocation.getY();
			double z = playerLocation.getZ();
			
			//CREATE THE WALL
			
			//Set the location for the starting block.
			blockLocation = new Location(playerWorld,x,y,z);
			
			//Move the wall a bit away from the player.
			blockLocation.setZ(playerLocation.getZ() - 4);
			
			//Create a 7x7 wall.
			for (int i = 0; i < 13; i++)
			{
				blockLocation.setX(playerLocation.getX() + i);
				
				for (int j = 0; j < 3; j++) 
				{
					blockLocation.setY(playerLocation.getY() + j);
					
					//Set the block at blockLocation to stone.
					playerWorld.getBlockAt(blockLocation).setType(Material.STONE);
				}
			}
			
			//CREATE THE SIGNS
			
			//Set the location for the starting block.
			blockLocation = new Location(playerWorld,x,y,z);
			
			//Move the wall a bit away from the player.
			blockLocation.setZ(playerLocation.getZ() - 3);
			
			String warpName;
			
			//Create a 7x7 wall.
			for (int i = 0; i < 13; i++)
			{
				blockLocation.setX(playerLocation.getX() + i);
				
				for (int j = 0; j < 3; j++) 
				{
					blockLocation.setY(playerLocation.getY() + j);
					
					//Get the new block.
					block = playerWorld.getBlockAt(blockLocation);
					
					//Set the sign to a wall_sign
					block.setType(Material.WALL_SIGN);
					
					//Set the block state to wall_sign.
					block.getState().setType(Material.WALL_SIGN);
					
					try
					{
						//Store the sign.
						sign = (Sign) block.getState();
						
						//0x2: Facing north / 0x3: Facing south / 0x4: Facing west / 0x5: Facing east
						sign.setRawData((byte) 0x3);
						
						if (j == 0)
						{
							try { warpName = FC_Rpg.warpConfig.getName(i); sign.setLine(0, "Teleport:"); sign.setLine(1, warpName); } catch (NullPointerException e) { continue; }
						}
						
						//Create class sign
						else if (j == 1)
						{
							//Set sign text
							sign.setLine(0, ChatColor.DARK_RED + "Pick Class:");
							
							try { sign.setLine(1, ChatColor.DARK_BLUE + FC_Rpg.classConfig.getRpgClass(i).getName()); }
							catch (ArrayIndexOutOfBoundsException e) { continue; }
						}
						
						//Create finish sign.
						else if (j == 2)
						{
							if (i == 0)
							{
								//Set sign text
								sign.setLine(0, ChatColor.DARK_PURPLE + "Finish!");
								sign.setLine(1, ChatColor.DARK_RED + "And Assign");
								sign.setLine(2, ChatColor.DARK_RED + "Stat Points");
								sign.setLine(3, ChatColor.DARK_RED + "Automatically");
							}
							else if (i == 1)
							{
								//Set sign text
								sign.setLine(0, ChatColor.DARK_PURPLE + "Finish!");
								sign.setLine(1, ChatColor.DARK_RED + "And Assign Stats");
								sign.setLine(2, ChatColor.DARK_RED + "Stat Points");
								sign.setLine(3, ChatColor.DARK_RED + "Manually");
							}
						}
						
						//Update the sign.
						sign.update();
					}
					catch (ClassCastException e)
					{
						
					}
				}
			}
		}
		
		private void teleportAdmin(String args[])
		{
			Location loc = player.getLocation();
			int A1 = -1;
			int A2 = -1;
			int A3 = -1;
			
			//Attempt to convert the input into numbers.
			try { A1 = Integer.valueOf(args[1]); }
			catch (NumberFormatException e) { }
			
			try { A2 = Integer.valueOf(args[2]); }
			catch (NumberFormatException e) { }
			
			try { A3 = Integer.valueOf(args[3]); }
			catch (NumberFormatException e) { }
			
			//Move based on sub-command or if coords are just given.
			if (args[1].equalsIgnoreCase("u") || args[1].equalsIgnoreCase("up"))
			{
				if (A2 == -1)
					loc.setY(loc.getY() + 5);
				else
					loc.setY(loc.getY() + A2);
			}
			else if (args[1].equalsIgnoreCase("d") || args[1].equalsIgnoreCase("down"))
			{
				if (A2 == -1)
					loc.setY(loc.getY() - 5);
				else
					loc.setY(loc.getY() - A2);
			}
			else
			{
				if (A1 > -1)
					loc.setX(A1);
				if (A2 > -1)
					loc.setY(A2);
				if (A3 > -1)
					loc.setZ(A3);
			}
			
			//Teleport the player if they would be going to a new location.
			if (loc != player.getLocation())
				player.teleport(loc);	//Teleport to the location
		}
	}

	public class SpellCE
	{
		SpellConfig util = new SpellConfig();
		
		public SpellCE() { }
		
		public boolean execute()
	    {
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Prevent console from using hat command.
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			//Without an argument, return.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpSpell();
			
			if (args[0].equalsIgnoreCase("bind") || args[0].equalsIgnoreCase("set"))
				return bindSubCommand();
			
			else if (args[0].equalsIgnoreCase("list"))
				return listSubCommand();
			
			else if (args[0].equalsIgnoreCase("upgrade") || args[0].equalsIgnoreCase("promote"))
				return upgradeSubCommand();
			
			else if (args[0].equalsIgnoreCase("reset"))
				return resetSubCommand();
			
			else if (args[0].equalsIgnoreCase("autocast"))
				return autocastSubCommand();
			
			return true;
	    }
		
		private boolean bindSubCommand()
		{
			if (args[1].equalsIgnoreCase(""))
				return msgLib.errorInvalidCommand();
			
			//Variable declaration.
			int intArg1 = getSpellNumber(args[1]);
			
			if (intArg1 < 0)
				return true;
			
			//Remove the item from other spell binds if true.
			for (int i = 0; i < rpgPlayer.getPlayerConfig().getRpgClass().getSpellBook().size(); i++)
			{
				if (rpgPlayer.getPlayerConfig().getSpellBind(i) == player.getItemInHand().getTypeId())
					rpgPlayer.getPlayerConfig().setSpellBind(i, 999);
			}
			
			if (rpgPlayer.getPlayerConfig().getSpellLevel(intArg1) < 1)
			{
				msgLib.standardError("You must level the skill up before you can bind it.");
				return true;
			}
			
			//Set the spell bind.
			rpgPlayer.getPlayerConfig().setSpellBind(intArg1, player.getItemInHand().getTypeId());
			
			//Send a success message to the player.
			msgLib.standardMessage("Successfully bound " + rpgPlayer.getPlayerConfig().getRpgClass().getSpell(intArg1).getName() + " to item: " + player.getItemInHand().getType());
			
			return true;
		}
		
		private boolean listSubCommand()
		{
			msgLib.standardHeader("Spells List");
			
			msgLib.standardMessage("Current Spell Points",String.valueOf(rpgPlayer.getPlayerConfig().getSpellPoints()));
			
			String[] msg = new String[8];
			
			for (int i = 0; i < rpgPlayer.getPlayerConfig().getRpgClass().getSpellBook().size(); i++)
			{
				msg[0] = "[N]: ";
				msg[1] = rpgPlayer.getPlayerConfig().getRpgClass().getSpell(i).getName();
				
				msg[2] = " [L]: ";
				msg[3] = String.valueOf(rpgPlayer.getPlayerConfig().getSpellLevel(i));
				
				if (rpgPlayer.getPlayerConfig().getSpellLevel(i) > 0)
				{
					msg[4] = " [MC]: ";
					msg[5] = String.valueOf(rpgPlayer.getPlayerConfig().getRpgClass().getSpell(i).getManaCost().get(i));
				}
				else
				{
					msg[4] = " [MC]: ";
					msg[5] = "0";
				}
				
				msg[6] = " [D]: ";
				msg[7] = rpgPlayer.getPlayerConfig().getRpgClass().getSpell(i).getDescription();
				
				msgLib.standardMessage(msg);
			}
			
			return true;
		}
		
		private boolean upgradeSubCommand()
		{
			//If the player doesn't have enough spell points tell them.
			if (rpgPlayer.getPlayerConfig().getSpellPoints() < 1 && !perms.isAdmin())
			{
				msgLib.standardError("You don't have enough spell points");
				return true;
			}
			
			//Get the spell number by using the function getSpellNumber().
			int intArg1 = getSpellNumber(args[1]);
			
			//Check to make sure that the spell specialized is proper.
			if (intArg1 < 0 || intArg1 > SpellConfig.SPELL_TIERS)
				return msgLib.errorInvalidCommand();
			
			if (rpgPlayer.getPlayerConfig().getSpellLevel(intArg1) >= 5)
			{
				msgLib.standardError("This skill is already maxed");
				return true;
			}
			
			//Increaese the spell level.
			rpgPlayer.getPlayerConfig().setSpellLevel(intArg1, rpgPlayer.getPlayerConfig().getSpellLevel(intArg1) + 1);
			
			//Decrease player spell points.
			rpgPlayer.getPlayerConfig().setSpellPoints(rpgPlayer.getPlayerConfig().getSpellPoints() - 1);
			
			//Return success.
			return msgLib.successCommand();
		}
		
		private boolean resetSubCommand()
		{
			for (int i = 0; i < rpgPlayer.getPlayerConfig().getRpgClass().getSpellBook().size(); i++)
			{
				if (rpgPlayer.getPlayerConfig().getSpellBind(i) == player.getItemInHand().getTypeId())
					rpgPlayer.getPlayerConfig().setSpellBind(i, 999);
			}
			
			return msgLib.successCommand();
		}
		
		private boolean autocastSubCommand()
		{
			if (args[1].equalsIgnoreCase("on"))
				rpgPlayer.getPlayerConfig().setAutoCast(true);
			else if (args[1].equalsIgnoreCase("off"))
				rpgPlayer.getPlayerConfig().setAutoCast(false);
			else
			{
				if (rpgPlayer.getPlayerConfig().getAutoCast() == false)
					rpgPlayer.getPlayerConfig().setAutoCast(true);
				else
					rpgPlayer.getPlayerConfig().setAutoCast(false);
			}
			
			return msgLib.successCommand();
		}
		
		private int getSpellNumber(String spellArgument)
		{
			int intArg1 = -1;
			int spellCount = rpgPlayer.getPlayerConfig().getRpgClass().getSpellBook().size();
			
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
					if (spellArgument.equalsIgnoreCase(rpgPlayer.getPlayerConfig().getRpgClass().getSpell(i).getName()))
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
	
	public class WarpCE
	{
		WarpConfig wc;
		
		public WarpCE() { }
		
		public boolean execute()
		{
			wc = new WarpConfig();
			
			//One argument commands.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpWarp();
			
			if (args[0].equalsIgnoreCase("list"))
				return listSubCommand();
			
			//Two argument commands.
			if (args[1].equalsIgnoreCase(""))
				return msgLib.helpWarp();
			
			if (args[0].equalsIgnoreCase("info"))
				return infoSubCommand();
			else if (args[0].equalsIgnoreCase("add"))
				return addSubCommand();
			else if (args[0].equalsIgnoreCase("del"))
				return delSubCommand();
			else if (args[0].equalsIgnoreCase("tp"))
				return tpSubCommand();
			
			//Four argument commands.
			if (args[2].equalsIgnoreCase("") || args[3].equalsIgnoreCase(""))
				return msgLib.helpWarp();
			
			if (args[0].equalsIgnoreCase("edit"))
				return editSubCommand();
			
			return true;
		}
		
		private boolean listSubCommand()
		{
			int breakPoint = 0;
			int startPoint = 0;
			int count = 0;
			String[] msg = new String[5];
			
			if (!args[1].equalsIgnoreCase(""))
				try { startPoint = Integer.valueOf(args[1]); } catch (NumberFormatException e) { }
			
			msgLib.standardHeader("Warp List");
			
			for (int i = startPoint; i < 10000; i++)
			{
				if (wc.getName(i) != null)
				{
					msg[0] = "#" + i + ": ";
					msg[1] = "[N]: ";
					msg[2] = wc.getName(i);
					msg[3] = " [D]: ";
					msg[4] = getDestinationString(i);
					
					msgLib.standardMessage(msg);
					
					count++;
					
					//Display 15 warps.
					if (count == count + 15)
						break;
				}
				else
				{
					breakPoint++;
					
					if (breakPoint > 50)
						break;
				}
			}
			
			if (count == 0)
				msgLib.standardError("There are no warps to list.");
			
			return msgLib.successCommand();
		}
		
		private boolean infoSubCommand()
		{
			int id = wc.getWarpIDByName(args[1]);
			
			if (id == -1)
				return msgLib.errorWarpDoesNotExist();
			
			msgLib.standardMessage("Name",wc.getName(id));
			msgLib.standardMessage("Cost",String.valueOf(wc.getCost(id)));
			msgLib.standardMessage("Destination",getDestinationString(id));
			msgLib.standardMessage("Welcome Message",wc.getWelcome(id));
			msgLib.standardMessage("Exit Message",wc.getExit(id));
			msgLib.standardMessage("Donator Only",String.valueOf(wc.getDonator(id)));
			msgLib.standardMessage("Admin Only",String.valueOf(wc.getAdmin(id)));
			
			return true;
		}
		
		// /w add~0 [name]~1
		private boolean addSubCommand()
		{
			for (int i = 0; i < 10000; i++)
			{
				if (wc.getName(i) == null)
					wc.setName(i, args[1]);
			}
			
			return msgLib.successCommand();
		}
		
		private boolean delSubCommand()
		{
			int id = wc.getWarpIDByName(args[1]);
			
			if (id == -1)
				return msgLib.errorWarpDoesNotExist();
			
			wc.setNull(id);
			
			return msgLib.successCommand();
		}
		
		private boolean tpSubCommand()
		{
			int id = wc.getWarpIDByName(args[1]);
			
			if (id == -1)
				return msgLib.errorWarpDoesNotExist();
			
			player.teleport(wc.getDestination(id));
			
			return msgLib.successCommand();
		}
		
		// /w edit~0 [name]~1 [modifiable]~2 [new value]~3
		private boolean editSubCommand()
		{
			int id = wc.getWarpIDByName(args[1]);
			
			if (id == -1)
				return msgLib.errorWarpDoesNotExist();
			
			ArgParser ap = new ArgParser(args);
			ap.setLastArg(3);
			
			if (args[2].equalsIgnoreCase("name"))
				wc.setName(id, args[3]);
			else if (args[2].equalsIgnoreCase("description"))
				wc.setDescription(id, ap.getFinalArg());
			else if (args[2].equalsIgnoreCase("cost"))
				try { wc.setCost(id, Double.valueOf(args[3])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
			else if (args[2].equalsIgnoreCase("welcome"))
				wc.setName(id, ap.getFinalArg());
			else if (args[2].equalsIgnoreCase("exit"))
				wc.setName(id, ap.getFinalArg());
			else if (args[2].equalsIgnoreCase("donator")) //test to see what error I get.
				try { wc.setDonator(id, Boolean.valueOf(args[3])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
			else if (args[2].equalsIgnoreCase("admin"))
				try { wc.setAdmin(id, Boolean.valueOf(args[3])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
			else
				return msgLib.helpWarp();
			
			return msgLib.successCommand();
		}
		
		private String getDestinationString(int warpNumber)
		{
			DecimalFormat df = new DecimalFormat("#.#");
			
			return df.format(wc.getDestination(warpNumber).getX()) + ", " + df.format(wc.getDestination(warpNumber).getY()) + ", " + df.format(wc.getDestination(warpNumber).getZ());
		}
	}
	
	public class BuffCE
	{
		Map<String, PotionEffect> peMap;
		
		public BuffCE() {  }
		
		public boolean execute()
		{
			initializePEMap();
			
			if (args[0].equals(""))
				return msgLib.helpBuff();
			
			if (args[0].equalsIgnoreCase("self"))
				return selfSubCommand();
			
			else if (args[0].equalsIgnoreCase("all"))
				return allSubCommand();
			
			else if (args[0].equalsIgnoreCase("clear"))
				return clearSubCommand();
			
			return msgLib.errorInvalidCommand();
		}
		
		private void initializePEMap()
		{
			peMap = new HashMap<String, PotionEffect>();
			
			peMap.put("jump", new PotionEffect(PotionEffectType.JUMP, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("speed", new PotionEffect(PotionEffectType.SPEED, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("regeneration", new PotionEffect(PotionEffectType.REGENERATION, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("fireresistance", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, getRandomDuration(), getRandomPotionStrength()));
			peMap.put( "fastdigging", new PotionEffect(PotionEffectType.FAST_DIGGING, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("increasedamage", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("damageresistance", new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, getRandomDuration(), getRandomPotionStrength()));
			peMap.put( "heal", new PotionEffect(PotionEffectType.HEAL, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("invisibility", new PotionEffect(PotionEffectType.INVISIBILITY, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("nightvision", new PotionEffect(PotionEffectType.NIGHT_VISION, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("waterbreathing", new PotionEffect(PotionEffectType.WATER_BREATHING, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("slow", new PotionEffect(PotionEffectType.SLOW, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("blindness", new PotionEffect(PotionEffectType.BLINDNESS, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("poison", new PotionEffect(PotionEffectType.POISON, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("confusion", new PotionEffect(PotionEffectType.CONFUSION, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("hunger", new PotionEffect(PotionEffectType.HUNGER, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("harm", new PotionEffect(PotionEffectType.HARM, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("slowdigging", new PotionEffect(PotionEffectType.SLOW_DIGGING, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("weakness", new PotionEffect(PotionEffectType.WEAKNESS, getRandomDuration(), getRandomPotionStrength()));
		}
		
		private boolean selfSubCommand()
		{
			PotionEffect pickedPE = getRandomPotionEffect();
			
			//Lets admin put potions on theirself.
			if (perms.isAdmin() == true && !args[1].equals("") && !args[2].equals("") && !args[3].equals(""))
				try { pickedPE = new PotionEffect(PotionEffectType.getByName(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3])); } catch (NumberFormatException e) { }
			
			//Add the potion effect.
			player.addPotionEffect(pickedPE);
			
			return msgLib.successCommand();
		}
		
		public boolean allSubCommand()
		{
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
			for (Player p : Bukkit.getServer().getOnlinePlayers())
				p.addPotionEffect(getRandomPotionEffect());
			
			msgLib.standardBroadcast("Everybody has been given a random (de)buff!");
			
			return true;
		}
		
		private boolean clearSubCommand()
		{
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
			Player target = player;
			
			if (!args[1].equals(""))
			{
				if (Bukkit.getServer().getPlayer(args[1]) != null)
					target = Bukkit.getServer().getPlayer(args[1]);
			}
			
			for (PotionEffect p : target.getActivePotionEffects()) 
				target.removePotionEffect(p.getType());
			
			return msgLib.standardMessage("Successfully cleared all buffs on player &p" + target.getName() + "&p!");
		}
		
		private PotionEffect getRandomPotionEffect()
		{
			Iterator<PotionEffect> itr = peMap.values().iterator();
			Random rand = new Random();
			int stoppingPoint = rand.nextInt(peMap.values().size());
			
			for (int i = 0; i < stoppingPoint - 1; i++)
				itr.next();
			
			return itr.next();
		}
		
		private int getRandomDuration()
		{
			Random rand = new Random();
			return 1200 * (rand.nextInt(2) + 1);
		}
		
		private int getRandomPotionStrength()
		{
			Random rand = new Random();
			return rand.nextInt(3) + 1;	//rand.nextInt((maxStrength - 1)) + 1;
		}
	}
}











/*

*/



























