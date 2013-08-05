 package com.pi.bukkit.worldgen.village;
 
 import java.util.Random;
 
 public abstract class StructurePieceBlockSelector
 {
   protected int a;
   protected int b;
 
   public abstract void a(Random paramRandom, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
 
   public int a()
   {
     return this.a;
   }
 
   public int b() {
     return this.b;
   }
 }

/* Location:           C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name:     net.minecraft.server.StructurePieceBlockSelector
 * JD-Core Version:    0.6.0
 */