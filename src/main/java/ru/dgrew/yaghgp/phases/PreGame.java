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
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.managers.ScoreboardManager;
import ru.dgrew.yaghgp.managers.SettingsManager;
import java.util.List;
import java.util.Random;

public class PreGame extends Phase {
    private int timer;
    private ChatManager cm;
    private SettingsManager sm;
    private PlayerManager pm;
    private BukkitTask countdown;
    //region Phase Methods
    @Override
    public void onEnable() {
        sm = Main.getSm();
        cm = Main.getCm();
        pm = Main.getPlm();
        timer = 10;
        startCountdown();
        scatterPlayers();
        sm.getArenaobj().setAutoSave(false);
        pm.updateTributesList();
        pm.giveIntrinsicAbilitiesToAllTributes();
        pm.clearAllPlayerScoreboards();
        Bukkit.getLogger().info("PreGame phase has started successfully!");
    }
    @Override
    public void onDisable() {
        countdown.cancel();
    }
    @Override
    public Phase next() {
        return new InvincibilityPeriod();
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
        pm.removeOnDC(e.getPlayer());
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) { e.setCancelled(true); }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onWorldDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
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
                    if (timer == 10) Bukkit.broadcastMessage(cm.getPrefix() + cm.getStartTimer(timer));
                    if (timer <= 5) {
                        for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getStartTimer(timer));
                    }

                    timer--;
                } else {
                    for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1, 1);
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimerend());
                    Main.getPm().nextPhase();
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }
    //endregion
    void scatterPlayers() {
        Bukkit.getLogger().info("Scattering players...");
        Random random = new Random();
        List<Location> list = sm.fetchSpawnLocations();
        int var;
        for (Player player : Bukkit.getOnlinePlayers()) {
            var = random.nextInt(list.size());
            player.teleport(list.get(var));
            list.remove(var);
        }
        Bukkit.getLogger().info("All online players should now be scattered!");
    }
}
