package me.Destro168.Util;

import me.Destro168.Configs.FaqConfig;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Messaging.MessageLib;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.entity.Player;

public class RpgMessageLib extends MessageLib 
{
	Player player;
	RpgPlayer rpgPlayer;
	FC_RpgPermissions perms;
	
	public RpgMessageLib(Player player_)
	{
		super(player_);
		
		//Store player
		player = player_;
		
		perms = new FC_RpgPermissions(player);
		
		//Store rpgPlayer
		rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
	}
	
	public RpgMessageLib(ColouredConsoleSender sender)
	{
		super(sender);
		
		perms = new FC_RpgPermissions(true);
		
		rpgPlayer = null;
	}
	
	public boolean errorMaxJob()
	{
		standardMessage("Unable to complete action because you are max job.");
		return true;
	}
	
	public boolean errorLevelToLow()
	{
		standardMessage("Unable to complete action because your level is too low.");
		return true;
	}
	
	public boolean errorLevelOutOfRange()
	{
		standardMessage("Unable to complete action because your level isn't in the proper range.");
		return true;
	}
	
	public boolean errorCreateCharacter()
	{
		standardMessage("A class must be chosen first!");
		return true;
	}
	
	public double errorOutOfMana()
	{
		standardMessage("Spell failed to cast because you don't have enough mana!");
		return 0;
	}
	
	public boolean errorOutOfManaBoolean()
	{
		standardMessage("Spell failed to cast because you don't have enough mana!");
		return true;
	}
	
	public boolean errorJobLevelTooLow()
	{
		standardMessage("Your job level is too low!");
		return true;
	}

	public boolean errorClassLevelTooLow()
	{
		standardMessage("Your class level is too low!");
		return true;
	}
	
	public boolean errorNotDonator()
	{
		standardMessage("You're not a donator!");
		return true;
	}
	
	public boolean errorConsoleCantUseCommand()
	{
		standardMessage("The Console Is Unable To Use This Command.");
		return true;
	}
	
	public boolean errorWarpDoesNotExist()
	{
		standardMessage("Warp specified does not exist.");
		return true;
	}
	
	public boolean helpFaq()
	{
		//Variable Declarations
		FaqConfig fm = new FaqConfig();
		String faq;
		String topics = "";
		String seperator = "";
		int breakPoint = 0;
		
		//Create topic list
		for (int i = 0; i < 10000; i++)
		{
			faq = fm.getDisplayTag(i);
			
			if (faq != null)
			{
				topics += seperator + faq;
				seperator = ", ";
			}
			else
			{
				breakPoint++;
				
				if (breakPoint > 50)
					break;
			}
		}
		
		standardHeader("Server Faq Topics!");
		standardMessage("/faq [topic]","Learn more about a FAQ.");
		standardMessage("FAQ Topics",topics);
		
		if (perms.isAdmin())
		{
			standardMessage("[A] /faq add [displayTag]","Add a faq.");
			standardMessage("[A] /faq del [displayTag] <line> <half>","Delete a faq.");
			standardMessage("[A] /faq eProperty [displayTag] [modifiable] [new value]","Edit a faq's property.");
			standardMessage("[A] /faq eLine [displayTag] <line> <half> [new value]","Edit a faq's lines.");
		}
		
		return true;
	}
	
	public boolean helpRpg()
	{
		standardHeader("Help for Everything!");
		standardError("Constantly Asking Questions In Help = Mute");
		standardMessage("/faq", ChatColor.RED + "Important Server Information! READ!");
		standardMessage("/class","Help for classes.");
		standardMessage("/job","Help for jobs.");
		standardMessage("/guild","Help for guilds.");
		standardMessage("/spell","Help for spells");
		standardMessage("/pvp","Help for pvp events.");
		standardMessage("/reset","Reset Your Character! (Infinite/You Keep Money)");
		standardMessage("/list","See who is online.");
		standardMessage("/buff","Help with buffs.");
		
		if (perms.isAdmin())
			standardMessage("/donator","Help for donators.");
		else if (rpgPlayer.isDonatorOrAdmin())
			standardMessage("/donator","Help for donators.");
		
		if (perms.isAdmin())
		{
			standardMessage("[A] /w", "Help with warps.");
			standardMessage("[A] /dungeon","Help for dungeons.");
			standardMessage("[A] /modify","Help for modify command.");
			standardMessage("[A] /rpg admin","Admin Commands.");
		}
		
		if (perms.isOwner())
			standardMessage("[A] /rpg owner","Owner Commands!");
		
		return true;
	}
	
	public boolean helpDungeon()
	{
		if (!perms.isAdmin())
			return errorNoPermission();
		
		standardHeader("Dungeon Help ~ 0 = D1, 1 = D2");
		standardMessage("/dungeon list","List dungeons.");
		standardMessage("/dungeon info [num]","See information regarding a specific dungeon.");
		standardMessage("/dungeon start [num]","Start a dungeon.");
		standardMessage("/dungeon stop [num]","Stop a dungeon.");
		standardMessage("/dungeon check [num]","Check if any mobs are left to update loot chest.");
		standardMessage("/dungeon kick [num] [name]","Kick somebody from a dungeon.");
		standardMessage("/dungeon define [num]","After selecting two dungeon points use this to define a dungeon.");
		secondaryMessage("Want to define dungeons in-game? Use /d9");
		
		return true;
	}
	
	public boolean helpDungeonDefinition()
	{
		standardHeader("Dungeon Definition Commands.");
		standardHeader("BE CAREFUL ~ READ PLUGIN HELP!");
		standardMessage("/dungeon name [num]");
		standardMessage("/dungeon ranges [num]");
		standardMessage("/dungeon lobby [num]");
		standardMessage("/dungeon playerStart [num]","NOT 'START'! Careful!");
		standardMessage("/dungeon playerEnd [num]","NOT 'END'! Careful!");
		standardMessage("/dungeon boss [num]");
		standardMessage("/dungeon treasure [num]");
		standardMessage("/dungeon ranges [num]");
		standardMessage("/dungeon cost [num] [value]");
		standardMessage("/dungeon lmin [num] [value]");
		standardMessage("/dungeon lmax [num] [value]");
		standardMessage("/dungeon spawnCount [num] [index]");
		standardMessage("/dungeon spawnchance [num] [index] [value]");
		
		return true;
	}
	
	public boolean helpPvp()
	{
		standardHeader("Pvp Commands");
		standardMessage("/pvp join","Join a pvp event.");
		standardMessage("/pvp leave","Leave a pvp event.");
		standardMessage("/pvp list","List team players for a pvp event.");
		
		if (perms.isAdmin())
		{
			standardMessage("[A] /pvp kick [name]","Remove a player from a pvp event.");
			standardMessage("[A] /pvp new","Force start a new pvp event.");
			standardMessage("[A] /pvp end","Force end a pvp event.");
		}
		
		return true;
	}
	
	public boolean helpSpell()
	{
		standardHeader("Spell Commands");
		standardMessage("/spell list","Lists all spells and spell points");
		standardMessage("/spell bind [spellName]","Binds a spell to your held item.");
		standardMessage("/spell upgrade [spellName]","Upgrade spells with spell points.");
		standardMessage("/spell cast [spellName]","Cast your self-buff spells.");
		standardMessage("/spell autocast","Toggles spell autocast on/off.");
		standardMessage("/spell reset","Removes the spell bound to your current item.");
		return true;
	}
	
	public boolean helpGuild()
	{
		standardHeader("Guild Commands");
		standardMessage("/guild list [page]","See a list of guilds.");
		standardMessage("/guild create [guildName]","Create a guild.");
		standardMessage("/guild join [guildName]","Join a guild.");
		standardMessage("/guild kick [member]","Kick a guild member.");
		standardMessage("/guild leave","Leave your guild.");
		standardMessage("/guild [<open>,<close>]","Open Or Close Your Guild.");
		standardMessage("/guild info [guildName]","See a guilds info.");
		standardMessage("[A] /guild delete [guildName]","Delete a guild.");
		standardMessage("[A] /guild [<open>,<close>] [name]","Open Or Close A Guild.");
		
		return true;
	}

	public boolean helpClass()
	{
		//Display header and commands.
		standardHeader("Class Commands");
		standardMessage("/class view [name]","Show Character Information");
		standardMessage("/class list","View a list of all server classes.");
		
		if (rpgPlayer != null)
		{
			standardMessage("/class spec [stat] [amount]","Distribute Stat Points");
			standardMessage("- Tip","If you have stat points, use this command.");
			standardMessage("/class allocate [on,off]","Set auto stat distribution.");
			
			if (rpgPlayer.hasClassChangeTicket() == true || perms.isAdmin())
			{
				standardMessage("/class switch [new class name]","Use a class ticket to switch class.");
				
				//Set the ticket count.
				if (perms.isAdmin())
					standardMessage("- You have [Infinite] tickets remaining.");
				else
					standardMessage("- You have " + rpgPlayer.getPlayerConfig().getClassChangeTickets() + " tickets remaining.");
			}
		}
		
		return true;
	}
	
	public boolean helpJob()
	{
		standardHeader("Job Commands");
		standardMessage("/job view [name]","Shows Job Information");
		
		if (rpgPlayer != null)
			standardMessage("/job promote","Promotes you to next job rank!");
		
		return true;
	}
	
	public boolean helpBuff()
	{
		standardHeader("Buff Commands");
		standardMessage("/buff self","Apply a random buff to yourself.");
		
		if (perms.isAdmin())
		{
			standardMessage("[A] /buff self [name] [length] [level]","Apply a specific buff with specified characteristics to yourself.");
			standardMessage("[A] /buff all","Buff everybody online.");
			standardMessage("[A] /buff clear [name]","Clear target of buffs.");
		}
		
		return true;
	}
	
	public void helpDonator()
	{
		if (rpgPlayer != null)
		{
			if (rpgPlayer.isDonatorOrAdmin())
			{
				standardHeader("Donator Commands");
				standardMessage("/hat", "Wear a block like a hat!");
				standardMessage("/donator respecialize", "Redistribute stat points on the fly!");
				standardMessage("- Have a suggestion? Ask Destro168!");
			}
		}
	}
	
	public boolean helpWarp()
	{
		if (!perms.isAdmin())
			return errorNoPermission();
		
		standardHeader("Warp Commands");
		standardMessage("/w list [start]","list warps from a startpoint.");
		standardMessage("/w info [name]","See detailed information about a warp.");
		standardMessage("/w add [name]","Add a warp at your position.");
		standardMessage("/w del [name]","Add a warp.");
		standardMessage("/w tp [name]","Teleport to a warp.");
		standardMessage("/w edit [name] [modifiable] [new val]","Edit warp.");
		standardMessage("Modifiables","name, description, cost, welcome, exit, donator, admin");
		
		return true;
	}
	
	public boolean helpModify()
	{
		if (!perms.isAdmin())
			return true;
		
		standardHeader("Modify Command ~ Modifiables");
		standardMessage("/modify [name] [modifiable] [newValue]", "Modify player settings in-game.");
		secondaryMessage("The following is a list of catagorized modifiables.");
		standardMessage("Stats", "strength, constitution, intelligence, magic, all, stats, ");
		standardMessage("Levels/Exp","level, addlevel, exp, addexp");
		standardMessage("Donator","donator, tickets");
		standardMessage("Play Time","seconds, addseconds");
		standardMessage("Misc","class, jobrank, spellPoint, prefix (use 'none' to reset')");
		secondaryMessage("Please note that most 's' characters can be ommited and there are many aliases for each modifiable.");
		
		return true;
	}
	
	public boolean helpAdmin()
	{
		if (!perms.isAdmin())
			return true;
		
		standardHeader("Admin Commands ~ BE CAREFUL");
		standardMessage("/rpg event [type]","loot,exp,off");
		standardMessage("/h - /g - /hg - /gh [name]","Heal/Gamemode single/hybrid commands.");
		standardMessage("/rpg listworld(s)","List all worlds on your server.");
		standardMessage("/rpg go [u [amount] | d [amount] | (coords)]","Teleport anywhere.");
		standardMessage("/rpg tpworld [worldname]","Teleport to a worlds spawn.");
		standardMessage("/rpg expMult [x]","Change current global exp multiplier. Currently: " + FC_Rpg.generalConfig.getGlobalExpMultiplier());
		
		return true;
	}
	
	public boolean helpOwner()
	{
		if (!perms.isOwner())
			return true;
	
		standardHeader("Owner Only commands");
		standardMessage("/rpg wall","Creates wall with all classes/jobs/start sign.");
		standardMessage("/rpg spawn [worldname] [x] [y] [z] [yaw] [pitch]","Change a worlds spawn");
		standardMessage("/rpg spawn here","Change a worlds spawn");
		standardMessage("/rpg levelone","Change mob level one spawnpoint to your loc.");
		standardMessage("/rpg tp [name]","Bypassed Player Tp.");
		standardMessage("/rpg sudo","Make somebody say anything/use any command.");
		
		return true;
	}
}












