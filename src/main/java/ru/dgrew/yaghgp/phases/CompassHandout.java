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
        timer = 780;
        cm = Main.getCm();
        lm = new LootManager();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        startTimer();
        Bukkit.getLogger().info("CompassHandout phase has started successfully!");
        for (Tribute t : pm.getRemainingTributesList()) {
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

    //    @EventHandler
//    public void onChestOpen(InventoryOpenEvent e) {
//        if(e.getInventory().getHolder() instanceof Chest){
//            Chest c = (Chest)e.getInventory().getHolder();
//            if (lm.getChestContents(c.getLocation()) == null) {
//                c.getInventory().clear();
//                lm.createRandomChest(c.getInventory());
//                lm.storeChestContents(c.getLocation(), c.getInventory());
//            }
//        }
//        if(e.getInventory().getHolder() instanceof DoubleChest){
//            DoubleChest c = (DoubleChest)e.getInventory().getHolder();
//            Chest left = (Chest)c.getLeftSide();
//            Chest right = (Chest)c.getRightSide();
//            if (lm.getChestContents(c.getLocation()) == null &&
//                    (lm.getChestContents(left.getLocation()) == null || lm.getChestContents(right.getLocation()) == null)) {
//                c.getInventory().clear();
//                left.getInventory().clear();
//                right.getInventory().clear();
//                lm.createRandomChest(c.getInventory());
//                lm.createRandomChest(left.getInventory());
//                lm.createRandomChest(right.getInventory());
//                lm.storeChestContents(c.getLocation(), c.getInventory());
//                lm.storeChestContents(left.getLocation(), left.getInventory());
//                lm.storeChestContents(right.getLocation(), right.getInventory());
//            }
//        }
//    }
//    @EventHandler
//    public void onChestClose(InventoryCloseEvent e){
//        if(e.getInventory().getHolder() instanceof Chest){
//            Chest c = (Chest)e.getInventory().getHolder();
//            lm.storeChestContents(c.getLocation(), c.getInventory());
//        }
//        if(e.getInventory().getHolder() instanceof DoubleChest){
//            DoubleChest c = (DoubleChest)e.getInventory().getHolder();
//            Chest left = (Chest)c.getLeftSide();
//            Chest right = (Chest)c.getRightSide();
//            lm.storeChestContents(c.getLocation(), c.getInventory());
//            lm.storeChestContents(left.getLocation(), left.getInventory());
//            lm.storeChestContents(right.getLocation(), right.getInventory());
//        }
//    }

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
                    // Refill chests
//                    if (timer == 600 || timer == 540 || timer == 480 || timer == 450)
//                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getRefill(timer - 420));
//                    if (timer == 420) {
//                        for (Player p : Bukkit.getOnlinePlayers()) {
//                            p.closeInventory();
//                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                        }
//                        lm.refillAllChests();
//                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getRefillcommencing());
//                    }

                    // End phase
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

