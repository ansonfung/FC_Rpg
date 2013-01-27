package me.Destro168.FC_Rpg.Util;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Entities.RpgMonster;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BattleCalculations
{
	public BattleCalculations() { }
	
	//Get weapon bonuses.
	public double getWeaponModifier(ItemStack weapon, int strength)
	{
		Material weaponType = weapon.getType();
		double swordBonus = 0;
		
		// Get sword bonus based on sword type.
		if (weaponType.equals(Material.WOOD_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementWood())
				swordBonus = FC_Rpg.balanceConfig.getSwordMultiplierWood();
		}
		else if (weaponType.equals(Material.STONE_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementStone())
				swordBonus = FC_Rpg.balanceConfig.getSwordMultiplierStone();
		}
		else if (weaponType.equals(Material.IRON_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementIron())
				swordBonus = FC_Rpg.balanceConfig.getSwordMultiplierIron();
		}
		else if (weaponType.equals(Material.DIAMOND_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementDiamond())
				swordBonus = FC_Rpg.balanceConfig.getSwordMultiplierDiamond();
		}
		else if (weaponType.equals(Material.GOLD_SWORD))
		{
			if (strength >= FC_Rpg.balanceConfig.getSwordAttackRequirementGold())
				swordBonus = FC_Rpg.balanceConfig.getSwordMultiplierGold();
		}
		
		// Increase sword bonus basedo n item meta
		if (swordBonus > 0)
		{
			if (weapon.getItemMeta().getLore() != null && weapon.getItemMeta().getLore().size() >= 2)
			{
				ItemMeta iMeta = weapon.getItemMeta();
				String tier = iMeta.getLore().get(1);
				
				if (tier.toLowerCase().contains("common"))
					swordBonus *= .8;
				else if (tier.toLowerCase().contains("rare"))
					swordBonus *= 1.1;
				else if (tier.toLowerCase().contains("unique"))
					swordBonus *= 1.2;
				else if (tier.toLowerCase().contains("mythical"))
					swordBonus *= 1.3;
				else if (tier.toLowerCase().contains("legendary"))
					swordBonus *= 1.5;
			}
			
			return 1 + swordBonus;
		}
		else
			return 1;
	}
	
	// Get armor bonuses.
	public double getArmorBonus(RpgPlayer rpgPlayer)
	{
		Player player = rpgPlayer.getPlayer();
		double armorBonus = 1;
		int constitution = rpgPlayer.playerConfig.getConstitution();
		
		armorBonus -= getArmorPieceBonus(player.getInventory().getHelmet(), constitution);
		armorBonus -= getArmorPieceBonus(player.getInventory().getLeggings(), constitution);
		armorBonus -= getArmorPieceBonus(player.getInventory().getChestplate(), constitution);
		armorBonus -= getArmorPieceBonus(player.getInventory().getBoots(), constitution);
		
		return armorBonus;
	}

	public double getArmorBonus(RpgMonster rpgMonster)
	{
		EntityEquipment entityEquipment = rpgMonster.getEntity().getEquipment();
		double armorBonus = 1;
		int constitution = rpgMonster.getConstitution();
		
		armorBonus -= getArmorPieceBonus(entityEquipment.getHelmet(), constitution);
		armorBonus -= getArmorPieceBonus(entityEquipment.getLeggings(), constitution);
		armorBonus -= getArmorPieceBonus(entityEquipment.getChestplate(), constitution);
		armorBonus -= getArmorPieceBonus(entityEquipment.getBoots(), constitution);
		
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
		
		if (constitution >= FC_Rpg.balanceConfig.getArmorWearRequirementChain())
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

		if (constitution >= FC_Rpg.balanceConfig.getArmorWearRequirementIron())
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
		
		if (constitution >= FC_Rpg.balanceConfig.getArmorWearRequirementDiamond())
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
		
		if (constitution >= FC_Rpg.balanceConfig.getArmorWearRequirementGold())
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
	public double getArmorEnchantmentBonus(LivingEntity entity, Enchantment enchant)
	{
		EntityEquipment entityEquipment = entity.getEquipment();
		double bonus = 1;
		
		bonus -= getEnchantValue(entityEquipment.getHelmet(), enchant);
		bonus -= getEnchantValue(entityEquipment.getChestplate(), enchant);
		bonus -= getEnchantValue(entityEquipment.getLeggings(), enchant);
		bonus -= getEnchantValue(entityEquipment.getBoots(), enchant);
		
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
	
	public double getEntityEnchantmentBonus(LivingEntity entity, Enchantment enchant)
	{
		ItemStack checkItem = entity.getEquipment().getItemInHand();
		
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
