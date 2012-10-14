package me.Destro168.events;

import java.util.ConcurrentModificationException;
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
import org.bukkit.inventory.ItemStack;

import me.Destro168.Configs.DungeonConfig;
import me.Destro168.Entities.RandomTreasureGenerator;
import me.Destro168.Entities.RpgMonster;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Util.RpgMessageLib;

public class DungeonEvent extends GeneralEvent
{
	//Variable Declarations
	private int dungeonNumber;	//Never allow this to be changed after set.
	private String dungeonName;
	private int[] tid;
	private World dungeonWorld;
	private LivingEntity[] spawnedMobs;
	private int lowestLevel;
	private DungeonConfig dm;
	
	public String getDungeonName() { return dungeonName; }
	public int getLowestLevel() { updateLowestLevel(); return lowestLevel; }
	
	public DungeonEvent()
	{
		setDungeonDefaults();
	}
	
	public void setDungeonDefaults()
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
		dungeonNumber = -1;
		lowestLevel = 999999;
		dungeonWorld = null;
		dm = new DungeonConfig();
	}
	
	public boolean addDungeoneer(Player player, int dungeonNumber)
	{
		//Variable declarations.
		RpgMessageLib msgLib = new RpgMessageLib(player);
		int level = FC_Rpg.rpgManager.getRpgPlayer(player).getPlayerConfigFile().getClassLevel();
		
		//Update the dungeonNumber
		setDungeonNumber(dungeonNumber);
		
		//Check to make sure they are in the correct level range.
		if (level < dm.getLevelMin(dungeonNumber) || level > dm.getLevelMax(dungeonNumber))
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
		
		//After 30 seconds announce 30 seconds to join.
		tid[0] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{
			@Override
			public void run()
			{
				if (phase == 1)
					bLib.standardBroadcast("[30] Seconds Remaining To Join The Dungeon " + dungeonName + "!");
			}
		}, 600);
		
		//After 30 seconds start the  lobby phase.
		tid[1] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
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
		}, 1200);
	}
	
	public void removeDungeoneer(Player requester, Player playerToRemove, boolean displayMessages)
	{
		super.removeParticipant(requester, playerToRemove, displayMessages);
		
		if (isHappening() == false)
			return;
		
		//Check if one player remains in the dungeon or not.
		boolean hasOne = false;
		
		for (int i = 0; i < participant.length; i++)
		{
			if (participant[i] != null)
			{
				hasOne = true;
				break;
			}
		}
		
		if (hasOne == false)
			end(false);
	}
	
	public void initialize(int dungeonNumber)
	{
		//Variable Declarations
		Location dungeonStart = null;
		Location bossLocation = null;
		int mobSpawnCount = dm.getSpawnCount(dungeonNumber);
		int mobCountMinusOne = mobSpawnCount - 1;
		RpgMonster bossMob;
		
		//Set that the event is has gone through lobby phase and people are fighting in the dungeon now.
		phase = 2;
		
		//Set dungeon number.
		setDungeonNumber(dungeonNumber);
		
		//Update the lowest level.
		updateLowestLevel();
		
		//Remove participants with a level that is too high
		kickTooStrong();
		
		//Load up spawn point and boss spawn.
		dungeonStart = dm.getStart(dungeonNumber);
		bossLocation = dm.getBossSpawn(dungeonNumber);
		
		//Teleport the players in now.
		super.teleportAllParticipants(dungeonStart);
		
		//Mob spawn.
		spawnedMobs = new LivingEntity[mobSpawnCount];
		
		//Spawn normal monsters if the mob spawn count is greater than 1.
		if (mobSpawnCount > 1)
		{
			for (int i = 0; i < mobCountMinusOne; i++)
			{
				Location randomSpawnLocation = getRandomMobSpawnLocation(dm.getStart(dungeonNumber).getWorld());
				
				spawnedMobs[i] = (LivingEntity) dungeonWorld.spawnEntity(randomSpawnLocation, returnRandomLivingEntity());
			}
		}
		
		//Check boss location, somehow makes the boss location work. Don't question the magic!
		if (isSafe(bossLocation) == false)
			FC_Rpg.plugin.getLogger().info("[ERROR] Boss Spawn Location Is NOT Safe!");
		
		//Spawn a boss monster.
		spawnedMobs[mobCountMinusOne] = (LivingEntity) dungeonWorld.spawnEntity(bossLocation, returnRandomLivingEntity());
		
		//Register the custom monster.
		bossMob = FC_Rpg.rpgManager.registerCustomLevelEntity(spawnedMobs[mobCountMinusOne], 5);
		
		//Make it a boss.
		bossMob.setIsBoss(true);
		
		tid[2] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
					messageAllParticipants("7 Minutes 30 Seconds To Beat The Dungeon!");
			}
		}, 3000); //7:30 minutes to beat the dungeon.
		
		tid[3] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
					messageAllParticipants("5 Minutes Remaining To Clear The Dungeon");
			}
		}, 6000); //5 minutes to beat the dungeon.
		
		tid[4] =  Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				if (phase == 2)
					messageAllParticipants("2 Minutes 30 Seconds To Finish The Dungeon!");
			}
		}, 9000); //2:30 minutes to beat the dungeon.
		
		//Start timer for when the dungeon has to be beat by.
		tid[5] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
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
		if (dungeonNumber_ < -1)
			return;
		
		dungeonNumber = FC_Rpg.trueDungeonNumbers.get(dungeonNumber_);
		dungeonName = dm.getName(dungeonNumber);
		dungeonWorld = dm.getLobby(dungeonNumber).getWorld();
	}
	
	private void updateLowestLevel()
	{
		int level = 0;
		
		//Reset the lowest level.
		lowestLevel = 999999;
		
		//Change lowest level based on player levels.
		for (Player player : participant)
		{
			if (player != null)
			{
				level = FC_Rpg.rpgManager.getRpgPlayer(player).getPlayerConfigFile().getClassLevel();
				
				if (level < lowestLevel)
					lowestLevel = level;
			}
		}
	}
	
	private void kickTooStrong()
	{
		//Change lowest level based on player levels.
		for (Player player : participant)
		{
			if (player != null)
			{
				if (FC_Rpg.rpgManager.getRpgPlayer(player).getPlayerConfigFile().getClassLevel() > lowestLevel + 5)
				{
					//Refund players
					FC_Rpg.economy.depositPlayer(player.getName(), dm.getCost(dungeonNumber));
					
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
		int spawnRangeCount = dm.getSpawnCount(dungeonNumber);
		int randomSpawnRange = 0;
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
				if (rand.nextInt(100) < dm.getSpawnChance(dungeonNumber, randomSpawnRange))
					success = true;
			}
		}
		
		//Store the bounds for the spawn region.
		Location spawnRangeMax = dm.getRange2(dungeonNumber, randomSpawnRange);
		Location spawnRangeMin = dm.getRange1(dungeonNumber, randomSpawnRange);
		
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
				FC_Rpg.plugin.getLogger().info("Forced Mob Spawn Break At - X: " + dbl_X + " Y: " + dbl_Y + " Z: " + dbl_Z);
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
	
	private void addChestLoot()
	{
		//Variable Declarations
		Location chestLocation = null;
		RandomTreasureGenerator rtg = new RandomTreasureGenerator();
		Random rand = new Random();
		ItemStack[] drops = null;
		Block chestBlock;
		Chest chest;
		
		//Set chest location
		chestLocation = dm.getTreasureChest(dungeonNumber);
		
		//Add chest loot.
		chestBlock = Bukkit.getServer().getWorld(chestLocation.getWorld().getName()).getBlockAt((int) chestLocation.getX(), (int) chestLocation.getY(), (int) chestLocation.getZ());
		chestBlock.setType(Material.CHEST);
		
		chest = (Chest) chestBlock.getState();
		
		//Clear the chest out from other runs.
		chest.getInventory().clear();
		
		//Add random treasure.
		chest.getInventory().addItem(getRandomTreasure());
		
		//Get the list of random treasure.
		drops = rtg.getRandomItemArray(rand.nextInt(5) + 1, lowestLevel);
		
		//Add items to the inventory.
		for (ItemStack drop : drops)
			chest.getInventory().addItem(drop);
	}
	
	private ItemStack getRandomTreasure()
	{
		Random rand = new Random();
		ItemStack item;
		int itemSize = rand.nextInt(64) + 64;
		
		if (lowestLevel <= 19)
			item = new ItemStack(Material.COAL, itemSize);
		else if (lowestLevel <= 39)
			item = new ItemStack(Material.IRON_ORE, itemSize);
		else if (lowestLevel <= 59)
			item = new ItemStack(Material.DIAMOND, itemSize);
		else if (lowestLevel <= 79)
			item = new ItemStack(Material.GOLD_ORE, itemSize);
		else if (lowestLevel >= 80)
			item = new ItemStack(Material.EMERALD, itemSize);
		else
			item = new ItemStack(Material.COAL, itemSize);
		
		return item;
	}
	
	private EntityType returnRandomLivingEntity()
	{
		Random rand = new Random();
		int number = rand.nextInt(4);
		
		if (number == 0)
			return EntityType.SPIDER;
		else if (number == 1)
			return EntityType.ZOMBIE;
		else if (number == 2)
			return EntityType.PIG_ZOMBIE;
		else
			return EntityType.SKELETON;
	}
	
	public void checkMobDeath(Entity entity)
	{
		//Check if the boss mob was slain.
		if (spawnedMobs != null)
		{
			if (spawnedMobs[spawnedMobs.length - 1] == entity)
				bLib.standardBroadcast("The Dungeon " + dungeonName + " Boss Monster Has Been Slain!");
		}
		
		tid[6] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				try
				{
					//Check if the dungeon is empty.
					if (checkEmpty() == true)
						end(true);
				}
				catch (ConcurrentModificationException e) { }
				catch (NullPointerException e) { }
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
	
	public boolean isInsideDungeon(Location loc)
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
			
			//Attempt to store dungeon bound locations.
			try
			{
				//Store the minimum for faster reference later.
				dMin = dm.getRange1(dungeonNumber, i);
			}
			catch (NullPointerException e) { }
			
			//If not null, evaluate
			if (dMin != null)
			{
				//Store the area it would spawn.
				dMax = dm.getRange2(dungeonNumber, i);
				
				//Check if x is in range.
				if (checkInRange(loc.getX(), dMin.getX(), dMax.getX()) == true)
					return true;
				
				if (checkInRange(loc.getZ(), dMin.getZ(), dMax.getZ()) == true)
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
		return true;
	}
	
	private boolean checkInRange(double direction, double min, double max)
	{
		//Variable declarations
		int test = 0;
		
		//Make sure min/max are ordered properly
		if (min > max)
		{
			max = test;
			max = min;
			min = test;
		}
		
		//Check if in range or out of range.
		if (direction < min || direction > max)
			return false;
		
		return true;
	}
	
	public void end(final boolean normalEnd)
	{
		//Remove the mobs.
		if (spawnedMobs != null)
		{
			for (Entity mob : spawnedMobs)
			{
				if (mob != null)
					mob.remove();
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
			addChestLoot();
			
			//In 30 seconds end the event.
			tid[7] = Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				public void run()
				{
					//Announce dungeon completion
					if (normalEnd == true)
						bLib.standardBroadcast("The Dungeon " + dungeonName + " Has Been Completed!");
					
					//Teleport all players out
					teleportAllParticipants(dm.getExit(dungeonNumber));
					
					//Reset everything.
					setDungeonDefaults();
				}
			}, 1200);
		}
		else
		{
			//Announce dungeon shut down.
			bLib.standardBroadcast("The Dungeon " + dungeonName + " Has Shut Down!");
			
			//Teleport all players out
			teleportAllParticipants(dm.getExit(dungeonNumber));
			
			//Reset everything.
			setDungeonDefaults();
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