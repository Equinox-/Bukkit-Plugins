package com.pi.bukkit.worldgen.village;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenBase;

public abstract class StructureGenerator extends WorldGenBase {
	protected HashMap<Long, StructureStart> e =
			new HashMap<Long, StructureStart>();

	@Override
	public void a(IChunkProvider paramIChunkProvider,
			World paramWorld, int paramInt1, int paramInt2,
			byte[] paramArrayOfByte) {
		super.a(paramIChunkProvider, paramWorld, paramInt1,
				paramInt2, paramArrayOfByte);
	}

	@Override
	protected void a(World paramWorld, int paramInt1,
			int paramInt2, int paramInt3, int paramInt4,
			byte[] paramArrayOfByte) {
		if (this.e.containsKey(Long.valueOf(ChunkCoordIntPair.a(
				paramInt1, paramInt2)))) {
			return;
		}

		this.c.nextInt();
		if (a(paramInt1, paramInt2)) {
			StructureStart localStructureStart =
					b(paramInt1, paramInt2);
			this.e.put(Long.valueOf(ChunkCoordIntPair.a(
					paramInt1, paramInt2)), localStructureStart);
		}
	}

	public boolean a(World paramWorld, Random paramRandom,
			int paramInt1, int paramInt2) {
		int i = (paramInt1 << 4) + 8;
		int j = (paramInt2 << 4) + 8;

		boolean k = false;
		for (StructureStart localStructureStart : this.e
				.values()) {
			if ((localStructureStart.a())
					&& (localStructureStart.b().a(i, j, i + 15,
							j + 15))) {
				localStructureStart.a(paramWorld, paramRandom,
						new StructureBoundingBox(i, j, i + 15,
								j + 15));
				k = true;
				break;
			}
		}
		return k;
	}

	public boolean a(int paramInt1, int paramInt2, int paramInt3) {
		for (StructureStart localStructureStart : this.e
				.values()) {
			if ((localStructureStart.a())
					&& (localStructureStart.b().a(paramInt1,
							paramInt3, paramInt1, paramInt3))) {
				Iterator<StructurePiece> localIterator2 =
						localStructureStart.c().iterator();
				while (localIterator2.hasNext()) {
					StructurePiece localStructurePiece =
							(StructurePiece) localIterator2
									.next();
					if (localStructurePiece.b().b(paramInt1,
							paramInt2, paramInt3)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public ChunkPosition getNearestGeneratedFeature(
			World paramWorld, int paramInt1, int paramInt2,
			int paramInt3) {
		this.d = paramWorld;

		this.c.setSeed(paramWorld.getSeed());
		long l1 = this.c.nextLong();
		long l2 = this.c.nextLong();
		long l3 = (paramInt1 >> 4) * l1;
		long l4 = (paramInt3 >> 4) * l2;
		this.c.setSeed(l3 ^ l4 ^ paramWorld.getSeed());

		a(paramWorld, paramInt1 >> 4, paramInt3 >> 4, 0, 0, null);

		double d1 = 1.7976931348623157E+308D;
		ChunkPosition localObject1 = null;

		for (Iterator<StructureStart> localObject2 =
				this.e.values().iterator(); localObject2
				.hasNext();) {
			StructureStart localObject3 = localObject2.next();
			if (localObject3.a()) {
				StructurePiece localObject4 =
						localObject3.c().get(0);
				ChunkPosition localChunkPosition =
						localObject4.b_();

				int i = localChunkPosition.x - paramInt1;
				int j = localChunkPosition.y - paramInt2;
				int k = localChunkPosition.z - paramInt3;
				int d2 = i + i * j * j + k * k;

				if (d2 < d1) {
					d1 = d2;
					localObject1 = localChunkPosition;
				}
			}
		}
		ChunkPosition localObject3;
		int i;
		int j;
		int k;
		double d2;
		if (localObject1 != null) {
			return localObject1;
		}
		List<ChunkPosition> localObject2 = a();
		if (localObject2 != null) {
			localObject3 = null;
			for (ChunkPosition localChunkPosition : localObject2) {

				i = localChunkPosition.x - paramInt1;
				j = localChunkPosition.y - paramInt2;
				k = localChunkPosition.z - paramInt3;
				d2 = i + i * j * j + k * k;
				if (d2 < d1) {
					d1 = d2;
					localObject3 = localChunkPosition;
				}
			}
			return localObject3;
		}

		return null;
	}

	protected List<ChunkPosition> a() {
		return null;
	}

	protected abstract boolean a(int paramInt1, int paramInt2);

	protected abstract StructureStart b(int paramInt1,
			int paramInt2);

}

/*
 * Location:
 * C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name: net.minecraft.server.StructureGenerator JD-Core Version:
 * 0.6.0
 */