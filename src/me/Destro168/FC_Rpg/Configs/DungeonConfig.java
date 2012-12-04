package me.Destro168.FC_Rpg.Configs;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DungeonConfig extends ConfigGod
{
	private WorldConfig wm;
	
	public void setName(int dNum, String x) { fcw.set(getDP(dNum) + "name", x); }
	public void setCost(int dNum, double x) { fcw.set(getDP(dNum) + "cost", x); }
	public void setSpawnCount(int dNum, int x) { fcw.set(getDP(dNum) + "spawnCount", x); }
	public void setLevelMin(int dNum, int x) { fcw.set(getDP(dNum) + "levelMin", x); }
	public void setLevelMax(int dNum, int x) { fcw.set(getDP(dNum) + "levelMax", x); }
	public void setLobby(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"lobby",world,x,y,z,a,b); }
	public void setStart(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"start",world,x,y,z,a,b); }
	public void setExit(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"exit",world,x,y,z,a,b); }
	public void setBossSpawn(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"bossSpawn",world,x,y,z,a,b); }
	public void setTreasureChest(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"treasure",world,x,y,z,a,b); }
	public int setRange1(int dNum, String world, double x, double y, double z, float a, float b) 
	{
		//Variable Declarations
		int emptyIndex = 0;
		Location loc = null;
		
		try
		{
			//Store location 0.
			loc = getLocation(dNum, "range1." + emptyIndex);
			
			//Make sure to put the new spawn point at the location.
			while (loc != null)
			{
				emptyIndex++;
				loc = getLocation(dNum, "range1." + emptyIndex);
			}
		}
		catch (NullPointerException e) { }
		
		//Set the location
		setLocation(dNum,"range1." + emptyIndex,world,x,y,z,a,b);
		
		//Return an emptyIndex.
		return emptyIndex;
	}
	public void setRange2(int dNum, int index, String world, double x, double y, double z, float a, float b) //The index is acquired from setMin()
	{
		String fullReferenceString = "range2." + index;
		setLocation(dNum,fullReferenceString,world,x,y,z,a,b);
	}
	public void setSpawnChance(int dNum, int index, int x) //The index is acquired from setRange1()
	{
		fcw.set(getDP(dNum) + "spawnChance." + index, x);
	}
	
	public String getName(int dNum) { return fcw.getString(getDP(dNum) + "name"); }
	public double getCost(int dNum) { return fcw.getDouble(getDP(dNum) + "cost"); }
	public int getSpawnCount(int dNum) { return fcw.getInt(getDP(dNum) + "spawnCount"); }
	public int getLevelMin(int dNum) { return fcw.getInt(getDP(dNum) + "levelMin"); }
	public int getLevelMax(int dNum) { return fcw.getInt(getDP(dNum) + "levelMax"); }
	public Location getLobby(int dNum) { return getLocation(dNum,"lobby"); }
	public Location getStart(int dNum) { return getLocation(dNum,"start"); }
	public Location getExit(int dNum) { return getLocation(dNum,"exit"); }
	public Location getBossSpawn(int dNum) { return getLocation(dNum,"bossSpawn"); }
	public Location getTreasureChest(int dNum) { return getLocation(dNum,"treasure"); }
	public Location getRange1(int dNum, int index) { return getLocation(dNum,"range1." + index); }
	public Location getRange2(int dNum, int index) { return getLocation(dNum,"range2." + index); }
	public int getSpawnChance(int dNum, int index) { return fcw.getInt(getDP(dNum) + "spawnChance." + index); }
	
	public void setLocation(int dNum, String field, String world, double x, double y, double z, float a, float b)
	{
		fcw.setLocation(getDP(dNum) + field, world, x, y, z, a, b);
	}
	public Location getLocation(int dNum, String field) { return fcw.getLocation(getDP(dNum) + field); }
	
	public DungeonConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Dungeons");
		handleUpdates();
	}
	
	private void handleUpdates()
	{
		wm = new WorldConfig();
		
		String worldName = wm.getSpawnWorld().getName();
		int maxIndex;
		
		if (getVersion() < 0.1)
		{
			setVersion(0.1);
			
			//Default Dungeon 1
			setName(0,"Grasslands");
			setCost(0,50);
			setLevelMin(0,0);
			setLevelMax(0,19);
			setSpawnCount(0,50);
			setLobby(0,worldName,134,83,11,270,0);
			setStart(0,worldName,142.5,89,15.5,270,0);
			setExit(0,worldName,128,84,15,270,0);
			setBossSpawn(0,worldName,342,90,15.5,90,0);
			setTreasureChest(0,worldName,344,87,15,0,0);
			
			maxIndex = setRange1(0,worldName,345,89,26.5,0,0);
			setRange2(0,maxIndex,worldName,141,89,4.5,0,0);
			setSpawnChance(0,maxIndex,80);
			
			maxIndex = setRange1(0,worldName,-63.3,127,-71.3,0,0);
			setRange2(0,maxIndex,worldName,-93.7,127,-101.7,0,0);
			setSpawnChance(0,maxIndex,20);
			
			setName(1,"Stronghold");
			setCost(1,200);
			setLevelMin(1,20);
			setLevelMax(1,39);
			setSpawnCount(1,50);
			setLobby(1,worldName,134,83,-35,270,0);
			setStart(1,worldName,149,98,-31.5,270,0);
			setExit(1,worldName,128,84,-31,270,0);
			setBossSpawn(1,worldName,351,114,-31.5,90,0);
			setTreasureChest(1,worldName,354,114,-32,0,0);
			
			maxIndex = setRange1(1,worldName,352,100,-42,0,0);
			setRange2(1,maxIndex,worldName,146,100,-21,0,0);
			setSpawnChance(1,maxIndex,100);
		}
	}
	
	//Get Dungeon Prefix
	private String getDP(int dNum)
	{
		return prefix + dNum + ".";
	}
	
	public int getSpawnRangeCount(int dungeonNumber)
	{
		int count = 0;
		
		for (int i = 0; i < 1000; i++)
		{
			try
			{
				getRange2(dungeonNumber, i);
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
				setCost(i, 50);
				setLevelMin(i,0);
				setLevelMax(i,100);
				setSpawnCount(i,50);
				setLobby(i,worldName,0,0,0,0,0);
				setStart(i,worldName,0,0,0,0,0);
				setExit(i,worldName,0,0,0,0,0);
				setBossSpawn(i,worldName,0,0,0,0,0);
				setTreasureChest(i,worldName,0,0,0,0,0);
				
				int maxIndex = setRange1(i,worldName,0,0,0,0,0);
				setRange2(i,maxIndex,worldName,0,0,0,0,0);
				setSpawnChance(i,maxIndex,100);
				
				break;
			}
		}
	}
}




























