package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ListGetter;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Events.DungeonEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class DungeonConfig extends ConfigGod
{
	// Constructor
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
	
	public void addNewDungeon(String dungeonName)
	{
		//Variable Declarations
		World w1 = Bukkit.getServer().getWorlds().get(0);
		Location empty  = new Location(w1,0,0,0,0,0);
		
		for (int i = 0; i < 1000; i++)
		{
			if (getName(i) == null || getName(i).equalsIgnoreCase(dungeonName))
			{
				setName(i, dungeonName);
				setEntryFee(i, 50);
				setTimerJoin(i, 60);
				setTimerEnd(i, 600);
				setPlayerLevelRequirementMinimum(i,0);
				setPlayerLevelRequirementMaximum(i,100);
				setMobsToSpawnCount(i,50);
				setLobby(i,empty);
				setStart(i,empty);
				setExit(i,empty);
				setBossSpawn(i,empty);
				
				setTreasureStart(i,0,empty);
				setTreasureEnd(i,0,empty);
				int maxIndex = addToSpawnBox1(i,empty);
				setSpawnBox2(i,maxIndex,empty);
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
		
		initializeDungeons();
	}
	
	/****************************************************************
	 ^ Configuration Accessing Methods 
	 * Helper functions
	****************************************************************/

	public Location getLocation(int dungeonNumber, String field) { return fcw.getLocation(getDungeonPrefix(dungeonNumber) + field); }
	public void removeDungeon(int dungeonNumber) { fcw.setNull(prefix + dungeonNumber); initializeDungeons(); } // Set null then refresh dungeons.
	
	private String getDungeonPrefix(int dungeonNumber) { return prefix + dungeonNumber + "."; }
	
	//Do NOT call in constructor or you will get an error.
	public void initializeDungeons()
	{
		// Variable Declarations/Initializations
		String ref = "";
		int count = 0;
		FC_Rpg.trueDungeonNumbers = new HashMap<Integer, Integer>();
		
		// Attempt to store all dungeon names.
		for (int i = 0; i < 1000; i++)
		{
			ref = FC_Rpg.dungeonConfig.getName(i);
			
			if (ref != null)
			{
				FC_Rpg.trueDungeonNumbers.put(i, count);
				count++;
			}
		}

		// Store number of dungeons.
		FC_Rpg.dungeonCount = FC_Rpg.trueDungeonNumbers.size();

		// Create dungeonEvents based on size of dungeons.
		FC_Rpg.dungeonEventArray = new DungeonEvent[FC_Rpg.dungeonCount];

		// Initate the dungeons.
		for (int i = 0; i < FC_Rpg.dungeonCount; i++)
			FC_Rpg.dungeonEventArray[i] = new DungeonEvent(i);
	}
	
	/********************************
	 * Sets section
	********************************/
	
	public void setName(int dungeonNumber, String x) { fcw.set(getDungeonPrefix(dungeonNumber) + "name", x); }
	public void setEntryFee(int dungeonNumber, double x) { fcw.set(getDungeonPrefix(dungeonNumber) + "entryFee", x); }
	public void setTimerJoin(int dungeonNumber, int x) { fcw.set(getDungeonPrefix(dungeonNumber) + "timer.join", x); }
	public void setTimerEnd(int dungeonNumber, int x) { fcw.set(getDungeonPrefix(dungeonNumber) + "timer.end", x); }
	public void setMobsToSpawnCount(int dungeonNumber, int x) { fcw.set(getDungeonPrefix(dungeonNumber) + "mobsToSpawnCount", x); }
	public void setPlayerLevelRequirementMinimum(int dungeonNumber, int x) { fcw.set(getDungeonPrefix(dungeonNumber) + "playerLevelRequirement.minimum", x); }
	public void setPlayerLevelRequirementMaximum(int dungeonNumber, int x) { fcw.set(getDungeonPrefix(dungeonNumber) + "playerLevelRequirement.maximum", x); }
	public void setLobby(int dungeonNumber, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "lobby", loc); }
	public void setStart(int dungeonNumber, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "start", loc);  }
	public void setExit(int dungeonNumber, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "exit", loc);  }
	public void setBossSpawn(int dungeonNumber, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "bossSpawn", loc); }
	
	/********************************
	 - Treasure Based Sets
	********************************/
	
	public void setTreasureStartNull(int dungeonNumber, int index) { fcw.setNull(getDungeonPrefix(dungeonNumber) + "treasure.start." + index); }
	public void setTreasureStart(int dungeonNumber, int index, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "treasure.start." + index, loc); }
	public void addToTreasureStart(int dungeonNumber, Location loc) 
	{
		//Variable Declarations
		int emptyIndex = -1;
		Location loc2 = null;
		
		do {
			emptyIndex++;
			loc2 = getLocation(dungeonNumber, "treasure.start." + emptyIndex);
		} while (loc2 != null);
		
		fcw.setLocation(getDungeonPrefix(dungeonNumber) + "treasure.start." + emptyIndex, loc); 
	}
	
	public void setTreasureEndNull(int dungeonNumber, int index) { fcw.setNull(getDungeonPrefix(dungeonNumber) + "treasure.end." + index); }
	public void setTreasureEnd(int dungeonNumber, int index, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "treasure.end." + index, loc); }
	public void attToTreasureEnd(int dungeonNumber, Location loc) 
	{
		//Variable Declarations
		int emptyIndex = -1;
		Location loc2 = null;
		
		do {
			emptyIndex++;
			loc2 = getLocation(dungeonNumber, "treasure.end." + emptyIndex);
		} while (loc2 != null);
		
		fcw.setLocation(getDungeonPrefix(dungeonNumber) + "treasure.end." + emptyIndex, loc); 
	}
	
	/********************************
	 - Spawn Box Based Sets
	********************************/
	
	public void setSpawnBoxesNull(int dungeonNumber, int index) { fcw.setNull(getDungeonPrefix(dungeonNumber) + "spawnBox.selection1." + index, getDungeonPrefix(dungeonNumber) + "spawnBox.selection2." + index); }
	public void setSpawnBox1(int dungeonNumber, int index, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "spawnBox.selection1." + index, loc); }
	public void setSpawnBox2(int dungeonNumber, int index, Location loc) { fcw.setLocation(getDungeonPrefix(dungeonNumber) + "spawnBox.selection2." + index,loc); }	// The index is generally acquired from addToSpawnBox1()
	public void setSpawnChance(int dungeonNumber, int index, int x) { fcw.set(getDungeonPrefix(dungeonNumber) + "spawnBox.spawnChance." + index, x); }
	public void setMobList(int dungeonNumber, int index, List<String> x) { fcw.setCustomList(getDungeonPrefix(dungeonNumber) + "spawnBox.mobList." + index, x); }
	public int addToSpawnBox1(int dungeonNumber, Location newLoc)
	{
		//Variable Declarations
		int emptyIndex = 0;
		Location loc = null;
		
		try
		{
			//Store location 0.
			loc = getLocation(dungeonNumber, "spawnBox.selection1." + emptyIndex);
			
			//Make sure to put the new spawn point at the location.
			while (loc != null)
			{
				emptyIndex++;
				loc = getLocation(dungeonNumber, "spawnBox.selection1." + emptyIndex);
			}
		}
		catch (NullPointerException e) { }
		
		//Set the location
		fcw.setLocation(getDungeonPrefix(dungeonNumber) + "spawnBox.selection1." + emptyIndex,newLoc);
		
		//Return an emptyIndex.
		return emptyIndex;
	}
	
	/********************************
	 * Gets Section
	********************************/
	
	public String getName(int dungeonNumber) { return fcw.getString(getDungeonPrefix(dungeonNumber) + "name"); }
	public double getEntryFee(int dungeonNumber) { return fcw.getDouble(getDungeonPrefix(dungeonNumber) + "entryFee"); }
	public int getTimerJoin(int dungeonNumber) { return fcw.getInt(getDungeonPrefix(dungeonNumber) + "timer.join"); }
	public int getTimerEnd(int dungeonNumber) { return fcw.getInt(getDungeonPrefix(dungeonNumber) + "timer.end"); }
	public int getMobsToSpawnCount(int dungeonNumber) { return fcw.getInt(getDungeonPrefix(dungeonNumber) + "mobsToSpawnCount"); }
	public int getPlayerLevelRequirementMinimum(int dungeonNumber) { return fcw.getInt(getDungeonPrefix(dungeonNumber) + "playerLevelRequirement.minimum"); }
	public int getPlayerLevelRequirementMaximum(int dungeonNumber) { return fcw.getInt(getDungeonPrefix(dungeonNumber) + "playerLevelRequirement.maximum"); }
	public Location getLobby(int dungeonNumber) { return getLocation(dungeonNumber,"lobby"); }
	public Location getStart(int dungeonNumber) { return getLocation(dungeonNumber,"start"); }
	public Location getExit(int dungeonNumber) { return getLocation(dungeonNumber,"exit"); }
	public Location getBossSpawn(int dungeonNumber) { return getLocation(dungeonNumber,"bossSpawn"); }
	
	public List<Location> getTreasureStart(int dungeonNumber) { ListGetter lg = new ListGetter(fcw, getDungeonPrefix(dungeonNumber) + "treasure.start"); return lg.getLocationList(); }
	public List<Location> getTreasureEnd(int dungeonNumber) { ListGetter lg = new ListGetter(fcw, getDungeonPrefix(dungeonNumber) + "treasure.end"); return lg.getLocationList(); }
	
	public Location getSpawnBox1(int dungeonNumber, int index) { return getLocation(dungeonNumber,"spawnBox.selection1." + index); }
	public Location getSpawnBox2(int dungeonNumber, int index) { return getLocation(dungeonNumber,"spawnBox.selection2." + index); }
	public int getSpawnChance(int dungeonNumber, int index) { return fcw.getInt(getDungeonPrefix(dungeonNumber) + "spawnBox.spawnChance." + index); }
	
	public List<EntityType> getMobList(int dungeonNumber, int index) 
	{
		List<String> entityTypeStringList = converter.getStringListFromString(fcw.getStringS(getDungeonPrefix(dungeonNumber) + "spawnBox.mobList." + index));
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
}




























