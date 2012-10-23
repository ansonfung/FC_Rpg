package me.Destro168.Configs;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.ConfigManagers.FileConfigPlus;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Location;

public class DungeonConfig extends ConfigGod
{
	private WorldConfig wm;
	
	private void setName(int dNum, String x) { ccm.set(getDP(dNum) + "name", x); }
	private void setCost(int dNum, double x) { ccm.set(getDP(dNum) + "cost", x); }
	private void setSpawnCount(int dNum, int x) { ccm.set(getDP(dNum) + "spawnCount", x); }
	private void setLevelMin(int dNum, int x) { ccm.set(getDP(dNum) + "levelMin", x); }
	private void setLevelMax(int dNum, int x) { ccm.set(getDP(dNum) + "levelMax", x); }
	private void setLobby(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"lobby",world,x,y,z,a,b); }
	private void setStart(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"start",world,x,y,z,a,b); }
	private void setExit(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"exit",world,x,y,z,a,b); }
	private void setBossSpawn(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"bossSpawn",world,x,y,z,a,b); }
	private void setTreasureChest(int dNum, String world, double x, double y, double z, float a, float b) { setLocation(dNum,"treasure",world,x,y,z,a,b); }
	private int setRange1(int dNum, String world, double x, double y, double z, float a, float b) 
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
	private void setRange2(int dNum, int index, String world, double x, double y, double z, float a, float b) //The index is acquired from setMin()
	{
		String fullReferenceString = "range2." + index;
		setLocation(dNum,fullReferenceString,world,x,y,z,a,b);
	}
	private void setSpawnChance(int dNum, int index, int x) //The index is acquired from setRange1()
	{
		ccm.set(getDP(dNum) + "spawnChance." + index, x);
	}
	
	public String getName(int dNum) { return ccm.getString(getDP(dNum) + "name"); }
	public double getCost(int dNum) { return ccm.getDouble(getDP(dNum) + "cost"); }
	public int getSpawnCount(int dNum) { return ccm.getInt(getDP(dNum) + "spawnCount"); }
	public int getLevelMin(int dNum) { return ccm.getInt(getDP(dNum) + "levelMin"); }
	public int getLevelMax(int dNum) { return ccm.getInt(getDP(dNum) + "levelMax"); }
	public Location getLobby(int dNum) { return getLocation(dNum,"lobby"); }
	public Location getStart(int dNum) { return getLocation(dNum,"start"); }
	public Location getExit(int dNum) { return getLocation(dNum,"exit"); }
	public Location getBossSpawn(int dNum) { return getLocation(dNum,"bossSpawn"); }
	public Location getTreasureChest(int dNum) { return getLocation(dNum,"treasure"); }
	public Location getRange1(int dNum, int index) { return getLocation(dNum,"range1." + index); }
	public Location getRange2(int dNum, int index) { return getLocation(dNum,"range2." + index); }
	public int getSpawnChance(int dNum, int index) { return ccm.getInt(getDP(dNum) + "spawnChance." + index); }
	private void setLocation(int dNum, String field, String world, double x, double y, double z, float a, float b)
	{
		FileConfigPlus fcp = new FileConfigPlus(ccm.getConfig());
		fcp.setLocation(getDP(dNum) + field, world, x, y, z, a, b);
	}
	private Location getLocation(int dNum, String field)
	{
		FileConfigPlus fcp = new FileConfigPlus(ccm.getConfig());
		return fcp.getLocation(getDP(dNum) + field);
	}
	
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
}




























