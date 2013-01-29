package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ListGetter;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WarpConfig extends ConfigGod
{
	public List<Integer> getWarpFieldList() { ListGetter lg = new ListGetter(fcw, prefix); return lg.getFieldIntegerList(); }
	
	public void setName(int i, String x) { fcw.set(prefix + i + ".name", x); }
	public void setDescription(int i, String x) { fcw.set(prefix + i + ".description", x); }
	public void setDescription(int i, List<String> x) { fcw.setList(prefix + i + ".description", x); }
	public void setWelcome(int i, String x) { fcw.set(prefix + i + ".welcome", x); }
	public void setExit(int i, String x) { fcw.set(prefix + i + ".exit", x); }
	public void setCost(int i, double x) { fcw.set(prefix + i + ".cost", x); }
	public void setAdmin(int i, boolean x) { fcw.set(prefix + i + ".admin", x); }
	public void setDonator(int i, boolean x) { fcw.set(prefix + i + ".donator", x); }
	public void setDestination(int i, Location loc) 
	{
		if (loc == null)
		{
			FC_Rpg.plugin.getLogger().info("Couldn't set destination due to null location.");
			return;
		}
		
		fcw.setLocation(prefix + i + ".destination", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}
	public void setClassRequirement(int i, int x) { fcw.set(prefix + i + ".classRequirement", x); }
	public void setJobRankMinimum(int i, int x) { fcw.set(prefix + i + ".jobRankMinimum", x); }
	public void setLevelMinimum(int i, int x) { fcw.set(prefix + i + ".levelMinimum", x); }
	
	
	public String getName(int i) { return fcw.getString(prefix + i + ".name"); }
	public List<String> getDescription(int i) { return fcw.getStringList(prefix + i + ".description"); }
	public String getWelcome(int i) { return fcw.getString(prefix + i + ".welcome"); }
	public String getExit(int i) { return fcw.getString(prefix + i + ".exit"); }
	public double getCost(int i) { return fcw.getDouble(prefix + i + ".cost"); }
	public boolean getAdmin(int i) { return fcw.getBoolean(prefix + i + ".admin"); }
	public boolean getDonator(int i) { return fcw.getBoolean(prefix + i + ".donator"); }
	public Location getDestination(int i) { return fcw.getLocation(prefix + i + ".destination"); }
	public int getClassRequirement(int i) { return fcw.getInt(prefix + i + ".classRequirement"); }
	public int getJobRankMinimum(int i) { return fcw.getInt(prefix + i + ".jobRankMinimum"); }
	public int getLevelMinimum(int i) { return fcw.getInt(prefix + i + ".levelMinimum"); }
	
	public void setNull(int i) { fcw.set(prefix + i, null); }
	
	public WarpConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Warps");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//Create first section if not created.
		if (getVersion() < 0.1)
		{
			//Update version
			setVersion(0.1);
			
			//Set the warp stuff.
			for (World w : Bukkit.getServer().getWorlds())
			{
				if (w != null)
					addNewWarp(w.getName());
			}
			
			//Save default warps.
			FC_Rpg.plugin.saveConfig();
		}
		
		if (getVersion() < 0.2)
		{
			for (int i : getWarpFieldList())
			{
				setClassRequirement(i,-1);
				setJobRankMinimum(i,-1);
				setLevelMinimum(i,-1);
			}
		}
	}
	
	public int getWarpIDByName(String name)
	{
		for (int i : getWarpFieldList())
		{
			if (getName(i).equalsIgnoreCase(name))
				return i;
		}
		
		return -1;
	}
	
	public void addNewWarp(String warpName)
	{
		List<String> description = new ArrayList<String>();
		description.add("This is the default warp description.");
		description.add("This is the second line!");
		
		WorldConfig worldConfig = new WorldConfig();
		Location worldSpawn;
		
		//Attempt to take warp location from world config.
		if (worldConfig.isCreated(warpName) == true)
		{
			worldSpawn = worldConfig.getWorldSpawn(warpName);
			
			//If that fails, then try to set the warp to the first world.
			if (worldSpawn == null)
				worldSpawn = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, (float) 0, (float) 0);
		}
		
		//If not in world config then we want to load new one.
		else
			worldSpawn = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, (float) 0, (float) 0);
		
		for (int i = 0; i < 1000; i++)
		{
			if ((getName(i) == null) || (getName(i).equalsIgnoreCase(warpName)))
			{
				setName(i, warpName);
				setDescription(i, description);
				setWelcome(i, "Welcome To " + warpName + "!");
				setExit(i, "You have left " + warpName + "!");
				setCost(i, 0);
				setAdmin(i, false);
				setDonator(i, false);
				
				if (worldSpawn != null)
					setDestination(i, worldSpawn);
				else
				{
					FC_Rpg.plugin.getLogger().info("No world to match warp name, setting to 0,0,0.");
					setDestination(i, worldSpawn);
				}
				
				break;
			}
		}
	}
}


















