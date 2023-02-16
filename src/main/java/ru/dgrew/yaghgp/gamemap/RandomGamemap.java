package ru.dgrew.yaghgp.gamemap;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class RandomGamemap extends Gamemap {
    private String datapack;

    public RandomGamemap(String filename, String title, String description, Material displayMaterial, String worldCentre, Integer borderRadius, Integer deathmatchBorderRadius, Boolean allowWorldBreaking, Boolean randomizeSpawnLocations, Integer randomizeSpread, List<String> spawnLocations) {
        super(filename, title, description, displayMaterial, worldCentre, borderRadius, deathmatchBorderRadius, allowWorldBreaking, randomizeSpawnLocations, randomizeSpread);
        this.setSpawnLocations(spawnLocations);
    }

    public void setDatapack(String datapack) {
        this.datapack = datapack;
    }
}
