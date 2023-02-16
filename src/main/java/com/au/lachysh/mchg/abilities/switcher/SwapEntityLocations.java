package com.au.lachysh.mchg.abilities.switcher;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.List;

public class SwapEntityLocations extends Ability<ProjectileHitEvent> {

    public SwapEntityLocations() {
        super("Snowball switch", ProjectileHitEvent.class, 10, false);
    }

    @Override
    public boolean precondition(ProjectileHitEvent event) {
        return event.getEntity() instanceof Snowball && event.getHitEntity() != null && event.getHitEntity() instanceof Damageable
                && event.getEntity().getShooter() instanceof Player;
    }

    @Override
    public AbilityCallable<ProjectileHitEvent> getCallable() {
        return event -> {
            event.setCancelled(true);
            // Set max health loss to 2 hearts
            Player shooter = (Player) event.getEntity().getShooter();
            Entity receiver = event.getHitEntity();

            Location shooterLocation = shooter.getLocation();
            Location receiverLocation = receiver.getLocation();

            shooter.teleport(receiverLocation);
            receiver.teleport(shooterLocation);

            // Transfer damage to nearby entities
            shooter.playSound(shooter.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            receiver.getWorld().playSound(receiver.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            cooldown();
        };
    }
}
