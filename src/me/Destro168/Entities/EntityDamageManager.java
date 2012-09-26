package me.Destro168.Entities;

import java.util.Date;
import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.RpgParty;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Util.HealthConverter;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityDamageManager 
{
	public EntityDamageManager() { }
	
	public void attackPlayerDefender(RpgPlayer rpgDefender, RpgPlayer rpgAttacker, RpgMonster rpgMobAttacker, double damage)
	{
		Player playerDefender = rpgDefender.getPlayer();
		RpgParty party = null;
		HealthConverter hc = null;
		int memberCount = 0;
		
		//Get party and member count.
		if (FC_Rpg.partyManager.getPartyByMember(playerDefender.getName()) != null)
		{
			party = FC_Rpg.partyManager.getPartyByMember(playerDefender.getName());
			memberCount = party.getMemberCount();
		}
		else
			memberCount = 1;
		
		//Check to see if the player was recently attacked to make sure he isn't attacked too much.
		if (canAttack(rpgDefender.getLastDamagedLong(), memberCount) == false)
			return;
		
		//Check if the player has dodge status on them.
		if (rpgDefender.getStatusIsActive(rpgDefender.getStatusDodge()))
		{
			Random rand = new Random();
			
			if (rand.nextInt(100) <= rpgDefender.getStatusDodgeStrength())
			{
				rpgDefender.attemptDamageAvoidNotification(false);
				return;
			}
		}
		
		//Deal thorns damage before anything is calculated.
		if (rpgDefender.getStatusIsActive(rpgDefender.getStatusImmortal()))
		{
			rpgDefender.attemptDamageAvoidNotification(true);
			return;
		}
		
		//Deal thorns damage before anything is calculated.
		if (rpgDefender.getStatusIsActive(rpgDefender.getStatusThorns()))
		{
			if (rpgAttacker != null)
				rpgAttacker.dealDamage(damage);
			else if (rpgMobAttacker != null)
				rpgMobAttacker.dealDamage(damage);
		}
		
		//Add in enchantment bonuses.
		damage = rpgDefender.calculateBonusEnchantmentDefense(playerDefender, Enchantment.PROTECTION_ENVIRONMENTAL, damage);
		
		//Calculate damage based on players defense.
		damage = damage * getArmorBonus(rpgDefender);
		
		//Handle block.
		if (playerDefender.isBlocking() == true)
		{
			if (rpgDefender.getCombatClass() == FC_Rpg.c_int_defender)
				damage = damage * .75; //25% damage reduction for blocking as defender.
			else
				damage = damage * .9; //10% damage reduction for blocking.
		}
		
		//If taunt status is active, then...
		if (rpgDefender.getStatusIsActive(rpgDefender.getStatusTaunt()) == true)
			damage = damage * .9;	//10% damage reduction from taunt.
		
		//Deal damage greater than 0.
		if (damage < 0.1)
			damage = 0.1;
		
		//Deal the damage to the player
		rpgDefender.dealDamage(damage);
		
		//Perform damage effect.
		playerDefender.playEffect(EntityEffect.HURT);
		
		if (rpgAttacker != null)
		{
			handleBerserkerSpells(rpgAttacker, playerDefender, damage);	//Handle berserker class spells
			rpgAttacker.attemptAttackNotification(playerDefender.getLevel(), rpgDefender.getCurHealth(), damage); 
		}
		
		//Set the players health based current health out of maximum health.
		hc = new HealthConverter(rpgDefender.getMaxHealth(), rpgDefender.getCurHealth());
		
		playerDefender.setHealth(hc.getMinecraftHearts());
		
		//If the players has 0 health, kill minecraft body and set to dead.
		if (rpgDefender.getCurHealth() < 1)
		{
			//Damage the player defender.
			playerDefender.damage(10000);
			
			//Set the player to alive.
			rpgDefender.setIsAlive(false);
		}
		else
		{
			rpgDefender.attemptDefenseNotification(damage);
			
			if (rpgAttacker != null)
				handlePlayerDefensePassives(damage, rpgDefender, rpgAttacker.getPlayer());
			else if (rpgMobAttacker != null)
				handlePlayerDefensePassives(damage, rpgDefender, rpgMobAttacker.getEntity());
		}
	}
	
	public void attackMobDefender(RpgMonster rpgMobDefender, RpgPlayer rpgAttacker, double damage)
	{
		//Variable Declarations.
		Player playerAttacker = rpgAttacker.getPlayer();
		LivingEntity entityDefender = rpgMobDefender.getEntity();
		Player playerLooter = null;
		RpgParty party = null;
		double loot = 0;
		double exp = 0;
		double levelDifference = 0;
		double bonusPercent = 0;
		int level1 = 0;
		int level2 = 0;
		boolean playerLevelHigher = false;
		boolean disableDrops = false;
		
		//Make sure that the mob wasn't attacked too recently.
		if (FC_Rpg.partyManager.getPartyByMember(playerAttacker.getName()) != null)
		{
			party = FC_Rpg.partyManager.getPartyByMember(playerAttacker.getName());
			
			if (canAttack(rpgMobDefender.getLastDamagedLong(),party.getPartySize()) == false)
				return;
		}
		
		//If the player has the fire arrow status, then...
		if (rpgAttacker.getStatusIsActive(rpgAttacker.getStatusFireArrow()))
		{
			//If the mob is alive...
			if (rpgMobDefender.getIsAlive() == true)
			{
				//Put mob on fire
				((LivingEntity) entityDefender).setFireTicks(20);	 //set the mob on fire!
				
				//Then return
				return;
			}
		}
		
		//If the player has the fire arrow status, then...
		if (rpgAttacker.getStatusIsActive(rpgAttacker.getStatusMorale()))
		{
			//If they have the morale buff, then increase damage by it.
			damage = damage * rpgAttacker.getStatusMoraleStrength();
		}
		
		damage = damage * rpgAttacker.getEnchantmentOffensiveBonuses(Enchantment.DAMAGE_ALL);
	
		if (entityDefender.getType() == EntityType.SPIDER || entityDefender.getType() == EntityType.CAVE_SPIDER)
			damage = damage * rpgAttacker.getEnchantmentOffensiveBonuses(Enchantment.DAMAGE_ARTHROPODS);
		else if (entityDefender.getType() == EntityType.ZOMBIE || entityDefender.getType() == EntityType.PIG_ZOMBIE || entityDefender.getType() == EntityType.ZOMBIE || entityDefender.getType() == EntityType.SKELETON)
			damage = damage * rpgAttacker.getEnchantmentOffensiveBonuses(Enchantment.DAMAGE_UNDEAD);
		
		//Handle the passives for attacking players.
		damage = handlePlayerOffensePassives(damage, rpgAttacker, playerAttacker);
		
		//Damage must always be 1 or 0. If it is negative it will heal the creature (not good).
		if (damage < 0.1) damage = 0.1;
		
		//Find the attacker and deal damage from the attacker to the defender.
		rpgMobDefender.dealDamage(damage);

		//Perform damage effect.
		entityDefender.playEffect(EntityEffect.HURT);
		
		//Attempt to send an attack notification.
		rpgAttacker.attemptAttackNotification(rpgMobDefender.getLevel(), rpgMobDefender.getCurHealth(), damage);
		
		//Handle berserker class spells
		handleBerserkerSpells(rpgAttacker, entityDefender, damage);
		
		//If the mob has 0 health handle it's death processes AND drop loot.
		if (rpgMobDefender.getCurHealth() <= 0)
		{
			if (rpgMobDefender.getIsAlive() == false)
				return;
			
			rpgMobDefender.setIsAlive(false);
			
			if (rpgMobDefender.getMobAggressionCheck().isHostile() == true)
			{
				//Give the attacker a mob kill
				rpgAttacker.setLifetimeMobKills(rpgAttacker.getLifetimeMobKills() + 1);
				
				//5 mob defender level, 1 player defender, that's 5 - 1 = 4, that's 4 levels up, gives (20 * 4)
				//Level 1 mob and level 5 player, that's 1 - 5, which is -4, give -20 * 4
				if (rpgMobDefender.getLevel() > rpgAttacker.getClassLevel())
				{
					level1 = rpgMobDefender.getLevel();
					level2 = rpgAttacker.getClassLevel();
					playerLevelHigher = false;
				}
				else
				{
					level1 = rpgAttacker.getClassLevel();
					level2 = rpgMobDefender.getLevel();
					playerLevelHigher = true;
				}
				
				levelDifference = level1 - level2;
				
				//Give a bonus percent based on level difference, 1 level = 20% more.
				if (levelDifference == 0)
					bonusPercent = 1;
				else if (levelDifference >= 6 || levelDifference <= -6)
				{
					bonusPercent = 0;
					
					if (playerAttacker != null)
					{
						MessageLib msgLib = new MessageLib(playerAttacker);
						msgLib.standardMessage("WARNING! This monster is not in your level range so you get no experience or loot from killing it.");
					}
					
					//Disable item drops.
					disableDrops = true;
				}
				
				else if (playerLevelHigher == true)
					bonusPercent = 1 - levelDifference * .2;
				else
					bonusPercent = 1 + levelDifference * .2;
				
				//Set up loot amounts.
				loot = rpgMobDefender.getLevel() * bonusPercent * .8;
				exp = rpgMobDefender.getLevel() * bonusPercent;
				
				//Add global modifiers
				loot = loot * FC_Rpg.lootMultiplier;
				exp = exp * FC_Rpg.expMultiplier;
				
				//Calculate how much loot and experience to aquire by donator
				if (rpgAttacker.isDonator())
				{
					loot = loot * FC_Rpg.DONATOR_LOOT_BONUS;
					exp = exp * FC_Rpg.DONATOR_LOOT_BONUS;
				}
				
				//If the player is in a party, then...
				if (party != null)
				{
					//Add a mob kill for that party once.
					party.addMobKill();
					
					//Drop loot
					for (int j = 0; j < party.getMemberCount(); j++)
					{
						playerLooter = Bukkit.getServer().getPlayer(party.getPartyMember(j));
						
						//If the mob is hostile then give money and experience.
						if (party.closeToPartyLeader(j) == true)
						{
							FC_Rpg.economy.depositPlayer(playerLooter.getName(), party.getPartyBonus(loot));
							FC_Rpg.rpgManager.getRpgPlayer(playerLooter).addClassExperience(party.getPartyBonus(exp), true);
						}
					}
				}
				
				//Else if not in a party, give the drops to the single player.
				else
				{
					FC_Rpg.economy.depositPlayer(playerAttacker.getName(), loot);
					rpgAttacker.addClassExperience(exp, true);
				}
				
				//Send a message to the player showing experience and loot gains.
				rpgAttacker.sendMonsterDeathNotification(rpgMobDefender.getLevel(), exp, loot);
				
				//Don't drop loot if the monster is too high level.
				if (disableDrops == false)
					rpgMobDefender.handleHostileMobDrops(entityDefender.getLocation());	//Drop items for hostile mobs.
			}
			else
			{
				//Drop items for non-hostile mobs.
				rpgMobDefender.handlePassiveMobDrops(entityDefender.getLocation());
			}
			
			//Remove the mob
			removeMob(entityDefender);
		}
	}
	
	private double calcArmorModifier(Material armor, int constitution)
	{
		//LEATHER - 10% total, .5%, 1%, 1.5%, 2%
		if (armor.equals(Material.LEATHER_BOOTS))
			return .005;

		else if (armor.equals(Material.LEATHER_HELMET))
			return .01;

		else if (armor.equals(Material.LEATHER_LEGGINGS))
			return .015;

		else if (armor.equals(Material.LEATHER_CHESTPLATE))
			return .02;
		
		if (constitution > 124)
		{
			//LEATHER - 10% total, 1%, 2%, 3%, 4%
			if (armor.equals(Material.CHAINMAIL_BOOTS))
				return .01;

			else if (armor.equals(Material.CHAINMAIL_HELMET))
				return .02;

			else if (armor.equals(Material.CHAINMAIL_LEGGINGS))
				return .03;

			else if (armor.equals(Material.CHAINMAIL_CHESTPLATE))
				return .04;
		}
		
		if (constitution > 249)
		{
			//IRON - 15% total, 1.5, 3, 4.5, 6
			if (armor.equals(Material.IRON_BOOTS))
				return .015;
	
			else if (armor.equals(Material.IRON_HELMET))
				return .03;
	
			else if (armor.equals(Material.IRON_LEGGINGS))
				return .045;
	
			else if (armor.equals(Material.IRON_CHESTPLATE))
				return .06;
		}
		
		if (constitution > 374)
		{
			//DIAMOND - 20% total, 2,4,6,8
			if (armor.equals(Material.DIAMOND_BOOTS))
				return .02;
	
			else if (armor.equals(Material.DIAMOND_HELMET))
				return .04;
	
			else if (armor.equals(Material.DIAMOND_LEGGINGS))
				return .06;
	
			else if (armor.equals(Material.DIAMOND_CHESTPLATE))
				return .08;
		}
		
		
		if (constitution > 499)
		{
			//Gold - 25% total, 2.5, 5, 7.5, 10
			if (armor.equals(Material.GOLD_BOOTS))
				return .025;
	
			else if (armor.equals(Material.GOLD_HELMET))
				return .05;
	
			else if (armor.equals(Material.GOLD_LEGGINGS))
				return .075;
	
			else if (armor.equals(Material.GOLD_CHESTPLATE))
				return .1;
		}
		
		return 0;
	}
	
	private double getArmorBonus(RpgPlayer rpgDefender)
	{
		Player playerDefender = rpgDefender.getPlayer();
		double armorBonus = 1;
		int constitution = rpgDefender.getConstitution();
		
		if (playerDefender.getInventory().getHelmet() != null)
			armorBonus = armorBonus + calcArmorModifier(playerDefender.getInventory().getHelmet().getType(), constitution);

		if (playerDefender.getInventory().getLeggings() != null)
			armorBonus = armorBonus + calcArmorModifier(playerDefender.getInventory().getLeggings().getType(), constitution);

		if (playerDefender.getInventory().getChestplate() != null)
			armorBonus = armorBonus + calcArmorModifier(playerDefender.getInventory().getChestplate().getType(), constitution);

		if (playerDefender.getInventory().getBoots() != null)
			armorBonus = armorBonus + calcArmorModifier(playerDefender.getInventory().getBoots().getType(), constitution);
		
		return armorBonus;
	}
	
	//Handle player defense skills
	private void handlePlayerDefensePassives(double damage, RpgPlayer rpgDefender, LivingEntity entityAttacker)
	{
		//Variable Declarations
		Random rand = new Random();
		
		if (rpgDefender.getCombatClass() == FC_Rpg.c_int_swordsman)
		{
			//10% chance for counter-attack.
			if (rand.nextInt(10) == 0)
				entityAttacker.damage(1, rpgDefender.getPlayer());
		}
	}
	
	//Handle player offense skills.
	private double handlePlayerOffensePassives(double damage, RpgPlayer rpgAttacker, LivingEntity entityDefender)
	{
		if (rpgAttacker.getCombatClass() == FC_Rpg.c_int_berserker)
			damage = damage * (1 + rpgAttacker.getMissingHealthDecimal() * .4); //Scale damage by 1/4th
		
		return damage;
	}
	
	private void handleBerserkerSpells(RpgPlayer caster, LivingEntity defender, double damage)
	{
		if (caster.getCombatClass() != FC_Rpg.c_int_berserker)
			return;
		
		if (caster.getStatusIsActive(caster.getStatusBloodthirst()))
			caster.heal(damage * caster.getStatusBloodthirstStrength());
		
		if (caster.getStatusIsActive(caster.getStatusFerocity()))
		{
			EntityLocationLib ell = new EntityLocationLib();
			caster.getPlayer().teleport(ell.getLocationBehindEntity(defender.getLocation()));
		}
	}
	
	public boolean canAttack(long time, int partySize)
	{
		Date now = new Date();
		
		//Check to see if entity was damage in last .25 seconds.
		if ((now.getTime() - time) < (125 / partySize))
			return false;
		else
			return true;
	}
	
	public void nukeMob(LivingEntity entity)
	{
		//Get The Entity.
		RpgMonster monster = FC_Rpg.rpgManager.getRpgMonster(entity);
		
		if (monster == null)
			entity.remove();
		else
			removeMob(entity);
	}
	
	public void removeMob(LivingEntity entity)
	{
		//Unregister a mob.
		FC_Rpg.rpgManager.unregisterRpgMonster(entity);
		
		//Remove the mob.
		entity.damage(99999);
	}
}
