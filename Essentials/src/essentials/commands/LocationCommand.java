package essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;

public class LocationCommand extends EssentialCommand {

	public LocationCommand(Essentials plugin) {
		super(plugin);
		name = "location";
		usage = "/<command>";
		desc = "Gets your current location";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			sender.sendMessage(ChatColor.AQUA + "x="
					+ p.getLocation().getBlockX() + ", y="
					+ p.getLocation().getY() + ", z"
					+ p.getLocation().getZ());
			return true;
		} else {
			sender.sendMessage(ChatColor.AQUA
					+ "You must be a player to use the /location command!");
			return true;
		}
	}

}
