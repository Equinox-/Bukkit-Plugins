package essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import essentials.Essentials;

public class RepairCommand extends EssentialCommand {

	public RepairCommand(Essentials plugin) {
		super(plugin);
		desc = "Repairs the item you're holding";
		usage = "/<command>";
		name = "repair";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			ItemStack holding =
					((Player) sender).getItemInHand();
			if (holding == null
					|| holding.getType().equals(Material.AIR)) {
				sender.sendMessage(ChatColor.AQUA
						+ "You must be holding an item to repair it!");
			} else {
				holding.setDurability((short) 0);
			}
			return true;
		}
		return false;
	}

}
