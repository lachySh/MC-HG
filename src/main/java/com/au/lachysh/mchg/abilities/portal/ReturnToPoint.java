package com.au.lachysh.mchg.abilities.portal;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Hearthstone;
import com.au.lachysh.mchg.shared.ChatUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ReturnToPoint extends Ability<PlayerInteractEvent> {
    private SetReturnPoint setReturnPointAbility;
    private boolean spellInProgress;
    private int counter;

    private BukkitTask spell;
    private static final String CANCEL_SPELL = ChatColor.RED + "Your return spell was cancelled!";

    private static final String RETURN_START = ChatColor.RED + "You will be returned to your set location in 5 seconds!";
    public ReturnToPoint(SetReturnPoint setReturnPoint) {
        super("Return to point", PlayerInteractEvent.class, 15, false);
        this.setReturnPointAbility = setReturnPoint;
        spellInProgress = false;
        counter = 0;
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.LAPIS_LAZULI &&
                (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
                && Hearthstone.itemNameSet.contains(event.getItem().getItemMeta().getDisplayName());
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player p = event.getPlayer();
            Location returnPoint = setReturnPointAbility.getReturnPoint();
            spellInProgress = true;
            ChatUtils.sendActionbar(p, RETURN_START);
            p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1, 1);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
            spell = new BukkitRunnable() {
                @Override
                public void run() {
                    if (counter < 5) {
                        p.spawnParticle(Particle.PORTAL, p.getLocation().add(0,1,0), 150);
                        p.spawnParticle(Particle.PORTAL, returnPoint, 50);
                        counter++;
                    } else {
                        counter = 0;
                        p.teleport(returnPoint);
                        p.playSound(p, Sound.BLOCK_PORTAL_TRAVEL, 0.3f, 1);
                        ItemMeta meta = event.getItem().getItemMeta();
                        meta.setDisplayName(Hearthstone.itemNameUnset);
                        event.getItem().setItemMeta(meta);
                        p.updateInventory();
                        setReturnPointAbility.cooldown();
                        spellInProgress = false;

                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);

            cooldown();
        };
    }

    public boolean isSpellInProgress() {
        return spellInProgress;
    }

    public void cancelSpell(Player p) {
        ChatUtils.sendActionbar(p, CANCEL_SPELL);
        spell.cancel();
        spellInProgress = false;
        counter = 0;
        p.removePotionEffect(PotionEffectType.SLOW);
        p.removePotionEffect(PotionEffectType.CONFUSION);
        cooldown();
    }
}