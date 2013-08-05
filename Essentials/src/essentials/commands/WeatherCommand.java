package essentials.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;

public class WeatherCommand extends EssentialCommand {

	public WeatherCommand(Essentials plugin) {
		super(plugin);
		name = "weather";
		desc = "Sets the weather";
		usage =
				"/<command> [off:calm:rain:snow:storm] [lock]  /<command> unlock";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (args.length > 0) {
			World w = ((Player) sender).getWorld();
			if (args[0].equalsIgnoreCase("unlock")) {
				plugin.weatherLockController.unlock(w.getName());
			} else {
				boolean storm = false, thunder = false;
				int duration;
				if (args[0].equalsIgnoreCase("calm")
						|| args[0].equalsIgnoreCase("off")) {
					storm = false;
					thunder = false;
					duration = random(16000, 18000);
				} else if (args[0].equalsIgnoreCase("rain")
						|| args[0].equalsIgnoreCase("snow")) {
					storm = true;
					thunder = false;
					duration = random(5000, 7000);
				} else if (args[0].equalsIgnoreCase("storm")) {
					thunder = true;
					storm = true;
					duration = random(5000, 7000);
				} else
					return false;
				if (args.length == 2
						&& args[1].equalsIgnoreCase("lock")) {
					plugin.weatherLockController.lock(
							w.getName(), storm, thunder);
					sender.sendMessage(ChatColor.AQUA
							+ "Locked weather at: "
							+ (storm ? thunder ? "Storming"
									: "Raining" : "Calm"));
				} else {
					plugin.weatherLockController.unlock(w
							.getName());
					w.setStorm(storm);
					w.setWeatherDuration(duration);
					w.setThundering(thunder);
					w.setThunderDuration(duration);
					sender.sendMessage(ChatColor.AQUA
							+ "Set weather to: "
							+ (storm ? thunder ? "Storming"
									: "Raining" : "Calm"));
				}
			}
			return true;
		}
		return false;
	}

	private int random(int lower, int upper) {
		return (int) (new Random().nextDouble() * (double) (upper - lower))
				+ lower;
	}

}
