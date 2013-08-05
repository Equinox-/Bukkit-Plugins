package essentials.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import essentials.Essentials;

public class EssentialPipeCommand extends EssentialCommand {
	private Map<Integer, String> addArgs =
			new HashMap<Integer, String>();
	private final String pipeTo;

	public EssentialPipeCommand(Essentials plugin,
			String pipeTo, Object[][] map, String command,
			String usage, String description) {
		super(plugin);
		this.pipeTo = pipeTo;
		if (map != null)
			for (int i = 0; i < map.length; i++) {
				if (map[i].length != 2)
					throw new IllegalArgumentException(
							"The map must have elements 2 objects in length!");
				if (!(map[i][0] instanceof Integer))
					throw new IllegalArgumentException(
							"The first element must be of type integer.");
				if (!(map[i][1] instanceof String))
					throw new IllegalArgumentException(
							"The first element must be of type string.");
				addArgs.put((Integer) map[i][0],
						(String) map[i][1]);
			}
		this.desc = description != null ? description : "";
		this.usage = usage != null ? usage : "";
		this.name = command;
	}

	public EssentialPipeCommand(Essentials plugin,
			String pipeTo, Object[][] map, String command,
			String description) {
		this(plugin, pipeTo, map, command, description, "");
	}

	public EssentialPipeCommand(Essentials plugin,
			String pipeTo, Object[][] map, String command) {
		this(plugin, pipeTo, map, command, "", "");
	}

	public EssentialPipeCommand(Essentials plugin,
			String pipeTo, String command, String description) {
		this(plugin, pipeTo, null, command, description, "");
	}

	public EssentialPipeCommand(Essentials plugin,
			String pipeTo, String command) {
		this(plugin, pipeTo, null, command, "", "");
	}

	public EssentialPipeCommand(Essentials plugin,
			String pipeTo, String command, String usage,
			String description) {
		this(plugin, pipeTo, null, command, usage, description);
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		String[] newArgs =
				new String[args.length + addArgs.size()];
		int argIdx = 0;
		for (int i = 0; i < newArgs.length; i++) {
			if (addArgs.containsKey(new Integer(i))) {
				newArgs[i] = addArgs.get(new Integer(i));
			} else {
				newArgs[i] = args[argIdx];
				argIdx++;
			}
		}
		EssentialCommand pipeTo =
				plugin.getEssentialCommand(this.pipeTo);
		if (pipeTo != null)
			return pipeTo.execute(sender, command, label,
					newArgs);
		else
			return false;
	}
}
