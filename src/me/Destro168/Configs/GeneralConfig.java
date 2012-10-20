package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GeneralConfig extends ConfigGod
{
	List<ItemStack> timedItems = new ArrayList<ItemStack>();
	
	public List<ItemStack> getTimedItems() { return timedItems; }
	
	private void setResetLocation(String worldName, double x, double y, double z, float a, float b) { ccm.setLocation(prefix + "resetLoc", worldName, x, y, z, a, b); }	//Pvp
	private void setDefaultPrefix(String x) { ccm.set(prefix + "defaultPrefix", x); }
	private void setPerfectBirch(boolean x) { ccm.set(prefix + "perfectBirch", x); }
	private void setCreativeControl(boolean x) { ccm.set(prefix + "creativeControl", x); }
	private void setHardcoreItemLoss(boolean x) { ccm.set(prefix + "hardcoreItemLoss", x); }
	private void setExpCancelled(boolean x) { ccm.set(prefix + "expCancelled", x); }
	private void setPerfectWheat(boolean x) { ccm.set(prefix + "perfectWheat", x); }
	private void setInfiniteGold(boolean x) { ccm.set(prefix + "infiniteGold", x); }
	private void setInfiniteDiamond(boolean x) { ccm.set(prefix + "infiniteDiamond", x); }
	private void setBetterFishing(boolean x) { ccm.set(prefix + "betterFishing", x); }
	private void setPreventSpongeBreak(boolean x) { ccm.set(prefix + "preventSpongeBreak", x); }
	private void setDonatorBonusStatPercent(double x) { ccm.set(prefix + "donatorBonusStatPercent", x); }
	private void setDonatorLootBonusPercent(double x) { ccm.set(prefix + "donatorLootBonusPercent", x); }
	private void setLevelCap(int x) { ccm.set(prefix + "levelCap", x); }
	private void setJobRankCosts(double a, double b, double c, double d, double e) { ccm.set(prefix + "jobRankCosts", a + "," + b + "," + c + "," + d + "," + e); }
	private void setNotificationInterval(int x) { ccm.set(prefix + "notificationInterval", x); }
	private void setXScale(double x) { ccm.set(prefix + "xScale", x); }
	private void setYScale(double x) { ccm.set(prefix + "yScale", x); }
	private void setZScale(double x) { ccm.set(prefix + "zScale", x); }
	private void setRpgChatOverride(boolean x) { ccm.set(prefix + "rpgChatOverride", x); }
	private void setTimedItemsInterval(long x) { ccm.set(prefix + "timedItemsInterval", x); }
	private void setTimedItemsIDs(int a, int b) { ccm.set(prefix + "timedItems.IDs", a + "," + b); }
	private void setTimedItemsCounts(int a, int b) { ccm.set(prefix + "timedItems.Counts", a + "," +  b); }
	private void setTimedItemsDataValues(byte a, byte b) { ccm.set(prefix + "timedItems.DataValues", a + "," +  b); }
	
	public Location getResetLocation() { return ccm.getLocation(prefix + "resetLoc"); }
	public String getDefaultPrefix() { return ccm.getString(prefix + "defaultPrefix"); }
	public boolean getPerfectBirch() { return ccm.getBoolean(prefix + "perfectBirch"); }
	public boolean getCreativeControl() { return ccm.getBoolean(prefix + "creativeControl"); }
	public boolean getHardcoreItemLoss() { return ccm.getBoolean(prefix + "hardcoreItemLoss"); }
	public boolean getExpCancelled() { return ccm.getBoolean(prefix + "expCancelled"); }
	public boolean getPerfectWheat() { return ccm.getBoolean(prefix + "perfectWheat"); }
	public boolean getInfiniteGold() { return ccm.getBoolean(prefix + "infiniteGold"); }
	public boolean getInfiniteDiamond() { return ccm.getBoolean(prefix + "infiniteDiamond"); }
	public boolean getBetterFishing() { return ccm.getBoolean(prefix + "betterFishing"); }
	public boolean getPreventSpongeBreak() { return ccm.getBoolean(prefix + "preventSpongeBreak"); }
	public double getDonatorBonusStatPercent() { return ccm.getDouble(prefix + "donatorBonusStatPercent"); }
	public double getDonatorLootBonusPercent() { return ccm.getDouble(prefix + "donatorLootBonusPercent"); }
	public int getLevelCap() { return ccm.getInt(prefix + "levelCap"); }
	public List<Double> getJobRankCosts() { return converter.getDoubleListFromString(ccm.getString(prefix + "jobRankCosts")); }
	public int getNotifcationInterval() { return ccm.getInt(prefix + "notificationInterval"); }
	public double getXScale() { return ccm.getDouble(prefix + "xScale"); }
	public double getYScale() { return ccm.getDouble(prefix + "yScale"); }
	public double getZScale() { return ccm.getDouble(prefix + "zScale"); }
	public boolean getRpgChatOverride() { return ccm.getBoolean(prefix + "rpgChatOverride"); }
	public long getTimedItemsInterval() { return ccm.getLong(prefix + "timedItemsInterval"); }
	
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
		
		if (getVersion() < 0.3)
		{
			setVersion(0.3);
			
			//Remove old hourly steak.
			ccm.set(prefix + ".hourlySteak", null);
			setXScale(20);
			setZScale(20);
			setYScale(-25);
			setRpgChatOverride(true);
			setTimedItemsIDs(364,322);
			setTimedItemsCounts(5,1);
			setTimedItemsDataValues((byte) 0, (byte) 1);
			setTimedItemsInterval(3600000);
		}
		
		//Load up timed items.
		loadTimedItems();
	}
	
	private void loadTimedItems() 
	{
		List<Integer> itemIDs = converter.getIntegerListFromString(ccm.getString(prefix + "timedItems.IDs"));
		List<Integer> itemCounts = converter.getIntegerListFromString(ccm.getString(prefix + ".timedItems.Counts"));
		List<Byte> itemDataValues = converter.getByteListFromString(ccm.getString(prefix + "timedItems.DataValues"));
		ItemStack newItem;
		Material mat;
		
		for (int i = 0; i < itemIDs.size(); i++)
		{
			mat = Material.getMaterial(itemIDs.get(i));
			newItem = new ItemStack(mat, 0, itemDataValues.get(i));
			newItem.setAmount(itemCounts.get(i));
			timedItems.add(newItem);
		}
	}
}













