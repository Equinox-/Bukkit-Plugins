package com.pi.bukkit.worldgen.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.server.World;

class WorldGenVillageStart extends StructureStart {
	private boolean c = false;

	public WorldGenVillageStart(World paramWorld,
			Random paramRandom, int paramInt1, int paramInt2,
			int paramInt3) {
		int i = paramInt3;
		ArrayList<WorldGenVillagePieceWeight> localArrayList1 =
				WorldGenVillagePieces.a(paramRandom, i);

		WorldGenVillageStartPiece localWorldGenVillageStartPiece =
				new WorldGenVillageStartPiece(
						paramWorld.getWorldChunkManager(), 0,
						paramRandom, (paramInt1 << 4) + 2,
						(paramInt2 << 4) + 2, localArrayList1, i);
		this.a.add(localWorldGenVillageStartPiece);
		localWorldGenVillageStartPiece.a(
				localWorldGenVillageStartPiece, this.a,
				paramRandom);

		ArrayList localArrayList2 =
				localWorldGenVillageStartPiece.f;
		ArrayList localArrayList3 =
				localWorldGenVillageStartPiece.e;
		int j;
		Object localObject;
		while ((!localArrayList2.isEmpty())
				|| (!localArrayList3.isEmpty())) {
			if (!localArrayList2.isEmpty()) {
				j = paramRandom.nextInt(localArrayList2.size());
				localObject =
						(StructurePiece) localArrayList2
								.remove(j);
				((StructurePiece) localObject).a(
						localWorldGenVillageStartPiece, this.a,
						paramRandom);
				continue;
			}
			j = paramRandom.nextInt(localArrayList3.size());
			localObject =
					(StructurePiece) localArrayList3.remove(j);
			((StructurePiece) localObject).a(
					localWorldGenVillageStartPiece, this.a,
					paramRandom);
		}

		d();

		j = 0;
		for (StructurePiece localStructurePiece : this.a) {
			if (!(localStructurePiece instanceof WorldGenVillageRoadPiece)) {
				j++;
			}
		}
		this.c = (j > 2);
	}

	@Override
	public boolean a() {
		return this.c;
	}
}

/*
 * Location:
 * C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name: net.minecraft.server.WorldGenVillageStart JD-Core Version:
 * 0.6.0
 */