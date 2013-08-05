package com.pi.bukkit.plugins.snowfight;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SnowballFight extends JavaPlugin {
    public final static ChatColor defaultChatColor = ChatColor.AQUA;
    public final static ChatColor errorChatColor = ChatColor.RED;

    public int cuboidWand = Material.STICK.getId();
    public int someoneHitMyTeam = 0;
    public int myTeamHitSomeone = 0;
    public int maxSnowDepth = 3;
    public int minSnowDepth = 1;
    public float thinSnowWeight = 0.5f;
    public float thinSnowCheck = 0.25f;
    public Range snowballsPerBlock = new Range(4, 6);
    public Range snowballsPerThinSnow = new Range(1, 2);
    public Range snowBlockRarity = new Range(7, 13);
    public final Class<? extends NoiseGenerator> noiseGenClass = SimplexNoiseGenerator.class;
    public Map<String, Arena> arenas = new HashMap<String, Arena>();
    public Map<String, SnowballFightHandler> fightHandlers = new HashMap<String, SnowballFightHandler>();
    public ArenaConfigurator configurator;

    @Override
    public void onDisable() {
	try {
	    saveLocalConfig();
	} catch (IOException e) {
	    getLog().severe(
		    "Unable to save SnowballFight config: " + e.toString());
	}
	saveArenas(null);
    }

    @Override
    public void onEnable() {
	try {
	    loadConfig();
	    loadArenas(null);
	    configurator = new ArenaConfigurator(
		    Material.getMaterial(cuboidWand));
	    getServer().getPluginManager().registerEvents(configurator, this);
	    getCommand("sfight").setExecutor(new ArenaCommand(this));
	} catch (Exception e) {
	    getLog().severe("Unable to load SnowballFight: " + e.toString());
	    for (StackTraceElement ele : e.getStackTrace())
		getLog().severe(e.toString());
	}
    }

    public Logger getLog() {
	return getServer().getLogger();
    }

    public Object[] getCurrentGame(String player) {
	for (SnowballFightHandler handle : fightHandlers.values()) {
	    if (handle.redTeam.contains(player)) {
		return new Object[] { handle, Team.RED };
	    } else if (handle.blueTeam.contains(player)) {
		return new Object[] { handle, Team.BLUE };
	    }
	}
	return null;
    }

    public void loadConfig() throws FileNotFoundException, IOException,
	    InvalidConfigurationException {
	if (!getDataFolder().isDirectory())
	    getDataFolder().mkdir();
	File f = new File(getDataFolder(), "config.yml");
	if (!f.exists()) {
	    f.createNewFile();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
	    writer.write("# The Main Config File For The Snowball Fight Plugin\n");
	    writer.write("# Store arenas in files prefixed by arena_*.yml");
	    writer.write("\n# General Configuration\n");
	    writer.write("cuboidWand: " + cuboidWand + "\n");
	    writer.write("\n# Points Configuration\n");
	    writer.write("someoneHitMyTeam: " + someoneHitMyTeam + "\n");
	    writer.write("myTeamHitSomeone: " + myTeamHitSomeone + "\n");

	    writer.write("\n# Snow Configuration\n");
	    writer.write("minSnowDepth: " + minSnowDepth + "\n");
	    writer.write("maxSnowDepth: " + maxSnowDepth + "\n");

	    writer.write("\n# Thin Snow Configuration\n");
	    writer.write("# thinSnowWeight defines a float value that will (mainly)\n");
	    writer.write("# increase the chance of snow on the top layer  DO NOT SET IT HIGHER THAN 1\n");
	    writer.write("# thinSnowCheck sets the amount over the integer height for thin snow.");
	    writer.write("thinSnowWeight: " + thinSnowWeight + "\n");
	    writer.write("thinSnowCheck: " + thinSnowCheck + "\n");

	    writer.write("\n# Snow Drop Configuration\n");
	    writer.write("# SnowBlock rarity determines if a snow block will drop when destroying a snow block, with a 1/x chance\n");
	    writer.write("snowBlockRarity: " + snowBlockRarity.write() + "\n");
	    writer.write("snowballsPerBlock: " + snowballsPerBlock.write()
		    + "\n");
	    writer.write("snowballsPerThinSnow: "
		    + snowballsPerThinSnow.write());
	    writer.close();
	}
	YamlConfiguration config = new YamlConfiguration();
	config.load(f);
	someoneHitMyTeam = config.getInt("someoneHitMyTeam", someoneHitMyTeam);
	myTeamHitSomeone = config.getInt("myTeamHitSomeone", myTeamHitSomeone);
	minSnowDepth = config.getInt("minSnowDepth", minSnowDepth);
	maxSnowDepth = config.getInt("maxSnowDepth", maxSnowDepth);
	thinSnowCheck = (float) config
		.getDouble("thinSnowCheck", thinSnowCheck);
	thinSnowWeight = (float) config.getDouble("thinSnowWeight",
		thinSnowWeight);
	snowballsPerBlock.read(config.getString("snowballsPerBlock",
		snowballsPerBlock.write()));
	snowballsPerThinSnow.read(config.getString("snowballsPerThinSnow",
		snowballsPerThinSnow.write()));
	snowBlockRarity.read(config.getString("snowBlockRarity",
		snowBlockRarity.write()));
	cuboidWand = config.getInt("cuboidWand", cuboidWand);
    }

    public void saveLocalConfig() throws IOException {
	File f = new File(getDataFolder(), "config.yml");
	if (!f.exists()) {
	    f.createNewFile();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
	    writer.write("# The Main Config File For The Snowball Fight Plugin\n");
	    writer.write("# Store arenas in files prefixed by arena_*.yml");
	    writer.write("\n# General Configuration\n");
	    writer.write("cuboidWand: " + cuboidWand + "\n");
	    writer.write("\n# Points Configuration\n");
	    writer.write("someoneHitMyTeam: " + someoneHitMyTeam + "\n");
	    writer.write("myTeamHitSomeone: " + myTeamHitSomeone + "\n");

	    writer.write("\n# Snow Configuration\n");
	    writer.write("minSnowDepth: " + minSnowDepth + "\n");
	    writer.write("maxSnowDepth: " + maxSnowDepth + "\n");

	    writer.write("\n# Thin Snow Configuration\n");
	    writer.write("# thinSnowWeight defines a float value that will (mainly)\n");
	    writer.write("# increase the chance of snow on the top layer  DO NOT SET IT HIGHER THAN 1\n");
	    writer.write("# thinSnowCheck sets the amount over the integer height for thin snow.");
	    writer.write("thinSnowWeight: " + thinSnowWeight + "\n");
	    writer.write("thinSnowCheck: " + thinSnowCheck + "\n");

	    writer.write("\n# Snow Drop Configuration\n");
	    writer.write("# SnowBlock rarity determines if a snow block will drop when destroying a snow block, with a 1/x chance\n");
	    writer.write("snowBlockRarity: " + snowBlockRarity.write() + "\n");
	    writer.write("snowballsPerBlock: " + snowballsPerBlock.write()
		    + "\n");
	    writer.write("snowballsPerThinSnow: "
		    + snowballsPerThinSnow.write());
	    writer.close();
	}
    }

    public void loadArenas(CommandSender sender) throws IOException {
	File example = new File(getDataFolder(), "example_arena.yml");
	if (!example.exists()) {
	    example.createNewFile();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(example));
	    writer.write("# Example arena config file\n");
	    writer.write("# Real arena files must be prefixed by \"arena_\" and suffixed by \".yml\" between these is the arena identifier, (no spaces)\n");
	    writer.write("# There is also a default arena, called \"arena_default.yml\"\n");
	    writer.write("# Team Names\n");
	    writer.write("blueTeamName: " + Arena.defaultBlueTeamName + "\n");
	    writer.write("redTeamName: " + Arena.defaultRedTeamName + "\n");

	    writer.write("\n# Game end messages\n");
	    writer.write("# To use variables in these messages, refer to the following guide: \n");
	    writer.write("# %OT = opposing team name, %MT represents my team name\n");
	    writer.write("# %OP = opposing team's points, %MP represents my team's points\n");
	    writer.write("# To use send multi-line messages, use %n to indicate a new line, also, %c to indicate a colon\n");
	    writer.write("victoryMessage: " + Arena.defaultVictoryMessage
		    + "\n");
	    writer.write("defeatedMessage: " + Arena.defaultDefeatedMessage
		    + "\n");
	    writer.write("tieMessage: " + Arena.defaultTieMessage + "\n");

	    writer.write("\n# Game score messages\n");
	    writer.write("# To use variables in these messages, refer to the guide of game end messages\n");
	    writer.write("winningScoreMessage: "
		    + Arena.defaultWinningScoreMessage + "\n");
	    writer.write("losingScoreMessage: "
		    + Arena.defaultLosingScoreMessage + "\n");
	    writer.write("tieScoreMessage: " + Arena.defaultTieScoreMessage
		    + "\n");
	    writer.write("# For anonymous messages, %MT & %MP represent the winning team, %OT & %OP represent the losing team\n");
	    writer.write("anonymousScoreMessage: "
		    + Arena.defaultAnonymousScoreMessage);

	    writer.write("# Cuboids: xMin,xMax,yMin,yMax,zMin,zMax\n");
	    writer.write("waitingRoom: 0,0,0,0,0,0\n");
	    writer.write("redTeamArea: 0,0,0,0,0,0\n");
	    writer.write("blueTeamArea: 0,0,0,0,0,0\n");
	    writer.write("noTeamArea: 0,0,0,0,0,0");
	    writer.close();
	    if (sender != null)
		sender.sendMessage(defaultChatColor
			+ "Created example arena config file!");
	}

	PluginManager pm = getServer().getPluginManager();
	File[] files = getDataFolder().listFiles();
	for (File f : files) {
	    if (sender != null)
		sender.sendMessage(f.getAbsolutePath() + "," + f.getName());
	    if (f.getName().startsWith("arena_")
		    && f.getName().endsWith(".yml")) {
		String arenaName = f.getName().replace("arena_", "")
			.replace(".yml", "");
		if (arenaName.contains(" ")) {
		    getLog().severe("Invalid arena identifier: " + arenaName);
		    if (sender != null)
			sender.sendMessage(errorChatColor
				+ "Invalid arena identifier: " + arenaName);
		} else {
		    try {
			Arena arena = new Arena(this);
			YamlConfiguration config = new YamlConfiguration();
			config.load(f);
			arena.blueTeamName = config.getString("blueTeamName",
				Arena.defaultBlueTeamName);
			arena.redTeamName = config.getString("redTeamName",
				Arena.defaultRedTeamName);

			arena.tieMessage = config.getString("tieMessage",
				Arena.defaultTieMessage);
			arena.defeatedMessage = config
				.getString("defeatedMessage",
					Arena.defaultDefeatedMessage);
			arena.victoryMessage = config.getString(
				"victoryMessage", Arena.defaultVictoryMessage);

			arena.anonymousScoreMessage = config.getString(
				"anonymouseScoreMessage",
				Arena.defaultAnonymousScoreMessage);
			arena.winningScoreMessage = config.getString(
				"winningScoreMessage",
				Arena.defaultWinningScoreMessage);
			arena.tieScoreMessage = config
				.getString("tieScoreMessage",
					Arena.defaultTieScoreMessage);
			arena.losingScoreMessage = config.getString(
				"losingScoreMessage",
				Arena.defaultLosingScoreMessage);

			arena.waiting = new Cuboid();
			arena.redTeam = new Cuboid();
			arena.blueTeam = new Cuboid();
			arena.noTeamArea = new Cuboid();
			arena.waiting.read(config.getString("waitingRoom",
				"0,0,0,0,0,0"));
			arena.redTeam.read(config.getString("redTeamArea",
				"0,0,0,0,0,0"));
			arena.blueTeam.read(config.getString("blueTeamArea",
				"0,0,0,0,0,0"));
			arena.noTeamArea.read(config.getString("noTeamArea",
				"0,0,0,0,0,0"));
			arenas.put(arenaName, arena);
			SnowballFightHandler handler = new SnowballFightHandler(
				arena);
			fightHandlers.put(arenaName, handler);
			pm.registerEvents(handler, this);
			if (sender != null)
			    sender.sendMessage(defaultChatColor
				    + "Loaded arena, " + arenaName + "!");
		    } catch (Exception e) {
			getLog().severe(
				"Unable to load arena: " + arenaName + ": "
					+ e.toString());
			if (sender != null)
			    sender.sendMessage(errorChatColor
				    + "Unable to load arena: " + arenaName
				    + ":" + e.toString());
		    }
		}
	    }
	}
    }

    public void saveArenas(CommandSender sender) {
	for (String arenaName : arenas.keySet()) {
	    try {
		File f = new File(getDataFolder(), "arena_" + arenaName
			+ ".yml");
		Arena arena = arenas.get(arenaName);
		if (arena != null) {
		    if (!f.exists())
			f.createNewFile();
		    BufferedWriter writer = new BufferedWriter(
			    new FileWriter(f));
		    writer.write("# arena " + arenaName + " config file\n");
		    writer.write("# Team Names\n");
		    writer.write("blueTeamName: " + arena.blueTeamName + "\n");
		    writer.write("redTeamName: " + arena.redTeamName + "\n");

		    writer.write("\n# Game end messages\n");
		    writer.write("# To use variables in these messages, refer to the following guide: \n");
		    writer.write("# %OT = opposing team name, %MT represents my team name\n");
		    writer.write("# %OP = opposing team's points, %MP represents my team's points\n");
		    writer.write("# To use send multi-line messages, use %n to indicate a new line, also %c to indicate a colon\n");
		    writer.write("victoryMessage: " + arena.victoryMessage
			    + "\n");
		    writer.write("defeatedMessage: " + arena.defeatedMessage
			    + "\n");
		    writer.write("tieMessage: " + arena.tieMessage + "\n");

		    writer.write("\n# Game score messages\n");
		    writer.write("# To use variables in these messages, refer to the guide of game end messages\n");
		    writer.write("winningScoreMessage: "
			    + arena.winningScoreMessage + "\n");
		    writer.write("losingScoreMessage: "
			    + arena.losingScoreMessage + "\n");
		    writer.write("tieScoreMessage: " + arena.tieScoreMessage
			    + "\n");
		    writer.write("# For anonymous messages, %MT & %MP represent the winning team, %OT & %OP represent the losing team\n");
		    writer.write("anonymousScoreMessage: "
			    + arena.anonymousScoreMessage + "\n");

		    writer.write("# Cuboids: xMin,xMax,yMin,yMax,zMin,zMax\n");
		    writer.write("waitingRoom: "
			    + (arena.waiting != null ? arena.waiting.write()
				    : "0,0,0,0,0,0") + "\n");
		    writer.write("redTeamArea: "
			    + (arena.redTeam != null ? arena.redTeam.write()
				    : "0,0,0,0,0,0") + "\n");
		    writer.write("blueTeamArea: "
			    + (arena.blueTeam != null ? arena.blueTeam.write()
				    : "0,0,0,0,0,0") + "\n");
		    writer.write("noTeamArea: "
			    + (arena.noTeamArea != null ? arena.noTeamArea
				    .write() : "0,0,0,0,0,0"));
		    writer.close();
		    if (sender != null)
			sender.sendMessage(defaultChatColor + "Saved arena "
				+ arenaName);
		}
	    } catch (IOException e) {
		if (sender != null)
		    sender.sendMessage(errorChatColor + "Unable to save arena "
			    + arenaName + ": " + e.toString());
		getLog().severe(
			"Unable to save arena " + arenaName + ": "
				+ e.toString());
	    }
	}
    }

    public Arena getArena(String name) {
	for (Entry<String, Arena> s : arenas.entrySet()) {
	    if (s.getKey().equalsIgnoreCase(name)) {
		return s.getValue();
	    }
	}
	return null;
    }

    public void addArena(String arenaName) {
	Arena arena = new Arena(this);
	arenas.put(arenaName, arena);
	SnowballFightHandler handler = new SnowballFightHandler(arena);
	fightHandlers.put(arenaName, handler);
	getServer().getPluginManager().registerEvents(handler,this);
    }

    public SnowballFightHandler getFightHandler(String name) {
	for (Entry<String, SnowballFightHandler> s : fightHandlers.entrySet()) {
	    if (s.getKey().equalsIgnoreCase(name)) {
		return s.getValue();
	    }
	}
	return null;
    }

    public static Player getPlayerByName(String name, List<Player> players) {
	for (Player p : players) {
	    if (p.getName().equalsIgnoreCase(name)) {
		return p;
	    }
	}
	return null;
    }
}
