package com.pi;

import org.bukkit.World;
import org.bukkit.material.MaterialData;

public class BoundingBox {
	private int x1, y1, z1;
	private int x2, y2, z2;

	public BoundingBox(int x1, int y1, int z1, int x2, int y2,
			int z2) {
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}

	public boolean intersects(BoundingBox b, int xOff, int yOff,
			int zOff) {
		return (b.x2 + xOff >= x1 && b.x1 + xOff <= x2)
				&& (b.y2 + yOff >= y1 && b.y1 + yOff <= y2)
				&& (b.z2 + zOff >= z1 && b.z1 + zOff <= z2);
	}

	public boolean contains(int bX, int bY, int bZ) {
		return bX >= x1 && bY >= y1 && bZ >= z1 && bX <= x2
				&& bY <= y2 && bZ <= z2;
	}

	public void fill(World w, int baseX, int baseY, int baseZ,
			MaterialData mat) {
		for (int rX = baseX + x1; rX <= baseX + x2; rX++) {
			for (int rY = baseY + y1; rY <= baseY + y2; rY++) {
				for (int rZ = baseZ + z1; rZ <= baseZ + z2; rZ++) {
					w.getBlockAt(rX, rY, rZ).setTypeIdAndData(
							mat.getItemTypeId(), mat.getData(),
							false);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "[" + x1 + "," + y1 + "," + z1 + "=>" + x2 + ","
				+ y2 + "," + z2 + "]";
	}
}
