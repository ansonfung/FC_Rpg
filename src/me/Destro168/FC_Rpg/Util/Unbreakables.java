package me.Destro168.FC_Rpg.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class Unbreakables 
{
	private List<Material> unbreakables = new ArrayList<Material>();
	
	public List<Material> getUnbreakables() { return unbreakables; }
	
	public Unbreakables()
	{
		unbreakables.add(Material.BEDROCK);
		unbreakables.add(Material.CHEST);
		unbreakables.add(Material.WOOD_DOOR);
		unbreakables.add(Material.SIGN);
		unbreakables.add(Material.SIGN_POST);
		unbreakables.add(Material.WALL_SIGN);
	}
}
