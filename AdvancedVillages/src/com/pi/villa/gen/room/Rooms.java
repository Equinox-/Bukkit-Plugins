package com.pi.villa.gen.room;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pi.villa.gen.Orientation;

public class Rooms {
	public static List<RoomData> rooms =
			new ArrayList<RoomData>();

	static {
		register(RoomFountain.class,
				RoomFountain.MAX_DISTANCE_FROM_CENTER,
				RoomFountain.MAX_ROOM_COUNT,
				RoomFountain.DISTANCE_FROM_MAPPING,
				RoomFountain.RARITY, RoomType.Building,
				Orientation.ROT_0);
	}

	public static Iterable<RoomData> iterator() {
		return rooms;
	}

	public static void register(Class<? extends Room> clazz,
			int maxDistanceFromCenter, int maxCount,
			Map<Class<? extends Room>, Integer> minDistanceFrom,
			float rarity, int roomType,
			Orientation... orientations) {
		rooms.add(new RoomData(clazz, maxDistanceFromCenter,
				maxCount, minDistanceFrom, rarity, roomType,
				orientations));
	}

	public static RoomData getRoomData(Room r) {
		for (RoomData rD : rooms) {
			if (rD.clazz == r.getClass()) {
				return rD;
			}
		}
		return null;
	}

	public static class RoomData {
		private final Class<? extends Room> clazz;
		private final int maxDistanceFromCenter;
		private final int maxCount;
		private final float rarity;
		private final Orientation[] orientations;
		private final Map<Class<? extends Room>, Integer> minDistanceFrom;
		private final int roomType;

		private RoomData(Class<? extends Room> clazz,
				int maxDistance, int maxCount,
				Map<Class<? extends Room>, Integer> minDistFrom,
				float rarity, int type, Orientation[] sOrient) {
			this.clazz = clazz;
			this.maxDistanceFromCenter = maxDistance;
			this.maxCount = maxCount;
			this.rarity = rarity;
			this.orientations = sOrient;
			this.minDistanceFrom = minDistFrom;
			this.roomType = type;
		}

		public Room createInstance(Orientation o) {
			try {
				Constructor<? extends Room> c =
						clazz.getConstructor(Orientation.class);
				return c.newInstance(o);
			} catch (Exception e) {
				return null;
			}
		}

		public int getMaximumDistanceFromCenter() {
			return maxDistanceFromCenter;
		}

		public int getMaximumRoomCount() {
			return maxCount;
		}

		public float getRarity() {
			return rarity;
		}

		public Class<? extends Room> getRoomClass() {
			return clazz;
		}

		public Orientation[] getOrientations() {
			return orientations;
		}

		public Map<Class<? extends Room>, Integer> getMinimumDistanceFrom() {
			return minDistanceFrom;
		}

		public int getRoomType() {
			return roomType;
		}
	}
}
