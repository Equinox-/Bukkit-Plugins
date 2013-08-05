package essentials.database;

import java.io.IOException;

import essentials.Essentials;

public class HomeDatabase extends DatabaseFile {

	public HomeDatabase(Essentials ess) throws IOException {
		super(ess, "homes", HomeEntry.class);
	}

	public void setHome(HomeEntry home) throws Exception {
		data.put(home.primary(), home);
	}

	public boolean removeHome(String homeOwner) throws Exception {
		return data.remove(homeOwner) != null;
	}

	public HomeEntry getHome(String owner) throws Exception {
		return (HomeEntry) data.get(owner);
	}
}
