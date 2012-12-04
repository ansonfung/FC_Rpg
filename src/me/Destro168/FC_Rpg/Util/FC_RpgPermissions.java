package me.Destro168.FC_Rpg.Util;

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
		
		return isConsole;
	}
	
	public boolean hasInfiniteTickets()
	{
		return getCanUseCommand("FC_Rpg.inifiniteTickets");
	}
	
	public boolean isInfiniteDonator()
	{
		return getCanUseCommand("FC_Rpg.infiniteDonator");
	}
	
	public boolean commandClass()
	{
		return getCanUseCommand("FC_Rpg.command.class");
	}
	
	public boolean commandClassViewOther()
	{
		return getCanUseCommand("FC_Rpg.command.class.viewOther");
	}
	
	public boolean commandFAQ()
	{
		return getCanUseCommand("FC_Rpg.command.faq");
	}
	
	public boolean commandDonator()
	{
		return getCanUseCommand("FC_Rpg.command.donator");
	}
	
	public boolean commandG()
	{
		return getCanUseCommand("FC_Rpg.command.g");
	}
	
	public boolean commandH()
	{
		return getCanUseCommand("FC_Rpg.command.h");
	}
	
	public boolean commandHat()
	{
		return getCanUseCommand("FC_Rpg.command.hat");
	}

	public boolean commandJob()
	{
		return getCanUseCommand("FC_Rpg.command.job");
	}
	
	public boolean commandList()
	{
		return getCanUseCommand("FC_Rpg.command.list");
	}
	
	public boolean commandGuild()
	{
		return getCanUseCommand("FC_Rpg.command.guild");
	}

	public boolean commandPvp()
	{
		return getCanUseCommand("FC_Rpg.command.pvp");
	}
	
	public boolean commandReset()
	{
		return getCanUseCommand("FC_Rpg.command.reset");
	}
	
	public boolean commandModify()
	{
		return getCanUseCommand("FC_Rpg.command.modify");
	}
	
	public boolean commandRpg()
	{
		return getCanUseCommand("FC_Rpg.command.rpg");
	}
	
	public boolean commandSpell()
	{
		return getCanUseCommand("FC_Rpg.command.spell");
	}
	
	public boolean commandWarp()
	{
		return getCanUseCommand("FC_Rpg.command.warp");
	}
	
	public boolean commandBuff()
	{
		return getCanUseCommand("FC_Rpg.command.buff");
	}
	
	public boolean commandBuffSelf()
	{
		return getCanUseCommand("FC_Rpg.command.buff.self");
	}
	
	public boolean commandWorld()
	{
		return getCanUseCommand("FC_Rpg.command.world");
	}
	
	private boolean getCanUseCommand(String checkPerm)
	{
		if (isAdmin() == true)
			return true;
		
		if (permission.has(player, checkPerm))
			return true;
		
		return false;
	}
}




















