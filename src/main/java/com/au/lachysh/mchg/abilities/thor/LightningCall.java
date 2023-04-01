package com.au.lachysh.mchg.abilities.thor;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Thor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class LightningCall extends Ability<PlayerInteractEvent> {
    public LightningCall() {
        super("Thor's lightning call", PlayerInteractEvent.class, 15, false);
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.WOODEN_AXE &&
                event.getAction() == Action.RIGHT_CLICK_BLOCK && Thor.itemName.contains(event.getItem().getItemMeta().getDisplayName());
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Location loc = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            Objects.requireNonNull(loc.getWorld()).strikeLightning(loc);
            Location fireLoc = loc.add(0, 1, 0);
            if (fireLoc.getBlock().getType() == Material.AIR) {
                fireLoc.getBlock().setType(Material.FIRE);
            }
            cooldown();
        };
    }
}
