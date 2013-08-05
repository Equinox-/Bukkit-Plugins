package com.pi.bukkit.plugins.snowfight;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Cuboid {
    public int minX, maxX;
    public int minY, maxY;
    public int minZ, maxZ;

    public void clean() {
	int minCX = minX, minCY = minY, minCZ = minZ;
	minX = Math.min(minX, maxX);
	maxX = Math.max(minCX, maxX);
	minY = Math.min(minY, maxY);
	maxY = Math.max(minCY, maxY);
	minZ = Math.min(minZ, maxZ);
	maxZ = Math.max(minCZ, maxZ);
    }

    public void read(String s) throws IOException {
	if (s == null)
	    throw new IOException("Badly formatted cuboid: null");
	String[] parts = s.split(",");
	if (parts.length != 6)
	    throw new IOException("Badly formatted cuboid: " + s);
	try {
	    minX = Integer.valueOf(parts[0]);
	    maxX = Integer.valueOf(parts[1]);

	    minY = Integer.valueOf(parts[2]);
	    maxY = Integer.valueOf(parts[3]);

	    minZ = Integer.valueOf(parts[4]);
	    maxZ = Integer.valueOf(parts[5]);
	} catch (NumberFormatException e) {
	    throw new IOException("Badly formatted number: " + e.toString());
	}
    }

    public String write() {
	return minX + "," + maxX + "," + minY + "," + maxY + "," + minZ + ","
		+ maxZ;
    }

    public boolean contains(int x, int y, int z) {
	return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ
		&& z <= maxZ;
    }

    public boolean contains(Location l) {
	return contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public Location randomLocation(Random rand, World w) {
	int x = minX + (int) (rand.nextFloat() * (maxX - minX));
	int z = minZ + (int) (rand.nextFloat() * (maxZ - minZ));
	int y = minY + (int) (rand.nextFloat() * (maxY - minY));
	return new Location(w, x, y, z);
    }

    public Location getSpawnableLocation(Random rand, World w) {
	int x = minX + (int) (rand.nextFloat() * (maxX - minX));
	int z = minZ + (int) (rand.nextFloat() * (maxZ - minZ));
	int y;
	boolean foundPlace = false;
	for (y = minY; y <= maxY; y++) {
	    if (w.getBlockAt(x, y, z).getType() == Material.AIR
		    && w.getBlockAt(x, y + 1, z).getType() == Material.AIR) {
		foundPlace = true;
		break;
	    }
	}
	if (foundPlace)
	    return new Location(w, x, y, z);
	else
	    return null;
    }

    public void clear(World w) {
	for (int x = minX; x <= maxX; x++) {
	    for (int y = minY; y <= maxY; y++) {
		for (int z = minZ; z <= maxZ; z++) {
		    w.getBlockAt(x, y, z).setTypeIdAndData(0, (byte) 0, false);
		}
	    }
	}
    }

    public boolean isEmpty() {
	return minX == maxX || minY == maxY || minZ == maxZ;
    }
}
