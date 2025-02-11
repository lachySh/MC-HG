package com.au.lachysh.mchg.managers;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.gamemap.Coordinates;
import com.au.lachysh.mchg.gamemap.Gamemap;
import com.au.lachysh.mchg.loot.EnchantmentEntry;
import com.au.lachysh.mchg.loot.LootEntry;
import com.au.lachysh.mchg.terrain.FlatWorldGenerator;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamemapManager {
    private ChatManager cm;
    private SettingsManager sm;
    private static final List<Gamemap> CUSTOM_GAMEMAP_OPTIONS = new ArrayList<>();
    private static Gamemap randomWorld;
    private static boolean mapLoaded = false;
    private static String gamemapFolder = "";
    private World arenaWorld;
    private Gamemap arenaGamemap;

    public GamemapManager() {
        this.cm = Main.getCm();
        this.sm = Main.getSm();
    }

    public void getGamemaps() {
        Set<File> arenaConfigs = Stream.of(new File(Main.getInstance().getDataFolder() + "/arenas").listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> Files.getFileExtension(file.getName()).equals("yml"))
                .collect(Collectors.toSet());

        Main.getInstance().getLogger().info("Loaded " + arenaConfigs.size() + " maps: " + arenaConfigs.toString());

        if (arenaConfigs.size() == 0) {
            Main.getInstance().getLogger().severe("NO MAPS FOUND IN /arenas FOLDER! This plugin will most certainly not work without maps... (at the very least needs a random.yml configuration)");
        }

        for (File f : arenaConfigs) {
            var arenaConfig = new YamlConfiguration();
            try {
                arenaConfig.load(f);
                Gamemap option;
                option = new Gamemap(
                        FilenameUtils.removeExtension(f.getName()),
                        ChatColor.GOLD + arenaConfig.getString("details.name"),
                        arenaConfig.getString("details.description"),
                        Material.valueOf(arenaConfig.getString("details.display-item")),
                        arenaConfig.getString("settings.world-centre"),
                        arenaConfig.getInt("settings.world-border-radius"),
                        arenaConfig.getInt("settings.deathmatch-border-radius"),
                        arenaConfig.getBoolean("settings.allow-world-breaking")
                );

                // Spawn settings
                if (arenaConfig.getBoolean("settings.spawn.randomize-spawn-locations")) {
                    option.setRandomizeSpawnLocations(true);
                    option.setRandomizeSpread(arenaConfig.getInt("settings.spawn.randomize-spread"));
                } else {
                    option.setRandomizeSpawnLocations(false);
                    option.setSpawnLocations(arenaConfig.getStringList("settings.spawn.spawn-locations"));
                }

                // Starting items
                if (arenaConfig.contains("settings.starting-items", false)) {
                    List<ItemStack> startingItems = new ArrayList<>();
                    ItemStack startingItem = null;
                    for (String s : arenaConfig.getConfigurationSection("settings.starting-items").getKeys(false)) {
                        try {
                            startingItem = arenaConfig.getItemStack("settings.starting-items." + s);
                            startingItems.add(startingItem);
                        } catch (Exception e) {
                            Main.getInstance().getLogger().warning("Could not parse starting item: {" + s + "}. Exception: " + e.getMessage());
                        }
                    }
                    option.setStartingItems(startingItems);
                }

                // Loot settings
                if (arenaConfig.getBoolean("settings.loot.loot-chests-enabled") || arenaConfig.getBoolean("settings.loot.feast-enabled")) {
                    option.setMinSlotsFilled(arenaConfig.getInt("settings.loot.min-slots-filled"));
                    option.setMaxSlotsFilled(arenaConfig.getInt("settings.loot.max-slots-filled"));
                }
                if (arenaConfig.getBoolean("settings.loot.loot-chests-enabled")) {
                        option.setLootEnabled(true);
                        option.setRareLootMultiplier(arenaConfig.getDouble("settings.loot.rare-loot-multiplier"));
                        option.setRefillLootMultiplier(arenaConfig.getDouble("settings.loot.refill-loot-multiplier"));
                        option.setRefillRareLootMultiplier(arenaConfig.getDouble("settings.loot.refill-rare-loot-multiplier"));
                        option.setClearLootOnStart(arenaConfig.getBoolean("settings.loot.clear-chests-on-game-start"));
                    try {
                        if (arenaConfig.getConfigurationSection("settings.loot.loot-table").getKeys(false).isEmpty()) {
                            Main.getInstance().getLogger().warning("Loot chests are enabled for map " + f.getName() + " but loot table was empty! Please fill in a loot table for this to work properly!");
                        }
                    } catch (NullPointerException e) {
                        Main.getInstance().getLogger().warning("Loot chests are enabled for map " + f.getName() + " but loot table was not found! Please fill in a loot table for this to work properly!");
                    }
                } else {
                    option.setLootEnabled(false);
                }

                // Feast settings
                if (arenaConfig.getBoolean("settings.loot.feast-enabled")) {
                    option.setFeastEnabled(true);
                    option.setFeastLootMultiplier(arenaConfig.getDouble("settings.loot.feast-loot-multiplier"));
                    if (arenaConfig.isInt("settings.loot.feast-y-coord")) {
                        option.setFeastYCoord(arenaConfig.getInt("settings.loot.feast-y-coord"));
                    }
                    try {
                        if (arenaConfig.getConfigurationSection("settings.loot.loot-table").getKeys(false).isEmpty()) {
                            Main.getInstance().getLogger().warning("Loot chests are enabled for map " + f.getName() + " but loot table was empty! Please fill in a loot table for this to work properly!");
                        }
                    } catch (NullPointerException e) {
                        Main.getInstance().getLogger().warning("Feast is enabled for map " + f.getName() + " but loot table was not found! Please fill in a loot table for this to work properly!");
                    }
                } else {
                    option.setFeastEnabled(false);
                }

                // LootTable
                if (arenaConfig.isConfigurationSection("settings.loot.loot-table")) {
                    HashMap<LootEntry, Integer> lootTable = new HashMap<>();
                    LootEntry curLootEntry = null;
                    EnchantmentEntry curEnchEntry = null;
                    try {
                        for (String material : arenaConfig.getConfigurationSection("settings.loot.loot-table").getKeys(false)) {
                            try {
                                List<EnchantmentEntry> enchantmentEntries = new ArrayList<>();
                                if (arenaConfig.contains("settings.loot.loot-table." + material + ".enchantments", false)) {
                                    for (Map<?, ?> enchantmentEntry : arenaConfig.getMapList("settings.loot.loot-table." + material + ".enchantments")) {
                                        curEnchEntry = new EnchantmentEntry(
                                                EnchantmentWrapper.getByKey(NamespacedKey.minecraft(((String) enchantmentEntry.get("type")).toLowerCase())),
                                                (Integer) enchantmentEntry.get("level"),
                                                enchantmentEntry.get("chance") instanceof Integer ?
                                                        (Double) ((Integer) enchantmentEntry.get("chance")).doubleValue()
                                                        : (Double) enchantmentEntry.get("chance")
                                        );
                                        enchantmentEntries.add(curEnchEntry);
                                    }
                                }
                                curLootEntry = new LootEntry(
                                        Material.matchMaterial(material),
                                        arenaConfig.getInt("settings.loot.loot-table." + material + ".min"),
                                        arenaConfig.getInt("settings.loot.loot-table." + material + ".max"),
                                        enchantmentEntries
                                );
                                lootTable.put(
                                        curLootEntry,
                                        arenaConfig.getInt("settings.loot.loot-table." + material + ".commonness")
                                );
                            } catch (Exception e) {
                                Main.getInstance().getLogger().warning("Could not parse loot table entry: {" + material + "}. Exception: " + e.getMessage());
                            }
                        }
                        option.setLootTable(lootTable);
                    } catch (Exception e) {
                        Main.getInstance().getLogger().warning("Something went wrong parsing gamemap file " + f.getName() + "'s loot table! Exception: " + e.getMessage());
                    }
                }

                // Random gamemap handling
                if (option.getFilename().equals("random")) {
                    Main.getInstance().getLogger().info("Loaded random world option");
                    option.setTitle(option.getTitle().replace("§6", "§a"));
                    randomWorld = option;
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
        if (randomWorld.getFilename().equals(map.getFilename())) {
            gamemapFolder = "random";
            Bukkit.createWorld(WorldCreator.name(gamemapFolder));
            retryRandomWorldUntilNoOceanAtSpawn();
        } else {
            gamemapFolder = "customarena";
            Main.getInstance().getLogger().info("Copying map: " + Main.getInstance().getDataFolder() + "/arenas/" + map.getFilename());
            File mapFolder = new File(Main.getInstance().getDataFolder() + "/arenas/" + map.getFilename());
            File destFolder = new File("./" + gamemapFolder);
            try {
                FileUtils.copyDirectory(mapFolder, destFolder);
            } catch (IOException e) {
                Main.getInstance().getLogger().severe("Something went wrong copying arena to server directory!");
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
            Main.getInstance().getLogger().warning("Random world generated water at spawn! Regenerating...");
            Bukkit.unloadWorld(gamemapFolder, false);
            File deleteFolder = new File("./" + gamemapFolder);
            Main.deleteWorld(deleteFolder);
            Bukkit.createWorld(WorldCreator.name(gamemapFolder));
            retryRandomWorldUntilNoOceanAtSpawn();
        }
        Main.getInstance().getLogger().info("World did not have water at spawn!");
    }

    private void setGameOptions(World arena, Gamemap map) {
        arena.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        arena.setGameRule(GameRule.DO_TILE_DROPS, true);
        arena.setTime(500);
        arena.setDifficulty(Difficulty.NORMAL);
        arena.getWorldBorder().setCenter(generateLocation(deserializeCoordinate(map.getWorldCentre()), arena));
        arena.getWorldBorder().setSize(map.getBorderRadius() * 2);
        arena.setAutoSave(false);
    }

    public boolean isMapLoaded() {
        return mapLoaded;
    }

    public World getArenaWorld() {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Main.getInstance().getLogger().severe("Tried to get arena World object when arena isn't loaded!");
        }
        return arenaWorld;
    }

    public Gamemap getArenaGamemap() {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Main.getInstance().getLogger().severe("Tried to get arena Gamemap object when arena isn't loaded!");
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
            Main.getInstance().getLogger().severe("Cannot deserialize location!");
            return null;
        }
    }

    public List<Location> getSpawnLocations(int playerCount) {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Main.getInstance().getLogger().severe("Tried to get arena spawn locations when arena isn't loaded!");
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
                Main.getInstance().getLogger().severe("Could not generate random spawn locations for players! Exception: " + e.getMessage());
                return null;
            }
        } else {
            try {
                List<Coordinates> coords = new ArrayList<>();
                for (String s : arenaGamemap.getSpawnLocations()) {
                    coords.add(deserializeCoordinate(s));
                }

                if (coords.isEmpty()) {
                    Main.getInstance().getLogger().severe("Spawn locations list was null!");
                    throw new Exception("Spawn locations expected, but none were found for " + getArenaGamemap().getFilename());
                }

                int listPos = 0;
                for (int i = 0; i < playerCount; i++) {
                    if (listPos >= coords.size()) listPos = 0;
                    locations.add(generateLocation(coords.get(listPos), arenaWorld));
                    listPos++;
                }
            } catch (Exception e) {
                Main.getInstance().getLogger().severe("Could not generate custom spawn locations for players! Exception: " + e.getMessage());
                return null;
            }
        }

        return locations;
    }

    public List<Location> getDeathmatchLocations(int playerCount) {
        if (gamemapFolder.equals("") || !mapLoaded) {
            Main.getInstance().getLogger().severe("Tried to get arena deathmatch locations when arena isn't loaded!");
            return null;
        }

        Random random = new Random();
        List<Location> locations = new ArrayList<>();

        try {
            Coordinates coords = deserializeCoordinate(arenaGamemap.getWorldCentre());
            for (int i = 0; i < playerCount; i++) {
                int x = (int) (random.nextInt((arenaGamemap.getDeathmatchBorderRadius() - 1) * 2) - (arenaGamemap.getDeathmatchBorderRadius() - 1) + coords.getX());
                int z = (int) (random.nextInt((arenaGamemap.getDeathmatchBorderRadius() - 1) * 2) - (arenaGamemap.getDeathmatchBorderRadius() - 1) + coords.getZ());
                locations.add(new Location(arenaWorld,
                                x,
                                arenaWorld.getHighestBlockYAt(x, z) + 1,
                                z
                        )
                );
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not generate random deathmatch locations for players!");
            return null;
        }

        return locations;
    }

    public Location getArenaCentre() {
        return generateLocation(deserializeCoordinate(arenaGamemap.getWorldCentre()), arenaWorld);
    }
}
