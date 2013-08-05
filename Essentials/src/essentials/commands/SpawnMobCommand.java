package essentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftSheep;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

import essentials.Essentials;
import essentials.StringCompare;

public class SpawnMobCommand extends EssentialCommand {

	public SpawnMobCommand(Essentials plugin) {
		super(plugin);
		desc = "Spawns a mob";
		usage = "/<command> [mob name] [extra]";
		name = "mob";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length >= 1) {
				String mobname = args[0];
				if (mobname.equalsIgnoreCase("list")) {
					List<String> list = new ArrayList<String>();
					for (EntityType m : EntityType.values()) {
						list.add(m.getName().toLowerCase());
					}
					sender.sendMessage(ChatColor.AQUA
							+ "Mob options: "
							+ formatList(list
									.toArray(new String[list
											.size()])));
					return true;
				}
				EntityType mob = null;
				for (EntityType m : EntityType.values())
					if (m.name().equalsIgnoreCase(mobname)) {
						mob = m;
						break;
					}
				if (mob == null) {
					sender.sendMessage(ChatColor.AQUA
							+ "Unable to find creature: "
							+ args[0]);
					ArrayList<String> pairs1 =
							StringCompare
									.wordLetterPairs(mobname);
					double best = 0;
					String bestMob = "";
					for (EntityType m : EntityType.values()) {
						double v =
								StringCompare.compareStrings(
										pairs1, m.name());
						if (v > best && v > 0.5d) {
							bestMob = m.getName();
							best = v;
						}
					}
					if (bestMob.length() > 0) {
						sender.sendMessage(ChatColor.AQUA
								+ "Did you mean \""
								+ bestMob.toLowerCase()
										.replaceAll("_", " ")
								+ "\"?");
					}
					return true;
				} else {
					Entity cE =
							((Player) sender)
									.getWorld()
									.spawnCreature(
											((Player) sender)
													.getLocation(),
											mob);
					if (args.length >= 2) {
						if (cE instanceof CraftVillager) {
							try {
								Profession p =
										Profession
												.valueOf(args[1]
														.toUpperCase());
								((CraftVillager) cE)
										.setProfession(p);
							} catch (IllegalArgumentException e) {
								List<String> list =
										new ArrayList<String>();
								for (Profession m : Profession
										.values()) {
									list.add(m.name()
											.toLowerCase());
								}
								sender.sendMessage(ChatColor.AQUA
										+ "Profession options: "
										+ formatList(list
												.toArray(new String[list
														.size()])));
							}
						} else if (cE instanceof CraftSheep) {
							try {
								DyeColor d =
										DyeColor.valueOf(args[1]
												.toUpperCase());
								((CraftSheep) cE).setColor(d);
							} catch (IllegalArgumentException e) {
								List<String> list =
										new ArrayList<String>();
								for (DyeColor m : DyeColor
										.values()) {
									list.add(m.name()
											.toLowerCase());
								}
								sender.sendMessage(ChatColor.AQUA
										+ "Color options: "
										+ formatList(list
												.toArray(new String[list
														.size()])));
							}
						}
						return true;
					}
				}
			}
		} else {
			sender.sendMessage("This command cannot be used from the console!");
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
