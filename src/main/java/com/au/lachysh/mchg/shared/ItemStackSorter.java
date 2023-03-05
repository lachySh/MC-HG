package com.au.lachysh.mchg.shared;

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public class ItemStackSorter implements Comparator<ItemStack> {
    public int compare(ItemStack a, ItemStack b) {
        return a.getItemMeta().getDisplayName().compareTo(b.getItemMeta().getDisplayName());
    }
}
