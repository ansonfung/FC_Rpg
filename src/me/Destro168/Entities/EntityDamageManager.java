package me.Destro168.Entities;

import java.util.Date;
import java.util.Random;

import me.Destro168.Configs.PassiveConfig;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.LoadedObjects.RpgClass;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Spells.EffectIDs;
import me.Destro168.Util.HealthConverter;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class EntityDamageManager 
{
	private final int DROP_FAIL_VALUE = 999999;
	
	public EntityDamageManager() { }
	
	public void attackPlayerDefender(RpgPlayer rpgDefender, RpgPlayer rpgAttacker, RpgMonster rpgMobAttacker, double damage)
	{
		Player playerDefender = rpgDefender.getPlayer();
		String party = "";
		HealthConverter hc = null;
		int memberCount = 0;
		
		//Get party and member count.
		if (FC_Rpg.guildManager.getGuildByMember(playerDefender.getName()) != null)
		{
			party = FC_Rpg.guildManager.getGuildByMember(playerDefender.getName());
			memberCount = FC_Rpg.guildManager.getOnlineGuildPlayerList(party).size();
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
		
		playerDefender.setHealth(hc.getPlayerHearts());
		
		//Check armor durabilities for the player defender.
		increaseArmorDurabilities(playerDefender);
		
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
	
	private void increaseArmorDurabilities(Player p)
	{
		short currentDurability = 0;
		boolean setAir = false;
		ItemStack boots = p.getInventory().getBoots();
		ItemStack chest = p.getInventory().getChestplate();
		ItemStack legs = p.getInventory().getLeggings();
		ItemStack helm = p.getInventory().getHelmet();
		ItemStack air = new ItemStack(Material.AIR);
		
		if (boots != null)
		{
			if (boots.getEnchantments().size() > 0)
			{
				boots.setDurability((short) (boots.getDurability() + 1));
				currentDurability = boots.getDurability();
				
				if (boots.getType() == Material.LEATHER_BOOTS && currentDurability >= Material.LEATHER_BOOTS.getMaxDurability())
					setAir = true;
				else if (boots.getType() == Material.CHAINMAIL_BOOTS && currentDurability >= Material.CHAINMAIL_BOOTS.getMaxDurability())
					setAir = true;
				else if (boots.getType() == Material.IRON_BOOTS && currentDurability >= Material.IRON_BOOTS.getMaxDurability())
					setAir = true;
				else if (boots.getType() == Material.DIAMOND_BOOTS && currentDurability >= Material.DIAMOND_BOOTS.getMaxDurability())
					setAir = true;
				else if (boots.getType() == Material.GOLD_BOOTS && currentDurability >= Material.GOLD_BOOTS.getMaxDurability())
					setAir = true;
				
				if (setAir == true)
					p.getInventory().setBoots(air);
				else
					p.getInventory().setBoots(boots);
			}
		}
		
		if (chest != null)
		{
			if (chest.getEnchantments().size() > 0)
			{
				chest.setDurability((short) (chest.getDurability() + 1));
				currentDurability = chest.getDurability();
				
				if (chest.getType() == Material.LEATHER_CHESTPLATE && currentDurability >= Material.LEATHER_CHESTPLATE.getMaxDurability())
					setAir = true;
				else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE && currentDurability >= Material.CHAINMAIL_CHESTPLATE.getMaxDurability())
					setAir = true;
				else if (chest.getType() == Material.IRON_CHESTPLATE && currentDurability >= Material.IRON_CHESTPLATE.getMaxDurability())
					setAir = true;
				else if (chest.getType() == Material.DIAMOND_CHESTPLATE && currentDurability >= Material.DIAMOND_CHESTPLATE.getMaxDurability())
					setAir = true;
				else if (chest.getType() == Material.GOLD_CHESTPLATE && currentDurability >= Material.GOLD_CHESTPLATE.getMaxDurability())
					setAir = true;
				
				if (setAir == true)
					p.getInventory().setChestplate(air);
				else
					p.getInventory().setChestplate(chest);
			}
		}
		
		if (legs != null)
		{
			if (legs.getEnchantments().size() > 0)
			{
				legs.setDurability((short) (legs.getDurability() + 1));
				currentDurability = legs.getDurability();
				
				if (legs.getType() == Material.LEATHER_LEGGINGS && currentDurability >= Material.LEATHER_LEGGINGS.getMaxDurability())
					setAir = true;
				else if (legs.getType() == Material.CHAINMAIL_LEGGINGS && currentDurability >= Material.CHAINMAIL_LEGGINGS.getMaxDurability())
					setAir = true;
				else if (legs.getType() == Material.IRON_LEGGINGS && currentDurability >= Material.IRON_LEGGINGS.getMaxDurability())
					setAir = true;
				else if (legs.getType() == Material.DIAMOND_LEGGINGS && currentDurability >= Material.DIAMOND_LEGGINGS.getMaxDurability())
					setAir = true;
				else if (legs.getType() == Material.GOLD_LEGGINGS && currentDurability >= Material.GOLD_LEGGINGS.getMaxDurability())
					setAir = true;
				
				if (setAir == true)
					p.getInventory().setLeggings(air);
				else
					p.getInventory().setLeggings(legs);
			}
		}
		
		if (helm != null)
		{
			if (helm.getEnchantments().size() > 0)
			{
				helm.setDurability((short) (helm.getDurability() + 1));
				currentDurability = helm.getDurability();
				
				if (helm.getType() == Material.LEATHER_HELMET && currentDurability >= Material.LEATHER_HELMET.getMaxDurability())
					setAir = true;
				else if (helm.getType() == Material.CHAINMAIL_HELMET && currentDurability >= Material.CHAINMAIL_HELMET.getMaxDurability())
					setAir = true;
				else if (helm.getType() == Material.IRON_HELMET && currentDurability >= Material.IRON_HELMET.getMaxDurability())
					setAir = true;
				else if (helm.getType() == Material.DIAMOND_HELMET && currentDurability >= Material.DIAMOND_HELMET.getMaxDurability())
					setAir = true;
				else if (helm.getType() == Material.GOLD_HELMET && currentDurability >= Material.GOLD_HELMET.getMaxDurability())
					setAir = true;
				
				if (setAir == true)
					p.getInventory().setHelmet(air);
				else
					p.getInventory().setHelmet(helm);
			}
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
		String guild = null;
		int fireUses = rpgAttacker.getPlayerConfig().getStatusUses(EffectIDs.FIRE_ARROW);
		int partyMemberCount = 1;
		
		//Make sure that the mob wasn't attacked too recently.
		if (FC_Rpg.guildManager.getGuildByMember(playerAttacker.getName()) != null)
		{
			guild = FC_Rpg.guildManager.getGuildByMember(playerAttacker.getName());
			partyMemberCount = FC_Rpg.guildManager.getOnlineGuildPlayerList(guild).size();
			
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
		
		//Update health for ender dragon and wither bosses.
		if (entityDefender.getType() == EntityType.ENDER_DRAGON)
		{
			HealthConverter hc = new HealthConverter(rpgMobDefender.getMaxHealth(), rpgMobDefender.getCurHealth());
			entityDefender.setHealth(hc.getEnderDragonHearts());
		}
		else if (entityDefender.getType() == EntityType.WITHER)
		{
			HealthConverter hc = new HealthConverter(rpgMobDefender.getMaxHealth(), rpgMobDefender.getCurHealth());
			entityDefender.setHealth(hc.getWitherHearts());
		}
		
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
				
				//If the player is in a party, then...
				if (guild != null)
				{
					//Add a mob kill for that party.
					FC_Rpg.guildManager.addMobKill(guild);
					
					for (Player p : FC_Rpg.guildManager.getOnlineGuildPlayerList(guild))
					{
						if (p != null)
							attemptGiveBattleWinnings(guild, p, rpgMobDefender);
					}
				}
				else
					attemptGiveBattleWinnings(guild, playerAttacker, rpgMobDefender);	//Else if not in a party, give loot to the single player.
				
				//Don't drop loot if the monster is too high level.
				if (!(getLevelDifference(playerAttacker, rpgMobDefender) == DROP_FAIL_VALUE))
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
	
	
	private int getLevelDifference(Player playerLooter, RpgMonster rpgMobDefender)
	{
		RpgPlayer rpgLooter = FC_Rpg.rpgManager.getRpgPlayer(playerLooter);
		int levelDifference = rpgMobDefender.getLevel() - rpgLooter.getPlayerConfig().getClassLevel();
		int powerLevelPrevention = FC_Rpg.generalConfig.getPowerLevelPrevention();
		
		if (powerLevelPrevention > -1)
		{
			if (levelDifference > powerLevelPrevention || levelDifference < powerLevelPrevention * -1)
				return DROP_FAIL_VALUE;
		}
		
		return levelDifference;
	}
	
	private void attemptGiveBattleWinnings(String guild, Player playerLooter, RpgMonster rpgMobDefender)
	{
		RpgPlayer rpgLooter = FC_Rpg.rpgManager.getRpgPlayer(playerLooter);
		double bonusPercent = 1;
		int levelDifference = getLevelDifference(playerLooter, rpgMobDefender);
		double cash;
		double exp;
		
		if (levelDifference == DROP_FAIL_VALUE)
		{
			MessageLib msgLib = new MessageLib(playerLooter);
			
			if (levelDifference > 0)
				msgLib.standardMessage("That monster was too strong so you got nothing.");
			else
				msgLib.standardMessage("That monster was too weak so you got nothing.");
			
			return;
		}
		
		//Give a bonus percent based on level difference, 1 level = x% more.
		if (levelDifference == 0)
			bonusPercent = 1;
		else
			bonusPercent = 1 + levelDifference * FC_Rpg.generalConfig.getMobLevelLootmodifier();
		
		//Set up loot amounts.
		cash = rpgMobDefender.getLevel() * bonusPercent * FC_Rpg.generalConfig.getMobCashMultiplier();
		exp = rpgMobDefender.getLevel() * bonusPercent * FC_Rpg.generalConfig.getMobExpMultiplier();
		
		//Add global modifiers
		cash = cash * FC_Rpg.eventCashMultiplier;
		exp = exp * FC_Rpg.eventExpMultiplier;
		
		//Calculate how much loot and experience to aquire by donator
		if (rpgLooter.getPlayerConfig().isDonator())
		{
			cash = cash * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
			exp = exp * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
		}
		
		FC_Rpg.economy.depositPlayer(playerLooter.getName(), FC_Rpg.guildManager.getGuildBonus(guild, cash));
		FC_Rpg.rpgManager.getRpgPlayer(playerLooter).addClassExperience(FC_Rpg.guildManager.getGuildBonus(guild, exp), true);
		
		//Send a message to the player showing experience and loot gains.
		rpgLooter.sendMonsterDeathNotification(rpgMobDefender.getLevel(), exp, cash);
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
