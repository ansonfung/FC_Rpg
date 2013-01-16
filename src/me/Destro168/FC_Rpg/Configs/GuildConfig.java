package me.Destro168.FC_Rpg.Configs;

import java.io.File;
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
	
	public GuildConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Guilds");
		handleConfig();
	}
	
	// Private general guild functions
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
			
			createGuild("Default", "Destro168", null);
		}
		
		if (getVersion() < 0.3)
		{
			setVersion(0.3);
			
			setCreationCost(10);
		}
	}

	private boolean checkGuildExists(String userInputForGuildName)
	{
		if (userInputForGuildName == null)
			return false;
		
		updateGfcw(userInputForGuildName);
		
		if (getLeader() == null)
			return false;
		
		return true;
	}
	
	// Public general guild functions
	public void messageGuildMembers(String msg)
	{
		for (String member : getMembers())
		{
			if (Bukkit.getServer().getPlayer(member) != null)
				Bukkit.getServer().getPlayer(member).sendMessage(msg);
		}
	}
	
	// Member Related functions
	public String getGuildByMember(String playerName)
	{
		List<String> guildList = getGuildList();
		
		if (guildList == null || guildList.size() == 0)
			return null;
		
		for (String guildName : guildList)
		{
			updateGfcw(guildName);
			
			if (getMembers().contains(playerName))
				return guildName;
		}
		
		return null;
	}
	
	public boolean addGuildMember(String playerName, String guildName, boolean forceAdmin)
	{
		if (!checkGuildExists(guildName))
			return false;
		
		//Check various conditions to see if this event should fail.
		if (getPrivate() && !forceAdmin)
			return false;
		
		List<String> members = getMembers();
		
		if (members == null || members.size() == 0 || members.contains(playerName))
			return false;
		
		members.add(playerName);
		
		setMembers(members);
		
		//Tell guild members that player joined.
		messageGuildMembers(ChatColor.GRAY + playerName + " successfully joined the guild!");
		
		return true;
	}
	
	public boolean removeGuildMember(String playerName)
	{
		// Updates GFCW and gets guild name.
		String guildName = getGuildByMember(playerName);
		
		if (guildName == null)
			return false;
		
		//Get current guild members
		List<String> members = getMembers();
		
		//If the guild has members and the player is a member.
		if (members != null && members.contains(playerName))
		{
			//Remove them from list of members.
			members.remove(playerName);
			
			if (members.size() == 0)
				deleteGuild(guildName); //If no members left, delete the guild.
			else
			{
				//Update the members.
				setMembers(members);
				
				// If the leader left then we update the leader.
				if (getLeader().equalsIgnoreCase(playerName))
					setLeader(members.get(0));
				
				//Tell guild members that the player left.
				messageGuildMembers(ChatColor.GRAY + playerName + " has left " + guildName + "!");
			}

			return true;
		}
		
		return false;
	}
	
	public boolean kickGuildMember(String requester, String targetPlayer, boolean forceAdmin)
	{
		if (!checkGuildExists(getGuildByMember(requester)))
			return false;
		
		if (getLeader().equalsIgnoreCase(requester) || forceAdmin)
		{
			removeGuildMember(targetPlayer);
			return true;
		}
		
		return false;
	}
	
	
	// General guild functions
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
    
    
    public boolean viewGuildInfoByGuildName(String guildName, MessageLib msgLib)
	{
		if (!checkGuildExists(guildName))
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
	
    // Guild creation and destruction commands.
    public void createGuild(String guildName, String newLeaderName, MessageLib msgLib)
    {
    	//Check to see if the person already is in a guild.
    	if (getGuildByMember(newLeaderName) != null)
    	{
    		if (msgLib != null)
    			msgLib.standardError("Command failed because you're already in a guild.");
    		
    		return;
    	}
    	
    	//Check to see if any guilds exist with that name;
    	if (getGuildList() != null)
    	{
    		for (String gName : getGuildList())
        	{
        		if (gName.equalsIgnoreCase(guildName))
        		{
        			if (msgLib != null)
        				msgLib.standardError("Command failed because Guild already exists.");
            		
            		return;
        		}
        	}
    	}
    	
    	//Prevent crazy guild names.
    	if (guildName.length() < 3 || guildName.length() > 10)
    	{
    		if (msgLib != null)
    			msgLib.standardError("Command failed. Guild names must be within 3 and 10 characters.");
    		
    		return;
    	}
    	
    	double creationCost = FC_Rpg.guildConfig.getCreationCost();
    	
    	if (FC_Rpg.economy.getBalance(newLeaderName) < creationCost)
    	{
    		if (msgLib != null)
    			msgLib.standardError("Command failed because you need " + creationCost + " to create a guild.");
    		
    		return;
    	}
    	
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
		
		//Broadcast guild creation.
		FC_Rpg.rpgBroadcast.rpgBroadcast("&p" + newLeaderName + "&p"," has created the guild ",guildName,"!");
    }
    
    
	public boolean deleteGuild(String guildName)
	{
		//Updates GFCW
		if (!checkGuildExists(guildName))
			return false;
		
		// Clear guild file data.
		gfcw.clearFileData();
		
		return true;
	}
	
	
	// Command related functions.
    public boolean playerAttemptSetGuildPrivate(String memberName, boolean trueOrFalse, boolean forcePrivate)
    {
    	if (!checkGuildExists(getGuildByMember(memberName)))
    		return false;
    	
    	if (getLeader().equalsIgnoreCase(memberName) || forcePrivate)
		{
			setPrivate(trueOrFalse);
			return true;
		}
    	
		return false;
    }
    
    
	public boolean teleportToLeader(Player player) 
	{
		if (!checkGuildExists(getGuildByMember(player.getName())))
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
		if (!checkGuildExists(guildName))
			return;
		
		int mobKills = getMobKills();
		
		setMobKills(mobKills + 1);
		
		//Perk 1 - exp, 2 - loot, 3 - damage, 4 - lifesteal
		/* Todo 
		if (mobKills == 9999999) //Add increments by 250 later.
			addPerk("1");
		
		else if (mobKills == 9999999)
			addPerk("2");
		
		else if (mobKills == 9999999)
			addPerk("3");
		
		else if (mobKills == 9999999)
			addPerk("4");
		*/
	}
	
	public List<Player> getOnlineGuildPlayerList(String guildName) 
	{
		if (!checkGuildExists(guildName))
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

	public double getGuildBonus(int nearbyCount) 
	{
		double totalBonus;
		
		totalBonus = 1.0D + nearbyCount * 0.0040D;
		
		if (totalBonus > 1.2)
			totalBonus = 1.2;
		
		return totalBonus;
	}
	
	/* - Todo
	public void addPerk(String perk)
	{
		List<String> perks = getPerks();
		perks.add(perk);
		setPerks(perks);
	}
	*/
	
	//Global guild settings
	public void setCreationCost(double x) { fcw.set(prefix + "creationCost", x); }
	public double getCreationCost() { return fcw.getDouble(prefix + "creationCost"); }
	
	//Individual specific guild settings
	public void setLeader(String x) { gfcw.set(prefix + "leader", x); }
	public void setMembers(List<String> x) { gfcw.setCustomList(prefix + "members", x); }
	public void setPerks(List<String> x) { gfcw.setCustomList(prefix + "perkList", x); }
	public void setPerks(String x) { gfcw.set(prefix + "perkList", x); }
	public void setPrivate(boolean x) { gfcw.set(prefix + "private", x); }
	public void setMobKills(int x) { gfcw.set(prefix + "mobKills", x); }
	
	public String getLeader() { return gfcw.getString(prefix + "leader"); }
	public List<String> getMembers() { try { return gfcw.getCustomStringList(prefix + "members"); } catch (NullPointerException e) { return null; } }
	public List<String> getPerks() { return gfcw.getCustomStringList(prefix + "perkList"); }
	public boolean getPrivate() { return gfcw.getBoolean(prefix + "private"); }
	public int getMobKills() { return gfcw.getInt(prefix + "mobKills"); }
	
	public List<String> getGuildList()
	{
		//Variable Declarations
		File folder = new File(FC_Rpg.dataFolderAbsolutePath + "/guilds");
		File[] fileList = folder.listFiles();
		List<String> guildList = new ArrayList<String>();
		
		for (int i = 0; i < fileList.length; i++)
		{
			if (fileList[i].isFile())
				guildList.add(fileList[i].getName().replaceAll(".yml", ""));
		}
		
		return guildList;
	}
	
	public void updateGfcw(String guildName) { gfcw = new FileConfigurationWrapper(FC_Rpg.dataFolderAbsolutePath + "/guilds", guildName); }
}
