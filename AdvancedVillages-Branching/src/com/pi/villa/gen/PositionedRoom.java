package com.pi.villa.gen;

import java.util.BitSet;

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
		if (!r.getMainBoundingBox().contains(tX, tY, tZ)) {
			return false;
		} else if (r.getBoundingBoxes().length == 0) {
			return true;
		}
		for (BoundingBox b : r.getBoundingBoxes()) {
			if (b.contains(tX, tY, tZ)) {
				return true;
			}
		}
		return false;
	}

	public boolean intersects(PositionedRoom pR) {
		int xOff = pR.baseX - baseX;
		int yOff = pR.baseY - baseY;
		int zOff = pR.baseZ - baseZ;
		if (!r.getMainBoundingBox().intersects(
				pR.r.getMainBoundingBox(), xOff, yOff, zOff)) {
			return false;
		} else if (r.getBoundingBoxes().length == 0
				&& pR.r.getBoundingBoxes().length == 0) {
			return true;
		}
		if (r.getBoundingBoxes().length == 0) {
			for (BoundingBox bB : pR.r.getBoundingBoxes()) {
				if (r.getMainBoundingBox().intersects(bB, xOff,
						yOff, zOff)) {
					return true;
				}
			}
			return false;
		} else if (pR.r.getBoundingBoxes().length == 0) {
			for (BoundingBox bB : r.getBoundingBoxes()) {
				if (bB.intersects(pR.r.getMainBoundingBox(),
						xOff, yOff, zOff)) {
					return true;
				}
			}
			return false;
		} else {
			for (BoundingBox bA : r.getBoundingBoxes()) {
				for (BoundingBox bB : pR.r.getBoundingBoxes()) {
					if (bA.intersects(bB, xOff, yOff, zOff)) {
						return true;
					}
				}
			}
			return false;
		}
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

	@Override
	public String toString() {
		return r.getClass().getSimpleName();
	}
}
