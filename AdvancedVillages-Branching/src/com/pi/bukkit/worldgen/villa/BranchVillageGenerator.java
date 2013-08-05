package com.pi.bukkit.worldgen.villa;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import com.pi.villa.gen.VillageGenerator;

public class BranchVillageGenerator extends ChunkGenerator {

	private final BranchVillagePlugin plugin;

	public BranchVillageGenerator(BranchVillagePlugin plug) {
		this.plugin = plug;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World w) {
		return Arrays
				.asList(new BlockPopulator[] { new VillageGenerator() });
	}

	private int getBlockIndex(int x, int y, int z) {
		if (y > 127)
			y = 127;
		return (((x << 4) + z) << 7) + y;
	}

	@Override
	public byte[] generate(World w, Random rand, int chunkX,
			int chunkZ) {
		byte[] data = new byte[32768];

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 64; y++) {
					data[getBlockIndex(x, y, z)] =
							(byte) Material.GRASS.getId();
				}
			}
		}
		return data;
	}
}
