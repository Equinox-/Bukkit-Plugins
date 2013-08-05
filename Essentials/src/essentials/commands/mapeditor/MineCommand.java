package essentials.commands.mapeditor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class MineCommand extends EssentialCommand {

	public MineCommand(Essentials plugin) {
		super(plugin);
		name = "mine";
		usage = "/mine";
		desc =
				"Clears a rectangular prisim specifed by points a & b, dropping the blocks";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			if (a == null || b == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to mine a cuboid!");
				return true;
			}
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x++) {
				for (int y =
						Math.min(a.getBlockY(), b.getBlockY()); y <= Math
						.max(a.getBlockY(), b.getBlockY()); y++) {
					for (int z =
							Math.min(a.getBlockZ(),
									b.getBlockZ()); z <= Math
							.max(a.getBlockZ(), b.getBlockZ()); z++) {
						Block block =
								player.getWorld().getBlockAt(x,
										y, z);
						Inventory inv = null;
						if (block.getState() != null
								&& block.getState() instanceof InventoryHolder) {
							inv =
									((InventoryHolder) block
											.getState())
											.getInventory();
						}
						ItemStack dropStack =
								Essentials.getStackFor(block);
						if (dropStack != null) {
							player.getWorld().dropItemNaturally(
									block.getLocation(),
									dropStack);
						}
						if (inv != null) {
							for (ItemStack itm : inv
									.getContents())
								if (itm != null
										&& !itm.getType()
												.equals(Material.AIR))
									player.getWorld()
											.dropItemNaturally(
													block.getLocation(),
													itm);
						}
						block.setTypeId(0);
					}
				}
			}
			return true;
		}
		return false;
	}

}
