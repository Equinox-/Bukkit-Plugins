package essentials;

import java.io.IOException;

import org.bukkit.Location;

import essentials.database.AntiFireDatabase;
import essentials.listeners.AntiFireListener;

public class AntiFireController {
	private AntiFireListener list;
	private AntiFireDatabase protection;
	public final Essentials ess;

	public AntiFireController(Essentials ess) {
		this.ess = ess;
		try {
			protection = new AntiFireDatabase(ess);
			protection.loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		list = new AntiFireListener(protection);
		ess.registerEventListener(list);
	}

	public void saveData() throws IOException {
		protection.saveData();
	}

	public void protect(Location l) {
		list.protectChunk(l);
	}

	public boolean isProtected(Location l) {
		return list.isChunkProtected(l);
	}

	public void unprotect(Location l) {
		list.unprotectChunk(l);
	}
}
