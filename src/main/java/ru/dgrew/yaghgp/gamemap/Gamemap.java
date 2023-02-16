package ru.dgrew.yaghgp.gamemap;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class Gamemap {
    private String filename;
    private String title;
    private String description;
    private Material displayMaterial;
    private String worldCentre;
    private Integer borderRadius;
    private Integer deathmatchBorderRadius;
    private Boolean allowWorldBreaking;
    private Boolean randomizeSpawnLocations;
    private Integer randomizeSpread;
    private List<String> spawnLocations;

    public Gamemap(String filename,
                   String title,
                   String description,
                   Material displayMaterial,
                   String worldCentre,
                   Integer borderRadius,
                   Integer deathmatchBorderRadius,
                   Boolean allowWorldBreaking,
                   Boolean randomizeSpawnLocations,
                   Integer randomizeSpread
    ) {
        assertNotNull(filename, "filename");
        assertNotNull(title, "title");
        assertNotNull(description, "description");
        assertNotNull(displayMaterial, "display-item");
        assertNotNull(worldCentre, "settings.world-centre");
        assertNotNull(borderRadius, "settings.world-border-radius");
        assertNotNull(allowWorldBreaking, "settings.allow-world-breaking");
        assertNotNull(deathmatchBorderRadius, "settings.deathmatch-border-radius");
        assertNotNull(randomizeSpawnLocations, "settings.spawn.randomize-spawn-locations");
        assertNotNull(randomizeSpread, "settings.spawn.randomize-spread");
        if (randomizeSpawnLocations == false) {
            Bukkit.getLogger().info("Wrong constructor used for Gamemap " + filename);
        }

        this.filename = filename;
        this.title = title;
        this.description = description;
        this.displayMaterial = displayMaterial;
        this.worldCentre = worldCentre;
        this.borderRadius = borderRadius;
        this.allowWorldBreaking = allowWorldBreaking;
        this.deathmatchBorderRadius = deathmatchBorderRadius;
        this.randomizeSpawnLocations = randomizeSpawnLocations;
        this.randomizeSpread = randomizeSpread;
    }

    public Gamemap(String filename,
                   String title,
                   String description,
                   Material displayMaterial,
                   String worldCentre,
                   Integer borderRadius,
                   Integer deathmatchBorderRadius,
                   Boolean allowWorldBreaking,
                   Boolean randomizeSpawnLocations,
                   List<String> spawnLocations
    ) {
        assertNotNull(filename, "filename");
        assertNotNull(title, "title");
        assertNotNull(description, "description");
        assertNotNull(displayMaterial, "display-item");
        assertNotNull(worldCentre, "settings.world-centre");
        assertNotNull(borderRadius, "settings.world-border-radius");
        assertNotNull(deathmatchBorderRadius, "settings.deathmatch-border-radius");
        assertNotNull(allowWorldBreaking, "settings.allow-world-breaking");
        assertNotNull(randomizeSpawnLocations, "settings.spawn.randomize-spawn-locations");
        assertNotNull(spawnLocations, "settings.spawn.spawn-locations");
        if (randomizeSpawnLocations == true) {
            Bukkit.getLogger().info("Wrong constructor used for Gamemap " + filename);
        }

        this.filename = filename;
        this.title = title;
        this.description = description;
        this.displayMaterial = displayMaterial;
        this.worldCentre = worldCentre;
        this.borderRadius = borderRadius;
        this.allowWorldBreaking = allowWorldBreaking;
        this.deathmatchBorderRadius = deathmatchBorderRadius;
        this.randomizeSpawnLocations = randomizeSpawnLocations;
        this.spawnLocations = spawnLocations;
    }

    public boolean equalsItemStack(ItemStack itemStack) {
        return itemStack.getItemMeta().getDisplayName().contains(title)
                && displayMaterial.equals(itemStack.getType());
    }

    public GameMode getGamemode() {
        if (allowWorldBreaking) {
            return GameMode.SURVIVAL;
        } else {
            return GameMode.ADVENTURE;
        }
    }

    private void assertNotNull(Object o, String fieldName) {
        if (o == null) {
            Bukkit.getLogger().severe("Could not load map! " + fieldName + " is null for map " + filename);
        }
    }

    public RandomGamemap toRandomGamemap() {
        return new RandomGamemap(
                this.filename,
                this.title,
                this.description,
                this.displayMaterial,
                this.worldCentre,
                this.borderRadius,
                this.deathmatchBorderRadius,
                this.allowWorldBreaking,
                this.randomizeSpawnLocations,
                this.randomizeSpread,
                this.spawnLocations
        );
    }
}
