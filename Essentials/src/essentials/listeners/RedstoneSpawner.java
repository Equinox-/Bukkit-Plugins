package essentials.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import essentials.Essentials;

public class RedstoneSpawner implements Listener {
	private Map<Location, SpawnerState> lastSpawn =
			new HashMap<Location, SpawnerState>();
	private Essentials plugin;

	public RedstoneSpawner(Essentials ess) {
		this.plugin = ess;
	}

	private static class SpawnerState {
		public long lastSpawn = 0;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockRedstoneChange(BlockRedstoneEvent e) {
		Block b = e.getBlock();
		for (int xo = -1; xo <= 1; xo++) {
			for (int yo = -1; yo <= 1; yo++) {
				for (int zo = -1; zo <= 1; zo++) {
					Block spawn =
							b.getWorld()
									.getBlockAt(b.getX() + xo,
											b.getY() + yo,
											b.getZ() + zo);
					try {
						if (spawn != null
								&& spawn.getType().equals(
										Material.MOB_SPAWNER)) {
							CraftCreatureSpawner spawner =
									new CraftCreatureSpawner(
											spawn);
							if (spawner != null
									&& spawner.getSpawnedType() != null) {
								SpawnerState lSpawn =
										lastSpawn.get(spawn
												.getLocation());
								if (lSpawn == null)
									lSpawn = new SpawnerState();
								if (lSpawn.lastSpawn + 1000 < System
										.currentTimeMillis()
										&& isPowered(spawn)) {
									spawn.getWorld()
											.spawnCreature(
													new Location(
															spawner.getWorld(),
															spawn.getX(),
															spawn.getY() + 3,
															spawn.getZ()),
													spawner.getSpawnedType());
									lSpawn.lastSpawn =
											System.currentTimeMillis();
								}
								lastSpawn.put(
										spawn.getLocation(),
										lSpawn);
							}
						}
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	private static boolean isPowered(Block b) {
		Block check =
				b.getWorld().getBlockAt(b.getX(), b.getY() - 1,
						b.getZ());
		return check != null && check.isBlockPowered();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		lastSpawn.remove(e.getBlock().getLocation());
	}
}
