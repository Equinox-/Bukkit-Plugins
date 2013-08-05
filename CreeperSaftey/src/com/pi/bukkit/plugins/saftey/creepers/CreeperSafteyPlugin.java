package com.pi.bukkit.plugins.saftey.creepers;

import java.util.ConcurrentModificationException;
import java.util.Vector;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CreeperSafteyPlugin extends JavaPlugin implements
		Listener, Runnable {
	private Vector<CreeperCrater> craters =
			new Vector<CreeperCrater>();

	private boolean proc = true;
	Thread t = new Thread(this);

	@Override
	public void onEnable() {
		getServer().getPluginManager()
				.registerEvents(this, this);
		proc = true;
		t.start();
	}

	@Override
	public void onDisable() {
		proc = false;
		try {
			t.join();
			for (CreeperCrater c : craters) {
				try {
					if (c.processLoop()) {
						craters.remove(c);
					}
				} catch (ConcurrentModificationException e) {
				}
			}
		} catch (InterruptedException e) {
			t.stop();
		}
	}

	@Override
	public void run() {
		while (proc) {
			for (CreeperCrater c : craters) {
				if (c.processLoop()) {
					craters.remove(c);
				}
			}
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
			}
			Thread.yield();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent e) {
		if (e.getEntityType() != null
				&& e.getEntityType() == EntityType.CREEPER) {
			//craters.add(new CreeperCrater(e.blockList()));
			e.setCancelled(true);
		}
	}
}
