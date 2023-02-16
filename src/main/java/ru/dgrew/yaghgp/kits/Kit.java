package ru.dgrew.yaghgp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dgrew.yaghgp.abilities.Ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Kit implements Comparable<Kit> {
    private String id;
    private String title;
    private String description;
    private KitType kitType;
    private List<ItemStack> kitItems;
    private List<Ability> kitAbilities;
    private ItemStack kitDisplayItem;

    public Kit(String id,
               String title,
               String description,
               KitType kitType,
               Material displayMaterial,
               List<ItemStack> kitItems,
               List<Ability> kitAbilities) {
        this.id = id;
        this.description = description;
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
               KitType kitType,
               ItemStack displayItem,
               List<ItemStack> kitItems,
               List<Ability> kitAbilities) {
        this.id = id;
        this.description = description;
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
        ArrayList<String> newItemLore = new ArrayList<>();
        newItemLore.addAll(formatLore(new ArrayList<String>(Arrays.asList(description.split("\n")))));
        newItemMeta.setLore(newItemLore);
        newItem.setItemMeta(newItemMeta);

        kitDisplayItem = newItem;
        return kitDisplayItem;
    }

    public ItemStack createKitDisplayItem(ItemStack itemStack) {
        if (kitDisplayItem != null) return kitDisplayItem;

        ItemMeta newItemMeta = itemStack.getItemMeta();
        newItemMeta.setDisplayName(title);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> newItemLore = new ArrayList<>();
        newItemLore.addAll(formatLore(new ArrayList<String>(Arrays.asList(description.split("\n")))));
        newItemMeta.setLore(newItemLore);
        itemStack.setItemMeta(newItemMeta);

        kitDisplayItem = itemStack;
        return kitDisplayItem;
    }

    public String id() {
        return id;
    }

    private static List<String> formatLore(List<String> lore) {
        var newList = new ArrayList<String>();
        for (String s : lore) {
            newList.add(ChatColor.GRAY + s);
        }
        return newList;
    }

    public int compareTo(Kit b) {
        return this.getKitName().compareTo(b.getKitName());
    }
}
