package com.pi.bukkit.worldgen.flatsky;

import java.util.logging.Logger;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyIslandsPlugin extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logMessage("Enabled");
	}

	@Override
	public void onDisable() {
		logMessage("Disabled");
	}

	public void logMessage(String s) {
		PluginDescriptionFile pd = this.getDescription();
		log.info(pd.getName() + "  " + pd.getVersion() + ": " + s);
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String world, String id) {
		return new SkyIslandsGenerator(this);
	}
}
