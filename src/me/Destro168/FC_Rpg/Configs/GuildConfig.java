package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.FileConfigurationWrapper;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;
import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GuildConfig extends ConfigGod
{
	final static int MAX_GUILDS = 1000;
	public FileConfigurationWrapper gfcw;
	
	//Global guild settings
	public void setCreationCost(double x) { fcw.set(prefix + "creationCost", x); }
	
	public double getCreationCost() { return fcw.getDouble(prefix + "creationCost"); }
	
	//Individual specific guild settings
	public void setGuildList(List<String> x) { fcw.setCustomList(prefix + "guildList", x); }
	public void setLeader(String x) { gfcw.set(prefix + "leader", x); }
	public void setMembers(List<String> x) { gfcw.setCustomList(prefix + "members", x); }
	public void setPerks(List<String> x) { gfcw.setCustomList(prefix + "perkList", x); }
	public void setPerks(String x) { gfcw.set(prefix + "perkList", x); }
	public void setPrivate(boolean x) { gfcw.set(prefix + "private", true); }
	public void setMobKills(int x) { gfcw.set(prefix + "mobKills", x); }
	
	public List<String> getGuildList() { try { return converter.getStringListFromString(fcw.getString(prefix + "guildList")); } catch (NullPointerException e) { return null; } }
	public String getLeader() { return gfcw.getString(prefix + "leader"); }
	public List<String> getMembers() { try { return converter.getStringListFromString(gfcw.getString(prefix + "members")); } catch (NullPointerException e) { return null; } }
	public List<String> getPerks() { return converter.getStringListFromString(gfcw.getString(prefix + "perkList")); }
	public boolean getPrivate() { return gfcw.getBoolean(prefix + "private"); }
	public int getMobKills() { return gfcw.getInt(prefix + "mobKills"); }
	
	public void addMember(String x)
	{
		List<String> members = getMembers();
		
		if (!(members == null))
			members.add(x);
		else
		{
			members = new ArrayList<String>();
			members.add(x);
		}
		
		setMembers(members);
	}
	
	public void removeMember(String x, String gName)
	{
		List<String> members = getMembers();
		
		if (!(members == null))
			members.remove(x);
		else
			return;
		
		if (members.size() == 0)
			guildListRemove(gName);
		else
			setMembers(members);
	}
    
	public boolean guildListAdd(String x)
	{
		if (!exists(x))
			return false;
		
		updateGfcw(x);
		
		List<String> gList = getGuildList();
		
		if (!(gList == null))
			gList.add(x);
		else
		{
			gList = new ArrayList<String>();
			gList.add(x);
		}
		
		setGuildList(gList);
		
		return true;
	}
	
	public boolean guildListRemove(String x)
	{
		if (!exists(x))
			return false;
		
		updateGfcw(x);
		
		List<String> gList = getGuildList();
		
		if (!(gList == null))
		{
			gList.remove(x);
			gfcw.clearFileData();
		}
		else
			return false;
		
		setGuildList(gList);
		
		return true;
	}
	
	public void addPerk(String perk)
	{
		List<String> perks = getPerks();
		perks.add(perk);
		setPerks(perks);
	}
	
	public GuildConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Guilds");
		handleConfig();
	}
    
	public void updateGfcw(String guildName)
	{
		gfcw = new FileConfigurationWrapper(FC_Rpg.dataFolderAbsolutePath + "/guilds", guildName);
	}
	
	private void handleConfig()
	{
		//Create a config  if not created
		if (getVersion() < 0.2)
		{
			setVersion(0.2);
			
			//Create a default guild in the configuration file.
			updateGfcw("Default");
			setLeader("[NONYA]");
			
			List<String> a = new ArrayList<String>();
			
			a.add("Face1");
			a.add("Face2");
			
			setMembers(a);
			setPerks("p1");
			setPrivate(true);
			setMobKills(99999);
			
			guildListAdd("Default");
		}
		
		if (getVersion() < 0.3)
		{
			setVersion(0.3);
			
			setCreationCost(10);
		}
	}
	
    public String listGuilds()
    {
    	List<String> guildList = getGuildList();
    	String noGuildMessage = ChatColor.RED + "There are currently no created guilds.";

    	if (guildList == null)
    		return noGuildMessage;
    	
    	String list = "Listing guilds: ";
    	int guildCount = 0;
    	boolean alternate = false;
    	int guildListSize = guildList.size();
    	ChatColor color = ChatColor.WHITE;
    	
    	for (int i = 0; i < guildListSize; i++)
		{
    		guildCount++;
			
    		if (alternate)
    			color = ChatColor.YELLOW;
			else if (!alternate)
    			color = ChatColor.WHITE;
			
    		list += color + guildList.get(i);
    		
    		if (i != guildListSize - 1)
    			list += ", ";
    		
    		alternate = !alternate;
		}
    	
    	if (guildCount > 0)
    		return list; //Return char array without strings.
    	else
    		return noGuildMessage;
    }
    
    public boolean createGuild(String guildName, Player newLeader, MessageLib msgLib)
    {
    	String newLeaderName = newLeader.getName();
    	
    	//Check to see if the person already is in a guild.
    	if (getGuildByMember(newLeaderName) != null)
    		return msgLib.standardError("Command failed because you're already in a guild.");
    	
    	//Check to see if any guilds exist with that name;
    	if (getGuildList() != null)
    	{
    		for (String gName : getGuildList())
        	{
        		if (gName.equalsIgnoreCase(guildName))
        			return msgLib.standardError("Command failed because Guild already exists.");
        	}
    	}
    	
    	//Prevent crazy guild names.
    	if (guildName.length() < 3 || guildName.length() > 10)
    		return msgLib.standardError("Command failed. Guild names must be within 3 and 10 characters.");
    	
    	double creationCost = FC_Rpg.guildConfig.getCreationCost();
    	
    	if (FC_Rpg.economy.getBalance(newLeaderName) < creationCost)
    		return msgLib.standardError("Command failed because you need " + creationCost + " to create a guild.");
    	
    	// Remove money from player.
    	FC_Rpg.economy.withdrawPlayer(newLeaderName, creationCost);
    	
    	updateGfcw(guildName);
    	
    	List<String> members = new ArrayList<String>();
		List<String> perks = new ArrayList<String>();
		
		members.add(newLeaderName);
		perks.add("none");
		
		setLeader(newLeaderName);
		setMembers(members);
		setPerks(perks);
		setPrivate(true);
		setMobKills(0);
		
		guildListAdd(guildName);
		
		FC_Rpg.rpgBroadcast.rpgBroadcast(newLeaderName + " has created the guild " + guildName + "!");
		
		return true;
    }
    
    public boolean playerAttemptSetGuildPrivate(String memberName, boolean trueOrFalse, boolean forcePrivate)
    {
    	if (!exists(getGuildByMember(memberName)))
    		return false;
    	
    	if (getLeader().equalsIgnoreCase(memberName) || forcePrivate)
		{
			setPrivate(trueOrFalse);
			return true;
		}
    	
		return false;
    }
    
	public boolean removeMemberFromGuild(String playerName)
	{
		for (String gName : getGuildList())
		{
			updateGfcw(gName);

			if (getMembers().contains(playerName))
			{
				//Remove the player.
				removeMember(playerName, gName);
				
				//Tell guild members that the player left.
				messageGuildMembers(ChatColor.GRAY + playerName + " has left " + gName + "!");
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean exists(String name)
	{
		if (name == null)
			return false;
		
		updateGfcw(name);
		
		if (getLeader() == null)
			return false;
		
		return true;
	}
	
	public String getGuildByMember(String playerName)
	{
		List<String> guildList = getGuildList();
		
		if (getGuildList() == null)
			return null;
		
		for (String gName : guildList)
		{
			updateGfcw(gName);
			
			for (String pName : getMembers())
			{
				if (pName.equalsIgnoreCase(playerName))
					return gName;
			}
		}
		
		return null;
	}
	
	public boolean attemptAddGuildMember(String playerName, String guildName, boolean forceAdmin)
	{
		removeMemberFromGuild(playerName);
    	
    	if (!exists(guildName))
    		return false;
    	
		if (getPrivate() && !forceAdmin)
			return false;
		
		addMember(playerName);
		
		//Tell guild members that player joined.
		messageGuildMembers(ChatColor.GRAY + playerName + " successfully joined the guild!");
		
		return true;
	}
	
	public void messageGuildMembers(String x)
	{
		for (String p : getMembers())
		{
			if (Bukkit.getServer().getPlayer(p) != null)
				Bukkit.getServer().getPlayer(p).sendMessage(x);
		}
	}
	
	public boolean kickMember(String requester, String targetPlayer, boolean forceAdmin)
	{
		if (!exists(getGuildByMember(requester)))
			return false;
		
		if (getLeader().equalsIgnoreCase(requester) || forceAdmin)
		{
			removeMemberFromGuild(targetPlayer);
			return true;
		}
		
		return false;
	}
	
	public boolean viewGuildInfoByGuildName(String guildName, MessageLib msgLib)
	{
		if (!exists(guildName))
			return false;
		
		return viewGuildInfo(guildName, msgLib);
	}
	
	public boolean viewGuildInfo(String guildName, MessageLib msgLib)
	{
		updateGfcw(guildName);
		
		msgLib.standardHeader("Guild Name: " + ChatColor.YELLOW + ChatColor.ITALIC + guildName);
		msgLib.standardMessage("Leader",getLeader());
		
    	boolean colorAlternate = true;
    	String list = "Members: ";
    	int count = 0;
    	
		for (String g : getMembers())
    	{
			if (colorAlternate == true)
			{
				if (count != 0)
					list += ", ";
				
				list += ChatColor.WHITE + g;
				colorAlternate = false;
			}
			else
			{
				list += ", " + ChatColor.YELLOW + g;
				colorAlternate = true;
			}
			
			count++;
    	}
		
		msgLib.standardMessage(list);
		
		//msgLib.standardMessage("Perks",guild.getPerks()); TODO - ADD PERKS
		msgLib.standardMessage("Private?",String.valueOf(getPrivate()));
		msgLib.standardMessage("Mob Kills",String.valueOf(getMobKills()));
		
		return true;
	}

	public boolean teleportToLeader(Player player) 
	{
		if (!exists(getGuildByMember(player.getName())))
			return false;
		
		String leaderName = getLeader();
		
		if (leaderName.equalsIgnoreCase(player.getName()))
			return true;
		
		if (Bukkit.getServer().getPlayer(leaderName) != null)
		{
			player.teleport(Bukkit.getServer().getPlayer(leaderName));
			return true;
		}
		
		return false;
	}
	
	public void addMobKill(String guildName)
	{
		if (!exists(guildName))
			return;
		
		int mobKills = getMobKills();
		
		setMobKills(mobKills + 1);
		
		//Perk 1 - exp, 2 - loot, 3 - damage, 4 - lifesteal
		if (mobKills == 9999999) //Add increments by 250 later.
			addPerk("1");
		
		else if (mobKills == 9999999)
			addPerk("2");
		
		else if (mobKills == 9999999)
			addPerk("3");
		
		else if (mobKills == 9999999)
			addPerk("4");
	}
	
	public List<Player> getOnlineGuildPlayerList(String guildName) 
	{
		if (!exists(guildName))
			return null;
		
		// Update the gfcw.
		this.updateGfcw(guildName);
		
		List<Player> playerList = new ArrayList<Player>();
		Player p;
		
		for (String pName : getMembers())
		{
			p = Bukkit.getServer().getPlayer(pName);
			
			if (p != null)
				playerList.add(p);
		}
		
		return playerList;
	}

	public double getGuildBonus(String guildName) 
	{
		List<Player> guildPlayers = getOnlineGuildPlayerList(guildName);
		double totalBonus;
		int size = 1;
		
		// Update the gfcw.
		this.updateGfcw(guildName);
		
		if (!(guildPlayers == null))
			size = guildPlayers.size();
		
		totalBonus = 1.0D + size * 0.0040D;
		
		if (totalBonus > 1.2)
			totalBonus = 1.2;
		
		return totalBonus;
	}
}
