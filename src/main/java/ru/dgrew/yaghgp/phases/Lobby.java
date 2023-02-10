package ru.dgrew.yaghgp.phases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.managers.ScoreboardManager;
import ru.dgrew.yaghgp.managers.SettingsManager;

public class Lobby extends Phase {
    private SettingsManager sm;
    private ChatManager cm;
    private ScoreboardManager sbm;
    //region Phase Methods
    @Override
    public void onEnable() {
        sm = Main.getSm();
        cm = Main.getCm();
        sbm = Main.getSbm();
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
        p.sendTitle(ChatColor.GOLD + "Welcome to the Hunger Games", ChatColor.GRAY + "Please use the command " + ChatColor.AQUA + "/vote" + ChatColor.GRAY + " to vote on what map should be played", 20, 200, 40);
        p.setScoreboard(sbm.getMapVoteBoard());
        Bukkit.broadcastMessage(cm.getPrefix() + cm.getLobbyPlayerCounter(Bukkit.getOnlinePlayers().size()));

    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has left!");
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
