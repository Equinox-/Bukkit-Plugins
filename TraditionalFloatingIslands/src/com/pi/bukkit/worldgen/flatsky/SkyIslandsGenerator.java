package com.pi.bukkit.worldgen.flatsky;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import com.pi.bukkit.worldgen.village.WorldGenVillage;

public class SkyIslandsGenerator extends ChunkGenerator
		implements Filter<Double> {

	private final SkyIslandsPlugin plugin;

	WorldGenVillage villageGen = new WorldGenVillage(10);

	public SkyIslandsGenerator(SkyIslandsPlugin plug) {
		this.plugin = plug;
	}

	private int getBlockIndex(int x, int y, int z) {
		if (y > 127)
			y = 127;
		return (((x << 4) + z) << 7) + y;
	}

	@Override
	public byte[] generate(World w, Random rand, int chunkX,
			int chunkZ) {
		long start = System.currentTimeMillis();

		int realX = chunkX * 16;
		int realZ = chunkZ * 16;
		byte[] data = new byte[32768];

		Random r = new Random(w.getSeed());
		NoiseLayer mask =
				new NoiseLayer(new SimplexNoiseGenerator(r),
						0.005D);
		NoiseLayer height =
				new NoiseLayer(new SimplexNoiseGenerator(r), 1D);
		NoiseLayer hill =
				new NoiseLayer(new SimplexNoiseGenerator(r),
						0.05D);

		LinkedList<Point> allClosed = new LinkedList<Point>();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				/*
				 * if (accept(mask.noise(realX + x, realZ + z))) { if
				 * (!allClosed.contains(new Point(realX + x, realZ + z))) {
				 * LinkedList<Point> islandClosed = new LinkedList<Point>();
				 * Point base = avgConnected(mask, realX + x, realZ + z, this,
				 * new LinkedList<Point>(), islandClosed);
				 * allClosed.addAll(islandClosed); double hNoise =
				 * height.noise(base.x, base.y); int iBase = (int) ((hNoise *
				 * 64D) + 40D); for (Point p : islandClosed) { int rX = p.x -
				 * realX, rZ = p.y - realZ; if (rX >= 0 && rZ >= 0 && rX < 16 &&
				 * rZ < 16) { double maskNoise = normalize(mask.noise(p.x,
				 * p.y)); int stoneCap = (int) (iBase + (maskNoise * 2D) + (hill
				 * .noise(p.x, p.y) * 3D)); for (int y = (int) (iBase - 2D -
				 * (maskNoise * 25D)); y < stoneCap; y++) {
				 * data[getBlockIndex(rX, y, rZ)] = (byte) Material.STONE
				 * .getId(); } int grassCap = stoneCap + 2; for (int y =
				 * stoneCap; y < grassCap; y++) { data[getBlockIndex(rX, y, rZ)]
				 * = (byte) Material.DIRT .getId(); } data[getBlockIndex(rX,
				 * grassCap, rZ)] = (byte) Material.GRASS .getId(); } } int rX =
				 * base.x - realX, rZ = base.y - realZ; if (rX >= 0 && rZ >= 0
				 * && rX < 16 && rZ < 16) { for (int y = 0; y < 128; y++) {
				 * data[getBlockIndex(rX, y, rZ)] = (byte) Material.GLOWSTONE
				 * .getId(); } } } }
				 */
				for (int y = 0; y < 64; y++) {
					data[getBlockIndex(x, y, z)] =
							(byte) Material.GRASS.getId();
				}
			}
		}

		villageGen.a(((CraftWorld) w).getHandle().chunkProvider,
				((CraftWorld) w).getHandle(), chunkX, chunkZ,
				data);
		//villageGen.a(((CraftWorld) w).getHandle().chunkProvider,
		//		((CraftWorld) w).getHandle(), chunkX * 16,
		//		chunkZ * 16, data);
		return data;
	}

	private Point avgConnected(NoiseLayer mask, int sX, int sZ,
			Filter<Double> filter, LinkedList<Point> closed,
			List<Point> islandClosed) {
		int xT = 0, zT = 0;
		int count = 0;

		int sXN = sX;
		while (true) {
			if (isMaskedBorder(mask, sXN, sZ, filter))
				break;
			sXN--;
			if (sX - sXN > 1000) {
				return null;
			}
		}
		List<Point> open = new ArrayList<Point>();
		open.add(new Point(sXN, sZ));

		while (open.size() > 0) {
			if (open.size() > 5000) {
				// We are getting out of hand...
				return null;
			}
			List<Point> nOpen = new ArrayList<Point>();
			for (Point p : open) {
				xT += p.x;
				zT += p.y;
				count++;

				// help out with island closed
				int lY = p.y;
				while (true) {
					Point closedBase = new Point(p.x, lY);
					islandClosed.add(closedBase);
					lY++;
					if (!filter.accept(mask.noise(closedBase.x,
							lY))
							|| isMaskedBorder(mask,
									closedBase.x, lY, filter))
						break;
				}

				for (int xO = -1; xO <= 1; xO++) {
					for (int zO = -1; zO <= 1; zO++) {
						Point pt;
						if (isMaskedBorder(mask, p.x + xO, p.y
								+ zO, filter)
								&& !closed.contains(pt =
										new Point(p.x + xO, p.y
												+ zO))) {
							nOpen.add(pt);
							closed.addFirst(pt);
						}
					}
				}
			}
			open = nOpen;
		}
		return count > 0 ? new Point(xT / count, zT / count)
				: null;
	}

	private boolean isMaskedBorder(NoiseLayer mask, int sX,
			int sZ, Filter<Double> filter) {
		if (!filter.accept(mask.noise(sX, sZ)))
			return false;
		for (int i = -1; i <= 1; i += 2) {
			if (!filter.accept(mask.noise(sX + i, sZ))
					|| !filter.accept(mask.noise(sX, sZ + i)))
				return true;
		}
		return false;
	}

	/*
	 * @Override public Location getFixedSpawnLocation(World w, Random rand) {
	 * int chunkX = (rand.nextInt(100) - 50) * 16; int chunkZ =
	 * (rand.nextInt(100) - 50) * 16;
	 * 
	 * int baseX = rand.nextInt(16);
	 * 
	 * int offX = 1; while (baseX - offX >= 0 || baseX + offX < 16) { int baseZ
	 * = rand.nextInt(16); if (baseX - offX >= 0) { int offZ = 1; while (baseZ -
	 * offZ >= 0 || baseZ + offZ < 16) { if (baseZ - offZ >= 0) { int y =
	 * w.getHighestBlockYAt(chunkX + baseX - offX, chunkZ + baseZ - offZ); if (y
	 * > 0) { return new Location(w, chunkX + baseX - offX, y, chunkZ + baseZ -
	 * offZ); } } if (baseZ + offZ < 16) { int y = w.getHighestBlockYAt(chunkX +
	 * baseX - offX, chunkZ + baseZ + offZ); if (y > 0) { return new Location(w,
	 * chunkX + baseX - offX, y, chunkZ + baseZ + offZ); } } offZ++; } } if
	 * (baseX + offX < 16) { int offZ = 1; while (baseZ - offZ >= 0 || baseZ +
	 * offZ < 16) { if (baseZ - offZ >= 0) { int y = w.getHighestBlockYAt(chunkX
	 * + baseX + offX, chunkZ + baseZ - offZ); if (y > 0) { return new
	 * Location(w, chunkX + baseX + offX, y, chunkZ + baseZ - offZ); } } if
	 * (baseZ + offZ < 16) { int y = w.getHighestBlockYAt(chunkX + baseX + offX,
	 * chunkZ + baseZ + offZ); if (y > 0) { return new Location(w, chunkX +
	 * baseX + offX, y, chunkZ + baseZ + offZ); } } offZ++; } } offX++; } return
	 * getFixedSpawnLocation(w, rand); }
	 */

	@Override
	public boolean accept(Double t) {
		return t > 0.4;
	}

	public double normalize(double d) {
		if (d > 0.4) {
			return (d - 0.4D) / 0.6D;
		}
		return -1;
	}
}
