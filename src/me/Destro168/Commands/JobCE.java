package me.Destro168.Commands;

import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ArgParser;
import me.Destro168.Util.RpgMessageLib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JobCE implements CommandExecutor
{
	Player player;
	RpgPlayer rpgPlayer;
	
	public JobCE() { }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args2)
    {
		player = (Player) sender;
		rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		
		RpgMessageLib msgLib;
		ArgParser ap = new ArgParser(args2);
		String[] args = ap.getArgs();
		
		msgLib = new RpgMessageLib(player);
		
		//Only let active players use this command.
		if (rpgPlayer.getPlayerConfigFile().getIsActive() == false)
			return msgLib.errorCreateCharacter();
		
		if (args[0].equals(""))
		{
			return msgLib.helpJob();
		}
		
		if (args[0].equalsIgnoreCase("view"))
		{
			msgLib.standardHeader("Job Information Sheet");
			
			if (!args[1].equals(""))
			{
				if (Bukkit.getServer().getPlayer(args[1]) != null)
				{
					rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(Bukkit.getServer().getPlayer(args[1]));
					msgLib.standardMessage("Successfully found player to view job.");
				}
				else
					msgLib.standardError("Couldn't find player to view job.");
			}
			
			msgLib.standardMessage("Name", rpgPlayer.getPlayerConfigFile().getName());
			msgLib.standardMessage("Job Rank",String.valueOf(rpgPlayer.getPlayerConfigFile().getJobRank()));
			msgLib.standardMessage("Job Promotion Cost",String.valueOf(rpgPlayer.getPromotionCost()));
			
			if (rpgPlayer.getPlayerConfigFile().getHunterLevel() > 0)
				msgLib.standardMessage("Hunter Level",String.valueOf(rpgPlayer.getPlayerConfigFile().getHunterLevel()));
		}
		else if (args[0].equalsIgnoreCase("promote"))
		{
			//Make sure the player isn't max job rank first
			if (rpgPlayer.getPlayerConfigFile().getJobRank() < 6)
			{
				//Defenders have to be prevented from promoting themselves.
				if (rpgPlayer.getPlayerConfigFile().getHunterLevel() > 0)
				{
					msgLib.standardError("You can't promote yourself as you are a hunter");
					return true;
				}
				
				//Make sure that players can only be promoted with the proper job rank.
				if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 20 && rpgPlayer.getPlayerConfigFile().getJobRank() == 1)
				{
					msgLib.standardError("You need level 20+ for promotion.");
					return true;
				}
				else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 40 && rpgPlayer.getPlayerConfigFile().getJobRank() == 2)
				{
					msgLib.standardError("You need level 40+ for promotion.");
					return true;
				}
				else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 60 && rpgPlayer.getPlayerConfigFile().getJobRank() == 3)
				{
					msgLib.standardError("You need level 60+ for promotion.");
					return true;
				}
				else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 80 && rpgPlayer.getPlayerConfigFile().getJobRank() == 4)
				{
					msgLib.standardError("You need level 80+ for promotion.");
					return true;
				}
				else if (rpgPlayer.getPlayerConfigFile().getClassLevel() < 100 && rpgPlayer.getPlayerConfigFile().getJobRank() == 5)
				{
					msgLib.standardError("You need level 100 for the final promotion. Good luck :)");
					return true;
				}
				
				//If the player can afford a promotion
				if (FC_Rpg.economy.getBalance(player.getName()) > rpgPlayer.getPromotionCost())
				{
					//Take away money from the player.
					FC_Rpg.economy.bankWithdraw(player.getName(), rpgPlayer.getPromotionCost());
					
					//Give them the promotion
					rpgPlayer.getPlayerConfigFile().setJobRank(rpgPlayer.getPlayerConfigFile().getJobRank() + 1);
					
					//Announce the promotion
					FC_Rpg.bLib.standardBroadcast(player.getName() + " is now Job Rank [" + rpgPlayer.getPlayerConfigFile().getJobRank() + "]");
				}
				else
				{
					msgLib.standardError("You need $" + String.valueOf(rpgPlayer.getPromotionCost() - FC_Rpg.economy.getBalance(player.getName())) + " for promotion");
				}
			}
			else
			{
				return msgLib.errorMaxJob();
			}
		}
		else if (args[0].equalsIgnoreCase("kit"))
		{
			//Only hunters can use kits.
			if (rpgPlayer.isHunterOrAdmin() == false)
				return msgLib.errorNoPermission();
			
			//Prevent kit spam.
			if (rpgPlayer.getPlayerConfigFile().getHunterCanKit() == false)
			{
				msgLib.standardError("You must die before using your kit again!");
				return true;
			}
			
			//Give the kit
			giveKit();
			
			//Return success.
			msgLib.successCommand();
		}
		
		return true;
	}
	
	public void giveKit()
	{
		ItemStack bow;
		ItemStack arrows;
		ItemStack food;
		ItemStack weapon;
		ItemStack boots;
		ItemStack chest;
		ItemStack helmet;
		ItemStack legs;
		int strength = rpgPlayer.getPlayerConfigFile().getAttack();
		int constitution = rpgPlayer.getPlayerConfigFile().getConstitution();
		
		//Remove hunter ability to kit until he dies.
		rpgPlayer.getPlayerConfigFile().setHunterCanKit(false);
		
		//Set the items that the hunter will get.
		bow = new ItemStack(Material.BOW, 5);
		arrows = new ItemStack(Material.ARROW, 32);
		food = new ItemStack(Material.COOKED_BEEF, 8);
		
		//Set the sword.
		if (strength < 125)
			weapon = new ItemStack(Material.WOOD_SWORD, 1);
		else if (strength < 250)
			weapon = new ItemStack(Material.STONE_SWORD, 1);
		else if (strength < 375)
			weapon = new ItemStack(Material.IRON_SWORD, 1);
		else if (strength < 500)
			weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
		else if (strength >= 500)
			weapon = new ItemStack(Material.GOLD_SWORD, 1);
		else
			weapon = new ItemStack(Material.WOOD_SWORD, 1);
			
		//Set the iron tools.
		if (constitution < 125)
		{
			boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			helmet = new ItemStack(Material.LEATHER_HELMET, 1);
		}
		else if (constitution < 250)
		{
			boots = new ItemStack(Material.IRON_BOOTS, 1);
			helmet = new ItemStack(Material.IRON_HELMET, 1);
			legs = new ItemStack(Material.IRON_LEGGINGS, 1);
			chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		}
		else if (constitution < 500)
		{
			boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
			helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
			legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
			chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		}
		else if (constitution >= 500)
		{
			boots = new ItemStack(Material.GOLD_BOOTS, 1);
			helmet = new ItemStack(Material.GOLD_HELMET, 1);
			legs = new ItemStack(Material.GOLD_LEGGINGS, 1);
			chest = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		}
		else
		{
			boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			helmet = new ItemStack(Material.LEATHER_HELMET, 1);
		}
		
		//Give the items to the hunter.
		player.getInventory().addItem(bow);
		player.getInventory().addItem(arrows);
		player.getInventory().addItem(food);
		player.getInventory().addItem(weapon);
		player.getInventory().addItem(boots);
		player.getInventory().addItem(chest);
		player.getInventory().addItem(helmet);
		player.getInventory().addItem(legs);
	}
}























