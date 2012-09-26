package me.Destro168.Configs;

import me.Destro168.ConfigManagers.FileConfigPlus;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class PvpManager 
{
	private FileConfiguration config;
	private String pvpPrefix = "Pvp.";
	private FileConfigPlus fcp;
	private WorldManager wm;
	
	private void setVersion(double x) { config.set(pvpPrefix + "version", x); }
	private double getVersion() { return config.getDouble(pvpPrefix + "version"); }

	public void setLobby1(String world, double x, double y, double z, float a, float b) { fcp.setLocation(pvpPrefix + "lobby1", world, x, y, z, a, b); }
	public void setLobby2(String world, double x, double y, double z, float a, float b) { fcp.setLocation(pvpPrefix + "lobby2", world, x, y, z, a, b); }
	public void setSpawn1(String world, double x, double y, double z, float a, float b) { fcp.setLocation(pvpPrefix + "spawn1", world, x, y, z, a, b); }
	public void setSpawn2(String world, double x, double y, double z, float a, float b) { fcp.setLocation(pvpPrefix + "spawn2", world, x, y, z, a, b); }
	
	public Location getLobby1() { return fcp.getLocation(pvpPrefix + "lobby1"); }
	public Location getLobby2() { return fcp.getLocation(pvpPrefix + "lobby2"); }
	public Location getSpawn1() { return fcp.getLocation(pvpPrefix + "spawn1"); }
	public Location getSpawn2() { return fcp.getLocation(pvpPrefix + "spawn2"); }
	
	public PvpManager()
	{
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//Variable Declarations
		config = FC_Rpg.plugin.getConfig();
		wm = new WorldManager();
		String pvpWorldName = wm.getPvpWorld().getName();
		
		//Initialize globals.
		fcp = new FileConfigPlus(config);
		wm = new WorldManager();
		
		//Create first section if not created.
		if (getVersion() < 1.0)
		{
			//Update version
			setVersion(1.0);
			
			//Begin setting default settings.
			setSpawn1(pvpWorldName, 180,81,106,180,0);
			setSpawn2(pvpWorldName, 177,81,72,270,0);
			setLobby1(pvpWorldName,170,99,82,270,0);
			setLobby2(pvpWorldName,185,99,97,90,0);
		}
		
		FC_Rpg.plugin.saveConfig();
	}
}





















