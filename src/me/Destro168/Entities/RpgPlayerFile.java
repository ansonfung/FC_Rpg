package me.Destro168.Entities;

import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;

import me.Destro168.ConfigManagers.SharedPlayerProfileManager;
import me.Destro168.Configs.ConfigOverlord;
import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.Util.SpellUtil;

// Handles storing of player data in files.
public class RpgPlayerFile extends RpgEntity
{
    private final String rpgPrefix = "FC_Rpg.";
	private SharedPlayerProfileManager sp;
	private int levelCap;
    protected String name;
    
	public String getName() { return name; }
	
	//Gets
	public boolean getIsActive()  //Very important get so it gets to take up space.
	{
		try	{
			return sp.getBoolean(rpgPrefix + "isActive");
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public double getVersion() { return sp.getDouble(rpgPrefix + "version"); }
	public String getCustomPrefix() { handleUpdates(); return sp.getString(rpgPrefix + "customPrefix"); }
	public String getActiveSpell() { return sp.getString(rpgPrefix + "activeSpell"); }
    public long getDonatorTime() { return sp.getLong(rpgPrefix + "donatorTime"); }
	public long getHunterLastPay() { return sp.getLong(rpgPrefix + "hunterLastPay"); }
	public long getLastAte() { return sp.getLong(rpgPrefix + "lastAte"); }
	public int getCombatClass() { return sp.getInt(rpgPrefix + "combatClass"); }
	public int getIntelligence() { return sp.getInt(rpgPrefix + "intelligence"); }
	public int getClassLevel() { return sp.getInt(rpgPrefix + "classLevel"); }
	public int getJobRank() { return sp.getInt(rpgPrefix + "jobRank"); }
	public int getStats() { return sp.getInt(rpgPrefix + "stats"); }
	public int getStrength() { return sp.getInt(rpgPrefix + "strength"); }
	public int getConstitution() { return sp.getInt(rpgPrefix + "constitution"); }
	public int getMagic() { return sp.getInt(rpgPrefix + "magic"); }
	public int getLifetimeMobKills() { return sp.getInt(rpgPrefix + "lifetimeMobKills"); }
	public int getClassChangeTickets() { return sp.getInt(rpgPrefix + "classChangeTickets"); }
	public int getHunterLevel() { return sp.getInt(rpgPrefix + "hunterLevel"); }
	public int getSecondsPlayed() { return sp.getInt(rpgPrefix + "secondsPlayed"); }
	public int getSpellPoints() { return sp.getInt(rpgPrefix + "spellPoints"); }
	public int getSpellLevel(int x) { return sp.getInt(rpgPrefix + "spellLevel." + x); }
	public int getSpellBind(int x) { return sp.getInt(rpgPrefix + "spellBind." + x); }
	public double getClassExperience() { return sp.getDouble(rpgPrefix + "classExperience"); }
	public boolean getManualAllocation() { return sp.getBoolean(rpgPrefix + "manualAllocation"); }
	public boolean getShowDamageTaken() { return sp.getBoolean(rpgPrefix + "showDamageTaken"); }
	public boolean getShowDamageGiven() { return sp.getBoolean(rpgPrefix + "showDamageGiven"); }
	public boolean getHunterCanKit() { return sp.getBoolean(rpgPrefix + "hunterCanKit"); }
	public boolean getAutoCast() { return sp.getBoolean(rpgPrefix + "autoCast"); }
	public boolean getRankFreeze() { return sp.getBoolean(rpgPrefix + "rankFreeze"); }
	
	//Sets
	public void setVersion(double x) { sp.set(rpgPrefix + "version", x); }
	public void setCustomPrefix(String x) { sp.set(rpgPrefix + "customPrefix", x); }
	public void setActiveSpell(String x) { sp.set(rpgPrefix + "activeSpell", x); }
	public void setCombatClass(int x) { sp.set(rpgPrefix + "combatClass", x); }
	public void setClassLevel(int x) { sp.set(rpgPrefix + "classLevel", x); }
	public void setJobRank(int x) { sp.set(rpgPrefix + "jobRank", x); }
	public void setStrength(int x) { sp.set(rpgPrefix + "strength", x); }
	public void setConstitution(int x) { sp.set(rpgPrefix + "constitution", x); }
	public void setMagic(int x) { sp.set(rpgPrefix + "magic", x); }
	public void setStats(int x) { sp.set(rpgPrefix + "stats", x); }
	public void setIntelligence(int x) { sp.set(rpgPrefix + "intelligence", x); }
	public void setLifetimeMobKills(int x) { sp.set(rpgPrefix + "lifetimeMobKills", x); }
	public void setClassChangeTickets(int x) { sp.set(rpgPrefix + "classChangeTickets", x); }
	public void setHunterLevel(int x) { sp.set(rpgPrefix + "hunterLevel", x); }
	public void setSecondsPlayed(int x) { sp.set(rpgPrefix + "secondsPlayed", x); }
	public void setSpellPoints(int x) { sp.set(rpgPrefix + "spellPoints", x); }
	public void setSpellLevel(int x, int y) { sp.set(rpgPrefix + "spellLevel." + x, y); }
	public void setSpellBind(int x, int y) { sp.set(rpgPrefix + "spellBind." + x, y); }
	public void setDonatorTime(Long x) { sp.set(rpgPrefix + "donatorTime", x); }
	public void setHunterLastPay(long x) { sp.set(rpgPrefix + "hunterLastPay", x); }
	public void setLastAte(long x) { sp.set(rpgPrefix + "lastAte", x); }
	public void setClassExperience(double x) { sp.set(rpgPrefix + "classExperience", x); }
	public void setIsActive(boolean x) { sp.set(rpgPrefix + "isActive",x); }
	public void setAutomaticAllocation(boolean x) { sp.set(rpgPrefix + "manualAllocation",x); }
	public void setShowDamageTaken(boolean x) { sp.set(rpgPrefix + "showDamageTaken",x); }
	public void setShowDamageGiven(boolean x) { sp.set(rpgPrefix + "showDamageGiven",x); }
	public void setHunterCanKit(boolean x) { sp.set(rpgPrefix + "hunterCanKit",x); }
	public void setAutoCast(boolean x) { sp.set(rpgPrefix + "autoCast",x); }
	public void setRankFreeze(boolean x) { sp.set(rpgPrefix + "rankFreeze",x); }
	
	//Status Set/Gets
	public long getStatusDodge() { return sp.getLong(rpgPrefix + "status.dodge"); }
	public int getStatusDodgeStrength() { return sp.getInt(rpgPrefix + "status.dodgeStrength"); }
	public long getStatusMorale() { return sp.getLong(rpgPrefix + "status.morale"); }
	public int getStatusMoraleStrength() { return sp.getInt(rpgPrefix + "status.moraleStrength"); }
	public long getStatusTaunt() { return sp.getLong(rpgPrefix + "status.taunt"); }
	public int getStatusTauntStrength() { return sp.getInt(rpgPrefix + "status.tauntStrength"); }
	public long getStatusThorns() { return sp.getLong(rpgPrefix + "status.thorns"); }
	public int getStatusThornsStrength() { return sp.getInt(rpgPrefix + "status.thornsStrength"); }
	public long getStatusBloodthirst() { return sp.getLong(rpgPrefix + "status.bloodthirst"); }
	public int getStatusBloodthirstStrength() { return sp.getInt(rpgPrefix + "status.bloodthirstStrength"); }
	public long getStatusFerocity() { return sp.getLong(rpgPrefix + "status.ferocity"); }
	public double getStatusFerocityStrength() { return sp.getDouble(rpgPrefix + "status.ferocityStrength"); }
	public long getStatusFireArrow() { return sp.getLong(rpgPrefix + "status.fireArrow"); }
	public long getStatusDisabled() { return sp.getLong(rpgPrefix + "status.disabled"); }
	public long getStatusImmortal() { return sp.getLong(rpgPrefix + "status.immortal"); }
	
	public void setStatusDodge(double x) { sp.set(rpgPrefix + "status.dodge", getFutureDate(x)); }
	public void setStatusDodgeStrength(int x) { sp.set(rpgPrefix + "status.dodgeStrength", x); }
	public void setStatusMorale(double x) { sp.set(rpgPrefix + "status.morale", getFutureDate(x)); }
	public void setStatusMoraleStrength(int x) { sp.set(rpgPrefix + "status.moraleStrength", x); }
	public void setStatusTaunt(double x) { sp.set(rpgPrefix + "status.taunt", getFutureDate(x)); }
	public void setStatusTauntStrength(int x) { sp.set(rpgPrefix + "status.tauntStrength", x); }
	public void setStatusThorns(double x) { sp.set(rpgPrefix + "status.thorns", getFutureDate(x)); }
	public void setStatusThornsStrength(int x) { sp.set(rpgPrefix + "status.thornsStrength", getFutureDate(x)); }
	public void setStatusBloodthirst(double x) { sp.set(rpgPrefix + "status.bloodthirst", getFutureDate(x)); }
	public void setStatusBloodthirstStrength(int x) { sp.set(rpgPrefix + "status.bloodthirstStrength", getFutureDate(x)); }
	public void setStatusFerocity(double x) { sp.set(rpgPrefix + "status.ferocity", getFutureDate(x)); }
	public void setStatusFerocityStrength(double x) { sp.set(rpgPrefix + "status.ferocityStrength", getFutureDate(x)); }
	public void setStatusFireArrow(double x) { sp.set(rpgPrefix + "status.fireArrow", getFutureDate(x)); }
	public void setStatusDisabled(double x) { sp.set(rpgPrefix + "status.disabled", getFutureDate(x)); }
	public void setStatusImmortal(double x) { sp.set(rpgPrefix + "status.immortal", getFutureDate(x)); }
	
	//Mana/Hp sets and gets.
	public void setCurManaFile(double x) { sp.set(rpgPrefix + "curMana", x); }
	public void setMaxManaFile(double x) { sp.set(rpgPrefix + "maxMana", x); }
	public void setCurHealthFile(double x) { sp.set(rpgPrefix + "curHealth", x); }
	public void setMaxHealthFile(double x) { sp.set(rpgPrefix + "maxHealth", x); }
	
	public double getCurManaFile() { return sp.getDouble(rpgPrefix + "curMana"); }
	public double getMaxManaFile() { return sp.getDouble(rpgPrefix + "maxMana"); }
	public double getCurHealthFile() { return sp.getDouble(rpgPrefix + "curHealth"); }
	public double getMaxHealthFile() { return sp.getDouble(rpgPrefix + "maxHealth"); }
	
	//Misc functions
	public synchronized void clearAllData() { sp.clearFileData(); }
	public double getRequiredExpPercent() { return (getClassExperience() * 100) / getLevelUpAmount(); }
	public int getLevelUpAmount() { return 3 * getClassLevel() * getClassLevel() + 11; }
	public void resetActiveSpell() { sp.set(rpgPrefix + "activeSpell", "none"); }
	
	public RpgPlayerFile()
	{
		name = "";
	}
	
	public RpgPlayerFile(String playerName)
	{
		ConfigOverlord co = new ConfigOverlord();
		
		//Set the levelcap.
		levelCap = co.getLevelCap();
		
		setPlayerName(playerName);
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
		
		//Create the new profile manager.
		sp = new SharedPlayerProfileManager(playerName, FC_Rpg.plugin.getDataFolder().getAbsolutePath());
		
		//Handle updates.
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If not active, return.
		if (getIsActive() == false)
			return;
		
		/* if (getVersion() < 1.1)
		{
			//Update the version.
			setVersion(1.1);
		} */
	}
	
	public void setDefaults()
	{
		//Variable Declarations
		Date now = new Date();
		ConfigOverlord co = new ConfigOverlord();
		
		//Set variables to the new style
		setCustomPrefix(co.getDefaultPrefix());
		setIsActive(false);
		
		//Don't wipe out donator time for players that donated.
		if (getDonatorTime() == 0)
			setDonatorTime((long) 0);
		
		setActiveSpell("none");
		setCombatClass(0);
		setClassLevel(0);
		setClassExperience(0);
		setJobRank(0);
		setStrength(0);
		setConstitution(0);
		setMagic(0);
		setIntelligence(0);
		
		setStats(0);
		setStrength(0);
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
		setHunterLastPay(now.getTime());
		setHunterCanKit(true);
		setLastAte(now.getTime());
		setClassChangeTickets(0);
		setHunterLevel(0);
		
		if (getSecondsPlayed() == 0)
			setSecondsPlayed(0);
		
		//Set the spell level for all spells to 0.
		for (int i = 0; i < SpellUtil.CLASS_SPELL_COUNT; i++)
		{
			setSpellLevel(i, 0);
			setSpellBind(i, 999);
		}
		
		setSpellPoints(1);
		setAutoCast(false);
		setRankFreeze(false);
		
		//Set default buffs.
		setStatusDodge(0);
		setStatusDodgeStrength(0);
		setStatusMorale(0);
		setStatusMoraleStrength(0);
		setStatusTaunt(0);
		setStatusTauntStrength(0);
		setStatusThorns(0);
		setStatusThornsStrength(0);
		setStatusBloodthirst(0);
		setStatusBloodthirstStrength(0);
		setStatusFerocity(0);
		setStatusFerocityStrength(0);
		setStatusFireArrow(0);
		setStatusDisabled(0);
		setStatusImmortal(0);
		
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
			if (getClassLevel() % 4 == 0)
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
				setStats(getStats() + 10);
			
			if (displayLevelUpMessage == true)
				FC_Rpg.bLib.standardBroadcast(name + " is now level [" + String.valueOf(getClassLevel()) + "]");	//Broadcast that he leveled up
		}
		
		//we now set player class experience to passed in experience.
		setClassExperience(classExperience);
	}
    
    public void assignClassStatPoints()
	{
		//Assign stat points to players based on class.
		if (getCombatClass() == FC_Rpg.c_int_swordsman)	
		{
			setStrength(getStrength() + 3);
			setConstitution(getConstitution() + 3);
			setMagic(getMagic() + 2);
			setIntelligence(getIntelligence() + 2);
		}
		else if (getCombatClass() == FC_Rpg.c_int_assassin)	
		{
			setStrength(getStrength() + 5);
			setConstitution(getConstitution() + 1);
			setMagic(getMagic() + 2);
			setIntelligence(getIntelligence() + 2);
		}
		else if (getCombatClass() == FC_Rpg.c_int_wizard)	
		{
			setConstitution(getConstitution() + 2);
			setMagic(getMagic() + 4);
			setIntelligence(getIntelligence() + 4);
		}
		else if (getCombatClass() == FC_Rpg.c_int_defender)	
		{
			setStrength(getStrength() + 1);
			setConstitution(getConstitution() + 5);
			setMagic(getMagic() + 2);
			setIntelligence(getIntelligence() + 2);
		}
		else if (getCombatClass() == FC_Rpg.c_int_berserker)
		{
			setStrength(getStrength() + 3);
			setConstitution(getConstitution() + 5);
			setIntelligence(getIntelligence() + 2);
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
}












