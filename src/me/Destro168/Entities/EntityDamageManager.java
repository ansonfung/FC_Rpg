package me.Destro168.Entities;

import java.util.Date;
import java.util.Random;

import me.Destro168.Configs.PassiveConfig;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.LoadedObjects.RpgClass;
import me.Destro168.LoadedObjects.Guild;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Spells.EffectIDs;
import me.Destro168.Util.HealthConverter;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageManager 
{
	public EntityDamageManager() { }
	
	public void attackPlayerDefender(RpgPlayer rpgDefender, RpgPlayer rpgAttacker, RpgMonster rpgMobAttacker, double damage)
	{
		Player playerDefender = rpgDefender.getPlayer();
		Guild party = null;
		HealthConverter hc = null;
		int memberCount = 0;
		
		//Get party and member count.
		if (FC_Rpg.guildManager.getGuildByMember(playerDefender.getName()) != null)
		{
			party = FC_Rpg.guildManager.getGuildByMember(playerDefender.getName());
			memberCount = party.getOnlineGuildPlayerList().size();
		}
		else
			memberCount = 1;
		
		//Check to see if the player was recently attacked to make sure he isn't attacked too much.
		if (canAttack(rpgDefender.getLastDamagedLong(), memberCount) == false)
			return;
		
		//Handle immortality effect first.
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.IMMORTAL))
		{
			rpgDefender.attemptDamageAvoidNotification(true);
			return;
		}
		
		//Check if the player has dodge status on them.
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.DODGE))
		{
			Random rand = new Random();
			
			if (rand.nextInt(100) < rpgDefender.getPlayerConfig().getStatusMagnitude(EffectIDs.DODGE))
			{
				rpgDefender.attemptDamageAvoidNotification(false);
				return;
			}
		}
		
		//Set the last damage cause for players.
		EntityDamageByEntityEvent edbe;
		
		if (rpgAttacker != null)
		{
			edbe = new EntityDamageByEntityEvent(rpgAttacker.getPlayer(), playerDefender, DamageCause.ENTITY_ATTACK, 0);
			playerDefender.setLastDamageCause(edbe);
		}
		else if (rpgMobAttacker != null)
		{
			edbe = new EntityDamageByEntityEvent(rpgMobAttacker.getEntity(), rpgDefender.getPlayer(), DamageCause.ENTITY_ATTACK, 0);
			playerDefender.setLastDamageCause(edbe);
		}
		
		//Deal thorns damage before anything is calculated.
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.THORNS))
		{
			double thornsDamage = damage * rpgDefender.getPlayerConfig().getStatusMagnitude(EffectIDs.THORNS);
			
			//Attempt a dodge notification
			rpgDefender.attemptThornsNotification(thornsDamage);
			
			if (rpgAttacker != null)
				rpgAttacker.dealDamage(thornsDamage);
			else if (rpgMobAttacker != null)
				rpgMobAttacker.dealDamage(thornsDamage);
		}
		
		//Add in enchantment bonuses.
		damage = rpgDefender.calculateBonusEnchantmentDefense(playerDefender, Enchantment.PROTECTION_ENVIRONMENTAL, damage);
		
		//Calculate damage based on players defense.
		damage = damage * getArmorBonus(rpgDefender);
		
		//Handle block.
		if (playerDefender.isBlocking() == true)
		{
			//Variable Declaration.
			RpgClass rpgClass = FC_Rpg.classConfig.getClassWithPassive(PassiveConfig.passive_StrongerParry);
			
			if (rpgClass != null)
			{
				if (rpgClass.getID() == rpgDefender.getPlayerConfig().getCombatClass())
					damage = damage * FC_Rpg.passiveConfig.getStrongerParry(); //25% damage reduction for blocking as defender.
				else
					damage = damage * .9; //10% damage reduction for blocking.
			}
			else
				damage = damage * .9; //10% damage reduction for blocking.
		}
		
		//If taunt status is active, then...
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.TAUNT) == true)
		{
			//Apply taunt damage reduction.
			damage = damage * rpgDefender.getPlayerConfig().getStatusMagnitude(EffectIDs.TAUNT);
		}
		
		//Deal damage greater than 0.
		if (damage < 0.1)
			damage = 0.1;
		
		//Deal the damage to the player
		rpgDefender.dealDamage(damage);
		
		//Perform damage effect.
		playerDefender.playEffect(EntityEffect.HURT);
		
		if (rpgAttacker != null)
		{
			handle_Postoffense_Buffs(rpgAttacker, playerDefender, damage);
			rpgAttacker.attemptAttackNotification(playerDefender.getLevel(), rpgDefender.getCurHealth(), damage);
			
			if (rpgAttacker.getPlayer().getItemInHand().containsEnchantment(Enchantment.KNOCKBACK))
				applyKnockback(rpgAttacker.getPlayer(), playerDefender);
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
				handle_Defense_Passives(damage, rpgDefender, rpgAttacker.getPlayer());
			else if (rpgMobAttacker != null)
				handle_Defense_Passives(damage, rpgDefender, rpgMobAttacker.getEntity());
		}
	}
	
	private void applyKnockback(Player attacker, LivingEntity attacked)
	{
		float knockback = attacker.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);
		
	    attacked.setVelocity(attacked.getVelocity().add(attacked.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockback)));
	}
	
	public void attackMobDefender(RpgMonster rpgMobDefender, RpgPlayer rpgAttacker, double damage)
	{
		//Variable Declarations.
		Player playerAttacker = rpgAttacker.getPlayer();
		LivingEntity entityDefender = rpgMobDefender.getEntity();
		Player playerLooter = null;
		Guild party = null;
		double cash = 0;
		double exp = 0;
		double levelDifference = 0;
		double bonusPercent = 0;
		int level1 = 0;
		int level2 = 0;
		int fireUses = rpgAttacker.getPlayerConfig().getStatusUses(EffectIDs.FIRE_ARROW);
		int partyMemberCount = 1;
		boolean playerLevelHigher = false;
		boolean disableDrops = false;
		
		//Make sure that the mob wasn't attacked too recently.
		if (FC_Rpg.guildManager.getGuildByMember(playerAttacker.getName()) != null)
		{
			party = FC_Rpg.guildManager.getGuildByMember(playerAttacker.getName());
			
			partyMemberCount = party.getOnlineGuildPlayerList().size();
			
			if (canAttack(rpgMobDefender.getLastDamagedLong(), partyMemberCount) == false)
				return;
		}
		
		//If the player has the fire arrow status, then...
		if (fireUses > 0)
		{
			//If the mob is alive...
			if (rpgMobDefender.getIsAlive() == true)
			{
				//Put mob on fire
				((LivingEntity) entityDefender).setFireTicks(20);	 //set the mob on fire!
				
				//Remove a fire arrow use.
				rpgAttacker.getPlayerConfig().setStatusUses(EffectIDs.FIRE_ARROW, fireUses - 1);
				
				//Then return
				return;
			}
		}
		
		//If the player has the attack buff, then...
		if (rpgAttacker.getStatusActiveRpgPlayer(EffectIDs.ATTACK))
		{
			//Increase attack damage by its magnitude.
			damage = damage * rpgAttacker.getPlayerConfig().getStatusMagnitude(EffectIDs.ATTACK);
		}
		
		damage = damage * rpgAttacker.getEnchantmentOffensiveBonuses(Enchantment.DAMAGE_ALL);
		
		if (entityDefender.getType() == EntityType.SPIDER || entityDefender.getType() == EntityType.CAVE_SPIDER)
			damage = damage * rpgAttacker.getEnchantmentOffensiveBonuses(Enchantment.DAMAGE_ARTHROPODS);
		else if (entityDefender.getType() == EntityType.ZOMBIE || entityDefender.getType() == EntityType.PIG_ZOMBIE || entityDefender.getType() == EntityType.ZOMBIE || entityDefender.getType() == EntityType.SKELETON)
			damage = damage * rpgAttacker.getEnchantmentOffensiveBonuses(Enchantment.DAMAGE_UNDEAD);
		
		//Handle the passives for attacking players.
		damage = handle_Offense_Passives(damage, rpgAttacker, playerAttacker);
		
		//Damage must always be 1 or 0. If it is negative it will heal the creature (not good).
		if (damage < 0.1) damage = 0.1;
		
		//Find the attacker and deal damage from the attacker to the defender.
		rpgMobDefender.dealDamage(damage);

		//Perform damage effect.
		entityDefender.playEffect(EntityEffect.HURT);
		
		//Attempt to send an attack notification.
		rpgAttacker.attemptAttackNotification(rpgMobDefender.getLevel(), rpgMobDefender.getCurHealth(), damage);
		
		//Handle berserker class spells
		handle_Postoffense_Buffs(rpgAttacker, entityDefender, damage);
		
		if (playerAttacker != null)
		{
			if (playerAttacker.getItemInHand().containsEnchantment(Enchantment.KNOCKBACK))
				applyKnockback(playerAttacker, entityDefender);
		}
		
		//If the mob has 0 health handle it's death processes AND drop loot.
		if (rpgMobDefender.getCurHealth() <= 0)
		{
			if (rpgMobDefender.getIsAlive() == false)
				return;
			
			rpgMobDefender.setIsAlive(false);
			
			if (rpgMobDefender.getMobAggressionCheck().isHostile() == true)
			{
				//Give the attacker a mob kill
				rpgAttacker.getPlayerConfig().setLifetimeMobKills(rpgAttacker.getPlayerConfig().getLifetimeMobKills() + 1);
				
				//5 mob defender level, 1 player defender, that's 5 - 1 = 4, that's 4 levels up, gives (20 * 4)
				//Level 1 mob and level 5 player, that's 1 - 5, which is -4, give -20 * 4
				if (rpgMobDefender.getLevel() > rpgAttacker.getPlayerConfig().getClassLevel())
				{
					level1 = rpgMobDefender.getLevel();
					level2 = rpgAttacker.getPlayerConfig().getClassLevel();
					playerLevelHigher = false;
				}
				else
				{
					level1 = rpgAttacker.getPlayerConfig().getClassLevel();
					level2 = rpgMobDefender.getLevel();
					playerLevelHigher = true;
				}
				
				levelDifference = level1 - level2;
				int powerLevelPrevention = FC_Rpg.generalConfig.getPowerLevelPrevention();
				
				if (powerLevelPrevention > -1)
				{
					if (levelDifference > powerLevelPrevention || levelDifference < powerLevelPrevention * -1)
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
				}
				
				if (disableDrops == false)
				{
					//Give a bonus percent based on level difference, 1 level = x% more.
					if (levelDifference == 0)
						bonusPercent = 1;
					else if (playerLevelHigher == true)
						bonusPercent = 1 - levelDifference * FC_Rpg.generalConfig.getMobLevelLootmodifier();
					else
						bonusPercent = 1 + levelDifference * FC_Rpg.generalConfig.getMobLevelLootmodifier();
					
					//Set up loot amounts.
					cash = rpgMobDefender.getLevel() * bonusPercent * FC_Rpg.generalConfig.getMobCashMultiplier();
					exp = rpgMobDefender.getLevel() * bonusPercent * FC_Rpg.generalConfig.getMobExpMultiplier();
					
					//Add global modifiers
					cash = cash * FC_Rpg.eventCashMultiplier;
					exp = exp * FC_Rpg.eventExpMultiplier;
					
					//Calculate how much loot and experience to aquire by donator
					if (rpgAttacker.getPlayerConfig().isDonator())
					{
						cash = cash * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
						exp = cash * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
					}
				}
				
				//If the player is in a party, then...
				if (party != null)
				{
					//Add a mob kill for that party.
					FC_Rpg.guildManager.addMobKill(party.getName());
					
					//Give loot
					for (String pName : party.getMembers())
					{
						playerLooter = Bukkit.getServer().getPlayer(pName);
						
						if (playerLooter != null)
						{
							FC_Rpg.economy.depositPlayer(playerLooter.getName(), party.getGuildBonus(cash));
							FC_Rpg.rpgManager.getRpgPlayer(playerLooter).addClassExperience(party.getGuildBonus(exp), true);
						}
					}
				}
				
				//Else if not in a party, give loot to the single player.
				else
				{
					FC_Rpg.economy.depositPlayer(playerAttacker.getName(), cash);
					rpgAttacker.addClassExperience(exp, true);
				}
				
				//Send a message to the player showing experience and loot gains.
				rpgAttacker.sendMonsterDeathNotification(rpgMobDefender.getLevel(), exp, cash);
				
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
		int constitution = rpgDefender.getPlayerConfig().getConstitution();
		
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
	private void handle_Defense_Passives(double damage, RpgPlayer rpgDefender, LivingEntity entityAttacker)
	{
		//Variable Declarations
		Random rand = new Random();
		RpgClass rpgClass = FC_Rpg.classConfig.getClassWithPassive(PassiveConfig.passive_CounterAttack);
		
		if (rpgClass != null)
		{
			if (rpgClass.getName().equals(rpgDefender.getPlayerConfig().getCombatClass()))
			{
				//Handle counter-attack chance.
				if (rand.nextInt(FC_Rpg.passiveConfig.getCounterAttack()) == 0)
					entityAttacker.damage(1, rpgDefender.getPlayer());
			}
		}
	}
	
	private double handle_Offense_Passives(double damage, RpgPlayer rpgAttacker, LivingEntity entityDefender)
	{
		//Variable Declaration.
		RpgClass rpgClass = FC_Rpg.classConfig.getClassWithPassive(PassiveConfig.passive_BattleLust);
		
		if (rpgClass != null)
		{
			if (rpgClass.getName().equals(rpgAttacker.getPlayerConfig().getCombatClass()))
			{
				//Scale damage by 1/4th
				damage = damage * (1 + rpgAttacker.getMissingHealthDecimal() * FC_Rpg.passiveConfig.getBattleLust());
			}
		}
		
		return damage;
	}
	
	private void handle_Postoffense_Buffs(RpgPlayer caster, LivingEntity defender, double damage)
	{
		if (caster.getStatusActiveRpgPlayer(EffectIDs.LIFESTEAL))
		{
			double healAmount = damage * caster.getPlayerConfig().getStatusMagnitude(EffectIDs.LIFESTEAL);
			
			caster.attemptHealSelfNotification(healAmount);
			caster.heal(healAmount);
		}
		
		if (caster.getStatusActiveRpgPlayer(EffectIDs.TELEPORT_STRIKE))
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
