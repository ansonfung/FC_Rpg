package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.ConfigManagers.FileConfigPlus;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class WarpManager 
{
	private FileConfiguration config;
	private String warpPrefix = "Warp.";
	
	private void setVersion(double x) { config.set(warpPrefix + "version", x); }
	private double getVersion() { return config.getDouble(warpPrefix + "version"); }
	
	private void setName(int i, String x) { config.set(warpPrefix + i + ".name", x); }
	private void setDescription(int i, List<String> x) { config.set(warpPrefix + i + ".description", x); }
	private void setWelcome(int i, String x) { config.set(warpPrefix + i + ".welcome", x); }
	private void setExit(int i, String x) { config.set(warpPrefix + i + ".exit", x); }
	private void setCost(int i, double x) { config.set(warpPrefix + i + ".cost", x); }
	private void setAdmin(int i, boolean x) { config.set(warpPrefix + i + ".admin", x); }
	private void setDonator(int i, boolean x) { config.set(warpPrefix + i + ".donator", x); }
	
	private void setDestination(int i, Location loc) 
	{
		if (loc == null)
		{
			FC_Rpg.plugin.getLogger().info("Couldn't set destination due to null location.");
			return;
		}
		
		FileConfigPlus fcp = new FileConfigPlus(config);
		fcp.setLocation(warpPrefix + i + ".destination", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
	}
	
	public String getName(int i) { return config.getString(warpPrefix + i + ".name"); }
	public List<String> getDescription(int i) { return config.getStringList(warpPrefix + i + ".description"); }
	public String getWelcome(int i) { return config.getString(warpPrefix + i + ".welcome"); }
	public String getExit(int i) { return config.getString(warpPrefix + i + ".exit"); }
	public double getCost(int i) { return config.getDouble(warpPrefix + i + ".cost"); }
	public boolean getAdmin(int i) { return config.getBoolean(warpPrefix + i + ".admin"); }
	public boolean getDonator(int i) { return config.getBoolean(warpPrefix + i + ".donator"); }
	
	public Location getDestination(int i)
	{
		FileConfigPlus fcp = new FileConfigPlus(config);
		return fcp.getLocation(warpPrefix + i + ".destination");
	}
	
	public WarpManager()
	{
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//Variable Declarations
		WorldManager wm = new WorldManager();
		config = FC_Rpg.plugin.getConfig();
		
		//Create first section if not created.
		if (getVersion() < 1.0)
		{
			//Variable declaration
			List<World> worldList = Bukkit.getServer().getWorlds();
			List<String> description = new ArrayList<String>();
			description.add("This is the default warp description.");
			description.add("This is the second line!");
			
			//Update version
			setVersion(1.0);
			
			//Set the warp stuff.
			for (int i = 0; i < worldList.size(); i++)
			{
				if (worldList.get(i) != null)
				{
					setName(i, worldList.get(i).getName());
					setDescription(i, description);
					setWelcome(i, "Welcome To " + worldList.get(i).getName() + "!");
					setExit(i, "You have left " + worldList.get(i).getName() + "!");
					setCost(i, 0);
					setAdmin(i, false);
					setDonator(i, false);
					
					if (wm.getWorldSpawn(worldList.get(i).getName()) != null)
						setDestination(i, wm.getWorldSpawn(worldList.get(i).getName()));
					else
						FC_Rpg.plugin.getLogger().info("Warp initialization Is null");
				}
			}
			
			//Save default warps.
			FC_Rpg.plugin.saveConfig();
		}
	}
}


















