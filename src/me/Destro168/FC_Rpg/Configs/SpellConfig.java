package me.Destro168.FC_Rpg.Configs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
import me.Destro168.FC_Rpg.Spells.EffectIDs;

//Rename spell manager
public class SpellConfig extends ConfigGod
{
	private final int SPELL_COUNT_MAX = 1000;
	public final static int SPELL_TIERS = 5;
	
	private Map<Integer, Spell> spells;
	
	public Spell getSpell(int i) { return spells.get(i); }
	public int getSpellCount() { return spells.size(); }
	
	//Sets
	public void setEffectID(int i, int x) { fcw.set(prefix + i + ".effectID", x); }
	public void setName(int i, String x) { fcw.set(prefix + i + ".name", x); }
	public void setDescription(int i, String x) { fcw.set(prefix + i + ".description", x); }
	public void setManaCost(int i, double a, double b, double c, double d, double e) { fcw.set(prefix + i + ".manaCost", a + "," + b + "," + c + "," + d + "," + e); }
	
	public void setDuration(int i, int a, int b, int c, int d, int e) { fcw.set(prefix + i + ".duration", a + "," + b + "," + c + "," + d + "," + e); }
	public void setConstantMagnitude(int i, double a, double b, double c, double d, double e) { fcw.set(prefix + i + ".constantMagnitude", a + "," + b + "," + c + "," + d + "," + e); }
	public void setAttackMagnitude(int i, double a, double b, double c, double d, double e) { fcw.set(prefix + i + ".attackMagnitude", a + "," + b + "," + c + "," + d + "," + e); }
	public void setMagicMagnitude(int i, double a, double b, double c, double d, double e) { fcw.set(prefix + i + ".magicMagnitude", a + "," + b + "," + c + "," + d + "," + e); }
	public void setIntelligenceMagnitude(int i, double a, double b, double c, double d, double e) { fcw.set(prefix + i + ".intelligenceMagnitude", a + "," + b + "," + c + "," + d + "," + e); }
	public void setConstitutionMagnitude(int i, double a, double b, double c, double d, double e) { fcw.set(prefix + i + ".constitutionMagnitude", a + "," + b + "," + c + "," + d + "," + e); }
	public void setRadius(int i, int a, int b, int c, int d, int e) { fcw.set(prefix + i + ".radius", a + "," + b + "," + c + "," + d + "," + e); }
	
	public void setTargetParty(int i, boolean x) { fcw.set(prefix + i + ".targetParty", x); }
	public void setRestricted(int i, boolean x) { fcw.set(prefix + i + ".restricted", x); }
	
	//Gets
	public int getEffectID(int i) { return fcw.getInt(prefix + i + ".effectID"); }
	public String getName(int i) { return fcw.getString(prefix + i + ".name"); }
	public String getDescription(int i) { return fcw.getString(prefix + i + ".description"); }
	public List<Double> getManaCost(int i) { return converter.getDoubleListFromString(fcw.getString(prefix + i + ".manaCost")); }
	
	public List<Integer> getDuration(int i) { try { return converter.getIntegerListFromString(fcw.getString(prefix + i + ".duration")); } catch (NullPointerException e) { return null; } }
	public List<Double> getConstantMagnitude(int i) { try { return converter.getDoubleListFromString(fcw.getString(prefix + i + ".constantMagnitude")); } catch (NullPointerException e) { return null; } }
	public List<Double> getAttackMagnitude(int i) { try { return converter.getDoubleListFromString(fcw.getString(prefix + i + ".attackMagnitude")); } catch (NullPointerException e) { return null; } }
	public List<Double> getMagicMagnitude(int i) { try { return converter.getDoubleListFromString(fcw.getString(prefix + i + ".magicMagnitude")); } catch (NullPointerException e) { return null; } }
	public List<Double> getIntelligenceMagnitude(int i) { try { return converter.getDoubleListFromString(fcw.getString(prefix + i + ".intelligenceMagnitude")); } catch (NullPointerException e) { return null; } }
	public List<Double> getConstitutionMagnitude(int i) { try { return converter.getDoubleListFromString(fcw.getString(prefix + i + ".constitutionMagnitude")); } catch (NullPointerException e) { return null; } }
	public List<Integer> getRadius(int i) { try { return converter.getIntegerListFromString(fcw.getString(prefix + i + ".radius")); } catch (NullPointerException e) { return null; } }
	
	public boolean getTargetParty(int i) { return fcw.getBoolean(prefix + i + ".targetParty"); }
	public boolean getRestricted(int i) { return fcw.getBoolean(prefix + i + ".restricted"); }
	
	public SpellConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Spells");
		handleConfig();
		loadConfigValues();
	}
	
	private void handleConfig()
	{
		if (getVersion() < 1.0)
		{
			//Set the version.
			setVersion(1.0);
			
			//Set the default spells.
			setDefaultSpells();
		}
		
		//Save the config.
		FC_Rpg.plugin.saveConfig();
	}
	
	public void loadConfigValues()
	{
		//Variable Declarations and Initializations
		spells = new HashMap<Integer, Spell>();
		
		//We want to load up all spells now.
		for (int i = 0; i < SPELL_COUNT_MAX; i++)
		{
			if (getName(i) != null)
			{
				if (!getName(i).equals(""))
				{
					spells.put(i, new Spell(getEffectID(i), getName(i), getDescription(i), getDuration(i), getManaCost(i), getConstantMagnitude(i), getAttackMagnitude(i), getMagicMagnitude(i),
							getIntelligenceMagnitude(i), getConstitutionMagnitude(i), getRadius(i), getTargetParty(i), getRestricted(i)));
				}
			}
		}
	}
	
	private void setBuffStats(int i)
	{
		setDuration(i, 10000, 10000, 10000, 10000, 10000);
		setManaCost(i, 10, 15, 20, 25, 30);
		setRestricted(i, false);
	}
	
	private void setD1Stats(int i)
	{
		setManaCost(i, 2, 4, 6, 8, 10);
		setRestricted(i, true);
	}
	
	private void setDefaultSpells()
	{
		/*******************************************
		 * Swordsman
		*******************************************/
		
		setEffectID(0, EffectIDs.DODGE);
		setName(0, "Nimble");
		setDescription(0, "Grants you (x)% passive dodge chance for 10 seconds.");
		setBuffStats(0);
		setConstantMagnitude(0, 4, 8, 12, 16, 20);
		
		setEffectID(1, EffectIDs.ATTACK);
		setName(1, "Morale");
		setDescription(1, "Apply a (x)% damage boost to your entire party for 10 seconds.");
		setBuffStats(1);
		setConstantMagnitude(1, 1.04, 1.08, 1.12, 1.16, 1.20);
		
		setEffectID(2, EffectIDs.DAMAGE_BOOST);
		setName(2, "Empower");
		setDescription(2, "Empowers your next attack to deal (x)% bonus damage.");
		setD1Stats(2);
		setConstantMagnitude(2, 1.2, 1.4, 1.6, 1.8, 2);
		
		setEffectID(3, EffectIDs.BLEED);
		setName(3, "Slice");
		setDescription(3, "Deal 2% of targets' hp each second for (x) seconds.");
		setD1Stats(3);
		setConstantMagnitude(3, 2, 4, 6, 8, 10);
		
		setEffectID(4, EffectIDs.AOE);
		setName(4, "Vortex");
		setDescription(4, "Strike all monsters in a (x) block AoE.");
		setD1Stats(4);
		setConstantMagnitude(4, 3, 6, 9, 12, 15);
		
		/*******************************************
		 * Assassin
		*******************************************/
		
		setEffectID(5, EffectIDs.SPEED);
		setName(5, "Adreneline");
		setDescription(5, "Applies (x) speed buff on character for 10 seconds.");
		setDuration(5, 10, 10, 10, 10, 10);
		setManaCost(5, 10, 15, 20, 25, 30);
		setRestricted(5, false);
		setConstantMagnitude(5, .3, .4, .5, .6, .7);
		
		setEffectID(6, EffectIDs.FIRE_ARROW);
		setName(6, "Flame");
		setDescription(6, "Gives your arrows a burning effect for (x) shots. Fire insta-kills mobs but does not " +
				"give loot/exp/items from them or kill bosses.");
		setD1Stats(6);
		setConstantMagnitude(6, 1, 2, 3, 4, 5);
		
		setEffectID(7, EffectIDs.DAMAGE_BOOST);
		setName(7, "Force");
		setDescription(7, "Strengthens your next arrow to deal (x)% bonus damage.");
		setD1Stats(7);
		setConstantMagnitude(7, 1.2, 1.4, 1.6, 1.8, 2);
		
		setEffectID(8, EffectIDs.POISON);
		setName(8, "Poison");
		setDescription(8, "Your next arrow applies a 5 second long, 1 tick a second poison that does (x) damage.");
		setManaCost(8, 3, 6, 9, 12, 15);
		setConstantMagnitude(8, .24, .48, .72, .96, 1.2);
		setRestricted(8, true);
		
		setEffectID(9, EffectIDs.BACKSTAB);
		setName(9, "Assassinate");
		setDescription(9, "Multiplies the damage of your next attack by (x) if it hits a target from behind.");
		setManaCost(9, 5, 10, 15, 20, 25);
		setConstantMagnitude(9, 1.6, 3.2, 4.8, 6.2, 8.0);
		setRestricted(9, true);
		
		/*******************************************
		 * Defender
		*******************************************/
		
		setEffectID(10, EffectIDs.TAUNT);
		setName(10, "Taunt");
		setDescription(10, "Teleport and set aggro of all monsters in 12 block radius to you. Increases party defense by (x)% for 10 seconds.");
		setBuffStats(10);
		setConstantMagnitude(10, .96, .92, .88, .84, .80);
		
		setEffectID(11, EffectIDs.THORNS);
		setName(11, "Thorns");
		setDescription(11, "All attacks against you return (x)% of damage. Lasts for 10 seconds.");
		setBuffStats(11);
		setConstantMagnitude(11, .04, .08, .12, .16, .20);
		
		setEffectID(12, EffectIDs.HEAL_SELF);
		setName(12, "Undefeated");
		setDescription(12, "Heal (x)% of your max health.");
		setBuffStats(12);
		setManaCost(12, 15, 30, 45, 60, 75);
		setConstantMagnitude(12, .1, .2, .3, .4, .5);
		
		setEffectID(13, EffectIDs.WEAKEN);
		setName(13, "Weaken");
		setDescription(13, "Permanently lower all mob stats by (x)%.");
		setManaCost(13, 3, 6, 9, 12, 15);
		setConstantMagnitude(13, .03, .06, .09, .12, .15);
		setRestricted(13, true);
		
		setEffectID(14, EffectIDs.DISABLED);
		setName(14, "Bash");
		setDescription(14, "Disables your targets ability to attack for (x) seconds.");
		setManaCost(14, 3, 6, 9, 12, 15);
		setConstantMagnitude(14, 500, 1000, 1500, 2000, 2500);
		setRestricted(14, true);
		
		/*******************************************
		 * Wizard
		*******************************************/
		
		setEffectID(15, EffectIDs.FIREBALL);
		setName(15, "Fireball");
		setDescription(15, "Release a fireball exploding an area for (x) damage.");
		setManaCost(15, 3, 6, 9, 12, 15);
		setConstantMagnitude(15, 1, 1, 1, 1, 1);
		setMagicMagnitude(15, .3333, .4166, .5, .5833, .6666);
		setRadius(15, 12, 12, 12, 12, 12);
		setRestricted(15, false);
		
		setEffectID(16, EffectIDs.ALCHEMY);
		setName(16, "Alchemy");
		setDescription(16, "Destroy enchanted items for items. Read wiki for info.");
		setManaCost(16, 10, 15, 20, 25, 30);
		setConstantMagnitude(16, 1, 1.25, 1.5, 1.75, 2);
		setRestricted(16, false);
		
		setEffectID(17, EffectIDs.LIGHTNING);
		setName(17, "Lightning");
		setDescription(17, "Strike a target with lightning for (x) damage.");
		setManaCost(17, 1, 2, 3, 4, 5);
		setConstantMagnitude(17, 1, 1, 1, 1, 1);
		setMagicMagnitude(17, 1, 1.25, 1.5, 1.75, 2);
		setRestricted(17, true);
		
		setEffectID(18, EffectIDs.HEAL_OTHER);
		setName(18, "Remedy");
		setDescription(18, "Heal an ally for (x) health.");
		setManaCost(18, 4, 8, 12, 16, 20);
		setConstantMagnitude(18, 1, 1, 1, 1, 1);
		setMagicMagnitude(18, 1, 1.25, 1.5, 1.75, 2);
		setRestricted(18, true);
		
		setEffectID(19, EffectIDs.BOOST_STATS);
		setName(19, "Invigorate");
		setDescription(19, "Buff an allies stats by (x) for 10 seconds.");
		setManaCost(19, 6, 12, 18, 24, 30);
		setConstantMagnitude(19, .03, .06, .09, .12, .15);
		setMagicMagnitude(19, 1, 1.25, 1.5, 1.75, 2);
		setRestricted(19, false);
		
		/*******************************************
		 * Bloodthirster
		*******************************************/
		
		setEffectID(20, EffectIDs.LIFESTEAL);
		setName(20, "Bloodthirst");
		setDescription(20, "Steal (x)% life from all damage for 10 seconds.");
		setBuffStats(20);
		setConstantMagnitude(20, .04, .08, .12, .16, .20);
		
		setEffectID(21, EffectIDs.IMMORTAL);
		setName(21, "Undying");
		setDescription(21, "Become immune to death for (x) seconds.");
		setBuffStats(21);
		setConstantMagnitude(21, 1, 1, 1, 1, 1);
		
		setEffectID(22, EffectIDs.TELEPORT_STRIKE);
		setName(22, "Ferocity");
		setDescription(22, "Imbue your normal strength with magic to attack with such strength that you teleport behind targets for (x) seconds.");
		setBuffStats(22);
		setConstantMagnitude(22, 3, 3.5, 4, 4.5, 5);
		
		setEffectID(23, EffectIDs.DAMAGE_BY_MISSING_HEALTH);
		setName(23, "Unbalanced");
		setDescription(23, "Deal missing health as (x)% instant bonus damage.");
		setD1Stats(23);
		setConstantMagnitude(23, 3, 3.5, 4, 4.5, 5);
		
		setEffectID(24, EffectIDs.SACRIFICE_HEALTH_FOR_DAMAGE);
		setName(24, "Reckless");
		setDescription(24, "Sacrifice 20% of your current hp for (x)% instant bonus damage.");
		setD1Stats(24);
		setConstantMagnitude(24, 1.6, 3.2, 4.8, 6.2, 8.0);
	}
}

















