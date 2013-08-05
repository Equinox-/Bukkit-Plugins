package com.pi;

import org.bukkit.World;
import org.bukkit.material.MaterialData;

import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.PositionedRoom;
import com.pi.villa.gen.room.RoomFountain;

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
		if ((b.x2 + xOff >= x1 && b.x1 + xOff <= x2)
				&& (b.y2 + yOff >= y1 && b.y1 + yOff <= y2)
				&& (b.z2 + zOff >= z1 && b.z1 + zOff <= z2)) {
			return true;// Actually collide
		}
		if ((b.x1 + xOff >= x1 && b.x2 + xOff <= x2)
				&& (b.y1 + yOff >= y1 && b.y2 + yOff <= y2)
				&& (b.z1 + zOff >= z1 && b.z2 + zOff <= z2)) {
			return true;// b is inside this one
		}
		if ((b.x1 + xOff <= x1 && b.x2 + xOff >= x2)
				&& (b.y1 + yOff <= y1 && b.y2 + yOff >= y2)
				&& (b.z1 + zOff <= z1 && b.z2 + zOff >= z2)) {
			return true;// this one is inside b
		}
		return false;
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

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getZ1() {
		return z1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public int getZ2() {
		return z2;
	}

	@Override
	public String toString() {
		return "[" + x1 + "," + y1 + "," + z1 + "=>" + x2 + ","
				+ y2 + "," + z2 + "]";
	}
}
