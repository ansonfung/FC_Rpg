package me.Destro168.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Util.MaterialLib;
import me.Destro168.Util.SpellUtil;

import org.bukkit.Bukkit;
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

public class SpellCastManager 
{
	private SpellUtil spellUtil;
	private EntityLocationLib ell;
	
	private LivingEntity target;
	private RpgPlayer rpgCaster;
	private RpgPlayer rpgDefender;
	private RpgMonster rpgMobDefender;
	
	private Player playerCaster;
	private Player playerDefender;
	
	private String activeSpell;
	private String spellName;
	private double spellMagnitude;
	private double damage;
	private int combatClass;
	private int spellNumber;
	private int damageType;
	private int spellTier;
	private int magic;
	private int manaCost;
	
	public double getDamage() { return damage; }
	public int getManaCost() { return manaCost; }
	
	public SpellCastManager() 
	{
		setDefaults();
	}
	
	public void setDefaults()
	{
		spellUtil = new SpellUtil();
		ell = new EntityLocationLib();
		target = null;
		rpgCaster = null;
		playerCaster = null;
		playerDefender = null;
		activeSpell = "";
		spellName = "";
		spellMagnitude = 0;
		damage = 0;
		combatClass = 0;
		spellNumber = -1;
		damageType = 0;
		spellTier = -1;
		magic = 0;
		manaCost = -1;
	}
	
	public boolean init_spellCast(RpgPlayer spellCaster_, LivingEntity target_, double damage_, int damageType_)
	{
		target = target_;
		rpgCaster = spellCaster_;
		damage = damage_;
		damageType = damageType_;
		playerCaster = spellCaster_.getPlayer();
		activeSpell = rpgCaster.getActiveSpell();
		combatClass = rpgCaster.getCombatClass();
		magic = rpgCaster.getTotalMagic();
		
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
		for (int i = 0; i < SpellUtil.CLASS_SPELL_COUNT; i++)
		{
			spellName = spellUtil.getSpellName(combatClass, i);
			
			if (activeSpell.equals(spellName))
			{
				//Assign Variables.
				spellNumber = i;
				spellTier = rpgCaster.getSpellLevel(i) - 1;
				
				//Make sure that assassins are using bows.
				if (combatClass == FC_Rpg.c_int_assassin)
				{
					if (damageType != 1 && spellNumber != 0 && spellNumber != 1)
					{
						MessageLib msgLib = new MessageLib(playerCaster);
						msgLib.standardMessage("Please shoot arrows from a bow to cast spells.");
						return false;
					}
				}
				else if (combatClass == FC_Rpg.c_int_wizard)
				{
					if (!(spellNumber == 1))
					{
						if (!(playerCaster.getItemInHand().getType() == Material.STICK))
						{
							MessageLib msgLib = new MessageLib(playerCaster);
							msgLib.standardMessage("Please use a wand (stick) to cast spells.");
							return false;
						}
					}
				}
				
				//Attempt to drain mana. On fail return damage.
				if (rpgCaster.hasEnoughMana(spellNumber, spellTier) == false)
				{
					rpgCaster.attemptNoManaNotification(spellNumber, spellTier);
					return false;
				}
				
				//Break loop execution.
				break;
			}
		}
		
		//If no valid spell was cast, then return.
		if (spellNumber == -1)
			return false;
		
		//Reset active spell.
		rpgCaster.resetActiveSpell();
		
		//Tell the caster they cast the spell.
		rpgCaster.attemptCastNotification(spellName);
		
		//Store spell magnitude.
		spellMagnitude = spellUtil.getSpellMagnitude(combatClass, spellNumber, spellTier);

		//Set the spell mana cost.
		manaCost = spellUtil.getSpellManaCost(combatClass, spellNumber, spellTier);
		
		return true;
	}
	
	public boolean cast_Swordsman()
	{
		//1st spell applies dodge status to player for given time.
		if (activeSpell.equals(spellUtil.getSpellName(0,0)))
		{
			//Give dodge buff.
			rpgCaster.setStatusDodge(10000);
			rpgCaster.setStatusDodgeStrength((int) spellMagnitude + magic / 166);
			return true;
		}
		
		//2nd spell applys an offensive bonus to allies
		else if (activeSpell.equals(spellUtil.getSpellName(0,1)))
		{
			//For all friendly party members
			for (RpgPlayer rpgPlayer : FC_Rpg.rpgManager.getNearbyPartiedRpgPlayers(playerCaster, 15))
			{
				//Add morale to allies.
				rpgPlayer.setStatusMorale(10000);
				rpgPlayer.setStatusMoraleStrength((int) (spellMagnitude + magic * .01));
			}
			
			return true;
		}
		
		//Next spells require target
		if (target == null)
			return false;
		
		//3rd spell just adds bonus daamge.
		if (spellName.equals(spellUtil.getSpellName(0,2)))
		{
			damage = (rpgCaster.getTotalStrength() * spellMagnitude) + (magic * .01);
			return true;
		}
		
		//4th spell applies a bleed for an amount of time.
		else if (activeSpell.equals(spellUtil.getSpellName(0,3)))
		{
			final EntityDamageManager edm = new EntityDamageManager();
			final int length = (int) (spellMagnitude + (magic * .01));
			
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
					}, i);
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
					}, i);
				}
			}
			
			return true;
		}

		//5th spell deals damage in an AoE around the attacker
		else if (activeSpell.equals(spellUtil.getSpellName(0,4)))
		{
			for (Entity entity : playerCaster.getNearbyEntities(spellMagnitude, spellMagnitude, spellMagnitude))
			{
				if (entity instanceof LivingEntity)
				{
					((LivingEntity) entity).damage(1, playerCaster);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public boolean cast_Assassin()
	{
		//1st spell applies a speed status.
		if (activeSpell.equals(spellUtil.getSpellName(1,0)))
		{
			//Create a potion effect and apply to the attacker.
			playerCaster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, (int) spellMagnitude));
			return true;
		}
		
		//2nd spell gives fire arrow status.
		else if (activeSpell.equals(spellUtil.getSpellName(1,1)))
		{
			rpgCaster.setStatusFireArrow(spellMagnitude + (magic * .01));
			return true;
		}
		
		//Next spells require target
		if (target == null)
			return false;
		
		//3rd spell is bonus damage.
		if (activeSpell.equals(spellUtil.getSpellName(1,2)))
		{
			damage = (rpgCaster.getTotalStrength() * spellMagnitude) + (magic * .01);
			return true;
		}
		
		//4th spell grants a poison arrow.
		else if (activeSpell.equals(spellUtil.getSpellName(1,3)))
		{
			final EntityDamageManager edm = new EntityDamageManager();
			final double finalDamage = rpgCaster.getStrength() * (spellMagnitude + (magic / 8000));
			
			if (rpgMobDefender != null)
			{
				final RpgMonster finalMobDefender = rpgMobDefender;
				
				for (int i = 0; i < spellMagnitude; i++)
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
				
				for (int i = 0; i < spellMagnitude; i++)
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
			
			return true;
		}
		
		//5th spell gives you powerful backstab damage.
		else if (activeSpell.equals(spellUtil.getSpellName(1,4)))
		{
			if (playerCaster.getLocation() == ell.getLocationBehindEntity(target.getLocation()))
				damage = rpgCaster.getTotalStrength() * spellMagnitude + (magic / 250);
			
			return true;
		}
		
		return false;
	}
	
	public boolean cast_Defender()
	{
		//1st spell taunts monsters and increases defense.
		if (activeSpell.equals(spellUtil.getSpellName(2,0)))
		{
			FC_Rpg.plugin.getLogger().info("TEST");
			
			//For all friendly party members
			for (RpgPlayer rpgPlayer : FC_Rpg.rpgManager.getNearbyPartiedRpgPlayers(playerCaster, 15))
			{
				//Add taunt defense buff.
				rpgPlayer.setStatusTaunt(10000);
				rpgPlayer.setStatusTauntStrength((int) spellMagnitude + magic / 166);
			}
			
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
			
			return true;
		}
		
		//2nd spell gives thorns status.
		else if (activeSpell.equals(spellUtil.getSpellName(2,1)))
		{
			rpgCaster.setStatusThorns(10000);
			rpgCaster.setStatusThornsStrength((int) spellMagnitude + magic / 166);
			return true;
		}
		
		//3rd spell heals a percentage of health
		else if (activeSpell.equals(spellUtil.getSpellName(2,2)))
		{
			//Heal by spell magnitude.
			rpgCaster.heal(rpgCaster.getMaxHealth() * spellMagnitude + magic / 5000);
			return true;
		}
		
		//Next spells require target
		if (target == null)
			return false;
		
		//4th spell permanently lowers a mobs stats
		else if (activeSpell.equals(spellUtil.getSpellName(2,3)))
		{
			spellMagnitude = spellMagnitude * (magic / 166);
			
			if (rpgMobDefender != null)
			{
				if (rpgMobDefender.getIsBoss())
					return false;
				
				if (rpgMobDefender.getIsWeak())
					return false;
				
				rpgMobDefender.setConstitution((int) (rpgMobDefender.getConstitution() * (1 - spellMagnitude)));
				rpgMobDefender.setStrength((int) (rpgMobDefender.getStrength() * (1 - spellMagnitude)));
				rpgMobDefender.setCurHealth((int) (rpgMobDefender.getCurHealth() * (1 - spellMagnitude)));
				rpgMobDefender.setMaxHealth((int) (rpgMobDefender.getMaxHealth() * (1 - spellMagnitude)));
				
				rpgMobDefender.setIsWeak(true);
			}
			
			return true;
		}
		
		//5th spell stuns.
		else if (activeSpell.equals(spellUtil.getSpellName(2,4)))
		{
			//Bosses are immune to stuns.
			if (rpgMobDefender.getIsBoss())
				return false;
			
			if (rpgMobDefender != null)
			{
				rpgMobDefender.setStatusDisabled(spellMagnitude + (magic / 1000));
			}
			else if (playerCaster != null)
			{
				rpgCaster.setStatusDisabled(spellMagnitude + (magic / 1000));
			}
			
			return true;
		}

		return false;
	}
	
	public boolean cast_Wizard()
	{
		//1st spell releases a fireball to deal AoE damage.
		if (activeSpell.equals(spellUtil.getSpellName(3,0)))
		{
			//Create a fireball
			Fireball fireball = playerCaster.launchProjectile(Fireball.class);
			
			//Set the shooter to player attacker.
			fireball.setShooter(playerCaster);
			
			//Don't create fire.
			fireball.setIsIncendiary(false);
			
			//Create and add the fireball to the players ownership
			rpgCaster.summon_Add(fireball, 40);
			
			return true;
		}
		
		//2nd spell performs alchemy
		else if (activeSpell.equals(spellUtil.getSpellName(3,1)))
		{
			return performAlchemy();
		}
		
		//Handle personal heals. (Goes a bit out of order on spell number)
		else if (activeSpell.equals(spellUtil.getSpellName(3,3)))
		{
			if (rpgCaster != null)
				rpgCaster.heal(rpgCaster.getMaxHealth() * spellMagnitude);
			
			return true;
		}
		
		//Handle personal invogorates.
		else if (activeSpell.equals(spellUtil.getSpellName(3,4)))
		{
			if (rpgCaster != null)
				rpgCaster.addSupportBuff(spellMagnitude);
			
			return true;
		}
		
		//Next spells require target
		if (target == null)
			return false;
		
		//3rd spell strikes lightning and alters damage.
		if (activeSpell.equals(spellUtil.getSpellName(3,2)))
		{
			damage = damage * spellMagnitude;
			playerCaster.getWorld().strikeLightningEffect(target.getLocation());
			return true;
		}
		
		//3rd spell heals.
		else if (activeSpell.equals(spellUtil.getSpellName(3,3)))
		{
			if (playerDefender != null)
				rpgDefender.heal(rpgDefender.getMaxHealth() * spellMagnitude);
			
			return true;
		}
		
		//4th spell adds a support buff to players.
		else if (activeSpell.equals(spellUtil.getSpellName(3,4)))
		{
			if (playerDefender != null)
				rpgDefender.addSupportBuff(spellMagnitude);
			
			return true;
		}

		return false;
	}
	
	public boolean cast_Berserker()
	{
		//1st spell adds lifesteal buff.
		if (activeSpell.equals(spellUtil.getSpellName(4,0)))
		{
			rpgCaster.setStatusBloodthirst(10000);
			rpgCaster.setStatusBloodthirstStrength((int) spellMagnitude);
			return true;
		}
		
		//2nd spell grants immortality
		else if (activeSpell.equals(spellUtil.getSpellName(4,1)))
		{
			rpgCaster.setStatusImmortal(spellMagnitude);
			return true;
		}
		
		//3rd spell teleports you behind a target you attack.
		else if (activeSpell.equals(spellUtil.getSpellName(4,2)))
		{
			rpgCaster.setStatusFerocity(10000);
			rpgCaster.setStatusFerocityStrength(spellMagnitude);
			return true;
		}
		
		//Need targets for remaining spells.
		if (target == null)
			return false;
		
		//4th spell gives you bonus damage based on missing health.
		else if (activeSpell.equals(spellUtil.getSpellName(4,3)))
		{
			damage = rpgCaster.getTotalStrength() * rpgCaster.getMissingHealthDecimal() * spellMagnitude;
			return true;
		}
		
		//5th spell sacrifices a % of your current health for bonus damage.
		else if (activeSpell.equals(spellUtil.getSpellName(4,4)))
		{
			double hpToLose = rpgCaster.getCurHealth() * .2;
			rpgCaster.dealDamage(hpToLose);
			damage = rpgCaster.getTotalStrength() * spellMagnitude;
			return true;
		}

		return false;
	}

	private boolean performAlchemy()
	{
		MaterialLib ml = new MaterialLib();
		ItemStack item = rpgCaster.getPlayer().getItemInHand();
		int quality = 0;
		int enchantmentStrength = 0;
		
		if (item != null)
		{
			if (ml.t1.contains(item))
				quality = 1;
			else if (ml.t2.contains(item))
				quality = 2;
			else if (ml.t3.contains(item))
				quality = 3;
			else if (ml.t4.contains(item))
				quality = 4;
			else if (ml.t5.contains(item))
				quality = 5;
			
			for (Enchantment enchant : Enchantment.values())
			{
				if (item.containsEnchantment(enchant))
				{
					enchantmentStrength = enchantmentStrength + item.getEnchantmentLevel(enchant);
				}
			}
			
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
		
		/* >1:Dead Shrub.Rotten Flesh.Stick.Live Shrub.Tall Grass.Air
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
		>20:Golden Apple.Emerald Block.Gold Block */
		
		//Return a random item.
		return itemList.get(rand.nextInt(itemList.size()));
	}
}



























