package me.Destro168.FC_Rpg.LoadedObjects;

import java.util.List;

public class Spell 
{
	private int effectID;
	private String name;
	private String description;
	private List<Integer> duration;
	private List<Double> manaCost;
	private List<Double> constantMagnitude;
	private List<Double> attackMagnitude;
	private List<Double> magicMagnitude;
	private List<Double> intelligenceMagnitude;
	private List<Double> constitutionMagnitude;
	private List<Integer> radius;
	private boolean targetParty;
	private boolean restricted;
	
	//Gets
	public int getEffectID() { return effectID; }
	public String getName() { return name; }
	public String getDescription() { return description; }
	public List<Integer> getDuration() { return duration; }
	public List<Double> getManaCost() { return manaCost; }
	public List<Double> getConstantMagnitude() { return constantMagnitude; }
	public List<Double> getAttackMagnitude() { return attackMagnitude; }
	public List<Double> getMagicMagnitude() { return magicMagnitude; }
	public List<Double> getIntelligenceMagnitude() { return intelligenceMagnitude; }
	public List<Double> getConstitutionMagnitude() { return constitutionMagnitude; }
	public List<Integer> getRadius() { return radius; }
	public boolean getTargetParty() { return targetParty; }
	public boolean getRestricted() { return restricted; }
	
	//Constructor
	public Spell(int effectID_, String name_, String description_, List<Integer> duration_, List<Double> manaCost_, List<Double> constantMagnitude_,
			List<Double> attackMagnitude_, List<Double> magicMagnitude_, List<Double> intelligenceMagnitude_, List<Double> constitutionMagnitude_,
			List<Integer> radius_, boolean targetParty_, boolean restricted_)
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
		restricted = restricted_;
	}
}
