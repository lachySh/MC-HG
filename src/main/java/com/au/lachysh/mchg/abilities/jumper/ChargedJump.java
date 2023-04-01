package com.au.lachysh.mchg.abilities.jumper;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ChargedJump extends Ability<PlayerToggleSneakEvent> {

    private int level;

    private static final int JUMP_MULTIPLIER = 3;
    private BukkitTask runnable;

    public ChargedJump() {
        super("Charged jump", PlayerToggleSneakEvent.class, 0, false);
        level = 0;
    }

    @Override
    public boolean precondition(PlayerToggleSneakEvent event) {
        return event.isSneaking();
    }

    @Override
    public AbilityCallable<PlayerToggleSneakEvent> getCallable() {
        return event -> {
            Player p = event.getPlayer();
            if (runnable != null && !runnable.isCancelled()) runnable.cancel();
            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.isSneaking()) {
                        if (level < 6) {
                            if (level > 0) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, level * JUMP_MULTIPLIER, false, false));
                                p.playSound(p, Sound.ENTITY_TNT_PRIMED, 0.5f, 1);
                            }
                            level++;
                        } else {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, level * JUMP_MULTIPLIER, false, false));
                        }
                    } else {
                        level = 0;
                        p.playSound(p, Sound.BLOCK_SAND_BREAK, 0.5f, 1);
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
            cooldown();
        };
    }
}
