package com.au.lachysh.mchg.abilities.fisherman;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

public class FishTeleport extends Ability<PlayerFishEvent> {
    public FishTeleport() {
        super("Fisherman's pull", PlayerFishEvent.class, 0, false);
    }

    @Override
    public boolean precondition(PlayerFishEvent event) {
        return event.getState() != PlayerFishEvent.State.REEL_IN && event.getCaught() instanceof Damageable;
    }

    @Override
    public AbilityCallable<PlayerFishEvent> getCallable() {
        return event -> {
            Player fisher = event.getPlayer();
            Entity caught = event.getCaught();

            Location shooterLocation = fisher.getLocation();

            caught.teleport(shooterLocation);
            if (caught instanceof Player) {
                ((Player) caught).playSound(shooterLocation, Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 1);
            }

            cooldown();
        };
    }
}
