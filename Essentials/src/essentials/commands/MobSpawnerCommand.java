package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;

public class MobSpawnerCommand extends EssentialCommand {

	public MobSpawnerCommand(Essentials plugin) {
		super(plugin);
		desc = "Sets the type of the mobspawner you're facing";
		usage = "/mobspawner [mob|info]";
		name = "mobspawner";
		alias = new String[] { "mspawn" };
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length >= 1) {
				String mobname = args[0];
				if (mobname.equalsIgnoreCase("info")
						|| mobname.equalsIgnoreCase("check")) {
					Block point =
							((Player) sender).getTargetBlock(
									null, 50);
					if (point != null
							&& point.getType().equals(
									Material.MOB_SPAWNER)) {

						try {
							CreatureSpawner cSpawn =
									(CreatureSpawner) point
											.getState();
							sender.sendMessage(ChatColor.AQUA
									+ "Mob Spawner @ "
									+ point.getX() + ","
									+ point.getY() + ","
									+ point.getZ());
							sender.sendMessage(ChatColor.AQUA
									+ "Mob Type: "
									+ cSpawn.getCreatureType()
											.name());
							sender.sendMessage(ChatColor.AQUA
									+ "Delay: "
									+ cSpawn.getDelay());
						} catch (Exception e) {
							sender.sendMessage(ChatColor.AQUA
									+ "Failed to get spawner data: "
									+ e.toString());
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.AQUA
								+ "There is no mob spawner at the targeted block!");
						return true;
					}
				} else {
					CreatureType mob = null;
					for (CreatureType m : CreatureType.values())
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
						for (CreatureType m : CreatureType
								.values()) {
							double v =
									StringCompare
											.compareStrings(
													pairs1,
													m.name());
							if (v > best && v > 0.5d) {
								bestMob = m.getName();
								best = v;
							}
						}
						if (bestMob.length() > 0) {
							sender.sendMessage(ChatColor.AQUA
									+ "Did you mean \""
									+ bestMob
											.toLowerCase()
											.replaceAll("_", " ")
									+ "\"?");
						}
						return true;
					} else {
						Block point =
								((Player) sender)
										.getTargetBlock(null, 50);
						if (point != null
								&& point.getType().equals(
										Material.MOB_SPAWNER)) {

							try {
								new CraftCreatureSpawner(point)
										.setCreatureType(mob);
							} catch (Exception e) {
								sender.sendMessage(ChatColor.AQUA
										+ "Failed to set creature type: "
										+ e.toString());
							}
							return true;
						} else {
							sender.sendMessage(ChatColor.AQUA
									+ "There is no mob spawner at the targeted block!");
							return true;
						}
					}
				}
			}
		} else {
			sender.sendMessage("This command cannot be used from the console!");
		}
		return false;
	}
}
