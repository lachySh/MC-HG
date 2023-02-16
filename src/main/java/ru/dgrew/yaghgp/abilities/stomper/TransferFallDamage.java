package ru.dgrew.yaghgp.abilities.stomper;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.abilities.AbilityCallable;
import ru.dgrew.yaghgp.managers.PlayerManager;

import java.util.*;

public class TransferFallDamage extends Ability<EntityDamageEvent> {

    private static final int RADIUS = 2;

    public TransferFallDamage() {
        super("Transfer fall damage", EntityDamageEvent.class, 0, false);
    }

    @Override
    public boolean precondition(EntityDamageEvent event) {
        return event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL;
    }

    @Override
    public AbilityCallable<EntityDamageEvent> getCallable() {
        return event -> {
            event.setCancelled(true);
            // Set max health loss to 2 hearts
            Player p = (Player) event.getEntity();
            p.damage(Math.min(event.getFinalDamage(), 4));

            // Transfer damage to nearby entities
            List<Entity> nearbyEntities = p.getNearbyEntities(RADIUS, 1, RADIUS);
            for (Entity e : nearbyEntities) {
                if (e.equals(event.getEntity())) continue;
                if (e instanceof Damageable) {
                    ((Damageable) e).damage(event.getFinalDamage());
                }
            }
            p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 10, RADIUS, 1, RADIUS);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);

            cooldown();
        };
    }
}
