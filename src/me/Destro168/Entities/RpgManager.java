package me.Destro168.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.RpgParty;
import me.Destro168.Messaging.BroadcastLib;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RpgManager 
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
		
		public PlayerInformation(Player player)
		{
			rpgPlayer = new RpgPlayer(player);
		}
		
		private void startPlayerUpdates()
		{
			//Cancel past player updates.
			stopPlayerUpdates();
			
			if (rpgPlayer.getIsActive() == false)
				return;
			
			updateTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					rpgPlayer.attemptFeedSteak();
					rpgPlayer.attemptPayHunter();
					rpgPlayer.calcMaxHM();
					rpgPlayer.updateTimePlayed();
				}
			}, 0, 1200);
		}
		
		private void stopPlayerUpdates()
		{
			if (updateTask != -1)
				Bukkit.getScheduler().cancelTask(updateTask);
		}
		
		private void startManaRegen()
		{
			//Cancel any past mana regenerations.
			cancelManaRegen();
			
			if (rpgPlayer.getIsActive() == false)
				return;
			
			//Start a new mana regeneration.
			manaTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(FC_Rpg.plugin, new Runnable()
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

    public RpgManager()
    {
    	piMap = new HashMap<Player, PlayerInformation>();
    	miMap = new HashMap<LivingEntity, RpgMonster>();
    	
    	//Check all online players registrations.
    	for (Player player : Bukkit.getServer().getOnlinePlayers())
    		checkPlayerRegistration(player);
    }
    
    public List<RpgPlayer> getOnlineRpgPlayers()
    {
    	List<RpgPlayer> onlinePlayers = new ArrayList<RpgPlayer>();
    	
    	for (Player player : Bukkit.getServer().getOnlinePlayers())
    		onlinePlayers.add(getRpgPlayer(player));
    	
    	return onlinePlayers;
    }
    
	public List<RpgPlayer> getNearbyPartiedRpgPlayers(Player sourcePlayer, int distance)
	{
		RpgParty party = FC_Rpg.partyManager.getParty(sourcePlayer.getName());
		
		//If no party return the source player in list form.
		if (party == null)
		{
			List<RpgPlayer> rpgPartyList = new ArrayList<RpgPlayer>();
			rpgPartyList.add(getRpgPlayer(sourcePlayer));
			return rpgPartyList;
		}
		
		EntityLocationLib ell = new EntityLocationLib();
		List<RpgPlayer> rpgPlayerList = new ArrayList<RpgPlayer>();
		
		for (Player player : party.getPartyPlayerList())
		{
			if (ell.isNearby(sourcePlayer, player, distance))
			{
				rpgPlayerList.add(getRpgPlayer(player));
			}
		}
		
		return rpgPlayerList;
	}
	
    //Functions
    private void checkPlayerRegistration(Player player)
    {
    	//If the player information isn't stored, then store.
    	if (!(piMap.containsKey(player)))
    	{
    		//We get the rpgPlayer.
    		PlayerInformation pi = new PlayerInformation(player);
    		piMap.put(player, pi);
    		piMap.get(player).startTasks();
    	}
    }
    
    public RpgPlayer getRpgPlayer(Player player)
    {
    	//Force register the player.
    	checkPlayerRegistration(player);
    	
    	//Return the rpg player.
    	return piMap.get(player).getRpgPlayer();
    }
    
    public void checkEntityRegistration(LivingEntity entity)
    {
    	//If the player information isn't stored, then store.
    	if (!(miMap.containsKey(entity)))
    	{
    		RpgMonster mob = new RpgMonster(entity, 0);
    		miMap.put(entity, mob);
    	}
    }
    
    public RpgMonster registerCustomLevelEntity(LivingEntity entity, int levelBonus)
    {
    	//If the player information isn't stored, then store.
    	if (!(miMap.containsKey(entity)))
    	{
    		RpgMonster mob = new RpgMonster(entity, levelBonus);
    		
    		miMap.put(entity, mob);
    		
    		return miMap.get(entity);
    	}
    	
    	return null;
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
    		piMap.get(player).getRpgPlayer().dumpHM();
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
    	FC_Rpg.bLib.standardBroadcast("Welcome " + player.getName() + ", the " + classSelection + "!");
		
    	//Convert stringClass to real class number.
    	for (int i = 0; i < FC_Rpg.c_classes.length; i++)
    	{
    		if (classSelection.equals(FC_Rpg.c_classes[i]))
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
    	checkPlayerRegistration(player);
    	
    	//Handle new player tasks.
    	piMap.get(player).getRpgPlayer().createPlayerRecord(player, intClass, manualSelection); //Create the player record
	}
    
    public void clearOldPlayerData()
	{
    	//Variable Declarations
    	Date now = new Date();
    	Long timeDifference;
    	BroadcastLib bLIb = new BroadcastLib();
    	
		//Attempt to delete player data if older than 14 days.
		for (OfflinePlayer player : Bukkit.getOfflinePlayers())
		{
			RpgPlayerFile file = new RpgPlayerFile(player.getName());
			
			if (player.isOnline() == true)
				return;
			
			//Never delete donator files.
			if (file.isDonator() == true)
				return;
			
			//Only delete files when the player record is considered "active".
			if (file.getIsActive() == true)
			{
				timeDifference = now.getTime() - player.getLastPlayed();
				
				if (timeDifference >= 1209600000)
				{
					bLIb.broadcastToAdmins(player.getName() + " has had his/her configuration file deleted due to inactivity.");
					file.clearAllData();
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





