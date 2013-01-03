package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.LoadedObjects.RpgItem;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;

import org.bukkit.Material;

public class ItemConfig extends ConfigGod
{
	private int buyableCount;
	
	public boolean isCreated(int i) { return fcw.isSet(prefix + i + ".materialName"); }
	
	public void setMaterialID(int i, String x) { fcw.set(prefix + i + ".materialName", x); }
	public void setDataValue(int i, short x) { fcw.set(prefix + i + ".dataValue", x); }
	public void setEnchantable(int i, boolean x) { fcw.set(prefix + i + ".enchantable", x); }
	public void setPriceBuy(int i, double x) { fcw.set(prefix + i + ".price.buy", x); }
	public void setPriceSell(int i, double x) { fcw.set(prefix + i + ".price.sell", x); }
	public void setDropLevelMax(int i, int x) { fcw.set(prefix + i + ".drop.level.max", x); }
	public void setDropLevelMin(int i, int x) { fcw.set(prefix + i + ".drop.level.min", x); }
	public void setDropAmountFlat(int i, int x) { fcw.set(prefix + i + ".drop.amount.flat", x); }
	public void setDropAmountRandom(int i, int x) { fcw.set(prefix + i + ".drop.amount.random", x); }
	
	public String getMaterialName(int i) { return fcw.getString(prefix + i + ".materialName"); }
	public short getDataValue(int i) { return fcw.getShort(prefix + i + ".dataValue"); }
	public boolean getEnchantable(int i) { return fcw.getBoolean(prefix + i + ".enchantable"); }
	public double getPriceBuy(int i) { return fcw.getDouble(prefix + i + ".price.buy"); }
	public double getPriceSell(int i) { return fcw.getDouble(prefix + i + ".price.sell"); }
	public int getDropLevelMax(int i) { return fcw.getInt(prefix + i + ".drop.level.max"); }
	public int getDropLevelMin(int i) { return fcw.getInt(prefix + i + ".drop.level.min"); }
	public int getDropAmountFlat(int i) { return fcw.getInt(prefix + i + ".drop.amount.flat"); }
	public int getDropAmountRandom(int i) { return fcw.getInt(prefix + i + ".drop.amount.random"); }

	public Material getMaterial(int i) { return Material.getMaterial(fcw.getInt(prefix + i + ".materialName")); }
	
	public void setAll(int i, String matName, int datavalue, boolean enchantable, double priceBuy, double priceSell,
			int dropLevelMin, int dropLevelMax, int dropAmountFlat, int dropAmountRandom)
	{ 
		setMaterialID(i, matName);
		setDataValue(i,(short) datavalue);
		setEnchantable(i,enchantable);
		setPriceBuy(i,priceBuy);
		
		if (priceSell == -1)
			setPriceSell(i,priceBuy * .05);
		else
			setPriceSell(i,priceSell);
		
		setDropLevelMin(i,dropLevelMin);
		setDropLevelMax(i,dropLevelMax);
		setDropAmountFlat(i,dropAmountFlat);
		setDropAmountRandom(i,dropAmountRandom);
	}
	
	public List<RpgItem> getLoadedRpgItemList()
	{
		List<RpgItem> rpgItemList = new ArrayList<RpgItem>();
		int breakPoint = 0;
		
		for (int i = 0; i < 99999; i++)
		{
			if (this.isCreated(i))
				rpgItemList.add(getLoadedRpgItem(i));
			else
			{
				breakPoint++;
				
				if (breakPoint >= 50)
					break;
			}
		}
		
		return rpgItemList;
	}
	
	public RpgItem getLoadedRpgItem(int i)
	{
		RpgItem item = new RpgItem(getMaterialName(i),getDataValue(i),getEnchantable(i),getPriceBuy(i),getPriceSell(i),
				getDropLevelMin(i),getDropLevelMax(i),getDropAmountFlat(i),getDropAmountRandom(i));
		
		return item;
	}
	
	public ItemConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Items");
		buyableCount = -1;
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		if (getVersion() < 0.1)
		{
			setVersion(0.1);
			List<RpgItem> rpgItem = new ArrayList<RpgItem>();
			
			//i, madId, datavalue, enchantable, priceBuy, priceSell, dropLevelMin, dropLevelMax, dropAmountFlat, dropAmountRandom
			rpgItem.add(new RpgItem(Material.LOG.toString(), 0, false, 4, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.DIRT.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.GRAVEL.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.SAND.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.GLASS.toString(), 0, false, 1.5, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.WOOL.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.NETHERRACK.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.COBBLESTONE.toString(), 0, false, 1.2, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.MOSSY_COBBLESTONE.toString(), 0, false, 1.2, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.STONE.toString(), 0, false, 1.5, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.BRICK.toString(), 0, false, 3, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.NETHER_BRICK.toString(), 0, false, 3, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.OBSIDIAN.toString(), 0, false, 5, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.BONE.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.COAL.toString(), 0, false, 1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.ARROW.toString(), 0, false, .1, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.REDSTONE.toString(), 0, false, .75, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.SUGAR.toString(), 0, false, .5, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.MELON.toString(), 0, false, .5, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.BOOK.toString(), 0, false, 5, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.COOKED_FISH.toString(), 0, false, 3, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.COOKED_CHICKEN.toString(), 0, false, 3, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.RAW_BEEF.toString(), 0, false, 4, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.SULPHUR.toString(), 0, false, 4, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.GLOWSTONE_DUST.toString(), 0, false, 4, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.SOUL_SAND.toString(), 0, false, 20, .1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.NETHER_STALK.toString(), 0, false, 100, .1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.GHAST_TEAR.toString(), 0, false, 20, .1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.SLIME_BALL.toString(), 0, false, 20, .1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.BLAZE_ROD.toString(), 0, false, 20, .1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.ICE.toString(), 0, false, 50, .1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.IRON_INGOT.toString(), 0, false, 4, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.DIAMOND.toString(), 0, false, 6, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.GOLD_INGOT.toString(), 0, false, 8, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.EXP_BOTTLE.toString(), 0, false, 100, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.ANVIL.toString(), 0, false, 124, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.BEACON.toString(), 0, false, 5000, -1, -1, -1, -1, -1));
			rpgItem.add(new RpgItem(Material.MOB_SPAWNER.toString(), 0, false, 99999, -1, -1, -1, -1, -1));
			
			rpgItem.add(new RpgItem(Material.BOW.toString(), 0, false, 3, -1, 1, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.WOOD_AXE.toString(), 0, true, -1, .3, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.WOOD_HOE.toString(), 0, true, -1, .2, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.WOOD_PICKAXE.toString(), 0, true, -1, .1, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.WOOD_SPADE.toString(), 0, true, -1, .2, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.WOOD_SWORD.toString(), 0, true, -1, .2, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.LEATHER_BOOTS.toString(), 0, true, -1, .4, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.LEATHER_HELMET.toString(), 0, true, -1, .5, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.LEATHER_LEGGINGS.toString(), 0, true, -1, .7, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.LEATHER_CHESTPLATE.toString(), 0, true, -1, .8, 1, 20, 1, 0));
			rpgItem.add(new RpgItem(Material.STONE_AXE.toString(), 0, true, -1, .6, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.STONE_HOE.toString(), 0, true, -1, .4, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.STONE_PICKAXE.toString(), 0, true, -1, .6, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.STONE_SPADE.toString(), 0, true, -1, .4, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.STONE_SWORD.toString(), 0, true, -1, .4, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.CHAINMAIL_BOOTS.toString(), 0, true, -1, .8, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.CHAINMAIL_HELMET.toString(), 0, true, -1, 1.0, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.CHAINMAIL_LEGGINGS.toString(), 0, true, -1, 1.4, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.CHAINMAIL_CHESTPLATE.toString(), 0, true, -1, 1.6, 21, 40, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_AXE.toString(), 0, true, -1, 40, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_HOE.toString(), 0, true, -1, .8, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_PICKAXE.toString(), 0, true, -1, 1.2, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_SPADE.toString(), 0, true, -1, .8, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_SWORD.toString(), 0, true, -1, .8, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_BOOTS.toString(), 0, true, -1, 1.6, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_HELMET.toString(), 0, true, -1, 2.0, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_LEGGINGS.toString(), 0, true, -1, 2.8, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.IRON_CHESTPLATE.toString(), 0, true, -1, 3.2, 41, 60, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_AXE.toString(), 0, true, -1, 1.8, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_HOE.toString(), 0, true, -1, 1.2, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_PICKAXE.toString(), 0, true, -1, 1.8, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_SPADE.toString(), 0, true, -1, 1.2, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_SWORD.toString(), 0, true, -1, 1.2, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_BOOTS.toString(), 0, true, -1, 2.4, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_HELMET.toString(), 0, true, -1, 3.0, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_LEGGINGS.toString(), 0, true, -1, 4.2, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.DIAMOND_CHESTPLATE.toString(), 0, true, -1, 4.8, 61, 80, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_AXE.toString(), 0, true, -1, 2.4, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_HOE.toString(), 0, true, -1, 1.6, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_PICKAXE.toString(), 0, true, -1, 2.4, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_SPADE.toString(), 0, true, -1, 1.6, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_SWORD.toString(), 0, true, -1, 1.6, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_BOOTS.toString(), 0, true, -1, 3.2, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_HELMET.toString(), 0, true, -1, 4.0, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_LEGGINGS.toString(), 0, true, -1, 5.6, 81, 100, 1, 0));
			rpgItem.add(new RpgItem(Material.GOLD_CHESTPLATE.toString(), 0, true, -1, 6.4, 81, 100, 1, 0));
			
			int count = 0;
			
			for (RpgItem i : rpgItem)
			{
				setAll(count, i.material, i.dataValue, i.enchantable, i.priceBuy, i.priceSell, i.dropLevelMin, i.dropLevelMax, i.dropAmountFlat, i.dropAmountRandom);
				count++;
			}
		}
	}
	
	public int getBuyableCount() 
	{
		if (buyableCount == -1)
		{
			buyableCount = 0;
			
			for (RpgItem item : FC_Rpg.rpgItemList)
			{
				if (item.priceBuy > -1)
				{
					buyableCount++;
				}
			}
		}
		
		return buyableCount;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	