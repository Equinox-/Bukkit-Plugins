package essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import essentials.Essentials;

public class EssentialCommandGroup extends EssentialCommand {
	String[] commands;

	public EssentialCommandGroup(Essentials plugin, String name,
			String... commands) {
		super(plugin);
		this.name = name;
		this.commands = commands;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		for (String cN : commands) {
			EssentialCommand c = plugin.commands.get(cN);
			if (c != null)
				sender.sendMessage(ChatColor.AQUA + "/" + c.name
						+ ":  " + c.desc);
		}
		return true;
	}
}
