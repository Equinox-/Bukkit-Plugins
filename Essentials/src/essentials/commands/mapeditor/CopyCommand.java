package essentials.commands.mapeditor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.*;
import essentials.commands.EssentialCommand;

public class CopyCommand extends EssentialCommand {

	public CopyCommand(Essentials plugin) {
		super(plugin);
		name = "copy";
		desc = "Copys a cuboid to memory";
		usage = "/copy";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (plugin.selector.getBlockA(player.getName()) == null
					|| plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to copy a cuboid!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			int minX = a.getBlockX(), minY = a.getBlockY(), minZ =
					a.getBlockZ();
			if (b != null) {
				minX = Math.min(a.getBlockX(), b.getBlockX());
				minY = Math.min(a.getBlockY(), b.getBlockY());
				minZ = Math.min(a.getBlockZ(), b.getBlockZ());
			}
			plugin.cuboids.put(player.getName(), new Cuboid());
			int blocks = 0;
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
						CuboidBlock cB = new CuboidBlock(block);
						cB.translateOrigin(minX, minY, minZ);
						plugin.cuboids.get(player.getName())
								.addBlock(cB);
						blocks++;
					}
				}
			}
			sender.sendMessage(ChatColor.AQUA + "Copied "
					+ blocks + " blocks!");
			return true;
		}
		return false;
	}
}
