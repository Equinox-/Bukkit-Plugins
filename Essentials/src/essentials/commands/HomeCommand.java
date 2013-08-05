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
import essentials.database.HomeEntry;

public class HomeCommand extends EssentialCommand {
	public HomeCommand(Essentials plugin) {
		super(plugin);
		name = "home";
		usage =
				"/<command>  /<command> [set|remove] /<command> [player] /<command> [invite|uninvite] [player]";
		desc =
				"Warps a player to their home or another person's home";
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		try {
			if (sender instanceof Player) {
				if (args.length == 0) {
					HomeEntry e =
							plugin.homeDatabase
									.getHome(((Player) sender)
											.getName());
					if (e != null) {
						((Player) sender).teleport(e
								.getLocation());
						sender.sendMessage(ChatColor.AQUA
								+ "Welcome to your home!");
						return true;
					} else {
						sender.sendMessage(ChatColor.AQUA
								+ "You don't have a house!");
						return true;
					}
				} else if (args.length == 1) {
					String command = args[0];
					if (command.equalsIgnoreCase("remove")
							|| command.equalsIgnoreCase("rm")) {
						if (plugin.homeDatabase
								.removeHome(((Player) sender)
										.getName())) {
							sender.sendMessage(ChatColor.AQUA
									+ "Home removed.");
						} else {
							sender.sendMessage(ChatColor.AQUA
									+ "You don't have a home!");
						}
						return true;
					} else if (command.equalsIgnoreCase("set")) {
						HomeEntry curr =
								plugin.homeDatabase
										.getHome(((Player) sender)
												.getName());
						HomeEntry w = new HomeEntry();
						w.setOwner((Player) sender);
						if (curr != null)
							w.setInvitesArray(curr
									.getInvitesArray());
						Location l =
								((Player) sender).getLocation();
						w.setLocation(l);
						plugin.homeDatabase.setHome(w);
						sender.sendMessage(ChatColor.AQUA
								+ "Set home location!  ("
								+ l.getBlockX() + ","
								+ l.getBlockY() + ","
								+ l.getBlockZ() + ")");
						return true;
					} else {
						HomeEntry home =
								plugin.homeDatabase
										.getHome(command);
						if (home == null) {
							sender.sendMessage(ChatColor.AQUA
									+ "Unable to find home of: "
									+ args[0]);
							ArrayList<String> pairs1 =
									StringCompare
											.wordLetterPairs(command
													.toUpperCase());
							double best = 0;
							HomeEntry bestHome = null;
							for (DBEntry m : plugin.homeDatabase.data
									.values()) {
								double v =
										StringCompare
												.compareStrings(
														pairs1,
														((HomeEntry) m)
																.getOwner());
								if (v > best && v > 0.5d) {
									bestHome = (HomeEntry) m;
									best = v;
								}
							}
							if (bestHome != null) {
								sender.sendMessage(ChatColor.AQUA
										+ "Did you mean "
										+ bestHome.getOwner()
												.toLowerCase()
										+ "?");
							}
							return true;
						} else {
							if (home.isInvited(((Player) sender)
									.getName())
									|| home.getOwner().equals(
											command)) {
								((Player) sender).teleport(home
										.getLocation());
								sender.sendMessage(ChatColor.AQUA
										+ "Welcome to "
										+ (home.getOwner()
												.equals(command) ? "your"
												: (command + "'s"))
										+ " home!");
								return true;
							} else {
								sender.sendMessage(ChatColor.AQUA
										+ "You are not invited to "
										+ (home.getOwner()
												.equals(command) ? "your"
												: (command + "'s"))
										+ " home!");
								return true;
							}
						}
					}
				} else if (args.length == 2) {
					String command = args[0];
					HomeEntry home =
							plugin.homeDatabase
									.getHome(((Player) sender)
											.getName());
					if (home != null) {
						if (command.equalsIgnoreCase("invite")) {
							home.invite(args[1]);
							plugin.homeDatabase.setHome(home);
							return true;
						} else if (command
								.equalsIgnoreCase("uninvite")) {
							home.revokeInvite(args[1]);
							plugin.homeDatabase.setHome(home);
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.AQUA
								+ "You do not have a home!");
						return true;
					}
				}
			} else {
				log.info(ChatColor.AQUA
						+ "You must be a player to use the /home command!");
				return true;
			}
		} catch (Exception e) {
			sender.sendMessage(e.toString());
			for (StackTraceElement ele : e.getStackTrace())
				sender.sendMessage(ele.toString());
			return false;
		}
		return false;
	}
}
