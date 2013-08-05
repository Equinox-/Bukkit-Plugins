package com.pi;

public class LocationMath {
	public static double distance(int x1, int y1, int z1,
			int x2, int y2, int z2) {
		int xD = x1 - x2;
		int yD = y1 - y2;
		int zD = z1 - z2;
		return Math.sqrt((xD * xD) + (yD * yD) + (zD * zD));
	}
}
