package me.Destro168.Configs;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.bukkit.Bukkit;

import me.Destro168.ConfigManagers.ConfigGod;
import me.Destro168.ConfigManagers.CustomConfigurationManager;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.LoadedObjects.RpgClass;
import me.Destro168.TimeUtils.DateManager;

// Handles storing of player data in files.
public class PlayerConfig extends ConfigGod
{
	private int levelCap;
	protected RpgClass rpgClass;
    protected String name;
    
	public String getName() { return name; }
	public RpgClass getRpgClass() { return rpgClass; }
	
	//Gets
	public boolean getIsActive()  //Very important get so it gets to take up space.
	{
		try	{
			return ccm.getBoolean(prefix + "isActive");
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public String getCustomPrefix() { return ccm.getString(prefix + "customPrefix"); }
	public String getActiveSpell() { return ccm.getString(prefix + "activeSpell"); }
    public long getDonatorTime() { return ccm.getLong(prefix + "donatorTime"); }
	public long getHunterLastPay() { return ccm.getLong(prefix + "hunterLastPay"); }
	public int getCombatClass() { return ccm.getInt(prefix + "combatClass"); }
	public int getIntelligence() { return ccm.getInt(prefix + "intelligence"); }
	public int getClassLevel() { return ccm.getInt(prefix + "classLevel"); }
	public int getJobRank() { return ccm.getInt(prefix + "jobRank"); }
	public int getStats() { return ccm.getInt(prefix + "stats"); }
	public int getAttack() { return ccm.getInt(prefix + "attack"); }
	public int getConstitution() { return ccm.getInt(prefix + "constitution"); }
	public int getMagic() { return ccm.getInt(prefix + "magic"); }
	public int getLifetimeMobKills() { return ccm.getInt(prefix + "lifetimeMobKills"); }
	public int getClassChangeTickets() { return ccm.getInt(prefix + "classChangeTickets"); }
	public int getSecondsPlayed() { return ccm.getInt(prefix + "secondsPlayed"); }
	public int getSpellPoints() { return ccm.getInt(prefix + "spellPoints"); }
	
	
	
	public List<Integer> getSpellLevels() { return converter.getIntegerListFromString(ccm.getString(prefix + "spell.levels")); }
	public List<Integer> getSpellBinds() { return converter.getIntegerListFromString(ccm.getString(prefix + "spell.binds")); }
	
	
	
	public double getClassExperience() { return ccm.getDouble(prefix + "classExperience"); }
	public boolean getManualAllocation() { return ccm.getBoolean(prefix + "manualAllocation"); }
	public boolean getShowDamageTaken() { return ccm.getBoolean(prefix + "showDamageTaken"); }
	public boolean getShowDamageGiven() { return ccm.getBoolean(prefix + "showDamageGiven"); }
	public boolean getHunterCanKit() { return ccm.getBoolean(prefix + "hunterCanKit"); }
	public boolean getAutoCast() { return ccm.getBoolean(prefix + "autoCast"); }
	
	public long getLastRecievedHourlyItems() { return ccm.getLong(prefix + "lastRecievedHourlyItems"); }
	
	//Sets
	public void setCustomPrefix(String x) { ccm.set(prefix + "customPrefix", x); }
	public void setActiveSpell(String x) { ccm.set(prefix + "activeSpell", x); }
	public void setCombatClass(int x) { 
		ccm.set(prefix + "combatClass", x);
		refreshClass();
	}
	public void setClassLevel(int x) { ccm.set(prefix + "classLevel", x); }
	public void setJobRank(int x) { ccm.set(prefix + "jobRank", x); }
	public void setAttack(int x) { ccm.set(prefix + "attack", x); }
	public void setConstitution(int x) { ccm.set(prefix + "constitution", x); }
	public void setMagic(int x) { ccm.set(prefix + "magic", x); }
	public void setStats(int x) { ccm.set(prefix + "stats", x); }
	public void setIntelligence(int x) { ccm.set(prefix + "intelligence", x); }
	public void setLifetimeMobKills(int x) { ccm.set(prefix + "lifetimeMobKills", x); }
	public void setClassChangeTickets(int x) { ccm.set(prefix + "classChangeTickets", x); }
	public void setSecondsPlayed(int x) { ccm.set(prefix + "secondsPlayed", x); }
	public void setSpellPoints(int x) { ccm.set(prefix + "spellPoints", x); }
	
	
	
	
	public void setSpellLevels(int x) { ccm.set(prefix + "spell.levels", x); }
	public void setSpellBinds(int x) { ccm.set(prefix + "spell.binds", x); }
	public void setSpellLevels(List<Integer> x) { ccm.setCustomList(prefix + "spell.levels", x); }
	public void setSpellBinds(List<Integer> x) { ccm.setCustomList(prefix + "spell.binds", x); }
	
	
	
	
	
	public void setDonatorTime(Long x) { ccm.set(prefix + "donatorTime", x); }
	public void setClassExperience(double x) { ccm.set(prefix + "classExperience", x); }
	public void setIsActive(boolean x) { ccm.set(prefix + "isActive",x); }
	public void setAutomaticAllocation(boolean x) { ccm.set(prefix + "manualAllocation",x); }
	public void setShowDamageTaken(boolean x) { ccm.set(prefix + "showDamageTaken",x); }
	public void setShowDamageGiven(boolean x) { ccm.set(prefix + "showDamageGiven",x); }
	public void setAutoCast(boolean x) { ccm.set(prefix + "autoCast",x); }
	
	public void setLastRecievedHourlyItems(long x) { ccm.set(prefix + "lastRecievedHourlyItems", x); }
	
	//Spell Status Set/Gets
	public void setStatusDuration(int effectID, int x) { //Sets the duration of a status in seconds.
		DateManager dm = new DateManager();
		ccm.set(prefix + "status." + effectID + ".duration", dm.getFutureDate_Milliseconds(x)); }
	public void setStatusMagnitude(int effectID, double x) { ccm.set(prefix + "status." + effectID + ".magnitude", x); }
	public void setStatusUses(int effectID, int x) { ccm.set(prefix + "status." + effectID + ".uses", x); }
	
	public long getStatusDuration(int effectID) { return ccm.getLong(prefix + "status." + effectID + ".duration"); }
	public double getStatusMagnitude(int effectID) { return ccm.getDouble(prefix + "status." + effectID + ".magnitude"); }
	public int getStatusUses(int effectID) { return ccm.getInt(prefix + "status." + effectID + ".uses"); }
	
	//Mana/Hp sets and gets.
	public void setCurManaFile(double x) { ccm.set(prefix + "curMana", x); }
	public void setMaxManaFile(double x) { ccm.set(prefix + "maxMana", x); }
	public void setCurHealthFile(double x) { ccm.set(prefix + "curHealth", x); }
	public void setMaxHealthFile(double x) { ccm.set(prefix + "maxHealth", x); }
	
	public double getCurManaFile() { return ccm.getDouble(prefix + "curMana"); }
	public double getMaxManaFile() { return ccm.getDouble(prefix + "maxMana"); }
	public double getCurHealthFile() { return ccm.getDouble(prefix + "curHealth"); }
	public double getMaxHealthFile() { return ccm.getDouble(prefix + "maxHealth"); }
	
	//Misc functions
	public synchronized void clearAllData() { ccm.clearFileData(); }
	public double getRequiredExpPercent() { return (getClassExperience() * 100) / getLevelUpAmount(); }
	public int getLevelUpAmount() { return (int) (getClassLevel() * getClassLevel() * FC_Rpg.generalConfig.getExpScaleRate() + FC_Rpg.generalConfig.getExpScaleBase()); }
	public void resetActiveSpell() { ccm.set(prefix + "activeSpell", "none"); }
	
	
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
		updateLevelCap();
		
		handleUpdates();
		name = "";
	}
	
	public PlayerConfig(String playerName)
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Rpg");
		updateLevelCap();
		
		handleUpdates();
		setPlayerName(playerName);
	}
	
	private void updateLevelCap()
	{
		levelCap = FC_Rpg.generalConfig.getLevelCap();
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
		ccm = new CustomConfigurationManager(FC_Rpg.dataFolderAbsolutePath + "/userinfo", playerName);
		
		//Set the players class.
		refreshClass();
		
		//Handle updates.
		handleUpdates();
	}
	
	private void refreshClass()
	{
		rpgClass = FC_Rpg.classConfig.getClassByID(getCombatClass());
		
		//Automatically detect missing levels and binds for spells and fill them into the players config. Null = player isn't active yet or upgraded
		//If the player isn't upgraded to 0.2, then the handleUpdates function will udpate rather than this instance.
		int playerStoredSpellLevels = 0;
		try { playerStoredSpellLevels = this.getSpellLevels().size(); } catch (NullPointerException e) { return; }
		
		//Check if the player needs more default level slots for spells.
		if (playerStoredSpellLevels < rpgClass.getSpellBook().size())
		{
			List<Integer> levels = new ArrayList<Integer>();
			List<Integer> binds = new ArrayList<Integer>();
			
			levels = getSpellLevels();
			binds = getSpellBinds();
			
			for (int i = playerStoredSpellLevels; i < rpgClass.getSpellBook().size(); i++)
			{
				levels.add(0);
				binds.add(999);
			}
			
			//Update spell levels and binds with new lists
			setSpellLevels(levels);
			setSpellBinds(binds);
		}
	}
	
	public void handleUpdates()
	{
		//If not active, return.
		if (getIsActive() == false)
			return;
		
		if (getVersion() < 0.1)
		{
			//Update the version.
			setVersion(0.1);
			
			//Remove outdated variables.
			ccm.set(prefix + "lastAte", null);
			ccm.set(prefix + "rankFreeze", null);
		}
		
		if (getVersion() < 0.2)
		{
			setVersion(0.2);
			
			List<Integer> levels = new ArrayList<Integer>();
			List<Integer> binds = new ArrayList<Integer>();
			
			//Load up old spell levels and spell binds.
			for (int i = 0; i < 5; i++)
			{
				levels.add(ccm.getInt(prefix + "spellLevel." + i));
				binds.add(ccm.getInt(prefix + "spellBind." + i));
			}
			
			//Remove them.
			ccm.set(prefix + "spellLevel", null);
			ccm.set(prefix + "spellBind", null);
			
			//Add in defaults for the class.
			for (int i = levels.size(); i < rpgClass.getSpellBook().size(); i++)
			{
				levels.add(0);
				binds.add(999);
			}
			
			setSpellLevels(levels);
			setSpellBinds(binds);
		}
	}
	
	public void setDefaults()
	{
		//Variable Declarations
		Date now = new Date();
		
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
		setShowDamageTaken(true);
		setLastRecievedHourlyItems(now.getTime());
		setClassChangeTickets(0);
		
		if (getSecondsPlayed() == 0)
			setSecondsPlayed(0);
		
		//Use a temporary rpgClass.
		RpgClass tempClass = FC_Rpg.classConfig.getClassByID(getCombatClass());
		
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
	}
	
	public void setPlayerDefaults(int pickedClass, boolean manualDistribution)
	{
		setDefaults();
		
		setIsActive(true);
		setCombatClass(pickedClass);
		setClassLevel(1);
		setJobRank(1);
		
		//Give the player stats based on their choice.
		if (manualDistribution == true)
		{
			setStats(10);
			setAutomaticAllocation(false);
		}
		else
		{
			assignClassStatPoints();
			setAutomaticAllocation(true);
		}
	}
    
    public boolean isDonator() 
	{
		Date now = new Date();
		
		//Store time into memory
		Long time = getDonatorTime();
		
		//If the donator time is less than what time is currently is.
		if (time > now.getTime()) 
			return true;
		else
		{
			//If there is any time on the donator time, then clear it.
			if (time > 0)
				setDonatorTime((long) 0);
			
			return false; 
		}
	}
    
    public void addLevels(int level)
	{
		int targetLevel = getClassLevel() + level;
		
		//Add enough to levelup onece
		addOfflineClassExperience(getLevelUpAmount() - getClassExperience(), false);
		
		//Keep adding experience to levelup until the player is the new level.
		while (getClassLevel() < targetLevel)
		{
			addOfflineClassExperience(getLevelUpAmount() - getClassExperience(), false);
			
			if (getClassLevel() == levelCap)
				return;
		}
		
		//Make sure to reset class experience to 0.
		setClassExperience(0);
	}
    
    public void addOfflineClassExperience(double x, boolean displayLevelUpMessage)
	{
		//Variable declarations
		double classExperience = getClassExperience() + x;
		
		//Account for level cap.
		if (getClassLevel() == levelCap)
		{
			setClassExperience(0);
			return;
		}
		
		//While the player has enough experience to levelup, level up.
		while (classExperience >= getLevelUpAmount())
		{
			//Add the experience to the player.
			classExperience = classExperience - getLevelUpAmount();
			
			//Increase the players level.
			setClassLevel(getClassLevel() + 1);
			
			//Spell points.
			if (getClassLevel() % FC_Rpg.generalConfig.getLevelsPerSkillPoint() == 0)
				setSpellPoints(getSpellPoints() + 1);
			
			//Acount for level cap.
			if (getClassLevel() == levelCap)
			{
				setClassExperience(0);
				return;
			}
			
			//Manage Stat Points
			if (getManualAllocation() == true)
				assignClassStatPoints(); 
			else
				setStats(getStats() + FC_Rpg.generalConfig.getStatsPerLevel());
			
			if (displayLevelUpMessage == true)
				FC_Rpg.bLib.standardBroadcast(name + " is now level [" + String.valueOf(getClassLevel()) + "]");	//Broadcast that he leveled up
		}
		
		//we now set player class experience to passed in experience.
		setClassExperience(classExperience);
	}
    
    public void assignClassStatPoints()
	{
    	List<Integer> statGrowth = rpgClass.getStatGrowth();
    	
    	//Assign stat points based on class.
    	for (RpgClass rpgClass : FC_Rpg.classConfig.getRpgClasses())
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
		setMaxHealthFile(100 + getConstitution() * 20);
		setMaxManaFile(20 + getIntelligence() * 1);
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
}












