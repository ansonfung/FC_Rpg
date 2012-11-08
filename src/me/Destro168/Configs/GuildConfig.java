package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.LoadedObjects.Guild;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GuildConfig extends ConfigGod
{
	final static int MAX_GUILDS = 1000;
	
	List<Guild> guildList = new ArrayList<Guild>();
	
	public void setName(int i, String x) { ccm.set(prefix + i + ".name", x); }
	public void setLeader(int i, String x) { ccm.set(prefix + i + ".leader", x); }
	public void setMembers(int i, List<String> x) { ccm.setCustomList(prefix + i + ".members", x); }
	public void setMembers(int i, String x) { ccm.set(prefix + i + ".members", x); }
	public void setPerks(int i, List<String> x) { ccm.setCustomList(prefix + i + ".perkList", x); }
	public void setPerks(int i, String x) { ccm.set(prefix + i + ".perkList", x); }
	public void setPrivate(int i, boolean x) { ccm.set(prefix + i + ".private", true); }
	public void setMobKills(int i, int x) { ccm.set(prefix + i + ".mobKills", x); }
	
	public String getName(int i) { return ccm.getString(prefix + i + ".name"); }
	public String getLeader(int i) { return ccm.getString(prefix + i + ".leader"); }
	public List<String> getMembers(int i) { return converter.getStringListFromString(ccm.getString(prefix + i + ".members")); }
	public List<String> getPerks(int i) { return converter.getStringListFromString(ccm.getString(prefix + i + ".perkList")); }
	public boolean getPrivate(int i) { return ccm.getBoolean(prefix + i + ".private"); }
	public int getMobKills(int i) { return ccm.getInt(prefix + i + ".mobKills"); }
	
	public void setGuildNull(int i) { ccm.set(prefix + i, null); guildList.remove(i); }
	
	public boolean setGuildNull(String x) 
	{
		int i = getGuildIndex(x);
		
		if (i != -1)
		{
			ccm.set(prefix + i, null);
			guildList.remove(i);
			return true;
		}
		else
			return false;
		
		
	}
	
	public void addPerk(int guildIndex, String perk)
	{
		List<String> perks = getPerks(guildIndex);
		perks.add(perk);
		setPerks(guildIndex, perks);
	}
	
	public GuildConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Guilds");
		handleConfig();
	}
    
	private void handleConfig()
	{
		//Create a config  if not created
		if (getVersion() < 0.1)
		{
			setVersion(0.1);
			
			setName(0, "Default");
			setLeader(0, "[NONYA]");
			setMembers(0, "Face1,Pie2");
			setPerks(0, "p1");
			setPrivate(0, true);
			setMobKills(0, 99999);
		}
		
		int breakPoint = 0;
		
		for (int i = 0; i < MAX_GUILDS; i++)
		{
			if (getName(i) != null)
			{
				if (!getName(i).equals(""))
					guildList.add(new Guild(getName(i), getLeader(i), getMembers(i), getPerks(i), getPrivate(i), getMobKills(i)));
			}
			else
			{
				breakPoint++;
				
				if (breakPoint < 50)
					break;
			}
		}
	}
	
    public String getGuildList()
    {
    	boolean guildGreen = true;
    	String list = "Listing guilds: ";
    	int guildCount = 0;
    	
    	for (Guild g : guildList)
    	{
    		guildCount++;
			
			if (guildGreen == true)
			{
				list = list + ChatColor.WHITE + g.getName() + " ";
				guildGreen = false;
			}
			else
			{
				list = list + ChatColor.YELLOW + g.getName() + " ";
				guildGreen = true;
			}
    	}
    	
    	if (guildCount > 0)
    		return list;
    	else
    		return ChatColor.RED + "There are currently no created guilds.";
    }
    
    public boolean createGuild(String guildName, String newLeader)
    {
    	int count = 0;
    	String newName = guildName;
    	
    	//Check to see if the person already is in a guild.
    	if (getGuildByMember(newLeader) != null)
    		return false;
    	
    	//Check to see if any guilds exist with that name;
    	while (getGuildByGuildName(guildName) != null)
    	{
    		guildName = newName + "_" + count;
    		count++;
    	}
    	
    	for (int i = 0; i < MAX_GUILDS; i++)
    	{
    		if (getName(i) == null)
    		{
    			List<String> members = new ArrayList<String>();
    			List<String> perks = new ArrayList<String>();
    			
    			members.add(newLeader);
    			perks.add("none");
    			
    			guildList.add(new Guild(guildName, newLeader, members, perks, true, 0));
    			
    			setName(i, guildName);
    			setLeader(i, newLeader);
    			setMembers(i, members);
    			setPerks(i, perks);
    			setPrivate(i, true);
    			setMobKills(i, 0);
    			
    			FC_Rpg.bLib.standardBroadcast(newLeader + " has created the guild " + guildName + ".");
    			
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean playerAttemptSetGuildPrivate(String memberName, boolean trueOrFalse)
    {
		for (int i = 0; i < guildList.size(); i++)
    	{
			if (guildList.get(i) != null)
			{
				if (guildList.get(i).isLeader(memberName) == true)
				{
					setPrivate(i, trueOrFalse);
					return true;
				}
    		}
    	}
		
		return false;
    }
    
    public void adminForceGuildPrivate(String guildName, boolean trueOrFalse)
    {
    	setPrivate(getGuildIndex(guildName), trueOrFalse);
    }
    
	public boolean removeMemberFromAllGuilds(String playerName)
	{
		for (int i = 0; i < guildList.size(); i++)
    	{
			if (guildList.get(i) != null)
			{
				if (guildList.get(i).isMember(playerName) == true)
				{
					guildList.get(i).removeMember(playerName);
					
					if (guildList.get(i).getMembers().size() > 0)
					{
						//Update members
						setMembers(i, guildList.get(i).getMembers());
						
						//Replace leader if needed
						if (guildList.get(i).getLeader().equalsIgnoreCase(playerName))
							setLeader(i, getMembers(i).get(0));
						
						guildList.get(i).messageGuildMembers(ChatColor.GRAY + playerName + " has left the guild!");
					}
					else
						setGuildNull(i);
					
					return true;
				}
			}
    	}
		
		return false;
	}
	
	public int getGuildIndex(String guildName)
	{
		for (int i = 0; i < guildList.size(); i++)
    	{
			if (guildList.get(i) != null)
			{
				if (guildList.get(i).getName().equalsIgnoreCase(guildName))
				{
					return i;
				}
			}
    	}
		
		return -1;
	}
	
	public Guild getGuildByGuildName(String guildName)
	{
		int guildIndex = getGuildIndex(guildName);
		
		if (guildIndex != -1)
			return guildList.get(guildIndex);
		else
			return null;
	}
	
	public Guild getGuildByMember(String playerName)
	{
		for (int i = 0; i < guildList.size(); i++)
    	{
			if (guildList.get(i) != null)
			{
				if (guildList.get(i).isMember(playerName))
				{
					return guildList.get(i);
				}
			}
    	}
		
		return null;
	}
	
	public boolean addMember(String playerName, String guildName, boolean forceAdmin)
	{
		removeMemberFromAllGuilds(playerName);
		
		Guild g = getGuildByGuildName(guildName);
		
		if (g == null)
			return false;
		
		if (g.getPrivate() && !forceAdmin)
			return false;
		
		g.getMembers().add(playerName);
		setMembers(getGuildIndex(guildName), g.getMembers());
		g.messageGuildMembers(ChatColor.GRAY + playerName + " successfully joined the guild!");
		return true;
	}
	
	public boolean kickMember(String requester, String targetPlayer, boolean forceAdmin)
	{
		Guild g = getGuildByMember(requester);
		
		if (g == null)
			return true;
		
		if (g.isLeader(requester) || forceAdmin)
		{
			removeMemberFromAllGuilds(targetPlayer);
			return true;
		}
		
		return false;
	}
	
	public boolean viewGuildInfoByGuildName(String guildName, RpgMessageLib msgLib)
	{
		Guild guild = getGuildByGuildName(guildName);
		
		if (guild == null)
			return false;
		
		return viewGuildInfo(guild, msgLib);
	}
	
	public boolean viewGuildInfo(Guild guild, RpgMessageLib msgLib)
	{
		msgLib.standardHeader("Guild Name: " + ChatColor.YELLOW + ChatColor.ITALIC + guild.getName());
		msgLib.standardMessage("Leader",guild.getLeader());
		msgLib.standardMessage("Members",guild.returnMemberList());
		//msgLib.standardMessage("Perks",guild.getPerks()); TODO - ADD PERKS
		msgLib.standardMessage("Private?",String.valueOf(guild.getPrivate()));
		msgLib.standardMessage("Mob Kills",String.valueOf(guild.getMobKills()));
		
		return true;
	}

	public boolean teleportToLeader(Player player) 
	{
		for (int i = 0; i < guildList.size(); i++)
    	{
    		if (guildList.get(i) != null)
    		{
    			if (guildList.get(i).isMember(player.getName()) == true)
    			{
    				if (guildList.get(i).isLeader(player.getName()) == true)
    					return true;
    				
    				if (Bukkit.getServer().getPlayer(guildList.get(i).getGuildMember(0)) != null)
    				{
    					player.teleport(Bukkit.getServer().getPlayer(guildList.get(i).getGuildMember(0)));
    					return true;
    				}
    				
    			}
    		}
    	}
		
		return false;
	}
	
	public void addMobKill(String guildName)
	{
		int mobKills = getGuildByGuildName(guildName).getMobKills();
		int pi = getGuildIndex(guildName);
		
		setMobKills(pi, mobKills + 1);
		
		//Perk 1 - exp, 2 - loot, 3 - damage, 4 - lifesteal
		if (mobKills == 9999999) //Add increments by 250 later.
			addPerk(pi, "1");
		
		else if (mobKills == 9999999)
			addPerk(pi, "2");
		
		else if (mobKills == 9999999)
			addPerk(pi, "3");
		
		else if (mobKills == 9999999)
			addPerk(pi, "4");
	}
}
