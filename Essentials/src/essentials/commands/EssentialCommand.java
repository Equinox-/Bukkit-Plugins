package essentials.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import essentials.Essentials;

public abstract class EssentialCommand implements
		CommandExecutor {
	protected final Essentials plugin;
	protected final Logger log;
	public String usage;
	public String desc;
	public String name;
	public String[] alias = null;
	public boolean requiresOp = false;

	public EssentialCommand(Essentials plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}

	@Override
	public final boolean onCommand(CommandSender sender,
			Command command, String label, String[] args) {
		if (canExecute(sender)) {
			if (args.length == 1
					&& args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA
						+ usage.replace("<command>", label));
				sender.sendMessage(ChatColor.AQUA
						+ desc.replace("<command>", label));
			} else if (!execute(sender, command, label, args)) {
				sender.sendMessage(ChatColor.AQUA
						+ usage.replace("<command>", label));
			}
		} else {
			sender.sendMessage(ChatColor.RED
					+ "You do not have permission to use this command!");
		}
		return true;
	}

	public boolean canExecute(CommandSender p) {
		return !requiresOp || ((Player) p).isOp()
				|| !(p instanceof Player);
	}

	public abstract boolean execute(CommandSender sender,
			Command command, String label, String[] args);
}
