package me.Destro168.FC_Rpg.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class MaterialLib
{
	public static final int TIER_TOTAL_ITEMS = 9;
	public static final int ARMOR_START_VALUE = 6;
	
	public List<List<Material>> tierList = new ArrayList<List<Material>>();
	
	public List<Material> t0 = new ArrayList<Material>();
	public List<Material> t1 = new ArrayList<Material>();
	public List<Material> t2 = new ArrayList<Material>();
	public List<Material> t3 = new ArrayList<Material>();
	public List<Material> t4 = new ArrayList<Material>();
	
	public List<Material> axes = new ArrayList<Material>();
	public List<Material> hoes = new ArrayList<Material>();
	public List<Material> pickaxes = new ArrayList<Material>();
	public List<Material> spades = new ArrayList<Material>();
	public List<Material> swords = new ArrayList<Material>();
	public List<Material> boots = new ArrayList<Material>();
	public List<Material> helmets = new ArrayList<Material>();
	public List<Material> leggings = new ArrayList<Material>();
	public List<Material> chestplates = new ArrayList<Material>();

	public List<Material> fullarmor = new ArrayList<Material>();
	
	public MaterialLib()
	{
		populateTiers();
		populateEquipment();
	}
	
	private void populateTiers()
	{
		t0.add(Material.WOOD_AXE);
		t0.add(Material.WOOD_HOE);
		t0.add(Material.WOOD_PICKAXE);
		t0.add(Material.WOOD_SPADE);
		t0.add(Material.WOOD_SWORD);
		
		t0.add(Material.LEATHER_BOOTS);
		t0.add(Material.LEATHER_HELMET);
		t0.add(Material.LEATHER_LEGGINGS);
		t0.add(Material.LEATHER_CHESTPLATE);
		
		t1.add(Material.STONE_AXE);
		t1.add(Material.STONE_HOE);
		t1.add(Material.STONE_PICKAXE);
		t1.add(Material.STONE_SPADE);
		t1.add(Material.STONE_SWORD);
		t1.add(Material.CHAINMAIL_BOOTS);
		t1.add(Material.CHAINMAIL_HELMET);
		t1.add(Material.CHAINMAIL_LEGGINGS);
		t1.add(Material.CHAINMAIL_CHESTPLATE);
		
		t2.add(Material.IRON_AXE);
		t2.add(Material.IRON_HOE);
		t2.add(Material.IRON_PICKAXE);
		t2.add(Material.IRON_SPADE);
		t2.add(Material.IRON_SWORD);
		t2.add(Material.IRON_BOOTS);
		t2.add(Material.IRON_HELMET);
		t2.add(Material.IRON_LEGGINGS);
		t2.add(Material.IRON_CHESTPLATE);
		
		t3.add(Material.DIAMOND_AXE);
		t3.add(Material.DIAMOND_HOE);
		t3.add(Material.DIAMOND_PICKAXE);
		t3.add(Material.DIAMOND_SPADE);
		t3.add(Material.DIAMOND_SWORD);
		t3.add(Material.DIAMOND_BOOTS);
		t3.add(Material.DIAMOND_HELMET);
		t3.add(Material.DIAMOND_LEGGINGS);
		t3.add(Material.DIAMOND_CHESTPLATE);
		
		t4.add(Material.GOLD_AXE);
		t4.add(Material.GOLD_HOE);
		t4.add(Material.GOLD_PICKAXE);
		t4.add(Material.GOLD_SPADE);
		t4.add(Material.GOLD_SWORD);
		t4.add(Material.GOLD_BOOTS);
		t4.add(Material.GOLD_HELMET);
		t4.add(Material.GOLD_LEGGINGS);
		t4.add(Material.GOLD_CHESTPLATE);
		
		tierList.add(t0);
		tierList.add(t1);
		tierList.add(t2);
		tierList.add(t3);
		tierList.add(t4);
	}
	
	public void populateEquipment()
	{
		for (int i = 0; i < tierList.size(); i++)
		{
			axes.add(tierList.get(i).get(0));
			hoes.add(tierList.get(i).get(1));
			pickaxes.add(tierList.get(i).get(2));
			spades.add(tierList.get(i).get(3));
			swords.add(tierList.get(i).get(4));
			boots.add(tierList.get(i).get(5));
			helmets.add(tierList.get(i).get(6));
			leggings.add(tierList.get(i).get(7));
			chestplates.add(tierList.get(i).get(8));
		}
	}
	
	public static String getCleanName(String itemName)
	{
		itemName = itemName.toLowerCase();
		char[] c = itemName.toCharArray();
		
		c[0] = toUpper(c[0]);
		
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == '_')
			{
				c[i] = ' ';
				c[i+1] = toUpper(c[i+1]);
				break;
			}
		}
		
		return String.valueOf(c);
	}
	
	private static char toUpper(char c)
	{
		if (c == 'a')
			return 'A';
		else if (c == 'b')
			return 'B';
		else if (c == 'c')
			return 'C';
		else if (c == 'd')
			return 'D';
		else if (c == 'e')
			return 'E';
		else if (c == 'f')
			return 'F';
		else if (c == 'g')
			return 'G';
		else if (c == 'h')
			return 'H';
		else if (c == 'i')
			return 'I';
		else if (c == 'j')
			return 'J';
		else if (c == 'k')
			return 'K';
		else if (c == 'l')
			return 'L';
		else if (c == 'm')
			return 'M';
		else if (c == 'n')
			return 'N';
		else if (c == 'o')
			return 'O';
		else if (c == 'p')
			return 'P';
		else if (c == 'q')
			return 'Q';
		else if (c == 'r')
			return 'R';
		else if (c == 's')
			return 'S';
		else if (c == 't')
			return 'T';
		else if (c == 'u')
			return 'U';
		else if (c == 'v')
			return 'V';
		else if (c == 'w')
			return 'W';
		else if (c == 'x')
			return 'X';
		else if (c == 'y')
			return 'Y';
		else if (c == 'z')
			return 'Z';
		
		return c;
	}
}






