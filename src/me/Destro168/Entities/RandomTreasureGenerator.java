package me.Destro168.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class RandomTreasureGenerator 
{
	Random rand;
	
	int totalDropsCount;
	int typeNumber = 0;
	
	//ItemType listed next to item.
	ItemStack drop;
	
	public RandomTreasureGenerator()
	{
		rand = new Random();
		totalDropsCount = 0;
		typeNumber = 0;
		drop = null;
	}
	
	public List<ItemStack> getRandomItemList(int entityLevel)
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
	
	public ItemStack[] getRandomItemArray(int size, int entityLevel)
	{
		ItemStack[] items = new ItemStack[size];
		ItemStack drop = null;
		
		//While successful, keep getting items.
		for (int i = 0; i < size; i++)
		{
			drop = null;
			
			while (drop == null)
				drop = getRandomItem(entityLevel);
			
			items[i] = drop;
		}
		
		return items;
	}
	
	private boolean getItemDropChance()
	{
		//Return a chance to drop the item
		if (totalDropsCount == 0)
		{
			if (rand.nextInt(100) < 30) //30% Chance for success
				return true;
		}
		else if (totalDropsCount == 1)
		{
			if (rand.nextInt(100) < 20) //20% Chance for success
				return true;
		}
		else if (totalDropsCount == 2)
		{
			if (rand.nextInt(100) < 10) //10% Chance for success
				return true;
		}
		
		//Increase the number of items that have been dropped.
		totalDropsCount++;
		
		return false;
	}
	
	private ItemStack getRandomItem(int mobLevel)
	{
		//Assign variables
		Random rand = new Random();
		boolean enchantable = true;
		
		//Now we determine what type of item to drop
		typeNumber = rand.nextInt(11);
		
		if (typeNumber == 0)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.WOOD_SWORD);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.STONE_SWORD);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_SWORD);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_SWORD);
			else
				drop = new ItemStack(Material.GOLD_SWORD);
		}
		else if (typeNumber == 1)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.LEATHER_CHESTPLATE);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_CHESTPLATE);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_CHESTPLATE);
			else
				drop = new ItemStack(Material.GOLD_CHESTPLATE);
		}
		else if (typeNumber == 2)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.LEATHER_LEGGINGS,1);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.CHAINMAIL_LEGGINGS);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_LEGGINGS);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_LEGGINGS);
			else
				drop = new ItemStack(Material.GOLD_LEGGINGS);
		}
		else if (typeNumber == 3)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.LEATHER_HELMET);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.CHAINMAIL_HELMET);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_HELMET);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_HELMET);
			else
				drop = new ItemStack(Material.GOLD_HELMET);
		}
		else if (typeNumber == 4)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.LEATHER_BOOTS);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.CHAINMAIL_BOOTS);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_BOOTS);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_BOOTS);
			else
				drop = new ItemStack(Material.GOLD_BOOTS);
		}
		else if (typeNumber == 5)
		{
			drop = new ItemStack(Material.BOW);
		}
		else if (typeNumber == 6)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.WOOD_PICKAXE);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.STONE_PICKAXE);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_PICKAXE);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_PICKAXE);
			else
				drop = new ItemStack(Material.GOLD_PICKAXE);
		}
		else if (typeNumber == 7)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.WOOD_SPADE);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.STONE_SPADE);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_SPADE);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_SPADE);
			else
				drop = new ItemStack(Material.GOLD_SPADE);
		}
		else if (typeNumber == 8)
		{
			//Choose sword based on mob level.
			if (mobLevel < 20)
				drop = new ItemStack(Material.WOOD_AXE);
			else if (mobLevel < 40)
				drop = new ItemStack(Material.STONE_AXE);
			else if (mobLevel < 60)
				drop = new ItemStack(Material.IRON_AXE);
			else if (mobLevel < 80)
				drop = new ItemStack(Material.DIAMOND_AXE);
			else
				drop = new ItemStack(Material.GOLD_AXE);
		}
		else if (typeNumber == 9)
		{
			drop = new ItemStack(Material.BREAD, rand.nextInt(1) + 1);
			enchantable = false;
		}
		else if (typeNumber == 10)
		{
			drop = new ItemStack(Material.COOKED_BEEF, rand.nextInt(1) + 1);
			enchantable = false;
		}
		
		if (enchantable == true)
			drop.addEnchantments(getEnchantment());	//Add sword enchantments
		
		return drop;
	}
	
	private Map<Enchantment, Integer> getEnchantment()
	{
		//<Key, Value>
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		
		int totalEnchantCount = 0;
		int enchantmentType = 0;
		boolean success = false;
		
		success = getEnchantChance(totalEnchantCount);
		
		while (success == true)
		{
			//Increase number of enchantments give.
			totalEnchantCount++;
			
			//Swords
			if (typeNumber == 0)
			{
				//Roll a type of enchantment to put on.
				enchantmentType = rand.nextInt(4);
				
				//Add that enchantment.
				if (enchantmentType == 0)
					success = addEnchantment(enchantmentMap, Enchantment.DAMAGE_ALL);
				
				else if (enchantmentType == 1)
					success = addEnchantment(enchantmentMap, Enchantment.DAMAGE_UNDEAD);
				
				else if (enchantmentType == 2)
					success = addEnchantment(enchantmentMap, Enchantment.DAMAGE_ARTHROPODS);

				else if (enchantmentType == 3)
					success = addEnchantment(enchantmentMap, Enchantment.KNOCKBACK);
			}
			
			//Chest - Legging
			else if (typeNumber == 1 || typeNumber == 2)
			{
				//Roll a type of enchantment to put on.
				enchantmentType = rand.nextInt(4);
				
				//Add that enchantment.
				success = addEnchantment(enchantmentMap, getProtectionEnchant(enchantmentType));
			}
			
			//Helmet
			else if (typeNumber == 3)
			{
				//Roll a type of enchantment to put on.
				enchantmentType = rand.nextInt(6);
				
				//Add that enchantment.
				if (enchantmentType < 4)
					success = addEnchantment(enchantmentMap, getProtectionEnchant(enchantmentType));
				
				else if (enchantmentType == 4)
					success = addEnchantment(enchantmentMap, Enchantment.OXYGEN);
				
				else if (enchantmentType == 5)
					success = addEnchantment(enchantmentMap, Enchantment.WATER_WORKER);
			}
			
			//Boots
			else if (typeNumber == 4)
			{
				//Roll a type of enchantment to put on.
				enchantmentType = rand.nextInt(5);
				
				//Add that enchantment.
				if (enchantmentType < 4)
					success = addEnchantment(enchantmentMap, getProtectionEnchant(enchantmentType));
				else if (enchantmentType == 4)
					success = addEnchantment(enchantmentMap, Enchantment.PROTECTION_FALL);
			}
			
			//Bows
			else if (typeNumber == 5)
			{
				//Roll a type of enchantment to put on.
				enchantmentType = rand.nextInt(3);
				
				//Add that enchantment.
				if (enchantmentType == 0)
					success = addEnchantment(enchantmentMap, Enchantment.ARROW_DAMAGE);
				
				else if (enchantmentType == 1)
					success = addEnchantment(enchantmentMap, Enchantment.ARROW_INFINITE);
				
				else if (enchantmentType == 2)
					success = addEnchantment(enchantmentMap, Enchantment.ARROW_KNOCKBACK);
			}
			
			//Pickaxe
			else if (typeNumber == 6)
			{
				enchantmentType = rand.nextInt(4);
				
				if (enchantmentType < 3)
					success = addEnchantment(enchantmentMap, getToolEnchantment(enchantmentType));
				else if (enchantmentType == 3)
					success = addEnchantment(enchantmentMap, Enchantment.SILK_TOUCH);
			}
			
			//Axe - Shovel
			else if (typeNumber == 7 || typeNumber == 8)
			{
				enchantmentType = rand.nextInt(3);
				
				success = addEnchantment(enchantmentMap, getToolEnchantment(enchantmentType));
			}
			
			//Recalculate success chance if we didn't get a unique enchantment.
			if (success == false)
				totalEnchantCount--;
			
			success = getEnchantChance(totalEnchantCount);
		}
		
		return enchantmentMap;
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
	
	private boolean addEnchantment(Map<Enchantment, Integer> enchantmentMap, Enchantment enchant)
	{
		if (enchantmentMap.containsKey(enchant))
			return false;
		
		enchantmentMap.put(enchant, getEnchantStrength(enchant.getMaxLevel()));
		
		return true;
	}

	//- Format: Chance - Count /\ 25% - 1, 5% - 2, 1% - 3, .2% - 4, 1% - 5 /\ x/100 = 20% chance
	private boolean getEnchantChance(int totalEnchantCount)
	{
		//Return a chance to enchant the item.
		if (totalEnchantCount == 0)
			return true;
		else if (totalEnchantCount == 1)
		{
			if (rand.nextInt(100) < 50) //50% Chance for success
				return true;
		}
		else if (totalEnchantCount == 2)
		{
			if (rand.nextInt(100) < 25) //25% Chance for success
				return true;
		}
		else if (totalEnchantCount == 3)
		{
			if (rand.nextInt(100) < 5) //5% Chance for success
				return true;
		}
		else if (totalEnchantCount == 4)
		{
			if (rand.nextInt(100) < 5) //5% Chance for success
				return true;
		}
		else if (totalEnchantCount == 5)
		{
			if (rand.nextInt(100) < 5) //5% Chance for success
				return true;
		}
		
		return false;
	}
	
	private int getEnchantStrength(int maxEnchantmentLevels)
	{
		int strength;
		
		//Return a chance to drop the item
		if (maxEnchantmentLevels == 5)	//1 - 50%, 2 - 30%, 3 - 15%, 4 - 9%, 1 - 1%  
		{
			strength = rand.nextInt(100);
			
			if (strength < 50)
				return 1;
			else if (strength < 80)
				return 2;
			else if (strength < 95)
				return 3;
			else if (strength < 99)
				return 4;
			else
				return 5;
		}
		else if (maxEnchantmentLevels == 4)	//1 - 55.4%, 2 - 33.3%, 3 - 10.1%, 4 - 1.2%  
		{
			strength = rand.nextInt(1000);
			
			if (strength < 554)
				return 1;
			else if (strength < 887)
				return 2;
			else if (strength < 989)
				return 3;
			else
				return 4;
		}
		else if (maxEnchantmentLevels == 3)	//80%, 16%, 4%
		{
			strength = rand.nextInt(100);
			
			if (strength < 80)
				return 1;
			else if (strength < 96)
				return 2;
			else
				return 3;
		}
		else if (maxEnchantmentLevels == 2)	//90%, 10%
		{
			strength = rand.nextInt(100);
			
			if (strength < 90)
				return 1;
			else if (strength < 10)
				return 2;
		}
		
		//Always return 1 by default.
		return 1;
	}
}
