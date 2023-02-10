package ru.dgrew.yaghgp.managers;

import com.google.common.io.Files;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.gamemap.Gamemap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamemapManager {
    private ChatManager cm;
    private static final List<Gamemap> GAMEMAP_OPTIONS = new ArrayList<Gamemap>();
    private static Gamemap randomWorld;

    public GamemapManager() {
        this.cm = Main.getCm();
        randomWorld = new Gamemap("random", cm.getVoteRandom(), cm.getVoteRandomSubtitle(), Material.GRASS_BLOCK);
    }

    public void getCustomGamemaps() {
        Set<File> arenaConfigs = Stream.of(new File(Main.getInstance().getDataFolder() + "/arenas").listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> Files.getFileExtension(file.getName()).equals("yml"))
                .collect(Collectors.toSet());

        Bukkit.getLogger().info("Loaded " + arenaConfigs.size() + " custom maps: " + arenaConfigs.toString());

        for (File f : arenaConfigs) {
            var arenaConfig = new YamlConfiguration();
            try {
                arenaConfig.load(f);
                Gamemap option = new Gamemap(
                        Files.getNameWithoutExtension(arenaConfig.getName()),
                        arenaConfig.getString("details.name"),
                        arenaConfig.getString("details.description"),
                        Material.valueOf(arenaConfig.getString("details.display-item"))
                );
                GAMEMAP_OPTIONS.add(option);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public Gamemap getRandomWorld() {
        return randomWorld;
    }

    public List<Gamemap> getGamemapOptions() {
        return GAMEMAP_OPTIONS;
    }

    private World generateEmptyWorld() {
        WorldCreator wc = new WorldCreator("customarena");
        wc.type(WorldType.FLAT);
        wc.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
        return wc.createWorld();
    }
}
