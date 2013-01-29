package me.Destro168.FC_Rpg.Spells;

import java.util.Map;

import org.bukkit.util.Java15Compat;

import com.google.common.collect.Maps;

public enum SpellEffect 
{
	// [0 - 999] Regular enchants.
	DEFENSE(0),
	DAMAGE_BONUS(1),
	IMMORTAL(2),
	THORNS(3),
	HEALTH_STEAL(4),
	DODGE(5),
	MANA_STEAL(6),
	TELEPORT_STRIKE(7),
	DISABLED(8),
	SPEED(9),
	FIREBALL(10),
	POISON(11),
	BLEED(12),
	AOE(13),
	FROST_STRIKE_AOE(14),
	HEAL_SELF(15),
	HEAL_OTHER(16),
	WEAKEN(17),
	LIGHTNING(18),
	BOOST_STATS(19),
	DAMAGE_SCALED_BY_MISSING_HEALTH(20),
	SACRIFICE_HEALTH_FOR_DAMAGE(21),
	HEAL_SELF_OR_OTHER(22),
	HEAL_SELF_PERCENT(23),
	CRITICAL_DAMAGE_DOUBLE(24),
	HEAL_CHANCE(25),
	FIRE_STRIKE(26),
	IGNORE_ARMOR(27),
	FROST_STRIKE(28),
	
	// [1000 - 1999] Fancy custom effect enchants.
	BONUS_GOLD(1000),
	BONUS_EXPERIENCE(1001),
	TAUNT(1003),
	ALCHEMY(1004),
	MANA_REGEN(1005);
	
	private final int id;
    private final static Map<String, SpellEffect> BY_NAME = Maps.newHashMap();
    private static SpellEffect[] byId = new SpellEffect[383];
    
	// Could never figure out enums. But, got a lot of practice, realize how they worked, was like "MATERIAL LIB DOES THIS". Jacked this from bukkit, fuck yeah. Thanks bukkit! <3
	private SpellEffect(int i) 
	{ 
		id = i;
	}
	
	public int getID()
	{
		return id;
	}
	
	public static String getSpellEffectName(final int id) 
	{
        if (byId.length > id && id >= 0) {
            return byId[id].name();
        } else {
            return null;
        }
    }
	
	public static SpellEffect getSpellEffect(final int id) 
	{
        if (byId.length > id && id >= 0) {
            return byId[id];
        } else {
            return null;
        }
    }
	
	public static SpellEffect getSpellEffect(final String name) {
        return BY_NAME.get(name);
    }
	
	static
	{
		for (SpellEffect eID : values())
		{
			if (byId.length > eID.id)
				byId[eID.id] = eID;
			else
			{
				byId = Java15Compat.Arrays_copyOfRange(byId, 0, eID.id + 2);
				byId[eID.id] = eID;
			}
		}
	}
}


















