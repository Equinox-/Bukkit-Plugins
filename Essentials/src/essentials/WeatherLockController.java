package essentials;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import essentials.database.WeatherLockDatabase;

public class WeatherLockController {
	private final Essentials ess;
	private final Map<String, WeatherLockThread> lockingThreads =
			new HashMap<String, WeatherLockThread>();
	private WeatherLockDatabase db;

	public WeatherLockController(Essentials ess) {
		this.ess = ess;
		try {
			this.db = new WeatherLockDatabase(ess, this);
			this.db.loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void lock(String world, boolean rain, boolean thunder) {
		if (!lockingThreads.containsKey(world))
			lockingThreads.put(world, new WeatherLockThread(ess,
					world));
		lockingThreads.get(world).setLock(rain, thunder);
		if (!lockingThreads.get(world).isAlive())
			lockingThreads.get(world).start();
		db.addLock(world, rain, thunder);
	}

	public void unlock(String world) {
		if (lockingThreads.containsKey(world)) {
			lockingThreads.get(world).quit();
			lockingThreads.remove(world);
		}
		db.data.remove(world);
	}

	public void saveData() throws IOException {
		db.saveData();
	}

	public void killThreads() {
		for (WeatherLockThread l : lockingThreads.values())
			l.quit();
	}

	public static class WeatherLockThread extends Thread {
		private final Essentials ess;
		private final String world;
		private boolean rain = false, storm = false;
		private boolean running = true;

		public WeatherLockThread(Essentials ess, String world) {
			this.ess = ess;
			this.world = world;
		}

		@Override
		public void run() {
			main: while (running) {
				while (isWeatherSet()) {
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
				ess.getServer().getWorld(world).setStorm(rain);
				ess.getServer().getWorld(world)
						.setThundering(storm);
				ess.getServer().getWorld(world)
						.setWeatherDuration(1000000);
				ess.getServer().getWorld(world)
						.setThunderDuration(1000000);
			}
		}

		public boolean isWeatherSet() {
			World w = ess.getServer().getWorld(world);
			if (w.isThundering() != storm
					|| w.getThunderDuration() < 500)
				return false;
			if (w.hasStorm() != rain
					|| w.getWeatherDuration() < 500)
				return false;
			return true;
		}

		public void quit() {
			running = false;
		}

		public void setLock(boolean rain, boolean storm) {
			this.rain = rain;
			this.storm = storm;
		}
	}
}
