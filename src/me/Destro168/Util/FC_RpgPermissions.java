package me.Destro168.Util;

import org.bukkit.entity.Player;

import me.Destro168.FC_Suite_Shared.PermissionManager;

public class FC_RpgPermissions extends PermissionManager
{
	public FC_RpgPermissions(Player player_) 
	{
		super(player_);
		
		setupPermissions();
	}
	
	public FC_RpgPermissions(boolean isConsole_) 
	{
		super(isConsole_);
	}
	
	public String[] getPlayerGroups()
	{
		return permission.getPlayerGroups(player);
	}
	
	//Add the player to the new group.
	public void addPlayerGroup(String newGroup)
	{
		permission.playerAddGroup(player, newGroup);
	}
	
	//Set the player Group to something.
	public void setPlayerGroup(String newGroup)
	{
		String[] PlayerGroups = getPlayerGroups();
		
		for (String group : PlayerGroups)
			permission.playerRemoveGroup(player, group);
		
		addPlayerGroup(newGroup);
	}
	
	//Remove the player from a group.
	public void removePlayerGroup(String group)
	{
		permission.playerRemoveGroup(player, group);
	}
	
	//Check if the player is in a group
	public boolean inGroup(String group)
	{
		return permission.playerInGroup(player, group);
	}
	
	public boolean isAdmin()
	{
		if (isGlobalAdmin() == true)
			return true;
		
		if (permission.has(player, "FC_Rpg.admin"))
			return true;
		
		return false;
	}
	
	public boolean commandHat()
	{
		if (isAdmin() == true)
			return true;
		
		if (permission.has(player, "FC_Rpg.hat"))
			return true;
		
		return false;
	}
}




















