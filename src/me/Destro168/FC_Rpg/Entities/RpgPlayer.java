package me.Destro168.FC_Rpg.Entities;

import java.util.Date;
import java.util.List;

import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.BalanceConfig;
import me.Destro168.FC_Rpg.Configs.GroupConfig;
import me.Destro168.FC_Rpg.Configs.PlayerConfig;
import me.Destro168.FC_Rpg.Configs.WorldConfig;
import me.Destro168.FC_Rpg.LoadedObjects.Group;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.Spells.SpellCaster;
import me.Destro168.FC_Rpg.Util.FC_RpgPermissions;
import me.Destro168.FC_Rpg.Util.HealthConverter;
import me.Destro168.FC_Suite_Shared.ColorLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RpgPlayer extends RpgEntity
{
	private MessageLib msgLib;
	private ItemStack air;
	private Player player;
	private PlayerConfig playerConfig;
	private String prefix = "";
	private String name = "";
	private boolean isSupportBuffed;
	private boolean isCasting;
	private int tempAttack;
	private int tempConstitution;
	private int tempMagic;
	private int tempIntelligence;
	private Date lastDodgeNotification;
	private Date lastThornsNotification;
	private Date lastHealNotification;
	private Date logonDate;
	private Date lastNoManaNotification;
	private Date lastMonsterDeathNotification;
	private Date lastCastNotification;
	private double curHealth;
	private double maxHealth;
	private double curMana;
	private double maxMana;
	
	public void switchIsCasting() { isCasting = !isCasting; }
	public boolean getIsCasting() { return isCasting; }
	
	//Gets
	public Player getPlayer() { return player; }
	public PlayerConfig getPlayerConfig() { return playerConfig; }
	
	//Only call twice, one when player is loaded, once when player deloads.
	public double getCurHealth() { return curHealth; }
	public double getMaxHealth() { return maxHealth; }
	public double getCurMana() { return curMana; }
	public double getMaxMana() { return maxMana; }
	
	//Functions that rely on file get/sets
	public int getTotalAttack() { return playerConfig.getAttack() + tempAttack; }
	public int getTotalConstitution() { return playerConfig.getConstitution() + tempConstitution; }
	public int getTotalMagic() { return playerConfig.getMagic() + tempMagic; }
	public int getTotalIntelligence() { return playerConfig.getIntelligence() + tempIntelligence; }
	
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
		playerConfig = null;
		prefix = "";
		isSupportBuffed = false;
		tempAttack = 0;
		tempConstitution = 0;
		tempMagic = 0;
		tempIntelligence = 0;
		lastDodgeNotification = new Date();
		lastThornsNotification = new Date();
		lastHealNotification = new Date();
		lastNoManaNotification = new Date();
		logonDate = new Date();
		lastMonsterDeathNotification = new Date();
		lastCastNotification = new Date();
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
		playerConfig = new PlayerConfig(player.getName());
		
		//Check for infinite donator.
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
    	
    	if (!playerConfig.isDonator())
    	{
    		if (perms.isInfiniteDonator())
        		setDonator(1);
    	}
    	
    	//Update player health hud.
		loadCriticalInformation();
		
		//Update the prefix.
		updatePrefix();
		
    	HealthConverter hc = new HealthConverter(maxHealth, curHealth);
    	
    	WorldConfig wm = new WorldConfig();
    	
    	if (wm.getIsRpgWorld(name) == true)
    		player_.setHealth(hc.getPlayerHearts());
		
		//Check and apply donator bonuses
		updateDonatorStats();
	}
	
	public void loadCriticalInformation()
	{
		curMana = playerConfig.getCurManaFile();
		maxMana = playerConfig.getMaxManaFile();
		curHealth = playerConfig.getCurHealthFile();
		maxHealth = playerConfig.getMaxHealthFile();
		name = playerConfig.getName();
	}
	
	public void dumpCriticalInformation()
	{
		playerConfig.setCurManaFile(curMana);
		playerConfig.setMaxManaFile(maxMana);
		playerConfig.setCurHealthFile(curHealth);
		playerConfig.setMaxHealthFile(maxHealth);
	}
	
	public void createPlayerRecord(Player player_, int pickedClass, boolean manualDistribution)
	{
		//Store player
		setPlayer(player_);
		
		//Set the files defaults.
		playerConfig.setPlayerDefaults(pickedClass, manualDistribution);
		
		//Update health and mana based on stats.
		calculateHealthAndMana();
		
		//Check and apply donator bonuses
		updateDonatorStats();
		
		if (manualDistribution == true)
		{
			msgLib.standardMessage("You chose to manually allocate stat points!");
			msgLib.standardMessage("You MUST assign stat points or you will always die!");
			msgLib.standardMessage("Please! Assign your stat points! Use /class for help.");
		}
		else
			msgLib.standardMessage("Your stat points are automatically distributed!");
		
		//Send the player some usefull information.
		msgLib.standardMessage("Type /rpghelp to see all help!");
		
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
		String customPrefix = playerConfig.getCustomPrefix();
		String defaultPrefix = FC_Rpg.generalConfig.getDefaultPrefix();
		String parsedDefault = cl.parse(defaultPrefix);
		
		//Check the customPrefix first.
		if (customPrefix == null)
		{
			playerConfig.setCustomPrefix(defaultPrefix);
			prefix = parsedDefault;
			return prefix;
		}
		else if (customPrefix.equals("") || customPrefix.equals("none"))
		{
			playerConfig.setCustomPrefix(defaultPrefix);
			prefix = parsedDefault;
			return prefix;
		}
		else if (!customPrefix.equals(defaultPrefix))
		{
			prefix = cl.parse(customPrefix);
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
				return cl.parse(group.getDisplay());
		}
		
		//If a donator set them to donator tag.
		if (playerConfig.isDonator() == true)
			prefix = ChatColor.WHITE + "["+ ChatColor.YELLOW + "Donator" + ChatColor.WHITE + "] ";
		
		return prefix;
	}
	
	public void setDonator(int periods)
	{
		playerConfig.offlineSetDonator(periods);
		
		//Update stats to account for new bonuses if a donator.
		updateDonatorStats();
	}
	
	public void addClassExperience(double x, boolean displayLevelUpMessage)
	{
		//Add the class experience
		if (playerConfig.addOfflineClassExperience(x, displayLevelUpMessage, this) == false)
			return;
		
		//Update Donator Stats
		updateDonatorStats();
		
		//Message the player a reminder to use stat points.
		if (playerConfig.getManualAllocation() == false)
		{
			if (playerConfig.getStats() > 0)
				msgLib.standardMessage("Remember to assign stat points. Use /class for help!");
		}
		
		if (playerConfig.getSpellPoints() > 0)
			msgLib.standardMessage("Remember to choose and upgrade spells. Use /spell for help!");
	}
	
	public void updateDonatorStats()
	{
		double donatorBonusPercent = FC_Rpg.generalConfig.getDonatorBonusStatPercent();
		
		if (playerConfig.isDonator() == true)
		{
			resetTempStats();
			
			tempAttack = (int) (playerConfig.getAttack() * donatorBonusPercent);
			tempConstitution = (int) (playerConfig.getConstitution() * donatorBonusPercent);
			tempMagic = (int) (playerConfig.getMagic() * donatorBonusPercent);
			tempIntelligence = (int) (playerConfig.getIntelligence() * donatorBonusPercent);
			
			calculateHealthAndMana();
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
		if (playerConfig.getClassChangeTickets() == 0)
			return false;
		
		return true;
	}
	
	public boolean switchClass(String x)
	{
		//Variable Declaration.
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		boolean hasInfiniteTickets = perms.hasInfiniteTickets();
		int cNumber = -1;
		
		//If they don't have the tickets or the permission for infinity, return no permission.
		if (hasClassChangeTicket() == false && !hasInfiniteTickets)
		{
			msgLib.errorNoPermission();
			return false;
		}
		
		//Revert user input by subtracting 1.
		try { cNumber = Integer.valueOf(x) - 1; }
		catch (NumberFormatException e)
		{
			//Attempt to get class by word entry.
			RpgClass[] c = FC_Rpg.classConfig.getRpgClasses();
			
			for (int i = 0; i < c.length; i++)
			{
				if (x.equalsIgnoreCase(c[i].getName()))
				{
					cNumber = i;
					break;
				}
			}
			
			//If the word failed to match a class return fail.
			if (cNumber == -1)
			{
				msgLib.standardError("Invalid Class Number.");
				return true;
			}
		}
		
		//Make sure the new class is different from current class.
		if (getPlayerConfig().getCombatClass() == cNumber)
			return msgLib.standardError("You Can't Switch To The Class You Are Currently.");
		
		//Make sure that the user is only picking from proper classes.
		if (cNumber < 0 || cNumber > FC_Rpg.classConfig.getRpgClasses().length)
			return msgLib.errorInvalidCommand();
		
		//Chance the combat class.
		playerConfig.setCombatClass(cNumber);
		
		//If not an admin we decrease class change tickets.
		if (hasInfiniteTickets == false)
			playerConfig.setClassChangeTickets(playerConfig.getClassChangeTickets() - 1);
		
		//Return true for success.
		return msgLib.successCommand();
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
		tempAttack += (int) (getTotalAttack() * buffStrength);
		tempMagic += (int) (getTotalMagic() * buffStrength);
		tempConstitution += (int) (getTotalConstitution() * buffStrength);
		tempIntelligence += (int) (getTotalIntelligence() * buffStrength);
		
		//Message lib.
		msgLib.standardMessage("The support spell has been applied to you!");
		
		//Recalculate health and mana.
		calculateHealthAndMana();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
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
				calculateHealthAndMana();
				
				//Remove support buff status.
				isSupportBuffed = false;
				
				//Message the player.
				msgLib.standardMessage("Support buff has worn off");
			}
		}, 400);
	}
	
	//Remember when changing this to change the RPGPLAYERFILE version! -> calculateHealthAndManaOffline()
	public void calculateHealthAndMana()
	{
		maxHealth = FC_Rpg.balanceConfig.getPlayerBaseHealth() + getTotalConstitution() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeConstitution();
		maxMana = FC_Rpg.balanceConfig.getPlayerBaseMana() + getTotalIntelligence() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeIntelligence();
		
		//Prevent overflow of health/mana.
		if (curHealth > maxHealth)
			curHealth = maxHealth;
		
		if (curMana > maxMana)
			curMana = maxMana;
	}
	
	public void healHealthAndMana(double d)
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		healHealth(d);
		restoreMana(d);
	}
	
	public void healFull()
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		curHealth = maxHealth;
		curMana = maxMana;
	}
	
	public double dealDamage(double damage)
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		curHealth -= damage;
		
		lastDamaged = new Date();
		
		return curHealth;
	}
	
	public void drainMana(double d)
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		curMana -= d;
	}
	
	public void restoreManaTick()
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		if (playerConfig.getRpgClass().getPassiveID() == BalanceConfig.passive_InnerFire)
			restoreMana(maxMana * .2); //20% mana regeneration per 5 seconds (4% per sec)
		else
			restoreMana(maxMana * .05); //5% mana regeneration per 5 seconds (1% per sec)
		
		//Attempt to restore mana.
		if (curMana > maxMana)
			curMana = maxMana;
	}
	
	public void healHealth(double amount) 
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		//Actually add in new amount.
		curHealth = curHealth + amount;
		
		//If new health is equal to 
		if (curHealth > maxHealth)
			curHealth = maxHealth;
	}
	
	public void restoreMana(double amount) 
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		//Actually add in new amount.
		curMana = curMana + amount;
		
		//If new health is equal to 
		if (curMana > maxMana)
			curMana = maxMana;
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
		int stats = playerConfig.getStats();
		
		stats += playerConfig.getAttack();
		stats += playerConfig.getConstitution();
		stats += playerConfig.getMagic();
		stats += playerConfig.getIntelligence();
		
		playerConfig.setAttack(0);
		playerConfig.setConstitution(0);
		playerConfig.setMagic(0);
		playerConfig.setIntelligence(0);
		
		playerConfig.setStats(stats);
	}
	
	public void respecSkills()
	{
		int spellPoints = playerConfig.getSpellPoints();
		
		for (int i = 0; i < playerConfig.getRpgClass().getSpellBook().size(); i++)
		{
			spellPoints += playerConfig.getSpellLevels().get(i);
			playerConfig.updateSpellLevel(i, 0);
		}
		
		playerConfig.setSpellPoints(spellPoints);
	}
	
	public boolean useStats(int stat, int amount)
	{
		//We make sure first that the player isn't trying to use more stats than they have.
		//if they are fine, then we subtract the amount from the amount of stats that the player has.
		if (amount > playerConfig.getStats() || amount < 1)
			return false;
		
		playerConfig.setStats(playerConfig.getStats() - amount);
		
		//Then we add the stat points to the player.
		switch (stat)
		{
			case 1:
				playerConfig.setAttack(playerConfig.getAttack() + amount);
				break;
			case 2:
				playerConfig.setConstitution(playerConfig.getConstitution() + amount);
				break;
			case 3:
				playerConfig.setMagic(playerConfig.getMagic() + amount);
				break;
			case 4:
				playerConfig.setIntelligence(playerConfig.getIntelligence() + amount);
				break;
		}
		
		//Handle donator stat updates
		updateDonatorStats();
		
		//Calculate health and mana.
		calculateHealthAndMana();
		
		return true;
	}
	
	private boolean getCanNotify(Date time)
	{
		return getCanNotify(time, FC_Rpg.generalConfig.getNotifcationInterval());
	}
	
	private boolean getCanNotify(Date time, int NotificationRepeatInterval)
	{
		Date now = new Date();
		
		if ((now.getTime() - time.getTime()) < NotificationRepeatInterval)
			return false;
		
		return true;
	}
	
	public void attemptAttackNotification(EntityType type, int level, double minHp, double maxHp, double damage)
	{
		if (getCanNotify(lastAttackNotification) == false)
			return;
		
		String[] p = getRemainingX(minHp,maxHp,0);
		lastAttackNotification = new Date();
		
		msgLib.infiniteMessage("Damage: ",ChatColor.GREEN + FC_Rpg.df.format(damage)," / HP: (",
				p[0],p[1],p[2],p[3],p[4],p[5] + " / ",type.toString(),"(",ChatColor.GREEN + String.valueOf(level),")");
	}
	
	public void attemptDefenseNotification(double damage)
	{
		if (getCanNotify(lastDefenseNotification) == false)
			return;
		
		String[] p = getRemainingX(curHealth,maxHealth,2);
		lastDefenseNotification = new Date();
		
		msgLib.infiniteMessage("Damage: ",ChatColor.RED + String.valueOf(FC_Rpg.df.format(damage))," / HP: (",
				p[0],p[1],p[2],p[3],p[4],p[5]);
	}
	
	public void attemptCastNotification(String spellName)
	{
		if (getCanNotify(lastCastNotification) == false)
			return;
		
		String[] p = getRemainingX(curMana,maxMana,1);
		msgLib.infiniteMessage("[","*Spell Cast*","] ",spellName," / MP: (",
				p[0],p[1],p[2],p[3],p[4],p[5]);
	}
	
	public void attemptHealOtherNotification(RpgPlayer healTarget)
	{
		if (getCanNotify(lastHealNotification) == false)
			return;
		
		lastHealNotification = new Date();
		
		//String[] d = getRemainingX(curMana,maxMana,1);
		String[] p = getRemainingX(healTarget.getCurHealth(),healTarget.getMaxHealth(),0);
		
		msgLib.infiniteMessage("[","-Healed Other-","]  / Target HP: (",
				p[0],p[1],p[2],p[3],p[4],p[5]);
	}
	
	public void attemptHealSelfNotification(double healAmount)
	{
		if (getCanNotify(lastHealNotification) == false)
			return;
		
		String[] p = getRemainingX(curHealth,maxHealth,0);
		lastHealNotification = new Date();
        
		msgLib.infiniteMessage("[","-Heal-","] HP: (",
				p[0],p[1],p[2],p[3],p[4],p[5]);
	}
	 
	public void attemptMonsterDeathNotification(int level, double exp, double loot)
	{
		if (!getCanNotify(lastMonsterDeathNotification, 100))
			return;
		
		lastMonsterDeathNotification = new Date();
		
		msgLib.infiniteMessage("[_","Mob Slain","_] Level: ",String.valueOf(level)," / Experience: ",FC_Rpg.df.format(exp)," (",
				FC_Rpg.df.format(playerConfig.getRequiredExpPercent()) + "%)"," / Money: ",FC_Rpg.df.format(loot));
	}
	
	public void attemptMonsterOutOfRangeNotification()
	{
		if (!getCanNotify(lastMonsterDeathNotification, 100))
			return;
		
		lastMonsterDeathNotification = new Date();
		
		msgLib.standardMessage("That monster is outside your level range so you got nothing.");
	}
	
	public void attemptDamageAvoidNotification(boolean isImmortal)
	{
		if (getCanNotify(lastDodgeNotification) == false)
			return;
		
		lastDodgeNotification = new Date();
		String type = "Dodged Damage";
		
		if (isImmortal == true)
			type = "Damage Immune";
		
		msgLib.infiniteMessage("[",type,"] Damage Avoided!");
	}
	
	public void attemptThornsNotification(double damage)
	{
		if (getCanNotify(lastThornsNotification) == false)
			return;
		
    	lastThornsNotification = new Date();
    	
		msgLib.infiniteMessage("[","Thorns","] Damage: ",FC_Rpg.df.format(damage));
	}
	
	public void attemptNoManaNotification(int spellNumber, int spellLevel)
	{
		if (getCanNotify(lastNoManaNotification) == false)
			return;

		String[] p = getRemainingX(curMana,maxMana,1);
		lastNoManaNotification = new Date();
		
		msgLib.infiniteMessage("[","*Spell Fail*","] Out Of Mana! (",
				p[0],p[1],p[2],p[3],p[4],p[5]);
	}
	
	private String[] getRemainingX(double min, double max, int colorID)
	{
		String[] parts = new String[6];
		ChatColor color = ChatColor.GREEN;
		
		if (colorID == 1)
			color = ChatColor.DARK_AQUA;
		else if (colorID == 2)
			color = ChatColor.RED;
		
		parts[0] = color + FC_Rpg.df.format(min);
		parts[1] = "/";
		parts[2] = color + FC_Rpg.df.format(max);
		parts[3] = ") (";
		parts[4] = color + FC_Rpg.df.format(min * 100 / max)+"%";
		parts[5] = ")";
		
		return parts;
	}
	
	public void attemptGiveTimedItems()
	{
		Date now = new Date();
		WorldConfig wm = new WorldConfig();
		List<ItemStack> timedItems = FC_Rpg.generalConfig.getTimedItems();
		
		//Only give steak in rpg world.
		if (!wm.getIsRpgWorld(player.getWorld().getName()))
			return;
		
		//Wait an hour before giving steak again.
		if (now.getTime() - playerConfig.getLastRecievedHourlyItems() > FC_Rpg.generalConfig.getTimedItemsInterval())
		{
			for (ItemStack hourlyItem : timedItems)
				addItemToInventory(hourlyItem);
			
			msgLib.standardMessage("Timed item(s) given to you!");
			
			//Update last ate time.
			playerConfig.setLastRecievedHourlyItems(now.getTime());
		}
	}
	
	public void addItemToInventory(ItemStack itemSTack)
	{
		//Tell the player about their steak and drop/add it.
		if (hasEmptyInventorySlot(itemSTack.getType(), itemSTack.getAmount()))
			player.getInventory().addItem(itemSTack);
		else
			Bukkit.getServer().getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), itemSTack);
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
	
	public void swordAttackRequirementCheck()
	{
		//Variable Declarations
		ItemStack heldItem = player.getItemInHand();
		Material handItemType = heldItem.getType();
		boolean success = false;
		
		if ((handItemType == Material.WOOD_SWORD) && (playerConfig.getAttack() < FC_Rpg.balanceConfig.getSwordAttackRequirementWood()))
			msgLib.standardMessage("Without " + FC_Rpg.balanceConfig.getSwordAttackRequirementWood() + "+ Attack, Stone Swords Are USELESS TO YOU!");
		else if ((handItemType == Material.STONE_SWORD) && (playerConfig.getAttack() < FC_Rpg.balanceConfig.getSwordAttackRequirementStone()))
			msgLib.standardMessage("Without " + FC_Rpg.balanceConfig.getSwordAttackRequirementStone() + "+ Attack, Stone Swords Are USELESS TO YOU!");
		else if ((handItemType == Material.IRON_SWORD) && (playerConfig.getAttack() < FC_Rpg.balanceConfig.getSwordAttackRequirementIron()))
			msgLib.standardMessage("Without " + FC_Rpg.balanceConfig.getSwordAttackRequirementIron() + "+ Attack, Iron Swords Are USELESS TO YOU!");
		else if ((handItemType == Material.DIAMOND_SWORD) && (playerConfig.getAttack() < FC_Rpg.balanceConfig.getSwordAttackRequirementDiamond()))
			msgLib.standardMessage("Without " + FC_Rpg.balanceConfig.getSwordAttackRequirementDiamond() + "+ Attack, Diamond Swords Are USELESS TO YOU!");
		else if ((handItemType == Material.GOLD_SWORD) && (playerConfig.getAttack() < FC_Rpg.balanceConfig.getSwordAttackRequirementGold()))
			msgLib.standardMessage("Without " + FC_Rpg.balanceConfig.getSwordAttackRequirementGold() + "+ Attack, Gold Swords Are USELESS TO YOU!");
		else
			success = true;
		
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
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementChain())
		{
			if (headType == Material.CHAINMAIL_HELMET)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementChain() + "+ Constitution To Wear Chain Helmets!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementIron())
		{
			if (headType == Material.IRON_HELMET)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementIron() + "+ Constitution To Wear Iron Helmets!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementDiamond())
		{
			if (headType == Material.DIAMOND_HELMET)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementDiamond() + "+ Constitution To Wear Diamond Helmets!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementGold())
		{
			if (headType == Material.GOLD_HELMET)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementGold() + "+ Constitution To Wear Gold Helmets!");
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
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementChain())
		{
			if (chestType == Material.CHAINMAIL_CHESTPLATE)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementChain() + "+ + Constitution To Wear Chain ChestPlates!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementIron())
		{
			if (chestType == Material.IRON_CHESTPLATE)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementIron() + "+ Constitution To Wear Iron ChestPlates!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementDiamond())
		{
			if (chestType == Material.DIAMOND_CHESTPLATE)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementDiamond() + "+ Constitution To Wear Diamond ChestPlates!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementGold())
		{
			if (chestType == Material.GOLD_CHESTPLATE)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementGold() + "+ Constitution To Wear Gold ChestPlates!");
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
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementChain())
		{
			if (leggingsType == Material.CHAINMAIL_LEGGINGS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementChain() + "+ Constitution To Wear Chain Leggings!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementIron())
		{
			if (leggingsType == Material.IRON_LEGGINGS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementIron() + "+ Constitution To Wear Iron Leggings!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementDiamond())
		{
			if (leggingsType == Material.DIAMOND_LEGGINGS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementDiamond() + "+ Constitution To Wear Diamond Leggings!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementGold())
		{
			if (leggingsType == Material.GOLD_LEGGINGS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementGold() + "+ Constitution To Wear Gold Leggings!");
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
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementChain())
		{
			if (bootsType == Material.CHAINMAIL_BOOTS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementChain() + "+ Constitution To Wear Chain Boots!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementIron())
		{
			if (bootsType == Material.IRON_BOOTS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementIron() + "+ Constitution To Wear Iron Boots!");
				success = false;
			}
		}
		
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementDiamond())
		{
			if (bootsType == Material.DIAMOND_BOOTS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementDiamond() + "+ Constitution To Wear Diamond Boots!");
				success = false;
			}
		}
		if (playerConfig.getConstitution() < FC_Rpg.balanceConfig.getArmorWearRequirementGold())
		{
			if (bootsType == Material.GOLD_BOOTS)
			{
				msgLib.standardMessage("You Need " + FC_Rpg.balanceConfig.getArmorWearRequirementGold() + "+ Constitution To Wear Gold Boots!");
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
		int timePlayedInSeconds = playerConfig.getSecondsPlayed();
		
		//We want to first set how long we have played in the configuration file.
		timeDifference = now.getTime() - logonDate.getTime(); //Calulate how long we have been online.
		intDifference = (int) (timeDifference / 1000); //Convert that time to seconds.
		timePlayedInSeconds = timePlayedInSeconds + intDifference;	//Update time played.
		playerConfig.setSecondsPlayed(timePlayedInSeconds); //Store it
		
		//Update the logon date to now.
		logonDate = new Date(); //Update for future time player updates.
		
		//Perform a promotion check.
		promotionCheck(timePlayedInSeconds);
	}
	
	private void promotionCheck(int timePlayedInSeconds)
	{
		//Don't continue if player isn't active.
		if (playerConfig.getIsActive() == false)
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
		if ((timePlayedInSeconds >= timeReq) && (playerConfig.getJobRank() >= jobReq))
		{
			newGroup = chosenGroup.getName();
			return;
		}
		
		//If the player is already in a group, return.
		if (playerGroups[0].equals(newGroup))
			return;
		
		//Broadcast Promotion.
		FC_Rpg.rpgBroadcast.rpgBroadcast(name + " Has Been Promoted To " + ChatColor.GREEN + newGroup);
		
		//Add the player to the new group.
		perms.setPlayerGroup(newGroup);
	}
	
	public boolean isDonatorOrAdmin() 
	{
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		if (playerConfig.isDonator())
			return true;
		
		if (perms.isAdmin())
			return true;
		
		return false;
	}
	
	public boolean prepareSpell(boolean displaySuccess)
	{
		//Variable Declarations and initializations
		int spellNumber = -1;
		
		//Make sure spell is valid.
		for (int i = 0; i < playerConfig.getSpellBinds().size(); i++)
		{
			if (playerConfig.getSpellBinds().get(i) == player.getItemInHand().getTypeId())
			{
				spellNumber = i;
				break;
			}
		}
		
		//If no valid spell was selected based on skill bind, then we just return damage.
		if (spellNumber == -1)
			return false;
		
		//We want to return if they don't have any points in the skill.
		if (playerConfig.getSpellLevels().get(spellNumber) < 1)
		{
			msgLib.standardError("You must levelup the spell once before you can use it.");
			return false;
		}
		
		//Set the active spell.
		playerConfig.setActiveSpell(playerConfig.getRpgClass().getSpell(spellNumber).getName());
		
		if (displaySuccess == true)
			msgLib.standardMessage("Successfully prepared spell.");
		
		return true;
	}
	
	public boolean hasEnoughMana(int spellNumber, int spellLevel)
	{
		if (curMana > playerConfig.getRpgClass().getSpell(spellNumber).getManaCost().get(spellLevel))
			return true;
		
		return false;
	}
	
	public double getMissingHealthDecimal()
	{
		//Update health and mana.
		calculateHealthAndMana();
		
		double percent = curHealth * 100 / maxHealth;
		
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
			if (!(player.getGameMode().equals(GameMode.CREATIVE)))
				drainMana(scm.getManaCost());	//Drain the mana
		}
		
		//Tell the caster they cast the spell.
		attemptCastNotification(scm.getName());
		
		if (scm.getDamage() > 0)
			return scm.getDamage();
		
		return damage;
	}
	
	public boolean getStatusActiveRpgPlayer(int effectID)
	{
		Date now = new Date();
		
		if (now.getTime() < playerConfig.getStatusDuration(effectID))
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