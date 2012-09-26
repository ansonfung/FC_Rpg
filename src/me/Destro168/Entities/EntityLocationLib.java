package me.Destro168.Entities;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityLocationLib 
{
	public EntityLocationLib()
	{
		
	}
	
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
	
	public Location getLocationBehindEntity(Location entityLocation)
	{
		//Variable Declarations
		World world = entityLocation.getWorld();
		Float yaw = entityLocation.getYaw();
		int damageTypeTeleportDistance = 2;
		Location tpLoc;
		double height = entityLocation.getY() + .5;
		
		//Make non-negative
		if (yaw < 0)
			yaw = yaw * -1;
		
		//Cut by 360 until within 0-360
		while (yaw >= 360)
			yaw = yaw - 360;
		
		//Determine location to move the player in based on the location of the facing direction of the monster.
		if (yaw >= 315 || yaw < 45)
		{
			tpLoc = new Location(world, entityLocation.getX(), height, entityLocation.getZ() - damageTypeTeleportDistance);
		}
		else if (yaw >= 45 && yaw < 135)
		{
			tpLoc = new Location(world, entityLocation.getX() + damageTypeTeleportDistance, entityLocation.getY() + .5, entityLocation.getZ());
		}
		else if (yaw >= 135 && yaw < 225)
		{
			tpLoc = new Location(world, entityLocation.getX(), entityLocation.getY() + .5, entityLocation.getZ() + damageTypeTeleportDistance);
		}
		else if (yaw >= 225 && yaw < 315)
		{
			tpLoc = new Location(world, entityLocation.getX() - damageTypeTeleportDistance, entityLocation.getY() + .5, entityLocation.getZ());
		}
		else
		{
			return entityLocation;
		}
		
		return tpLoc;
	}
	
	public Location getLocationInFrontOfEntity(Location entityLocation)
	{
		Location inFrontLoc;
		World world = entityLocation.getWorld();
		Float yaw = entityLocation.getYaw();
		int damageTypeTeleportDistance = 2;
		double height = entityLocation.getY() + .5;
		
		//Make non-negative
		if (yaw < 0)
			yaw = yaw * -1;
		
		//Cut by 360 until within 0-360
		while (yaw >= 360)
			yaw = yaw - 360;
		
		//Determine location to move the player in based on the location of the facing direction of the monster.
		if (yaw >= 315 || yaw < 45)
		{
			inFrontLoc = new Location(world, entityLocation.getX(), height, entityLocation.getZ() + damageTypeTeleportDistance);
		}
		else if (yaw >= 45 && yaw < 135)
		{
			inFrontLoc = new Location(world, entityLocation.getX() - damageTypeTeleportDistance, entityLocation.getY() + .5, entityLocation.getZ());
		}
		else if (yaw >= 135 && yaw < 225)
		{
			inFrontLoc = new Location(world, entityLocation.getX(), entityLocation.getY() + .5, entityLocation.getZ() - damageTypeTeleportDistance);
		}
		else if (yaw >= 225 && yaw < 315)
		{
			inFrontLoc = new Location(world, entityLocation.getX() + damageTypeTeleportDistance, entityLocation.getY() + .5, entityLocation.getZ());
		}
		else
		{
			return entityLocation;
		}
		
		return inFrontLoc;
	}
}




/*
Set new yaw to the reverse of yaw.
newYaw = yaw + 180;

if (newYaw >= 360)
	newYaw = newYaw - 360;
*/















