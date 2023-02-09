package ru.dgrew.yaghgp;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dgrew.yaghgp.abilities.AbilityListener;
import ru.dgrew.yaghgp.commands.Start;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.PhaseManager;
import ru.dgrew.yaghgp.managers.PlayerManager;
import ru.dgrew.yaghgp.managers.SettingsManager;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;
    private static ChatManager cm;
    private static PhaseManager pm;
    private static PlayerManager plm;
    private static SettingsManager sm;
    World lobby;
    World arena;
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        plm = new PlayerManager();
        cm = new ChatManager(this.getConfig());
        pm = new PhaseManager();
        sm = new SettingsManager(this.getConfig());
        lobby = Bukkit.createWorld(WorldCreator.name(this.getConfig().getString("settings.lobby", "arena")));
        arena = Bukkit.createWorld(WorldCreator.name(this.getConfig().getString("settings.arena", "arena")));
        deleteArena();
        arena = Bukkit.createWorld(WorldCreator.name(this.getConfig().getString("settings.arena", "arena")));
        lobby.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        lobby.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        lobby.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        lobby.setGameRule(GameRule.DO_FIRE_TICK, false);
        lobby.setGameRule(GameRule.DO_TILE_DROPS, false);
        arena.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        lobby.setTime(5000);
        arena.setTime(500);
        arena.setDifficulty(Difficulty.NORMAL);
        arena.getWorldBorder().setSize(sm.getBorderRadius());
        this.getCommand("start").setExecutor(new Start());
        int pluginId = 17670;
        Metrics metrics = new Metrics(this, pluginId);
        if (sm.getUpdateCheck()) {
            UpdateChecker uc = new UpdateChecker(getDescription().getVersion());
            uc.checkForUpdates();
        }

        // temp
        Bukkit.getPluginManager().registerEvents(new AbilityListener(),Main.getInstance());
    }

    private void deleteArena() {
        try {
            Bukkit.getLogger().info("Deleting current arena world...");
            World lobby = Bukkit.getWorld(this.getConfig().getString("settings.arena", "arena"));
            File deleteFolder = lobby.getWorldFolder();
            Bukkit.unloadWorld(lobby, false);

            deleteWorld(deleteFolder);
            Bukkit.getLogger().info("Arena deleted successfully!");
        } catch (Exception ex) {
            Bukkit.getLogger().severe("Could not delete arena world!");
            ex.printStackTrace();
        }
    }

    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    public void onDisable() {

    }
    public static Main getInstance() {
        return instance;
    }
    public static ChatManager getCm() { return cm; }
    public static PhaseManager getPm() { return pm; }
    public static PlayerManager getPlm() {return plm; }
    public static SettingsManager getSm() { return sm; }
}
