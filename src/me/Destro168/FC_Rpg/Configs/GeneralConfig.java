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
	private void setScaleX(double x) { fcw.set(prefix + "scale.x", x); }
	private void setScaleY(double x) { fcw.set(prefix + "scale.y", x); }
	private void setScaleZ(double x) { fcw.set(prefix + "scale.z", x); }
	private void setTimedItemsInterval(long x) { fcw.set(prefix + "timedItems.interval", x); }
	private void setTimedItemsIDs(int a, int b) { fcw.set(prefix + "timedItems.ids", a + "," + b); }
	private void setTimedItemsCounts(int a, int b) { fcw.set(prefix + "timedItems.counts", a + "," +  b); }
	private void setTimedItemsDataValues(byte a, byte b) { fcw.set(prefix + "timedItems.dataValues", a + "," +  b); }
	private void setDisableEnderPearls(boolean x) { fcw.set(prefix + "disableEnderPearls", x); }
	private void setDungeonSelectionToolID(int x) { fcw.set(prefix + "dungeonSelectionToolID", x); }
	private void setDonatorsCanHat(boolean x) { fcw.set(prefix + "donatorsCanHat", x); }
	private void setChatFormat(String x) { fcw.set(prefix + "chatFormat", x); }
	private void setChatFormatAdmin(String x) { fcw.set(prefix + "chatFormatAdmin", x); }
	private void setInactivePlayerFileDeleteTime(long x) { fcw.set(prefix + "inactivePlayerFileDeleteTime", x); }
	private void setBuffCommandCost(double x) { fcw.set(prefix + "buffCommandCost", x); }
	private void setBonusMobSpawns(boolean x) { fcw.set(prefix + "bonusMobSpawns", x); }
	private void setPerfectWarts(boolean x) { fcw.set(prefix + "perfectWarts", x); }
	private void setCommandKeyWordClass(String x) { fcw.set(prefix + "commandKeyWord.class", x); }
	private void setCommandKeyWordDonator(String x) { fcw.set(prefix + "commandKeyWord.donator", x); }
	private void setCommandKeyWordDungeon(String x) { fcw.set(prefix + "commandKeyWord.dungeon", x); }
	private void setCommandKeyWordDE(String x) { fcw.set(prefix + "commandKeyWord.DE", x); }
	private void setCommandKeyWordFaq(String x) { fcw.set(prefix + "commandKeyWord.faq", x); }
	private void setCommandKeyWordG(String x) { fcw.set(prefix + "commandKeyWord.g", x); }
	private void setCommandKeyWordH(String x) { fcw.set(prefix + "commandKeyWord.h", x); }
	private void setCommandKeyWordGH(String x) { fcw.set(prefix + "commandKeyWord.gh", x); }
	private void setCommandKeyWordHG(String x) { fcw.set(prefix + "commandKeyWord.hg", x); }
	private void setCommandKeyWordHead(String x) { fcw.set(prefix + "commandKeyWord.head", x); }
	private void setCommandKeyWordJob(String x) { fcw.set(prefix + "commandKeyWord.job", x); }
	private void setCommandKeyWordGuild(String x) { fcw.set(prefix + "commandKeyWord.guild", x); }
	private void setCommandKeyWordPvp(String x) { fcw.set(prefix + "commandKeyWord.pvp", x); }
	private void setCommandKeyWordReset(String x) { fcw.set(prefix + "commandKeyWord.reset", x); }
	private void setCommandKeyWordRpgHelp(String x) { fcw.set(prefix + "commandKeyWord.rpgHelp", x); }
	private void setCommandKeyWordRpgRAdmin(String x) { fcw.set(prefix + "commandKeyWord.rAdmin", x); }
	private void setCommandKeyWordSpell(String x) { fcw.set(prefix + "commandKeyWord.spell", x); }
	private void setCommandKeyWordAlchemy(String x) { fcw.set(prefix + "commandKeyWord.alchemy", x); }
	private void setCommandKeyWordModify(String x) { fcw.set(prefix + "commandKeyWord.modify", x); }
	private void setCommandKeyWordW(String x) { fcw.set(prefix + "commandKeyWord.w", x); }
	private void setCommandKeyWordBuff(String x) { fcw.set(prefix + "commandKeyWord.buff", x); }
	private void setCommandKeyWordWorld(String x) { fcw.set(prefix + "commandKeyWord.world", x); }
	private void setCommandKeyWordPlayers(String x) { fcw.set(prefix + "commandKeyWord.players", x); }
	private void setRecordExpRewards(boolean x) { fcw.set(prefix + "recordExpRewards", x); }
	private void setCustomChatExclusions(String x) { fcw.set(prefix + "customChatExclusions", x); }
	
	public String getDefaultPrefix() { return fcw.getStringS(prefix + "defaultPrefix"); }
	public boolean getPerfectBirch() { return fcw.getBooleanS(prefix + "perfectBirch"); }
	public boolean getCreativeControl() { return fcw.getBooleanS(prefix + "creativeControl"); }
	public boolean getHardcoreItemLoss() { return fcw.getBooleanS(prefix + "hardcoreItemLoss"); }
	public boolean getPerfectWheat() { return fcw.getBooleanS(prefix + "perfectWheat"); }
	public boolean getInfiniteGold() { return fcw.getBooleanS(prefix + "infiniteGold"); }
	public boolean getInfiniteDiamond() { return fcw.getBooleanS(prefix + "infiniteDiamond"); }
	public boolean getBetterFishing() { return fcw.getBooleanS(prefix + "betterFishing"); }
	public boolean getPreventSpongeBreak() { return fcw.getBooleanS(prefix + "preventSpongeBreak"); }
	public double getDonatorBonusStatPercent() { return fcw.getDoubleS(prefix + "donatorBonusStatPercent"); }
	public double getDonatorLootBonusPercent() { return fcw.getDoubleS(prefix + "donatorLootBonusPercent"); }
	public int getLevelCap() { return fcw.getIntS(prefix + "levelCap"); }
	public List<Double> getJobRankCosts() { return converter.getDoubleListFromString(fcw.getStringS(prefix + "jobRankCosts")); }
	public int getNotifcationInterval() { return fcw.getIntS(prefix + "notificationInterval"); }
	public double getScaleX() { return fcw.getDoubleS(prefix + "scale.x"); }
	public double getScaleY() { return fcw.getDoubleS(prefix + "scale.y"); }
	public double getScaleZ() { return fcw.getDoubleS(prefix + "scale.z"); }
	public long getTimedItemsInterval() { return fcw.getLongS(prefix + "timedItems.interval"); }
	public boolean getDisableEnderPearls() { return fcw.getBooleanS(prefix + "disableEnderPearls"); }
	public int getDungeonSelectionToolID() { return fcw.getIntS(prefix + "dungeonSelectionToolID"); }
	public boolean getDonatorsCanHat() { return fcw.getBooleanS(prefix + "donatorsCanHat"); }
	public String getChatFormat() { return fcw.getStringS(prefix + "chatFormat"); }
	public String getChatFormatAdmin() { return fcw.getStringS(prefix + "chatFormatAdmin"); }
	public long getInactivePlayerFileDeleteTime() { return fcw.getLongS(prefix + "inactivePlayerFileDeleteTime"); }
	public double getBuffCommandCost() { return fcw.getDoubleS(prefix + "buffCommandCost"); }
	public boolean getBonusMobSpawns() { return fcw.getBooleanS(prefix + "bonusMobSpawns"); }
	public boolean getPerfectWarts() { return fcw.getBooleanS(prefix + "perfectWarts"); }
	public String getCommandKeyWordClass() { return fcw.getStringS(prefix + "commandKeyWord.class"); }
	public String getCommandKeyWordDonator() { return fcw.getStringS(prefix + "commandKeyWord.donator"); }
	public String getCommandKeyWordDungeon() { return fcw.getStringS(prefix + "commandKeyWord.dungeon"); }
	public String getCommandKeyWordDE() { return fcw.getStringS(prefix + "commandKeyWord.DE"); }
	public String getCommandKeyWordFaq() { return fcw.getStringS(prefix + "commandKeyWord.faq"); }
	public String getCommandKeyWordG() { return fcw.getStringS(prefix + "commandKeyWord.g"); }
	public String getCommandKeyWordH() { return fcw.getStringS(prefix + "commandKeyWord.h"); }
	public String getCommandKeyWordGH() { return fcw.getStringS(prefix + "commandKeyWord.gh"); }
	public String getCommandKeyWordHG() { return fcw.getStringS(prefix + "commandKeyWord.hg"); }
	public String getCommandKeyWordHead() { return fcw.getStringS(prefix + "commandKeyWord.head"); }
	public String getCommandKeyWordJob() { return fcw.getStringS(prefix + "commandKeyWord.job"); }
	public String getCommandKeyWordGuild() { return fcw.getStringS(prefix + "commandKeyWord.guild"); }
	public String getCommandKeyWordPvp() { return fcw.getStringS(prefix + "commandKeyWord.pvp"); }
	public String getCommandKeyWordReset() { return fcw.getStringS(prefix + "commandKeyWord.reset"); }
	public String getCommandKeyWordRpgHelp() { return fcw.getStringS(prefix + "commandKeyWord.rpgHelp"); }
	public String getCommandKeyWordRAdmin() { return fcw.getStringS(prefix + "commandKeyWord.rAdmin"); }
	public String getCommandKeyWordSpell() { return fcw.getStringS(prefix + "commandKeyWord.spell"); }
	public String getCommandKeyWordAlchemy() { return fcw.getStringS(prefix + "commandKeyWord.alchemy"); }
	public String getCommandKeyWordModify() { return fcw.getStringS(prefix + "commandKeyWord.modify"); }
	public String getCommandKeyWordW() { return fcw.getStringS(prefix + "commandKeyWord.w"); }
	public String getCommandKeyWordBuff() { return fcw.getStringS(prefix + "commandKeyWord.buff"); }
	public String getCommandKeyWordWorld() { return fcw.getStringS(prefix + "commandKeyWord.world"); }
	public String getCommandKeyWordPlayers() { return fcw.getStringS(prefix + "commandKeyWord.players"); }
	public boolean getRecordExpRewards() { return fcw.getBooleanS(prefix + "recordExpRewards"); }
	public List<String> getCustomChatExclusions() { return converter.getStringListFromString(fcw.getStringS(prefix + "customChatExclusions")); }
	
	public GeneralConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "General");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If no config was previously created, then...
		if (getVersion() < 1.16)
		{
			//Set defaults
			setDefaultPrefix("&f[&7Guest&f] ");
			setPerfectBirch(false);
			setCreativeControl(true);
			setHardcoreItemLoss(true);
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
			setScaleX(20);
			setScaleZ(20);
			setScaleY(-25);
			setTimedItemsIDs(364,322);
			setTimedItemsCounts(5,1);
			setTimedItemsDataValues((byte) 0, (byte) 1);
			setTimedItemsInterval(3600000);
			setDisableEnderPearls(true);
			setDungeonSelectionToolID(7);
			setDonatorsCanHat(true);
			setChatFormat("&7%time% [&6%level%&7] %prefix%&7%name%: %chat%");
			setChatFormatAdmin("&e%time% [&6%level%&e] %prefix%&e%name%: %chat%");
			setInactivePlayerFileDeleteTime(1209600000);
			setBuffCommandCost(10);
			setBonusMobSpawns(true);
			setCommandKeyWordClass("class");
			setCommandKeyWordDonator("donator");
			setCommandKeyWordDungeon("dungeon");
			setCommandKeyWordFaq("faq");
			setCommandKeyWordG("g");
			setCommandKeyWordH("h");
			setCommandKeyWordGH("gh");
			setCommandKeyWordHG("hg");
			setCommandKeyWordHead("hat");
			setCommandKeyWordJob("job");
			setCommandKeyWordGuild("guild");
			setCommandKeyWordPvp("pvp");
			setCommandKeyWordReset("reset");
			setCommandKeyWordSpell("spell");
			setCommandKeyWordModify("modify");
			setCommandKeyWordW("w");
			setCommandKeyWordBuff("buff");
			setCommandKeyWordWorld("world");
			setCommandKeyWordPlayers("list");
			setCommandKeyWordAlchemy("alchemy");
			setCommandKeyWordPlayers("players");
			setCommandKeyWordHead("head");
			setPerfectWarts(true);
		}
		
		if (getVersion() < 1.18)
		{
			setVersion(1.18);
			
			fcw.set(prefix + "commandKeyWord.rp", null); 
			fcw.set(prefix + "commandKeyWord.rpg", null); 
			setCommandKeyWordRpgHelp("rpgHelp");
			setCommandKeyWordRpgRAdmin("rAdmin");
		}
		
		if (getVersion() < 1.19)
		{
			setVersion(1.19);
			
			setCommandKeyWordDE("de");
		}
		
		if (getVersion() < 1.21)
		{
			setVersion(1.21);
			
			setRecordExpRewards(true);
			setCustomChatExclusions("TC,NC,ADMIN,MOD");
		}
		
		//Load up timed items.
		loadTimedItems();
	}
	
	private void loadTimedItems() 
	{
		List<Integer> itemIDs = converter.getIntegerListFromString(fcw.getStringS(prefix + "timedItems.ids"));
		List<Integer> itemCounts = converter.getIntegerListFromString(fcw.getStringS(prefix + ".timedItems.counts"));
		List<Byte> itemDataValues = converter.getByteListFromString(fcw.getStringS(prefix + "timedItems.dataValues"));
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













