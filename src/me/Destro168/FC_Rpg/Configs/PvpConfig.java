package me.Destro168.FC_Rpg.Configs;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Location;
import org.bukkit.World;

public class PvpConfig extends ConfigGod
{
	private void setPvpArenaAward(double x) { fcw.set(prefix + ".pvpArenaReward", x); }
	public void setLobby1(String world, double x, double y, double z, float a, float b) { fcw.setLocation(prefix + "lobby1", world, x, y, z, a, b); }
	public void setLobby2(String world, double x, double y, double z, float a, float b) { fcw.setLocation(prefix + "lobby2", world, x, y, z, a, b); }
	public void setSpawn1(String world, double x, double y, double z, float a, float b) { fcw.setLocation(prefix + "spawn1", world, x, y, z, a, b); }
	public void setSpawn2(String world, double x, double y, double z, float a, float b) { fcw.setLocation(prefix + "spawn2", world, x, y, z, a, b); }
	
	public double getPvpArenaReward() { return fcw.getDouble(prefix + ".pvpArenaReward"); }
	public Location getLobby1() { return fcw.getLocation(prefix + "lobby1"); }
	public Location getLobby2() { return fcw.getLocation(prefix + "lobby2"); }
	public Location getSpawn1() { return fcw.getLocation(prefix + "spawn1"); }
	public Location getSpawn2() { return fcw.getLocation(prefix + "spawn2"); }
	
	public PvpConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Pvp");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//Variable Declarations/Initializations
		WorldConfig wm = new WorldConfig();
		World pvpWorld = wm.getPvpWorld();
		String pvpWorldName;
		
		//Set pvpworld based on if pvp world is set or not.
		if (pvpWorld == null)
			pvpWorldName = wm.getSpawnWorld().getName();
		else
			pvpWorldName = pvpWorld.getName();
		
		//Create first section if not created.
		if (getVersion() < 0.1)
		{
			//Update version
			setVersion(0.1);
			
			//Begin setting default settings.
			setPvpArenaAward(-1);
			setSpawn1(pvpWorldName, 180,81,106,180,0);
			setSpawn2(pvpWorldName, 177,81,72,270,0);
			setLobby1(pvpWorldName,170,99,82,270,0);
			setLobby2(pvpWorldName,185,99,97,90,0);
		}
		
		FC_Rpg.plugin.saveConfig();
	}
}





















