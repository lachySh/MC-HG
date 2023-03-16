package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.LootManager;
import com.au.lachysh.mchg.managers.PlayerManager;
import com.au.lachysh.mchg.structure.FeastStructure;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class PreFeast extends Phase {
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
        cm = Main.getCm();
        lm = new LootManager();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        timer = 180;

        feast = new FeastStructure();
        feastLocation = spl.calculateFeastLocation(feast);

        startTimer();
        Main.getInstance().getLogger().info("PreFeast phase has started successfully! Next phase: Feast");
    }


    @Override
    public void onDisable() {
        gameTimer.cancel();
    }

    @Override
    public Phase next() {
        return new Feast(feast, feastLocation);
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
                    spl.playTimerAnnouncement(timer, cm.getPrefix() + cm.getFeastTimerNotification(spl.formatLocation(feastLocation), timer));
                    timer--;
                } else {
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

}

