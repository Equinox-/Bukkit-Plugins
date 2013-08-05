package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;
import essentials.database.DBEntry;
import essentials.database.WarpEntry;

public class WarpCommand extends EssentialCommand {
	public WarpCommand(Essentials plugin) {
		super(plugin);
		name = "warp";
		usage =
				"/<command> [warp name]  /<command> [set|remove] [warp name] /<command> list";
		desc = "Warps a player to a warp location";
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		try {
			if (args.length == 1) {
				String warpName = args[0];
				if (warpName.equalsIgnoreCase("list")) {
					if (!(sender instanceof Player)
							|| ((Player) sender).isOp()) {
						String[] warps =
								plugin.warpDatabase.getWarps();
						sender.sendMessage(ChatColor.AQUA
								+ "There "
								+ (warps.length == 1 ? "is "
										: "are ")
								+ (warps.length > 0 ? warps.length
										: "no") + " warp"
								+ (warps.length == 1 ? "" : "s")
								+ " registered.");
						if (warps.length > 0) {
							sender.sendMessage(ChatColor.AQUA
									+ formatList(warps));
						}
					} else {
						sender.sendMessage(ChatColor.AQUA
								+ "You must be an op to list the registered warps");
					}
					return true;
				}
				WarpEntry warp =
						plugin.warpDatabase.getWarp(warpName);
				if (warp == null) {
					sender.sendMessage(ChatColor.AQUA
							+ "Unable to find warp: " + args[0]);
					ArrayList<String> pairs1 =
							StringCompare
									.wordLetterPairs(args[0]
											.toUpperCase());
					double best = 0;
					WarpEntry bestWarp = null;
					for (DBEntry m : plugin.warpDatabase.data
							.values()) {
						double v =
								StringCompare.compareStrings(
										pairs1, ((WarpEntry) m)
												.getName());
						if (v > best && v > 0.5d) {
							bestWarp = (WarpEntry) m;
							best = v;
						}
					}
					if (bestWarp != null) {
						sender.sendMessage(ChatColor.AQUA
								+ "Did you mean "
								+ bestWarp.getName()
										.toLowerCase() + "?");
					}
					return true;
				} else if (sender instanceof Player) {
					((Player) sender).teleport(warp
							.getLocation());
					sender.sendMessage(ChatColor.AQUA
							+ "Warped to " + warpName + "!");
					return true;
				} else {
					log.info(ChatColor.AQUA
							+ "This command can only be run by a player!");
					return true;
				}
			} else if (args.length == 2) {
				String command = args[0];
				String warpName = args[1];
				if (command.equalsIgnoreCase("set")
						|| command.equalsIgnoreCase("remove")
						|| command.equalsIgnoreCase("rm")) {
					WarpEntry warp =
							plugin.warpDatabase
									.getWarp(warpName);
					if (command.equalsIgnoreCase("remove")
							|| command.equalsIgnoreCase("rm")) {
						if (warp == null) {
							sender.sendMessage(ChatColor.AQUA
									+ "Unable to find warp: "
									+ args[0]);
							ArrayList<String> pairs1 =
									StringCompare
											.wordLetterPairs(args[0]
													.toUpperCase());
							double best = 0;
							WarpEntry bestWarp = null;
							for (DBEntry m : plugin.warpDatabase.data
									.values()) {
								double v =
										StringCompare
												.compareStrings(
														pairs1,
														((WarpEntry) m)
																.getName());
								if (v > best && v > 0.5d) {
									bestWarp = (WarpEntry) m;
									best = v;
								}
							}
							if (bestWarp != null) {
								sender.sendMessage(ChatColor.AQUA
										+ "Did you mean "
										+ bestWarp.getName()
												.toLowerCase()
										+ "?");
							}
							return true;
						}
						plugin.warpDatabase.removeWarp(warpName);
						return true;
					} else if (sender instanceof Player) {
						WarpEntry w = new WarpEntry();
						w.setName(warpName);
						Location l =
								((Player) sender).getLocation();
						w.setLocation(l);
						plugin.warpDatabase.setWarp(w);
						sender.sendMessage(ChatColor.AQUA
								+ "Set warp " + warpName
								+ " location!  ("
								+ l.getBlockX() + ","
								+ l.getBlockY() + ","
								+ l.getBlockZ() + ")");
						return true;
					} else {
						log.info(ChatColor.AQUA
								+ "You can only set a warp as a player!");
						return true;
					}
				}
			}
		} catch (Exception e) {
			sender.sendMessage(e.toString());
			for (StackTraceElement ele : e.getStackTrace())
				sender.sendMessage(ele.toString());
			return false;
		}
		return false;
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
