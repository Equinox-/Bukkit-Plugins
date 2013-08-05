package com.pi.villa.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.World;

import com.pi.Filter;
import com.pi.LocationMath;
import com.pi.villa.gen.room.Room;
import com.pi.villa.gen.room.RoomJoint;
import com.pi.villa.gen.room.Rooms;
import com.pi.villa.gen.room.Rooms.RoomData;

public class Village implements Filter<PositionedRoom> {
	private List<PositionedRoom> rooms =
			new ArrayList<PositionedRoom>();
	private List<Integer> openRooms = new ArrayList<Integer>();
	private Map<Class<? extends Room>, Integer> roomCounts =
			new HashMap<Class<? extends Room>, Integer>();

	public Village(PositionedRoom start) {
		rooms.add(start);
		openRooms.add(0);
		incrementRoom(start.getRoom().getClass());
	}

	public void doExpansion(World w) {
		int startSize = rooms.size();
		List<Integer> newOpen = new ArrayList<Integer>();
		for (Integer i : openRooms) {
			if (i < 0 || i >= startSize) {
				continue;
			}
			PositionedRoom openRoom = rooms.get(i);
			for (RoomJoint side : openRoom.getRoom()
					.getPathNodes()) {
				List<PositionedRoom> possibleRooms =
						PathConnector.getConnectableRooms(
								openRoom, side, this);
				if (possibleRooms.size() > 0) {
					PositionedRoom tRoom =
							possibleRooms.get((int) Math
									.floor(Math.random()
											* possibleRooms
													.size()));
					if (tRoom != null) {
						System.out.println("Add room: "
								+ tRoom.getRoom().getClass()
										.getSimpleName());
						tRoom.generate(w);
						newOpen.add(rooms.size());
						rooms.add(tRoom);
						incrementRoom(tRoom.getRoom().getClass());
					}
				}
			}
		}
		openRooms = newOpen;
	}

	public boolean isVillageAt(int x, int z) {
		for (PositionedRoom r : rooms) {
			if (r.contains(x, r.getY(), z)) {
				return true;
			}
		}
		return false;
	}

	private void incrementRoom(Class<? extends Room> clazz) {
		roomCounts.put(clazz, getCurrentRoomCount(clazz) + 1);
	}

	private int getCurrentRoomCount(Class<? extends Room> clazz) {
		Integer curr = roomCounts.get(clazz);
		if (curr == null) {
			curr = 0;
		}
		return curr.intValue();
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

	@Override
	public int accept(PositionedRoom t) {
		RoomData rD = Rooms.getRoomData(t.getRoom());
		if (rD != null) {
			if (rD.getMaximumRoomCount() != -1
					&& getCurrentRoomCount(t.getRoom()
							.getClass()) >= rD
							.getMaximumRoomCount()) {
				return 0;
			}
			PositionedRoom base = rooms.get(0);
			if (rD.getMaximumDistanceFromCenter() != -1) {
				double dist =
						LocationMath.distance(base.getX(),
								base.getY(), base.getZ(),
								t.getX(), t.getY(), t.getZ());
				if (dist > rD.getMaximumDistanceFromCenter()) {
					return 0;
				}
			}
			for (Entry<Class<? extends Room>, Integer> minDist : rD
					.getMinimumDistanceFrom().entrySet()) {
				int actDist =
						getDistanceFrom(minDist.getKey(), t);
				if (actDist < minDist.getValue()) {
					return 0;
				}
			}
		}
		boolean[] goodJoints =
				new boolean[t.getRoom().getPathNodes().size()];

		int linkedNodes = 0;
		for (PositionedRoom r : rooms) {
			if (r.intersects(t)) {
				return 0;
			}
			// Now check if we are connecting up to this room... shouldn't
			// be doing that :p
			for (int i = 0; i < t.getRoom().getPathNodes()
					.size(); i++) {
				RoomJoint node =
						t.getRoom().getPathNodes().get(i);
				int nodeX =
						t.getPathNodeX(node)
								+ node.side.getXOff();
				int nodeZ =
						t.getPathNodeZ(node)
								+ node.side.getZOff();
				if (r.contains(nodeX, node.y + t.getY(), nodeZ)) {
					// However if the node is correct, we should
					boolean goodNode = false;
					for (RoomJoint o : r.getRoom()
							.getPathNodes()) {
						if (o.side == node.side.getInverse()) {
							if (r.getPathNodeX(o) == nodeX
									&& r.getPathNodeZ(o) == nodeZ) {
								goodJoints[i] = true;
								linkedNodes++;
								goodNode = true;
							}
						}
					}
					if (!goodNode) {
						return 0;
					}
				}
			}
		}

		// The last test. If we build this, can we build off of it again, from
		// all the nodes?
		if (expansionTest) {
			expansionTest = false;
			rooms.add(t);
			for (int i = 0; i < t.getRoom().getPathNodes()
					.size(); i++) {
				RoomJoint entry =
						t.getRoom().getPathNodes().get(i);
				if (!goodJoints[i] && entry.required) {
					List<PositionedRoom> pRooms =
							PathConnector.getConnectableRooms(t,
									entry, this);
					if (pRooms.size() == 0) {
						linkedNodes = 0;
						break;
					}
				}
			}
			rooms.remove(rooms.size() - 1);
			expansionTest = true;
		}
		return linkedNodes;
	}

	private boolean expansionTest = true;
}
