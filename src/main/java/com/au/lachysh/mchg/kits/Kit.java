package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.Ability;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Kit implements Comparable<Kit> {
    private String id;
    private String title;
    private String description;
    private Boolean goodForWorldUnbreakable;
    private KitType kitType;
    private List<ItemStack> kitItems;
    private List<Ability> kitAbilities;
    private ItemStack kitDisplayItem;

    public Kit(String id,
               String title,
               String description,
               Boolean goodForWorldUnbreakable,
               KitType kitType,
               Material displayMaterial,
               List<ItemStack> kitItems,
               List<Ability> kitAbilities) {
        this.id = id;
        this.description = description;
        this.goodForWorldUnbreakable = goodForWorldUnbreakable;
        this.kitType = kitType;
        this.kitItems = kitItems;
        this.kitAbilities = kitAbilities;

        switch (kitType) {
            case FIGHTER:
                this.title = ChatColor.GOLD + title;
                break;
            case UTILITY:
                this.title = ChatColor.GREEN + title;
                break;
            case DEFENSIVE:
                this.title = ChatColor.BLUE + title;
                break;
        }

        this.kitDisplayItem = createKitDisplayItem(displayMaterial);
    }

    public Kit(String id,
               String title,
               String description,
               Boolean goodForWorldUnbreakable,
               KitType kitType,
               ItemStack displayItem,
               List<ItemStack> kitItems,
               List<Ability> kitAbilities) {
        this.id = id;
        this.description = description;
        this.goodForWorldUnbreakable = goodForWorldUnbreakable;
        this.kitType = kitType;
        this.kitItems = kitItems;
        this.kitAbilities = kitAbilities;

        switch (kitType) {
            case FIGHTER:
                this.title = ChatColor.GOLD + title;
                break;
            case UTILITY:
                this.title = ChatColor.GREEN + title;
                break;
            case DEFENSIVE:
                this.title = ChatColor.BLUE + title;
                break;
        }

        this.kitDisplayItem = createKitDisplayItem(displayItem);
    }

    public List<ItemStack> getKitItems() {
        return kitItems;
    }

    public List<Ability> getKitAbilities() {
        return kitAbilities;
    }

    public void setKitAbilities(List<Ability> abilities) {
        this.kitAbilities = abilities;
    }

    public String getKitName() {
        return title;
    }

    public ItemStack getKitDisplayItem() {
        return kitDisplayItem;
    }

    public ItemStack createKitDisplayItem(Material displayMaterial) {
        if (kitDisplayItem != null) return kitDisplayItem;

        ItemStack newItem = new ItemStack(displayMaterial);
        ItemMeta newItemMeta = newItem.getItemMeta();
        newItemMeta.setDisplayName(title);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        newItemMeta.setLore(getFormattedLore());
        newItem.setItemMeta(newItemMeta);

        kitDisplayItem = newItem;
        return kitDisplayItem;
    }

    public ItemStack createKitDisplayItem(ItemStack itemStack) {
        if (kitDisplayItem != null) return kitDisplayItem;

        ItemMeta newItemMeta = itemStack.getItemMeta();
        newItemMeta.setDisplayName(title);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        newItemMeta.setLore(getFormattedLore());
        itemStack.setItemMeta(newItemMeta);

        kitDisplayItem = itemStack;
        return kitDisplayItem;
    }

    public String id() {
        return id;
    }

    private List<String> getFormattedLore() {
        var descList = Arrays.asList(description.split("\n"));
        var newList = new ArrayList<String>();
        for (String s : descList) {
            newList.add(ChatColor.GRAY + s);
        }

        if (!goodForWorldUnbreakable) {
            newList.add("");
            newList.add(ChatColor.RED + " ▶ " + ChatColor.ITALIC + "This kit is not recommended for maps");
            newList.add(ChatColor.RED + "   " + ChatColor.ITALIC + "where building / breaking is disabled!");
        }
        return newList;
    }

    public int compareTo(Kit b) {
        return this.getKitName().compareTo(b.getKitName());
    }

    public Boolean isGoodForWorkUnbreakable() {
        return goodForWorldUnbreakable;
    }
}
