package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.configuration.file.FileConfiguration;

public class GroupManager 
{
	private FileConfiguration config;
	private final String groupPrefix = "Groups.";
	private List<Group> groupList;
	
	private void setVersion(double x) { config.set(groupPrefix + "version", x); }
	public double getVersion() { return config.getDouble(groupPrefix + "version"); }
	
	public List<Group> getGroups() { return groupList; }
	
	public GroupManager()
	{
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		config = FC_Rpg.plugin.getConfig();
		
		//If a past config wasn't created, create a new one.
		if (getVersion() < 0.1)
		{
			//Set the version.
			setVersion(0.1);
			
			int totalGroups = 11;
			
			//Variable Declarations
			String[] groups = new String[totalGroups];
			String[] groupsFormats = new String[totalGroups];
			int[] timeReqs = new int[totalGroups];
			int[] jobReqs = new int[totalGroups];
			
			//Set the default groups.
			groups[0] = "Owner";
			groups[1] = "Administrator";
			groups[2] = "Master";
			groups[3] = "Supreme";
			groups[4] = "Elite";
			groups[5] = "Veteran";
			groups[6] = "Trusted";
			groups[7] = "Member";
			groups[8] = "Newcomer";
			groups[9] = "Newbie";
			groups[10] = "Guest";
			
			//Set the group formats.

			groupsFormats[0] = "&f[&4Owner&f] ";
			groupsFormats[1] = "&f[&cAdministrator&f] ";
			groupsFormats[2] = "&f[&0Master&f] ";
			groupsFormats[3] = "&f[&6Supreme&f] ";
			groupsFormats[4] = "&f[&dElite&f] ";
			groupsFormats[5] = "&f[&9Veteran&f] ";
			groupsFormats[6] = "&f[&3Trusted&f] ";
			groupsFormats[7] = "&f[&bMember&f] ";
			groupsFormats[8] = "&f[&2Newcommer&f] ";
			groupsFormats[9] = "&f[&aNewbie&f] ";
			groupsFormats[10] = "&f[&7Guest&f] ";
			
			//Set default timers.
			timeReqs[0] = -1;
			timeReqs[1] = -1;
			timeReqs[2] = 38880000;
			timeReqs[3] = 23328000;
			timeReqs[4] = 15552000;
			timeReqs[5] = 7776000;
			timeReqs[6] = 3628800;
			timeReqs[7] = 1814400;
			timeReqs[8] = 259200;
			timeReqs[9] = 86400;
			timeReqs[10] = 0;
			
			//Set the requirements.
			jobReqs[0] = -1;
			jobReqs[1] = -1;
			jobReqs[2] = 6;
			jobReqs[3] = 5;
			jobReqs[4] = 4;
			jobReqs[5] = 3;
			jobReqs[6] = 2;
			jobReqs[7] = 2;
			jobReqs[8] = 1;
			jobReqs[9] = 1;
			jobReqs[10] = 1;
			
			for (int i = 0; i < totalGroups; i++)
				config.set(groupPrefix + "GroupString." + i, groups[i] + "," + groupsFormats[i] + "," + timeReqs[i] + "," + jobReqs[i]);
			
			//Save group defaults
			FC_Rpg.plugin.saveConfig();
		}
		
		//Always load up groups.
		loadGroups();
	}
	
	public void loadGroups()
	{
		Group group;
		String groupString = "";
		groupList = new ArrayList<Group>();
		
		for (int i = 0; i < 100; i++)
		{
			try
			{
				groupString = config.getString(groupPrefix + "GroupString." + i);
				group = new Group(groupString);
				groupList.add(group);
			}
			catch (NullPointerException e)
			{
				continue;
			}
		}
	}
}












