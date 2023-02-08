package ru.dgrew.yaghgp.phases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.Phase;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.SettingsManager;
import java.util.List;
import java.util.Random;

public class InvincibilityPeriod extends Phase {
    private int timer;
    private ChatManager cm;
    private SettingsManager sm;
    private BukkitTask countdown;
    //region Phase Methods
    @Override
    public void onEnable() {
        sm = Main.getSm();
        cm = Main.getCm();
        timer = 120;
        startCountdown();
        sm.getArenaobj().setAutoSave(false);
        Bukkit.getLogger().info("InvincibilityPeriod phase has started successfully!");
    }
    @Override
    public void onDisable() {
        countdown.cancel();
    }
    @Override
    public Phase next() {
        return new InGame();
    }
    //endregion
    //region Phase Listeners
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        e.getPlayer().kickPlayer("Game already started!");
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has left!");
        Main.getPlm().removeOnDC(e.getPlayer());
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) { e.setCancelled(true); }
    @EventHandler
    public void onWorldDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
    }
    //endregion
    //region Runnables
    void startCountdown() {
        countdown = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    if (timer == 120) Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimer(timer));
                    if (timer == 90) Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimer(timer));
                    if (timer == 60) Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimer(timer));
                    if (timer == 30) Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimer(timer));
                    if (timer == 10) Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimer(timer));
                    if (timer <= 5) {
                        for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimer(timer));
                    }

                    timer--;
                } else {
                    for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimerend());
                    Main.getPm().nextPhase();
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }
}
