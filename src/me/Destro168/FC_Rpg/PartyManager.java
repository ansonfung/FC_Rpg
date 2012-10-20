package me.Destro168.FC_Rpg;

import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PartyManager
{
	final static int MAX_PARTIES = 1000;
	RpgParty[] rpgParty = new RpgParty[MAX_PARTIES];
	
	public PartyManager()
	{
		for (int i = 0; i < MAX_PARTIES; i++)
			rpgParty[i] = null;
	}
    
    public String getPartyList()
    {
    	boolean partyNameGreen = true;
    	String list = "Listing parties: ";
    	int partyCount = 0;
    	
    	for (int i = 0; i < MAX_PARTIES; i++)
    	{
    		if (rpgParty[i] != null)
    		{
    			partyCount++;
    			
    			if (partyNameGreen == true)
    			{
    				list = list + ChatColor.WHITE + rpgParty[i].getName() + " ";
    				partyNameGreen = false;
    			}
    			else
    			{
    				list = list + ChatColor.YELLOW + rpgParty[i].getName() + " ";
    				partyNameGreen = true;
    			}
    		}
    	}
    	
    	if (partyCount > 0)
    		return list;
    	else
    		return ChatColor.RED + "There are currently no created parties.";
    }
    
    public boolean createParty(String name, String partyName)
    {
    	int count = 0;
    	String newName = partyName;
    	
    	//Check to see if the person already is in a party.
    	if (getPartyByMember(name) != null)
    		return false;
    	
    	//Check to see if any parties exist with that name;
    	while (getParty(partyName) != null)
    	{
    		partyName = newName + count;
    		count++;
    	}
    	
    	for (int i = 0; i < MAX_PARTIES; i++)
    	{
    		if (rpgParty[i] == null)
    		{
    			rpgParty[i] = new RpgParty(name, partyName);
    			
    			FC_Rpg.bLib.standardBroadcast(name + " has created the party " + partyName + ".");
    			
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean closeParty(String name)
    {
		for (int i = 0; i < MAX_PARTIES; i++)
    	{
			if (rpgParty[i] != null)
			{
				if (rpgParty[i].isLeader(name) == true)
				{
					rpgParty[i].setIsOpen(false);
					
					return true;
				}
    		}
    	}
		
		return false;
    }
    
    public boolean openParty(String name)
    {
    	for (int i = 0; i < MAX_PARTIES; i++)
    	{
			if (rpgParty[i] != null)
			{
				if (rpgParty[i].isLeader(name) == true)
				{
					rpgParty[i].setIsOpen(true);
					
					return true;
				}
    		}
    	}
		
		return false;
    }

	public boolean removeMemberFromAllParties(String name)
	{
		for (int i = 0; i < MAX_PARTIES; i++)
    	{
			if (rpgParty[i] != null)
			{
				if (rpgParty[i].isMember(name) == true)
				{
					rpgParty[i].removeMember(name);
					
					rpgParty[i].messageParty(ChatColor.GRAY + name + " has left the party!");
					
					if (rpgParty[i].getPartyMember(0).equals(""))
						rpgParty[i] = null;
					
					return true;
				}
			}
    	}
		
		return false;
	}
	
	public RpgParty getParty(String partyName)
	{
		for (int i = 0; i < MAX_PARTIES; i++)
    	{
			if (rpgParty[i] != null)
			{
				if (rpgParty[i].getName().equals(partyName))
				{
					return rpgParty[i];
				}
			}
    	}
		
		return null;
	}
	
	public RpgParty getPartyByMember(String name)
	{
		for (int i = 0; i < MAX_PARTIES; i++)
    	{
			if (rpgParty[i] != null)
			{
				if (rpgParty[i].isMember(name))
				{
					return rpgParty[i];
				}
			}
    	}
		
		return null;
	}
	
	public boolean addMember(String name, String partyName)
	{
		RpgParty party = getParty(partyName);
		
		if (party == null)
			return false;
				
		if (party.getIsOpen() == true)
		{
			party.addMember(name);
			
			party.messageParty(ChatColor.GRAY + name + " successfully joined the party!");
			
			return true;
		}
		
		return false;
	}
	
	public boolean viewPartyInfo(String partyName, RpgMessageLib msgLib)
	{
		RpgParty party = getParty(partyName);
		
		if (party == null)
			return false;
		
		msgLib.standardHeader("Party Name: " + ChatColor.YELLOW + ChatColor.ITALIC + party.getName());
		msgLib.standardMessage("Mob Kills",String.valueOf(party.getMobKills()));
		msgLib.standardMessage("Party Members",party.returnMemberList());
		
		return true;
	}

	public boolean teleportToLeader(Player player) 
	{
		for (int i = 0; i < MAX_PARTIES; i++)
    	{
    		if (rpgParty[i] != null)
    		{
    			if (rpgParty[i].isMember(player.getName()) == true)
    			{
    				if (rpgParty[i].isLeader(player.getName()) == true)
    					return true;
    				
    				if (Bukkit.getServer().getPlayer(rpgParty[i].getPartyMember(0)) != null)
    				{
    					player.teleport(Bukkit.getServer().getPlayer(rpgParty[i].getPartyMember(0)));
    					return true;
    				}
    				
    			}
    		}
    	}
		
		return false;
	}
}
