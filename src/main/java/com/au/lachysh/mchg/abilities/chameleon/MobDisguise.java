package com.au.lachysh.mchg.abilities.chameleon;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.managers.ChatManager;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDisguise extends Ability<EntityDamageByEntityEvent> {


    private static ChatManager cm;
    private me.libraryaddict.disguise.disguisetypes.MobDisguise currentMobDisguise;

    public MobDisguise() {
        super("Mob disguise", EntityDamageByEntityEvent.class, 10, false);
        if (cm == null) {
            cm = Main.getCm();
        }
    }

    @Override
    public AbilityCallable<EntityDamageByEntityEvent> getCallable() {
        return event -> {
            Player damager = (Player) event.getDamager();
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            me.libraryaddict.disguise.disguisetypes.MobDisguise mobDisguise =
                    new me.libraryaddict.disguise.disguisetypes.MobDisguise(DisguiseType.getType(livingEntity.getType()));
            mobDisguise.setEntity(event.getDamager());
            mobDisguise.startDisguise();
            currentMobDisguise = mobDisguise;
            damager.sendMessage(cm.getPrefix() + "You are now disguised as a " + mobDisguise.getDisguiseName());
        };
    }

    @Override
    public boolean precondition(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity;
    }

    public me.libraryaddict.disguise.disguisetypes.MobDisguise getCurrentMobDisguise() {
        return currentMobDisguise;
    }

    public void clearCurrentMobDisguise() {
        currentMobDisguise = null;
    }
}
