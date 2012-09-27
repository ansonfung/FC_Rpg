package me.Destro168.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.Configs.ConfigOverlord;
import me.Destro168.Configs.PvpManager;
import me.Destro168.Configs.WorldManager;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PvpEvent extends GeneralEvent
{
	private double rewardAmount;
	
	private WorldManager wm;
	private PvpManager pm;
	private Map<Player, Boolean> hasLost;
	private List<Player> redTeam;
	private List<Player>  yellowTeam;
	
	public List<Player> getRedTeam() { return redTeam; }
	public List<Player> getYellowTeam() { return yellowTeam; }
	
	public PvpEvent() 
	{
		setPvpDefaults();
	}
	
	public void setPvpDefaults()
	{
		//Set super defaults.
		super.setDefaults();
		
		//Initialize globals.
		wm = new WorldManager();
		pm = new PvpManager();
		hasLost = new HashMap<Player, Boolean>();
		redTeam = new ArrayList<Player>();
		yellowTeam = new ArrayList<Player>();
		
		ConfigOverlord co = new ConfigOverlord();
		rewardAmount = co.getPvpArenaReward();
	}
	
	public boolean addPvper(Player player)
	{
		//Set that the player has lost.
		hasLost.put(player, false);
		
		//Add the participant.
		return super.addParticipant(player);
	}
	
	public boolean removePvper(Player requester, Player removePlayer, boolean dsplayMessages)
	{
		super.removeParticipant(requester,removePlayer,dsplayMessages);
		
		//If the player is in the event, then...
		if (isEventPlayer(removePlayer) == true)
		{
			//Attempt to remove from team.
			if (redTeam.contains(removePlayer))
				redTeam = kickPlayer(redTeam, removePlayer);
			
			if (yellowTeam.contains(removePlayer))
				yellowTeam = kickPlayer(yellowTeam, removePlayer);
			
			//Set that the player has lost.
			hasLost.put(removePlayer, true);
			
			return true;
		}
		
		return false;
	}
	
	private List<Player> kickPlayer(List<Player> team, Player player)
	{
		//Tell the player they are getting kicked.
		bLib.standardBroadcast(player.getName() + " Has Been Kicked From The Pvp Event");
		
		//Find the player and remove him from the team.
		for (int i = 0; i < team.size(); i++)
		{
			//We want to find the player on the team and remove them.
			if (team.get(i) == player)
			{
				//Remove the player from the team.
				team.remove(i);
			}
		}
		
		//Attempt to remove the player from the event.
		for (int i = 0; i < participant.length; i++)
		{
			if (participant[i] != null)
			{
				if (participant[i] == player)
				{
					participant[i] = null;
					hasLost.put(player, true);
				}
			}
		}
		
		//Return the new team without the player.
		return team;
	}
	
	public void begin()
	{
		//Set pvp defaults.
		setPvpDefaults();
		
		//If the phase isn't equal to 0, then return.
		if (phase != 0)
			return;
		
		//Give players 30 seconds to type
		bLib.standardBroadcast("Want $" + rewardAmount + "? Join The Pvp Event Starting In [60] seconds! Type: '/pvp join' To Enter!");
		
		//After 30 seconds announce 30 seconds to join.
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 1)
					bLib.standardBroadcast("Want $" + rewardAmount + "? Pvp Event Starting In [30] seconds! Type: '/pvp join' To Enter!");
			}
		}, 600);
		
		//After 30 seconds start the  lobby phase.
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 1)
					lobbyPhase();
			}
		}, 1200);
	}
	
	private void lobbyPhase()
	{
		//Variable declarations
		boolean alternate = true;
		
		//Update phase.
		phase = 2;
		
		//Add players to teams.
		for (int i = 0; i < participant.length; i++)
		{
			//Make sure the event player isn't null.
			if (participant[i] != null)
			{
				if (alternate == true)
				{
					redTeam.add(participant[i]);
					alternate = false;
				}
				else
				{
					yellowTeam.add(participant[i]);
					alternate = true;
				}
			}
		}
		
		//If there are no players on either team, then we want to just end the lobby phase.
		if (redTeam.size() == 0 | yellowTeam.size() == 0)
		{
			end(false);
			return;
		}
		
		//Announce autobalance.
		bLib.standardBroadcast("Autobalancing Teams! Somebody may be kicked!");
		
		//Check if teams are uneven and kick player to make player counts even.
		if (redTeam.size() > yellowTeam.size())
			kickPlayer(redTeam, redTeam.get(redTeam.size() - 1));
		
		else if (yellowTeam.size() > redTeam.size())
			kickPlayer(yellowTeam, yellowTeam.get(yellowTeam.size() - 1));
		
		//Teleport the players to the lobby.
		for (Player player : redTeam)
			player.teleport(pm.getLobby1());
		
		for (Player player : yellowTeam)
			player.teleport(pm.getLobby2());
		
		//Announce upcoming pvp phase.
		messageAllParticipants("The Fight Begins In [30] Seconds, Prepare Yourself!");
		
		//Begin the pvp phase after 30 seconds
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 2)
					pvpPhase();
			}
		}, 600);
	}
	
	private void pvpPhase()
	{
		//Variable declarations
		RpgPlayer rpgPlayer;
		
		//Update phase.
		phase = 3;
		
		//Attempt to heal the players.
		for (int i = 0; i < participant.length; i++)
		{
			if (participant[i] != null)
			{
				//Fully heal the participants.
				rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(participant[i]);
				
				if (rpgPlayer != null)
					rpgPlayer.healFull();
				
				participant[i].setFoodLevel(20);
				participant[i].setHealth(20);
			}
		}
		
		//Teleport players to respective spawns.
		for (Player player : redTeam)
		{
			if (player != null)
				player.teleport(pm.getSpawn1());
		}
		
		for (Player player : yellowTeam)
		{
			if (player != null)
				player.teleport(pm.getSpawn2());
		}
		
		//Announce they have 2 mintues to win.
		messageAllParticipants("You Have [120] Seconds To Slaughter Your Foes.");
		
		//Announce every 30 seconds the amount of time left for the pvp event.
		
		//30 second warning
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 3)
					messageAllParticipants("You Have [90] Seconds To Slaughter Your Foes.");
			}
		}, 600);
		
		//60 second warning
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 3)
					messageAllParticipants("You Have [60] Seconds To Slaughter Your Foes.");
			}
		}, 1200);
		
		//90 second warning
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 3)
					messageAllParticipants("You Have [30] Seconds To Slaughter Your Foes.");
			}
		}, 1800);
		
		
		//After two minutes we want to teleport everybody out of the arena.
		Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable() 
		{ 
			@Override
			public void run()
			{
				if (phase == 3)
					end(false);
			}
		}, 2400);
	}
	
	public int returnWinnerSlot()
	{
		int winners = 0;
		int winnerSlot = 0;
		
		for (int i = 0; i < Bukkit.getServer().getMaxPlayers(); i++)
		{
			if (hasLost.containsKey(participant[i]))
			{
				if (hasLost.get(participant[i]) == false)
				{
					winnerSlot = i;
					winners++;
				}
			}
		}
		
		if (winners != 1)
			return -1;
		
		return winnerSlot;
	}
	
	public void setHasLostTrue(Player player) 
	{
		int id = getEventPlayerId(player);
		
		if (id > -1)
			hasLost.put(player, true);
		
		if (returnWinnerSlot() != -1)
			end(false);
	}
	
	public void end(boolean forceEnd)
	{
		//Variable Declarations
		int winnerSlot = returnWinnerSlot();
		boolean onePlayer = false;
		
		//If more than one person joined set onePlayer to true.
		if (redTeam.size() == 0 || yellowTeam.size() == 0)
			onePlayer = true;
		
		if (onePlayer == false)
		{
			//Teleport all players out to spawn.
			teleportAllParticipants(wm.getWorldSpawn(wm.getPvpWorld().getName()));
		}
		
		//When forceEnd is true, we want to skip messages and determining of pvp champion.
		if (forceEnd == false)
		{
			//If there are no players on either team, then we want to just end the lobby phase.
			if (onePlayer == true)
				bLib.standardBroadcast("Not Enough Players To Start A Pvp Event.");
			else if (winnerSlot > -1)
			{
				//Announce the winner!
				bLib.standardBroadcast(participant[winnerSlot].getName() + " Is The New Pvp Champion And Has Won $" + rewardAmount + "!");
				
				//Give the reward
				FC_Rpg.economy.bankDeposit(participant[winnerSlot].getName(), rewardAmount);
			}
		}
		
		//Reset key variables.
		setPvpDefaults();
	}
}

/*

So basically, a pvp event will be a pvp event between anybody that wants to join every 30 minutes.

- An announcement goes out to join the pvp event.

People type /pvp join the pvp event.

After 30 seconds (time), close off joining. Announce 15 seconds (time / 2) left to join. 

- Take everybody that joined and split them up 50/50.
- Teleport players into the lobbies based on their 'team'.
- Announce to teams that the gates will open in 30 seconds.

- After 30 seconds, open up the doors so people can go in and block off the exit. Teleport blocked off people into the fight area.

*/
