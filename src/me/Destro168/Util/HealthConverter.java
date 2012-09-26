package me.Destro168.Util;

public class HealthConverter 
{
	double minecraftHearts;
	
	public int getMinecraftHearts() { return (int) minecraftHearts; }
	
	public HealthConverter(double maxHealth, double currentHealth)
	{
		double a;
		double healthEquivolent;
		
		//For max health and no health, we just handle the cases of 0, and 20 health.
		if (currentHealth >= maxHealth)
		{
			minecraftHearts = 20;
			return;
		}
		
		if (currentHealth <= 0)
		{
			minecraftHearts = 0;
			return;
		}
		
		//If health isn't 0, or 20, then we have to get a forumula for everything at 1-19
		
		a = maxHealth / 20; //We have to make sure to not include the 0 and 20. That's 1-19 aka or 0-18. Divide by 18 bitch.
		healthEquivolent = currentHealth / a; //Then divide by current Health to get how many one-eighteightns faggot you get.
		
		//Enforce everything staying in the range of 1-19 super hardcore.
		if (healthEquivolent >= 19)
		{
			minecraftHearts = 19;
			return;
		}
		
		if (healthEquivolent <= 0)
		{
			minecraftHearts = 1;
			return;
		}
		
		//Store minecraft hearts to be accessed later.
		minecraftHearts = healthEquivolent;
	}
}


/*
//See bottom comments for more explanation on the math.
a = maxHealth / 21; //max health divided by 21. Gives how much one half heart is, but a bit lower. Scales from 1-21. 21 is maxHealth.
healthEquivolent = currentHealth / a; //Current health divided by how many 1/2 hearts you have.

//Set the health to be in range of minecraft normal health (1-20)
if (healthEquivolent < 0)
	normalHealth = 0;
else if (healthEquivolent > 20)
{
	if (currentHealth < maxHealth)
		normalHealth = 20;
	else
		normalHealth = 19;
}
*/