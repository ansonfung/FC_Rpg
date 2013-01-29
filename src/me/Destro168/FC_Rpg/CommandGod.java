package me.Destro168.FC_Rpg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.FC_Rpg.Configs.DungeonConfig;
import me.Destro168.FC_Rpg.Configs.FaqConfig;
import me.Destro168.FC_Rpg.Configs.GroupConfig;
import me.Destro168.FC_Rpg.Configs.PlayerConfig;
import me.Destro168.FC_Rpg.Configs.SpellConfig;
import me.Destro168.FC_Rpg.Configs.WarpConfig;
import me.Destro168.FC_Rpg.Configs.WorldConfig;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.Events.DungeonEvent;
import me.Destro168.FC_Rpg.Listeners.PlayerInteractionListener;
import me.Destro168.FC_Rpg.LoadedObjects.Group;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.LoadedObjects.RpgItem;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
import me.Destro168.FC_Rpg.Spells.SpellEffect;
import me.Destro168.FC_Rpg.Spells.SpellCaster;
import me.Destro168.FC_Rpg.Util.FC_RpgPermissions;
import me.Destro168.FC_Rpg.Util.RpgMessageLib;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.FC_Suite_Shared.TimeUtils.DateManager;

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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandGod implements CommandExecutor
{
	private CommandSender sender;
	private ConsoleCommandSender console;
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
			msgLib.standardError("Command failed to initialize.");
			return true;
		}
		if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordFaq()))
		{
			FaqCE cmd = new FaqCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordPlayers()))
		{
			ListCE cmd = new ListCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordReset()))
		{
			ResetCE cmd = new ResetCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordRpg()))
		{
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpRpg();
			else if (args[0].equalsIgnoreCase("help"))
				return msgLib.displayRpgHelp(args[1]);
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordRAdmin()))
		{
			RAdminCE cmd = new RAdminCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordHead()))
		{
			HatCE cmd = new HatCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordRealm()))
		{
			RealmCE cmd = new RealmCE();
			return cmd.execute();
		}
		
		//Check if the player is active for future commands.
		if (isActive == false)
			return msgLib.errorCreateCharacter();
		
		if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordClass()))
		{
			ClassCE cmd = new ClassCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordDonator()))
		{
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			DonatorCE cmd = new DonatorCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordDungeon()))
		{
			DungeonCE cmd = new DungeonCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordDE()))
		{
			DungeonCE cmd = new DungeonCE();
			return cmd.executeDE();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordG()) || command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordH()) || command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordGH()) || command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordHG()))
		{
			QuickCE cmd = new QuickCE();
			return cmd.execute(command.getName());
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordJob()))
		{
			JobCE cmd = new JobCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordGuild()))
		{
			GuildCE cmd = new GuildCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordPvp()))
		{
			PvpCE cmd = new PvpCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordSpell()))
		{
			SpellCE cmd = new SpellCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordAlchemy()))
		{
			AlchemyCE cmd = new AlchemyCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordModify()))
		{
			ModifyCE cmd = new ModifyCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordW()))
		{
			WarpCE cmd = new WarpCE();
			return cmd.execute();
		}
		else if (command.getName().equalsIgnoreCase(FC_Rpg.generalConfig.getCommandKeyWordBuff()))
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
			rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(player);
			perms = new FC_RpgPermissions(player);
			msgLib = new RpgMessageLib(sender_, player);
			
			try { isActive = rpgPlayer.playerConfig.getIsActive(); } catch (NullPointerException e) { isActive = false; }
			
			console = null;
		}
		else if (sender instanceof ConsoleCommandSender)
		{
			console = (ConsoleCommandSender) sender;
			perms = new FC_RpgPermissions(true);
			msgLib = new RpgMessageLib(sender);
			isActive = true;
			player = null;
		}
		else
		{
			FC_Rpg.plugin.getLogger().info("Unknown command sender, returning rpg command.");
			return false;
		}
		
		return true;
	}
	
	public class FaqCE
	{
		FaqConfig fc = new FaqConfig();
		
		public FaqCE() { }
		
		public boolean execute()
	    {
			if (!perms.commandFAQ())
				return msgLib.errorNoPermission();
			
			if (perms.isAdmin())
			{
				if (args[0].equalsIgnoreCase("new"))
					return newSubCommand();
				else if (args[0].equalsIgnoreCase("del"))
					return delSubCommand();
				else if (args[0].equalsIgnoreCase("eProperty"))
					return editPropertySubCommand();
				else if (args[0].equalsIgnoreCase("eLine"))
					return editLineSubCommand();
			}
			
			return displaySubTopic();
	    }
		
		// /faq new [displayTag] 
		private boolean newSubCommand()
		{
			if (args[1].equalsIgnoreCase(""))
				return msgLib.errorInvalidCommand();
			
			fc.addNewFaq(args[1]);
			fc.editFaqProperties(args[1], "header", args[1]);
			fc.editFaqProperties(args[1], "displayTag", args[1]);
			fc.editFaqLines(args[1], 0, 1, "Example");
			fc.editFaqLines(args[1], 0, 2, "Example");
			
			return msgLib.successCommand();
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
	
	public class ClassCE
	{
		public ClassCE() { }
		
		public boolean execute()
	    {
			if (!perms.commandClass())
				return msgLib.errorNoPermission();
			
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
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
			String totalKeyWord = " // Total: ";
			
			//Check to see if we are viewing somebody elses's file
			if (!args[1].equalsIgnoreCase(""))
			{
				if (!perms.commandClassViewOther())
					return msgLib.errorNoPermission();
				
				//If so load the file
				rpgPlayerFile = new PlayerConfig(args[1]);
				
				//Also set the rpgPlayer.
				if (Bukkit.getServer().getPlayer(args[1]) != null)
					rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
				else
					rpgPlayer = null;
			}
			else
			{
				if (console != null)
					return msgLib.standardError("The Console Must Enter A Player Name To Use This Command.");
				
				//If so load the file
				rpgPlayerFile = rpgPlayer.playerConfig;
			}
			
			//If rpgPlayer isn't null, then...
			if (rpgPlayer != null)
				rpgPlayer.updateTimePlayed(); //update time played.
			
			msgLib.standardMessage("");
			
			//Begin displaying stats.
			msgLib.standardHeader(rpgPlayerFile.getName() + " The Level " + String.valueOf(rpgPlayerFile.getClassLevel()) + " " + FC_Rpg.classConfig.getRpgClass(rpgPlayerFile.getCombatClass()).getName());
			
			msgLib.standardMessage("Time Played",String.valueOf(dm.getTimeStringFromTimeInteger(rpgPlayerFile.getSecondsPlayed())));
			msgLib.standardMessage("Lifetime Mob Kills",String.valueOf(rpgPlayerFile.getLifetimeMobKills()));
			
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
				
				curHealth = FC_Rpg.df.format(rpgPlayer.playerConfig.curHealth);
				maxHealth = FC_Rpg.df.format(rpgPlayer.playerConfig.maxHealth);
				curMana = FC_Rpg.df.format(rpgPlayer.playerConfig.curMana);
				maxMana = FC_Rpg.df.format(rpgPlayer.playerConfig.maxMana);
			}
			else
			{
				curHealth = FC_Rpg.df.format(rpgPlayerFile.getCurHealthFile());
				maxHealth = FC_Rpg.df.format(rpgPlayerFile.getMaxHealthFile());
				curMana = FC_Rpg.df.format(rpgPlayerFile.getCurManaFile());
				maxMana = FC_Rpg.df.format(rpgPlayerFile.getMaxManaFile());
			}
			
			//Expereience and mana.
			String[] p = rpgPlayerFile.getRemainingX(Double.valueOf(curHealth),Double.valueOf(maxHealth),0);
			
			msgLib.infiniteMessage("Health: (",
					p[0],p[1],p[2],p[3],p[4],p[5]);
			
			p = rpgPlayerFile.getRemainingX(Double.valueOf(curMana),Double.valueOf(maxMana),1);
			
			msgLib.infiniteMessage("Mana: (",
					p[0],p[1],p[2],p[3],p[4],p[5]);
			
			//Class experience and stat points.
			msgLib.infiniteMessage("Class Experience: ",FC_Rpg.df.format(rpgPlayerFile.getClassExperience())," of ",
					FC_Rpg.df.format(rpgPlayerFile.getLevelUpAmount()),
					" (",FC_Rpg.df.format(100 - rpgPlayerFile.getRequiredExpPercent()) + "%"," To Next Level)");
			msgLib.standardMessage("Stat points",String.valueOf(rpgPlayerFile.getStats()));
			
			//Stats
			if (rpgPlayerFile.isDonator())
				msgLib.standardHeader("Stats ~ " + FC_Rpg.generalConfig.getDonatorBonusStatPercent() * 100 + "% Donator Bonus!");
			else
				msgLib.standardHeader("Stats");
			
			msgLib.standardMessage("Attack - ",attackDisplay);
			msgLib.standardMessage("Constitution - ",constDisplay);
			msgLib.standardMessage("Magic - ",magicDisplay);
			msgLib.standardMessage("Intelligence - ",intelligenceDisplay);
			
			//Display what the manual allocation setting is currently.
			if (rpgPlayerFile.getManualAllocation() == true)
				msgLib.standardMessage("Note: Your stats are assigned by the server.");
			else
				msgLib.standardMessage("Note: You distribute your stat points.");
			
			return true;
		}
		
		//Allow a player to list server classes.
		private boolean listSubCommand()
		{
			RpgClass[] classes = FC_Rpg.classConfig.getRpgClasses();
			
			msgLib.standardHeader("Server Classes List");
			
			for (int i = 0; i < classes.length; i++)
				msgLib.standardMessage("#" + (i+1),classes[i].getName());
			
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
					rpgPlayer.playerConfig.setAutomaticAllocation(false);
					return true;
				}
				else if (args[1].equalsIgnoreCase("off"))
				{
					msgLib.standardMessage("Auto stat allocation disabled.");
					rpgPlayer.playerConfig.setAutomaticAllocation(true);
					return true;
				}
			}

			if (rpgPlayer.playerConfig.getManualAllocation() == true)
			{
				msgLib.standardMessage("Auto stat allocation disabled.");
				rpgPlayer.playerConfig.setAutomaticAllocation(false);
				return true;
			}
			else
			{
				msgLib.standardMessage("Auto stat allocation enabled.");
				rpgPlayer.playerConfig.setAutomaticAllocation(true);
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
			return rpgPlayer.switchClass(args[1]);
		}
		
		private List<String> getMessageList(String p1, int p2)
		{
			List<String> messageList = new ArrayList<String>();
			
			messageList.add(p1);
			messageList.add(String.valueOf(p2));
			
			return messageList;
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
			
			//Check for command permission.
			if (!perms.commandDonator())
				return msgLib.errorNoPermission();
			
			//Check if they are a donator to use the command.
			if (rpgPlayer.playerConfig.isDonator() == false)
				return msgLib.errorNoPermission();
			
			if (args[0].equalsIgnoreCase(""))
			{
				//Display the players donation information.
				msgLib.standardHeader("Donator Information");
				msgLib.standardMessage("Thank you for donating!");
				
				String timeRemaining;
				
				if (perms.isInfiniteDonator())
					timeRemaining = "Never";
				else
					timeRemaining = FC_Rpg.sdf.format(rpgPlayer.playerConfig.getDonatorTime());
				
				msgLib.standardMessage("Donation Perks End On",timeRemaining);
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
		
		public boolean execute()
	    {
			//Only admins and console can use dungeon command.
			if (perms.isAdmin() == false)
				return msgLib.errorNoPermission();
			
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpDungeon();
			
			//Set the dungeon number.
			if (!args[1].equalsIgnoreCase(""))
				try { dungeonNumber = Integer.valueOf(args[1]); updateDungeonInfo(dungeonNumber); } catch (NumberFormatException e) { }
			
			if (args[0].equalsIgnoreCase("list"))
			{
				String[] msg = new String[5];
				msgLib.standardHeader("Dungeon List");
				
				for (int i = 0; i < FC_Rpg.dungeonEventArray.length; i++)
				{
					updateDungeonInfo(i);
					
					msg[0] = "#" + (i) + ": ";
					msg[1] = "[N]: ";
					msg[2] = dungeon.getDungeonName();
					msg[3] = " [S]: ";
					msg[4] = dungeon.isHappening() + "";
					
					msgLib.standardMessage(msg);
				}
				
				return true;
			}
			
			else if (args[0].equalsIgnoreCase("print"))
			{
				//Console can't use print command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				msgLib.standardHeader("Location Information");
				msgLib.displayLocation("Selection 1",FC_Rpg.sv.getBlockLoc1(player));
				msgLib.displayLocation("Selection 2",FC_Rpg.sv.getBlockLoc2(player));
				msgLib.displayLocation("Your Location",player.getLocation());
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("init"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				dungeon.initialize(dungeonNumber);
				return msgLib.successCommand();
			}
			else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("end"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				dungeon.end(false);
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
			
			return msgLib.errorInvalidCommand();
	    }
		
		public boolean executeDE()
		{
			//Only admins and console can use dungeon command.
			if (perms.isAdmin() == false)
				return msgLib.errorNoPermission();
			
			// 0 argument commands.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpDungeonDE();
			
			//Set the dungeon number.
			if (!args[1].equalsIgnoreCase(""))
				try { dungeonNumber = Integer.valueOf(args[1]); updateDungeonInfo(dungeonNumber); } catch (NumberFormatException e) { }
			
			// 1 argument commands.
			if (args[0].equalsIgnoreCase("new"))
			{
				FC_Rpg.dungeonConfig.addNewDungeon(args[1]);
				FC_Rpg.reloadDungeons();
				return msgLib.successCommand();
			}
			
			DungeonConfig dc = FC_Rpg.dungeonConfig;
			
			//Commands that require an existing dungeon.
			updateDungeonInfo(dungeonNumber);
			
			//If the dungeon number is -1 we return.
			if (dungeonNumber == -1)
				return msgLib.errorBadInput();
			
			if (args[0].equalsIgnoreCase("delete"))
			{
				dc.removeDungeon(dungeonNumber);
				return msgLib.successCommand();
			}
			else if (args[0].equalsIgnoreCase("info"))
			{
				if (args[2].equalsIgnoreCase("spawnBox"))
				{
					int breakPoint = 0;
					Location sb1;
					Location sb2;
					
					msgLib.standardHeader("Spawn Box List");
					
					for (int i = 0; i < 99999; i++)
					{
						sb1 = dc.getSpawnBox1(dungeonNumber, i);
						
						if (sb1 != null)
						{
							sb2 = dc.getSpawnBox2(dungeonNumber, i);
							msgLib.infiniteMessage("#" + i + ":"," sb1: ",getLocationDisplayNormal(sb1)," sb2: ",
									getLocationDisplayNormal(sb2)," spawnChance: ",dc.getSpawnChance(dungeonNumber, i) + "",
									" mobSpawnList: ",dc.getMobList(dungeonNumber, i).toString());
						}
						else
						{
							breakPoint++;
							
							if (breakPoint >= 50)
								break;
						}
					}
					
					msgLib.standardMessage("Finished Listing");
				}
				else if (args[2].equalsIgnoreCase("treasureStart"))
				{
					msgLib.standardHeader("Treasure Start List");
					
					List<Location> treasureStartList = dc.getTreasureStart(dungeonNumber);
					
					for (int i = 0; i < treasureStartList.size(); i++)
						msgLib.infiniteMessage("#" + i + ":"," ts: ",getLocationDisplayNormal(treasureStartList.get(i)));
					
					msgLib.standardMessage("Finished Listing");
				}
				else if (args[2].equalsIgnoreCase("treasureEnd"))
				{
					msgLib.standardHeader("Treasure End List");
					
					List<Location> treasureEndList = dc.getTreasureEnd(dungeonNumber);
					
					for (int i = 0; i < treasureEndList.size(); i++)
						msgLib.infiniteMessage("#" + i + ":"," te: ",getLocationDisplayNormal(treasureEndList.get(i)));
					
					msgLib.standardMessage("Finished Listing");
				}
				else
				{
					msgLib.standardHeader("The Dungeon: " + FC_Rpg.dungeonEventArray[dungeonNumber].getDungeonName());
					msgLib.standardMessage("Entry Fee",String.valueOf(dc.getEntryFee(dungeonNumber)));
					msgLib.infiniteMessage("Join Timer: ",String.valueOf(dc.getTimerJoin(dungeonNumber)),
							" End Timer: ",String.valueOf(dc.getTimerEnd(dungeonNumber)));
					msgLib.infiniteMessage("Level Requirement Min: ",String.valueOf(dc.getPlayerLevelRequirementMinimum(dungeonNumber)),
							" Max: ",String.valueOf(dc.getPlayerLevelRequirementMaximum(dungeonNumber)));
					msgLib.standardMessage("Mobs To Spawn",String.valueOf(dc.getMobsToSpawnCount(dungeonNumber)));
					msgLib.standardMessage("Lobby",getLocationDisplayNormal(dc.getLobby(dungeonNumber)));
					msgLib.standardMessage("Start",getLocationDisplayNormal(dc.getStart(dungeonNumber)));
					msgLib.standardMessage("Exit",getLocationDisplayNormal(dc.getExit(dungeonNumber)));
					msgLib.standardMessage("Boss Spawn",getLocationDisplayNormal(dc.getBossSpawn(dungeonNumber)));
					msgLib.standardHeader("Current State");
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
						
						msgLib.standardMessage("Participants",names);
					}
					else
						msgLib.standardMessage("Participants","Currently Empty.");
				}
				
				return true;
			}
			
			// Commands can't be used by console
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			Location loc1 = FC_Rpg.sv.getBlockLoc1(player);
			
			//de spawnbox [num] <add, remove [num], swap [num1] [num2], spawnChance, mobList <"default"> 
			if (args[0].equalsIgnoreCase("spawnBox"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				if (args[2].equalsIgnoreCase("new") || (args[2].equalsIgnoreCase("add")))
				{
					Location loc2 = FC_Rpg.sv.getBlockLoc2(player);
					int index;
					
					if (loc1 == null || loc2 == null)
						return msgLib.errorInvalidSelectionNoPoints();
					
					index = dc.addToSpawnBox1(dungeonNumber, loc1);
					dc.setSpawnBox2(dungeonNumber, index, loc2);
				}
				else if (args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("delete"))
				{
					int s1;
					
					try { s1 = Integer.valueOf(args[3]); }
					catch (NumberFormatException e) { return msgLib.errorBadInput(); }
					
					dc.setSpawnBoxesNull(dungeonNumber, s1);
				}
				else if (args[2].equalsIgnoreCase("spawnChance"))
				{
					int s1;
					int s2;
					
					try { s1 = Integer.valueOf(args[3]); s2 = Integer.valueOf(args[4]); }
					catch (NumberFormatException e) { return msgLib.errorBadInput(); }
					
					dc.setSpawnChance(dungeonNumber, s1, s2);
				}
				else if (args[2].equalsIgnoreCase("mobList"))
				{
					List<String> mobs = new ArrayList<String>();
					int s1;
					
					try { s1 = Integer.valueOf(args[3]); }
					catch (NumberFormatException e) { return msgLib.errorBadInput(); }
					
					if (args[4].equalsIgnoreCase("default"))
					{
						mobs.add("ZOMBIE");
						mobs.add("PIGZOMBIE");
						mobs.add("SKELETON");
						mobs.add("SPIDER");
					}
					else
					{
						for (int i = 4; i < args.length; i++)
							mobs.add(args[i]);
					}
					
					dc.setMobList(dungeonNumber,s1,mobs);
				}
				else if (args[2].equalsIgnoreCase("swap"))
				{
					Location sb11;
					Location sb12;
					Location sb21;
					Location sb22;
					int s1;
					int s2;
					
					try
					{
						s1 = Integer.valueOf(args[3]);
						s2 = Integer.valueOf(args[4]);
					}
					catch (NumberFormatException e)
					{
						return msgLib.errorBadInput();
					}
					
					sb11 = dc.getSpawnBox1(dungeonNumber,s1);
					sb12 = dc.getSpawnBox2(dungeonNumber,s1);
					sb21 = dc.getSpawnBox1(dungeonNumber,s2);
					sb22 = dc.getSpawnBox2(dungeonNumber,s2);
					
					if (sb11 != null)
					{
						dc.setSpawnBox1(dungeonNumber, s2, sb11);
						dc.setSpawnBox2(dungeonNumber, s2, sb12);
					}
					
					if (sb21 != null)
					{
						dc.setSpawnBox1(dungeonNumber, s2, sb21);
						dc.setSpawnBox2(dungeonNumber, s2, sb22);
					}
				}
				else
				{
					return msgLib.errorInvalidCommand();
				}
				
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("lobby"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				dc.setLobby(dungeonNumber, loc1);
				return msgLib.successCommand();
			}

			else if (args[0].equalsIgnoreCase("playerStart"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				dc.setStart(dungeonNumber, loc1);
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("playerExit"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				dc.setExit(dungeonNumber, loc1);
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("bossSpawn"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				dc.setBossSpawn(dungeonNumber, loc1);
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("timerJoin"))
			{
				try {
					dc.setTimerJoin(dungeonNumber, Integer.valueOf(args[2]));
					return msgLib.successCommand();
				}
				catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
			}
			
			else if (args[0].equalsIgnoreCase("timerEnd"))
			{
				try {
					dc.setTimerEnd(dungeonNumber, Integer.valueOf(args[2]));
					return msgLib.successCommand();
				}
				catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
			}
			
			//de treasureStart [num] <add, remove [num]> 
			else if (args[0].equalsIgnoreCase("treasureStart"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				if (args[2].equalsIgnoreCase("add"))
					dc.addToTreasureStart(dungeonNumber, loc1);
				else if (args[2].equalsIgnoreCase("remove"))
				{
					if (args[3].equalsIgnoreCase(""))
						return msgLib.errorBadInput();
					
					try { dc.setTreasureStartNull(dungeonNumber, Integer.valueOf(args[3])); } catch (NumberFormatException e) { return msgLib.errorBadInput(); }
				}
				
				return msgLib.successCommand();
			}
			
			//de treasureEnd [num] <add, remove [num]> 
			else if (args[0].equalsIgnoreCase("treasureEnd"))
			{
				if (loc1 == null)
					return msgLib.errorInvalidSelectionOnePoint();
				
				if (args[2].equalsIgnoreCase("add"))
					dc.attToTreasureEnd(dungeonNumber, loc1);
				else if (args[2].equalsIgnoreCase("remove"))
				{
					if (args[3].equalsIgnoreCase(""))
						return msgLib.errorBadInput();
					
					try { dc.setTreasureEndNull(dungeonNumber, Integer.valueOf(args[3])); } catch (NumberFormatException e) { return msgLib.errorBadInput(); }
				}
				
				return msgLib.successCommand();
			}
			else if (args[0].equalsIgnoreCase("name"))
			{
				if (dungeonNumber == -1)
					return msgLib.errorInvalidCommand();
				
				FC_Rpg.dungeonConfig.setName(dungeonNumber, args[2]);
				return msgLib.successCommand();
			}
			
			// Commands that require a second part.
			if (args[2].equalsIgnoreCase(""))
				return msgLib.helpDungeon();
			
			if (args[0].equalsIgnoreCase("fee"))
			{
				try
				{
					dc.setEntryFee(dungeonNumber, Double.valueOf(args[2]));
				} catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
				
				return msgLib.successCommand();
			}

			else if (args[0].equalsIgnoreCase("lmin"))
			{
				try
				{
					dc.setPlayerLevelRequirementMinimum(dungeonNumber, Integer.valueOf(args[2]));
				} catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
				
				return msgLib.successCommand();
			}

			else if (args[0].equalsIgnoreCase("lmax"))
			{
				try
				{
					dc.setPlayerLevelRequirementMaximum(dungeonNumber, Integer.valueOf(args[2]));
				} catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
				
				return msgLib.successCommand();
			}

			else if (args[0].equalsIgnoreCase("spawncount"))
			{
				try
				{
					dc.setMobsToSpawnCount(dungeonNumber, Integer.valueOf(args[2]));
				} catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
				
				return msgLib.successCommand();
			}
			
			// Commands that require a third part.
			if (args[3].equalsIgnoreCase(""))
				return msgLib.helpDungeonDE();
			else if (args[0].equalsIgnoreCase("spawnchance"))
			{
				try
				{
					dc.setSpawnChance(dungeonNumber, Integer.valueOf(args[2]), Integer.valueOf(args[3]));
				} catch (NumberFormatException e)	{ return msgLib.errorInvalidCommand(); }
			}
			else
				return msgLib.helpDungeonDE();
			
			//Dungeon help.
			return msgLib.successCommand();
		}
		
		private String getLocationDisplayNormal(Location loc)
		{
			String normal = "";
			
			normal = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getY() + ", " + 
					loc.getZ() + ", " + loc.getPitch() + ", " + loc.getYaw();
			
			return normal;
		}
		private boolean updateDungeonInfo(int newDungeonNumber)
		{
			//Convert dungeon number.
			dungeonNumber = newDungeonNumber;
			
			if (!FC_Rpg.trueDungeonNumbers.containsKey(dungeonNumber))
			{
				dungeonNumber = -1;
				return false;
			}
			
			//Store dungeon for convenient use.
			dungeon = FC_Rpg.dungeonEventArray[FC_Rpg.trueDungeonNumbers.get(dungeonNumber)];
			
			return true;
		}
	}
	
	public class QuickCE
	{
		public boolean execute(String commandName)
		{
			//Deny console access to this command.
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
			//Variable Declaration
			boolean useG = false;
			boolean useH = false;
			
			//Evaluate what the player wants to use and ensure that the player has permission.
			if (commandName.equalsIgnoreCase("g"))
				useG = true;
			
			else if (commandName.equalsIgnoreCase("h"))
				useH = true;
			
			else if ((commandName.equalsIgnoreCase("gh") || commandName.equalsIgnoreCase("hg")))
			{
				useG = true;
				useH = true;
			}
			
			else
				return msgLib.errorNoPermission();
			
			//Make sure player is online.
			if (args[0].equalsIgnoreCase(""))
				args[0] = player.getName();
			
			if (Bukkit.getServer().getPlayer(args[0]) == null)
				return msgLib.errorPlayerNotOnline();
			
			//Execute what was set to true.
			if (useG)
				quick_Gamemode();
			
			if (useH)
				quick_Heal();
			
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
			FC_Rpg.rpgEntityManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[0])).healFull();
			
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
			
			//Variable Declaration
			boolean canHat = false;
			
			//If donators can hat, then check if the player is donator.
			if (FC_Rpg.generalConfig.getDonatorsCanHat())
			{
				if (rpgPlayer != null)
					canHat = rpgPlayer.playerConfig.isDonator();
			}
			
			//If they can't hat or aren't a donator, then check if they have perm.
			if (canHat == false)
				canHat = perms.commandHead();
			
			if (canHat)
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
			if (!perms.commandJob())
				return msgLib.errorNoPermission();
			
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
						rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
						playerFile = rpgPlayer.playerConfig;
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
					
					playerFile = rpgPlayer.playerConfig;
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
				if (rpgPlayer.playerConfig.getJobRank() < 6)
				{
					//Make sure that players can only be promoted with the proper job rank.
					if (rpgPlayer.playerConfig.getClassLevel() < 20 && rpgPlayer.playerConfig.getJobRank() == 1)
					{
						msgLib.standardError("You need level 20+ for promotion.");
						return true;
					}
					else if (rpgPlayer.playerConfig.getClassLevel() < 40 && rpgPlayer.playerConfig.getJobRank() == 2)
					{
						msgLib.standardError("You need level 40+ for promotion.");
						return true;
					}
					else if (rpgPlayer.playerConfig.getClassLevel() < 60 && rpgPlayer.playerConfig.getJobRank() == 3)
					{
						msgLib.standardError("You need level 60+ for promotion.");
						return true;
					}
					else if (rpgPlayer.playerConfig.getClassLevel() < 80 && rpgPlayer.playerConfig.getJobRank() == 4)
					{
						msgLib.standardError("You need level 80+ for promotion.");
						return true;
					}
					else if (rpgPlayer.playerConfig.getClassLevel() < 100 && rpgPlayer.playerConfig.getJobRank() == 5)
					{
						msgLib.standardError("You need level 100 for the final promotion. Good luck :)");
						return true;
					}
					
					//If the player can afford a promotion
					if (FC_Rpg.economy.getBalance(player.getName()) > rpgPlayer.playerConfig.getPromotionCost())
					{
						//Take away money from the player.
						FC_Rpg.economy.bankWithdraw(player.getName(), rpgPlayer.playerConfig.getPromotionCost());
						
						//Give them the promotion
						rpgPlayer.playerConfig.setJobRank(rpgPlayer.playerConfig.getJobRank() + 1);
						
						//Announce the promotion
						FC_Rpg.rpgBroadcast.rpgBroadcast(player.getName() + " is now Job Rank [" + rpgPlayer.playerConfig.getJobRank() + "]");
					}
					else
					{
						msgLib.standardError("You need $" + String.valueOf(rpgPlayer.playerConfig.getPromotionCost() - FC_Rpg.economy.getBalance(player.getName())) + " for promotion");
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
		public ListCE()  { }
		
		public boolean execute()
	    {
			if (!perms.commandPlayers())
				return msgLib.errorNoPermission();
			
			//Variable Declarations
			GroupConfig conf = new GroupConfig();
			List<Group> groups = conf.getGroups();
			List<Player> groupMembers = new ArrayList<Player>();
			Player[] onlinePlayersArray = Bukkit.getServer().getOnlinePlayers();
			Map<Integer, Player> onlinePlayersMap = new HashMap<Integer, Player>();
			String message = "";
			
			for (int i = 0; i < onlinePlayersArray.length; i++) 
				onlinePlayersMap.put(i,onlinePlayersArray[i]);
			
			//Begin displaying the information.
			msgLib.standardHeader("Total Connected Players: " + ChatColor.GREEN + ChatColor.BOLD + (Bukkit.getOnlinePlayers().length - FC_Rpg.vanishedPlayers.size()) + ChatColor.GRAY + ChatColor.BOLD + "/" +
					ChatColor.RED + ChatColor.BOLD + Bukkit.getServer().getMaxPlayers());
			
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
							groupMembers.add(onlinePlayersMap.get(j));
							onlinePlayersMap.remove(j);
						}
					}
				}
				
				if (groupMembers.size() > 0)
				{
					String conversion = specialConversion(groupMembers);
					
					if (!conversion.equals(""))
						message += groups.get(i).getDisplay() + specialConversion(groupMembers) + " ";
				}
			}
			
			if (!message.equals(""))
				msgLib.standardMessage(message);
			
			return true;
		}
		
		protected String specialConversion(List<Player> playerList)
		{
			//Variable Declarations
			List<String> displayNameList = new ArrayList<String>();
			
			String message = "";
			String displayNick = "";
			int alternateState = 0;
			
			List<Player> newPlayerList = playerList;
			List<String> vanishedPlayers = FC_Rpg.vanishedPlayers;
			
			//First don't add vanished players.
			if (vanishedPlayers.size() > 0)
			{
				for (int i = 0; i < playerList.size(); i++)
				{
					for (int j = 0; j < vanishedPlayers.size(); j++)
					{
						if (playerList.get(i).getName().equalsIgnoreCase(vanishedPlayers.get(j)))
							newPlayerList.remove(playerList.get(i));
					}
				}
			}
			
			//Get names and/or nicks from players if essentials is enabled.
			for (Player p : playerList)
			{
				if (p != null)
					displayNameList.add(p.getName());
			}
			
			//Make list alphebetical.
			java.util.Collections.sort(displayNameList);
			
			//We want to alternate the colors for the standard  message.
			for (int i = 0; i < displayNameList.size(); i++)
			{
				displayNick = displayNameList.get(i);
				
				if (displayNick != null)
				{
					if (alternateState == 0)
					{
						message += ChatColor.WHITE + "" + displayNick;
						alternateState = 1;
					}
					else if (alternateState == 1)
					{
						message += ChatColor.GRAY + ", " + displayNick;
						alternateState = 2;
					}
					else if (alternateState == 2)
					{
						message += ChatColor.WHITE + ", " + displayNick;
						alternateState = 1;
					}
				}
			}
			
			return message;
		}
	}
	
	public class GuildCE
	{
		public GuildCE() { }
		
		public boolean execute()
	    {
			if (!perms.commandGuild())
				return msgLib.errorNoPermission();
			
			//Variable declarations
			boolean success = true;
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpGuild();
			
			if (args[0].equalsIgnoreCase("list"))
			{
				msgLib.standardMessage(FC_Rpg.guildConfig.listGuilds());
			}
			else if (args[0].equalsIgnoreCase("create"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equalsIgnoreCase(""))
					args[1] = "No Name! Rename!";
				
				FC_Rpg.guildConfig.createGuild(args[1], player.getName(), msgLib);
			}
			else if (args[0].equalsIgnoreCase("close") || args[0].equalsIgnoreCase("private"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equals(""))
					success = FC_Rpg.guildConfig.playerAttemptSetGuildPrivate(player.getName(), true, perms.isAdmin());
				
				if (success == false)
					msgLib.standardError("Failed to close the guild.");
				else
					msgLib.standardMessage("Successfully closed the guild!");
			}
			else if (args[0].equalsIgnoreCase("open"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equals(""))
					success = FC_Rpg.guildConfig.playerAttemptSetGuildPrivate(player.getName(), false, perms.isAdmin());
				
				if (success == false)
					msgLib.standardError("Failed to open the guild.");
				else
					msgLib.standardMessage("Successfully opened the guild!");
			}
			else if (args[0].equalsIgnoreCase("join"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equalsIgnoreCase(""))
					return msgLib.standardError("You must specify a guild to join!");
				
				//Add to the new guild
				success = FC_Rpg.guildConfig.addGuildMember(player.getName(), args[1], perms.isAdmin());
				
				if (success == false)
					msgLib.standardError("Failed to join guild.");
				else
					msgLib.standardMessage("Successfully joined guild");
			}
			else if (args[0].equalsIgnoreCase("kick"))
			{
				if (args[1].equalsIgnoreCase(""))
					return msgLib.standardError("You must specify a player to kick!");
				
				success = FC_Rpg.guildConfig.kickGuildMember(player.getName(), args[1], perms.isAdmin());
				
				if (success == false)
					msgLib.standardError("Failed to kick " + args[1] + " from a guild.");
				else
					msgLib.standardMessage("Successfully kicked " + args[1] + " from a guild.");
			}
			else if (args[0].equalsIgnoreCase("leave"))
			{
				//Prevent console from using this command.
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				//Remove the member from any old guilds
				success = FC_Rpg.guildConfig.removeGuildMember(player.getName());
				
				if (success == false)
					msgLib.standardError("Failed to leave any guilds.");
				else
					msgLib.standardMessage("Successfully left any and all guilds.");
			}
			else if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("info"))
			{
				if (args[1].equalsIgnoreCase(""))
				{
					if (FC_Rpg.guildConfig.getGuildByMember(sender.getName()) != null)
						success = FC_Rpg.guildConfig.viewGuildInfo(FC_Rpg.guildConfig.getGuildByMember(sender.getName()), msgLib);
					else
						success = false;
					
					if (success == false)
						msgLib.standardError("Failed to view any guilds.");
				}
				else
				{
					success = FC_Rpg.guildConfig.viewGuildInfoByGuildName(args[1], msgLib);
					
					if (success == false)
						msgLib.standardError("Failed to view any guilds.");
				}
			}
			else if (args[0].equalsIgnoreCase("delete"))
			{
				if (perms.isAdmin() == false)
					return msgLib.errorNoPermission();
				
				if (args[1].equalsIgnoreCase(""))
					return msgLib.standardError("You must enter a guild to delete.");
				
				success = FC_Rpg.guildConfig.deleteGuild(args[1]);
				
				if (success == false)
					return msgLib.standardError("Failed to delete the guild.");
				else
					return msgLib.successCommand();
			}
			
			return true;
	    }
	}
	
	public class PvpCE
	{
		public boolean execute()
	    {
			if (!perms.commandPvp())
				return msgLib.errorNoPermission();
			
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
					FC_Rpg.rpgBroadcast.rpgBroadcast(player.getName() + " Has Joined The " + ChatColor.RED + "Arena");
				
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
			FC_Rpg.rpgBroadcast.rpgBroadcast("Pvp Events Ended By Admin.");
		}
	}
	
	public class ResetCE
	{
		public ResetCE() { }
		
		public boolean execute()
	    {
			if (!perms.commandReset())
				return msgLib.errorNoPermission();
			
			//Variable declarations
			PlayerConfig rpgPlayerFile;
			Player target;
			
			if (args[0].equalsIgnoreCase(""))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (!FC_Rpg.worldConfig.getIsRpg(player.getWorld().getName()))
					return msgLib.standardError("You can only use this command in rpg worlds.");
				
				rpgPlayerFile = new PlayerConfig(player.getName());
				
				//Only let active players use this command.
				if (rpgPlayerFile.getIsActive() == false)
					return msgLib.errorCreateCharacter();
				
				//Set target.
				target = player;
			}
			else
			{
				if (perms.isAdmin() == false)
					return msgLib.errorNoPermission();
				
				rpgPlayerFile = new PlayerConfig(args[0]);
				
				//Only let active players use this command.
				if (rpgPlayerFile.getIsActive() == false)
					return msgLib.standardError("This player can't be reset because they aren't active.");
				
				//Set target.
				target = Bukkit.getServer().getPlayer(args[0]);
			}
			
			//Set to inactive
			rpgPlayer.playerConfig.setDefaults();
			
			//Unregister the player.
			FC_Rpg.rpgEntityManager.unregisterRpgPlayer(player);
			
			//Send confirmation message.
			msgLib.standardMessage("The reset has been performed successfully.");
			
			//Stop the targets tasks.
			if (target != null)
				FC_Rpg.rpgEntityManager.unregisterRpgPlayer(target);
			
			return true;
		}
	}
	
	public class ModifyCE
	{
		PlayerConfig playerFile;
		
		public ModifyCE() { }
		
		public boolean execute()
		{
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
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
			
			RpgPlayer rpgTarget = null;
			
			if (FC_Rpg.rpgEntityManager.getRpgPlayer(Bukkit.getServer().getPlayer(name)) != null)
			{
				rpgTarget = FC_Rpg.rpgEntityManager.getRpgPlayer(Bukkit.getServer().getPlayer(name));
				playerFile = rpgTarget.playerConfig;
			}
			else
				playerFile = new PlayerConfig(name);
			
			if (playerFile == null)
				return msgLib.errorPlayerNotFound();
			
			if (playerFile.getIsActive() != true)
				return msgLib.errorCreateCharacter();
			
			//Evaluate command parts.
			if (modifable.equalsIgnoreCase("prefix"))
				playerFile.setCustomPrefix(args[2]);
			else if (modifable.equalsIgnoreCase("nick"))
				playerFile.setNick(args[2]);
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
					playerFile.addOfflineClassExperience(intArg2, true, null);
				else if (modifable.equalsIgnoreCase("class"))
				{
					if (intArg2 >= 0 && intArg2 < FC_Rpg.classConfig.getRpgClasses().length)
						playerFile.setCombatClass(intArg2 - 1);
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
					FC_Rpg.rpgBroadcast.standardBroadcast("Thank you for donating " + args[0] + "!!!");
				}
				else if (modifable.equalsIgnoreCase("ticket") || modifable.equalsIgnoreCase("tickets"))
					playerFile.setClassChangeTickets(intArg2);
				else if (modifable.equalsIgnoreCase("arcanium"))
					playerFile.setArcanium(intArg2);
				else
					return msgLib.errorInvalidCommand();
				
				// Update the players health and mana
				playerFile.calculateHealthAndManaOffline();

				if (rpgTarget != null)
				{
					rpgTarget.updateDonatorStats();
					rpgTarget.calculateHealthAndMana();
				}
			}
			
			return msgLib.successCommand();
		}
	}
	
	public class RAdminCE
	{
		public RAdminCE() { }
		
		public boolean execute()
	    {
			// Without permissions, then return.
			if (!perms.commandRpg())
				return msgLib.errorNoPermission();
			
			//Handle admin commands with /rpg.
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
			//We want to send help on empty commands.
			if (args[0].equalsIgnoreCase("") || args[0].equalsIgnoreCase("admin"))
				return msgLib.helpRAdmin();
			
			//Handle admin events
			if (args[0].equalsIgnoreCase("event"))
			{
				eventSubCommand();
				return true;
			}
			
			//Handle super fast uber teleport command.
			else if (args[0].equalsIgnoreCase("go"))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				teleportAdmin(args);
			}
			
			//Change global experience multiplier
			else if (args[0].equalsIgnoreCase("expMult"))
			{
				if (args[1].equalsIgnoreCase(""))
					return msgLib.errorInvalidCommand();
				
				try { FC_Rpg.balanceConfig.setGlobalExpMultiplier(Integer.valueOf(args[1])); } catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
				
				return msgLib.successCommand();
			}
			
			//Handle wall generation command that will create a wall with signs!
			else if (args[0].equalsIgnoreCase("wall"))
			{
				createBoard();
				msgLib.standardMessage("Successfully generated wall.");
			}
			
			//Teleport to a player.
			else if (args[0].equalsIgnoreCase("tp"))
			{
				if (tpSubCommand())
					return msgLib.successCommand();
				
				return true;
			}
			
			else if (args[0].equalsIgnoreCase("sudo"))
			{
				sudoSubCommand();
				return true;
			}
			
			else if (args[0].equalsIgnoreCase("vanish"))
			{
				if (!vanishSubCommand())
					return true;
				
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("spawn"))
			{
				if (!spawnSubCommand())
					return true;
				
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("taskwipe"))
			{
				Bukkit.getScheduler().cancelTasks(FC_Rpg.plugin);
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("stop"))
			{
				Bukkit.getScheduler().cancelTasks(FC_Rpg.plugin);
				Bukkit.getPluginManager().disablePlugin(FC_Rpg.plugin);
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("reload"))
			{
				Bukkit.getPluginManager().disablePlugin(FC_Rpg.plugin);
				Bukkit.getPluginManager().enablePlugin(FC_Rpg.plugin);
				return msgLib.successCommand();
			}
			
			//Show admin help.
			else if (args[0].equalsIgnoreCase("admin"))
			{
				return msgLib.helpRAdmin();
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
							try { warpName = FC_Rpg.warpConfig.getName(i); 
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signTeleport)); 
								sign.setLine(1, warpName); 
							} catch (NullPointerException e) { continue; }
						}
						
						//Create class sign
						else if (j == 1)
						{
							//Set sign text
							sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signPickClass));
							
							try { sign.setLine(1, ChatColor.DARK_BLUE + FC_Rpg.classConfig.getRpgClass(i).getName()); }
							catch (ArrayIndexOutOfBoundsException e) { continue; }
						}
						
						//Create finish sign.
						else if (j == 2)
						{
							FC_Rpg.plugin.getLogger().info("i: " + i);
							
							if (i == 0)
							{
								//Set sign text
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signFinish));
								sign.setLine(1, ChatColor.DARK_RED + "And Assign");
								sign.setLine(2, ChatColor.DARK_RED + "Stat Points");
								sign.setLine(3, ChatColor.DARK_RED + "Automatically");
							}
							else if (i == 1)
							{
								//Set sign text
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signFinish));
								sign.setLine(1, ChatColor.DARK_RED + "And Assign Stats");
								sign.setLine(2, ChatColor.DARK_RED + "Stat Points");
								sign.setLine(3, ChatColor.DARK_RED + "Manually");
							}
							else if (i == 2)
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signFillMana));
							else if (i == 3)
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signFillHealth));
							else if (i == 4)
							{
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signTeleport));
								sign.setLine(1, "GrassLands");
								sign.setLine(2, "[Demo For -");
								sign.setLine(3, "Dungeons]");
							}
							else if (i == 5)
							{
								sign.setLine(0, FC_Rpg.cl.parse(PlayerInteractionListener.signExit));
								sign.setLine(1, "GrassLands");
								sign.setLine(2, "[Demo For -");
								sign.setLine(3, "Dungeons]");
							}
						}
						
						//Update the sign.
						sign.update();
					}
					catch (ClassCastException e)
					{
						continue;
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
		
		private boolean tpSubCommand()
		{
			if (args[1].equalsIgnoreCase(""))
				return msgLib.errorInvalidCommand();

			Player p1 = Bukkit.getServer().getPlayer(args[1]);
			Player p2 = null;

			if (!args[2].equalsIgnoreCase(""))
				p2 = Bukkit.getServer().getPlayer(args[2]);
			
			if (p2 != null)
				p1.teleport(p2);
			else if (p1 != null)
			{
				if (console != null)
				{
					msgLib.errorConsoleCantUseCommand();
					return false;
				}
				
				player.teleport(p1);
			}
			else
			{
				msgLib.errorPlayerNotOnline();
				return false;
			}
			
			return true;
		}
		
		private void eventSubCommand()
		{
			//Variable declarations.
			int eventLength = 3600;
			
			if (args[1].equalsIgnoreCase(""))
				args[1] = "";
			
			if (args[1].equalsIgnoreCase("exp"))
			{
				//Set the exp multiplier up high
				FC_Rpg.eventExpMultiplier = 2;
				
				//Announce the start of the event.
				FC_Rpg.rpgBroadcast.rpgBroadcast("Double Experience Event For One Hour Started!");
				
				FC_Rpg.tid3 = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
				{
					@Override
					public void run()
					{
						FC_Rpg.eventExpMultiplier = 1;
						FC_Rpg.rpgBroadcast.rpgBroadcast("Double Experience Event Has Ended!");
					}
				} , eventLength * 20);
			}
			else if (args[1].equalsIgnoreCase("loot"))
			{
				FC_Rpg.eventCashMultiplier = 2;
				
				FC_Rpg.rpgBroadcast.rpgBroadcast("Double Money Event For One Hour Started!");
				
				FC_Rpg.tid4 = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
				{
					public void run()
					{
						FC_Rpg.eventCashMultiplier = 1;
						FC_Rpg.rpgBroadcast.rpgBroadcast("Double Money Event Has Ended!");
					}
				} , eventLength * 20);
			}
			else if (args[1].equalsIgnoreCase("off"))
			{
				Bukkit.getServer().getScheduler().cancelTask(FC_Rpg.tid3);
				Bukkit.getServer().getScheduler().cancelTask(FC_Rpg.tid4);
				
				FC_Rpg.eventExpMultiplier = 1;
				FC_Rpg.eventCashMultiplier = 1;
				
				FC_Rpg.rpgBroadcast.rpgBroadcast("All Bonus Events Have Been Stopped By An Admin!");
			}
		}
	
		private boolean spawnSubCommand()
		{
			if (args[1].equalsIgnoreCase(""))
			{
				msgLib.errorInvalidCommand();
				return false;
			}
			
			if (console != null)
			{
				msgLib.errorConsoleCantUseCommand();
				return false;
			}
			
			//Variable declarations
			int mobsToSpawn = 0;
			
			try { mobsToSpawn = Integer.valueOf(args[2]); } catch (NumberFormatException e) { mobsToSpawn = 1; }
			
			for (int i = 0; i < mobsToSpawn; i++)
			{
				if (spawnMob() == false)
					return false;
			}
			
			return true;
		}
		
		private boolean spawnMob()
		{
			org.bukkit.entity.LivingEntity entity;
			
			try
			{
				entity = (LivingEntity) player.getWorld().spawnEntity(player.getTargetBlock(null, 300).getLocation().add(0,1,0), org.bukkit.entity.EntityType.valueOf(args[1].toUpperCase()));
			}
			catch (ClassCastException e)
			{
				msgLib.errorBadInput();
				return false;
			}
			
			try { 
				// If input for args[3], then register the custom monster.
				if (!args[3].equals(""))
					FC_Rpg.rpgEntityManager.registerCustomLevelEntity(entity, Integer.valueOf(args[3]), 0, false);
			} catch (NumberFormatException e) { msgLib.errorBadInput(); return false; }
			
			return true;
		}
		
		private boolean vanishSubCommand()
		{
			if (console != null)
			{
				msgLib.errorConsoleCantUseCommand();
				return false;
			}
			
			String playerName = player.getName();
			
			if (args[1].equalsIgnoreCase("on"))
			{
				if (!FC_Rpg.vanishedPlayers.contains(playerName))
					FC_Rpg.vanishedPlayers.add(playerName);
			}
			else if (args[1].equalsIgnoreCase("off"))
			{
				if (FC_Rpg.vanishedPlayers.contains(playerName))
					FC_Rpg.vanishedPlayers.remove(playerName);
			}
			else
			{
				if (FC_Rpg.vanishedPlayers.contains(playerName))
					FC_Rpg.vanishedPlayers.remove(playerName);
				else
					FC_Rpg.vanishedPlayers.add(playerName);
			}
			
			return true;
		}
		
		private void sudoSubCommand()
		{
			//Name and command.
			if (args[1].equalsIgnoreCase("") || args[2].equalsIgnoreCase(""))
			{
				msgLib.errorInvalidCommand();
				return;
			}
			
			//Variable Declarations
			ArgParser targetArgs = new ArgParser(args);
			targetArgs.setLastArg(2);
			NameMatcher nm = new NameMatcher();
			String name = nm.getNameByMatch(args[1]);
			Player victim = Bukkit.getServer().getPlayer(name);
			
			if (victim != null)
			{
				victim.chat(targetArgs.getFinalArg());
				msgLib.successCommand();
			}
			else
				msgLib.errorPlayerNotFound();
		}
	}
	
	public class SpellCE
	{
		SpellConfig util = new SpellConfig();
		
		public SpellCE() { }
		
		public boolean execute()
	    {
			if (!perms.commandSpell())
				return msgLib.errorNoPermission();
			
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
			for (int i = 0; i < rpgPlayer.playerConfig.getRpgClass().getSpellBook().size(); i++)
			{
				if (rpgPlayer.playerConfig.getSpellBinds().get(i) == player.getItemInHand().getTypeId())
					rpgPlayer.playerConfig.updateSpellBind(i, 999);
			}
			
			if (rpgPlayer.playerConfig.getSpellLevels().get(intArg1) < 1)
			{
				msgLib.standardError("You must level the skill up before you can bind it.");
				return true;
			}
			
			//Set the spell bind.
			rpgPlayer.playerConfig.updateSpellBind(intArg1, player.getItemInHand().getTypeId());
			
			//Send a success message to the player.
			msgLib.standardMessage("Successfully bound " + rpgPlayer.playerConfig.getRpgClass().getSpell(intArg1).name + " to item: " + player.getItemInHand().getType());
			
			return true;
		}
		
		private boolean listSubCommand()
		{
			msgLib.standardHeader("Spells List");
			
			int spellPoints = rpgPlayer.playerConfig.getSpellPoints();
			List<Integer> spellLevels = rpgPlayer.playerConfig.getSpellLevels();
			RpgClass rpgClass = rpgPlayer.playerConfig.getRpgClass();
			int spellLevel;
			Spell spell = null;
			
			msgLib.standardMessage("Current Spell Points",spellPoints + "");
			
			if (spellPoints > 0 && rpgPlayer.playerConfig.getClassLevel() < 10)
				msgLib.standardError("Upgrade spells with /spell upgrade [name]");
			
			for (int i = 0; i < rpgPlayer.playerConfig.getRpgClass().getSpellBook().size(); i++)
			{
				spellLevel = spellLevels.get(i);
				spell = rpgClass.getSpell(i);
				
				if (spellLevel == 0)
				{
					msgLib.standardError("#" + String.valueOf(i+1) + ": " + spell.name + "(" +
							String.valueOf(spellLevel) + "): " + spell.description + " This spell is unleveled.");
				}
				else if (spellLevel > 0)
				{
					try {
						msgLib.infiniteMessage("#" + String.valueOf(i+1) + ": " + spell.name + "(",
							String.valueOf(spellLevel),"): ",spell.description + " This spell costs ",
							ChatColor.DARK_AQUA + String.valueOf(spell.manaCost.get(spellLevel - 1)), " Mana." + " Next level costs ",
							ChatColor.DARK_AQUA + String.valueOf(spell.manaCost.get(spellLevel)), " Mana.");
					} catch (IndexOutOfBoundsException e) {
						msgLib.infiniteMessage("#" + String.valueOf(i+1) + ": " + spell.name + "(",
							String.valueOf(spellLevel),"): ",spell.description + " This spell costs ",
							ChatColor.DARK_AQUA + String.valueOf(spell.manaCost.get(spellLevel - 1)), " Mana." + " This spell is max level.");
					}
				}
			}
			
			return true;
		}
		
		private boolean upgradeSubCommand()
		{
			//If the player doesn't have enough spell points tell them.
			if (rpgPlayer.playerConfig.getSpellPoints() < 1)
			{
				msgLib.standardError("You don't have enough spell points");
				return true;
			}
			
			//Get the spell number by using the function getSpellNumber().
			int intArg1 = getSpellNumber(args[1]);
			
			//Return if intArg1 is -1.
			if (intArg1 == -1)
				return true;
			
			if (rpgPlayer.playerConfig.getSpellLevels().get(intArg1) >= 5)
			{
				msgLib.standardError("This skill is already maxed");
				return true;
			}
			
			//Increaese the spell level.
			rpgPlayer.playerConfig.updateSpellLevel(intArg1, rpgPlayer.playerConfig.getSpellLevels().get(intArg1) + 1);
			
			//If alchemy set alchemy to true.
			Spell spell = rpgPlayer.playerConfig.getRpgClass().getSpellBook().get(intArg1);
			
			if (spell.effectID == SpellEffect.ALCHEMY.getID())
				rpgPlayer.playerConfig.hasAlchemy = true;
			
			//Decrease player spell points.
			rpgPlayer.playerConfig.setSpellPoints(rpgPlayer.playerConfig.getSpellPoints() - 1);
			
			//Return success.
			return msgLib.infiniteMessage("You have successfully upgraded ",spell.name,"!");
		}
		
		private boolean resetSubCommand()
		{
			for (int i = 0; i < rpgPlayer.playerConfig.getRpgClass().getSpellBook().size(); i++)
			{
				if (rpgPlayer.playerConfig.getSpellBinds().get(i) == player.getItemInHand().getTypeId())
					rpgPlayer.playerConfig.updateSpellBind(i, 999);
			}
			
			rpgPlayer.playerConfig.resetActiveSpell();
			
			return msgLib.successCommand();
		}
		
		private boolean autocastSubCommand()
		{
			String enabled = "Auto-Cast has been enabled.";
			String disabled = "Auto-Cast has been disabled.";
			
			if (args[1].equalsIgnoreCase("on"))
			{
				rpgPlayer.playerConfig.setAutoCast(true);
				return msgLib.standardMessage(enabled);
			}
			else if (args[1].equalsIgnoreCase("off"))
			{
				rpgPlayer.playerConfig.setAutoCast(false);
				return msgLib.standardMessage(disabled);
			}
			else
			{
				if (rpgPlayer.playerConfig.getAutoCast() == false)
				{
					rpgPlayer.playerConfig.setAutoCast(true);
					return msgLib.standardMessage(enabled);
				}
				else
				{
					rpgPlayer.playerConfig.setAutoCast(false);
					return msgLib.standardMessage(disabled);
				}
			}
		}
		
		private int getSpellNumber(String spellArgument)
		{
			int intArg1 = -1;
			int spellCount = rpgPlayer.playerConfig.getRpgClass().getSpellBook().size();
			
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
					if (spellArgument.equalsIgnoreCase(rpgPlayer.playerConfig.getRpgClass().getSpell(i).name))
					{
						intArg1 = i;
						break;
					}
				}
			}
			
			//Check to make sure the spell is in the right range.
			if (intArg1 < 0 || intArg1 > spellCount)
			{
				msgLib.standardError("You have chosen an invalid spell.");
				return -1;
			}
			
			return intArg1;
		}
	}
	
	public class AlchemyCE
	{
		private double spellMagnitude;
		
		public AlchemyCE() { }
		
		public boolean execute()
		{
			if (!rpgPlayer.playerConfig.getHasAlchemy())
				return msgLib.errorNoPermission();
			
			if (console != null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (args[0].equals(""))
				return msgLib.helpAlchemy();
			
			SpellCaster sc = new SpellCaster();
			List<Spell> sb = rpgPlayer.playerConfig.getRpgClass().getSpellBook();
			
			for (int i = 0; i < sb.size(); i++)
			{
				if (sb.get(i).effectID == SpellEffect.ALCHEMY.getID())
				{
					spellMagnitude = sc.updatefinalSpellMagnitude(rpgPlayer, sb.get(i),(rpgPlayer.playerConfig.getSpellLevels().get(i) - 1));
					break;
				}
			}
			
			if (args[0].equals("list"))
				return listSubCommand();
			else if (args[0].equals("buy"))
			{
				if (buySubCommand())
					return true;
				else
					return msgLib.errorInvalidCommand();
			}
			
			else if (args[0].equals("smelt"))
				return smeltSubCommand();
			
			return true;
		}
		
		private boolean listSubCommand()
		{
			int startPoint;
			int endPoint;
			int storeSize = FC_Rpg.buyList.size();
			int buyableCount = FC_Rpg.itemConfig.getBuyableCount();
			
			if (!args[1].equalsIgnoreCase(""))
				try { startPoint = Integer.valueOf(args[1])-1 ; } catch (NumberFormatException e) { startPoint = storeSize; }
			else
				startPoint = 0;
			
			endPoint = startPoint + 15;
			
			if (endPoint > storeSize)
				endPoint = storeSize;
			
			if (startPoint > buyableCount - 15)
				startPoint = buyableCount - 15;
			
			//Begin displaying.
			msgLib.standardHeader("Alchemical Purchasables - " + buyableCount + " Items");
			
			for (int i = startPoint; i < endPoint; i++)
			{
				RpgItem p;
				
				try { p = FC_Rpg.buyList.get(i); } catch (IndexOutOfBoundsException e) { break; }
				
				if (p.priceBuy > 0)
					msgLib.infiniteMessage("#" + (i+1) + ": ",p.material.toString()," for ",FC_Rpg.df3.format(p.priceBuy)," Arcanium.");
				else
					endPoint++;
			}
			
			msgLib.standardMessage("Finished Listing.");
			
			return true;
		}
		
		private boolean buySubCommand()
		{
			// Requires an item specified to buy.
			if (args[1].equalsIgnoreCase(""))
				return true;
			
			//Variable Declarations
			RpgItem puchasable;
			int intSelection = 0;
			int count = 0;
			boolean hasSelection = true;
			
			//User input storing and validation.
			try { intSelection = Integer.valueOf(args[1]) - 1; } catch (NumberFormatException e) { hasSelection = false; }
			try { count = Integer.valueOf(args[2]); } catch (NumberFormatException e) { count = 1; }
			
			if (hasSelection == false)
			{
				for (int i = 0; i < FC_Rpg.buyList.size(); i++)
				{
					if (args[1].equalsIgnoreCase(FC_Rpg.buyList.get(i).material.toString()))
					{
						intSelection = i;
						hasSelection = true;
						break;
					}
				}
			}
			
			if (hasSelection == false)
				return false;
			
			if (intSelection > FC_Rpg.buyList.size() || intSelection < 0)
				return false;
			
			if (count < 1)
				return false;
			
			puchasable = FC_Rpg.buyList.get(intSelection);
			double cost = puchasable.priceBuy * count;
			
			// Prevent purchase without enough money.
			if (rpgPlayer.playerConfig.getArcanium() < cost)
				return false;
			
			// Take away arcanium.
			rpgPlayer.playerConfig.subtractArcanium(cost);
			rpgPlayer.addItemToInventory(new ItemStack(puchasable.getMaterial(), count));
			
			msgLib.infiniteMessage("You have bought ",count + "x ",puchasable.material.toString(), " for " + FC_Rpg.df3.format(cost)," Arcanium.");
			
			return true;
		}
		
		private boolean smeltSubCommand()
		{
			double totalArcaniumRecieved = 0;
			double arcaniumRecieved = 0;
			ItemStack air = new ItemStack(Material.AIR,0);
			
			if (args[1].equalsIgnoreCase("all"))
			{
				ItemStack[] contents = player.getInventory().getContents();
				
				for (int i = 0; i < contents.length; i++)
				{
					arcaniumRecieved = smelt(contents[i]);
					
					if (arcaniumRecieved > 0)
					{
						player.getInventory().setItem(i, air);
						totalArcaniumRecieved += arcaniumRecieved;
					}
				}
			}
			else
			{
				totalArcaniumRecieved = smelt(player.getItemInHand());
				
				if (totalArcaniumRecieved > -1)
					player.setItemInHand(new ItemStack(Material.AIR,1));
			}
			
			//Display how much arcanium they have recieved if it's above 0.
			if (totalArcaniumRecieved > 0)
				msgLib.infiniteMessage("You have earned ",FC_Rpg.df3.format(totalArcaniumRecieved)," Arcanium.");
			else
				msgLib.standardError("You failed to react any items.");
			
			return true;
		}
		
		private double smelt(ItemStack item)
		{
			RpgItem p = getRpgItem(item);
			
			if (p == null)
				return 0;
			
			double multiplier = 1;
			
			if (spellMagnitude > 0)
				multiplier += spellMagnitude;
			
			double arcaniumToGive = p.priceSell * multiplier * item.getAmount();
			int enchantmentStrength = 0;
			
			for (Enchantment enchant : Enchantment.values())
			{
				if (!enchant.equals(Enchantment.DURABILITY) && item.containsEnchantment(enchant))
					enchantmentStrength = item.getEnchantmentLevel(enchant);
			}
			
			arcaniumToGive = arcaniumToGive * (1 + (enchantmentStrength * .2));
			
			rpgPlayer.playerConfig.addArcanium(arcaniumToGive);
			
			return arcaniumToGive;
		}
		
		private RpgItem getRpgItem(ItemStack item)
		{
			if (item == null)
				return null;
			
			for (RpgItem p : FC_Rpg.buyList)
			{
				if (p.getMaterial() == item.getType())
					return p;
			}
			
			return null;
		}
	}
	
	public class WarpCE
	{
		WarpConfig wc;
		
		public WarpCE() { }
		
		public boolean execute()
		{
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
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
			else if (args[0].equalsIgnoreCase("new"))
				return newSubCommand();
			
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
				try { startPoint = Integer.valueOf(args[1]) - 1; } catch (NumberFormatException e) { }
			
			msgLib.standardHeader("Warp List");
			
			for (int i = startPoint; i < 10000; i++)
			{
				if (wc.getName(i) != null)
				{
					msg[0] = "#" + (i+1) + ": ";
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
				{
					wc.setName(i, args[1]);
					break;
				}
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
		
		//w new~0 [name]~1
		private boolean newSubCommand()
		{
			FC_Rpg.warpConfig.addNewWarp(args[1]);
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
			else if (args[2].equalsIgnoreCase("destination"))
			{
				try { wc.setDestination(id, new Location(player.getWorld(), Integer.valueOf(args[3]), Integer.valueOf(args[4])
						, Integer.valueOf(args[5]), Float.valueOf(args[6]), Float.valueOf(args[7]))); } 
				catch (NumberFormatException e) { return msgLib.errorInvalidCommand(); }
			}
			else
				return msgLib.helpWarp();
			
			return msgLib.successCommand();
		}
		
		private String getDestinationString(int warpNumber)
		{
			DecimalFormat df = new DecimalFormat("#.#");
			
			try
			{
				return df.format(wc.getDestination(warpNumber).getX()) + ", " + df.format(wc.getDestination(warpNumber).getY()) + ", " + df.format(wc.getDestination(warpNumber).getZ());
			}
			catch (NullPointerException e)
			{
				return "[Broken Warp, Fix In Config]";
			}
		}
	}
	
	public class BuffCE
	{
		Map<String, PotionEffect> peMap;
		Player target;
		
		public BuffCE() {  }
		
		public boolean execute()
		{
			if (!perms.commandBuff())
				return msgLib.errorNoPermission();
			
			//Make sure the starting argument isn't empty.
			if (args[0].equals(""))
				return msgLib.helpBuff();
			
			initializePEMap();
			
			if (args[0].equalsIgnoreCase("random"))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				return randomSubCommand();
			}
			
			if (perms.isAdmin())
			{
				//Check permission.
				if (!perms.commandBuff())
					return msgLib.errorNoPermission();
				
				//Check subcommand.
				if (args[0].equalsIgnoreCase("all"))
					return allSubCommand();
				
				else if (args[0].equalsIgnoreCase("clear"))
				{
					if (updateTarget() == false)
						return true;
					
					return clearSubCommand();
				}
				else if (args[0].equalsIgnoreCase("max"))
				{
					if (updateTarget() == false)
						return true;
					
					return maxSubCommand();
				}
			}
			
			return msgLib.errorInvalidCommand();
		}
		
		private boolean randomSubCommand()
		{
			PotionEffect pickedPE = getRandomPotionEffect();
			
			//Lets admin put potions on theirself.
			if (perms.isAdmin())
			{
				if (!args[2].equals("") && !args[3].equals("") && !args[4].equals(""))
					try { pickedPE = new PotionEffect(PotionEffectType.getByName(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4])); } catch (NumberFormatException e) { }
				
				if (updateTarget() == false)
					return true;
			}
			else
			{
				FC_Rpg.economy.withdrawPlayer(player.getName(), FC_Rpg.generalConfig.getBuffCommandCost());
				
				target = player;
			}
			
			//Add the potion effect.
			target.addPotionEffect(pickedPE);
			
			return msgLib.successCommand();
		}
		
		public boolean allSubCommand()
		{
			for (Player p : Bukkit.getServer().getOnlinePlayers())
				p.addPotionEffect(getRandomPotionEffect());
			
			FC_Rpg.rpgBroadcast.standardBroadcast("Everybody has been given a random (de)buff!");
			
			return true;
		}
		
		private boolean clearSubCommand()
		{
			if (updateTarget() == false)
				return true;
			
			for (PotionEffect p : target.getActivePotionEffects()) 
				target.removePotionEffect(p.getType());
			
			return msgLib.standardMessage("Successfully cleared all buffs on player &p" + target.getName() + "&p!");
		}
		
		private boolean maxSubCommand()
		{
			if (updateTarget() == false)
				return true;
			
			for (PotionEffect p : peMap.values()) 
				target.addPotionEffect(p);
			
			return msgLib.standardMessage("Successfully put all buffs player &p" + target.getName() + "&p!");
		}
		
		private boolean updateTarget()
		{
			if (!args[1].equals(""))
			{
				if (Bukkit.getServer().getPlayer(args[1]) != null)
					target = Bukkit.getServer().getPlayer(args[1]);
				else
				{
					msgLib.errorPlayerNotFound();
					return false;
				}
			}
			else
			{
				if (player != null)
					target = player;
				else if (console != null)
				{
					msgLib.errorConsoleCantUseCommand();
					return false;
				}
			}
			
			return true;
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
		
		private void initializePEMap()
		{
			peMap = new HashMap<String, PotionEffect>();
			
			peMap.put("jump", new PotionEffect(PotionEffectType.JUMP, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("speed", new PotionEffect(PotionEffectType.SPEED, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("regeneration", new PotionEffect(PotionEffectType.REGENERATION, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("fireresistance", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("fastdigging", new PotionEffect(PotionEffectType.FAST_DIGGING, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("increasedamage", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("damageresistance", new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("invisibility", new PotionEffect(PotionEffectType.INVISIBILITY, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("nightvision", new PotionEffect(PotionEffectType.NIGHT_VISION, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("waterbreathing", new PotionEffect(PotionEffectType.WATER_BREATHING, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("slow", new PotionEffect(PotionEffectType.SLOW, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("blindness", new PotionEffect(PotionEffectType.BLINDNESS, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("poison", new PotionEffect(PotionEffectType.POISON, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("confusion", new PotionEffect(PotionEffectType.CONFUSION, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("hunger", new PotionEffect(PotionEffectType.HUNGER, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("slowdigging", new PotionEffect(PotionEffectType.SLOW_DIGGING, getRandomDuration(), getRandomPotionStrength()));
			peMap.put("weakness", new PotionEffect(PotionEffectType.WEAKNESS, getRandomDuration(), getRandomPotionStrength()));
		}
	}
	
	public class RealmCE
	{
		public RealmCE() { }
		
		public boolean execute()
		{
			if (args[0].equalsIgnoreCase(""))
				return msgLib.helpRealm();
			
			String playerWorld = player.getWorld().getName();
			
			//List worlds
			if (args[0].equalsIgnoreCase("list"))
			{
				int count = 0;
				String[] msg = new String[3];
				
				//Give a header
				msgLib.standardHeader("World List");
				
				//List all the worlds.
				for (World world : Bukkit.getServer().getWorlds())
				{
					msg[0] = "#" + (count+1) + ": ";
					msg[1] = world.getName();
					
					if (FC_Rpg.worldConfig.isCreated(world.getName()))
						msg[2] = " [In Config]";
					else
						msg[2] = " [Not In Config]";
					
					msgLib.standardMessage(msg);
					count++;
				}
				
				return true;
			}
			
			else if (args[0].equalsIgnoreCase("toggleRpg"))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				FC_Rpg.worldConfig.setIsRpg(playerWorld, !FC_Rpg.worldConfig.getIsRpg(playerWorld), player);
				
				return msgLib.successCommand();
			}
			
			else if (args[0].equalsIgnoreCase("levelone"))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
				FC_Rpg.worldConfig.setLevelOne(player.getLocation());
				return msgLib.successCommand();
			}
			
			if (args[1].equalsIgnoreCase(""))
				return msgLib.errorInvalidCommand();
			
			//Teleport to different worlds.
			else if (args[0].equalsIgnoreCase("tp"))
			{
				if (console != null)
					return msgLib.errorConsoleCantUseCommand();
				
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
			
			//Load up a world into the world config and warp config.
			else if (args[0].equalsIgnoreCase("new"))
			{
				 if (FC_Rpg.worldConfig.addNewWorld(args[1]))
					 return msgLib.successCommand();
				 else
					 return msgLib.standardError("Unable to detect world.");
			}
			
			//Handle setting world spawns.
			else if (args[0].equalsIgnoreCase("spawn")) //rpg spawn [here] OR //rpg spawn [worldname] [x] [y] [z] [yaw] [pitch]
			{
				WorldConfig worldConfig = new WorldConfig();
				
				if (args[1].equalsIgnoreCase("here"))
				{
					if (console != null)
						return msgLib.errorConsoleCantUseCommand();
					
					worldConfig.setWorldSpawn(player.getLocation());
				}
				else
				{
					//Make sure all arguments are valid.
					for (int i = 1; i < 7; i++)
						if (args[i].equalsIgnoreCase("")) return msgLib.helpRealm();
					
					try
					{
						worldConfig.setWorldSpawn(args[1], Double.valueOf(args[2]), Double.valueOf(args[3]), Double.valueOf(args[4]), Float.valueOf(args[5]), Float.valueOf(args[6]));
					}
					catch (NumberFormatException e)
					{
						return msgLib.errorInvalidCommand();
					}
				}
				
				return msgLib.standardMessage("Successfully changed world spawn.");
			}
			
			return msgLib.successCommand();
		}
	}
}











/*

*/



























