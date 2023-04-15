package com.au.lachysh.mchg.abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.PlayerManager;

import java.util.NoSuchElementException;

public class AbilityListener implements Listener {

    private PlayerManager pm;

    public AbilityListener() {
        pm = Main.getPlm();
    }

    private boolean tryPreconditionSafely(Ability ability, Event event) {
        try {
            if (ability.getType().isInstance(event)) return ability.precondition(event);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void notifyOnCooldown(Player player, Ability ability) {
        sendActionbar(player, ChatColor.RED + ability.getName() + " is on cooldown for " + ability.getCurrentCooldown() + " more seconds!");
    }

    private void notifyOnCooldown(HumanEntity entity, Ability ability) {
        sendActionbar(Bukkit.getPlayer(entity.getName()), ChatColor.RED + ability.getName() + " is on cooldown for " + ability.getCurrentCooldown() + " more seconds!");
    }

    private void notifyOnDisabled(Player player, Ability ability) {
        sendActionbar(player, ChatColor.RED + ability.getName() + " has been disabled!");
    }

    private void notifyOnDisabled(HumanEntity entity, Ability ability) {
        sendActionbar(Bukkit.getPlayer(entity.getName()), ChatColor.RED + ability.getName() + " has been disabled!");
    }

    private void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event.getPlayer(), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event.getPlayer(), selectedAbility);
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
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            event.getInventory().setResult(new ItemStack(Material.AIR));
                            notifyOnDisabled(event.getViewers().get(0), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            event.getInventory().setResult(new ItemStack(Material.AIR));
                            notifyOnCooldown(event.getViewers().get(0), selectedAbility);
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            event.setCancelled(true);
                            notifyOnDisabled(event.getPlayer(), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            event.setCancelled(true);
                            notifyOnCooldown(event.getPlayer(), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerTakesDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        pm.findTribute((Player) event.getEntity()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(((Player) event.getEntity()), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(((Player) event.getEntity()), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onProjectileHitShooter(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        pm.findTribute((Player) event.getEntity().getShooter()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(((Player) event.getEntity().getShooter()), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(((Player) event.getEntity().getShooter()), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onPlayerDealsDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        pm.findTribute((Player) event.getDamager()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(((Player) event.getDamager()), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(((Player) event.getDamager()), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event.getPlayer(), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event.getPlayer(), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event.getPlayer(), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event.getPlayer(), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event.getPlayer(), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event.getPlayer(), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }

    @EventHandler
    public void onPlayerBreakBlockDropsItem(BlockDropItemEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event.getPlayer(), selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event.getPlayer(), selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                }
        );
    }
}
