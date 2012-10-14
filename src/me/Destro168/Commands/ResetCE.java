package me.Destro168.Commands;

import me.Destro168.Configs.GeneralConfig;
import me.Destro168.Configs.PlayerFileConfig;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCE implements CommandExecutor
{
	public ResetCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		//Variable declarations.
		final Player player = (Player) sender;
		RpgMessageLib msgLib = new RpgMessageLib(player);
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		PlayerFileConfig rpgPlayerFile;
		RpgPlayer rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		Player target;
		
		if (args[0].equals(""))
			args[0] = player.getName();
		
		rpgPlayerFile = new PlayerFileConfig(args[0]);
		
		//Only let active players use this command.
		if (rpgPlayerFile.getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		//Store target.
		target = Bukkit.getServer().getPlayer(args[0]);
		
		if (perms.isAdmin())
		{
			final RpgPlayer rpgPlayer2 = rpgPlayer;
			
			//Set to inactive
			rpgPlayer.getPlayerConfigFile().setDefaults();
			
			//Stop the players tasks.
			FC_Rpg.rpgManager.unregisterRpgPlayer(player);
			
			//Send confirmation message.
			msgLib.standardMessage("Successfully reset",rpgPlayer.getPlayerConfigFile().getName());
			
			if (target != null)
			{
				FC_Rpg.rpgManager.unregisterRpgPlayer(target);
				
				//Teleport the player to spawn
				Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
				{
					@Override
					public void run() 
					{
						GeneralConfig co = new GeneralConfig();
						Bukkit.getServer().getPlayer(rpgPlayer2.getPlayerConfigFile().getName()).teleport(co.getResetLocation());
					}
				}, 20);
			}
		}
		else
		{
			//We want to return if the player is attempting to reset somebody else.
			if (!rpgPlayerFile.getName().equals(player.getName()))
				return true;
			
			//Set to inactive
			rpgPlayerFile.setDefaults();
			
			//Send confirmation message.
			msgLib.standardMessage("You have successfully reset yourself.");
			
			//Unregister
			FC_Rpg.rpgManager.unregisterRpgPlayer(player);
			
			//Teleport the player to spawn
			Bukkit.getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run() 
				{
					GeneralConfig co = new GeneralConfig();
					player.teleport(co.getResetLocation());
				}
			}, 20);
		}
		
		return true;
	}
}





