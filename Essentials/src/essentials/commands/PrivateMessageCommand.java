package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;

public class PrivateMessageCommand extends EssentialCommand {

	public PrivateMessageCommand(Essentials plugin) {
		super(plugin);
		desc = "Sends a private message to a player";
		usage = "/<command> [player] [message]";
		name = "pm";
		alias = new String[] { "msg", "tell", "message" };
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length < 2)
			return false;
		String message = "";
		for (int i = 1; i < args.length; i++)
			message += args[i] + " ";
		Player to = plugin.matchPlayer(args[0]);
		if (to == null) {
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
						+ bestMat.getName().toLowerCase() + "?");
			}
			return true;
		} else {
			String from =
					(sender instanceof Player) ? ((Player) sender)
							.getDisplayName() : "Console";
			String sent =
					ChatColor.WHITE + "<" + from + "> "
							+ ChatColor.RED + "[PM] "
							+ ChatColor.WHITE + message;
			sender.sendMessage(sent);
			to.sendMessage(sent);
			return true;
		}
	}
}
