package me.Destro168.Entities;

import java.text.DecimalFormat;
import java.util.Date;

import me.Destro168.Messaging.MessageLib;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.HealthConverter;
import me.Destro168.Util.RpgMessageLib;
import me.Destro168.Util.SpellUtil;
import me.Destro168.Configs.ConfigOverlord;
import me.Destro168.Configs.Group;
import me.Destro168.Configs.GroupManager;
import me.Destro168.Configs.WorldManager;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ColorLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RpgPlayer extends RpgPlayerFile
{
	private MessageLib msgLib;
	private ItemStack air;
	private SpellUtil util;
	private Player player;
	private String prefix = "";
	private boolean isSupportBuffed;
	private int tempStrength;
	private int tempConstitution;
	private int tempMagic;
	private int tempIntelligence;
	private Date lastDodgeNotification;
	private Date logonDate;
	private double curHealth;
	private double maxHealth;
	private double curMana;
	private double maxMana;
	
	//Gets
	public Player getPlayer() { return player; }
	
	//Only call twice, one when player is loaded, once when player deloads.
	public double getCurHealth() { return curHealth; }
	public double getMaxHealth() { return maxHealth; }
	public double getCurMana() { return curMana; }
	public double getMaxMana() { return maxMana; }
	
	//Functions that rely on file get/sets
	public int getTotalStrength() { return getStrength() + tempStrength; }
	public int getTotalConstitution() { return getConstitution() + tempConstitution; }
	public int getTotalMagic() { return getMagic() + tempMagic; }
	public int getTotalIntelligence() { return getIntelligence() + tempIntelligence; }
	
	//Constructor
	public RpgPlayer()
	{
		setPlayerDefaults();
	}
	
	public RpgPlayer(Player player)
	{
		super(player.getName());
		
		//We want to set the player to the 'name', which will be matched by RpgPlayerFile
		setPlayer(Bukkit.getServer().getPlayer(name));
	}
	
	public void setPlayerDefaults()
	{
		util = new SpellUtil();
		air = new ItemStack(Material.AIR);
		player = null;
		prefix = "";
		isSupportBuffed = false;
		tempStrength = 0;
		tempConstitution = 0;
		tempMagic = 0;
		tempIntelligence = 0;
		lastDodgeNotification = new Date();
		logonDate = new Date();
		air = new ItemStack(Material.AIR);
	}
    
	public void setPlayer(Player player_)
	{
		//Set player defaults
		setPlayerDefaults();
		
		//Set local variable information based on player.
		player = player_;
		
		//Create the message lib.
		msgLib = new MessageLib(player_);
		
		//Update the prefix.
		updatePrefix();
		
		//Set the name for the player name.
		setPlayerName(player_.getName());
		
    	//Update player health hud.
		loadHM();
    	HealthConverter hc = new HealthConverter(maxHealth, curHealth);
    	
    	WorldManager wm = new WorldManager();
    	
    	if (wm.getIsRpgWorld(player_.getWorld().getName()) == true)
    		player_.setHealth(hc.getMinecraftHearts());
		
		//Check and apply donator bonuses
		donatorStatUpdate();
	}
	
	public void loadHM()
	{
		curMana = getCurManaFile();
		maxMana = getMaxManaFile();
		curHealth = getCurHealthFile();
		maxHealth = getMaxHealthFile();
	}
	
	public void dumpHM()
	{
		setCurManaFile(curMana);
		setMaxManaFile(maxMana);
		setCurHealthFile(curHealth);
		setMaxHealthFile(maxHealth);
	}
	
	public void createPlayerRecord(Player player_, int pickedClass, boolean manualDistribution)
	{
		//Store player
		setPlayer(player_);
		
		//Set the files defaults.
		setPlayerDefaults(pickedClass, manualDistribution);
		
		//Update health and mana based on stats.
		calcMaxHM();
		
		//Check and apply donator bonuses
		donatorStatUpdate();
		
		if (manualDistribution == true)
		{
			msgLib.standardMessage("You chose to manually allocate stat points!");
			msgLib.standardMessage("You MUST assign stat points or you will always die!");
			msgLib.standardMessage("Please! Assign your stat points! Use /class for help.");
		}
		else
			msgLib.standardMessage("Your stat points are automatically distributed!");
		
		//Send the player some usefull information.
		msgLib.standardMessage("Type /rpg if you need help!");
		
		//Fully heal the player.
		healFull();
	}
	
	public String updatePrefix()
	{
		//Variable Declarations
		ColorLib cl = new ColorLib();
		ConfigOverlord co = new ConfigOverlord();
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		GroupManager gm = new GroupManager();
		
		String[] playerGroups;
		String customPrefix = getCustomPrefix();
		String defaultPrefix = co.getDefaultPrefix();
		String parsedDefault = cl.parseColors(defaultPrefix);
		
		//Check the customPrefix first.
		if (customPrefix == null)
		{
			setCustomPrefix(defaultPrefix);
			prefix = parsedDefault;
			return prefix;
		}
		else if (customPrefix.equals(""))
		{
			setCustomPrefix(defaultPrefix);
			prefix = parsedDefault;
			return prefix;
		}
		else if (!customPrefix.equals(defaultPrefix))
		{
			prefix = cl.parseColors(customPrefix);
			return prefix;
		}
		
		//If no player, return parsed tag.
		if (player == null)
		{
			prefix = parsedDefault;
			return parsedDefault;
		}
		
		//Store player groups
		playerGroups = perms.getPlayerGroups();
		
		//If no groups return.
		if (playerGroups.length == 0)
			return parsedDefault;
		
		//Find what group matches the players group and return the format for it.
		for (Group group : gm.getGroups())
		{
			if (group.getName().equals(playerGroups[0]))
				return cl.parseColors(group.getDisplay());
		}
		
		//If a donator set them to donator tag.
		if (isDonator() == true)
			prefix = ChatColor.WHITE + "["+ ChatColor.YELLOW + "Donator" + ChatColor.WHITE + "]";
		
		//If a hunter add hunter tag.
		if (getHunterLevel() == 1)
			prefix += "["+ ChatColor.BLUE + "Hunter" + ChatColor.WHITE + "]";
		else if (getHunterLevel() == 2)
			prefix += "["+ ChatColor.DARK_BLUE + "Predator" + ChatColor.WHITE + "]";
		else if (getHunterLevel() == 3)
			prefix += "["+ ChatColor.BLACK + "Terror" + ChatColor.WHITE + "]";
		
		return prefix;
	}
	
	public void setDonator(int periods)
	{
		offlineSetDonator(periods);
		
		//Update stats to account for new bonuses if a donator.
		donatorStatUpdate();
	}
	
	public void addClassExperience(double x, boolean displayLevelUpMessage)
	{
		//Add the class experience
		addOfflineClassExperience(x, displayLevelUpMessage);
		
		//Update Donator Stats
		donatorStatUpdate();
		
		//Message the player a reminder to use stat points.
		if (getManualAllocation() == false)
			msgLib.standardMessage("Remember to assign stat points. Use /class for help!");
	}
	
	public void donatorStatUpdate()
	{
		if (isDonator() == true)
		{
			resetTempStats();
			
			tempStrength = (int) (getStrength() * FC_Rpg.DONATOR_PERK);
			tempConstitution = (int) (getConstitution() * FC_Rpg.DONATOR_PERK);
			tempMagic = (int) (getMagic() * FC_Rpg.DONATOR_PERK);
			tempIntelligence = (int) (getIntelligence() * FC_Rpg.DONATOR_PERK);
			
			calcMaxHM();
		}
	}
	
	public void resetTempStats()
	{
		//Reset temp stats.
		tempStrength = 0;
		tempConstitution = 0;
		tempMagic = 0;
		tempIntelligence = 0;
	}
	
	public boolean hasClassChangeTicket()
	{
		//If they have no more tickets, return false.
		if (getClassChangeTickets() == 0)
			return false;
		
		return true;
	}
	
	public void switchClass(int combatClass)
	{
		setCombatClass(combatClass);
		
		//Else we decrease tickets and return true.
		setClassChangeTickets(getClassChangeTickets() - 1);
	}
	
	public void addSupportBuff(double buffStrength)
	{
		final int[] base = new int[4];
		
		//Prevent stacking support buffs.
		if (isSupportBuffed == true)
			return;
		
		//Set support buffed.
		isSupportBuffed = true;
		
		//Store old buffs.
		base[0] = tempStrength;
		base[1] = tempMagic;
		base[2] = tempConstitution;
		base[3] = tempIntelligence;
		
		//Update them.
		tempStrength = (int) (getTotalStrength() * buffStrength);
		tempMagic = (int) (getTotalMagic() * buffStrength);
		tempConstitution = (int) (getTotalConstitution() * buffStrength);
		tempIntelligence = (int) (getTotalIntelligence() * buffStrength);
		
		//Message lib.
		msgLib.standardMessage("The support spell has been applied to you!");
		
		//Recalculate health and mana.
		calcMaxHM();
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{
			@Override
			public void run()
			{
				//Revert player stats.
				tempStrength = base[0];
				tempMagic = base[1];
				tempConstitution = base[2];
				tempIntelligence = base[3];
				
				//Recalculate health and mana.
				calcMaxHM();
				
				//Remove support buff status.
				isSupportBuffed = false;
				
				//Message the player.
				msgLib.standardMessage("Support buff has worn off");
			}
		}, 400);
	}
	
	//Remember when changing this to change the RPGPLAYERFILE version! -> calculateHealthAndManaOffline()
	public void calcMaxHM()
	{
		maxHealth = 100 + getTotalConstitution() * 20;
		maxMana = 20 + getTotalIntelligence() * 1;
	}
	
	public void heal(double d)
	{
		//Update health and mana.
		calcMaxHM();
		
		restoreHealth(d);
		restoreMana(d);
	}
	
	public void healFull()
	{
		//Update health and mana.
		calcMaxHM();
		
		curHealth = maxHealth;
		curMana = maxMana;
	}
	
	public double dealDamage(double damage)
	{
		//Update health and mana.
		calcMaxHM();
		
		curHealth -= damage;
		
		lastDamaged = new Date();
		
		return curHealth;
	}
	
	private boolean drainMana(int amount)
	{
		//Update health and mana.
		calcMaxHM();
		
		if (curMana >= amount)
		{
			curMana -= amount;
			return true;
		}
		else
			return false;
	}
	
	public void restoreManaTick()
	{
		//Update health and mana.
		calcMaxHM();
		
		//5% mana regeneration per 5 seconds (1% per sec)
		restoreMana(maxMana * .05);
		
		//Attempt to restore mana.
		if (curMana > maxMana)
			curMana = maxMana;
	}
	
	public void restoreHealth(double amount) 
	{
		//Update health and mana.
		calcMaxHM();
		
		//Actually add in new amount.
		curHealth = curHealth + amount;
		
		//If new health is equal to 
		if (curHealth > maxHealth)
			curHealth = maxHealth;
	}
	
	public void restoreMana(double amount) 
	{
		//Update health and mana.
		calcMaxHM();
		
		//Actually add in new amount.
		curMana = curMana + amount;
		
		//If new health is equal to 
		if (curMana > maxMana)
			curMana = maxMana;
	}
	
	public double getPromotionCost()
	{
		switch (getJobRank())
		{
		case 1:
			return 3200;
		case 2:
			return 20000;
		case 3:
			return 55000;
		case 4:
			return 105000;
		case 5:
			return 175000;
		}
		
		return 0;
	}
	
	//Return stat points to a player.
	public void respec()
	{
		int stats = getStats();
		
		stats += getStrength();
		stats += getConstitution();
		stats += getMagic();
		stats += getIntelligence();
		
		setStrength(0);
		setConstitution(0);
		setMagic(0);
		setIntelligence(0);
		
		setStats(stats);
	}
	
	public boolean useStats(int stat, int amount)
	{
		//We make sure first that the player isn't trying to use more stats than they have.
		//if they are fine, then we subtract the amount from the amount of stats that the player has.
		if (amount > getStats() || amount < 1)
		{
			return false;
		}
		
		setStats(getStats() - amount);
		
		//Then we add the stat points to the player.
		switch (stat)
		{
			case 1:
				setStrength(getStrength() + amount);
				break;
			case 2:
				setConstitution(getConstitution() + amount);
				break;
			case 3:
				setMagic(getMagic() + amount);
				break;
			case 4:
				setIntelligence(getIntelligence() + amount);
				break;
		}
		
		//Handle donator stat updates
		donatorStatUpdate();
		
		//Calculate health and mana.
		calcMaxHM();
		
		return true;
	}
	
	public void attemptAttackNotification(int level, double health, double damage)
	{
		Date now = new Date();
        DecimalFormat df = new DecimalFormat("#.#");
		String[] msg = new String[8];
		
		if ((now.getTime() - getLastAttackNotificationLong()) > 3000)
		{
			msg[0] = "[";
			msg[1] = "You Hit";
			msg[2] = "] Level: ";
			msg[3] = String.valueOf(level);
			msg[4] = " / Remaining Health: ";
			msg[5] = df.format(health);
			msg[6] = " / Damage: ";
			msg[7] = String.valueOf(df.format(damage));
			
			//Send the message.
			msgLib.standardMessage(msg);
			
			updateLastAttackNotification();
		}
	}
	
	public void attemptDefenseNotification(double damage)
	{
		Date now = new Date();
        DecimalFormat df = new DecimalFormat("#.#");
		String[] msg = new String[6];
		
		if ((now.getTime() - getLastDefenseNotificationLong()) > 3000)
		{
			msg[0] = "[";
			msg[1] = "You Got Hit";
			msg[2] = "] Damage: ";
			msg[3] = String.valueOf(df.format(damage));
			msg[4] = " / Remaining Health: ";
			msg[5] = String.valueOf(df.format(curHealth));

			msgLib.standardMessage(msg);
			
			updateLastDefenseNotification();
		}
	}
	
	public void attemptCastNotification(String spellName)
	{
        DecimalFormat df = new DecimalFormat("#.#");
		String[] msg = new String[6];
		
		msg[0] = "[";
		msg[1] = "Spell Cast";
		msg[2] = "] Name: ";
		msg[3] = spellName;
		msg[4] = " / Remaining Mana: ";
		msg[5] = df.format(curMana);
		
		msgLib.standardMessage(msg);
	}
	
	public void attemptHealNotification(RpgPlayer healTarget)
	{
        DecimalFormat df = new DecimalFormat("#.#");
		String[] msg = new String[9];
        
		msg[0] = "[";
		msg[1] = "Healed";
		msg[2] = "] Remaining Mana: ";
		msg[3] = String.valueOf(curMana);
		msg[4] = " / [Target Health]: (";
		msg[5] = ChatColor.GREEN + df.format(healTarget.getCurHealth());
		msg[6] = "/";
		msg[7] = ChatColor.GREEN + df.format(healTarget.getMaxHealth());
		msg[8] = ")";

		msgLib.standardMessage(msg);
	}
	
	public void sendMonsterDeathNotification(int level, double exp, double loot)
	{
        DecimalFormat df = new DecimalFormat("#.#");
		String[] msg = new String[8];
        
		msg[0] = "[";
		msg[1] = "Mob Slain";
		msg[2] = "] Level: ";
		msg[3] = String.valueOf(level);
		msg[4] = " / Experience: ";
		msg[5] = df.format(exp);
		msg[6] = " / Money: ";
		msg[7] = df.format(loot);
		
		msgLib.standardMessage(msg);
	}
	
	public void attemptDamageAvoidNotification(boolean isImmortal)
	{
		Date now = new Date();
		String type = "avoided";
		
		if (isImmortal == true)
			type = "negated";
		
        if ((now.getTime() - lastDodgeNotification.getTime()) > 500)
		{
        	msgLib.standardMessage("You have just " + type + " damage.");
        	lastDodgeNotification = new Date();
		}
	}
	
	public void attemptFeedSteak()
	{
		Date now = new Date();
		boolean addToInventory = false;
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 5);
		WorldManager wm = new WorldManager();
		ConfigOverlord co = new ConfigOverlord();
		
		//If hourly steak is disabled don't feed hourly steak.
		if (co.getHourlySteak() == false)
			return;
		
		//Only give steak in rpg world.
		if (!wm.getIsRpgWorld(player.getWorld().getName()))
			return;
		
		//Wait an hour before giving steak again.
		if (now.getTime() - getLastAte() > 3600000)
		{
			//We want to determine whether to drop the steak or to not.
			for (ItemStack item : player.getInventory())
			{
				if (item == null)
				{
					addToInventory = true;
					break;
				}
				//If air, or enough room from steak, then add to inventory.
				else if (item.getType() == Material.AIR)
				{
					addToInventory = true;
					break;
				}
				else if (item.getType() == Material.COOKED_BEEF)
				{
					if (item.getAmount() < 60)
					{
						addToInventory = true;
						break;
					}
				}
			}
			
			//Tell the player about their steak and drop/add it.
			if (addToInventory == true)
			{
				msgLib.standardMessage("Your hourly steak has been added to your inventory! :D");
				player.getInventory().addItem(steak);
			}
			else
			{
				msgLib.standardMessage("Your hourly steak has been dropped by you! :D");
				Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), steak);
			}
			
			//Update last ate time.
			setLastAte(now.getTime());
		}
	}
	
	public void attemptPayHunter()
	{
		Date now = new Date();
		
		//If it isn't time yet, don't pay them.
		if (now.getTime() - getHunterLastPay() < 3600000)
			return;
		
		//Else we want to give them money as well as experience.
		if (getHunterLevel() == 1)
		{
			FC_Rpg.economy.depositPlayer(name, 200 * getJobRank());
			addClassExperience(100 * getJobRank(), true);
			msgLib.standardMessage("You have just recieved your Hunter pay.");
		}
		else if (getHunterLevel() == 2)
		{
			FC_Rpg.economy.depositPlayer(name, 500 * getJobRank());
			addClassExperience(200 * getJobRank(), true);
			msgLib.standardMessage("You have just recieved your Predator pay.");
		}
		else if (getHunterLevel() == 3)
		{
			FC_Rpg.economy.depositPlayer(name, 5000);
			msgLib.standardMessage("You have just recieved your Terror pay.");
		}
		
		setHunterLastPay(now.getTime());
	}
	
	public void swordCheck()
	{
		//Variable Declarations
		ItemStack heldItem = player.getItemInHand();
		Material handItemType = heldItem.getType();
		short handItemDurability = 0;
		
		boolean success = true;
		
		if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null)
			handItemDurability = player.getInventory().getItem(player.getInventory().getHeldItemSlot()).getDurability();
		
		if (handItemType == Material.WOOD_SWORD)
		{
			if (handItemDurability >= 56)
			{
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			}
		}
		
		if (handItemType == Material.STONE_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 128)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (getStrength() < 125)
			{
				success = false;
				msgLib.standardMessage("Without 125+ Attack, Stone Swords Are USELESS TO YOU!");
			}
		}
		
		if (handItemType == Material.IRON_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 247)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (getStrength() < 250)
			{
				success = false;
				msgLib.standardMessage("Without 250+ Attack, Iron Swords Are USELESS TO YOU!");
			}
		}
	
		if (handItemType == Material.DIAMOND_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 1558)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (getStrength() < 375)
			{
				success = false;
				msgLib.standardMessage("Without 375+ Attack, Diamond Swords Are USELESS TO YOU!");
			}
		}
		
		if (handItemType == Material.GOLD_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 27)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (getStrength() < 500)
			{
				success = false;
				msgLib.standardMessage("Without 500+ Attack, Gold Swords Are USELESS TO YOU!");
			}
		}
		
		if (success == false)
		{
			player.getInventory().setItem(player.getInventory().getHeldItemSlot(), air);
			Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), heldItem);
		}
	}
	
	public void fullArmorCheck()
	{
		helmetCheck();
		chestCheck();
		leggingCheck();
		bootsCheck();
	}
	
	private boolean helmetCheck()
	{
		ItemStack helmet = player.getInventory().getHelmet();
		Material headType;
		boolean success = true;
		
		if (helmet == null)
			return true;
		else
			headType = helmet.getType();
		
		if (getConstitution() < 125)
		{
			if (headType == Material.CHAINMAIL_HELMET)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain Helmets!");
				success = false;
			}
		}
		
		if (getConstitution() < 250)
		{
			if (headType == Material.IRON_HELMET)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron Helmets!");
				success = false;
			}
		}
		
		if (getConstitution() < 375)
		{
			if (headType == Material.DIAMOND_HELMET)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond Helmets!");
				success = false;
			}
		}
		
		if (getConstitution() < 500)
		{
			if (headType == Material.GOLD_HELMET)
			{
				msgLib.standardMessage("You Need 500+ Constitution To Wear Gold Helmets!");
				success = false;
			}
		}
		
		if (success == false)
		{
			player.getInventory().setHelmet(air);
			Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), helmet);
		}
		
		return true;
	}
	
	private boolean chestCheck()
	{
		ItemStack chest = player.getInventory().getChestplate();
		Material chestType;
		boolean success = true;
		
		if (chest == null)
			return true;
		else
			chestType = chest.getType();
		
		if (getConstitution() < 125)
		{
			if (chestType == Material.CHAINMAIL_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain ChestPlates!");
				success = false;
			}
		}
		
		if (getConstitution() < 250)
		{
			if (chestType == Material.IRON_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron ChestPlates!");
				success = false;
			}
		}
		
		if (getConstitution() < 375)
		{
			if (chestType == Material.DIAMOND_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond ChestPlates!");
				success = false;
			}
		}
		
		if (getConstitution() < 500)
		{
			if (chestType == Material.GOLD_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 500+ Constitution To Wear Gold ChestPlates!");
				success = false;
			}
		}
		
		if (success == false)
		{
			player.getInventory().setChestplate(air);
			Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), chest);
		}
		
		return true;
	}
	
	private boolean leggingCheck()
	{
		ItemStack leggings = player.getInventory().getLeggings();
		Material leggingsType;
		boolean success = true;
		
		if (leggings == null)
			return true;
		else
			leggingsType = leggings.getType();
		
		if (getConstitution() < 125)
		{
			if (leggingsType == Material.CHAINMAIL_LEGGINGS)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain Leggings!");
				success = false;
			}
		}
		
		if (getConstitution() < 250)
		{
			if (leggingsType == Material.IRON_LEGGINGS)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron Leggings!");
				success = false;
			}
		}
		
		if (getConstitution() < 375)
		{
			if (leggingsType == Material.DIAMOND_LEGGINGS)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond Leggings!");
				success = false;
			}
		}
		
		if (getConstitution() < 500)
		{
			if (leggingsType == Material.GOLD_LEGGINGS)
			{
				msgLib.standardMessage("You Need 500+ Constitution To Wear Gold Leggings!");
				success = false;
			}
		}
		
		if (success == false)
		{
			player.getInventory().setLeggings(air);
			Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), leggings);
		}
		
		return true;
	}
	
	private boolean bootsCheck()
	{
		ItemStack boots = player.getInventory().getBoots();
		Material bootsType;
		boolean success = true;
		
		if (boots == null)
			return true;
		else
			bootsType = boots.getType();
		
		if (getConstitution() < 125)
		{
			if (bootsType == Material.CHAINMAIL_BOOTS)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain Boots!");
				success = false;
			}
		}
		
		if (getConstitution() < 250)
		{
			if (bootsType == Material.IRON_BOOTS)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron Boots!");
				success = false;
			}
		}
		
		if (getConstitution() < 375)
		{
			if (bootsType == Material.DIAMOND_BOOTS)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond Boots!");
				success = false;
			}
		}
		if (getConstitution() < 500)
		{
			if (bootsType == Material.GOLD_BOOTS)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Gold Boots!");
				success = false;
			}
		}
		
		if (success == false)
		{
			player.getInventory().setBoots(air);
			Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), boots);
		}
		
		return true;
	}
	
	public void updateTimePlayed()
	{
		//Variable Declarations
		Date now = new Date();
		Long timeDifference;
		int intDifference;
		int timePlayedInSeconds = getSecondsPlayed();
		
		//We want to first set how long we have played in the configuration file.
		timeDifference = now.getTime() - logonDate.getTime(); //Calulate how long we have been online.
		intDifference = (int) (timeDifference / 1000); //Convert that time to seconds.
		timePlayedInSeconds = timePlayedInSeconds + intDifference;	//Update time played.
		setSecondsPlayed(timePlayedInSeconds); //Store it
		
		//Update the logon date to now.
		logonDate = new Date(); //Update for future time player updates.
		
		//Perform a promotion check.
		promotionCheck(timePlayedInSeconds);
	}
	
	private void promotionCheck(int timePlayedInSeconds)
	{
		//Variable Declarations
		String newGroup = "";
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		String[] playerGroups = perms.getPlayerGroups();
		GroupManager gm = new GroupManager();
		int jobReq = 0;
		
		if (getIsActive() == false)
			return;
		
		if (getRankFreeze() == true)
			return;
		
		//If they have no group, make them a guest and return null.
		if (playerGroups == null)
		{
			perms.setPlayerGroup(gm.getGroups().get(0).getName());
			return;
		}
		
		for (Group group : gm.getGroups())
		{
			//Store for fast access later.
			jobReq = group.getJobReq();
			
			//If the jobReq is greater than -1.
			if (jobReq > -1)
			{
				//If they meet the time played requirements and the job rank requirements, promote them.
				if ((timePlayedInSeconds >= group.getTimeReq()) && (getJobRank() >= jobReq))
				{
					newGroup = group.getName();
					break;
				}
			}
		}
		
		//If the player is already in a group, return.
		if (playerGroups[0].equals(newGroup))
			return;
		
		//Broadcast Promotion.
		FC_Rpg.bLib.standardBroadcast(name + " has been promoted to " + ChatColor.GREEN + newGroup);
		
		//Add the player to the new group.
		perms.setPlayerGroup(newGroup);
	}
	
	public boolean isDonatorOrAdmin() 
	{
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		if (isDonator() == true)
			return true;
		
		if (perms.isAdmin() == true)
			return true;
		
		return false;
	}
	
	public boolean isHunterOrAdmin() 
	{
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		if (getHunterLevel() > 0)
			return true;
		
		if (perms.isAdmin() == true)
			return true;
		
		return false;
	}
	
	public boolean prepareSpell(boolean displaySuccess)
	{
		int spellNumber = -1;
		int spellLevel;
		
		//Make sure spell is valid.
		for (int i = 0; i < SpellUtil.TOTAL_SPELL_COUNT; i++)
		{
			if (getSpellBind(i) == player.getItemInHand().getTypeId())
			{
				spellNumber = i;
				break;
			}
		}
		
		//If no valid spell was selected based on skill bind, then we just return damage.
		if (spellNumber == -1)
			return false;
		
		//Set the spell tier
		spellLevel = getSpellLevel(spellNumber);
		
		//We want to return if they don't have any points in the skill.
		if (spellLevel < 1)
		{
			msgLib.standardError("You must levelup the spell once before you can use it.");
			return false;
		}
		
		//Set the active spell.
		setActiveSpell(util.getSpellName(getCombatClass(), spellNumber));
		
		if (displaySuccess == true)
			msgLib.standardMessage("Successfully prepared spell.");
		
		return true;
	}
	
	public boolean hasEnoughMana(int spellNumber, int spellLevel)
	{
		if (curMana > util.getSpellManaCost(getCombatClass(), spellNumber, spellLevel))
			return true;
		
		return false;
	}
	
	public void attemptNoManaNotification(int spellNumber, int spellLevel)
	{
		RpgMessageLib rpgLib = new RpgMessageLib(player);
		
		drainMana(util.getSpellManaCost(getCombatClass(), spellNumber, spellLevel));
		
		rpgLib.errorOutOfMana();
	}
	
	public double calculateBonusEnchantmentDefense(Player player, Enchantment enchant, double damage)
	{
		damage = damage * getEnchantmentDefenseBonuses(player, enchant);
		
		return damage;
	}
	
	public double getEnchantmentDefenseBonuses(Player player, Enchantment enchant)
	{
		double bonus = 1;
		PlayerInventory inv = player.getInventory();
		
		if (inv.getHelmet() != null)
			bonus = getDefenseBonuses(inv.getHelmet(), bonus, enchant);
		
		if (inv.getChestplate() != null)
			bonus = getDefenseBonuses(inv.getChestplate(), bonus, enchant);
		
		if (inv.getHelmet() != null)
			bonus = getDefenseBonuses(inv.getHelmet(), bonus, enchant);
		
		if (inv.getBoots() != null)
			bonus = getDefenseBonuses(inv.getBoots(), bonus, enchant);

		return bonus;
	}
	
	 public double getWeaponModifier(Material weapon, int strength)
	{
		if (weapon.equals(Material.WOOD_SWORD))
		{
			return 1.25;
		}
		else if (weapon.equals(Material.STONE_SWORD))
		{
			if (strength > 124)
				return 1.5;
		}
		else if (weapon.equals(Material.IRON_SWORD))
		{
			if (strength > 249)
				return 1.75;
		}
		else if (weapon.equals(Material.DIAMOND_SWORD))
		{
			if (strength > 374)
				return 2;
		}
		else if (weapon.equals(Material.GOLD_SWORD))
		{
			if (strength > 499)
				return 2.5;
		}

		return 1;
	}
	
	private double getDefenseBonuses(ItemStack armorPiece, double bonus, Enchantment enchant)
	{
		final double baseProtectionPercentPerPoint = 0.0025;
		
		if (armorPiece.containsEnchantment(enchant))
			bonus = bonus - (baseProtectionPercentPerPoint * armorPiece.getEnchantmentLevel(enchant));
		
		return bonus;
	}
	
	public double getEnchantmentOffensiveBonuses(Enchantment enchant)
	{
		double bonus = 1;
		PlayerInventory inv = player.getInventory();
		
		if (inv.getItemInHand() != null)
		{
			if (inv.getItemInHand().getType() == Material.WOOD_SWORD)
				bonus = getOffensiveBonuses(inv.getItemInHand(), bonus, enchant);
			else if (inv.getItemInHand().getType() == Material.STONE_SWORD)
				bonus = getOffensiveBonuses(inv.getItemInHand(), bonus, enchant);
			else if (inv.getItemInHand().getType() == Material.IRON_SWORD)
				bonus = getOffensiveBonuses(inv.getItemInHand(), bonus, enchant);
			else if (inv.getItemInHand().getType() == Material.DIAMOND_SWORD)
				bonus = getOffensiveBonuses(inv.getItemInHand(), bonus, enchant);
			else if (inv.getItemInHand().getType() == Material.GOLD_SWORD)
				bonus = getOffensiveBonuses(inv.getItemInHand(), bonus, enchant);
		}
		
		return bonus;
	}
	
	private double getOffensiveBonuses(ItemStack armorPiece, double bonus, Enchantment enchant)
	{
		final double baseProtectionPercentPerPoint = 0.005;
		
		if (armorPiece.containsEnchantment(enchant))
			bonus = bonus + (baseProtectionPercentPerPoint * armorPiece.getEnchantmentLevel(enchant));
		
		return bonus;
	}
	
	public double getMissingHealthDecimal()
	{
		//Update health and mana.
		calcMaxHM();
		
		double percent = curMana * 100 / maxHealth;
		
		//We have to inverse percent for lower hp = more damage.
		percent = (percent - 100) * -1;
		percent = percent / 100;
		
		return percent;
	}
	
	//Cast a spell, returns true on successful casts and false on failed casts.
	public double castSpell(LivingEntity target, double damage, int damageType)
	{
		//Variable Declarations
		SpellCastManager scm = new SpellCastManager();
		int combatClass = getCombatClass();
		boolean success = false;
		
		//Initiates the spell cast.
		if (scm.init_spellCast(this, target, damage, damageType) == false)
			return damage;
		
		//Handle spells for each class based on class.
		if (combatClass == FC_Rpg.c_int_swordsman)
			success = scm.cast_Swordsman();
		else if (combatClass == FC_Rpg.c_int_assassin)
			success = scm.cast_Assassin();
		else if (combatClass == FC_Rpg.c_int_defender)
			success = scm.cast_Defender();
		else if (combatClass == FC_Rpg.c_int_wizard)
			success = scm.cast_Wizard();
		else if (combatClass == FC_Rpg.c_int_wizard)
			success = scm.cast_Berserker();
		
		//Show effects on spells when requested.
		if (success == true)
		{
			//Play a visual effect.
			Bukkit.getServer().getWorld(player.getWorld().getName()).playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 0);
			Bukkit.getServer().getWorld(player.getWorld().getName()).playEffect(player.getLocation().add(0.0D, 1, 0.0D), Effect.ENDER_SIGNAL, 0);
			Bukkit.getServer().getWorld(player.getWorld().getName()).playEffect(player.getLocation().add(0.0D, 2, 0.0D), Effect.ENDER_SIGNAL, 0);
			
			//Creative don't lose mana.
			if (!(player.getGameMode() == GameMode.CREATIVE))
				drainMana(scm.getManaCost());	//Drain the mana
		}
		
		if (scm.getDamage() > 0)
			return scm.getDamage();
		else
			return damage;
	}
}


/*

Very mathematical function
The first part is a constant amount of experience that will be recieved based on level.
That constant is then multiplied by a scaling amount consistent with expected required hours
of playtime to get to the next level.

((level * 10) * 360) * (((level * 13.2) / 100) + 1) -> Gets simplified to below

1 hour - 7.2 hours - 14.4 hours

Level 1:
	- 10 exp a kill
	- 10 seconds a kill
	- 60 exp per minute
	- 3600 exp an hour
	- 3600 exp for 1.0 hours to levelup
	
Level 50:
	- 500 exp a kill
	- 10 seconds a kill
	- 3000 exp a minute
	- 180,000 exp a hour
	- 1,296,000 exp for 7.2 hours to levelup
	
Level 100:
	- 1000 exp a kill
	- 10 seconds a kill
	- 6000 exp a minute
	- 360,000 exp a hour
	- 1,728,000 exp for 14.4 hours to levelup
	
Level x:
	- killExp = x * 10 exp a kill
	- 10 seconds to kill
	- hourExp = killExp * 360
	- hourExp * time to levelup
	
Experience needed equals:
	((level * 10) * 360) * (((level * 13.2) / 100) + 1)
	
1 = 1
100 = 14.4

 

hourExp = (level * 10) * 360

 */

/*


switch (jobRank)
		{
		case 1:
			jobLetter += "Investor";
			break;
		case 2:
			
			break;
		case 3:
			jobLetter += "Vendor";
			break;
		case 4:
			jobLetter += "Bargainer";
			break;
		case 5:
			jobLetter += "Merchant";
			break;
		case 6:
			jobLetter += "Manager";
			break;
		}
		
		
		switch (jobRank)
		{
		case 1:
			jobLetter += "Tiller";
			break;
		case 2:
			jobLetter += "Fisherman";
			break;
		case 3:
			jobLetter += "Waterer";
			break;
		case 4:
			jobLetter += "Farmer";
			break;
		case 5:
			jobLetter += "Cultivator";
			break;
		case 6:
			jobLetter += "Domesticator";
			break;
		}
		
		switch (jobRank)
		{
		case 1:
			jobLetter += "Designer";
			break;
		case 2:
			jobLetter += "Constructor";
			break;
		case 3:
			jobLetter += "Builder";
			break;
		case 4:
			jobLetter += "Inventor";
			break;
		case 5:
			jobLetter += "Architect";
			break;
		case 6:
			jobLetter += "Engineer";
			break;
		}
*/

/* - Code that may one day be useful, so just keep for now.
public int getTempStrength() { return tempStrength; }
public int getTempDefense() { return tempDefense; }
public int getTempConstitution() { return tempConstitution; }
public int getTempMagic() { return tempMagic; }
public int getTempIntelligence() { return tempIntelligence; }
public void setTempStrength(int x) { tempStrength = x; }
public void setTempDefense(int x) { tempDefense = x; }
public void setTempConstitution(int x) { tempConstitution = x; }
public void setTempMagic(int x) { tempMagic = x; }
public void setTempIntelligence(int x) { tempIntelligence = x; }

public long getLastLogin() { config = plugin.getConfig(); return config.getLong(playerPath + "lastLogin"); }
public void setLastLogin(long x) { config = plugin.getConfig(); config.set(playerPath + "lastLogin", x); plugin.saveConfig(); }
*/