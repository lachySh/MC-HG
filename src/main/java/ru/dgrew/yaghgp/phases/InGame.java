package ru.dgrew.yaghgp.phases;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.LootManager;
import ru.dgrew.yaghgp.managers.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class InGame extends Phase {
    private int timer;
    private ChatManager cm;
    private LootManager lm;
    private PlayerManager pm;
    private BukkitTask gameTimer;
    //region Phase Methods
    @Override
    public void onEnable() {
        timer = 900;
        cm = Main.getCm();
        lm = new LootManager();
        pm = Main.getPlm();
        startTimer();
        Bukkit.getLogger().info("InGame phase has started successfully!");
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
        e.setJoinMessage(null);
        e.getPlayer().kickPlayer("Game already started!");
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has left!");
        Main.getPlm().removeOnDC(e.getPlayer());
        checkForPlayerCount();
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
    public void onDeath(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player killed = (Player)e.getEntity();
            if (killed.getHealth() <= e.getFinalDamage()) {
                e.setCancelled(true);
                onDeath(killed, e.getDamager());
            }
        }
    }
    @EventHandler
    public void onWorldDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player killed = (Player)e.getEntity();
            if (!e.getCause().toString().startsWith("ENTITY_") && !e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                if (killed.getHealth() <= e.getFinalDamage()) {
                    e.setCancelled(true);
                    onDeath(killed, null);
                }
            }
        }
        else if (e.getEntity() instanceof ItemFrame) e.setCancelled(true);
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) { e.setCancelled(true); }
    //endregion
    //region Runnables
    void startTimer() {
        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    if (timer == 600 || timer == 540 || timer == 480 || timer == 450)
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getRefill(timer-420));
                    if (timer == 420){
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            p.closeInventory();
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        }
                        lm.refillAllChests();
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getRefillcommencing());
                    }
                    if (timer == 300 || timer == 180 || timer == 120 || timer == 60)
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatch(timer));
                    if (timer == 30 || timer == 15 || timer == 10)
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatch(timer));
                    if (timer <= 5) {
                        for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatch(timer));
                    }
                    timer--;
                } else {
                    Main.getPm().nextPhase();
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }
    //endregion
    void checkForPlayerCount() {
        if (pm.getRemainingTributesList().size() == 1) {
            timer = 0;
            return;
        }
        if (pm.getRemainingTributesList().size() <= 3 && timer >= 30) {
            timer = 60;
            return;
        }
        if (pm.getRemainingTributesList().size() <= 7) {
            timer = 480;
        }
    }
    void onDeath(Player killed, Entity killerent) {
        Player killer;
        if (killerent instanceof Player) {
            killer = (Player)killerent;
            killer.sendMessage(cm.getPrefix() + cm.getKill().replace("{player}",killed.getName()));
            killed.sendMessage(cm.getPrefix() + cm.getKilled().replace("{player}",killer.getName()));
        }
        else if (killerent instanceof Projectile) {
            Projectile pj = (Projectile)killerent;
            killer = (Player)pj.getShooter();
            killer.sendMessage(cm.getPrefix() + cm.getKill().replace("{player}",killed.getName()));
            killed.sendMessage(cm.getPrefix() + cm.getKilled().replace("{player}",killer.getName()));
        }
        else killed.sendMessage(cm.getPrefix() + cm.getKillednat());
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack i : killed.getInventory()) items.add(i);
        killed.getInventory().clear();
        for (ItemStack i : items) if (i != null) killed.getWorld().dropItem(killed.getLocation(), i).setPickupDelay(20);
        killed.setHealth(20);
        killed.getWorld().strikeLightningEffect(killed.getLocation());
        pm.transferToSpectators(killed);
        Bukkit.broadcastMessage(cm.getGlobalkill().replace("{players}", String.valueOf(pm.getRemainingTributesList().size())));
        checkForPlayerCount();
    }
}
