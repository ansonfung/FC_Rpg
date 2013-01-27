package me.Destro168.FC_Rpg.Spells;

import java.util.ArrayList;
import java.util.List;

public class EffectIDs 
{
	public List<Integer> effectIDList = new ArrayList<Integer>();
	
	public static final int EFFECT_ID_COUNT = 30;
	public static final int SPECIAL_EFFECT_ID_COUNT = 5;
	
	// [0 - BUFF_COUNT_MAX] Timed versions of spells.
	public static final int DEFENSE = 0;
	public static final int DAMAGE_BONUS = 1;
	public static final int IMMORTAL = 2;
	public static final int THORNS = 3;
	public static final int HEALTH_STEAL = 4;
	public static final int DODGE = 5;
	public static final int MANA_STEAL = 6;
	public static final int TELEPORT_STRIKE = 7;
	public static final int DISABLED = 8;
	public static final int SPEED = 9;
	public static final int FIREBALL = 10;
	public static final int POISON = 11;
	public static final int BLEED = 12;
	public static final int AOE = 13;
	public static final int FROST_STRIKE_AOE = 14;
	public static final int HEAL_SELF = 15;
	public static final int HEAL_OTHER = 16;
	public static final int WEAKEN = 17;
	public static final int LIGHTNING = 18;
	public static final int BOOST_STATS = 19;
	public static final int DAMAGE_SCALED_BY_MISSING_HEALTH = 20;
	public static final int SACRIFICE_HEALTH_FOR_DAMAGE = 21;
	public static final int HEAL_SELF_OR_OTHER = 22;
	public static final int HEAL_SELF_PERCENT = 23;
	public static final int CRITICAL_DAMAGE_DOUBLE = 24;
	public static final int HEAL_CHANCE = 25;
	public static final int BONUS_STATS = 26;
	public static final int FIRE_STRIKE = 27;
	public static final int IGNORE_ARMOR = 28;
	public static final int FROST_STRIKE = 29;
	
	// [1000 - 1999] Some kind of extremely custom effect.
	public static final int BONUS_GOLD = 1000;
	public static final int BONUS_EXPERIENCE = 1001;
	public static final int TAUNT = 1003;
	public static final int ALCHEMY = 1004;
	public static final int MANA_REGEN = 1005;
	
	public EffectIDs() 
	{
		// Populate effect id list with effect ids.
		for (int i = 0; i < EFFECT_ID_COUNT; i++)
			effectIDList.add(i);
		
		for (int i = 0; i < SPECIAL_EFFECT_ID_COUNT; i++)
			effectIDList.add(i + 1000);
	}
}


















