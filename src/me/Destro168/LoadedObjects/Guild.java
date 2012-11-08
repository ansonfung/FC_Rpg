package me.Destro168.LoadedObjects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Guild 
{
	private String name;
	private String leader;
	private List<String> members = new ArrayList<String>();
	private List<String> perks = new ArrayList<String>();
	private boolean isPrivate;
	private int mobKills;
	
	public String getName() { return name; }
	public String getLeader() { return leader; }
	public List<String> getMembers() { return members; }
	public List<String> getPerks() { return perks; }
	public boolean getPrivate() { return isPrivate; }
	public int getMobKills() { return mobKills; }
	
	public Guild(String name_, String leader_, List<String> members_, List<String> perks_, boolean isPrivate_, int mobKills_)
	{
		name = name_;
		leader = leader_;
		members = members_;
		perks = perks_;
		isPrivate = isPrivate_;
		mobKills = mobKills_;
	}
	
	public void messageGuildMembers(String message)
	{
		for (String name : members)
		{
			if (Bukkit.getServer().getPlayer(name) != null)
				Bukkit.getServer().getPlayer(name).sendMessage(message);
		}
	}
	
	public Boolean isMember(String x)
	{
		for (String memberName : members)
		{
			if (memberName.equalsIgnoreCase(x))
				return true;
		}
		
		return false;
	}

	public Boolean isLeader(String name)
	{
		if (name.equalsIgnoreCase(leader))
			return true;
		
		return false;
	}
	
	public int getLeaderIndex()
	{
		for (int i = 0; i < members.size(); i++)
		{
			if (members.get(i).equalsIgnoreCase(leader))
				return i;
		}
		
		return -1;
	}
	
	public int getGuildMemberByName(String name)
	{
		for (int i = 0; i < 100; i++)
		{
			if (members.get(i).equalsIgnoreCase(name))
				return i;
		}
		
		return -1;
	}
	
	public String getGuildMember(int i)
	{
		return members.get(i);
	}
	
	//Returns a list of guild members with cool coloring :D
	public String returnMemberList()
	{
		String message = members.get(0);
		boolean white = true;
		
		if (members.size() == 1)
			return message;
		
		for (int i = 1; i < members.size(); i++)
		{
			if (white == true)
			{
				message = message + ", " + ChatColor.WHITE + members.get(i);
				white = false;
			}
			else
			{
				message = message + ", " + ChatColor.YELLOW + members.get(i);
				white = true;
			}
		}
		
		return message;
	}
	
	public List<Player> getOnlineGuildPlayerList()
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
	
	public double getGuildBonus(double loot)
	{
		return loot * (1 + (getOnlineGuildPlayerList().size() * .0025));
	}
	
	//Will return whether the guild is empty or not.
	public void removeMember(String name)
	{
		//Remove the member from the guild.
		if (members.contains(name))
			members.remove(name);
		
		//Check to see if leader is removed.
		if (name.equalsIgnoreCase(leader))
		{
			if (members.size() > 0)
				leader = members.get(0);
		}
	}
}
