package ru.dgrew.yaghgp;

import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dgrew.yaghgp.abilities.AbilityListener;
import ru.dgrew.yaghgp.commands.Start;
import ru.dgrew.yaghgp.managers.*;
import ru.dgrew.yaghgp.commands.VoteGUICommand;
import ru.dgrew.yaghgp.voting.VoteGUIListener;

import java.io.File;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;
    private static ChatManager cm;
    private static PhaseManager pm;
    private static VotingManager vm;
    private static PlayerManager plm;
    private static SettingsManager sm;
    private static GamemapManager gm;
    private static ScoreboardManager sbm;
    World lobby;
    World arena;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        plm = new PlayerManager();
        cm = new ChatManager(this.getConfig());
        sm = new SettingsManager(this.getConfig());
        sbm = new ScoreboardManager();
        vm = new VotingManager();
        gm = new GamemapManager();
        pm = new PhaseManager();
        gm.getCustomGamemaps();
        lobby = Bukkit.createWorld(WorldCreator.name(this.getConfig().getString("settings.lobby", "arena")));
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
        this.getCommand("vote").setExecutor(new VoteGUICommand());
        int pluginId = 17670;
        Metrics metrics = new Metrics(this, pluginId);
        if (sm.getUpdateCheck()) {
            UpdateChecker uc = new UpdateChecker(getDescription().getVersion());
            uc.checkForUpdates();
        }
        Bukkit.getPluginManager().registerEvents(new AbilityListener(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new VoteGUIListener(), Main.getInstance());
    }

    @Override
    public void onDisable() {
        lobby.save();
        arena.save();
    }

    private void deleteArena() {
        try {
            Bukkit.getLogger().info("Deleting current arena world...");
            World arena = new WorldCreator(this.getConfig().getString("settings.arena", "arena")).createWorld();
            File deleteFolder = arena.getWorldFolder();
            Bukkit.unloadWorld(arena, false);

            deleteWorld(deleteFolder);
            Bukkit.getLogger().info("Arena deleted successfully!");
        } catch (Exception ex) {
            Bukkit.getLogger().severe("Could not delete arena world!");
            ex.printStackTrace();
        }
    }

    private boolean deleteWorld(File path) {
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

    public static Main getInstance() {
        return instance;
    }
    public static ChatManager getCm() { return cm; }
    public static PhaseManager getPm() { return pm; }
    public static PlayerManager getPlm() {return plm; }
    public static SettingsManager getSm() { return sm; }
    public static VotingManager getVm() { return vm; }
    public static GamemapManager getGm() { return gm; }
    public static ScoreboardManager getSbm() { return sbm; }
}
