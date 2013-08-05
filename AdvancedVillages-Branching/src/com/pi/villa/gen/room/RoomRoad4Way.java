package com.pi.villa.gen.room;

import org.bukkit.World;

import com.pi.BoundingBox;
import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.Side;

public class RoomRoad4Way extends Room {
	public static final int MAX_DISTANCE_FROM_CENTER = -1;
	public static final int MAX_ROOM_COUNT = -1;
	public static final float RARITY = 0.125f;

	private final BoundingBox[] boundingBoxes;

	public RoomRoad4Way(Orientation o) {
		super(RoomRoad.ROAD_WIDTH + (RoomRoad.SPUR_LENGTH * 2),
				1, RoomRoad.ROAD_WIDTH
						+ (RoomRoad.SPUR_LENGTH * 2), o);

		addPathNode(Side.Z_MINUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0, RoomType.Path,
				true);
		addPathNode(Side.X_PLUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0, RoomType.Path,
				true);
		addPathNode(Side.Z_PLUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0, RoomType.Path,
				true);
		addPathNode(Side.X_MINUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0, RoomType.Path,
				true);

		boundingBoxes = new BoundingBox[3];
		boundingBoxes[0] =
				createBoundingBox(RoomRoad.SPUR_LENGTH, 0, 0,
						RoomRoad.ROAD_WIDTH, 1,
						(RoomRoad.SPUR_LENGTH * 2)
								+ RoomRoad.ROAD_WIDTH);
		boundingBoxes[1] =
				createBoundingBox(RoomRoad.ROAD_WIDTH
						+ RoomRoad.SPUR_LENGTH, 0,
						RoomRoad.SPUR_LENGTH,
						RoomRoad.SPUR_LENGTH, 1,
						RoomRoad.ROAD_WIDTH);
		boundingBoxes[2] =
				createBoundingBox(0, 0, RoomRoad.SPUR_LENGTH,
						RoomRoad.SPUR_LENGTH, 1,
						RoomRoad.ROAD_WIDTH);
	}

	@Override
	public void generate(World w) {
		for (BoundingBox bB : boundingBoxes) {
			bB.fill(w, baseX, baseY, baseZ,
					RoomRoad.ROAD_MATERIAL);
		}
	}

	@Override
	public BoundingBox[] getBoundingBoxes() {
		return boundingBoxes;
	}
}
