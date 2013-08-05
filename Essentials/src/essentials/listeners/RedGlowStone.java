package essentials.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import essentials.Essentials;

public class RedGlowStone implements Listener {
	private Essentials plugin;
	private static final Material LIGHT_OFF =
			Material.LAPIS_BLOCK;
	private static final Material LIGHT_ON = Material.GLOWSTONE;

	public RedGlowStone(Essentials ess) {
		this.plugin = ess;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockRedstoneChange(BlockRedstoneEvent e) {
		Block b = e.getBlock();
		for (int xo = -1; xo <= 1; xo++) {
			for (int yo = -2; yo <= 2; yo++) {
				for (int zo = -1; zo <= 1; zo++) {
					Block glow =
							b.getWorld()
									.getBlockAt(b.getX() + xo,
											b.getY() + yo,
											b.getZ() + zo);
					try {
						if (glow != null) {
							if (glow.getType().equals(LIGHT_ON)) {
								Block powerable =
										b.getWorld().getBlockAt(
												b.getX() + xo,
												b.getY() + yo
														- 1,
												b.getZ() + zo);
								if (powerable != null
										&& !isPowered(powerable)) {
									glow.setType(LIGHT_OFF);
								}
							} else if (glow.getType().equals(
									LIGHT_OFF)) {
								Block powerable =
										b.getWorld().getBlockAt(
												b.getX() + xo,
												b.getY() + yo
														- 1,
												b.getZ() + zo);
								if (powerable != null
										&& isPowered(powerable)) {
									glow.setType(LIGHT_ON);
								}
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
}
