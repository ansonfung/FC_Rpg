package me.Destro168.FC_Rpg.Listeners;

import java.util.List;
import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.WorldConfig;
import me.Destro168.FC_Rpg.Entities.EntityDamageManager;
import me.Destro168.FC_Rpg.Entities.RpgMonster;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
import me.Destro168.FC_Rpg.Spells.EffectIDs;
import me.Destro168.FC_Rpg.Spells.SpellCaster;
import me.Destro168.FC_Rpg.Util.DistanceModifierLib;
import me.Destro168.FC_Rpg.Util.MobAggressionCheck;

import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener implements Listener
{
	// Variable Declarations
	EntityDamageEvent event;

	Player playerDefender;
	Player playerAttacker;
	String party;

	Random defendChance;
	Random counterattackChance;
	
	MobAggressionCheck mac;
	EntityDamageManager edm;
	
	RpgPlayer rpgDefender;
	RpgPlayer rpgAttacker;
	RpgMonster rpgMobAttacker;
	RpgMonster rpgMobDefender;

	boolean preventInfiniteSpecial;
	boolean cancelRpgDamage;
	int memberCount;
	int posX;
	int posZ;
	int damageType; // 0 = melee, 1 = arrow, 2 = meteor, 3 = spell

	LivingEntity creatureAttacker;
	LivingEntity mobDefender;

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event_)
	{
		// If the event is cancelled, then we want to return.
		if (event_.isCancelled() == true)
			return;
		
		// Block damage in creative world.
		WorldConfig wm = new WorldConfig();
		
		if (!wm.getIsRpgWorld(event_.getEntity().getWorld().getName()))
			return;
		
		// Store event.
		event = event_;

		// Set defaults.
		setGlobalDefaults();

		// Always set event damage to 0.
		event.setDamage(0);

		boolean dealEnviromentalDamage = false;
		
		// For entity damage events we want to handle the attck normally.
		if (event instanceof EntityDamageByEntityEvent)
		{
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			
			if (damager.getType() == EntityType.WITHER)
				dealEnviromentalDamage = true;
			else if (damager.getType() == EntityType.WITHER_SKULL)
				dealEnviromentalDamage = true;
			else if (damager.getType() == EntityType.SPLASH_POTION)
				dealEnviromentalDamage = true;
			else if (damager.getType() == EntityType.PRIMED_TNT)
				dealEnviromentalDamage = true;
			else
				entityAttack((EntityDamageByEntityEvent) event, -1);
		}
		else
			dealEnviromentalDamage = true;
		
		// Else we want to deal the damage to the player/mob as though not from an entity.
		if (dealEnviromentalDamage == true)
		{
			// If the defender is a player, then we attacker player defender.
			if (event.getEntity() instanceof Player)
			{
				Player player = (Player) event.getEntity();
				
				// Prevent damage done to already dead players
				if (FC_Rpg.rpgEntityManager.getRpgPlayer(player).getPlayerConfig().getIsActive() == true)
				{
					if (FC_Rpg.rpgEntityManager.getRpgPlayer(player).getIsAlive() == false)
						return;
				}
				
				// Prepare the defender.
				prepareDefender(player);
				
				double damage = getEnviromentalDamage(player);

				if (damage == 0)
					return;
				
				// Attack the player with enviromental damage.
				edm.attackPlayerDefender(rpgDefender, null, null, damage, damageType);
			}
			else
			{
				if (event.getCause() != DamageCause.FALL)
				{
					if (event.getEntity() instanceof LivingEntity)
						edm.nukeMob((LivingEntity) event.getEntity());
				}
			}
		}
	}

	public void setGlobalDefaults()
	{
		// Cancel the event.
		event.setCancelled(true);
		
		playerDefender = null;
		playerAttacker = null;
		party = null;

		defendChance = new Random();
		counterattackChance = new Random();

		mac = null;
		edm = new EntityDamageManager();

		rpgDefender = null;
		rpgAttacker = null;
		rpgMobAttacker = null;
		rpgMobDefender = null;

		preventInfiniteSpecial = false;
		cancelRpgDamage = false;

		memberCount = 0;
		posX = 0;
		posZ = 0;
		damageType = 0;

		creatureAttacker = null;
		mobDefender = null;
	}

	// -1, false, false are defaults, attackmob and attack player are only processed when damage = -1.
	public void entityAttack(EntityDamageByEntityEvent ed, double customDamage)
	{
		double damage = 0;

		event = ed;

		// Reset all globals.
		setGlobalDefaults();

		// Set event damage to 0.
		ed.setDamage(0);

		LivingEntity entity;
		Entity eEntity = ed.getEntity();
		
		if (eEntity instanceof Arrow)
		{
			Arrow ca = (Arrow) eEntity;
			entity = ca.getShooter();
		}
		else if (eEntity instanceof LargeFireball)
		{
			LargeFireball cf = (LargeFireball) eEntity;
			entity = cf.getShooter();
		}
		else if (eEntity instanceof ExperienceOrb) {return;}
		else if (eEntity instanceof Item) {return;}
		else
		{
			// Set entity equal to the entity that got hit.
			try { 
				entity = (LivingEntity) ed.getEntity(); 
			} catch (ClassCastException e) {
				FC_Rpg.plugin.getLogger().info("Failed to cast an entity to living entity, damage cancelled -> " + eEntity.toString() + " <- report to a FC_Rpg developer via a ticket please.");
				return;
			}
		}
		
		// Prepare the defender and attacker variables.
		prepareDefender(entity);

		// If a damage was passed in, then we don't want to set damage. Else, store new damage
		damage = prepareAttacker(ed);

		// If we are canceling rpg damage, then return.
		if (cancelRpgDamage == true)
			return;

		if (customDamage > -1)
			damage = customDamage;

		/***********************************************************
		 * 
		 * 
		 * SET THE BASE DAMAGE BASED ON IF A PLAYER, MOB, OR ENVIROMENT CAUSED DAMAGE
		 * 
		 * 
		 ***********************************************************/
		
		// We do a sword check for atttackers.
		if (rpgAttacker != null)
		{
			// Check the attackers sword.
			rpgAttacker.swordAttackRequirementCheck();
			
			// We handle spells on non-fireball player attacks.
			if (damageType != 2)
			{
				if (rpgAttacker.getPlayerConfig().getAutoCast() == true)
					rpgAttacker.prepareSpell(false);
				
				if (rpgMobDefender != null)
					damage = rpgAttacker.castSpell(rpgMobDefender.getEntity(), damage, damageType);
				else
					damage = rpgAttacker.castSpell(rpgDefender.getPlayer(), damage, damageType);
			}
		}
		
		// We do a armor check for defenders.
		if (rpgDefender != null)
			rpgDefender.fullArmorCheck();
		
		//Apply randomization to damage.
		damage = getRandomDamageModifier(damage);
		
		if (rpgMobDefender != null)
		{
			// Prevent mobs from damaging friendly mobs.
			if (rpgMobAttacker != null)
				return;
			
			// If no player attacked, then we want to nuke the mob (enviromental damage).
			if (rpgAttacker == null)
			{
				edm.nukeMob(rpgMobDefender.getEntity());
				return;
			}
			
			// Attack the mob defender
			edm.attackMobDefender(rpgMobDefender, rpgAttacker, damage, damageType);
			
			// Make creatures forcibly attack attacker.
			if (playerAttacker != null && mobDefender != null)
			{
				// Set wolves to angry.
				if (mobDefender instanceof Wolf)
					((Wolf) mobDefender).setAngry(true);

				if (mobDefender instanceof Creature)
				{
					// Change aggro
					((Creature) mobDefender).setTarget(playerAttacker);
				}
			}
		}
		else if (rpgDefender != null)
		{
			// Attack the player Defender.
			if (rpgAttacker != null)
				edm.attackPlayerDefender(rpgDefender, rpgAttacker, null, damage, damageType);
			else if (rpgMobAttacker != null)
				edm.attackPlayerDefender(rpgDefender, null, rpgMobAttacker, damage, damageType);
		}
	}

	private double getRandomDamageModifier(double damage)
	{
		double damageMultiplier = damage * .05;
		
		Random rand;
		
		if (damageMultiplier >= 1)
		{
			rand = new Random();
			
			if (rand.nextBoolean() == true)
				damage += rand.nextInt((int) damageMultiplier) + 1;
			else
				damage -= rand.nextInt((int) damageMultiplier) - 1;
		}
		
		rand = new Random();
		
		if (rand.nextBoolean() == true)
			damage += rand.nextDouble();
		else
			damage -= rand.nextDouble();
		
		return damage;
	}
	
	public void prepareDefender(LivingEntity entityDefender)
	{
		// Set up all rpg information based on involved entities.
		if (entityDefender instanceof Player)
		{
			playerDefender = (Player) entityDefender;
			rpgDefender = FC_Rpg.rpgEntityManager.getRpgPlayer(playerDefender);
		}
		else if (entityDefender instanceof LivingEntity)
		{
			mobDefender = (LivingEntity) entityDefender;
			rpgMobDefender = FC_Rpg.rpgEntityManager.getRpgMonster(mobDefender);
		}
	}

	public double prepareAttacker(EntityDamageByEntityEvent e)
	{
		// Variable Declarations/Initializations
		Arrow arrow = null;
		List<Spell> spellBook = null;
		double damage = 0;
		rpgMobAttacker = null;

		// Determine what the source of damage was.
		if (e.getDamager() instanceof Egg)
		{
			cancelRpgDamage = true;
			return 0;
		}
		
		else if (e.getDamager() instanceof Fish)
		{
			cancelRpgDamage = true;
			return 0;
		}

		else if (e.getDamager() instanceof Snowball)
		{
			cancelRpgDamage = true;
			return 0;
		}

		else if (e.getDamager() instanceof Fireball)
		{
			boolean success = false;
			
			Fireball fb = (Fireball) (e.getDamager());

			if (fb.getShooter() == e.getEntity())
			{
				cancelRpgDamage = true;
				return 0;
			}
			
			if (e.getEntity() instanceof Fireball || e.getEntity() instanceof LargeFireball)
			{
				cancelRpgDamage = true;
				return 0;
			}
			
			for (RpgPlayer rpgPlayer : FC_Rpg.rpgEntityManager.getOnlineRpgPlayers())
			{
				if (rpgPlayer.summon_Owns(e.getDamager()))
				{
					// Variable Initializations
					rpgAttacker = rpgPlayer;
					spellBook = rpgAttacker.getPlayerConfig().getRpgClass().getSpellBook();
					damageType = 2;
					
					for (int i = 0; i < spellBook.size(); i++)
					{
						if (spellBook.get(i).getEffectID() == EffectIDs.FIREBALL)
						{
							SpellCaster sc = new SpellCaster();
							damage = sc.updatefinalSpellMagnitude(rpgAttacker, spellBook.get(i), (rpgAttacker.getPlayerConfig().getSpellLevels().get(i) - 1));
							break;
						}
					}
					
					success = true;
					break;
				}
			}

			if (success == false)
			{
				cancelRpgDamage = true;
				return 0;
			}
		}

		else if (e.getDamager() instanceof Arrow)
		{
			// Set damage type to arrow
			damageType = 1;
			
			// Store the arrow.
			arrow = (Arrow) e.getDamager();
			
			// If the player shot the arrow, set damage to player stuff.
			if (arrow.getShooter() instanceof Player)
			{
				// Store player
				playerAttacker = (Player) arrow.getShooter();

				// Store the attacker
				rpgAttacker = FC_Rpg.rpgEntityManager.getRpgPlayer(playerAttacker);

				// Set damage of arrows.
				damage = rpgAttacker.getPlayerConfig().getAttack() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeAttack();
			}
			else
			{
				// Set the creature attacker.
				creatureAttacker = arrow.getShooter();

				// Get the shooter entity monster and store.
				rpgMobAttacker = FC_Rpg.rpgEntityManager.getRpgMonster(arrow.getShooter());
				
				// Set the damage to the monsters strength.
				if (rpgMobAttacker != null)
					damage = rpgMobAttacker.getAttack() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeAttack();
			}

			// Remove all arrows.
			arrow.remove();
		}
		
		else if (e.getDamager() instanceof LightningStrike)
		{
			// Initialize rpgMobAttacker;
			rpgMobAttacker = new RpgMonster();

			damage = rpgMobAttacker.getAttack() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeAttack();
		}
		
		// Melee player attacks
		else if (e.getDamager() instanceof Player)
		{
			playerAttacker = (Player) e.getDamager();
			
			// Store the attacker
			rpgAttacker = FC_Rpg.rpgEntityManager.getRpgPlayer(playerAttacker);
			
			if (rpgAttacker == null)
				return -1;

			// Get base damage.
			damage = rpgAttacker.getTotalAttack() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeAttack();
			
			// Add weapon Bonus
			damage = damage * FC_Rpg.battleCalculations.getWeaponModifier(playerAttacker.getItemInHand().getType(), rpgAttacker.getTotalAttack());
		}
		
		// If the entity is a living entity we want to store it.
		else if (e.getDamager() instanceof LivingEntity)
		{
			//Set creature attacker.
			creatureAttacker = (LivingEntity) e.getDamager();
			
			// Initialize rpgMobAttacker;
			rpgMobAttacker = FC_Rpg.rpgEntityManager.getRpgMonster(creatureAttacker);
			
			damage = rpgMobAttacker.getAttack() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeAttack();
			
			// Account for mob weapon type for damage.
			ItemStack mobWeapon = creatureAttacker.getEquipment().getItemInHand();
			
			if (mobWeapon != null)
				damage = damage * FC_Rpg.battleCalculations.getWeaponModifier(mobWeapon.getType(),999999);
		}
		
		// Else if not it's an error and return the monster strength.
		else
		{
			FC_Rpg.plugin.getLogger().info("Error: PrepareAttacker(), undefined entity type: " + e.getDamager().toString());
			
			// Initialize rpgMobAttacker;
			rpgMobAttacker = new RpgMonster();
			
			damage = rpgMobAttacker.getAttack() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeAttack();
		}

		// Cancel damage for stunned stuff.
		if (rpgAttacker != null)
		{
			// If disabled cancel attack
			if (rpgAttacker.getStatusActiveEntity(rpgAttacker.getPlayerConfig().getStatusDuration(EffectIDs.DISABLED)))
			{
				cancelRpgDamage = true;
				return 0;
			}
		}
		else if (rpgMobAttacker != null)
		{
			if (rpgMobAttacker.getStatusActiveEntity(rpgMobAttacker.getStatusDisabled()))
			{
				cancelRpgDamage = true;
				return 0;
			}
		}
		
		return damage;
	}

	private double getEnviromentalDamage(Player player)
	{
		// Variable declarations
		double damage = 0;
		DistanceModifierLib dmm = new DistanceModifierLib();
		int distanceModifier = dmm.getWorldDML(event.getEntity().getLocation());

		// No damage for creative players.
		if (player.getGameMode() == GameMode.CREATIVE)
			return 0;
		
		// Deal damage to the player based on the type of damage.
		if (event.getCause().equals(DamageCause.ENTITY_EXPLOSION))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageExplosion();
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(player, Enchantment.PROTECTION_EXPLOSIONS);
		}
		else if (event.getCause().equals(DamageCause.FALL))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageFall();
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(player, Enchantment.PROTECTION_FALL);
		}
		else if (event.getCause().equals(DamageCause.CONTACT))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageContact();
		}
		else if (event.getCause().equals(DamageCause.ENTITY_ATTACK))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageEntityAttack();
		}
		else if (event.getCause().equals(DamageCause.LIGHTNING))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageLightning();
		}
		else if (event.getCause().equals(DamageCause.FIRE))
		{
			if (hasFireResistance(player))
				return 0;
			
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageFire();
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(player, Enchantment.PROTECTION_FIRE);
			
			// 95% damage reduction from fire resistance.
			if (rpgDefender.getPlayer().getActivePotionEffects().contains(PotionEffectType.FIRE_RESISTANCE))
				damage = damage * .05;
		}
		else if (event.getCause().equals(DamageCause.FIRE_TICK))
		{
			if (hasFireResistance(player))
				return 0;
			
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageFireTick();
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(player, Enchantment.PROTECTION_FIRE);

			// 95% damage reduction from fire resistance.
			if (rpgDefender.getPlayer().getActivePotionEffects().contains(PotionEffectType.FIRE_RESISTANCE))
				damage = damage * .05;
		}
		else if (event.getCause().equals(DamageCause.LAVA))
		{
			if (hasFireResistance(player))
				return 0;
			
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageLava();
			damage = damage * FC_Rpg.battleCalculations.getArmorEnchantmentBonus(player, Enchantment.PROTECTION_FIRE);
			
			// 95% damage reduction from fire resistance.
			if (rpgDefender.getPlayer().getActivePotionEffects().contains(PotionEffectType.FIRE_RESISTANCE))
				damage = damage * .05;
		}
		else if (event.getCause().equals(DamageCause.STARVATION))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageStarvation();
		}
		else if (event.getCause().equals(DamageCause.POISON))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamagePoison();
		}
		else if (event.getCause().equals(DamageCause.MAGIC))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageMagic();
		}
		else if (event.getCause().equals(DamageCause.BLOCK_EXPLOSION))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageBlockExplosion();
		}
		else if (event.getCause().equals(DamageCause.WITHER))
		{
			damage = distanceModifier * FC_Rpg.balanceConfig.getDamageWither();
		}
		else if (event.getCause().equals(DamageCause.SUICIDE))
		{
			return 99999999;
		}
		else if (event.getCause().equals(DamageCause.VOID))
		{
			return 99999999;
		}
		
		if (damage < 1)
			damage = 1;
		
		return damage;
	}
	
	private boolean hasFireResistance(Player player)
	{
		for (PotionEffect pe : player.getActivePotionEffects())
		{
			if (pe.getType().equals(PotionEffectType.FIRE_RESISTANCE))
				return true;
		}
		
		return false;
	}
}

/*
 * ~ Berserker math. 1% hp lost = .4% bonus. 20/100 hp = 80% lost = .4 * 80 = 32% bonus. 33/66 hp = 50% lost 66/100 * curHealth/x bonus = 100 * curHealth / maxHealth * .4 bonus = 40 * curHealth/maxHealth
 */

