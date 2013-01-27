package me.Destro168.FC_Rpg.Enchantment;

import me.Destro168.FC_Rpg.LoadedObjects.Spell;

public class Enchantment
{
	public String name = "";
	public String description = "";
	public Spell spell;
	public int procChance;
	
	// Constant enchants that don't proc.
	public Enchantment(String name_, String description_) 
	{
		name = name_;
		description = description_;
		spell = null;
		procChance = 0;
	}
	
	// Enchantments that proc spell effects.
	public Enchantment(Spell spell_, int procChance_) 
	{
		spell = spell_;
		name = spell.name;
		description = spell.description;
		procChance = procChance_;
	}
}
