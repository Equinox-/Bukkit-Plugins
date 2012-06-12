package com.pi.bukkit.worldgen.floatingisland;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.WorldGenBase;
import net.minecraft.server.WorldGenMineshaft;
import net.minecraft.server.WorldGenVillage;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.pi.bukkit.worldgen.floatingisland.gen.BiomeDecoratorPopulator;
import com.pi.bukkit.worldgen.floatingisland.gen.CavesGenerator;
import com.pi.bukkit.worldgen.floatingisland.gen.LakesGenerator;
import com.pi.bukkit.worldgen.floatingisland.gen.SnowPopulator;

public class FloatingIslandGenerator extends ChunkGenerator {

	private FloatingIslandPlugin plugin;

	@Override
	public List<BlockPopulator> getDefaultPopulators(World w) {
		return Arrays.asList(new LakesGenerator(), new SnowPopulator(),
				new BiomeDecoratorPopulator());
	}

	public FloatingIslandGenerator(FloatingIslandPlugin plugin) {
		this.plugin = plugin;
	}

	private int getBlockIndex(int x, int y, int z) {
		if (y > 127)
			y = 127;
		return (((x << 4) + z) << 7) + y;
	}

	@Override
	public byte[] generate(World w, Random rand, int chunkX, int chunkZ) {
		int realX = chunkX << 4;
		int realZ = chunkZ << 4;

		LayeredOctaveNoise noise = new LayeredOctaveNoise(
				new SimplexOctaveGenerator(new Random(99), 1), 7);

		double sizeRoot = noise.noise(0, realX << 2, realZ << 2);

		/*
		 * IslandConfig rConfig = IslandConfig.forBiome(w.getBiome(realX,
		 * realZ));
		 * 
		 * noise.setScale(0, sizeRoot (rConfig.islandSizeMax -
		 * rConfig.islandSizeMin) + rConfig.islandSizeMin); // mask
		 */

		byte[] data = new byte[32768];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int noiseX = realX + x;
				int noiseZ = realZ + z;

				IslandConfig config = IslandConfig.forBiome(w.getBiome(noiseX,
						noiseZ));

				noise.setScale(0, 0.01D);/*TODO
										 * sizeRoot (config.islandSizeMax -
										 * config.islandSizeMin) +
										 * config.islandSizeMin); // mask
										 */

				noise.setScale(1, 0.01D); // base
				noise.setScale(2, 2D); // ext spike
				noise.setScale(3, 0.1D); // root spike
				noise.setScale(4, config.hillNoise); // hill
				noise.setScale(5, 0.001D); // dirt gen
				noise.setScale(6, 0.0001D); // hill size

				double mask = noise.noise(0, noiseX, noiseZ);
				int iLayer = -1;
				for (int i = 0; i < config.islandRarityMin.length; i++) {
					if (mask >= config.islandRarityMin[i]
							&& mask < config.islandRarityMax[i]) {
						iLayer = i;
						break;
					}
				}
				if (iLayer >= 0) {
					int baseY = (int) (noise.noise(1, noiseX, noiseZ) * (config.baseMax[iLayer] - config.baseMin[iLayer]))
							+ config.baseMin[iLayer];
					int spikeHeight = (int) (noise.noise(3, noiseX, noiseZ) * (config.rootSpikeMax - config.rootSpikeMin))
							+ config.rootSpikeMin;

					int spikeNoise = (int) (noise.noise(2, noiseX, noiseZ) * (config.extSpikeMax - config.extSpikeMin))
							+ config.extSpikeMin;

					int hillSize = (int) (noise.noise(6, noiseX, noiseZ) * config.hillMax.length);
					int hillHeight = (int) (noise.noise(4, noiseX, noiseZ) * (config.hillMax[hillSize] - config.hillMin[hillSize]))
							+ config.hillMin[hillSize];
					int dirtHeight = (int) (noise.noise(5, noiseX, noiseZ) * (config.dirtMax - config.dirtMin))
							+ config.dirtMin;

					for (int y = Math.max(0, Math.min(
							baseY + hillHeight - config.minStoneMinThickness,
							baseY
									- Math.max(config.maxStoneMinThickness,
											spikeHeight) + spikeNoise)); y < baseY
							+ hillHeight; y++) {
						data[getBlockIndex(x, y, z)] = (byte) Material.STONE
								.getId();
					}
					int coatingMid = (int) (noise.noise(2, noiseX, noiseZ)
							* ((double) dirtHeight) * .85D);
					for (int y = baseY + hillHeight; y < baseY + hillHeight
							+ coatingMid; y++) {
						data[getBlockIndex(x, y, z)] = (byte) config.lowerCoating
								.getId();
					}

					for (int y = baseY + hillHeight + coatingMid; y < baseY
							+ hillHeight + dirtHeight; y++) {
						data[getBlockIndex(x, y, z)] = (byte) config.coating
								.getId();
					}

					data[getBlockIndex(x, baseY + hillHeight + dirtHeight, z)] = (byte) config.topCoating
							.getId();
				}
			}
		}
		genStructures(w, chunkX << 4, chunkZ << 4, data);
		return data;
	}

	public void genStructures(World world, int cX, int cZ, byte[] data) {
		net.minecraft.server.World handle = ((CraftWorld) world).getHandle();

		WorldGenBase t = new CavesGenerator();
		// WorldGenStronghold u = new WorldGenStronghold();
		WorldGenVillage v = new WorldGenVillage(0);
		WorldGenMineshaft w = new WorldGenMineshaft();
		// WorldGenBase x = new WorldGenCanyon();

		t.a(null, handle, cX, cZ, data);
		// x.a(null, handle, cX, cZ, data);
		if (world.canGenerateStructures()) {
			w.a(null, handle, cX, cZ, data);
			v.a(null, handle, cX, cZ, data);
			// u.a(null, handle, cX, cZ, data);
		}
	}

	public FloatingIslandPlugin getPlugin() {
		return plugin;
	}
}
