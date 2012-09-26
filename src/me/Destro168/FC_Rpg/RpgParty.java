package me.Destro168.FC_Rpg;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.Entities.EntityLocationLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RpgParty 
{
	String[] partyMember = new String[100]; //Assumes party leader is always in the first index.
	String partyName;
	boolean[] perk = new boolean[4];
	boolean isOpen;
	int mobKills;
	
	public void setPartyName(String x) { partyName = x; }
	public void setPartyLeader(String x) { partyMember[0] = x; }
	public void setIsOpen(Boolean x) { isOpen = x; }
	
	public String getName() { return partyName; }
	public boolean getPerk(int x) { return perk[x]; }
	public boolean getIsOpen() { return isOpen; }
	public int getMobKills() { return mobKills; }
	
	public int getPartySize() 
	{
		int count = 0;
		
		for (int i = 0; i < 100; i++)
		{
			if (partyMember[i] != "")
				count++;
		}
		
		return count;
	}
	
	public RpgParty(String leaderName, String partyName_)
	{
		partyMember[0] = leaderName;
		partyName = partyName_;
		
		for (int i = 1; i < 100; i++)
			partyMember[i] = "";
		
		for (int i = 0; i < 4; i++)
			perk[i] = false;
		
		isOpen = true;
		
		mobKills = 0;
	}
	
	public void messageParty(String message)
	{
		for (int i = 0; i < 100; i++)
		{
			if (!(Bukkit.getServer().getPlayer(partyMember[i]).equals("")))
			{
				Bukkit.getServer().getPlayer(partyMember[i]).sendMessage(message);
				i = 100;
			}
		}
	}
	
	public boolean closeToPartyLeader(int index)
	{
		return closeToPartyMember(0, index);
	}
	
	public boolean closeToPartyMember(int index, int index2)
	{
		EntityLocationLib ell = new EntityLocationLib();
		
		Player p1 = Bukkit.getServer().getPlayer(partyMember[index]);
		Player p2 = Bukkit.getServer().getPlayer(partyMember[index2]);
		
		return ell.isNearby(p1, p2, 5);
	}
	
	/*
	 * 
	 * EntityLocationLib ell = new EntityLocationLib();
		
		Player p1 = Bukkit.getServer().getPlayer(partyMember[index]);
		Player p2 = Bukkit.getServer().getPlayer(partyMember[index2]);
		
		return ell.isInRange(p1, p2, 5);
	 */
	
	public void addMobKill()
	{
		mobKills = mobKills + 1;
		
		//Perk 1 - exp, 2 - loot, 3 - damage, 4 - lifesteal
		if (mobKills == 250)
			perk[0] = true;
		
		if (mobKills == 500)
			perk[1] = true;
		
		if (mobKills == 750)
			perk[2] = true;
		
		if (mobKills == 1000)
			perk[3] = true;
	}
	
	public Boolean isLeader(String x)
	{
		if (partyMember[0].equals(x))
			return true;
		
		return false;
	}
	
	public Boolean isMember(String x)
	{
		for (int i = 0; i < 100; i++)
		{
			if (partyMember[i].equals(x))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean addMember(String name)
	{
		for (int i = 0; i < 100; i++)
		{
			if (partyMember[i].equals(""))
			{
				partyMember[i] = name;
				return true;
			}
		}
		
		return false;
	}
	
	public int getPartyMemberByName(String name)
	{
		for (int i = 0; i < 100; i++)
		{
			if (partyMember[i].equals(name))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public String getPartyMember(int index)
	{
		return partyMember[index];
	}
	
	public int getMemberCount()
	{
		int count = 0;
		
		for (int i = 0; i < 100; i++)
		{
			if (!(partyMember[i].equals("")))
			{
				count++;
			}
		}
		
		return count;
	}
	
	//Will return whether the party is empty or not.
	public boolean removeMember(String name)
	{
		int index = 0;
		
		//Remove the member from the party.
		for (int i = 0; i < 100; i++)
		{
			if (partyMember[i].equals(name))
			{
				partyMember[i].equals("");
				index = i;
			}
		}
		
		//Shift all party members down to that index to keep a 'newest member last' scheme.
		//Also automatically reassigns party leader.
		for (int i = index; i < 99; i++)
		{
			partyMember[i] = partyMember[i + 1];
		}
		
		//If more than 1 member is left return true;
		if (getMemberCount() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Returns a list of party members with cool coloring :D
	public String returnMemberList()
	{
		String message = ChatColor.AQUA + "[Party Leader] " + partyMember[0];
		boolean white = true;
		
		for (int i = 1; i < 100; i++)
		{
			if (!(partyMember[i].equals("")))
			{
				if (white == true)
				{
					message = message + ", " + ChatColor.WHITE + partyMember[i];
					white = false;
				}
				else
				{
					message = message + ", " + ChatColor.YELLOW + partyMember[i];
					white = true;
				}
			}
		}
		
		return message;
	}
	
	public String[] getPartyMembers()
	{
		return partyMember;
	}
	
	public List<Player> getPartyPlayerList()
	{
		List<Player> playerList = new ArrayList<Player>();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			if (isMember(player.getName()) == true)
			{
				playerList.add(player);
			}
		}
		
		return playerList;
	}
	
	public double getPartyBonus(double loot)
	{
		int nearLeader = 1; //Include leader
		
		for (int j = 1; j < getMemberCount(); j++)
		{
			if (closeToPartyLeader(j) == true)
			{
				nearLeader++;
			}
		}
		
		if (nearLeader == 1)
			return loot;
		else
			return (loot / nearLeader) + nearLeader;
	}
}
