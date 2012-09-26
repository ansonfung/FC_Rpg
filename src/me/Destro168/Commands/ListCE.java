package me.Destro168.Commands;

import java.util.ArrayList;
import java.util.List;

import me.Destro168.Messaging.MessageLib;
import me.Destro168.Util.FC_RpgPermissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ListCE implements CommandExecutor
{
	public ListCE() { }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable Declarations
		Player player = (Player) sender;
		MessageLib msgLib = new MessageLib(player);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		List<String> ownerList = new ArrayList<String>();
		List<String> adminList = new ArrayList<String>();
		List<String> masterList = new ArrayList<String>();
		List<String> supremeList = new ArrayList<String>();
		List<String> eliteList = new ArrayList<String>();
		List<String> veteranList = new ArrayList<String>();
		List<String> trustedList = new ArrayList<String>();
		List<String> memberList = new ArrayList<String>();
		List<String> newcomerList = new ArrayList<String>();
		List<String> newbieList = new ArrayList<String>();
		List<String> guestList = new ArrayList<String>();
		
		for (Player onlinePlayer: Bukkit.getOnlinePlayers())
		{
			perms = new FC_RpgPermissions(onlinePlayer);
			
			if (perms.inGroup("Owner"))
				ownerList.add(onlinePlayer.getName());
			
			else if (perms.inGroup("Administrator"))
				adminList.add(onlinePlayer.getName());

			else if (perms.inGroup("Master"))
				masterList.add(onlinePlayer.getName());
			
			else if (perms.inGroup("Supreme"))
				supremeList.add(onlinePlayer.getName());
			
			else if (perms.inGroup("Elite"))
				eliteList.add(onlinePlayer.getName());
			
			else if (perms.inGroup("Veteran"))
				veteranList.add(onlinePlayer.getName());
			
			else if (perms.inGroup("Trusted"))
				trustedList.add(onlinePlayer.getName());

			else if (perms.inGroup("Member"))
				memberList.add(onlinePlayer.getName());

			else if (perms.inGroup("Newcomer"))
				newcomerList.add(onlinePlayer.getName());
			
			else if (perms.inGroup("Newbie"))
				newbieList.add(onlinePlayer.getName());
			
			else
				guestList.add(onlinePlayer.getName());
		}
		
		//Begin displaying the information.
		msgLib.standardHeader("Total Connected Players: " + ChatColor.GREEN + ChatColor.BOLD + Bukkit.getOnlinePlayers().length + ChatColor.GRAY + ChatColor.BOLD + "/" +
				ChatColor.RED + ChatColor.BOLD + Bukkit.getServer().getMaxPlayers());
		
		//Send the new standard message.
		if (guestList.size() > 0)
			msgLib.standardMessage(ChatColor.GRAY + "Guest",specialConversion(guestList));
		
		if (newbieList.size() > 0)
			msgLib.standardMessage(ChatColor.GREEN + "Newbie",specialConversion(newbieList));
		
		if (newcomerList.size() > 0)
			msgLib.standardMessage(ChatColor.DARK_GREEN + "Newcomer",specialConversion(newcomerList));
		
		if (memberList.size() > 0)
			msgLib.standardMessage(ChatColor.AQUA + "Member",specialConversion(memberList));
		
		if (trustedList.size() > 0)
			msgLib.standardMessage(ChatColor.DARK_AQUA + "Trusted",specialConversion(trustedList));
		
		if (veteranList.size() > 0)
			msgLib.standardMessage(ChatColor.BLUE + "Veteran",specialConversion(veteranList));
		
		if (eliteList.size() > 0)
			msgLib.standardMessage(ChatColor.LIGHT_PURPLE + "Elite",specialConversion(eliteList));
		
		if (supremeList.size() > 0)
			msgLib.standardMessage(ChatColor.GOLD + "Supreme",specialConversion(supremeList));
		
		if (masterList.size() > 0)
			msgLib.standardMessage(ChatColor.BLACK + "Master",specialConversion(masterList));
		
		if (adminList.size() > 0)
			msgLib.standardMessage(ChatColor.RED + "Admin",specialConversion(adminList));
		
		if (ownerList.size() > 0)
			msgLib.standardMessage(ChatColor.DARK_RED + "Owner",specialConversion(ownerList));
		
		return true;
	}
	
	public String returnCombinedNames(List<String> names)
	{
		String combinedNames = "";
		
		if (names.size() > 0)
		{
			for (int i = 0; i < names.size(); i++)
			{
				if (i == names.size() - 1)
					combinedNames += names.get(i);
				else
					combinedNames += names.get(i) + ", ";
			}
		}
		else
			combinedNames = "[None Online Atm]";
		
		return combinedNames;
	}
	
	protected String specialConversion(List<String> msg)
	{
		//Variable Declarations
		String message = "";
		int alternateState = 0;
		
		//We want to alternate the colors for the standard  message.
		for (int i = 0; i < msg.size(); i++)
		{
			if (msg.get(i) != null)
			{
				if (alternateState == 0)
				{
					message += ChatColor.WHITE + "" + msg.get(i);
					alternateState = 1;
				}
				else if (alternateState == 1)
				{
					message += ChatColor.GRAY + ", " + msg.get(i);
					alternateState = 2;
				}
				else if (alternateState == 2)
				{
					message += ChatColor.WHITE + ", " + msg.get(i);
					alternateState = 1;
				}
			}
		}
		
		return message;
	}
}
