package essentials.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;

public class TimeCommand extends EssentialCommand {
	private static final Map<String[], Long> timeMap =
			new HashMap<String[], Long>();
	static {
		timeMap.put(new String[] { "night", "midnight" },
				new Long(18000));
		timeMap.put(new String[] { "day", "noon", "midday" },
				new Long(6000));
	}

	public static long lookupTime(String time)
			throws NumberFormatException {
		for (String[] keys : timeMap.keySet())
			for (String s : keys)
				if (time.equalsIgnoreCase(s))
					return timeMap.get(keys);
		return Long.valueOf(time);
	}

	public TimeCommand(Essentials plugin) {
		super(plugin);
		name = "time";
		desc = "Sets the time";
		usage =
				"/<command> [day|night|time] /<command> unlock /<command> [day|night|time] lock";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length > 0 && sender instanceof Player) {
			try {
				if (args[0].equalsIgnoreCase("unlock")) {
					plugin.timeLockController
							.unlock(((Player) sender).getWorld()
									.getName());
					sender.sendMessage(ChatColor.AQUA
							+ "Unlocked time");
				} else {
					long time = lookupTime(args[0]);
					((Player) sender).getWorld().setTime(time);
					if (args.length == 2
							&& args[1].equalsIgnoreCase("lock")) {
						plugin.timeLockController.lock(
								((Player) sender).getWorld()
										.getName(), time);
						sender.sendMessage(ChatColor.AQUA
								+ "Locked time at: " + time);
					} else {
						sender.sendMessage(ChatColor.AQUA
								+ "Set time to: " + time);
					}
				}
				return true;
			} catch (NumberFormatException e) {
				sender.sendMessage("\"" + args[0]
						+ "\" is not a valid number!");
				return false;
			}
		}
		return false;
	}
}
