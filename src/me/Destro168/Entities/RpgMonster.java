package me.Destro168.Entities;

import java.util.Date;
import java.util.List;
import java.util.Random;

import me.Destro168.Configs.WorldConfig;
import me.Destro168.FC_Suite_Shared.LocationInsideAreaCheck;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.TimeUtils.DateManager;
import me.Destro168.Util.DistanceModifierLib;
import me.Destro168.Util.MobAggressionCheck;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

public class RpgMonster extends RpgEntity
{
	private MobAggressionCheck mac;
	private LivingEntity entity;
	private int mobId;
	private int level;
	private int strength;
	private int constitution;
	private double maxHealth;
	private double curHealth;
	private int modifier;
	private boolean isBoss;
	private boolean isWeak;
	private long statusDisabled;
	
	public LivingEntity getEntity() { return entity; }
	public int getStrength() { return strength; }
	public int getConstitution() { return constitution; }
	public double getMaxHealth() { return maxHealth; }
	public double getCurHealth() { return curHealth; }
	public int getMobId() { return mobId; }
	public int getLevel() { return level; }
	public MobAggressionCheck getMobAggressionCheck() { return mac; }
	public boolean getIsBoss() { return isBoss; }
	public boolean getIsWeak() { return isWeak; }
	public long getStatusDisabled() { return statusDisabled; }
	
	public void setStrength(int x) { strength = x; }
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
	
	public boolean isCreated() { return mobId > -1; } 
	
	public RpgMonster()
	{
		setDefaults();
	}
	
	public RpgMonster(LivingEntity entity_, int levelBonus)
	{
		setDefaults();
		create(entity_, levelBonus);
	}
	
	public void setDefaults()
	{
		mac = null;
		entity = null;
		mobId = -1;
		level = -1;
		strength = -1;
		constitution = -1;
		maxHealth = -1;
		curHealth = -1;
		modifier = 1;
		isBoss = false;
		isWeak = false;
		statusDisabled = 0;
	}
	
	public void create(LivingEntity entity_, int levelBonus)
	{
		entity = entity_;
		
		//Variable Declarations
		mac = new MobAggressionCheck(entity.getType());
		boolean calculateModifierByArea = true;
		int difficultyCoefficient;
		int tdm; //True dungeonNumber
		
		for (int i = 0; i < FC_Rpg.dungeonCount; i++)
		{
			tdm = FC_Rpg.trueDungeonNumbers.get(i);
			
			if (FC_Rpg.dungeonEventArray[tdm].getPhase() == 2)
			{
				if (FC_Rpg.dungeonEventArray[tdm].isInsideDungeon(entity.getLocation()))
				{
					modifier = FC_Rpg.dungeonEventArray[tdm].getLowestLevel() + levelBonus;
					calculateModifierByArea = false;
					break;
				}
			}
		}
		
		if (calculateModifierByArea == true)
		{
			//Set the modifier by mob location.
			setModifierByArea();
			
			if (entity instanceof Wither)
				levelBonus = levelBonus + FC_Rpg.generalConfig.getWitherLevelBonus();
			else if (entity instanceof EnderDragon)
				levelBonus = levelBonus + FC_Rpg.generalConfig.getEnderDragonLevelBonus();
			
			//Increase modifier by level bonus.
			modifier = modifier + levelBonus;
		}
		
		//Set all variables
		mobId = entity.getEntityId();
		level = modifier;
		
		//Increases stats based on monster level.
		difficultyCoefficient = (1 + modifier / 500); //500 = roughly 20% increase from 0-100.
		
		//Stat stats
		strength = (int) 2 * modifier * difficultyCoefficient;
		
		//Only set constitution to high for hostile mobs.
		if (mac.isHostile() == true)
			constitution = (int) (100 * modifier * difficultyCoefficient);
		else
			constitution = 1;
		
		//Update health.
		maxHealth = constitution;
		curHealth = maxHealth;
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
		
		//Make all non-hostile mobs level 1.
		if (mac.isHostile() == false)
			return;
		
		dml.getWorldDML(entity.getLocation());
		
		modifier = dml.getDistanceModifier();
	}
	
	public void handleHostileMobDrops(Location dropSpot)
	{
		List<ItemStack> drops = FC_Rpg.treasureConfig.getRandomDrops(level);
		
		for (ItemStack drop : drops)
			dropSpot.getWorld().dropItem(dropSpot, drop);	//Drop the item.
	}
	
	public void handlePassiveMobDrops(Location dropSpot)
	{
		EntityType entityType = getEntity().getType();
		
		//Drop loot for passive mobs.
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
		
		/* Drop emerald later
		else if (entityType == EntityType.VILLAGER))
		{
			dropPassiveItems(Material.DIAMOND, modifier, dropSpot);
		} */
		
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
	
	private void dropPassiveItem(Material material, Location dropSpot)
	{
		Random rand = new Random();
		ItemStack loot = new ItemStack(material, rand.nextInt(2) + 1);
		
		dropSpot.getWorld().dropItemNaturally(dropSpot, loot);
	}
	
	public boolean checkDungeonOne()
	{
		Location pointOne = new Location(entity.getWorld(), 141,0,1);
		Location pointTwo = new Location(entity.getWorld(), 344,300,28);
		
		LocationInsideAreaCheck liac = new LocationInsideAreaCheck(entity.getLocation(), pointOne, pointTwo);
		
		return liac.getIsInside();
	}
	
	public boolean checkDungeonTwo()
	{
		Location pointOne = new Location(entity.getWorld(), 148,0,-42);
		Location pointTwo = new Location(entity.getWorld(), 355,300,-20);
		
		LocationInsideAreaCheck liac = new LocationInsideAreaCheck(entity.getLocation(), pointOne, pointTwo);
		
		return liac.getIsInside();
	}
}

//Bukkit.getServer().broadcastMessage(ChatColor.RED + "Modifier: " + String.valueOf(modifier));

//Bukkit.getServer().broadcastMessage("(Create Function) Active for " + String.valueOf(mobId));
/*
if (entity.getType() == EntityType.CREEPER)
{
	defense = 3;
	dexterity = 1;
	constitution = 3;
}
else if (entity.getType() == EntityType.SPIDER)
{
	defense = 3;
	dexterity = 1;
	constitution = 3;
}
//Projectiles need to be strong so it goes above the 10 point spec.
else if (entity.getType() == EntityType.SKELETON)
{
	defense = 3;
	dexterity = 1;
	constitution = 3;
}
else if (entity.getType() == EntityType.ZOMBIE)
{
	defense = 3;
	dexterity = 1;
	constitution = 3;
}

defense = defense * modifier;
dexterity = dexterity * modifier;
constitution = constitution * modifier;
*/
