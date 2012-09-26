package me.Destro168.Util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DistanceModifierLib 
{
	int distanceModifier;
	
	int x;
	int y;
	int z;
	
	int xShiftAmount;
	int zShiftAmount;
	int yShiftAmount;
	
	public int getDistanceModifier() { return distanceModifier; }
	
	//Takes an x-coord, y-coord, and z-coord
	public DistanceModifierLib()
	{
		x = 0;
		y = 0;
		z = 0;
		xShiftAmount = 0;
		yShiftAmount = 0;
		zShiftAmount = 0;
	}
	
	private void setBaseCoordsByLocation(Location loc)
	{
		x = (int) loc.getX();
		y = (int) loc.getY();
		z = (int) loc.getZ();
	}
	
	private void calculateXYZModifier()
	{
		x = x - xShiftAmount;
		y = y - yShiftAmount;
		z = z - zShiftAmount;
		
		//Every 20 out equals 1 distance Modifier.
		if (x < 20 && x > -20) x = 0; //At least 1
		else x = x / 20;
		
		if (z < 20 && z > -20) z = 0;
		else z = z / 20;
		
		if (y < 10) y = 10;
		else if (y < 20) y = 8;
		else if (y < 30) y = 6;
		else if (y < 40) y = 4;
		else if (y < 50) y = 2;
		else y = 1;
		
		if (x < 0)
			x = x - x - x;

		if (z < 0)
			z = z - z - z;
		
		if ((x+z) < 1)
			distanceModifier = (1)*y;
		else
			distanceModifier = (x+z)*y;
	}
	
	private void calculateXYModifier()
	{
		x = x - xShiftAmount;
		y = y - yShiftAmount;
		
		//Every 20 out equals 1 distance Modifier.
		if (x < 10 && x > -10) x = 1; //At least 1
		else x = x / 10;

		if (y < 10) y = 10;
		else if (y < 20) y = 8;
		else if (y < 30) y = 6;
		else if (y < 40) y = 4;
		else if (y < 50) y = 2;
		else y = 1;

		if (x < 0)
			x = x * -1;

		distanceModifier = x*y;
	}
	
	private void calculateZYModifier()
	{
		z = z - zShiftAmount;
		y = y - yShiftAmount;
		
		//Every 20 out equals 1 distance Modifier.
		if (z < 10 && z > -10) z = 1; //At least 1
		else z = z / 10;

		if (y < 10) y = 10;
		else if (y < 20) y = 8;
		else if (y < 30) y = 6;
		else if (y < 40) y = 4;
		else if (y < 50) y = 2;
		else y = 1;

		if (z < 0)
			z = z * -1;
		
		distanceModifier = z*y;
	}
	
	public int getWorldDML(Location loc)
	{
		//Return a pure distance modifier that is exactly corrolated to distance.
		setBaseCoordsByLocation(loc);
		
		calculateXYZModifier();
		
		return distanceModifier;
	}
	
	public int getDungeonDML(Entity entity, boolean dungeonTravelsXDirection, int xShift, int yShift, int zShift)
	{
		setBaseCoordsByLocation(entity.getLocation());
		
		xShiftAmount = xShift;
		yShiftAmount = yShift;
		zShiftAmount = zShift;
		
		if (dungeonTravelsXDirection == true)
			calculateXYModifier();
		else
			calculateZYModifier();
		
		return distanceModifier;
	}
}

//Adjust to account for world spawn.
/*
if (x < 0)
	xShiftAmount = -305;
else
	xShiftAmount = 305;

if (y < 0)
	zShiftAmount = -321;
else
	zShiftAmount = 321;
*/

//Alter numbers to account for coordinates from map shift.
//x = x - 108;
//z = z - 88;
//y = y - 33; //Lower height to get to harder mobs faster.