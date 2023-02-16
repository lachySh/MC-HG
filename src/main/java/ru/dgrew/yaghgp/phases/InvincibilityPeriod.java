package ru.dgrew.yaghgp.phases;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.GamemapManager;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.managers.SettingsManager;
import ru.dgrew.yaghgp.tribute.Tribute;

public class InvincibilityPeriod extends Phase {
    private int timer;
    private ChatManager cm;
    private PlayerManager pm;
    private SettingsManager sm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private BukkitTask countdown;

    //region Phase Methods
    @Override
    public void onEnable() {
        sm = Main.getSm();
        cm = Main.getCm();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        timer = 120;
        for (Tribute tribute : pm.getRemainingTributesList()) {
            tribute.getPlayerObject().setGameMode(gm.getArenaGamemap().getGamemode());
        }
        startCountdown();
        gm.getArenaWorld().setAutoSave(false);
        Bukkit.getLogger().info("InvincibilityPeriod phase has started successfully!");
    }

    @Override
    public void onDisable() {
        countdown.cancel();
    }

    @Override
    public Phase next() {
        return new FightPeriodStart();
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
    public void onCommand(PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWorldDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e) {
        e.setCancelled(true);
    }

    //endregion
    //region Runnables
    void startCountdown() {
        countdown = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    if (timer == 120) Bukkit.broadcastMessage(cm.getPrefix() + cm.getInvincibilityTimer(timer));
                    if (timer == 60) Bukkit.broadcastMessage(cm.getPrefix() + cm.getInvincibilityTimer(timer));
                    if (timer == 30) Bukkit.broadcastMessage(cm.getPrefix() + cm.getInvincibilityTimer(timer));
                    if (timer == 10) Bukkit.broadcastMessage(cm.getPrefix() + cm.getInvincibilityTimer(timer));
                    if (timer <= 5) {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getInvincibilityTimer(timer));
                    }

                    timer--;
                } else {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getTimerend());
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }
}
