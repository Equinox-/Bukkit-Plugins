package com.pi.bukkit.plugins.saftey.creepers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.TileEntitySign;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftSign;

public class CreeperCrater {
	private static final long REAPPEAR_START = 5000;
	private static final long REAPPEAR_EACH_LOW = 2000;
	private static final long REAPPEAR_EACH_HIGH = 5000;

	private BlockCache[] blocks;
	private World w;
	private long nextBlockPlacement;
	private int cBlock = 0;

	public CreeperCrater(List<Block> blockList) {
		List<BlockCache> blocks =
				new ArrayList<BlockCache>(blockList.size());
		int mask = new Random().nextInt();
		for (Block b : blockList) {
			this.w = b.getWorld();
			Material type = b.getType();
			if (type == Material.CHEST
					|| type == Material.DISPENSER
					|| type == Material.FURNACE
					|| type == Material.MOB_SPAWNER) {
				continue;
			}
			blocks.add(BlockCacheData.get(b, mask));
		}
		Arrays.sort(this.blocks =
				blocks.toArray(new BlockCache[blocks.size()]));
		System.out.println("Crater with " + this.blocks.length
				+ " blocks");
		nextBlockPlacement =
				System.currentTimeMillis() + REAPPEAR_START;
	}

	public boolean processLoop() {
		if (cBlock < blocks.length
				&& System.currentTimeMillis() >= nextBlockPlacement) {
			BlockCache c = blocks[cBlock];
			c.place(w);
			cBlock++;
			nextBlockPlacement =
					System.currentTimeMillis()
							+ (int) (Math.random() * ((double) (REAPPEAR_EACH_HIGH - REAPPEAR_EACH_LOW)))
							+ REAPPEAR_EACH_LOW;
		}
		return cBlock >= blocks.length;
	}

	private static class BlockCache implements
			Comparable<BlockCache> {
		private int x, y, z;
		private Material m;
		private byte data;
		private int mask;

		private BlockCache(Block b, int mask) {
			this.x = b.getX();
			this.y = b.getY();
			this.z = b.getZ();
			this.m = b.getType();
			this.data = b.getData();
			this.mask = mask;
		}

		@Override
		public int compareTo(BlockCache o) {
			if (o.y > y) {
				return -1;
			} else if (o.y < y) {
				return 1;
			}
			if ((o.x ^ mask) < (x ^ mask)) {
				return -1;
			} else if ((o.x ^ mask) > (x ^ mask)) {
				return 1;
			}
			if ((o.z ^ mask) < (z ^ mask)) {
				return -1;
			} else if ((o.z ^ mask) > (z ^ mask)) {
				return 1;
			}
			return 0;
		}

		public final void place(World w) {
			Block b = w.getBlockAt(x, y, z);
			if (w.getBlockAt(x, y, z).getType() == Material.AIR) {
				w.getBlockAt(x, y, z).setTypeIdAndData(
						m.getId(), data, false);
				try {
					applyExtraData(b);
				} catch (Exception e) {
				}
			}
		}

		public void applyExtraData(Block b) {

		}
	}

	private static class BlockCacheSign extends BlockCache {
		private String[] lines;

		private BlockCacheSign(Block b, int mask) {
			super(b, mask);
			try {
				CraftSign s = new CraftSign(b);
				lines = new String[s.getLines().length];
				for (int i = 0; i < lines.length; i++) {
					lines[i] = new String(s.getLine(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void applyExtraData(Block b) {
			if (lines != null) {
				if (((CraftWorld) b.getWorld()).getTileEntityAt(
						b.getX(), b.getY(), b.getZ()) == null) {
					((CraftWorld) b.getWorld()).getHandle()
							.setTileEntity(b.getX(), b.getY(),
									b.getZ(),
									new TileEntitySign());
				}
				CraftSign s = new CraftSign(b);
				for (int i = 0; i < lines.length; i++) {
					s.setLine(i, lines[i]);
				}
			}
		}
	}

	private enum BlockCacheData {
		SIGN1(Material.SIGN_POST, BlockCacheSign.class),
		SIGN2(Material.WALL_SIGN, BlockCacheSign.class);
		private Material m;
		private Class<? extends BlockCache> o;

		private BlockCacheData(Material m,
				Class<? extends BlockCache> o) {
			this.m = m;
			this.o = o;
		}

		private static BlockCache get(Block b, int mask) {
			for (BlockCacheData d : values()) {
				if (d.m == b.getType()) {
					try {
						Constructor<? extends BlockCache> struct =
								d.o.getConstructor(Block.class,
										Integer.class);
						return struct.newInstance(b, mask);
					} catch (Exception e) {
					}
				}
			}
			return new BlockCache(b, mask);
		}
	}
}
