package me.Destro168.Util;

public class SpellUtil
{
	public final static int TOTAL_SPELL_COUNT = 5;
	public final static int CLASS_SPELL_COUNT = 5;
	public final static int CLASS_COUNT = 5;
	public final static int TIER_COUNT = 5;
	
	private Spell[][] spells;
	
	public class Spell
	{
		public String name;
		public String description;
		public double[] magnitude;
		public int[] manaCost;
		
		public Spell(String spellName_)
		{
			name = spellName_;
			
			magnitude = new double[TIER_COUNT];
			manaCost = new int[TIER_COUNT];
		}
		
		public void setSpellInformationByTier(int x, double magnitude_,  int manaCost_)
		{
			magnitude[x] = magnitude_;
			manaCost[x] = manaCost_;
		}
		
		public void setSpellDescription(String description_)
		{
			description = description_;
		}
	}
	
	public SpellUtil()
	{
		//Set spells to a spell array.
		spells = new Spell[CLASS_COUNT][];
		
		for (int i = 0; i < TIER_COUNT; i++)
			spells[i] = new Spell[TIER_COUNT];
		
		//Initialize various things.
		init_Swordsman_Spells();
		init_Assassin_Spells();
		init_Defender_Spells();
		init_Wizard_Skills();
		init_Berserker_Skills();
	}
	
	public String getSpellName(int combatClass, int spellNumber)
	{
		return spells[combatClass][spellNumber].name;
	}
	
	public String getSpellDescription(int combatClass, int spellNumber)
	{
		return spells[combatClass][spellNumber].description;
	}
	
	public double getSpellMagnitude(int classNumber, int spellNumber, int spellTier)
	{
		return spells[classNumber][spellNumber].magnitude[spellTier];
	}
	
	public int getSpellManaCost(int classNumber, int spellNumber, int spellTier)
	{
		return spells[classNumber][spellNumber].manaCost[spellTier];
	}
	
	private void init_Swordsman_Spells()
	{
		spells[0][0] = new Spell("Nimble");
		spells[0][0].setSpellDescription("Grants you (x)% passive dodge chance for 10 seconds.");
		spells[0][0].setSpellInformationByTier(0,3,10);
		spells[0][0].setSpellInformationByTier(1,6,20);
		spells[0][0].setSpellInformationByTier(2,9,30);
		spells[0][0].setSpellInformationByTier(3,12,40);
		spells[0][0].setSpellInformationByTier(4,15,50);
		
		spells[0][1] = new Spell("Morale");
		spells[0][1].setSpellDescription("Apply a (x)% damage boost to your entire party for 10 seconds.");
		spells[0][1].setSpellInformationByTier(0,1.02,10);
		spells[0][1].setSpellInformationByTier(1,1.04,20);
		spells[0][1].setSpellInformationByTier(2,1.06,30);
		spells[0][1].setSpellInformationByTier(3,1.08,40);
		spells[0][1].setSpellInformationByTier(4,1.1,50);
		
		spells[0][2] = new Spell("Empower");
		spells[0][2].setSpellDescription("Empowers your next attack to deal (x) bonus damage.");
		spells[0][2].setSpellInformationByTier(0,.2,2);
		spells[0][2].setSpellInformationByTier(1,.4,4);
		spells[0][2].setSpellInformationByTier(2,.6,6);
		spells[0][2].setSpellInformationByTier(3,.8,8);
		spells[0][2].setSpellInformationByTier(4,1,10);
		
		spells[0][3] = new Spell("Slice");
		spells[0][3].setSpellDescription("Deal 2% of targets' hp each second for (x) seconds.");
		spells[0][3].setSpellInformationByTier(0,2,2);
		spells[0][3].setSpellInformationByTier(1,4,4);
		spells[0][3].setSpellInformationByTier(2,6,6);
		spells[0][3].setSpellInformationByTier(3,8,8);
		spells[0][3].setSpellInformationByTier(4,10,10);
		
		spells[0][4] = new Spell("Vortex");
		spells[0][4].setSpellDescription("Strike all monsters in a (x) block AoE.");
		spells[0][4].setSpellInformationByTier(0,3,2);
		spells[0][4].setSpellInformationByTier(1,6,4);
		spells[0][4].setSpellInformationByTier(2,9,6);
		spells[0][4].setSpellInformationByTier(3,12,8);
		spells[0][4].setSpellInformationByTier(4,15,10);
	}
	
	private void init_Assassin_Spells()
	{
		spells[1][0] = new Spell("Adreneline");
		spells[1][0].setSpellDescription("Applies (x) speed buff on character for 10 seconds.");
		spells[1][0].setSpellInformationByTier(0,1,10);
		spells[1][0].setSpellInformationByTier(1,2,20);
		spells[1][0].setSpellInformationByTier(2,3,30);
		spells[1][0].setSpellInformationByTier(3,4,40);
		spells[1][0].setSpellInformationByTier(4,5,50);
		
		spells[1][1] = new Spell("FlameArrow");
		spells[1][1].setSpellDescription("Gives your arrows a burning effect for (x) shots. Fire insta-kills mobs but does not " +
				"give loot/exp/items from them or kill bosses.");
		spells[1][1].setSpellInformationByTier(0,1,2);
		spells[1][1].setSpellInformationByTier(1,2,4);
		spells[1][1].setSpellInformationByTier(2,3,6);
		spells[1][1].setSpellInformationByTier(3,4,8);
		spells[1][1].setSpellInformationByTier(4,5,10);
		
		spells[1][2] = new Spell("ForceArrow");
		spells[1][2].setSpellDescription("Strengthens your next arrow to deal (x)% bonus damage.");
		spells[1][2].setSpellInformationByTier(0,.2,2);
		spells[1][2].setSpellInformationByTier(1,.4,4);
		spells[1][2].setSpellInformationByTier(2,.6,6);
		spells[1][2].setSpellInformationByTier(3,.8,8);
		spells[1][2].setSpellInformationByTier(4,1,10);
		
		spells[1][3] = new Spell("PoisonArrow");
		spells[1][3].setSpellDescription("Your next arrow applies a 5 second long, 1 tick a second poison that does (x) damage.");
		spells[1][3].setSpellInformationByTier(0,.24,3);
		spells[1][3].setSpellInformationByTier(1,.48,6);
		spells[1][3].setSpellInformationByTier(2,.72,9);
		spells[1][3].setSpellInformationByTier(3,.96,12);
		spells[1][3].setSpellInformationByTier(4,1.2,15);
		
		spells[1][4] = new Spell("Assassinate");
		spells[1][4].setSpellDescription("Multiplies the damage of your next attack by (x) if it hits a target from behind.");
		spells[1][4].setSpellInformationByTier(0,1.6,5);
		spells[1][4].setSpellInformationByTier(1,3.2,10);
		spells[1][4].setSpellInformationByTier(2,4.8,15);
		spells[1][4].setSpellInformationByTier(3,6.2,20);
		spells[1][4].setSpellInformationByTier(4,8.0,25);
	}
	
	private void init_Defender_Spells()
	{
		spells[2][0] = new Spell("Taunt");
		spells[2][0].setSpellDescription("Teleport and set aggro of all monsters in 12 block radius to you. Increases party defense by (x)% for 10 seconds.");
		spells[2][0].setSpellInformationByTier(0,2,10);
		spells[2][0].setSpellInformationByTier(1,4,20);
		spells[2][0].setSpellInformationByTier(2,6,30);
		spells[2][0].setSpellInformationByTier(3,8,40);
		spells[2][0].setSpellInformationByTier(4,10,50);
		
		spells[2][1] = new Spell("Thorns");
		spells[2][1].setSpellDescription("All attacks against you return (x)% of damage. Lasts for 10 seconds.");
		spells[2][1].setSpellInformationByTier(0,3,10);
		spells[2][1].setSpellInformationByTier(1,6,20);
		spells[2][1].setSpellInformationByTier(2,9,30);
		spells[2][1].setSpellInformationByTier(3,12,40);
		spells[2][1].setSpellInformationByTier(4,15,50);
		
		spells[2][2] = new Spell("Undefeated");
		spells[2][2].setSpellDescription("Heal (x)% of your max health.");
		spells[2][2].setSpellInformationByTier(0,.1,15);
		spells[2][2].setSpellInformationByTier(1,.2,30);
		spells[2][2].setSpellInformationByTier(2,.3,45);
		spells[2][2].setSpellInformationByTier(3,.4,60);
		spells[2][2].setSpellInformationByTier(4,.5,75);
		
		spells[2][3] = new Spell("Weaken");
		spells[2][3].setSpellDescription("Permanently lower all mob stats by (x)%.");
		spells[2][3].setSpellInformationByTier(0,.03,3);
		spells[2][3].setSpellInformationByTier(1,.06,6);
		spells[2][3].setSpellInformationByTier(2,.09,9);
		spells[2][3].setSpellInformationByTier(3,.12,12);
		spells[2][3].setSpellInformationByTier(4,.15,15);
		
		spells[2][4] = new Spell("Bash");
		spells[2][4].setSpellDescription("Disables your targets ability to attack for (x) seconds.");
		spells[2][4].setSpellInformationByTier(0,.5,3);
		spells[2][4].setSpellInformationByTier(1,1,6);
		spells[2][4].setSpellInformationByTier(2,1.5,9);
		spells[2][4].setSpellInformationByTier(3,2,12);
		spells[2][4].setSpellInformationByTier(4,2.5,15);
	}
	
	private void init_Wizard_Skills()
	{
		spells[3][0] = new Spell("Fireball");
		spells[3][0].setSpellDescription("Release a fireball exploding an area for (x) damage.");
		spells[3][0].setSpellInformationByTier(0,.3333,3);
		spells[3][0].setSpellInformationByTier(1,.4166,6);
		spells[3][0].setSpellInformationByTier(2,.5,9);
		spells[3][0].setSpellInformationByTier(3,.5833,12);
		spells[3][0].setSpellInformationByTier(4,.6666,15);
		
		spells[3][1] = new Spell("Alchemy");
		spells[3][1].setSpellDescription("Destroy enchanted items for items. Read wiki for info.");
		spells[3][1].setSpellInformationByTier(0,1,10);
		spells[3][1].setSpellInformationByTier(1,1.25,20);
		spells[3][1].setSpellInformationByTier(2,1.50,30);
		spells[3][1].setSpellInformationByTier(3,1.75,40);
		spells[3][1].setSpellInformationByTier(4,2,50);
		
		spells[3][2] = new Spell("Lightning");
		spells[3][2].setSpellDescription("Strike a target with lightning for (x) damage.");
		spells[3][2].setSpellInformationByTier(0,1,2);
		spells[3][2].setSpellInformationByTier(1,1.25,4);
		spells[3][2].setSpellInformationByTier(2,1.5,6);
		spells[3][2].setSpellInformationByTier(3,1.75,8);
		spells[3][2].setSpellInformationByTier(4,2.0,10);
		
		spells[3][3] = new Spell("Remedy");
		spells[3][3].setSpellDescription("Heal an ally for (x) health.");
		spells[3][3].setSpellInformationByTier(0,.03,4);
		spells[3][3].setSpellInformationByTier(1,.06,8);
		spells[3][3].setSpellInformationByTier(2,.09,12);
		spells[3][3].setSpellInformationByTier(3,.12,16);
		spells[3][3].setSpellInformationByTier(4,.15,20);
		
		spells[3][4] = new Spell("Invigorate");
		spells[3][4].setSpellDescription("Buff an allies stats by (x)% for 10 seconds.");
		spells[3][4].setSpellInformationByTier(0,.03,6);
		spells[3][4].setSpellInformationByTier(1,.06,12);
		spells[3][4].setSpellInformationByTier(2,.09,18);
		spells[3][4].setSpellInformationByTier(3,.12,24);
		spells[3][4].setSpellInformationByTier(4,.15,30);
	}
	
	private void init_Berserker_Skills()
	{
		spells[4][0] = new Spell("Bloodthirst");
		spells[4][0].setSpellDescription("Steal (x)% life from all damage for 10 seconds.");
		
		spells[4][0].setSpellInformationByTier(0,3,10);
		spells[4][0].setSpellInformationByTier(0,6,20);
		spells[4][0].setSpellInformationByTier(0,9,30);
		spells[4][0].setSpellInformationByTier(0,12,40);
		spells[4][0].setSpellInformationByTier(0,15,50);
		
		spells[4][1] = new Spell("Undying");
		spells[4][1].setSpellDescription("Become immune to death for (x) seconds.");
		
		spells[4][1].setSpellInformationByTier(0,3,10);
		spells[4][1].setSpellInformationByTier(0,3.5,15);
		spells[4][1].setSpellInformationByTier(0,4,20);
		spells[4][1].setSpellInformationByTier(0,4.5,25);
		spells[4][1].setSpellInformationByTier(0,5,30);
		
		spells[4][2] = new Spell("Ferocity");
		spells[4][2].setSpellDescription("Imbue your normal strength with magic to attack with such strength that you teleport behind targets for (x) seconds.");
		
		spells[4][2].setSpellInformationByTier(0,3,10);
		spells[4][2].setSpellInformationByTier(0,3.5,20);
		spells[4][2].setSpellInformationByTier(0,4,30);
		spells[4][2].setSpellInformationByTier(0,4.5,40);
		spells[4][2].setSpellInformationByTier(0,5,50);
		
		spells[4][3] = new Spell("Inequality");
		spells[4][3].setSpellDescription("Deal missing health as (x)% instant bonus damage.");	//Every 1% = 4% bonus damage. ~ 99% missing = 396% bonus damage.
		
		spells[4][3].setSpellInformationByTier(0,1,2);
		spells[4][3].setSpellInformationByTier(0,1.75,4);
		spells[4][3].setSpellInformationByTier(0,2.5,6);
		spells[4][3].setSpellInformationByTier(0,3.25,8);
		spells[4][3].setSpellInformationByTier(0,4,10);
		
		spells[4][4] = new Spell("Reckless");
		spells[4][4].setSpellDescription("Sacrifice 20% of your current hp for (x)% instant bonus damage.");
		
		spells[4][4].setSpellInformationByTier(0,1.6,1);
		spells[4][4].setSpellInformationByTier(0,3.2,2);
		spells[4][4].setSpellInformationByTier(0,4.8,3);
		spells[4][4].setSpellInformationByTier(0,6.4,4);
		spells[4][4].setSpellInformationByTier(0,8.0,5);
	}
}


















