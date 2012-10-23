package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;
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
	
	private void setDropChances(int a, int b, int c, int d, int e) { ccm.set(prefix + "dropChances", a + "," + b + "," + c); }
	private void setEnchantChances(int a, int b, int c, int d, int e) { ccm.set(prefix + "enchantChances", a + "," + b + "," + c + "," + d + "," + e); }
	
	private void setEnchantLevelFiveChance(int a, int b, int c, int d) { ccm.set(prefix + "enchantLevelFiveChance", a + "," + b + "," + c + "," + d); }
	private void setEnchantLevelFourChance(int a, int b, int c) { ccm.set(prefix + "enchantLevelFourChance", a + "," + b + "," + c); }
	private void setEnchantLevelThreeChance(int a, int b) { ccm.set(prefix + "enchantLevelThreeChance", a + "," + b); }
	private void setEnchantLevelTwoChance(int a) { ccm.set(prefix + "enchantLevelTwoChance", a); }
	private void setTreasureListIDs(List<Integer> a) { ccm.setCustomList(prefix + "treasureList.ids", a); }
	private void setTreasureListEnchantable(List<Integer> a) { ccm.setCustomList(prefix + "treasureList.enchantable", a); }
	private void setTreasureListMobLevelRangeMin(List<Integer> a) { ccm.setCustomList(prefix + "treasureList.mobLevelRangeMin", a); }
	private void setTreasureListMobLevelRangeMax(List<Integer> a) { ccm.setCustomList(prefix + "treasureList.mobLevelRangeMax", a); }
	private void setTreasureListAmountFlat(List<Integer> a) { ccm.setCustomList(prefix + "treasureList.amountFlat", a); }
	private void setTreasureListAmountRandom(List<Integer> a) { ccm.setCustomList(prefix + "treasureList.amountRandom", a); }
	
	private List<Integer> getDropChances() { return converter.getIntegerListFromString(ccm.getString(prefix + "dropChances")); }
	private List<Integer> getEnchantChances() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantChances")); }
	private List<Integer> getEnchantLevelFiveChance() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantLevelFiveChance")); }
	private List<Integer> getEnchantLevelFourChance() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantLevelFourChance")); }
	private List<Integer> getEnchantLevelThreeChance() { return converter.getIntegerListFromString(ccm.getString(prefix + "enchantLevelThreeChance")); }
	private int getEnchantLevelTwoChance() { return ccm.getInt(prefix + "enchantLevelTwoChance"); }
	private List<Integer> getTreasureListIDs() { return converter.getIntegerListFromString(ccm.getString(prefix + "treasureList.ids")); }
	private List<Integer> getTreasureListEnchantable() { return converter.getIntegerListFromString(ccm.getString(prefix + "treasureList.enchantable")); }
	private List<Integer> getTreasureListMobLevelRangeMin() { return converter.getIntegerListFromString(ccm.getString(prefix + "treasureList.mobLevelRangeMin")); }
	private List<Integer> getTreasureListMobLevelRangeMax() { return converter.getIntegerListFromString(ccm.getString(prefix + "treasureList.mobLevelRangeMax")); }
	private List<Integer> getTreasureListAmountFlat() { return converter.getIntegerListFromString(ccm.getString(prefix + "treasureList.amountFlat")); }
	private List<Integer> getTreasureListAmountRandom() { return converter.getIntegerListFromString(ccm.getString(prefix + "treasureList.amountRandom")); }
	
	List<Treasure> treasureList = new ArrayList<Treasure>();
	
	public class Treasure
	{
		int id;
		int enchantable;
		int rangeMin;
		int rangeMax;
		int amountFlat;
		int amountRandom;
		
		public Treasure(int id_, int enchantable_, int rangeMin_, int rangeMax_, int amountFlat_, int amountRandom_)
		{
			id = id_;
			enchantable = enchantable_;
			rangeMin = rangeMin_;
			rangeMax = rangeMax_;
			amountFlat = amountFlat_;
			amountRandom = amountRandom_;
		}
	}
	
	public TreasureConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Treasure");
		
		setDefaults();
		handleUpdates();
	}
	
	private void setDefaults()
	{
		rand = new Random();
		totalDropsCount = 0;
		drop = null;
	}
	
	private void handleUpdates()
	{
		if (getVersion() < 0.1)
		{
			setVersion(0.1);
			
			//Reset settings.
			setDropChances(3500,2000,1000,3000,1000);
			setEnchantChances(5000,2500,1200,700,300);
			setEnchantLevelFiveChance(5000, 8000, 9500, 9900);
			setEnchantLevelFourChance(5540, 8870, 9890);
			setEnchantLevelThreeChance(8000, 9600);
			setEnchantLevelTwoChance(9000);
		}
		
		if (getVersion() < 0.2)
		{
			MaterialLib mLib = new MaterialLib();
			int total_mats = MaterialLib.TIER_TOTAL_ITEMS;
			
			List<Integer> a = new ArrayList<Integer>();
			List<Integer> b = new ArrayList<Integer>();
			List<Integer> c = new ArrayList<Integer>();
			List<Integer> d = new ArrayList<Integer>();
			List<Integer> e = new ArrayList<Integer>();
			List<Integer> f = new ArrayList<Integer>();
			
			for (int i = 0; i < 5; i++)
			{
				for (int j = 0; j < total_mats; j++)
				{
					a.add(mLib.tierList.get(i).get(j).getId());
					b.add(1);
					c.add(i * 20);
					d.add(i * 20 + 20);
					e.add(1);
					f.add(0);
				}
			}
			
			a.add(Material.BOW.getId());
			b.add(1);
			c.add(0);
			d.add(100);
			e.add(1);
			f.add(0);
			
			a.add(Material.BREAD.getId());
			b.add(1);
			c.add(0);
			d.add(100);
			e.add(1);
			f.add(1);
			
			a.add(Material.COOKED_BEEF.getId());
			b.add(1);
			c.add(0);
			d.add(100);
			e.add(1);
			f.add(1);
			
			setTreasureListIDs(a);
			setTreasureListEnchantable(b);
			setTreasureListMobLevelRangeMin(c);
			setTreasureListMobLevelRangeMax(d);
			setTreasureListAmountFlat(e);
			setTreasureListAmountRandom(f);
		}
		
		//Load up treasure.
		loadTreasure();
	}
	
	public void loadTreasure()
	{
		List<Integer> a = getTreasureListIDs();
		List<Integer> b = getTreasureListEnchantable();
		List<Integer> c = getTreasureListMobLevelRangeMin();
		List<Integer> d = getTreasureListMobLevelRangeMax();
		List<Integer> e = getTreasureListAmountFlat();
		List<Integer> f = getTreasureListAmountRandom();
		
		for (int i = 0; i < a.size(); i++)
			treasureList.add(new Treasure(a.get(i), b.get(i), c.get(i), d.get(i), e.get(i), f.get(i)));
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
		Treasure t = treasureList.get(rand.nextInt(treasureList.size()));
		int breakLimit = 0;
		
		//Only check if the mobs levle is above 0.
		if (mobLevel > 0)
		{
			//Get a random item in the treasures mob level range.
			while (mobLevel < t.rangeMin && mobLevel > t.rangeMin)
			{
				t = treasureList.get(rand.nextInt(treasureList.size()));
				
				breakLimit++;
				
				if (breakLimit > 50)
				{
					FC_Rpg.plugin.getLogger().info("Unable to find a proper item to drop due to mob level ranges.");
					return null;
				}
			}
		}
		
		//Create the itemstack
		drop = new ItemStack(t.id);
		
		if (t.amountRandom > 0)
			drop.setAmount(drop.getAmount() + rand.nextInt(t.amountRandom));	//Set it's amount.
		
		//If it is enchantable, then we enchant.
		if (t.enchantable == 1)
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














