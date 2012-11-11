package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.ConfigManagers.CustomConfigurationManager;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GuildConfig extends ConfigGod
{
	final static int MAX_GUILDS = 1000;
	public CustomConfigurationManager gccm;
	
	public void setGuildList(List<String> x) { ccm.setCustomList(prefix + "guildList", x); }
	public void setLeader(String x) { gccm.set(prefix + "leader", x); }
	public void setMembers(List<String> x) { gccm.setCustomList(prefix + "members", x); }
	public void setPerks(List<String> x) { gccm.setCustomList(prefix + "perkList", x); }
	public void setPerks(String x) { gccm.set(prefix + "perkList", x); }
	public void setPrivate(boolean x) { gccm.set(prefix + "private", true); }
	public void setMobKills(int x) { gccm.set(prefix + "mobKills", x); }
	
	public List<String> getGuildList() { try { return converter.getStringListFromString(ccm.getString(prefix + "guildList")); } catch (NullPointerException e) { return null; } }
	public String getLeader() { return gccm.getString(prefix + "leader"); }
	public List<String> getMembers() { try { return converter.getStringListFromString(gccm.getString(prefix + "members")); } catch (NullPointerException e) { return null; } }
	public List<String> getPerks() { return converter.getStringListFromString(gccm.getString(prefix + "perkList")); }
	public boolean getPrivate() { return gccm.getBoolean(prefix + "private"); }
	public int getMobKills() { return gccm.getInt(prefix + "mobKills"); }
	
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
	
	public void removeMember(String x)
	{
		List<String> members = getMembers();
		
		if (!(members == null))
			members.remove(x);
		else
			return;
		
		if (members.size() == 0)
			setGuildList(null);
		else
			setMembers(members);
	}
    
	public void guildListAdd(String x)
	{
		List<String> gList = getGuildList();
		
		if (!(gList == null))
			gList.add(x);
		else
		{
			gList = new ArrayList<String>();
			gList.add(x);
		}
		
		setGuildList(gList);
	}
	
	public void guildListRemove(String x)
	{
		List<String> gList = getGuildList();
		
		if (!(gList == null))
			gList.remove(x);
		else
			return;
		
		if (gList.size() == 0)
			setGuildList(null);
		else
			setGuildList(gList);
	}
	
	public boolean setGuildNull(String guildName) 
	{
		if (!exists(guildName))
			return false;
		
		updateGCCM(guildName);
		
		gccm.set("Guilds", null);
		guildListRemove(guildName);
		
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
    
	public void updateGCCM(String guildName)
	{
		gccm = new CustomConfigurationManager(FC_Rpg.dataFolderAbsolutePath + "\\guilds", guildName);
	}
	
	private void handleConfig()
	{
		//Create a config  if not created
		if (getVersion() < 0.2)
		{
			setVersion(0.2);
			
			//Create a default guild in the configuration file.
			updateGCCM("Default");
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
	}
	
    public String listGuilds()
    {
    	boolean guildGreen = true;
    	String list = "Listing guilds: ";
    	int guildCount = 0;
    	String noGuildMessage = ChatColor.RED + "There are currently no created guilds.";
    	
    	if (getGuildList() == null)
    		return noGuildMessage;
    	
    	for (String g : getGuildList())
    	{
    		guildCount++;
			
			if (guildGreen == true)
			{
				list = list + ChatColor.WHITE + g + " ";
				guildGreen = false;
			}
			else
			{
				list = list + ChatColor.YELLOW + g + " ";
				guildGreen = true;
			}
    	}
    	
    	if (guildCount > 0)
    		return list;
    	else
    		return noGuildMessage;
    }
    
    public boolean createGuild(String guildName, String newLeader)
    {
    	//Check to see if the person already is in a guild.
    	if (getGuildByMember(newLeader) != null)
    		return false;
    	
    	//Check to see if any guilds exist with that name;
    	if (getGuildList() != null)
    	{
    		for (String gName : getGuildList())
        	{
        		if (gName.equalsIgnoreCase(guildName))
        			return false;
        	}
    	}
    	
    	updateGCCM(guildName);
    	
    	List<String> members = new ArrayList<String>();
		List<String> perks = new ArrayList<String>();
		
		members.add(newLeader);
		perks.add("none");
		
		setLeader(newLeader);
		setMembers(members);
		setPerks(perks);
		setPrivate(true);
		setMobKills(0);
		
		guildListAdd(guildName);
		
		FC_Rpg.bLib.standardBroadcast(newLeader + " has created the guild " + guildName + ".");
		
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
    
	public boolean removeMemberFromAllGuilds(String playerName)
	{
		for (String gName : getGuildList())
		{
			updateGCCM(gName);
			
			if (getMembers().contains(playerName))
			{
				removeMember(playerName);
				
				List<String> members = getMembers();
				
				if (members.size() > 0)
				{
					//Replace leader if needed
					if (getLeader().equalsIgnoreCase(playerName))
						setLeader(playerName);
				}
				else
					setGuildNull(gName);
				
				//Tell guild members that the player left.
				messageGuildMembers(ChatColor.GRAY + playerName + " has left the guild!");
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean exists(String name)
	{
		if (name == null)
			return false;
		
		updateGCCM(name);
		
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
			updateGCCM(gName);
			
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
		removeMemberFromAllGuilds(playerName);
    	
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
			removeMemberFromAllGuilds(targetPlayer);
			return true;
		}
		
		return false;
	}
	
	public boolean viewGuildInfoByGuildName(String guildName, RpgMessageLib msgLib)
	{
		if (!exists(guildName))
			return false;
		
		return viewGuildInfo(guildName, msgLib);
	}
	
	public boolean viewGuildInfo(String guildName, RpgMessageLib msgLib)
	{
		updateGCCM(guildName);
		
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
		
		List<Player> playerList = new ArrayList<Player>();
		
		for (String pName : getMembers())
		{
			if (Bukkit.getServer().getPlayer(pName) != null)
				playerList.add(Bukkit.getServer().getPlayer(pName));
		}
		
		return playerList;
	}

	public double getGuildBonus(String guildName, double loot) 
	{
		return loot * (1.0D + getOnlineGuildPlayerList(guildName).size() * 0.0025D);
	}
}
