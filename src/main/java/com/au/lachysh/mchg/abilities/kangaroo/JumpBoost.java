package com.au.lachysh.mchg.abilities.kangaroo;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Kangaroo;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

@Getter
@Setter
public class JumpBoost extends Ability<PlayerInteractEvent> {

    private boolean cancelNextFall;
    private boolean nextTickFall;

    public JumpBoost() {
        super("Jump boost", PlayerInteractEvent.class, 5, false);
        cancelNextFall = false;
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        boolean precond = event.getItem() != null && event.getItem().getType() == Material.FIREWORK_ROCKET &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && Kangaroo.itemName.contains(event.getItem().getItemMeta().getDisplayName());
        // Cheeky trick to make something happen even when on cooldown
        if (precond) {
            event.setCancelled(true);
        }
        return precond;
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Vector direction = player.getLocation().getDirection();
            player.setVelocity(direction.multiply(1.05).setY(0.9));
            player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
            player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 30, 0.01, 0.01, 0.01);
            cancelNextFall = true;
            cooldown();
        };
    }
}