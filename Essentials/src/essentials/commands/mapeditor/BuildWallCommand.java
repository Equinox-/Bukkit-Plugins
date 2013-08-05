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

public class BuildWallCommand extends EssentialCommand {

	public BuildWallCommand(Essentials plugin) {
		super(plugin);
		name = "buildwall";
		usage = "/buildwall [blockname|id] [height]";
		desc =
				"Increases the height of all walls one or more blocks high of the specified material";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		if (sender instanceof Player && args.length >= 2) {
			ItemStack find =
					Essentials.fetchStack(sender, args[0]);
			if (find == null)
				return true;
			int height = 1;
			try {
				height = Integer.valueOf(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.AQUA + "\""
						+ args[1] + "\" is not a valid number!");
				return false;
			}
			Player player = (Player) sender;

			if (plugin.selector.getBlockA(player.getName()) == null
					|| plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to build walls!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			sender.sendMessage(ChatColor.AQUA
					+ "Building a wall " + height
					+ " blocks high of type "
					+ args[0].replaceAll("_", " ").toLowerCase());
			int y = Math.min(a.getBlockY(), b.getBlockY());
			boolean increase = false;
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x++) {
				for (int z =
						Math.min(a.getBlockZ(), b.getBlockZ()); z <= Math
						.max(a.getBlockZ(), b.getBlockZ()); z++) {
					Block block =
							player.getWorld()
									.getBlockAt(x, y, z);
					if (block != null
							&& block.getType().equals(
									find.getType())) {
						for (int y2 = y; y2 <= y + height; y2++) {
							Block rep =
									player.getWorld()
											.getBlockAt(x, y2, z);
							rep.setTypeIdAndData(
									find.getType().getId(),
									find.getData() != null ? find
											.getData().getData()
											: 0, Essentials
											.hasPhysics(find
													.getType()));
							increase = true;
						}
					}
				}
			}
			if (increase) {
				int newY = y + height;
				if (height > 0) {
					if (b.getBlockY() > a.getBlockY()) {
						b.setY(newY);
						plugin.selector.setBlockB(
								player.getName(), b);
					} else {
						a.setY(newY);
						plugin.selector.setBlockB(
								player.getName(), a);
					}
				} else {
					if (b.getBlockY() < a.getBlockY()) {
						b.setY(newY);
						plugin.selector.setBlockB(
								player.getName(), b);
					} else {
						a.setY(newY);
						plugin.selector.setBlockB(
								player.getName(), a);
					}
				}
			}
			return true;
		}
		return false;
	}
}
