package me.Destro168.Commands;

import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BigHelpCE implements CommandExecutor
{
	public BigHelpCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		RpgPlayer rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		
		//Only let active players use this command.
		if (rpgPlayer.getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		return msgLib.bigHelp();
    }
}
