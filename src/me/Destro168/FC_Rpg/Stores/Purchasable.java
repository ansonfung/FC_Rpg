package me.Destro168.FC_Rpg.Stores;

import org.bukkit.Material;

public class Purchasable
{
	public double cost;
	public Material material;
	public boolean canBuy;
	
	public Purchasable(Material mat_, double cost_, boolean canBuy_)
	{
		cost = cost_;
		material = mat_;
		canBuy = canBuy_;
	}
}