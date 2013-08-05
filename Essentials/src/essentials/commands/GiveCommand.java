package essentials.commands;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;
import essentials.StringCompare;

public class GiveCommand extends EssentialCommand {
	public GiveCommand(Essentials plugin) {
		super(plugin);
		name = "give";
		usage =
				"/<command> [blockname:id] [amount] [damage]   /<command> [blockname:id] [player:block] [amount] [damage]";
		desc = "Gives a player an item";
		alias = new String[] { "g", "i", "item" };
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender, Command comm,
			String commandLabel, String[] args) {
		try {
			int len = args.length;
			if (len == 0)
				return false;
			boolean otherPlayer =
					len >= 2
							&& !isInteger(args[1])
							&& !args[1]
									.equalsIgnoreCase("block");
			boolean fillBlock =
					len >= 2 && !isInteger(args[1])
							&& args[1].equalsIgnoreCase("block");
			if (!otherPlayer && !(sender instanceof Player)) {
				sender.sendMessage(ChatColor.AQUA
						+ "You must supply a player if running from the console!");
				return false;
			}
			ItemStack stack =
					Essentials.fetchStack(sender, args[0]);
			if (stack == null)
				return true;
			try {
				short damage =
						len > ((otherPlayer || fillBlock) ? 3
								: 2) ? Short
								.valueOf(args[(otherPlayer || fillBlock) ? 3
										: 2])
								: 0;
				if (damage != 0)
					stack.setDurability(damage);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.AQUA
						+ "Invalid argument: "
						+ args[(otherPlayer || fillBlock) ? 3
								: 2] + " must be a number!");
				return false;
			}
			try {
				int count =
						len > ((otherPlayer || fillBlock) ? 2
								: 1) ? Integer
								.valueOf(args[(otherPlayer || fillBlock) ? 2
										: 1])
								: 1;
				stack.setAmount(count);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.AQUA
						+ "Invalid argument: "
						+ args[(otherPlayer || fillBlock) ? 2
								: 1] + " must be a number!");
				return false;
			}
			Player player =
					otherPlayer ? plugin.matchPlayer(args[1])
							: (Player) sender;
			if (player == null && otherPlayer) {
				sender.sendMessage(ChatColor.AQUA
						+ "Unable to find player: " + args[1]);
				ArrayList<String> pairs1 =
						StringCompare.wordLetterPairs(args[1]
								.toUpperCase());
				double best = 0;
				Player bestPlayer = null;
				for (Player p : sender.getServer()
						.getOnlinePlayers()) {
					double v =
							StringCompare.compareStrings(pairs1,
									p.getName());
					if (v > best && v > 0.5d) {
						bestPlayer = p;
						best = v;
					}
				}
				if (bestPlayer != null) {
					sender.sendMessage(ChatColor.AQUA
							+ "Did you mean "
							+ bestPlayer.getName() + "?");
				}
				return true;
			}
			if (!fillBlock) {
				Map<Integer, ItemStack> map =
						player.getInventory().addItem(stack);
				for (ItemStack s : map.values())
					player.getWorld().dropItem(
							player.getLocation(), s);
				return true;
			} else {
				Block target = player.getTargetBlock(null, 5);
				if (target != null
						&& target.getState() != null
						&& target.getState() instanceof ContainerBlock) {
					BlockState state = target.getState();
					Inventory inv =
							((ContainerBlock) target.getState())
									.getInventory();
					inv.addItem(stack);
					state.update();
					return true;
				} else {
					sender.sendMessage(ChatColor.AQUA
							+ "To give items to a block you must be targeting a block with an inventory!");
					return true;
				}
			}
		} catch (Exception e) {
			log.info(e.toString());
			for (StackTraceElement ele : e.getStackTrace())
				log.info(ele.toString());
			return false;
		}
	}

	private boolean isInteger(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
