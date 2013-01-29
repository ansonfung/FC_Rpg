package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.LoadedObjects.RpgItem;
import me.Destro168.FC_Rpg.Util.MaterialLib;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		getEnchantChancePrefix();
		getEnchantChanceSuffix();
		getMultiplierCommon();
		getMultiplierRare();
		getMultiplierUnique();
		getMultiplierMythical();
		getMultiplierLegendary();
		getChancesPlus();
		getChancesTier();
	}
	
	public List<ItemStack> getRandomItemStackList(int entityLevel, List<Integer> materialMatchList)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		rand = new Random();
		ItemStack drop = null;
		totalDropsCount = 0;
		
		//While successful, keep getting items.
		while (getItemDropChance() == true)
		{
			drop = getRandomItem(entityLevel, materialMatchList);
			
			if (drop != null)
				items.add(drop);
		}
		
		return items;
	}
	
	public List<ItemStack> getRandomItemStackList(int entityLevel, int size, List<Integer> materialMatchList)
	{
		//Variable Declarations
		List<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack drop = null;
		rand = new Random();
		totalDropsCount = 0;
		
		//Get a fixed amount of loot.
		for (int i = 0; i < size; i++)
		{
			drop = getRandomItem(entityLevel, materialMatchList);
			
			if (drop != null)
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
		//Return if mob level is less than or equal to 0.
		if (mobLevel <= 0)
			return null;
		
		//Assign variables
		Random rand = new Random();
		List<RpgItem> itemList = new ArrayList<RpgItem>();
		RpgItem rpgItem;
		
		//We want to remove all items that don't have a range above -1.
		for (RpgItem ri : FC_Rpg.rpgItemList)
		{
			if (ri.dropLevelMin != -1 && materialMatchList.contains(ri.configFieldNumber) && mobLevel >= ri.dropLevelMin && mobLevel <= ri.dropLevelMax)
				itemList.add(ri);
		}
		
		if (itemList.size() == 0)
		{
			FC_Rpg.plugin.getLogger().info("ERROR, no valid items found to be dropped from monster");
			return null;
		}
		
		//Pick a random rpg item.
		rpgItem = itemList.get(rand.nextInt(itemList.size()));
		
		//Create the itemstack
		drop = new ItemStack(rpgItem.getMaterial(), rpgItem.dropAmountFlat);
		
		if (rpgItem.dropAmountRandom > 0)
			drop.setAmount(drop.getAmount() + rand.nextInt(rpgItem.dropAmountRandom));	//Set it's amount.
		
		//If it is enchantable, then we enchant.
		if (rpgItem.enchantType == 1) // 1 = Passive enchants.
		{
			drop.addEnchantments(getVanillaEnchantments());
		}
		else if (rpgItem.enchantType == 2)
		{
			// Variable Declarations
			Material dropType = drop.getType();
			ItemMeta iMeta = drop.getItemMeta();
			List<String> lore = new ArrayList<String>();
			double magnitude = 0;
			int plusValue = 0;
			int randValue;
			String plusValueString = "";
			String tier = "";
			String prefix = "";
			String prefixDescription = "";
			String suffix = "";
			String suffixDescription = "";
			String magString = "Damage Reduction: ";
			String tierColor = "";
			String itemName = "";
			boolean isArmor = true;
			
			// Determine drop effect magnitude (damge for swords, defense % for armor) for first line of lore.
			if (dropType.equals(Material.BOW))
			{
				magnitude = 1 + FC_Rpg.balanceConfig.getBowMultiplier();
				isArmor = false;
			}
		    else if (dropType.equals(Material.WOOD_SWORD))
		    {
		    	magnitude = 1 + FC_Rpg.balanceConfig.getSwordMultiplierWood();
				isArmor = false;
		    }
		    else if (dropType.equals(Material.STONE_SWORD))
		    {
		    	magnitude = 1 + FC_Rpg.balanceConfig.getSwordMultiplierStone();
				isArmor = false;
		    }
		    else if (dropType.equals(Material.IRON_SWORD))
		    {
		    	magnitude = 1 + FC_Rpg.balanceConfig.getSwordMultiplierIron();
				isArmor = false;
		    }
		    else if (dropType.equals(Material.DIAMOND_SWORD))
		    {
		    	magnitude = 1 + FC_Rpg.balanceConfig.getSwordMultiplierDiamond();
				isArmor = false;
		    }
		    else if (dropType.equals(Material.GOLD_SWORD))
		    {
		    	magnitude = 1 + FC_Rpg.balanceConfig.getSwordMultiplierGold();
				isArmor = false;
		    }
		    else if (dropType.equals(Material.LEATHER_BOOTS))
	    		magnitude = FC_Rpg.balanceConfig.getArmorMultiplierLB();
			
			else if (dropType.equals(Material.LEATHER_HELMET))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierLH();

			else if (dropType.equals(Material.LEATHER_LEGGINGS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierLL();

			else if (dropType.equals(Material.LEATHER_CHESTPLATE))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierLC();

			else if (dropType.equals(Material.CHAINMAIL_BOOTS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierCB();
			
			else if (dropType.equals(Material.CHAINMAIL_HELMET))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierCH();

			else if (dropType.equals(Material.CHAINMAIL_LEGGINGS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierCL();

			else if (dropType.equals(Material.CHAINMAIL_CHESTPLATE))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierCC();

			else if (dropType.equals(Material.IRON_BOOTS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierIB();

			else if (dropType.equals(Material.IRON_HELMET))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierIH();

			else if (dropType.equals(Material.IRON_LEGGINGS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierIL();

			else if (dropType.equals(Material.IRON_CHESTPLATE))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierIC();

			else if (dropType.equals(Material.DIAMOND_BOOTS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierDB();

			else if (dropType.equals(Material.DIAMOND_HELMET))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierDH();

			else if (dropType.equals(Material.DIAMOND_LEGGINGS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierDL();

			else if (dropType.equals(Material.DIAMOND_CHESTPLATE))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierDC();

			else if (dropType.equals(Material.GOLD_BOOTS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierGB();

			else if (dropType.equals(Material.GOLD_HELMET))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierGH();

			else if (dropType.equals(Material.GOLD_LEGGINGS))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierGL();

			else if (dropType.equals(Material.GOLD_CHESTPLATE))
				magnitude = FC_Rpg.balanceConfig.getArmorMultiplierGC();
			
			if (isArmor == false)
				magString = "Damage Bonus: ";
			
			// Determine tier and magnitude from it.
			rand = new Random();
			randValue = rand.nextInt(100);
			List<Integer> chances = getChancesTier();
			
			if (randValue < chances.get(0))
			{
				tier = "Common";
				magnitude = magnitude * getMultiplierCommon();
				tierColor = "&7";
			}
			else if (randValue < chances.get(1))
			{
				tier = "Normal";
				tierColor = "&f";
			}
			else if (randValue < chances.get(2))
			{
				tier = "Rare";
				magnitude = magnitude * getMultiplierRare();
				tierColor = "&9";
			}
			else if (randValue < chances.get(3))
			{
				tier = "Unique";
				magnitude = magnitude * getMultiplierUnique();
				tierColor = "&a";
			}
			else if (randValue < chances.get(4))
			{
				tier = "Mythical";
				magnitude = magnitude * getMultiplierMythical();
				tierColor = "&c";
			}
			else if (randValue >= chances.get(5))
			{
				tier = "Legendary";
				magnitude = magnitude * getMultiplierLegendary();
				tierColor = "&6";
			}
			
			// Determine prefix.
			rand = new Random();
			randValue = rand.nextInt(100);
			boolean checkPlusValue = false;
			
			// If we roll an enchantment prefix, then...
			if (randValue < getEnchantChancePrefix())
			{
				rand = new Random();
				randValue = rand.nextInt(FC_Rpg.enchantmentConfig.prefixList.size());
				prefix = FC_Rpg.enchantmentConfig.prefixList.get(randValue).name;
				prefixDescription = FC_Rpg.enchantmentConfig.prefixList.get(randValue).description;
				checkPlusValue = true;
			}
			
			// Determine suffix.
			rand = new Random();
			randValue = rand.nextInt(100);
			
			// If we roll an enchantment suffix (20% chance of getting one), then...
			if (randValue < getEnchantChanceSuffix())
			{
				rand = new Random();
				
				// Pick out enchants.
				if (isArmor)
				{
					randValue = rand.nextInt(FC_Rpg.enchantmentConfig.armorSuffixList.size());
					suffix = FC_Rpg.enchantmentConfig.armorSuffixList.get(randValue).name;
					suffixDescription = FC_Rpg.enchantmentConfig.armorSuffixList.get(randValue).description;
				}
				else
				{
					randValue = rand.nextInt(FC_Rpg.enchantmentConfig.weaponSuffixList.size());
					suffix = FC_Rpg.enchantmentConfig.weaponSuffixList.get(randValue).name;
					suffixDescription = FC_Rpg.enchantmentConfig.weaponSuffixList.get(randValue).description;
				}
				
				checkPlusValue = true;
			}
			
			if (checkPlusValue)
			{
				// Determine plus value
				rand = new Random();
				randValue = rand.nextInt(100);
				chances = getChancesPlus();
				
				if (randValue >= chances.get(0)) { plusValue++; plusValueString = " [+" + String.valueOf(plusValue) + "]"; }
				if (randValue >= chances.get(1)) { plusValue++; plusValueString = " [+" + String.valueOf(plusValue) + "]"; }
				if (randValue >= chances.get(2)) { plusValue++; plusValueString = " [+" + String.valueOf(plusValue) + "]"; }
				if (randValue >= chances.get(3)) { plusValue++; plusValueString = " [+" + String.valueOf(plusValue) + "]"; }
			}
			
			//Set item name.
			itemName = MaterialLib.getCleanName(dropType.toString());
			
			// Begin setting item meta with settings.
			ColorLib cl = new ColorLib();
			
			iMeta.setDisplayName(cl.parse(tierColor + prefix + " " + itemName + suffix + plusValueString));
			
			// Add spacer.
			lore.add(cl.parse("&3-----------"));
			
			lore.add(cl.parse("&3Tier: " + tierColor + tier));
			lore.add(cl.parse("&3" + magString + "&b" + FC_Rpg.df4.format(magnitude*100) + "&3%"));
			
			int counter = 1;
			
			if (!prefix.equalsIgnoreCase(""))
			{
				lore.add(cl.parse("&3Enchant " + counter + ": &b" + prefixDescription));
				counter++;
			}
			
			if (!suffix.equalsIgnoreCase(""))
			{
				lore.add(cl.parse("&3Enchant " + counter + ": &b" + suffixDescription));
				counter++;
			}
			
			//Set the final lore.
			iMeta.setLore(lore);
			
			//Set the final item meta.
			drop.setItemMeta(iMeta);
		}
		
		return drop;
	}
	
	private Map<Enchantment, Integer> getVanillaEnchantments()
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

	private int getEnchantChancePrefix() { return fcw.getStaticInt(prefix + "enchantChance.prefix", 10); }
	private int getEnchantChanceSuffix() { return fcw.getStaticInt(prefix + "enchantChance.suffix", 10); }
	public double getMultiplierCommon() { return fcw.getStaticDouble(prefix + "multiplier.common", .9); }
	public double getMultiplierRare() { return fcw.getStaticDouble(prefix + "multiplier.rare", 1.05); }
	public double getMultiplierUnique() { return fcw.getStaticDouble(prefix + "multiplier.unique", 1.1); }
	public double getMultiplierMythical() { return fcw.getStaticDouble(prefix + "multiplier.mythical", 1.15); }
	public double getMultiplierLegendary() { return fcw.getStaticDouble(prefix + "multiplier.legendary", 1.25); }
	private List<Integer> getChancesPlus() { return fcw.getStaticCustomIntegerList(prefix + "chances.plus","95,80,65,50"); }
	private List<Integer> getChancesTier() { return fcw.getStaticCustomIntegerList(prefix + "chances.tier","35,64,84,94,99,99"); }
	
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














