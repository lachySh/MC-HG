package ru.dgrew.yaghgp.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.PlayerManager;

import java.util.NoSuchElementException;

public class AbilityListener implements Listener {

    private PlayerManager pm;

    public AbilityListener() {
        pm = Main.getPlm();
    }

    private boolean tryPreconditionSafely(Ability ability, Event event) {
        try {
            return ability.precondition(event);
        } catch (ClassCastException e) {
            return false;
        }
    }

    private void notifyOnCooldown(PlayerEvent event, Ability ability) {
        event.getPlayer().sendTitle("", "§c" + ability.getName() + " is on cooldown for " + ability.getCurrentCooldown() + " more seconds!", 5, 20, 5);
    }

    private void notifyOnCooldown(PrepareItemCraftEvent event, Ability ability) {
        event.getViewers().get(0).sendMessage("", "§c" + ability.getName() + " is on cooldown for " + ability.getCurrentCooldown() + " more seconds!");
    }

    private void notifyOnDisabled(PlayerEvent event, Ability ability) {
        event.getPlayer().sendTitle("", "§c" + ability.getName() + " has been disabled!!", 5, 20, 5);
    }

    private void notifyOnDisabled(PrepareItemCraftEvent event, Ability ability) {
        event.getViewers().get(0).sendMessage("", "§c" + ability.getName() + " has been disabled!!");
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream().parallel()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event, selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event, selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onPrepareCraftEventDisallow(PrepareItemCraftEvent event) {
        if (isRecipeDefault(event.getRecipe())) return;
        pm.findTribute(event.getViewers().get(0)).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream().parallel()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            event.getInventory().setResult(new ItemStack(Material.AIR));
                            notifyOnDisabled(event, selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            event.getInventory().setResult(new ItemStack(Material.AIR));
                            notifyOnCooldown(event, selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                        event.getInventory().setResult(new ItemStack(Material.AIR));
                    }
                }
        );
    }

    private boolean isRecipeDefault(Recipe recipe) {
        if (recipe instanceof ShapelessRecipe) {
            var shapeless = (ShapelessRecipe) recipe;
            var key = shapeless.getKey().getNamespace().toUpperCase().strip();
            return key.equals("MINECRAFT") || key.equals("BUKKIT");
        } else if (recipe instanceof ShapedRecipe) {
            var shaped = (ShapedRecipe) recipe;
            var key = shaped.getKey().getNamespace().toUpperCase().strip();
            return key.equals("MINECRAFT") || key.equals("BUKKIT");
        } else {
            return true;
        }
    }
}
