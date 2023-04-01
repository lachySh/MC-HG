package com.au.lachysh.mchg.abilities.portal;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnDamageCancelSpell extends Ability<EntityDamageEvent> {
    private ReturnToPoint returnToPointAbility;
    public OnDamageCancelSpell(ReturnToPoint returnToPointAbility) {
        super("On damage, cancel spell", EntityDamageEvent.class, 0, false);
        // Required for cooldown
        this.returnToPointAbility = returnToPointAbility;
    }

    @Override
    public AbilityCallable<EntityDamageEvent> getCallable() {
        return event -> {
            Player p = (Player) event.getEntity();
            if (returnToPointAbility.isSpellInProgress()) {
                returnToPointAbility.cancelSpell(p);
                cooldown();
            }
        };
    }

    @Override
    public boolean precondition(EntityDamageEvent event) {
        return true;
    }
}