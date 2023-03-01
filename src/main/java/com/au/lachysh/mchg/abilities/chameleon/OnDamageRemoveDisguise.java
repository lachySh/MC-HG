package com.au.lachysh.mchg.abilities.chameleon;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.managers.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnDamageRemoveDisguise extends Ability<EntityDamageEvent> {

    private static ChatManager cm;
    private MobDisguise mobDisguiseAbility;
    public OnDamageRemoveDisguise(MobDisguise mobDisguiseAbility) {
        super("On damage, remove disguise", EntityDamageEvent.class, 0, false);
        // Required for cooldown
        this.mobDisguiseAbility = mobDisguiseAbility;
        if (cm == null) {
            cm = Main.getCm();
        }
    }

    @Override
    public AbilityCallable<EntityDamageEvent> getCallable() {
        return event -> {
            Player p = (Player) event.getEntity();
            if (mobDisguiseAbility.getCurrentMobDisguise() != null) {
                mobDisguiseAbility.getCurrentMobDisguise().stopDisguise();
                p.sendMessage(cm.getPrefix() + "Your disguise has disappeared!");
                mobDisguiseAbility.clearCurrentMobDisguise();
                mobDisguiseAbility.cooldown();

                cooldown();
            }
        };
    }

    @Override
    public boolean precondition(EntityDamageEvent event) {
        return true;
    }
}
