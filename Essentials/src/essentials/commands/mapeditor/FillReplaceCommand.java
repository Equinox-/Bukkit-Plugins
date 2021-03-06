package essentials.commands.mapeditor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import essentials.ColoredItems;
import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class FillReplaceCommand extends EssentialCommand {

	public FillReplaceCommand(Essentials plugin) {
		super(plugin);
		name = "fillreplace";
		usage = "/fillreplace [find name|id] [replace name|id]";
		desc =
				"Fills a rectangular prisim specifed by points a & b by replacing blocks of type [find] by blocks of type [replace]";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		if (sender instanceof Player && args.length >= 2) {
			int alternatex = 1, alternatey = 1, alternatez = 1;
			for (int i = 0; i < args.length; i++) {
				String s = args[i];
				if (s.equalsIgnoreCase("alternate-x")) {
					if (i + 1 < args.length
							&& isInteger(args[i + 1])) {
						alternatex =
								Integer.valueOf(args[i + 1]);
					} else {
						alternatex = 2;
					}
				} else if (s.equalsIgnoreCase("alternate-y")) {
					if (i + 1 < args.length
							&& isInteger(args[i + 1])) {
						alternatey =
								Integer.valueOf(args[i + 1]);
					} else {
						alternatey = 2;
					}
				} else if (s.equalsIgnoreCase("alternate-z")) {
					if (i + 1 < args.length
							&& isInteger(args[i + 1])) {
						alternatez =
								Integer.valueOf(args[i + 1]);
					} else {
						alternatez = 2;
					}
				}
			}
			ItemStack find =
					Essentials.fetchStack(sender, args[0]);
			if (find == null)
				return true;
			ItemStack replace =
					Essentials.fetchStack(sender, args[1]);
			if (replace == null)
				return true;
			Player player = (Player) sender;
			if (plugin.selector.getBlockA(player.getName()) == null
					|| plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to fill a cuboid!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x +=
					alternatex) {
				for (int y =
						Math.min(a.getBlockY(), b.getBlockY()); y <= Math
						.max(a.getBlockY(), b.getBlockY()); y +=
						alternatey) {
					for (int z =
							Math.min(a.getBlockZ(),
									b.getBlockZ()); z <= Math
							.max(a.getBlockZ(), b.getBlockZ()); z +=
							alternatez) {
						Block block =
								player.getWorld().getBlockAt(x,
										y, z);
						if (block != null
								&& (block.getType() == find
										.getType() || (block
										.getType()
										.name()
										.toLowerCase()
										.contains(
												"redstone_torch") && find
										.getType()
										.name()
										.toLowerCase()
										.contains(
												"redstone_torch")))
								&& (!ColoredItems
										.isColorable(block) || (((find
										.getData() == null && block
										.getData() == 0) || (find
										.getData() != null && block
										.getData() == find
										.getData().getData()))))) {
							block.setTypeIdAndData(replace
									.getType().getId(), replace
									.getData() != null ? replace
									.getData().getData() : 0,
									Essentials
											.hasPhysics(replace
													.getType()));
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private static boolean isInteger(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
