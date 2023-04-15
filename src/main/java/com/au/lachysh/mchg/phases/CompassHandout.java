package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.intrinsic.CompassTrack;
import com.au.lachysh.mchg.kits.Spy;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.LootManager;
import com.au.lachysh.mchg.managers.PlayerManager;
import com.au.lachysh.mchg.tribute.Tribute;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class CompassHandout extends Phase {
    private int timer;
    private ChatManager cm;
    private LootManager lm;
    private PlayerManager pm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private BukkitTask gameTimer;

    //region Phase Methods
    @Override
    public void onEnable() {
        cm = Main.getCm();
        lm = new LootManager();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();

        if (gm.getArenaGamemap().getLootEnabled() || gm.getArenaGamemap().getFeastEnabled()) {
            timer = 480;
        } else {
            timer = 780;
        }

        startTimer();
        Main.getInstance().getLogger().info("CompassHandout phase has started successfully! Next phase: " +
                (gm.getArenaGamemap().getLootEnabled() ? "LootRefill" :
                        gm.getArenaGamemap().getFeastEnabled() ? "PreFeast" : "Deathmatch"));
        for (Tribute t : pm.getRemainingTributesList()) {
            if (t.getKit() instanceof Spy) return;
            t.getPlayerObject().playSound(t.getPlayerObject().getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, 1, 1);
            t.addIntrinsicAbility(new CompassTrack());
            givePlayerCompass(t.getPlayerObject());
        }
        Bukkit.broadcastMessage(cm.getPrefix() + cm.getCompassTime());
    }

    private void givePlayerCompass(Player player) {
        HashMap unstored = player.getInventory().addItem(CompassTrack.getTrackingCompass());
        if (unstored.size() > 0) {
            gm.getArenaWorld().dropItemNaturally(player.getLocation(), CompassTrack.getTrackingCompass());
        }
    }

    @Override
    public void onDisable() {
        gameTimer.cancel();
    }

    @Override
    public Phase next() {
        if (gm.getArenaGamemap().getLootEnabled()) {
            return new LootRefill();
        } else if (gm.getArenaGamemap().getFeastEnabled()) {
            return new PreFeast();
        } else {
            return new Deathmatch();
        }
    }

    //endregion
    //region Phase Listeners
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        spl.inGameOnJoin(e);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        spl.inGameOnLeave(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeath(EntityDamageByEntityEvent e) {
        spl.onDeath(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldDeath(EntityDamageEvent e) {
        spl.onWorldDeath(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    //endregion
    //region Runnables
    void startTimer() {
        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    if (!gm.getArenaGamemap().getFeastEnabled()) {
                        spl.playTimerAnnouncement(timer, cm.getPrefix() +
                                (gm.getArenaGamemap().getLootEnabled() ?
                                        cm.getDeathmatch(timer) : cm.getRefill(timer)));
                    }
                    timer--;
                } else {
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

}

