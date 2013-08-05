package com.pi.villa.gen.room;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Step;

import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.Side;

public class RoomFountain extends Room {
	public static final int MAX_DISTANCE_FROM_CENTER = 50;
	public static final int MAX_ROOM_COUNT = 3;
	public static final float RARITY = 0.5f;
	public static final Map<Class<? extends Room>, Integer> DISTANCE_FROM_MAPPING =
			new HashMap<Class<? extends Room>, Integer>();

	static {
		DISTANCE_FROM_MAPPING.put(RoomFountain.class, 50);
	}

	private final static int FOUNTAIN_SIZE = 3;
	private final static int PATH_WIDTH = 2;
	private final static int FOUNTAIN_HEIGHT = 1;
	private static int SIZE = FOUNTAIN_SIZE + 2
			+ (PATH_WIDTH * 2);

	private final MaterialData FOUNTAIN_BOTTOM =
			new MaterialData(Material.STONE);
	private final MaterialData FOUNTAIN_BORDER = new Step(
			Material.STONE);
	private final MaterialData FOUNTAIN_PATH = new MaterialData(
			Material.GRAVEL);
	private final MaterialData FOUNTAIN_BASE = new MaterialData(
			Material.STONE);
	private final MaterialData FOUNTAIN_LIQUID =
			new MaterialData(Material.WATER);

	public RoomFountain(Orientation o) {
		super(SIZE, FOUNTAIN_HEIGHT + 2, SIZE, o);

		for (Side s : Side.values()) {
			addPathNode(s, SIZE / 2, 0, RoomType.Path, true);
		}
	}

	@Override
	public void generate(World w) {
		// Path Border
		for (int i = 0; i < SIZE; i++) {
			for (int o = 0; o < PATH_WIDTH; o++) {
				getBlockInRoom(w, o, 0, i).setTypeIdAndData(
						FOUNTAIN_PATH.getItemTypeId(),
						FOUNTAIN_PATH.getData(), false);
				getBlockInRoom(w, SIZE - o - 1, 0, i)
						.setTypeIdAndData(
								FOUNTAIN_PATH.getItemTypeId(),
								FOUNTAIN_PATH.getData(), false);
				getBlockInRoom(w, i, 0, o).setTypeIdAndData(
						FOUNTAIN_PATH.getItemTypeId(),
						FOUNTAIN_PATH.getData(), false);
				getBlockInRoom(w, i, 0, SIZE - o - 1)
						.setTypeIdAndData(
								FOUNTAIN_PATH.getItemTypeId(),
								FOUNTAIN_PATH.getData(), false);
			}
		}
		// Fountain Border
		for (int i = PATH_WIDTH; i < SIZE - PATH_WIDTH; i++) {
			getBlockInRoom(w, PATH_WIDTH, 1, i)
					.setTypeIdAndData(
							FOUNTAIN_BORDER.getItemTypeId(),
							FOUNTAIN_BORDER.getData(), false);
			getBlockInRoom(w, SIZE - PATH_WIDTH - 1, 1, i)
					.setTypeIdAndData(
							FOUNTAIN_BORDER.getItemTypeId(),
							FOUNTAIN_BORDER.getData(), false);
			getBlockInRoom(w, i, 1, PATH_WIDTH)
					.setTypeIdAndData(
							FOUNTAIN_BORDER.getItemTypeId(),
							FOUNTAIN_BORDER.getData(), false);
			getBlockInRoom(w, i, 1, SIZE - PATH_WIDTH - 1)
					.setTypeIdAndData(
							FOUNTAIN_BORDER.getItemTypeId(),
							FOUNTAIN_BORDER.getData(), false);
		}
		// Fountain Base
		for (int x = PATH_WIDTH; x < SIZE - PATH_WIDTH; x++) {
			for (int z = PATH_WIDTH; z < SIZE - PATH_WIDTH; z++) {
				getBlockInRoom(w, x, 0, z).setTypeIdAndData(
						FOUNTAIN_BOTTOM.getItemTypeId(),
						FOUNTAIN_BOTTOM.getData(), false);
			}
		}
		if ((SIZE & 1) == 1) {
			for (int i = 0; i < FOUNTAIN_HEIGHT; i++) {
				getBlockInRoom(w, (SIZE / 2), i + 1, (SIZE / 2))
						.setTypeIdAndData(
								FOUNTAIN_BASE.getItemTypeId(),
								FOUNTAIN_BASE.getData(), false);
			}
			getBlockInRoom(w, (SIZE / 2), FOUNTAIN_HEIGHT + 1,
					(SIZE / 2)).setTypeIdAndData(
					FOUNTAIN_LIQUID.getItemTypeId(),
					FOUNTAIN_LIQUID.getData(), true);
		} else {
			for (int xO = -1; xO <= 0; xO++) {
				for (int zO = -1; zO <= 0; zO++) {
					for (int i = 0; i < FOUNTAIN_HEIGHT; i++) {
						getBlockInRoom(w, (SIZE / 2) + xO,
								i + 1, (SIZE / 2) + zO)
								.setTypeIdAndData(
										FOUNTAIN_BASE
												.getItemTypeId(),
										FOUNTAIN_BASE.getData(),
										false);
					}
					getBlockInRoom(w, (SIZE / 2) + xO,
							FOUNTAIN_HEIGHT + 1, (SIZE / 2) + zO)
							.setTypeIdAndData(
									FOUNTAIN_LIQUID
											.getItemTypeId(),
									FOUNTAIN_LIQUID.getData(),
									true);
				}
			}
		}
	}
}
