package ru.dgrew.yaghgp.phases;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.abilities.CompassTrack;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.GamemapManager;
import ru.dgrew.yaghgp.managers.LootManager;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.tribute.Tribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Bukkit.getLogger().info("FightPeriodStart phase has started successfully!");
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

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e){
        e.setCancelled(true);
    }

    //endregion
    //region Runnables
    void startTimer() {
        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    // Compasses
                    if (timer == 120 || timer == 60) {
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getCompassTimerNotification(timer));
                    }
                    if (timer == 30 || timer == 15 || timer == 5) {
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getCompassTimerNotification(timer));
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
