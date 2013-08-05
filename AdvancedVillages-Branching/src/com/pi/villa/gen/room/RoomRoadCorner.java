package com.pi.villa.gen.room;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;

import com.pi.BoundingBox;
import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.Side;

public class RoomRoadCorner extends Room {
	public static final int MAX_DISTANCE_FROM_CENTER = -1;
	public static final int MAX_ROOM_COUNT = -1;
	public static final float RARITY = 0.5f;

	private final BoundingBox[] boundingBoxes;

	public RoomRoadCorner(Orientation o) {
		super(RoomRoad.ROAD_WIDTH + RoomRoad.SPUR_LENGTH,
				RoomRoad.ROAD_HEIGHT, RoomRoad.ROAD_WIDTH
						+ RoomRoad.SPUR_LENGTH, o);

		addPathNode(Side.Z_MINUS, RoomRoad.ROAD_WIDTH / 2, 0,
				RoomType.Path, true);
		addPathNode(Side.X_PLUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0, RoomType.Path,
				true);

		boundingBoxes = new BoundingBox[2];
		boundingBoxes[0] =
				createBoundingBox(0, 0, 0, RoomRoad.ROAD_WIDTH,
						RoomRoad.ROAD_HEIGHT,
						RoomRoad.SPUR_LENGTH
								+ RoomRoad.ROAD_WIDTH);
		boundingBoxes[1] =
				createBoundingBox(RoomRoad.ROAD_WIDTH, 0,
						RoomRoad.SPUR_LENGTH,
						RoomRoad.SPUR_LENGTH,
						RoomRoad.ROAD_HEIGHT,
						RoomRoad.ROAD_WIDTH);
	}

	@Override
	public void generate(World w) {
		for (BoundingBox bB : boundingBoxes) {
			bB.fill(w, baseX, baseY, baseZ, new MaterialData(
					Material.AIR));
		}

		if (RoomRoad.SPUR_LENGTH > 0) {
			for (int y = 0; y < RoomRoad.ROAD_HEIGHT - 1; y++) {
				getBlockInRoom(w, RoomRoad.ROAD_WIDTH, y,
						RoomRoad.SPUR_LENGTH).setType(
						Material.FENCE);
				getBlockInRoom(
						w,
						RoomRoad.ROAD_WIDTH,
						y,
						RoomRoad.SPUR_LENGTH
								+ RoomRoad.ROAD_WIDTH - 1)
						.setType(Material.FENCE);
			}
			for (int z = RoomRoad.SPUR_LENGTH; z < RoomRoad.SPUR_LENGTH
					+ RoomRoad.ROAD_WIDTH; z++) {
				getBlockInRoom(w, RoomRoad.ROAD_WIDTH,
						RoomRoad.ROAD_HEIGHT - 1, z).setType(
						Material.WOOD);
			}
		}
	}

	@Override
	public BoundingBox[] getBoundingBoxes() {
		return boundingBoxes;
	}
}
