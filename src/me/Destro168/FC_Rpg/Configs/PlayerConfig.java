package me.Destro168.FC_Rpg.Configs;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Suite_Shared.ConfigManagers.FileConfigurationWrapper;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Rpg.Entities.RpgPlayer;
import me.Destro168.FC_Rpg.LoadedObjects.RpgClass;
import me.Destro168.FC_Rpg.LoadedObjects.Spell;
import me.Destro168.FC_Rpg.Spells.SpellEffect;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;
import me.Destro168.FC_Suite_Shared.TimeUtils.DateManager;

// Handles storing of player data in files.
public class PlayerConfig extends ConfigGod
{
	//Variable Declarations
	protected RpgClass rpgClass;
    protected String name;
    public boolean hasAlchemy;
    
	public String getName() { return name; }
	public RpgClass getRpgClass() { return rpgClass; }
	public boolean getHasAlchemy() { return hasAlchemy; }
	
	// Loaded dynamic variables.
	public double curMana;
	public double maxMana;
	public double curHealth;
	public double maxHealth;
	
	// Gets
	public List<Integer> getSpellLevels() { return fcw.getCustomIntegerList(prefix + "spell.levels"); }
	public List<Integer> getSpellBinds() { return fcw.getCustomIntegerList(prefix + "spell.binds"); }
	public String getCustomPrefix() { return fcw.getString(prefix + "customPrefix"); }
	public String getActiveSpell() { return fcw.getString(prefix + "activeSpell"); }
	public String getNick() { return fcw.getString(prefix + "nick"); }
    public long getDonatorTime() { return fcw.getLong(prefix + "donatorTime"); }
	public long getLastDungeonCompletion() { return fcw.getLong(prefix + "lastDungeonCompletion"); }
	public long getLastRecievedHourlyItems() { return fcw.getLong(prefix + "lastRecievedHourlyItems"); }
	public double getClassExperience() { return fcw.getDouble(prefix + "classExperience"); }
	public double getGold() { return fcw.getDouble(prefix + "gold"); }
	public double getCurManaFile() { return fcw.getDouble(prefix + "curMana"); }
	public double getMaxManaFile() { return fcw.getDouble(prefix + "maxMana"); }
	public double getCurHealthFile() { return fcw.getDouble(prefix + "curHealth"); }
	public double getMaxHealthFile() { return fcw.getDouble(prefix + "maxHealth"); }
	public int getCombatClass() { return fcw.getInt(prefix + "combatClass"); }
	public int getIntelligence() { return fcw.getInt(prefix + "intelligence"); }
	public int getClassLevel() { return fcw.getInt(prefix + "classLevel"); }
	public int getJobRank() { return fcw.getInt(prefix + "jobRank"); }
	public int getStats() { return fcw.getInt(prefix + "stats"); }
	public int getAttack() { return fcw.getInt(prefix + "attack"); }
	public int getConstitution() { return fcw.getInt(prefix + "constitution"); }
	public int getMagic() { return fcw.getInt(prefix + "magic"); }
	public int getLifetimeMobKills() { return fcw.getInt(prefix + "lifetimeMobKills"); }
	public int getClassChangeTickets() { return fcw.getInt(prefix + "classChangeTickets"); }
	public int getSecondsPlayed() { return fcw.getInt(prefix + "secondsPlayed"); }
	public int getSpellPoints() { return fcw.getInt(prefix + "spellPoints"); }
	public boolean getIsActive() { if (fcw.isSet(prefix + "isActive")) return fcw.getBoolean(prefix + "isActive"); else return false; }
	public boolean getManualAllocation() { return fcw.getBoolean(prefix + "manualAllocation"); }
	public boolean getAutoCast() { return fcw.getBoolean(prefix + "autoCast"); }
	
	// Sets
	public void setSpellLevels(List<Integer> x) { fcw.setCustomList(prefix + "spell.levels", x); }
	public void setSpellBinds(List<Integer> x) { fcw.setCustomList(prefix + "spell.binds", x); }
	public void setCustomPrefix(String x) { fcw.set(prefix + "customPrefix", x); }
	public void setActiveSpell(String x) { fcw.set(prefix + "activeSpell", x); }
	public void setNick(String x) { fcw.set(prefix + "nick", x); }
	public void setDonatorTime(long x) { fcw.set(prefix + "donatorTime", x); }
	public void setLastDungeonCompletion(long x) { fcw.set(prefix + "lastDungeonCompletion", x); }
	public void setLastRecievedHourlyItems(long x) { fcw.set(prefix + "lastRecievedHourlyItems", x); }
	public void setClassExperience(double x) { fcw.set(prefix + "classExperience", x); }
	public void setGold(double x) { fcw.set(prefix + "gold",x); }
	public void setCurManaFile(double x) { fcw.set(prefix + "curMana", x); }
	public void setMaxManaFile(double x) { fcw.set(prefix + "maxMana", x); }
	public void setCurHealthFile(double x) { fcw.set(prefix + "curHealth", x); }
	public void setMaxHealthFile(double x) { fcw.set(prefix + "maxHealth", x); }
	public void setCombatClass(int x) { fcw.set(prefix + "combatClass", x); refreshClass(); }
	public void setIntelligence(int x) { fcw.set(prefix + "intelligence", x); }
	public void setClassLevel(int x) { fcw.set(prefix + "classLevel", x); }
	public void setJobRank(int x) { fcw.set(prefix + "jobRank", x); }
	public void setStats(int x) { fcw.set(prefix + "stats", x); }
	public void setAttack(int x) { fcw.set(prefix + "attack", x); }
	public void setConstitution(int x) { fcw.set(prefix + "constitution", x); }
	public void setMagic(int x) { fcw.set(prefix + "magic", x); }
	public void setLifetimeMobKills(int x) { fcw.set(prefix + "lifetimeMobKills", x); }
	public void setClassChangeTickets(int x) { fcw.set(prefix + "classChangeTickets", x); }
	public void setSecondsPlayed(int x) { fcw.set(prefix + "secondsPlayed", x); }
	public void setSpellPoints(int x) { fcw.set(prefix + "spellPoints", x); }
	public void setIsActive(boolean x) { fcw.set(prefix + "isActive",x); }
	public void setAutomaticAllocation(boolean x) { fcw.set(prefix + "manualAllocation",x); }
	public void setAutoCast(boolean x) { fcw.set(prefix + "autoCast",x); }
	
	// Status Based functions
	public void setStatusDuration(int effectID, int x) 
	{ 
		//Sets the duration of a status in seconds.
		DateManager dm = new DateManager();
		fcw.set(prefix + "status." + effectID + ".duration", dm.getFutureDate_Milliseconds(x)); 
	}
	
	public void setStatusMagnitude(int effectID, double x) { fcw.set(prefix + "status." + effectID + ".magnitude", x); }
	public void setStatusUses(int effectID, int x) { fcw.set(prefix + "status." + effectID + ".uses", x); }
	public void setStatusTier(int effectID, int x) { fcw.set(prefix + "status." + effectID + ".tier", x); }
	
	public long getStatusDuration(int effectID) { return fcw.getLong(prefix + "status." + effectID + ".duration"); }
	public double getStatusMagnitude(int effectID) { return fcw.getDouble(prefix + "status." + effectID + ".magnitude"); }
	public int getStatusUses(int effectID) { return fcw.getInt(prefix + "status." + effectID + ".uses"); }
	public int getStatusTier(int effectID) { return fcw.getInt(prefix + "status." + effectID + ".tier"); }
	
	public List<Integer> getAllActiveBuffs()
	{
		List<Integer> activeBuffs = new ArrayList<Integer>();
		
		for (SpellEffect eID : SpellEffect.values())
		{
			if (getStatusActiveRpgPlayer(eID.getID()))
				activeBuffs.add(eID.getID());
		}
		
		return activeBuffs;
	}
	
	// Used to check if a status is active.
	public boolean getStatusActiveRpgPlayer(int effectID)
	{
		// See if the status is applied as a buff.
		if (System.currentTimeMillis() < getStatusDuration(effectID))
			return true;
		
		// Attempt to use a charge on a status.
		if (getStatusUses(effectID) > 0)
		{
			setStatusUses(effectID, getStatusUses(effectID) - 1);
			return true;
		}
		
		return false;
	}
	
	// Misc functions
	public synchronized void clearAllData() { 
		FileConfigurationWrapper temp = new FileConfigurationWrapper(FC_Rpg.dataFolderAbsolutePath + "/userinfo", name);
		temp.clearFileData();
	}
	public double getRequiredExpPercent() { return (getClassExperience() * 100) / getLevelUpAmount(); }
	public int getLevelUpAmount() { return (int) (getClassLevel() * getClassLevel() * FC_Rpg.balanceConfig.getPlayerExpScaleRate() + FC_Rpg.balanceConfig.getPlayerExpScaleBase()); }
	public void resetActiveSpell() { fcw.set(prefix + "activeSpell", "none"); }
	public void subtractGold(double x) { setGold(getGold() - x); }
	public void addGold(double x) { setGold(getGold() + x); }
	public void adjustNewCombatclass(int x) { setCombatClass(x); refreshClass(); }
	
	public void updateSpellLevel(int spellID, int newVal)
	{
		List<Integer> sl = getSpellLevels();
		sl.set(spellID, newVal);
		setSpellLevels(sl);
	}
	
	public void updateSpellBind(int spellID, int newVal)
	{
		List<Integer> sb = getSpellBinds();
		sb.set(spellID, newVal);
		setSpellBinds(sb);
	}
	
	public PlayerConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Rpg");
		
		handleUpdates();
		name = "";
	}
	
	public PlayerConfig(String playerName)
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Rpg");
		
		handleUpdates();
		setPlayerName(playerName);
	}
	
	public void handleUpdates()
	{
		//If not active, return.
		if (getIsActive() == false)
			return;
		
		if (getVersion() < 1.0)
		{
			setVersion(1.0);
			setLastDungeonCompletion(0);
		}
		
		if (getVersion() < 1.1)
		{
			setVersion(1.1);
			
			try { setSpellLevels(fcw.getIntegerList(prefix + "spell.levels")); } catch (IndexOutOfBoundsException e) { }
			try { setSpellBinds(fcw.getIntegerList(prefix + "spell.binds")); } catch (IndexOutOfBoundsException e) { }
		}
		
		if (getVersion() < 1.3)
		{
			setVersion(1.3);
			
			setNick("none");
		}
		
		if (getVersion() < 1.4)
		{
			setVersion(1.4);
			
			checkStatAndSpellPoints();
		}
		
		if (getVersion() < 1.5)
		{
			setVersion (1.5);
			
			// Transfer arcanium to gold in this update.
			if (fcw.isSet(prefix + "arcanium"))
				setGold(fcw.getDouble(prefix + "arcanium"));
		}
	}
	
	public void setPlayerName(String playerName)
	{
		if (Bukkit.getServer().getPlayer(playerName) != null)
		{
			NameMatcher nm = new NameMatcher();
			playerName = nm.getNameByMatch(playerName);
		}
		
		//Store the playerName
		name = playerName;
		
		//OVERWRITE A BITCH - Create the new profile manager.
		fcw = new FileConfigurationWrapper(FC_Rpg.dataFolderAbsolutePath + "/userinfo", playerName);
		
		//Set the players class.
		refreshClass();
		
		//Handle updates.
		handleUpdates();
		
		//Update whether or not the player has alchemy or not.
		Spell s;
		
		for (int j = 0; j < rpgClass.getSpellBook().size(); j++)
		{
			s = rpgClass.getSpellBook().get(j);
			
			if (s.effectID == SpellEffect.ALCHEMY.getID())
			{
				if (getSpellLevels().get(j) > 0)
				{
					hasAlchemy = true;
					break;
				}
				else
					break;
			}
		}
	}
	
	private void refreshClass()
	{
		rpgClass = FC_Rpg.classConfig.getRpgClass(getCombatClass());
		
		//Automatically detect missing levels and binds for spells and fill them into the players config. Null = player isn't active yet or upgraded
		//If the player isn't upgraded to 0.2, then the handleUpdates function will udpate rather than this instance.
		int playerStoredSpellLevels = 0;
		try { playerStoredSpellLevels = getSpellLevels().size(); } catch (NullPointerException e) { return; }
		
		//Check if the player needs more default level slots for spells.
		if (playerStoredSpellLevels < rpgClass.getSpellBook().size())
		{
			//Update spell levels and binds with new lists
			List<Integer> spellLevels = new ArrayList<Integer>();
			List<Integer> spellBinds = new ArrayList<Integer>();
			
			for (int i = 0; i < rpgClass.getSpellBook().size(); i++)
			{
				spellLevels.add(0);
				spellBinds.add(999);
			}
			
			setSpellLevels(spellLevels);
			setSpellBinds(spellBinds);
		}
	}
	
	private void checkStatAndSpellPoints()
	{
		int totalStats = getAttack() + getIntelligence() + getConstitution() + getMagic() + getStats();
		int shouldHaveStats = getClassLevel() * FC_Rpg.balanceConfig.getPlayerStatsPerLevel();
		int shouldHaveSpellPoints = (int) Math.floor(getClassLevel() / FC_Rpg.balanceConfig.getPlayerLevelsPerSpellPoint()) + 1;
		
		if (totalStats != shouldHaveStats)
		{
			respecStatPoints();
			setStats(shouldHaveStats);
		}
		
		if (getSpellPoints() != shouldHaveSpellPoints)
		{
			this.respectSpellPoints();
			setSpellPoints(shouldHaveSpellPoints);
		}
	}
	
	public void offlineSave()
	{
		setCurManaFile(curMana);
		setMaxManaFile(maxMana);
		setCurHealthFile(curHealth);
		setMaxHealthFile(maxHealth);
	}
	
	public void setDefaults()
	{
		//Set variables to the new style
		setCustomPrefix(FC_Rpg.generalConfig.getDefaultPrefix());
		setIsActive(false);

		//Don't wipe out donator time for players that donated.
		if (getDonatorTime() == 0)
			setDonatorTime((long) 0);
		
		setActiveSpell("none");
		setCombatClass(0);
		setClassLevel(0);
		setClassExperience(0);
		setJobRank(0);
		setAttack(0);
		setConstitution(0);
		setMagic(0);
		setIntelligence(0);

		setStats(0);
		setAttack(0);
		setConstitution(0);
		setMagic(0);
		setIntelligence(0);
		setCurManaFile(0);
		setMaxManaFile(0);
		setCurHealthFile(0);
		setMaxHealthFile(0);
		
		setLifetimeMobKills(0);
		setAutomaticAllocation(true);
		setLastRecievedHourlyItems(System.currentTimeMillis());
		setClassChangeTickets(0);
		
		if (getSecondsPlayed() == 0)
			setSecondsPlayed(0);
		
		//Use a temporary rpgClass.
		RpgClass tempClass = FC_Rpg.classConfig.getRpgClass(getCombatClass());

		//Set the spell level and binds for spells.
		List<Integer> levels = new ArrayList<Integer>();
		List<Integer> binds = new ArrayList<Integer>();

		for (int i = 0; i < tempClass.getSpellBook().size(); i++)
		{
			levels.add(0);
			binds.add(999);
		}

		setSpellLevels(levels);
		setSpellBinds(binds);

		setSpellPoints(1);
		setAutoCast(false);
		
		setLastDungeonCompletion(0);

		setNick("");
		
		offlineSave();
	}
	
	public void setPlayerDefaults(int pickedClass, boolean manualAllocation_)
	{
		setDefaults();

		setIsActive(true);
		setCombatClass(pickedClass);
		setClassLevel(1);
		setJobRank(1);

		//Give the player stats based on their choice.
		if (manualAllocation_ == true)
		{
			setStats(10);
			setAutomaticAllocation(false);
		}
		else
		{
			assignClassStatPoints();
			setAutomaticAllocation(true);
		}
		
		offlineSave();
	}
	
    public boolean isDonator() 
	{
		//Store time into memory
		Long time = getDonatorTime();
		
		//If the donator time is less than what time is currently is.
		if (time > System.currentTimeMillis()) 
			return true;
		else
		{
			//If there is any time on the donator time, then clear it.
			if (time > 0)
				setDonatorTime(0);
			
			return false; 
		}
	}
    
    public void addLevels(int level)
	{
		int targetLevel = getClassLevel() + level;
		
		//Add enough to levelup onece
		addOfflineClassExperience(getLevelUpAmount() - getClassExperience(), false, null);
		
		//Keep adding experience to levelup until the player is the new level.
		while (getClassLevel() < targetLevel)
		{
			addOfflineClassExperience(getLevelUpAmount() - getClassExperience(), false, null);
			
			if (getClassLevel() == FC_Rpg.generalConfig.getLevelCap())
				return;
		}
		
		//Make sure to reset class experience to 0.
		setClassExperience(0);
	}
    
    public boolean addOfflineClassExperience(double x, boolean displayLevelUpMessage, RpgPlayer rpgPlayer)
	{
		//Variable declarations
		double newExperience = getClassExperience() + x;
		int levelCap = FC_Rpg.generalConfig.getLevelCap();
		
		//Account for level cap.
		if (getClassLevel() == levelCap)
		{
			setClassExperience(0);
			return false;
		}
		
		//While the player has enough experience to levelup, level up.
		while (newExperience >= getLevelUpAmount())
		{
			//Add the experience to the player.
			newExperience = newExperience - getLevelUpAmount();
			
			//Increase the players level.
			setClassLevel(getClassLevel() + 1);
			
			//Spell points.
			if ((getClassLevel() % FC_Rpg.balanceConfig.getPlayerLevelsPerSpellPoint()) == 0 && getClassLevel() != levelCap)
				setSpellPoints(getSpellPoints() + 1);
			
			//Manage Stat Points
			if (getManualAllocation() == true)
				assignClassStatPoints();
			else
				setStats(getStats() + FC_Rpg.balanceConfig.getPlayerStatsPerLevel());
			
			if (displayLevelUpMessage == true)
			{
				if (getClassLevel() >= 50)
					FC_Rpg.rpgBroadcast.rpgBroadcast(name + " is now level [" + String.valueOf(getClassLevel()) + "]!");	//Broadcast that he leveled up
				else if (rpgPlayer != null)
				{
					MessageLib msgLib = new MessageLib(rpgPlayer.getPlayer());
					msgLib.infiniteMessage("You have just reached level [",getClassLevel() + "","]!");
				}
				
				// Attempt to shoot fireworks if rpgPlayer isn't null.
				if (rpgPlayer != null)
				{
					for (int i = 0; i < 3; i++)
					{
						Location pLoc = rpgPlayer.getPlayer().getLocation();
						
						Firework fw = (Firework) pLoc.getWorld().spawnEntity(pLoc, EntityType.FIREWORK);
						FireworkMeta fwMeta = fw.getFireworkMeta();
						
						fwMeta.setPower(i);
						
						fwMeta.addEffect(FireworkEffect.builder()
								.flicker(true)
								.trail(true)
								.with(Type.BALL_LARGE)
								.withColor(getRandomColor(), getRandomColor(), getRandomColor())
								.withFade(getRandomColor(), getRandomColor(), getRandomColor())
								.withFlicker()
								.withTrail()
								.build());
						
						fw.setFireworkMeta(fwMeta);
					}
				}
			}
			
			//Acount for level cap.
			if (getClassLevel() == levelCap)
			{
				setClassExperience(0);
				return false;
			}
		}
		
		//we now set player class experience to passed in experience.
		setClassExperience(newExperience);
		
		return true;
	}
    
    private Color getRandomColor()
    {
		Random rand = new Random();
		int c1 = rand.nextInt(256);
		rand = new Random();
		int c2 = rand.nextInt(256);
		rand = new Random();
		int c3 = rand.nextInt(256);
		
		return Color.fromRGB(c1, c2, c3);
    }
    
    public void assignClassStatPoints()
	{
    	List<Integer> statGrowth = rpgClass.getStatGrowth();
    	
    	//Assign stat points based on class.
    	for (RpgClass rpgClass : FC_Rpg.classConfig.rpgClassList)
    	{
    		if (rpgClass.getID() == getCombatClass())
    		{
				setAttack(getAttack() + statGrowth.get(0));
				setConstitution(getConstitution() + statGrowth.get(1));
				setMagic(getMagic() + statGrowth.get(2));
				setIntelligence(getIntelligence() + statGrowth.get(3));
    		}
    	}
    	
		//Update health and mana based on stats.
		calculateHealthAndManaOffline();
	}
    
    public void calculateHealthAndManaOffline()
	{
    	maxHealth = FC_Rpg.balanceConfig.getPlayerBaseHealth() + getConstitution() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeConstitution();
    	maxMana = FC_Rpg.balanceConfig.getPlayerBaseMana() + getIntelligence() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeIntelligence();
	}
    
    public void offlineSetDonator(int periods)
	{
		//Variable Declarations
		Date now = new Date();
		
		//Calculate the future date in which donator time will expire.
		GregorianCalendar gc = new GregorianCalendar();
		
		gc.setGregorianChange(now);
		gc.add(GregorianCalendar.DATE, 30 * periods);
		
		//Give the donator their time.
		setDonatorTime(gc.getTime().getTime());
	}
    
    public double getPromotionCost()
	{
		return FC_Rpg.generalConfig.getJobRankCosts().get(getJobRank() - 1);
	}
    
    public String[] getRemainingX(double min, double max, int colorID)
	{
		String[] parts = new String[6];
		ChatColor color = ChatColor.GREEN;
		
		if (colorID == 1)
			color = ChatColor.DARK_AQUA;
		else if (colorID == 2)
			color = ChatColor.RED;
		
		parts[0] = color + FC_Rpg.df.format(min);
		parts[1] = "/";
		parts[2] = color + FC_Rpg.df.format(max);
		parts[3] = ") (";
		parts[4] = color + FC_Rpg.df.format(min * 100 / max)+"%";
		parts[5] = ")";
		
		return parts;
	}
    
    public void respecStatPoints()
	{
		//Reset stats
		int stats = getStats();
		
		stats += getAttack();
		stats += getConstitution();
		stats += getMagic();
		stats += getIntelligence();
		
		setAttack(0);
		setConstitution(0);
		setMagic(0);
		setIntelligence(0);
		
		setStats(stats);
	}
	
	public void respectSpellPoints()
	{
		int spellPoints = getSpellPoints();
		
		for (int i = 0; i < getRpgClass().getSpellBook().size(); i++)
		{
			spellPoints += getSpellLevels().get(i);
			updateSpellLevel(i, 0);
		}
		
		setSpellPoints(spellPoints);
	}
}












