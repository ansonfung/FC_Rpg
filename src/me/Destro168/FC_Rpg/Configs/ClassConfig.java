package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ListGetter;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;

public class ClassConfig extends ConfigGod
{
	public List<RpgClass> rpgClassList;
	
	public ClassConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Classes");
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
			setName(5, "DebugClass");
			
			//Set default descriptions
			setDescription(0, "Close range melee warrior.");
			setDescription(1, "Ranged long range assassin.");
			setDescription(2, "Close ranged melee tank.");
			setDescription(3, "Versatile Magician with high risk/reward.");
			setDescription(4, "Incredible Melee Devestation Unit.");
			setDescription(5, "A class designed for testing.");
			
			//Set all the default spells, using my quite complicated, but extremely line efficient algorithm.
			int a = 0;
			
			for (int i = 0; i < 5; i++)
			{
				setSpells(i,a,a+1,a+2,a+3,a+4);
				a += 5;
			}
			
			fcw.set(prefix + 5 + ".spellIDs", getDebugClassString());
			
			//Set all the class passives
			setPassiveID(0, BalanceConfig.passive_CounterAttack);
			setPassiveID(1, BalanceConfig.passive_ScalingArrows);
			setPassiveID(2, BalanceConfig.passive_StrongerParry);
			setPassiveID(3, BalanceConfig.passive_InnerFire);
			setPassiveID(4, BalanceConfig.passive_BattleLust);
			
			//Set restrictions for certain classes.
			setRestrictionID(1,1); //Only can use bows for magic.
			setRestrictionID(3,2); //Only can use sticks for magic.
			
			//Set stat growths for all the classes.
			setStatGrowth(0,5,4,0,1);
			setStatGrowth(1,6,2,0,2);
			setStatGrowth(2,4,5,0,1);
			setStatGrowth(3,0,2,4,4);
			setStatGrowth(4,8,1,0,1);
			setStatGrowth(5,999,999,999,999);
		}
		
		// Add in promotion groups for classes.
		if (getVersion() < 1.1)
		{
			setVersion(1.1);
			
			for (int i : getClassFieldList())
			{
				setGroupPromotion(i, "Member");
				setGroupDemotion(i, "Guest");
			}
		}
	}
	
	private String getDebugClassString()
	{
		String a = "1";
		
		for (int i = 0; i < 25; i++)
			a += "," + String.valueOf(i);
		
		return a;
	}
	
	private void loadConfig()
	{
		rpgClassList = new ArrayList<RpgClass>();
		
		//We load data from configuration files now.
		for (int i : getClassFieldList())
			rpgClassList.add(new RpgClass(i, getName(i), getDescription(i), getPassiveID(i), getRestrictionID(i), getStatGrowth(i), getSpells(i)));
	}
	
	public RpgClass getRpgClass(String className)
	{
		for (RpgClass currentClass : rpgClassList)
		{
			if (currentClass.getName().equalsIgnoreCase(className))
				return currentClass;
		}
		
		return null;
	}
	
	public RpgClass getRpgClass(int classNumber)
	{
		for (RpgClass currentClass : rpgClassList)
		{
			if (currentClass.getID() == classNumber)
				return currentClass;
		}
		
		return null;
	}
	
	/****************************************************************
	 ^ Configuration Accessing Methods 
	 - All Dynamically Accessed
	****************************************************************/
	
	public List<Integer> getClassFieldList() { ListGetter lg = new ListGetter(fcw, prefix); return lg.getFieldIntegerList(); }
	
	private void setName(int i, String x) { fcw.set(prefix + i + ".name", x); }
	private void setDescription(int i, String x) { fcw.set(prefix + i + ".description", x); }
	private void setGroupPromotion(int i, String x) { fcw.set(prefix + i + ".group.promotion", x); }
	private void setGroupDemotion(int i, String x) { fcw.set(prefix + i + ".group.demotion", x); }
	private void setPassiveID(int i, int x) { fcw.set(prefix + i + ".passiveID", x); }
	private void setRestrictionID(int i, int x) { fcw.set(prefix + i + ".restrictionID", x); }
	private void setStatGrowth(int i, int a, int b, int c, int d) { fcw.set(prefix + i + ".statGrowth", a + "," + b + "," + c + "," + d); }
	private void setSpells(int i, int a, int b, int c, int d, int e) { fcw.set(prefix + i + ".spellIDs", a + "," + b + "," + c + "," + d + "," + e); }
	
	private String getName(int i) { return fcw.getString(prefix + i + ".name");  }
	private String getDescription(int i) { return fcw.getString(prefix + i + ".description"); }
	public String getGroupPromotion(int i) { return fcw.getString(prefix + i + ".group.promotion"); }
	public String getGroupDemotion(int i) { return fcw.getString(prefix + i + ".group.demotion"); }
	private int getPassiveID(int i) { if (fcw.isSet(prefix + i + ".passiveID")) return fcw.getInt(prefix + i + ".passiveID"); else return -1; }
	private int getRestrictionID(int i) { if (fcw.isSet(prefix + i + ".restrictionID")) return fcw.getInt(prefix + i + ".restrictionID"); else return -1; }
	private List<Integer> getStatGrowth(int i) { return fcw.getCustomIntegerList(prefix + i + ".statGrowth"); }
	private List<Integer> getSpells(int i) { return fcw.getCustomIntegerList(prefix + i + ".spellIDs"); }
}


/*
- Class Manager should load up spells first.
- Class Manager then loads up the classes.
*/