package me.Destro168.FC_Rpg.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.BalanceConfig;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.Spells.SpellEffect;
import me.Destro168.FC_Rpg.Util.HealthConverter;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class EntityDamageManager
{
	public EntityDamageManager() { }
	
	public void attackPlayerDefender(RpgPlayer rpgDefender, RpgPlayer rpgAttacker, RpgMonster rpgMobAttacker, double damage, int damageType, String typeString)
	{
		EntityDamageByEntityEvent edbe;
		Player playerDefender = rpgDefender.getPlayer();
		String guildName = "";
		HealthConverter hc = null;
		boolean ignoreArmor = false;
		int guildMemberCount = 0;
		
		// Get party and member count.
		if (FC_Rpg.guildConfig.getGuildByMember(playerDefender.getName()) != null)
		{
			guildName = FC_Rpg.guildConfig.getGuildByMember(playerDefender.getName());
			guildMemberCount = FC_Rpg.guildConfig.getOnlineGuildPlayerList(guildName).size();
		}
		else
			guildMemberCount = 1;
		
		// Check to see if the player was recently attacked to make sure he isn't attacked too much.
		if (canAttack(rpgDefender.getLastDamagedLong(), guildMemberCount) == false)
			return;
		
		// Refresh player enchants.
		rpgDefender.refreshItemEnchants();
		
		// Handle immortality effect first.
		if (rpgDefender.playerConfig.getStatusActiveRpgPlayer(SpellEffect.IMMORTAL.getID()))
		{
			rpgDefender.attemptDamageAvoidNotification(true);
			return;
		}
		
		// Check if the player has dodge status on them.
		if (rpgDefender.playerConfig.getStatusActiveRpgPlayer(SpellEffect.DODGE.getID()))
		{
			Random rand = new Random();
			
			if (rand.nextInt(100) < rpgDefender.playerConfig.getStatusMagnitude(SpellEffect.DODGE.getID()))
			{
				rpgDefender.attemptDamageAvoidNotification(false);
				return;
			}
		}
		
		if (rpgDefender.playerConfig.getStatusActiveRpgPlayer(SpellEffect.HEAL_CHANCE.getID()))
			rpgDefender.healHealth(rpgDefender.playerConfig.maxHealth * rpgDefender.playerConfig.getStatusMagnitude(SpellEffect.HEAL_CHANCE.getID()));
		
		// Set the last damage cause for players.
		if (rpgAttacker != null)
		{
			// Refresh player enchants.
			rpgAttacker.refreshItemEnchants();
			
			// Update durability of weapons.
			updateSwordDurabilities(rpgAttacker.getPlayer());
			
			// Apply false EDBEE
			edbe = new EntityDamageByEntityEvent(rpgAttacker.getPlayer(), playerDefender, DamageCause.ENTITY_ATTACK, 0);
			playerDefender.setLastDamageCause(edbe);
			
			// Calculate potions.
			damage *= FC_Rpg.battleCalculations.getPotionOffenseBonus(playerDefender);
			
			// True gets returned, we ignore armor.
			ignoreArmor = rpgAttacker.playerConfig.getStatusActiveRpgPlayer(SpellEffect.IGNORE_ARMOR.getID());
		}
		else if (rpgMobAttacker != null)
		{
			// Apply false EDBEE
			edbe = new EntityDamageByEntityEvent(rpgMobAttacker.getEntity(), rpgDefender.getPlayer(), DamageCause.ENTITY_ATTACK, 0);
			playerDefender.setLastDamageCause(edbe);

			// Handle enchantments for mobs.
			if (damageType == 0)
				damage *= FC_Rpg.battleCalculations.getEntityEnchantmentBonus(rpgMobAttacker.getEntity(), Enchantment.DAMAGE_ARTHROPODS);
			
			if (damageType == 1)
				damage *= FC_Rpg.battleCalculations.getEntityEnchantmentBonus(rpgMobAttacker.getEntity(), Enchantment.ARROW_DAMAGE);
			
			// Calculate potions.
			damage *= FC_Rpg.battleCalculations.getPotionOffenseBonus(rpgMobAttacker.getEntity());
		}
		
		// Deal thorns damage before anything is calculated.
		if (rpgDefender.playerConfig.getStatusActiveRpgPlayer(SpellEffect.THORNS.getID()))
		{
			double thornsDamage = damage * rpgDefender.playerConfig.getStatusMagnitude(SpellEffect.THORNS.getID());
			
			// Attempt a dodge notification
			rpgDefender.attemptThornsNotification(thornsDamage);

			if (rpgAttacker != null)
				rpgAttacker.dealDamage(thornsDamage);
			else if (rpgMobAttacker != null)
				rpgMobAttacker.dealDamage(thornsDamage);
		}
		
		// If the attacker doesn't have the ignore defense status, then...
		if (!ignoreArmor)
		{
			// Calculate damage based on players armor.
			damage *= FC_Rpg.battleCalculations.getArmorBonus(rpgDefender);

			// Add in enchantment bonuses.
			damage *= FC_Rpg.battleCalculations.getArmorEnchantmentBonus(playerDefender, Enchantment.PROTECTION_ENVIRONMENTAL);

			if (damageType == 1)
				damage *= FC_Rpg.battleCalculations.getArmorEnchantmentBonus(playerDefender, Enchantment.PROTECTION_PROJECTILE);
		}
		
		damage *= FC_Rpg.battleCalculations.getPotionDefenseBonus(playerDefender);
		
		// Handle block.
		if (playerDefender.isBlocking() == true)
		{
			// Variable Declaration.
			RpgClass rpgClass = rpgDefender.playerConfig.getRpgClass();
			
			if (rpgClass != null)
			{
				if (rpgClass.getPassiveID() == BalanceConfig.passive_StrongerParry)
					damage *= FC_Rpg.balanceConfig.getPassivesStrongerParry(); // 25% damage reduction for blocking as defender.
				else
					damage *= .9; // 10% damage reduction for blocking.
			}
			else
				damage *= .9; // 10% damage reduction for blocking.
		}

		// If taunt status is active, then...
		if (rpgDefender.playerConfig.getStatusActiveRpgPlayer(SpellEffect.TAUNT.getID()) == true)
		{
			// Apply taunt damage reduction.
			damage *= rpgDefender.playerConfig.getStatusMagnitude(SpellEffect.TAUNT.getID());
		}
		
		// Deal damage greater than 0.
		if (damage < 0.1)
			damage = 0.1;
		
		// Deal the damage to the player
		rpgDefender.dealDamage(damage);

		// Perform damage effect.
		playerDefender.playEffect(EntityEffect.HURT);
		
		if (rpgAttacker != null)
		{
			handle_Post_Offense_Buffs(rpgAttacker, playerDefender, damage);
			
			if (playerDefender != null)
				handle_Post_Offense_Buffs(rpgAttacker, playerDefender, damage);
			
			rpgAttacker.attemptAttackNotification(rpgDefender.getPlayer().getName(), rpgDefender.playerConfig.getClassLevel(), rpgDefender.playerConfig.curHealth, rpgDefender.playerConfig.maxHealth, damage);
			applyKnockback(rpgAttacker.getPlayer(), playerDefender, damageType);
		}
		
		// Set the players health based current health out of maximum health.
		hc = new HealthConverter(rpgDefender.playerConfig.maxHealth, rpgDefender.playerConfig.curHealth);

		playerDefender.setHealth(hc.getPlayerHearts());

		// Check armor durabilities for the player defender.
		increaseArmorDurabilities(playerDefender);
		
		// If the players has 0 health, kill minecraft body and set to dead.
		if (rpgDefender.playerConfig.curHealth < 1)
		{
			// Damage the player defender.
			playerDefender.damage(10000);
			
			// Set the player to alive.
			rpgDefender.setIsAlive(false);
		}
		else
		{
			if (rpgAttacker != null)
			{
				rpgDefender.attemptDefenseNotification(damage, rpgAttacker.getPlayer().getName(), rpgAttacker.playerConfig.getClassLevel());
				handle_Defense_Passives(damage, rpgDefender, rpgAttacker.getPlayer());
			}
			else if (rpgMobAttacker != null)
			{
				rpgDefender.attemptDefenseNotification(damage, rpgMobAttacker.getEntity().getType().toString(), rpgMobAttacker.getLevel());
				handle_Defense_Passives(damage, rpgDefender, rpgMobAttacker.getEntity());
			}
			else
			{
				rpgDefender.attemptDefenseNotification(damage, typeString, -1);
			}
		}
	}

	// Mob combat
	public void attackMobDefender(RpgMonster rpgMobDefender, RpgPlayer rpgAttacker, double damage, int damageType)
	{
		// Variable Declarations.
		Player playerAttacker = rpgAttacker.getPlayer();
		LivingEntity entityDefender = rpgMobDefender.getEntity();
		String guild = null;
		int partyMemberCount = 1;

		// Make sure that the mob wasn't attacked too recently.
		if (FC_Rpg.guildConfig.getGuildByMember(playerAttacker.getName()) != null)
		{
			guild = FC_Rpg.guildConfig.getGuildByMember(playerAttacker.getName());
			partyMemberCount = FC_Rpg.guildConfig.getOnlineGuildPlayerList(guild).size();
			
			if (canAttack(rpgMobDefender.getLastDamagedLong(), partyMemberCount) == false)
				return;
		}
		
		// Check to see if the player was recently attacked to make sure he isn't attacked too much.
		if (canAttack(rpgMobDefender.getLastDamagedLong(), partyMemberCount) == false)
			return;
		
		// Refresh player enchants.
		rpgAttacker.refreshItemEnchants();
		
		// If the player has the fire arrow status, then...
		if (rpgAttacker.playerConfig.getStatusActiveRpgPlayer(SpellEffect.FIRE_STRIKE.getID()))
		{
			// If the mob is alive and not a boss.
			if (rpgMobDefender.getIsBoss() == false && rpgMobDefender.getIsAlive() == true)
			{
				// Put mob on fire
				((LivingEntity) entityDefender).setFireTicks(20); // set the mob on fire!
				
				// Then return
				return;
			}
		}
		
		// Update sword durabilities.
		updateSwordDurabilities(playerAttacker);
		
		// If the player has the attack buff, then...
		if (rpgAttacker.playerConfig.getStatusActiveRpgPlayer(SpellEffect.DAMAGE_BONUS.getID()))
		{
			// Increase attack damage by its magnitude.
			damage *= rpgAttacker.playerConfig.getStatusMagnitude(SpellEffect.DAMAGE_BONUS.getID());
		}
		
		if (rpgAttacker.playerConfig.getStatusActiveRpgPlayer(SpellEffect.CRITICAL_DAMAGE_DOUBLE.getID()))
		{
			Random rand = new Random();
			
			// Increase attack damage by its magnitude.
			if (rand.nextInt(100) > rpgAttacker.playerConfig.getStatusMagnitude(SpellEffect.CRITICAL_DAMAGE_DOUBLE.getID()))
				damage *= 2;
		}
		
		// Handle the passives for attacking players.
		damage = handle_Offense_Passives(damage, rpgAttacker, playerAttacker);
		
		// If the attacker doesn't have the ignore defense status, then...
		if (rpgAttacker.playerConfig.getStatusActiveRpgPlayer(SpellEffect.IGNORE_ARMOR.getID()) == false)
		{
			damage *= FC_Rpg.battleCalculations.getArmorBonus(rpgMobDefender);
			
			// Add in enchantment bonuses.
			damage *= FC_Rpg.battleCalculations.getArmorEnchantmentBonus(rpgMobDefender.getEntity(), Enchantment.PROTECTION_ENVIRONMENTAL);
			
			if (damageType == 1)
				damage *= FC_Rpg.battleCalculations.getArmorEnchantmentBonus(rpgMobDefender.getEntity(), Enchantment.PROTECTION_PROJECTILE);
		}
		
		// Account for potions.
		damage *= FC_Rpg.battleCalculations.getPotionOffenseBonus(playerAttacker);
		damage *= FC_Rpg.battleCalculations.getPotionDefenseBonus(rpgMobDefender.getEntity());
		
		// Damage must always be 1 or 0. If it is negative it will heal the creature (not good).
		if (damage < 0.1)
			damage = 0.1;
		
		// Find the attacker and deal damage from the attacker to the defender.
		rpgMobDefender.dealDamage(damage);
		
		// Perform damage effect.
		entityDefender.playEffect(EntityEffect.HURT);
		
		// Update health for ender dragon and wither bosses.
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
		
		// Attempt to send an attack notification.
		rpgAttacker.attemptAttackNotification(rpgMobDefender.getEntity().getType().toString(), rpgMobDefender.getLevel(), rpgMobDefender.getCurHealth(), rpgMobDefender.getMaxHealth(), damage);
		
		// Handle berserker class spells
		handle_Post_Offense_Buffs(rpgAttacker, entityDefender, damage);
		
		if (playerAttacker != null)
			applyKnockback(playerAttacker, entityDefender, damageType);

		// Put entity damage by entity event on both attacker and defender.
		EntityDamageByEntityEvent edbe = new EntityDamageByEntityEvent(playerAttacker, entityDefender, DamageCause.ENTITY_ATTACK, 0);
		playerAttacker.setLastDamageCause(edbe);
		entityDefender.setLastDamageCause(edbe);
		
		// If the mob has 0 health handle it's death processes AND drop loot.
		if (rpgMobDefender.getCurHealth() <= 0)
		{
			if (rpgMobDefender.getIsAlive() == false)
				return;
			
			rpgMobDefender.setIsAlive(false);
			
			if (rpgMobDefender.getMobAggressionCheck())
			{
				// Give the attacker a mob kill
				rpgAttacker.playerConfig.setLifetimeMobKills(rpgAttacker.playerConfig.getLifetimeMobKills() + 1);
				
				// If the player is in a party, then...
				if (guild != null)
					FC_Rpg.guildConfig.addMobKill(guild);	// Add a mob kill for that party.
				
				// Give battle winnings
				attemptGiveBattleWinnings(guild, playerAttacker, rpgMobDefender);
				
				// Don't drop loot if the monster level is too high/low.
				if ((rpgMobDefender.getLevel() - rpgAttacker.playerConfig.getClassLevel()) > FC_Rpg.balanceConfig.getPowerLevelPrevention() * -1)
					rpgMobDefender.handleHostileMobDrops(entityDefender.getLocation()); // Drop rpg items for hostile mobs.
			}
			else
			{
				// Drop items for non-hostile mobs.
				if (FC_Rpg.balanceConfig.getDefaultItemDrops() == false)
					rpgMobDefender.handlePassiveMobDrops(entityDefender.getLocation());
			}

			// Drop experience
			rpgMobDefender.dropExperience();

			// Remove the mob
			removeMob(entityDefender);
		}
	}
	
	// Player combat private functions
	private void updateSwordDurabilities(Player p)
	{
		// Variable Declaration
		ItemStack handItem = p.getItemInHand();
		Random rand = new Random();
		
		// For items with 0 max durability, we return.
		if (handItem.getType().getMaxDurability() == 0)
			return;
		
		// Adjust durability of sword.
		if (handItem.getType().equals(Material.BOW))
			return;
		else if (handItem.getType().equals(Material.WOOD_SWORD) && rand.nextInt(5) != 0) //73
			return;
		else if (handItem.getType().equals(Material.STONE_SWORD) && rand.nextInt(36) != 0)
			return;
		else if (handItem.getType().equals(Material.IRON_SWORD) && rand.nextInt(20) != 0)
			return;
		else if (handItem.getType().equals(Material.DIAMOND_SWORD) && rand.nextInt(3) != 0)
			return;
		else if (handItem.getType().equals(Material.GOLD_SWORD) && rand.nextInt(150) != 0)
			return;
		
		// Increase durability.
		p.getItemInHand().setDurability((short) (p.getItemInHand().getDurability() + 1));
		
		// Set the hand item
		 if (p.getItemInHand().getDurability() >= handItem.getType().getMaxDurability())
			p.setItemInHand(new ItemStack(Material.AIR));
	}
	
	private void increaseArmorDurabilities(Player p)
	{
		short currentDurability = 0;
		ItemStack boots = p.getInventory().getBoots();
		ItemStack chest = p.getInventory().getChestplate();
		ItemStack legs = p.getInventory().getLeggings();
		ItemStack helm = p.getInventory().getHelmet();
		ItemStack air = new ItemStack(Material.AIR);
		Random rand = new Random();
		short maxDurability = 0;
		int leatherCheck = 200;
		int chainCheck = 66;
		int diamondCheck = 30;
		int goldCheck = 140;
		
		if (boots != null)
		{
			if (boots.getType() == Material.LEATHER_BOOTS && rand.nextInt(leatherCheck) == 0)
				maxDurability = Material.LEATHER_BOOTS.getMaxDurability();
			else if (boots.getType() == Material.CHAINMAIL_BOOTS && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.CHAINMAIL_BOOTS.getMaxDurability();
			else if (boots.getType() == Material.IRON_BOOTS && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.IRON_BOOTS.getMaxDurability();
			else if (boots.getType() == Material.DIAMOND_BOOTS && rand.nextInt(diamondCheck) == 0)
				maxDurability = Material.DIAMOND_BOOTS.getMaxDurability();
			else if (boots.getType() == Material.GOLD_BOOTS && rand.nextInt(goldCheck) == 0)
				maxDurability = Material.GOLD_BOOTS.getMaxDurability();
			
			if (maxDurability == 0)
				return;
			
			boots.setDurability((short) (boots.getDurability() + 1));
			currentDurability = boots.getDurability();
			
			if (currentDurability >= maxDurability)
				p.getInventory().setBoots(air);
			else
				p.getInventory().setBoots(boots);
		}
		
		if (chest != null)
		{
			if (chest.getType() == Material.LEATHER_CHESTPLATE && rand.nextInt(leatherCheck) == 0)
				maxDurability = Material.LEATHER_CHESTPLATE.getMaxDurability();
			else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.CHAINMAIL_CHESTPLATE.getMaxDurability();
			else if (chest.getType() == Material.IRON_CHESTPLATE && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.IRON_CHESTPLATE.getMaxDurability();
			else if (chest.getType() == Material.DIAMOND_CHESTPLATE && rand.nextInt(diamondCheck) == 0)
				maxDurability = Material.DIAMOND_CHESTPLATE.getMaxDurability();
			else if (chest.getType() == Material.GOLD_CHESTPLATE && rand.nextInt(goldCheck) == 0)
				maxDurability = Material.GOLD_CHESTPLATE.getMaxDurability();
			
			if (maxDurability == 0)
				return;
			
			chest.setDurability((short) (chest.getDurability() + 1));
			currentDurability = chest.getDurability();
			
			if (currentDurability >= maxDurability)
				p.getInventory().setChestplate(air);
			else
				p.getInventory().setChestplate(chest);
		}
		
		if (legs != null)
		{
			if (legs.getType() == Material.LEATHER_LEGGINGS && rand.nextInt(leatherCheck) == 0)
				maxDurability = Material.LEATHER_LEGGINGS.getMaxDurability();
			else if (legs.getType() == Material.CHAINMAIL_LEGGINGS && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.CHAINMAIL_LEGGINGS.getMaxDurability();
			else if (legs.getType() == Material.IRON_LEGGINGS && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.IRON_LEGGINGS.getMaxDurability();
			else if (legs.getType() == Material.DIAMOND_LEGGINGS && rand.nextInt(diamondCheck) == 0)
				maxDurability = Material.DIAMOND_LEGGINGS.getMaxDurability();
			else if (legs.getType() == Material.GOLD_LEGGINGS && rand.nextInt(goldCheck) == 0)
				maxDurability = Material.GOLD_LEGGINGS.getMaxDurability();
			
			if (maxDurability == 0)
				return;
			
			legs.setDurability((short) (legs.getDurability() + 1));
			currentDurability = legs.getDurability();
			
			if (currentDurability >= maxDurability)
				p.getInventory().setLeggings(air);
			else
				p.getInventory().setLeggings(legs);
		}

		if (helm != null)
		{
			if (helm.getType() == Material.LEATHER_LEGGINGS && rand.nextInt(leatherCheck) == 0)
				maxDurability = Material.LEATHER_LEGGINGS.getMaxDurability();
			else if (helm.getType() == Material.CHAINMAIL_LEGGINGS && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.CHAINMAIL_LEGGINGS.getMaxDurability();
			else if (helm.getType() == Material.IRON_LEGGINGS && rand.nextInt(chainCheck) == 0)
				maxDurability = Material.IRON_LEGGINGS.getMaxDurability();
			else if (helm.getType() == Material.DIAMOND_LEGGINGS && rand.nextInt(diamondCheck) == 0)
				maxDurability = Material.DIAMOND_LEGGINGS.getMaxDurability();
			else if (helm.getType() == Material.GOLD_LEGGINGS && rand.nextInt(goldCheck) == 0)
				maxDurability = Material.GOLD_LEGGINGS.getMaxDurability();
			
			if (maxDurability == 0)
				return;
			
			helm.setDurability((short) (helm.getDurability() + 1));
			currentDurability = helm.getDurability();
			
			if (currentDurability >= maxDurability)
				p.getInventory().setHelmet(air);
			else
				p.getInventory().setHelmet(helm);
		}
	}

	private void applyKnockback(Player attacker, LivingEntity attacked, int damageType)
	{
		ItemStack handItem = attacker.getItemInHand();
		float knockback = 0;

		// If arrow knockback is on, and the damage is arrow type, then...
		if (FC_Rpg.balanceConfig.getArrowKnockback() == true && damageType == 1)
		{
			// Add knockback.
			knockback += 1;

			// Add in additional knockback based on the punch enchants strength.
			if (handItem.containsEnchantment(Enchantment.ARROW_KNOCKBACK))
				knockback += handItem.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK);
		}
		else if (FC_Rpg.balanceConfig.getSwordKnockback())
		{
			// Increase knockback based on damage type.
			if (handItem.containsEnchantment(Enchantment.KNOCKBACK))
				knockback += handItem.getEnchantmentLevel(Enchantment.KNOCKBACK);
		}

		// If there is a knockback the we apply it.
		if (knockback > 0)
			attacked.setVelocity(attacked.getVelocity().add(attacked.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockback)));
	}
	
	// Mob combat private functions
	private void attemptGiveBattleWinnings(String guild, Player playerLooter, RpgMonster rpgMobDefender)
	{
		List<RpgPlayer> recipients = new ArrayList<RpgPlayer>();
		double baseCash;
		double baseExp;
		double donatorBaseCash;
		double donatorBaseExp;
		double cash;
		double exp;
		double guildBonus = 1;
		int powerLevelPrevention = FC_Rpg.balanceConfig.getPowerLevelPrevention();
		boolean checkPowerLeveling;

		if (rpgMobDefender.getIsBoss())
			checkPowerLeveling = false;
		else
			checkPowerLeveling = (powerLevelPrevention > -1);
		
		if (guild != null)
		{
			recipients = FC_Rpg.rpgEntityManager.getNearbyPartiedRpgPlayers(playerLooter, 50);
			guildBonus = FC_Rpg.guildConfig.getGuildBonus(recipients.size());
		}
		else
			recipients.add(FC_Rpg.rpgEntityManager.getRpgPlayer(playerLooter));
		
		// Determine base amount of cash and experience to give.
		baseCash = rpgMobDefender.baseCash * FC_Rpg.eventCashMultiplier;
		baseExp = rpgMobDefender.baseExp * FC_Rpg.eventExpMultiplier;
		
		// Add in guild bonus if there is one.
		if (guildBonus > 1)
		{
			baseCash *= guildBonus;
			baseExp *= guildBonus;
		}
		
		// For slimes we reduce the gold and exp based on size.
		if (rpgMobDefender.getEntity() instanceof Slime)
		{
			Slime slime = (Slime) rpgMobDefender.getEntity();
			int slimeSize = slime.getSize();

			if (slimeSize == 2)
			{
				baseExp *= .25;
				baseCash *= .25;
			}
			else if (slimeSize == 1)
			{
				baseExp *= .125;
				baseCash *= .125;
			}
		}
		
		donatorBaseCash = baseCash * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
		donatorBaseExp = baseExp * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
		
		// If we check power leveling and the player isn't solo, then...
		if (checkPowerLeveling && recipients.size() != 1)
		{
			// Variable Declaration
			RpgPlayer rPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(playerLooter);
			
			// Check to see if killer is too strong to give exp.
			// Mob Level - Player Level = levelDifference : Positive = Mob stronger, negative = mob weaker.
			if (rpgMobDefender.getLevel() - rPlayer.playerConfig.getClassLevel() > powerLevelPrevention) {
				rPlayer.attemptMonsterOutOfRangeNotification();
				return;
			}
			else if (rpgMobDefender.getLevel() - rPlayer.playerConfig.getClassLevel() < powerLevelPrevention * -1)
			{
				MessageLib msgLib = new MessageLib(rPlayer.getPlayer());
				msgLib.standardError("You annhilated the monster so brutally most loot was destroyed.");
				return;
			}
		}
		
		for (RpgPlayer rpgLooter : recipients)
		{
			// Set up loot amounts.
			if (rpgLooter.playerConfig.isDonator())
			{
				cash = donatorBaseCash;
				exp = donatorBaseExp;
			}
			else
			{
				cash = baseCash;
				exp = baseExp;
			}
			
			if (rpgLooter.playerConfig.getStatusActiveRpgPlayer(SpellEffect.BONUS_EXPERIENCE.getID()))
				exp *= rpgLooter.playerConfig.getStatusMagnitude(SpellEffect.BONUS_EXPERIENCE.getID());
			
			if (rpgLooter.playerConfig.getStatusActiveRpgPlayer(SpellEffect.BONUS_GOLD.getID()))
				exp *= rpgLooter.playerConfig.getStatusMagnitude(SpellEffect.BONUS_GOLD.getID());
			
			rpgLooter.playerConfig.addGold(cash);
			rpgLooter.addClassExperience(exp, true);
			
			// Send a message to the player showing experience and loot gains.
			rpgLooter.attemptMonsterDeathNotification(rpgMobDefender.getLevel(), exp, cash);
		}
	}

	
	// Handle player defense skills
	private void handle_Defense_Passives(double damage, RpgPlayer rpgDefender, LivingEntity entityAttacker)
	{
		// Variable Declarations
		Random rand = new Random();
		RpgClass rpgClass = rpgDefender.playerConfig.getRpgClass();

		if (rpgClass != null)
		{
			if (rpgClass.getPassiveID() == BalanceConfig.passive_CounterAttack)
			{
				// Handle counter-attack chance.
				if (rand.nextInt(FC_Rpg.balanceConfig.getPassivesCounterAttack()) == 0)
					entityAttacker.damage(1, rpgDefender.getPlayer());
			}
		}
	}

	private double handle_Offense_Passives(double damage, RpgPlayer rpgAttacker, LivingEntity entityDefender)
	{
		// Variable Declaration.
		RpgClass rpgClass = rpgAttacker.playerConfig.getRpgClass();

		if (rpgClass != null)
		{
			if (rpgClass.getPassiveID() == BalanceConfig.passive_BattleLust)
			{
				// Scale damage by 1/4th
				damage *= (1 + rpgAttacker.getMissingHealthDecimal() * FC_Rpg.balanceConfig.getPassivesBattleLust());
			}
		}

		return damage;
	}

	private void handle_Post_Offense_Buffs(RpgPlayer bearer, LivingEntity defender, double damage)
	{
		if (bearer.playerConfig.getStatusActiveRpgPlayer(SpellEffect.HEALTH_STEAL.getID()))
		{
			double healAmount = damage * bearer.playerConfig.getStatusMagnitude(SpellEffect.HEALTH_STEAL.getID());

			bearer.attemptHealthHealSelfNotification(healAmount);
			bearer.healHealth(healAmount);
			bearer.dequeHealMessage();
		}
		
		if (bearer.playerConfig.getStatusActiveRpgPlayer(SpellEffect.TELEPORT_STRIKE.getID()))
		{
			EntityLocationLib ell = new EntityLocationLib();
			bearer.getPlayer().teleport(ell.getLocationBehindEntity(defender.getLocation()));
		}
	}
	
	// This will check for Pvp buffs
	public void handle_Post_Offense_Buffs(RpgPlayer bearer, RpgPlayer playerDefender, double damage)
	{
		if (bearer.playerConfig.getStatusActiveRpgPlayer(SpellEffect.MANA_STEAL.getID()))
		{
			double drainAmount = damage * bearer.playerConfig.getStatusMagnitude(SpellEffect.MANA_STEAL.getID());
			
			// Give to bearer.
			bearer.healMana(drainAmount);
			bearer.attemptManaHealSelfNotification(drainAmount);
			bearer.dequeHealMessage();
		}
	}
	
	public boolean canAttack(long time, int partySize)
	{
		// Variable Declaration.
		int check = 0;

		// Set attack delay equal to setting.
		if (FC_Rpg.balanceConfig.getAttackDelayHard() > -1)
			check = FC_Rpg.balanceConfig.getAttackDelayHard();
		else
			check = FC_Rpg.balanceConfig.getAttackDelaySoft() / partySize;

		// Return true if time difference is great enough.
		return (System.currentTimeMillis() - time) >= check;
	}

	public void nukeMob(LivingEntity entity)
	{
		// Get The Entity.
		RpgMonster monster = FC_Rpg.rpgEntityManager.getRpgMonster(entity);

		if (monster == null)
			entity.remove();
		else
			removeMob(entity);
	}

	public void removeMob(LivingEntity entity)
	{
		// Unregister a mob.
		FC_Rpg.rpgEntityManager.unregisterRpgMonster(entity);

		// Remove the mob.
		entity.damage(99999);
	}
}
