package me.Destro168.Configs;

import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GeneralConfig extends ConfigGod
{
	private void setResetLocation(String worldName, double x, double y, double z, float a, float b) { ccm.setLocation(prefix + ".resetLoc", worldName, x, y, z, a, b); }	//Pvp
	private void setDefaultPrefix(String x) { ccm.set(prefix + ".defaultPrefix", x); }
	private void setPerfectBirch(boolean x) { ccm.set(prefix + ".perfectBirch", x); }
	private void setCreativeControl(boolean x) { ccm.set(prefix + ".creativeControl", x); }
	private void setHardcoreItemLoss(boolean x) { ccm.set(prefix + ".hardcoreItemLoss", x); }
	private void setExpCancelled(boolean x) { ccm.set(prefix + ".expCancelled", x); }
	private void setPerfectWheat(boolean x) { ccm.set(prefix + ".perfectWheat", x); }
	private void setInfiniteGold(boolean x) { ccm.set(prefix + ".infiniteGold", x); }
	private void setInfiniteDiamond(boolean x) { ccm.set(prefix + ".infiniteDiamond", x); }
	private void setBetterFishing(boolean x) { ccm.set(prefix + ".betterFishing", x); }
	private void setHourlySteak(boolean x) { ccm.set(prefix + ".hourlySteak", x); }
	private void setPreventSpongeBreak(boolean x) { ccm.set(prefix + ".preventSpongeBreak", x); }
	
	private void setDonatorBonusStatPercent(double x) { ccm.set(prefix + ".donatorBonusStatPercent", x); }
	private void setDonatorLootBonusPercent(double x) { ccm.set(prefix + ".donatorLootBonusPercent", x); }
	private void setLevelCap(int x) { ccm.set(prefix + ".levelCap", x); }
	private void setJobRankCosts(double a, double b, double c, double d, double e) { ccm.set(prefix + ".jobRankCosts", a + "," + b + "," + c + "," + d + "," + e); }
	private void setNotificationInterval(int x) { ccm.set(prefix + ".notificationInterval", x); }
	
	public Location getResetLocation() { return ccm.getLocation(prefix + ".resetLoc"); }
	public String getDefaultPrefix() { return ccm.getString(prefix + ".defaultPrefix"); }
	public boolean getPerfectBirch() { return ccm.getBoolean(prefix + ".perfectBirch"); }
	public boolean getCreativeControl() { return ccm.getBoolean(prefix + ".creativeControl"); }
	public boolean getHardcoreItemLoss() { return ccm.getBoolean(prefix + ".hardcoreItemLoss"); }
	public boolean getExpCancelled() { return ccm.getBoolean(prefix + ".expCancelled"); }
	public boolean getPerfectWheat() { return ccm.getBoolean(prefix + ".perfectWheat"); }
	public boolean getInfiniteGold() { return ccm.getBoolean(prefix + ".infiniteGold"); }
	public boolean getInfiniteDiamond() { return ccm.getBoolean(prefix + ".infiniteDiamond"); }
	public boolean getBetterFishing() { return ccm.getBoolean(prefix + ".betterFishing"); }
	public boolean getHourlySteak() { return ccm.getBoolean(prefix + ".hourlySteak"); }
	public boolean getPreventSpongeBreak() { return ccm.getBoolean(prefix + ".preventSpongeBreak"); }
	public double getDonatorBonusStatPercent() { return ccm.getDouble(prefix + ".donatorBonusStatPercent"); }
	public double getDonatorLootBonusPercent() { return ccm.getDouble(prefix + ".donatorLootBonusPercent"); }
	public int getLevelCap() { return ccm.getInt(prefix + ".levelCap"); }
	public List<Double> getJobRankCosts() { return converter.getDoubleListFromString(ccm.getString(prefix + ".jobRankCosts")); }
	public int getNotifcationInterval() { return ccm.getInt(prefix + ".notificationInterval"); }
	
	public GeneralConfig()
	{
		super("General");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If no config was previously created, then...
		if (getVersion() < 0.2)
		{
			//Update the version.
			setVersion(0.2);
			
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
			setJobRankCosts(3200.0D, 20000.0D, 55000.0D, 105000.0D, 175000.0D);
			setNotificationInterval(2000);
			
			//Initialize a few things to let them attempt to generate configurations.
			@SuppressWarnings("unused")
			WorldConfig wm = new WorldConfig();
			
			@SuppressWarnings("unused")
			WarpConfig warp = new WarpConfig();
			
			@SuppressWarnings("unused")
			FaqConfig fm = new FaqConfig();
			
			@SuppressWarnings("unused")
			GroupConfig gm = new GroupConfig();
			
			//Save config overlord.
			FC_Rpg.plugin.saveConfig();
		}
	}
}













