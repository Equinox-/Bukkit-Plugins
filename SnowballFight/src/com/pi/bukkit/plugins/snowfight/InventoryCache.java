package com.pi.bukkit.plugins.snowfight;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryCache {
    private ItemStack[] items = new ItemStack[36];
    private ItemStack[] armor = new ItemStack[4];

    public void restoreInventory(Player player) {
	for (int i = 0; i < items.length; i++) {
	    player.getInventory().setItem(i,
		    items[i] != null ? items[i].clone() : null);
	}
	ItemStack[] aClone = new ItemStack[armor.length];
	for (int i = 0; i < armor.length; i++) {
	    aClone[i] = armor[i] != null ? armor[i].clone() : null;
	}
	player.getInventory().setArmorContents(aClone);
    }

    public void cloneInventory(Player player) {
	ItemStack[] contents = player.getInventory().getContents();
	ItemStack[] pArmor = player.getInventory().getArmorContents();
	if (items.length != contents.length)
	    items = new ItemStack[contents.length];
	if (pArmor.length != armor.length)
	    armor = new ItemStack[pArmor.length];
	for (int i = 0; i < contents.length; i++)
	    items[i] = contents[i] != null ? contents[i].clone() : null;
	for (int i = 0; i < pArmor.length; i++)
	    armor[i] = pArmor[i] != null ? pArmor[i].clone() : null;
    }
}
