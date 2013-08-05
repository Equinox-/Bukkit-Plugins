package essentials.database;

import java.io.IOException;

import essentials.Essentials;

public class WarpDatabase extends DatabaseFile {
	public WarpDatabase(Essentials ess) throws IOException {
		super(ess, "warps", WarpEntry.class);
	}

	public void setWarp(WarpEntry home) throws Exception {
		data.put(home.primary(), home);
	}

	public void removeWarp(String homeOwner) throws Exception {
		data.remove(homeOwner);
	}

	public String[] getWarps() {
		return data.keySet().toArray(
				new String[data.keySet().size()]);
	}

	public WarpEntry getWarp(String owner) throws Exception {
		return (WarpEntry) data.get(owner);
	}
}
