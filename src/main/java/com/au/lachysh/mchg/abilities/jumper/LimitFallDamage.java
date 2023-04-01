package com.au.lachysh.mchg.abilities.jumper;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class LimitFallDamage extends Ability<EntityDamageEvent> {

    public LimitFallDamage() {
        super("Limit fall damage", EntityDamageEvent.class, 0, false);
    }

    @Override
    public boolean precondition(EntityDamageEvent event) {
        return event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL;
    }

    @Override
    public AbilityCallable<EntityDamageEvent> getCallable() {
        return event -> {
            event.setCancelled(true);
            // Set max health loss to 3 hearts
            Player p = (Player) event.getEntity();
            p.damage(Math.min(event.getFinalDamage(), 6));
            cooldown();
        };
    }
}
