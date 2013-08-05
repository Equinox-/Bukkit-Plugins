package essentials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

public class Cuboid {
	public List<CuboidBlock> data = new ArrayList<CuboidBlock>();
	public int minX = Integer.MAX_VALUE,
			minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE,
			maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE,
			maxZ = Integer.MIN_VALUE;

	public void addBlock(CuboidBlock b) {
		data.add(b);
		if (b.x < minX)
			minX = b.x;
		if (b.y < minY)
			minY = b.y;
		if (b.z < minZ)
			minZ = b.z;
		if (b.x > maxX)
			maxX = b.x;
		if (b.y > maxY)
			maxY = b.y;
		if (b.z > maxZ)
			maxZ = b.z;
	}

	public void addBlock(Block blk) {
		CuboidBlock b = new CuboidBlock();
		b.setBlock(blk);
		data.add(b);
		if (b.x < minX)
			minX = b.x;
		if (b.y < minY)
			minY = b.y;
		if (b.z < minZ)
			minZ = b.z;
		if (b.x > maxX)
			maxX = b.x;
		if (b.y > maxY)
			maxY = b.y;
		if (b.z > maxZ)
			maxZ = b.z;
	}

	public void clear() {
		data.clear();
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		minZ = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
		maxZ = Integer.MIN_VALUE;
	}
}
