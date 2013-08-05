package essentials.listeners;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import essentials.Essentials;

public class ItemTeleportInstant implements Listener {
	final Essentials ess;
	final Material teleportationTool;

	public ItemTeleportInstant(Essentials ess,
			Material teleportationTool) {
		this.ess = ess;
		this.teleportationTool = teleportationTool;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack stack = e.getItem();
		Player p = e.getPlayer();
		if (p.isOp()) {
			if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e
					.getAction()
					.equals(Action.RIGHT_CLICK_BLOCK))
					&& stack != null
					&& stack.getType().equals(teleportationTool)) {
				int maxDistance = 140;
				Block b = null;
				Iterator<Block> itr =
						new BlockIterator(p, maxDistance);
				while (itr.hasNext()) {
					b = itr.next();
					int id = b.getTypeId();
					if (Essentials.isSolid(id)) {
						break;
					}
				}
				if (b != null && b.getTypeId() != 0) {
					int x = b.getX();
					int z = b.getZ();
					for (int y = b.getY(); y < 125; y++) {
						Block prev =
								p.getWorld().getBlockAt(x,
										y - 1, z);
						Block idThis =
								p.getWorld().getBlockAt(x, y, z);
						Block idNext =
								p.getWorld().getBlockAt(x,
										y + 1, z);
						if (idNext != null
								&& idThis != null
								&& prev != null
								&& !Essentials.isSolid(idThis
										.getTypeId())
								&& !Essentials.isSolid(idNext
										.getTypeId())
								&& Essentials.isSolid(prev
										.getTypeId())) {
							p.teleport(new Location(
									p.getWorld(), x, y, z, p
											.getLocation()
											.getYaw(), p
											.getLocation()
											.getPitch()));
							return;
						}
					}
					int y =
							p.getWorld()
									.getHighestBlockYAt(x, z);
					p.teleport(new Location(p.getWorld(), x, y,
							z, p.getLocation().getYaw(), p
									.getLocation().getPitch()));
				}
			}
		}
	}
}
