package com.pi.villa.gen.room;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;

import com.pi.BoundingBox;
import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.Side;

public class RoomRoadStraight extends Room {
	public static final int MAX_DISTANCE_FROM_CENTER = -1;
	public static final int MAX_ROOM_COUNT = -1;
	public static final float RARITY = 1f;

	public RoomRoadStraight(Orientation o) {
		super(RoomRoad.ROAD_WIDTH + (RoomRoad.SPUR_LENGTH * 2),
				RoomRoad.ROAD_HEIGHT, RoomRoad.ROAD_WIDTH, o);

		addPathNode(Side.X_PLUS, RoomRoad.ROAD_WIDTH / 2, 0,
				RoomType.Path, true);
		addPathNode(Side.X_MINUS, RoomRoad.ROAD_WIDTH / 2, 0,
				RoomType.Path, true);
		addPathNode(Side.Z_MINUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0,
				RoomType.Building, false);
		addPathNode(Side.Z_PLUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0,
				RoomType.Building, false);
	}

	@Override
	public void generate(World w) {
		super.getMainBoundingBox().fill(w, baseX, baseY, baseZ,
				new MaterialData(Material.AIR));
		for (int x = 0; x <= roomWidth; x += 3) {
			for (int y = 0; y < RoomRoad.ROAD_HEIGHT - 1; y++) {
				getBlockInRoom(w, x, y, 0).setType(
						Material.FENCE);
				getBlockInRoom(w, x, y, RoomRoad.ROAD_WIDTH - 1)
						.setType(Material.FENCE);
			}
			for (int z = 0; z < RoomRoad.ROAD_WIDTH; z++) {
				getBlockInRoom(w, x, RoomRoad.ROAD_HEIGHT - 1, z)
						.setType(Material.WOOD);
			}
		}
	}

	@Override
	public BoundingBox[] getBoundingBoxes() {
		return new BoundingBox[0];
	}
}
