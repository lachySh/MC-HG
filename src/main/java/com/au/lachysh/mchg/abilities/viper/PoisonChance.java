package com.au.lachysh.mchg.abilities.viper;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.managers.ChatManager;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PoisonChance extends Ability<EntityDamageByEntityEvent> {

    private static Random rand = new Random();

    public PoisonChance() {
        super("Poison chance", EntityDamageByEntityEvent.class, 0, false);
    }

    @Override
    public AbilityCallable<EntityDamageByEntityEvent> getCallable() {
        return event -> {
            LivingEntity victim = (LivingEntity) event.getEntity();
            if (rand.nextDouble() < 0.333333333) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                if (victim instanceof Player) ((Player) victim).playSound(victim.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 1, 1);
                ((Player) event.getDamager()).playSound(victim.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 0.5f, 1);
            }
            cooldown();
        };
    }

    @Override
    public boolean precondition(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity;
    }
}
