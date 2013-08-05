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

public class LineCommand extends EssentialCommand {

	public LineCommand(Essentials plugin) {
		super(plugin);
		name = "line";
		usage = "/line [blockname|id] [alternate]";
		desc = "Draws a line between point a & b";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		if (sender instanceof Player && args.length >= 1) {
			int alternate = 1;
			for (int i = 0; i < args.length; i++) {
				String s = args[i];
				if (s.equalsIgnoreCase("alternate")) {
					if (i + 1 < args.length
							&& isInteger(args[i + 1])) {
						alternate = Integer.valueOf(args[i + 1]);
					} else {
						alternate = 2;
					}
				}
			}
			try {
				ItemStack stack =
						Essentials.fetchStack(sender, args[0]);
				if (stack == null)
					return true;
				Player player = (Player) sender;

				if (plugin.selector.getBlockA(player.getName()) == null
						|| plugin.selector.getBlockB(player
								.getName()) == null) {
					player.sendMessage(ChatColor.AQUA
							+ "You must have at least 2 blocks selected to draw a line!");
					return true;
				}
				Location a =
						plugin.selector.getBlockA(player
								.getName());
				Location b =
						plugin.selector.getBlockB(player
								.getName());
				int xD = a.getBlockX() - b.getBlockX();
				int yD = a.getBlockY() - b.getBlockY();
				int zD = a.getBlockZ() - b.getBlockZ();
				double dist =
						Math.max(
								Math.abs(xD),
								Math.max(Math.abs(yD),
										Math.abs(xD)));
				if (dist != 0) {
					double xE = xD / dist, yE = yD / dist, zE =
							zD / dist;
					for (double i = 0; i <= dist; i += alternate) {
						Block block =
								player.getWorld()
										.getBlockAt(
												b.getBlockX()
														+ (int) Math
																.round(xE
																		* i),
												b.getBlockY()
														+ (int) Math
																.round(yE
																		* i),
												b.getBlockZ()
														+ (int) Math
																.round(zE
																		* i));
						block.setTypeIdAndData(
								stack.getType().getId(),
								stack.getData() != null ? stack
										.getData().getData() : 0,
								Essentials.hasPhysics(stack
										.getType()));
					}
				} else {
					player.getWorld()
							.getBlockAt(a)
							.setTypeIdAndData(
									stack.getType().getId(),
									stack.getData() != null ? stack
											.getData().getData()
											: 0,
									Essentials.hasPhysics(stack
											.getType()));
				}
				return true;
			} catch (Exception e) {
				log.info(e.toString());
				for (StackTraceElement ele : e.getStackTrace())
					log.info(ele.toString());
				return false;
			}
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
