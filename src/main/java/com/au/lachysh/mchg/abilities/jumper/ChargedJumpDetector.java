package com.au.lachysh.mchg.abilities.jumper;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ChargedJumpDetector extends Ability<PlayerMoveEvent> {
    public ChargedJumpDetector() {
        super("Charged jump detector", PlayerMoveEvent.class, 0, false);
    }

    @Override
    public boolean precondition(PlayerMoveEvent event) {
        return event.getPlayer().getVelocity().getY() > 0;
    }

    @Override
    public AbilityCallable<PlayerMoveEvent> getCallable() {
        return event -> {
            Player p = event.getPlayer();
            if (p.getPotionEffect(PotionEffectType.JUMP) != null) {
                p.removePotionEffect(PotionEffectType.JUMP);
                p.setVelocity(p.getVelocity().setX(p.getVelocity().getX()*5).setZ(p.getVelocity().getZ()*5));
                p.spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 50);
                p.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1);
            }
            cooldown();
        };
    }
}
