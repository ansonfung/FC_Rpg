package me.Destro168.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.Destro168.Entities.EntityDamageManager;
import me.Destro168.Entities.EntityLocationLib;
import me.Destro168.Entities.RpgMonster;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Util.MaterialLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpellCaster 
{
	private EntityLocationLib ell;
	private EffectIDs e;
	
	private LivingEntity target;
	private RpgPlayer rpgCaster;
	private RpgPlayer rpgDefender;
	private RpgMonster rpgMobDefender;
	
	private Player playerCaster;
	private Player playerDefender;
	
	private Spell spell;
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
	
	public double getDamage() { return damage; }
	public double getManaCost() { return manaCost; }
	
	public SpellCaster() 
	{
		setDefaults();
	}
	
	private void setDefaults()
	{
		ell = new EntityLocationLib();
		e = new EffectIDs();
		target = null;
		rpgCaster = null;
		playerCaster = null;
		playerDefender = null;
		spell = null;
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
		//Variable Initializations
		target = target_;
		rpgCaster = spellCaster_;
		damage = damage_;
		damageType = damageType_;
		playerCaster = spellCaster_.getPlayer();
		combatClass = rpgCaster.getPlayerConfigFile().getCombatClass();
		
		//Variable Declarations
		String activeSpell = rpgCaster.getPlayerConfigFile().getActiveSpell();
		MessageLib msgLib = new MessageLib(playerCaster);
		List<Spell> spellBook = rpgCaster.getPlayerConfigFile().getRpgClass().getSpellBook();
		
		//If the player has no active spell return.
		if (activeSpell == null)
			return false;
		
		//If there is a target.
		if (target != null)
		{
			//Assign the variables based on the target.
			if (!(target_ instanceof Player))
				rpgMobDefender = FC_Rpg.rpgManager.getRpgMonster(target_);
			else
				rpgDefender = FC_Rpg.rpgManager.getRpgPlayer((Player) target_);
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
		
		//Assign Variables.
		spellTier = rpgCaster.getPlayerConfigFile().getSpellLevel(spellNumber) - 1;
		
		//If the spell is restricted, then...
		if (spell.getRestricted())
		{
			//If there is no target return failure.
			if (rpgMobDefender == null && rpgDefender == null)
				return false;
			
			//Restricted spells also require a target.
			for (RpgClass rpgClass : FC_Rpg.classConfig.getRpgClasses())
			{
				if (rpgClass.getName().equals(combatClass))
				{
					//If the damage is an arrow, and has the restriction id 0, and if restricted spell, then return false.
					if (rpgClass.getRestrictionID() == 0 && damageType != 1)
					{
						msgLib.standardMessage("Please shoot arrows from a bow to cast spells.");
						return false;
					}
					else if (rpgClass.getRestrictionID() == 1 && !(playerCaster.getItemInHand().getType() == Material.STICK))
					{
						msgLib.standardMessage("Please use a wand (stick) to cast spells.");
						return false;
					}
				}
			}
		}
		
		//If no valid spell was cast, then return.
		if (spellNumber == -1)
		{
			FC_Rpg.plugin.getLogger().info("Invalid spell number");
			return false;
		}
		
		//Attempt to drain mana. On fail return damage.
		if (rpgCaster.hasEnoughMana(spellNumber, spellTier) == false)
		{
			rpgCaster.attemptNoManaNotification(spellNumber, spellTier);
			return false;
		}
		
		//Reset active spell for caster.
		rpgCaster.getPlayerConfigFile().resetActiveSpell();
		
		//Store spell information for use later.
		try { duration = spell.getDuration().get(spellTier); } catch (NullPointerException e) {  }
		try { manaCost = spell.getManaCost().get(spellTier); } catch (NullPointerException e) {  }
		try { radius = spell.getRadius().get(spellTier); } catch (NullPointerException e) {  }
		try { targetParty = spell.getTargetParty(); } catch (NullPointerException e) {  }
		
		//Tell the caster they cast the spell.
		rpgCaster.attemptCastNotification(spell.getName());
				
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
			rpgCaster.getPlayerConfigFile().setStatusUses(x, (int) finalSpellMagnitude);
		
		else if (x == EffectIDs.DAMAGE_BOOST)
			damage = damage * finalSpellMagnitude;
		
		else if (x == EffectIDs.DISABLED)
			effect_Disable();
		
		else if (x == EffectIDs.BLEED)
			effect_Bleed();

		else if (x == EffectIDs.AOE)
			effect_AoE();

		else if (x == EffectIDs.BACKSTAB)
			effect_Backstab();
		
		else if (x == EffectIDs.HEAL_SELF)
			effect_Heal_Self();
		
		else if (x == EffectIDs.WEAKEN)
			effect_Weaken();
		
		else if (x == EffectIDs.DISABLED)
			effect_Disable();

		else if (x == EffectIDs.FIREBALL)
			effect_Fireball();
		
		else if (x == EffectIDs.ALCHEMY)
			effect_Alchemy();
		
		else if (x == EffectIDs.LIGHTNING)
			effect_Lightning();
		
		else if (x == EffectIDs.HEAL_OTHER)
			effect_Heal_Other();
		
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
		
		else if (x == EffectIDs.SPEED_POTION)
			effect_Potion_Speed();
		
		else if (x == EffectIDs.POISON)
			effect_Poison();
		
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
		//We can give dodge to allies only.
		if (radius > 0 && targetParty == true)
		{
			//For all friendly party members
			for (RpgPlayer partyMember : FC_Rpg.rpgManager.getNearbyPartiedRpgPlayers(playerCaster, radius))
			{
				partyMember.getPlayerConfigFile().setStatusDuration(effectID, duration);
				partyMember.getPlayerConfigFile().setStatusMagnitude(effectID, finalSpellMagnitude);
			}
		}
		else
		{
			//Give buff to only caster.
			rpgCaster.getPlayerConfigFile().setStatusDuration(effectID, duration);
			rpgCaster.getPlayerConfigFile().setStatusMagnitude(effectID, finalSpellMagnitude);
		}
	}
	
	private void effect_Potion_Speed()
	{
		//Create a potion effect and apply to the attacker.
		playerCaster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, (int) finalSpellMagnitude));
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
				Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackMobDefender(finalMobDefender, rpgCaster, finalDamage);
						}
					}
				}, i * 20);
			}
		}
		
		if (target instanceof Player)
		{
			final RpgPlayer finalPlayerDefender = FC_Rpg.rpgManager.getRpgPlayer((Player) target);
			final double finalDamage = rpgDefender.getMaxHealth() * .02;
			
			for (int i = 0; i < length; i++)
			{
				Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackPlayerDefender(rpgCaster, finalPlayerDefender, null, finalDamage);
						}
					}
				}, i * 20);
			}
		}
	}
	
	private void effect_AoE()
	{
		for (Entity entity : playerCaster.getNearbyEntities(finalSpellMagnitude, finalSpellMagnitude, finalSpellMagnitude))
		{
			if (entity instanceof LivingEntity)
			{
				((LivingEntity) entity).damage(1, playerCaster);
			}
		}
	}
	
	private void effect_Poison()
	{
		final EntityDamageManager edm = new EntityDamageManager();
		final double finalDamage = damage * finalSpellMagnitude;
		
		if (rpgMobDefender != null)
		{
			final RpgMonster finalMobDefender = rpgMobDefender;
			
			for (int i = 0; i < finalSpellMagnitude; i++)
			{
				Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackMobDefender(finalMobDefender, rpgCaster, finalDamage);
						}
					}
				}, i * 20 + 40);
			}
		}
		else if (target instanceof Player)
		{
			final RpgPlayer finalPlayerDefender = FC_Rpg.rpgManager.getRpgPlayer((Player) target);
			
			for (int i = 0; i < finalSpellMagnitude; i++)
			{
				Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					public void run()
					{
						if (rpgMobDefender != null)
						{
							edm.attackPlayerDefender(rpgCaster, finalPlayerDefender, null, finalDamage);
						}
					}
				}, i * 20 + 40);
			}
		}
	}
	
	private void effect_Backstab()
	{
		Location behindLoc = ell.getLocationBehindEntity(target.getLocation());
		double x = playerCaster.getLocation().getX();
		double z = playerCaster.getLocation().getZ();
		boolean behindX = false;
		boolean behindZ = false;
		
		//Make sure it's in range
		if (x > behindLoc.getX() - 1 && x < behindLoc.getX() + 1)
			behindX = true;
		
		if (z > behindLoc.getZ() - 1 && z < behindLoc.getZ() + 1)
			behindZ = true;
		
		//TODO - remove
		//FC_Rpg.plugin.getLogger().info("player:" + x + "," + z + "  behind: " + behindLoc.getX() + "," + behindLoc.getZ());
		
		if (behindX == true && behindZ == true)
			damage = damage * finalSpellMagnitude;
	}
	
	private void effect_Heal_Self()
	{
		double healAmount = rpgCaster.getMaxHealth() * finalSpellMagnitude;
		
		rpgCaster.heal(healAmount);
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
			rpgMobDefender.setStrength((int) (rpgMobDefender.getStrength() * (1 - finalSpellMagnitude)));
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
			rpgDefender.getPlayerConfigFile().setStatusMagnitude(EffectIDs.DISABLED, finalSpellMagnitude);
		
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
	
	private void effect_Heal_Other()
	{
		if (playerDefender != null)
		{
			rpgDefender.heal(rpgDefender.getMaxHealth() * finalSpellMagnitude);
			rpgCaster.attemptHealOtherNotification(rpgDefender);
		}
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
		damage = damage * finalSpellMagnitude;
	}
	
	private boolean effect_Alchemy()
	{
		MaterialLib ml = new MaterialLib();
		ItemStack item = rpgCaster.getPlayer().getItemInHand();
		int quality = 0;
		int enchantmentStrength = 0;
		
		if (item != null)
		{
			if (ml.t0.contains(item))
				quality = 1;
			else if (ml.t1.contains(item))
				quality = 2;
			else if (ml.t2.contains(item))
				quality = 3;
			else if (ml.t3.contains(item))
				quality = 4;
			else if (ml.t4.contains(item))
				quality = 5;
			
			for (Enchantment enchant : Enchantment.values())
			{
				if (item.containsEnchantment(enchant))
				{
					enchantmentStrength = enchantmentStrength + item.getEnchantmentLevel(enchant);
				}
			}
			
			//TOOD - remove
			FC_Rpg.plugin.getLogger().info("A: " + quality * enchantmentStrength * spellTier);
			
			rpgCaster.getPlayer().setItemInHand(getRandomTieredItem(quality * enchantmentStrength * spellTier));
			
			return true;
		}
		
		return false;
	}
	
	private ItemStack getRandomTieredItem(int enchantStrength)
	{
		List<ItemStack> itemList = new ArrayList<ItemStack>();
		Random rand = new Random();
		int amount = (rand.nextInt((enchantStrength / 20) + 3) + 1);
		
		itemList.add(new ItemStack(Material.ROTTEN_FLESH, amount));
		itemList.add(new ItemStack(Material.STICK, amount));
		itemList.add(new ItemStack(Material.DEAD_BUSH, amount));
		
		if (enchantStrength >= 0)
		{
			itemList.add(new ItemStack(Material.BOOK, amount));
			itemList.add(new ItemStack(Material.WOOD, amount));
			itemList.add(new ItemStack(Material.SAPLING, amount));
			itemList.add(new ItemStack(Material.COBBLESTONE, amount));
		}
		
		if (enchantStrength >= 1)
		{
			itemList.add(new ItemStack(Material.COAL, amount));
			itemList.add(new ItemStack(Material.EGG, amount));
			itemList.add(new ItemStack(Material.INK_SACK, amount));
			itemList.add(new ItemStack(Material.COOKIE, amount));
		}
		
		if (enchantStrength >= 2)
		{
			itemList.add(new ItemStack(Material.SHEARS, amount));
			itemList.add(new ItemStack(Material.RAW_FISH, amount));
			itemList.add(new ItemStack(Material.COAL, amount));
			itemList.add(new ItemStack(Material.STONE, amount));
			itemList.add(new ItemStack(Material.SMOOTH_BRICK, amount));
		}
		
		if (enchantStrength >= 4)
		{
			itemList.add(new ItemStack(Material.GLASS_BOTTLE, amount));
			itemList.add(new ItemStack(Material.ARROW, amount));
			itemList.add(new ItemStack(Material.FEATHER, amount));
			itemList.add(new ItemStack(Material.WOOL, amount));
			itemList.add(new ItemStack(Material.RAW_CHICKEN, amount));
		}
		
		if (enchantStrength >= 6)
		{
			itemList.add(new ItemStack(Material.LADDER, amount));
			itemList.add(new ItemStack(Material.GOLD_NUGGET, amount));
			itemList.add(new ItemStack(Material.RAILS, amount));
			itemList.add(new ItemStack(Material.CLAY, amount));
			itemList.add(new ItemStack(Material.LEAVES, amount));
		}
		
		if (enchantStrength >= 9)
		{
			itemList.add(new ItemStack(Material.REDSTONE, amount));
			itemList.add(new ItemStack(Material.SUGAR, amount));
			itemList.add(new ItemStack(Material.BONE, amount));
			itemList.add(new ItemStack(Material.MELON, amount));
			itemList.add(new ItemStack(Material.RAW_BEEF, amount));
		}
		
		if (enchantStrength >= 15)
		{
			itemList.add(new ItemStack(Material.COOKED_FISH, amount));
			itemList.add(new ItemStack(Material.COMPASS, amount));
			itemList.add(new ItemStack(Material.STRING, amount));
			itemList.add(new ItemStack(Material.RED_MUSHROOM, amount));
			itemList.add(new ItemStack(Material.COCOA, amount));
			itemList.add(new ItemStack(Material.SAND, amount));
		}
		
		if (enchantStrength >= 22)
		{
			itemList.add(new ItemStack(Material.SLIME_BALL, amount));
			itemList.add(new ItemStack(Material.MINECART, amount));
			itemList.add(new ItemStack(Material.RAILS, amount));
			itemList.add(new ItemStack(Material.SULPHUR, amount));
			itemList.add(new ItemStack(Material.MOSSY_COBBLESTONE, amount));
		}

		if (enchantStrength >= 35)
		{
			itemList.add(new ItemStack(Material.CACTUS, amount));
			itemList.add(new ItemStack(Material.BOOKSHELF, amount));
			itemList.add(new ItemStack(Material.BUCKET, amount));
			itemList.add(new ItemStack(Material.GLOWSTONE_DUST, amount));
			itemList.add(new ItemStack(Material.GLASS, amount));
			itemList.add(new ItemStack(Material.ICE, amount));
		}
		
		if (enchantStrength >= 50)
		{
			itemList.add(new ItemStack(Material.NETHERRACK, amount));
			itemList.add(new ItemStack(Material.NETHER_BRICK, amount));
			itemList.add(new ItemStack(Material.DIRT, amount));
			itemList.add(new ItemStack(Material.IRON_INGOT, amount));
			itemList.add(new ItemStack(Material.PISTON_BASE, amount));
		}
		
		if (enchantStrength >= 75)
		{
			itemList.add(new ItemStack(Material.OBSIDIAN, amount));
			itemList.add(new ItemStack(Material.DIAMOND, amount));
			itemList.add(new ItemStack(Material.SANDSTONE, amount));
			itemList.add(new ItemStack(Material.TNT, amount));
		}
		
		if (enchantStrength >= 90)
		{
			itemList.add(new ItemStack(Material.OBSIDIAN, amount));
			itemList.add(new ItemStack(Material.GOLD_INGOT, amount));
			itemList.add(new ItemStack(Material.GRASS, amount));
			itemList.add(new ItemStack(Material.ENCHANTMENT_TABLE, amount));
			itemList.add(new ItemStack(Material.GOLDEN_APPLE, amount));
		}
		
		//Return a random item.
		return itemList.get(rand.nextInt(itemList.size()));
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


















