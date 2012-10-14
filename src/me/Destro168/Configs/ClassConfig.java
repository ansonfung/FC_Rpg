package me.Destro168.Configs;

import java.util.List;

import me.Destro168.Classes.RpgClass;

public class ClassConfig extends ConfigGod
{
	private final int CLASS_COUNT_MAX = 100;
	
	public static int passive_ScalingArrows = 0;
	public static int passive_CounterAttack = 1;
	public static int passive_StrongerParry = 2;
	public static int passive_BattleLust = 3;
	
	private int classCount;
	private RpgClass[] rpgClass;
	
	public RpgClass[] getRpgClasses() { return rpgClass; }
	public RpgClass getRpgClass(int i) { return rpgClass[i]; }
	
	private void setName(int i, String x) { ccm.set(prefix + i + ".name", x); }
	private void setDescription(int i, String x) { ccm.set(prefix + i + ".description", x); }
	private void setPassiveID(int i, int x) { ccm.set(prefix + i + ".passiveID", x); }
	private void setRestrictionID(int i, int x) { ccm.set(prefix + i + ".restrictionID", x); }
	private void setStatGrowth(int i, int a, int b, int c, int d) { ccm.set(prefix + i + ".statGrowth", a + "," + b + "," + c + "," + d); }
	private void setSpells(int i, int a, int b, int c, int d, int e) { ccm.set(prefix + i + ".spellIDs", a + "," + b + "," + c + "," + d + "," + e); }
	
	private String getName(int i) { return ccm.getString(prefix + i + ".name"); }
	private String getDescription(int i) { return ccm.getString(prefix + i + ".description"); }
	private int getPassiveID(int i) { return ccm.getInt(prefix + i + ".passiveID"); }
	private int setRestrictionID(int i) { return ccm.getInt(prefix + i + ".restrictionID"); }
	private List<Integer> getStatGrowth(int i) { return converter.getIntegerListFromString(ccm.getString(prefix + i + ".statGrowth")); }
	private List<Integer> getSpells(int i) { return converter.getIntegerListFromString(ccm.getString(prefix + i + ".spellIDs")); }
	
	public ClassConfig()
	{
		super("Classes");
		handleConfig();
		loadConfig();
	}
	
	private void handleConfig()
	{
		if (getVersion() < 1.0)
		{
			//Set the version.
			setVersion(1.0);
			
			//Set all the default classes.
			setName(0, "Swordsman");
			setName(1, "Assassin");
			setName(2, "Defender");
			setName(3, "Wizard");
			setName(4, "Berserker");
			
			//Set default descriptions
			setDescription(0, "Close range melee warrior.");
			setDescription(1, "Ranged long range assassin.");
			setDescription(2, "Close ranged melee tank.");
			setDescription(3, "Versatile Magician with high risk/reward.");
			setDescription(4, "Incredible Melee Devestation Unit.");
			
			//Set all the default spells, using my quite complicated, but extremely line efficient algorithm.
			int a = 0;
			
			for (int i = 0; i < 5; i++)
			{
				setSpells(i,a,a+1,a+2,a+3,a+4);
				a += 5;
			}
			
			//Set all the class passives
			setPassiveID(0, passive_ScalingArrows);
			setPassiveID(1, passive_CounterAttack);
			setPassiveID(2, passive_StrongerParry);
			setPassiveID(4, passive_BattleLust);
			
			//Set restrictions for certain classes.
			setRestrictionID(1,0); //Only can use bows for magic.
			setRestrictionID(3,1); //Only can use sticks for magic.
			
			//Set stat growths for all the classes.
			setStatGrowth(0,3,3,2,2);
			setStatGrowth(1,5,1,2,2);
			setStatGrowth(2,1,5,2,2);
			setStatGrowth(3,0,2,4,4);
			setStatGrowth(4,3,5,0,2);
		}
	}
	
	private void loadConfig()
	{
		//We load data from configuration files now.
		classCount = countClasses();
		
		rpgClass = new RpgClass[classCount];
		
		for (int i = 0; i < classCount; i++)
			rpgClass[i] = new RpgClass(i, getName(i), getDescription(i), getPassiveID(i), setRestrictionID(i), getStatGrowth(i), getSpells(i));
	}
	
	private int countClasses()
	{
		//Variable Declarations
		int count = 0;
		
		//Count up the classes based on having a name.
		for (int i = 0; i < CLASS_COUNT_MAX; i++)
		{
			if (getName(i) != null)
			{
				if (!getName(i).equals(""))
					count++;
			}
		}
		
		return count;
	}
	
	public RpgClass getClassWithPassive(int passiveID)
	{
		for (RpgClass currentClass : rpgClass)
		{
			if (currentClass.getPassiveID() == passiveID)
				return currentClass;
		}
		
		return null;
	}
	
	public RpgClass getClassByName(String className)
	{
		for (RpgClass currentClass : rpgClass)
		{
			if (currentClass.getName() == className)
				return currentClass;
		}
		
		return null;
	}
	
	public RpgClass getClassByID(int classID)
	{
		for (RpgClass currentClass : rpgClass)
		{
			if (currentClass.getID() == classID)
				return currentClass;
		}
		
		return null;
	}
}


/*
- Class Manager should load up spells first.
- Class Manager then loads up the classes.
*/