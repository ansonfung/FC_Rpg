package me.Destro168.Entities;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityLocationLib 
{
	public EntityLocationLib() { }
	
	//Compares two players to each other
	//Returns true if the second index is close to the first one.
	public boolean isNearby(Entity e1, Entity e2, int distance)
	{
		//Variable Declarations
		int x,y,z;
		
		if ((e1 == null) || (e2 == null))
			return false;
		
		Location a = e1.getLocation();
		Location b = e2.getLocation();
		
		//Find the party leader - party member distance.
		x = a.getBlockX() - b.getBlockX();
		y = a.getBlockY() - b.getBlockY();
		z = a.getBlockZ() - b.getBlockZ();
		
		//Find absolute values of those distances if negative.
		if (x < 0)
			x = x * -1;
		
		if (z < 0)
			z = z * -1;
		
		if (y < 0)
			y = y * -1;
		
		//Return that the player isn't close enough if they are further than 10 away on any axis to the party leader.
		if (x > distance)
			return false;
		
		if (y > distance)
			return false;
		
		if (z > distance)
			return false;
		
		//Return true if they are in range.
		return true;
	}
	
	public Location getLocationBehindEntity(Location entityLoc)
	{
		//Variable Declarations
		World world = entityLoc.getWorld();
		Float yaw = entityLoc.getYaw();
		int damageTypeTeleportDistance = 2;
		Location tpLoc;
		double x;
		double z;
		double y = entityLoc.getY() + .2;
		
		//Make non-negative
		if (yaw < 0)
			yaw = yaw * -1;
		
		//Cut by 360 until within 0-360
		while (yaw >= 360)
			yaw = yaw - 360;
		
		//Determine location to move the player in based on the location of the facing direction of the monster.
		if (yaw >= 315 || yaw < 45)
		{
			x = entityLoc.getX();
			z = entityLoc.getZ() - damageTypeTeleportDistance;
			yaw = 0.0F;
		}
		else if (yaw >= 45 && yaw < 135)
		{
			x = entityLoc.getX() - damageTypeTeleportDistance;
			z = entityLoc.getZ();
			yaw = 270.0F;
		}
		else if (yaw >= 135 && yaw < 225)
		{
			x = entityLoc.getX();
			z = entityLoc.getZ() + damageTypeTeleportDistance;
			yaw = 180.0F;
		}
		else if (yaw >= 225 && yaw < 315)
		{
			x = entityLoc.getX() + damageTypeTeleportDistance;
			z = entityLoc.getZ();
			yaw = 90.0F;
		}
		else
		{
			FC_Rpg.plugin.getLogger().info("Error with yaw being out of range.");
			return entityLoc;
		}
		
		//Set the tpLoc.
		tpLoc = new Location(world, x, y, z, yaw, 30.0F);
		
		return tpLoc;
	}
}




/*
Set new yaw to the reverse of yaw.
newYaw = yaw + 180;

if (newYaw >= 360)
	newYaw = newYaw - 360;
*/















