package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldConfig extends ConfigGod
{
	public void setWorldSpawn(Location loc) { fcw.setLocation(prefix + loc.getWorld().getName() + ".spawn", loc); }
	public void setLevelOne(Location loc) { fcw.setLocation(prefix + loc.getWorld().getName() + ".levelOne", loc); }
	public void setWorldSpawn(String worldName, double x, double y, double z, float a, float b) { fcw.setLocation(prefix + worldName + ".spawn", worldName, x, y, z, a, b); }
	public void setLevelOne(String worldName, double x, double y, double z, float a, float b) { fcw.setLocation(prefix + worldName + ".levelOne", worldName, x, y, z, a, b); }
	
	public void setWorldType(String name, int x) { fcw.set(prefix + name + ".worldType", x); }	//0 - Vanilla/1 - Creative/Nether/End/Other
	public void setIsRpg(String name, boolean x) { fcw.set(prefix + name + ".isRpg", x); }	//Vanilla or Rpg
	public void setIsPvp(String name, boolean x) { fcw.set(prefix + name + ".isPvp", x); }	//Pvp
	public void setIsSpawn(String name, boolean x) { fcw.set(prefix + name + ".isSpawn", x); }	//Pvp
	public void setIsAoEWorld(String name, boolean x) { fcw.set(prefix + name + ".isAoEWorld", x); }	//Pvp
	public void setIsMobWorld(String name, boolean x) { fcw.set(prefix + name + ".isMobWorld", x); }	//Pvp
	public void setLevelCap(String name, int x) { fcw.set(prefix + name + ".levelCap", x); }	//Pvp
	public void setScaleX(String name, double x) { fcw.set(prefix + name + ".scale.x", x); }	//Pvp
	public void setScaleY(String name, double x) { fcw.set(prefix + name + ".scale.y", x); }	//Pvp
	public void setScaleZ(String name, double x) { fcw.set(prefix + name + ".scale.z", x); }	//Pvp
	
	public Location getWorldSpawn(String worldName) { return fcw.getLocation(prefix + worldName + ".spawn"); }
	public Location getLevelOne(String worldName) { return fcw.getLocation(prefix + worldName + ".levelOne"); }
	public int getWorldType(String name) { return fcw.getInt(prefix + name + ".worldType"); }
	public boolean getIsRpg(String name) { return fcw.getBoolean(prefix + name + ".isRpg"); }
	public boolean getIsPvp(String name) { return fcw.getBoolean(prefix + name + ".isPvp"); }
	public boolean getIsSpawn(String name) { return fcw.getBoolean(prefix + name + ".isSpawn"); }
	public boolean getIsAoEWorld(String name) { return fcw.getBoolean(prefix + name + ".isAoEWorld"); }
	public boolean getIsMobWorld(String name) { return fcw.getBoolean(prefix + name + ".isMobWorld"); }
	public int getLevelCap(String name) { return fcw.getInt(prefix + name + ".levelCap"); }
	public double getScaleX(String name) { return fcw.getDouble(prefix + name + ".scale.x"); }
	public double getScaleY(String name) { return fcw.getDouble(prefix + name + ".scale.y"); }
	public double getScaleZ(String name) { return fcw.getDouble(prefix + name + ".scale.z"); }
	public void removeWorld(String name) { fcw.set(prefix + name, null); }
	
	public WorldConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Worlds");
		handleUpdates();
	}
	
	private void handleUpdates()
	{
		//Create initial config if no previous versions are found.
		if (getVersion() < 0.1)
		{
			//Update version.
			setVersion(0.1);
			
			//Set up the rest of the world information.
			for (World world : Bukkit.getServer().getWorlds())
				addNewWorld(world.getName());
			
			//Save the config.
			FC_Rpg.plugin.saveConfig();
		}
		
		//Update verison to 0.3
		if (getVersion() < 0.4)
		{
			setVersion(0.4);
			
			for (World world : getRpgWorlds())
			{
				setLevelCap(world.getName(), -1);
				setScaleX(world.getName(), 20);
				setScaleY(world.getName(), -25);
				setScaleZ(world.getName(), 20);
			}
		}
	}
	
	public List<World> getRpgWorlds()
	{
		List<World> worldList = new ArrayList<World>();
		
		for (World w: Bukkit.getServer().getWorlds())
		{
			if (getIsRpgWorld(w.getName()))
				worldList.add(w);
		}
		
		return worldList;
	}
	
	public boolean getIsRpgWorld(String worldName)
	{
		//Check if a world is an rpg world or not.
		if (getIsRpg(worldName))
			return true;
		
		return false;
	}
	
	public World getSpawnWorld()
	{
		//Go through all worlds loaded and see if the world is spawn or not. Return that world.
		for (World world : Bukkit.getServer().getWorlds())
		{
			if (getIsSpawn(world.getName()))
				return world;
		}
		
		return null;
	}
	
	public boolean isCreativeWorld(World world)
	{
		if (getWorldType(world.getName()) == 1)
			return true;
		
		return false;
	}
	
	public World getPvpWorld()
	{
		//Go through all worlds loaded and see if the world is spawn or not. Return that world.
		for (World world : Bukkit.getServer().getWorlds())
		{
			if (getIsPvp(world.getName()))
				return world;
		}
		
		return null;
	}
	
	public boolean isCreated(String worldName)
	{
		if (fcw.get(prefix + worldName) == null)
			return false;
		
		return true;
	}
	
	public boolean addNewWorld(String worldName)
	{
		//Variable Declarations
		boolean createNew = true;
		
		//Only add if world exists.
		if (Bukkit.getServer().getWorld(worldName) == null)
			return false;
		
		for (World w : Bukkit.getServer().getWorlds())
		{
			if (isCreated(w.getName()))
			{
				createNew = false;
				break;
			}
		}
		
		//Set the world type to vanilla.
		setWorldType(worldName,0);
		
		//Set all worlds to rpg by default.
		setIsRpg(worldName, false);
		
		if (createNew == true)
			setNewWorldSettings(worldName);
		else
			setWorldSettings(worldName);
		
		//Set aoe mining to disabled by default.
		setIsAoEWorld(worldName, false);
		
		//Set the worlds spawn last, makes config look nice. :)
		setWorldSpawn(worldName,0,110,0,0,0);
		
		//Set level one.
		setLevelOne(worldName,0,70,0,0,0);
		
		return true;
	}
	
	public void setNewWorldSettings(String worldName)
	{
		setIsPvp(worldName, true);
		setIsSpawn(worldName, true);
		setIsMobWorld(worldName, false);
	}
	
	//Only set one world pvp and to being the spawn world. Run once then lock out.
	public void setWorldSettings(String worldName)
	{
		setIsPvp(worldName, false);
		setIsSpawn(worldName, false);
		setIsMobWorld(worldName, true);
	}
}











