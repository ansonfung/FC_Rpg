package me.Destro168.FC_Rpg.Configs;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.FC_Suite_Shared.ConfigManagers.ConfigGod;

public class BalanceConfig extends ConfigGod
{
	public static final int passive_ScalingArrows = 1;
	public static final int passive_CounterAttack = 2;
	public static final int passive_StrongerParry = 3;
	public static final int passive_InnerFire = 4;
	public static final int passive_BattleLust = 5;
	
	
	// Gets
	
	public BalanceConfig()
	{
		super(FC_Rpg.dataFolderAbsolutePath, "Balance");
		handleUpdates();
	}
	
	public void handleUpdates()
	{
		// Handle version updates.
		if (getVersion() < 1.0)
		{
			setVersion(1.06);
			setGlobalExpMultiplier(1);
		}
		
		getDefaultItemDrops();
		getArrowKnockback();
		getSwordKnockback();
		getDifficultyScalor();
		getRandomMobLevelDeviation();
		getPowerLevelPrevention();
		getWitherLevelBonus();
		getEnderDragonLevelBonus();
		
		getEnchantmentMultiplierSharpness();
		getEnchantmentMultiplierBane();
		getEnchantmentMultiplierSmite();
		getEnchantmentMultiplierPower();
		getEnchantmentMultiplierProtection();
		getEnchantmentMultiplierFire();
		getEnchantmentMultiplierExplosion();
		getEnchantmentMultiplierFall();
		getEnchantmentMultiplierProjectile();
		
		getArmorWearRequirementChain();
		getArmorWearRequirementIron();
		getArmorWearRequirementDiamond();
		getArmorWearRequirementGold();
		
		getArmorMultiplierLB();
		getArmorMultiplierLH();
		getArmorMultiplierLL();
		getArmorMultiplierLC();
		
		getArmorMultiplierCB();
		getArmorMultiplierCH();
		getArmorMultiplierCL();
		getArmorMultiplierCC();
		
		getArmorMultiplierIB();
		getArmorMultiplierIH();
		getArmorMultiplierIL();
		getArmorMultiplierIC();
		
		getArmorMultiplierDB();
		getArmorMultiplierDH();
		getArmorMultiplierDL();
		getArmorMultiplierDC();
		
		getArmorMultiplierGB();
		getArmorMultiplierGH();
		getArmorMultiplierGL();
		getArmorMultiplierGC();
		
		getMobCashMultiplier();
		getMobExpMultiplier();
		getMobSpawnWithItemChance();
		getMobSpawnWithEnchantsChance();
		getMobAttackMultiplier();
		getMobConstitutionMultiplier();
		getMobLootList();
		
		getSwordMultiplierWood();
		getSwordMultiplierStone();
		getSwordMultiplierIron();
		getSwordMultiplierDiamond();
		getSwordMultiplierGold();

		getSwordAttackRequirementWood();
		getSwordAttackRequirementStone();
		getSwordAttackRequirementIron();
		getSwordAttackRequirementDiamond();
		getSwordAttackRequirementGold();
		
		getPlayerBaseHealth();
		getPlayerBaseMana();
		getPlayerStatMagnitudeAttack();
		getPlayerStatMagnitudeConstitution();
		getPlayerStatMagnitudeIntelligence();
		getPlayerLevelsPerSkillPoint();
		getPlayerStatsPerLevel();
		getPlayerExpScaleRate();
		getPlayerExpScaleBase();
		
		getDamageExplosion();
		getDamageFall();
		getDamageContact();
		getDamageEntityAttack();
		getDamageLightning();
		getDamageFire();
		getDamageFireTick();
		getDamageLava();
		getDamageStarvation();
		getDamagePoison();
		getDamageMagic();
		getDamageBlockExplosion();
		getDamageWither();
		
		getPotionMultiplierStrength();
		getPotionMultiplierWeakness();
		getPotionMultiplierResistance();
		
		getHealPercentEating();
		getHealPercentMagic();
		getHealPercentMagicRegen();
		getHealPercentSatiated();
		
		getPassivesScalingArrow();
		getPassivesCounterAttack();
		getPassivesStrongerParry();
		getPassivesBattleLust();
		getPassivesInnerFlame();
	}
	
	/****************************************************************
	 ^ Configuration Accessing Methods 
	 - All Dyanmically Accessed
	****************************************************************/
	
	public void setGlobalExpMultiplier(int x) { fcw.set(prefix + "globalExpMultiplier", x); }
	public int getGlobalExpMultiplier() { return fcw.getInt(prefix + "globalExpMultiplier"); }
	
	/****************************************************************
	 - All Statically Accessed
	****************************************************************/
	
	public boolean getDefaultItemDrops() { return fcw.getStaticBoolean(prefix + "defaultItemDrops", true); }
	public boolean getArrowKnockback() { return fcw.getStaticBoolean(prefix + "arrowKnockback", false); }
	public boolean getSwordKnockback() { return fcw.getStaticBoolean(prefix + "swordKnockback", false); }
	public int getDifficultyScalor() { return fcw.getStaticInt(prefix + "difficultyScalor", 100); } //500 = roughly 20% increase from 0-100.
	public int getRandomMobLevelDeviation() { return fcw.getStaticInt(prefix + "randomMobLevelDeviation", 2); }
	public int getPowerLevelPrevention() { return fcw.getStaticInt(prefix + "powerLevelPrevention", 5); }
	public int getWitherLevelBonus() { return fcw.getStaticInt(prefix + "witherLevelBonus", 10); }
	public int getEnderDragonLevelBonus() { return fcw.getStaticInt(prefix + "enderDragonLevelBonus", 100); }
	
	public double getEnchantmentMultiplierSharpness() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.sharpness", 0.02); }
	public double getEnchantmentMultiplierBane() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.bane", 0.02); }
	public double getEnchantmentMultiplierSmite() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.smite", 0.02); }
	public double getEnchantmentMultiplierPower() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.power", 0.02); }
	public double getEnchantmentMultiplierProtection() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.protection", 0.025); }
	public double getEnchantmentMultiplierFire() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.fire", 0.025); }
	public double getEnchantmentMultiplierExplosion() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.explosion", 0.025); }
	public double getEnchantmentMultiplierFall() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.fall", 0.025); }
	public double getEnchantmentMultiplierProjectile() { return fcw.getStaticDouble(prefix + "enchantmentMultiplier.projectile", 0.025); }
	
	public int getArmorWearRequirementChain() { return fcw.getStaticInt(prefix + "armor.wearRequirement.chain", 60); }
	public int getArmorWearRequirementIron() { return fcw.getStaticInt(prefix + "armor.wearRequirement.iron", 120); }
	public int getArmorWearRequirementDiamond() { return fcw.getStaticInt(prefix + "armor.wearRequirement.diamond", 180); }
	public int getArmorWearRequirementGold() { return fcw.getStaticInt(prefix + "armor.wearRequirement.gold", 240); }
	
	public double getArmorMultiplierLB() { return fcw.getStaticDouble(prefix + "armor.multiplier.leather.boots", .005); }	//LEATHER - 10% total, .5%, 1%, 1.5%, 2%
	public double getArmorMultiplierLH() { return fcw.getStaticDouble(prefix + "armor.multiplier.leather.helmet", .01); }
	public double getArmorMultiplierLL() { return fcw.getStaticDouble(prefix + "armor.multiplier.leather.leggings", .015); }
	public double getArmorMultiplierLC() { return fcw.getStaticDouble(prefix + "armor.multiplier.leather.chestplate", .02); }
	
	public double getArmorMultiplierCB() { return fcw.getStaticDouble(prefix + "armor.multiplier.chain.boots", .01); }	//CHAIN - 10% total, 1%, 2%, 3%, 4%
	public double getArmorMultiplierCH() { return fcw.getStaticDouble(prefix + "armor.multiplier.chain.helmet", .02); }
	public double getArmorMultiplierCL() { return fcw.getStaticDouble(prefix + "armor.multiplier.chain.leggings", .03); }
	public double getArmorMultiplierCC() { return fcw.getStaticDouble(prefix + "armor.multiplier.chain.chestplate", .04); }
	
	public double getArmorMultiplierIB() { return fcw.getStaticDouble(prefix + "armor.multiplier.iron.boots", .015); }	//IRON - 15% total, 1.5, 3, 4.5, 6
	public double getArmorMultiplierIH() { return fcw.getStaticDouble(prefix + "armor.multiplier.iron.helmet", .03); }
	public double getArmorMultiplierIL() { return fcw.getStaticDouble(prefix + "armor.multiplier.iron.leggings", .045); }
	public double getArmorMultiplierIC() { return fcw.getStaticDouble(prefix + "armor.multiplier.iron.chestplate", .06); }
	
	public double getArmorMultiplierDB() { return fcw.getStaticDouble(prefix + "armor.multiplier.diamond.boots", .02); }	//DIAMOND - 20% total, 2,4,6,8
	public double getArmorMultiplierDH() { return fcw.getStaticDouble(prefix + "armor.multiplier.diamond.helmet", .04); }
	public double getArmorMultiplierDL() { return fcw.getStaticDouble(prefix + "armor.multiplier.diamond.leggings", .06); }
	public double getArmorMultiplierDC() { return fcw.getStaticDouble(prefix + "armor.multiplier.diamond.chestplate", .08); }
	
	public double getArmorMultiplierGB() { return fcw.getStaticDouble(prefix + "armor.multiplier.gold.boots", .025); }	//Gold - 25% total, 2.5, 5, 7.5, 10
	public double getArmorMultiplierGH() { return fcw.getStaticDouble(prefix + "armor.multiplier.gold.helmet", .05); }
	public double getArmorMultiplierGL() { return fcw.getStaticDouble(prefix + "armor.multiplier.gold.leggings", .075); }
	public double getArmorMultiplierGC() { return fcw.getStaticDouble(prefix + "armor.multiplier.gold.chestplate", .1); }
	
	public double getMobCashMultiplier() { return fcw.getStaticDouble(prefix + "mob.cashMultiplier", .8); }
	public double getMobExpMultiplier() { return fcw.getStaticDouble(prefix + "mob.expMultiplier", .1); }
	public int getMobSpawnWithItemChance() { return fcw.getStaticInt(prefix + "mob.spawnWithItemChance", 50); }
	public int getMobSpawnWithEnchantsChance() { return fcw.getStaticInt(prefix + "mob.spawnWithEnchantsChance", 50); }
	public int getMobAttackMultiplier() { return fcw.getStaticInt(prefix + "mob.attackMultiplier", 5); }
	public int getMobConstitutionMultiplier() { return fcw.getStaticInt(prefix + "mob.constitutionMultiplier", 100); }
	public String getMobLootList() { return fcw.getStaticString(prefix + "mob.lootList", "default"); }
	
	public double getSwordMultiplierWood() { return fcw.getStaticDouble(prefix + "sword.multiplier.wood", 1.25); }
	public double getSwordMultiplierStone() { return fcw.getStaticDouble(prefix + "sword.multiplier.stone", 1.5); }
	public double getSwordMultiplierIron() { return fcw.getStaticDouble(prefix + "sword.multiplier.iron", 1.75); }
	public double getSwordMultiplierDiamond() { return fcw.getStaticDouble(prefix + "sword.multiplier.diamond", 2); }
	public double getSwordMultiplierGold() { return fcw.getStaticDouble(prefix + "sword.multiplier.gold", 2.5); }
	
	public double getSwordAttackRequirementWood() { return fcw.getStaticDouble(prefix + "sword.attackRequirement.wood", 0); }
	public double getSwordAttackRequirementStone() { return fcw.getStaticDouble(prefix + "sword.attackRequirement.stone", 60); }
	public double getSwordAttackRequirementIron() { return fcw.getStaticDouble(prefix + "sword.attackRequirement.iron", 120); }
	public double getSwordAttackRequirementDiamond() { return fcw.getStaticDouble(prefix + "sword.attackRequirement.diamond", 180); }
	public double getSwordAttackRequirementGold() { return fcw.getStaticDouble(prefix + "sword.attackRequirement.gold", 240); }
	
	public int getPlayerBaseHealth() { return fcw.getStaticInt(prefix + "player.baseHealth", 100); }
	public int getPlayerBaseMana() { return fcw.getStaticInt(prefix + "player.baseMana", 20); }
	public int getPlayerStatMagnitudeAttack() { return fcw.getStaticInt(prefix + "player.statMagnitutude.attack", 1); }
	public int getPlayerStatMagnitudeConstitution() { return fcw.getStaticInt(prefix + "player.statMagnitutude.constitution", 20); }
	public int getPlayerStatMagnitudeIntelligence() { return fcw.getStaticInt(prefix + "player.statMagnitutude.intelligence", 1); }
	public int getPlayerLevelsPerSkillPoint() { return fcw.getStaticInt(prefix + "player.levelsPerSkillPoint", 4); }
	public int getPlayerStatsPerLevel() { return fcw.getStaticInt(prefix + "player.statsPerLevel", 10); }
	public double getPlayerExpScaleRate() { return fcw.getStaticDouble(prefix + "player.expScaleRate", 3); }
	public double getPlayerExpScaleBase() { return fcw.getStaticDouble(prefix + "player.expScaleBase", 11); }
	
	public double getDamageExplosion() { return fcw.getStaticDouble(prefix + "damage.explosion", 9); }
	public double getDamageFall() { return fcw.getStaticDouble(prefix + "damage.fall", 1); }
	public double getDamageContact() { return fcw.getStaticDouble(prefix + "damage.contact", 1); }
	public double getDamageEntityAttack() { return fcw.getStaticDouble(prefix + "damage.entityAttack", 3); }
	public double getDamageLightning() { return fcw.getStaticDouble(prefix + "damage.lightning", 50); }
	public double getDamageFire() { return fcw.getStaticDouble(prefix + "damage.fire", 2); }
	public double getDamageFireTick() { return fcw.getStaticDouble(prefix + "damage.firetick", 2); }
	public double getDamageLava() { return fcw.getStaticDouble(prefix + "damage.lava", 1); }
	public double getDamageStarvation() { return fcw.getStaticDouble(prefix + "damage.starvation", .3); }
	public double getDamagePoison() { return fcw.getStaticDouble(prefix + "damage.poison", .5); }
	public double getDamageMagic() { return fcw.getStaticDouble(prefix + "damage.magic", 3); }
	public double getDamageBlockExplosion() { return fcw.getStaticDouble(prefix + "damage.blockExplosion", 5); }
	public double getDamageWither() { return fcw.getStaticDouble(prefix + "damage.wither", .7); }
	
	public double getPotionMultiplierStrength() { return fcw.getStaticDouble(prefix + "potionMultiplier.strength", 0.05); }
	public double getPotionMultiplierWeakness() { return fcw.getStaticDouble(prefix + "potionMultiplier.weakness", 0.05); }
	public double getPotionMultiplierResistance() { return fcw.getStaticDouble(prefix + "potionMultiplier.resistance", 0.05); }
	
	public double getHealPercentEating() { return fcw.getStaticDouble(prefix + "healMultiplier.eating", .05); }
	public double getHealPercentMagic() { return fcw.getStaticDouble(prefix + "healMultiplier.magic", .2); }
	public double getHealPercentMagicRegen() { return fcw.getStaticDouble(prefix + "healMultiplier.magicRegen", .05); }
	public double getHealPercentSatiated() { return fcw.getStaticDouble(prefix + "healMultiplier.satiated", .05); }
	
	public int getPassivesScalingArrow() { return fcw.getStaticInt(prefix + "passives.scalingArrows", 28); }
	public int getPassivesCounterAttack() { return fcw.getStaticInt(prefix + "passives.counterAttack", 10); }
	public double getPassivesStrongerParry() { return fcw.getStaticDouble(prefix + "passives.strongerParry", .75); }
	public double getPassivesBattleLust() { return fcw.getStaticDouble(prefix + "passives.battleLust", .4); }
	public double getPassivesInnerFlame() { return fcw.getStaticDouble(prefix + "passives.innerFlame", .15); }
}












