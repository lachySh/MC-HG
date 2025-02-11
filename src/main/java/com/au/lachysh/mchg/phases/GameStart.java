package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.tribute.Tribute;

import java.util.List;
import java.util.Random;

public class GameStart extends Phase {
    private int timer;
    private ChatManager cm;
    private PlayerManager pm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private BukkitTask countdown;
    //region Phase Methods
    @Override
    public void onEnable() {
        cm = Main.getCm();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        timer = 10;
        pm.updateTributesList();
        pm.giveIntrinsicAbilitiesToAllTributes();
        pm.clearAllPlayerScoreboards();
        scatterPlayers();
        startCountdown();
        for (Tribute tribute : pm.getRemainingTributesList()) {
            tribute.getPlayerObject().setGameMode(GameMode.ADVENTURE);
            // Kit specific starting items
            for (ItemStack kitStartingItem : tribute.getStartingItems()) {
                tribute.getPlayerObject().getInventory().addItem(kitStartingItem);
            }
            // Gamemap specific starting items
            if (gm.getArenaGamemap().getStartingItems() != null && !gm.getArenaGamemap().getStartingItems().isEmpty()) {
                for (ItemStack gamemapStartingItem : gm.getArenaGamemap().getStartingItems()) {
                    tribute.getPlayerObject().getInventory().addItem(gamemapStartingItem);
                }
            }
        }
        Main.getInstance().getLogger().info("GameStart phase has started successfully!");
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
        spl.inGameOnJoin(e);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        spl.inGameOnLeave(e);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onWorldDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
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
                    spl.playTimerAnnouncement(timer, cm.getPrefix() + cm.getMovementTimeNotification(timer));
                    timer--;
                } else {
                    for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimerend());
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }
    //endregion
    void scatterPlayers() {
        Main.getInstance().getLogger().info("Scattering players...");
        Random random = new Random();
        List<Location> list = gm.getSpawnLocations(pm.getRemainingTributesList().size());
        int var;
        for (Player player : Bukkit.getOnlinePlayers()) {
            var = random.nextInt(list.size());
            player.teleport(list.get(var));
            list.remove(var);
        }
        Main.getInstance().getLogger().info("All online players should now be scattered!");
    }
}
