package ru.dgrew.yaghgp.phases;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.GamemapManager;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.tribute.Tribute;

import java.util.List;
import java.util.Random;

public class Deathmatch extends Phase {
    private int timer;
    private ChatManager cm;
    private PlayerManager pm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private boolean prepbool;
    private BukkitTask gracep;
    //region Phase Methods
    @Override
    public void onEnable() {
        prepbool = true;
        timer = 10;
        cm = Main.getCm();
        gm = Main.getGm();
        pm = Main.getPlm();
        spl = Main.getSpl();
        World arena = gm.getArenaWorld();
        scatterPlayers();
        arena.getWorldBorder().setCenter(gm.getArenaCentre());
        arena.getWorldBorder().setSize(gm.getArenaGamemap().getDeathmatchBorderRadius()*2);
        arena.setTime(18000L);
        startTimer();
        Bukkit.getLogger().info("Deathmatch phase has started successfully!");
        checkForLastPlayer();
    }
    @Override
    public void onDisable() {
        gracep.cancel();
    }
    @Override
    public Phase next() {
        return new EndGame();
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
        checkForLastPlayer();
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        spl.onDeath(e);
        checkForLastPlayer();
    }

    @EventHandler
    public void onWorldDeath(EntityDamageEvent e) {
        spl.onWorldDeath(e);
        checkForLastPlayer();
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
        gracep = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    if (timer == 10) Bukkit.broadcastMessage(cm.getPrefix() + (cm.getDeathmatchprep(timer)));
                    if (timer <= 5) {
                        for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatchprep(timer));
                    }
                    timer--;
                } else {
                    for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getDeathmatchstart());
                    prepbool = false;
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }

    //endregion
    void scatterPlayers() {
        Bukkit.getLogger().info("Scattering players for deathmatch...");
        Random random = new Random();
        List<Location> list = gm.getDeathmatchLocations(pm.getRemainingTributesList().size());
        int var;
        for (Tribute t : pm.getRemainingTributesList()) {
            var = random.nextInt(list.size());
            t.getPlayerObject().teleport(list.get(var));
            list.remove(var);
        }
        Bukkit.getLogger().info("All online players should now be scattered!");
    }

    void checkForLastPlayer() {
        if (pm.getRemainingTributesList().size() == 1) Main.getPm().nextPhase();
    }
}
