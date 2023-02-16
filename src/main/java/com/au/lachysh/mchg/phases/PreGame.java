package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.au.lachysh.mchg.Main;
import ru.dgrew.yaghgp.managers.*;
import com.au.lachysh.mchg.tribute.Tribute;

import java.util.List;
import java.util.Random;

public class PreGame extends Phase {
    private int timer;
    private ChatManager cm;
    private PlayerManager plm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private BukkitTask countdown;

    //region Phase Methods
    @Override
    public void onEnable() {
        cm = Main.getCm();
        plm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        timer = 30;
        plm.updateTributesList();
        plm.giveIntrinsicAbilitiesToAllTributes();
        plm.clearAllPlayerScoreboards();
        scatterPlayers();
        startCountdown();
        for (Tribute tribute : plm.getRemainingTributesList()) {
            tribute.getPlayerObject().setGameMode(GameMode.SPECTATOR);
        }
        Bukkit.getLogger().info("PreGame phase has started successfully!");
    }

    @Override
    public void onDisable() {
        countdown.cancel();
    }

    @Override
    public Phase next() {
        return new GameStart();
    }

    //endregion
    //region Phase Listeners
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // Allow player to join during pregame
        Player p = e.getPlayer();
        e.setJoinMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has joined!");
        p.teleport(gm.getSpawnLocations(1).get(0));
        p.setGameMode(GameMode.SPECTATOR);
        p.setExp(0);
        p.setLevel(0);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setSaturation(1.0f);
        p.setTotalExperience(0);
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        p.sendTitle(ChatColor.GOLD + "Welcome to the Hunger Games", ChatColor.GRAY + "Use " + ChatColor.AQUA + "/kits" + ChatColor.GRAY + " to choose a kit!", 20, 200, 40);
        p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
        plm.addTribute(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        spl.inGameOnLeave(e);
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e) {
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

    //endregion
    //region Runnables
    void startCountdown() {
        countdown = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    if (timer == 30 || timer == 15 || timer == 10)
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getStartTimer(timer));
                    if (timer <= 5) {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getStartTimer(timer));
                    }

                    timer--;
                } else {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1, 1);
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

    //endregion
    void scatterPlayers() {
        Bukkit.getLogger().info("Scattering players...");
        Random random = new Random();
        List<Location> list = gm.getSpawnLocations(plm.getRemainingTributesList().size());
        int var;
        for (Player player : Bukkit.getOnlinePlayers()) {
            var = random.nextInt(list.size());
            player.teleport(list.get(var));
            list.remove(var);
        }
        Bukkit.getLogger().info("All online players should now be scattered!");
    }
}
