package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;

public class TPHereCommand extends EssentialCommand {

	public TPHereCommand(Essentials plugin) {
		super(plugin);
		name = "tphere";
		desc = "Teleports a player to your location";
		usage = "/<command> [player]";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length != 1)
			return false;
		if (sender instanceof Player) {
			Player other = plugin.matchPlayer(args[0]);
			if (other == null) {
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
			} else {
				if (other.teleport(((Player) sender)
						.getLocation())) {
					other.sendMessage(ChatColor.AQUA
							+ "Teleported to "
							+ ((Player) sender).getDisplayName());
					sender.sendMessage(ChatColor.AQUA
							+ "Teleported "
							+ other.getDisplayName()
							+ " to you!");
				} else {
					sender.sendMessage(ChatColor.AQUA
							+ "Failed to teleport "
							+ other.getDisplayName()
							+ " to you!");
				}
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.AQUA
					+ "You cannot use the command in the console!");
			return true;
		}
	}

}
