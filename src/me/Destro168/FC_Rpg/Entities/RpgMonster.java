package me.Destro168.FC_Rpg.Entities;

import java.util.Date;
import java.util.List;
import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.WorldConfig;
import me.Destro168.FC_Rpg.Util.DistanceModifierLib;
import me.Destro168.FC_Rpg.Util.MobAggressionCheck;
import me.Destro168.FC_Suite_Shared.TimeUtils.DateManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class RpgMonster extends RpgEntity
{
	private MobAggressionCheck mac;
	private LivingEntity entity;
	private int mobId;
	private int level;
	private int attack;
	private int constitution;
	private double maxHealth;
	private double curHealth;
	private int modifier;
	private boolean isBoss;
	private boolean isWeak;
	private long statusDisabled;
	public double baseCash;
	public double baseExp;
	
	public LivingEntity getEntity() { return entity; }
	public int getAttack() { return attack; }
	public int getConstitution() { return constitution; }
	public double getMaxHealth() { return maxHealth; }
	public double getCurHealth() { return curHealth; }
	public int getMobId() { return mobId; }
	public int getLevel() { return level; }
	public boolean getMobAggressionCheck() { return mac.getIsHostile(entity.getType()); }
	public boolean getIsBoss() { return isBoss; }
	public boolean getIsWeak() { return isWeak; }
	public long getStatusDisabled() { return statusDisabled; }
	
	public void setAttack(int x) { attack = x; }
	public void setConstitution(int x) { constitution = x; }
	public void setCurHealth(int x) { curHealth = x; }
	public void setMaxHealth(int x) { maxHealth = x; }
	public void setMobId(int x) { mobId = x; }
	public void setLevel(int x) { level = x; }
	public void setIsBoss(boolean x) { isBoss = x; }
	public void setIsWeak(boolean x) { isWeak = x; }
	public void setStatusDisabled(int x)
	{
		DateManager dm = new DateManager();
		statusDisabled = dm.getFutureDate_Milliseconds(x);
	}

	public boolean isCreated()
	{
		return mobId > -1;
	}

	public RpgMonster()
	{
		setDefaults();
	}
	
	public RpgMonster(LivingEntity entity_, int fixedLevel, int levelBonus, boolean isBoss_)
	{
		setDefaults();
		isBoss = isBoss_;
		create(entity_, fixedLevel, levelBonus);
	}
	
	public void setDefaults()
	{
		mac = new MobAggressionCheck();
		entity = null;
		mobId = -1;
		level = -1;
		attack = -1;
		constitution = -1;
		maxHealth = -1;
		curHealth = -1;
		modifier = 1;
		isBoss = false;
		isWeak = false;
		statusDisabled = 0;
	}

	public void create(LivingEntity entity_, int fixedLevel, int levelBonus)
	{
		// Variable Assignment
		entity = entity_;
		
		// Variable Declarations
		Random rand = new Random();
		boolean calculateModifierByArea = true;
		int difficultyCoefficient;
		int trueDungeonNumber;
		int randomLevelDeviation = FC_Rpg.balanceConfig.getRandomMobLevelDeviation();
		int levelCap = FC_Rpg.worldConfig.getLevelCap(entity.getWorld().getName());
		int staticLevel;
		
		for (int i = 0; i < FC_Rpg.dungeonCount; i++)
		{
			trueDungeonNumber = FC_Rpg.trueDungeonNumbers.get(i);
			
			staticLevel = FC_Rpg.dungeonConfig.getStaticLevel(trueDungeonNumber);
			
			if (FC_Rpg.dungeonEventArray[trueDungeonNumber].getPhase() == 2)
			{
				if (FC_Rpg.dungeonEventArray[trueDungeonNumber].isInsideDungeon(entity.getLocation()))
				{
					if (staticLevel > 0)
						modifier = staticLevel;
					else if (fixedLevel > 0)
						modifier = fixedLevel;
					else
						modifier = FC_Rpg.dungeonEventArray[trueDungeonNumber].getLowestLevel() + levelBonus;
					
					calculateModifierByArea = false;
					break;
				}
			}
		}
		
		if (calculateModifierByArea == true)
		{
			if (fixedLevel > 0)
				modifier = fixedLevel;
			else
			{
				// Set the modifier by mob location.
				setModifierByArea();
				
				if (entity.getType() == EntityType.WITHER)
				{
					levelBonus += FC_Rpg.balanceConfig.getWitherLevelBonus();
					isBoss = true;
				}
				else if (entity.getType() == EntityType.ENDER_DRAGON)
				{
					levelBonus += FC_Rpg.balanceConfig.getEnderDragonLevelBonus();
					isBoss = true;
				}
			}
			
			// Increase modifier by level bonus.
			modifier += levelBonus;
		}
		
		// Set all variables
		mobId = entity.getEntityId();
		level = modifier + (rand.nextInt(randomLevelDeviation) - rand.nextInt(randomLevelDeviation));
		
		// Ensure level is never below 1.
		if (level < 1)
			level = 1;
		
		// Make sure that mobs are not a higher level than the worlds level cap.
		if (levelCap > 0 && level > levelCap)
			level = levelCap;
		
		// Increases stats based on monster level.
		difficultyCoefficient = (1 + modifier / FC_Rpg.balanceConfig.getDifficultyScalor());
		
		// Stat stats
		attack = (int) FC_Rpg.balanceConfig.getMobAttackMultiplier() * modifier * difficultyCoefficient;
		
		// Only set constitution for hostile mobs.
		if (getMobAggressionCheck())
			constitution = (int) (FC_Rpg.balanceConfig.getMobConstitutionMultiplier() * modifier * difficultyCoefficient);
		else
			constitution = 1;
		
		// Set base cash and exp
		baseCash = level * FC_Rpg.balanceConfig.getMobCashMultiplier();
		baseExp = level * FC_Rpg.balanceConfig.getMobExpMultiplier();
		
		// Modify stats based on config values.
		for (int field : FC_Rpg.balanceConfig.getMobBonusFieldList())
		{
			if (entity_.getType().toString().toLowerCase().equals(FC_Rpg.balanceConfig.getMobBonusType(field).toLowerCase()))
			{
				attack *= FC_Rpg.balanceConfig.getMobBonusAttack(field);
				constitution *= FC_Rpg.balanceConfig.getMobBonusConstitution(field);
				baseCash *= FC_Rpg.balanceConfig.getMobBonusGold(field);
				baseExp *= FC_Rpg.balanceConfig.getMobBonusExp(field);
			}
		}
		
		setStartingHealth();
	}
	
	private void setStartingHealth()
	{
		maxHealth = constitution;
		curHealth = maxHealth;
	}
	
	public void checkEquipment()
	{
		//Variable declarations.
		EntityEquipment equip = entity.getEquipment();
		double constitutionModifier = 1;
		
		//Update attack by weapon.
		checkWeapon(equip.getItemInHand());
		
		//Update constitution and attack based on equipment.
		constitutionModifier += checkItem(equip.getBoots());
		constitutionModifier += checkItem(equip.getChestplate());
		constitutionModifier += checkItem(equip.getHelmet());
		constitutionModifier += checkItem(equip.getLeggings());
		
		//Update constitution.
		constitution = (int) (constitution * constitutionModifier);
		
		//Update starting health.
		setStartingHealth();
	}
	
	private double checkItem(ItemStack itemStack)
	{
		if (itemStack.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL))
			return itemStack.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) * 0.01;
		
		return 0;
	}
	
	private void checkWeapon(ItemStack itemStack)
	{
		if (itemStack.containsEnchantment(Enchantment.DAMAGE_ALL))
			attack = (int) (attack * (itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 0.005));
	}
	
	public double dealDamage(double x)
	{
		curHealth = curHealth - x;

		if (curHealth < 0)
			curHealth = 0;

		lastDamaged = new Date();

		return curHealth;
	}

	public void restoreHealth(double x)
	{
		curHealth = curHealth + x;

		if (curHealth > maxHealth)
			curHealth = maxHealth;
	}

	public void clear()
	{
		mobId = -1;
	}

	public void setModifierByArea()
	{
		DistanceModifierLib dml = new DistanceModifierLib();
		WorldConfig wm = new WorldConfig();
		
		if (!wm.getIsRpgWorld(entity.getWorld().getName()))
			return;
		
		// Make all non-hostile mobs level 1.
		if (getMobAggressionCheck() == false)
			return;
		
		dml.getWorldDML(entity.getLocation());

		modifier = dml.getDistanceModifier();
	}

	public void handleHostileMobDrops(Location dropSpot)
	{
		List<ItemStack> drops = FC_Rpg.treasureConfig.getRandomItemStackList(level, FC_Rpg.treasureConfig.getLootList(FC_Rpg.balanceConfig.getMobLootList()));
		
		for (ItemStack drop : drops)
			dropSpot.getWorld().dropItem(dropSpot, drop); // Drop the item.
	}
	
	public void handlePassiveMobDrops(Location dropSpot)
	{
		EntityType entityType = getEntity().getType();
		
		// Drop loot for passive mobs.
		if (entityType == EntityType.COW)
		{
			dropPassiveItem(Material.RAW_BEEF, dropSpot);
			dropPassiveItem(Material.LEATHER, dropSpot);
		}
		else if (entityType == EntityType.CHICKEN)
		{
			dropPassiveItem(Material.RAW_CHICKEN, dropSpot);
			dropPassiveItem(Material.FEATHER, dropSpot);
		}
		else if (entityType == EntityType.PIG)
		{
			dropPassiveItem(Material.PORK, dropSpot);
		}
		else if (entityType == EntityType.OCELOT)
		{
			dropPassiveItem(Material.RAW_FISH, dropSpot);
		}
		else if (entityType == EntityType.SQUID)
		{
			dropPassiveItem(Material.INK_SACK, dropSpot);
		}
		else if (entityType == EntityType.SNOWMAN)
		{
			dropPassiveItem(Material.SNOW_BALL, dropSpot);
		}
		else if (entityType == EntityType.SHEEP)
		{
			dropPassiveItem(Material.WOOL, dropSpot);
		}
		else if (entityType == EntityType.MUSHROOM_COW)
		{
			dropPassiveItem(Material.RAW_BEEF, dropSpot);
			dropPassiveItem(Material.LEATHER, dropSpot);
			dropPassiveItem(Material.MUSHROOM_SOUP, dropSpot);
		}
		else if (entityType == EntityType.WOLF)
		{
			dropPassiveItem(Material.BONE, dropSpot);
		}
	}
	
	public void dropExperience()
	{
		// Variable Declaration
		Random rand = new Random();
		
		// Drop 3 experience orbs.
		for (int i = 0; i < 3; i++)
		{
			ExperienceOrb orb = (ExperienceOrb) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.EXPERIENCE_ORB);
			orb.setExperience(rand.nextInt(3) + 1);
		}
	}

	private void dropPassiveItem(Material material, Location dropSpot)
	{
		Random rand = new Random();
		ItemStack loot = new ItemStack(material, rand.nextInt(3));
		
		dropSpot.getWorld().dropItemNaturally(dropSpot, loot);
	}
}






