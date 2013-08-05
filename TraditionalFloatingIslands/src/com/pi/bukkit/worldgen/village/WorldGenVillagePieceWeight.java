package com.pi.bukkit.worldgen.village;

public class WorldGenVillagePieceWeight {
	public Class a;
	public final int b;
	public int c;
	public int d;

	public WorldGenVillagePieceWeight(Class paramClass,
			int paramInt1, int paramInt2) {
		this.a = paramClass;
		this.b = paramInt1;
		this.d = paramInt2;
	}

	public boolean a(int paramInt) {
		return (this.d == 0) || (this.c < this.d);
	}

	public boolean a() {
		return (this.d == 0) || (this.c < this.d);
	}
}

/*
 * Location:
 * C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name: net.minecraft.server.WorldGenVillagePieceWeight JD-Core
 * Version: 0.6.0
 */