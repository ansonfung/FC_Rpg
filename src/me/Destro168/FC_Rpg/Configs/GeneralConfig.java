package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GeneralConfig extends ConfigGod
{
	public List<ItemStack> timedItems;
	
	public GeneralConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "General");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		// Handle version updates.
		if (getVersion() < 1.27)
			setVersion(1.27);
		
		// Load static information
		getCustomChatExclusions();
		getJobRankCosts();
		getDefaultPrefix();
		getChatFormat();
		getChatFormatAdmin();
		getCommandKeyWordClass();
		getCommandKeyWordDonator();
		getCommandKeyWordDungeon();
		getCommandKeyWordDE();
		getCommandKeyWordFaq();
		getCommandKeyWordG();
		getCommandKeyWordH();
		getCommandKeyWordGH();
		getCommandKeyWordHG();
		getCommandKeyWordHead();
		getCommandKeyWordJob();
		getCommandKeyWordGuild();
		getCommandKeyWordPvp();
		getCommandKeyWordReset();
		getCommandKeyWordRpg();
		getCommandKeyWordRAdmin();
		getCommandKeyWordSpell();
		getCommandKeyWordAlchemy();
		getCommandKeyWordModify();
		getCommandKeyWordW();
		getCommandKeyWordBuff();
		getCommandKeyWordWorld();
		getCommandKeyWordPlayers();
		getTimedItemsInterval();
		getInactivePlayerFileDeleteTime();
		getDungeonEnterWaitPeriod();
		getDonatorBonusStatPercent();
		getDonatorLootBonusPercent();
		getBuffCommandCost();
		getLevelCap();
		getNotifcationInterval();
		getDungeonSelectionToolID();
		getBetterFishing();
		getPreventSpongeBreak();
		getBonusMobSpawns();
		getPerfectWarts();
		getDisableEnderPearls();
		getDonatorsCanHat();
		getRecordExpRewards();
		getInfiniteGold();
		getInfiniteDiamond();
		getHardcoreItemLoss();
		getCreativeControl();
		getPerfectBirch();
		
		// Always load up static timed items.
		loadTimedItems();
	}
	
	private void loadTimedItems() 
	{
		List<Integer> itemIDs = fcw.getStaticCustomIntegerList(prefix + "timedItems.ids", "364,322");
		List<Integer> itemCounts = fcw.getStaticCustomIntegerList(prefix + ".timedItems.counts", "5,1");
		List<Byte> itemDataValues = fcw.getStaticCustomByteList(prefix + "timedItems.dataValues", "0,1");
		ItemStack newItem;
		Material mat;
		
		// If the item id is set to null, then return.
		if (itemIDs.get(0).equals(-1))
			return;
		
		timedItems = new ArrayList<ItemStack>();
		
		for (int i = 0; i < itemIDs.size(); i++)
		{
			mat = Material.getMaterial(itemIDs.get(i));
			newItem = new ItemStack(mat, 0, itemDataValues.get(i));
			newItem.setAmount(itemCounts.get(i));
			timedItems.add(newItem);
		}
	}
	
	/****************************************************************
	 ^ Configuration Accessing Methods 
	 - All Staticly Accessed
	****************************************************************/
	
	public List<String> getCustomChatExclusions() { return fcw.getStaticCustomStringList(prefix + "customChatExclusions", "TC,NC,ADMIN,MOD"); }
	public List<Double> getJobRankCosts() { return fcw.getStaticCustomDoubleList(prefix + "jobRankCosts", "3200.0,20000.0,55000.0,105000.0,175000.0"); }
	public String getDefaultPrefix() { return fcw.getStaticString(prefix + "defaultPrefix", "&f[&7Guest&f] "); }
	public String getChatFormat() { return fcw.getStaticString(prefix + "chatFormat", "&7%time% [&6%level%&7] %prefix%&7%name%: %chat%"); }
	public String getChatFormatAdmin() { return fcw.getStaticString(prefix + "chatFormatAdmin", "&e%time% [&6%level%&e] %prefix%&e%name%: %chat%"); }
	public String getCommandKeyWordClass() { return fcw.getStaticString(prefix + "commandKeyWord.class", "class"); }
	public String getCommandKeyWordDonator() { return fcw.getStaticString(prefix + "commandKeyWord.donator", "donator"); }
	public String getCommandKeyWordDungeon() { return fcw.getStaticString(prefix + "commandKeyWord.dungeon", "dungeon"); }
	public String getCommandKeyWordDE() { return fcw.getStaticString(prefix + "commandKeyWord.DE", "de"); }
	public String getCommandKeyWordFaq() { return fcw.getStaticString(prefix + "commandKeyWord.faq", "faq"); }
	public String getCommandKeyWordG() { return fcw.getStaticString(prefix + "commandKeyWord.g", "g"); }
	public String getCommandKeyWordH() { return fcw.getStaticString(prefix + "commandKeyWord.h", "h"); }
	public String getCommandKeyWordGH() { return fcw.getStaticString(prefix + "commandKeyWord.gh", "gh"); }
	public String getCommandKeyWordHG() { return fcw.getStaticString(prefix + "commandKeyWord.hg", "hg"); }
	public String getCommandKeyWordHead() { return fcw.getStaticString(prefix + "commandKeyWord.head", "head"); }
	public String getCommandKeyWordJob() { return fcw.getStaticString(prefix + "commandKeyWord.job", "job"); }
	public String getCommandKeyWordGuild() { return fcw.getStaticString(prefix + "commandKeyWord.guild", "guild"); }
	public String getCommandKeyWordPvp() { return fcw.getStaticString(prefix + "commandKeyWord.pvp", "pvp"); }
	public String getCommandKeyWordReset() { return fcw.getStaticString(prefix + "commandKeyWord.reset", "reset"); }
	public String getCommandKeyWordRpg() { return fcw.getStaticString(prefix + "commandKeyWord.rpg", "rpg"); }
	public String getCommandKeyWordRAdmin() { return fcw.getStaticString(prefix + "commandKeyWord.rAdmin", "radmin"); }
	public String getCommandKeyWordSpell() { return fcw.getStaticString(prefix + "commandKeyWord.spell", "spell"); }
	public String getCommandKeyWordAlchemy() { return fcw.getStaticString(prefix + "commandKeyWord.alchemy", "alchemy"); }
	public String getCommandKeyWordModify() { return fcw.getStaticString(prefix + "commandKeyWord.modify", "modify"); }
	public String getCommandKeyWordW() { return fcw.getStaticString(prefix + "commandKeyWord.w", "w"); }
	public String getCommandKeyWordBuff() { return fcw.getStaticString(prefix + "commandKeyWord.buff", "buff"); }
	public String getCommandKeyWordWorld() { return fcw.getStaticString(prefix + "commandKeyWord.world", "world"); }
	public String getCommandKeyWordPlayers() { return fcw.getStaticString(prefix + "commandKeyWord.players", "players"); }
	public long getTimedItemsInterval() { return fcw.getStaticLong(prefix + "timedItems.interval", 3600000); }
	public long getInactivePlayerFileDeleteTime() { return fcw.getStaticLong(prefix + "inactivePlayerFileDeleteTime", 1209600000); }
	public long getDungeonEnterWaitPeriod() { return fcw.getStaticLong(prefix + "dungeonEnterWaitPeriod", 3600000); }
	public double getDonatorBonusStatPercent() { return fcw.getStaticDouble(prefix + "donatorBonusStatPercent", .1); }
	public double getDonatorLootBonusPercent() { return fcw.getStaticDouble(prefix + "donatorLootBonusPercent", .1); }
	public double getBuffCommandCost() { return fcw.getStaticDouble(prefix + "buffCommandCost", 10); }
	public int getLevelCap() { return fcw.getStaticInt(prefix + "levelCap", 100); }
	public int getNotifcationInterval() { return fcw.getStaticInt(prefix + "notificationInterval", 2000); }
	public int getDungeonSelectionToolID() { return fcw.getStaticInt(prefix + "dungeonSelectionToolID", 264); }
	public boolean getBetterFishing() { return fcw.getStaticBoolean(prefix + "betterFishing", true); }
	public boolean getPreventSpongeBreak() { return fcw.getStaticBoolean(prefix + "preventSpongeBreak", true); }
	public boolean getBonusMobSpawns() { return fcw.getStaticBoolean(prefix + "bonusMobSpawns", true); }
	public boolean getPerfectWarts() { return fcw.getStaticBoolean(prefix + "perfectWarts", true); }
	public boolean getDisableEnderPearls() { return fcw.getStaticBoolean(prefix + "disableEnderPearls", true); }
	public boolean getDonatorsCanHat() { return fcw.getStaticBoolean(prefix + "donatorsCanHat", true); }
	public boolean getRecordExpRewards() { return fcw.getStaticBoolean(prefix + "recordExpRewards", true); }
	public boolean getInfiniteGold() { return fcw.getStaticBoolean(prefix + "infiniteGold", true); }
	public boolean getInfiniteDiamond() { return fcw.getStaticBoolean(prefix + "infiniteDiamond", true); }
	public boolean getHardcoreItemLoss() { return fcw.getStaticBoolean(prefix + "hardcoreItemLoss", true); }
	public boolean getCreativeControl() { return fcw.getStaticBoolean(prefix + "creativeControl", true); }
	public boolean getPerfectBirch() { return fcw.getStaticBoolean(prefix + "perfectBirch", true); }
	public boolean getPerfectWheat() { return fcw.getStaticBoolean(prefix + "perfectWheat", true); }
}



















