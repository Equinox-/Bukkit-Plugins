package essentials;

import net.minecraft.server.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

public class CuboidBlock {
	public Material type;
	public byte data;
	public TileEntity entity;
	public int x, y, z;

	public CuboidBlock() {

	}

	public CuboidBlock(Block b) {
		setBlock(b);
	}

	public void setBlock(Block b) {
		setLocation(b.getLocation());
		data = b.getData();
		type = b.getType();
		entity =
				copyEntityData(((CraftWorld) b.getWorld())
						.getTileEntityAt(x, y, z));
	}

	public void translateOrigin(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void setLocation(Location l) {
		this.x = l.getBlockX();
		this.y = l.getBlockY();
		this.z = l.getBlockZ();
	}

	@Override
	public CuboidBlock clone() {
		CuboidBlock cN = new CuboidBlock();
		cN.type = this.type;
		cN.data = this.data;
		cN.x = x;
		cN.y = y;
		cN.z = z;
		return cN;
	}

	public void saveToBlock(Block rep) {
		rep.setTypeIdAndData(type.getId(), data,
				Essentials.hasPhysics(type));
		if (entity != null) {
			((CraftWorld) rep.getWorld()).getHandle()
					.setTileEntity(rep.getX(), rep.getY(),
							rep.getZ(), entity);
			((CraftWorld) rep.getWorld()).getHandle().manager
					.flagDirty(rep.getX(), rep.getY(),
							rep.getZ());
		}
	}

	public static TileEntity copyEntityData(TileEntity entity) {
		if (entity != null) {
			if (entity instanceof TileEntitySign) {
				TileEntitySign orig = (TileEntitySign) entity;
				TileEntitySign copy = new TileEntitySign();
				copy.isEditable = orig.isEditable;
				copy.b = orig.b;
				copy.lines = orig.lines.clone();
				return copy;
			} else if (entity instanceof TileEntityChest) {
				TileEntityChest orig = (TileEntityChest) entity;
				TileEntityChest copy = new TileEntityChest();
				for (int i = 0; i < orig.getContents().length; i++) {
					ItemStack old = orig.getContents()[i];
					copy.setItem(i, new ItemStack(old.id,
							old.count, old.getData()));
				}
				return copy;
			} else if (entity instanceof TileEntityNote) {
				TileEntityNote orig = (TileEntityNote) entity;
				TileEntityNote copy = new TileEntityNote();
				copy.note = orig.note;
				copy.b = orig.b;
				return copy;
			} else if (entity instanceof TileEntityMobSpawner) {
				TileEntityMobSpawner orig =
						(TileEntityMobSpawner) entity;
				TileEntityMobSpawner copy =
						new TileEntityMobSpawner();
				copy.b = orig.b;
				copy.c = orig.c;
				copy.mobName = orig.mobName;
				copy.spawnDelay = orig.spawnDelay;
				return copy;
			} else if (entity instanceof TileEntityDispenser) {
				TileEntityDispenser orig =
						(TileEntityDispenser) entity;
				TileEntityDispenser copy =
						new TileEntityDispenser();
				for (int i = 0; i < orig.getContents().length; i++) {
					ItemStack old = orig.getContents()[i];
					copy.setItem(i, new ItemStack(old.id,
							old.count, old.getData()));
				}
				return copy;
			} else if (entity instanceof TileEntityFurnace) {
				TileEntityFurnace orig =
						(TileEntityFurnace) entity;
				TileEntityFurnace copy = new TileEntityFurnace();
				for (int i = 0; i < orig.getContents().length; i++) {
					ItemStack old = orig.getContents()[i];
					copy.setItem(i, new ItemStack(old.id,
							old.count, old.getData()));
				}
				copy.ticksForCurrentFuel =
						orig.ticksForCurrentFuel;
				copy.burnTime = orig.burnTime;
				copy.cookTime = orig.cookTime;
				return copy;
			}
		}
		return null;
	}

}
