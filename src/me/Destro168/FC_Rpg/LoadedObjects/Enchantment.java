package me.Destro168.FC_Rpg.LoadedObjects;

public class Enchantment
{
	public String name = "";
	public String description = "";
	public Spell spell;
	public boolean modifyAttack;
	public boolean modifyConstitution;
	public boolean modifyIntelligence;
	public boolean modifyMagic;
	public int procChance;
	
	// Prefix enchantments ~ Enchants that modify stats.
	public Enchantment(String name_, String description_, boolean modifyAttack_, boolean modifyConstitution_,
			boolean modifyIntelligence_, boolean modifyMagic_) 
	{
		name = name_;
		description = description_;
		spell = null;
		procChance = 0;
		
		modifyAttack = modifyAttack_;
		modifyConstitution = modifyConstitution_;
		modifyIntelligence = modifyIntelligence_;
		modifyMagic = modifyMagic_;
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
