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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
	public double getArmorBonus(RpgPlayer rpgPlayer)
	{
		Player player = rpgPlayer.getPlayer();
		double armorBonus = 1;
		int constitution = rpgPlayer.getPlayerConfig().getConstitution();

		armorBonus -= getArmorPieceBonus(player.getInventory().getHelmet(), constitution);
		armorBonus -= getArmorPieceBonus(player.getInventory().getLeggings(), constitution);
		armorBonus -= getArmorPieceBonus(player.getInventory().getChestplate(), constitution);
		armorBonus -= getArmorPieceBonus(player.getInventory().getBoots(), constitution);

		return armorBonus;
	}

	public double getArmorBonus(RpgMonster rpgMonster)
	{
		LivingEntity entity = rpgMonster.getEntity();
		double armorBonus = 1;
		int constitution = rpgMonster.getConstitution();

		armorBonus -= getArmorPieceBonus(MobEquipment.getHelmet(entity), constitution);
		armorBonus -= getArmorPieceBonus(MobEquipment.getPants(entity), constitution);
		armorBonus -= getArmorPieceBonus(MobEquipment.getChestplate(entity), constitution);
		armorBonus -= getArmorPieceBonus(MobEquipment.getBoots(entity), constitution);
		
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
	public double getArmorEnchantmentBonus(Player player, Enchantment enchant)
	{
		double bonus = 1;
		PlayerInventory inv = player.getInventory();
		
		bonus -= getEnchantValue(inv.getHelmet(), enchant);
		bonus -= getEnchantValue(inv.getChestplate(), enchant);
		bonus -= getEnchantValue(inv.getLeggings(), enchant);
		bonus -= getEnchantValue(inv.getBoots(), enchant);
		
		return bonus;
	}

	public double getArmorEnchantmentBonus(LivingEntity entity, Enchantment enchant)
	{
		double bonus = 1;
		
		bonus -= getEnchantValue(MobEquipment.getHelmet(entity), enchant);
		bonus -= getEnchantValue(MobEquipment.getChestplate(entity), enchant);
		bonus -= getEnchantValue(MobEquipment.getPants(entity), enchant);
		bonus -= getEnchantValue(MobEquipment.getBoots(entity), enchant);
		
		return bonus;
	}
	
	private double getEnchantValue(ItemStack armorPiece, Enchantment enchant)
	{
		if (armorPiece == null)
			return 0;
		
		double baseProtectionPercentPerPoint;
		
		if (enchant == Enchantment.PROTECTION_ENVIRONMENTAL)
			baseProtectionPercentPerPoint = FC_Rpg.balanceConfig.getEnchantmentMultiplierProtection();
		else if (enchant == Enchantment.PROTECTION_EXPLOSIONS)
			baseProtectionPercentPerPoint = FC_Rpg.balanceConfig.getEnchantmentMultiplierExplosion();
		else if (enchant == Enchantment.PROTECTION_FIRE)
			baseProtectionPercentPerPoint = FC_Rpg.balanceConfig.getEnchantmentMultiplierFire();
		else if (enchant == Enchantment.PROTECTION_FALL)
			baseProtectionPercentPerPoint = FC_Rpg.balanceConfig.getEnchantmentMultiplierFall();
		else if (enchant == Enchantment.PROTECTION_PROJECTILE)
			baseProtectionPercentPerPoint = FC_Rpg.balanceConfig.getEnchantmentMultiplierProjectile();
		else
			return 0;
		
		if (armorPiece.containsEnchantment(enchant))
			return baseProtectionPercentPerPoint * armorPiece.getEnchantmentLevel(enchant);
		
		return 0;
	}
	
	public double getPlayerEnchantmentBonus(Player player, Enchantment enchant)
	{
		ItemStack checkItem = player.getInventory().getItemInHand();
		
		if (checkItem == null)
			return 1;
		
		if (FC_Rpg.mLib.swords.contains(checkItem.getType()))
		{
			if (enchant == Enchantment.DAMAGE_ALL)
			{
				if (checkItem.containsEnchantment(Enchantment.DAMAGE_ALL))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierSharpness() * checkItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
			}
			else if (enchant == Enchantment.DAMAGE_ARTHROPODS)
			{
				if (checkItem.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierBane() * checkItem.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
			}
			else if (enchant == Enchantment.DAMAGE_UNDEAD)
			{
				if (checkItem.containsEnchantment(Enchantment.DAMAGE_UNDEAD))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierSmite() * checkItem.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
			}
		}
		else if (checkItem.getType() == Material.BOW)
		{
			 if (enchant == Enchantment.ARROW_DAMAGE)
			{
				if (checkItem.containsEnchantment(Enchantment.ARROW_DAMAGE))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierPower() * checkItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
			}
		}
		
		return 1;
	}
	
	public double getEntityEnchantmentBonus(LivingEntity entity, Enchantment enchant)
	{
		ItemStack checkItem = MobEquipment.getWeapon(entity);
		
		if (checkItem == null)
			return 1;
		
		if (FC_Rpg.mLib.swords.contains(checkItem.getType()))
		{
			if (enchant == Enchantment.DAMAGE_ARTHROPODS)
			{
				if (checkItem.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierBane() * checkItem.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
			}
			else if (enchant == Enchantment.DAMAGE_ALL)
			{
				if (checkItem.containsEnchantment(Enchantment.DAMAGE_ALL))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierSharpness() * checkItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
			} 
			else if (enchant == Enchantment.DAMAGE_UNDEAD)
			{
				if (checkItem.containsEnchantment(Enchantment.DAMAGE_UNDEAD))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierSmite() * checkItem.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
			}
		}
		else if (checkItem.getType() == Material.BOW)
		{
			 if (enchant == Enchantment.ARROW_DAMAGE)
			{
				if (checkItem.containsEnchantment(Enchantment.ARROW_DAMAGE))
					return 1 + FC_Rpg.balanceConfig.getEnchantmentMultiplierPower() * checkItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
			}
		}
		
		return 1;
	}
	
	//Get potion values
	public double getPotionOffenseBonus(LivingEntity entity)
	{
		double bonus = 1;
		
		for (PotionEffect pe : entity.getActivePotionEffects())
		{
			if (pe.getType().equals(PotionEffectType.INCREASE_DAMAGE))
				bonus += FC_Rpg.balanceConfig.getPotionMultiplierStrength() + pe.getAmplifier() * FC_Rpg.balanceConfig.getPotionMultiplierStrength();
			if (pe.getType().equals(PotionEffectType.WEAKNESS))
				bonus -= FC_Rpg.balanceConfig.getPotionMultiplierWeakness() + pe.getAmplifier() * FC_Rpg.balanceConfig.getPotionMultiplierWeakness();
		}
		
		return bonus;
	}
	
	public double getPotionDefenseBonus(LivingEntity entity)
	{
		double bonus = 1;
		
		for (PotionEffect pe : entity.getActivePotionEffects())
		{
			if (pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE))
				bonus -= FC_Rpg.balanceConfig.getPotionMultiplierResistance() + pe.getAmplifier() * FC_Rpg.balanceConfig.getPotionMultiplierResistance();
		}
		
		return bonus;
	}
	
}