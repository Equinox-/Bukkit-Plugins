package com.pi.bukkit.plugins.snowfight;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {
    private SnowballFight plugin;

    public ArenaCommand(SnowballFight plugin) {
	this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
	    String label, String[] args) {
	if (sender instanceof Player) {
	    Player player = (Player) sender;
	    try {
		boolean result = true;
		if (args.length > 0) {
		    if (args[0].equalsIgnoreCase("edit"))
			result = editCommand(player, args);
		    else if (args[0].equalsIgnoreCase("new"))
			result = newCommand(player, args);
		    else if (args[0].equalsIgnoreCase("list"))
			result = listCommand(player, args);
		    else if (args[0].equalsIgnoreCase("save")) {
			if (result = player.hasPermission("snowfight.save")) {
			    plugin.saveArenas(sender);
			    plugin.saveLocalConfig();
			}
		    } else if (args[0].equalsIgnoreCase("start"))
			result = startCommand(player, args);
		    else if (args[0].equalsIgnoreCase("end"))
			result = endCommand(player, args);
		    else if (args[0].equalsIgnoreCase("reload")) {
			if (result = player.hasPermission("snowfight.reload")) {
			    plugin.loadConfig();
			    plugin.loadArenas(player);
			}
		    } else if (args[0].equalsIgnoreCase("score"))
			result = scoreCommand(player, args);
		    else if (args[0].equalsIgnoreCase("startauto")) {
			result = startAutoStopCommand(player, args);
		    } else if (args[0].equalsIgnoreCase("regen")) {
			result = regenCommand(player, args);
		    } else {
			return helpCommand(player, args);
		    }
		}
		if (!result) {
		    sender.sendMessage(SnowballFight.errorChatColor
			    + "SnowballFight can't give you access to this comand.  Snowball fight is sad.");
		    return true;
		}
	    } catch (Exception e) {
		sender.sendMessage(SnowballFight.errorChatColor
			+ "Error in executing command: " + e.toString());
	    }
	} else {
	    sender.sendMessage(SnowballFight.errorChatColor
		    + "You must be a player to use this command.");
	}
	return true;
    }

    private boolean regenCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.regen")) {
	    String arenaName = "default";
	    if (args.length > 1)
		arenaName = args[1];
	    SnowballFightHandler fight = plugin.getFightHandler(arenaName);
	    if (fight != null)
		if (fight.isInGame())
		    player.sendMessage(SnowballFight.errorChatColor + arenaName
			    + " is in a game!");
		else {
		    fight.prepareArea(player.getWorld());
		    player.sendMessage(SnowballFight.defaultChatColor
			    + "The terrain at " + arenaName
			    + " was regenerated.");
		}
	    else
		player.sendMessage(SnowballFight.errorChatColor
			+ "Bad arena name: " + arenaName);
	    return true;
	}
	return false;
    }

    private boolean scoreCommand(Player player, String[] args) {
	Object[] myGame = plugin.getCurrentGame(player.getName().toLowerCase());
	String arenaName = "default";
	if (args.length > 1) {
	    arenaName = args[1];
	}
	SnowballFightHandler setHandler = plugin.getFightHandler(arenaName);
	if (myGame != null && (args.length == 1 || setHandler == myGame[0])) {
	    if (player.hasPermission("snowfight.myscore")) {
		SnowballFightHandler handler = (SnowballFightHandler) myGame[0];
		if (myGame[1] == Team.RED) {
		    player.sendMessage(SnowballFight.defaultChatColor
			    + handler.getArena().getGameScoreMessage(Team.RED,
				    handler.redPoints, handler.bluePoints));
		} else {
		    player.sendMessage(SnowballFight.defaultChatColor
			    + handler.getArena().getGameScoreMessage(Team.BLUE,
				    handler.bluePoints, handler.redPoints));
		}
		return true;
	    }
	    return false;
	} else {
	    if (player.hasPermission("snowfight.otherscore")) {

		if (setHandler != null) {
		    player.sendMessage(SnowballFight.defaultChatColor
			    + "In the arena "
			    + arenaName
			    + ", "
			    + setHandler.getArena()
				    .getAnonymousScoreMessage(
					    setHandler.redPoints,
					    setHandler.bluePoints));
		    return true;
		} else {
		    player.sendMessage(SnowballFight.errorChatColor
			    + "Bad arena name: " + arenaName);
		    return true;
		}
	    } else {
		return false;
	    }
	}
    }

    private boolean endCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.endgame")) {
	    String arenaName = "default";
	    if (args.length > 1)
		arenaName = args[1];
	    SnowballFightHandler fight = plugin.getFightHandler(arenaName);
	    if (fight != null)
		if (!fight.isInGame())
		    player.sendMessage(SnowballFight.errorChatColor + arenaName
			    + " already not in an active game!");
		else {
		    fight.endGame(player.getWorld());
		    player.sendMessage(SnowballFight.defaultChatColor
			    + "The snowball fight at " + arenaName
			    + " stopped.");
		}
	    else
		player.sendMessage(SnowballFight.errorChatColor
			+ "Bad arena name: " + arenaName);
	    return true;
	}
	return false;
    }

    private boolean startCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.startgame")) {
	    String arenaName = "default";
	    if (args.length > 1)
		arenaName = args[1];
	    SnowballFightHandler fight = plugin.getFightHandler(arenaName);
	    if (fight != null)
		if (fight.isInGame())
		    player.sendMessage(SnowballFight.errorChatColor + arenaName
			    + " already in an active game!");
		else {
		    fight.startGame(player.getWorld(), -1);
		    player.sendMessage(SnowballFight.defaultChatColor
			    + "Started a snowball fight at " + arenaName);
		}
	    else
		player.sendMessage(SnowballFight.errorChatColor
			+ "Bad arena name: " + arenaName);
	    return true;
	}
	return false;
    }

    private boolean startAutoStopCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.startgame")) {
	    String arenaName = "default";
	    if (args.length > 1) {
		if (args.length > 2)
		    arenaName = args[1];
		int points = -1;
		try {
		    points = Integer.valueOf(args[args.length > 2 ? 2 : 1]);
		} catch (NumberFormatException e) {
		    player.sendMessage(SnowballFight.errorChatColor + "\""
			    + args[args.length > 2 ? 2 : 1]
			    + "\" is not a valid number format!");
		    return true;
		}
		SnowballFightHandler fight = plugin.getFightHandler(arenaName);
		if (fight != null)
		    if (fight.isInGame())
			player.sendMessage(SnowballFight.errorChatColor
				+ arenaName + " already in an active game!");
		    else {
			fight.startGame(player.getWorld(), points);
			player.sendMessage(SnowballFight.defaultChatColor
				+ "Started a snowball fight at " + arenaName);
		    }
		else
		    player.sendMessage(SnowballFight.errorChatColor
			    + "Bad arena name: " + arenaName);
		return true;
	    } else {
		player.sendMessage(SnowballFight.errorChatColor
			+ "To have an auto stop game you must have specified a stop point count!");
	    }
	}
	return false;
    }

    private boolean listCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.listarenas")) {
	    player.sendMessage(SnowballFight.defaultChatColor
		    + "Active arenas: "
		    + formatList(plugin.arenas.keySet().toArray(new String[0])));
	    return true;
	}
	return false;
    }

    public boolean newCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.createarena")) {
	    String arenaName = "default";
	    if (args.length > 1)
		arenaName = args[1];
	    Arena arena = plugin.getArena(arenaName);
	    if (arena != null)
		player.sendMessage(SnowballFight.errorChatColor
			+ "An arena by that name already exists!");
	    else {
		plugin.addArena(arenaName);
		player.sendMessage(SnowballFight.defaultChatColor
			+ "Created arena: " + arenaName);
	    }
	    return true;
	}
	return false;
    }

    public boolean editCommand(Player player, String[] args) {
	if (player.hasPermission("snowfight.editarena")) {
	    String arenaName = "default";
	    if (args.length > 1)
		arenaName = args[1];
	    Arena arena = plugin.getArena(arenaName);
	    if (arena != null)
		plugin.configurator.configure(arena, player);
	    else
		player.sendMessage(SnowballFight.errorChatColor
			+ "Bad arena name: " + arenaName);
	    return true;
	}
	return false;
    }

    public boolean helpCommand(Player player, String[] args) {
	player.sendMessage("/sfight edit [arenaName]  Edits the cuboids of an arena.  No arenaName and it assumes \"default\"");
	player.sendMessage("/sfight new [arenaName]  Creates a new arena.  No arenaName and it assumes \"default\"");
	player.sendMessage("/sfight list  Lists the loaded arenas");
	player.sendMessage("/sfight save  Forces a save of the plugin config");
	player.sendMessage("/sfight reload  Reloads the plugin config");
	player.sendMessage("/sfight score [arenaName]  Gets the current score of an arena, or your arena if none is specified.");
	player.sendMessage("/sfight startauto [endpoints]  /sfight startauto [arenaName] [endpoints]  Starts a game that will automatically stop at the given number of points");
	player.sendMessage("/sfight regen [arenaName]  Regenerates the terrain of an arena.  No arenaName and it assumes \"default\"");
	return true;
    }

    public static String formatList(String[] list) {
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

    public static String formatList(String[] list, String exclude) {
	List<String> res = new ArrayList<String>();
	for (String s : list)
	    if (!s.equalsIgnoreCase(exclude))
		res.add(s);
	return formatList(res.toArray(new String[res.size()]));
    }
}
