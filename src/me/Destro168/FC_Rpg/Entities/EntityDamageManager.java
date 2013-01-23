package me.Destro168.FC_Rpg.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.BalanceConfig;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.Spells.EffectIDs;
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
	
	//Player combat.
	public void attackPlayerDefender(RpgPlayer rpgDefender, RpgPlayer rpgAttacker, RpgMonster rpgMobAttacker, double damage, int damageType, String typeString)
	{
		Player playerDefender = rpgDefender.getPlayer();
		String party = "";
		HealthConverter hc = null;
		int memberCount = 0;
		
		// Get party and member count.
		if (FC_Rpg.guildConfig.getGuildByMember(playerDefender.getName()) != null)
		{
			party = FC_Rpg.guildConfig.getGuildByMember(playerDefender.getName());
			memberCount = FC_Rpg.guildConfig.getOnlineGuildPlayerList(party).size();
		}
		else
			memberCount = 1;
		
		// Check to see if the player was recently attacked to make sure he isn't attacked too much.
		if (canAttack(rpgDefender.getLastDamagedLong(), memberCount) == false)
			return;
		
		// Handle immortality effect first.
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.IMMORTAL))
		{
			rpgDefender.attemptDamageAvoidNotification(true);
			return;
		}

		// Check if the player has dodge status on them.
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.DODGE))
		{
			Random rand = new Random();
			
			if (rand.nextInt(100) < rpgDefender.playerConfig.getStatusMagnitude(EffectIDs.DODGE))
			{
				rpgDefender.attemptDamageAvoidNotification(false);
				return;
			}
		}
		
		// Set the last damage cause for players.
		EntityDamageByEntityEvent edbe;

		if (rpgAttacker != null)
		{
			updateSwordDurabilities(rpgAttacker.getPlayer());
			
			edbe = new EntityDamageByEntityEvent(rpgAttacker.getPlayer(), playerDefender, DamageCause.ENTITY_ATTACK, 0);
			playerDefender.setLastDamageCause(edbe);
			
			// Handle sharpness enchant.
			if (damageType == 0)
				damage = damage * FC_Rpg.battleCalculations.getPlayerEnchantmentBonus(rpgAttacker.getPlayer(), Enchantment.DAMAGE_ALL);
			
			if (damageType == 1)
				damage = damage * FC_Rpg.battleCalculations.getPlayerEnchantmentBonus(rpgAttacker.getPlayer(), Enchantment.ARROW_DAMAGE);
			
			damage = damage * FC_Rpg.battleCalculations.getPotionOffenseBonus(playerDefender);
		}
		else if (rpgMobAttacker != null)
		{
			edbe = new EntityDamageByEntityEvent(rpgMobAttacker.getEntity(), rpgDefender.getPlayer(), DamageCause.ENTITY_ATTACK, 0);
			playerDefender.setLastDamageCause(edbe);
			
			// Handle sharpness enchant.
			if (damageType == 0)
				damage = damage * FC_Rpg.battleCalculations.getEntityEnchantmentBonus(rpgMobAttacker.getEntity(), Enchantment.DAMAGE_ARTHROPODS);
			
			if (damageType == 1)
				damage = damage * FC_Rpg.battleCalculations.getEntityEnchantmentBonus(rpgMobAttacker.getEntity(), Enchantment.ARROW_DAMAGE);
			
			damage = damage * FC_Rpg.battleCalculations.getPotionOffenseBonus(rpgMobAttacker.getEntity());
		}
		
		// Deal thorns damage before anything is calculated.
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.THORNS))
		{
			double thornsDamage = damage * rpgDefender.playerConfig.getStatusMagnitude(EffectIDs.THORNS);
			
			// Attempt a dodge notification
			rpgDefender.attemptThornsNotification(thornsDamage);

			if (rpgAttacker != null)
				rpgAttacker.dealDamage(thornsDamage);
			else if (rpgMobAttacker != null)
				rpgMobAttacker.dealDamage(thornsDamage);
		}
		
		// Calculate damage based on players armor.
		damage = damage * FC_Rpg.battleCalculations.getArmorBonus(rpgDefender);
		
		// Add in enchantment bonuses.
		damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(playerDefender, Enchantment.PROTECTION_ENVIRONMENTAL);
		
		if (damageType == 1)
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(playerDefender, Enchantment.PROTECTION_PROJECTILE);
		
		damage = damage * FC_Rpg.battleCalculations.getPotionDefenseBonus(playerDefender);
		
		// Handle block.
		if (playerDefender.isBlocking() == true)
		{
			// Variable Declaration.
			RpgClass rpgClass = rpgDefender.playerConfig.getRpgClass();
			
			if (rpgClass != null)
			{
				if (rpgClass.getPassiveID() == BalanceConfig.passive_StrongerParry)
					damage = damage * FC_Rpg.balanceConfig.getPassivesStrongerParry(); // 25% damage reduction for blocking as defender.
				else
					damage = damage * .9; // 10% damage reduction for blocking.
			}
			else
				damage = damage * .9; // 10% damage reduction for blocking.
		}

		// If taunt status is active, then...
		if (rpgDefender.getStatusActiveRpgPlayer(EffectIDs.TAUNT) == true)
		{
			// Apply taunt damage reduction.
			damage = damage * rpgDefender.playerConfig.getStatusMagnitude(EffectIDs.TAUNT);
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
			handle_Postoffense_Buffs(rpgAttacker, playerDefender, damage);
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
	
	private void updateSwordDurabilities(final Player p)
	{
		//Variable Declaration
		ItemStack handItem = p.getItemInHand();
		int handItemDurability = handItem.getDurability();
		int maxDurability = handItem.getType().getMaxDurability();
		int unbreakingLevel = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
		Random rand = new Random();
		
		//Adjust durability of sword.
		if (FC_Rpg.mLib.swords.contains(handItem.getType()) || handItem.getType().equals(Material.BOW))
		{
			//If the item has unbreaking, then we have to give the item a chance to not break based on it.
			if (handItem.getEnchantments().size() == 0)
				return;
			
			//Handle sword unbreaking level.
			if (unbreakingLevel > 0)
			{
				if ((rand.nextInt(unbreakingLevel)) == 0)
					return;
			}
			
			// Increase durability.
			handItem.setDurability((short) (handItemDurability + 1));
			
			// Set the hand item 
			if (handItemDurability >= maxDurability)
				p.setItemInHand(new ItemStack(Material.AIR));
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

	private void applyKnockback(Player attacker, LivingEntity attacked, int damageType)
	{
		ItemStack handItem = attacker.getItemInHand();
		float knockback = 0;
		
		// If arrow knockback is on, and the damage is arrow type, then...
		if (FC_Rpg.balanceConfig.getArrowKnockback() == true && damageType == 1)
		{
			//Add knockback.
			knockback += 1;
			
			//Add in additional knockback based on the punch enchants strength.
			if (handItem.containsEnchantment(Enchantment.ARROW_KNOCKBACK))
				knockback += handItem.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK);
		}
		else if (FC_Rpg.balanceConfig.getSwordKnockback())
		{
			//Increase knockback based on damage type.
			if (handItem.containsEnchantment(Enchantment.KNOCKBACK))
				knockback += handItem.getEnchantmentLevel(Enchantment.KNOCKBACK);
		}
		
		// If there is a knockback the we apply it.
		if (knockback > 0)
			attacked.setVelocity(attacked.getVelocity().add(attacked.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockback)));
	}
	
	//Mob combat
	public void attackMobDefender(RpgMonster rpgMobDefender, RpgPlayer rpgAttacker, double damage, int damageType)
	{
		// Variable Declarations.
		Player playerAttacker = rpgAttacker.getPlayer();
		LivingEntity entityDefender = rpgMobDefender.getEntity();
		String guild = null;
		int fireUses = rpgAttacker.playerConfig.getStatusUses(EffectIDs.FIRE_ARROW);
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
		
		// If the player has the fire arrow status, then...
		if (fireUses > 0)
		{
			// If the mob is alive and not a boss.
			if (rpgMobDefender.getIsBoss() == false && rpgMobDefender.getIsAlive() == true)
			{
				// Put mob on fire
				((LivingEntity) entityDefender).setFireTicks(20); // set the mob on fire!
				
				// Remove a fire arrow use.
				rpgAttacker.playerConfig.setStatusUses(EffectIDs.FIRE_ARROW, fireUses - 1);
				
				// Then return
				return;
			}
		}
		
		//Update sword durabilities.
		updateSwordDurabilities(rpgAttacker.getPlayer());
		
		// If the player has the attack buff, then...
		if (rpgAttacker.getStatusActiveRpgPlayer(EffectIDs.ATTACK))
		{
			// Increase attack damage by its magnitude.
			damage = damage * rpgAttacker.playerConfig.getStatusMagnitude(EffectIDs.ATTACK);
		}
		
		if (damageType == 0)
		{
			damage = damage * FC_Rpg.battleCalculations.getPlayerEnchantmentBonus(rpgAttacker.getPlayer(), Enchantment.DAMAGE_ALL);
			
			if (entityDefender.getType() == EntityType.SPIDER || entityDefender.getType() == EntityType.CAVE_SPIDER)
				damage = damage * FC_Rpg.battleCalculations.getPlayerEnchantmentBonus(rpgAttacker.getPlayer(), Enchantment.DAMAGE_ARTHROPODS);
			else if (entityDefender.getType() == EntityType.ZOMBIE || entityDefender.getType() == EntityType.PIG_ZOMBIE || entityDefender.getType() == EntityType.SKELETON)
				damage = damage * FC_Rpg.battleCalculations.getPlayerEnchantmentBonus(rpgAttacker.getPlayer(), Enchantment.DAMAGE_UNDEAD);
		}
		
		if (damageType == 1)
			damage = damage * FC_Rpg.battleCalculations.getPlayerEnchantmentBonus(rpgAttacker.getPlayer(), Enchantment.ARROW_DAMAGE);
		
		// Handle the passives for attacking players.
		damage = handle_Offense_Passives(damage, rpgAttacker, playerAttacker);
		
		// Calculate damage based on monsters defense.
		damage = damage * FC_Rpg.battleCalculations.getArmorBonus(rpgMobDefender);
		
		// Add in enchantment bonuses.
		damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(rpgMobDefender.getEntity(), Enchantment.PROTECTION_ENVIRONMENTAL);
		
		if (damageType == 1)
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(rpgMobDefender.getEntity(), Enchantment.PROTECTION_PROJECTILE);
		
		//Account for potions.
		damage = damage * FC_Rpg.battleCalculations.getPotionOffenseBonus(playerAttacker);
		damage = damage * FC_Rpg.battleCalculations.getPotionDefenseBonus(rpgMobDefender.getEntity());
		
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
		handle_Postoffense_Buffs(rpgAttacker, entityDefender, damage);
		
		if (playerAttacker != null)
			applyKnockback(playerAttacker, entityDefender, damageType);
		
		//Put entity damage by entity event on both attacker and defender.
		EntityDamageByEntityEvent edbe = new EntityDamageByEntityEvent(rpgAttacker.getPlayer(), entityDefender, DamageCause.ENTITY_ATTACK, 0);
		rpgAttacker.getPlayer().setLastDamageCause(edbe);
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
				{
					// Add a mob kill for that party.
					FC_Rpg.guildConfig.addMobKill(guild);
					
					//Give battle winnings
					attemptGiveBattleWinnings(guild, rpgAttacker.getPlayer(), rpgMobDefender);
				}
				else
					attemptGiveBattleWinnings(guild, playerAttacker, rpgMobDefender); // Else if not in a party, give loot to the single player.
				
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
			
			//Drop experience
			rpgMobDefender.dropExperience();
			
			// Remove the mob
			removeMob(entityDefender);
		}
	}
	
	private void attemptGiveBattleWinnings(String guild, Player playerLooter, RpgMonster rpgMobDefender)
	{
		List<RpgPlayer> recipients = new ArrayList<RpgPlayer>();
		double cash;
		double exp;
		double guildBonus = 1;
		int powerLevelPrevention = FC_Rpg.balanceConfig.getPowerLevelPrevention();
		int levelDifference;
		boolean checkPowerLeveling = (powerLevelPrevention > -1);
		
		if (checkPowerLeveling && rpgMobDefender.getIsBoss())
			checkPowerLeveling = false;
		
		if (guild != null)
		{
			recipients = FC_Rpg.rpgEntityManager.getNearbyPartiedRpgPlayers(playerLooter, 50);
			guildBonus = FC_Rpg.guildConfig.getGuildBonus(recipients.size());
		}
		else
			recipients.add(FC_Rpg.rpgEntityManager.getRpgPlayer(playerLooter));
		
		for (RpgPlayer rpgLooter : recipients)
		{
			
			//Mob level - player level : Positive = Mob stronger, negative = mob weaker.
			levelDifference = rpgMobDefender.getLevel() - rpgLooter.playerConfig.getClassLevel();
			
			if (checkPowerLeveling && levelDifference < powerLevelPrevention * -1)
			{
				MessageLib msgLib = new MessageLib(rpgLooter.getPlayer());
				msgLib.standardError("You annhilated the monster so brutally most loot was destroyed.");
			}
			else if (checkPowerLeveling && levelDifference > powerLevelPrevention)
			{
				//Allow solo players to still recieve loot from stronger level monsters.
				if (recipients.size() == 1)
					levelDifference = powerLevelPrevention;
				else
				{
					rpgLooter.attemptMonsterOutOfRangeNotification();
					return;
				}
			}
			else
			{
				// Set up loot amounts.
				cash = rpgMobDefender.getLevel() * FC_Rpg.balanceConfig.getMobCashMultiplier() * FC_Rpg.eventCashMultiplier;;
				exp = rpgMobDefender.getLevel() * FC_Rpg.balanceConfig.getMobExpMultiplier() * FC_Rpg.eventExpMultiplier;
				
				// Calculate how much loot and experience to aquire by donator
				if (rpgLooter.playerConfig.isDonator())
				{
					cash = cash * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
					exp = exp * (1 + FC_Rpg.generalConfig.getDonatorLootBonusPercent());
				}
				
				// For slimes we reduce the gold and exp based on size.
				if (rpgMobDefender.getEntity() instanceof Slime)
				{
					Slime slime = (Slime) rpgMobDefender.getEntity();
					int slimeSize = slime.getSize();
					
					if (slimeSize == 2)
					{
						exp = exp * .25;
						cash = cash * .25;
					}
					else if (slimeSize == 1)
					{
						exp = exp * .125;
						cash = cash * .125;
					}
				}
				
				if (guildBonus > 1)
				{
					cash = cash * guildBonus;
					exp = exp * guildBonus;
				}
				
				FC_Rpg.economy.depositPlayer(rpgLooter.getPlayer().getName(),cash);
				FC_Rpg.rpgEntityManager.getRpgPlayer(rpgLooter.getPlayer()).addClassExperience(exp, true);
				
				// Send a message to the player showing experience and loot gains.
				rpgLooter.attemptMonsterDeathNotification(rpgMobDefender.getLevel(), exp, cash);
			}
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
				damage = damage * (1 + rpgAttacker.getMissingHealthDecimal() * FC_Rpg.balanceConfig.getPassivesBattleLust());
			}
		}

		return damage;
	}

	private void handle_Postoffense_Buffs(RpgPlayer caster, LivingEntity defender, double damage)
	{
		if (caster.getStatusActiveRpgPlayer(EffectIDs.LIFESTEAL))
		{
			double healAmount = damage * caster.playerConfig.getStatusMagnitude(EffectIDs.LIFESTEAL);
			
			caster.attemptHealSelfNotification(healAmount);
			caster.healHealth(healAmount);
		}
		
		if (caster.getStatusActiveRpgPlayer(EffectIDs.TELEPORT_STRIKE))
		{
			EntityLocationLib ell = new EntityLocationLib();
			caster.getPlayer().teleport(ell.getLocationBehindEntity(defender.getLocation()));
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
