package me.Destro168.Spells;

public class EffectIDs 
{
	private final int BUFF_COUNT = 1000;
	
	public static final int DEFENSE = 1;
	public static final int ATTACK = 2;
	public static final int IMMORTAL = 4;
	public static final int THORNS = 5;
	public static final int LIFESTEAL = 6;
	public static final int DODGE = 7;
	
	public static final int TAUNT = 1000;
	public static final int FIREBALL = 1001;
	public static final int SPEED_POTION = 1002;
	public static final int FIRE_ARROW = 1003;
	public static final int DISABLED = 1004;
	public static final int POISON = 1005;
	public static final int BLEED = 1006;
	public static final int AOE = 1007;
	public static final int BACKSTAB = 1008;
	public static final int HEAL_SELF = 1009;
	public static final int HEAL_OTHER = 1010;
	public static final int WEAKEN = 1011;
	public static final int ALCHEMY = 1012;
	public static final int LIGHTNING = 1013;
	public static final int BOOST_STATS = 1014;
	public static final int TELEPORT_STRIKE = 1015;
	public static final int DAMAGE_SCALED_BY_HEALTH = 1016;
	public static final int DAMAGE_BY_MISSING_HEALTH = 1017;
	public static final int SACRIFICE_HEALTH_FOR_DAMAGE = 1018;
	
	public static final int DAMAGE_BOOST = 2000;
	
	public EffectIDs() { }
	
	public boolean getIsBuff(int id)
	{
		if (id < BUFF_COUNT)
			return true;
		
		return false;
	}
}
