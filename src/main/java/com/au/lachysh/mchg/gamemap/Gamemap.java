package com.au.lachysh.mchg.gamemap;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.loot.LootEntry;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Boolean lootEnabled;
    private Boolean clearLootOnStart;
    private Integer minSlotsFilled;
    private Integer maxSlotsFilled;
    private Map<LootEntry, Integer> lootTable;
    private Double rareLootMultiplier;
    private Double refillLootMultiplier;
    private Double refillRareLootMultiplier;

    public Gamemap(String filename,
                   String title,
                   String description,
                   Material displayMaterial,
                   String worldCentre,
                   Integer borderRadius,
                   Integer deathmatchBorderRadius,
                   Boolean allowWorldBreaking
    ) {
        assertNotNull(filename, "filename");
        assertNotNull(title, "title");
        assertNotNull(description, "description");
        assertNotNull(displayMaterial, "display-item");
        assertNotNull(worldCentre, "settings.world-centre");
        assertNotNull(borderRadius, "settings.world-border-radius");
        assertNotNull(allowWorldBreaking, "settings.allow-world-breaking");
        assertNotNull(deathmatchBorderRadius, "settings.deathmatch-border-radius");

        this.filename = filename;
        this.title = title;
        this.description = description;
        this.displayMaterial = displayMaterial;
        this.worldCentre = worldCentre;
        this.borderRadius = borderRadius;
        this.allowWorldBreaking = allowWorldBreaking;
        this.deathmatchBorderRadius = deathmatchBorderRadius;
        this.randomizeSpawnLocations = false;
        this.randomizeSpread = 30;
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
            Main.getInstance().getLogger().severe("Could not load map! " + fieldName + " is null for map " + filename);
        }
    }
}
