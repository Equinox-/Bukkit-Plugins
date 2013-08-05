package essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;

public class RepeatCommand extends EssentialCommand {

	public RepeatCommand(Essentials plugin) {
		super(plugin);
		name = "repeat";
		alias = new String[] { "r" };
		usage = "/<command>";
		desc = "Repeats the last command";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (plugin.lastUsedCommand != null) {
				Player p = (Player) sender;
				String last =
						plugin.lastUsedCommand
								.getLastUsedCommand(p
										.getDisplayName());
				if (last != null) {
					p.performCommand(last.substring(1));
				} else {
					sender.sendMessage(ChatColor.AQUA
							+ "You don't have a last used command!");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA
						+ "This server doesn't have repeat commands enabled!");
			}
		} else {
			sender.sendMessage(ChatColor.AQUA
					+ "You  must be a player to use this command!");
			return true;
		}
		return true;
	}
}
