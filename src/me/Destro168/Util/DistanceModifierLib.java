package me.Destro168.Util;

import me.Destro168.Configs.WorldConfig;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Location;

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
	
	public void setShiftByLocation(Location shiftLocation)
	{
		xShiftAmount = (int) shiftLocation.getX();
		yShiftAmount = (int) shiftLocation.getY();
		zShiftAmount = (int) shiftLocation.getZ();
	}
	
	private void positizeXZ()
	{
		if (x < 0)
			x = x * -1;
		
		if (z < 0)
			z = z * -1;
	}
	
	private int calculateXYZModifier()
	{
		double xScale = FC_Rpg.generalConfig.getXScale();
		double zScale = FC_Rpg.generalConfig.getZScale();
		double yScale = FC_Rpg.generalConfig.getYScale();
		double xFinal;
		double zFinal;
		double yFinal;
		
		positizeXZ();
		
		if (xShiftAmount < 0)
			xShiftAmount = xShiftAmount * -1;
		
		if (zShiftAmount < 0)
			zShiftAmount = zShiftAmount * -1;
		
		x = x - xShiftAmount;
		z = z - zShiftAmount;
		y = y - yShiftAmount;
		
		if (xScale < 0)
			xScale = xScale * -1;
		
		if (zScale < 0)
			zScale = zScale * -1;
		
		positizeXZ();
		
		//Every xScale out equals 1 distance Modifier.
		if (x < xScale) xFinal = 0;
		else xFinal = x / xScale;
		
		if (z < zScale) zFinal = 0;
		else zFinal = z / zScale;
		
		yFinal = 1;
		
		if (y < 0)
			yFinal += y / yScale;
		
		if (x < xScale && z < zScale)
			distanceModifier = (int) ((1)*yFinal);
		else
		{
			if (x < xScale || z < zScale)
				distanceModifier = 1 + (int) ((xFinal+zFinal)*yFinal);
			else
				distanceModifier = (int) ((xFinal+zFinal)*yFinal);
		}
		
		return distanceModifier;
	}
	
	public int getWorldDML(Location entityLoc)
	{
		//Variable Declarations
		WorldConfig wm = new WorldConfig();
		
		//Set base coords and shift amount for the distance modifier.
		setBaseCoordsByLocation(entityLoc);
		
		//Set shift location.
		setShiftByLocation(wm.getLevelOne(entityLoc.getWorld().getName()));
		
		//Return a pure distance modifier that is exactly corrolated to distance.
		return calculateXYZModifier();
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