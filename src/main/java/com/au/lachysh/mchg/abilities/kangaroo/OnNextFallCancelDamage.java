package com.au.lachysh.mchg.abilities.kangaroo;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnNextFallCancelDamage extends Ability<EntityDamageEvent> {
    private JumpBoost jumpBoost;
    public OnNextFallCancelDamage(JumpBoost jumpBoost) {
        super("On next fall, cancel damage", EntityDamageEvent.class, 0, false);
        this.jumpBoost = jumpBoost;
    }

    @Override
    public boolean precondition(EntityDamageEvent event) {
        return event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL;
    }

    @Override
    public AbilityCallable<EntityDamageEvent> getCallable() {
        return event -> {
            if (jumpBoost.isCancelNextFall() && jumpBoost.isNextTickFall()) {
                event.setCancelled(true);
                jumpBoost.setCancelNextFall(false);
                jumpBoost.setNextTickFall(false);
            }
            cooldown();
        };
    }
}
