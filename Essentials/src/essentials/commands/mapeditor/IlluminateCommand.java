package essentials.commands.mapeditor;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class IlluminateCommand extends EssentialCommand {

	public IlluminateCommand(Essentials plugin) {
		super(plugin);
		name = "illuminate";
		desc = "Lights an area up with torches.";
		usage = "/<command>";
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (plugin.selector.getBlockA(player.getName()) == null
					|| plugin.selector.getBlockB(player
							.getName()) == null) {
				player.sendMessage(ChatColor.AQUA
						+ "You must have at least 2 blocks selected to illuminate a cuboid!");
				return true;
			}
			Location a =
					plugin.selector.getBlockA(player.getName());
			Location b =
					plugin.selector.getBlockB(player.getName());
			for (int x = Math.min(a.getBlockX(), b.getBlockX()); x <= Math
					.max(a.getBlockX(), b.getBlockX()); x += 4) {
				for (int z =
						Math.min(a.getBlockZ(), b.getBlockZ()); z <= Math
						.max(a.getBlockZ(), b.getBlockZ()); z +=
						4) {
					boolean placedTorch = false;
					for (int y =
							Math.min(a.getBlockY(),
									b.getBlockY()); y <= Math
							.max(a.getBlockY(), b.getBlockY()); y++) {
						Block block =
								player.getWorld().getBlockAt(x,
										y, z);
						Block below =
								player.getWorld().getBlockAt(x,
										y - 1, z);
						if (below != null && block != null) {
							if (block.getType() == Material.AIR
									&& Essentials
											.canPlaceTorchOn(below
													.getTypeId())) {
								block.setTypeIdAndData(
										Material.TORCH.getId(),
										(byte) 0x5, true);
								placedTorch = true;
							}
						}
					}
					for (int xO = -1; xO <= 1 && !placedTorch; xO++) {
						for (int zO = -1; zO <= 1
								&& !placedTorch; zO++) {
							for (int y =
									Math.min(a.getBlockY(),
											b.getBlockY()); y <= Math
									.max(a.getBlockY(),
											b.getBlockY()); y++) {
								Block block =
										player.getWorld()
												.getBlockAt(
														x + xO,
														y,
														z + zO);
								Block below =
										player.getWorld()
												.getBlockAt(
														x + xO,
														y - 1,
														z + zO);
								if (below != null
										&& block != null) {
									if (block.getType() == Material.AIR
											&& Essentials
													.canPlaceTorchOn(below
															.getTypeId())) {
										block.setTypeIdAndData(
												Material.TORCH
														.getId(),
												(byte) 0x5, true);
										placedTorch = true;
									}
								}
							}

						}
					}
				}
			}
			return true;
		}
		return false;
	}
}
