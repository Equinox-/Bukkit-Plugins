package essentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.StringCompare;

public class HealCommand extends EssentialCommand {

	public HealCommand(Essentials plugin) {
		super(plugin);
		desc = "Heals a player";
		usage = "/heal /heal [player]";
		name = "heal";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		Player p = null;
		if (args.length == 0 && sender instanceof Player) {
			p = (Player) sender;
		} else if (args.length == 1) {
			p = plugin.matchPlayer(args[0]);
			if (p == null) {
				sender.sendMessage(ChatColor.AQUA
						+ "Unable to find player: " + args[0]);
				ArrayList<String> pairs1 =
						StringCompare.wordLetterPairs(args[0]
								.toUpperCase());
				double best = 0;
				Player bestMat = null;
				for (Player m : plugin.getServer()
						.getOnlinePlayers()) {
					double v =
							StringCompare.compareStrings(pairs1,
									m.getName());
					if (v > best && v > 0.5d) {
						bestMat = m;
						best = v;
					}
				}
				if (bestMat != null) {
					sender.sendMessage(ChatColor.AQUA
							+ "Did you mean "
							+ bestMat.getName().toLowerCase()
							+ "?");
				}
				return true;
			}
		} else {
			sender.sendMessage("You cannot use this command in the console!");
			return true;
		}
		p.setHealth(20);
		p.setFireTicks(0);
		return true;
	}

}
