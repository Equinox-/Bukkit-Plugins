package essentials;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.Leaves;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sandstone;
import org.bukkit.material.SmoothBrick;
import org.bukkit.material.Step;
import org.bukkit.material.Tree;
import org.bukkit.material.Wool;

public enum ColoredItems {
	// Wool Start
	WHITE_WOOL(new Wool(DyeColor.WHITE)),
	ORANGE_WOOL(new Wool(DyeColor.ORANGE)),
	MAGENTA_WOOL(new Wool(DyeColor.MAGENTA)),
	LIGHTBLUE_WOOL(new Wool(DyeColor.LIGHT_BLUE)),
	YELLOW_WOOL(new Wool(DyeColor.YELLOW)),
	LIMEGREEN_WOOL(new Wool(DyeColor.LIME)),
	PINK_WOOL(new Wool(DyeColor.PINK)),
	GRAY__WOOL(new Wool(DyeColor.GRAY)),
	GREY_WOOL(new Wool(DyeColor.GRAY)),
	LIGHTGRAY_WOOL(new Wool(DyeColor.SILVER)),
	LIGHTGREY_WOOL(new Wool(DyeColor.SILVER)),
	SILVER_WOOL(new Wool(DyeColor.SILVER)),
	CYAN_WOOL(new Wool(DyeColor.CYAN)),
	PURPLE_WOOL(new Wool(DyeColor.PURPLE)),
	BLUE_WOOL(new Wool(DyeColor.BLUE)),
	BROWN_WOOL(new Wool(DyeColor.BROWN)),
	GREEN_WOOL(new Wool(DyeColor.GREEN)),
	RED_WOOL(new Wool(DyeColor.RED)),
	BLACK_WOOL(new Wool(DyeColor.BLACK)),

	// Dyes Start
	BLACK_DYE(getDye(DyeColor.BLACK)),
	RED_DYE(getDye(DyeColor.RED)),
	GREEN_DYE(getDye(DyeColor.GREEN)),
	BROWN_DYE(getDye(DyeColor.BROWN)),
	BLUE_DYE(getDye(DyeColor.BLUE)),
	PURPLE_DYE(getDye(DyeColor.PURPLE)),
	CYAN_DYE(getDye(DyeColor.CYAN)),
	LIGHTGRAY_DYE(getDye(DyeColor.SILVER)),
	LIGHTGREY_DYE(getDye(DyeColor.SILVER)),
	SILVER_DYE(getDye(DyeColor.SILVER)),
	GRAY_DYE(getDye(DyeColor.GRAY)),
	GREY_DYE(getDye(DyeColor.GRAY)),
	PINK_DYE(getDye(DyeColor.PINK)),
	LIMEGREEN_DYE(getDye(DyeColor.LIME)),
	YELLOW_DYE(getDye(DyeColor.YELLOW)),
	LIGHTBLUE_DYE(getDye(DyeColor.LIGHT_BLUE)),
	MAGENTA_DYE(getDye(DyeColor.MAGENTA)),
	ORANGE_DYE(getDye(DyeColor.ORANGE)),
	WHITE_DYE(getDye(DyeColor.WHITE)),

	// Logs Start
	OAK_LOG(new Tree(TreeSpecies.GENERIC)),
	BIRCH_LOG(new Tree(TreeSpecies.BIRCH)),
	FIR_LOG(new Tree(TreeSpecies.REDWOOD)),
	JUNGLE_LOG(new Tree(TreeSpecies.JUNGLE)),

	// Leaves Start
	OAK_LEAVES(new Leaves(TreeSpecies.GENERIC)),
	BIRCH_LEAVES(new Leaves(TreeSpecies.BIRCH)),
	FIR_LEAVES(new Leaves(TreeSpecies.REDWOOD)),
	JUNGLE_LEAVES(new Leaves(TreeSpecies.JUNGLE)),

	// Planks Start
	OAK_PLANKS(getPlanks(TreeSpecies.GENERIC)),
	BIRCH_PLANKS(getPlanks(TreeSpecies.BIRCH)),
	FIR_PLANKS(getPlanks(TreeSpecies.REDWOOD)),
	JUNGLE_PLANKS(getPlanks(TreeSpecies.JUNGLE)),
	OAK_PLANK(getPlanks(TreeSpecies.GENERIC)),
	BIRCH_PLANK(getPlanks(TreeSpecies.BIRCH)),
	FIR_PLANK(getPlanks(TreeSpecies.REDWOOD)),
	JUNGLE_PLANK(getPlanks(TreeSpecies.JUNGLE)),
	OAK_WOOD(getPlanks(TreeSpecies.GENERIC)),
	BIRCH_WOOD(getPlanks(TreeSpecies.BIRCH)),
	FIR_WOOD(getPlanks(TreeSpecies.REDWOOD)),
	JUNGLE_WOOD(getPlanks(TreeSpecies.JUNGLE)),

	// Smooth Brick Start
	MOSSY_SMOOTH_BRICK(new SmoothBrick(
			Material.MOSSY_COBBLESTONE)),
	STONE_SMOOTH_BRICK(new SmoothBrick(Material.STONE)),
	COBBLESTONE_SMOOTH_BRICK(new SmoothBrick(
			Material.COBBLESTONE)),
	COBBLE_SMOOTH_BRICK(new SmoothBrick(Material.COBBLESTONE)),

	// Single Slabs Start
	STONE_SLAB(getStep(Material.STONE)),
	SANDSTONE_SLAB(getStep(Material.SANDSTONE)),
	WOODEN_SLAB(getStep(Material.WOOD)),
	COBBLESTONE_SLAB(getStep(Material.COBBLESTONE)),
	STONE_STEP(getStep(Material.STONE)),
	SANDSTONE_STEP(getStep(Material.SANDSTONE)),
	WOODEN_STEP(getStep(Material.WOOD)),
	SMOOTH_BRICK_STEP(getStep(Material.SMOOTH_BRICK)),
	SMOOTH_BRICK_SLAB(getStep(Material.SMOOTH_BRICK)),
	STONE_BRICK_STEP(getStep(Material.SMOOTH_BRICK)),
	STONE_BRICK_SLAB(getStep(Material.SMOOTH_BRICK)),
	COBBLESTONE_STEP(getStep(Material.COBBLESTONE)),

	// Double Slabs Start
	STONE_DOUBLE_SLAB(getDblStep(Material.STONE)),
	SANDSTONE_DOUBLE_SLAB(getDblStep(Material.SANDSTONE)),
	WOODEN_DOUBLE_SLAB(getDblStep(Material.WOOD)),
	COBBLESTONE_DOUBLE_SLAB(getDblStep(Material.COBBLESTONE)),
	STONE_DOUBLE_STEP(getDblStep(Material.STONE)),
	SANDSTONE_DOUBLE_STEP(getDblStep(Material.SANDSTONE)),
	WOODEN_DOUBLE_STEP(getDblStep(Material.WOOD)),
	COBBLESTONE_DOUBLE_STEP(getDblStep(Material.COBBLESTONE)),
	STONE_DBL_SLAB(getDblStep(Material.STONE)),
	SANDSTONE_DBL_SLAB(getDblStep(Material.SANDSTONE)),
	WOODEN_DBL_SLAB(getDblStep(Material.WOOD)),
	COBBLESTONE_DBL_SLAB(getDblStep(Material.COBBLESTONE)),
	STONE_DBL_STEP(getDblStep(Material.STONE)),
	SANDSTONE_DBL_STEP(getDblStep(Material.SANDSTONE)),
	WOODEN_DBL_STEP(getDblStep(Material.WOOD)),
	COBBLESTONE_DBL_STEP(getDblStep(Material.COBBLESTONE)),
	SMOOTH_BRICK_DBL_STEP(getDblStep(Material.SMOOTH_BRICK)),
	SMOOTH_BRICK_DBL_SLAB(getDblStep(Material.SMOOTH_BRICK)),
	STONE_BRICK_DBL_STEP(getDblStep(Material.SMOOTH_BRICK)),
	STONE_BRICK_DBL_SLAB(getDblStep(Material.SMOOTH_BRICK)),
	SMOOTH_BRICK_DOUBLE_STEP(getDblStep(Material.SMOOTH_BRICK)),
	SMOOTH_BRICK_DOUBLE_SLAB(getDblStep(Material.SMOOTH_BRICK)),
	STONE_BRICK_DOUBLE_STEP(getDblStep(Material.SMOOTH_BRICK)),
	STONE_BRICK_DOUBLE_SLAB(getDblStep(Material.SMOOTH_BRICK)),

	// Sandstone start
	CRACKED_SANDSTONE(new Sandstone(SandstoneType.CRACKED)),
	GLYPHED_SANDSTONE(new Sandstone(SandstoneType.GLYPHED)),
	GLYPH_SANDSTONE(new Sandstone(SandstoneType.GLYPHED)),
	SMOOTH_SANDSTONE(new Sandstone(SandstoneType.SMOOTH)), ;

	final MaterialData val;

	private ColoredItems(MaterialData val) {
		this.val = val;
	}

	public ItemStack getStack() {
		return getStack(1);
	}

	public ItemStack getStack(int count) {
		ItemStack s = new ItemStack(val.getItemType(), count);
		s.setData(val);
		s.setDurability(val.getData());
		return s;
	}

	public static ColoredItems lookup(String name) {
		String nName = name.replaceAll("_", "");
		for (ColoredItems i : values()) {
			String iName = i.name().replaceAll("_", "");
			if (iName.equalsIgnoreCase(nName))
				return i;
		}
		return null;
	}

	public static boolean isColorable(ItemStack stack) {
		return stack.getType().equals(Material.LOG)
				|| stack.getType().equals(Material.INK_SACK)
				|| stack.getType().equals(Material.WOOL)
				|| stack.getType().equals(Material.STEP)
				|| stack.getType().equals(Material.DOUBLE_STEP)
				|| stack.getType().equals(Material.SAPLING)
				|| stack.getType().equals(Material.COAL);
	}

	public static boolean isColorable(Block stack) {
		return stack.getType().equals(Material.LOG)
				|| stack.getType().equals(Material.INK_SACK)
				|| stack.getType().equals(Material.WOOL)
				|| stack.getType().equals(Material.STEP)
				|| stack.getType().equals(Material.DOUBLE_STEP)
				|| stack.getType().equals(Material.SAPLING)
				|| stack.getType().equals(Material.COAL);
	}

	public static Dye getDye(final DyeColor col) {
		Dye d = new Dye();
		d.setColor(col);
		return d;
	}

	public static Step getStep(final Material color) {
		Step s = new Step(Material.STEP);
		s.setMaterial(color);
		return s;
	}

	public static Step getDblStep(final Material color) {
		Step s = new Step(Material.DOUBLE_STEP);
		s.setMaterial(color);
		return s;
	}

	public static Tree getPlanks(final TreeSpecies type) {
		Tree t = new Tree(Material.WOOD);
		t.setSpecies(type);
		return t;
	}
}