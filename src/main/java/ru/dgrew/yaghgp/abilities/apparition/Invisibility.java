package ru.dgrew.yaghgp.abilities.apparition;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.abilities.AbilityCallable;

public class Invisibility extends Ability<PlayerInteractEvent> {

    public Invisibility() {
        super("Invisibility", PlayerInteractEvent.class, 15, false);
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.FIREWORK_STAR &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 3));
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(Main.getInstance(), player);
            }
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1, 1);
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.showPlayer(Main.getInstance(), player);
                    }
                }
            }.runTaskLater(Main.getInstance(), 60);

            cooldown();
        };
    }


}
