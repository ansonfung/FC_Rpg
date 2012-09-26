package me.Destro168.Configs;

import me.Destro168.ConfigManagers.FileConfigPlus;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class WorldManager 
{
	private FileConfiguration config;
	private final String worldPrefix = "Worlds.";
	private FileConfigPlus fcp;
	
	private void setVersion(double x) { config.set(worldPrefix + "version", x); }
	private double getVersion() { return config.getDouble(worldPrefix + "version"); }
	
	public void setWorldSpawn(String worldName, double x, double y, double z, float a, float b) { fcp.setLocation(worldPrefix + worldName + ".spawn", worldName, x, y, z, a, b); }
	public void setWorldSpawn(Location loc) { setWorldSpawn(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); }
	private void setWorldType(String name, int x) { config.set(worldPrefix + name + ".worldType", x); }	//0 - Vanilla/1 - Creative/Nether/End/Other
	private void setIsRpg(String name, boolean x) { config.set(worldPrefix + name + ".isRpg", x); }	//Vanilla or Rpg
	private void setIsPvp(String name, boolean x) { config.set(worldPrefix + name + ".isPvp", x); }	//Pvp
	private void setIsSpawn(String name, boolean x) { config.set(worldPrefix + name + ".isSpawn", x); }	//Pvp
	private void setIsAoEWorld(String name, boolean x) { config.set(worldPrefix + name + ".isAoEWorld", x); }	//Pvp
	private void setIsMobWorld(String name, boolean x) { config.set(worldPrefix + name + ".isMobWorld", x); }	//Pvp
	
	public Location getWorldSpawn(String worldName) { return fcp.getLocation(worldPrefix + worldName + ".spawn"); }
	public int getWorldType(String name) { return config.getInt(worldPrefix + name + ".worldType"); }
	public boolean getIsRpg(String name) { return config.getBoolean(worldPrefix + name + ".isRpg"); }
	public boolean getIsPvp(String name) { return config.getBoolean(worldPrefix + name + ".isPvp"); }
	public boolean getIsSpawn(String name) { return config.getBoolean(worldPrefix + name + ".isSpawn"); }
	public boolean getIsAoEWorld(String name) { return config.getBoolean(worldPrefix + name + ".isAoEWorld"); }
	public boolean getIsMobWorld(String name) { return config.getBoolean(worldPrefix + name + ".isMobWorld"); }
	
	public WorldManager()
	{
		handleUpdates();
	}
	
	private void handleUpdates()
	{
		//Variable Declarations
		String worldName = "";
		boolean oneRun = false;
		
		//Load the config.
		config = FC_Rpg.plugin.getConfig();
		
		//Update the fcp
		fcp = new FileConfigPlus(config);
		
		//Create initial config if no previous versions are found.
		if (getVersion() < 1.01)
		{
			//Update version.
			setVersion(1.01);
			
			//Set up the rest of the world information.
			for (World world : Bukkit.getServer().getWorlds())
			{
				worldName = world.getName();
				
				//Set the world type to vanilla.
				setWorldType(worldName,0);
				
				//Set all worlds to rpg by default.
				setIsRpg(worldName, true);
				
				//Only set one world pvp and to being the spawn world. Run once then lock out.
				if (oneRun == false)
				{
					setIsPvp(worldName, true);
					setIsSpawn(worldName, true);
					setIsMobWorld(worldName, false);
					oneRun = true;
				}
				else
				{
					setIsPvp(worldName, false);
					setIsSpawn(worldName, false);
					setIsMobWorld(worldName, true);
				}
				
				//Set aoe mining to disabled by default.
				setIsAoEWorld(worldName, false);
				
				//Set the worlds spawn last, makes config look nice. :)
				setWorldSpawn(worldName,0,110,0,0,0);
			}

			//Save the config.
			FC_Rpg.plugin.saveConfig();
		}
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
}











