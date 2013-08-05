package essentials.database;

import java.io.IOException;

import org.bukkit.*;

public class WarpEntry extends DBEntry {
	private String name;
	private double x, y, z;
	private float pitch, yaw;
	private String worldName;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getYaw() {
		return yaw;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setLocation(Location location) {
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}

	public Location getLocation() {
		World world = Bukkit.getServer().getWorld(worldName);
		return new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public void fromString(String s) throws IOException {
		String[] data = s.split(":");
		if (data.length == 7) {
			name = data[0];
			x = Double.valueOf(data[1]);
			y = Double.valueOf(data[2]);
			z = Double.valueOf(data[3]);
			yaw = Float.valueOf(data[4]);
			pitch = Float.valueOf(data[5]);
			worldName = data[6];
			return;
		}
		throw new IOException("Malformed Entry!");
	}

	@Override
	public String toString() {
		return name + ":" + x + ":" + y + ":" + z + ":" + yaw
				+ ":" + pitch + ":" + worldName;
	}

	@Override
	public String primary() {
		return name;
	}
}
