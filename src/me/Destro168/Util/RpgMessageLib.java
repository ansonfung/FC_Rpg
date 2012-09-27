package me.Destro168.Util;

import me.Destro168.Configs.FaqManager;
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
		if (FC_Rpg.rpgManager.getRpgPlayer(player) != null)
			rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		else
			rpgPlayer = null;
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
		standardMessage("You need to pick a class/job first!");
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
	
	public boolean errorWrongJob()
	{
		standardMessage("You aren't the proper job to do this!");
		return true;
	}

	public boolean errorClassLevelTooLow()
	{
		standardMessage("Your class level is too low!");
		return true;
	}
	
	public boolean errorNotDonator()
	{
		standardMessage("Your are not a donator!");
		return true;
	}
	
	public boolean bigHelp()
	{
		help();
		helpClass();
		helpJob();
		helpSpell();
		helpParty();
		helpDonator();
		helpPotion();
		helpAdmin();
		helpOwner();
		
		return true;
	}
	
	public boolean helpFaq()
	{
		FaqManager fm = new FaqManager();
		String faq;
		String topics = "";
		int breakPoint = 0;
		
		standardHeader("Server Faq Topics!");
		standardMessage("Type /faq followed by the topic you want to learn about!");
		
		for (int i = 0; i < 10000; i++)
		{
			faq = fm.getFaqTag(i);
			
			if (faq != null)
				topics = topics + faq;
			else
			{
				breakPoint++;
				
				if (breakPoint > 50)
					break;
			}
		}
		
		standardMessage("Topics", topics);
		
		return true;
	}
	
	public boolean help()
	{
		standardHeader("Help for Everything!");
		standardError("Constantly Asking Questions In Help = Mute");
		standardMessage("/faq", ChatColor.RED + "Important Server Information! READ!");
		standardMessage("/class","Help for classes.");
		standardMessage("/job","Help for jobs.");
		standardMessage("/party","Help for parties.");
		standardMessage("/spell","Help for spells");
		standardMessage("/pvp","Help for pvp events.");
		standardMessage("/reset","Reset Your Character! (Infinite/You Keep Money)");
		
		if (rpgPlayer.isDonatorOrAdmin())
			standardMessage("/donator","Help for donators.");
		
		standardMessage("/bigHelp","List all helps at once.");
		
		if (perms.isAdmin())
			standardMessage("[A] /rpg admin","Help for admins.");
		
		if (perms.isOwner())
			standardMessage("[A] /rpg owner","Destro168's Private Commands");
		
		return true;
	}
	
	public boolean helpDungeon()
	{
		if (perms.isAdmin())
		{
			standardHeader("Dungeon Help ~ 0 = D1, 1 = D2");
			standardMessage("[A] /dungeon init [num]","Start a dungeon.");
			standardMessage("[A] /dungeon stop [num]","Stop a dungeon.");
			standardMessage("[A] /dungeon check [num]","Check if any mobs are left to update loot chest.");
			standardMessage("[A] /dungeon kick [num]","Kick somebody from a dungeon.");
		}
		
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
	
	public boolean helpParty()
	{
		standardHeader("Party Commands");
		standardMessage("/party list [page]","See a list of parties.");
		standardMessage("/party create [partyName]","Create a party.");
		standardMessage("/party join [partyName]","Join a party.");
		standardMessage("/party leave","Leave your party.");
		standardMessage("/party close","Prevents new members from joining.");
		standardMessage("/party open","Allows new members to join.");
		standardMessage("/party view [partyName]","See party info.");
		
		return true;
	}

	public boolean helpClass()
	{
		standardHeader("Class Commands");
		standardMessage("/class view [name]","Show Character Information");
		standardMessage("/class spec [stat] [amount]","Distribute Stat Points");
		standardMessage("- Tip","If you have stat points, use this command.");
		standardMessage("/class allocate [on,off]","Set auto stat distribution.");
		
		if (rpgPlayer.hasClassChangeTicket() == true)
		{
			standardMessage("/class switch [new class name]","Use a class ticket to switch class.");
			standardMessage("- You have [" + ChatColor.WHITE + rpgPlayer.getClassChangeTickets() + ChatColor.GRAY + "] tickets remaining.");
		}
		
		return true;
	}
	
	public boolean helpJob()
	{
		standardHeader("Job Commands");
		standardMessage("/job view [name]","Shows Job Information");
		standardMessage("/job promote","Promotes you to next job rank!");
		
		if (rpgPlayer.isHunterOrAdmin())
			standardMessage("[H] /job kit","Gives you a kit once per life.");
		
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
	
	public boolean helpPotion()
	{
		if (perms.isAdmin())
		{
			standardHeader("Potion information");
			standardMessage("/rpg potion [type] [amount]");
			standardMessage("The following is a reference of potion IDs. All are splash max level/length.");
			standardMessage("8225 - reg     / 8229 - hea");
			standardMessage("8228 - poi    / 8236 - har");
			standardMessage("8233 - str  / 8226 - swi");
			standardMessage("8266 - slo      / 8264 - wea");
			standardMessage("8259 - fir");
		}
		
		return true;
	}
	
	public boolean helpAdmin()
	{
		if (perms.isAdmin())
		{
			standardHeader("Admin Commands ~ BE CAREFUL");
			standardMessage("/rpg modify [name] [modifiable] [newValue]");
			standardMessage("- modifiable = [any stat], level, addLevel, exp, addexp, class, hunterLevel, stat(s), addsecond(s), jobrank, all (all stats), spellPoint(s), rankfreeze");
			standardMessage("/rpg heal [name]");
			standardMessage("/rpg event [type]","loot,exp,off");
			standardMessage("/rpg set [name] [to] [duration]","Set people to various ranks.");
			standardMessage("- To = donator, hunter, predator, terror. donator needs time in months for duration.");
			standardMessage("/rpg giveTicket [name] [count]","Gives donator");
			standardMessage("/rpg generate [mine]","Regenerates specified mine.");
			standardMessage("/h [name]","Rpg heal style a player.");
			standardMessage("/g ","Toggle between gamemode/survival.");
			standardMessage("/dungeon","Help for dungeons.");
			standardMessage("/rpg potion","Help for potions.");
			standardMessage("/rpg listworld(s)","List all worlds on your server.");
			standardMessage("/rpg go [u [amount] | d [amount] | (coords)]","Teleport anywhere.");
			standardMessage("/rpg tpworld [worldname]","Teleport to a worlds spawn.");
		}
		
		return true;
	}
	
	public boolean helpOwner()
	{
		if (perms.isOwner())
		{
			standardHeader("Owner Only commands");
			standardMessage("/rpg wall","Creates wall with all classes/jobs/start sign.");
			standardMessage("/rpg spawn [worldname] [x] [y] [z] [yaw] [pitch]","Change a worlds spawn");
			standardMessage("/rpg spawn here","Change a worlds spawn");
			standardMessage("/rpg tp [name]","Bypassed Player Tp.");
			standardMessage("/rpg sudo","Make somebody say anything/use any command.");
		}
		
		return true;
	}
}












