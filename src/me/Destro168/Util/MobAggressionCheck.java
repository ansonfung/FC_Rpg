package me.Destro168.Util;

import org.bukkit.entity.EntityType;

public class MobAggressionCheck
{
	boolean isHostile;
	
	public boolean isHostile() {return isHostile;}
	
	public MobAggressionCheck(EntityType type)
	{
		isHostile = true;
		
		if (type == EntityType.COW)
			isHostile = false;
		else if (type == EntityType.SHEEP)
			isHostile = false;
		else if (type == EntityType.SQUID)
			isHostile = false;
		else if (type == EntityType.SNOWMAN)
			isHostile = false;
		else if (type == EntityType.PIG)
			isHostile = false;
		else if (type == EntityType.CHICKEN)
			isHostile = false;
		else if (type == EntityType.MUSHROOM_COW)
			isHostile = false;
		else if (type == EntityType.VILLAGER)
			isHostile = false;
		else if (type == EntityType.OCELOT)
			isHostile = false;
	}
}
