package ru.dgrew.yaghgp.phases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.*;

public class Lobby extends Phase {
    private SettingsManager sm;
    private ChatManager cm;
    private ScoreboardManager sbm;
    private VotingManager vm;
    private GamemapManager gm;
    private PlayerManager plm;
    private SharedPhaseLogic spl;
    private BukkitTask countdown;
    private int voteTimer;
    private boolean mapLoadInProgress = false;
    //region Phase Methods
    @Override
    public void onEnable() {
        sm = Main.getSm();
        cm = Main.getCm();
        sbm = Main.getSbm();
        vm = Main.getVm();
        gm = Main.getGm();
        plm = Main.getPlm();
        spl = Main.getSpl();
        voteTimer = 15;
        Bukkit.getLogger().info("Lobby phase has started successfully!");
    }
    @Override
    public void onDisable() {
    }
    @Override
    public Phase next() {
        return new PreGame();
    }
    //endregion
    //region Phase Listeners
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has joined!");
        p.teleport(sm.fetchLobbySpawn());
        p.setGameMode(GameMode.ADVENTURE);
        p.setExp(0);
        p.setLevel(0);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setSaturation(1.0f);
        p.setTotalExperience(0);
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
        p.sendTitle(ChatColor.GOLD + "Welcome to the Hunger Games", ChatColor.GRAY + "Use " + ChatColor.AQUA + "/vote" + ChatColor.GRAY + " to vote for a map and " + ChatColor.AQUA + "/kits" + ChatColor.GRAY + " to choose a kit!", 20, 200, 40);
        p.setScoreboard(sbm.getMapVoteBoard());
        plm.addTribute(p);
        Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbyPlayerCounter(Bukkit.getOnlinePlayers().size()));
    }

    public void startVoteCountdown() {
        countdown = new BukkitRunnable() {
            @Override
            public void run() {
                if (voteTimer > 0) {
                    if (voteTimer == 15) Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbytimer(voteTimer));
                    if (voteTimer == 10) Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbytimer(voteTimer));
                    if (voteTimer <= 5) {
                        for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbytimer(voteTimer));
                    }

                    voteTimer--;
                } else if (!gm.isMapLoaded() && !mapLoadInProgress) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbyMapSelection(vm.getHighestMapVote().getTitle()));
                    gm.loadGamemap(vm.getHighestMapVote()); // WIP
                } else if (!gm.isMapLoaded() && mapLoadInProgress) {
                    // wait for map to load
                } else if (gm.isMapLoaded()) {
                    Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbyMapLoaded());
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(),20L, 20L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        spl.inGameOnLeave(e);
    }
    @EventHandler
    public void onWorldDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e){
        e.setCancelled(true);
    }
    //endregion
}
