package me.Destro168.events;

import me.Destro168.Messaging.BroadcastLib;
import me.Destro168.Messaging.MessageLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GeneralEvent 
{
	//Variables
	protected Player[] participant;
	protected BroadcastLib bLib;
	protected int phase; //The phase variable represents all the phases of the event. 1: Start, 2: Lobby Wait, 3: Fight, 0:, 4: Duel None
	
	//Gets
	public int getPhase() { return phase; }
	public Player[] getParticipants() { return participant; }
	
	//General functions
	public boolean isHappening() { if (phase != 0) return true; else return false; }
	
	public GeneralEvent()
	{
		setDefaults();
	}
	
	protected void setDefaults()
	{
		//Initialize globals.
		phase = 0;
		participant = new Player[Bukkit.getServer().getMaxPlayers()];
		bLib = new BroadcastLib();
		
		for (int i = 0; i < Bukkit.getServer().getMaxPlayers(); i++)
			participant[i] = null;
	}
	
	public boolean isEventPlayer(Player player)
	{
		if (participant == null)
			participant = new Player[Bukkit.getServer().getMaxPlayers()];
		
		for (int i = 0; i < participant.length; i++)
		{
			if (participant[i] != null)
			{
				if (participant[i] == player)
					return true;
			}
		}
		
		return false;
	}
	
	protected int getEventPlayerId(Player player)
	{
		for (int i = 0; i < participant.length; i++)
		{
			if (participant[i] != null)
			{
				if (participant[i] == player)
					return i;
			}
		}
		
		return -1;
	}
	
	protected boolean addParticipant(Player player)
	{
		MessageLib msgLib = new MessageLib(player);
		
		//Check if the player is already a participant
		if (isEventPlayer(player) == true)
		{
			msgLib.standardMessage("You Are Already A Participant.");
			return false;
		}
		
		//Add the player to the event player array.
		for (int i = 0; i < participant.length; i++)
		{
			if (participant[i] == null)
			{
				participant[i] = player;
				break;
			}
		}
		
		return true;
	}
	
	protected boolean removeParticipant(Player requester, Player removePlayer, boolean displayMessages)
	{
		MessageLib msgLib = null;
		MessageLib msgLib2 = null;
		
		//If the requester is online, then we want to store that information.
		if (requester != null)
			msgLib = new MessageLib(requester);
		
		//If the player to remove is online, and not null, then we want to store that information.
		if (removePlayer != null)
			msgLib2 = new MessageLib(removePlayer);
		
		//If the player is in the event, then...
		if (isEventPlayer(removePlayer) == true)
		{
			//Remove the players.
			for (int i = 0; i < participant.length; i++)
			{
				if (participant[i] == removePlayer)
				{
					participant[i] = null;
					break;
				}
			}
			
			//Tell the requester if they aren't the player that sent the command a success message
			if (msgLib != null && requester != removePlayer && displayMessages == true)
				msgLib.standardMessage("Successfully Removed The Player.");
			
			if (msgLib2 != null && displayMessages == true)
				msgLib2.standardMessage("You Have Been Removed From The Event.");	//Tell the person removed that they were removed from the event.
			
			return true;
		}
		
		if (msgLib != null && requester != removePlayer && displayMessages == true)
			msgLib.standardMessage("Player Is Not Currently In An Event To Be Removed.");
		
		else if (msgLib2 != null && displayMessages == true)
			msgLib.standardMessage("You Can't Be Kicked From The Event Because You Aren't In The Event Currently.");	//Else tell them they aren't in the event and return false.
		
		return false;
	}
	
	protected void messageAllParticipants(String message)
	{
		for (Player player: participant)
		{
			MessageLib msgLib = new MessageLib(player);
			msgLib.standardMessage(message);
		}
	}
	
	protected void teleportAllParticipants(Location loc)
	{
		for (Player player: participant)
		{
			if (player != null)
				player.teleport(loc);
		}
	}
}





















