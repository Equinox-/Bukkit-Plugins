package com.pi.villa.gen;

import org.bukkit.World;

import com.pi.BoundingBox;
import com.pi.villa.gen.room.Room;
import com.pi.villa.gen.room.RoomJoint;

public class PositionedRoom {
	private Room r;
	private int baseX, baseY, baseZ;

	public PositionedRoom(final Room sR, final int sX,
			final int sY, final int sZ) {
		this.r = sR;
		this.baseX = sX;
		this.baseY = sY;
		this.baseZ = sZ;
	}

	public void generate(World w) {
		r.generate(w, baseX, baseY, baseZ);
	}

	public boolean contains(int sX, int sY, int sZ) {
		int tX = sX - baseX;
		int tY = sY - baseY;
		int tZ = sZ - baseZ;
		return r.getMainBoundingBox().contains(tX, tY, tZ);
	}

	public boolean intersects(PositionedRoom pR) {
		int xOff = pR.baseX - baseX;
		int yOff = pR.baseY - baseY;
		int zOff = pR.baseZ - baseZ;
		return r.getMainBoundingBox().intersects(
				pR.r.getMainBoundingBox(), xOff, yOff, zOff);
	}

	public boolean intersects(BoundingBox world) {
		return r.getMainBoundingBox().intersects(world, -baseX,
				-baseY, -baseZ);
	}

	public Room getRoom() {
		return r;
	}

	public int getX() {
		return baseX;
	}

	public int getY() {
		return baseY;
	}

	public int getZ() {
		return baseZ;
	}

	public int getPathNodeX(RoomJoint ent) {
		return r.getPathNodeX(ent) + baseX;
	}

	public int getPathNodeZ(RoomJoint ent) {
		return r.getPathNodeZ(ent) + baseZ;
	}

	public void averageWorldHeight(World w, int minY, int maxY) {
		int y = 0;
		int count = 0;
		for (int x = baseX; x < baseX + r.getWidth(); x++) {
			for (int z = baseZ; z < baseZ + r.getDepth(); z++) {
				int tY = w.getHighestBlockYAt(x, z);
				if (tY < minY) {
					y += minY;
				} else if (tY > maxY) {
					y += maxY;
				} else {
					y += tY;
				}
				count++;
			}
		}
		baseY = y / count;
	}

	@Override
	public String toString() {
		return r.getClass().getSimpleName();
	}
}
