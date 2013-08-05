package com.pi.villa.gen.room;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.material.Sign;

import com.pi.BoundingBox;
import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.Side;

public abstract class Room {
	protected final int roomWidth, roomHeight, roomDepth;
	protected Orientation orientation;
	private final List<RoomJoint> pathNodes =
			new ArrayList<RoomJoint>();
	private BoundingBox mainBoundingBox;

	protected Room(int rWidth, int rHeight, int rDepth,
			Orientation o) {
		this.orientation = o;
		this.roomWidth = rWidth;
		this.roomHeight = rHeight;
		this.roomDepth = rDepth;
		this.mainBoundingBox =
				createBoundingBox(0, 0, 0, rWidth, rHeight,
						rDepth);
	}

	public int getWidth() {
		if (orientation == Orientation.ROT_270
				|| orientation == Orientation.ROT_90) {
			return roomDepth;
		} else {
			return roomWidth;
		}
	}

	public int getHeight() {
		return roomHeight;
	}

	public int getDepth() {
		if (orientation == Orientation.ROT_270
				|| orientation == Orientation.ROT_90) {
			return roomWidth;
		} else {
			return roomDepth;
		}
	}

	public abstract BoundingBox[] getBoundingBoxes();

	public final BoundingBox getMainBoundingBox() {
		return mainBoundingBox;
	}

	protected int baseX, baseY, baseZ;

	public final void generate(World w, int baseX, int baseY,
			int baseZ) {
		this.baseX = baseX;
		this.baseY = baseY;
		this.baseZ = baseZ;
		generate(w);

		for (RoomJoint node : getPathNodes()) {
			w.getBlockAt(baseX + getPathNodeX(node),
					baseY + node.y, baseZ + getPathNodeZ(node))
					.setType(
							node.type == RoomType.Building ? Material.OBSIDIAN
									: Material.GLOWSTONE);
		}

		Sign sS = new Sign();
		sS.setFacingDirection(BlockFace.DOWN);
		Block blck =
				w.getBlockAt(baseX,
						w.getHighestBlockYAt(baseX, baseZ),
						baseZ);
		blck.setTypeIdAndData(sS.getItemTypeId(), sS.getData(),
				false);
		CraftSign s = new CraftSign(blck);
		s.setLine(0, getMainBoundingBox().toString());
		s.setLine(1, orientation.name());
		s.setLine(2, baseX + "," + baseY + "," + baseZ);
		s.setLine(3, getClass().getSimpleName());
		s.update(true);
	}

	protected abstract void generate(World w);

	public final List<RoomJoint> getPathNodes() {
		return pathNodes;
	}

	public final Iterator<RoomJoint> getPathNodes(final Side s) {
		return new Iterator<RoomJoint>() {
			private int cID = 0;
			private RoomJoint next = rNext();

			@Override
			public boolean hasNext() {
				return next != null;
			}

			public RoomJoint rNext() {
				while (cID < pathNodes.size()) {
					RoomJoint r = pathNodes.get(cID);
					cID++;
					if (r != null && r.side == s) {
						return r;
					}
				}
				return null;
			}

			@Override
			public RoomJoint next() {
				RoomJoint next = this.next;
				this.next = rNext();
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	protected Block getBlockInRoom(World w, int x, int y, int z) {
		return w.getBlockAt(baseX + getXInRoom(x, z), y + baseY,
				baseZ + getZInRoom(x, z));
	}

	protected int getXInRoom(int x, int z) {
		switch (orientation) {
		case ROT_90:
			return roomDepth - 1 - z;
		case ROT_180:
			return roomWidth - 1 - x;
		case ROT_270:
			return z;
		default:
			return x;
		}
	}

	protected int getZInRoom(int x, int z) {
		switch (orientation) {
		case ROT_90:
			return x;
		case ROT_180:
			return roomDepth - 1 - z;
		case ROT_270:
			return roomWidth - 1 - x;
		default:
			return z;
		}
	}

	protected BoundingBox createBoundingBox(int x, int y, int z,
			int width, int height, int depth) {
		int x1 = getXInRoom(x, z);
		int z1 = getZInRoom(x, z);
		int x2 = getXInRoom(x + width - 1, z + depth - 1);
		int z2 = getZInRoom(x + width - 1, z + depth - 1);
		return new BoundingBox(Math.min(x1, x2), y, Math.min(z1,
				z2), Math.max(x1, x2), y + height - 1, Math.max(
				z1, z2));
	}

	protected void addPathNode(Side s, int val, int y,
			int type, boolean sReq) {
		int x = 0;
		int z = 0;
		if (s == Side.X_PLUS) {
			x = roomWidth - 1;
		} else if (s == Side.X_MINUS) {
			x = 0;
		} else {
			x = val;
		}
		if (s == Side.Z_PLUS) {
			z = roomDepth - 1;
		} else if (s == Side.Z_MINUS) {
			z = 0;
		} else {
			z = val;
		}
		Side rotated = s.rotate(orientation);
		pathNodes
				.add(new RoomJoint(
						rotated,
						rotated == Side.X_MINUS
								|| rotated == Side.X_PLUS ? getZInRoom(
								x, z) : getXInRoom(x, z), y,
						type, sReq));
	}

	public int getPathNodeX(RoomJoint ent) {
		if (ent.side == Side.X_PLUS) {
			return getWidth() - 1;
		} else if (ent.side == Side.X_MINUS) {
			return 0;
		} else {
			return ent.axis;
		}
	}

	public int getPathNodeZ(RoomJoint ent) {
		if (ent.side == Side.Z_PLUS) {
			return getDepth() - 1;
		} else if (ent.side == Side.Z_MINUS) {
			return 0;
		} else {
			return ent.axis;
		}
	}
}
