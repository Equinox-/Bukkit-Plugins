package essentials.commands.mapeditor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.CuboidBlock;
import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class PasteCommand extends EssentialCommand {

	public PasteCommand(Essentials plugin) {
		super(plugin);
		name = "paste";
		desc = "Pastes a cuboid stored in the memory";
		usage = "/paste";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (plugin.selector.getBlockA(player.getName()) == null
					&& plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 1 block selected to paste a cuboid!");
				return true;
			}
			if (plugin.cuboids.get(player.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have a cuboid copied in memory to paste a cuboid!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			int blocks = 0;
			int minX = a.getBlockX(), minY = a.getBlockY(), minZ =
					a.getBlockZ();
			if (b != null) {
				minX = Math.min(a.getBlockX(), b.getBlockX());
				minY = Math.min(a.getBlockY(), b.getBlockY());
				minZ = Math.min(a.getBlockZ(), b.getBlockZ());
			}
			for (CuboidBlock block : plugin.cuboids.get(player
					.getName()).data) {
				Block rep =
						player.getWorld().getBlockAt(
								minX + block.x, minY + block.y,
								minZ + block.z);
				block.saveToBlock(rep);
				blocks++;
			}
			sender.sendMessage(ChatColor.AQUA + "Pasted "
					+ blocks + " blocks!");
			return true;
		}
		return false;
	}
}
