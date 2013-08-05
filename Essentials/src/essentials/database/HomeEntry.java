package essentials.database;

import java.io.IOException;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class HomeEntry extends DBEntry {
	private String owner;
	private double x, y, z;
	private float pitch, yaw;
	private String worldName;
	private String invites;

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String[] getInvites() {
		return invites.split("||");
	}

	public boolean isInvited(String name) {
		for (String s : getInvites())
			if (s.equals(name))
				return true;
		return false;
	}

	public void invite(String name) {
		for (String s : getInvites())
			if (s.equals(name))
				return;
		invites += name + "||";
	}

	public void revokeInvite(String name) {
		invites = invites.replace(name + "||", "");
	}

	public void setOwner(Player player) {
		this.owner = player.getName();
	}

	public String getOwner() {
		return owner;
	}

	public void setInvitesArray(String invites) {
		this.invites = invites;
	}

	public String getInvitesArray() {
		return invites;
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
		if (data.length == 8) {
			owner = data[0];
			x = Double.valueOf(data[1]);
			y = Double.valueOf(data[2]);
			z = Double.valueOf(data[3]);
			yaw = Float.valueOf(data[4]);
			pitch = Float.valueOf(data[5]);
			worldName = data[6];
			invites = data[7];
			return;
		}
		throw new IOException("Malformed Entry!");
	}

	@Override
	public String toString() {
		return owner + ":" + x + ":" + y + ":" + z + ":" + yaw
				+ ":" + pitch + ":" + worldName + ":" + invites;
	}

	@Override
	public String primary() {
		return owner;
	}
}
