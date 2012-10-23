package me.Destro168.Entities;

import java.util.Date;
import java.util.List;

import me.Destro168.LoadedObjects.Group;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Spells.SpellCaster;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.HealthConverter;
import me.Destro168.Util.RpgMessageLib;
import me.Destro168.Configs.GroupConfig;
import me.Destro168.Configs.PlayerFileConfig;
import me.Destro168.Configs.WorldConfig;
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

public class RpgPlayer extends RpgEntity
{
	private MessageLib msgLib;
	private ItemStack air;
	private Player player;
	private PlayerFileConfig playerConfigFile;
	private String prefix = "";
	private String name = "";
	private boolean isSupportBuffed;
	private int tempAttack;
	private int tempConstitution;
	private int tempMagic;
	private int tempIntelligence;
	private Date lastDodgeNotification;
	private Date lastThornsNotification;
	private Date lastHealNotification;
	private Date logonDate;
	private double curHealth;
	private double maxHealth;
	private double curMana;
	private double maxMana;
	
	//Gets
	public Player getPlayer() { return player; }
	public PlayerFileConfig getPlayerConfigFile() { return playerConfigFile; }
	
	//Only call twice, one when player is loaded, once when player deloads.
	public double getCurHealth() { return curHealth; }
	public double getMaxHealth() { return maxHealth; }
	public double getCurMana() { return curMana; }
	public double getMaxMana() { return maxMana; }
	
	//Functions that rely on file get/sets
	public int getTotalAttack() { return playerConfigFile.getAttack() + tempAttack; }
	public int getTotalConstitution() { return playerConfigFile.getConstitution() + tempConstitution; }
	public int getTotalMagic() { return playerConfigFile.getMagic() + tempMagic; }
	public int getTotalIntelligence() { return playerConfigFile.getIntelligence() + tempIntelligence; }
	
	//Constructor
	public RpgPlayer()
	{
		//Set default player information.
		setPlayerDefaults();
	}
	
	public RpgPlayer(Player player)
	{
		//Set the player information based on name.
		setPlayer(player);
	}
	
	public void setPlayerDefaults()
	{
		msgLib = null;
		air = new ItemStack(Material.AIR);
		player = null;
		playerConfigFile = null;
		prefix = "";
		isSupportBuffed = false;
		tempAttack = 0;
		tempConstitution = 0;
		tempMagic = 0;
		tempIntelligence = 0;
		lastDodgeNotification = new Date();
		lastThornsNotification = new Date();
		lastHealNotification = new Date();
		logonDate = new Date();
		curHealth = 0;
		maxHealth = 0;
		curMana = 0;
		maxMana = 0;
	}
    
	public void setPlayer(Player player_)
	{
		//Set player defaults
		setPlayerDefaults();
		
		//Set local variable information based on player.
		player = player_;
		
		//Create the message lib.
		msgLib = new MessageLib(player);
		
		//Create the player config
		playerConfigFile = new PlayerFileConfig(player.getName());
		
    	//Update player health hud.
		loadCriticalInformation();
		
		//Update the prefix.
		updatePrefix();
		
    	HealthConverter hc = new HealthConverter(maxHealth, curHealth);
    	
    	WorldConfig wm = new WorldConfig();
    	
    	if (wm.getIsRpgWorld(name) == true)
    		player_.setHealth(hc.getMinecraftHearts());
		
		//Check and apply donator bonuses
		donatorStatUpdate();
	}
	
	public void loadCriticalInformation()
	{
		curMana = playerConfigFile.getCurManaFile();
		maxMana = playerConfigFile.getMaxManaFile();
		curHealth = playerConfigFile.getCurHealthFile();
		maxHealth = playerConfigFile.getMaxHealthFile();
		name = playerConfigFile.getName();
	}
	
	public void dumpCriticalInformation()
	{
		playerConfigFile.setCurManaFile(curMana);
		playerConfigFile.setMaxManaFile(maxMana);
		playerConfigFile.setCurHealthFile(curHealth);
		playerConfigFile.setMaxHealthFile(maxHealth);
	}
	
	public void createPlayerRecord(Player player_, int pickedClass, boolean manualDistribution)
	{
		//Store player
		setPlayer(player_);
		
		//Set the files defaults.
		playerConfigFile.setPlayerDefaults(pickedClass, manualDistribution);
		
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
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		GroupConfig gm = new GroupConfig();
		
		String[] playerGroups;
		String customPrefix = playerConfigFile.getCustomPrefix();
		String defaultPrefix = FC_Rpg.generalConfig.getDefaultPrefix();
		String parsedDefault = cl.parseColors(defaultPrefix);
		
		//Check the customPrefix first.
		if (customPrefix == null)
		{
			playerConfigFile.setCustomPrefix(defaultPrefix);
			prefix = parsedDefault;
			return prefix;
		}
		else if (customPrefix.equals(""))
		{
			playerConfigFile.setCustomPrefix(defaultPrefix);
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
		if (playerConfigFile.isDonator() == true)
			prefix = ChatColor.WHITE + "["+ ChatColor.YELLOW + "Donator" + ChatColor.WHITE + "]";
		
		return prefix;
	}
	
	public void setDonator(int periods)
	{
		playerConfigFile.offlineSetDonator(periods);
		
		//Update stats to account for new bonuses if a donator.
		donatorStatUpdate();
	}
	
	public void addClassExperience(double x, boolean displayLevelUpMessage)
	{
		//Add the class experience
		playerConfigFile.addOfflineClassExperience(x, displayLevelUpMessage);
		
		//Update Donator Stats
		donatorStatUpdate();
		
		//Message the player a reminder to use stat points.
		if (playerConfigFile.getManualAllocation() == false)
			msgLib.standardMessage("Remember to assign stat points. Use /class for help!");
	}
	
	public void donatorStatUpdate()
	{
		double donatorBonusPercent = FC_Rpg.generalConfig.getDonatorBonusStatPercent();
		
		if (playerConfigFile.isDonator() == true)
		{
			resetTempStats();
			
			tempAttack = (int) (playerConfigFile.getAttack() * donatorBonusPercent);
			tempConstitution = (int) (playerConfigFile.getConstitution() * donatorBonusPercent);
			tempMagic = (int) (playerConfigFile.getMagic() * donatorBonusPercent);
			tempIntelligence = (int) (playerConfigFile.getIntelligence() * donatorBonusPercent);
			
			calcMaxHM();
		}
	}
	
	public void resetTempStats()
	{
		//Reset temp stats.
		tempAttack = 0;
		tempConstitution = 0;
		tempMagic = 0;
		tempIntelligence = 0;
	}
	
	public boolean hasClassChangeTicket()
	{
		//If they have no more tickets, return false.
		if (playerConfigFile.getClassChangeTickets() == 0)
			return false;
		
		return true;
	}
	
	public boolean switchClass(String x)
	{
		//Variable Declaration.
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		boolean isAdmin = perms.isAdmin();
		int cNumber = -1;
		
		//If they have no class tickets and aren't an admin, we return no permission.
		if (hasClassChangeTicket() == false && !isAdmin)
		{
			msgLib.errorNoPermission();
			return false;
		}

		try { cNumber = Integer.valueOf(x); }
		catch (NumberFormatException e)
		{
			msgLib.standardMessage("Invalid Class Number.");
			return true;
		}
		
		//Make sure the new class is different from current class.
		if (getPlayerConfigFile().getCombatClass() == cNumber)
		{
			msgLib.standardMessage("You Can't Switch To The Class You Are Currently.");
			return true;
		}
		
		//Make sure that the user is only picking from proper classes.
		if (cNumber < 0 || cNumber > FC_Rpg.classConfig.getRpgClasses().length)
			return msgLib.errorInvalidCommand();
		
		//Chance the combat class.
		playerConfigFile.setCombatClass(cNumber);
		
		//If not an admin we decrease class change tickets.
		if (isAdmin == false)
			playerConfigFile.setClassChangeTickets(playerConfigFile.getClassChangeTickets() - 1);
		
		//Return true for success.
		return true;
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
		base[0] = tempAttack;
		base[1] = tempMagic;
		base[2] = tempConstitution;
		base[3] = tempIntelligence;
		
		//Update them.
		tempAttack = (int) (getTotalAttack() * buffStrength);
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
				tempAttack = base[0];
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
	
	private boolean drainMana(double d)
	{
		//Update health and mana.
		calcMaxHM();
		
		if (curMana >= d)
		{
			curMana -= d;
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
		return FC_Rpg.generalConfig.getJobRankCosts().get(playerConfigFile.getJobRank() - 1);
	}
	
	//Return stat points to a player.
	public void respecAll()
	{
		respecStats();
		respecSkills();
	}
	
	public void respecStats()
	{
		//Reset stats
		int stats = playerConfigFile.getStats();
		
		stats += playerConfigFile.getAttack();
		stats += playerConfigFile.getConstitution();
		stats += playerConfigFile.getMagic();
		stats += playerConfigFile.getIntelligence();
		
		playerConfigFile.setAttack(0);
		playerConfigFile.setConstitution(0);
		playerConfigFile.setMagic(0);
		playerConfigFile.setIntelligence(0);
		
		playerConfigFile.setStats(stats);
	}
	
	public void respecSkills()
	{
		int spellPoints = playerConfigFile.getSpellPoints();
		
		for (int i = 0; i < playerConfigFile.getRpgClass().getSpellBook().size(); i++)
		{
			spellPoints += playerConfigFile.getSpellLevel(i);
			playerConfigFile.setSpellLevel(i, 0);
		}
		
		playerConfigFile.setSpellPoints(spellPoints);
	}
	
	public boolean useStats(int stat, int amount)
	{
		//We make sure first that the player isn't trying to use more stats than they have.
		//if they are fine, then we subtract the amount from the amount of stats that the player has.
		if (amount > playerConfigFile.getStats() || amount < 1)
		{
			return false;
		}
		
		playerConfigFile.setStats(playerConfigFile.getStats() - amount);
		
		//Then we add the stat points to the player.
		switch (stat)
		{
			case 1:
				playerConfigFile.setAttack(playerConfigFile.getAttack() + amount);
				break;
			case 2:
				playerConfigFile.setConstitution(playerConfigFile.getConstitution() + amount);
				break;
			case 3:
				playerConfigFile.setMagic(playerConfigFile.getMagic() + amount);
				break;
			case 4:
				playerConfigFile.setIntelligence(playerConfigFile.getIntelligence() + amount);
				break;
		}
		
		//Handle donator stat updates
		donatorStatUpdate();
		
		//Calculate health and mana.
		calcMaxHM();
		
		return true;
	}
	
	private boolean getCanNotify(Date time)
	{
		Date now = new Date();
		int NotificationRepeatInterval = 2000;
		
		if ((now.getTime() - time.getTime()) < NotificationRepeatInterval)
			return false;
		
		return true;
	}
	
	public void attemptAttackNotification(int level, double health, double damage)
	{
		if (getCanNotify(lastAttackNotification) == false)
			return;
		
		String[] msg = new String[8];
		
		msg[0] = "[";
		msg[1] = "You Hit";
		msg[2] = "] Level: ";
		msg[3] = String.valueOf(level);
		msg[4] = " / Remaining Health: ";
		msg[5] = FC_Rpg.df.format(health);
		msg[6] = " / Damage: ";
		msg[7] = String.valueOf(FC_Rpg.df.format(damage));
		
		msgLib.standardMessage(msg);
		lastAttackNotification = new Date();
	}
	
	public void attemptDefenseNotification(double damage)
	{
		if (getCanNotify(lastDefenseNotification) == false)
			return;
		
		String[] msg = new String[6];
		
		msg[0] = "[";
		msg[1] = "You Got Hit";
		msg[2] = "] Damage: ";
		msg[3] = String.valueOf(FC_Rpg.df.format(damage));
		msg[4] = " / Remaining Health: ";
		msg[5] = String.valueOf(FC_Rpg.df.format(curHealth));

		msgLib.standardMessage(msg);
		lastDefenseNotification = new Date();
	}
	
	public void attemptCastNotification(String spellName)
	{
		String[] msg = new String[6];
		
		msg[0] = "[";
		msg[1] = "Spell Cast";
		msg[2] = "] Name: ";
		msg[3] = spellName;
		msg[4] = " / Remaining Mana: ";
		msg[5] = FC_Rpg.df.format(curMana);
		
		msgLib.standardMessage(msg);
	}
	
	public void attemptHealOtherNotification(RpgPlayer healTarget)
	{
		String[] msg = new String[9];
        
		msg[0] = "[";
		msg[1] = "Healed Other";
		msg[2] = "] Remaining Mana: ";
		msg[3] = String.valueOf(curMana);
		msg[4] = " / [Target Health]: (";
		msg[5] = ChatColor.GREEN + FC_Rpg.df.format(healTarget.getCurHealth());
		msg[6] = "/";
		msg[7] = ChatColor.GREEN + FC_Rpg.df.format(healTarget.getMaxHealth());
		msg[8] = ")";

		msgLib.standardMessage(msg);
	}
	
	public void attemptHealSelfNotification(double healAmount)
	{
		if (getCanNotify(lastHealNotification) == false)
			return;
		
		String[] msg = new String[7];
        
		msg[0] = "[";
		msg[1] = "Heal";
		msg[2] = "] Health: (";
		msg[3] = ChatColor.GREEN + FC_Rpg.df.format(getCurHealth());
		msg[4] = "/";
		msg[5] = ChatColor.GREEN + FC_Rpg.df.format(getMaxHealth());
		msg[6] = ")";

		msgLib.standardMessage(msg);
		lastHealNotification = new Date();
	}
	
	public void sendMonsterDeathNotification(int level, double exp, double loot)
	{
		String[] msg = new String[8];
        
		msg[0] = "[";
		msg[1] = "Mob Slain";
		msg[2] = "] Level: ";
		msg[3] = String.valueOf(level);
		msg[4] = " / Experience: ";
		msg[5] = FC_Rpg.df.format(exp);
		msg[6] = " / Money: ";
		msg[7] = FC_Rpg.df.format(loot);
		
		msgLib.standardMessage(msg);
	}
	
	public void attemptDamageAvoidNotification(boolean isImmortal)
	{
		if (getCanNotify(lastDodgeNotification) == false)
			return;
		
		String type = "Dodged Damage";
		String[] msg = new String[3];
		
		if (isImmortal == true)
			type = "Damage Immune";
		
		msg[0] = "[";
		msg[1] = type;
		msg[2] = "] Damage Avoided!";
		
		msgLib.standardMessage(msg);
		lastDodgeNotification = new Date();
	}
	
	public void attemptThornsNotification(double damage)
	{
		if (getCanNotify(lastThornsNotification) == false)
			return;
		
		String[] msg = new String[4];
		
		msg[0] = "[";
		msg[1] = "Thorns";
		msg[2] = "] Damage: ";
		msg[3] = FC_Rpg.df.format(damage);
		
    	msgLib.standardMessage(msg);
    	lastThornsNotification = new Date();
	}
	
	public void attemptFeedSteak()
	{
		Date now = new Date();
		WorldConfig wm = new WorldConfig();
		List<ItemStack> timedItems = FC_Rpg.generalConfig.getTimedItems();
		String itemText = "item";
		
		//If there are no hourly items, then return.
		if (timedItems.size() == 0)
			return;
		if (timedItems.size() > 1)
			itemText += "s";
		
		//Only give steak in rpg world.
		if (!wm.getIsRpgWorld(player.getWorld().getName()))
			return;
		
		//Wait an hour before giving steak again.
		if (now.getTime() - playerConfigFile.getLastRecievedHourlyItems() > FC_Rpg.generalConfig.getTimedItemsInterval())
		{
			for (ItemStack hourlyItem : timedItems)
			{
				//Tell the player about their steak and drop/add it.
				if (hasEmptyInventorySlot(hourlyItem.getType(), hourlyItem.getAmount()))
					player.getInventory().addItem(hourlyItem);
				else
					Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), hourlyItem);
			}
			
			msgLib.standardMessage("Your hourly " + itemText + " have just been given to you!");
			
			//Update last ate time.
			playerConfigFile.setLastRecievedHourlyItems(now.getTime());
		}
	}
	
	private boolean hasEmptyInventorySlot(Material itemType, int itemCount)
	{
		//We want to determine whether to drop the steak or to not.
		for (ItemStack item : player.getInventory())
		{
			if (item == null)
				return true;
			
			//If air, or enough room from steak, then add to inventory.
			else if (item.getType() == Material.AIR)
				return true;
			
			else if (item.getType() == itemType)
			{
				if (item.getAmount() < (64 - itemCount))
				{
					return true;
				}
			}
		}
		
		return false;
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
			
			if (playerConfigFile.getAttack() < 125)
			{
				success = false;
				msgLib.standardMessage("Without 125+ Attack, Stone Swords Are USELESS TO YOU!");
			}
		}
		
		if (handItemType == Material.IRON_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 247)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (playerConfigFile.getAttack() < 250)
			{
				success = false;
				msgLib.standardMessage("Without 250+ Attack, Iron Swords Are USELESS TO YOU!");
			}
		}
	
		if (handItemType == Material.DIAMOND_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 1558)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (playerConfigFile.getAttack() < 375)
			{
				success = false;
				msgLib.standardMessage("Without 375+ Attack, Diamond Swords Are USELESS TO YOU!");
			}
		}
		
		if (handItemType == Material.GOLD_SWORD)
		{
			if (player.getItemInHand().getDurability() >= 27)
				player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setDurability((short) 0);
			
			if (playerConfigFile.getAttack() < 500)
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
		
		if (playerConfigFile.getConstitution() < 125)
		{
			if (headType == Material.CHAINMAIL_HELMET)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain Helmets!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 250)
		{
			if (headType == Material.IRON_HELMET)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron Helmets!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 375)
		{
			if (headType == Material.DIAMOND_HELMET)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond Helmets!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 500)
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
		
		if (playerConfigFile.getConstitution() < 125)
		{
			if (chestType == Material.CHAINMAIL_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain ChestPlates!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 250)
		{
			if (chestType == Material.IRON_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron ChestPlates!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 375)
		{
			if (chestType == Material.DIAMOND_CHESTPLATE)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond ChestPlates!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 500)
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
		
		if (playerConfigFile.getConstitution() < 125)
		{
			if (leggingsType == Material.CHAINMAIL_LEGGINGS)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain Leggings!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 250)
		{
			if (leggingsType == Material.IRON_LEGGINGS)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron Leggings!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 375)
		{
			if (leggingsType == Material.DIAMOND_LEGGINGS)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond Leggings!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 500)
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
		
		if (playerConfigFile.getConstitution() < 125)
		{
			if (bootsType == Material.CHAINMAIL_BOOTS)
			{
				msgLib.standardMessage("You Need 125+ Constitution To Wear Chain Boots!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 250)
		{
			if (bootsType == Material.IRON_BOOTS)
			{
				msgLib.standardMessage("You Need 250+ Constitution To Wear Iron Boots!");
				success = false;
			}
		}
		
		if (playerConfigFile.getConstitution() < 375)
		{
			if (bootsType == Material.DIAMOND_BOOTS)
			{
				msgLib.standardMessage("You Need 375+ Constitution To Wear Diamond Boots!");
				success = false;
			}
		}
		if (playerConfigFile.getConstitution() < 500)
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
		int timePlayedInSeconds = playerConfigFile.getSecondsPlayed();
		
		//We want to first set how long we have played in the configuration file.
		timeDifference = now.getTime() - logonDate.getTime(); //Calulate how long we have been online.
		intDifference = (int) (timeDifference / 1000); //Convert that time to seconds.
		timePlayedInSeconds = timePlayedInSeconds + intDifference;	//Update time played.
		playerConfigFile.setSecondsPlayed(timePlayedInSeconds); //Store it
		
		//Update the logon date to now.
		logonDate = new Date(); //Update for future time player updates.
		
		//Perform a promotion check.
		promotionCheck(timePlayedInSeconds);
	}
	
	private void promotionCheck(int timePlayedInSeconds)
	{
		//Don't continue if player isn't active.
		if (playerConfigFile.getIsActive() == false)
			return;
		
		//Variable Declarations
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		GroupConfig gm = new GroupConfig();
		String[] playerGroups = perms.getPlayerGroups();
		List<Group> groupList = gm.getGroups();
		Group chosenGroup = null;
		String newGroup = "";
		int jobReq = 0;
		int timeReq = 0;
		
		//If they have no group, make them a guest and return null.
		if (playerGroups == null)
		{
			perms.setPlayerGroup(groupList.get(0).getName());
			return;
		}
		
		for (int i = 0; i < groupList.size(); i++)
		{
			for (int j = 0; j < playerGroups.length; j++)
			{
				if (playerGroups[j].equals(groupList.get(i)))
				{
					chosenGroup = groupList.get(i);
					jobReq = chosenGroup.getJobReq();
					timeReq = chosenGroup.getTimeReq();
					break;
				}
			}
		}
		
		//Don't attempt promotions on groups with -1, -1.
		if (chosenGroup == null || jobReq == -1 && timeReq == -1)
			return;
		
		//If they meet the time played requirements and the job rank requirements, promote them.
		if ((timePlayedInSeconds >= timeReq) && (playerConfigFile.getJobRank() >= jobReq))
		{
			newGroup = chosenGroup.getName();
			return;
		}
		
		//If the player is already in a group, return.
		if (playerGroups[0].equals(newGroup))
			return;
		
		//Broadcast Promotion.
		FC_Rpg.bLib.standardBroadcast(name + " Has Been Promoted To " + ChatColor.GREEN + newGroup);
		
		//Add the player to the new group.
		perms.setPlayerGroup(newGroup);
	}
	
	public boolean isDonatorOrAdmin() 
	{
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		if (playerConfigFile.isDonator() == true)
			return true;
		
		if (perms.isAdmin() == true)
			return true;
		
		return false;
	}
	
	public boolean prepareSpell(boolean displaySuccess)
	{
		//Variable Declarations and initializations
		int spellNumber = -1;
		
		//Make sure spell is valid.
		for (int i = 0; i < FC_Rpg.spellConfig.getSpellCount(); i++)
		{
			if (playerConfigFile.getSpellBind(i) == player.getItemInHand().getTypeId())
			{
				spellNumber = i;
				break;
			}
		}
		
		//If no valid spell was selected based on skill bind, then we just return damage.
		if (spellNumber == -1)
			return false;
		
		//We want to return if they don't have any points in the skill.
		if (playerConfigFile.getSpellLevel(spellNumber) < 1)
		{
			msgLib.standardError("You must levelup the spell once before you can use it.");
			return false;
		}
		
		//Set the active spell.
		playerConfigFile.setActiveSpell(playerConfigFile.getRpgClass().getSpell(spellNumber).getName());
		
		if (displaySuccess == true)
			msgLib.standardMessage("Successfully prepared spell.");
		
		return true;
	}
	
	public boolean hasEnoughMana(int spellNumber, int spellLevel)
	{
		if (curMana > playerConfigFile.getRpgClass().getSpell(spellNumber).getManaCost().get(spellLevel))
			return true;
		
		return false;
	}
	
	public void attemptNoManaNotification(int spellNumber, int spellLevel)
	{
		RpgMessageLib rpgLib = new RpgMessageLib(player);
		
		drainMana(playerConfigFile.getRpgClass().getSpell(spellNumber).getManaCost().get(spellLevel));
		
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
		SpellCaster scm = new SpellCaster();
		boolean success = false;
		
		//Initiates the spell cast.
		if (scm.init_spellCast(this, target, damage, damageType) == false)
			return damage;
		
		//Get the success.
		success = scm.handleEffects();
		
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
		
		return damage;
	}
	
	public boolean getStatusActiveRpgPlayer(int effectID)
	{
		Date now = new Date();
		
		if (now.getTime() < playerConfigFile.getStatusDuration(effectID))
			return true;
		
		return false;
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