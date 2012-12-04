package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GeneralConfig extends ConfigGod
{
	List<ItemStack> timedItems = new ArrayList<ItemStack>();
	
	public List<ItemStack> getTimedItems() { return timedItems; }
	
	private void setDefaultPrefix(String x) { fcw.set(prefix + "defaultPrefix", x); }
	private void setPerfectBirch(boolean x) { fcw.set(prefix + "perfectBirch", x); }
	private void setCreativeControl(boolean x) { fcw.set(prefix + "creativeControl", x); }
	private void setHardcoreItemLoss(boolean x) { fcw.set(prefix + "hardcoreItemLoss", x); }
	private void setExpCancelled(boolean x) { fcw.set(prefix + "expCancelled", x); }
	private void setPerfectWheat(boolean x) { fcw.set(prefix + "perfectWheat", x); }
	private void setInfiniteGold(boolean x) { fcw.set(prefix + "infiniteGold", x); }
	private void setInfiniteDiamond(boolean x) { fcw.set(prefix + "infiniteDiamond", x); }
	private void setBetterFishing(boolean x) { fcw.set(prefix + "betterFishing", x); }
	private void setPreventSpongeBreak(boolean x) { fcw.set(prefix + "preventSpongeBreak", x); }
	private void setDonatorBonusStatPercent(double x) { fcw.set(prefix + "donatorBonusStatPercent", x); }
	private void setDonatorLootBonusPercent(double x) { fcw.set(prefix + "donatorLootBonusPercent", x); }
	private void setLevelCap(int x) { fcw.set(prefix + "levelCap", x); }
	private void setJobRankCosts(double a, double b, double c, double d, double e) { fcw.set(prefix + "jobRankCosts", a + "," + b + "," + c + "," + d + "," + e); }
	private void setNotificationInterval(int x) { fcw.set(prefix + "notificationInterval", x); }
	private void setXScale(double x) { fcw.set(prefix + "xScale", x); }
	private void setYScale(double x) { fcw.set(prefix + "yScale", x); }
	private void setZScale(double x) { fcw.set(prefix + "zScale", x); }
	private void setRpgChatOverride(boolean x) { fcw.set(prefix + "rpgChatOverride", x); }
	private void setTimedItemsInterval(long x) { fcw.set(prefix + "timedItemsInterval", x); }
	private void setTimedItemsIDs(int a, int b) { fcw.set(prefix + "timedItems.IDs", a + "," + b); }
	private void setTimedItemsCounts(int a, int b) { fcw.set(prefix + "timedItems.Counts", a + "," +  b); }
	private void setTimedItemsDataValues(byte a, byte b) { fcw.set(prefix + "timedItems.DataValues", a + "," +  b); }
	private void setLevelsPerSkillPoint(int x) { fcw.set(prefix + "levelsPerSkillPoint", x); }
	private void setStatsPerLevel(int x) { fcw.set(prefix + "statsPerLevel", x); }
	private void setMobLevelLootModifier(double x) { fcw.set(prefix + "mobLevelLootModifier", x); }
	private void setMobCashMultiplier(double x) { fcw.set(prefix + "mobCashMultiplier", x); }
	private void setMobExpMultiplier(double x) { fcw.set(prefix + "mobExpMultiplier", x); }
	private void setPowerLevelPrevention(int x) { fcw.set(prefix + "powerLevelPrevention", x); }
	private void setExpScaleRate(int x) { fcw.set(prefix + "expScaleRate", x); }
	private void setExpScaleBase(int x) { fcw.set(prefix + "expScaleBase", x); }
	private void setDisableEnderPearls(boolean x) { fcw.set(prefix + "disableEnderPearls", x); }
	private void setDungeonSelectionToolID(int x) { fcw.set(prefix + "dungeonSelectionToolID", x); }
	public void setGlobalExpMultiplier(int x) { fcw.set(prefix + "globalExpMultiplier", x); }
	private void setChainReq(int x) { fcw.set(prefix + "stoneReq", x); }
	private void setIronReq(int x) { fcw.set(prefix + "ironReq", x); }
	private void setDiamondReq(int x) { fcw.set(prefix + "diamondReq", x); }
	private void setGoldReq(int x) { fcw.set(prefix + "goldReq", x); }
	private void setWitherLevelBonus(int x) { fcw.set(prefix + "witherLevelBonus", x); }
	private void setEnderDragonLevelBonus(int x) { fcw.set(prefix + "enderDragonLevelBonus", x); }
	private void setDonatorsCanHat(boolean x) { fcw.set(prefix + "donatorsCanHat", x); }
	private void setDefaultItemDrops(boolean x) { fcw.set(prefix + "defaultItemDrops", x); }
	
	public String getDefaultPrefix() { return fcw.getString(prefix + "defaultPrefix"); }
	public boolean getPerfectBirch() { return fcw.getBoolean(prefix + "perfectBirch"); }
	public boolean getCreativeControl() { return fcw.getBoolean(prefix + "creativeControl"); }
	public boolean getHardcoreItemLoss() { return fcw.getBoolean(prefix + "hardcoreItemLoss"); }
	public boolean getExpCancelled() { return fcw.getBoolean(prefix + "expCancelled"); }
	public boolean getPerfectWheat() { return fcw.getBoolean(prefix + "perfectWheat"); }
	public boolean getInfiniteGold() { return fcw.getBoolean(prefix + "infiniteGold"); }
	public boolean getInfiniteDiamond() { return fcw.getBoolean(prefix + "infiniteDiamond"); }
	public boolean getBetterFishing() { return fcw.getBoolean(prefix + "betterFishing"); }
	public boolean getPreventSpongeBreak() { return fcw.getBoolean(prefix + "preventSpongeBreak"); }
	public double getDonatorBonusStatPercent() { return fcw.getDouble(prefix + "donatorBonusStatPercent"); }
	public double getDonatorLootBonusPercent() { return fcw.getDouble(prefix + "donatorLootBonusPercent"); }
	public int getLevelCap() { return fcw.getInt(prefix + "levelCap"); }
	public List<Double> getJobRankCosts() { return converter.getDoubleListFromString(fcw.getString(prefix + "jobRankCosts")); }
	public int getNotifcationInterval() { return fcw.getInt(prefix + "notificationInterval"); }
	public double getXScale() { return fcw.getDouble(prefix + "xScale"); }
	public double getYScale() { return fcw.getDouble(prefix + "yScale"); }
	public double getZScale() { return fcw.getDouble(prefix + "zScale"); }
	public boolean getRpgChatOverride() { return fcw.getBoolean(prefix + "rpgChatOverride"); }
	public long getTimedItemsInterval() { return fcw.getLong(prefix + "timedItemsInterval"); }
	public int getLevelsPerSkillPoint() { return fcw.getInt(prefix + "levelsPerSkillPoint"); }
	public int getStatsPerLevel() { return fcw.getInt(prefix + "statsPerLevel"); }
	public double getMobLevelLootmodifier() { return fcw.getDouble(prefix + "mobLevelLootModifier"); }
	public double getMobCashMultiplier() { return fcw.getDouble(prefix + "mobCashMultiplier"); }
	public double getMobExpMultiplier() { return fcw.getDouble(prefix + "mobExpMultiplier"); }
	public int getPowerLevelPrevention() { return fcw.getInt(prefix + "powerLevelPrevention"); }
	public double getExpScaleRate() { return fcw.getDouble(prefix + "expScaleRate"); }
	public double getExpScaleBase() { return fcw.getDouble(prefix + "expScaleBase"); }
	public boolean getDisableEnderPearls() { return fcw.getBoolean(prefix + "disableEnderPearls"); }
	public int getDungeonSelectionToolID() { return fcw.getInt(prefix + "dungeonSelectionToolID"); }
	public int getGlobalExpMultiplier() { return fcw.getInt(prefix + "globalExpMultiplier"); }
	public int getChainReq() { return fcw.getInt(prefix + "stoneReq"); }
	public int getIronReq() { return fcw.getInt(prefix + "ironReq"); }
	public int getDiamondReq() { return fcw.getInt(prefix + "diamondReq"); }
	public int getGoldReq() { return fcw.getInt(prefix + "goldReq"); }
	public int getWitherLevelBonus() { return fcw.getInt(prefix + "witherLevelBonus"); }
	public int getEnderDragonLevelBonus() { return fcw.getInt(prefix + "enderDragonLevelBonus"); }
	public boolean getDonatorsCanHat() { return fcw.getBoolean(prefix + "donatorsCanHat"); }
	public boolean getDefaultItemDrops() { return fcw.getBoolean(prefix + "defaultItemDrops"); }
	
	public GeneralConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "General");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If no config was previously created, then...
		if (getVersion() < 0.2)
		{
			//Update the version.
			setVersion(0.2);
			
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
		
		if (getVersion() < 0.31)
		{
			setVersion(0.31);
			
			//Remove old hourly steak.
			fcw.set(prefix + ".hourlySteak", null);
			setXScale(20);
			setZScale(20);
			setYScale(-25);
			setRpgChatOverride(true);
			setTimedItemsIDs(364,322);
			setTimedItemsCounts(5,1);
			setTimedItemsDataValues((byte) 0, (byte) 1);
			setTimedItemsInterval(3600000);
		}
		
		if (getVersion() < 0.4)
		{
			setVersion(0.4);
			
			setLevelsPerSkillPoint(4);
			setStatsPerLevel(10);
			setMobLevelLootModifier(.2);
			setMobCashMultiplier(.8);
			setMobExpMultiplier(1);
			setPowerLevelPrevention(5);
			setExpScaleRate(3);
			setExpScaleBase(11);
		}
		
		if (getVersion() < 0.5)
		{
			setVersion(0.5);
			
			setDisableEnderPearls(true);
			setDungeonSelectionToolID(7);
		}
		
		if (getVersion() < 0.7)
		{
			setVersion(0.7);
			
			setGlobalExpMultiplier(1);
			setChainReq(125);
			setIronReq(250);
			setDiamondReq(375);
			setGoldReq(500);
			setWitherLevelBonus(100);
			setEnderDragonLevelBonus(50);
		}
		
		if (getVersion() < 1.0)
		{
			setVersion(1.0);
			
			setDonatorsCanHat(true);
			setDefaultItemDrops(true);
		}
		
		//Load up timed items.
		loadTimedItems();
	}
	
	private void loadTimedItems() 
	{
		List<Integer> itemIDs = converter.getIntegerListFromString(fcw.getString(prefix + "timedItems.IDs"));
		List<Integer> itemCounts = converter.getIntegerListFromString(fcw.getString(prefix + ".timedItems.Counts"));
		List<Byte> itemDataValues = converter.getByteListFromString(fcw.getString(prefix + "timedItems.DataValues"));
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













