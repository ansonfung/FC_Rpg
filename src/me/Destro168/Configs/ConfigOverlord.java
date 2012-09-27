package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

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
	
	private void setDonatorBonusStatPercent(double x) { config.set(overlordPrefix + ".donatorBonusStatPercent", x); }
	private void setDonatorLootBonusPercent(double x) { config.set(overlordPrefix + ".donatorLootBonusPercent", x); }
	private void setLevelCap(int x) { config.set(overlordPrefix + ".levelCap", x); }
	private void setJobRankCosts(List<Double> x) { config.set(overlordPrefix + ".jobRankCosts", x); }
	private void setPvpArenaAward(double x) { config.set(overlordPrefix + ".pvpArenaReward", x); }
	
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
	public double getDonatorBonusStatPercent() { return config.getDouble(overlordPrefix + ".donatorBonusStatPercent"); }
	public double getDonatorLootBonusPercent() { return config.getDouble(overlordPrefix + ".donatorLootBonusPercent"); }
	public int getLevelCap() { return config.getInt(overlordPrefix + ".levelCap"); }
	public List<Double> getJobRankCosts() { return config.getDoubleList(overlordPrefix + ".jobRankCosts"); }
	public double getPvpArenaReward() { return config.getDouble(overlordPrefix + ".pvpArenaReward"); }
	
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
		if (getVersion() < 0.1)
		{
			//Update the version.
			setVersion(0.1);
			
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
			setDonatorBonusStatPercent(.1);
			setDonatorLootBonusPercent(.1);
			setLevelCap(100);
			
			List<Double> jobRankCosts = new ArrayList<Double>();
			jobRankCosts.add(3200.0D);
			jobRankCosts.add(20000.0D);
			jobRankCosts.add(55000.0D);
			jobRankCosts.add(105000.0D);
			jobRankCosts.add(175000.0D);
			setJobRankCosts(jobRankCosts);
			
			setPvpArenaAward(-1);
			
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













