package essentials.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;

public class InstaPickBlockRemoval implements Listener {
	final Essentials ess;
	final Material instaPick;

	public InstaPickBlockRemoval(Essentials ess,
			Material instaPick) {
		this.ess = ess;
		this.instaPick = instaPick;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack stack = e.getItem();
		if (e.getPlayer().isOp()) {
			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
					&& stack != null
					&& stack.getType().equals(instaPick)) {
				try {
					Block b = e.getClickedBlock();
					Inventory inv = null;
					if (b.getState() != null
							&& b.getState() instanceof ContainerBlock) {
						inv =
								((ContainerBlock) b.getState())
										.getInventory();
					}
					ItemStack dropStack =
							Essentials.getStackFor(b);
					if (dropStack != null) {
						b.getWorld().dropItemNaturally(
								b.getLocation(), dropStack);
					}
					if (inv != null) {
						for (ItemStack itm : inv.getContents())
							if (itm != null
									&& !itm.getType().equals(
											Material.AIR))
								b.getWorld().dropItemNaturally(
										b.getLocation(), itm);
					}
					if (b.getState() != null
							&& b.getState() instanceof CreatureSpawner) {
						((CraftWorld) b.getWorld())
								.getHandle()
								.setTileEntity(b.getX(),
										b.getY(), b.getZ(), null);
					}
					((CraftWorld) b.getWorld()).getHandle().o(
							b.getX(), b.getY(), b.getZ());
					b.setType(Material.AIR);
					((CraftWorld) b.getWorld()).getHandle().manager
							.flagDirty(b.getX(), b.getY(),
									b.getZ());
				} catch (NullPointerException ignore) {
					e.getPlayer()
							.sendMessage(
									ChatColor.AQUA
											+ "You cannot insta remove this block!");
				}
			}
		}
	}
}
