package me.Destro168.FC_Rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.Configs.FaqConfig;
import me.Destro168.Configs.GroupConfig;
import me.Destro168.Configs.PlayerFileConfig;
import me.Destro168.Configs.SpellConfig;
import me.Destro168.Configs.WorldConfig;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.LoadedObjects.Group;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class CommandGod implements CommandExecutor
{
	private CommandSender sender;
	private ColouredConsoleSender console;
	private Player player;
	private RpgPlayer rpgPlayer;
	private RpgMessageLib msgLib;
	private ArgParser ap;
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
		
		if (command.getName().equalsIgnoreCase("bighelp"))
		{
			return msgLib.bigHelp();
		}
		
		else if (command.getName().equalsIgnoreCase("class"))
		{
			ClassCE cmd = new ClassCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("donator"))
		{
			DonatorCE cmd = new DonatorCE();
			return cmd.execute();
		}
		
		else if (command.getName().equalsIgnoreCase("dungeon"))
		{
			DungeonCE cmd = new DungeonCE();
			return cmd.execute();
		}
		
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
			PartyCE cmd = new PartyCE();
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
		
		return true;
    }
	
	private boolean initialize(CommandSender sender_, String[] args2)
	{
		//Variable Declarations/Initializations
		ap = new ArgParser(args2);
		sender = sender_;
		args = ap.getArgs();
		
		//Assign key variables based on command input and arguments.
		if (sender instanceof Player)
		{
			player = (Player) sender;
			rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
			perms = new FC_RpgPermissions(player);
			msgLib = new RpgMessageLib(player);
			isActive = rpgPlayer.getPlayerConfigFile().getIsActive();
		}
		else if (sender instanceof ColouredConsoleSender)
		{
			console = (ColouredConsoleSender) sender;
			perms = new FC_RpgPermissions(true);
			msgLib = new RpgMessageLib(console);
			isActive = true;
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
			if (args[0].equals(""))
				return msgLib.helpClass();
			if (args[0].equalsIgnoreCase("spec"))
				return specSubCommand();
			else if (args[0].equalsIgnoreCase("view"))
				return viewSubCommand();
			else if (args[0].equalsIgnoreCase("allocate"))
				return allocateSubCommand();
			else if (args[0].equals("switch"))
				return switchSubCommand();
			
			return true;
		}
		
		//Let the player add stat points.
		private boolean specSubCommand()
		{
			if (player == null)
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
				return msgLib.help();
			}
			
			return true;
		}
		
		//Let the player view their stat points, experience, just all statistics really.
		private boolean viewSubCommand()
		{
			//Variable Declarations
			PlayerFileConfig rpgPlayerFile;
			DateManager dm = new DateManager();
			
			String curHealth = "";
			String maxHealth = "";
			String curMana = "";
			String maxMana = "";
			String baseKeyWord = " Base: ";
			String totalKeyWord = " Total: ";
			
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
			else
			{
				//If so load the file
				rpgPlayerFile = rpgPlayer.getPlayerConfigFile();
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
		
		//Let the player view their stat points, experience, just all statistics really.
		private boolean allocateSubCommand()
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
		private boolean switchSubCommand()
		{
			//Only consoles can use the switch command.
			if (player == null)
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
			if (player == null)
				return msgLib.errorConsoleCantUseCommand();
			
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
				else if (args[0].equalsIgnoreCase("respecialize"))
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
	
	public class DungeonCE
	{
		public boolean execute()
	    {
			//Variable Declarations
			DungeonEvent dungeon;
			int dungeonNumber = 0;
			
			//Only admins can use dungeon command.
			if (perms.isAdmin() == false)
				return true;
			
			//Convert input into a dungeon number.
			try
			{
				dungeonNumber = Integer.valueOf(ap.getArg(1)) - 1;
			}
			catch (NumberFormatException e)
			{
				return msgLib.helpDungeon();
			}
			
			//Store dungeon for convenient use.
			dungeon = FC_Rpg.dungeon[FC_Rpg.trueDungeonNumbers.get(dungeonNumber)];
			
			//If the new sub command is used, ...
			if (ap.getArg(0).equalsIgnoreCase("stop") || ap.getArg(0).equalsIgnoreCase("end"))
			{
				dungeon.end(false);
				return msgLib.successCommand();
			}
			
			else if (ap.getArg(0).equalsIgnoreCase("init") || ap.getArg(0).equalsIgnoreCase("start") || ap.getArg(0).equalsIgnoreCase("begin"))
			{
				//Start the dungeon
				dungeon.initialize(dungeonNumber);
				
				return msgLib.successCommand();
			}
			
			else if (ap.getArg(0).equalsIgnoreCase("check"))
			{
				//Start the dungeon
				dungeon.checkMobDeath(null);
				return msgLib.successCommand();
			}
			
			else if (ap.getArg(0).equalsIgnoreCase("kick"))
			{
				//Start the dungeon
				if (ap.getArg(1).equals(""))
					return msgLib.errorInvalidCommand();
				
				Player p2 = Bukkit.getServer().getPlayer(ap.getArg(1));
				
				if (p2 == null)
					return msgLib.errorPlayerNotOnline();
				
				dungeon.removeDungeoneer(player, p2, true);
				
				return true;
			}
			
			//Dungeon help.
			return msgLib.helpDungeon();
	    }
	}
	
	public class FaqCE
	{
		public FaqCE() { }
			
		public boolean execute()
	    {
			FaqConfig fm = new FaqConfig();
			String tag = "";
			String faqFirstHalf = "";
			String faqSecondHalf = "";
			boolean success = false;
			int breakPoint1 = 0;
			int breakPoint2 = 0;
			
			//We attempt to display faqs and parse based on faq.
			for (int i = 0; i < 10000; i++)
			{
				//Store the tag that we are currently parsing.
				tag = fm.getFaqTag(i);
				
				//If the tag isn't null...
				if (fm.getFaqTag(i) != null)
				{
					//Also if the argument entered is equal to the tag...
					if (ap.getArg(0).equals(tag))
					{
						//We prase through the faqs for that tag now.
						for (int j = 0; j < 10000; j++)
						{
							//Store first and second half of the faq.
							try
							{
								faqFirstHalf = fm.getFaq1(i).get(j);
								faqSecondHalf = fm.getFaq2(i).get(j);
								
								if (faqFirstHalf != null && faqSecondHalf != null)
								{
									if (success == false)
										msgLib.standardHeader(fm.getFaqName(i));
									
									if (!(faqSecondHalf.equals("[empty]")))
										msgLib.standardMessage(faqFirstHalf,faqSecondHalf);
									else
										msgLib.standardMessage(faqFirstHalf);

									success = true;
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
			
			if (success == false)
				return msgLib.helpFaq();
			
			return true;
	    }
	}
	
	public class QuickCE
	{
		public boolean execute(String commandName)
		{
			if (perms.isAdmin() == false)
				return msgLib.errorNoPermission();
			
			if (args[0].equals(""))
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
			
			return msgLib.successCommand();
		}
		
		private void quick_Gamemode()
	    {
			if (player.getGameMode() == GameMode.SURVIVAL)
				player.setGameMode(GameMode.CREATIVE);
			else
				player.setGameMode(GameMode.SURVIVAL);
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
			if (player == null)
				return msgLib.errorConsoleCantUseCommand();
			
			if (rpgPlayer.getPlayerConfigFile().isDonator() || perms.commandHat())
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
			if (args[0].equals(""))
				return msgLib.helpJob();
			
			if (args[0].equalsIgnoreCase("view"))
			{
				if (!args[1].equals(""))
				{
					if (Bukkit.getServer().getPlayer(args[1]) != null)
					{
						rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
						msgLib.standardMessage("Successfully found player to view job.");
					}
					else
						msgLib.standardError("Couldn't find player to view job.");
				}
				else
				{
					//Console can't view job information about itself.
					if (player == null)
						return msgLib.errorConsoleCantUseCommand();
				}
				
				//Send the information.
				msgLib.standardHeader("Job Information Sheet");
				msgLib.standardMessage("Name", rpgPlayer.getPlayerConfigFile().getName());
				msgLib.standardMessage("Job Rank",String.valueOf(rpgPlayer.getPlayerConfigFile().getJobRank()));
				msgLib.standardMessage("Job Promotion Cost",String.valueOf(rpgPlayer.getPromotionCost()));
			}
			else if (args[0].equalsIgnoreCase("promote"))
			{
				//Console can't promote itself.
				if (player == null)
					return msgLib.errorConsoleCantUseCommand();
				
				//Make sure the player isn't max job rank first
				if (rpgPlayer.getPlayerConfigFile().getJobRank() < 6)
				{
					//Make sure that players can only be promoted with the proper job rank.
					if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 20 && rpgPlayer.getPlayerConfigFile().getJobRank() == 1)
					{
						msgLib.standardError("You need level 20+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 40 && rpgPlayer.getPlayerConfigFile().getJobRank() == 2)
					{
						msgLib.standardError("You need level 40+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 60 && rpgPlayer.getPlayerConfigFile().getJobRank() == 3)
					{
						msgLib.standardError("You need level 60+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 80 && rpgPlayer.getPlayerConfigFile().getJobRank() == 4)
					{
						msgLib.standardError("You need level 80+ for promotion.");
						return true;
					}
					else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 100 && rpgPlayer.getPlayerConfigFile().getJobRank() == 5)
					{
						msgLib.standardError("You need level 100 for the final promotion. Good luck :)");
						return true;
					}
					
					//If the player can afford a promotion
					if (FC_Rpg.economy.getBalance(player.getName()) > rpgPlayer.getPromotionCost())
					{
						//Take away money from the player.
						FC_Rpg.economy.bankWithdraw(player.getName(), rpgPlayer.getPromotionCost());
						
						//Give them the promotion
						rpgPlayer.getPlayerConfigFile().setJobRank(rpgPlayer.getPlayerConfigFile().getJobRank() + 1);
						
						//Announce the promotion
						FC_Rpg.bLib.standardBroadcast(player.getName() + " is now Job Rank [" + rpgPlayer.getPlayerConfigFile().getJobRank() + "]");
					}
					else
					{
						msgLib.standardError("You need $" + String.valueOf(rpgPlayer.getPromotionCost() - FC_Rpg.economy.getBalance(player.getName())) + " for promotion");
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
				//Prevent console from using this command.
				if (player == null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (args[1].equals(""))
					args[1] = "No Name! Rename!";
				
				success = FC_Rpg.partyManager.createParty(player.getName(), args[1]);
				
		    	if (success == false)
		    		msgLib.standardError("Failed to create a new party.");
			}
			else if (args[0].equalsIgnoreCase("close"))
			{
				//Prevent console from using this command.
				if (player == null)
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
				if (player == null)
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
				if (player == null)
					return msgLib.errorConsoleCantUseCommand();
				
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
				//Prevent console from using this command.
				if (player == null)
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
				if (args[1].equals(""))
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
			if (ap.getArg(0).equals("new"))
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
				if (ap.getArg(0).equals(""))
					return msgLib.helpPvp();
				else
					return msgLib.standardMessage("Command was blocked because no event is currently happening.");
			}
			
			//If the end sub command is used, ...
			else if (ap.getArg(0).equals("end") || ap.getArg(0).equals("stop"))
			{
				//Only admins can use this command.
				if (perms.isAdmin() == false)
					return true;
				
				//End existing pvp events.
				endPvpEvent();
				return true;
			}
			
			//If the join command is used, ...
			if (ap.getArg(0).equals("join"))
			{
				//Prevent console from using this command.
				if (player == null)
					return msgLib.errorConsoleCantUseCommand();
				
				//Add the player
				if (FC_Rpg.pvp.addPvper(player) == true)
					FC_Rpg.bLib.standardBroadcast(player.getName() + " Has Joined The " + ChatColor.RED + "Arena");
				
				return true;
			}
			
			//If the leave command is used, ...
			else if (ap.getArg(0).equals("leave"))
			{
				//Prevent console from using this command.
				if (player == null)
					return msgLib.errorConsoleCantUseCommand();
				
				if (FC_Rpg.pvp.removePvper(player,player,true) == true)
					return msgLib.successCommand();
				
				return true;
			}
			
			//If the leave command is used, ...
			else if (ap.getArg(0).equals("kick"))
			{
				//Ensure an admin is performing the command.
				if (perms.isAdmin() == false)
					return msgLib.errorNoPermission();
				
				//Store the target.
				target = Bukkit.getServer().getPlayer(ap.getArg(1));
				
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
			else if (ap.getArg(0).equals("list"))
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
			//TODO - Optimize/Update reset again.
			
			//Variable declarations
			PlayerFileConfig rpgPlayerFile;
			Player target;
			
			if (args[0].equals(""))
				args[0] = player.getName();
			
			rpgPlayerFile = new PlayerFileConfig(args[0]);
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Store target.
			target = Bukkit.getServer().getPlayer(args[0]);
			
			if (perms.isAdmin())
			{
				//Set to inactive
				rpgPlayer.getPlayerConfigFile().setDefaults();
				
				//Stop the players tasks.
				FC_Rpg.rpgManager.unregisterRpgPlayer(player);
				
				//Send confirmation message.
				msgLib.standardMessage("Successfully reset",rpgPlayer.getPlayerConfigFile().getName());
				
				if (target != null)
					FC_Rpg.rpgManager.unregisterRpgPlayer(target);
			}
			else
			{
				//We want to return if the player is attempting to reset somebody else.
				if (!rpgPlayerFile.getName().equals(player.getName()))
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
	
	public class RpgCE 
	{
		PlayerFileConfig playerFile;
		
		public RpgCE() { }
		
		public boolean execute()
	    {
			//Variable declarations.
			int eventLength = 3600;
			boolean cont = true;
			
			//Prevent console from using this command.
			if (player == null)
				return msgLib.errorConsoleCantUseCommand();
			
			playerFile = rpgPlayer.getPlayerConfigFile();
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Handle admin commands with /rpg.
			if (!perms.isAdmin())
				return msgLib.errorNoPermission();
			
			//We want to send the player help on empty commands.
			if (args[0].equalsIgnoreCase(""))
				return msgLib.help();
			
			//Further variable Declarations.
			NameMatcher nm = new NameMatcher();
			String name = nm.getNameByMatch(args[1]);
			
			//Admin Modify Command
			if (args[0].equalsIgnoreCase("modify") || args[0].equalsIgnoreCase("m"))
			{
				cont = true;
				
				if (args[1].equals(""))
				{
					args[1] = "";
					cont = false;
				}
				
				else if (args[2].equals(""))
				{
					args[2] = "";
					cont = false;
				}
				
				if (args[3].equals(""))
				{
					args[3] = "";
					cont = false;
				}
				
				if (cont == true)
				{
					playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(name)).getPlayerConfigFile();
					
					if (playerFile == null)
						return msgLib.errorPlayerNotFound();
					
					try
					{
						if (args[2].equalsIgnoreCase("strength") || args[2].equalsIgnoreCase("attack"))
							playerFile.setAttack(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("magic"))
							playerFile.setMagic(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("constitution") || args[2].equalsIgnoreCase("health"))
							playerFile.setConstitution(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("intelligence") || args[2].equalsIgnoreCase("mana"))
							playerFile.setIntelligence(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("addLevel") || args[2].equalsIgnoreCase("addLevels"))
							playerFile.addLevels(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("level"))
							playerFile.setClassLevel(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("exp"))
							playerFile.setClassExperience(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("addExp") || args[2].equalsIgnoreCase("addExperience"))
							playerFile.addOfflineClassExperience(Integer.valueOf(args[3]), true);
						else if (args[2].equalsIgnoreCase("class"))
						{
							int intArg3 = Integer.valueOf(args[3]);
							
							if (intArg3 >= 0 && intArg3 < FC_Rpg.classConfig.getRpgClasses().length)
								playerFile.setCombatClass(intArg3);
						}
						else if (args[2].equalsIgnoreCase("tickets"))
							playerFile.setClassChangeTickets(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("stat") || args[2].equalsIgnoreCase("stats"))
							playerFile.setStats(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("jobRank"))
							playerFile.setJobRank(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("prefix"))
							playerFile.setCustomPrefix(args[3]);
						else if (args[2].equalsIgnoreCase("spellPoint") || args[2].equalsIgnoreCase("spellPoints"))
							playerFile.setSpellPoints(Integer.valueOf(args[3]));
						else if (args[2].equalsIgnoreCase("all"))
						{
							int intArg3 = Integer.valueOf(args[3]);
							
							playerFile.setAttack(intArg3);
							playerFile.setMagic(intArg3);
							playerFile.setConstitution(intArg3);
							playerFile.setIntelligence(intArg3);
						}
						else if (args[2].equalsIgnoreCase("addsecond") || args[2].equalsIgnoreCase("addseconds"))
							playerFile.setSecondsPlayed(playerFile.getSecondsPlayed() + Integer.valueOf(args[3]));
						else
							return msgLib.errorInvalidCommand();
						
						//Update the players health and mana
						playerFile.calculateHealthAndManaOffline();
					}
					catch (NumberFormatException e)
					{
						return msgLib.errorInvalidCommand();
					}
					
					return msgLib.successCommand();
				}
			}
			
			//Give self potions.
			else if (args[0].equalsIgnoreCase("potion"))
			{
				if (args[1].equals(""))
					return msgLib.helpPotion();
				else
				{
					givePotion(args[1], args[2]);
					return msgLib.successCommand();
				}
			}
			
			//Handle admin events
			else if (args[0].equalsIgnoreCase("event"))
			{
				if (args[1].equals(""))
				{
					args[1] = "";
				}
				
				if (args[1].equalsIgnoreCase("exp"))
				{
					//Set the exp multiplier up high
					FC_Rpg.expMultiplier = 2;
					
					//Announce the start of the event.
					FC_Rpg.bLib.standardBroadcast("Double Experience Event For One Hour Started!");
					
					FC_Rpg.tid3 = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
					{
						@Override
						public void run()
						{
							FC_Rpg.expMultiplier = 1;
							
							FC_Rpg.bLib.standardBroadcast("Double Experience Event Has Ended!");
						}
					} , eventLength * 20);
				}
				else if (args[1].equalsIgnoreCase("loot"))
				{
					FC_Rpg.cashMultiplier = 2;
					
					FC_Rpg.bLib.standardBroadcast("Double Money Event For One Hour Started!");
					
					FC_Rpg.tid4 = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
					{
						@Override
						public void run()
						{
							FC_Rpg.expMultiplier = 1;
							
							FC_Rpg.bLib.standardBroadcast("Double Money Event Has Ended!");
						}
					} , eventLength * 20);
				}
				else if (args[1].equalsIgnoreCase("off"))
				{
					Bukkit.getServer().getScheduler().cancelTask(FC_Rpg.tid3);
					Bukkit.getServer().getScheduler().cancelTask(FC_Rpg.tid4);
					
					FC_Rpg.bLib.standardBroadcast("All Bonus Events Have Been Stopped By An Admin!");
				}
			}
			
			//Handle donator promotion commands
			else if (args[0].equalsIgnoreCase("set"))	//rpg set [name] [to]
			{
				//If no name is specified, we want to return an error.
				if (args[1].equals(""))
				{
					msgLib.standardError("Enter a name to set rank of.");
					return true;
				}
				
				//If the name specified can't be found, display an error.
				if (Bukkit.getPlayer(args[1]) == null)
				{
					msgLib.standardError("The specified player has never logged onto the server.");
					return true;
				}
				
				//If the player is found, get the rpg player.
				playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1])).getPlayerConfigFile();
				
				//If they haven't picked a class/job, don't promote.
				if (playerFile.getIsActive() != true)
				{
					msgLib.standardError("The specified player has not picked a class/job yet.");
					return true;
				}
				
				//Handle donator promotion
				if (args[2].equalsIgnoreCase("donator"))
				{
					//If promoting to donator, we need a number of months entered. /rpg promote [name] donator [time in months]
					if (args[3].equals(""))
					{
						msgLib.standardError("Enter a number of months");
						return true;
					}
					else
					{
						//Attempt to promote them and also give broadcast.
						try
						{
							playerFile.offlineSetDonator(Integer.valueOf(args[3]));
							FC_Rpg.bLib.standardBroadcast("Thank you for donating " + args[1] + "!!!");
						}
						catch (NumberFormatException e)
						{
							msgLib.standardMessage("You have to enter a number for the months.");
						}
					}
				}
			}
			
			//cm2(0, "/rpg giveTicket [name] [count]","Gives donator");
			else if (args[0].equalsIgnoreCase("giveTickets") || args[0].equalsIgnoreCase("giveTicket"))
			{
				if (args[1].equals(""))
				{
					msgLib.standardError("Enter a name to give tickets too.");
					return true;
				}
				
				if (args[2].equals(""))
				{
					msgLib.standardError("Enter a number of tickets to give.");
					return true;
				}
				
				if (Bukkit.getServer().getPlayer(args[1]) == null)
				{
					return msgLib.errorPlayerNotFound();
				}
				
				//Set new rpgPlayer.
				playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1])).getPlayerConfigFile();
				
				//Add the classChangeTickets.
				playerFile.setClassChangeTickets(playerFile.getClassChangeTickets() + Integer.valueOf(args[2]));
				
				msgLib.standardMessage("Successfully gave the player tickets.");
			}
			
			//Handle super fast uber teleport command.
			else if (args[0].equalsIgnoreCase("go"))
				teleportAdmin(args);
			
			//List worlds
			else if (args[0].equalsIgnoreCase("listworld") || args[0].equalsIgnoreCase("listworlds"))
			{
				int count = 0;
				
				//Give a header
				msgLib.standardHeader("World List");
				
				//List all the worlds.
				for (World world : Bukkit.getServer().getWorlds())
				{
					msgLib.standardMessage("World " + count + ": " + world.getName());
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
					
					if (args[1].equals("here"))
					{
						wm.setWorldSpawn(player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
								player.getLocation().getYaw(), player.getLocation().getPitch());
					}
					else
					{
						if (args[1].equals(""))
							return msgLib.helpOwner();
						else if (args[2].equals(""))
							return msgLib.helpOwner();
						else if (args[3].equals(""))
							return msgLib.helpOwner();
						else if (args[4].equals(""))
							return msgLib.helpOwner();
						else if (args[5].equals(""))
							return msgLib.helpOwner();
						else if (args[6].equals(""))
							return msgLib.helpOwner();
						
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
			}
			
			//Handle mine generation command.
			else if (args[0].equalsIgnoreCase("generate"))
			{
				if (args[1].equals(""))
					args[1] = "1";
			}
			
			else if (args[0].equalsIgnoreCase("tp"))
			{
				if (perms.isOwner() == false)
					return msgLib.errorNoPermission();
				
				if (args[1].equals(""))
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
				
				if (args[1].equals(""))
					return msgLib.errorInvalidCommand();
				
				if (args[2].equals(""))
					return msgLib.errorInvalidCommand();
				
				ArgParser targetArgs = new ArgParser(args);
				
				targetArgs.setLastArg(2);
				
				if (Bukkit.getServer().getPlayer(name) != null)
				{
					Bukkit.getServer().getPlayer(name).chat(targetArgs.getFinalArg());
					return msgLib.successCommand();
				}
				else
					return msgLib.errorPlayerNotFound();
			}
			
			//Show owner help.
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
		
		public void givePotion(String type, String amount)
		{
			MaterialData data;
			ItemStack itemPotion;
			Potion potion;
			int potionId;
			
			if (amount.equals(""))
				amount = "64";
			
			//Now give the potion of the specified amount.
			if (type.contains("spe"))
			{
				potion = new Potion(PotionType.SPEED, 2);
				potion.setSplash(true);
				
				potionId = 8226;
			}
			else if (type.contains("reg"))
			{
				potion = new Potion(PotionType.REGEN, 2);
				potion.setSplash(true);
				
				potionId = 8225;
			}
			else if (type.contains("poi"))
			{
				potion = new Potion(PotionType.POISON, 2);
				potion.setSplash(true);
				
				potionId = 8228;
			}
			else if (type.contains("hea"))
			{
				potion = new Potion(PotionType.INSTANT_HEAL, 2);
				potion.setSplash(true);
				
				potionId = 8229;
			}
			else if (type.contains("str"))
			{
				potion = new Potion(PotionType.STRENGTH, 2);
				potion.setSplash(true);
				
				potionId = 8233;
			}
			else if (type.contains("dam"))
			{
				potion = new Potion(PotionType.INSTANT_DAMAGE, 2);
				potion.setSplash(true);
				
				potionId = 8236;
			}
			else if (type.contains("fir"))
			{
				potion = new Potion(PotionType.FIRE_RESISTANCE, 1);
				potion.setSplash(true);
				
				potionId = 8259;
			}
			else if (type.contains("wea"))
			{
				potion = new Potion(PotionType.WEAKNESS, 1);
				potion.setSplash(true);
				
				potionId = 8264;
			}
			else if (type.contains("slo"))
			{
				potion = new Potion(PotionType.SLOWNESS, 1);
				potion.setSplash(true);
				
				potionId = 8266;
			}
			else
			{
				potion = new Potion(PotionType.SPEED, 2);
				potion.setSplash(true);
				
				potionId = 8226;
			}
			
			//If an id was set, add the potion.
			if (potionId > 0) 
			{
				//Set the potion data.
				data = new MaterialData(373, (byte) potionId);
				
				try
				{
					//Set the amount of the potion
					itemPotion = (ItemStack) data.toItemStack(Integer.valueOf(amount));
				}
				catch (NumberFormatException e)
				{
					//Set the amount of the potion
					itemPotion = (ItemStack) data.toItemStack(64);
				}
				
				//Apply potion effects to the potion itemstack.
				potion.apply(itemPotion);
				
				//Give the potions to the player.
				player.getInventory().addItem(itemPotion);
			}
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
			//TODO - Optimize/Update Spells.
			
			//Only let active players use this command.
			if (isActive == false)
				return msgLib.errorCreateCharacter();
			
			//Prevent console from using hat command.
			if (player == null)
				return msgLib.errorConsoleCantUseCommand();
			
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
			
			else if (args[0].equalsIgnoreCase("upgrade") || args[0].equalsIgnoreCase("promote"))
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


}







































