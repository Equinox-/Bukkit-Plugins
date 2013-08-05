package com.pi.bukkit.worldgen.village;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.WorldChunkManager;

public class WorldGenVillageStartPiece extends
		WorldGenVillageWell {
	public WorldChunkManager a;
	public int b;
	public WorldGenVillagePieceWeight c;
	public ArrayList<WorldGenVillagePieceWeight> d;
	public ArrayList<StructurePiece> e = new ArrayList<StructurePiece>();
	public ArrayList<StructurePiece> f = new ArrayList<StructurePiece>();

	public WorldGenVillageStartPiece(
			WorldChunkManager paramWorldChunkManager,
			int paramInt1, Random paramRandom, int paramInt2,
			int paramInt3, ArrayList paramArrayList,
			int paramInt4) {
		super(0, paramRandom, paramInt2, paramInt3);
		this.a = paramWorldChunkManager;
		this.d = paramArrayList;
		this.b = paramInt4;
	}

	public WorldChunkManager a() {
		return this.a;
	}
}

/*
 * Location:
 * C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name: net.minecraft.server.WorldGenVillageStartPiece JD-Core
 * Version: 0.6.0
 */