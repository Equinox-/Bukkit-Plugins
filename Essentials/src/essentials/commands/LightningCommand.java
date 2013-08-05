package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;

public class LightningCommand extends EssentialCommand {

	public LightningCommand(Essentials plugin) {
		super(plugin);
		desc =
				"Strikes the block you're pointing at with lightning";
		name = "lightning";
		usage = "/<command> /<command> [player]";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				Block point =
						((Player) sender).getTargetBlock(null,
								50);
				if (point != null) {
					int z = point.getZ();
					int x = point.getX();
					((Player) sender)
							.getWorld()
							.strikeLightning(
									new Location(
											((Player) sender)
													.getWorld(),
											x,
											((Player) sender)
													.getWorld()
													.getHighestBlockYAt(
															x, z),
											z));
					return true;
				}
			}
		} else if (args.length == 1) {
			Player p = plugin.matchPlayer(args[0]);
			if (p == null) {
				sender.sendMessage(ChatColor.AQUA
						+ "Unable to find player: " + args[0]);
				ArrayList<String> pairs1 =
						StringCompare.wordLetterPairs(args[0]
								.toUpperCase());
				double best = 0;
				Player bestMat = null;
				for (Player m : plugin.getServer()
						.getOnlinePlayers()) {
					double v =
							StringCompare.compareStrings(pairs1,
									m.getName());
					if (v > best && v > 0.5d) {
						bestMat = m;
						best = v;
					}
				}
				if (bestMat != null) {
					sender.sendMessage(ChatColor.AQUA
							+ "Did you mean "
							+ bestMat.getName().toLowerCase()
							+ "?");
				}
				return true;
			}
			Location point = p.getLocation();
			int x = point.getBlockX();
			int z = point.getBlockZ();
			p.getWorld().strikeLightning(
					new Location(p.getWorld(), x, p.getWorld()
							.getHighestBlockYAt(x, z), z));
			return true;
		}
		return false;
	}

}
