package essentials.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import essentials.Essentials;

public class LastUsedCommand implements Listener {
	private final Essentials plugin;
	private final Map<String, String> lastUsed =
			new HashMap<String, String>();

	public LastUsedCommand(Essentials plugin) {
		super();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(
			PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (msg != null && msg.startsWith("/")
				&& !msg.equalsIgnoreCase("/repeat")
				&& !msg.equalsIgnoreCase("/r") && p != null
				&& p.getDisplayName() != null) {
			lastUsed.put(p.getDisplayName(), msg);
		}
	}

	public String getLastUsedCommand(String player) {
		return lastUsed.get(player);
	}
}
