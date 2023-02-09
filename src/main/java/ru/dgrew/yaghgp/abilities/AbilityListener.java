package ru.dgrew.yaghgp.abilities;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.tribute.Tribute;

import java.util.NoSuchElementException;

public class AbilityListener implements Listener {

    private PlayerManager pm;

    public AbilityListener() {
        pm = Main.getPlm();
    }

    private boolean tryPreconditionSafely(Ability ability, Event event) {
        try {
            return ability.isPreconditionMet(event);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        pm.findTribute(event.getPlayer()).ifPresent(
                (t) -> {
                    try {
                        Ability selectedAbility = t.getAbilities().stream().parallel()
                                .filter(ability -> tryPreconditionSafely(ability, event))
                                .findFirst().get();
                        selectedAbility.getCallable().execute(event);
                    } catch (NoSuchElementException e) { }
                }
        );
    }
}
