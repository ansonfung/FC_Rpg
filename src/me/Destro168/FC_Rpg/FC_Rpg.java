package me.Destro168.FC_Rpg;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.Destro168.Configs.ClassConfig;
import me.Destro168.Configs.GeneralConfig;
import me.Destro168.Configs.DungeonConfig;
import me.Destro168.Configs.GuildConfig;
import me.Destro168.Configs.PassiveConfig;
import me.Destro168.Configs.SpellConfig;
import me.Destro168.Configs.TreasureConfig;
import me.Destro168.Configs.WarpConfig;
import me.Destro168.Configs.WorldConfig;
import me.Destro168.Entities.RpgManager;
import me.Destro168.Entities.RpgPlayer;
import me.Destro168.FC_Suite_Shared.ColorLib;
import me.Destro168.FC_Suite_Shared.SelectionVector;
import me.Destro168.Listeners.BlockBreakListener;
import me.Destro168.Listeners.DamageListener;
import me.Destro168.Listeners.PlayerInteractionListener;
import me.Destro168.LoadedObjects.RpgClass;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.Vector;

public class FC_Rpg extends JavaPlugin
{
	final static double MAX_HP = 999999;
	public final static DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static final DecimalFormat df = new DecimalFormat("#.#");
	
	public final static boolean debugModeEnabled = false;
	
	public static FC_Rpg plugin;
	public static String dataFolderAbsolutePath;
	
	public static RpgManager rpgManager;
	public static GuildConfig guildManager;
	public static SpellConfig spellConfig;
	public static ClassConfig classConfig;
	public static GeneralConfig generalConfig;
	public static PassiveConfig passiveConfig;
	public static TreasureConfig treasureConfig;
	public static WarpConfig warpConfig;
	
	public static ColorLib cl = new ColorLib();
	public static PvpEvent pvp;
	public static BroadcastLib bLib;
	public static WorldConfig worldConfig;
	public static Economy economy = null;
	public static DungeonEvent[] dungeonEventArray;
	public static SelectionVector sv;
	
	public static int eventExpMultiplier = 1;
	public static int eventCashMultiplier = 1;
	public static int tid3;
	public static int tid4;
	public static int dungeonCount;
	
	public int tid;
	public int tid2;
	
	public static Map<Player, String> classSelection = new HashMap<Player, String>();
	public static Map<Integer, Integer> trueDungeonNumbers;
	public static Map<Player, Boolean> aoeLock = new HashMap<Player, Boolean>();
	
	private CommandGod commandCE;
	
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
			if (dungeonEventArray[FC_Rpg.trueDungeonNumbers.get(i)].isHappening() == true)
				dungeonEventArray[FC_Rpg.trueDungeonNumbers.get(i)].end(false);
		}
		
		this.getLogger().info("Disabled");
	}
	
	@Override
	public void onEnable() 
	{
		//Derp
		plugin = this;
		dataFolderAbsolutePath = FC_Rpg.plugin.getDataFolder().getAbsolutePath();
		
		//World manager = new world manager;
		generalConfig = new GeneralConfig();
		worldConfig = new WorldConfig();
		bLib = new BroadcastLib();
		guildManager = new GuildConfig();
		pvp = new PvpEvent();
		spellConfig = new SpellConfig();
		classConfig = new ClassConfig();
		passiveConfig = new PassiveConfig();
		treasureConfig = new TreasureConfig();
		warpConfig = new WarpConfig();
		sv = new SelectionVector();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers())
			player.setWalkSpeed((float) .2);
		
		//Set up the economy.
		setupEconomy();
		
		//Initialize dungeons.
		initializeDungeons();
		
		//Register Listeners
		getServer().getPluginManager().registerEvents(new FishingListener(), plugin);
		getServer().getPluginManager().registerEvents(new HealthRegenListener(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
		getServer().getPluginManager().registerEvents(new RespawnListener(), plugin);
		getServer().getPluginManager().registerEvents(new DamageListener(), plugin);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(), plugin);
		
		//If rpg chat is overriden, then don't perform this.
		if (generalConfig.getRpgChatOverride() == true)
			getServer().getPluginManager().registerEvents(new AyncPlayerChatListener(), plugin);
		
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
		getServer().getPluginManager().registerEvents(new teleportListener(), plugin);
		
		//Register Commands
		commandCE = new CommandGod();
		getCommand("bighelp").setExecutor(commandCE);
		getCommand("class").setExecutor(commandCE);
		getCommand("donator").setExecutor(commandCE);
		getCommand("dungeon").setExecutor(commandCE);
		getCommand("d9").setExecutor(commandCE);
		getCommand("faq").setExecutor(commandCE);
		getCommand("g").setExecutor(commandCE);
		getCommand("h").setExecutor(commandCE);
		getCommand("gh").setExecutor(commandCE);
		getCommand("hg").setExecutor(commandCE);
		getCommand("hat").setExecutor(commandCE);
		getCommand("job").setExecutor(commandCE);
		getCommand("list").setExecutor(commandCE);
		getCommand("guild").setExecutor(commandCE);
		getCommand("pvp").setExecutor(commandCE);
		getCommand("reset").setExecutor(commandCE);
		getCommand("rpg").setExecutor(commandCE);
		getCommand("spell").setExecutor(commandCE);
		getCommand("modify").setExecutor(commandCE);
		getCommand("w").setExecutor(commandCE);
		getCommand("buff").setExecutor(commandCE);
		
		//Handle tasks that happen every 30 minutes. Delay'd by 5 seconds.
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run() 
			{
				pvp.begin();	//Start pvp event.
			}
		}, 100, 36000);
		
		//Start an rpg manager.
		rpgManager = new RpgManager();
		
		//We want to delete records of all players that haven't logged on in a while.
		rpgManager.clearOldPlayerData();
		
		//We want to perform promotion checks.
		this.getLogger().info("Enabled");
	}
	
	private void initializeDungeons()
	{
		//Variable Declarations/Initializations
		DungeonConfig dm = new DungeonConfig();
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
		dungeonEventArray = new DungeonEvent[dungeonCount];
		
		//Initate the dungeons.
		for (int i = 0; i < dungeonCount; i++)
			dungeonEventArray[i] = new DungeonEvent(i);
	}
	
	//Listen for block growth
	public class CreativeControlListeners implements Listener
	{
		@EventHandler
		public void onBlockBreak(StructureGrowEvent event)
		{
			//If perfect birch is disabled return.
			if (generalConfig.getPerfectBirch() == false)
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
			if (generalConfig.getCreativeControl() == false)
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
				msgLib.standardMessage("Ask An Admin To Place This.");
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
			if (generalConfig.getCreativeControl() == false)
				return;
			
			//Check for thrown itmes that should be blocked.
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
			//Variable Declarations
			boolean fail = false;
			FC_RpgPermissions perms = new FC_RpgPermissions(event.getPlayer());
			MessageLib msgLib = new MessageLib(event.getPlayer());
			
			//Without adequate permission...
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
			//Store entity
			Entity entity = event.getEntity();
			
			//Do dungeon checking for mob deaths to see if dungeons are cleared.
			for (int i = 0; i < dungeonCount; i++)
				dungeonEventArray[i].checkMobDeath(entity);
			
			if (!worldConfig.getIsRpgWorld(entity.getWorld().getName()))
				return;
			
			//If exp drops are cancelled, then...
			if (generalConfig.getExpCancelled() == true)
				event.setDroppedExp(0);	//Disable exp drops.
			
			//Handle item loss if a player dies with hunger.
			if (entity instanceof Player)
			{
				//Store entity as player for fast access.
				player = (Player) entity;
				
				//If hardcore item loss is enabled, then...
				if (generalConfig.getHardcoreItemLoss() == true)
				{
					//If the player has 6 hearts or less, then...
					if (player.getFoodLevel() < 7)
						removeRandomItemFromPlayer();	//Remove a random item from the player.
					
					if (player.getFoodLevel() < 2)
						removeRandomItemFromPlayer();	//Remove a random item from the player.
				}
				
				//Remove from any dungeons.
				for (int i = 0; i < dungeonCount; i++)
					dungeonEventArray[i].removeDungeoneer(player,player,false);
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
			if (generalConfig.getBetterFishing() == true)
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
			
			//Register the player if needed.
			FC_Rpg.rpgManager.checkPlayerRegistration(player, true);
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
			
			//Remove from any dungeons.
			for (int i = 0; i < dungeonCount; i++)
				dungeonEventArray[i].removeDungeoneer(p,p,false);
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
					Location spectatorBooth = new Location(worldConfig.getSpawnWorld(),177,99,74,90,0);
					
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
			WorldConfig wm = new WorldConfig();
			
			if (!wm.getIsRpgWorld(entity.getWorld().getName()))
				return;
			
			//Deal damage to the defender but the defender isn't type Player. The defender is type RpgEntity.
			if (entity instanceof Player)
			{
				player = (Player) entity;
				
				//Cancel the event.
				event.setCancelled(true);
				
				//Set the magnitude to heal by the players max health.
				magnitudeModifier = rpgManager.getRpgPlayer(player).getMaxHealth();
				
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
				rpgManager.getRpgPlayer(player).restoreHealth(magnitudeModifier);
				
				//Update health
				hc = new HealthConverter(rpgManager.getRpgPlayer(player).getMaxHealth(), rpgManager.getRpgPlayer(player).getCurHealth());
				
				player.setHealth(hc.getPlayerHearts());
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
				
				else if (event.getRegainReason() == RegainReason.MAGIC)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) != null)
						rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier * 5);
				}

				else if (event.getRegainReason() == RegainReason.MAGIC_REGEN)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) != null)
						rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier);
				}
				
				else if (event.getRegainReason() == RegainReason.WITHER_SPAWN)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) == null)
						rpgManager.registerCustomLevelEntity((LivingEntity) entity, 50);
					
					rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(999999999);
					((LivingEntity) entity).setHealth(300);
				}
				
				else if (event.getRegainReason() == RegainReason.WITHER)
				{
					if (rpgManager.getRpgMonster((LivingEntity) entity) != null)
						rpgManager.getRpgMonster((LivingEntity) entity).restoreHealth(magnitudeModifier);
				}
			}
		}
	}
	
	public class AyncPlayerChatListener implements Listener //<%1$s> %2$s = base format
	{
		@EventHandler
		public void onChat(AsyncPlayerChatEvent event)
		{
			//Variable Declaration
			RpgPlayer rpgPlayer = rpgManager.getRpgPlayer(event.getPlayer());
			
			//If rpg player is null don't continue.
			if (rpgPlayer == null)
				return;
			
			//Variable Declaration
			Player player = event.getPlayer();
			FC_RpgPermissions perms = new FC_RpgPermissions(player);
			DateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			ChatColor messageColor = ChatColor.GRAY;
			ChatColor secondaryColor = ChatColor.GRAY;
			
			//Change features for admins.
			if (perms.isAdmin() == true)
			{
				event.setMessage(cl.parse(event.getMessage()));
				messageColor = ChatColor.YELLOW;
				secondaryColor = ChatColor.WHITE;
			}
			
			//Set the message format.
			event.setFormat(messageColor + timeStamp.format(now) + " " + rpgPlayer.updatePrefix() + messageColor + rpgPlayer.getPlayerConfig().getName() + secondaryColor + ": " + "%2$s");
		}
	}
	
	//Cancel out poison damage
	public class EntityDamageListener implements Listener
	{
		@EventHandler
		public void OnEntityDamage(EntityDamageEvent event)
		{
			if (!worldConfig.getIsRpgWorld(event.getEntity().getWorld().getName()))
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
			if (!worldConfig.getIsMobWorld(event.getEntity().getWorld().getName()) == false)
				return;
			
			//Remove from any dungeons.
			for (int i = 0; i < dungeonCount; i++)
			{
				if (dungeonEventArray[i].isInsideDungeon(event.getLocation()))
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
			//If experience is cancelled, then cancel it.
			if (generalConfig.getExpCancelled() == true)
			{
				//Variable declarations
				Player player = event.getPlayer();
				
				if (!worldConfig.getIsRpgWorld(player.getWorld().getName()))
					return;
				
				//Change everything we can to remove player experience.
				event.setAmount(0);
				player.setLevel(0);
				player.setExp(0);
				
				return;
			}
			
			//If experience isn't canceled, then we just alter by global exp multiplier.
			event.setAmount(event.getAmount() * FC_Rpg.generalConfig.getGlobalExpMultiplier());
		}
	}
	
	public class ArrowFireListener implements Listener
	{
		@EventHandler
		public void ArrowFireEvent(EntityShootBowEvent event)
		{
			//Don't perform this event in non-rpg worlds.
			if (!worldConfig.getIsRpgWorld(event.getEntity().getWorld().getName()))
				return;
			
			//Variable Declarations
			final Arrow arrow = (Arrow) event.getProjectile();
			Vector speed;
			RpgClass rpgClass = FC_Rpg.classConfig.getClassWithPassive(PassiveConfig.passive_ScalingArrows);
			
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
			
			//If the player has a class with the faster arrows passive, then give faster arrows.
			
			if (rpgClass != null)
			{
				if (rpgClass.getName().equals(rpgPlayer.getPlayerConfig().getCombatClass()))
				{
					speed = arrow.getVelocity().multiply((rpgPlayer.getPlayerConfig().getClassLevel() / FC_Rpg.passiveConfig.getScalingArrow()) + 1);
					arrow.setVelocity(speed);
				}
			}
			
			//If auto-cast is enabled.
			if (rpgPlayer.getPlayerConfig().getAutoCast() == true)
				rpgPlayer.prepareSpell(false);	//Prepare the spell.
		}
	}
	
	public class teleportListener implements Listener
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
	
	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		
		return (economy != null);
	}
}



