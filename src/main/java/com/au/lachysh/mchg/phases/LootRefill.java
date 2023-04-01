package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.intrinsic.CompassTrack;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.LootManager;
import com.au.lachysh.mchg.managers.PlayerManager;
import com.au.lachysh.mchg.structure.FeastStructure;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
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
import java.util.Random;

public class LootRefill extends Phase {
    private int timer;
    private ChatManager cm;
    private LootManager lm;
    private PlayerManager pm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private BukkitTask gameTimer;
    private FeastStructure feast;
    private Location feastLocation;

    //region Phase Methods
    @Override
    public void onEnable() {
        gm = Main.getGm();
        if (gm.getArenaGamemap().getFeastEnabled()) {
            timer = 180;
        } else {
            timer = 300;
        }
        cm = Main.getCm();
        lm = Main.getLm();
        pm = Main.getPlm();
        spl = Main.getSpl();
        startTimer();
        lm.enableRefillLootChestListener();
        Main.getInstance().getLogger().info("LootRefill phase has started successfully! Next phase: " + (gm.getArenaGamemap().getFeastEnabled() ? "Feast" : "Deathmatch"));

        Bukkit.broadcastMessage(cm.getPrefix() + cm.getRefillCommencing());

        if (gm.getArenaGamemap().getFeastEnabled()) {
            feast = new FeastStructure();
            feastLocation = spl.calculateFeastLocation(feast);
            PreFeast.setFeastEnabledForCompasses(feastLocation);
        }
    }

    @Override
    public void onDisable() {
        gameTimer.cancel();
    }

    @Override
    public Phase next() {
        if (gm.getArenaGamemap().getLootEnabled()) {
            return new Feast(feast, feastLocation);
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

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        spl.onDeath(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    @EventHandler
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
                    spl.playTimerAnnouncement(timer, cm.getPrefix() +
                            ((gm.getArenaGamemap().getLootEnabled()) ? cm.getFeastTimerNotification(spl.formatLocation(feastLocation), timer) : cm.getDeathmatch(timer)));
                    timer--;
                } else {
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

}

