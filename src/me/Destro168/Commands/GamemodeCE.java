package me.Destro168.Commands;

import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCE implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		
		if (perms.isAdmin() == true)
		{
			if (player.getGameMode() == GameMode.SURVIVAL)
				player.setGameMode(GameMode.CREATIVE);
			else
				player.setGameMode(GameMode.SURVIVAL);
			
			return msgLib.successCommand();
		}
		
		return msgLib.errorNoPermission();
    }
}
