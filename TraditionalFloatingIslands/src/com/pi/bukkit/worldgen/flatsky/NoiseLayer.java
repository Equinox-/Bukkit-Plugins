package com.pi.bukkit.worldgen.flatsky;

import org.bukkit.util.noise.NoiseGenerator;

public class NoiseLayer extends NoiseGenerator {
	private NoiseGenerator instance;
	double scale;

	public NoiseLayer(NoiseGenerator backend, double scale) {
		this.instance = backend;
		this.scale = scale;
	}

	@Override
	public double noise(double x, double y, double z) {
		return Math.abs(instance.noise(x * scale, y * scale, z * scale));// + 1D) / 2D;
	}
}
