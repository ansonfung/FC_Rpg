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
		
		standardMessage("/rpg help [page]", "View general help for FC_Rpg.");
		
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
			if (rpgPlayer.playerConfig.getHasAlchemy())
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
			standardMessage("[A] /realm", "Help for realm command.");
			standardMessage("[A] /radmin", "Admin Commands.");
		}

		return true;
	}
	
	public boolean displayRpgHelp(String strPage)
	{
		int page;
		
		try { page = Integer.valueOf(strPage); } catch (NumberFormatException e) { page = 1; strPage = "1"; }
		
		switch (page)
		{
			case 1:
				standardHeader("FC_Rpg Help Main ~ Page " + strPage);
				standardMessage("These documents are here to help players explain how to use some of the features of FC_Rpg. FC_Rpg is an rpg plugin designed to bring an RPG experience to servers!" +
						"The following is a list of the information contained on each page. Feel free to make suggestions to the help documentation in a ticket on the FC_Rpg plugin page.");
				standardMessage("");
				standardMessage("Page 1: This Page.");
				standardMessage("Page 2: Basics");
				standardMessage("Page 3: Combat");
				standardMessage("Page 4: Spells");
				standardMessage("Page 5: Alchemy (Alchemist Only)");
				standardMessage("Page 6: Guilds");
				standardMessage("Page 7: Dungeons");
				break;
			case 2:
				standardHeader("Basics ~ Page " + strPage);
				standardMessage("First and foremost, a disclaimer: FC_Rpg is an extremely customizable plugin. Thus, the information contained here will apply ONLY to default settings. " +
						"If your server customizes settings, then this help may have innaccuracies. Use this help to get a better general idea of how FC_Rpg works and operates.");
				secondaryMessage("To get started with FC_Rpg, you will need to find your servers class selection board. Right-Click the signs to select your class. After you select a class, find a finish " +
						"sign nearby and right-click on that. All signs need to be right-click to be used with FC_Rpg.");
				standardMessage("Now that you have a class, you should have access to the majority of the plugins commands.");
				break;
			case 3:
				standardHeader("Combat ~ Page " + strPage);
				standardMessage("Combat in FC_Rpg is simple. There are four stats for players and two stats for monsters. Killing monsters will grant gold and experience so that you become " +
						"more rich and eventually will levelup. Higher level monsters wear stronger gear, drop better items, reward more gold, and reward more experience.");
				secondaryMessage("To become powerful in FC_Rpg, slay monsters that you feel comfortable defeating until you levelup. As you level up you will be awarded stat and spell points to allocate " +
						"into stats and spells.");
				standardMessage("While it is recommended to fight monsters roughly your level, killing monsters above and below your level will still reward normal experience and loot. Thus, if you find a " +
						"strategy to kill level 100 monsters at level 1's (cheater), then you will levelup significantly faster than somebody fighting level 1's.");
				break;
			case 4:
				standardHeader("Spells ~ Page " + strPage);
				standardMessage("FC_Rpg has a diverse set of spells. There are five classes with five spells each. By using the /spell command, you can begin the process of using spells. When you aquire " +
						"your first spell point at level four, use /spell list to pick a spell. Then upgrade it with /spell upgrade and the name or number of the spell you want to upgrade. After that, " +
						"to case the spell you need to bind it to an item. Use /spell bind [name,num] to assign the spell to the item you are holding. Now right-click to ready a spell and left-click to cast it." +
						"Some spells are able to be cast without a target while others require a target. The general trend for this is that offensive spells require targets with notable exceptions for spells like " +
						"fireball which are ranged whereas defensive, buff, and utility spells will not require a target. Depending on your class, you might have to hold an item to cast spells. For example, the " +
						"wizard requires a wand to cast spells. A wand can be a stick or a blaze rod <-- THIS IS REALLY BIG, BECAUSE IT LETS YOU BIND TWO SPELLS!!! Consider it a reward for reading the help! :D");
				break;
			case 5:
				standardHeader("Alchemy ~ Page " + strPage);
				standardMessage("Alchemy is a special mage spell that once invested into, allows access to the command /alchemy. The /alchemy command allows you to convert items into a raw material known by " +
						"the arcane as arcanium. Arcanium can be extracted from items using magic power. Higher levels of the alchemy spell allow for greater amounts of arcanium to be harvested from items. As well, " +
						"it is well known in the magic world that enchantments are made of solid arcanium and will grant major bonuses to the amount of arcanium given from items during extraction. The only downside to " +
						"arcanium is that composing items with it (aka buying) requires significantly more arcanium when used with no raw materials. The greatest mages are break planets apart only to forge " +
						"them into gold and diamonds.");
				break;
			case 6:
				standardHeader("Guilds ~ Page " + strPage);
				standardMessage("Guilds are a feature of FC_Rpg that allows players to group up. The main advantages of joining a guild is the experience share, party buffs get applied to members, and" +
						"the experience bonus. The more online members of a guild there are, the greater the experience bonus that all members of the guild will recieve. The exp bonus caps at 20% with 50 members" +
						"All experience sharing and bonuses will only take place between members that are nearby each other. Thus, if there are 8 members of a guild online, but only 3 are together killing " +
						"monsters, then the three players will get a 1.2% exp boost for being near each other. The distance for this check is 50 blocks from monster slayer.");
				secondaryMessage("Spells can also apply buffs to nearby guild members. Buffs will only apply to nearby guild members. Thus, if you have 50 guild members stacked on top of you and you cast a damage boosting, " +
						"spell, all 50 members will recieve that damage boost. The distance that a buff applies varies per spell but the distance generally increases per level as well as strength and duration.");
				standardMessage("A final important note on guilds is that once you join them, you are subject to power leveling rules. Basically, these make it so that whenever you kill a monster, anybody " +
						"that is significantly stronger or weaker than you will not recieve combat rewards. When fighting solo you will not have the rules applied to yourself but if " +
						"another guild member is within 50 blocks, you will be subject to the power leveling rules yourself.");
				break;
			case 7:
				standardHeader("Dungeons ~ Page " + strPage);
				standardMessage("Dungeons are the final topic of this help. Dungeons are a way for players to go kill monsters and get tons of extra loot in the process. Simply find a dungeon, right-click the " +
						"sign and enter it. After a delay, you will enter and be free to slay monsters. Dungeons are fully instanced, so feel free to loot all the treasure and items you find because they will " +
						"be gone the next run! Every dungeon has a boss. This boss is placed by the server admin when designed and you should beware " +
						"of him as he WILL be stronger than the rest of the mobs in the dungeon.");
				break;
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
					standardMessage("- You have " + rpgPlayer.playerConfig.getClassChangeTickets() + " tickets remaining.");
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
			infiniteMessage("Automatic Casting Currently: ", rpgPlayer.playerConfig.getAutoCast() + "");

		return true;
	}

	public boolean helpAlchemy()
	{
		if (rpgPlayer == null || !rpgPlayer.playerConfig.getHasAlchemy())
			return true;

		standardHeader("Alchemy Commands");
		standardMessage("/alchemy", "Help for alchemy");
		standardMessage("/alchemy [list] <startpoint>", "List items purchasable with Arcanium.");
		standardMessage("/alchemy [buy] [num] [count]", "Buy items from the alchemy list.");
		standardMessage("/alchemy [smelt] <all>", "Break down an item in your hand or all of your items into Arcanium.");
		infiniteMessage("You currently have ", FC_Rpg.df.format(rpgPlayer.playerConfig.getArcanium()), " Arcanium.");

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

	public boolean helpRealm()
	{
		if (!perms.isAdmin())
			return errorNoPermission();

		standardHeader("Realm Commands");
		standardMessage("/realm list", "List all worlds on your server.");
		standardMessage("/realm toggleRpg", "Marks current world as an rpg world..");
		standardMessage("/realm tp [realmName]", "Teleport to a worlds spawn.");
		standardMessage("/realm new [realmName]", "Add a world to world config.");
		standardMessage("/realm levelone", "Set a worlds level one to your loc.");
		standardMessage("/realm spawn [realmName] [x] [y] [z] [yaw] [pitch]", "Change a worlds spawn to information you specify.");
		standardMessage("/realm spawn here", "Change a worlds spawn to your loc.");
		
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
