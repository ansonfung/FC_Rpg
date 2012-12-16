package me.Destro168.FC_Rpg.Util;

import java.util.List;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.Messaging.BroadcastLib;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class RpgBroadcast extends BroadcastLib
{
	public RpgBroadcast() { }
	
	public boolean rpgBroadcast(String msg)
	{
		List<World> rpgWorlds = FC_Rpg.worldConfig.getRpgWorlds();
		
		if (rpgWorlds == null || rpgWorlds.size() <= 0)
			return true;
		
		for (World rpgWorld : rpgWorlds)
		{
			for (Player p : rpgWorld.getPlayers())
			{
				p.sendMessage(cLib.parseCustom(cm.primaryColor, cm.broadcastTag + msg));
			}
		}
		
		return true;
	}
}
