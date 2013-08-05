package essentials;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import essentials.database.TimeLockDatabase;

public class TimeLockController {
	private final Essentials ess;
	private final Map<String, TimeLockThread> lockingThreads =
			new HashMap<String, TimeLockThread>();
	private TimeLockDatabase db;

	public TimeLockController(Essentials ess) {
		this.ess = ess;
		try {
			this.db = new TimeLockDatabase(ess, this);
			this.db.loadData();
		} catch (IOException e) {
		}
	}

	public void lock(String world, long time) {
		if (!lockingThreads.containsKey(world))
			lockingThreads.put(world, new TimeLockThread(ess,
					world));
		lockingThreads.get(world).setLock(time);
		if (!lockingThreads.get(world).isAlive())
			lockingThreads.get(world).start();
		this.db.addLock(world, time);
	}

	public void unlock(String world) {
		if (lockingThreads.containsKey(world)) {
			lockingThreads.get(world).quit();
			lockingThreads.remove(world);
		}
		this.db.data.remove(world);
	}

	public void killThreads() {
		for (TimeLockThread l : lockingThreads.values())
			l.quit();
	}

	public void saveData() throws IOException {
		this.db.saveData();
	}

	public static class TimeLockThread extends Thread {
		private final Essentials ess;
		private final String world;
		private long time;
		private boolean running = true;

		public TimeLockThread(Essentials ess, String world) {
			this.ess = ess;
			this.world = world;
		}

		@Override
		public void run() {
			main: while (running) {
				while (Math.abs(ess.getServer().getWorld(world)
						.getTime()
						- time) < 1000) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						running = false;
						break main;
					}
					if (!running)
						break main;
				}
				ess.getServer().getWorld(world).setTime(time);
			}
		}

		public void quit() {
			running = false;
		}

		public void setLock(long time) {
			this.time = time;
		}
	}
}
