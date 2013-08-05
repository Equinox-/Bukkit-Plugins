package essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.AntiFireController;
import essentials.Essentials;

public class ProtectFireCommand extends EssentialCommand {
	private final AntiFireController control;

	public ProtectFireCommand(Essentials plugin) {
		super(plugin);
		name = "protectfire";
		usage = "/<command> [state|protect|unprotect]";
		desc = "Protects your current chunk from fire.";
		this.control = plugin.antiFireController;
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length >= 1 && sender instanceof Player) {
			Location l = ((Player) sender).getLocation();
			if (args[0].equalsIgnoreCase("state")) {
				sender.sendMessage(ChatColor.AQUA
						+ "This chunk [" + l.getChunk().getX()
						+ "," + l.getChunk().getZ() + "] is "
						+ (control.isProtected(l) ? "not " : "")
						+ "protected!");
				return true;
			} else if (args[0].equalsIgnoreCase("protect")) {
				control.protect(l);
				sender.sendMessage(ChatColor.AQUA
						+ "This chunk [" + l.getChunk().getX()
						+ "," + l.getChunk().getZ()
						+ "] is now protected!");
				return true;
			} else if (args[0].equalsIgnoreCase("unprotect")) {
				control.unprotect(l);
				sender.sendMessage(ChatColor.AQUA
						+ "This chunk [" + l.getChunk().getX()
						+ "," + l.getChunk().getZ()
						+ "] is no longer protected!");
				return true;
			}
		}
		return false;
	}

}
