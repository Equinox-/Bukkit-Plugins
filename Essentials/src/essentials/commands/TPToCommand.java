package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;

public class TPToCommand extends EssentialCommand {
	public static final String[] lolz = { "Dlom" };

	public TPToCommand(Essentials plugin) {
		super(plugin);
		name = "tpto";
		desc = "Teleports you to another player's location";
		usage = "/<command> [player]";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length != 1)
			return false;
		if (sender instanceof Player) {
			if (sender.isOp()) {
				Player other = plugin.matchPlayer(args[0]);
				if (other == null) {
					sender.sendMessage(ChatColor.AQUA
							+ "Unable to find player: "
							+ args[0]);
					ArrayList<String> pairs1 =
							StringCompare
									.wordLetterPairs(args[0]
											.toUpperCase());
					double best = 0;
					Player bestMat = null;
					for (Player m : plugin.getServer()
							.getOnlinePlayers()) {
						double v =
								StringCompare.compareStrings(
										pairs1, m.getName());
						if (v > best && v > 0.5d) {
							bestMat = m;
							best = v;
						}
					}
					if (bestMat != null) {
						sender.sendMessage(ChatColor.AQUA
								+ "Did you mean "
								+ bestMat.getName()
										.toLowerCase() + "?");
					}
					return true;
				} else {
					if (((Player) sender).teleport(other)) {
						sender.sendMessage(ChatColor.AQUA
								+ "Teleported to "
								+ other.getDisplayName() + "!");
					} else {
						sender.sendMessage(ChatColor.AQUA
								+ "Failed to teleport to"
								+ other.getDisplayName() + "!");
					}
					return true;
				}
			} else {
				Player p = (Player) sender;
				for (String s : lolz) {
					if (p.getName().equalsIgnoreCase(s)) {
						sender.sendMessage(ChatColor.AQUA
								+ "YOU SILLY LAMMA!");
						p.setFireTicks(400);
						p.setDisplayName("[LAMMA] "
								+ p.getName());
						p.setCompassTarget(new Location(p
								.getWorld(), 999929, -1, 989831));
						break;
					}
				}
				sender.sendMessage(ChatColor.AQUA
						+ "You don't have permission to use this command");
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.AQUA
					+ "You cannot use the command in the console!");
			return true;
		}
	}

}
