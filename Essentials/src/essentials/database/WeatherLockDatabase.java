package essentials.database;

import java.io.IOException;

import essentials.Essentials;
import essentials.WeatherLockController;

public class WeatherLockDatabase extends DatabaseFile {
	WeatherLockController control;

	public WeatherLockDatabase(Essentials ess,
			WeatherLockController control) throws IOException {
		super(ess, "weatherlock", WeatherLockEntry.class);
		this.control = control;
	}

	@Override
	public void loadData() throws IOException {
		super.loadData();
		for (DBEntry e : data.values()) {
			WeatherLockEntry tE = (WeatherLockEntry) e;
			this.control.lock(tE.world, tE.rain, tE.storm);
		}
	}

	public void addLock(String world, boolean rain, boolean storm) {
		if (data.get(world) == null)
			data.put(world, new WeatherLockEntry());
		((WeatherLockEntry) data.get(world)).world = world;
		((WeatherLockEntry) data.get(world)).rain = rain;
		((WeatherLockEntry) data.get(world)).storm = storm;
	}

	public static class WeatherLockEntry extends DBEntry {
		String world;
		boolean rain;
		boolean storm;

		@Override
		public void fromString(String s) throws IOException {
			String[] data = s.split(":");
			if (data.length == 3) {
				this.world = data[0];
				this.rain = Boolean.valueOf(data[1]);
				this.storm = Boolean.valueOf(data[2]);
			} else {
				throw new IOException();
			}
		}

		@Override
		public String primary() {
			return world;
		}

		@Override
		public String toString() {
			return world + ":" + Boolean.toString(rain) + ":"
					+ Boolean.toString(storm);
		}

	}
}
