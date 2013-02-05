package me.Destro168.FC_Rpg.Spells;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Entities.EntityDamageManager;
import me.Destro168.FC_Rpg.Entities.RpgMonster;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
import me.Destro168.FC_Rpg.Util.MaterialLib;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpellCaster
{
	private LivingEntity target;
	private RpgPlayer rpgCaster;
	private RpgPlayer rpgDefender;
	private RpgMonster rpgMobDefender;
	
	private Player playerCaster;
	private Player playerDefender;
	
	private Spell spell;
	private String name;
	private double finalSpellMagnitude;
	private double damage;
	private int duration;
	private int spellNumber;
	private int damageType;
	private int spellTier;
	private double manaCost;
	private int radius;
	private boolean targetParty;
	
	public String getName() { return name; }
	public double getDamage() { return damage; }
	public double getManaCost() { return manaCost; }
	
	public SpellCaster() 
	{
		setDefaults();
	}
	
	private void setDefaults()
	{
		target = null;
		rpgCaster = null;
		playerCaster = null;
		playerDefender = null;
		spell = null;
		name = "";
		finalSpellMagnitude = 0;
		damage = 0;
		duration = 0;
		spellNumber = -1;
		damageType = 0;
		spellTier = -1;
		manaCost = -1D;
		radius = 0;
		targetParty = false;
	}
	
	public boolean init_spellCast(RpgPlayer spellCaster_, LivingEntity target_, double damage_, int damageType_)
	{
		rpgCaster = spellCaster_;	//Variable Initialization
		
		// If already casting, return.
		if (rpgCaster.getIsCasting() == true)
			return false;
		
		String activeSpell = rpgCaster.playerConfig.getActiveSpell();		//Variable Initialization
		
		//First check that the player has an active spell before continuing.
		if (activeSpell == null)
			return false; //Without one return.
		
		target = target_;	//Variable Initializations
		damage = damage_;
		damageType = damageType_;
		playerCaster = spellCaster_.getPlayer();
		
		MessageLib msgLib = new MessageLib(playerCaster);	//Variable Declarations
		List<Spell> spellBook = rpgCaster.playerConfig.getRpgClass().getSpellBook();
		
		// Evaluate the target.
		evaluateTarget();
		
		//Find the players currently active spell and store it's information.
		for (int i = 0; i < spellBook.size(); i++)
		{
			//If the players active spell is equal to the spells name, we found our spell!
			if (activeSpell.equals(spellBook.get(i).name))
			{
				//Store the spell info.
				spell = spellBook.get(i);
				spellNumber = i;
				break;
			}
		}
		
		if (spell == null)
			return false;
		
		try { if (spell.uncastable) { msgLib.standardError("This spell cannot be cast normally conditions."); return false; } } catch (NullPointerException e) { }
		
		//Assign Variables.
		spellTier = rpgCaster.playerConfig.getSpellLevels().get(spellNumber) - 1;
		
		//If the spell is restricted, then...
		if (spell.requiresTarget)
		{
			//If there is no target return failure.
			if (rpgMobDefender == null && rpgDefender == null)
				return false;
		}
		
		if (spell.classRestricted)
		{
			//Restricted spells also require a target.
			for (RpgClass rpgClass : FC_Rpg.classConfig.rpgClassList)
			{
				if (rpgClass.getID() == rpgCaster.playerConfig.getCombatClass())
				{
					//If the damage is an arrow, and has the restriction id 0, and if restricted spell, then return false.
					if (rpgClass.getRestrictionID() == 1 && damageType != 1)
					{
						msgLib.standardError("Please shoot arrows from a bow to cast this spell.");
						rpgCaster.playerConfig.resetActiveSpell();
						return false;
					}
					else if (rpgClass.getRestrictionID() == 2 && !(playerCaster.getItemInHand().getType() == Material.STICK || playerCaster.getItemInHand().getType() == Material.BLAZE_ROD))
					{
						msgLib.standardError("Please use a wand (stick) to cast this spell.");
						rpgCaster.playerConfig.resetActiveSpell();
						return false;
					}
				}
			}
		}
		
		//If no valid spell was cast, then return.
		if (spellNumber == -1)
		{
			FC_Rpg.plugin.getLogger().info("Invalid spell number. Fix your config yo.");
			return false;
		}
		
		//Attempt to drain mana. On fail return damage.
		if (rpgCaster.hasEnoughMana(spellNumber, spellTier) == false)
		{
			rpgCaster.attemptNoManaNotification(spellNumber, spellTier);
			return false;
		}
		
		//Reset active spell for caster.
		rpgCaster.playerConfig.resetActiveSpell();
		
		//Store spell information for use later.
		storeSpellInfo();
		
		//Update the final stat modifier.
		updatefinalSpellMagnitude(rpgCaster, spell, spellTier);
		
		// Apply spell as buff.
		if (duration > 0)
			applyBuff(spell.effectID);
		
		//Return success.
		return true;
	}
	
	// Handles casts for when you attack and have persistant buffs. (items, buffing spells)
	public void fastOffensiveCast(RpgPlayer spellCaster_, LivingEntity target_, double damage_, int effectID, int plusValue)
	{
		damage = damage_;
		target = target_;
		rpgCaster = spellCaster_;
		playerCaster = spellCaster_.getPlayer();
		
		// Setup target variables
		evaluateTarget();
		
		//Store spell information for use later.
		if (FC_Rpg.enchantmentConfig.getProcEnchantmentByID(effectID) == null)
			return;
		
		spell = FC_Rpg.enchantmentConfig.getProcEnchantmentByID(effectID).spell;
		spellTier = plusValue;
		
		// Store spell info for later use.
		storeSpellInfo();
		
		//Update the final stat modifier.
		updatefinalSpellMagnitude(rpgCaster, spell, spellTier);
		
		// Use spell effects.
		handleEffects();
	}
	
	// Handles casts for when you are attacked and have persistant buffs. (items, buffing spells)
	public void fastDefensiveCast(RpgPlayer spellCaster_, Spell spell_, int plusValue)
	{
		rpgCaster = spellCaster_;
		playerCaster = spellCaster_.getPlayer();
		
		// Setup target variables
		evaluateTarget();
		
		//Store spell information for use later.
		spellTier = plusValue;
		
		// Store spell.
		spell = spell_;
		
		// Store spell info for later use.
		storeSpellInfo();

		//Update the final stat modifier.
		updatefinalSpellMagnitude(rpgCaster, spell, spellTier);
		
		// Use spell effects.
		handleEffects();
	}
	
	public void evaluateTarget()
	{
		//If there is a target.
		if (target != null)
		{
			//Assign the variables based on the target.
			if (!(target instanceof Player))
				rpgMobDefender = FC_Rpg.rpgEntityManager.getRpgMonster(target);
			else
			{
				playerDefender = (Player) target;
				rpgDefender = FC_Rpg.rpgEntityManager.getRpgPlayer(playerDefender);
			}
		}
	}
	
	private void storeSpellInfo()
	{
		// Store name of spell
		name = spell.name;
		
		//Store spell information for use later.
		try { duration = spell.duration.get(spellTier); } catch (NullPointerException e) {  }
		try { manaCost = spell.manaCost.get(spellTier); } catch (NullPointerException e) {  }
		try { radius = spell.radius.get(spellTier); } catch (NullPointerException e) {  }
		try { targetParty = spell.targetParty; } catch (NullPointerException e) {  }
	}
	
	public boolean handleEffects()
	{
		//Variable Decalarations
		int x = spell.effectID;
		
		// Handle effects with custom requirements first.
		if (x == SpellEffect.TAUNT.getID())
		{
			effect_Taunt();
			applyBuff(x);
			return true;
		}
		else if (x == SpellEffect.FIRE_STRIKE.getID())
		{
			rpgCaster.playerConfig.setStatusUses(x, (int) finalSpellMagnitude);
			return true;
		}
		else if (x == SpellEffect.DISABLED.getID())
			effect_Disable();
		
		else if (x == SpellEffect.BLEED.getID())
			effect_Bleed();
		
		else if (x == SpellEffect.AOE.getID())
			effect_AoE();
		
		else if (x == SpellEffect.FROST_STRIKE_AOE.getID())
			effect_Frost_Strike(true);
		
		else if (x == SpellEffect.FROST_STRIKE.getID())
			effect_Frost_Strike(false);
		
		else if (x == SpellEffect.HEAL_SELF.getID())
			effect_Heal_Self();
		
		else if (x == SpellEffect.WEAKEN.getID())
			effect_Weaken();
		
		else if (x == SpellEffect.DISABLED.getID())
			effect_Disable();

		else if (x == SpellEffect.FIREBALL.getID())
			effect_Fireball();
		
		else if (x == SpellEffect.ALCHEMY.getID())
			return false;
		
		else if (x == SpellEffect.LIGHTNING.getID())
			effect_Lightning();
		
		else if (x == SpellEffect.HEAL_SELF_OR_OTHER.getID())
			effect_Heal_Self_Or_Other();
		
		else if (x == SpellEffect.BOOST_STATS.getID())
			effect_Boost_Stats();
		
		else if (x == SpellEffect.DAMAGE_SCALED_BY_MISSING_HEALTH.getID())
			effect_Damage_Scaled_By_Missing_Health();
		
		else if (x == SpellEffect.SACRIFICE_HEALTH_FOR_DAMAGE.getID())
			effect_Sacrifice_Health_For_Damage();
		
		else if (x == SpellEffect.SPEED.getID())
			effect_Speed();
		
		else if (x == SpellEffect.POISON.getID())
			effect_Poison();
		
		else if (x == SpellEffect.HEAL_OTHER.getID())
			effect_Heal_Other();
		
		else if (x == SpellEffect.HEAL_SELF_PERCENT.getID())
			effect_Heal_Self_Percent();
		
		else if (x == SpellEffect.HEAL_SELF_PERCENT.getID())
			effect_Heal_Self_Percent();
		
		else
		{
			rpgCaster.playerConfig.setStatusUses(x, 1);
			return true;
		}
		
		return true;
	}
	
	public double updatefinalSpellMagnitude(RpgPlayer rpgCaster, Spell spell, int spellTier)
	{
		double constantMagnitude = 0;
		double am = 0;
		double cm = 0;
		double mm = 0;
		double im = 0;
		
		try { constantMagnitude = spell.constantMagnitude.get(spellTier); } catch (NullPointerException e) {  }
		try { am = spell.attackMagnitude.get(spellTier); } catch (NullPointerException e) {  }
		try { cm = spell.constitutionMagnitude.get(spellTier); } catch (NullPointerException e) {  }
		try { mm = spell.magicMagnitude.get(spellTier); } catch (NullPointerException e) {  }
		try { im = spell.intelligenceMagnitude.get(spellTier); } catch (NullPointerException e) {  }
		
		if (constantMagnitude > 0)
			finalSpellMagnitude = constantMagnitude;
		
		if (am > 0)
			finalSpellMagnitude += rpgCaster.getTotalAttack() * am;
		
		if (cm > 0)
			finalSpellMagnitude += rpgCaster.getTotalConstitution() * cm;
		
		if (mm > 0)
			finalSpellMagnitude += rpgCaster.getTotalMagic() * mm;
		
		if (im > 0)
			finalSpellMagnitude += rpgCaster.getTotalIntelligence() * im;
		
		return finalSpellMagnitude;
	}
	
	 // Apply buffs for enchants and spell casts
	public void applyBuff(final int effectID)
	{
		// Variable Declaration(s)
		List<RpgPlayer> playerConfigs = new ArrayList<RpgPlayer>();
		
		// If it has a radius and targets party, then
		if (radius > 0 && targetParty == true)
		{
			// Give them the buff.
			for (RpgPlayer partyMember : FC_Rpg.rpgEntityManager.getNearbyPartiedRpgPlayers(playerCaster, radius))
				playerConfigs.add(partyMember);
		}
		else
			playerConfigs.add(rpgCaster);
		
		//Return if there are no configs, then return.
		if (playerConfigs.size() == 0)
			return;
		
		// Set buff information on each player.
		for (final RpgPlayer p : playerConfigs)
		{
			//Give buff to only caster.
			p.playerConfig.setStatusDuration(effectID, duration);
			p.playerConfig.setStatusMagnitude(effectID, finalSpellMagnitude);
			p.playerConfig.setStatusTier(effectID, spellTier);
			
			// Cancel past buff related timers.
			if (FC_Rpg.playerBuffTimerTIDs.containsKey(p.getPlayer()))
				Bukkit.getScheduler().cancelTask(FC_Rpg.playerBuffTimerTIDs.get(p.getPlayer()));

			final MessageLib msgLib = new MessageLib(p.getPlayer());
			msgLib.infiniteMessage("Spell Effect: ",MaterialLib.getCleanName(SpellEffect.getSpellEffectName(effectID))," Has Activated.");
			
			// Create a new one.
			FC_Rpg.playerBuffTimerTIDs.put(p.getPlayer(), Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					msgLib.infiniteMessage("Spell Effect: ",MaterialLib.getCleanName(SpellEffect.getSpellEffectName(effectID))," Has Been Used Up.");
				}
			}, (int) (duration * .02)));
		}
	}
	
	private void effect_Speed()
	{
		//Adjust player move speed.
		playerCaster.setWalkSpeed((float) finalSpellMagnitude);
		playerCaster.setFlySpeed((float) finalSpellMagnitude);
		
		int speedTID = FC_Rpg.rpgEntityManager.getRpgPlayer(playerCaster).getSpeedTID();
		
		//Prevent overlapping tasks to reset players speed.
		if (speedTID > -1)
		{
			Bukkit.getServer().getScheduler().cancelTask(speedTID);
			FC_Rpg.rpgEntityManager.getRpgPlayer(playerCaster).setSpeedTID(-1);
		}
		
		//Revert after duration.
		int tid = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				//Adjust player move speed.
				playerCaster.setWalkSpeed((float) .2);
				playerCaster.setFlySpeed((float) .2);
				
				//Remove player from map.
				FC_Rpg.rpgEntityManager.getRpgPlayer(playerCaster).setSpeedTID(-1);
			}
		}, duration * 20);
		
		//Remove player from map.
		FC_Rpg.rpgEntityManager.getRpgPlayer(playerCaster).setSpeedTID(tid);
	}
	
	private void effect_Taunt()
	{
		//Taunt nearby mobs.
		for (Entity entity : playerCaster.getNearbyEntities(12, 12, 12))
		{
			if (entity instanceof LivingEntity)
			{
				if (!(entity instanceof Player))
					entity.teleport(playerCaster.getLocation());
			}
			
			if (entity instanceof Creature)
			{
				((Creature) entity).setTarget(playerCaster);
			}
		}
	}
	
	private void effect_Fireball()
	{
		//Create a fireball
		Fireball fireball = playerCaster.launchProjectile(Fireball.class);
		
		//Set the shooter to player attacker.
		fireball.setShooter(playerCaster);
		
		//Don't create fire.
		fireball.setIsIncendiary(false);
		
		fireball.setYield(2F);
		
		//Create and add the fireball to the players ownership
		rpgCaster.summon_Add(fireball, 40);
	}
	
	private void effect_Bleed()
	{
		final EntityDamageManager edm = new EntityDamageManager();
		final int length = (int) finalSpellMagnitude;
		
		if (rpgMobDefender != null)
		{
			final RpgMonster finalMobDefender = rpgMobDefender;
			final double finalDamage = rpgMobDefender.getMaxHealth() * .02;
			
			for (int i = 0; i < length; i++)
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackMobDefender(finalMobDefender, rpgCaster, finalDamage, damageType);
						}
					}
				}, i * 20);
			}
		}
		
		if (target instanceof Player)
		{
			final RpgPlayer finalPlayerDefender = FC_Rpg.rpgEntityManager.getRpgPlayer((Player) target);
			final double finalDamage = rpgDefender.playerConfig.maxHealth * .02;
			
			for (int i = 0; i < length; i++)
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackPlayerDefender(rpgCaster, finalPlayerDefender, null, finalDamage, 3, finalPlayerDefender.getPlayer().getName());
						}
					}
				}, i * 20);
			}
		}
	}
	
	private void effect_AoE()
	{
		//Disable spell from being cast further.
		rpgCaster.switchIsCasting();
		
		for (Entity entity : playerCaster.getNearbyEntities(finalSpellMagnitude, finalSpellMagnitude, finalSpellMagnitude))
		{
			if (entity instanceof LivingEntity)
			{
				((LivingEntity) entity).damage(1, playerCaster);
			}
		}
		
		//Allow spell to be cast again.
		rpgCaster.switchIsCasting();
	}
	
	private void effect_Poison()
	{
		final EntityDamageManager edm = new EntityDamageManager();
		final double finalDamage = damage * finalSpellMagnitude;
		
		if (rpgMobDefender != null)
		{
			final RpgMonster finalMobDefender = rpgMobDefender;
			
			for (int i = 0; i < 5; i++)
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackMobDefender(finalMobDefender, rpgCaster, finalDamage, damageType);
						}
					}
				}, i * 20 + 20);
			}
		}
		else if (target instanceof Player)
		{
			final RpgPlayer finalPlayerDefender = FC_Rpg.rpgEntityManager.getRpgPlayer((Player) target);
			
			for (int i = 0; i < 5; i++)
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackPlayerDefender(rpgCaster, finalPlayerDefender, null, finalDamage, 3, finalPlayerDefender.getPlayer().getName());
						}
					}
				}, i * 20 + 20);
			}
		}
	}
	
	private void effect_Frost_Strike(boolean isAOE)
	{
		PotionEffect pe;
		
		if (isAOE == true)
		{
			pe = new PotionEffect(PotionEffectType.SLOW, duration, 5);
			
			for (Entity entity : rpgCaster.getPlayer().getNearbyEntities(finalSpellMagnitude, finalSpellMagnitude, finalSpellMagnitude))
			{
				/*
				if (entity instanceof Player)
				{
					Player player = (Player) entity;
					
					if (FC_Rpg.playerSlowTimerTIDs.containsKey(player))
						Bukkit.getScheduler().cancelTask(FC_Rpg.playerSlowTimerTIDs.get(player));
					
					playerDefender.setWalkSpeed(0.0F);
					
					FC_Rpg.playerSlowTimerTIDs.put(player, Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
					{
						@Override
						public void run()
						{
							playerDefender.setWalkSpeed(0.2F);
						}
					}, duration));
				}
				else 
				*/
				
				if (entity instanceof LivingEntity)
				{
					((LivingEntity) entity).removePotionEffect(PotionEffectType.SLOW);
					((LivingEntity) entity).addPotionEffect(pe);
				}
			}
		}
		else
		{
			pe = new PotionEffect(PotionEffectType.SLOW, duration, (int) finalSpellMagnitude);
			
			target.removePotionEffect(PotionEffectType.SLOW);
			target.addPotionEffect(pe);
		}
	}
	
	private void effect_Heal_Self()
	{
		double healAmount = finalSpellMagnitude;
		
		rpgCaster.healHealth(healAmount);
		rpgCaster.attemptHealthHealSelfNotification(healAmount);
	}
	
	private boolean effect_Weaken()
	{
		if (rpgMobDefender != null)
		{
			//Never debuff bosses.
			if (rpgMobDefender.getIsBoss())
				return false;
			
			//Prevent multiple debuffs.
			if (rpgMobDefender.getIsWeak())
				return false;
			
			rpgMobDefender.setConstitution((int) (rpgMobDefender.getConstitution() * (1 - finalSpellMagnitude)));
			rpgMobDefender.setAttack((int) (rpgMobDefender.getAttack() * (1 - finalSpellMagnitude)));
			rpgMobDefender.setCurHealth((int) (rpgMobDefender.getCurHealth() * (1 - finalSpellMagnitude)));
			rpgMobDefender.setMaxHealth((int) (rpgMobDefender.getMaxHealth() * (1 - finalSpellMagnitude)));
			
			//Set the monster to weak so it can't be debuffed in the future.
			rpgMobDefender.setIsWeak(true);
		}
		
		return true;
	}
	
	private boolean effect_Disable()
	{
		//Bosses are immune to disables.
		if (rpgMobDefender.getIsBoss())
			return false;
		
		if (rpgMobDefender != null)
			rpgMobDefender.setStatusDisabled((int) finalSpellMagnitude);
		
		else if (playerDefender != null)
			rpgDefender.playerConfig.setStatusMagnitude(SpellEffect.DISABLED.getID(), finalSpellMagnitude);
		
		return true;
	}
	
	private void effect_Damage_Scaled_By_Missing_Health()
	{
		damage = rpgCaster.getTotalAttack() * rpgCaster.getMissingHealthDecimal() * finalSpellMagnitude;
	}
	
	private void effect_Sacrifice_Health_For_Damage()
	{
		double hpToLose = rpgCaster.playerConfig.curHealth * .2;
		rpgCaster.dealDamage(hpToLose);
		damage = rpgCaster.getTotalAttack() * finalSpellMagnitude;
	}
	
	private void effect_Heal_Self_Or_Other()
	{
		if (playerDefender != null)
		{
			rpgDefender.healHealth(finalSpellMagnitude);
			rpgCaster.attemptHealOtherNotification(rpgDefender);
		}
		else
		{
			rpgCaster.healHealth(finalSpellMagnitude);
			rpgCaster.attemptHealthHealSelfNotification(rpgCaster.playerConfig.maxHealth * finalSpellMagnitude);
		}
	}
	
	private void effect_Heal_Other()
	{
		if (playerDefender != null)
		{
			rpgDefender.healHealth(finalSpellMagnitude);
			rpgCaster.attemptHealOtherNotification(rpgDefender);
		}
	}
	
	private void effect_Heal_Self_Percent()
	{
		double healAmount = rpgCaster.playerConfig.maxHealth * finalSpellMagnitude;
		
		rpgCaster.healHealth(healAmount);
		rpgCaster.attemptHealthHealSelfNotification(healAmount);
	}
	
	private void effect_Boost_Stats()
	{
		if (playerDefender != null)
			rpgDefender.addSupportBuff(finalSpellMagnitude);
		else if (rpgCaster != null)
			rpgCaster.addSupportBuff(finalSpellMagnitude);
	}
	
	private void effect_Lightning()
	{
		playerCaster.getWorld().strikeLightningEffect(target.getLocation());
		damage = finalSpellMagnitude;
	}
}


/*
Okoll1's amazing list of alchemy items.

>1:Dead Shrub.Rotten Flesh.Stick.Live Shrub.Tall Grass.Air
>2:Apple.Torch.Wood.Redwood
>3:Melon.Ink Sack..Bread.Charcoal.Birchwood
>4:Raw Chicken.Raw Beef.Shears.Raw Fish.Raw Porkchop.Coal.Sapling
>5:Book and Quill.Glass Bottle.Steak.Cooked Chicken.Cooked Porkchop
>6:Cauldron.Brewing Stand.Bed.Cooked Fish..Flint.Feather.Arrow.Paper.White Wool
>7:Melon Seeds.Pumpkin Seeds.Cookie.Clay Balls.Leather.Wheat.Bow.Birch Sapling
>8:Redstone Repeater.Sugar.Bone.Bone Meal.Lapis Lazuli.Redstone.Melon Block
>9:Map.Cake.Coco Beans.Glowstone Dust.Clock.Sugarcane.Wheat Seeds.Brown Mushroom
>10:Fishing Rod.Compass.String.Red Mushroom.Book.
>11:Eye of Ender.Egg.Slimeball.Web
>12:Saddle.Minecart.Cactus.Ladder.Bookshelf.
>13:Fire Charge.Bucket.Sulphur.Cracked Stone Brick.Mossy Stone Brick
>14:Glowstone.Soul Sand.Rails.Piston.Dirt
>15:Netherrack.Iron Block.Sand.
>16:Map.Mossy Cobblestone.Sandstone
>17:Obsidian.TNT.Grass
>18:Diamond.Enchantment Table.
>19:Emerald.Diamond Block
>20:Golden Apple.Emerald Block.Gold Block 
*/


















