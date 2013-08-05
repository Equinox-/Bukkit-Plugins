package essentials.commands.mapeditor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class CoatCommand extends EssentialCommand {

	public CoatCommand(Essentials plugin) {
		super(plugin);
		name = "coat";
		usage = "/coat [blockname|id] <number>";
		desc =
				" Coats a cuboid with the specified items, with a optional height.";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		if (sender instanceof Player && args.length >= 1) {
			ItemStack find =
					Essentials.fetchStack(sender, args[0]);
			if (find == null)
				return true;
			int height = 1;
			if (args.length >= 2) {
				try {
					height = Integer.valueOf(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.AQUA + "\""
							+ args[1]
							+ "\" is not a valid number!");
					return false;
				}
			}
			Player player = (Player) sender;

			if (plugin.selector.getBlockA(player.getName()) == null
					|| plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to coat a cuboid!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			sender.sendMessage(ChatColor.AQUA
					+ "Coating this cuboid " + height
					+ " blocks high of type "
					+ args[0].replaceAll("_", " ").toLowerCase());
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x++) {
				for (int z =
						Math.min(a.getBlockZ(), b.getBlockZ()); z <= Math
						.max(a.getBlockZ(), b.getBlockZ()); z++) {
					boolean lastWasAir = false;
					for (int y =
							Math.max(a.getBlockY(),
									b.getBlockY()); y >= Math
							.min(a.getBlockY(), b.getBlockY()); y--) {
						Block rep =
								player.getWorld().getBlockAt(x,
										y, z);
						if (lastWasAir
								&& Essentials.isSolid(rep
										.getTypeId())) {
							for (int y2 = y + 1; y2 < y + 1
									+ height; y2++) {
								Block app =
										player.getWorld()
												.getBlockAt(x,
														y2, z);
								if (app != null) {
									app.setTypeIdAndData(
											find.getTypeId(),
											find.getData() != null ? find
													.getData()
													.getData()
													: 0, false);
								}
							}
							break;
						}
						lastWasAir =
								rep.getType() == Material.AIR;
					}
				}
			}
			return true;
		}
		return false;
	}
}
