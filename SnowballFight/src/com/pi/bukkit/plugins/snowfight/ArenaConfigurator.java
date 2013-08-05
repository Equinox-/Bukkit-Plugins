package com.pi.bukkit.plugins.snowfight;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ArenaConfigurator implements Listener {
    final Material wand;
    private Map<String, Arena> configuring = new HashMap<String, Arena>();
    private Map<String, ConfigureState> configState = new HashMap<String, ConfigureState>();

    public ArenaConfigurator(Material wand) {
	this.wand = wand;
    }

    public void configure(Arena arena, Player player) {
	configuring.put(player.getName(), arena);
	configState.put(player.getName(), ConfigureState.WAITING_MIN);
	player.sendMessage(SnowballFight.defaultChatColor
		+ "Select the minimum block of the waiting cuboid with a "
		+ wand.name().toLowerCase());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent e) {
	ItemStack stack = e.getItem();
	if (e.getClickedBlock() != null && stack != null
		&& stack.getType() == wand) {
	    Arena arena = configuring.get(e.getPlayer().getName());
	    ConfigureState state = configState.get(e.getPlayer().getName());
	    if (state != null && arena != null) {
		state = state
			.execute(arena, e.getClickedBlock(), e.getPlayer());
		configState.put(e.getPlayer().getName(), state);
		if (state == null) {
		    configuring.remove(e.getPlayer().getName());
		    configState.remove(e.getPlayer().getName());
		}
	    }
	}
    }

    public static enum ConfigureState {
	WAITING_MIN() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		if (a.getWaitingArea() == null)
		    a.waiting = new Cuboid();
		a.getWaitingArea().minX = b.getX();
		a.getWaitingArea().minY = b.getY();
		a.getWaitingArea().minZ = b.getZ();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the minimum of the waiting cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the maximum block of the waiting cuboid...");
		return WAITING_MAX;
	    }
	},
	WAITING_MAX() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		a.getWaitingArea().maxX = b.getX();
		a.getWaitingArea().maxY = b.getY();
		a.getWaitingArea().maxZ = b.getZ();
		a.getWaitingArea().clean();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the maximum of the waiting cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor + "Waiting area: "
			+ a.getWaitingArea().write());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the minimum block of the red team cuboid...");
		return REDTEAM_MIN;
	    }
	},
	REDTEAM_MIN() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		if (a.getRedTeam() == null)
		    a.redTeam = new Cuboid();
		a.getRedTeam().minX = b.getX();
		a.getRedTeam().minY = b.getY();
		a.getRedTeam().minZ = b.getZ();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the minimum of the red ream cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the maximum block of the red team cuboid...");
		return REDTEAM_MAX;
	    }
	},
	REDTEAM_MAX() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		a.getRedTeam().maxX = b.getX();
		a.getRedTeam().maxY = b.getY();
		a.getRedTeam().maxZ = b.getZ();
		a.getRedTeam().clean();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the maximum of the red team cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Red team area: " + a.getRedTeam().write());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the minimum block of the blue team cuboid...");
		return BLUETEAM_MIN;
	    }
	},
	BLUETEAM_MIN() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		if (a.getBlueTeam() == null)
		    a.blueTeam = new Cuboid();
		a.getBlueTeam().minX = b.getX();
		a.getBlueTeam().minY = b.getY();
		a.getBlueTeam().minZ = b.getZ();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the minimum of the blue team cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the maximum block of the blue team cuboid...");
		return BLUETEAM_MAX;
	    }
	},
	BLUETEAM_MAX() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		a.getBlueTeam().maxX = b.getX();
		a.getBlueTeam().maxY = b.getY();
		a.getBlueTeam().maxZ = b.getZ();
		a.getBlueTeam().clean();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the maximum of the blue team cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Blue team area: " + a.getBlueTeam().write());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the minimum block of the public area cuboid.");
		p.sendMessage(SnowballFight.defaultChatColor
			+ "If you don't want to use this feature, select the same block twice.");
		return PUBLICAREA_MIN;
	    }
	},
	PUBLICAREA_MIN() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		if (a.noTeamArea == null)
		    a.noTeamArea = new Cuboid();
		a.noTeamArea.minX = b.getX();
		a.noTeamArea.minY = b.getY();
		a.noTeamArea.minZ = b.getZ();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the minimum of the public area cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Now select the maximum block of the public area cuboid...");
		return PUBLICAREA_MAX;
	    }
	},
	PUBLICAREA_MAX() {
	    @Override
	    public ConfigureState execute(Arena a, Block b, Player p) {
		a.noTeamArea.maxX = b.getX();
		a.noTeamArea.maxY = b.getY();
		a.noTeamArea.maxZ = b.getZ();
		a.noTeamArea.clean();
		p.sendMessage(SnowballFight.defaultChatColor
			+ "Set the maximum of the public area cuboid at: "
			+ b.getX() + "," + b.getY() + "," + b.getZ());
		if (a.noTeamArea.isEmpty())
		    p.sendMessage(SnowballFight.defaultChatColor
			    + "There is no public area");
		else
		    p.sendMessage(SnowballFight.defaultChatColor
			    + "Public area: " + a.noTeamArea.write());
		p.sendMessage(SnowballFight.defaultChatColor + "All done!");
		return null;
	    }
	};
	public abstract ConfigureState execute(Arena a, Block b, Player p);
    }
}
