package com.au.lachysh.mchg.abilities.poseidon;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class WaterBonuses extends Ability<PlayerMoveEvent> {
    private BukkitTask runnable;
    public WaterBonuses() {
        super("Water bonuses", PlayerMoveEvent.class, 0, false);
    }

    @Override
    public boolean precondition(PlayerMoveEvent event) {
        return event.getTo().getBlock().getType() == Material.WATER;
    }

    @Override
    public AbilityCallable<PlayerMoveEvent> getCallable() {
        return event -> {
            Player p = event.getPlayer();
            if (p.getPotionEffect(PotionEffectType.DOLPHINS_GRACE) == null) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, PotionEffect.INFINITE_DURATION, 2, false, false));
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, PotionEffect.INFINITE_DURATION, 0, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1, false, true));
            if (runnable != null && !runnable.isCancelled()) runnable.cancel();
            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.getLocation().getBlock().getType() != Material.WATER) {
                        if (Objects.requireNonNull(p.getPotionEffect(PotionEffectType.DOLPHINS_GRACE)).getDuration() == PotionEffect.INFINITE_DURATION) {
                            p.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                        }
                        p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                        p.removePotionEffect(PotionEffectType.SPEED);
                        cooldown();
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 10);
        };
    }
}