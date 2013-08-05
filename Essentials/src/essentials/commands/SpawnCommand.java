package essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;

public class SpawnCommand extends EssentialCommand {

	public SpawnCommand(Essentials plugin) {
		super(plugin);
		name = "spawn";
		desc = "Sets the spawn and warps to spawn";
		usage = "/<command>  /<command> set";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			boolean setCommand =
					args.length > 0
							&& args[0].equalsIgnoreCase("set");
			if (setCommand) {
				if (!(sender instanceof Player)
						|| ((Player) sender).isOp()) {
					Location l = ((Player) sender).getLocation();
					if (((Player) sender)
							.getWorld()
							.setSpawnLocation(l.getBlockX(),
									l.getBlockY(), l.getBlockZ())) {
						sender.sendMessage(ChatColor.AQUA
								+ "Set respawn location!  ("
								+ l.getBlockX() + ","
								+ l.getBlockY() + ","
								+ l.getBlockZ() + ")");
					}
				} else {
					sender.sendMessage(ChatColor.AQUA
							+ "You must be an op to change the spawn");
				}
			} else {
				if (((Player) sender).teleport(((Player) sender)
						.getWorld().getSpawnLocation())) {
					sender.sendMessage(ChatColor.AQUA
							+ "Respawned!");
				}
			}
			return true;
		}
		return false;
	}
}
