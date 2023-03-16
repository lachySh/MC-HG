package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.managers.PlayerManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.LootManager;

public class FightPeriodStart extends Phase {
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
        timer = 120;
        cm = Main.getCm();
        lm = new LootManager();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        startTimer();
        Main.getInstance().getLogger().info("FightPeriodStart phase has started successfully!");
    }

    @Override
    public void onDisable() {
        gameTimer.cancel();
    }

    @Override
    public Phase next() {
        return new CompassHandout();
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
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        spl.onDeath(e);
    }

    @EventHandler
    public void onWorldDeath(EntityDamageEvent e) {
        spl.onWorldDeath(e);
    }

    //endregion
    //region Runnables
    void startTimer() {
        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    // Compasses
                    spl.playTimerAnnouncement(timer, cm.getPrefix() + cm.getCompassTimerNotification(timer));
                    timer--;
                } else {
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }
}
