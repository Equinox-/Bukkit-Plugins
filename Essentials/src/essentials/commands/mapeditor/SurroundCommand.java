package essentials.commands.mapeditor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class SurroundCommand extends EssentialCommand {

	public SurroundCommand(Essentials plugin) {
		super(plugin);
		name = "surround";
		usage = "/surround [blockname|id]";
		desc =
				"Surrounds a rectangular prisim specifed by points a & b";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		if (sender instanceof Player && args.length >= 1) {
			ItemStack stack =
					Essentials.fetchStack(sender, args[0]);
			if (stack == null)
				return true;
			Player player = (Player) sender;

			if (plugin.selector.getBlockA(player.getName()) == null
					|| plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to surround a cuboid!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x++) {
				for (int y =
						Math.min(a.getBlockY(), b.getBlockY()); y <= Math
						.max(a.getBlockY(), b.getBlockY()); y++) {
					Block block =
							player.getWorld().getBlockAt(x, y,
									a.getBlockZ());
					block.setTypeIdAndData(stack.getType()
							.getId(),
							stack.getData() != null ? stack
									.getData().getData() : 0,
							Essentials.hasPhysics(stack
									.getType()));
					block =
							player.getWorld().getBlockAt(x, y,
									b.getBlockZ());
					block.setTypeIdAndData(stack.getType()
							.getId(),
							stack.getData() != null ? stack
									.getData().getData() : 0,
							Essentials.hasPhysics(stack
									.getType()));
				}
			}
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x++) {
				for (int z =
						Math.min(a.getBlockZ(), b.getBlockZ()); z <= Math
						.max(a.getBlockZ(), b.getBlockZ()); z++) {
					Block block =
							player.getWorld().getBlockAt(x,
									a.getBlockY(), z);
					block.setTypeIdAndData(stack.getType()
							.getId(),
							stack.getData() != null ? stack
									.getData().getData() : 0,
							Essentials.hasPhysics(stack
									.getType()));
					block =
							player.getWorld().getBlockAt(x,
									b.getBlockY(), z);
					block.setTypeIdAndData(stack.getType()
							.getId(),
							stack.getData() != null ? stack
									.getData().getData() : 0,
							Essentials.hasPhysics(stack
									.getType()));
				}
			}
			for (int z = Math.min(a.getBlockZ(), b.getBlockZ()); z <= Math
					.max(a.getBlockZ(), b.getBlockZ()); z++) {
				for (int y =
						Math.min(a.getBlockY(), b.getBlockY()); y <= Math
						.max(a.getBlockY(), b.getBlockY()); y++) {
					Block block =
							player.getWorld().getBlockAt(
									a.getBlockX(), y, z);
					block.setTypeIdAndData(stack.getType()
							.getId(),
							stack.getData() != null ? stack
									.getData().getData() : 0,
							Essentials.hasPhysics(stack
									.getType()));
					block =
							player.getWorld().getBlockAt(
									b.getBlockX(), y, z);
					block.setTypeIdAndData(stack.getType()
							.getId(),
							stack.getData() != null ? stack
									.getData().getData() : 0,
							Essentials.hasPhysics(stack
									.getType()));
				}
			}
			return true;
		}
		return false;
	}
}
