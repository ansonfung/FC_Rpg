package me.Destro168.FC_Rpg.LoadedObjects;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;

public class RpgClass 
{
	private int ID;
	private String name;
	private String description;
	private int passiveID;
	private int restrictionID;
	private List<Integer> statGrowth;
	private List<Spell> spellBook;
	
	public int getID() { return ID; }
	public String getName() { return name; }
	public String getDescription() { return description; }
	public int getPassiveID() { return passiveID; }
	public int getRestrictionID() { return restrictionID; }
	public List<Integer> getStatGrowth() { return statGrowth; }
	public Spell getSpell(int i) { return spellBook.get(i); }
	public List<Spell> getSpellBook() { return spellBook; }
	
	public RpgClass(int ID_, String name_, String description_,  int passiveID_, int restrictionID_, List<Integer> statGrowth_, List<Integer> spellArray)
	{
		ID = ID_;
		name = name_;
		description = description_;
		passiveID = passiveID_;
		statGrowth = statGrowth_;
		restrictionID = restrictionID_;
		
		getSpells(spellArray);
	}
	
	private void getSpells(List<Integer> spellArray)
	{
		//Variable Declarations and Initializations
		Spell spell;
		spellBook = new ArrayList<Spell>();
		
		//Load up spells and store them inside of the spell list.
		for (int i : spellArray)
		{
			spell = FC_Rpg.spellConfig.getSpell(i);
			
			//Add all non-null spells.
			if (spell != null)
				spellBook.add(spell);
		}
	}
}



















