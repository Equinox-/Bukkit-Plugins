package essentials.commands.mapeditor;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import essentials.Essentials;
import essentials.commands.EssentialCommand;

public class SmoothCommand extends EssentialCommand {

	public SmoothCommand(Essentials plugin) {
		super(plugin);
		name = "smooth";
		desc = "Smooths a cuboid";
		usage = "/<command> [itr] [rand]";
		requiresOp = true;
	}

	@Override
	public boolean execute(CommandSender sender,
			Command command, String label, String[] args) {
		try {
			if (sender instanceof Player) {
				int itrs = 1;
				boolean rand = false;
				for (String s : args) {
					try {
						itrs = Integer.valueOf(args[0]);
					} catch (Exception e) {
						if (s.equalsIgnoreCase("rand"))
							rand = true;
					}
				}
				Player player = (Player) sender;
				if (plugin.selector.getBlockA(player.getName()) == null
						|| plugin.selector.getBlockB(player
								.getName()) == null) {
					player.sendMessage(ChatColor.AQUA
							+ "You must have at least 2 blocks selected to smooth a cuboid!");
					return true;
				}
				Location a =
						plugin.selector.getBlockA(player
								.getName());
				Location b =
						plugin.selector.getBlockB(player
								.getName());
				int minX =
						Math.min(a.getBlockX(), b.getBlockX());
				int minZ =
						Math.min(a.getBlockZ(), b.getBlockZ());
				int maxX =
						Math.max(a.getBlockX(), b.getBlockX());
				int maxZ =
						Math.max(a.getBlockZ(), b.getBlockZ());

				// Save height maps
				int[][] hMap =
						new int[maxX - minX + 1][maxZ - minZ + 1];
				float[][] nHMap =
						new float[maxX - minX + 1][maxZ - minZ
								+ 1];
				for (int x =
						Math.min(a.getBlockX(), b.getBlockX()); x <= Math
						.max(a.getBlockX(), b.getBlockX()); x++) {
					for (int z =
							Math.min(a.getBlockZ(),
									b.getBlockZ()); z <= Math
							.max(a.getBlockZ(), b.getBlockZ()); z++) {
						int maxY = Integer.MIN_VALUE;
						for (int y =
								Math.min(a.getBlockY(),
										b.getBlockY()); y <= Math
								.max(a.getBlockY(),
										b.getBlockY()); y++) {
							Block block =
									player.getWorld()
											.getBlockAt(x, y, z);
							if (block != null
									&& Essentials.isSolid(block
											.getTypeId())) {
								maxY = y;
							}
						}
						int fX = x - minX, fY = z - minZ;
						if (maxY == Integer.MIN_VALUE) {
							hMap[fX][fY] = -1;
							nHMap[fX][fY] = -1;
						} else {
							hMap[fX][fY] = maxY;
							nHMap[fX][fY] = maxY;
						}
					}
				}
				sender.sendMessage(ChatColor.AQUA
						+ "Loaded height map");

				for (int itr = 0; itr < itrs; itr++) {
					sender.sendMessage("Before: "
							+ Arrays.deepToString(nHMap));
					float[][] nHLMap =
							new float[maxX - minX + 1][maxZ
									- minZ + 1];
					// Smooth it
					for (int x = 0; x < nHMap.length; x++) {
						nHLMap[x][0] = hMap[x][0];
						nHLMap[x][nHMap[x].length - 1] =
								hMap[x][hMap[x].length - 1];
					}
					for (int z = 0; z < nHMap[0].length; z++) {
						nHLMap[0][z] = hMap[0][z];
						nHLMap[nHMap.length - 1][z] =
								hMap[hMap.length - 1][z];
					}
					for (int x = 1; x < nHMap.length - 1; x++) {
						for (int z = 1; z < nHMap[0].length - 1; z++) {
							if (hMap[x][z] != -1) {
								float cnt = 0;
								float ttl = 0;
								for (int xO = -1; xO <= 1; xO++) {
									for (int zO = -1; zO <= 1; zO++) {
										float val =
												nHMap[x + xO][z
														+ zO];
										if (val != -1) {
											cnt++;
											ttl += val;
										}
									}
								}
								nHLMap[x][z] =
										(ttl / cnt)
												+ (rand ? ((float) Math
														.random() - 0.5f * 0.25f)
														: 0);
							}
						}
					}
					nHMap = nHLMap.clone();
					sender.sendMessage(ChatColor.AQUA
							+ "Smooth cycle " + (itr + 1) + "/"
							+ itrs);
					sender.sendMessage("After: "
							+ Arrays.deepToString(nHMap));
				}

				// Save map to wor ld
				for (int x = 1; x < nHMap.length - 1; x++) {
					for (int z = 1; z < nHMap[0].length - 1; z++) {
						if (hMap[x][z] >= 0 && nHMap[x][z] >= 0) {
							int wX = x + minX, wZ = z + minZ;
							int oH = hMap[x][z];
							int nH = Math.round(nHMap[x][z]);
							if (oH < nH) {
								// Need to increase height
								Block type =
										player.getWorld()
												.getBlockAt(wX,
														oH, wZ);
								for (int y = oH; y <= nH; y++) {
									Block block =
											player.getWorld()
													.getBlockAt(
															wX,
															y,
															wZ);
									block.setTypeIdAndData(
											type.getTypeId(),
											type.getData(),
											Essentials
													.hasPhysics(type
															.getType()));
								}
							} else if (oH > nH) {
								// Need to decrease height
								for (int y = nH + 1; y <= oH; y++) {
									Block block =
											player.getWorld()
													.getBlockAt(
															wX,
															y,
															wZ);
									block.setTypeIdAndData(0,
											(byte) 0, false);
								}
							}
						}
					}
				}
				sender.sendMessage(ChatColor.AQUA
						+ "Saved height map");
				return true;
			}
			return false;
		} catch (Exception e) {
			sender.sendMessage(ChatColor.AQUA + e.toString());
			if (e.getStackTrace().length > 0) {
				sender.sendMessage(ChatColor.AQUA
						+ e.getStackTrace()[0].toString());
			}
			return true;
		}
	}
}
