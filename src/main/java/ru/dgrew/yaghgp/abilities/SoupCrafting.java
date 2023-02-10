package ru.dgrew.yaghgp.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dgrew.yaghgp.Main;

public class SoupCrafting extends Ability<PrepareItemCraftEvent>  {

    private static final NamespacedKey SEED_SOUP_KEY = new NamespacedKey(Main.getInstance(), "seed_soup");
    private static final NamespacedKey CACTI_SOUP_KEY = new NamespacedKey(Main.getInstance(), "cacti_soup");
    private static final NamespacedKey COCOA_SOUP_KEY = new NamespacedKey(Main.getInstance(), "cocoa_soup");
    private static boolean registered = false;

    private static ItemStack getCustomSoupItem(String name) {
        ItemStack cactiSoupItem = new ItemStack(Material.MUSHROOM_STEW, 1);
        ItemMeta itemMeta = cactiSoupItem.getItemMeta();
        itemMeta.setDisplayName(name);
        cactiSoupItem.setItemMeta(itemMeta);
        return cactiSoupItem;
    }

    private static final ItemStack SEED_SOUP_ITEM = getCustomSoupItem("Seed Soup");
    private static final ItemStack CACTI_SOUP_ITEM = getCustomSoupItem("Cacti Soup");
    private static final ItemStack COCOA_SOUP_ITEM = getCustomSoupItem("Cocoa Soup");

    private static ShapelessRecipe seedSoupRecipe;
    private static ShapelessRecipe cactiSoupRecipe;
    private static ShapelessRecipe cocoaSoupRecipe;

    public SoupCrafting() {
        super("Soup crafting", 0, false);
        getSeedSoupRecipe();
        getCactiSoupRecipe();
        getCocoaSoupRecipe();
        registerRecipes();
        registered = true;
    }

    @Override
    public boolean precondition(PrepareItemCraftEvent event) {
        return event.getRecipe().getResult().isSimilar(SEED_SOUP_ITEM)
                || event.getRecipe().getResult().isSimilar(CACTI_SOUP_ITEM)
                || event.getRecipe().getResult().isSimilar(COCOA_SOUP_ITEM);

    }

    @Override
    public AbilityCallable<PrepareItemCraftEvent> getCallable() {
        return event -> {
            cooldown();
        };
    }

    private static ShapelessRecipe getSeedSoupRecipe() {
        if (seedSoupRecipe != null) return seedSoupRecipe;
        var recipe = new ShapelessRecipe(SEED_SOUP_KEY, SEED_SOUP_ITEM);
        recipe.addIngredient(Material.BOWL);
        recipe.addIngredient(4, Material.WHEAT_SEEDS);
        seedSoupRecipe = recipe;
        return seedSoupRecipe;
    }

    private static ShapelessRecipe getCactiSoupRecipe() {
        if (cactiSoupRecipe != null) return cactiSoupRecipe;
        var recipe = new ShapelessRecipe(CACTI_SOUP_KEY, CACTI_SOUP_ITEM);
        recipe.addIngredient(Material.BOWL);
        recipe.addIngredient(3, Material.CACTUS);
        cactiSoupRecipe = recipe;
        return cactiSoupRecipe;
    }

    private static ShapelessRecipe getCocoaSoupRecipe() {
        if (cocoaSoupRecipe != null) return cocoaSoupRecipe;
        var recipe = new ShapelessRecipe(COCOA_SOUP_KEY, COCOA_SOUP_ITEM);
        recipe.addIngredient(Material.BOWL);
        recipe.addIngredient(3, Material.COCOA_BEANS);
        cocoaSoupRecipe = recipe;
        return cocoaSoupRecipe;
    }

    // Register function only run once
    private void registerRecipes() {
        if (!registered) {
            Bukkit.getServer().addRecipe(seedSoupRecipe);
            Bukkit.getServer().addRecipe(cactiSoupRecipe);
            Bukkit.getServer().addRecipe(cocoaSoupRecipe);
        }
    }
}
