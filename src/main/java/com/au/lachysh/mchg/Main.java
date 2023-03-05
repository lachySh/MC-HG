package com.au.lachysh.mchg;

import com.au.lachysh.mchg.abilities.AbilityListener;
import com.au.lachysh.mchg.biome.WorldInitEventListener;
import com.au.lachysh.mchg.commands.*;
import com.au.lachysh.mchg.gui.VoteGUIListener;
import com.au.lachysh.mchg.managers.*;
import com.au.lachysh.mchg.phases.SharedPhaseLogic;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.au.lachysh.mchg.gui.KitsGUIListener;

import java.io.File;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;
    private static ChatManager cm;
    private static PhaseManager pm;
    private static VotingManager vm;
    private static PlayerManager plm;
    private static SettingsManager sm;
    private static GamemapManager gm;
    private static LootManager lm;
    private static ScoreboardManager sbm;
    private static SharedPhaseLogic spl;
    private static KitManager km;
    World lobby;
    World arena;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        plm = new PlayerManager();
        cm = new ChatManager(this.getConfig());
        sm = new SettingsManager(this.getConfig());
        sbm = new ScoreboardManager();
        gm = new GamemapManager();
        lm = new LootManager();
        vm = new VotingManager();
        spl = new SharedPhaseLogic();
        pm = new PhaseManager();
        km = new KitManager();
        gm.getGamemaps();

        lobby = Bukkit.createWorld(WorldCreator.name(this.getConfig().getString("settings.lobby", "arena")));
        deleteArenas();
        lobby.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        lobby.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        lobby.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        lobby.setGameRule(GameRule.DO_FIRE_TICK, false);
        lobby.setGameRule(GameRule.DO_TILE_DROPS, false);
        lobby.setTime(5000);

        this.getCommand("start").setExecutor(new StartCommand());
        this.getCommand("vote").setExecutor(new VoteGUICommand());
        this.getCommand("endvote").setExecutor(new EndVoteCommand());
        this.getCommand("nextphase").setExecutor(new NextPhaseCommand());
        this.getCommand("kits").setExecutor(new KitsGUICommand());

        // TODO
//        int pluginId = 17670;
//        Metrics metrics = new Metrics(this, pluginId);
//        if (sm.getUpdateCheck()) {
//            UpdateChecker uc = new UpdateChecker(getDescription().getVersion());
//            uc.checkForUpdates();
//        }
        Bukkit.getPluginManager().registerEvents(new VoteGUIListener(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new KitsGUIListener(), Main.getInstance());
        if (sm.getUseCustomWorldGen()) {
            Bukkit.getPluginManager().registerEvents(new WorldInitEventListener(), Main.getInstance());
        }
    }

    @Override
    public void onDisable() {
        Bukkit.unloadWorld("random", false);
    }

    private void deleteArenas() {
        try {
            Main.getInstance().getLogger().info("Deleting current random arena world...");
            Bukkit.unloadWorld("random", false);
            File deleteFolder = new File("./random");
            deleteWorld(deleteFolder);
            Main.getInstance().getLogger().info("random deleted successfully!");

            Main.getInstance().getLogger().info("Deleting current customarena world...");
            Bukkit.unloadWorld("customarena", false);
            deleteFolder = new File("./customarena");
            deleteWorld(deleteFolder);
            Main.getInstance().getLogger().info("customarena deleted successfully!");
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("Could not delete world! See error trace for details");
            ex.printStackTrace();
        }
    }

    public static boolean deleteWorld(File path) {
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

    public static void registerAbilityListener() {
        Bukkit.getPluginManager().registerEvents(new AbilityListener(), Main.getInstance());
    }

    public static void registerLootManagerListeners() {
        Bukkit.getPluginManager().registerEvents(getLm(), Main.getInstance());
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
    public static SharedPhaseLogic getSpl() { return spl; }
    public static KitManager getKm() { return km; }
    public static LootManager getLm() { return lm; }
}
