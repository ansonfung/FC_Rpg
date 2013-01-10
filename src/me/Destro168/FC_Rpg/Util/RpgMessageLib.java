package me.Destro168.FC_Rpg.Util;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.FaqConfig;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgMessageLib extends MessageLib
{
	Player player;
	RpgPlayer rpgPlayer;
	FC_RpgPermissions perms;

	public RpgMessageLib(CommandSender sender, Player player_)
	{
		super(sender);

		// Store player
		player = player_;

		perms = new FC_RpgPermissions(player);

		// Store rpgPlayer
		rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(player);
	}

	public RpgMessageLib(CommandSender sender)
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

	public boolean helpRpg()
	{
		standardHeader("FC_Rpg Command Help!");

		if (perms.commandFAQ())
			standardMessage("/faq", "View Server Frequently Asked Questions.");

		if (perms.commandClass())
			standardMessage("/class", "Help for classes.");

		if (perms.commandJob())
			standardMessage("/job", "Help for jobs.");

		if (perms.commandGuild())
			standardMessage("/guild", "Help for guilds.");

		if (perms.commandSpell())
			standardMessage("/spell", "Help for spells");

		if (rpgPlayer != null)
		{
			if (rpgPlayer.getPlayerConfig().getHasAlchemy())
				standardMessage("/alchemy", "Help for alchemy");

			if (rpgPlayer.isDonatorOrAdmin())
				standardMessage("/donator", "Help for donators.");
		}

		if (perms.commandReset())
			standardMessage("/reset", "Reset Your FC_RPG Character!");

		if (perms.commandPvp())
			standardMessage("/pvp", "Help for pvp events.");

		if (perms.commandPlayers())
			standardMessage("/players", "See who is online.");

		if (perms.commandBuff())
			standardMessage("/buff", "Help with buffs.");

		if (perms.isAdmin())
		{
			standardMessage("[A] /w", "Help with warps.");
			standardMessage("[A] /dungeon", "Help for dungeons.");
			standardMessage("[A] /de", "Help for dungeon setup.");
			standardMessage("[A] /modify", "Help for modify command.");
			standardMessage("[A] /world", "Help for world command.");
			standardMessage("[A] /radmin", "Admin Commands.");
		}

		return true;
	}

	public boolean helpFaq()
	{
		// Variable Declarations
		FaqConfig fm = new FaqConfig();
		String faq;
		String topics = "";
		String seperator = "";
		int breakPoint = 0;

		// Create topic list
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
		standardMessage("/faq [topic]", "Learn more about a FAQ.");
		standardMessage("FAQ Topics", topics);

		if (perms.isAdmin())
		{
			standardMessage("[A] /faq new [displayTag]", "Add a faq.");
			standardMessage("[A] /faq del [displayTag] <line> <half>", "Delete a faq.");
			standardMessage("[A] /faq eProperty [displayTag] [modifiable] [new value]", "Edit a faq's property.");
			standardMessage("[A] /faq eLine [displayTag] <line> <half> [new value]", "Edit a faq's lines.");
		}

		return true;
	}

	public boolean helpClass()
	{
		if (!perms.commandClass())
			return this.errorNoPermission();

		// Display header and commands.
		standardHeader("Class Commands");
		standardMessage("/class view [name]", "Show FC_RPG Character Information.");
		standardMessage("/class list", "View a list of all server classes.");

		if (rpgPlayer != null)
		{
			standardMessage("/class spec [stat] [amount]", "Distribute Stat Points.");
			standardMessage("- Tip", "If you have stat points, use this command.");
			standardMessage("/class allocate [<on>,<off>]", "Set auto stat distribution.");

			// Variable Declaration
			boolean hasInfiniteTickets = perms.hasInfiniteTickets();

			if (rpgPlayer.hasClassChangeTicket() == true || hasInfiniteTickets)
			{
				standardMessage("/class switch [num]", "Use a class ticket to switch to a class specified by number or name.");

				// Set the ticket count.
				if (hasInfiniteTickets)
					standardMessage("- You have [Infinite] tickets remaining.");
				else
					standardMessage("- You have " + rpgPlayer.getPlayerConfig().getClassChangeTickets() + " tickets remaining.");
			}
		}

		return true;
	}

	public boolean helpPvp()
	{
		standardHeader("Pvp Commands");
		standardMessage("/pvp join", "Join a pvp event.");
		standardMessage("/pvp leave", "Leave a pvp event.");
		standardMessage("/pvp list", "List team players for a pvp event.");

		if (perms.isAdmin())
		{
			standardMessage("[A] /pvp kick [name]", "Remove a player from a pvp event.");
			standardMessage("[A] /pvp new", "Force start a new pvp event.");
			standardMessage("[A] /pvp end", "Force end a pvp event.");
		}

		return true;
	}

	public boolean helpSpell()
	{
		standardHeader("Spell Commands");
		standardMessage("/spell list", "Lists all spells and spell points");
		standardMessage("/spell bind [spellName]", "Binds a spell to your held item.");
		standardMessage("/spell upgrade [spellName]", "Upgrade spells with spell points.");
		standardMessage("/spell cast [spellName]", "Cast your self-buff spells.");
		standardMessage("/spell reset", "Removes the spell bound to your current item.");
		standardMessage("/spell autocast", "Toggles spell autocast on/off.");

		if (rpgPlayer != null)
			infiniteMessage("Automatic Casting Currently: ", rpgPlayer.getPlayerConfig().getAutoCast() + "");

		return true;
	}

	public boolean helpAlchemy()
	{
		if (rpgPlayer == null || !rpgPlayer.getPlayerConfig().getHasAlchemy())
			return true;

		standardHeader("Alchemy Commands");
		standardMessage("/alchemy", "Help for alchemy");
		standardMessage("/alchemy [list] <startpoint>", "List items purchasable with Arcanium.");
		standardMessage("/alchemy [buy] [num] [count]", "Buy items from the alchemy list.");
		standardMessage("/alchemy [smelt] <all>", "Break down an item in your hand or all of your items into Arcanium.");
		infiniteMessage("You currently have ", FC_Rpg.df.format(rpgPlayer.getPlayerConfig().getArcanium()), " Arcanium.");

		return true;
	}

	public boolean helpGuild()
	{
		if (!perms.commandGuild())
			return this.errorNoPermission();

		standardHeader("Guild Commands");
		standardMessage("/guild list [page]", "See a list of guilds.");
		standardMessage("/guild create [guildName]", "Create a guild.");
		standardMessage("/guild join [guildName]", "Join a guild.");
		standardMessage("/guild kick [member]", "Kick a guild member.");
		standardMessage("/guild leave", "Leave your guild.");
		standardMessage("/guild [<open>,<close>]", "Open Or Close Your Guild.");
		standardMessage("/guild info [guildName]", "See a guilds info.");
		standardMessage("[A] /guild delete [guildName]", "Delete a guild.");
		standardMessage("[A] /guild [<open>,<close>] [name]", "Open Or Close A Guild.");

		return true;
	}

	public boolean helpJob()
	{
		if (!perms.commandJob())
			return this.errorNoPermission();

		standardHeader("Job Commands");
		standardMessage("/job view [name]", "Shows Job Information");

		if (rpgPlayer != null)
			standardMessage("/job promote", "Promotes you to next job rank!");

		return true;
	}

	public boolean helpBuff()
	{
		standardHeader("Buff Commands");
		standardMessage("/buff random", "Apply a random buff to yourself for &q" + FC_Rpg.generalConfig.getBuffCommandCost() + "&q");

		if (perms.isAdmin())
		{
			standardMessage("[A] /buff random [name] [effect] [length] [level]", "Apply a specific buff with specified characteristics to yourself.");
			standardMessage("[A] /buff all", "Buff everybody online.");
			standardMessage("[A] /buff clear [name]", "Clear target of buffs.");
			standardMessage("[A] /buff max [name]", "Put all buffs on target.");
		}

		return true;
	}

	public boolean helpDonator()
	{
		if (rpgPlayer == null)
			return errorCreateCharacter();

		if (!rpgPlayer.isDonatorOrAdmin())
			return errorNoPermission();

		standardHeader("Donator Commands");

		if (FC_Rpg.generalConfig.getDonatorsCanHat() || perms.commandHead())
			standardMessage("/head", "Wear a block on your head!");

		standardMessage("/donator respecialize", "Redistribute stat points on the fly!");
		standardMessage("- Have a suggestion? Ask Destro168!");

		return true;
	}

	public boolean helpWarp()
	{
		if (!perms.isAdmin())
			return errorNoPermission();

		standardHeader("Warp Commands");
		standardMessage("/w list [start]", "list warps from a startpoint.");
		standardMessage("/w info [name]", "See detailed information about a warp.");
		standardMessage("/w add [name]", "Add a warp at your position.");
		standardMessage("/w del [name]", "Add a warp.");
		standardMessage("/w tp [name]", "Teleport to a warp.");
		standardMessage("/w edit [name] [modifiable] [new val]", "Edit warp.");
		standardMessage("/w new [name]", "Add a warp to warp config.");
		standardMessage("Modifiables", "name, description, cost, welcome, exit, donator, admin");

		return true;
	}

	public boolean helpDungeon()
	{
		if (!perms.isAdmin())
			return errorNoPermission();

		standardHeader("Dungeon Help");
		standardMessage("/dungeon list", "List dungeons.");
		standardMessage("/dungeon print", "Prints out information for dungeon setup.");
		standardMessage("/dungeon start [num]", "Start a dungeon.");
		standardMessage("/dungeon stop [num]", "Stop a dungeon.");
		standardMessage("/dungeon check [num]", "Check if any mobs are left to update loot chest.");
		standardMessage("/dungeon kick [num] [name]", "Kick somebody from a dungeon.");;
		secondaryMessage("Type /de to see extremely advanced config editing commands for creating dungeons. <- 100% Perfect FC_Rpg Mastery Required.");
		
		return true;
	}

	public boolean helpDungeonDE()
	{
		if (!perms.isAdmin())
			return errorNoPermission();

		standardHeader("Dungeon Definition Commands");
		standardMessage("/de info [num] <spawnbox, treasureStart, treasureEnd>");
		standardMessage("/de new [num]", "Creates empty dungeon in dungeon config.");
		standardMessage("/de delete [num]", "Deletes a dungeon.");
		standardMessage("/de name [num]");
		standardMessage("/de spawnbox [num] [add, remove [num], swap [num1] [num2], spawnChance [num], mobList [<Name List>,<\"default\">]]");
		standardMessage("/de lobby [num]");
		standardMessage("/de playerStart [num]", "NOT 'START'! Careful!");
		standardMessage("/de playerExit [num]", "NOT 'END'! Careful!");
		standardMessage("/de bossSpawn [num]");
		standardMessage("/de timerJoin [num] [value]");
		standardMessage("/de timerEnd [num] [value]");
		standardMessage("/de treasureStart [num] <add, remove [num]>");
		standardMessage("/de treasureEnd [num] <add, remove [num]>");
		standardMessage("/de fee [num] [value]");
		standardMessage("/de lmin [num] [value]");
		standardMessage("/de lmax [num] [value]");
		standardMessage("/de spawnCount [num] [index]");
		standardMessage("/de spawnchance [num] [index] [value]");

		return true;
	}

	public boolean helpModify()
	{
		if (!perms.isAdmin())
			return errorNoPermission();

		standardHeader("Modify Command ~ Modifiables");
		standardMessage("/modify [name] [modifiable] [newValue]", "Modify player settings in-game.");
		secondaryMessage("The following is a list of catagorized modifiables.");
		standardMessage("Stats", "strength, constitution, intelligence, magic, all, stats, ");
		standardMessage("Levels/Exp", "level, addlevel, exp, addexp");
		standardMessage("Donator", "donator, tickets");
		standardMessage("Play Time", "seconds, addseconds");
		standardMessage("Misc", "class, jobrank, spellPoint, prefix (use 'none' to reset'), arcanium");
		secondaryMessage("Please note that most 's' characters can be ommited and there are many aliases for each modifiable.");

		return true;
	}

	public boolean helpWorld()
	{
		if (!perms.isAdmin())
			return errorNoPermission();

		standardHeader("World Commands");
		standardMessage("/world list", "List all worlds on your server.");
		standardMessage("/world tp [worldname]", "Teleport to a worlds spawn.");
		standardMessage("/world new [worldname]", "Add a world to world config.");
		standardMessage("/world levelone", "Set a worlds level one to your loc.");
		standardMessage("/world spawn [worldname] [x] [y] [z] [yaw] [pitch]", "Change a worlds spawn to information you specify.");
		standardMessage("/world spawn here", "Change a worlds spawn to your loc.");

		return true;
	}

	public boolean helpRAdmin()
	{
		if (!perms.isAdmin())
			return true;

		standardHeader("Admin Commands");
		standardMessage("/rAdmin event [type]", "loot,exp,off");
		standardMessage("/rAdmin go [u [amount],d [amount],(coords)]", "Teleport anywhere.");
		standardMessage("/rAdmin tp [name] <name>", "Teleport to anybody. Teleport somebody to somebody.");
		standardMessage("/rAdmin spawn [entity] [level]", "Spawn a mob.");
		standardMessage("/rAdmin expMult [x]", "Change current global exp multiplier. Currently: " + FC_Rpg.balanceConfig.getGlobalExpMultiplier());
		standardMessage("/rAdmin wall", "Creates wall with all classes/jobs/start sign.");
		standardMessage("/rAdmin sudo [name]", "Make somebody say anything/use any command.");
		standardMessage("/rAdmin taskwipe", "Stops all tasks of FC_Rpg.");
		standardMessage("/rAdmin stop", "Disables the plugin FC_Rpg.");
		standardMessage("/rAdmin reload", "Reloads FC_Rpg.");
		standardMessage("/h,/g,/hg,/gh [name]", "Heal/Gamemode single/hybrid commands.");

		return true;
	}
}
