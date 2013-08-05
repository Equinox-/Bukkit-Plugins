package com.pi.bukkit.worldgen.village;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.BiomeBase;

public class WorldGenVillage extends StructureGenerator {
	public static List<BiomeBase> a = Arrays.asList(
			BiomeBase.BEACH, BiomeBase.DESERT, BiomeBase.FOREST,
			BiomeBase.DESERT_HILLS, BiomeBase.EXTREME_HILLS,
			BiomeBase.FOREST_HILLS, BiomeBase.FROZEN_OCEAN,
			BiomeBase.FROZEN_RIVER, BiomeBase.ICE_MOUNTAINS,
			BiomeBase.ICE_PLAINS, BiomeBase.JUNGLE,
			BiomeBase.JUNGLE_HILLS, BiomeBase.OCEAN,
			BiomeBase.PLAINS, BiomeBase.RIVER,
			BiomeBase.SMALL_MOUNTAINS, BiomeBase.SWAMPLAND,
			BiomeBase.TAIGA, BiomeBase.TAIGA_HILLS);
	private final int f;

	public WorldGenVillage(int paramInt) {
		this.f = paramInt;
	}

	@Override
	protected boolean a(int paramInt1, int paramInt2) {
		int i = 32;
		int j = 8;

		int k = paramInt1;
		int m = paramInt2;
		if (paramInt1 < 0)
			paramInt1 -= i - 1;
		if (paramInt2 < 0)
			paramInt2 -= i - 1;

		int n = paramInt1 / i;
		int i1 = paramInt2 / i;
		Random localRandom = this.d.A(n, i1, 10387312);
		n *= i;
		i1 *= i;
		n += localRandom.nextInt(i - j);
		i1 += localRandom.nextInt(i - j);
		paramInt1 = k;
		paramInt2 = m;

		if ((paramInt1 == n) && (paramInt2 == i1)) {
			boolean bool =
					this.d.getWorldChunkManager().a(
							paramInt1 * 16 + 8,
							paramInt2 * 16 + 8, 0, a);
			if (bool) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected StructureStart b(int paramInt1, int paramInt2) {
		return new WorldGenVillageStart(this.d, this.c,
				paramInt1, paramInt2, this.f);
	}
}

/*
 * Location:
 * C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name: net.minecraft.server.WorldGenVillage JD-Core Version: 0.6.0
 */