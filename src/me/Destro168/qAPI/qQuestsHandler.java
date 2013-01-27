package me.Destro168.qAPI;
import java.util.logging.Logger;

import me.Destro168.FC_Rpg.Configs.PlayerConfig;
import me.quaz3l.qQuests.qQuests;
import me.quaz3l.qQuests.API.QuestAPI;
import me.quaz3l.qQuests.API.Requirements.qRequirement;

import org.bukkit.plugin.Plugin;

public class qQuestsHandler
{
	Plugin plugin;
	QuestAPI qAPI;

	public qQuestsHandler(Plugin pl)
	{
		this.plugin = pl;
	}

	public boolean setupQQuests()
	{
		qQuests qQuests = (qQuests) plugin.getServer().getPluginManager().getPlugin("qQuests");

		if (this.qAPI == null)
		{
			if (qQuests != null)
			{
				this.qAPI = qQuests.qAPI;
				return true;
			}
		}
		return false;
	}

	public void addRequirements()
	{
		try
		{
			Class.forName("me.quaz3l.qQuests.qQuests");
			
			this.qAPI.getRequirementHandler().addRequirement(new qRequirement()
			{
				@Override
				public boolean passedRequirement(String player, Object value)
				{
					PlayerConfig playerConfig = new PlayerConfig(player);
					try
					{
						if (Integer.parseInt(value.toString()) > playerConfig.getClassLevel()) // Player config level check here
							return false;
					} catch (NumberFormatException e)
					{
						Logger.getLogger(("Minecraft")).info("The requirement " + this.getName() + ", is NOT a number, it MUST be a number!");
					}
					return true;
				}

				@Override
				public boolean validate(Object value)
				{
					if (value == null)
					{
						return false;
					}
					try
					{
						Integer.parseInt(value.toString());
					} catch (NumberFormatException e)
					{
						return false;
					}
					return true;
				}

				@Override
				public String getName()
				{
					return "rpgLevelMin";
				}
			});
		} catch (ClassNotFoundException e) { }
		
		// Copy the above code from the comment "rpgLevelMin" to here, and edit to add more requirements
	}
}
