package essentials.database;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import essentials.Essentials;

public abstract class DatabaseFile {
	private final File dataFile;
	protected final Essentials ess;
	private final Class<? extends DBEntry> entryType;
	public Map<String, DBEntry> data =
			new HashMap<String, DBEntry>();

	public DatabaseFile(Essentials ess, String dbName,
			Class<? extends DBEntry> entryType)
			throws IOException {
		this.ess = ess;
		File dir = ess.getDataFolder();
		if (!dir.exists()) {
			dir.mkdir();
			ess.log.info("[Essentials] Creating directory: "
					+ dir.toString());
		}
		dataFile = new File(dir, dbName + ".db");
		if (!dataFile.exists()) {
			dataFile.createNewFile();
			ess.log.info("[Essentials] Creating database file: "
					+ dataFile.toString());
			String write = getDefaultFile();
			if (write.length() > 0) {
				BufferedWriter w =
						new BufferedWriter(new FileWriter(write));
				w.write(write);
				w.close();
			}
		}
		this.entryType = entryType;
	}

	public void loadData() throws IOException {
		BufferedReader reader =
				new BufferedReader(new FileReader(dataFile));
		data.clear();
		while (reader.ready()) {
			String line = reader.readLine();
			if (line.length() > 0) {
				try {
					if (line.trim().startsWith("#"))
						continue;
					DBEntry newEntry =
							(DBEntry) entryType.getConstructor()
									.newInstance();
					newEntry.fromString(line);
					data.put(newEntry.primary(), newEntry);
				} catch (Exception e) {
				}
			}
		}
		reader.close();
	}

	public void saveData() throws IOException {
		BufferedWriter writer =
				new BufferedWriter(new FileWriter(dataFile));
		for (DBEntry e : data.values()) {
			if (e != null) {
				writer.write(e.toString());
				writer.newLine();
			}
		}
		writer.close();
	}

	public String getDefaultFile() {
		return "";
	}
}
