package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.intrinsic.CompassTrack;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.LootManager;
import com.au.lachysh.mchg.managers.PlayerManager;
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

public class LootRefill extends Phase {
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
        timer = 300;
        cm = Main.getCm();
        lm = Main.getLm();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        startTimer();
        lm.enableRefillLootChestListener();
        Main.getInstance().getLogger().info("LootRefill phase has started successfully!");

        Bukkit.broadcastMessage(cm.getPrefix() + cm.getRefillCommencing());
    }

    @Override
    public void onDisable() {
        gameTimer.cancel();
    }

    @Override
    public Phase next() {
        return new Deathmatch();
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
                    if (timer == 300 || timer == 180 || timer == 120 || timer == 60)
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatch(timer));
                    if (timer == 30 || timer == 15 || timer == 10)
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatch(timer));
                    if (timer <= 5) {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatch(timer));
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

