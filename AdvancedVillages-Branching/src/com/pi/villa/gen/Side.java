package com.pi.villa.gen;

public enum Side {
	X_MINUS(-1, 0), Z_MINUS(0, -1), X_PLUS(1, 0), Z_PLUS(0, 1);
	private int xOff, zOff;

	private Side(final int sXOff, final int sZOff) {
		this.xOff = sXOff;
		this.zOff = sZOff;
	}

	public int getXOff() {
		return xOff;
	}

	public int getZOff() {
		return zOff;
	}

	public Side getInverse() {
		return getSide(-xOff, -zOff);
	}

	public Side rotate(Orientation o) {
		switch (o) {
		case ROT_0:
			return this;
		case ROT_90:
			return rotate(1);
		case ROT_180:
			return rotate(2);
		case ROT_270:
			return rotate(3);
		default:
			return this;
		}
	}

	public Side rotate(int ticks) {
		int idx = (ordinal() + ticks) % values().length;
		return values()[idx];
	}

	public static Side getSide(final int xOff, final int zOff) {
		for (Side d : values()) {
			if (d.getXOff() == xOff && d.getZOff() == zOff) {
				return d;
			}
		}
		return null;
	}
}
