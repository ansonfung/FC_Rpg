package me.Destro168.FC_Rpg.LoadedObjects;

import org.bukkit.Material;

public class RpgItem
{
	public String material;
	public short dataValue;
	public int enchantType;
	public double priceBuy;
	public double priceSell;
	public int dropLevelMax;
	public int dropLevelMin;
	public int dropAmountFlat;
	public int dropAmountRandom;
	public int configFieldNumber;
	
	public Material getMaterial() { return Material.getMaterial(material); }
	
	public RpgItem(String materialName, int datavalue_, int enchantType_, double priceBuy_, double priceSell_,
			int dropLevelMin_, int dropLevelMax_, int dropAmountFlat_, int dropAmountRandom_)
	{ 
		material = materialName;
		dataValue = (short) datavalue_;
		enchantType = enchantType_;
		priceBuy = priceBuy_;
		priceSell = priceSell_;
		dropLevelMin = dropLevelMin_;
		dropLevelMax = dropLevelMax_;
		dropAmountFlat = dropAmountFlat_;
		dropAmountRandom = dropAmountRandom_;
	}
}
