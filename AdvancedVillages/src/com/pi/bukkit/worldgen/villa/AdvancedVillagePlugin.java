package com.pi.bukkit.worldgen.villa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.pi.villa.gen.Orientation;
import com.pi.villa.gen.PositionedRoom;
import com.pi.villa.gen.Village;
import com.pi.villa.gen.room.RoomFountain;

public class AdvancedVillagePlugin extends JavaPlugin implements
		CommandExecutor {
	private Logger log = Logger.getLogger("Minecraft");
	private List<Village> villages = new ArrayList<Village>();

	@Override
	public void onEnable() {
		logMessage("Enabled");
		getCommand("village").setExecutor(this);
	}

	@Override
	public void onDisable() {
		logMessage("Disabled");
	}

	public void logMessage(String s) {
		PluginDescriptionFile pd = this.getDescription();
		log.info(pd.getName() + "  " + pd.getVersion() + ": "
				+ s);
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String world,
			String id) {
		return new AdvancedVillageGenerator(this);
	}

	@Override
	public boolean onCommand(CommandSender commandsender,
			Command command, String s, String as[]) {
		if (s.trim().equalsIgnoreCase("village")) {
			if (commandsender instanceof Player) {
				Player p = (Player) commandsender;

				Village v = null;
				for (Village vT : villages) {
					if (vT.isVillageAt(p.getLocation()
							.getBlockX(), p.getLocation()
							.getBlockZ())) {
						v = vT;
						break;
					}
				}
				if (v == null) {
					PositionedRoom sR =
							new PositionedRoom(
									new RoomFountain(
											Orientation.ROT_0),
									p.getLocation().getBlockX(),
									p.getWorld()
											.getHighestBlockYAt(
													p.getLocation()
															.getBlockX(),
													p.getLocation()
															.getBlockZ()),
									p.getLocation().getBlockZ());
					sR.generate(p.getWorld());
					v = new Village(sR);
					villages.add(v);
				} else {
					int count = 1;
					if (as.length == 1) {
						count = Integer.valueOf(as[0]);
					}
					for (int i = 0; i < count; i++)
						v.doExpansion(p.getWorld());
				}
				return true;
			}
		}
		return false;
	}
}
