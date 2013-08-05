package com.pi.bukkit.plugins.snowfight;

import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnHandler implements Listener {
    SnowballFightHandler fight;

    public PlayerRespawnHandler(SnowballFightHandler fight) {
	this.fight = fight;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
	if (fight.isInGame()) {
	    if (e.getPlayer() != null) {
		if (fight.isPlaying(e.getPlayer().getName())) {
		    e.getPlayer()
			    .sendMessage(
				    SnowballFight.defaultChatColor
					    + "You were removed from the game because you died :'(");
		    fight.spawnPlayerInCuboid(new Random(), e.getPlayer(),
			    fight.getArena().getWaitingArea());
		    InventoryCache cache = fight.inventoryCache.get(e
			    .getPlayer().getName().toLowerCase());
		    if (cache != null)
			cache.restoreInventory(e.getPlayer());
		}
	    }
	}
    }
}
