package me.Destro168.FC_Rpg.Configs;

import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;
import me.Destro168.FC_Rpg.FC_Rpg;

public class PassiveConfig extends ConfigGod
{
	public static final int passive_ScalingArrows = 0;
	public static final int passive_CounterAttack = 1;
	public static final int passive_StrongerParry = 2;
	public static final int passive_BattleLust = 3;

	public void setScalingArrows(int x) { fcw.set(prefix + "scalingArrows", x); }
	public void setCounterAttack(int x) { fcw.set(prefix + "counterAttack", x); }
	public void setStrongerParry(double x) { fcw.set(prefix + "strongerParry", x); }
	public void setBattleLust(double x) { fcw.set(prefix + "battleLust", x); }
	
	public int getScalingArrow() { return fcw.getInt(prefix + "scalingArrows"); }
	public int getCounterAttack() { return fcw.getInt(prefix + "counterAttack"); }
	public double getStrongerParry() { return fcw.getDouble(prefix + "strongerParry"); }
	public double getBattleLust() { return fcw.getDouble(prefix + "battleLust"); }
	
	public PassiveConfig() 
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Passives");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		if (getVersion() < 0.1)
		{
			setVersion(0.1);
			
			setScalingArrows(28);
			setCounterAttack(10);
			setStrongerParry(.75);
			setBattleLust(.4);
		}
	}
}
















