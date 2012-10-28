package me.Destro168.Listeners;

import java.util.Random;

import me.Destro168.FC_Rpg.FC_Rpg;
import me.Destro168.Util.FC_RpgPermissions;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

//Listen for block breaks.
public class BlockBreakListener implements Listener
{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		//Variable Declarations
		Player player = event.getPlayer();
		ItemStack handItem = player.getItemInHand();
		Material handItemType = handItem.getType();
		FC_RpgPermissions perms = new FC_RpgPermissions(player);
		String worldName = event.getBlock().getWorld().getName();
		Block block = event.getBlock();
		
		//Prevent breaking of sponge blocks.
		if (block.getType() == Material.SPONGE)
		{
			if (FC_Rpg.generalConfig.getPreventSpongeBreak() == true)
			{
				if (!perms.isAdmin())
					event.setCancelled(true);
			}
		}
		
		//Handle aoe world modifier for worlds.
		if (FC_Rpg.worldConfig.getIsAoEWorld(worldName))
		{
			World world = block.getWorld();
			boolean breakSuccess = false;
			
			if (handItemType == Material.STONE_PICKAXE)
				breakSuccess = true;
			else if (handItemType == Material.IRON_PICKAXE)
				breakSuccess = true;
			else if (handItemType == Material.GOLD_PICKAXE)
				breakSuccess = true;
			else if (handItemType == Material.DIAMOND_PICKAXE)
				breakSuccess = true;
			
			if (breakSuccess == false)
				return;
			
			for (int x = block.getLocation().getBlockX() - 1; x < block.getLocation().getBlockX() + 2; x++)
			{
				for (int y = block.getLocation().getBlockY() - 1; y < block.getLocation().getBlockY() + 2; y++)
				{
					for (int z = block.getLocation().getBlockZ() - 1; z < block.getLocation().getBlockZ() + 2; z++)
					{
						if (world.getBlockAt(x,y,z).getType() != Material.BEDROCK)
							world.getBlockAt(x,y,z).breakNaturally();
					}
				}
			}
		}
		
		//If not an rpg world, don't continue execution.
		if (!FC_Rpg.worldConfig.getIsRpgWorld(worldName))
			return;
		
		//If exp drops are globally cancelled.
		if (FC_Rpg.generalConfig.getExpCancelled() == true)
			event.setExpToDrop(0);	//Cancel experience drops.
		
		//Handle perfect wheat if enabled.
		if (FC_Rpg.generalConfig.getPerfectWheat() == true)
		{
			if (block.getType().equals(Material.CROPS))
			{
				//Variable Declarations
				Random rand = new Random();
				ItemStack wheat = new ItemStack(Material.WHEAT, rand.nextInt(2) + 1);
				ItemStack seed = new ItemStack(Material.SEEDS, rand.nextInt(2));
				
				//If wheat is fully grown.
				if (block.getData() == 7)
				{
					//Drop the wheat and seeds.
					block.getWorld().dropItemNaturally(block.getLocation(), wheat);
					block.getWorld().dropItemNaturally(block.getLocation(), seed);
					
					//Set the data back to 0.
					block.setData((byte) 0);
				}
				
				if (!perms.isAdmin())
					event.setCancelled(true);
			}
		}
		
		//If infinite gold is enabled
		if (FC_Rpg.generalConfig.getInfiniteGold() == true)
		{
			//Prevent gold tools from breaking.
			if (handItemType == Material.GOLD_PICKAXE && handItem.getDurability() == 29)
				handItem.setDurability((short) 0);
			
			else if (handItemType == Material.GOLD_SPADE && handItem.getDurability() == 29)
				handItem.setDurability((short) 0);
			
			else if (handItemType == Material.GOLD_AXE && handItem.getDurability() == 29)
				handItem.setDurability((short) 0);
		}
		
		//If infinite diamond is enabled
		if (FC_Rpg.generalConfig.getInfiniteDiamond() == true)
		{
			//Prevent diamond tools from breaking.
			if (handItemType == Material.DIAMOND_PICKAXE && handItem.getDurability() == 1558)
				handItem.setDurability((short) 0);
			
			else if (handItemType == Material.DIAMOND_SPADE && handItem.getDurability() == 1558)
				handItem.setDurability((short) 0);
			
			else if (handItemType == Material.DIAMOND_AXE && handItem.getDurability() == 1558)
				handItem.setDurability((short) 0);
		}
	}
}
