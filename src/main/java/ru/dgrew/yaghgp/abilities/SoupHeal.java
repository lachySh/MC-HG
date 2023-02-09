package ru.dgrew.yaghgp.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class SoupHeal extends Ability<PlayerInteractEvent> {

    public SoupHeal() {
        super("Soup heal", 0, false);
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.MUSHROOM_STEW && event.getPlayer().getHealth() < 20.0;
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player player = event.getPlayer();
            player.setHealth(Math.min((player.getHealth() + 7.0), 20));
            event.getItem().setType(Material.BOWL);
            player.updateInventory();
            cooldown();
        };
    }
}
