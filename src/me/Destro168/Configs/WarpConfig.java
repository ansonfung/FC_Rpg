package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WarpConfig extends ConfigGod
{
	private void setName(int i, String x) { ccm.set(prefix + i + ".name", x); }
	private void setDescription(int i, List<String> x) { ccm.setList(prefix + i + ".description", x); }
	private void setWelcome(int i, String x) { ccm.set(prefix + i + ".welcome", x); }
	private void setExit(int i, String x) { ccm.set(prefix + i + ".exit", x); }
	private void setCost(int i, double x) { ccm.set(prefix + i + ".cost", x); }
	private void setAdmin(int i, boolean x) { ccm.set(prefix + i + ".admin", x); }
	private void setDonator(int i, boolean x) { ccm.set(prefix + i + ".donator", x); }
	
	private void setDestination(int i, Location loc) 
	{
		if (loc == null)
		{
			FC_Rpg.plugin.getLogger().info("Couldn't set destination due to null location.");
			return;
		}
		
		ccm.setLocation(prefix + i + ".destination", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
	}
	
	public String getName(int i) { return ccm.getString(prefix + i + ".name"); }
	public List<String> getDescription(int i) { return ccm.getStringList(prefix + i + ".description"); }
	public String getWelcome(int i) { return ccm.getString(prefix + i + ".welcome"); }
	public String getExit(int i) { return ccm.getString(prefix + i + ".exit"); }
	public double getCost(int i) { return ccm.getDouble(prefix + i + ".cost"); }
	public boolean getAdmin(int i) { return ccm.getBoolean(prefix + i + ".admin"); }
	public boolean getDonator(int i) { return ccm.getBoolean(prefix + i + ".donator"); }
	
	public Location getDestination(int i)
	{
		return ccm.getLocation(prefix + i + ".destination");
	}
	
	public WarpConfig()
	{
		super("Warps");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//Variable Declarations
		WorldConfig wm = new WorldConfig();
		
		//Create first section if not created.
		if (getVersion() < 0.1)
		{
			//Variable declaration
			List<World> worldList = Bukkit.getServer().getWorlds();
			List<String> description = new ArrayList<String>();
			description.add("This is the default warp description.");
			description.add("This is the second line!");
			
			//Update version
			setVersion(0.1);
			
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


















