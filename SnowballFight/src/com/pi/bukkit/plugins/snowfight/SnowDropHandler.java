package com.pi.bukkit.plugins.snowfight;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class SnowDropHandler implements Listener {
    final SnowballFightHandler handler;

    public SnowDropHandler(SnowballFightHandler handler) {
	this.handler = handler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
	if (e.getBlock() != null
		&& handler.isInGame()
		&& handler.isPlaying(e.getPlayer().getName())) {
	    boolean hasShovel = e.getPlayer() != null
		    && e.getPlayer().getItemInHand() != null
		    && isShovel(e.getPlayer().getItemInHand().getType());
	    if (e.getBlock().getType() == Material.SNOW) {
		int count = handler.getPlugin().snowballsPerThinSnow.random()
			- (hasShovel ? 1 : 0);
		if (count > 0)
		    e.getBlock()
			    .getWorld()
			    .dropItemNaturally(e.getBlock().getLocation(),
				    new ItemStack(Material.SNOW_BALL, count));
	    }
	    if (e.getBlock().getType() == Material.SNOW_BLOCK) {
		int count = handler.getPlugin().snowballsPerBlock.random()
			- (hasShovel ? 4 : 0);
		if (count > 0)
		    e.getBlock()
			    .getWorld()
			    .dropItemNaturally(e.getBlock().getLocation(),
				    new ItemStack(Material.SNOW_BALL, count));
		if (Math.random()
			* handler.getPlugin().snowBlockRarity.random() <= 1d) {
		    e.getBlock()
			    .getWorld()
			    .dropItemNaturally(
				    e.getBlock().getLocation(),
				    new ItemStack(Material.SNOW_BLOCK,
					    (int) (Math.random()) + 1));
		}
	    }
	}
    }

    private static boolean isShovel(Material mat) {
	return mat == Material.WOOD_SPADE || mat == Material.STONE_SPADE
		|| mat == Material.IRON_SPADE || mat == Material.GOLD_SPADE
		|| mat == Material.DIAMOND_SPADE;
    }
}
