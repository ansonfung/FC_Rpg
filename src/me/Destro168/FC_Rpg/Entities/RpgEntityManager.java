package me.Destro168.FC_Rpg.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.PlayerConfig;
import me.Destro168.FC_Rpg.FC_Rpg.CreatureSpawnListener;
import me.Destro168.FC_Suite_Shared.Messaging.BroadcastLib;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class RpgEntityManager 
{
	private Map<Player, PlayerInformation> piMap;
	private Map<LivingEntity, RpgMonster> miMap;
	
	public class PlayerInformation
	{
		private RpgPlayer rpgPlayer;
		private int manaTask;
		private int updateTask;
		
		public RpgPlayer getRpgPlayer() { return rpgPlayer; }
		public int getManaTask() { return manaTask; }
		public int getUpdateTask() { return updateTask; }
		
		public void setRpgPlayer(RpgPlayer x) { rpgPlayer = x; }
		public void setManaTask(int x) { manaTask = x; }
		public void setUpdateTask(int x) { updateTask = x; }
		
		public PlayerInformation(RpgPlayer rpgPlayer_)
		{
			rpgPlayer = rpgPlayer_;
		}
		
		private void startPlayerUpdates()
		{
			//Cancel past player updates.
			stopPlayerUpdates();
			
			if (rpgPlayer.getPlayerConfig().getIsActive() == false)
				return;
			
			updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					rpgPlayer.attemptGiveTimedItems();
					rpgPlayer.calculateHealthAndMana();
					rpgPlayer.updateTimePlayed();
				}
			}, 0, 1200);
		}
		
		private void startManaRegen()
		{
			//Cancel any past mana regenerations.
			cancelManaRegen();
			
			if (rpgPlayer.getPlayerConfig().getIsActive() == false)
				return;
			
			//Start a new mana regeneration.
			manaTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FC_Rpg.plugin, new Runnable()
			{
				public void run()
				{
					try
					{
						rpgPlayer.restoreManaTick();
					}
					catch (NullPointerException e) { }
				}
			}, 0, 100);
		}
		
		private void stopPlayerUpdates()
		{
			if (updateTask != -1)
				Bukkit.getScheduler().cancelTask(updateTask);
		}
		
		private void cancelManaRegen()
		{
			//Cancel all past mana regenerations.
			if (manaTask != -1)
				Bukkit.getScheduler().cancelTask(manaTask);
		}
		
		public void startTasks()
		{
			startPlayerUpdates();
			startManaRegen();
		}
		
		public void stopTasks()
		{
			stopPlayerUpdates();
			cancelManaRegen();
		}
	}

    public RpgEntityManager()
    {
    	piMap = new HashMap<Player, PlayerInformation>();
    	miMap = new HashMap<LivingEntity, RpgMonster>();
    	
    	//Check all online players registrations.
    	for (Player player : Bukkit.getServer().getOnlinePlayers())
    	{
    		if (FC_Rpg.worldConfig.getIsRpg(player.getWorld().getName()))
    			checkPlayerRegistration(player, true);
    	}
    }
    
    public List<RpgPlayer> getOnlineRpgPlayers()
    {
    	List<RpgPlayer> onlinePlayers = new ArrayList<RpgPlayer>();
    	RpgPlayer rpgPlayer;
    	
    	for (Player player : Bukkit.getServer().getOnlinePlayers())
    	{
    		rpgPlayer = getRpgPlayer(player);
    		
    		if (rpgPlayer != null)
    			onlinePlayers.add(rpgPlayer);
    	}
    	
    	return onlinePlayers;
    }
	
	public List<RpgPlayer> getNearbyPartiedRpgPlayers(Player player, int distance)
	{
		// Variable Declarations
		List<RpgPlayer> playerList = new ArrayList<RpgPlayer>();
		EntityLocationLib ell = new EntityLocationLib();
		String guild = FC_Rpg.guildManager.getGuildByMember(player.getName());
		
		// If no party return the source player in list form.
		if (guild == null)
		{
			playerList.add(getRpgPlayer(player));
			return playerList;
		}
		
		//If the guild isn't null, then we get online guild members and add them.
		for (Player guildMember : FC_Rpg.guildManager.getOnlineGuildPlayerList(guild))
		{
			if (ell.isNearby(player, guildMember, distance))
			{
				playerList.add(getRpgPlayer(player));
			}
		}
		
		return playerList;
	}
	
	public List<Player> getNearbyPartiedPlayers(Player player, int distance)
	{
		// Variable Declarations
		List<Player> playerList = new ArrayList<Player>();
		EntityLocationLib ell = new EntityLocationLib();
		String guild = FC_Rpg.guildManager.getGuildByMember(player.getName());
		
		// If no party return the source player in list form.
		if (guild == null)
		{
			playerList.add(player);
			return playerList;
		}
		
		//If the guild isn't null, then we get online guild members and add them.
		for (Player guildMember : FC_Rpg.guildManager.getOnlineGuildPlayerList(guild))
		{
			if (ell.isNearby(player, guildMember, distance))
			{
				playerList.add(player);
			}
		}
		
		return playerList;
	}
	
    //Functions
    public void checkPlayerRegistration(Player player, boolean forceStart)
    {
    	//If the player information isn't stored, then store.
    	if (!(piMap.containsKey(player)))
    	{
    		//Variable Decalrations
        	RpgPlayer rpgPlayer = new RpgPlayer(player);
        	
    		//We get the rpgPlayer.
    		PlayerInformation pi = new PlayerInformation(rpgPlayer);
    		
    		piMap.put(player, pi);
    		
    		if (forceStart == true) //Ignore for new
    		{
    			//If they aren't active, then we want to make them active by creating their rpg player.
        		if (rpgPlayer.getPlayerConfig().getIsActive() == false)
        			setPlayerStart("Swordsman", player, false);	//Store the player as a new player.
        		
        		piMap.get(player).startTasks();
    		}
    	}
    }
    
    public RpgPlayer getRpgPlayer(Player player)
    {
    	//Create a pcf to check if a player config file is created or not.
    	PlayerConfig pcf = new PlayerConfig(player.getName());
    	
		//If the player config file isn't active return.
		if (pcf.getIsActive() == false)
			return null;
		
    	//Force register the player.
    	checkPlayerRegistration(player, true);
    	
    	//Return the rpg player.
    	return piMap.get(player).getRpgPlayer();
    }
    
    public void checkEntityRegistration(LivingEntity entity)
    {
    	//If the player information isn't stored, then store.
    	if (!(miMap.containsKey(entity)))
    	{
    		RpgMonster mob = new RpgMonster(entity, 0, false);
    		miMap.put(entity, mob);
    	}
    }
    
    public RpgMonster registerCustomLevelEntity(LivingEntity entity, int levelBonus, boolean isBoss)
    {
    	//If the entity is already in miMap, then we remote it.
    	if (miMap.containsKey(entity))
    		miMap.remove(entity);
    	
    	//Register the new entity and return it.
    	RpgMonster mob = new RpgMonster(entity, levelBonus, isBoss);
    	
		miMap.put(entity, mob);
		
		//Perform false event to register the monster
		CreatureSpawnListener l = FC_Rpg.plugin.new CreatureSpawnListener();
		CreatureSpawnEvent event = new CreatureSpawnEvent(entity, SpawnReason.CUSTOM);
		l.onCreaturespawn(event);
		
		//Check the monsters equipment.
		FC_Rpg.rpgEntityManager.getRpgMonster(entity).checkEquipment();
		
		return miMap.get(entity);
    }
    
    public boolean containsMonster(LivingEntity entity)
    {
    	return miMap.containsKey(entity);
    }
    
    public RpgMonster getRpgMonster(LivingEntity entity)
    {	
    	//Force register the entity.
    	checkEntityRegistration(entity);
    	
    	//Return the rpg entity
    	return miMap.get(entity);
    }
    
    public void unregisterRpgPlayer(Player player)
    {
    	//If the player is in the hash map, remove the player.
    	if (piMap.containsKey(player))
    	{
    		piMap.get(player).stopTasks();
    		piMap.get(player).getRpgPlayer().updateTimePlayed();
    		piMap.get(player).getRpgPlayer().dumpCriticalInformation();
    		piMap.remove(player);
    	}
    }
    
    public void unregisterRpgMonster(LivingEntity entity)
    {
    	//If the player is in the hash map, remove the player.
    	if (miMap.containsKey(entity))
    		miMap.remove(entity);
    }
    
    public void setPlayerStart(String classSelection, Player player, boolean manualSelection)
	{
    	//Variable Declarations
    	int intClass = -1;
    	
		//Tell the server that the class/job have been picked.
    	FC_Rpg.rpgBroadcast.rpgBroadcast(player.getName() + " has picked " + classSelection + "!");
		
    	//Convert stringClass to real class number.
    	for (int i = 0; i < FC_Rpg.classConfig.getRpgClasses().length; i++)
    	{
    		if (classSelection.equals(FC_Rpg.classConfig.getRpgClass(i).getName()))
    		{
    			intClass = i;
    			break;
    		}
    	}
    	
    	//If we have an invalid class chosen, then return.
    	if (intClass < 0)
    	{
    		FC_Rpg.plugin.getLogger().info("Critical error creating RPGPlayer class.");
    		return;
    	}
    	
    	//Check player registration
    	checkPlayerRegistration(player, false);
    	
    	//Handle new player tasks.
    	piMap.get(player).getRpgPlayer().createPlayerRecord(player, intClass, manualSelection); //Create the player record
    	
    	piMap.get(player).startTasks();
	}
    
    public void clearOldPlayerData()
	{
    	//Variable Declarations
    	Date now = new Date();
    	Long timeDifference;
    	BroadcastLib bLib = new BroadcastLib();
    	
		//Attempt to delete player data if older than 14 days.
		for (OfflinePlayer player : Bukkit.getOfflinePlayers())
		{
			PlayerConfig file = new PlayerConfig(player.getName());
			
			if (player.isOnline() == true)
				return;
			
			//Never delete donator files.
			if (file.isDonator() == true)
				return;
			
			//Only delete files when the player record is considered "active".
			if (file.getIsActive() == true)
			{
				timeDifference = now.getTime() - player.getLastPlayed();
				long deleteTime = FC_Rpg.generalConfig.getInactivePlayerFileDeleteTime();
				
				if ((deleteTime > 0) && (timeDifference >= deleteTime))
				{
					file.clearAllData();
					bLib.broadcastToAdmins(player.getName() + " has had his/her configuration file deleted due to inactivity.");
				}
			}
		}
	}
}

//Bukkit.getServer().broadcastMessage("You never found mob: " + String.valueOf(x));
//Bukkit.getServer().broadcastMessage("You never found player: " + name);

/*
public void registerEntityById(int id)
{
	for (int i = 0; i < MAX_ENTITIES; i++)
	{
		if (monsterId[i] == -1)
		{
			monsterId[i] = id;
			rpgMonster[i] = new RpgMonster();
			i = MAX_ENTITIES;
		}
	}
}

public void entityRemove(Entity entity)
{
	for (int i = 0; i < MAX_ENTITIES; i++)
	{
		if (monsterId[i] == entity.getEntityId())
		{
			monsterId[i] = -1;
			rpgMonster[i].clear();
		}
	}
}
*/


/* - Code Alogorithm for storing mobs/players
Problem: Store player ids and overlap and remove old records after all 99,999 places are used.
Solution: 
- We keep track of the currentArray we are using with currentArray.
- We go into our current array and we use that array. Whenever it is full, it won't return a value
	so we clear out the other array and set our current array to the other array. */





