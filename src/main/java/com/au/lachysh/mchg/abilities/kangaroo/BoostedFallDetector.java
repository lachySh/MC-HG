package com.au.lachysh.mchg.abilities.kangaroo;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class BoostedFallDetector extends Ability<PlayerMoveEvent> {
    private JumpBoost jumpBoost;
    public BoostedFallDetector(JumpBoost jumpBoost) {
        super("Boosted fall detector", PlayerMoveEvent.class, 0, false);
        this.jumpBoost = jumpBoost;
    }

    @Override
    public boolean precondition(PlayerMoveEvent event) {
        return Math.abs(event.getPlayer().getVelocity().getY() - (-0.0784)) < 0.001;
    }

    @Override
    public AbilityCallable<PlayerMoveEvent> getCallable() {
        return event -> {
            if (jumpBoost.isCancelNextFall()) {
                if (!jumpBoost.isNextTickFall()) {
                    jumpBoost.setNextTickFall(true);
                } else {
                    jumpBoost.setNextTickFall(false);
                    jumpBoost.setCancelNextFall(false);
                }
            }
            cooldown();
        };
    }
}
