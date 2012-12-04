package me.Destro168.FC_Rpg.Configs;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;

public class BalanceConfig extends ConfigGod
{
	private void setWoodSwordMultiplier(double x) { fcw.set(prefix + "woodSwordMultiplier", x); }
	private void setStoneSwordMultiplier(double x) { fcw.set(prefix + "stoneSwordMultiplier", x); }
	private void setIronSwordMultiplier(double x) { fcw.set(prefix + "ironSwordMultiplier", x); }
	private void setDiamondSwordMultiplier(double x) { fcw.set(prefix + "diamondSwordMultiplier", x); }
	private void setGoldSwordMultiplier(double x) { fcw.set(prefix + "goldSwordMultiplier", x); }
	
	private void setStoneSwordAttackReq(double x) { fcw.set(prefix + "stoneSwordAttackReq", x); }
	private void setIronSwordAttackReq(double x) { fcw.set(prefix + "ironSwordAttackReq", x); }
	private void setDiamondSwordAttackReq(double x) { fcw.set(prefix + "diamondSwordAttackReq", x); }
	private void setGoldSwordAttackReq(double x) { fcw.set(prefix + "goldSwordAttackReq", x); }
	
	private void setMobAttackHardMultiplier(int x) { fcw.set(prefix + "attackMultiplier", x); }
	private void setMobConstitutionMultiplier(int x) { fcw.set(prefix + "constitutionMultiplier", x); }
	private void setRandomMobLevelDeviation(int x) { fcw.set(prefix + "randomMobLevelDeviation", x); }
	private void setBasePlayerHealth(int x) { fcw.set(prefix + "basePlayerHealth", x); }
	private void setBasePlayerMana(int x) { fcw.set(prefix + "basePlayerMana", x); }
	private void setAttackImpact(int x) { fcw.set(prefix + "attackImpact", x); }
	private void setConstitutionImpact(int x) { fcw.set(prefix + "constitutionImpact", x); }
	private void setIntelligenceImpact(int x) { fcw.set(prefix + "intelligenceImpact", x); }
	private void setDifficultyScalor(int x) { fcw.set(prefix + "difficultyScalor", x); }
	
	//Add weapon damage multipliers.
	public double getWoodSwordMultiplier() { return fcw.getDouble(prefix + "woodSwordMultiplier"); }
	public double getStoneSwordMultiplier() { return fcw.getDouble(prefix + "stoneSwordMultiplier"); }
	public double getIronSwordMultiplier() { return fcw.getDouble(prefix + "ironSwordMultiplier"); }
	public double getDiamondSwordMultiplier() { return fcw.getDouble(prefix + "diamondSwordMultiplier"); }
	public double getGoldSwordMultiplier() { return fcw.getDouble(prefix + "goldSwordMultiplier"); }
	
	public double getWoodSwordAttackReq() { return fcw.getDouble(prefix + "woodSwordAttackReq"); }
	public double getStoneSwordAttackReq() { return fcw.getDouble(prefix + "stoneSwordAttackReq"); }
	public double getIronSwordAttackReq() { return fcw.getDouble(prefix + "ironSwordAttackReq"); }
	public double getDiamondSwordAttackReq() { return fcw.getDouble(prefix + "diamondSwordAttackReq"); }
	public double getGoldSwordAttackReq() { return fcw.getDouble(prefix + "goldSwordAttackReq"); }
	
	public int getMobAttackMultiplier() { return fcw.getInt(prefix + "attackMultiplier"); }
	public int getMobConstitutionMultiplier() { return fcw.getInt(prefix + "constitutionMultiplier"); }
	
	public int getDifficultyScalor() { return fcw.getInt(prefix + "difficultyScalor"); }
	public int getBasePlayerHealth() { return fcw.getInt(prefix + "basePlayerHealth"); }
	public int getBasePlayerMana() { return fcw.getInt(prefix + "basePlayerMana"); }
	public int getAttackImpact() { return fcw.getInt(prefix + "attackImpact"); }
	public int getConstitutionImpact() { return fcw.getInt(prefix + "constitutionImpact"); }
	public int getIntelligenceImpact() { return fcw.getInt(prefix + "intelligenceImpact"); }
	public int getRandomMobLevelDeviation() { return fcw.getInt(prefix + "randomMobLevelDeviation"); }
	
	public BalanceConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Balance");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If no config was previously created, then...
		if (getVersion() < 0.1)
		{
			//Set version
			setVersion(0.1);
			
			//Use magicmultiplier for spells to set damage.
			setMobAttackHardMultiplier(2);
			setMobConstitutionMultiplier(100);
			setBasePlayerHealth(100);
			setBasePlayerMana(20);
			
			setWoodSwordMultiplier(1.25);
			setStoneSwordMultiplier(1.5);
			setIronSwordMultiplier(1.75);
			setDiamondSwordMultiplier(2);
			setGoldSwordMultiplier(2.5);
			
			setStoneSwordAttackReq(124);
			setIronSwordAttackReq(249);
			setDiamondSwordAttackReq(374);
			setGoldSwordAttackReq(499);
			
			setAttackImpact(1);
			setConstitutionImpact(20);
			setIntelligenceImpact(1);
			setDifficultyScalor(500);	//500 = roughly 20% increase from 0-100.
			setRandomMobLevelDeviation(2);
		}
	}
	
}
