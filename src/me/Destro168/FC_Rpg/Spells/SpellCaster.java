package me.Destro168.FC_Rpg.Spells;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Entities.EntityDamageManager;
import me.Destro168.FC_Rpg.Entities.RpgMonster;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
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
	private EffectIDs e;
	
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
	private int combatClass;
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
		e = new EffectIDs();
		target = null;
		rpgCaster = null;
		playerCaster = null;
		playerDefender = null;
		spell = null;
		name = "";
		finalSpellMagnitude = 0;
		damage = 0;
		duration = 0;
		combatClass = 0;
		spellNumber = -1;
		damageType = 0;
		spellTier = -1;
		manaCost = -1D;
		radius = 0;
		targetParty = false;
	}
	
	public boolean init_spellCast(RpgPlayer spellCaster_, LivingEntity target_, double damage_, int damageType_)
	{
		//Variable Initialization
		rpgCaster = spellCaster_;
		String activeSpell = rpgCaster.getPlayerConfig().getActiveSpell();
		
		//First check that the player has an active spell before continuing.
		if (activeSpell == null)
			return false; //Without one return.
		
		//Variable Initializations
		target = target_;
		damage = damage_;
		damageType = damageType_;
		playerCaster = spellCaster_.getPlayer();
		combatClass = rpgCaster.getPlayerConfig().getCombatClass();
		
		//Variable Declarations
		MessageLib msgLib = new MessageLib(playerCaster);
		List<Spell> spellBook = rpgCaster.getPlayerConfig().getRpgClass().getSpellBook();
		
		if (rpgCaster.getIsCasting() == true)
			return false;
		
		//If there is a target.
		if (target != null)
		{
			//Assign the variables based on the target.
			if (!(target_ instanceof Player))
				rpgMobDefender = FC_Rpg.rpgEntityManager.getRpgMonster(target_);
			else
				rpgDefender = FC_Rpg.rpgEntityManager.getRpgPlayer((Player) target_);
		}
		
		//Find the players currently active spell and store it's information.
		for (int i = 0; i < spellBook.size(); i++)
		{
			//If the players active spell is equal to the spells name, we found our spell!
			if (activeSpell.equals(spellBook.get(i).getName()))
			{
				//Store the spell info.
				spell = spellBook.get(i);
				spellNumber = i;
				break;
			}
		}
		
		if (spell == null)
			return false;
		
		try { if (spell.getUncastable()) { msgLib.standardError("This spell cannot be cast normally conditions."); return false; } } catch (NullPointerException e) { }
		
		//Assign Variables.
		spellTier = rpgCaster.getPlayerConfig().getSpellLevels().get(spellNumber) - 1;
		
		//If the spell is restricted, then...
		if (spell.getRequiresTarget())
		{
			//If there is no target return failure.
			if (rpgMobDefender == null && rpgDefender == null)
				return false;
		}
		
		if (spell.getIsClassRestricted())
		{
			//Restricted spells also require a target.
			for (RpgClass rpgClass : FC_Rpg.classConfig.getRpgClasses())
			{
				if (rpgClass.getID() == combatClass)
				{
					//If the damage is an arrow, and has the restriction id 0, and if restricted spell, then return false.
					if (rpgClass.getRestrictionID() == 1 && damageType != 1)
					{
						msgLib.standardError("Please shoot arrows from a bow to cast this spell.");
						return false;
					}
					else if (rpgClass.getRestrictionID() == 2 && !(playerCaster.getItemInHand().getType() == Material.STICK || playerCaster.getItemInHand().getType() == Material.BLAZE_ROD))
					{
						msgLib.standardError("Please use a wand (stick) to cast this spell.");
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
		rpgCaster.getPlayerConfig().resetActiveSpell();
		
		//Store spell information for use later.
		try { duration = spell.getDuration().get(spellTier); } catch (NullPointerException e) {  }
		try { manaCost = spell.getManaCost().get(spellTier); } catch (NullPointerException e) {  }
		try { radius = spell.getRadius().get(spellTier); } catch (NullPointerException e) {  }
		try { targetParty = spell.getTargetParty(); } catch (NullPointerException e) {  }
		
		name = spell.getName();
		
		//Return success.
		return true;
	}
	
	public boolean handleEffects()
	{
		//Variable Decalarations
		int x = spell.getEffectID();
		
		//Update the final stat modifier.
		updatefinalSpellMagnitude(rpgCaster, spell, spellTier);
		
		if (e.getIsBuff(x) == true)
			applyBuff(x);
		
		else if (x == EffectIDs.TAUNT)
		{
			effect_Taunt();
			applyBuff(x);
		}
		
		else if (x == EffectIDs.FIRE_ARROW)
			rpgCaster.getPlayerConfig().setStatusUses(x, (int) finalSpellMagnitude);
		
		else if (x == EffectIDs.DAMAGE_BOOST)
			damage = damage * finalSpellMagnitude;
		
		else if (x == EffectIDs.DISABLED)
			effect_Disable();
		
		else if (x == EffectIDs.BLEED)
			effect_Bleed();

		else if (x == EffectIDs.AOE)
			effect_AoE();
		
		else if (x == EffectIDs.FROST_ARROW)
			effect_Frost_Arrow();
		
		else if (x == EffectIDs.HEAL_SELF)
			effect_Heal_Self();
		
		else if (x == EffectIDs.WEAKEN)
			effect_Weaken();
		
		else if (x == EffectIDs.DISABLED)
			effect_Disable();

		else if (x == EffectIDs.FIREBALL)
			effect_Fireball();
		
		else if (x == EffectIDs.ALCHEMY)
			return false;
		
		else if (x == EffectIDs.LIGHTNING)
			effect_Lightning();
		
		else if (x == EffectIDs.HEAL_SELF_OR_OTHER)
			effect_Heal_Self_Or_Other();
		
		else if (x == EffectIDs.BOOST_STATS)
			effect_Boost_Stats();
		
		else if (x == EffectIDs.LIFESTEAL)
			applyBuff(x);
		
		else if (x == EffectIDs.IMMORTAL)
			applyBuff(x);
		
		else if (x == EffectIDs.TELEPORT_STRIKE)
			applyBuff(x);
		
		else if (x == EffectIDs.DAMAGE_BY_MISSING_HEALTH)
			effect_Damage_By_Missing_Health();
		
		else if (x == EffectIDs.SACRIFICE_HEALTH_FOR_DAMAGE)
			effect_Sacrifice_Health_For_Damage();
		
		else if (x == EffectIDs.SPEED)
			effect_Speed();
		
		else if (x == EffectIDs.POISON)
			effect_Poison();
		
		else if (x == EffectIDs.HEAL_OTHER)
			effect_Heal_Other();
		
		else if (x == EffectIDs.HEAL_SELF_PERCENT)
			effect_Heal_Self_Percent();
		
		return true;
	}
	
	public double updatefinalSpellMagnitude(RpgPlayer rpgCaster, Spell spell, int spellTier)
	{
		double constantMagnitude = 0;
		double am = 0;
		double cm = 0;
		double mm = 0;
		double im = 0;
		
		try { constantMagnitude = spell.getConstantMagnitude().get(spellTier); } catch (NullPointerException e) {  }
		try { am = spell.getAttackMagnitude().get(spellTier); } catch (NullPointerException e) {  }
		try { cm = spell.getConstitutionMagnitude().get(spellTier); } catch (NullPointerException e) {  }
		try { mm = spell.getMagicMagnitude().get(spellTier); } catch (NullPointerException e) {  }
		try { im = spell.getIntelligenceMagnitude().get(spellTier); } catch (NullPointerException e) {  }
		
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
	
	//	If the spell has the requirements to target an ally, give the buff to allies. Else solely give to caster.
	private void applyBuff(int effectID)
	{
		List<RpgPlayer> playerConfigs = new ArrayList<RpgPlayer>();
		
		//We can give dodge to allies only.
		if (radius > 0 && targetParty == true)
		{
			//For all friendly party members
			for (RpgPlayer partyMember : FC_Rpg.rpgEntityManager.getNearbyPartiedRpgPlayers(playerCaster, radius))
				playerConfigs.add(partyMember);
		}
		else
			playerConfigs.add(rpgCaster);
		
		//Return if there are no configs.
		if (playerConfigs.size() == 0)
			return;
		
		//For each player we set the buff duration and magnitude.
		for (final RpgPlayer p : playerConfigs)
		{
			//Give buff to only caster.
			p.getPlayerConfig().setStatusDuration(effectID, duration);
			p.getPlayerConfig().setStatusMagnitude(effectID, finalSpellMagnitude);
			
			if (FC_Rpg.playerBuffTimerTIDs.containsKey(p.getPlayer()))
				Bukkit.getScheduler().cancelTask(FC_Rpg.playerBuffTimerTIDs.get(p.getPlayer()));
			
			FC_Rpg.playerBuffTimerTIDs.put(p.getPlayer(), Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					MessageLib msgLib = new MessageLib(p.getPlayer());
					msgLib.standardMessage("A Buff Has Expired And Been Removed.");
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
			final double finalDamage = rpgDefender.getMaxHealth() * .02;
			
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
	
	private void effect_Frost_Arrow()
	{
		PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, duration, 5);
		
		for (Entity entity : rpgCaster.getPlayer().getNearbyEntities(finalSpellMagnitude, finalSpellMagnitude, finalSpellMagnitude))
		{
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
			else if (entity instanceof LivingEntity)
			{
				((LivingEntity) entity).removePotionEffect(PotionEffectType.SLOW);
				((LivingEntity) entity).addPotionEffect(pe);
			}
		}
	}
	
	private void effect_Heal_Self()
	{
		double healAmount = finalSpellMagnitude;
		
		rpgCaster.healHealth(healAmount);
		rpgCaster.attemptHealSelfNotification(healAmount);
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
			rpgDefender.getPlayerConfig().setStatusMagnitude(EffectIDs.DISABLED, finalSpellMagnitude);
		
		return true;
	}
	
	private void effect_Damage_By_Missing_Health()
	{
		damage = rpgCaster.getTotalAttack() * rpgCaster.getMissingHealthDecimal() * finalSpellMagnitude;
	}
	
	private void effect_Sacrifice_Health_For_Damage()
	{
		double hpToLose = rpgCaster.getCurHealth() * .2;
		rpgCaster.dealDamage(hpToLose);
		damage = rpgCaster.getTotalAttack() * finalSpellMagnitude;
	}
	
	private void effect_Heal_Self_Or_Other()
	{
		if (playerDefender != null)
		{
			rpgDefender.healHealth(rpgDefender.getMaxHealth() * finalSpellMagnitude);
			rpgCaster.attemptHealOtherNotification(rpgDefender);
		}
		else
		{
			rpgCaster.healHealth(rpgCaster.getMaxHealth() * finalSpellMagnitude);
			rpgCaster.attemptHealSelfNotification(rpgCaster.getMaxHealth() * finalSpellMagnitude);
		}
	}
	
	private void effect_Heal_Other()
	{
		if (playerDefender != null)
		{
			rpgDefender.healHealth(rpgDefender.getMaxHealth() * finalSpellMagnitude);
			rpgCaster.attemptHealOtherNotification(rpgDefender);
		}
	}
	
	private void effect_Heal_Self_Percent()
	{
		double healAmount = rpgCaster.getMaxHealth() * finalSpellMagnitude;
		
		rpgCaster.healHealth(healAmount);
		rpgCaster.attemptHealSelfNotification(healAmount);
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


















