package me.Destro168.FC_Rpg.Util;

import org.bukkit.entity.EntityType;

public class MobAggressionCheck
{
	public MobAggressionCheck() { }
	
	public boolean getIsHostile(EntityType type) 
	{
		if (type == EntityType.COW)
			return false;
		else if (type == EntityType.SHEEP)
			return false;
		else if (type == EntityType.SQUID)
			return false;
		else if (type == EntityType.SNOWMAN)
			return false;
		else if (type == EntityType.PIG)
			return false;
		else if (type == EntityType.CHICKEN)
			return false;
		else if (type == EntityType.MUSHROOM_COW)
			return false;
		else if (type == EntityType.VILLAGER)
			return false;
		else if (type == EntityType.OCELOT)
			return false;
		else if (type == EntityType.WOLF)
			return false;
		else if (type == EntityType.BAT)
			return false;
		
		return true;
	}
}
