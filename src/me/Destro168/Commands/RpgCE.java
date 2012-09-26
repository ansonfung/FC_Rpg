package me.Destro168.Commands;

import me.Destro168.Configs.WorldManager;
import me.Destro168.Entities.RpgPlayerFile;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class RpgCE implements CommandExecutor 
{
	Player player = null;
	RpgPlayerFile playerFile;
	RpgMessageLib msgLib;
	FC_RpgPermissions perms;
	
	public RpgCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable declarations.
		player = (Player) sender;
		playerFile = FC_Rpg.rpgManager.getRpgPlayer(player);
		msgLib = new RpgMessageLib(player);
		RpgMessageLib msgLib;
		ArgParser ap = new ArgParser(args2);
		msgLib = new RpgMessageLib(player);
		String[] args = ap.getArgs();
		perms = new FC_RpgPermissions(player);
		int eventLength = 3600;
		boolean cont = true;
		
		//Only let active players use this command.
		if (playerFile.getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		if (args[0].equalsIgnoreCase("") || args[0].equalsIgnoreCase(""))
		{
			//We want to send the player help.
			return msgLib.help();
		}
		
		//Handle admin commands with /rpg.
		if (!perms.isAdmin())
			return msgLib.errorNoPermission();
		
		//Further variable Declarations.
		NameMatcher nm = new NameMatcher();
		String name = nm.getNameByMatch(args[1]);
		
		//Admin Modify Command
		if (args[0].equalsIgnoreCase("modify"))
		{
			cont = true;
			
			if (args[1].equals(""))
			{
				args[1] = "";
				cont = false;
			}
			
			if (args[2].equalsIgnoreCase("rankfreeze"))
			{
				if (playerFile.getRankFreeze() == true)
				{
					playerFile.setRankFreeze(false);
					msgLib.standardMessage("Successfully unfroze " + playerFile.getName() + "'s rank.");
				}
				else if (playerFile.getRankFreeze() == false)
				{
					playerFile.setRankFreeze(true);
					msgLib.standardMessage("Successfully froze " + playerFile.getName() + "'s rank.");
				}
				
				return true;
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
				playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(name));
				
				if (playerFile == null)
					return msgLib.errorPlayerNotFound();
				
				try
				{
					if (args[2].equalsIgnoreCase("strength") || args[2].equalsIgnoreCase("attack"))
						playerFile.setStrength(Integer.valueOf(args[3]));
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
						
						if (intArg3 >= 0 && intArg3 < FC_Rpg.c_classes.length)
							playerFile.setCombatClass(intArg3);
					}
					else if (args[2].equalsIgnoreCase("tickets"))
						playerFile.setClassChangeTickets(Integer.valueOf(args[3]));
					else if (args[2].equalsIgnoreCase("hunterLevel"))
						playerFile.setHunterLevel(Integer.valueOf(args[3]));
					else if (args[2].equalsIgnoreCase("stat") || args[2].equalsIgnoreCase("stats"))
						playerFile.setHunterLevel(Integer.valueOf(args[3]));
					else if (args[2].equalsIgnoreCase("jobRank"))
						playerFile.setJobRank(Integer.valueOf(args[3]));
					else if (args[2].equalsIgnoreCase("prefix"))
						playerFile.setCustomPrefix(args[3]);
					else if (args[2].equalsIgnoreCase("spellPoint") || args[2].equalsIgnoreCase("spellPoints"))
						playerFile.setSpellPoints(Integer.valueOf(args[3]));
					else if (args[2].equalsIgnoreCase("all"))
					{
						int intArg3 = Integer.valueOf(args[3]);
						
						playerFile.setStrength(intArg3);
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
				FC_Rpg.lootMultiplier = 2;
				
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
			playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
			
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
			
			//Handle hunter promotion
			else if (args[2].equalsIgnoreCase("hunter"))
			{
				playerFile.setHunterLevel(1);
				FC_Rpg.bLib.standardBroadcast(args[1] + " Has Been Promoted To " + ChatColor.BLUE + "Hunter. Be Careful!");
			}
			else if (args[2].equalsIgnoreCase("predator"))
			{
				playerFile.setHunterLevel(2);
				FC_Rpg.bLib.standardBroadcast(args[1] + " Has Been Promoted To " + ChatColor.DARK_BLUE + "Predator. Beware!");
			}
			else if (args[2].equalsIgnoreCase("terror"))
			{
				playerFile.setHunterLevel(3);
				FC_Rpg.bLib.standardBroadcast(args[1] + " Has Been Promoted To " + ChatColor.BLACK + "Terror! RUN FOR YOUR LIVES!");
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
			playerFile = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
			
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
					Location tpLoc = FC_Rpg.wm.getWorldSpawn(args[1]);
					
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
				WorldManager wm = new WorldManager();
				
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
			
			ArgParser fap = new ArgParser(args2);
			
			fap.setLastArg(2);
			
			if (Bukkit.getServer().getPlayer(name) != null)
			{
				Bukkit.getServer().getPlayer(name).chat(fap.getFinalArg());
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
					
					//Create class sign
					if (j == 1)
					{
						//Set sign text
						sign.setLine(0, ChatColor.DARK_RED + "Pick Class:");
						
						try
						{
							sign.setLine(1, ChatColor.DARK_BLUE + FC_Rpg.c_classes[i]);
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							continue;
						}
					}
					
					//Create finish sign.
					if (j == 2 && i == 0)
					{
						//Set sign text
						sign.setLine(0, ChatColor.GOLD + "Finish!");
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




