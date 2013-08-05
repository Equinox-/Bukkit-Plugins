package essentials.database;

import java.io.IOException;

import essentials.Essentials;

public class AntiFireDatabase extends DatabaseFile {

	public AntiFireDatabase(Essentials ess) throws IOException {
		super(ess, "fireprotect", AntiFireEntry.class);
	}

	public boolean isProtected(int x, int z) {
		return data.get(x + ":" + z) != null;
	}

	public void protect(int x, int z) {
		DBEntry d = new AntiFireEntry().setLocation(x, z);
		data.put(d.primary(), d);
		try {
			saveData();
		} catch (IOException e) {
		}
	}

	public void unprotect(int x, int z) {
		data.remove(x + ":" + z);
		try {
			saveData();
		} catch (IOException e) {
		}
	}

	public static class AntiFireEntry extends DBEntry {
		int chunkX = 0, chunkZ = 0;

		public AntiFireEntry setLocation(int x, int z) {
			chunkX = x;
			chunkZ = z;
			return this;
		}

		@Override
		public void fromString(String s) throws IOException {
			String[] data = s.split(":");
			if (data.length == 2) {
				try {
					chunkX = Integer.valueOf(data[0]);
					chunkZ = Integer.valueOf(data[1]);
				} catch (NumberFormatException e) {
					throw new IOException();
				}
			} else {
				throw new IOException();
			}
		}

		@Override
		public String primary() {
			return toString();
		}

		@Override
		public String toString() {
			return chunkX + ":" + chunkZ;
		}

	}
}
