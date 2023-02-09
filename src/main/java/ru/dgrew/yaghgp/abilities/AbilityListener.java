package ru.dgrew.yaghgp.abilities;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.PlayerManager;

import java.util.NoSuchElementException;

public class AbilityListener implements Listener {

    private PlayerManager pm;

    public AbilityListener() {
        pm = Main.getPlm();
    }

    private boolean tryPreconditionSafely(Ability ability, Event event) {
        try {
            return ability.precondition(event);
        } catch (ClassCastException e) {
            return false;
        }
    }

    private void notifyOnCooldown(PlayerEvent event, Ability ability) {
        event.getPlayer().sendTitle("", "§c" + ability.getName() + " is on cooldown for " + ability.getCurrentCooldown() + " more seconds!", 5, 20, 5);
    }

    private void notifyOnDisabled(PlayerEvent event, Ability ability) {
        event.getPlayer().sendTitle("", "§c" + ability.getName() + " has been disabled!!", 5, 20, 5);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream().parallel()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        if (!selectedAbility.isDisabled() && !selectedAbility.isOnCooldown()) {
                            selectedAbility.getCallable().execute(event);
                        } else if (selectedAbility.isDisabled()) {
                            notifyOnDisabled(event, selectedAbility);
                        } else if (selectedAbility.isOnCooldown()) {
                            notifyOnCooldown(event, selectedAbility);
                        }
                    } catch (NoSuchElementException ignored) { }
                }
        );
    }
}
