package com.au.lachysh.mchg.abilities.forger;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

public class InstantSmelt extends Ability<PrepareItemCraftEvent> {

    private static final NamespacedKey COPPER_INSTANT_SMELT = new NamespacedKey(Main.getInstance(), "copper_instant_smelt");
    private static final NamespacedKey IRON_INSTANT_SMELT = new NamespacedKey(Main.getInstance(), "iron_instant_smelt");
    private static final NamespacedKey GOLD_INSTANT_SMELT = new NamespacedKey(Main.getInstance(), "gold_instant_smelt");
    private static boolean registered = false;


    private static final ItemStack COPPER_ITEM = new ItemStack(Material.COPPER_INGOT);
    private static final ItemStack IRON_ITEM = new ItemStack(Material.IRON_INGOT);
    private static final ItemStack GOLD_ITEM = new ItemStack(Material.GOLD_INGOT);

    private static ShapelessRecipe copperRecipe;
    private static ShapelessRecipe ironRecipe;
    private static ShapelessRecipe goldRecipe;

    public InstantSmelt() {
        super("Instant smelt", PrepareItemCraftEvent.class, 0, false);
        getCopperRecipe();
        getIronRecipe();
        getGoldRecipe();
        registerRecipes();
        registered = true;
    }

    @Override
    public boolean precondition(PrepareItemCraftEvent event) {
        return event.getRecipe().getResult().isSimilar(COPPER_ITEM)
                || event.getRecipe().getResult().isSimilar(IRON_ITEM)
                || event.getRecipe().getResult().isSimilar(GOLD_ITEM);

    }

    @Override
    public AbilityCallable<PrepareItemCraftEvent> getCallable() {
        return event -> {
            cooldown();
        };
    }

    private static ShapelessRecipe getCopperRecipe() {
        if (copperRecipe != null) return copperRecipe;
        var recipe = new ShapelessRecipe(COPPER_INSTANT_SMELT, COPPER_ITEM);
        recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.COAL, Material.CHARCOAL));
        recipe.addIngredient(Material.COPPER_ORE);
        copperRecipe = recipe;
        return copperRecipe;
    }

    private static ShapelessRecipe getIronRecipe() {
        if (ironRecipe != null) return ironRecipe;
        var recipe = new ShapelessRecipe(IRON_INSTANT_SMELT, IRON_ITEM);
        recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.COAL, Material.CHARCOAL));
        recipe.addIngredient(Material.IRON_ORE);
        ironRecipe = recipe;
        return ironRecipe;
    }

    private static ShapelessRecipe getGoldRecipe() {
        if (goldRecipe != null) return goldRecipe;
        var recipe = new ShapelessRecipe(GOLD_INSTANT_SMELT, GOLD_ITEM);
        recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.COAL, Material.CHARCOAL));
        recipe.addIngredient(Material.GOLD_ORE);
        goldRecipe = recipe;
        return goldRecipe;
    }

    // Register function only run once
    private void registerRecipes() {
        if (!registered) {
            Bukkit.getServer().addRecipe(copperRecipe);
            Bukkit.getServer().addRecipe(ironRecipe);
            Bukkit.getServer().addRecipe(goldRecipe);
        }
    }
}
