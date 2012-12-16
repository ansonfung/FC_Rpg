package me.Destro168.FC_Rpg.Stores;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Material;

public class AlchemyStore
{
	private List<Purchasable> store = new ArrayList<Purchasable>();
	private int buyableCount;
	
	public List<Purchasable> getStore() { return store; }
	public int getBuyableCount() { return buyableCount; }
	
	public AlchemyStore()
	{
		loadStore();
	}
	
	private void loadStore()
	{
		store.add(new Purchasable(Material.DIRT,.1,true));
		store.add(new Purchasable(Material.GRAVEL,.1,true));
		store.add(new Purchasable(Material.SAND,.1,true));
		store.add(new Purchasable(Material.WOOL,.1,true));
		store.add(new Purchasable(Material.WOOD,.1,true));
		store.add(new Purchasable(Material.MOSSY_COBBLESTONE,.2,true));
		store.add(new Purchasable(Material.STONE,.2,true));
		store.add(new Purchasable(Material.SMOOTH_BRICK,.4,true));
		store.add(new Purchasable(Material.BRICK,.4,true));
		store.add(new Purchasable(Material.NETHERRACK,.4,true));
		store.add(new Purchasable(Material.NETHER_BRICK,.6,true));
		store.add(new Purchasable(Material.OBSIDIAN,3,true));
		store.add(new Purchasable(Material.BONE,1,true));
		store.add(new Purchasable(Material.BOOK,1,true));
		store.add(new Purchasable(Material.COAL,3,true));
		store.add(new Purchasable(Material.COOKIE,4,true));
		store.add(new Purchasable(Material.EGG,5,true));
		store.add(new Purchasable(Material.ARROW,1,true));
		store.add(new Purchasable(Material.REDSTONE,2,true));
		store.add(new Purchasable(Material.SUGAR,4,true));
		store.add(new Purchasable(Material.MELON,4,true));
		store.add(new Purchasable(Material.COOKED_FISH,10,true));
		store.add(new Purchasable(Material.COOKED_CHICKEN,12,true));
		store.add(new Purchasable(Material.RAW_BEEF,15,true));
		store.add(new Purchasable(Material.SULPHUR,4,true));
		store.add(new Purchasable(Material.BOOKSHELF,20,true));
		store.add(new Purchasable(Material.GLOWSTONE_DUST,3,true));
		store.add(new Purchasable(Material.NETHER_WARTS,5,true));
		store.add(new Purchasable(Material.BLAZE_ROD,3,true));
		store.add(new Purchasable(Material.GLASS,6,true));
		store.add(new Purchasable(Material.ICE,6,true));
		store.add(new Purchasable(Material.COBBLESTONE,1,true));
		store.add(new Purchasable(Material.IRON_INGOT,3,true));
		store.add(new Purchasable(Material.DIAMOND,5,true));
		store.add(new Purchasable(Material.GOLD_INGOT,10,true));
		
		//Store a count of all buyable items.
		buyableCount = store.size();
		
		store.add(new Purchasable(Material.GOLD_AXE,30,false));
		store.add(new Purchasable(Material.GOLD_HOE,20,false));
		store.add(new Purchasable(Material.GOLD_PICKAXE,30,false));
		store.add(new Purchasable(Material.GOLD_SPADE,10,false));
		store.add(new Purchasable(Material.GOLD_SWORD,20,false));
		store.add(new Purchasable(Material.GOLD_BOOTS,40,false));
		store.add(new Purchasable(Material.GOLD_HELMET,50,false));
		store.add(new Purchasable(Material.GOLD_LEGGINGS,70,false));
		store.add(new Purchasable(Material.GOLD_CHESTPLATE,80,false));
		store.add(new Purchasable(Material.LOG,.4,false));
		store.add(new Purchasable(Material.WOOD_AXE,.3,false));
		store.add(new Purchasable(Material.WOOD_HOE,.2,false));
		store.add(new Purchasable(Material.WOOD_PICKAXE,.3,false));
		store.add(new Purchasable(Material.WOOD_SPADE,.1,false));
		store.add(new Purchasable(Material.WOOD_SWORD,.2,false));
		store.add(new Purchasable(Material.LEATHER_BOOTS,.4,false));
		store.add(new Purchasable(Material.LEATHER_HELMET,.5,false));
		store.add(new Purchasable(Material.LEATHER_LEGGINGS,.7,false));
		store.add(new Purchasable(Material.LEATHER_CHESTPLATE,.8,false));
		store.add(new Purchasable(Material.STONE_AXE,3,false));
		store.add(new Purchasable(Material.STONE_HOE,2,false));
		store.add(new Purchasable(Material.STONE_PICKAXE,3,false));
		store.add(new Purchasable(Material.STONE_SPADE,1,false));
		store.add(new Purchasable(Material.STONE_SWORD,2,false));
		store.add(new Purchasable(Material.CHAINMAIL_BOOTS,4,false));
		store.add(new Purchasable(Material.CHAINMAIL_HELMET,5,false));
		store.add(new Purchasable(Material.CHAINMAIL_LEGGINGS,7,false));
		store.add(new Purchasable(Material.CHAINMAIL_CHESTPLATE,8,false));
		store.add(new Purchasable(Material.IRON_AXE,9,false));
		store.add(new Purchasable(Material.IRON_HOE,6,false));
		store.add(new Purchasable(Material.IRON_PICKAXE,9,false));
		store.add(new Purchasable(Material.IRON_SPADE,3,false));
		store.add(new Purchasable(Material.IRON_SWORD,6,false));
		store.add(new Purchasable(Material.IRON_BOOTS,12,false));
		store.add(new Purchasable(Material.IRON_HELMET,15,false));
		store.add(new Purchasable(Material.IRON_LEGGINGS,21,false));
		store.add(new Purchasable(Material.IRON_CHESTPLATE,24,false));
		store.add(new Purchasable(Material.DIAMOND_AXE,15,false));
		store.add(new Purchasable(Material.DIAMOND_HOE,10,false));
		store.add(new Purchasable(Material.DIAMOND_PICKAXE,15,false));
		store.add(new Purchasable(Material.DIAMOND_SPADE,5,false));
		store.add(new Purchasable(Material.DIAMOND_SWORD,10,false));
		store.add(new Purchasable(Material.DIAMOND_BOOTS,20,false));
		store.add(new Purchasable(Material.DIAMOND_HELMET,25,false));
		store.add(new Purchasable(Material.DIAMOND_LEGGINGS,35,false));
		store.add(new Purchasable(Material.DIAMOND_CHESTPLATE,40,false));
	}
}
