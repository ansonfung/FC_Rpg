package me.Destro168.FC_Rpg.LoadedObjects;

import java.util.List;

public class Spell 
{
	public int effectID;
	public String name;
	public String description;
	public List<Integer> duration;
	public List<Double> manaCost;
	public List<Double> constantMagnitude;
	public List<Double> attackMagnitude;
	public List<Double> magicMagnitude;
	public List<Double> intelligenceMagnitude;
	public List<Double> constitutionMagnitude;
	public List<Integer> radius;
	public boolean targetParty;
	public boolean classRestricted;
	public boolean requiresTarget;
	public boolean uncastable;
	
	//Constructor
	public Spell(int effectID_, String name_, String description_, List<Integer> duration_, List<Double> manaCost_, List<Double> constantMagnitude_,
			List<Double> attackMagnitude_, List<Double> magicMagnitude_, List<Double> intelligenceMagnitude_, List<Double> constitutionMagnitude_,
			List<Integer> radius_, boolean targetParty_, boolean classRestricted_, boolean requiresTarget_, boolean uncastable_)
	{
		effectID = effectID_;
		name = name_;
		description = description_;
		duration = duration_;
		manaCost = manaCost_;
		constantMagnitude = constantMagnitude_;
		attackMagnitude = attackMagnitude_;
		magicMagnitude = magicMagnitude_;
		intelligenceMagnitude = intelligenceMagnitude_;
		constitutionMagnitude = constitutionMagnitude_;
		radius = radius_;
		targetParty = targetParty_;
		classRestricted = classRestricted_;
		requiresTarget = requiresTarget_;
		uncastable = uncastable_;
	}
}


