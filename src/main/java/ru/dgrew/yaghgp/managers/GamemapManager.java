package ru.dgrew.yaghgp.managers;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.gamemap.Coordinates;
import ru.dgrew.yaghgp.gamemap.Gamemap;
import ru.dgrew.yaghgp.gamemap.RandomGamemap;
import ru.dgrew.yaghgp.shared.BiomeChecker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamemapManager {
    private ChatManager cm;
    private SettingsManager sm;
    private static final List<Gamemap> CUSTOM_GAMEMAP_OPTIONS = new ArrayList<>();
    private static RandomGamemap randomWorld;
    private static boolean mapLoaded = false;
    private static String gamemapFolder = "";
    private World arenaWorld;
    private Gamemap arenaGamemap;

    public GamemapManager() {
        this.cm = Main.getCm();
        this.sm = Main.getSm();
    }

    public void getCustomGamemaps() {
        Set<File> arenaConfigs = Stream.of(new File(Main.getInstance().getDataFolder() + "/arenas").listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> Files.getFileExtension(file.getName()).equals("yml"))
                .collect(Collectors.toSet());

        Bukkit.getLogger().info("Loaded " + arenaConfigs.size() + " maps: " + arenaConfigs.toString());

        if (arenaConfigs.size() == 0) {
            Bukkit.getLogger().severe("NO MAPS FOUND IN /arenas FOLDER! This plugin will most certainly not work without maps... (at the very least needs a random.yml configuration)");
        }

        for (File f : arenaConfigs) {
            var arenaConfig = new YamlConfiguration();
            try {
                arenaConfig.load(f);
                Gamemap option;
                if (arenaConfig.getBoolean("settings.spawn.randomize-spawn-locations")) {
                    option = new Gamemap(
                            FilenameUtils.removeExtension(f.getName()),
                            ChatColor.GOLD + arenaConfig.getString( "details.name"),
                            arenaConfig.getString("details.description"),
                            Material.valueOf(arenaConfig.getString("details.display-item")),
                            arenaConfig.getString("settings.world-centre"),
                            arenaConfig.getInt("settings.world-border-radius"),
                            arenaConfig.getInt("settings.deathmatch-border-radius"),
                            arenaConfig.getBoolean("settings.allow-world-breaking"),
                            arenaConfig.getBoolean("settings.spawn.randomize-spawn-locations"),
                            arenaConfig.getInt("settings.spawn.randomize-spread")
                    );
                } else {
                    option = new Gamemap(
                            FilenameUtils.removeExtension(f.getName()),
                            ChatColor.GOLD + arenaConfig.getString("details.name"),
                            arenaConfig.getString("details.description"),
                            Material.valueOf(arenaConfig.getString("details.display-item")),
                            arenaConfig.getString("settings.world-centre"),
                            arenaConfig.getInt("settings.world-border-radius"),
                            arenaConfig.getInt("settings.deathmatch-border-radius"),
                            arenaConfig.getBoolean("settings.allow-world-breaking"),
                            arenaConfig.getBoolean("settings.spawn.randomize-spawn-locations"),
                            arenaConfig.getStringList("settings.spawn.spawn-locations")
                    );
                }
                if (option.getFilename().equals("random")) {
                    Bukkit.getLogger().info("Loaded random world option");
                    option.setTitle(option.getTitle().replace("§6","§a"));
                    RandomGamemap randomOption = option.toRandomGamemap();
                    randomWorld = randomOption;
                } else {
                    CUSTOM_GAMEMAP_OPTIONS.add(option);
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public Gamemap getRandomWorld() {
        return randomWorld;
    }

    public List<Gamemap> getCustomGamemapOptions() {
        return CUSTOM_GAMEMAP_OPTIONS;
    }

    public void loadGamemap(Gamemap map) {
        if (map instanceof RandomGamemap randMap) {
            gamemapFolder = "random";
            Bukkit.createWorld(WorldCreator.name(gamemapFolder));
            retryRandomWorldUntilNoOceanAtSpawn();
        } else {
            gamemapFolder = "customarena";
            Bukkit.getLogger().info("Copying map: " + Main.getInstance().getDataFolder() + "/arenas/" + map.getFilename());
            File mapFolder = new File(Main.getInstance().getDataFolder() + "/arenas/" + map.getFilename());
            File destFolder = new File("./" + gamemapFolder);
            try {
                FileUtils.copyDirectory(mapFolder, destFolder);
            } catch (IOException e) {
                Bukkit.getLogger().severe("Something went wrong copying arena to server directory!");
                e.printStackTrace();
            }
            Bukkit.createWorld(WorldCreator.name(gamemapFolder));
        }

        arenaWorld = Bukkit.getWorld(gamemapFolder);
        arenaGamemap = map;
        assert arenaWorld != null;
        setGameOptions(arenaWorld, arenaGamemap);

        mapLoaded = true;
    }

    // May replace with a relocation of the spawn point, but for now I want to keep the spawn at 0,0,0 always
    // (or whatever is set in the configs)
    private void retryRandomWorldUntilNoOceanAtSpawn() {
        World w = Bukkit.getWorld(gamemapFolder);
        Location potentialWater = generateLocation(deserializeCoordinate(getRandomWorld().getWorldCentre()), w);
        potentialWater.setY(62);
        if (potentialWater.getBlock().getBlockData().getMaterial() == Material.WATER) {
            Bukkit.getLogger().warning("Random world generated water at spawn! Regenerating...");
            Bukkit.unloadWorld(gamemapFolder, false);
            File deleteFolder = new File("./" + gamemapFolder);
            Main.deleteWorld(deleteFolder);
            Bukkit.createWorld(WorldCreator.name(gamemapFolder));
            retryRandomWorldUntilNoOceanAtSpawn();
        }
        Bukkit.getLogger().info("World did not have water at spawn!");
    }

    private void setGameOptions(World arena, Gamemap map) {
        arena.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        arena.setGameRule(GameRule.DO_TILE_DROPS, true);
        arena.setTime(500);
        arena.setDifficulty(Difficulty.NORMAL);
        arena.getWorldBorder().setCenter(generateLocation(deserializeCoordinate(map.getWorldCentre()), arena));
        arena.getWorldBorder().setSize(map.getBorderRadius()*2);
        arena.setAutoSave(false);
    }

    public boolean isMapLoaded() {
        return mapLoaded;
    }

    public String getGamemapFolder() {
        return gamemapFolder;
    }

    public World getArenaWorld() {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Bukkit.getLogger().severe("Tried to get arena World object when arena isn't loaded!");
        }
        return arenaWorld;
    }

    public Gamemap getArenaGamemap() {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Bukkit.getLogger().severe("Tried to get arena Gamemap object when arena isn't loaded!");
        }
        return arenaGamemap;
    }

    private Location generateLocation(Coordinates coordinates, World world) {
        return new Location(world,
                coordinates.getX(),
                coordinates.getY(),
                coordinates.getZ(),
                coordinates.getYaw(),
                coordinates.getPitch());
    }

    private Coordinates deserializeCoordinate(String coordinate) {
        try {
            String[] coords = coordinate.split(":");
            return new Coordinates(
                    Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]),
                    Double.parseDouble(coords[2]),
                    Float.parseFloat(coords[3]),
                    Float.parseFloat(coords[4])
            );
        } catch (NumberFormatException e) {
            Bukkit.getLogger().severe("Cannot deserialize location!");
            return null;
        }
    }

    public List<Location> getSpawnLocations(int playerCount) {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Bukkit.getLogger().severe("Tried to get arena spawn locations when arena isn't loaded!");
            return null;
        }

        Random random = new Random();
        List<Location> locations = new ArrayList<>();

        if (arenaGamemap.getRandomizeSpawnLocations()) {
            try {
                Coordinates coords = deserializeCoordinate(arenaGamemap.getWorldCentre());
                for (int i = 0; i < playerCount; i++) {
                    int x = (int) (random.nextInt(arenaGamemap.getRandomizeSpread() * 2) - arenaGamemap.getRandomizeSpread() + coords.getX());
                    int z = (int) (random.nextInt(arenaGamemap.getRandomizeSpread() * 2) - arenaGamemap.getRandomizeSpread() + coords.getZ());
                    locations.add(new Location(arenaWorld,
                                    x,
                                    arenaWorld.getHighestBlockYAt(x, z) + 1,
                                    z
                            )
                    );
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("Could not generate random spawn locations for players!");
                return null;
            }
        } else {
            try {
                List<Coordinates> coords = new ArrayList<>();
                for (String s : arenaGamemap.getSpawnLocations()) {
                    coords.add(deserializeCoordinate(s));
                }

                int listPos = 0;
                for (int i = 0; i < playerCount; i++) {
                    if (listPos > coords.size()-1) listPos = 0;
                    locations.add(generateLocation(coords.get(listPos), arenaWorld));
                    listPos++;
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("Could not generate custom spawn locations for players!");
                return null;
            }
        }

        return locations;
    }

    public List<Location> getDeathmatchLocations(int playerCount) {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Bukkit.getLogger().severe("Tried to get arena deathmatch locations when arena isn't loaded!");
            return null;
        }

        Random random = new Random();
        List<Location> locations = new ArrayList<>();

        try {
            Coordinates coords = deserializeCoordinate(arenaGamemap.getWorldCentre());
            for (int i = 0; i < playerCount; i++) {
                int x = (int) (random.nextInt((arenaGamemap.getDeathmatchBorderRadius()-1) * 2) - (arenaGamemap.getDeathmatchBorderRadius()-1) + coords.getX());
                int z = (int) (random.nextInt((arenaGamemap.getDeathmatchBorderRadius()-1) * 2) - (arenaGamemap.getDeathmatchBorderRadius()-1) + coords.getZ());
                locations.add(new Location(arenaWorld,
                                x,
                                arenaWorld.getHighestBlockYAt(x, z) + 1,
                                z
                        )
                );
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Could not generate random deathmatch locations for players!");
            return null;
        }

        return locations;
    }

    public Location getArenaCentre() {
        return generateLocation(deserializeCoordinate(arenaGamemap.getWorldCentre()), arenaWorld);
    }
}
