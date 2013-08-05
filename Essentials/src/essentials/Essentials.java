package essentials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import essentials.commands.EssentialCommand;
import essentials.commands.EssentialCommandGroup;
import essentials.commands.EssentialPipeCommand;
import essentials.database.HomeDatabase;
import essentials.database.WarpDatabase;
import essentials.listeners.BlockSelector;
import essentials.listeners.LastUsedCommand;

public class Essentials extends JavaPlugin {
	public Logger log;
	public WarpDatabase warpDatabase;
	public HomeDatabase homeDatabase;
	public BlockSelector selector;
	public LastUsedCommand lastUsedCommand;
	public TimeLockController timeLockController;
	public WeatherLockController weatherLockController;
	public AntiFireController antiFireController;

	public Map<String, Cuboid> cuboids =
			new HashMap<String, Cuboid>();
	public Map<String, EssentialCommand> commands =
			new HashMap<String, EssentialCommand>();
	public static HashSet<Byte> nonsolid = new HashSet<Byte>(
			Arrays.asList(new Byte[] { 0, 6, 8, 9, 10, 11, 27,
					28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55,
					59, 63, 64, 65, 66, 68, 69, 70, 71, 72, 75,
					76, 77, 78, 83, 90, 92, 93, 94, 96 }));
	public static HashSet<Byte> noTorchPlacement =
			new HashSet<Byte>(Arrays.asList(new Byte[] { 18, 19,
					20, 23, 25, 26, 44, 46, 53, 54, 58, 61, 62,
					67, 71, 81, 84, 85, 86, 91, 95 }));

	@Override
	public void onDisable() {
		try {
			warpDatabase.saveData();
			homeDatabase.saveData();
			timeLockController.saveData();
			weatherLockController.saveData();
		} catch (Exception e) {
			log.info(e.toString());
		}
		timeLockController.killThreads();
		weatherLockController.killThreads();
	}

	@Override
	public void onEnable() {
		log = getServer().getLogger();
		timeLockController = new TimeLockController(this);
		weatherLockController = new WeatherLockController(this);
		antiFireController = new AntiFireController(this);
		if (loadCommand("essentials.commands.WarpCommand")) {
			try {
				warpDatabase = new WarpDatabase(this);
				warpDatabase.loadData();
			} catch (Exception e) {
				log.info(e.toString());
				for (StackTraceElement ele : e.getStackTrace())
					log.info(ele.toString());
			}
		}
		if (loadCommand("essentials.commands.HomeCommand")) {
			try {
				homeDatabase = new HomeDatabase(this);
				homeDatabase.loadData();
			} catch (Exception e) {
				log.info(e.toString());
				for (StackTraceElement ele : e.getStackTrace())
					log.info(ele.toString());
			}
		}
		selector =
				(BlockSelector) loadListener(
						"essentials.listeners.BlockSelector",
						this, Material.STICK);
		lastUsedCommand =
				(LastUsedCommand) loadListener(
						"essentials.listeners.LastUsedCommand",
						this);
		if (lastUsedCommand != null) {
			loadCommand("essentials.commands.RepeatCommand");
		}
		if (selector != null) {
			boolean fillCommand =
					loadCommand("essentials.commands.mapeditor.FillCommand");
			if (fillCommand) {
				registerCommand(new EssentialPipeCommand(this,
						"fill", new Object[][] { new Object[] {
								new Integer(0),
								new String("air") } }, "clear",
						"Clears a cuboid"));
			}
			boolean lineCommand =
					loadCommand("essentials.commands.mapeditor.LineCommand");
			boolean lineReplaceCommand =
					loadCommand("essentials.commands.mapeditor.LineReplaceCommand");
			boolean fillReplaceCommand =
					loadCommand("essentials.commands.mapeditor.FillReplaceCommand");
			boolean pasteCommand =
					loadCommand("essentials.commands.mapeditor.PasteCommand");
			boolean copyCommand =
					loadCommand("essentials.commands.mapeditor.CopyCommand");
			boolean illuminateCommand =
					loadCommand("essentials.commands.mapeditor.IlluminateCommand");
			boolean buildWallCommand =
					loadCommand("essentials.commands.mapeditor.BuildWallCommand");
			boolean smoothCommand =
					loadCommand("essentials.commands.mapeditor.SmoothCommand");
			boolean mineCommand =
					loadCommand("essentials.commands.mapeditor.MineCommand");
			boolean coatCommand =
					loadCommand("essentials.commands.mapeditor.CoatCommand");
			boolean surroundCommand =
					loadCommand("essentials.commands.mapeditor.SurroundCommand");
			if (lineCommand || fillCommand || lineReplaceCommand
					|| fillReplaceCommand || pasteCommand
					|| copyCommand || illuminateCommand
					|| buildWallCommand || smoothCommand
					|| mineCommand || coatCommand
					|| surroundCommand)
				registerCommand(new EssentialCommandGroup(this,
						"cuboid", "line", "linereplace",
						"fillreplace", "fill", "copy", "paste",
						"illuminate", "buildwall", "smooth",
						"clear", "mine", "coat", "surround"));
		}
		loadCommand("essentials.commands.GiveCommand");
		loadCommand("essentials.commands.TimeCommand");
		loadCommand("essentials.commands.SpawnCommand");
		loadCommand("essentials.commands.OnlineCommand");
		loadCommand("essentials.commands.TPHereCommand");
		loadCommand("essentials.commands.TPToCommand");
		loadCommand("essentials.commands.WeatherCommand");
		loadCommand("essentials.commands.PrivateMessageCommand");
		loadCommand("essentials.commands.RepairCommand");
		loadCommand("essentials.commands.BurnCommand");
		loadCommand("essentials.commands.HealCommand");
		loadCommand("essentials.commands.KillCommand");
		loadCommand("essentials.commands.SpawnMobCommand");
		loadCommand("essentials.commands.LightningCommand");
		loadCommand("essentials.commands.MobSpawnerCommand");
		loadCommand("essentials.commands.ProtectFireCommand");
		loadCommand("essentials.commands.LocationCommand");
		loadListener(
				"essentials.listeners.InstaPickBlockRemoval",
				this, Material.DIAMOND_PICKAXE);
		loadListener("essentials.listeners.ItemTargetLightning",
				this, Material.GOLD_SWORD);
		loadListener("essentials.listeners.ItemTeleportInstant",
				this, Material.COMPASS);
		loadListener("essentials.listeners.RedstoneSpawner",
				this);
		loadListener("essentials.listeners.RedGlowStone", this);
	}

	public EssentialCommand getEssentialCommand(String name) {
		return commands.get(name);
	}

	public Class<?> loadClass(String name) {
		try {
			return getClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public Listener loadListener(String name, Object... initArgs) {
		Class<?> clazz = loadClass(name);
		if (clazz != null) {
			Class<?>[] initTypes = new Class<?>[initArgs.length];
			for (int i = 0; i < initArgs.length; i++)
				initTypes[i] = initArgs[i].getClass();
			try {
				Listener l =
						(Listener) clazz.getConstructor(
								initTypes).newInstance(initArgs);
				registerEventListener(l);
				log.info("Loaded Essential Listenener: " + name);
				return l;
			} catch (Exception e) {
				log.info("Unable to load " + name + ": "
						+ e.toString());
			}
		}
		return null;
	}

	public Listener getListener(String name, Object... initArgs) {
		Class<?> clazz = loadClass(name);
		if (clazz != null) {
			Class<?>[] initTypes = new Class<?>[initArgs.length];
			for (int i = 0; i < initArgs.length; i++)
				initTypes[i] = initArgs[i].getClass();
			try {
				Listener l =
						(Listener) clazz.getConstructor(
								initTypes).newInstance(initArgs);
				return l;
			} catch (Exception e) {
				log.info("Unable to load " + name + ": "
						+ e.toString());
			}
		}
		return null;
	}

	public void registerEventListener(Listener listener) {
		getServer().getPluginManager().registerEvents(listener,
				this);
	}

	public void registerCommand(EssentialCommand e) {
		if (getCommand(e.name) != null) {
			getCommand(e.name).setExecutor(e);
			if (e.desc != null && e.desc.length() > 0)
				getCommand(e.name).setDescription(e.desc);
			if (e.usage != null && e.desc.length() > 0)
				getCommand(e.name).setUsage(e.usage);
			commands.put(e.name, e);
		} else {
			log.severe("Not in config file: " + e.name);
		}
	}

	public Player matchPlayer(String name) {
		for (Player p : getServer().getOnlinePlayers())
			if (p.getName().equalsIgnoreCase(name))
				return p;
		return null;
	}

	public static boolean hasPhysics(Material mat) {
		return mat.equals(Material.REDSTONE_WIRE)
				|| mat.equals(Material.REDSTONE_TORCH_ON)
				|| mat.equals(Material.REDSTONE_TORCH_OFF)
				|| mat.equals(Material.WATER)
				|| mat.equals(Material.LAVA);
	}

	public static ItemStack fetchStack(CommandSender sender,
			String name) {
		Material mat = Material.matchMaterial(name);
		if (name.equalsIgnoreCase("redstone_torch"))
			mat = Material.REDSTONE_TORCH_ON;
		ColoredItems coloredMat = ColoredItems.lookup(name);
		if (mat == null && coloredMat == null) {
			sender.sendMessage(ChatColor.AQUA
					+ "Unable to find material: " + name);
			ArrayList<String> pairs1 =
					StringCompare.wordLetterPairs(name
							.toUpperCase());
			double best = 0;
			String bestMat = "";
			for (Material m : Material.values()) {
				double v =
						StringCompare.compareStrings(pairs1,
								m.name());
				if (v > best && v > 0.5d) {
					bestMat = m.name();
					best = v;
				}
			}
			for (ColoredItems m : ColoredItems.values()) {
				double v =
						StringCompare.compareStrings(pairs1,
								m.name());
				if (v > best && v > 0.5d) {
					bestMat = m.name();
					best = v;
				}
			}
			if (bestMat != null) {
				sender.sendMessage(ChatColor.AQUA
						+ "Did you mean "
						+ bestMat.toLowerCase() + "?");
			}
			return null;
		}
		ItemStack stack;
		if (coloredMat != null) {
			stack = coloredMat.getStack(1);
		} else {
			stack = new ItemStack(mat, 1);
			stack.setData(mat.getNewData((byte) 0));
		}
		return stack;
	}

	public static boolean isSolid(int id) {
		for (Byte b : nonsolid)
			if (b != null)
				if (b.byteValue() == id)
					return false;
		return true;
	}

	public static boolean canPlaceTorchOn(int id) {
		for (Byte b : nonsolid)
			if (b != null)
				if (b.byteValue() == id)
					return false;
		for (Byte b : noTorchPlacement)
			if (b != null)
				if (b.byteValue() == id)
					return false;
		return true;
	}

	public boolean loadCommand(String command) {
		try {
			Class<?> clazz = getClassLoader().loadClass(command);
			EssentialCommand cmd =
					(EssentialCommand) clazz.getConstructor(
							getClass()).newInstance(this);
			if (getCommand(cmd.name) == null)
				throw new Exception("Not in config file");
			registerCommand(cmd);
			log.info("Loaded Essential Command: " + command);
			return true;
		} catch (Exception e) {
			log.info("Failed to register " + command + ": "
					+ e.toString());
			log.info(e.toString());
			return false;
		}
	}

	public static ItemStack getStackFor(Block b) {
		Material mat = b.getType();
		if (mat.equals(Material.WOOL)
				|| mat.equals(Material.STEP))
			return new ItemStack(mat, 1, (short) 0, b.getData());
		if (mat.equals(Material.DOUBLE_STEP))
			return new ItemStack(Material.STEP, 2, (short) 0,
					b.getData());
		if (mat.equals(Material.COAL_ORE))
			return new ItemStack(Material.COAL);
		if (mat.equals(Material.DIAMOND_ORE))
			return new ItemStack(Material.DIAMOND);
		if (mat.equals(Material.GLOWING_REDSTONE_ORE))
			return new ItemStack(Material.REDSTONE, random(4, 6));
		if (mat.equals(Material.REDSTONE_ORE)
				|| mat.equals(Material.GLASS))
			return null;
		if (mat.equals(Material.REDSTONE_WIRE))
			return new ItemStack(Material.REDSTONE);
		if (mat.equals(Material.REDSTONE_TORCH_OFF))
			return new ItemStack(Material.REDSTONE_TORCH_ON);
		if (mat.equals(Material.BURNING_FURNACE))
			return new ItemStack(Material.FURNACE);
		if (mat.equals(Material.LAPIS_ORE))
			return ColoredItems.BLUE_DYE.getStack(random(4, 8));
		if (mat.equals(Material.WALL_SIGN)
				|| mat.equals(Material.SIGN_POST))
			return new ItemStack(Material.SIGN);
		if (mat.equals(Material.WOODEN_DOOR))
			return new ItemStack(Material.WOOD_DOOR);
		if (mat.equals(Material.IRON_DOOR_BLOCK))
			return new ItemStack(Material.IRON_DOOR);
		if (mat.equals(Material.DIODE_BLOCK_ON)
				|| mat.equals(Material.DIODE_BLOCK_OFF))
			return new ItemStack(Material.DIODE);
		return new ItemStack(mat, 1);
	}

	private static int random(int lower, int upper) {
		return (int) (Math.random() * (double) (upper - lower))
				+ lower;
	}
}
