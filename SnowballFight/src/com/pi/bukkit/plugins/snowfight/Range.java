package com.pi.bukkit.plugins.snowfight;

import java.io.IOException;

public class Range {
    private int min, max;

    public Range(int min, int max) {
	this.min = min;
	this.max = max;
    }

    public int random() {
	return (int) ((Math.random() * (max - min)) + min);
    }

    public String write() {
	return min + "," + max;
    }

    public void read(String s) throws IOException {
	String[] parts = s.split(",");
	if (parts.length != 2)
	    throw new IOException("Bad range format: " + s);
	try {
	    min = Integer.valueOf(parts[0]);
	    max = Integer.valueOf(parts[1]);
	} catch (NumberFormatException e) {
	    throw new IOException("Bad number format: " + e.toString());
	}
    }
}
