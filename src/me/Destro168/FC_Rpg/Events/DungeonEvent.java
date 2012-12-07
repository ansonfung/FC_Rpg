package me.Destro168.FC_Rpg.Events;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.FC_Rpg.CreatureSpawnListener;
import me.Destro168.FC_Rpg.Util.RpgMessageLib;
import me.Destro168.FC_Suite_Shared.SuiteConfig;

public class DungeonEvent extends GeneralEvent
{
	//Variable Declarations
	private int dungeonNumber;
	private String dungeonName;
	private int[] tid;
	private World dungeonWorld;
	private LivingEntity[] spawnedMobs;
	private int lowestLevel;
	private boolean isRpgDungeon;
	int randomSpawnRange;
	
	public String getDungeonName() { return dungeonName; }
	public int getLowestLevel() { updateLowestLevel(); return lowestLevel; }
	
	public DungeonEvent(int dungeonNumber_)
	{
		setDungeonDefaults(dungeonNumber_);
	}
	
	public void setDungeonDefaults(int dungeonNumber_)
	{
		super.setDefaults();
		
		//Cancel any past tasks.
		if (tid != null)
		{
			for (int i = 0; i < 8; i++)
				Bukkit.getScheduler().cancelTask(tid[i]);
		}
		
		//Reset variables.
		tid = new int[8];
		lowestLevel = 999999;
		
		//Set the dungeon number.
		setDungeonNumber(dungeonNumber_);
	}
	
	public boolean addDungeoneer(Player player, int dungeonNumber)
	{
		//Variable declarations.
		RpgMessageLib msgLib = new RpgMessageLib(player);
		int level = FC_Rpg.rpgEntityManager.getRpgPlayer(player).getPlayerConfig().getClassLevel();
		
		//Update the dungeonNumber
		setDungeonNumber(dungeonNumber);
		
		//Check to make sure they are in the correct level range.
		if (level < FC_Rpg.dungeonConfig.getPlayerLevelRequirementMinimum(dungeonNumber) || level > FC_Rpg.dungeonConfig.getPlayerLevelRequirementMaximum(dungeonNumber))
		{
			msgLib.errorLevelOutOfRange();
			return false;
		}
		
		//Add the participant.
		if (super.addParticipant(player) == true)
		{
			super.messageAllParticipants(player.getName() + " Has Joined The Dungeon.");	//Send a join message.
			
			//If the dungeon wasn't started, start it.
			if (isHappening() == false)
				begin(dungeonNumber);
			
			return true;
		}
		
		return false;
	}
	
	private void begin(final int dungeonNumber_)
	{
		//Set that the event is happening.
		phase = 1;
		
		//Give players 60 seconds to join.
		bLib.standardBroadcast("The Dungeon " + dungeonName + " Has Been Activated. [60] Seconds Remain To Join In On The Action!");
		
		int wait1 = 600;
		int wait2 = 1200;
		
		SuiteConfig sc = new SuiteConfig();
		
		if (sc.getDebug())
		{
			wait1 = 1;
			wait2 = 2;
		}
	
		//After 30 seconds announce 30 seconds to join.
		tid[0] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{
			@Override
			public void run()
			{
				if (phase == 1)
					bLib.standardBroadcast("[30] Seconds Remaining To Join The Dungeon " + dungeonName + "!");
			}
		}, wait1);
		
		//After 30 seconds start the  lobby phase.
		tid[1] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 1)
				{
					bLib.standardBroadcast("The Dungeon " + dungeonName + " Has Started! It's Now Closed To Entry! Stick Around To Join Next Time!");
					initialize(dungeonNumber_);
				}
			}
		}, wait2);
	}
	
	public void removeDungeoneer(Player requester, Player playerToRemove, boolean displayMessages)
	{
		super.removeParticipant(requester, playerToRemove, displayMessages);
		
		if (isHappening() == false)
			return;
		
		if (participantList.size() == 0)
			end(false);
	}
	
	public void initialize(int dungeonNumber)
	{
		//Variable Declarations
		Location dungeonStart = null;
		Location bossLocation = null;
		int mobSpawnCount = FC_Rpg.dungeonConfig.getMobsToSpawnCount(dungeonNumber);
		int mobCountMinusOne = mobSpawnCount - 1;
		
		//Set that the event is has gone through lobby phase and people are fighting in the dungeon now.
		phase = 2;
		
		//Set dungeon number.
		setDungeonNumber(dungeonNumber);
		
		//Update the lowest level.
		updateLowestLevel();
		
		//Remove participants with a level that is too high
		kickTooStrong();
		
		//Load up spawn point and boss spawn.
		dungeonStart = FC_Rpg.dungeonConfig.getStart(dungeonNumber);
		bossLocation = FC_Rpg.dungeonConfig.getBossSpawn(dungeonNumber);
		
		//Teleport the players in now.
		super.teleportAllParticipants(dungeonStart);
		
		//Mob spawn.
		spawnedMobs = new LivingEntity[mobSpawnCount];
		
		//Spawn normal monsters if the mob spawn count is greater than 1.
		if (mobSpawnCount > 1)
		{
			for (int i = 0; i < mobCountMinusOne; i++)
			{
				Location randomSpawnLocation = getRandomMobSpawnLocation(dungeonStart.getWorld());
				spawnedMobs[i] = (LivingEntity) dungeonWorld.spawnEntity(randomSpawnLocation, returnRandomLivingEntity());
				FC_Rpg.rpgEntityManager.getRpgMonster(spawnedMobs[i]).checkEquipment();
			}
		}
		
		//Check boss location, somehow makes the boss location work. Don't question the magic!
		if (isSafe(bossLocation) == false)
			FC_Rpg.plugin.getLogger().info("[ERROR] Boss Spawn Location Is NOT Safe!");
		
		//Spawn a boss monster.
		spawnedMobs[mobCountMinusOne] = (LivingEntity) dungeonWorld.spawnEntity(bossLocation, returnRandomLivingEntity());
		
		//Add loot to chests.
		addChestLoot(true);
		
		//Begin tasks to notify end of dungeon.
		if (FC_Rpg.worldConfig.getIsRpgWorld(dungeonStart.getWorld().getName()))
		{
			//Register the custom monster.
			FC_Rpg.rpgEntityManager.registerCustomLevelEntity(spawnedMobs[mobCountMinusOne], 5, true);
			
			CreatureSpawnListener l = FC_Rpg.plugin.new CreatureSpawnListener();
			CreatureSpawnEvent event = new CreatureSpawnEvent(spawnedMobs[mobCountMinusOne], SpawnReason.CUSTOM);
			l.onCreaturespawn(event);
			
			FC_Rpg.rpgEntityManager.getRpgMonster(spawnedMobs[mobCountMinusOne]).checkEquipment();
		}
		
		tid[2] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
					messageAllParticipants("7 Minutes 30 Seconds To Beat The Dungeon!");
			}
		}, 3000); //7:30 minutes to beat the dungeon.
		
		tid[3] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
					messageAllParticipants("5 Minutes Remaining To Clear The Dungeon");
			}
		}, 6000); //5 minutes to beat the dungeon.
		
		tid[4] =  Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
					messageAllParticipants("2 Minutes 30 Seconds To Finish The Dungeon!");
			}
		}, 9000); //2:30 minutes to beat the dungeon.
		
		//Start timer for when the dungeon has to be beat by.
		tid[5] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
				{
					bLib.standardBroadcast("The Adventurers Were Too Slow! Dungeon " + dungeonName + " Has Reset And Is Ready For More Action!");
					end(false);
				}
			}
		}, 12000); //10 minutes to beat the dungeon.
	}
	
	private void setDungeonNumber(int dungeonNumber_)
	{
		if (dungeonNumber_ < 0)
			return;
		
		dungeonNumber = FC_Rpg.trueDungeonNumbers.get(dungeonNumber_);
		dungeonName = FC_Rpg.dungeonConfig.getName(dungeonNumber);
		dungeonWorld = FC_Rpg.dungeonConfig.getLobby(dungeonNumber).getWorld();
		isRpgDungeon = FC_Rpg.worldConfig.getIsRpgWorld(dungeonWorld.getName());
	}
	
	private void updateLowestLevel()
	{
		int level = 0;
		
		//If not an rpg dungeon we set lowest level to -1.
		if (!isRpgDungeon)
		{
			lowestLevel = -1;
			return;
		}
		
		//Reset the lowest level.
		lowestLevel = 999999;
		
		//Change lowest level based on player levels.
		for (Player player : participantList)
		{
			if (player != null)
			{
				level = FC_Rpg.rpgEntityManager.getRpgPlayer(player).getPlayerConfig().getClassLevel();
				
				if (level < lowestLevel)
					lowestLevel = level;
			}
		}
	}
	
	private void kickTooStrong()
	{
		//Don't perform kick if the dungeon isn't rpg.
		if (!isRpgDungeon)
			return;
		
		//Change lowest level based on player levels.
		for (Player player : participantList)
		{
			if (player != null)
			{
				if (FC_Rpg.rpgEntityManager.getRpgPlayer(player).getPlayerConfig().getClassLevel() > lowestLevel + 5)
				{
					//Refund players
					FC_Rpg.economy.depositPlayer(player.getName(), FC_Rpg.dungeonConfig.getEntryFee(dungeonNumber));
					
					//Remove the player
					removeDungeoneer(player,player,true);
				}
			}
		}
	}
	
	private Location getRandomMobSpawnLocation(World dungeonWorld)
	{
		//Variable Declarations
		Random rand = new Random();
		int spawnRangeCount = FC_Rpg.dungeonConfig.getMobsToSpawnCount(dungeonNumber);
		randomSpawnRange  = 0;
		boolean success = false;
		int b = 0;
		
		//If the spawn range count is less than 0, we can't pick a spawn.
		if (spawnRangeCount < 0)
		{
			FC_Rpg.plugin.getLogger().log(Level.SEVERE, "You must define spawn ranges before you can spawn mobs!");
			return null;
		}
		
		//If there is only one spawn point, choose that.
		else if (spawnRangeCount == 0)
			randomSpawnRange = 0;
		
		//Else we want to pick a random spawn choice from the multiple existing spawn choices.
		else
		{
			//If we did not pick a spawn choice yet, then...
			while (success == false)
			{
				//Pick a random spawn.
				randomSpawnRange = rand.nextInt(spawnRangeCount);
				
				//Check if the spawnRate check passes for that area.
				if (rand.nextInt(100) < FC_Rpg.dungeonConfig.getSpawnChance(dungeonNumber, randomSpawnRange))
					success = true;
			}
		}
		
		//Store the bounds for the spawn region.
		Location spawnRangeMin = FC_Rpg.dungeonConfig.getSpawnBox1(dungeonNumber, randomSpawnRange);
		Location spawnRangeMax = FC_Rpg.dungeonConfig.getSpawnBox2(dungeonNumber, randomSpawnRange);
		
		//Check if spawnrange min is null.
		if (spawnRangeMin == null)
		{
			FC_Rpg.plugin.getLogger().log(Level.SEVERE, "You need to add a secondary or primary point to define the dungeon spawn region. Stuff will be all ker-broke until you do.");
			return null;
		}
		
		//Variable Declarations pt. 2
		Location spawnPoint = null;
		double dbl_X = 0;
		double dbl_Y = 0;
		double dbl_Z = 0;
		int spawnRangeMaxX = (int) spawnRangeMax.getX();
		int spawnRangeMinX = (int) spawnRangeMin.getX();
		int spawnRangeMaxY = (int) spawnRangeMax.getY();
		int spawnRangeMinY = (int) spawnRangeMin.getY();
		int spawnRangeMaxZ = (int) spawnRangeMax.getZ();
		int spawnRangeMinZ = (int) spawnRangeMin.getZ();
		int temp;
		
		if (spawnRangeMaxX < spawnRangeMinX)
		{
			temp = spawnRangeMaxX;
			spawnRangeMaxX = spawnRangeMinX;
			spawnRangeMinX = temp;
		}
		
		if (spawnRangeMaxY < spawnRangeMinY)
		{
			temp = spawnRangeMaxY;
			spawnRangeMaxY = spawnRangeMinY;
			spawnRangeMinY = temp;
		}
		
		if (spawnRangeMaxZ < spawnRangeMinZ)
		{
			temp = spawnRangeMaxZ;
			spawnRangeMaxZ = spawnRangeMinZ;
			spawnRangeMinZ = temp;
		}
		
		//Get a safe spawn point for the dungeon.
		while (isSafe(spawnPoint) == false)
		{
			dbl_X = modifyDifference(spawnRangeMaxX - spawnRangeMinX, spawnRangeMinX);
			dbl_Y = modifyDifference(spawnRangeMaxY - spawnRangeMinY, spawnRangeMinY);
			dbl_Z = modifyDifference(spawnRangeMaxZ - spawnRangeMinZ, spawnRangeMinZ);
			
			spawnPoint = new Location(dungeonWorld, dbl_X, dbl_Y, dbl_Z);
			
			b++;
			
			if (b > 300)
			{
				FC_Rpg.plugin.getLogger().info("Forced Mob Spawn Break At - X: " + dbl_X + " Y: " + dbl_Y + " Z: " + dbl_Z + ". Check that there are safe spots for mobs to spawn in your dungeon.");
				break;
			}
		}
		
		return spawnPoint;
	}
	
	private int modifyDifference(int difference, int spawnRangeMin)
	{
		Random rand = new Random();
		int dbl_rand = 0;
		
		if (difference != 0)
		{
			if (difference < 0)
			{
				//Make positive
				difference = difference * -1;
				
				//Make sure to revert to being negative again.
				dbl_rand = rand.nextInt(difference) * -1 + spawnRangeMin;
			}
			else
			{
				dbl_rand = rand.nextInt(difference) + spawnRangeMin;
			}
		}
		else
			dbl_rand = 0 + spawnRangeMin;
		
		return dbl_rand;
	}
	
	private boolean isSafe(Location loc)
	{
		//Prelim check.
		if (loc == null)
			return false;
		
		//Variable Declarations
		Material blockType = dungeonWorld.getBlockAt(loc).getType();
		
		//If the blocks aren't air, return false.
		if (blockType != Material.AIR && blockType != Material.GRASS && blockType != Material.RED_ROSE && blockType != Material.YELLOW_FLOWER)
			return false;
		
		//Else return true.
		return true;
	}
	
	private void addChestLoot(boolean isStart)
	{
		List<Location> loot;
		
		if (isStart)
			loot = FC_Rpg.dungeonConfig.getTreasureStart(dungeonNumber);
		else
			loot = FC_Rpg.dungeonConfig.getTreasureEnd(dungeonNumber);
		
		for (Location l : loot)
			addLoot(l);
	}
	
	private void addLoot(Location chestLocation)
	{
		//Variable Declarations
		List<ItemStack> drops = null;
		Random rand = new Random();
		Block chestBlock;
		Chest chest;
		
		// Create the chest.
		chestBlock = Bukkit.getServer().getWorld(chestLocation.getWorld().getName()).getBlockAt((int) chestLocation.getX(), (int) chestLocation.getY(), (int) chestLocation.getZ());
		chestBlock.setType(Material.CHEST);
		chest = (Chest) chestBlock.getState();
		
		// Clear the chest inventory.
		chest.getInventory().clear();
		
		// Get the list of random treasure.
		drops = FC_Rpg.treasureConfig.getRandomTreasure(lowestLevel, rand.nextInt(5) + 1);
		
		// Add items to the chest inventory.
		for (ItemStack drop : drops)
			chest.getInventory().addItem(drop);
	}
	
	private EntityType returnRandomLivingEntity()
	{
		Random rand = new Random();
		List<EntityType> a = FC_Rpg.dungeonConfig.getMobList(dungeonNumber, randomSpawnRange);
		return a.get(rand.nextInt(a.size()));
	}
	
	public void checkMobDeath(Entity entity)
	{
		//Perform boss checks only in rpg worlds.
		if (FC_Rpg.worldConfig.getIsRpgWorld(entity.getWorld().getName()))
		{
			//Check if the boss mob was slain.
			if (spawnedMobs != null)
			{
				if (spawnedMobs[spawnedMobs.length - 1] == entity)
					bLib.standardBroadcast("The Dungeon " + dungeonName + " Boss Monster Has Been Slain!");
			}
		}
		
		tid[6] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				try	{
					//Check if the dungeon is empty.
					if (checkEmpty() == true)
						end(true);
				} catch (NullPointerException e) { } //Allows you to pass in nulls.
			}
		}, 20);
	}
	
	public boolean checkEmpty()
	{
		//Variable Declarations
		List<LivingEntity> entities = dungeonWorld.getLivingEntities();
		
		//Return if not the proper phase.
		if (phase != 2)
			return true;

		//Return if no entities.
		if (entities == null)
			return true;

		
		//Check all wordly entities and compare coords.
		for (LivingEntity entity : entities)
		{
			if (!(entity instanceof Player))
			{
				if (isInsideDungeon(entity.getLocation()))
					return false;
			}
		}
		
		return true;
	}
	
	public boolean isInsideDungeon(Location entityLocation)
	{
		//Variable Declarations
		Location dMin;
		Location dMax;
		int breakPoint = 0;
		
		//Check to see if the mob would spawn in any spawn region.
		for (int i = 0; i < 10000; i++)
		{
			//Reset min
			dMin = null;
			
			//Store the minimum for faster reference later.
			try { dMin = FC_Rpg.dungeonConfig.getSpawnBox1(dungeonNumber, i); } catch (NullPointerException e) { }
			
			//If not null, evaluate
			if (dMin != null)
			{
				//Store the area it would spawn.
				dMax = FC_Rpg.dungeonConfig.getSpawnBox2(dungeonNumber, i);
				
				//Check if x is in range.
				if (isInsideRange(entityLocation.getX(), dMin.getX(), dMax.getX()) == true && isInsideRange(entityLocation.getZ(), dMin.getZ(), dMax.getZ()) == true)
					return true;
			}
			
			//Break after 50 null checks.
			else
			{
				breakPoint++;
				
				if (breakPoint > 100)
					break;
			}
		}
		
		//Return false by default.
		return false;
	}
	
	private boolean isInsideRange(double checkPos, double r1, double r2)
	{
		//Variable declarations
		double test = 0;
		
		//Make sure min/max are ordered properly
		if (r1 > r2)
		{
			test = r2;
			r2 = r1;
			r1 = test;
		}
		
		checkPos = Math.floor(checkPos);
		
		//If outside range, return true.
		if (checkPos >= r1 && checkPos <= r2)
			return true;
		
		return false;
	}
	
	public void end(final boolean normalEnd)
	{
		//Remove the mobs.
		if (spawnedMobs != null)
		{
			for (Entity mob : spawnedMobs)
			{
				if (mob != null)
				{
					mob.remove();
				}
			}
		}
		
		//Handle normal and forced ends.
		if (normalEnd == true)
		{
			//Return if not the proper phase.
			if (phase != 2)
				return;
			
			//Update phase.
			phase = 3;
			
			//Message the participants they will be tp'd out.
			super.messageAllParticipants("You will be teleported out in [60] seconds. Remember to loot the chest at the end of the dungeon! It's now full of loot!");
			
			//Add loot to the chest.
			addChestLoot(false);
			
			//In 30 seconds end the event.
			tid[7] = Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				public void run()
				{
					//Announce dungeon completion
					if (normalEnd == true)
						bLib.standardBroadcast("The Dungeon " + dungeonName + " Has Been Completed!");
					
					//Teleport all players out
					teleportAllParticipants(FC_Rpg.dungeonConfig.getExit(dungeonNumber));
					
					//Reset everything.
					setDungeonDefaults(-1);
				}
			}, 1200);
		}
		else
		{
			//Announce dungeon shut down.
			bLib.standardBroadcast("The Dungeon " + dungeonName + " Has Shut Down!");
			
			//Teleport all players out
			teleportAllParticipants(FC_Rpg.dungeonConfig.getExit(dungeonNumber));
			
			//Reset everything.
			setDungeonDefaults(dungeonNumber);
		}
	}
}

/*

- Person hits the sign to enter the lobby - Announcement is made. - People are given 30 seconds to enter.
- After that 30 seconds, the dungeon initializes and players are teleported into the dungeon.
	Initiliazation = Chest at end is generated.
	All monsters are pre-spawned into the dungeon.
	Any object traps are initialized
- At 15 seconds left the players inside the lobby will be reminded they can make parties to share exp/loot.
- Once every monster is killed or 10 minutes passes, every monster is killed, the dungeon resets, all players are teleported out of the dungeon.

- Player deaths in the dungeon will teleport them outside the dungeon.
*/