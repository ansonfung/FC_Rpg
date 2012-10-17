package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.Util.MaterialLib;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TreasureConfig extends ConfigGod
{
	private static final int randomConstant = 10000;
	
	private Random rand;
	private ItemStack drop;
	private int totalDropsCount;
	private int typeNumber = 0;
	
	private void setDropChances(int a, int b, int c, int d, int e) { ccm.set(prefix + "dropChances", a + "," + b + "," + c); }
	private void setEnchantChances(int a, int b, int c, int d, int e) { ccm.set(prefix + "enchantChances", a + "," + b + "," + c + "," + d + "," + e); }
	
	private void setEnchantLevelFiveChance(int a, int b, int c, int d) { ccm.set(prefix + "enchantLevelFiveChance", a + "," + b + "," + c + "," + d); }
	private void setEnchantLevelFourChance(int a, int b, int c) { ccm.set(prefix + "enchantLevelFourChance", a + "," + b + "," + c); }
	private void setEnchantLevelThreeChance(int a, int b) { ccm.set(prefix + "enchantLevelThreeChance", a + "," + b); }
	private void setEnchantLevelTwoChance(int a) { ccm.set(prefix + "enchantLevelTwoChance", a); }
	
	private List<Integer> getDropChances() { return converter.getIntegerListFromString(ccm.getString(prefix + "dropChances")); }
	private List<Integer> getEnchantChances() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantChances")); }
	private List<Integer> getEnchantLevelFiveChance() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantLevelFiveChance")); }
	private List<Integer> getEnchantLevelFourChance() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantLevelFourChance")); }
	private List<Integer> getEnchantLevelThreeChance() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantLevelThreeChance")); }
	private int getEnchantLevelTwoChance() { return ccm.getInt(prefix + "enchantLevelTwoChance"); }
	
	public TreasureConfig()
	{
		super("Treasure");
		
		setDefaults();
		handleUpdates();
	}
	
	private void setDefaults()
	{
		rand = new Random();
		totalDropsCount = 0;
		typeNumber = 0;
		drop = null;
	}
	
	private void handleUpdates()
	{
		//TODO - revert to 1.0
		if (getVersion() < 1.2)
		{
			setDropChances(9900,9900,9900,9900,9900);
			//setDropChances(3500,2000,1000,3000,1000);
			setEnchantChances(5000,2500,1200,700,300);
			setEnchantLevelFiveChance(5000, 8000, 9500, 9900);
			setEnchantLevelFourChance(5540, 8870, 9890);
			setEnchantLevelThreeChance(8000, 9600);
			setEnchantLevelTwoChance(9000);
		}
	}
	
	public List<ItemStack> getRandomDrops(int entityLevel)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack drop = null;
		
		//While successful, keep getting items.
		while (getItemDropChance() == true)
		{
			drop = null;
			
			while (drop == null)
				drop = getRandomItem(entityLevel);
			
			items.add(drop);
		}
		
		return items;
	}
	
	public List<ItemStack> getRandomTreasure(int entityLevel, int size)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack drop = null;
		
		//Get a fixed amount of loot.
		for (int i = 0; i < size; i++)
		{
			drop = null;
			
			while (drop == null)
				drop = getRandomItem(entityLevel);
			
			items.add(drop);
		}
		
		return items;
	}
	
	private boolean getItemDropChance()
	{
		//Variable Declarations
		List<Integer> dropChances = getDropChances();
		
		//Return a chance to drop the item
		if (rand.nextInt(randomConstant) < dropChances.get(totalDropsCount))
			return true;
		
		//Increase the number of items that have been dropped.
		totalDropsCount++;
		
		return false;
	}
	
	private ItemStack getRandomItem(int mobLevel)
	{
		//Assign variables
		Random rand = new Random();
		MaterialLib mLib = new MaterialLib();
		int total_mats = MaterialLib.TIER_TOTAL_ITEMS;
		int total_other_mats = 3;
		
		boolean enchantable = true;
		
		//Now we determine what type of item to drop
		typeNumber = rand.nextInt(total_mats + total_other_mats);
		
		//Check first material lib items.
		if (typeNumber < total_mats)
		{
			if (mobLevel < 20)
				drop = new ItemStack(mLib.t0.get(typeNumber));
			else if (mobLevel < 40)
				drop = new ItemStack(mLib.t1.get(typeNumber));
			else if (mobLevel < 60)
				drop = new ItemStack(mLib.t2.get(typeNumber));
			else if (mobLevel < 80)
				drop = new ItemStack(mLib.t3.get(typeNumber));
			else
				drop = new ItemStack(mLib.t4.get(typeNumber));
		}
		
		//Check bows.
		else if (typeNumber == total_mats)
		{
			drop = new ItemStack(Material.BOW);
		}
		
		//Check bread.
		else if (typeNumber == total_mats + 1)
		{
			drop = new ItemStack(Material.BREAD, rand.nextInt(1) + 1);
			enchantable = false;
		}
		
		//Check cooked_beef.
		else if (typeNumber == total_mats + 2)
		{
			drop = new ItemStack(Material.COOKED_BEEF, rand.nextInt(1) + 1);
			enchantable = false;
		}
		
		//Add enchants if enchantable.
		if (enchantable == true)
			drop.addEnchantments(getEnchantment());
		
		return drop;
	}
	
	private Map<Enchantment, Integer> getEnchantment()
	{
		//<Key, Value>
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		int totalEnchantCount = 0;
		
		while (getChanceForEnchantment(totalEnchantCount) == true)
		{
			if (addEnchantment(enchantmentMap) == true)
			{
				//Increase number of enchantments given.
				totalEnchantCount++;
			}
			else
				break;
		}
		
		return enchantmentMap;
	}
	
	private boolean addEnchantment(Map<Enchantment, Integer> enchantmentMap)
	{
		MaterialLib ml = new MaterialLib();
		int enchantmentType = 0;
		Material dropType = drop.getType();
		boolean success = false;
		
		//Swords
		if (ml.swords.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(4);
			
			//Add that enchantment.
			if (enchantmentType == 0)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.DAMAGE_ALL);
			
			else if (enchantmentType == 1)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.DAMAGE_UNDEAD);
			
			else if (enchantmentType == 2)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.DAMAGE_ARTHROPODS);

			else if (enchantmentType == 3)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.KNOCKBACK);
		}
		
		//Chest - Legging
		else if (ml.chestplates.contains(dropType) || ml.leggings.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(4);
			
			//Add that enchantment.
			success = addEnchantToEnchantmentMap(enchantmentMap, getProtectionEnchant(enchantmentType));
		}
		
		//Helmet
		else if (ml.helmets.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(6);
			
			//Add that enchantment.
			if (enchantmentType < 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, getProtectionEnchant(enchantmentType));
			
			else if (enchantmentType == 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.OXYGEN);
			
			else if (enchantmentType == 5)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.WATER_WORKER);
		}
		
		//Boots
		else if (ml.boots.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(5);
			
			//Add that enchantment.
			if (enchantmentType < 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, getProtectionEnchant(enchantmentType));
			else if (enchantmentType == 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.PROTECTION_FALL);
		}
		
		//Bows
		else if (dropType == Material.BOW)
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(3);
			
			//Add that enchantment.
			if (enchantmentType == 0)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.ARROW_DAMAGE);
			
			else if (enchantmentType == 1)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.ARROW_INFINITE);
			
			else if (enchantmentType == 2)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.ARROW_KNOCKBACK);
		}
		
		//Pickaxe
		else if (ml.pickaxes.contains(dropType))
		{
			enchantmentType = rand.nextInt(4);
			
			if (enchantmentType < 3)
				success = addEnchantToEnchantmentMap(enchantmentMap, getToolEnchantment(enchantmentType));
			else if (enchantmentType == 3)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.SILK_TOUCH);
		}
		
		//Axe - Shovel
		else if (ml.axes.contains(dropType) || ml.spades.contains(dropType))
		{
			enchantmentType = rand.nextInt(3);
			
			success = addEnchantToEnchantmentMap(enchantmentMap, getToolEnchantment(enchantmentType));
		}
		
		return success;
	}
	private Enchantment getToolEnchantment(int enchantmentType)
	{
		if (enchantmentType == 0)
			return Enchantment.DIG_SPEED;
		
		else if (enchantmentType == 1)
			return Enchantment.DURABILITY;

		else
			return Enchantment.LOOT_BONUS_BLOCKS;
	}
	
	private Enchantment getProtectionEnchant(int enchantmentType)
	{
		if (enchantmentType == 0)
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		
		else if (enchantmentType == 1)
			return Enchantment.PROTECTION_EXPLOSIONS;
		
		else if (enchantmentType == 2)
			return Enchantment.PROTECTION_FIRE;
		
		else
			return Enchantment.PROTECTION_PROJECTILE;
	}
	
	private boolean addEnchantToEnchantmentMap(Map<Enchantment, Integer> enchantmentMap, Enchantment enchant)
	{
		if (enchantmentMap.containsKey(enchant))
			return false;
		
		enchantmentMap.put(enchant, getEnchantStrength(enchant.getMaxLevel()));
		
		return true;
	}

	//- Format: Chance - Count /\ 25% - 1, 5% - 2, 1% - 3, .2% - 4, 1% - 5 /\ x/100 = 20% chance
	private boolean getChanceForEnchantment(int totalEnchantCount)
	{
		//Variable Declarations
		List<Integer> enchantChances = getEnchantChances();
		
		//Return a chance to enchant the item.
		if (totalEnchantCount >= enchantChances.size())
			return false;
		else
		{
			if (rand.nextInt(randomConstant) < enchantChances.get(totalEnchantCount))
				return true;
		}
		
		return false;
	}
	
	private int getEnchantStrength(int maxEnchantmentLevels)
	{
		//Variable Declarations
		int strength = rand.nextInt(randomConstant);
		List<Integer> strengthChance = new ArrayList<Integer>();
		
		//Return a chance to add an enchantment
		if (maxEnchantmentLevels == 5)
			strengthChance = getEnchantLevelFiveChance();
		
		else if (maxEnchantmentLevels == 4)
			strengthChance = getEnchantLevelFourChance();
		
		else if (maxEnchantmentLevels == 3)
			strengthChance = getEnchantLevelThreeChance();
			
		else if (maxEnchantmentLevels == 2)
			strengthChance.add(getEnchantLevelTwoChance());
		
		for (int i = 0; i < strengthChance.size(); i++)
		{
			if (strength < strengthChance.get(i))
				return i + 1;
		}
		
		//Always return 1 by default if no enchant was found.
		return 1;
	}
}














