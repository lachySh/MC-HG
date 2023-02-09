package ru.dgrew.yaghgp.phases;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.SettingsManager;
import ru.dgrew.yaghgp.tribute.Tribute;

public class EndGame extends Phase {
    private int timer;
    private SettingsManager sm;
    private ChatManager cm;
    private Tribute victor;
    //region Phase Methods
    @Override
    public void onEnable() {
        timer = 15;
        sm = Main.getSm();
        cm = Main.getCm();
        victor = Main.getPlm().getRemainingTributesList().get(0);
        victor.getPlayerObject().sendTitle(cm.getVictorytitle(), cm.getVictory(), 20, 40, 20);
        victor.getPlayerObject().playSound(victor.getPlayerObject().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        Bukkit.broadcastMessage(cm.getPrefix() + cm.getGlobalvictory().replace("{player}", victor.getPlayerObject().getName()));
        startTimer();
        Bukkit.getLogger().info("EndGame phase has started successfully!");
    }
    @Override
    public void onDisable() {
        Bukkit.getServer().unloadWorld(sm.getArenaobj(), true);
        Bukkit.getServer().reload();
    }
    @Override
    public Phase next() {
        return null;
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
    //endregion
    //region Runnables
    void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    Firework fw = (Firework)victor.getPlayerObject().getWorld().spawnEntity(victor.getPlayerObject().getLocation(), EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.RED).withFade(Color.FUCHSIA).with(FireworkEffect.Type.BALL).trail(true).build();
                    fwm.addEffect(effect);
                    int rp = 3;
                    fwm.setPower(rp);
                    fw.setFireworkMeta(fwm);
                    if (timer == 15) Bukkit.broadcastMessage(cm.getPrefix() + cm.getEndgame(timer));
                    if (timer == 10) Bukkit.broadcastMessage(cm.getPrefix() + cm.getEndgame(timer));
                    if (timer <= 5) Bukkit.broadcastMessage(cm.getPrefix() + cm.getEndgame(timer));
                    timer--;
                } else {
                    for(Player p : Bukkit.getOnlinePlayers()) p.kickPlayer("Game ended! Restarting server...");
                    Bukkit.getServer().unloadWorld(sm.getArenaobj(), false);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Bukkit.getServer().shutdown(), 60);
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }
    //endregion
}
