package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.permission.Permission;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.LoadedObjects.Group;
import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.FC_Suite_Shared.PermissionManager;

public class GroupConfig extends ConfigGod
{
	private List<Group> groupList;
	
	public List<Group> getGroups() { return groupList; }
	
	public GroupConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Groups");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If a past config wasn't created, create a new one.
		if (getVersion() < 0.23)
		{
			//Set the version.
			setVersion(0.23);
			
			//Variable Declarations
			PermissionManager pm = new PermissionManager(true);
			Permission permission = pm.getPermission();
			
			String[] groups = permission.getGroups();
			
			for (int i = 0; i < groups.length; i++)
				fcw.set(prefix + "GroupString." + i, groups[i] + ",&f[" + ColorLib.getRandomColor() + groups[i] + "&f] ,-1,-1");
			
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
				groupString = fcw.getString(prefix + "GroupString." + i);
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












