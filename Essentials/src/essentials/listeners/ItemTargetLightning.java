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

public class ItemTargetLightning implements Listener {
	final Essentials ess;
	final Material electrumWand;

	public ItemTargetLightning(Essentials ess,
			Material electrumWand) {
		this.ess = ess;
		this.electrumWand = electrumWand;
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
					&& stack.getType().equals(electrumWand)) {
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
				if (b != null) {
					strike(p, b.getLocation());
				}
			}
		}
	}

	private void strike(Player p, Location point) {
		int x = point.getBlockX();
		int z = point.getBlockZ();
		int y = p.getWorld().getHighestBlockYAt(x, z);
		p.getWorld().strikeLightning(
				new Location(p.getWorld(), x, y, z));
	}
}
