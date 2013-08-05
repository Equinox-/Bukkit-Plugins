package com.pi.villa.gen;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.pi.BoundingBox;
import com.pi.LocationMath;
import com.pi.villa.gen.room.Room;
import com.pi.villa.gen.room.Rooms;
import com.pi.villa.gen.room.Rooms.RoomData;

public class Village {
	private static final int STARTING_VILLAGE_RADIUS = 50;
	private List<PositionedRoom> rooms =
			new ArrayList<PositionedRoom>();
	private BoundingBox bb;

	public Village(PositionedRoom start) {
		rooms.add(start);
		bb =
				new BoundingBox(
						start.getX()
								+ (start.getRoom().getWidth() / 2)
								- STARTING_VILLAGE_RADIUS,
						0,
						start.getZ()
								+ (start.getRoom().getDepth() / 2)
								- STARTING_VILLAGE_RADIUS,
						start.getX()
								+ (start.getRoom().getWidth() / 2)
								+ STARTING_VILLAGE_RADIUS,
						128,
						start.getZ()
								+ (start.getRoom().getDepth() / 2)
								+ STARTING_VILLAGE_RADIUS);
	}

	public void doExpansion(World w) {
		List<PositionedRoom> possible =
				new ArrayList<PositionedRoom>();
		for (RoomData r : Rooms.iterator()) {
			possible = getRoomPositions(w, r, possible);
		}
		if (possible.size() > 0) {
			PositionedRoom pR =
					possible.get((int) (Math.random() * possible
							.size()));
			if (pR != null) {
				System.out.println(pR.getRoom().getClass()
						.getSimpleName());
				rooms.add(pR);
				pR.generate(w);
			}
		}
	}

	public List<PositionedRoom> getRoomPositions(World w,
			RoomData roomDat, List<PositionedRoom> possible) {
		for (Orientation o : roomDat.getOrientations()) {
			Room tR = roomDat.createInstance(o);
			for (int x = bb.getX1(); x <= bb.getX2()
					- tR.getWidth(); x++) {
				for (int z = bb.getZ1(); z <= bb.getZ2()
						- tR.getDepth(); z++) {
					PositionedRoom tmp =
							new PositionedRoom(tR, x,
									w.getHighestBlockYAt(x, z),
									z);
					if (checkRoomPosition(tmp)) {
						tmp.averageWorldHeight(w, bb.getY1(),
								bb.getY2());
						possible.add(tmp);
					}
				}
			}
		}
		return possible;
	}

	public boolean isVillageAt(int x, int z) {
		for (PositionedRoom r : rooms) {
			if (r.contains(x, r.getY(), z)) {
				return true;
			}
		}
		return false;
	}

	private int getCurrentRoomCount(Class<? extends Room> clazz) {
		int count = 0;
		for (PositionedRoom r : rooms) {
			if (r.getRoom().getClass() == clazz) {
				count++;
			}
		}
		return count;
	}

	public PositionedRoom getRoomAt(int x, int y, int z) {
		for (PositionedRoom r : rooms) {
			if (r.contains(x, y, z)) {
				return r;
			}
		}
		return null;
	}

	public int getDistanceFrom(Class<? extends Room> rClass,
			PositionedRoom r) {
		int minDist = Integer.MAX_VALUE;
		for (PositionedRoom sR : rooms) {
			if (sR.getRoom().getClass() == rClass) {
				double dist =
						LocationMath.distance(r.getX(),
								r.getY(), r.getZ(), sR.getX(),
								sR.getY(), sR.getZ());
				if (dist < minDist) {
					minDist = (int) dist;
				}
			}
		}
		return minDist;
	}

	public boolean checkRoomPosition(PositionedRoom sR) {
		if (sR.intersects(bb)) {
			for (PositionedRoom pR : rooms) {
				if (sR.intersects(pR)) {
					return false;
				}
			}
			return true;
		}
		System.out.println(sR.getRoom().getClass()
				.getSimpleName()
				+ ": isn't in the bounding box");
		System.out.println(bb.toString() + "&" + sR.getX() + ","
				+ sR.getY() + "," + sR.getZ());
		return false;
	}
}
