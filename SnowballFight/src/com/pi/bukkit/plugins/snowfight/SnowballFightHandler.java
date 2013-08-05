package com.pi.bukkit.plugins.snowfight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SnowballFightHandler implements Listener {
    private Arena arena;
    private SnowballFight plugin;
    public int redPoints = 0;
    public int bluePoints = 0;
    private boolean inGame = false;
    List<String> redTeam = new ArrayList<String>(),
	    blueTeam = new ArrayList<String>();
    Map<String, InventoryCache> inventoryCache = new HashMap<String, InventoryCache>();
    public int stopGameAtPoints = -1;

    public SnowballFightHandler(Arena arena) {
	this.arena = arena;
	this.plugin = arena.plugin;
	plugin.getServer().getPluginManager()
		.registerEvents(new SnowDropHandler(this), plugin);
	plugin.getServer().getPluginManager()
		.registerEvents(new PlayerRespawnHandler(this), plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent e) {
	if (inGame && e instanceof EntityDamageByEntityEvent) {
	    EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
	    if (ee.getDamager() != null && ee.getEntity() != null
		    && ee.getEntity() instanceof Player
		    && ee.getDamager() instanceof Snowball) {
		Player player = (Player) ee.getEntity();
		if (arena.inPlayingArea(player.getLocation())) {
		    Team hitTeam = getTeam(player.getName().toLowerCase());
		    if (hitTeam == Team.RED) {
			bluePoints += plugin.myTeamHitSomeone;
			redPoints += plugin.someoneHitMyTeam;
		    } else if (hitTeam == Team.BLUE) {
			redPoints += plugin.myTeamHitSomeone;
			bluePoints += plugin.someoneHitMyTeam;
		    }
		    if (stopGameAtPoints > 0) {
			if (redPoints >= stopGameAtPoints
				|| bluePoints >= stopGameAtPoints) {
			    endGame(player.getWorld());
			}
		    }
		}
	    }
	}
    }

    public boolean isPlaying(String player) {
	return redTeam.contains(player) || blueTeam.contains(player);
    }

    public Team getTeam(String player) {
	return redTeam.contains(player) ? Team.RED
		: blueTeam.contains(player) ? Team.BLUE : null;
    }

    public void prepareArea(World w) {
	long seed = new Random().nextLong();
	noisyCuboid(arena.getRedTeam(), w, seed);
	noisyCuboid(arena.getBlueTeam(), w, seed);
	if (arena.noTeamArea != null && !arena.noTeamArea.isEmpty())
	    noisyCuboid(arena.noTeamArea, w, seed);
    }

    public void noisyCuboid(Cuboid c, World w, long seed) {
	NoiseGenerator gen;
	try {
	    gen = plugin.noiseGenClass.getConstructor(Long.class).newInstance(
		    seed);
	} catch (Exception e) {
	    gen = new SimplexNoiseGenerator(seed);
	}
	c.clear(w);
	for (int x = c.minX; x <= c.maxX; x++) {
	    for (int z = c.minZ; z <= c.maxZ; z++) {
		float noise = (float) ((gen.noise(x, z) + 1d) / 2d);
		float myHeight = plugin.minSnowDepth
			+ (noise * (plugin.maxSnowDepth - plugin.minSnowDepth + plugin.thinSnowWeight));
		int hI = Math.round(myHeight);
		if (hI > plugin.maxSnowDepth)
		    hI--;
		if (hI < plugin.minSnowDepth)
		    hI++;
		boolean thinStuff = myHeight - plugin.thinSnowCheck >= hI;
		int y;
		for (y = c.minY; y < Math.min(c.minY + hI, c.maxY); y++) {
		    w.getBlockAt(x, y, z).setType(Material.SNOW_BLOCK);
		}
		if (thinStuff)
		    w.getBlockAt(x, y, z).setType(Material.SNOW);
	    }
	}
    }

    public void formTeams(World w) {
	redTeam.clear();
	blueTeam.clear();
	Random rand = new Random();
	List<Player> players = w.getPlayers();
	List<Player> usablePlayers = new ArrayList<Player>();
	for (Player p : players) {
	    if (arena.getWaitingArea().contains(p.getLocation())) {
		usablePlayers.add(p);
		InventoryCache c = new InventoryCache();
		c.cloneInventory(p);
		inventoryCache.put(p.getName().toLowerCase(), c);
		p.getInventory().clear();
	    }
	}
	int maxPlayersPerTeam = (usablePlayers.size() + 1) / 2;
	for (Player p : usablePlayers) {
	    if (rand.nextBoolean() && redTeam.size() < maxPlayersPerTeam) {
		addToTeam(Team.RED, p);
	    } else if (blueTeam.size() < maxPlayersPerTeam) {
		addToTeam(Team.BLUE, p);
	    } else {
		if (blueTeam.size() < redTeam.size()) {
		    addToTeam(Team.BLUE, p);
		} else {
		    addToTeam(Team.RED, p);
		}
	    }
	}
	for (String s : redTeam) {
	    Player p = SnowballFight.getPlayerByName(s, players);
	    p.sendMessage(SnowballFight.defaultChatColor
		    + "You're on "
		    + arena.redTeamName
		    + " with "
		    + ArenaCommand.formatList(redTeam.toArray(new String[0]), s));
	    p.sendMessage(SnowballFight.defaultChatColor
		    + "You're opposing "
		    + arena.blueTeamName
		    + " containing "
		    + ArenaCommand.formatList(blueTeam.toArray(new String[0]),
			    s));
	}
	for (String s : blueTeam) {
	    Player p = SnowballFight.getPlayerByName(s, players);
	    p.sendMessage(SnowballFight.defaultChatColor
		    + "You're on "
		    + arena.blueTeamName
		    + " with "
		    + ArenaCommand.formatList(blueTeam.toArray(new String[0]),
			    s));
	    p.sendMessage(SnowballFight.defaultChatColor
		    + "You're opposing "
		    + arena.redTeamName
		    + " containing "
		    + ArenaCommand.formatList(redTeam.toArray(new String[0]), s));
	}
    }

    public void disbandTeams(World w) {
	Random rand = new Random();
	String[] blueTeamMessage = arena.getGameEndMessage(Team.BLUE,
		bluePoints, redPoints).split("\n");
	String[] redTeamMessage = arena.getGameEndMessage(Team.RED, redPoints,
		bluePoints).split("\n");
	List<Player> players = w.getPlayers();
	for (String name : blueTeam) {
	    Player p = SnowballFight.getPlayerByName(name, players);
	    for (String s : blueTeamMessage) {
		p.sendMessage(s);
	    }
	    spawnPlayerInCuboid(rand, p, arena.getWaitingArea());
	    InventoryCache cache = inventoryCache.get(name.toLowerCase());
	    if (cache != null)
		cache.restoreInventory(p);
	}
	blueTeam.clear();
	for (String name : redTeam) {
	    Player p = SnowballFight.getPlayerByName(name, players);
	    for (String s : redTeamMessage) {
		p.sendMessage(s);
	    }
	    spawnPlayerInCuboid(rand, p, arena.getWaitingArea());
	    InventoryCache cache = inventoryCache.get(name.toLowerCase());
	    if (cache != null)
		cache.restoreInventory(p);
	}
	redTeam.clear();
	inventoryCache.clear();
	bluePoints = 0;
	redPoints = 0;
    }

    public void addToTeam(Team t, Player p) {
	if (spawnPlayerInCuboid(new Random(), p,
		t == Team.RED ? arena.getRedTeam() : arena.getBlueTeam(),
		arena.getBlueTeam())) {
	    (t == Team.RED ? redTeam : blueTeam).add(p.getName());
	}
    }

    public void startGame(World w, int stopAtPoints) {
	formTeams(w);
	stopGameAtPoints = stopAtPoints;
	inGame = true;
    }

    public void endGame(World w) {
	inGame = false;
	disbandTeams(w);
	prepareArea(w);
    }

    private boolean spawnPlayerInCuboid(Random rand, Player player,
	    Cuboid cuboid, Cuboid lookAt) {
	// Get random location in cuboid...
	Location loc = null;
	for (int tryy = 0; tryy <= 10; tryy++) {
	    loc = cuboid.getSpawnableLocation(rand, player.getWorld());
	    if (loc != null)
		break;
	}
	if (loc != null) {
	    Location face = lookAt.randomLocation(rand, player.getWorld());
	    double xV = face.getX() - loc.getX();
	    double yV = face.getY() - loc.getY();
	    double zV = face.getZ() - loc.getZ();
	    double xzMag = Math.sqrt((xV * xV) + (zV * zV));
	    float pitch = (float) Math.toDegrees(Math.atan(yV / xzMag));
	    // double h = Math.cos(Math.toRadians(pitch));
	    float yaw = (float) Math.toDegrees(Math.acos(zV / xzMag));
	    loc.setPitch(pitch);
	    loc.setYaw(yaw);
	    player.teleport(loc);
	    return true;
	} else {
	    plugin.getLog().severe(
		    "Unable to find spawnable location in cuboid: "
			    + cuboid.write());
	    return false;
	}
    }

    public boolean spawnPlayerInCuboid(Random rand, Player player, Cuboid cuboid) {
	// Get random location in cuboid...
	Location loc = null;
	for (int tryy = 0; tryy <= 10; tryy++) {
	    loc = cuboid.getSpawnableLocation(rand, player.getWorld());
	    if (loc != null)
		break;
	}
	if (loc != null) {
	    player.teleport(loc);
	    return true;
	} else {
	    plugin.getLog().severe(
		    "Unable to find spawnable location in cuboid: "
			    + cuboid.write());
	    return false;
	}
    }

    public boolean isInGame() {
	return inGame;
    }

    public Arena getArena() {
	return arena;
    }

    public SnowballFight getPlugin() {
	return plugin;
    }
}
