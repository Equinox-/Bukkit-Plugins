package com.pi.villa.gen.room;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;

import com.pi.BoundingBox;
import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.Side;

public class RoomRoadTee extends Room {
	public static final int MAX_DISTANCE_FROM_CENTER = -1;
	public static final int MAX_ROOM_COUNT = -1;
	public static final float RARITY = 0.25f;

	private final BoundingBox[] boundingBoxes;
	private final MaterialData roadMaterial = new MaterialData(
			Material.GRAVEL);

	public RoomRoadTee(Orientation o) {
		super(
				RoomRoad.ROAD_WIDTH + RoomRoad.SPUR_LENGTH,
				1,
				RoomRoad.ROAD_WIDTH + (RoomRoad.SPUR_LENGTH * 2),
				o);

		addPathNode(Side.Z_MINUS, RoomRoad.ROAD_WIDTH / 2, 0,
				RoomType.Path, true);
		addPathNode(Side.X_PLUS, RoomRoad.SPUR_LENGTH
				+ (RoomRoad.ROAD_WIDTH / 2), 0, RoomType.Path,
				true);
		addPathNode(Side.Z_PLUS, RoomRoad.ROAD_WIDTH / 2, 0,
				RoomType.Path, true);

		boundingBoxes = new BoundingBox[2];
		boundingBoxes[0] =
				createBoundingBox(0, 0, 0, RoomRoad.ROAD_WIDTH,
						1, (RoomRoad.SPUR_LENGTH * 2)
								+ RoomRoad.ROAD_WIDTH);
		boundingBoxes[1] =
				createBoundingBox(RoomRoad.ROAD_WIDTH, 0,
						RoomRoad.SPUR_LENGTH,
						RoomRoad.SPUR_LENGTH, 1,
						RoomRoad.ROAD_WIDTH);
	}

	@Override
	public void generate(World w) {
		for (BoundingBox bB : boundingBoxes) {
			bB.fill(w, baseX, baseY, baseZ, roadMaterial);
		}
	}

	@Override
	public BoundingBox[] getBoundingBoxes() {
		return boundingBoxes;
	}
}
