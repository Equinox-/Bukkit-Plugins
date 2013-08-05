package essentials.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;

import essentials.database.AntiFireDatabase;

public class AntiFireListener implements Listener {
	private final AntiFireDatabase db;

	public AntiFireListener(AntiFireDatabase db) {
		this.db = db;
	}

	public void protectChunk(Location l) {
		if (!isChunkProtected(l)) {
			db.protect(l.getChunk().getX(), l.getChunk().getZ());
		}
	}

	public boolean isChunkProtected(Location l) {
		return db.isProtected(l.getChunk().getX(), l.getChunk()
				.getZ());
	}

	public void unprotectChunk(Location l) {
		db.unprotect(l.getChunk().getX(), l.getChunk().getZ());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent evt) {
		if (isChunkProtected(evt.getBlock().getLocation()))
			evt.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent evt) {
		if (isChunkProtected(evt.getBlock().getLocation()))
			evt.setCancelled(true);
	}
}
