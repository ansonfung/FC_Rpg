package me.Destro168.Listeners;

import me.Destro168.Configs.DungeonManager;
import me.Destro168.Configs.WarpManager;
import me.Destro168.Configs.WorldManager;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.RpgMessageLib;
import me.Destro168.events.DungeonEvent;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractionListener implements Listener
{
	Player player;
	RpgMessageLib msgLib;
	WorldManager wm;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLeftClick(PlayerInteractEvent event)
	{
		//Variable Declerations
		Sign sign;
		Block block;
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		Boolean allowUse = true;
		wm = new WorldManager();
		
		player = event.getPlayer();
		msgLib = new RpgMessageLib(player);
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			block = event.getClickedBlock();
		else
			return;
		
		//Blacklist items that can't be used.
		if (player.getItemInHand().getType() == Material.MONSTER_EGG || player.getItemInHand().getType() == Material.MONSTER_EGGS)
			allowUse = false;
		else if (player.getItemInHand().getType() == Material.LAVA_BUCKET)
			allowUse = false;
		else if (player.getItemInHand().getType() == Material.WATER_BUCKET)
			allowUse = false;
		else if (player.getItemInHand().getType() == Material.BUCKET)
			allowUse = false;
		else if (player.getItemInHand().getType() == Material.FLINT_AND_STEEL)
			allowUse = false;
		else if (player.getItemInHand().getType() == Material.FIREBALL)
			allowUse = false;
		
		if (allowUse == false)
		{
			if (!wm.isCreativeWorld(player.getWorld()))
				return;
			
			//Inform the player they can't use eggs, cancel the event, and return if they aren't admin.
			if (!perms.isAdmin())
			{
				msgLib.standardMessage("Only admins may use this");
				event.setCancelled(true);
				return;
			}
			else
			{
				msgLib.standardMessage("Admin Override Successful.");
			}
		}
		
		//Handle signs
		if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
		{
			sign = (Sign) block.getState();
			
			parseSignClick(sign);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick2(PlayerInteractEvent event)
	{
		//Variable declarations.
		Player p = event.getPlayer();
		RpgPlayer rp = FC_Rpg.rpgManager.getRpgPlayer(p);
		boolean leftClick = false;
		
		//Act on left and right clicks only.
		if ((event.getAction() == Action.LEFT_CLICK_AIR) && (!(event.getAction() == Action.LEFT_CLICK_BLOCK)))
			leftClick = true;
		else if ((event.getAction() == Action.RIGHT_CLICK_AIR) && (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)))
			leftClick = false;
		else
			return;
		
		//If they left-clicked, attempt to cast spells.
		if (leftClick == true)
		{
			//If auto-cast is enabled, we cast the spell.
			if (rp.getAutoCast() == true)
			{
				//Then set active spell.
				rp.prepareSpell(false);
				
				//Then cast the spell.
				rp.castSpell(null, 0, 0);
			}
			
			else if (rp.getActiveSpell() != null)
			{
				//If a spell is ready to be cast, then...
				if (!rp.getActiveSpell().equals("none"))
				{
					//Then set active spell.
					rp.prepareSpell(false);
					
					//Then cast the spell.
					rp.castSpell(null, 0, 0);
				}
			}
		}
		else
		{
			//Make sure they use the approriate action before continuing.
			if (!(event.getAction() == Action.RIGHT_CLICK_AIR) && (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)))
				return;

			//If everything succeeded, set the active spell.
			rp.prepareSpell(true);
		}
	}
	
	//If the player left-clicks a sign, we want to evaluate this.
	public void parseSignClick(Sign sign)
	{
		String pickedClass = "";
		ColorLib cl = new ColorLib();
		
		try
		{
			//If the player clicks a special pick class sign.
			if (sign.getLine(0).contains("Pick Class:"))
			{
				//Strip colors
				pickedClass = cl.removeColors(sign.getLine(1));
				
				//If the sign was proper, then 
				for (int i = 0; i < FC_Rpg.c_classes.length; i++)
				{
					if (pickedClass.equals(FC_Rpg.c_classes[i]))
					{
						//Send the player a confirmation message.
						msgLib.standardMessage("You have selected the " + pickedClass + " class.");
						
						//Store their class selection in a hashmap
						FC_Rpg.classSelection.put(player, pickedClass);
						
						//Break.
						break;
					}
				}
			}
			
			else if (sign.getLine(0).contains("Finish"))
			{
				//If the player has picked both a class and job.
				if (FC_Rpg.classSelection.containsKey(player))
				{
					//Prevent players from picking a job/class again without respecing.
					if (FC_Rpg.rpgManager.getRpgPlayer(player) != null)
					{
						if (FC_Rpg.rpgManager.getRpgPlayer(player).getIsActive() == true)
						{
							msgLib.standardMessage("You have to use /reset first.");
							return;
						}
					}
					
					if (sign.getLine(3).contains("Manually"))
					{
						//Store the player as a new player.
						FC_Rpg.rpgManager.setPlayerStart(FC_Rpg.classSelection.get(player), player, true);
					}
					else
					{
						//Store the player as a new player.
						FC_Rpg.rpgManager.setPlayerStart(FC_Rpg.classSelection.get(player), player, false);
					}
				}
				else
				{
					msgLib.standardMessage("Please pick a class first!");
					return;
				}
			}
			else if (sign.getLine(0).contains("Refill Mana!"))
			{
				if (FC_Rpg.rpgManager.getRpgPlayer(player) != null)
					FC_Rpg.rpgManager.getRpgPlayer(player).restoreMana(99999);
				
				msgLib.standardMessage("Refilled Mana!");
			}
			else if (sign.getLine(0).contains("Teleport:"))
			{
				if (FC_Rpg.rpgManager.getRpgPlayer(player) != null)
					teleportPlayer(FC_Rpg.rpgManager.getRpgPlayer(player), sign.getLine(1));
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return;
		}
	}

	//Teleports players based on sign text.
	public void teleportPlayer(RpgPlayer rpgPlayer, String text)
	{
		//Variable declarations
		rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer(player);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		WarpManager warpManager = new WarpManager();
		int breakLimit = 0;
		
		//Go through all potential warps.
		for (int i = 0; i < 10000; i++)
		{
			//If the sign contains the word dungeon, then..
			if (text.contains("Dungeon " + i))
			{
				dungeonTeleport(text, FC_Rpg.trueDungeonNumbers.get(i - 1));
				break;
			}
			
			//If not null
			else if (warpManager.getName(i) != null)
			{
				//If the name is contained on the sign
				if (text.contains(warpManager.getName(i)))
				{
					//Attempt to pay the cost.
					if (FC_Rpg.economy.has(player.getName(), warpManager.getCost(i)) == true)
						FC_Rpg.economy.withdrawPlayer(player.getName(), warpManager.getCost(i));
					else
					{
						msgLib.errorNotEnoughMoney();
						break;
					}
					
					//If it requires admin permission and the teleporter doesn't have perms.
					if (warpManager.getAdmin(i) == true && perms.isAdmin() == false)
					{
						 msgLib.errorNoPermission();
						 break;
					}
					
					//If the sign requres donator, if not donator/admin, return false.
					if (warpManager.getDonator(i) == true && (rpgPlayer.isDonatorOrAdmin() == false))
					{
						msgLib.errorNotDonator();
						break;
					}
					
					//Give the person a teleport message.
					teleportMessage(warpManager.getWelcome(i),"");
					
					//Send the warp description to the player.
					for (String descrip : warpManager.getDescription(i))
						msgLib.standardMessage(descrip);
					
					//Teleport the player.
					player.teleport(warpManager.getDestination(i));
					
					//Break to return.
					break;
				}
			}
			else
			{
				//Increase break limit
				breakLimit++;
				
				//If a warp was failed to be found 50 times, break.
				if (breakLimit == 50)
					break;
			}
		}
	}
	
	private void teleportMessage(String msgP1, String msgP2)
	{
		//Variable Declarations
		String msg = "";
		String stringToEvaluate = "";
		
		if (msgP2.length() > msgP1.length())
			stringToEvaluate = msgP2;
		else
			stringToEvaluate = msgP1;
		
		//Form the lines.
		for (int i = 0; i < stringToEvaluate.length(); i++)
		{
			msg += "-";
		}
		
		msg += "----";
		
		msgLib.standardMessage(msg);
		msgLib.standardMessage("- " + msgP1 + " -");
		
		//Only show the second message part if not empty.
		if (!msgP2.equals(""))
			msgLib.standardMessage("- " + msgP2 + " -");
		
		msgLib.standardMessage(msg);
	}
	
	private void dungeonTeleport(String text, int dNumber)
	{
		//Dungeon lobby
		DungeonManager dm = new DungeonManager();
		
		//If we have a dungeon exit symbol, then,
		if (text.contains("-"))
		{
			teleportMessage("You Have Exited The Dungeon " + dm.getName(dNumber) + "!","");
			FC_Rpg.dungeon[dNumber].removeDungeoneer(player, player,true);
			player.teleport(dm.getExit(dNumber));
			
			//Also return for exiting.
			return;
		}
		
		//If no dungeon was found, return
		if (dNumber == -1)
			return;
		
		//Store dungeon for faster access.
		DungeonEvent dungeon = FC_Rpg.dungeon[dNumber];
		
		if (dungeon.getPhase() == 0 || dungeon.getPhase() == 1)
		{
			double payAmount = dm.getCost(dNumber);
			
			//Attempt to pay.
			if (attemptPay(payAmount) == false)
			{
				msgLib.errorNotEnoughMoney();
				return;
			}
			
			//Add the player to the dungeon.
			if (dungeon.addDungeoneer(player, dNumber) == false)
				return;
			
			//Welcome them.
			teleportMessage("Welcome To The Dungeon " + dm.getName(dNumber) + "! ", "$" + payAmount + " Deducted.");
			
			//Teleport them to the lobby.
			player.teleport(dm.getLobby(dNumber));
		}
		else if (dungeon.getPhase() != 1)
		{
			msgLib.standardMessage("The dungeon has already started");
			return;
		}
	}
	
	private boolean attemptPay(double amount)
	{
		if (FC_Rpg.economy.has(player.getName(), amount) == true)
		{
			FC_Rpg.economy.withdrawPlayer(player.getName(), amount);
			return true;
		}
		else
		{
			return false;
		}
	}
}








