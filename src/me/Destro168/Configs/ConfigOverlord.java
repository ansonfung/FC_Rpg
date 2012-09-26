package me.Destro168.Configs;

import me.Destro168.ConfigManagers.FileConfigPlus;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigOverlord 
{
	private FileConfiguration config;
	private final String overlordPrefix = "Overlord.";
	private FileConfigPlus fcp;
	
	private void setVersion(double x) { config.set(overlordPrefix + "version", x); }
	private double getVersion() { return config.getDouble(overlordPrefix + "version"); }
	
	private void setResetLocation(String worldName, double x, double y, double z, float a, float b) { fcp.setLocation(overlordPrefix + ".resetLoc", worldName, x, y, z, a, b); }	//Pvp
	private void setDefaultPrefix(String x) { config.set(overlordPrefix + ".defaultPrefix", x); }
	private void setPerfectBirch(boolean x) { config.set(overlordPrefix + ".perfectBirch", x); }
	private void setCreativeControl(boolean x) { config.set(overlordPrefix + ".creativeControl", x); }
	private void setHardcoreItemLoss(boolean x) { config.set(overlordPrefix + ".hardcoreItemLoss", x); }
	private void setExpCancelled(boolean x) { config.set(overlordPrefix + ".expCancelled", x); }
	private void setPerfectWheat(boolean x) { config.set(overlordPrefix + ".perfectWheat", x); }
	private void setInfiniteGold(boolean x) { config.set(overlordPrefix + ".infiniteGold", x); }
	private void setInfiniteDiamond(boolean x) { config.set(overlordPrefix + ".infiniteDiamond", x); }
	private void setBetterFishing(boolean x) { config.set(overlordPrefix + ".betterFishing", x); }
	private void setHourlySteak(boolean x) { config.set(overlordPrefix + ".hourlySteak", x); }
	private void setPreventSpongeBreak(boolean x) { config.set(overlordPrefix + ".preventSpongeBreak", x); }
	
	public Location getResetLocation() { return fcp.getLocation(overlordPrefix + ".resetLoc"); }
	public String getDefaultPrefix() { return config.getString(overlordPrefix + ".defaultPrefix"); }
	public boolean getPerfectBirch() { return config.getBoolean(overlordPrefix + ".perfectBirch"); }
	public boolean getCreativeControl() { return config.getBoolean(overlordPrefix + ".creativeControl"); }
	public boolean getHardcoreItemLoss() { return config.getBoolean(overlordPrefix + ".hardcoreItemLoss"); }
	public boolean getExpCancelled() { return config.getBoolean(overlordPrefix + ".expCancelled"); }
	public boolean getPerfectWheat() { return config.getBoolean(overlordPrefix + ".perfectWheat"); }
	public boolean getInfiniteGold() { return config.getBoolean(overlordPrefix + ".infiniteGold"); }
	public boolean getInfiniteDiamond() { return config.getBoolean(overlordPrefix + ".infiniteDiamond"); }
	public boolean getBetterFishing() { return config.getBoolean(overlordPrefix + ".betterFishing"); }
	public boolean getHourlySteak() { return config.getBoolean(overlordPrefix + ".hourlySteak"); }
	public boolean getPreventSpongeBreak() { return config.getBoolean(overlordPrefix + ".preventSpongeBreak"); }
	
	public ConfigOverlord()
	{
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//Variable Declarations
		config = FC_Rpg.plugin.getConfig();
		fcp = new FileConfigPlus(config);
		
		//If no config was previously created, then...
		if (getVersion() < 1.0)
		{
			//Update the version.
			setVersion(1.0);
			
			//Set the main respawn location.
			setResetLocation(Bukkit.getServer().getWorlds().get(0).getName(),32.5,83,160.5,90,0);
			
			//Set the default prefix.
			setDefaultPrefix("&f[&7Guest&f] ");
			
			//Set a few global defaults.
			setPerfectBirch(false);
			setCreativeControl(true);
			setHardcoreItemLoss(true);
			setExpCancelled(true);
			setPerfectWheat(true);
			setInfiniteGold(true);
			setInfiniteDiamond(true);
			setBetterFishing(true);
			setHourlySteak(true);
			setPreventSpongeBreak(true);
			
			//Initialize a few things to let them attempt to generate configurations.
			@SuppressWarnings("unused")
			WorldManager wm = new WorldManager();
			
			@SuppressWarnings("unused")
			WarpManager warp = new WarpManager();
			
			@SuppressWarnings("unused")
			FaqManager fm = new FaqManager();
			
			@SuppressWarnings("unused")
			GroupManager gm = new GroupManager();
			
			//Save config overlord.
			FC_Rpg.plugin.saveConfig();
		}
	}
}













