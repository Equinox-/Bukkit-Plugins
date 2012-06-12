package com.pi.bukkit.worldgen.floatingisland;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.BiomeBase;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public class IslandConfig {
	private final static Map<Biome, IslandConfig> biomeMapping = new HashMap<Biome, IslandConfig>();
	public final static Map<Biome, BiomeBase> biomeBaseMapping = new HashMap<Biome, BiomeBase>();
	private final static IslandConfig defaultIslandConfig = new IslandConfig();

	static {
		biomeBaseMapping.put(Biome.BEACH, BiomeBase.BEACH);
		biomeBaseMapping.put(Biome.DESERT, BiomeBase.DESERT);
		biomeBaseMapping.put(Biome.DESERT_HILLS, BiomeBase.DESERT_HILLS);
		biomeBaseMapping.put(Biome.EXTREME_HILLS, BiomeBase.EXTREME_HILLS);
		biomeBaseMapping.put(Biome.FOREST, BiomeBase.FOREST);
		biomeBaseMapping.put(Biome.FOREST_HILLS, BiomeBase.FOREST_HILLS);
		biomeBaseMapping.put(Biome.FROZEN_OCEAN, BiomeBase.FROZEN_OCEAN);
		biomeBaseMapping.put(Biome.FROZEN_RIVER, BiomeBase.FROZEN_RIVER);
		biomeBaseMapping.put(Biome.ICE_MOUNTAINS, BiomeBase.ICE_MOUNTAINS);
		biomeBaseMapping.put(Biome.ICE_PLAINS, BiomeBase.ICE_PLAINS);
		biomeBaseMapping.put(Biome.JUNGLE, BiomeBase.JUNGLE);
		biomeBaseMapping.put(Biome.JUNGLE_HILLS, BiomeBase.JUNGLE_HILLS);
		biomeBaseMapping.put(Biome.MUSHROOM_ISLAND, BiomeBase.MUSHROOM_ISLAND);
		biomeBaseMapping.put(Biome.MUSHROOM_SHORE, BiomeBase.MUSHROOM_SHORE);
		biomeBaseMapping.put(Biome.OCEAN, BiomeBase.OCEAN);
		biomeBaseMapping.put(Biome.PLAINS, BiomeBase.PLAINS);
		biomeBaseMapping.put(Biome.RIVER, BiomeBase.RIVER);
		biomeBaseMapping.put(Biome.SKY, BiomeBase.SKY);
		biomeBaseMapping.put(Biome.SMALL_MOUNTAINS, BiomeBase.SMALL_MOUNTAINS);
		biomeBaseMapping.put(Biome.SWAMPLAND, BiomeBase.SWAMPLAND);
		biomeBaseMapping.put(Biome.TAIGA, BiomeBase.TAIGA);
		biomeBaseMapping.put(Biome.TAIGA_HILLS, BiomeBase.TAIGA_HILLS);

		IslandConfig hills = new IslandConfig();
		hills.hillMin = new int[] { 0, 2, 5 };
		hills.hillMax = new int[] { 2, 10, 15 };
		hills.islandSizeMin = 0.02;
		hills.islandSizeMax = 0.03;

		IslandConfig extremeHills = new IslandConfig();
		extremeHills.hillMin = new int[] { 0, 2, 5, 10 };
		extremeHills.hillMax = new int[] { 2, 10, 20, 40 };
		extremeHills.islandSizeMin = 0.04;
		extremeHills.islandSizeMax = 0.06;
		biomeMapping.put(Biome.EXTREME_HILLS, extremeHills);

		IslandConfig grasslands = new IslandConfig();
		grasslands.islandSizeMin = 0.005D;
		grasslands.islandSizeMax = 0.0125D;
		grasslands.hillMin = new int[] { 0, 2 };
		grasslands.hillMax = new int[] { 2, 3 };
		grasslands.hillNoise = 0.075D;
		biomeMapping.put(Biome.PLAINS, grasslands);

		IslandConfig icePlains = grasslands.clone();
		icePlains.minSnowMin = new int[] { 20, 70 };
		icePlains.minSnowMax = new int[] { 40, 90 };
		biomeMapping.put(Biome.ICE_PLAINS, icePlains);

		IslandConfig iceMountains = extremeHills.clone();
		iceMountains.minSnowMin = new int[] { 20, 70 };
		iceMountains.minSnowMax = new int[] { 40, 90 };
		biomeMapping.put(Biome.ICE_MOUNTAINS, iceMountains);

		IslandConfig forestHills = hills.clone();
		biomeMapping.put(Biome.FOREST_HILLS, forestHills);

		IslandConfig ocean = grasslands.clone();
		biomeMapping.put(Biome.OCEAN, ocean);
		ocean.bigLakesSizeBase = 10;
		ocean.bigLakesSizeRand = 6;
		ocean.bigLakes = true;

		IslandConfig iceocean = ocean.clone();
		iceocean.minSnowMin = new int[] { 20, 70 };
		iceocean.minSnowMax = new int[] { 40, 90 };
		biomeMapping.put(Biome.FROZEN_OCEAN, iceocean);

		IslandConfig desert = grasslands.clone();
		desert.coating = desert.topCoating = Material.SAND;
		desert.lowerCoating = Material.SANDSTONE;
		biomeMapping.put(Biome.DESERT, desert);

		IslandConfig desertHills = hills.clone();
		desertHills.coating = desertHills.topCoating = Material.SAND;
		desertHills.lowerCoating = Material.SANDSTONE;
		biomeMapping.put(Biome.DESERT_HILLS, desertHills);

		IslandConfig iceDesert = desert.clone();
		iceDesert.minSnowMin = new int[] { 20, 70 };
		iceDesert.minSnowMax = new int[] { 40, 90 };
		biomeMapping.put(Biome.ICE_DESERT, iceDesert);

		IslandConfig swampLands = new IslandConfig();
		swampLands.bigLakes = true;
		swampLands.bigLakesDepth = 2;
		biomeMapping.put(Biome.SWAMPLAND, swampLands);

		IslandConfig river = new IslandConfig();
		river.bigLakes = true;
		biomeMapping.put(Biome.RIVER, river);

		IslandConfig snowyRiver = river.clone();
		snowyRiver.minSnowMin = new int[] { 20, 70 };
		snowyRiver.minSnowMax = new int[] { 40, 90 };
		biomeMapping.put(Biome.FROZEN_RIVER, snowyRiver);
	}

	public static IslandConfig forBiome(Biome biome) {
		IslandConfig mapping = biomeMapping.get(biome);
		return mapping != null ? mapping : defaultIslandConfig;
	}

	public int dirtMax = 7;
	public int dirtMin = 4;
	public int[] hillMin = { 0, 2 };
	public int[] hillMax = { 2, 5 };

	public int extSpikeMin = -4;
	public int extSpikeMax = 4;
	public int rootSpikeMin = -8;
	public int rootSpikeMax = 8;
	public int minStoneMinThickness = 5;
	public int maxStoneMinThickness = 7;

	// Island Size Settings
	public int[] baseMin = { 5, 35, 50, 60 };// { 5, 15, 20, 35, 50, 65, 80 };
	public int[] baseMax = { 15, 50, 80, 85 };// { 15, 30, 50, 50, 80, 90, 100
												// };
	public double[] islandRarityMin = { 0D, .25D, .5D, .75D };// { 0D, .1D,
																// .25D, .45D,
																// .7D, .85D,
																// 95D };
	public double[] islandRarityMax = { 0.15D, 0.4D, .65D, .9D };// { .05D, .2D,
																	// .375D,
																	// .65D,
																	// .8D, 9D,
																	// 1D };

	public double islandSizeMin = 0.001D;
	public double islandSizeMax = 0.003D;

	public double hillNoise = 0.1D;

	public int[] minSnowMin = { 128 };
	public int[] minSnowMax = { 128 };

	public Material coating = Material.DIRT;
	public Material topCoating = Material.GRASS;
	public Material lowerCoating = Material.DIRT;

	public boolean bigLakes = false;
	public int bigLakesSizeBase = 7;
	public int bigLakesSizeRand = 5;
	public int bigLakesDepth = 4;
	public int bigLakeHeightClamp = 1;

	@Override
	public IslandConfig clone() {
		IslandConfig clone = new IslandConfig();
		for (Field f : IslandConfig.class.getFields()) {
			try {
				f.set(clone, f.get(this));
			} catch (Exception e) {
			}
		}
		return clone;
	}
}
