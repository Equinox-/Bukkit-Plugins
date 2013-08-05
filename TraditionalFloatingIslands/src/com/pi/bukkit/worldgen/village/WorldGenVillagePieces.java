package com.pi.bukkit.worldgen.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MathHelper;

public class WorldGenVillagePieces {

	public WorldGenVillagePieces() {
	}

	public static ArrayList<WorldGenVillagePieceWeight> a(
			Random random, int i) {
		ArrayList<WorldGenVillagePieceWeight> arraylist =
				new ArrayList<WorldGenVillagePieceWeight>();
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageHouse.class, 4, MathHelper.a(
						random, 2 + i, 4 + i * 2)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageTemple.class, 20, MathHelper.a(
						random, 0 + i, 1 + i)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageLibrary.class, 20, MathHelper.a(
						random, 0 + i, 2 + i)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageHut.class, 3, MathHelper.a(
						random, 2 + i, 5 + i * 3)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageButcher.class, 15, MathHelper.a(
						random, 0 + i, 2 + i)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageBigFarm.class, 3, MathHelper.a(
						random, 1 + i, 4 + i)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageFarm.class, 3, MathHelper.a(
						random, 2 + i, 4 + i * 2)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageBlacksmith.class, 15, MathHelper
						.a(random, 0, 1 + i)));
		arraylist.add(new WorldGenVillagePieceWeight(
				WorldGenVillageHouse2.class, 8, MathHelper.a(
						random, 0 + i, 3 + i * 2)));
		Iterator<WorldGenVillagePieceWeight> iterator =
				arraylist.iterator();
		do {
			if (!iterator.hasNext())
				break;
			if (iterator.next().d == 0)
				iterator.remove();
		} while (true);
		return arraylist;
	}

	private static int a(
			ArrayList<WorldGenVillagePieceWeight> arraylist) {
		boolean flag = false;
		int i = 0;
		for (Iterator<WorldGenVillagePieceWeight> iterator =
				arraylist.iterator(); iterator.hasNext();) {
			WorldGenVillagePieceWeight worldgenvillagepieceweight =
					(WorldGenVillagePieceWeight) iterator.next();
			if (worldgenvillagepieceweight.d > 0
					&& worldgenvillagepieceweight.c < worldgenvillagepieceweight.d)
				flag = true;
			i += worldgenvillagepieceweight.b;
		}

		return flag ? i : -1;
	}

	private static WorldGenVillagePiece a(
			WorldGenVillagePieceWeight worldgenvillagepieceweight,
			List<StructurePiece> list, Random random, int i,
			int j, int k, int l, int i1) {
		Class class1 = worldgenvillagepieceweight.a;
		Object obj = null;
		if (class1 == WorldGenVillageHouse.class)
			obj =
					WorldGenVillageHouse.a(list, random, i, j,
							k, l, i1);
		else if (class1 == WorldGenVillageTemple.class)
			obj =
					WorldGenVillageTemple.a(list, random, i, j,
							k, l, i1);
		else if (class1 == WorldGenVillageLibrary.class)
			obj =
					WorldGenVillageLibrary.a(list, random, i, j,
							k, l, i1);
		else if (class1 == WorldGenVillageHut.class)
			obj =
					WorldGenVillageHut.a(list, random, i, j, k,
							l, i1);
		else if (class1 == WorldGenVillageButcher.class)
			obj =
					WorldGenVillageButcher.a(list, random, i, j,
							k, l, i1);
		else if (class1 == WorldGenVillageBigFarm.class)
			obj =
					WorldGenVillageBigFarm.a(list, random, i, j,
							k, l, i1);
		else if (class1 == WorldGenVillageFarm.class)
			obj =
					WorldGenVillageFarm.a(list, random, i, j, k,
							l, i1);
		else if (class1 == WorldGenVillageBlacksmith.class)
			obj =
					WorldGenVillageBlacksmith.a(list, random, i,
							j, k, l, i1);
		else if (class1 == WorldGenVillageHouse2.class)
			obj =
					WorldGenVillageHouse2.a(list, random, i, j,
							k, l, i1);
		return ((WorldGenVillagePiece) (obj));
	}

	private static WorldGenVillagePiece c(
			WorldGenVillageStartPiece paramWorldGenVillageStartPiece,
			List<StructurePiece> paramList, Random paramRandom,
			int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, int paramInt5) {
		int i = a(paramWorldGenVillageStartPiece.d);
		if (i <= 0) {
			return null;
		}

		int j = 0;
		int k;
		while (j < 5) {
			j++;

			k = paramRandom.nextInt(i);
			for (WorldGenVillagePieceWeight localWorldGenVillagePieceWeight : paramWorldGenVillageStartPiece.d) {
				k -= localWorldGenVillagePieceWeight.b;
				if (k < 0) {
					if ((!localWorldGenVillagePieceWeight
							.a(paramInt5))
							|| ((localWorldGenVillagePieceWeight == paramWorldGenVillageStartPiece.c) && (paramWorldGenVillageStartPiece.d
									.size() > 1))) {
						break;
					}
					WorldGenVillagePiece localWorldGenVillagePiece =
							a(localWorldGenVillagePieceWeight,
									paramList, paramRandom,
									paramInt1, paramInt2,
									paramInt3, paramInt4,
									paramInt5);
					if (localWorldGenVillagePiece != null) {
						localWorldGenVillagePieceWeight.c += 1;
						paramWorldGenVillageStartPiece.c =
								localWorldGenVillagePieceWeight;

						if (!localWorldGenVillagePieceWeight.a()) {
							paramWorldGenVillageStartPiece.d
									.remove(localWorldGenVillagePieceWeight);
						}
						return localWorldGenVillagePiece;
					}

				}

			}
		}
		return null;
	}

	private static StructurePiece d(
			WorldGenVillageStartPiece worldgenvillagestartpiece,
			List<StructurePiece> list, Random random, int i,
			int j, int k, int l, int i1) {
		if (i1 > 50)
			return null;
		if (Math.abs(i - worldgenvillagestartpiece.b().a) > 112
				|| Math.abs(k - worldgenvillagestartpiece.b().c) > 112)
			return null;
		WorldGenVillagePiece worldgenvillagepiece =
				c(worldgenvillagestartpiece, list, random, i, j,
						k, l, i1 + 1);
		if (worldgenvillagepiece != null) {
			int j1 =
					(((StructurePiece) (worldgenvillagepiece)).g.a + ((StructurePiece) (worldgenvillagepiece)).g.d) / 2;
			int k1 =
					(((StructurePiece) (worldgenvillagepiece)).g.c + ((StructurePiece) (worldgenvillagepiece)).g.f) / 2;
			int l1 =
					((StructurePiece) (worldgenvillagepiece)).g.d
							- ((StructurePiece) (worldgenvillagepiece)).g.a;
			int i2 =
					((StructurePiece) (worldgenvillagepiece)).g.f
							- ((StructurePiece) (worldgenvillagepiece)).g.c;
			int j2 = l1 <= i2 ? i2 : l1;
			if (worldgenvillagestartpiece.a().a(j1, k1,
					j2 / 2 + 4, WorldGenVillage.a)) {
				list.add(worldgenvillagepiece);
				worldgenvillagestartpiece.e
						.add(worldgenvillagepiece);
				return worldgenvillagepiece;
			}
		}
		return null;
	}

	private static StructurePiece e(
			WorldGenVillageStartPiece worldgenvillagestartpiece,
			List<StructurePiece> list, Random random, int i,
			int j, int k, int l, int i1) {
		if (i1 > 3 + worldgenvillagestartpiece.b)
			return null;
		if (Math.abs(i - worldgenvillagestartpiece.b().a) > 112
				|| Math.abs(k - worldgenvillagestartpiece.b().c) > 112)
			return null;
		StructureBoundingBox structureboundingbox =
				WorldGenVillageRoad.a(worldgenvillagestartpiece,
						list, random, i, j, k, l);
		if (structureboundingbox != null
				&& structureboundingbox.b > 10) {
			WorldGenVillageRoad worldgenvillageroad =
					new WorldGenVillageRoad(i1, random,
							structureboundingbox, l);
			int j1 =
					worldgenvillageroad.g.a
							+ worldgenvillageroad.g.d / 2;
			int k1 =
					worldgenvillageroad.g.c
							+ worldgenvillageroad.g.f / 2;
			int l1 =
					worldgenvillageroad.g.d
							- worldgenvillageroad.g.a;
			int i2 =
					worldgenvillageroad.g.f
							- worldgenvillageroad.g.c;
			int j2 = l1 <= i2 ? i2 : l1;
			if (worldgenvillagestartpiece.a().a(j1, k1,
					j2 / 2 + 4, WorldGenVillage.a)) {
				list.add(worldgenvillageroad);
				worldgenvillagestartpiece.f
						.add(worldgenvillageroad);
				return worldgenvillageroad;
			}
		}
		return null;
	}

	static StructurePiece a(
			WorldGenVillageStartPiece worldgenvillagestartpiece,
			List<StructurePiece> list, Random random, int i,
			int j, int k, int l, int i1) {
		return d(worldgenvillagestartpiece, list, random, i, j,
				k, l, i1);
	}

	static StructurePiece b(
			WorldGenVillageStartPiece worldgenvillagestartpiece,
			List<StructurePiece> list, Random random, int i,
			int j, int k, int l, int i1) {
		return e(worldgenvillagestartpiece, list, random, i, j,
				k, l, i1);
	}
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * C:\Users\Westin\java-workspace\BukkitPlugins\craftbukkit-1.2.5-R3.0.jar Total
 * time: 21 ms Jad reported messages/errors: Couldn't fully decompile method c
 * Exit status: 0 Caught exceptions:
 */