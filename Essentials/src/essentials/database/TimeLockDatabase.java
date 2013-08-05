package essentials.database;

import java.io.IOException;

import essentials.Essentials;
import essentials.TimeLockController;

public class TimeLockDatabase extends DatabaseFile {
	TimeLockController control;

	public TimeLockDatabase(Essentials ess,
			TimeLockController control) throws IOException {
		super(ess, "timelock", TimeLockEntry.class);
		this.control = control;
	}

	@Override
	public void loadData() throws IOException {
		super.loadData();
		for (DBEntry e : data.values()) {
			TimeLockEntry tE = (TimeLockEntry) e;
			this.control.lock(tE.world, tE.time);
		}
	}

	public void addLock(String world, long time) {
		if (data.get(world) == null)
			data.put(world, new TimeLockEntry());
		((TimeLockEntry) data.get(world)).world = world;
		((TimeLockEntry) data.get(world)).time = time;
	}

	public static class TimeLockEntry extends DBEntry {
		String world;
		long time;

		@Override
		public void fromString(String s) throws IOException {
			String[] data = s.split(":");
			if (data.length == 2) {
				this.world = data[0];
				this.time = Long.valueOf(data[1]);
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
			return world + ":" + time;
		}

	}
}
