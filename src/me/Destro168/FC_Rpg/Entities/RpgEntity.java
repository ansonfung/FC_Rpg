package me.Destro168.FC_Rpg.Entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Destro168.FC_Rpg.FC_Rpg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class RpgEntity
{
	protected Date lastDamaged;
	protected Date lastAttackNotification;
	protected Date lastDefenseNotification;
	private boolean isAlive;
	
	private Map<Entity, Integer> summonTIDMap;
	private List<Entity> summon;
	public int speedTID;
	
	public Date getLastDamage() { return lastDamaged; }
	public long getLastDamagedLong() { return lastDamaged.getTime(); }
	public long getLastAttackNotificationLong() { return lastAttackNotification.getTime(); }
	public long getLastDefenseNotificationLong() { return lastDefenseNotification.getTime(); }
	public boolean getIsAlive() { return isAlive; }
	public int getSpeedTID() { return speedTID; }
	
	public void setSpeedTID(int x) { speedTID = x; }
	
	public void setIsAlive(boolean x) { isAlive = x; }
	
	public RpgEntity()
	{
		Calendar gc = new GregorianCalendar();
		gc.setTimeInMillis(0);

		lastDamaged = gc.getTime();
		lastAttackNotification = gc.getTime();
		lastDefenseNotification = gc.getTime();
		summon = new ArrayList<Entity>();
		summonTIDMap = new HashMap<Entity, Integer>();
		isAlive = true;
		speedTID = -1;
	}

	public boolean getStatusActiveEntity(Long statusExpirationDate)
	{
		if (System.currentTimeMillis() < statusExpirationDate)
			return true;

		return false;
	}

	public void summon_Add(final Entity entity, int expiration)
	{
		// Only perform if the summon isn't owned already.
		if (summon_Owns(entity) == true)
			return;

		summon.add(entity);

		summonTIDMap.put(entity, Bukkit.getScheduler().scheduleSyncDelayedTask(FC_Rpg.plugin, new Runnable()
		{
			public void run()
			{
				summon_Remove(entity, true);
			}
		}, expiration));
	}

	public void summon_Remove(Entity entity, boolean natural)
	{
		// Only perform if the summon is owned.
		if (summon_Owns(entity) == false)
			return;

		// Cancel the task whenevr the expiration was forced.
		if (natural == false)
			Bukkit.getScheduler().cancelTask(summonTIDMap.get(entity));

		// Remove the entry from the map.
		summonTIDMap.remove(entity);
	}

	public boolean summon_Owns(Entity entity)
	{
		if (entity == null)
			return false;

		// Return true if contained.
		if (summon.contains(entity))
			return true;

		// Return false if not contained.
		return false;
	}
}


























