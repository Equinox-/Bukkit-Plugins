package com.pi.bukkit.worldgen.village;

import java.util.List;
import java.util.Random;

import net.minecraft.server.Block;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public class WorldGenVillageBigFarm extends WorldGenVillagePiece {
	private int a = -1;

	public WorldGenVillageBigFarm(int paramInt1,
			Random paramRandom,
			StructureBoundingBox paramStructureBoundingBox,
			int paramInt2) {
		super(paramInt1);

		this.h = paramInt2;
		this.g = paramStructureBoundingBox;
	}

	public void a(StructurePiece paramStructurePiece,
			List paramList, Random paramRandom) {
	}

	public static WorldGenVillageBigFarm a(List paramList,
			Random paramRandom, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4, int paramInt5) {
		StructureBoundingBox localStructureBoundingBox =
				StructureBoundingBox.a(paramInt1, paramInt2,
						paramInt3, 0, 0, 0, 13, 4, 9, paramInt4);

		if ((!a(localStructureBoundingBox))
				|| (StructurePiece.a(paramList,
						localStructureBoundingBox) != null)) {
			return null;
		}

		return new WorldGenVillageBigFarm(paramInt5,
				paramRandom, localStructureBoundingBox,
				paramInt4);
	}

	public boolean a(World paramWorld, Random paramRandom,
			StructureBoundingBox paramStructureBoundingBox) {
		if (this.a < 0) {
			this.a = b(paramWorld, paramStructureBoundingBox);
			if (this.a < 0) {
				return true;
			}
			this.g.a(0, this.a - this.g.e + 4 - 1, 0);
		}

		a(paramWorld, paramStructureBoundingBox, 0, 1, 0, 12, 4,
				8, 0, 0, false);

		a(paramWorld, paramStructureBoundingBox, 1, 0, 1, 2, 0,
				7, Block.SOIL.id, Block.SOIL.id, false);
		a(paramWorld, paramStructureBoundingBox, 4, 0, 1, 5, 0,
				7, Block.SOIL.id, Block.SOIL.id, false);
		a(paramWorld, paramStructureBoundingBox, 7, 0, 1, 8, 0,
				7, Block.SOIL.id, Block.SOIL.id, false);
		a(paramWorld, paramStructureBoundingBox, 10, 0, 1, 11,
				0, 7, Block.SOIL.id, Block.SOIL.id, false);

		a(paramWorld, paramStructureBoundingBox, 0, 0, 0, 0, 0,
				8, Block.LOG.id, Block.LOG.id, false);
		a(paramWorld, paramStructureBoundingBox, 6, 0, 0, 6, 0,
				8, Block.LOG.id, Block.LOG.id, false);
		a(paramWorld, paramStructureBoundingBox, 12, 0, 0, 12,
				0, 8, Block.LOG.id, Block.LOG.id, false);
		a(paramWorld, paramStructureBoundingBox, 1, 0, 0, 11, 0,
				0, Block.LOG.id, Block.LOG.id, false);
		a(paramWorld, paramStructureBoundingBox, 1, 0, 8, 11, 0,
				8, Block.LOG.id, Block.LOG.id, false);

		a(paramWorld, paramStructureBoundingBox, 3, 0, 1, 3, 0,
				7, Block.WATER.id, Block.WATER.id, false);
		a(paramWorld, paramStructureBoundingBox, 9, 0, 1, 9, 0,
				7, Block.WATER.id, Block.WATER.id, false);

		for (int i = 1; i <= 7; i++) {
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 1, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 2, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 4, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 5, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 7, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 8, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 10, 1, i,
					paramStructureBoundingBox);
			a(paramWorld, Block.CROPS.id,
					MathHelper.a(paramRandom, 2, 7), 11, 1, i,
					paramStructureBoundingBox);
		}

		for (i = 0; i < 9; i++) {
			for (int j = 0; j < 13; j++) {
				b(paramWorld, j, 4, i, paramStructureBoundingBox);
				b(paramWorld, Block.DIRT.id, 0, j, -1, i,
						paramStructureBoundingBox);
			}

		}

		return true;
	}
}

/*
 * Location:
 * C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name: net.minecraft.server.WorldGenVillageBigFarm JD-Core Version:
 * 0.6.0
 */