package com.au.lachysh.mchg.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LootManager {
    public LootManager() {
        fillFood();
        fillEFood();
        fillWeapons();
        fillEWeapons();
        fillArmor();
        fillEArmor();
        fillOther();
        fillEOther();
        Bukkit.getLogger().info("Loot tables filled!");
    }
    List<ItemStack> food = new ArrayList<>();
    List<ItemStack> efood = new ArrayList<>();
    List<ItemStack> weapons = new ArrayList<>();
    List<ItemStack> eweapons = new ArrayList<>();
    List<ItemStack> armor = new ArrayList<>();
    List<ItemStack> earmor = new ArrayList<>();
    List<ItemStack> other = new ArrayList<>();
    List<ItemStack> eother = new ArrayList<>();
    void fillFood() {
        food.add(new ItemStack(Material.BREAD, 1));
        food.add(new ItemStack(Material.APPLE, 1));
        food.add(new ItemStack(Material.CARROT, 1));
        food.add(new ItemStack(Material.POTATO, 1));
        food.add(new ItemStack(Material.MELON_SLICE, 1));
        food.add(new ItemStack(Material.COOKIE, 1));
        food.add(new ItemStack(Material.BEEF, 1));
        food.add(new ItemStack(Material.PORKCHOP, 1));
        food.add(new ItemStack(Material.CHICKEN, 1));
        food.add(new ItemStack(Material.MUTTON, 1));
        food.add(new ItemStack(Material.COD, 1));
        food.add(new ItemStack(Material.SALMON, 1));
    }
    void fillEFood(){
        efood.add(new ItemStack(Material.APPLE, 1));
        efood.add(new ItemStack(Material.GOLDEN_APPLE, 1));
        efood.add(new ItemStack(Material.GOLDEN_CARROT, 1));
        efood.add(new ItemStack(Material.BAKED_POTATO, 1));
        efood.add(new ItemStack(Material.PUMPKIN_PIE, 1));
        efood.add(new ItemStack(Material.BREAD, 1));
        efood.add(new ItemStack(Material.COOKED_BEEF, 1));
        efood.add(new ItemStack(Material.COOKED_PORKCHOP, 1));
        efood.add(new ItemStack(Material.COOKED_CHICKEN, 1));
        efood.add(new ItemStack(Material.COOKED_MUTTON, 1));
        efood.add(new ItemStack(Material.COOKED_COD, 1));
        efood.add(new ItemStack(Material.COOKED_SALMON, 1));
    }
    void fillWeapons(){
        weapons.add(new ItemStack(Material.FISHING_ROD,1));
        weapons.add(new ItemStack(Material.WOODEN_SWORD,1));
        weapons.add(new ItemStack(Material.WOODEN_AXE,1));
        weapons.add(new ItemStack(Material.STONE_SWORD,1));
        weapons.add(new ItemStack(Material.STONE_AXE,1));
        weapons.add(new ItemStack(Material.BOW,1));
    }
    void fillEWeapons(){
        eweapons.add(new ItemStack(Material.STONE_SWORD,1));
        eweapons.add(new ItemStack(Material.STONE_AXE,1));
        eweapons.add(new ItemStack(Material.BOW,1));
    }
    void fillArmor(){
        armor.add(new ItemStack(Material.LEATHER_HELMET,1));
        armor.add(new ItemStack(Material.LEATHER_CHESTPLATE,1));
        armor.add(new ItemStack(Material.LEATHER_LEGGINGS,1));
        armor.add(new ItemStack(Material.LEATHER_BOOTS,1));
        armor.add(new ItemStack(Material.GOLDEN_HELMET,1));
        armor.add(new ItemStack(Material.GOLDEN_LEGGINGS,1));
        armor.add(new ItemStack(Material.GOLDEN_BOOTS,1));
        armor.add(new ItemStack(Material.CHAINMAIL_HELMET,1));
        armor.add(new ItemStack(Material.CHAINMAIL_BOOTS,1));
    }
    void fillEArmor(){
        earmor.add(new ItemStack(Material.GOLDEN_CHESTPLATE,1));
        earmor.add(new ItemStack(Material.GOLDEN_LEGGINGS,1));
        earmor.add(new ItemStack(Material.CHAINMAIL_HELMET,1));
        earmor.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE,1));
        earmor.add(new ItemStack(Material.CHAINMAIL_LEGGINGS,1));
        earmor.add(new ItemStack(Material.CHAINMAIL_BOOTS,1));
        earmor.add(new ItemStack(Material.IRON_HELMET,1));
        earmor.add(new ItemStack(Material.IRON_CHESTPLATE,1));
        earmor.add(new ItemStack(Material.IRON_LEGGINGS,1));
        earmor.add(new ItemStack(Material.IRON_BOOTS,1));
    }
    void fillOther(){
        other.add(new ItemStack(Material.BOWL,1));
        other.add(new ItemStack(Material.BROWN_MUSHROOM,1));
        other.add(new ItemStack(Material.RED_MUSHROOM,1));
        other.add(new ItemStack(Material.FLINT,1));
        other.add(new ItemStack(Material.STICK,1));
        other.add(new ItemStack(Material.FEATHER,1));
        other.add(new ItemStack(Material.ARROW,1));
        other.add(new ItemStack(Material.LEATHER,1));
        other.add(new ItemStack(Material.GOLD_INGOT,1));
        other.add(new ItemStack(Material.IRON_INGOT,1));
    }
    void fillEOther(){
        eother.add(new ItemStack(Material.STICK,1));
        eother.add(new ItemStack(Material.ARROW,1));
        eother.add(new ItemStack(Material.IRON_INGOT,1));
        eother.add(new ItemStack(Material.DIAMOND,1));
        eother.add(new ItemStack(Material.FLINT_AND_STEEL,1));
        eother.add(new ItemStack(Material.EXPERIENCE_BOTTLE,1));
    }
    Map<Location, Inventory> chests = new HashMap<>();
    public void createRandomChest(Inventory chest) {
        int randInt;
        int randItemInt;
        for(int i = 0; i<chest.getSize(); i++){
            Random r = new Random();
            randInt = r.nextInt(101);
            if (randInt < 4) {
                randItemInt = r.nextInt(food.size());
                chest.setItem(i, food.get(randItemInt));
            }
            else if (randInt == 4) {
                randItemInt = r.nextInt(efood.size());
                chest.setItem(i, efood.get(randItemInt));
            }
            else if (randInt < 9) {
                randItemInt = r.nextInt(weapons.size());
                chest.setItem(i, weapons.get(randItemInt));
            }
            else if (randInt == 9) {
                randItemInt = r.nextInt(eweapons.size());
                chest.setItem(i, eweapons.get(randItemInt));
            }
            else if (randInt < 14) {
                randItemInt = r.nextInt(armor.size());
                chest.setItem(i, armor.get(randItemInt));
            }
            else if (randInt == 14) {
                randItemInt = r.nextInt(earmor.size());
                chest.setItem(i, earmor.get(randItemInt));
            }
            else if (randInt < 21) {
                randItemInt = r.nextInt(other.size());
                chest.setItem(i, other.get(randItemInt));
            }
            else if (randInt == 21) {
                randItemInt = r.nextInt(eother.size());
                chest.setItem(i, eother.get(randItemInt));
            }
            else chest.setItem(i, new ItemStack(Material.AIR, 1));
        }
    }
    public Inventory getChestContents(Location loc) {
        return chests.getOrDefault(loc,null);
    }
    public void storeChestContents(Location loc, Inventory inv) {
        chests.put(loc, inv);
    }
    public void refillAllChests() { chests.clear(); }
}

