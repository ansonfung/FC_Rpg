package me.Destro168.FC_Rpg;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.Destro168.FC_Rpg.Configs.BalanceConfig;
import me.Destro168.FC_Rpg.Configs.ClassConfig;
import me.Destro168.FC_Rpg.Configs.DungeonConfig;
import me.Destro168.FC_Rpg.Configs.FaqConfig;
import me.Destro168.FC_Rpg.Configs.GeneralConfig;
import me.Destro168.FC_Rpg.Configs.GroupConfig;
import me.Destro168.FC_Rpg.Configs.GuildConfig;
import me.Destro168.FC_Rpg.Configs.ItemConfig;
import me.Destro168.FC_Rpg.Configs.SpellConfig;
import me.Destro168.FC_Rpg.Configs.TreasureConfig;
import me.Destro168.FC_Rpg.Configs.WarpConfig;
import me.Destro168.FC_Rpg.Configs.WorldConfig;
import me.Destro168.FC_Rpg.Entities.RpgEntityManager;
import me.Destro168.FC_Rpg.Entities.RpgMonster;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.Events.DungeonEvent;
import me.Destro168.FC_Rpg.Events.PvpEvent;
import me.Destro168.FC_Rpg.Listeners.BlockBreakListener;
import me.Destro168.FC_Rpg.Listeners.DamageListener;
import me.Destro168.FC_Rpg.Listeners.PlayerInteractionListener;
import me.Destro168.FC_Rpg.LoadedObjects.RpgItem;
import me.Destro168.FC_Rpg.Util.BattleCalculations;
import me.Destro168.FC_Rpg.Util.DistanceModifierLib;
import me.Destro168.FC_Rpg.Util.FC_RpgPermissions;
import me.Destro168.FC_Rpg.Util.HealthConverter;
import me.Destro168.FC_Rpg.Util.MaterialLib;
import me.Destro168.FC_Rpg.Util.RpgBroadcast;
import me.Destro168.FC_Suite_Shared.AutoUpdate;
import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.FC_Suite_Shared.SelectionVector;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;
// import me.Destro168.qQuest.MinimumRPGLevelRequirement;
// import me.quaz3l.qQuests.qQuests;
import me.quaz3l.qQuests.API.QuestAPI;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.Vector;

public class FC_Rpg extends JavaPlugin
{
	//Public global variables.
	public static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static final DecimalFormat df = new DecimalFormat("#.#");
	public static final DecimalFormat df2 = new DecimalFormat("#.##");
	public static final DecimalFormat df3 = new DecimalFormat("#.###");
	public QuestAPI qAPI = null;
	
	//Uncommon class globals.
	public static FC_Rpg plugin;
	public static RpgEntityManager rpgEntityManager;
	public static GuildConfig guildConfig;
	public static SpellConfig spellConfig;
	public static ClassConfig classConfig;
	public static GeneralConfig generalConfig;
	public static TreasureConfig treasureConfig;
	public static WarpConfig warpConfig;
	public static BalanceConfig balanceConfig;
	public static DungeonConfig dungeonConfig;
	public static ItemConfig itemConfig;
	public static PvpEvent pvp;
	public static RpgBroadcast rpgBroadcast;
	public static WorldConfig worldConfig;
	public static Economy economy = null;
	public static DungeonEvent[] dungeonEventArray;
	public static SelectionVector sv;
	public static MaterialLib mLib;
	public static BattleCalculations battleCalculations;
	public static MobSpawnManager msm;
	public static ColorLib cl = new ColorLib();
	
	//Common class globals.
	public static Map<Player, String> classSelection = new HashMap<Player, String>();
	public static Map<Integer, Integer> trueDungeonNumbers;
	public static Map<Player, Boolean> aoeLock = new HashMap<Player, Boolean>();
	public static Map<Player, Integer> playerBuffTimerTIDs = new HashMap<Player, Integer>();
	public static Map<Player, Integer> playerSlowTimerTIDs = new HashMap<Player, Integer>();
	public static Map<Material, Double> recordExpBonuses;
	public static Map<Player, Boolean> playerChatModeMap = new HashMap<Player, Boolean>();
	public static List<String> vanishedPlayers = new ArrayList<String>();
	public static List<RpgItem> rpgItemList;
	public static List<RpgItem> buyList;
	public static String dataFolderAbsolutePath;
	public static int eventExpMultiplier = 1;
	public static int eventCashMultiplier = 1;
	public static int tid3;
	public static int tid4;
	public static int dungeonCount;
	
	//Private variables.
	private final double MAX_HP = 999999;
	private CommandGod commandCE;
	
	@Override
	public void onDisable()
	{
		// Cancel all the tasks of this plugin.
		plugin.getServer().getScheduler().cancelTasks(plugin);
		
		// Save all players times for being logged on.
		for (Player player : Bukkit.getOnlinePlayers())
		{
			rpgEntityManager.unregisterRpgPlayer(player);
			msm.endMobSpawns(player);
			player.setWalkSpeed(0.2F);
		}
		
		for (int i = 0; i < dungeonCount; i++)
		{
			if (dungeonEventArray[FC_Rpg.trueDungeonNumbers.get(i)].isHappening() == true)
				dungeonEventArray[FC_Rpg.trueDungeonNumbers.get(i)].end(false);
		}
		
		for (String s : vanishedPlayers)
		{
			Player t = Bukkit.getServer().getPlayer(s);
			
			if (t != null)
				t.sendMessage(ChatColor.RED + "[FC_RPG] Use /rpg vanish to rehide.");
		}
		
		this.getLogger().info("Disabled");
	}
	
	@Override
	public void onEnable()
	{
		// Derp
		plugin = this;
		dataFolderAbsolutePath = FC_Rpg.plugin.getDataFolder().getAbsolutePath();
		
		// World manager = new world manager;
		mLib = new MaterialLib();
		sv = new SelectionVector();
		
		battleCalculations = new BattleCalculations();
		generalConfig = new GeneralConfig();
		
		//Initialize a few things to let them attempt to generate configurations.
		@SuppressWarnings("unused")
		FaqConfig fm = new FaqConfig();
		
		@SuppressWarnings("unused")
		GroupConfig gm = new GroupConfig();
		
		worldConfig = new WorldConfig();
		rpgBroadcast = new RpgBroadcast();
		guildConfig = new GuildConfig();
		pvp = new PvpEvent();
		spellConfig = new SpellConfig();
		classConfig = new ClassConfig();
		itemConfig = new ItemConfig();
		handleRpgItemList();	//Have to do item config and rpgItem list before treasure config.
		treasureConfig = new TreasureConfig();
		warpConfig = new WarpConfig();
		balanceConfig = new BalanceConfig();
		dungeonConfig = new DungeonConfig();
		
		// Set up the economy.
		setupEconomy();
		
		// Initialize dungeons.
		reloadDungeons();
		
		// Register Listeners
		getServer().getPluginManager().registerEvents(new FishingListener(), plugin);
		getServer().getPluginManager().registerEvents(new HealthRegenListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
		getServer().getPluginManager().registerEvents(new RespawnListener(), plugin);
		getServer().getPluginManager().registerEvents(new DamageListener(), plugin);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(), plugin);
		
		// Register chat listener if chat is enabled.
		if (!generalConfig.getChatFormat().equalsIgnoreCase("") && !generalConfig.getChatFormatAdmin().equalsIgnoreCase(""))
			getServer().getPluginManager().registerEvents(new AyncPlayerChatListener(), plugin);
		
		getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
		getServer().getPluginManager().registerEvents(new SignChangeListener(), plugin);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(), plugin);
		getServer().getPluginManager().registerEvents(new CreativeControlListeners(), plugin);
		getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(), plugin);
		getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), plugin);
		getServer().getPluginManager().registerEvents(new ExperienceChangeListener(), plugin);
		getServer().getPluginManager().registerEvents(new ArrowFireListener(), plugin);
		getServer().getPluginManager().registerEvents(new TeleportListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), plugin);
		getServer().getPluginManager().registerEvents(new EnchantEventListener(), plugin);
		
		// Register Commands
		commandCE = new CommandGod();
		
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordClass()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordDonator()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordDungeon()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordDE()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordFaq()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordG()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordH()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordGH()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordHG()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordJob()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordGuild()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordPvp()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordReset()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordRpg()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordRAdmin()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordSpell()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordAlchemy()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordModify()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordW()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordBuff()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordWorld()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordHead()).setExecutor(commandCE);
		getCommand(FC_Rpg.generalConfig.getCommandKeyWordPlayers()).setExecutor(commandCE);
		
		// Handle tasks that happen every 30 minutes. Delay'd by 5 seconds.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				pvp.begin(); // Start pvp event.
			}
		}, 100, 36000);
		
		// Start an rpg manager.
		rpgEntityManager = new RpgEntityManager();

		// We want to delete records of all players that haven't logged on in a while.
		rpgEntityManager.clearOldPlayerData();
		
		msm = new MobSpawnManager();
		
		if (FC_Rpg.generalConfig.getRecordExpRewards())
			handleRecordExpMap();
		
		// Handle autoupdate.
		try {
			new AutoUpdate(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Call qQuest questing API.
		// this.setupQQuests();
		
		// quaz3l's code
		// if (this.qAPI != null)
	    //     this.qAPI.getRequirementHandler().addRequirement(new MinimumRPGLevelRequirement());
		
		// We want to perform promotion checks.
		this.getLogger().info("Enabled");
	}
	
	private void handleRecordExpMap()
	{
		recordExpBonuses = new HashMap<Material, Double>();
		
		recordExpBonuses.put(Material.RECORD_4, .01D);
		recordExpBonuses.put(Material.RECORD_5, .05D);
		recordExpBonuses.put(Material.RECORD_6, .1D);
		recordExpBonuses.put(Material.RECORD_7, .15D);
		recordExpBonuses.put(Material.RECORD_8, .20D);
		recordExpBonuses.put(Material.RECORD_9, .25D);
		recordExpBonuses.put(Material.GREEN_RECORD, .5D);
		recordExpBonuses.put(Material.GOLD_RECORD, 1D);
	}
	
	public static void reloadDungeons()
	{
		// Variable Declarations/Initializations
		int count = 0;
		trueDungeonNumbers = new HashMap<Integer, Integer>();
		
		// Attempt to store all dungeon names.
		for (int i = 0; i < 1000; i++)
		{
			if (dungeonConfig.getNameIsSet(i) == true)
			{
				trueDungeonNumbers.put(i, count);
				count++;
			}
		}
		
		// Store number of dungeons.
		dungeonCount = trueDungeonNumbers.size();

		// Create dungeonEvents based on size of dungeons.
		dungeonEventArray = new DungeonEvent[FC_Rpg.dungeonCount];

		// Initate the dungeons.
		for (int i = 0; i < FC_Rpg.dungeonCount; i++)
			dungeonEventArray[i] = new DungeonEvent(i);
	}
	
	// Listen for block growth
	public class CreativeControlListeners implements Listener
	{
		@EventHandler
		public void onBlockBreak(StructureGrowEvent event)
		{
			// If perfect birch is disabled return.
			if (generalConfig.getPerfectBirch() == false)
				return;
			
			// Multi-world check for perfrect birch.
			if (!FC_Rpg.worldConfig.getIsRpg(event.getLocation().getWorld().getName()))
				return;
			
			// For birch trees
			if (event.getSpecies() == TreeType.BIRCH)
			{
				// Store location and world.
				event.getBlocks().clear();

				Location blockLocation = event.getLocation();
				World world = Bukkit.getServer().getWorld(event.getWorld().getName());

				// Now we create a custom tree.
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
			// If creative control is disabled, return.
			if (generalConfig.getCreativeControl() == false)
				return;

			// Variable Declarations Pt. 2
			Player player = event.getPlayer();
			Block block = event.getBlock();
			FC_RpgPermissions perms = new FC_RpgPermissions(player);
			MessageLib msgLib;
			boolean success = true;

			// Make sure key variables aren't null.
			if (block == null || player == null)
				return;

			// Handle player messaging and admin overrides if player isn't null.
			msgLib = new MessageLib(player);

			// Blacklist items in the creative world.
			if (worldConfig.isCreativeWorld(block.getWorld()))
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

			// On fail, attempt to send message/override.
			if (success == false)
			{
				// Admin override
				if (perms.isAdmin())
				{
					msgLib.standardMessage("Admin Override Successful.");
					return;
				}

				// Inform the player that the event was canceled.
				msgLib.standardMessage("Ask An Admin To Place This.");
				event.setCancelled(true); // Cancel the event
			}
		}
	}

	// Listen for creature spawns to block them.
	public class ProjectileLaunchListener implements Listener
	{
		@EventHandler
		public void onProjectileLaunch(ProjectileLaunchEvent event)
		{
			// If creative control is disabled, return.
			if (generalConfig.getCreativeControl() == false)
				return;

			// Check for thrown itmes that should be blocked.
			if (worldConfig.isCreativeWorld(event.getEntity().getWorld()))
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
			// Variable Declarations
			boolean fail = false;
			FC_RpgPermissions perms = new FC_RpgPermissions(event.getPlayer());
			MessageLib msgLib = new MessageLib(event.getPlayer());

			// Without adequate permission...
			if (!perms.isAdmin())
			{
				if (event.getLine(0).contains("Pick Class:"))
					fail = true;
				else if (event.getLine(0).contains("Teleport:"))
					fail = true;
				else if (event.getLine(0).contains("Refill Mana!"))
					fail = true;
				else if (event.getLine(1).contains("Dungeon"))
					fail = true;
			}

			// If they tried to change a sign without the proper permission, then ...
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
			// Store entity
			Entity entity = event.getEntity();
			
			// Do dungeon checking for mob deaths to see if dungeons are cleared.
			for (int i = 0; i < dungeonCount; i++)
				dungeonEventArray[i].checkMobDeath(entity);

			if (!worldConfig.getIsRpgWorld(entity.getWorld().getName()))
				return;
			
			// Handle item loss if a player dies with hunger.
			if (entity instanceof Player)
			{
				// Store entity as player for fast access.
				player = (Player) entity;

				// If hardcore item loss is enabled, then...
				if (generalConfig.getHardcoreItemLoss() == true)
				{
					// If the player has 6 hearts or less, then...
					if (player.getFoodLevel() < 7)
						removeRandomItemFromPlayer(); // Remove a random item from the player.

					if (player.getFoodLevel() < 2)
						removeRandomItemFromPlayer(); // Remove a random item from the player.
				}

				// Remove from any dungeons.
				for (int i = 0; i < dungeonCount; i++)
					dungeonEventArray[i].removeDungeoneer(player, player, false);
			}
			
			// Clear drops for mobs
			else if (entity instanceof LivingEntity)
			{
				// Recycle positions of killed monsters so as to not use as much memory.
				rpgEntityManager.unregisterRpgMonster((LivingEntity) entity);

				if (FC_Rpg.balanceConfig.getDefaultItemDrops() == false)
					event.getDrops().clear(); // Remove item drops from mobs.
				else
				{
					Random rand = new Random();
					
					if (rand.nextInt(100) < 10)
						event.getDrops().clear();
				}
			}
		}

		public void removeRandomItemFromPlayer()
		{
			// Variable Declarations
			boolean hasItem = false;
			int playerInventorySize = player.getInventory().getSize();

			// We want to return if the players inventory is all air
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

			// If the player doesn't have any items, return false.
			if (hasItem == false)
				return;

			// Remove the item from teh player.
			removeItem();
		}

		public void removeItem()
		{
			ItemStack air = new ItemStack(Material.AIR);
			Random remove = new Random();
			int removePosition;
			int playerInventorySize = player.getInventory().getSize();

			// Remove an item
			removePosition = remove.nextInt(playerInventorySize);

			while (player.getInventory().getItem(removePosition).getType() == Material.AIR)
			{
				removePosition = remove.nextInt(playerInventorySize);
			}

			player.getInventory().setItem(removePosition, air);
		}
	}

	// Cancel loss of durability while fishing
	public class FishingListener implements Listener
	{
		@EventHandler
		public void onFish(PlayerFishEvent event)
		{
			if (generalConfig.getBetterFishing() == true)
			{
				Player player = (Player) event.getPlayer();

				// Make fishing rods infinite.
				if (player.getItemInHand().getType() == Material.FISHING_ROD)
					player.getItemInHand().setDurability((short) 0);
			}
		}
	}

	// Handle rpgPlayer related logon events.
	public class PlayerJoinListener implements Listener
	{
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event)
		{
			// Variable Declarations
			final Player player = (Player) event.getPlayer();
			
			// If the player is null, we want to return.
			if (player == null)
				return;
			
			if (!FC_Rpg.worldConfig.getIsRpgWorld(player.getWorld().getName()))
				return;
			
			// Register the player if needed.
			FC_Rpg.rpgEntityManager.checkPlayerRegistration(player, true);
			
			msm.beginMobSpawns(player);
			
			player.setWalkSpeed(0.2F);
		}
	}

	public class PlayerQuitListener implements Listener
	{
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event)
		{
			// Variable Declaration
			Player player = event.getPlayer();

			// Remove from any potential pvp events they may be in.
			if (pvp.isEventPlayer(player))
				pvp.removePvper(player, player, false);

			// Unregister the player from the object pool.
			rpgEntityManager.unregisterRpgPlayer(player);

			// Remove from any dungeons.
			for (int i = 0; i < dungeonCount; i++)
				dungeonEventArray[i].removeDungeoneer(player, player, false);
			
			msm.endMobSpawns(player);
		}
	}
	
	public class RespawnListener implements Listener
	{
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onRespawn(final PlayerRespawnEvent event)
		{
			// Variable Declarations
			final Player player = event.getPlayer();

			if (!FC_Rpg.worldConfig.getIsRpgWorld(event.getPlayer().getWorld().getName()))
				return;
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					RpgPlayer rpgPlayer = rpgEntityManager.getRpgPlayer(player);
					Location spectatorBooth = new Location(worldConfig.getSpawnWorld(), 177, 99, 74, 90, 0);

					// If the player is in a pvp event, we handle the pvp event.
					if (pvp.isHappening() == true && pvp.isEventPlayer(player) == true)
					{
						// Set that the player has lost.
						pvp.setHasLostTrue(player);

						// Change respawn location to spectator booth.
						event.setRespawnLocation(spectatorBooth);
					}

					// Reset rpg player stuff.
					rpgPlayer.setIsAlive(true);
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
			// Variable Declaration
			Player player = null;
			Entity entity = event.getEntity();
			double magnitudeModifier = 0;
			DistanceModifierLib dmm = null;
			HealthConverter hc = null;
			WorldConfig wm = new WorldConfig();
			
			if (!wm.getIsRpgWorld(entity.getWorld().getName()))
				return;
			
			// Deal damage to the defender but the defender isn't type Player. The defender is type RpgEntity.
			if (entity instanceof Player)
			{
				player = (Player) entity;

				// Cancel the event.
				event.setCancelled(true);
				
				// Set the magnitude to heal by the players max health.
				magnitudeModifier = rpgEntityManager.getRpgPlayer(player).playerConfig.maxHealth;
				
				// Heal the player different amounts for different things.
				if (event.getRegainReason() == RegainReason.EATING)
				{
					magnitudeModifier *= FC_Rpg.balanceConfig.getHealPercentEating(); // 5%
				}
				else if (event.getRegainReason() == RegainReason.MAGIC)
				{
					magnitudeModifier *= FC_Rpg.balanceConfig.getHealPercentMagic(); // 25%
				}
				else if (event.getRegainReason() == RegainReason.MAGIC_REGEN)
				{
					magnitudeModifier *= FC_Rpg.balanceConfig.getHealPercentMagicRegen(); // 5%
				}
				else if (event.getRegainReason() == RegainReason.SATIATED)
				{
					magnitudeModifier *= FC_Rpg.balanceConfig.getHealPercentSatiated(); // 2%
				}
				
				// Make sure to heal at least one
				if (magnitudeModifier < 1)
					magnitudeModifier = 1;
				
				// Restore health/mana to the player
				rpgEntityManager.getRpgPlayer(player).healHealth(magnitudeModifier);
				
				// Update health
				hc = new HealthConverter(rpgEntityManager.getRpgPlayer(player).playerConfig.maxHealth, rpgEntityManager.getRpgPlayer(player).playerConfig.curHealth);
				
				player.setHealth(hc.getPlayerHearts());
			}
			else if (entity instanceof LivingEntity)
			{
				// Cancel the event.
				event.setCancelled(true);
				
				// Find the attacker and deal damage from the attack to the defender.
				dmm = new DistanceModifierLib();

				// Get distance magnitude modifier;
				magnitudeModifier = dmm.getWorldDML(entity.getLocation());

				// Increase the heals by a lot.
				if (event.getRegainReason() == RegainReason.ENDER_CRYSTAL)
				{
					if (rpgEntityManager.getRpgMonster((LivingEntity) entity) != null)
						rpgEntityManager.getRpgMonster((LivingEntity) entity).restoreHealth(MAX_HP);
				}
				
				else if (event.getRegainReason() == RegainReason.MAGIC)
				{
					if (rpgEntityManager.getRpgMonster((LivingEntity) entity) != null)
						rpgEntityManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier * 5);
				}
				
				else if (event.getRegainReason() == RegainReason.MAGIC_REGEN)
				{
					if (rpgEntityManager.getRpgMonster((LivingEntity) entity) != null)
						rpgEntityManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier);
				}
				
				else if (event.getRegainReason() == RegainReason.WITHER_SPAWN)
				{
					if (rpgEntityManager.getRpgMonster((LivingEntity) entity) == null)
						rpgEntityManager.registerCustomLevelEntity((LivingEntity) entity, 0, 50, true);
					
					rpgEntityManager.getRpgMonster((LivingEntity) entity).restoreHealth(999999999);
					((LivingEntity) entity).setHealth(300);
				}
				
				else if (event.getRegainReason() == RegainReason.WITHER)
				{
					if (rpgEntityManager.getRpgMonster((LivingEntity) entity) != null)
						rpgEntityManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier);
				}
			}
		}
	}

	public class AyncPlayerChatListener implements Listener // <%1$s> %2$s = base format
	{
		@EventHandler
		public void onChat(AsyncPlayerChatEvent event)
		{
			if (!FC_Rpg.worldConfig.getIsRpgWorld(event.getPlayer().getWorld().getName()))
				return;
			
			for (String s : FC_Rpg.generalConfig.getCustomChatExclusions())
			{
				if (event.getFormat().contains(s))
					return;
			}
			
			RpgPlayer rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer(event.getPlayer());
			
			if (rpgPlayer == null)
				return;
			
			Player player = event.getPlayer();
			FC_RpgPermissions perms = new FC_RpgPermissions(player);
			DateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			String chatFormat;
			
			if (perms.chatAdmin())
				chatFormat = FC_Rpg.generalConfig.getChatFormatAdmin();
			else
				chatFormat = FC_Rpg.generalConfig.getChatFormat();
			if (chatFormat.equalsIgnoreCase(""))
				return;
			
			chatFormat = chatFormat.replaceAll("%time%", timeStamp.format(now.getTime()));
			chatFormat = chatFormat.replaceAll("%prefix%", rpgPlayer.updatePrefix());
			
			String name = rpgPlayer.playerConfig.getName();
			
			chatFormat = chatFormat.replaceAll("%name%", name);
			chatFormat = chatFormat.replaceAll("%chat%", "%2\\$s");
			chatFormat = chatFormat.replaceAll("%level%", rpgPlayer.playerConfig.getClassLevel() + "");
			
			event.setFormat(FC_Rpg.cl.parse(chatFormat));
		}
	}
	
	public class CreatureSpawnListener implements Listener
	{
		RpgMonster m;
		LivingEntity livingEntity;
		boolean isBoss;
		
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onCreaturespawn(CreatureSpawnEvent event)
		{
			if (event.isCancelled() == true)
				return;
			
			String worldName = event.getEntity().getWorld().getName();
			
			//If not an rpg world, cancel.
			if (!FC_Rpg.worldConfig.getIsRpgWorld(worldName))
				return;
			
			//If not a mob world, cancel.
			if (FC_Rpg.worldConfig.getIsMobWorld(worldName))
				event.setCancelled(true);
			
			// Prevent dungeon mob spawns.
			for (int i = 0; i < dungeonCount; i++)
			{
				if (dungeonEventArray[i].isInsideDungeon(event.getLocation()))
				{
					// Prevent mobs spawned for certain reasons.
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
			
			//Uncancel the event if it's a dungeon monster.
			event.setCancelled(false);
			
			//Initialize variables
			livingEntity = event.getEntity();
			m = FC_Rpg.rpgEntityManager.getRpgMonster(livingEntity);
			isBoss = m.getIsBoss();
			
			if (event.getEntity().getType() == EntityType.ZOMBIE)
			{
				setArmor();
				setWeapon();
			}
			else if (event.getEntity().getType() == EntityType.PIG_ZOMBIE)
			{
				setArmor();
				setWeapon();
			}
			else if (event.getEntity().getType() == EntityType.SKELETON)
			{
				setArmor();
				livingEntity.getEquipment().setItemInHand(getBow());
			}
		}
		
		private void setArmor()
		{
			EntityEquipment equip = livingEntity.getEquipment();
			
			equip.setHelmet(getItem(6));
			equip.setBoots( getItem(5));
			equip.setChestplate(getItem(8));
			equip.setLeggings(getItem(7));
		}
		
		private void setWeapon()
		{
			livingEntity.getEquipment().setItemInHand(getItem(4));
		}
		
		private ItemStack getItem(int refNumber)
		{
			Material material;
			Random rand = new Random();
			int monsterLevel = m.getLevel();
			
			//Bosses always spawn armor.
			if (!isBoss)
			{
				if (rand.nextInt(100) >= FC_Rpg.balanceConfig.getMobSpawnWithItemChance())
					return null;
			}
			
			if (monsterLevel <= 19)
				material = FC_Rpg.mLib.t0.get(refNumber);
			else if (monsterLevel > 19 && monsterLevel <= 39)
				material = FC_Rpg.mLib.t1.get(refNumber);
			else if (monsterLevel > 39 && monsterLevel <= 59)
				material = FC_Rpg.mLib.t2.get(refNumber);
			else if (monsterLevel > 59 && monsterLevel <= 79)
				material = FC_Rpg.mLib.t3.get(refNumber);
			else if (monsterLevel > 79)
				material = FC_Rpg.mLib.t4.get(refNumber);
			else
				return null;
			
			// Variable declarations/refresh.
			ItemStack equipment = new ItemStack(material, 1);
			rand = new Random();
			
			//40% to get a random enchant on mob armor.
			if (isBoss || (rand.nextInt(100) < FC_Rpg.balanceConfig.getMobSpawnWithEnchantsChance()))
			{
				rand = new Random();
				
				if (refNumber >= 5 && refNumber <= 8)
					equipment.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, rand.nextInt(4) + 1);
				
				else if (refNumber == 4)
					equipment.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, rand.nextInt(5) + 1);
			}
			
			return equipment;
		}
		
		private ItemStack getBow()
		{
			Material material = Material.BOW;
			Random rand = new Random();
			
			//Bosses always spawn armor.
			if (!isBoss)
			{
				if (rand.nextInt(100) >= FC_Rpg.balanceConfig.getMobSpawnWithItemChance())
					return null;
			}
			
			// Variable declarations/refresh.
			ItemStack equipment = new ItemStack(material, 1);
			rand = new Random();
			
			//40% to get a random enchant on mob armor.
			if (isBoss || (rand.nextInt(100) < FC_Rpg.balanceConfig.getMobSpawnWithEnchantsChance()))
			{
				rand = new Random();
				equipment.addEnchantment(Enchantment.ARROW_DAMAGE, rand.nextInt(5) + 1);
			}
			
			return equipment;
		}
	}

	public class ExperienceChangeListener implements Listener
	{
		@EventHandler
		public void PlayerExpChangeEvent(PlayerExpChangeEvent event)
		{
			int expAmount = event.getAmount();
			int million = 1000000;
			
			if (expAmount > million)
			{
				event.setAmount(expAmount - million);
			}
			else
			{
				// If experience isn't canceled, then we just alter by global exp multiplier.
				event.setAmount(expAmount * FC_Rpg.balanceConfig.getGlobalExpMultiplier());
			}
		}
	}
	
	public class ArrowFireListener implements Listener
	{
		@EventHandler
		public void ArrowFireEvent(EntityShootBowEvent event)
		{
			// Don't perform this event in non-rpg worlds.
			if (!worldConfig.getIsRpgWorld(event.getEntity().getWorld().getName()))
				return;
			
			// Variable Declarations
			final Arrow arrow = (Arrow) event.getProjectile();
			Vector speed;
			
			// Make it so that arrows don't bounce.
			arrow.setBounce(false);
			
			// Remove fired arrows after 60 seconds. Very nice cleanup function. :)
			Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
			{
				public void run()
				{
					arrow.remove();
				}
			}, 1200);
			
			// Now we want to check if a player is casting a spell.
			if (!(event.getEntity() instanceof Player))
				return;
			
			// Get rpgPlayer.
			RpgPlayer rpgPlayer = FC_Rpg.rpgEntityManager.getRpgPlayer((Player) event.getEntity());
			
			// If the player has a class with the faster arrows passive, then give faster arrows.
			if (rpgPlayer.playerConfig.getRpgClass() != null)
			{
				if (rpgPlayer.playerConfig.getRpgClass().getPassiveID() == BalanceConfig.passive_ScalingArrows)
				{
					speed = arrow.getVelocity().multiply((rpgPlayer.playerConfig.getClassLevel() / FC_Rpg.balanceConfig.getPassivesScalingArrow()) + 1);
					arrow.setVelocity(speed);
				}
			}
			
			// If auto-cast is enabled.
			if (rpgPlayer.playerConfig.getAutoCast() == true)
				rpgPlayer.prepareSpell(false); // Prepare the spell.
		}
	}

	public class TeleportListener implements Listener
	{
		@EventHandler
		public void onPearlThrow(PlayerTeleportEvent event)
		{
			if (FC_Rpg.generalConfig.getDisableEnderPearls() == false)
				return;
			
			if (event.getCause() == TeleportCause.ENDER_PEARL)
			{
				FC_Rpg.plugin.getLogger().info("Ender pearl teleportation by " + event.getPlayer().getName() + " prevented.");
				event.getPlayer().sendMessage("Ender Pearl Teleportation Is Disabled");
				event.setCancelled(true);
			}
		}
	}
	
	public class PlayerTeleportListener implements Listener
	{
		@EventHandler
		public void onPlayerTeleport(PlayerTeleportEvent event)
		{
			if (event.isCancelled() == true)
				return;
			
			World from = event.getFrom().getWorld();
			World to = event.getTo().getWorld();
			
			if (from == to)
				return;
			
			Player p = event.getPlayer();
			
			if (!FC_Rpg.worldConfig.getIsRpgWorld(to.getName()))
				FC_Rpg.msm.endMobSpawns(p);
			else
				FC_Rpg.msm.beginMobSpawns(p);
		}
	}
	
	public class EnchantEventListener implements Listener
	{
		@EventHandler
		public void onEnchantItem(EnchantItemEvent event)
		{
			if (!FC_Rpg.worldConfig.getIsRpgWorld(event.getEnchantBlock().getWorld().getName()))
				return;
			
			Map<Enchantment, Integer> enchantMap = event.getEnchantsToAdd();
			
			//Ban all fire enchants.
			if (enchantMap.containsKey(Enchantment.FIRE_ASPECT))
				enchantMap.remove(Enchantment.FIRE_ASPECT);
			
			if (enchantMap.containsKey(Enchantment.ARROW_FIRE))
				enchantMap.remove(Enchantment.ARROW_FIRE);
			
			// Remove arrow knockback if blocked.
			if (!FC_Rpg.balanceConfig.getArrowKnockback())
			{
				if (enchantMap.containsKey(Enchantment.ARROW_KNOCKBACK))
					enchantMap.remove(Enchantment.ARROW_KNOCKBACK);
			}
			
			// Remove sword knockback if blocked.
			if (!FC_Rpg.balanceConfig.getSwordKnockback())
			{
				if (enchantMap.containsKey(Enchantment.KNOCKBACK))
					enchantMap.remove(Enchantment.KNOCKBACK);
			}
			
			// Remove looting if blocked.
			if (!FC_Rpg.balanceConfig.getDefaultItemDrops())
			{
				if (enchantMap.containsKey(Enchantment.LOOT_BONUS_MOBS))
					enchantMap.remove(Enchantment.LOOT_BONUS_MOBS);
			}
		}
	}
	
	public void handleRpgItemList()
	{
		//Variable Declarations
		RpgItem temp;
		
		rpgItemList = itemConfig.getLoadedRpgItemList();
		buyList = new ArrayList<RpgItem>();
		
		for (RpgItem item : rpgItemList)
			buyList.add(item);
		
		for (RpgItem item : rpgItemList)
		{
			if (item.priceBuy == -1)
				buyList.remove(item);
		}
		
		for (int i = 0; i < buyList.size(); i++)
		{
			for (int j = i; j < buyList.size(); j++)
			{
				if (buyList.get(i).priceBuy > buyList.get(j).priceBuy)
				{
					temp = buyList.get(j);
					buyList.set(j, buyList.get(i));
					buyList.set(i, temp);
				}
			}
		}
	}
	
	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

		if (economyProvider != null)
		{
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
	
	/*
	private boolean setupQQuests()
    {
        qQuests qQuests = (qQuests) getServer().getPluginManager().getPlugin("qQuests");
        
        if (this.qAPI == null) {
            if (qQuests != null) {
                this.qAPI = qQuests.qAPI;
                return true;
            }
        }
        
        return false;
    }
    
    */
}













