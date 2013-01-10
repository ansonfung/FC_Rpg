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
import me.Destro168.FC_Rpg.Spells.EffectIDs;
import me.Destro168.FC_Suite_Shared.NameMatcher;
import me.Destro168.FC_Suite_Shared.Messaging.MessageLib;
import me.Destro168.FC_Suite_Shared.TimeUtils.DateManager;

// Handles storing of player data in files.
public class PlayerConfig extends ConfigGod
{
	private int levelCap;
	protected RpgClass rpgClass;
    protected String name;
    public boolean hasAlchemy;
    
	public String getName() { return name; }
	public RpgClass getRpgClass() { return rpgClass; }
	
	public boolean getHasAlchemy() { return hasAlchemy; }
	
	//Gets
	public boolean getIsActive()  //Very important get so it gets to take up space.
	{
		try	{
			return fcw.getBoolean(prefix + "isActive");
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public String getCustomPrefix() { return fcw.getString(prefix + "customPrefix"); }
	public String getActiveSpell() { return fcw.getString(prefix + "activeSpell"); }
    public long getDonatorTime() { return fcw.getLong(prefix + "donatorTime"); }
	public long getHunterLastPay() { return fcw.getLong(prefix + "hunterLastPay"); }
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
	
	public List<Integer> getSpellLevels() { return converter.getIntegerListFromString(fcw.getString(prefix + "spell.levels")); }
	public List<Integer> getSpellBinds() { return converter.getIntegerListFromString(fcw.getString(prefix + "spell.binds")); }
	
	public double getClassExperience() { return fcw.getDouble(prefix + "classExperience"); }
	public boolean getManualAllocation() { return fcw.getBoolean(prefix + "manualAllocation"); }
	public boolean getShowDamageTaken() { return fcw.getBoolean(prefix + "showDamageTaken"); }
	public boolean getShowDamageGiven() { return fcw.getBoolean(prefix + "showDamageGiven"); }
	public boolean getHunterCanKit() { return fcw.getBoolean(prefix + "hunterCanKit"); }
	public boolean getAutoCast() { return fcw.getBoolean(prefix + "autoCast"); }
	public double getArcanium() { return fcw.getDouble(prefix + "arcanium"); }
	
	public long getLastDungeonCompletion() { return fcw.getLong(prefix + "lastDungeonCompletion"); }
	public long getLastRecievedHourlyItems() { return fcw.getLong(prefix + "lastRecievedHourlyItems"); }
	
	//Sets
	public void setCustomPrefix(String x) { fcw.set(prefix + "customPrefix", x); }
	public void setActiveSpell(String x) { fcw.set(prefix + "activeSpell", x); }
	public void setCombatClass(int x) { 
		fcw.set(prefix + "combatClass", x);
		refreshClass();
	}
	public void setClassLevel(int x) { fcw.set(prefix + "classLevel", x); }
	public void setJobRank(int x) { fcw.set(prefix + "jobRank", x); }
	public void setAttack(int x) { fcw.set(prefix + "attack", x); }
	public void setConstitution(int x) { fcw.set(prefix + "constitution", x); }
	public void setMagic(int x) { fcw.set(prefix + "magic", x); }
	public void setStats(int x) { fcw.set(prefix + "stats", x); }
	public void setIntelligence(int x) { fcw.set(prefix + "intelligence", x); }
	public void setLifetimeMobKills(int x) { fcw.set(prefix + "lifetimeMobKills", x); }
	public void setClassChangeTickets(int x) { fcw.set(prefix + "classChangeTickets", x); }
	public void setSecondsPlayed(int x) { fcw.set(prefix + "secondsPlayed", x); }
	public void setSpellPoints(int x) { fcw.set(prefix + "spellPoints", x); }
	
	public void setSpellLevels(int x) { fcw.set(prefix + "spell.levels", x); }
	public void setSpellBinds(int x) { fcw.set(prefix + "spell.binds", x); }
	public void setSpellLevels(List<Integer> x) { fcw.setCustomList(prefix + "spell.levels", x); }
	public void setSpellBinds(List<Integer> x) { fcw.setCustomList(prefix + "spell.binds", x); }
	
	public void setDonatorTime(Long x) { fcw.set(prefix + "donatorTime", x); }
	public void setClassExperience(double x) { fcw.set(prefix + "classExperience", x); }
	public void setIsActive(boolean x) { fcw.set(prefix + "isActive",x); }
	public void setAutomaticAllocation(boolean x) { fcw.set(prefix + "manualAllocation",x); }
	public void setShowDamageTaken(boolean x) { fcw.set(prefix + "showDamageTaken",x); }
	public void setShowDamageGiven(boolean x) { fcw.set(prefix + "showDamageGiven",x); }
	public void setAutoCast(boolean x) { fcw.set(prefix + "autoCast",x); }
	public void setArcanium(double x) { fcw.set(prefix + "arcanium",x); }

	public void setLastDungeonCompletion(long x) { fcw.set(prefix + "lastDungeonCompletion", x); }
	public void setLastRecievedHourlyItems(long x) { fcw.set(prefix + "lastRecievedHourlyItems", x); }
	
	//Spell Status Set/Gets
	public void setStatusDuration(int effectID, int x) { //Sets the duration of a status in seconds.
		DateManager dm = new DateManager();
		fcw.set(prefix + "status." + effectID + ".duration", dm.getFutureDate_Milliseconds(x)); }
	public void setStatusMagnitude(int effectID, double x) { fcw.set(prefix + "status." + effectID + ".magnitude", x); }
	public void setStatusUses(int effectID, int x) { fcw.set(prefix + "status." + effectID + ".uses", x); }
	
	public long getStatusDuration(int effectID) { return fcw.getLong(prefix + "status." + effectID + ".duration"); }
	public double getStatusMagnitude(int effectID) { return fcw.getDouble(prefix + "status." + effectID + ".magnitude"); }
	public int getStatusUses(int effectID) { return fcw.getInt(prefix + "status." + effectID + ".uses"); }
	
	//Mana/Hp sets and gets.
	public void setCurManaFile(double x) { fcw.set(prefix + "curMana", x); }
	public void setMaxManaFile(double x) { fcw.set(prefix + "maxMana", x); }
	public void setCurHealthFile(double x) { fcw.set(prefix + "curHealth", x); }
	public void setMaxHealthFile(double x) { fcw.set(prefix + "maxHealth", x); }
	
	public double getCurManaFile() { return fcw.getDouble(prefix + "curMana"); }
	public double getMaxManaFile() { return fcw.getDouble(prefix + "maxMana"); }
	public double getCurHealthFile() { return fcw.getDouble(prefix + "curHealth"); }
	public double getMaxHealthFile() { return fcw.getDouble(prefix + "maxHealth"); }
	
	//Misc functions
	public synchronized void clearAllData() { 
		FileConfigurationWrapper temp = new FileConfigurationWrapper(FC_Rpg.dataFolderAbsolutePath + "/userinfo", name);
		temp.clearFileData();
	}
	public double getRequiredExpPercent() { return (getClassExperience() * 100) / getLevelUpAmount(); }
	public int getLevelUpAmount() { return (int) (getClassLevel() * getClassLevel() * FC_Rpg.balanceConfig.getPlayerExpScaleRate() + FC_Rpg.balanceConfig.getPlayerExpScaleBase()); }
	public void resetActiveSpell() { fcw.set(prefix + "activeSpell", "none"); }
	public void subtractArcanium(double x) { setArcanium(getArcanium() - x); }
	public void addArcanium(double x) { setArcanium(getArcanium() + x); }
	
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
			
			if (s.getEffectID() == EffectIDs.ALCHEMY)
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
			fcw.set(prefix + "lastAte", null);
			fcw.set(prefix + "rankFreeze", null);
		}
		
		if (getVersion() < 0.2)
		{
			setVersion(0.2);
			
			List<Integer> levels = new ArrayList<Integer>();
			List<Integer> binds = new ArrayList<Integer>();
			
			//Load up old spell levels and spell binds.
			for (int i = 0; i < 5; i++)
			{
				levels.add(fcw.getInt(prefix + "spellLevel." + i));
				binds.add(fcw.getInt(prefix + "spellBind." + i));
			}
			
			//Remove them.
			fcw.set(prefix + "spellLevel", null);
			fcw.set(prefix + "spellBind", null);
			
			//Add in defaults for the class.
			for (int i = levels.size(); i < rpgClass.getSpellBook().size(); i++)
			{
				levels.add(0);
				binds.add(999);
			}
			
			setSpellLevels(levels);
			setSpellBinds(binds);
		}
		
		if (getVersion() < 0.3)
		{
			setVersion(0.3);
			setArcanium(0);
		}
		
		if (getVersion() < 0.4)
		{
			setVersion(0.4);
			setLastDungeonCompletion(0);
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
		
		setLastDungeonCompletion(0);
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
		addOfflineClassExperience(getLevelUpAmount() - getClassExperience(), false, null);
		
		//Keep adding experience to levelup until the player is the new level.
		while (getClassLevel() < targetLevel)
		{
			addOfflineClassExperience(getLevelUpAmount() - getClassExperience(), false, null);
			
			if (getClassLevel() == levelCap)
				return;
		}
		
		//Make sure to reset class experience to 0.
		setClassExperience(0);
	}
    
    public boolean addOfflineClassExperience(double x, boolean displayLevelUpMessage, RpgPlayer rpgPlayer)
	{
		//Variable declarations
		double newExperience = getClassExperience() + x;
		
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
			if (getClassLevel() % FC_Rpg.balanceConfig.getPlayerLevelsPerSkillPoint() == 0)
				setSpellPoints(getSpellPoints() + 1);
			
			//Acount for level cap.
			if (getClassLevel() == levelCap)
			{
				setClassExperience(0);
				return false;
			}
			
			//Manage Stat Points
			if (getManualAllocation() == true)
				assignClassStatPoints();
			else
				setStats(getStats() + FC_Rpg.balanceConfig.getPlayerStatsPerLevel());
			
			if (displayLevelUpMessage == true)
			{
				if (getClassLevel() >= 50)
					FC_Rpg.rpgBroadcast.rpgBroadcast(name + " is now level [" + String.valueOf(getClassLevel()) + "]!");	//Broadcast that he leveled up
				else
				{
					if (rpgPlayer != null)
					{
						MessageLib msgLib = new MessageLib(rpgPlayer.getPlayer());
						msgLib.infiniteMessage("You have just reached level [",getClassLevel() + "","]!");
					}
				}
				
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
		setMaxHealthFile(FC_Rpg.balanceConfig.getPlayerBaseHealth() + getConstitution() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeConstitution());
		setMaxManaFile(FC_Rpg.balanceConfig.getPlayerBaseMana() + getIntelligence() * FC_Rpg.balanceConfig.getPlayerStatMagnitudeIntelligence());
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
}












