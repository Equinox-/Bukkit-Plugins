package essentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;

public class OnlineCommand extends EssentialCommand {

	public OnlineCommand(Essentials plugin) {
		super(plugin);
		name = "who";
		desc = "List online players";
		usage = "/who  /who ops";
		alias =
				new String[] { "list", "players", "listplayers",
						"online" };
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		List<String> users = new ArrayList<String>();
		boolean ops = false;
		if (args.length >= 1
				&& args[0].toLowerCase().contains("op")) {
			for (Player p : plugin.getServer()
					.getOnlinePlayers())
				if (p.isOp())
					users.add(p.getDisplayName());
			ops = true;
		} else {
			for (Player p : plugin.getServer()
					.getOnlinePlayers())
				users.add(p.getDisplayName());
		}

		sender.sendMessage(ChatColor.AQUA + "There "
				+ (users.size() == 1 ? "is " : "are ")
				+ (users.size() > 0 ? users.size() : "no") + " "
				+ (ops ? "op" : "player")
				+ (users.size() == 1 ? "" : "s") + " online.");
		if (users.size() > 0) {
			sender.sendMessage(ChatColor.AQUA
					+ formatList(users.toArray(new String[users
							.size()])));
		}
		return true;
	}

	private static String formatList(String[] list) {
		String s = "";
		if (list != null)
			if (list.length == 2) {
				s = list[0] + " and " + list[1];
			} else if (list.length > 2) {
				for (int i = 0; i < list.length - 1; i++)
					s += list[i] + ", ";
				s += "and " + list[list.length - 1];
			} else if (list.length == 1)
				s = list[0];
		return s;
	}
}
