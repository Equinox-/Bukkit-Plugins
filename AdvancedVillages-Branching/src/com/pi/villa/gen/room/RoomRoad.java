package com.pi.villa.gen.room;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class RoomRoad {
	public static final int ROAD_WIDTH = 3;
	public static final int SPUR_LENGTH = 3;
	public static final int ROAD_HEIGHT = 3;
	public static final MaterialData ROAD_MATERIAL =
			new MaterialData(Material.GRAVEL);
	public static final Material STEEP_STAIR_MATERIAL =
			Material.COBBLESTONE_STAIRS;
	public static final Material SHALLOW_STAIR_MATERIAL =
			Material.COBBLESTONE;

	public static final Map<Class<? extends Room>, Integer> ROAD_JOINT_NEAR_LIMIT =
			new HashMap<Class<? extends Room>, Integer>();

	static {
		ROAD_JOINT_NEAR_LIMIT.put(RoomRoad4Way.class, 10);
		ROAD_JOINT_NEAR_LIMIT.put(RoomRoadCorner.class, 10);
		ROAD_JOINT_NEAR_LIMIT.put(RoomRoadTee.class, 10);
	}
}
