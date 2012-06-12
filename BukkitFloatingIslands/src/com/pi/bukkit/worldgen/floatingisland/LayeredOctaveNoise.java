package com.pi.bukkit.worldgen.floatingisland;

import java.util.Arrays;

import org.bukkit.util.noise.OctaveGenerator;

public class LayeredOctaveNoise {
	private final OctaveGenerator gen;
	// private int layers;
	private double[] scale;
	private double[] freq;
	private double[] amp;
	private int blendSize = 0;

	public LayeredOctaveNoise(OctaveGenerator gen, int layers) {
		this.gen = gen;
		// this.layers = layers;
		this.scale = new double[layers];
		this.freq = new double[layers];
		this.amp = new double[layers];

		setScale(1D);
		setFrequency(1D);
		setAmplitude(1D);
	}

	public void setFrequency(double d) {
		Arrays.fill(freq, d);
	}

	public void setAmplitude(double d) {
		Arrays.fill(amp, d);
	}

	public void setScale(double d) {
		Arrays.fill(scale, d);
	}

	public void setFrequency(int layer, double d) {
		freq[layer] = d;
	}

	public void setAmplitude(int layer, double d) {
		amp[layer] = d;
	}

	public void setScale(int layer, double d) {
		scale[layer] = d;
	}

	public double noise(int layer, double x, double y) {
		double ttl = 0;
		for (double xO = -blendSize; xO <= blendSize; xO++) {
			for (double yO = -blendSize; yO <= blendSize; yO++) {
				ttl += cleanNoise(gen.noise((x + xO) * scale[layer], (y + yO)
						* scale[layer], layer, freq[layer], amp[layer], true));
			}
		}
		int bArea = (1 + (2 * blendSize));
		return ttl / (bArea * bArea);
	}

	private static double cleanNoise(double d) {
		return Math.abs((d + 1D) / 2D);
	}
}
