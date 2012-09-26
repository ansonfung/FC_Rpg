package me.Destro168.FC_Rpg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.Destro168.Commands.BigHelpCE;
import me.Destro168.Commands.ClassCE;
import me.Destro168.Commands.DonatorCE;
import me.Destro168.Commands.DungeonCE;
import me.Destro168.Commands.FaqCE;
import me.Destro168.Commands.GamemodeCE;
import me.Destro168.Commands.HatCE;
import me.Destro168.Commands.HealCE;
import me.Destro168.Commands.JobCE;
import me.Destro168.Commands.ListCE;
import me.Destro168.Commands.PartyCE;
import me.Destro168.Commands.PvpCE;
import me.Destro168.Commands.ResetCE;
import me.Destro168.Commands.RpgCE;
import me.Destro168.Commands.SpellCE;
import me.Destro168.Configs.ConfigOverlord;
import me.Destro168.Configs.DungeonManager;
import me.Destro168.Configs.WorldManager;
import me.Destro168.Entities.RpgManager;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.Entities.RpgPlayerFile;
import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.Listeners.DamageListener;
import me.Destro168.Listeners.PlayerInteractionListener;
import me.Destro168.Messaging.BroadcastLib;
import me.Destro168.Messaging.MessageLib;
import me.Destro168.Util.DistanceModifierLib;
import me.Destro168.Util.FC_RpgPermissions;
import me.Destro168.Util.HealthConverter;
import me.Destro168.events.DungeonEvent;
import me.Destro168.events.PvpEvent;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.Vector;

public class FC_Rpg extends JavaPlugin
{
	final static double MAX_HP = 999999;
	public final static DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public final static boolean debugModeEnabled = false;
	
	public static FC_Rpg plugin;
	public static RpgManager rpgManager;
	public static PartyManager partyManager;
	public static ColorLib cl = new ColorLib();
	public static PvpEvent pvp;
	public static BroadcastLib bLib;
	public static WorldManager wm;
	public static Economy economy = null;
	public static DungeonEvent[] dungeon;
	public static String[] c_classes;
	
	public final static int c_int_swordsman = 0;
	public final static int c_int_assassin = 1;
	public final static int c_int_defender = 2;
	public final static int c_int_wizard = 3;
	public final static int c_int_berserker = 4;
	
	public final static double DONATOR_PERK = .1;
	public final static double DONATOR_LOOT_BONUS = 1.1;
	public static int expMultiplier = 1;
	public static int lootMultiplier = 1;
	public static int tid3;
	public static int tid4;
	public static int dungeonCount;
	
	public int tid;
	public int tid2;
	
	public static Map<Player, String> classSelection = new HashMap<Player, String>();
	public static Map<Integer, Integer> trueDungeonNumbers;
	
	private RpgCE rpgCE;
	private ClassCE classCE;
	private JobCE jobCE;
	private SpellCE spellCE;
	private PartyCE partyCE;
	private DonatorCE donatorCE;
	private ResetCE resetCE;
	private BigHelpCE bigHelpCE;
	private HatCE hatCE;
	private ListCE listCE;
	private PvpCE pvpCE;
	private FaqCE faqCE;
	private HealCE healCE;
	private GamemodeCE gamemodeCE;
	private DungeonCE dungeonCE;
	
	private ConfigOverlord co;
	
	@Override
	public void onDisable() 
	{
		//Cancel all the tasks of this plugin.
		plugin.getServer().getScheduler().cancelTasks(plugin);
		
		//Save all players times for being logged on.
		for (Player player : Bukkit.getOnlinePlayers())
			rpgManager.unregisterRpgPlayer(player);
		
		for (int i = 0; i < dungeonCount; i++)
		{
			if (dungeon[FC_Rpg.trueDungeonNumbers.get(i)].isHappening() == true)
				dungeon[FC_Rpg.trueDungeonNumbers.get(i)].end(false);
		}
		
		this.getLogger().info("Disabled");
	}
	
	@Override
	public void onEnable() 
	{
		//Derp
		plugin = this;
		
		//Handle the CONFIGURATION OVERLORD. HAIL!
		co = new ConfigOverlord();
		
		//World manager = new world manager;
		rpgManager = new RpgManager();
		wm = new WorldManager();
		bLib = new BroadcastLib();
		partyManager = new PartyManager();
		pvp = new PvpEvent();
		c_classes = new String[5];
		
		c_classes[0] = "Swordsman";
		c_classes[1] = "Assassin";
		c_classes[2] = "Defender";
		c_classes[3] = "Wizard";
		c_classes[4] = "Berserker";
		
		//Set up the economy.
		setupEconomy();
		
		//Initialize dungeons.
		initializeDungeons();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
    		//Get the rpgPlayer.
    		RpgPlayer rpgPlayer = new RpgPlayer(player);
    		
			//If they aren't active, then we want to make them active by creating their rpg player.
			if (rpgPlayer.getIsActive() == false)
				rpgManager.setPlayerStart("Swordsman", player, false);	//Store the player as a new player.
			
			//Update the player file.
			rpgPlayer.handleUpdates();
		}
		
		//Register Listeners
		getServer().getPluginManager().registerEvents(new FishingListener(), plugin);
		getServer().getPluginManager().registerEvents(new HealthRegenListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
		getServer().getPluginManager().registerEvents(new RespawnListener(), plugin);
		getServer().getPluginManager().registerEvents(new DamageListener(), plugin);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(), plugin);
		getServer().getPluginManager().registerEvents(new ChatListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
		getServer().getPluginManager().registerEvents(new SignChangeListener(), plugin);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(), plugin);
		getServer().getPluginManager().registerEvents(new EntityDamageListener(), plugin);
		getServer().getPluginManager().registerEvents(new CreativeControlListeners(), plugin);
		getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(), plugin);
		getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), plugin);
		getServer().getPluginManager().registerEvents(new ExperienceChangeListener(), plugin);
		getServer().getPluginManager().registerEvents(new ArrowFireListener(), plugin);
		
		//Register Commands
		rpgCE = new RpgCE();
		getCommand("rpg").setExecutor(rpgCE);
		
		classCE = new ClassCE();
		getCommand("class").setExecutor(classCE);
		
		jobCE = new JobCE();
		getCommand("job").setExecutor(jobCE);
		
		spellCE = new SpellCE();
		getCommand("spell").setExecutor(spellCE);
		
		partyCE = new PartyCE();
		getCommand("party").setExecutor(partyCE);
		
		donatorCE = new DonatorCE();
		getCommand("donator").setExecutor(donatorCE);
		
		resetCE = new ResetCE();
		getCommand("reset").setExecutor(resetCE);
		
		hatCE = new HatCE();
		getCommand("hat").setExecutor(hatCE);
		
		bigHelpCE = new BigHelpCE();
		getCommand("bigHelp").setExecutor(bigHelpCE);
		
		listCE = new ListCE();
		getCommand("list").setExecutor(listCE);
		
		pvpCE = new PvpCE();
		getCommand("pvp").setExecutor(pvpCE);
		
		faqCE = new FaqCE();
		getCommand("faq").setExecutor(faqCE);
		
		healCE = new HealCE();
		getCommand("h").setExecutor(healCE);
		
		gamemodeCE = new GamemodeCE();
		getCommand("g").setExecutor(gamemodeCE);
		
		dungeonCE = new DungeonCE();
		getCommand("dungeon").setExecutor(dungeonCE);
		
		//Handle tasks that happen every 30 minutes. Delay'd by 5 seconds.
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run() 
			{
				//Start pvp event.
				pvp.begin();
			}
		}, 100, 36000);
		
		//We want to delete records of all players that haven't logged on in a while.
		rpgManager.clearOldPlayerData();
		
		//We want to perform promotion checks.
		this.getLogger().info("Enabled");
	}
	
	private void initializeDungeons()
	{
		//Variable Declarations/Initializations
		DungeonManager dm = new DungeonManager();
		String ref = "";
		int count = 0;
		trueDungeonNumbers = new HashMap<Integer, Integer>();
		
		//Attempt to store all dungeon names.
		for (int i = 0; i < 1000; i++)
		{
			ref = dm.getName(i);
			
			if (ref != null)
			{
				trueDungeonNumbers.put(i,count);
				count++;
			}
		}
		
		//Store number of dungeons.
		dungeonCount = trueDungeonNumbers.size();
		
		//Create dungeonEvents based on size of dungeons.
		dungeon = new DungeonEvent[dungeonCount];
		
		//Initate the dungeons.
		for (int i = 0; i < dungeonCount; i++)
			dungeon[i] = new DungeonEvent();
	}
	
	//Listen for block breaks.
	public class BlockBreakListener implements Listener
	{
		@EventHandler
		public void onBlockBreak(BlockBreakEvent event)
		{
			//Variable Declarations
			Player player = event.getPlayer();
			ItemStack handItem = player.getItemInHand();
			Material handItemType = handItem.getType();
			FC_RpgPermissions perms = new FC_RpgPermissions(player);
			String worldName = event.getBlock().getWorld().getName();
			
			//Prevent breaking of sponge blocks.
			if (event.getBlock().getType() == Material.SPONGE)
			{
				if (co.getPreventSpongeBreak() == true)
				{
					if (!perms.isAdmin())
						event.setCancelled(true);
				}
			}
			
			//Handle aoe world modifier for worlds.
			if (wm.getIsAoEWorld(worldName))
			{
				Block block = event.getBlock();
				World world = event.getBlock().getWorld();
				boolean breakSuccess = false;
				
				if (handItemType == Material.STONE_PICKAXE)
					breakSuccess = true;
				else if (handItemType == Material.IRON_PICKAXE)
					breakSuccess = true;
				else if (handItemType == Material.GOLD_PICKAXE)
					breakSuccess = true;
				else if (handItemType == Material.DIAMOND_PICKAXE)
					breakSuccess = true;
				
				if (breakSuccess == false)
					return;
				
				for (int x = block.getLocation().getBlockX() - 1; x < block.getLocation().getBlockX() + 2; x++)
				{
					for (int y = block.getLocation().getBlockY() - 1; y < block.getLocation().getBlockY() + 2; y++)
					{
						for (int z = block.getLocation().getBlockZ() - 1; z < block.getLocation().getBlockZ() + 2; z++)
						{
							if (world.getBlockAt(x,y,z).getType() != Material.BEDROCK)
								world.getBlockAt(x,y,z).breakNaturally();
						}
					}
				}
			}
			
			//If not an rpg world, don't continue execution.
			if (!wm.getIsRpgWorld(worldName))
				return;
			
			//If exp drops are globally cancelled.
			if (co.getExpCancelled() == true)
				event.setExpToDrop(0);	//Cancel experience drops.
			
			//Handle perfect wheat if enabled.
			if (co.getPerfectWheat() == true)
			{
				if (event.getBlock().getType().equals(Material.CROPS))
				{
					//Variable Declarations
					Random rand = new Random();
					ItemStack wheat = new ItemStack(Material.WHEAT, rand.nextInt(2) + 1);
					ItemStack seed = new ItemStack(Material.SEEDS, rand.nextInt(2));
					
					//If wheat is fully grown.
					if (event.getBlock().getData() == 7)
					{
						//Drop the wheat and seeds.
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), wheat);
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), seed);
						
						//Set the data back to 0.
						event.getBlock().setData((byte) 0);
					}
					
					if (!perms.isAdmin())
						event.setCancelled(true);
				}
			}
			
			//If infinite gold is enabled
			if (co.getInfiniteGold() == true)
			{
				//Prevent gold tools from breaking.
				if (handItemType == Material.GOLD_PICKAXE && handItem.getDurability() == 29)
					handItem.setDurability((short) 0);
				
				else if (handItemType == Material.GOLD_SPADE && handItem.getDurability() == 29)
					handItem.setDurability((short) 0);
				
				else if (handItemType == Material.GOLD_AXE && handItem.getDurability() == 29)
					handItem.setDurability((short) 0);
			}
			
			//If infinite diamond is enabled
			if (co.getInfiniteDiamond() == true)
			{
				//Prevent diamond tools from breaking.
				if (handItemType == Material.DIAMOND_PICKAXE && handItem.getDurability() == 1558)
					handItem.setDurability((short) 0);
				
				else if (handItemType == Material.DIAMOND_SPADE && handItem.getDurability() == 1558)
					handItem.setDurability((short) 0);
				
				else if (handItemType == Material.DIAMOND_AXE && handItem.getDurability() == 1558)
					handItem.setDurability((short) 0);
			}
		}
	}
	
	//Listen for block growth
	public class CreativeControlListeners implements Listener
	{
		@EventHandler
		public void onBlockBreak(StructureGrowEvent event)
		{
			//If perfect birch is disabled return.
			if (co.getPerfectBirch() == false)
				return;
			
			//For birch trees
			if (event.getSpecies() == TreeType.BIRCH)
			{
				//Store location and world.
				event.getBlocks().clear();
					
				Location blockLocation = event.getLocation();
				World world = Bukkit.getServer().getWorld(event.getWorld().getName());
				
				//Now we create a custom tree.
				for (int i = 0; i < 5; i++)
				{
					world.getBlockAt((int) blockLocation.getX(), (int) blockLocation.getY() + i, (int) blockLocation.getZ()).setType(Material.LOG);
					world.getBlockAt((int) blockLocation.getX(), (int) blockLocation.getY() + i, (int) blockLocation.getZ()).setData((byte) 2);
				}
			}
		}
		
		@EventHandler
		public void onBlockPlace(BlockPlaceEvent event)
		{
			//If creative control is disabled, return.
			if (co.getCreativeControl() == false)
				return;
			
			//Variable Declarations Pt. 2
			Player player = event.getPlayer();
			Block block = event.getBlock();
			FC_RpgPermissions perms = new FC_RpgPermissions(player);
			MessageLib msgLib;
			boolean success = true;
			
			//Make sure key variables aren't null.
			if (block == null || player == null)
				return;
			
			//Handle player messaging and admin overrides if player isn't null.
			msgLib = new MessageLib(player);
			
			//Blacklist items in the creative world.
			if (wm.isCreativeWorld(block.getWorld()))
			{
				if (block.getType().equals(Material.REDSTONE_WIRE))
					success = false;
				if (block.getType() == Material.REDSTONE)
					success = false;
				if (block.getType() == Material.TNT)
					success = false;
				else if (block.getType() == Material.DISPENSER)
					success = false;
				else if (block.getType() == Material.REDSTONE_TORCH_ON)
					success = false;
				else if (block.getType() == Material.REDSTONE_TORCH_OFF)
					success = false;
				else if (block.getType() == Material.DIODE)
					success = false;
				else if (block.getType() == Material.DIODE_BLOCK_OFF)
					success = false;
				else if (block.getType() == Material.DIODE_BLOCK_ON)
					success = false;
				else if (block.getType() == Material.WATER)
					success = false;
				else if (block.getType() == Material.LAVA)
					success = false;
				else if (block.getType() == Material.STATIONARY_LAVA)
					success = false;
				else if (block.getType() == Material.STATIONARY_WATER)
					success = false;
				else if (block.getType() == Material.ICE)
					success = false;
				else if (block.getType() == Material.SAPLING)
					success = false;
				else if (block.getType() == Material.FIRE)
					success = false;
			}
			
			//On fail, attempt to send message/override.
			if (success == false)
			{
				//Admin override
				if (perms.isAdmin())
				{
					msgLib.standardMessage("Admin Override Successful.");
					return;
				}
				
				//Inform the player that the event was canceled.
				msgLib.standardMessage("Reach Elite Rank or Ask An Admin To Place This.");
				event.setCancelled(true);	//Cancel the event
			}
		}
	}
	
	//Listen for creature spawns to block them.
	public class ProjectileLaunchListener implements Listener
	{
		@EventHandler
		public void onProjectileLaunch(ProjectileLaunchEvent event)
		{
			//If creative control is disabled, return.
			if (co.getCreativeControl() == false)
				return;
			
			//Check for thrown itmes that should be blocked.
			if (wm.isCreativeWorld(event.getEntity().getWorld()))
			{
				if (event.getEntityType() == EntityType.EGG)
					event.setCancelled(true);
				else if (event.getEntityType() == EntityType.THROWN_EXP_BOTTLE)
					event.setCancelled(true);
			}
		}
	}
	
	public class SignChangeListener implements Listener
	{
		@EventHandler
		public void onSignChange(SignChangeEvent event)
		{
			//Variable Declarations
			boolean fail = false;
			FC_RpgPermissions perms = new FC_RpgPermissions(event.getPlayer());
			MessageLib msgLib = new MessageLib(event.getPlayer());
			
			//Without adequate permission...
			if (!perms.isAdmin())
			{
				if (event.getLine(0).contains("Pick Class:"))
					fail = true;
				else if (event.getLine(0).contains("Pick Job:"))
					fail = true;
				else if (event.getLine(0).contains("Finish And"))
					fail = true;
				else if (event.getLine(0).contains("Teleport:"))
					fail = true;
				else if (event.getLine(0).contains("Refill Mana!"))
					fail = true;
				else if (event.getLine(1).contains("Dungeon"))
					fail = true;
			}
			
			//If they tried to change a sign without the proper permission, then ...
			if (fail == true)
			{
				msgLib.standardMessage("Key words detected on signs, edit cancelled.");
				event.setCancelled(true);
			}
		}
	}
	
	public class EntityDeathListener implements Listener
	{
		Player player;
		
		@EventHandler
		public void entityDeath(EntityDeathEvent event)
		{
			if (!wm.getIsRpgWorld(event.getEntity().getWorld().getName()))
				return;
			
			//If exp drops are cancelled, then...
			if (co.getExpCancelled() == true)
				event.setDroppedExp(0);	//Disable exp drops.
			
			//Store entity
			Entity entity = event.getEntity();
			
			//Do dungeon checking for mob deaths to see if dungeons are cleared.
			for (int i = 0; i < dungeonCount; i++)
				dungeon[i].checkMobDeath(entity);
			
			//Handle item loss if a player dies with hunger.
			if (entity instanceof Player)
			{
				//Store entity as player for fast access.
				player = (Player) entity;
				
				//If hardcore item loss is enabled, then...
				if (co.getHardcoreItemLoss() == true)
				{
					//If the player has 6 hearts or less, then...
					if (player.getFoodLevel() < 7)
						removeRandomItemFromPlayer();	//Remove a random item from the player.
					
					if (player.getFoodLevel() < 2)
						removeRandomItemFromPlayer();	//Remove a random item from the player.
				}
				
				//Remove from any dungeons.
				for (int i = 0; i < dungeonCount; i++)
					dungeon[i].removeDungeoneer(player,player,false);
			}
			
			//Clear drops for mobs
			else if (entity instanceof LivingEntity)
			{
				//Recycle positions of killed monsters so as to not use as much memory.
				rpgManager.unregisterRpgMonster((LivingEntity) entity);
				
				//Remove item drops from mobs.
				event.getDrops().clear();
			}
		}
		
		public void removeRandomItemFromPlayer()
		{
			//Variable Declarations
			boolean hasItem = false;
			int playerInventorySize = player.getInventory().getSize();
			
			//We want to return if the players inventory is all air
			for (int i = 0; i < playerInventorySize; i++)
			{
				if (player.getInventory().getItem(i) != null)
				{
					if (player.getInventory().getItem(i).getType() == Material.AIR)
					{
						i = playerInventorySize;
						hasItem = true;
					}
				}
			}
			
			//If the player doesn't have any items, return false.
			if (hasItem == false)
				return;
			
			//Remove the item from teh player.
			removeItem();
		}
		
		public void removeItem()
		{
			ItemStack air = new ItemStack(Material.AIR);
			Random remove = new Random();
			int removePosition;
			int playerInventorySize = player.getInventory().getSize();
			
			//Remove an item
			removePosition = remove.nextInt(playerInventorySize);
			
			while (player.getInventory().getItem(removePosition).getType() == Material.AIR)
			{
				removePosition = remove.nextInt(playerInventorySize);
			}
			
			player.getInventory().setItem(removePosition, air);
		}
	}
	
	//Cancel loss of durability while fishing
	public class FishingListener implements Listener
	{
		@EventHandler
		public void onFish(PlayerFishEvent event)
		{
			if (co.getBetterFishing() == true)
			{
				Player player = (Player) event.getPlayer();
				
				//Make fishing rods infinite.
				if (player.getItemInHand().getType() == Material.FISHING_ROD)
					player.getItemInHand().setDurability((short) 0);
			}
		}
	}
	
	//Handle rpgPlayer related logon events.
	public class PlayerJoinListener implements Listener
	{
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event)
		{
			//Variable Declarations
			Player player = (Player) event.getPlayer();
			
			//If the player is null, we want to return.
			if (player == null)
				return;
			
			//Get the players file.
			RpgPlayerFile playerFile = new RpgPlayerFile(player.getName());
			
			//If they aren't active, then we want to make them active by creating their rpg player.
			if (playerFile.getIsActive() == false)
				FC_Rpg.rpgManager.setPlayerStart("Swordsman", player, false);	//Store the player as a new player.
			
			//Update the player file.
			playerFile.handleUpdates();
		}
	}
	
	public class PlayerQuitListener implements Listener
	{
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event)
		{
			//Variable Declaration
			Player p = event.getPlayer();
			
			//Remove from any potential pvp events they may be in.
			if (pvp.isEventPlayer(p))
				pvp.removePvper(p, p,false);
			
			//Unregister the player from the object pool.
			rpgManager.unregisterRpgPlayer(p);
			
			//Kick them from their party.
			partyManager.removeMemberFromAllParties(p.getName());
			
			//Remove from any dungeons.
			for (int i = 0; i < dungeonCount; i++)
				dungeon[i].removeDungeoneer(p,p,false);
		}
	}
	
	public class RespawnListener implements Listener
	{
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onRespawn(final PlayerRespawnEvent event)
		{
			//Variable Declarations
			final Player player = event.getPlayer();
			
			Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					RpgPlayer rpgPlayer = rpgManager.getRpgPlayer(player);
					Location spectatorBooth = new Location(wm.getSpawnWorld(),177,99,74,90,0);
					
					//If the player is in a pvp event, we handle the pvp event.
					if (pvp.isHappening() == true && pvp.isEventPlayer(player) == true)
					{
						//Set that the player has lost.
						pvp.setHasLostTrue(player);
						
						//Change respawn location to spectator booth.
						event.setRespawnLocation(spectatorBooth);
					}
					
					//Reset rpg player stuff.
					rpgPlayer.setIsAlive(true);
					rpgPlayer.setHunterCanKit(true);
					rpgPlayer.healFull();
				}
			}, 1L);
		}
	}

	public class HealthRegenListener implements Listener
	{
		@EventHandler
		public void onHealthRegen(EntityRegainHealthEvent event)
		{
			//Variable Declaration
			Player player = null;
			Entity entity = event.getEntity();
			double magnitudeModifier = 0;
			DistanceModifierLib dmm = null;
			HealthConverter hc = null;
			RpgPlayer rpgPlayer;
			WorldManager wm = new WorldManager();
			
			if (wm.getIsRpgWorld(entity.getWorld().getName()))
				return;
			
			//Deal damage to the defender but the defender isn't type Player. The defender is type RpgEntity.
			if (entity instanceof Player)
			{
				player = (Player) entity;
				
				//Cancel the event.
				event.setCancelled(true);
				
				//Set rpgPlayer
				rpgPlayer = rpgManager.getRpgPlayer(player);
				
				//Set the magnitude to heal by the players max health.
				magnitudeModifier = rpgPlayer.getMaxHealth();
				
				//Heal the player different amounts for different things.
				if (event.getRegainReason() == RegainReason.EATING)
				{
					magnitudeModifier = magnitudeModifier / 20; //5%
				}
				else if (event.getRegainReason() == RegainReason.MAGIC)
				{
					magnitudeModifier = magnitudeModifier / 4; //25%
				}
				else if (event.getRegainReason() == RegainReason.MAGIC_REGEN)
				{
					magnitudeModifier = magnitudeModifier / 20; //5%
				}
				else if (event.getRegainReason() == RegainReason.SATIATED)
				{
					magnitudeModifier = magnitudeModifier / 50; //2%
				}
				
				//Make sure to heal at least one
				if (magnitudeModifier < 1)
					magnitudeModifier = 1;
				
				//Restore health/mana to the player
				rpgPlayer.restoreHealth(magnitudeModifier);
				
				//Update health
				hc = new HealthConverter(rpgManager.getRpgPlayer(player).getMaxHealth(),
						rpgManager.getRpgPlayer(player).getCurHealth());
				
				player.setHealth(hc.getMinecraftHearts());
			}
			else if (entity instanceof LivingEntity)
			{
				//Cancel the event.
				event.setCancelled(true);
				
				//Find the attacker and deal damage from the attack to the defender.
				dmm = new DistanceModifierLib();
				
				//Get distance magnitude modifier;
				magnitudeModifier = dmm.getWorldDML(entity.getLocation());
				
				//Increase the heals by a lot.
				if (event.getRegainReason() == RegainReason.ENDER_CRYSTAL)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) != null)
						rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(MAX_HP);
				}

				if (event.getRegainReason() == RegainReason.MAGIC)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) != null)
						rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier * 5);
				}

				if (event.getRegainReason() == RegainReason.MAGIC_REGEN)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) != null)
						rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier);
				}
			}
		}
	}
	
	public class ChatListener implements Listener //<%1$s> %2$s = base format
	{
		@EventHandler
		public void onChat(AsyncPlayerChatEvent event)
		{
			//Variable Declaration
			Player player = event.getPlayer();
			RpgPlayer rpgPlayer = rpgManager.getRpgPlayer(event.getPlayer());
			FC_RpgPermissions perms = new FC_RpgPermissions(player);
			DateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			ChatColor messageColor = ChatColor.GRAY;
			ChatColor secondaryColor = ChatColor.GRAY;
			
			//Change features for admins.
			if (perms.isAdmin() == true)
			{
				event.setMessage(cl.parseColors(event.getMessage()));
				messageColor = ChatColor.YELLOW;
				secondaryColor = ChatColor.WHITE;
			}
			
			//Set the message format.
			event.setFormat(messageColor + timeStamp.format(now) + " " + rpgPlayer.updatePrefix() + messageColor + rpgPlayer.getName() + secondaryColor + ": " + "%2$s");
		}
	}
	
	//Cancel out poison damage
	public class EntityDamageListener implements Listener
	{
		@EventHandler
		public void OnEntityDamage(EntityDamageEvent event)
		{
			if (!wm.getIsRpgWorld(event.getEntity().getWorld().getName()))
				return;
			
			if (event.getCause() == DamageCause.POISON)
				event.setCancelled(true);
		}
	}
	
	public class CreatureSpawnListener implements Listener
	{
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onCreaturespawn(CreatureSpawnEvent event)
		{
			if (!wm.getIsMobWorld(event.getEntity().getWorld().getName()) == false)
				return;
			
			//Remove from any dungeons.
			for (int i = 0; i < dungeonCount; i++)
			{
				if (dungeon[i].isInsideDungeon(event.getLocation()))
				{
					//Prevent mobs spawned for certain reasons.
					if (event.getSpawnReason() == SpawnReason.NATURAL)
					{
						event.setCancelled(true);
						return;
					}
					else if (event.getSpawnReason() == SpawnReason.DEFAULT)
					{
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	public class ExperienceChangeListener implements Listener
	{
		@EventHandler
		public void PlayerExpChangeEvent(PlayerExpChangeEvent event)
		{
			//If exp disabled is disabled.
			if (co.getExpCancelled() == false)
				return;
			
			//Variable declarations
			Player player = event.getPlayer();
			
			if (!wm.getIsRpgWorld(player.getWorld().getName()))
				return;
			
			//Change everything we can to remove player experience.
			event.setAmount(0);
			player.setLevel(0);
			player.setExp(0);
		}
	}
	
	public class ArrowFireListener implements Listener
	{
		@EventHandler
		public void ArrowFireEvent(EntityShootBowEvent event)
		{
			//Don't perform this event in non-rpg worlds.
			if (!wm.getIsRpgWorld(event.getEntity().getWorld().getName()))
				return;
			
			//Get the arrow from the event.
			final Arrow arrow = (Arrow) event.getProjectile();
			Vector speed;
			
			//Make it so that arrows don't bounce.
			arrow.setBounce(false);
			
			//Remove fired arrows after 60 seconds. Very nice cleanup function. :)
			Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				public void run()
				{
					arrow.remove();
				}
			}, 1200);
			
			//Now we want to check if a player is casting a spell.
			if (!(event.getEntity() instanceof Player))
				return;
			
			//Get rpgPlayer.
			RpgPlayer rpgPlayer = FC_Rpg.rpgManager.getRpgPlayer((Player) event.getEntity());
			
			//Assassins shoot arrows faster passively.
			if (rpgPlayer.getCombatClass() == FC_Rpg.c_int_assassin)
			{
				speed = arrow.getVelocity().multiply((rpgPlayer.getClassLevel() / 28) + 1);
				arrow.setVelocity(speed);
			}
			
			//If auto-cast is enabled.
			if (rpgPlayer.getAutoCast() == true)
				rpgPlayer.prepareSpell(false);	//Prepare the spell.
		}
	}
	
	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
}


