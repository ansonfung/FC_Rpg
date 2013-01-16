package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.LoadedObjects.RpgItem;
import me.Destro168.FC_Rpg.Util.MaterialLib;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TreasureConfig extends ConfigGod
{
	private static final int randomConstant = 10000;
	
	private Random rand;
	private ItemStack drop;
	private String lootList = "";
	private int totalDropsCount;
	
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
		// Handle version updates.
		if (getVersion() < 0.2)
			setVersion(0.2);
		
		// Always load up a default satatic loot list.
		getDropChances();
		getEnchantChances();
		getEnchantLevelFiveChance();
		getEnchantLevelFourChance();
		getEnchantLevelThreeChance();
		getEnchantLevelTwoChance();
		getLootList("default");
	}
	
	public List<ItemStack> getRandomDrops(int entityLevel, List<Integer> materialMatchList)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		rand = new Random();
		ItemStack drop = null;
		totalDropsCount = 0;
		
		//While successful, keep getting items.
		while (getItemDropChance() == true)
		{
			drop = null;
			
			while (drop == null)
				drop = getRandomItem(entityLevel, materialMatchList);
			
			items.add(drop);
		}
		
		return items;
	}
	
	public List<ItemStack> getRandomTreasure(int entityLevel, int size, List<Integer> materialMatchList)
	{
		//Variable Declarations
		List<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack drop = null;
		rand = new Random();
		totalDropsCount = 0;
		
		//Get a fixed amount of loot.
		for (int i = 0; i < size; i++)
		{
			drop = null;
			
			while (drop == null)
				drop = getRandomItem(entityLevel, materialMatchList);
			
			items.add(drop);
		}
		
		return items;
	}
	
	private boolean getItemDropChance()
	{
		//Variable Declarations
		List<Integer> dropChances = getDropChances();
		
		//Return true to drop an item if true.
		try
		{
			if (rand.nextInt(randomConstant) < dropChances.get(totalDropsCount))
			{
				totalDropsCount++;
				return true;
			}
		}
		catch (IndexOutOfBoundsException e) { 
			return false; 
		}
		
		return false;
	}
	
	private ItemStack getRandomItem(int mobLevel, List<Integer> materialMatchList)
	{
		//Assign variables
		Random rand = new Random();
		List<RpgItem> itemList = new ArrayList<RpgItem>();
		int breakLimit = 0;
		
		//We want to remove all items that don't have a range above -1.
		for (RpgItem ri : FC_Rpg.rpgItemList)
		{
			if (ri.dropLevelMin != -1 && materialMatchList.contains(ri.configFieldNumber))
				itemList.add(ri);
		}
		
		//Return if mob level is less than or equal to 0.
		if (mobLevel <= 0)
			return null;
		
		//Pick a random rpg item.
		RpgItem rpgItem = itemList.get(rand.nextInt(itemList.size()));
		
		//Get a random item in the treasures mob level range.
		while (mobLevel < rpgItem.dropLevelMin || mobLevel > rpgItem.dropLevelMax)
		{
			rpgItem = itemList.get(rand.nextInt(itemList.size()));
			
			breakLimit++;
			
			if (breakLimit > 50)
			{
				//Don't attempt to drop items when dungeons are started with no players (ie, while debugging).
				if (mobLevel == 999999)
					return null;
				
				return null;
			}
		}
		
		//Create the itemstack
		drop = new ItemStack(rpgItem.getMaterial(), rpgItem.dropAmountFlat);
		
		if (rpgItem.dropAmountRandom > 0)
			drop.setAmount(drop.getAmount() + rand.nextInt(rpgItem.dropAmountRandom));	//Set it's amount.
		
		//If it is enchantable, then we enchant.
		if (rpgItem.enchantable == true)
		{
			drop.addEnchantments(getEnchantment());
			
			MaterialLib ml = FC_Rpg.mLib;
			Material dropType = drop.getType();
			
			// Add unbreaking to all armor.
		    if (ml.chestplates.contains(dropType) || ml.leggings.contains(dropType) || ml.helmets.contains(dropType) || ml.boots.contains(dropType))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
		    
		    else if (dropType.equals(Material.BOW))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 30);
		    
		    else if (dropType.equals(Material.WOOD_SWORD))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 73);
		    
		    else if (dropType.equals(Material.STONE_SWORD))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 36);
			
		    else if (dropType.equals(Material.IRON_SWORD))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		    
		    else if (dropType.equals(Material.DIAMOND_SWORD))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		    
		    else if (dropType.equals(Material.GOLD_SWORD))
				drop.addUnsafeEnchantment(Enchantment.DURABILITY, 150);
		}
		
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
		MaterialLib ml = FC_Rpg.mLib;
		int enchantmentType = 0;
		Material dropType = drop.getType();
		boolean success = false;
		
		//Swords
		if (ml.swords.contains(dropType))
		{
			//Variable declaration
			List<Enchantment> enchantList = new ArrayList<Enchantment>();
			
			enchantList.add(Enchantment.DAMAGE_ALL);
			enchantList.add(Enchantment.DAMAGE_UNDEAD);
			enchantList.add(Enchantment.DAMAGE_ARTHROPODS);
			
			// Add knockback if sword knockback is enabled.
			if (FC_Rpg.balanceConfig.getSwordKnockback())
				enchantList.add(Enchantment.KNOCKBACK);
			
			// Add lootbonus if default drops are enabled.
			if (FC_Rpg.balanceConfig.getDefaultItemDrops())
				enchantList.add(Enchantment.LOOT_BONUS_MOBS);
			
			//Add the enchantment by rolling from available enchants.
			success = addEnchantToEnchantmentMap(enchantmentMap, enchantList.get(rand.nextInt(enchantList.size())), -1);
		}
		
		//Chest - Legging
		else if (ml.chestplates.contains(dropType) || ml.leggings.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(4);
			
			//Add that enchantment.
			success = addEnchantToEnchantmentMap(enchantmentMap, getProtectionEnchant(enchantmentType), -1);
		}
		
		//Helmet
		else if (ml.helmets.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(6);
			
			//Add that enchantment.
			if (enchantmentType < 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, getProtectionEnchant(enchantmentType), -1);
			
			else if (enchantmentType == 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.OXYGEN, -1);
			
			else if (enchantmentType == 5)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.WATER_WORKER, -1);
		}
		
		//Boots
		else if (ml.boots.contains(dropType))
		{
			//Roll a type of enchantment to put on.
			enchantmentType = rand.nextInt(5);
			
			//Add that enchantment.
			if (enchantmentType < 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, getProtectionEnchant(enchantmentType), -1);
			else if (enchantmentType == 4)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.PROTECTION_FALL, -1);
		}
		
		//Bows
		else if (dropType.equals(Material.BOW))
		{
			//Roll a type of enchantment to put on.
			if (FC_Rpg.balanceConfig.getArrowKnockback())
				enchantmentType = rand.nextInt(3); // include knockback
			else
				enchantmentType = rand.nextInt(2); // don't include knockback
			
			//Add that enchantment.
			if (enchantmentType == 0)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.ARROW_DAMAGE, -1);
			
			else if (enchantmentType == 1)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.ARROW_INFINITE, -1);
			
			else if (enchantmentType == 2)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.ARROW_KNOCKBACK, -1);
		}
		
		//Pickaxe
		else if (ml.pickaxes.contains(dropType))
		{
			enchantmentType = rand.nextInt(4);
			
			if (enchantmentType < 3)
				success = addEnchantToEnchantmentMap(enchantmentMap, getToolEnchantment(enchantmentType), -1);
			else if (enchantmentType == 3)
				success = addEnchantToEnchantmentMap(enchantmentMap, Enchantment.SILK_TOUCH, -1);
		}
		
		//Axe - Shovel
		else if (ml.axes.contains(dropType) || ml.spades.contains(dropType))
		{
			enchantmentType = rand.nextInt(3);
			
			success = addEnchantToEnchantmentMap(enchantmentMap, getToolEnchantment(enchantmentType), -1);
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
	
	private boolean addEnchantToEnchantmentMap(Map<Enchantment, Integer> enchantmentMap, Enchantment enchant, int enchantStrength)
	{
		if (enchantmentMap.containsKey(enchant))
			return false;
		
		if (enchantStrength > 0)
			enchantmentMap.put(enchant, enchantStrength);
		else
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
		int strength;
		List<Integer> strengthChance = new ArrayList<Integer>();
		int finalEnchantStrength = 1;
		
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
			strength = rand.nextInt(randomConstant);
			
			if (strengthChance.get(i) < strength)
				finalEnchantStrength += 1;
		}
		
		//Always return 1 by default if no enchant was found.
		return finalEnchantStrength;
	}
	
	/****************************************************************
	 ^ Configuration Accessing Methods 
	 - All Dynamically Accessed
	****************************************************************/
	
	private List<Integer> getDropChances() { return fcw.getStaticCustomIntegerList(prefix + "dropChances","3500,2000,1000,300,100"); }
	private List<Integer> getEnchantChances() { return fcw.getStaticCustomIntegerList(prefix + "enchantChances","8000,6000,4000,4000,4000"); }
	private List<Integer> getEnchantLevelFiveChance() { return fcw.getStaticCustomIntegerList(prefix + "enchantLevelFiveChance","8000,7000,6000,5000"); }
	private List<Integer> getEnchantLevelFourChance() { return fcw.getStaticCustomIntegerList(prefix + "enchantLevelFourChance","8000,7000,600"); }
	private List<Integer> getEnchantLevelThreeChance() { return fcw.getStaticCustomIntegerList(prefix + "enchantLevelThreeChance","8000,7000"); }
	private int getEnchantLevelTwoChance() { return fcw.getStaticInt(prefix + "enchantLevelTwoChance",8000); }
	
	public List<Integer> getLootList(String name)
	{
		if (lootList.equals(""))
		{
			for (int i = 0; i < FC_Rpg.rpgItemList.size() - 1; i++)
				lootList += String.valueOf(i) + ",";
			
			lootList += (FC_Rpg.rpgItemList.size() - 1) + "";
		}
		
		return fcw.getStaticCustomIntegerList(prefix + "lootList." + name, lootList);
	}
}














