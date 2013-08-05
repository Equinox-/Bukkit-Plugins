 package com.pi.bukkit.worldgen.village;

import net.minecraft.server.WeightedRandomChoice;

 
 public class StructurePieceTreasure extends WeightedRandomChoice
 {
   public int a;
   public int b;
   public int c;
   public int e;
 
   public StructurePieceTreasure(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
   {
     super(paramInt5);
     this.a = paramInt1;
     this.b = paramInt2;
     this.c = paramInt3;
     this.e = paramInt4;
   }
 }

/* Location:           C:\Users\Westin\java-workspace\BukkitWorldGen\craftbukkit-1.2.5-R3.0.jar
 * Qualified Name:     net.minecraft.server.StructurePieceTreasure
 * JD-Core Version:    0.6.0
 */