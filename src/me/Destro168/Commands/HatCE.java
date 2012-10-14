package me.Destro168.Commands;

import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.entity.Player;

public class HatCE implements CommandExecutor
{
	public HatCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable declarations.
		Player player;
		FC_RpgPermissions perms;
		RpgMessageLib msgLib;
		RpgPlayer rpgPlayer;
		
		if (sender instanceof Player)
		{
			player = (Player) sender;
			perms = new FC_RpgPermissions(player);
			msgLib = new RpgMessageLib(player);
			rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
			
		}
		else if (sender instanceof ColouredConsoleSender)
		{
			FC_Rpg.plugin.getLogger().info("The console can't wear hats.");
			return false;
		}
		else
		{
			FC_Rpg.plugin.getLogger().info("Unknown command sender, returning ban command.");
			return false;
		}
		
		//Only let active players use this command.
		if (rpgPlayer.getPlayerConfigFile().getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		if (rpgPlayer.getPlayerConfigFile().isDonator() || perms.commandHat())
		{
			if (player.getInventory().getHelmet() != null)
				Bukkit.getServer().getWorld(player.getWorld().getName()).dropItem(player.getLocation(), player.getInventory().getHelmet());
			
			player.getInventory().setHelmet(player.getItemInHand());
			player.setItemInHand(null);
		}
		else
			return msgLib.errorNoPermission();
		
		return true;
    }
}
