package com.au.lachysh.mchg.abilities.endermage;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Endermage;
import com.au.lachysh.mchg.managers.ChatManager;
import org.bukkit.*;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class TeleportEntities extends Ability<BlockPlaceEvent> {

    private int counter;
    private static PotionEffect invincible3Seconds = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 10);
    private static PotionEffect weakness3Seconds = new PotionEffect(PotionEffectType.WEAKNESS, 60, 10);
    private static ChatManager cm;

    public TeleportEntities() {
        super("Endermage", BlockPlaceEvent.class, 10, false);
        counter = 0;
        if (cm == null) {
            cm = Main.getCm();
        }
    }

    @Override
    public boolean precondition(BlockPlaceEvent event) {
        return event.getBlockPlaced().getBlockData().getMaterial() == Material.END_PORTAL_FRAME;
    }

    @Override
    public AbilityCallable<BlockPlaceEvent> getCallable() {
        return event -> {
            Location eventLoc = event.getBlockPlaced().getLocation();
            World world = event.getBlockPlaced().getWorld();
            Collection<Entity> teleported = new HashSet<>();
            teleported.add(event.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    Collection<Entity> nearbyEntities = world.getNearbyEntities(eventLoc, 3, 350, 3, (e) -> e instanceof Player || e instanceof Animals);
                    for (Entity e : nearbyEntities) {
                        if (!teleported.contains(e)) {
                            e.teleport(eventLoc);
                            teleported.add(e);
                            ((LivingEntity) e).addPotionEffect(invincible3Seconds);
                            ((LivingEntity) e).addPotionEffect(weakness3Seconds);
                            if (e instanceof Player) {
                                e.sendMessage(cm.getPrefix() + ChatColor.RED + "You have been teleported by an Endermage! You are invincible, but can't hurt others for 3 seconds.");
                                world.playSound(eventLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                            }
                        }
                    }
                    if (counter >= 20) {
                        counter = 0;
                        endEndermageRun(event, Endermage.endermagesPortal());
                        cancel();
                    }
                    counter++;
                }
            }.runTaskTimer(Main.getInstance(), 0, 5);

            cooldown();
        };
    }

    private void endEndermageRun(BlockPlaceEvent event, ItemStack endermageItem) {
        event.getBlockPlaced().setType(Material.AIR);
        HashMap<Integer, ItemStack> failed = event.getPlayer().getInventory().addItem(endermageItem);
        if (!failed.isEmpty()) {
            event.getBlockPlaced().getWorld().dropItemNaturally(event.getBlockPlaced().getLocation(), endermageItem);
        }
    }
}
