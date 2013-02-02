package me.Destro168.FC_Rpg.Listeners;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Configs.PlayerConfig;
import me.Destro168.FC_Rpg.Configs.WarpConfig;
import me.Destro168.FC_Rpg.Configs.WorldConfig;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.Events.DungeonEvent;
import me.Destro168.FC_Rpg.Util.FC_RpgPermissions;
import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.FC_Suite_Shared.FC_Suite_Shared;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractionListener implements Listener
{
	public static String signColor1 = FC_Suite_Shared.sc.primaryColor;
	public static String signColor2 = "&0";
	
	public static String signTeleport = "[" + signColor1 + "Teleport" + signColor2 + "]";
	public static String signFillMana = "[" + signColor1 + "Heal Mp" + signColor2 + "]";
	public static String signFillHealth = "[" + signColor1 + "Heal Hp" + signColor2 + "]";
	public static String signPickClass = "[" + signColor1 + "Class" + signColor2 + "]";
	public static String signFinish = "[" + signColor1 + "Finish" + signColor2 + "]";
	public static String signExit = "[" + signColor1 + "Exit" + signColor2 + "]";
	
	private PlayerInteractEvent event;
	private Player player;
	private MessageLib msgLib;
	private WorldConfig wm;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void teleportHandler(PlayerInteractEvent event_)
	{
		//Check world.
		event = event_;
		player = event.getPlayer();
		
		if (!FC_Rpg.worldConfig.getIsRpgWorld(player.getWorld().getName()))
			return;
		
		teleportHandler();
		
		if (FC_Rpg.generalConfig.getCreativeControl() && wm.isCreativeWorld(player.getWorld()))
			creativeControlHander();
		
		spellHandler();
		selectionHandler();
		spendRecord();
	}
	
	public void teleportHandler()
	{
		//Variable Declerations
		Sign sign;
		Block block;
		wm = new WorldConfig();
		
		//Initialize variables
		msgLib = new MessageLib(player);
		
		//Initialize the block.
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			block = event.getClickedBlock();
		else
			return;
		
		//Handle signs
		if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
		{
			sign = (Sign) block.getState();
			parseSignClick(sign);
			return;
		}
	}
	
	public void creativeControlHander()
	{
		//Initialize msgLib.
		MessageLib msgLib = new MessageLib(player);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		Boolean allowUse = true;
		
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
	}
	
	public void spellHandler()
	{
		//Check world.
		player = event.getPlayer();
		
		//Variable declarations.
		RpgPlayer rp = FC_Rpg.rpgEntityManager.getRpgPlayer(player);
		
		if (rp == null)
			return;
		
		boolean autocast = rp.playerConfig.getAutoCast();
		boolean leftClick = false;
		boolean hasBow = false;
		
		//Act on left and right clicks only.
		if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK))
			leftClick = true;
		else if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
			leftClick = false;
		else
			return;
		
		if (player.getItemInHand() != null)
		{
			if (player.getItemInHand().getType() == Material.BOW)
				hasBow = true;
		}
		
		//If they left-clicked, attempt to cast spells.
		if (leftClick == true)
		{
			//If the player has a bow, handle that.
			if (hasBow == true && !autocast)
			{
				rp.prepareSpell(true); //Prepare spell
				return;
			}
			
			//If auto-cast is enabled, we cast the spell.
			if (autocast == true)
			{
				//Then set active spell.
				rp.prepareSpell(false);
				
				//Then cast the spell.
				rp.castSpell(null, 0, 0);
			}
			else if (rp.playerConfig.getActiveSpell() != null)
			{
				//If a spell is ready to be cast, then...
				if (!rp.playerConfig.getActiveSpell().equals("none"))
				{
					//Then set active spell.
					rp.prepareSpell(false);
					
					//Then cast the spell.
					rp.castSpell(null, 0, 0);
				}
			}
		}
		else if (leftClick == false && hasBow == false)
		{
			//If autocast is disabled, then you can prepare.
			if (autocast == false)
				rp.prepareSpell(true); //Prepare spell
			
			if (hasBow == true)
			{
				// If autocast is disabled, then you can prepare.
				if (autocast)
					rp.castSpell(null, 0, 0);	//Then cast the spell.
			}
		}
	}
	
	public void selectionHandler()
	{
		//Check world.
		player = event.getPlayer();
		
		if (!FC_Rpg.worldConfig.getIsRpgWorld(player.getWorld().getName()))
			return;
		
		boolean isLeft;
		
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
			isLeft = true;
		else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK))
			isLeft = false;
		else
			return;
		
		if (player == null)
			return;
		
		if (!player.getItemInHand().getType().equals(Material.getMaterial(FC_Rpg.generalConfig.getDungeonSelectionToolID())))
			return;
		
		//Select the two points.
		FC_Rpg.sv.selectNewPoint(event.getPlayer(), event.getClickedBlock().getLocation(), isLeft);
		
		//Cancel the event.
		event.setCancelled(true);
	}
	
	//If the player left-clicks a sign, we want to evaluate this.
	private void parseSignClick(Sign sign)
	{
		String pickedClass = "";
		ColorLib cl = new ColorLib();
		String rawLine = cl.removeColorCodes(sign.getLine(0));
		
		//If the player clicks a special pick class sign.
		if (rawLine.contains(cl.removeColorCodes(signPickClass)))
		{
			//Strip colors
			pickedClass = cl.removeColors(cl.removeColorCodes(sign.getLine(1)));
			
			//If the sign was proper, then 
			for (int i = 0; i < FC_Rpg.classConfig.getRpgClasses().length; i++)
			{
				if (pickedClass.equalsIgnoreCase(cl.removeColorCodes(FC_Rpg.classConfig.getRpgClass(i).getName())))
				{
					//Prevent players from picking a job/class again without respecing.
					if (FC_Rpg.rpgEntityManager.getRpgPlayer(player) != null)
					{
						msgLib.standardMessage("You have to use /reset before picking a class.");
						return;
					}
					
					//Send the player a confirmation message.
					msgLib.infiniteMessage("You have selected the ",sign.getLine(1)," class. Next, hit a finish sign!");
					
					//Store their class selection in a hashmap
					if (FC_Rpg.classSelection.containsKey(player))
						FC_Rpg.classSelection.remove(player);
					
					FC_Rpg.classSelection.put(player, pickedClass);
					
					//Break.
					break;
				}
			}
		}
		else if (rawLine.contains(cl.removeColorCodes(signFinish)))
		{
			//If the player has picked both a class and job.
			if (FC_Rpg.classSelection.containsKey(player))
			{
				if (sign.getLine(1).contains("Manually") || sign.getLine(2).contains("Manually") || sign.getLine(3).contains("Manually"))
				{
					//Store the player as a new player.
					FC_Rpg.rpgEntityManager.setPlayerStart(FC_Rpg.classSelection.get(player), player, true);
					FC_Rpg.classSelection.remove(player);
				}
				else if (sign.getLine(1).contains("Auto") || sign.getLine(2).contains("Auto") || sign.getLine(3).contains("Auto"))
				{
					//Store the player as a new player.
					FC_Rpg.rpgEntityManager.setPlayerStart(FC_Rpg.classSelection.get(player), player, false);
					FC_Rpg.classSelection.remove(player);
				}
			}
			else
			{
				msgLib.standardMessage("Please pick a class first!");
				return;
			}
		}
		else if (rawLine.contains(cl.removeColorCodes(signFillMana)))
		{
			if (FC_Rpg.rpgEntityManager.getRpgPlayer(player) != null)
				FC_Rpg.rpgEntityManager.getRpgPlayer(player).healMana(99999999);
			
			msgLib.standardMessage("Refilled Mana!");
		}
		else if (rawLine.contains(cl.removeColorCodes(signFillHealth)))
		{
			if (FC_Rpg.rpgEntityManager.getRpgPlayer(player) != null)
				FC_Rpg.rpgEntityManager.getRpgPlayer(player).healHealth(99999999);
			
			msgLib.standardMessage("Refilled Health!");
		}
		else if (rawLine.contains(cl.removeColorCodes(signTeleport)))
		{
			if (FC_Rpg.rpgEntityManager.getRpgPlayer(player) != null)
				teleportPlayer(FC_Rpg.rpgEntityManager.getRpgPlayer(player), sign.getLine(1));
		}
		else if (rawLine.contains(cl.removeColorCodes(signExit)))
		{
			if (FC_Rpg.rpgEntityManager.getRpgPlayer(player) != null)
				exitPlayer(sign.getLine(1));
		}
	}
	
	private void spendRecord()
	{
		//If record bonuses are disabled or the player has no item in their hand, then return.
		if (!FC_Rpg.generalConfig.getRecordExpRewards() || player.getItemInHand() == null)
			return;
		
		Material mat = player.getItemInHand().getType();
		
		if (FC_Rpg.recordExpBonuses.containsKey(mat))
		{
			//Perform calculations
			RpgPlayer rPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(player);
			int expAmount = (int) (rPlayer.playerConfig.getLevelUpAmount() * FC_Rpg.recordExpBonuses.get(mat));
			
			//Add experience
			rPlayer.addClassExperience(expAmount, true);
			
			//Message player.
			MessageLib msgLib = new MessageLib(player);
			msgLib.infiniteMessage("You have just consumed a record for ",expAmount + ""," experience.");
			
			//Consume the record.
			player.setItemInHand(new ItemStack(Material.AIR));
		}
	}
	
	//Teleports players based on sign text.
	private void teleportPlayer(RpgPlayer rpgPlayer, String text)
	{
		//Variable declarations
		rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(player);
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		WarpConfig warpConfig = FC_Rpg.warpConfig;
		
		//Go through all potential warps.
		for (int i : FC_Rpg.warpConfig.getWarpFieldList())
		{
			//If the name is contained on the sign
			if (text.contains(warpConfig.getName(i)))
			{
				//Attempt to pay the cost.
				if (rpgPlayer.playerConfig.getGold() >= warpConfig.getCost(i))
					rpgPlayer.playerConfig.subtractGold(warpConfig.getCost(i));
				else
				{
					msgLib.errorNotEnoughMoney();
					break;
				}
				
				//If it requires admin permission and the teleporter doesn't have perms.
				if (warpConfig.getAdmin(i) == true && perms.isAdmin() == false)
				{
					 msgLib.errorNoPermission();
					 break;
				}
				
				//If the sign requres donator, if not donator/admin, return false.
				if (warpConfig.getDonator(i) == true && (rpgPlayer.isDonatorOrAdmin() == false))
				{
					msgLib.standardError("Sorry but you can't use this warp because you aren't a donator.");
					break;
				}
				
				// Variable Declaration
				int classRequirement = warpConfig.getClassRequirement(i);
				int jobRankMinimum = warpConfig.getJobRankMinimum(i);
				int levelMinimum = warpConfig.getLevelMinimum(i);
				
				if (classRequirement > -1 && rpgPlayer.playerConfig.getCombatClass() != classRequirement)
				{
					msgLib.standardError("Sorry but you aren't the proper class to use this warp.");
					break;
				}
				
				if (jobRankMinimum > -1 && rpgPlayer.playerConfig.getJobRank() < jobRankMinimum)
				{
					msgLib.standardError("Sorry but you need a higher job rank to use this warp.");
					break;
				}
				
				if (levelMinimum > -1 && rpgPlayer.playerConfig.getClassLevel() < jobRankMinimum)
				{
					msgLib.standardError("Sorry but you need a higher job rank to use this warp.");
					break;
				}
				
				//Give the person a teleport message.
				teleportMessage(warpConfig.getWelcome(i),"");
				
				//Send the warp description to the player.
				for (String descrip : warpConfig.getDescription(i))
					msgLib.standardMessage(descrip);
				
				//Teleport the player.
				player.teleport(warpConfig.getDestination(i));
				
				// Return.
				return;
			}
		}
		
		for (int i : FC_Rpg.trueDungeonNumbers.keySet())
		{
			if (text.equalsIgnoreCase(FC_Rpg.dungeonConfig.getName(i)))
			{
				dungeonTeleport(FC_Rpg.trueDungeonNumbers.get(i));
				return;
			}
		}
		
		msgLib.standardError("This warp leads to nowhere.");
	}
	
	public void exitPlayer(String signText)
	{
		for (int i : FC_Rpg.trueDungeonNumbers.keySet())
		{
			if (signText.equalsIgnoreCase(FC_Rpg.dungeonConfig.getName(i)))
			{
				// Exit message
				teleportMessage("You Have Exited " + FC_Rpg.dungeonConfig.getName(i) + "!","");
				FC_Rpg.dungeonEventArray[i].removeDungeoneer(player, player,true);
				player.teleport(FC_Rpg.dungeonConfig.getExit(i));
				
				//Also return for exiting.
				return;
			}
		}
		
		msgLib.standardError("This warp leads to nowhere.");
	}
	
	private void teleportMessage(String msgP1, String msgP2)
	{
		msgLib.standardHeader("- You Have Teleported -");
		msgLib.standardMessage("- " + msgP1 + " -");
		
		//Only show the second message part if not empty.
		if (!msgP2.equals(""))
			msgLib.standardMessage("- " + msgP2 + " -");
	}
	
	private void dungeonTeleport(int dNumber)
	{
		//If no dungeon was found, return
		if (dNumber == -1)
			return;
		
		RpgPlayer rPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(player);
		
		if (rPlayer != null)
		{
			if (!rPlayer.canEnterDungeon())
			{
				msgLib.infiniteError("You need to wait [",rPlayer.getDungeonWaitTime() + "","] more seconds.");
				return;
			}
		}
		
		//Store dungeon for faster access.
		DungeonEvent dungeon = FC_Rpg.dungeonEventArray[dNumber];
		
		if (dungeon.getPhase() == 0 || dungeon.getPhase() == 1)
		{
			double payAmount = FC_Rpg.dungeonConfig.getEntryFee(dNumber);
			
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
			if (payAmount > 0)
				teleportMessage("Welcome To The Dungeon " + FC_Rpg.dungeonConfig.getName(dNumber) + "! ", "&q" + payAmount + "&q Deducted.");
			else
				teleportMessage("Welcome To The Dungeon " + FC_Rpg.dungeonConfig.getName(dNumber) + "! ", "");
			
			//Teleport them to the lobby.
			player.teleport(FC_Rpg.dungeonConfig.getLobby(dNumber));
		}
		else if (dungeon.getPhase() != 1)
		{
			msgLib.standardMessage("The dungeon has already started");
			return;
		}
	}
	
	private boolean attemptPay(double amount)
	{
		PlayerConfig pc = new PlayerConfig(player.getName());
		
		if (!pc.getIsActive() || pc.getGold() < amount)
			return false;
		
		pc.subtractGold(amount);
		
		return true;
	}
}








