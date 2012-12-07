package me.Destro168.FC_Rpg.Configs;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;

public class BalanceConfig extends ConfigGod
{
	// Sets
	public void setGlobalExpMultiplier(int x) { fcw.set(prefix + "globalExpMultiplier", x); }
	
	private void setRandomMobLevelDeviation(int x) { fcw.set(prefix + "randomMobLevelDeviation", x); }
	private void setDifficultyScalor(int x) { fcw.set(prefix + "difficultyScalor", x); }
	private void setWitherLevelBonus(int x) { fcw.set(prefix + "witherLevelBonus", x); }
	private void setEnderDragonLevelBonus(int x) { fcw.set(prefix + "enderDragonLevelBonus", x); }
	private void setDefaultItemDrops(boolean x) { fcw.set(prefix + "defaultItemDrops", x); }
	private void setArrowKnockback(boolean x) { fcw.set(prefix + "arrowKnockback", x); }
	private void setPowerLevelPrevention(int x) { fcw.set(prefix + "powerLevelPrevention", x); }
	private void setBonusPercentCap(double x) { fcw.set(prefix + "bonusPercentCap", x); }
	private void setEnchantmentMultiplierSharpness(double x) { fcw.set(prefix + "enchantmentMultiplier.sharpness", x); }
	private void setEnchantmentMultiplierBane(double x) { fcw.set(prefix + "enchantmentMultiplier.bane", x); }
	private void setEnchantmentMultiplierSmite(double x) { fcw.set(prefix + "enchantmentMultiplier.smite", x); }
	private void setEnchantmentMultiplierPower(double x) { fcw.set(prefix + "enchantmentMultiplier.power", x); }
	private void setEnchantmentMultiplierProtection(double x) { fcw.set(prefix + "enchantmentMultiplier.protection", x); }
	private void setEnchantmentMultiplierFire(double x) { fcw.set(prefix + "enchantmentMultiplier.fire", x); }
	private void setEnchantmentMultiplierExplosion(double x) { fcw.set(prefix + "enchantmentMultiplier.explosion", x); }
	private void setEnchantmentMultiplierFall(double x) { fcw.set(prefix + "enchantmentMultiplier.fall", x); }
	private void setEnchantmentMultiplierProjectile(double x) { fcw.set(prefix + "enchantmentMultiplier.projectile", x); }
	
	private void setArmorWearRequirementChain(int x) { fcw.set(prefix + "armor.wearRequirement.chain", x); }
	private void setArmorWearRequirementIron(int x) { fcw.set(prefix + "armor.wearRequirement.iron", x); }
	private void setArmorWearRequirementDiamond(int x) { fcw.set(prefix + "armor.wearRequirement.diamond", x); }
	private void setArmorWearRequirementGold(int x) { fcw.set(prefix + "armor.wearRequirement.gold", x); }
	
	private void setArmorMultiplierLB(double x) { fcw.set(prefix + "armor.multiplier.leather.boots", x); }
	private void setArmorMultiplierLH(double x) { fcw.set(prefix + "armor.multiplier.leather.helmet", x); }
	private void setArmorMultiplierLL(double x) { fcw.set(prefix + "armor.multiplier.leather.leggings", x); }
	private void setArmorMultiplierLC(double x) { fcw.set(prefix + "armor.multiplier.leather.chestplate", x); }
	
	private void setArmorMultiplierCB(double x) { fcw.set(prefix + "armor.multiplier.chain.boots", x); }
	private void setArmorMultiplierCH(double x) { fcw.set(prefix + "armor.multiplier.chain.helmet", x); }
	private void setArmorMultiplierCL(double x) { fcw.set(prefix + "armor.multiplier.chain.leggings", x); }
	private void setArmorMultiplierCC(double x) { fcw.set(prefix + "armor.multiplier.chain.chestplate", x); }
	
	private void setArmorMultiplierIB(double x) { fcw.set(prefix + "armor.multiplier.iron.boots", x); }
	private void setArmorMultiplierIH(double x) { fcw.set(prefix + "armor.multiplier.iron.helmet", x); }
	private void setArmorMultiplierIL(double x) { fcw.set(prefix + "armor.multiplier.iron.leggings", x); }
	private void setArmorMultiplierIC(double x) { fcw.set(prefix + "armor.multiplier.iron.chestplate", x); }
	
	private void setArmorMultiplierDB(double x) { fcw.set(prefix + "armor.multiplier.diamond.boots", x); }
	private void setArmorMultiplierDH(double x) { fcw.set(prefix + "armor.multiplier.diamond.helmet", x); }
	private void setArmorMultiplierDL(double x) { fcw.set(prefix + "armor.multiplier.diamond.leggings", x); }
	private void setArmorMultiplierDC(double x) { fcw.set(prefix + "armor.multiplier.diamond.chestplate", x); }
	
	private void setArmorMultiplierGB(double x) { fcw.set(prefix + "armor.multiplier.gold.boots", x); }
	private void setArmorMultiplierGH(double x) { fcw.set(prefix + "armor.multiplier.gold.helmet", x); }
	private void setArmorMultiplierGL(double x) { fcw.set(prefix + "armor.multiplier.gold.leggings", x); }
	private void setArmorMultiplierGC(double x) { fcw.set(prefix + "armor.multiplier.gold.chestplate", x); }
	
	private void setMobLootPercentStronger(double x) { fcw.set(prefix + "mob.lootPercent.stronger", x); }
	private void setMobLootPercentWeaker(double x) { fcw.set(prefix + "mob.lootPercent.weaker", x); }
	private void setMobCashMultiplier(double x) { fcw.set(prefix + "mob.cashMultiplier", x); }
	private void setMobExpMultiplier(double x) { fcw.set(prefix + "mob.expMultiplier", x); }
	private void setMobSpawnWithItemChance(int x) { fcw.set(prefix + "mob.spawnWithItemChance", x); }
	private void setMobSpawnWithEnchantsChance(int x) { fcw.set(prefix + "mob.spawnWithEnchantsChance", x); }
	private void setMobAttackHardMultiplier(int x) { fcw.set(prefix + "mob.attackMultiplier", x); }
	private void setMobConstitutionMultiplier(int x) { fcw.set(prefix + "mob.constitutionMultiplier", x); }
	
	private void setSwordMultiplierWood(double x) { fcw.set(prefix + "sword.multiplier.wood", x); }
	private void setSwordMultiplierStone(double x) { fcw.set(prefix + "sword.multiplier.stone", x); }
	private void setSwordMultiplierIron(double x) { fcw.set(prefix + "sword.multiplier.iron", x); }
	private void setSwordMultiplierDiamond(double x) { fcw.set(prefix + "sword.multiplier.diamond", x); }
	private void setSwordMultiplierGold(double x) { fcw.set(prefix + "sword.multiplier.gold", x); }

	private void setSwordAttackRequirementWood(double x) { fcw.set(prefix + "sword.attackRequirement.wood", x); }
	private void setSwordAttackRequirementStone(double x) { fcw.set(prefix + "sword.attackRequirement.stone", x); }
	private void setSwordAttackRequirementIron(double x) { fcw.set(prefix + "sword.attackRequirement.iron", x); }
	private void setSwordAttackRequirementDiamond(double x) { fcw.set(prefix + "sword.attackRequirement.diamond", x); }
	private void setSwordAttackRequirementGold(double x) { fcw.set(prefix + "sword.attackRequirement.gold", x); }
	
	private void setPlayerBaseHealth(int x) { fcw.set(prefix + "player.baseHealth", x); }
	private void setPlayerBaseMana(int x) { fcw.set(prefix + "player.baseMana", x); }
	private void setPlayerStatMagnitudeAttack(int x) { fcw.set(prefix + "player.statMagnitutude.attack", x); }
	private void setPlayerStatMagnitudeConstitution(int x) { fcw.set(prefix + "player.statMagnitutude.constitution", x); }
	private void setPlayerStatMagnitudeIntelligence(int x) { fcw.set(prefix + "player.statMagnitutude.intelligence", x); }
	private void setPlayerLevelsPerSkillPoint(int x) { fcw.set(prefix + "player.levelsPerSkillPoint", x); }
	private void setPlayerStatsPerLevel(int x) { fcw.set(prefix + "player.statsPerLevel", x); }
	private void setPlayerExpScaleRate(int x) { fcw.set(prefix + "player.expScaleRate", x); }
	private void setPlayerExpScaleBase(int x) { fcw.set(prefix + "player.expScaleBase", x); }
	
	private void setDamageExplosion(double x) { fcw.set(prefix + "damage.explosion", x); }
	private void setDamageFall(double x) { fcw.set(prefix + "damage.fall", x); }
	private void setDamageContact(double x) { fcw.set(prefix + "damage.contact", x); }
	private void setDamageEntityAttack(double x) { fcw.set(prefix + "damage.entityAttack", x); }
	private void setDamageLightning(double x) { fcw.set(prefix + "damage.lightning", x); }
	private void setDamageFire(double x) { fcw.set(prefix + "damage.fire", x); }
	private void setDamageFireTick(double x) { fcw.set(prefix + "damage.firetick", x); }
	private void setDamageLava(double x) { fcw.set(prefix + "damage.lava", x); }
	private void setDamageStarvation(double x) { fcw.set(prefix + "damage.starvation", x); }
	private void setDamagePoison(double x) { fcw.set(prefix + "damage.poison", x); }
	private void setDamageMagic(double x) { fcw.set(prefix + "damage.magic", x); }
	private void setDamageBlockExplosion(double x) { fcw.set(prefix + "damage.blockExplosion", x); }
	private void setDamageWither(double x) { fcw.set(prefix + "damage.wither", x); }
	
	private void setPotionMultiplierStrength(double x) { fcw.set(prefix + "potionMultiplier.strength", x); }
	private void setPotionMultiplierWeakness(double x) { fcw.set(prefix + "potionMultiplier.weakness", x); }
	private void setPotionMultiplierResistance(double x) { fcw.set(prefix + "potionMultiplier.resistance", x); }
	
	private void setHealPercentEating(double x) { fcw.set(prefix + "healMultiplier.eating", x); }
	private void setHealPercentMagic(double x) { fcw.set(prefix + "healMultiplier.magic", x); }
	private void setHealPercentMagicRegen(double x) { fcw.set(prefix + "healMultiplier.magicRegen", x); }
	private void setHealPercentSatiated(double x) { fcw.set(prefix + "healMultiplier.satiated", x); }
	
	// Gets
	public int getGlobalExpMultiplier() { return fcw.getInt(prefix + "globalExpMultiplier"); }
	
	public int getPowerLevelPrevention() { return fcw.getIntS(prefix + "powerLevelPrevention"); }
	public int getWitherLevelBonus() { return fcw.getIntS(prefix + "witherLevelBonus"); }
	public int getEnderDragonLevelBonus() { return fcw.getIntS(prefix + "enderDragonLevelBonus"); }
	public boolean getDefaultItemDrops() { return fcw.getBooleanS(prefix + "defaultItemDrops"); }
	public boolean getArrowKnockback() { return fcw.getBooleanS(prefix + "arrowKnockback"); }
	public int getDifficultyScalor() { return fcw.getIntS(prefix + "difficultyScalor"); }
	public int getRandomMobLevelDeviation() { return fcw.getIntS(prefix + "randomMobLevelDeviation"); }
	public double getBonusPercentCap() { return fcw.getDoubleS(prefix + "bonusPercentCap"); }
	public double getEnchantmentMultiplierSharpness() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.sharpness"); }
	public double getEnchantmentMultiplierBane() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.bane"); }
	public double getEnchantmentMultiplierSmite() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.smite"); }
	public double getEnchantmentMultiplierPower() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.power"); }
	public double getEnchantmentMultiplierProtection() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.protection"); }
	public double getEnchantmentMultiplierFire() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.fire"); }
	public double getEnchantmentMultiplierExplosion() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.explosion"); }
	public double getEnchantmentMultiplierFall() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.fall"); }
	public double getEnchantmentMultiplierProjectile() { return fcw.getDoubleS(prefix + "enchantmentMultiplier.projectile"); }
	
	public int getArmorWearRequirementChain() { return fcw.getIntS(prefix + "armor.wearRequirement.chain"); }
	public int getArmorWearRequirementIron() { return fcw.getIntS(prefix + "armor.wearRequirement.iron"); }
	public int getArmorWearRequirementDiamond() { return fcw.getIntS(prefix + "armor.wearRequirement.diamond"); }
	public int getArmorWearRequirementGold() { return fcw.getIntS(prefix + "armor.wearRequirement.gold"); }
	
	public double getArmorMultiplierLB() { return fcw.getDoubleS(prefix + "armor.multiplier.leather.boots"); }
	public double getArmorMultiplierLH() { return fcw.getDoubleS(prefix + "armor.multiplier.leather.helmet"); }
	public double getArmorMultiplierLL() { return fcw.getDoubleS(prefix + "armor.multiplier.leather.leggings"); }
	public double getArmorMultiplierLC() { return fcw.getDoubleS(prefix + "armor.multiplier.leather.chestplate"); }
	
	public double getArmorMultiplierCB() { return fcw.getDoubleS(prefix + "armor.multiplier.chain.boots"); }
	public double getArmorMultiplierCH() { return fcw.getDoubleS(prefix + "armor.multiplier.chain.helmet"); }
	public double getArmorMultiplierCL() { return fcw.getDoubleS(prefix + "armor.multiplier.chain.leggings"); }
	public double getArmorMultiplierCC() { return fcw.getDoubleS(prefix + "armor.multiplier.chain.chestplate"); }
	
	public double getArmorMultiplierIB() { return fcw.getDoubleS(prefix + "armor.multiplier.iron.boots"); }
	public double getArmorMultiplierIH() { return fcw.getDoubleS(prefix + "armor.multiplier.iron.helmet"); }
	public double getArmorMultiplierIL() { return fcw.getDoubleS(prefix + "armor.multiplier.iron.leggings"); }
	public double getArmorMultiplierIC() { return fcw.getDoubleS(prefix + "armor.multiplier.iron.chestplate"); }
	
	public double getArmorMultiplierDB() { return fcw.getDoubleS(prefix + "armor.multiplier.diamond.boots"); }
	public double getArmorMultiplierDH() { return fcw.getDoubleS(prefix + "armor.multiplier.diamond.helmet"); }
	public double getArmorMultiplierDL() { return fcw.getDoubleS(prefix + "armor.multiplier.diamond.leggings"); }
	public double getArmorMultiplierDC() { return fcw.getDoubleS(prefix + "armor.multiplier.diamond.chestplate"); }
	
	public double getArmorMultiplierGB() { return fcw.getDoubleS(prefix + "armor.multiplier.gold.boots"); }
	public double getArmorMultiplierGH() { return fcw.getDoubleS(prefix + "armor.multiplier.gold.helmet"); }
	public double getArmorMultiplierGL() { return fcw.getDoubleS(prefix + "armor.multiplier.gold.leggings"); }
	public double getArmorMultiplierGC() { return fcw.getDoubleS(prefix + "armor.multiplier.gold.chestplate"); }
	
	public double getMobLootPercentStronger() { return fcw.getDoubleS(prefix + "mob.lootPercent.stronger"); }
	public double getMobLootPercentWeaker() { return fcw.getDoubleS(prefix + "mob.lootPercent.weaker"); }
	public double getMobCashMultiplier() { return fcw.getDoubleS(prefix + "mob.cashMultiplier"); }
	public double getMobExpMultiplier() { return fcw.getDoubleS(prefix + "mob.expMultiplier"); }
	public int getMobSpawnWithItemChance() { return fcw.getIntS(prefix + "mob.spawnWithItemChance"); }
	public int getMobSpawnWithEnchantsChance() { return fcw.getIntS(prefix + "mob.spawnWithEnchantsChance"); }
	public int getMobAttackMultiplier() { return fcw.getIntS(prefix + "mob.attackMultiplier"); }
	public int getMobConstitutionMultiplier() { return fcw.getIntS(prefix + "mob.constitutionMultiplier"); }
	
	public double getSwordMultiplierWood() { return fcw.getDoubleS(prefix + "sword.multiplier.wood"); }
	public double getSwordMultiplierStone() { return fcw.getDoubleS(prefix + "sword.multiplier.stone"); }
	public double getSwordMultiplierIron() { return fcw.getDoubleS(prefix + "sword.multiplier.iron"); }
	public double getSwordMultiplierDiamond() { return fcw.getDoubleS(prefix + "sword.multiplier.diamond"); }
	public double getSwordMultiplierGold() { return fcw.getDoubleS(prefix + "sword.multiplier.gold"); }

	public double getSwordAttackRequirementWood() { return fcw.getDoubleS(prefix + "sword.attackRequirement.wood"); }
	public double getSwordAttackRequirementStone() { return fcw.getDoubleS(prefix + "sword.attackRequirement.stone"); }
	public double getSwordAttackRequirementIron() { return fcw.getDoubleS(prefix + "sword.attackRequirement.iron"); }
	public double getSwordAttackRequirementDiamond() { return fcw.getDoubleS(prefix + "sword.attackRequirement.diamond"); }
	public double getSwordAttackRequirementGold() { return fcw.getDoubleS(prefix + "sword.attackRequirement.gold"); }
	
	public int getPlayerBaseHealth() { return fcw.getIntS(prefix + "player.baseHealth"); }
	public int getPlayerBaseMana() { return fcw.getIntS(prefix + "player.baseMana"); }
	public int getPlayerStatMagnitudeAttack() { return fcw.getIntS(prefix + "player.statMagnitutude.attack"); }
	public int getPlayerStatMagnitudeConstitution() { return fcw.getIntS(prefix + "player.statMagnitutude.constitution"); }
	public int getPlayerStatMagnitudeIntelligence() { return fcw.getIntS(prefix + "player.statMagnitutude.intelligence"); }
	
	public int getPlayerLevelsPerSkillPoint() { return fcw.getIntS(prefix + "player.levelsPerSkillPoint"); }
	public int getPlayerStatsPerLevel() { return fcw.getIntS(prefix + "player.statsPerLevel"); }
	public double getPlayerExpScaleRate() { return fcw.getDoubleS(prefix + "player.expScaleRate"); }
	public double getPlayerExpScaleBase() { return fcw.getDoubleS(prefix + "player.expScaleBase"); }
	
	public double getDamageExplosion() { return fcw.getDoubleS(prefix + "damage.explosion"); }
	public double getDamageFall() { return fcw.getDoubleS(prefix + "damage.fall"); }
	public double getDamageContact() { return fcw.getDoubleS(prefix + "damage.contact"); }
	public double getDamageEntityAttack() { return fcw.getDoubleS(prefix + "damage.entityAttack"); }
	public double getDamageLightning() { return fcw.getDoubleS(prefix + "damage.lightning"); }
	public double getDamageFire() { return fcw.getDoubleS(prefix + "damage.fire"); }
	public double getDamageFireTick() { return fcw.getDoubleS(prefix + "damage.firetick"); }
	public double getDamageLava() { return fcw.getDoubleS(prefix + "damage.lava"); }
	public double getDamageStarvation() { return fcw.getDoubleS(prefix + "damage.starvation"); }
	public double getDamagePoison() { return fcw.getDoubleS(prefix + "damage.poison"); }
	public double getDamageMagic() { return fcw.getDoubleS(prefix + "damage.magic"); }
	public double getDamageBlockExplosion() { return fcw.getDoubleS(prefix + "damage.blockExplosion"); }
	public double getDamageWither() { return fcw.getDoubleS(prefix + "damage.wither"); }
	
	public double getPotionMultiplierStrength() { return fcw.getDoubleS(prefix + "potionMultiplier.strength"); }
	public double getPotionMultiplierWeakness() { return fcw.getDoubleS(prefix + "potionMultiplier.weakness"); }
	public double getPotionMultiplierResistance() { return fcw.getDoubleS(prefix + "potionMultiplier.resistance"); }
	
	public double getHealPercentEating() { return fcw.getDoubleS(prefix + "healMultiplier.eating"); }
	public double getHealPercentMagic() { return fcw.getDoubleS(prefix + "healMultiplier.magic"); }
	public double getHealPercentMagicRegen() { return fcw.getDoubleS(prefix + "healMultiplier.magicRegen"); }
	public double getHealPercentSatiated() { return fcw.getDoubleS(prefix + "healMultiplier.satiated"); }
	
	public BalanceConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Balance");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		//If no config was previously created, then...
		if (getVersion() < 1.0)
		{
			//Set version
			setVersion(1.0);
			
			setDifficultyScalor(500);	//500 = roughly 20% increase from 0-100.
			setRandomMobLevelDeviation(2);
			setPowerLevelPrevention(5);
			setGlobalExpMultiplier(1);
			setWitherLevelBonus(100);
			setEnderDragonLevelBonus(50);
			setDefaultItemDrops(true);
			setArrowKnockback(false);
			setBonusPercentCap(3);
			setEnchantmentMultiplierSharpness(0.02);
			setEnchantmentMultiplierBane(0.02);
			setEnchantmentMultiplierSmite(0.02);
			setEnchantmentMultiplierPower(0.02);
			setEnchantmentMultiplierProtection(0.025);
			setEnchantmentMultiplierFire(0.025);
			setEnchantmentMultiplierExplosion(0.025);
			setEnchantmentMultiplierFall(0.025);
			setEnchantmentMultiplierProjectile(0.025);
			
			setPlayerBaseHealth(100);
			setPlayerBaseMana(20);
			setPlayerStatMagnitudeAttack(1);
			setPlayerStatMagnitudeConstitution(20);
			setPlayerStatMagnitudeIntelligence(1);	
			setPlayerLevelsPerSkillPoint(4);
			setPlayerStatsPerLevel(10);
			setPlayerExpScaleRate(3);
			setPlayerExpScaleBase(11);
			//Use magicmultiplier for spells to set damage.
			
			setMobAttackHardMultiplier(2);
			setMobConstitutionMultiplier(100);
			setMobLootPercentStronger(0.2D);
			setMobLootPercentWeaker(0.166667D);
			
			setMobSpawnWithItemChance(20);
			setMobSpawnWithEnchantsChance(40);
			setMobCashMultiplier(.8);
			setMobExpMultiplier(1);
		
			setSwordMultiplierWood(1.25);
			setSwordMultiplierStone(1.5);
			setSwordMultiplierIron(1.75);
			setSwordMultiplierDiamond(2);
			setSwordMultiplierGold(2.5);
			
			setSwordAttackRequirementWood(0);
			setSwordAttackRequirementStone(125);
			setSwordAttackRequirementIron(250);
			setSwordAttackRequirementDiamond(375);
			setSwordAttackRequirementGold(500);
			
			setArmorWearRequirementChain(125);
			setArmorWearRequirementIron(250);
			setArmorWearRequirementDiamond(375);
			setArmorWearRequirementGold(500);

			//LEATHER - 10% total, .5%, 1%, 1.5%, 2%
			setArmorMultiplierLB(.005);
			setArmorMultiplierLH(.01);
			setArmorMultiplierLL(.015);
			setArmorMultiplierLC(.02);

			//CHAIN - 10% total, 1%, 2%, 3%, 4%
			setArmorMultiplierCB(.01);
			setArmorMultiplierCH(.02);
			setArmorMultiplierCL(.03);
			setArmorMultiplierCC(.04);

			//IRON - 15% total, 1.5, 3, 4.5, 6
			setArmorMultiplierIB(.015);
			setArmorMultiplierIH(.03);
			setArmorMultiplierIL(.045);
			setArmorMultiplierIC(.06);

			//DIAMOND - 20% total, 2,4,6,8
			setArmorMultiplierDB(.02);
			setArmorMultiplierDH(.04);
			setArmorMultiplierDL(.06);
			setArmorMultiplierDC(.08);
			
			//Gold - 25% total, 2.5, 5, 7.5, 10
			setArmorMultiplierGB(.025);
			setArmorMultiplierGH(.05);
			setArmorMultiplierGL(.075);
			setArmorMultiplierGC(.1);
			
			setDamageExplosion(9);
			setDamageFall(1);
			setDamageContact(1);
			setDamageEntityAttack(3);
			setDamageLightning(50);
			setDamageFire(2);
			setDamageFireTick(2);
			setDamageLava(1);
			setDamageStarvation(.3);
			setDamagePoison(.5);
			setDamageMagic(3);
			setDamageBlockExplosion(5);
			setDamageWither(.7);
			
			setPotionMultiplierStrength(0.05);
			setPotionMultiplierWeakness(0.05);
			setPotionMultiplierResistance(0.05);
			
			setHealPercentEating(.05);
			setHealPercentMagic(.2);
			setHealPercentMagicRegen(.05);
			setHealPercentSatiated(.02);
		}
	}
}












