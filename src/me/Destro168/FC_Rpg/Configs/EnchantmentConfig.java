package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Enchantment.Enchantment;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
import me.Destro168.FC_Rpg.Spells.EffectIDs;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ListGetter;

public class EnchantmentConfig extends ConfigGod
{
	// Class Global Variables
	public List<Enchantment> prefixList; // Not altered by config stuff
	public List<Enchantment> weaponSuffixList;
	public List<Enchantment> armorSuffixList;
	
	//Spell Config Specific Sets/Gets (Complete carbon copy)
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
	public void setIsClassRestricted(int i, boolean x) { fcw.set(prefix + i + ".isClassRestricted", x); }
	public void setRequiresTarget(int i, boolean x) { fcw.set(prefix + i + ".requiresTarget", x); }
	public void setUncastable(int i, boolean x) { fcw.set(prefix + i + ".uncastable", x); }
	
	//Gets
	public int getEffectID(int i) { return fcw.getInt(prefix + i + ".effectID"); }
	public String getName(int i) { return fcw.getString(prefix + i + ".name"); }
	public String getDescription(int i) { return fcw.getString(prefix + i + ".description"); }
	public List<Double> getManaCost(int i) { return fcw.getCustomDoubleList(prefix + i + ".manaCost"); }
	public List<Integer> getDuration(int i) { try { return fcw.getCustomIntegerList(prefix + i + ".duration"); } catch (NullPointerException e) { return null; } }
	public List<Double> getConstantMagnitude(int i) { try { return fcw.getCustomDoubleList(prefix + i + ".constantMagnitude"); } catch (NullPointerException e) { return null; } }
	public List<Double> getAttackMagnitude(int i) { try { return fcw.getCustomDoubleList(prefix + i + ".attackMagnitude"); } catch (NullPointerException e) { return null; } }
	public List<Double> getMagicMagnitude(int i) { try { return fcw.getCustomDoubleList(prefix + i + ".magicMagnitude"); } catch (NullPointerException e) { return null; } }
	public List<Double> getIntelligenceMagnitude(int i) { try { return fcw.getCustomDoubleList(prefix + i + ".intelligenceMagnitude"); } catch (NullPointerException e) { return null; } }
	public List<Double> getConstitutionMagnitude(int i) { try { return fcw.getCustomDoubleList(prefix + i + ".constitutionMagnitude"); } catch (NullPointerException e) { return null; } }
	public List<Integer> getRadius(int i) { try { return fcw.getCustomIntegerList(prefix + i + ".radius"); } catch (NullPointerException e) { return null; } }
	public boolean getTargetParty(int i) { return fcw.getBoolean(prefix + i + ".targetParty"); }
	public boolean getIsClassRestricted(int i) { return fcw.getBoolean(prefix + i + ".isClassRestricted"); }
	public boolean getRequiresTarget(int i) { return fcw.getBoolean(prefix + i + ".requiresTarget"); }
	public boolean getUncastable(int i) { return fcw.getBoolean(prefix + i + ".uncastable"); }
	
	// Enchantment Specific Config Accessing methods.
	public List<Integer> getEnchantmentList() { ListGetter lg = new ListGetter(fcw, prefix); return lg.getFieldIntegerList(); }
	
	public void setProcChance(int i, int x) { fcw.set(prefix + i + ".procChance", x); }
	public void setIsArmor(int i, boolean x) { fcw.set(prefix + i + ".isArmor", x); }
	
	public int getProcChance(int i) { return fcw.getInt(prefix + i + ".procChance"); }
	public boolean getIsArmor(int i) { return fcw.getBoolean(prefix + i + ".isArmor"); }
	
	public void setDefaultProcChance(int i) { setProcChance(i, 5); }
	
	//Constructor
	public EnchantmentConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Enchantments");
		prefix = "Enchantments.";
		
		handleConfig();
		
		// Initialize lists.
		prefixList = new ArrayList<Enchantment>();
		weaponSuffixList = new ArrayList<Enchantment>();
		armorSuffixList = new ArrayList<Enchantment>();
		
		prefixList.add(new Enchantment("Strong", "+Attack"));
		prefixList.add(new Enchantment("Tough", "+Constitution"));
		prefixList.add(new Enchantment("Wise", "+Intelligence"));
		prefixList.add(new Enchantment("Magic", "+Magic"));
		prefixList.add(new Enchantment("Burly", "+Attack, +Constitution"));
		prefixList.add(new Enchantment("Magic Forged", "+Attack, +Intelligence"));
		prefixList.add(new Enchantment("Brutal", "+Attack, +Magic"));
		prefixList.add(new Enchantment("Fortified", "+Constitution, +Intelligence"));
		prefixList.add(new Enchantment("Blessed", "+Constitution, +Magic"));
		prefixList.add(new Enchantment("Arcane", "+Magic, +Intelligence"));
		prefixList.add(new Enchantment("Elemental", "+Attack, +Constitution, +Magic, +Intelligence"));
		
		// Load weapon and armor suffix's from config.
		for (int i : getEnchantmentList())
		{
			if (getIsArmor(i) == true)
				armorSuffixList.add(loadEnchant(i));
			else
				weaponSuffixList.add(loadEnchant(i));
		}
	}
	
	public void handleConfig()
	{
		if (getVersion() < 1.0)
		{
			setVersion(1.0);
			setDefaultEnchantments();
		}
	}
	
	public Enchantment loadEnchant(int i)
	{
		loadConfigValues();
		
		return new Enchantment(spells.get(i), getProcChance(i));
	}
	
	protected Map<Integer, Spell> spells;
	
	// Copy of load default config settings function.
	public void loadConfigValues()
	{
		//Variable Declarations and Initializations
		spells = new HashMap<Integer, Spell>();
		
		//We want to load up all spells now.
		for (int i = 0; i < SpellConfig.SPELL_COUNT_MAX; i++)
		{
			if (getName(i) != null)
			{
				if (!getName(i).equals(""))
				{
					spells.put(i, new Spell(getEffectID(i), getName(i), getDescription(i), getDuration(i), getManaCost(i), getConstantMagnitude(i), getAttackMagnitude(i), getMagicMagnitude(i),
							getIntelligenceMagnitude(i), getConstitutionMagnitude(i), getRadius(i), getTargetParty(i),
							getIsClassRestricted(i), getRequiresTarget(i), getUncastable(i)));
				}
			}
		}
	}
	
	private void setDefaultEnchantments()
	{
		int i = 0;
		
		/*******************************************
		 * Weapon Enchantments
		*******************************************/
		
		setEffectID(i, EffectIDs.DAMAGE_BONUS);
		setName(i, " Of Destruction");
		setDescription(i, "Attacks have a chance to deal bonus damage.");
		setConstantMagnitude(i, 4, 8, 12, 16, 20);
		setDefaultProcChance(i);
		setIsArmor(i, false);
		
		i++;
		setEffectID(i, EffectIDs.FROST_STRIKE);
		setName(i, " Of Chilling");
		setDescription(i, "Attacks have a chance to freeze target.");
		setConstantMagnitude(i, 1, 2, 3, 4, 5);
		setDefaultProcChance(i);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.DISABLED);
		setName(i, " Of Malice");
		setDescription(i, "Attacks have a chance to stop target from attacking.");
		setConstantMagnitude(i, 500, 1000, 1500, 2000, 2500);
		setDefaultProcChance(i);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.MANA_STEAL);
		setName(i, " Of Mind Flaying");
		setDescription(i, "Attacks have a chance to steal mana.");
		setConstantMagnitude(i, .008, .016, .024, .032, .04);
		setDefaultProcChance(i);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.HEALTH_STEAL);
		setName(i, " Of Viper");
		setDescription(i, "Attacks have a chance to steal health.");
		setConstantMagnitude(i, .008, .016, .024, .032, .04);
		setDefaultProcChance(i);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.CRITICAL_DAMAGE_DOUBLE);
		setName(i, " Of Splintering");
		setDescription(i, "Attacks have a chance to crit for double damage.");
		setConstantMagnitude(i, 4, 8, 12, 16, 20);
		setProcChance(i,100);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.IGNORE_ARMOR);
		setName(i, " Of Penetration");
		setDescription(i, "Chance to apply a small buff to oneself so that target defense is ignored.");
		setDuration(i, 200, 400, 600, 800, 1000);
		setDefaultProcChance(i);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.BONUS_GOLD);
		setName(i, " Of Greed");
		setDescription(i, "Monsters have a chance to award bonus gold.");
		setConstantMagnitude(i, 1.05, 1.1, 1.15, 1.2, 1.25);
		setDefaultProcChance(i);
		setIsArmor(i, false);

		i++;
		setEffectID(i, EffectIDs.BONUS_EXPERIENCE);
		setName(i, " Of Traveling");
		setDescription(i, "Monsters have a chance to award bonus exp.");
		setConstantMagnitude(i, 1.05, 1.1, 1.15, 1.2, 1.25);
		setDefaultProcChance(i);
		setIsArmor(i, false);
		
		/*******************************************
		 * Armor Enchantments
		*******************************************/

		i++;
		setEffectID(i, EffectIDs.HEAL_CHANCE);
		setName(i, " Of Restoration");
		setDescription(i, "Chance to heal when attacked");
		setConstantMagnitude(i, .01, .02, .03, .04, .05);
		setProcChance(i,1);
		setIsArmor(i, true);

		i++;
		setEffectID(i, EffectIDs.DODGE);
		setName(i, " Of Agility");
		setDescription(i, "Chance to temporarily dodge all damage.");
		setDuration(i, 200, 400, 600, 800, 1000);
		setProcChance(i,1);
		setIsArmor(i, true);

		i++;
		setEffectID(i, EffectIDs.DEFENSE);
		setName(i, " Of Blocking");
		setDescription(i, "Chance to reduce incoming damage.");
		setConstantMagnitude(i, .98, .96, .94, .92, .90);
		setProcChance(i,1);
		setIsArmor(i, true);
	}
	
	public Enchantment getEnchantmentByID(int id)
	{
		Enchantment e = checkList(id, prefixList);
		
		if (e != null)
			return e;
		
		e = checkList(id, weaponSuffixList);
		
		if (e != null)
			return e;

		return checkList(id, armorSuffixList);
	}
	
	private Enchantment checkList(int id, List<Enchantment> enchantList)
	{
		if (enchantList.contains(id))
		{
			for (Enchantment e : enchantList)
				if (e.spell.effectID == id) return e;
		}
		
		return null;
	}
}
