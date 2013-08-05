package essentials.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;

public class BlockSelector implements Listener {
	final Essentials ess;
	final Material wand;
	private Map<String, Location> a = new HashMap<String, Location>(),
			b = new HashMap<String, Location>();

	public BlockSelector(Essentials ess, Material wand) {
		this.ess = ess;
		this.wand = wand;
	}

	public Location getBlockA(String player) {
		return a.get(player);
	}

	public Location getBlockB(String player) {
		return b.get(player);
	}

	public void setBlockA(String player, Location block) {
		a.put(player, block);
	}

	public void setBlockB(String player, Location block) {
		b.put(player, block);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack stack = e.getItem();
		if (e.getPlayer().isOp()) {
			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
					&& stack != null
					&& stack.getType().equals(wand)) {
				if (a.get(e.getPlayer().getName()) == null
						|| (b.get(e.getPlayer().getName()) != null && a
								.get(e.getPlayer().getName()) != null)) {
					a.put(e.getPlayer().getName(), e
							.getClickedBlock().getLocation());
					b.remove(e.getPlayer().getName());
					e.getPlayer().sendMessage(
							ChatColor.AQUA
									+ "Selected block A: "
									+ e.getClickedBlock()
											.getLocation()
											.toVector()
											.toString());
				} else if (b.get(e.getPlayer().getName()) == null) {
					b.put(e.getPlayer().getName(), e
							.getClickedBlock().getLocation());
					e.getPlayer().sendMessage(
							ChatColor.AQUA
									+ "Selected block B: "
									+ e.getClickedBlock()
											.getLocation()
											.toVector()
											.toString());
				}
			}
		}
	}
}
