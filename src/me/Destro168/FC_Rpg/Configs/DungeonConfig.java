package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ListGetter;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class DungeonConfig extends ConfigGod
{
	private void setName(int dNum, String x) { fcw.set(getDP(dNum) + "name", x); }
	private void setEntryFee(int dNum, double x) { fcw.set(getDP(dNum) + "entryFee", x); }
	private void setTimerJoin(int dNum, int x) { fcw.set(getDP(dNum) + "timer.join", x); }
	private void setTimerEnd(int dNum, int x) { fcw.set(getDP(dNum) + "timer.end", x); }
	private void setMobsToSpawnCount(int dNum, int x) { fcw.set(getDP(dNum) + "mobsToSpawnCount", x); }
	private void setPlayerLevelRequirementMinimum(int dNum, int x) { fcw.set(getDP(dNum) + "playerLevelRequirement.minimum", x); }
	private void setPlayerLevelRequirementMaximum(int dNum, int x) { fcw.set(getDP(dNum) + "playerLevelRequirement.maximum", x); }
	private void setLobby(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"lobby",world,x,y,z,a,b); }
	private void setStart(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"start",world,x,y,z,a,b); }
	private void setExit(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"exit",world,x,y,z,a,b); }
	private void setBossSpawn(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"bossSpawn",world,x,y,z,a,b); }
	
	private void setTreasureStart(int dNum, int index, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"treasure.start." + index,world,x,y,z,a,b); }
	private void setTreasureEnd(int dNum, int index, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"treasure.end." + index,world,x,y,z,a,b); }
	
	private int setSpawnBox1(int dNum, String world, double x, double y, double z, float a, float b)
	{
		//Variable Declarations
		int emptyIndex = 0;
		Location loc = null;
		
		try
		{
			//Store location 0.
			loc = getLocation(dNum, "spawnBox.selection1." + emptyIndex);
			
			//Make sure to put the new spawn point at the location.
			while (loc != null)
			{
				emptyIndex++;
				loc = getLocation(dNum, "spawnBox.selection1." + emptyIndex);
			}
		}
		catch (NullPointerException e) { }
		
		//Set the location
		setLocation(dNum,"spawnBox.selection1." + emptyIndex,world,x,y,z,a,b);
		
		//Return an emptyIndex.
		return emptyIndex;
	}
	private void setSpawnBox2(int dNum, int index, String world, double x, double y, double z, float a, float b) //The index is acquired from setMin()
	{
		String fullReferenceString = "spawnBox.selection2." + index;
		setLocation(dNum,fullReferenceString,world,x,y,z,a,b);
	}
	private void setSpawnChance(int dNum, int index, int x)
	{
		fcw.set(getDP(dNum) + "spawnBox.spawnChance." + index, x);
	}

	private void setMobList(int dNum, int index, List<String> x)
	{
		fcw.setCustomList(getDP(dNum) + "spawnBox.mobList." + index, x);
	}
	
	public void setLocation(int dNum, String field, String world, double x, double y, double z, float a, float b) { fcw.setLocation(getDP(dNum) + field, world, x, y, z, a, b); }
	
	public String getName(int dNum) { return fcw.getStringS(getDP(dNum) + "name"); }
	public double getEntryFee(int dNum) { return fcw.getDoubleS(getDP(dNum) + "entryFee"); }
	public int getTimerJoin(int dNum) { return fcw.getIntS(getDP(dNum) + "timer.join"); }
	public int getTimerEnd(int dNum) { return fcw.getIntS(getDP(dNum) + "timer.end"); }
	public int getMobsToSpawnCount(int dNum) { return fcw.getIntS(getDP(dNum) + "mobsToSpawnCount"); }
	public int getPlayerLevelRequirementMinimum(int dNum) { return fcw.getIntS(getDP(dNum) + "playerLevelRequirement.minimum"); }
	public int getPlayerLevelRequirementMaximum(int dNum) { return fcw.getIntS(getDP(dNum) + "playerLevelRequirement.maximum"); }
	public Location getLobby(int dNum) { return getLocation(dNum,"lobby"); }
	public Location getStart(int dNum) { return getLocation(dNum,"start"); }
	public Location getExit(int dNum) { return getLocation(dNum,"exit"); }
	public Location getBossSpawn(int dNum) { return getLocation(dNum,"bossSpawn"); }
	public List<Location> getTreasureStart(int dNum) { ListGetter lg = new ListGetter(fcw, getDP(dNum) + "treasure.start"); return lg.getLocationList(); }
	public List<Location> getTreasureEnd(int dNum) { ListGetter lg = new ListGetter(fcw, getDP(dNum) + "treasure.end"); return lg.getLocationList(); }
	public Location getSpawnBox1(int dNum, int index) { return getLocation(dNum,"spawnBox.selection1." + index); }
	public Location getSpawnBox2(int dNum, int index) { return getLocation(dNum,"spawnBox.selection2." + index); }
	public int getSpawnChance(int dNum, int index) { return fcw.getIntS(getDP(dNum) + "spawnBox.spawnChance." + index); }
	
	public List<EntityType> getMobList(int dNum, int index) 
	{
		List<String> entityTypeStringList = converter.getStringListFromString(fcw.getStringS(getDP(dNum) + "spawnBox.mobList." + index));
		List<EntityType> entityTypeList = new ArrayList<EntityType>();
		
		for (String s : entityTypeStringList)
		{
			if (EntityType.fromName(s) == null)
			{
				FC_Rpg.plugin.getLogger().info("WARNING: " + s + " COULD NOT BE PARSED. Fix your moblist for this dungeon region.");
				entityTypeList.clear();
				entityTypeList.add(EntityType.ZOMBIE);
				return entityTypeList;
			}
			
			entityTypeList.add(EntityType.fromName(s));
		}
		
		return entityTypeList;
	}
	
	public Location getLocation(int dNum, String field) { return fcw.getLocation(getDP(dNum) + field); }
	
	//Get Dungeon Prefix
	private String getDP(int dNum) { return prefix + dNum + "."; }
	
	public DungeonConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Dungeons");
		handleUpdates();
	}
	
	private void handleUpdates()
	{
		//If no version established create new one.
		if (getVersion() < 1.0)
		{
			setVersion(1.0);
			
			//Add a default dungeon
			addNewDungeon("Grasslands");
		}
	}
	
	public int getSpawnRangeCount(int dungeonNumber)
	{
		int count = 0;
		
		for (int i = 0; i < 1000; i++)
		{
			try
			{
				getSpawnBox2(dungeonNumber, i);
				count++;
			}
			catch (NullPointerException e)
			{
				continue;
			}
		}
		
		return count;
	}
	
	public void addNewDungeon(String dungeonName)
	{
		for (int i = 0; i < 1000; i++)
		{
			if (getName(i) == null || getName(i) == dungeonName)
			{
				String worldName = Bukkit.getServer().getWorlds().get(0).getName();
				
				setName(i, dungeonName);
				setEntryFee(i, 50);
				setTimerJoin(i, 60);
				setTimerEnd(i, 600);
				setPlayerLevelRequirementMinimum(i,0);
				setPlayerLevelRequirementMaximum(i,100);
				setMobsToSpawnCount(i,50);
				setLobby(i,worldName,0,0,0,0,0);
				setStart(i,worldName,0,0,0,0,0);
				setExit(i,worldName,0,0,0,0,0);
				setBossSpawn(i,worldName,0,0,0,0,0);
				setTreasureStart(i,0,worldName,0,0,0,0,0);
				setTreasureEnd(i,0,worldName,0,0,0,0,0);
				int maxIndex = setSpawnBox1(i,worldName,0,0,0,0,0);
				setSpawnBox2(i,maxIndex,worldName,0,0,0,0,0);
				setSpawnChance(i,maxIndex,100);
				
				List<String> defaultMobs = new ArrayList<String>();
				
				defaultMobs.add("ZOMBIE");
				defaultMobs.add("PIGZOMBIE");
				defaultMobs.add("SKELETON");
				defaultMobs.add("SPIDER");
				
				setMobList(i, maxIndex, defaultMobs);
				
				break;
			}
		}
	}
}




























