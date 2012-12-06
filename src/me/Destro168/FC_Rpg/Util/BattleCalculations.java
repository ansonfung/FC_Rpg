package me.Destro168.FC_Rpg.Util;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Entities.MobEquipment;
import me.Destro168.FC_Rpg.Entities.RpgMonster;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BattleCalculations
{
	public BattleCalculations() { }
	
	//Get weapon bonuses.
	public double getWeaponModifier(Material weapon, int strength)
	{
		if (weapon.equals(Material.WOOD_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementWood())
				return FC_Rpg.balanceConfig.getSwordMultiplierWood();
		}
		else if (weapon.equals(Material.STONE_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementStone())
				return FC_Rpg.balanceConfig.getSwordMultiplierStone();
		}
		else if (weapon.equals(Material.IRON_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementIron())
				return FC_Rpg.balanceConfig.getSwordMultiplierIron();
		}
		else if (weapon.equals(Material.DIAMOND_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementDiamond())
				return FC_Rpg.balanceConfig.getSwordMultiplierDiamond();
		}
		else if (weapon.equals(Material.GOLD_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementGold())
				return FC_Rpg.balanceConfig.getSwordMultiplierGold();
		}
		
		return 1;
	}
	
	// Get armor bonuses.
	public double getRpgPlayerArmorBonus(RpgPlayer rpgPlayer)
	{
		Player player = rpgPlayer.getPlayer();
		double armorBonus = 1;
		int constitution = rpgPlayer.getPlayerConfig().getConstitution();

		armorBonus += getArmorPieceBonus(player.getInventory().getHelmet(), constitution);
		armorBonus += getArmorPieceBonus(player.getInventory().getLeggings(), constitution);
		armorBonus += getArmorPieceBonus(player.getInventory().getChestplate(), constitution);
		armorBonus += getArmorPieceBonus(player.getInventory().getBoots(), constitution);

		return armorBonus;
	}

	public double getRpgMonsterArmorBonus(RpgMonster rpgMonster)
	{
		LivingEntity entity = rpgMonster.getEntity();
		double armorBonus = 1;
		int constitution = rpgMonster.getConstitution();

		armorBonus += getArmorPieceBonus(MobEquipment.getHelmet(entity), constitution);
		armorBonus += getArmorPieceBonus(MobEquipment.getPants(entity), constitution);
		armorBonus += getArmorPieceBonus(MobEquipment.getChestplate(entity), constitution);
		armorBonus += getArmorPieceBonus(MobEquipment.getBoots(entity), constitution);

		return armorBonus;
	}

	public double getArmorPieceBonus(ItemStack armor, int constitution)
	{
		if (armor != null)
			return getArmorMultiplier(armor.getType(), constitution);

		return 0;
	}

	public double getArmorMultiplier(Material armor, int constitution)
	{
		if (armor.equals(Material.LEATHER_BOOTS))
			return FC_Rpg.balanceConfig.getArmorMultiplierLB();

		else if (armor.equals(Material.LEATHER_HELMET))
			return FC_Rpg.balanceConfig.getArmorMultiplierLH();

		else if (armor.equals(Material.LEATHER_LEGGINGS))
			return FC_Rpg.balanceConfig.getArmorMultiplierLL();

		else if (armor.equals(Material.LEATHER_CHESTPLATE))
			return FC_Rpg.balanceConfig.getArmorMultiplierLC();

		if (constitution > 124)
		{
			if (armor.equals(Material.CHAINMAIL_BOOTS))
				return FC_Rpg.balanceConfig.getArmorMultiplierCB();

			else if (armor.equals(Material.CHAINMAIL_HELMET))
				return FC_Rpg.balanceConfig.getArmorMultiplierCH();

			else if (armor.equals(Material.CHAINMAIL_LEGGINGS))
				return FC_Rpg.balanceConfig.getArmorMultiplierCL();

			else if (armor.equals(Material.CHAINMAIL_CHESTPLATE))
				return FC_Rpg.balanceConfig.getArmorMultiplierCC();
		}

		if (constitution > 249)
		{
			if (armor.equals(Material.IRON_BOOTS))
				return FC_Rpg.balanceConfig.getArmorMultiplierIB();

			else if (armor.equals(Material.IRON_HELMET))
				return FC_Rpg.balanceConfig.getArmorMultiplierIH();

			else if (armor.equals(Material.IRON_LEGGINGS))
				return FC_Rpg.balanceConfig.getArmorMultiplierIL();

			else if (armor.equals(Material.IRON_CHESTPLATE))
				return FC_Rpg.balanceConfig.getArmorMultiplierIC();
		}

		if (constitution > 374)
		{
			if (armor.equals(Material.DIAMOND_BOOTS))
				return FC_Rpg.balanceConfig.getArmorMultiplierDB();

			else if (armor.equals(Material.DIAMOND_HELMET))
				return FC_Rpg.balanceConfig.getArmorMultiplierDH();

			else if (armor.equals(Material.DIAMOND_LEGGINGS))
				return FC_Rpg.balanceConfig.getArmorMultiplierDL();

			else if (armor.equals(Material.DIAMOND_CHESTPLATE))
				return FC_Rpg.balanceConfig.getArmorMultiplierDC();
		}

		if (constitution > 499)
		{
			if (armor.equals(Material.GOLD_BOOTS))
				return FC_Rpg.balanceConfig.getArmorMultiplierGB();

			else if (armor.equals(Material.GOLD_HELMET))
				return FC_Rpg.balanceConfig.getArmorMultiplierGH();

			else if (armor.equals(Material.GOLD_LEGGINGS))
				return FC_Rpg.balanceConfig.getArmorMultiplierGL();

			else if (armor.equals(Material.GOLD_CHESTPLATE))
				return FC_Rpg.balanceConfig.getArmorMultiplierGC();
		}

		return 0;
	}
	
	// Get enchantment values.
	public double updateDamageByArmorEnchantments(Player player, Enchantment enchant, double damage)
	{
		double bonus = 1;
		PlayerInventory inv = player.getInventory();

		bonus -= getEnchantValue(inv.getHelmet(), enchant);
		bonus -= getEnchantValue(inv.getChestplate(), enchant);
		bonus -= getEnchantValue(inv.getLeggings(), enchant);
		bonus -= getEnchantValue(inv.getBoots(), enchant);

		return damage * bonus;
	}

	public double updateDamageByArmorEnchantments(LivingEntity entity, Enchantment enchant, double damage)
	{
		double bonus = 1;

		bonus -= getEnchantValue(MobEquipment.getHelmet(entity), enchant);
		bonus -= getEnchantValue(MobEquipment.getChestplate(entity), enchant);
		bonus -= getEnchantValue(MobEquipment.getPants(entity), enchant);
		bonus -= getEnchantValue(MobEquipment.getBoots(entity), enchant);

		return damage * bonus;
	}
	
	public double getEnchantValue(ItemStack armorPiece, Enchantment enchant)
	{
		if (armorPiece == null)
			return 0;
		
		double baseProtectionPercentPerPoint = 0.0025;
		
		if (armorPiece.containsEnchantment(enchant))
			return baseProtectionPercentPerPoint * armorPiece.getEnchantmentLevel(enchant);
		
		return 0;
	}
	
	// Get offensive enchant values
	public double getEnchantmentOffensiveBonuses(Player player, Enchantment enchant)
	{
		PlayerInventory inv = player.getInventory();
		ItemStack handItem = inv.getItemInHand();
		double bonus = 1;
		
		if (handItem != null)
		{
			if (FC_Rpg.mLib.swords.contains(handItem.getType()))
			{
				if (handItem.containsEnchantment(enchant))
					bonus += (0.005 * handItem.getEnchantmentLevel(enchant));
			}
		}
		
		return bonus;
	}
	
	public double getEnchantmentOffensiveBonuses(LivingEntity entity, Enchantment enchant)
	{
		ItemStack handItem = MobEquipment.getWeapon(entity);
		double bonus = 1;
		
		if (handItem != null)
		{
			if (FC_Rpg.mLib.swords.contains(handItem.getType()))
			{
				if (handItem.containsEnchantment(enchant))
					bonus += (0.005 * handItem.getEnchantmentLevel(enchant));
			}
		}
		
		return bonus;
	}
	
}
