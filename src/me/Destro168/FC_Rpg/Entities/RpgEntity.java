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
		Date now = new Date();
		
		if (now.getTime() < statusExpirationDate)
			return true;
		
		return false;
	}
	
	public void summon_Add(final Entity entity, int expiration)
	{
		//Only perform if the summon isn't owned already.
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
		//Only perform if the summon is owned.
		if (summon_Owns(entity) == false)
			return;
		
		//Cancel the task whenevr the expiration was forced.
		if (natural == false)
			Bukkit.getScheduler().cancelTask(summonTIDMap.get(entity));
		
		//Remove the entry from the map.
		summonTIDMap.remove(entity);
	}
	
	public boolean summon_Owns(Entity entity)
	{
		if (entity == null)
			return false;
		
		//Return true if contained.
		if (summon.contains(entity))
			return true;
		
		//Return false if not contained.
		return false;
	}
}


/*
Todo:

Combat:
- Item drops from mobs
- Defense for mobs the further out.
- Hourly epic boss fights.
- Admin power to spawn bosses of different tiers.

-Classes:
- Magic system
- Ability to view a character page
- Information toggling:
/rpg enable/disable DamageRecieved
/rpg enable/disable DamageDealt
/rpg enable/disable MagicRemaining
/rpg enable/disable regeneration
 
-Jobs:
- Promotions
- Restrictions to doing anything unless you are in the right job
- Gives farmers no hunger loss.
- Able to switch jobs if you aren't 2nd tier of it.

Overall:
Server help command. /server help [1-infinity]
Statistics about number of people in everything: /rpg total --> shows total in each class and in each job.
- Lots more in-game admin controls.
 */

/*

x = entity.getLocation().getBlockX();
y = entity.getLocation().getBlockY();
z = entity.getLocation().getBlockZ();

1 strength = 1/2 damage;
1 defense = 1/2 damage mitigation;
1 dexterity = 1/2 bow damage;
1 magic = 0.5% more spell damage;
1 constitution = 10 health;
1 intelligence = 5 mana;

100, 100, 60
1 + 1 * 1
1

1, 1, 60
 */

/*
Mobs only need:
Damage, defense, health.
 */

//Respec option with cost based on level.
//(Level * 5)^2 

/*
Combat things to do:

Make mobs spawn stronger based on distance from 0,0 and depth.





 */

/*
1 strength = 1/2 damage;
1 defense = 1/2 damage mitigation;
1 dexterity = 1/4th damage, .075% dodge per point
1 magic = 0.5% more spell damage;
1 constitution = 10 health;
1 intelligence = 5 mana;

Wooden = 1.25x more
Iron = 1.5x more
Gold = 1.75x more
Diamond = 2x

Gold Items = 656 uses.

Gold Helmet = 265 uses.
Gold Chestplate = 385 uses.
Gold Leggings = 361 uses.
Gold Boots = 144 uses.
*/

/*
Interactions and how they should play out:
When the player logs on, all of his information is loaded into an RPG player.
Then the information of RPG player is substitued into the default minecraft system.

Health for health and Damage for damage

Cancel out damage done to mobs and damage done to players. Cancel out all entity damage.
Calculate health loss from rpg player health

Mob stats will always be in memory because they will be created based on predefined stuff.

Player stats stay configuration side.


Later:
When the player attempts to do things, he needs permissions based on his class/rank.
Disable XP orbs with world guard.

Normal Tier and Boss Tier monsters
A boss tier monster will have firewalk.

You can only attack 10 times per second.

*/