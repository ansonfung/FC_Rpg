package me.Destro168.FC_Rpg;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.Destro168.FC_Rpg.Util.DistanceModifierLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MobSpawnManager
{
	public Map<Player, Integer> taskIDs = new HashMap<Player, Integer>();
	
	public MobSpawnManager() 
	{
		for (Player p : Bukkit.getServer().getOnlinePlayers())
			beginMobSpawns(p);
	}
	
	public void endMobSpawns(final Player player)
	{
		if (taskIDs.containsKey(player))
			Bukkit.getScheduler().cancelTask(taskIDs.get(player));
	}
	
	public void beginMobSpawns(final Player player)
	{
		if (!FC_Rpg.generalConfig.getBonusMobSpawns())
			return;
		
		endMobSpawns(player);
		
		taskIDs.put(player,Bukkit.getScheduler().scheduleSyncRepeatingTask(FC_Rpg.plugin, new Runnable() 
		{
			@Override
			public void run()
			{
				Location playerLocation = player.getLocation();
				
				if (!FC_Rpg.worldConfig.getIsRpgWorld(playerLocation.getWorld().getName()))
					return;
				
				//If it's raining and it's night time, then...
				if ((playerLocation.getWorld().hasStorm()) || (playerLocation.getWorld().getTime() >= 14000 || playerLocation.getWorld().getTime() < 0))
				{
					Location loc = playerLocation.add(a(),b(),a());
					
					while (!loc.getBlock().getType().equals(Material.AIR))
						loc = playerLocation.add(a(),b(),a());
					
					while (loc.getBlock().getType() == Material.AIR)
					{
						loc = loc.subtract(0,1,0);
						
						if (loc.getY() <= 0)
							return;
					}
					
					if (loc.getBlock().isLiquid() == true)
						return;
					
					loc.add(0,3,0);
					
					DistanceModifierLib dml = new DistanceModifierLib();
					final LivingEntity e = (LivingEntity) loc.getWorld().spawnEntity(loc,getRandomMobType(dml.getWorldDML(loc)));
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
					{
						@Override
						public void run()
						{
							nukeMob(e);
						}
					}, 3200);
				}
			}
			
			private int a() //Get a random coordinate
			{
				Random rand = new Random();
				int x;
				
				if (rand.nextBoolean())
					x = rand.nextInt(40) + 8;
				else
					x = 0 - rand.nextInt(40) - 8;
				
				return x;
			}
			
			private int b() //Get a random coordinate
			{
				Random rand = new Random();
				int x;
				
				if (rand.nextBoolean())
					x = rand.nextInt(10);
				else
					x = 0 - rand.nextInt(10);
				
				return x;
			}
			
			private EntityType getRandomMobType(int modifier)
			{
				Random rand = new Random();
				
				if (modifier < 50)
				{
					int spawnChance = rand.nextInt(5);
					
					if (spawnChance == 0)
						return EntityType.ZOMBIE;
					else if (spawnChance == 1)
						return EntityType.ENDERMAN;
					else if (spawnChance == 2)
						return EntityType.CREEPER;
					else if (spawnChance == 3)
						return EntityType.SKELETON;
					else if (spawnChance == 4)
						return EntityType.PIG_ZOMBIE;
				}
				else
				{
					int spawnChance = rand.nextInt(91002);
					
					if (spawnChance <= 16000)
						return EntityType.ZOMBIE;
					else if (spawnChance <= 32000)
						return EntityType.ENDERMAN;
					else if (spawnChance <= 48000)
						return EntityType.CREEPER;
					else if (spawnChance <= 60000)
						return EntityType.SKELETON;
					else if (spawnChance <= 80000)
						return EntityType.PIG_ZOMBIE;
					else if (spawnChance <= 85000)
						return EntityType.BLAZE;
					else if (spawnChance <= 90000)
						return EntityType.SILVERFISH;
					else if (spawnChance <= 91000)
						return EntityType.GHAST;
					else
						return EntityType.WITHER;
				}
				
				return EntityType.ZOMBIE;
			}
			
		},20,80));
	}
	
	public void nukeMob(LivingEntity entity)
	{
		// Unregister the mob.
		FC_Rpg.rpgEntityManager.unregisterRpgMonster(entity);
		
		//Remove the monster
		entity.remove();
	}
}






















