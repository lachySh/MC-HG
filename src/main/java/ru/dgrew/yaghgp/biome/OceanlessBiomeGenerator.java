package ru.dgrew.yaghgp.biome;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import ru.dgrew.yaghgp.shared.BiomeChecker;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class OceanlessBiomeGenerator extends BiomeProvider {
    private Random rand;
    private int replacementIndex;
    private static List<Biome> replacementBiomes = List.of(
            Biome.PLAINS,
            Biome.DESERT,
            Biome.FOREST,
            Biome.SAVANNA,
            Biome.JUNGLE
    );

    private final BiomeProvider originalBiomeGenerator;

    public OceanlessBiomeGenerator(BiomeProvider originalBiomeGenerator) {
        this.originalBiomeGenerator = Objects.requireNonNull(originalBiomeGenerator, "originalBiomeGenerator");
        this.rand = new Random();
        this.replacementIndex = rand.nextInt(replacementBiomes.size());
    }

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        Biome originalBiome = this.originalBiomeGenerator.getBiome(worldInfo, x, y, z);
        if (BiomeChecker.isOcean(originalBiome)) {
            return getRandomReplacementBiome();
        }
        return originalBiome;
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return originalBiomeGenerator.getBiomes(worldInfo);
    }

    private Biome getRandomReplacementBiome() {
        return replacementBiomes.get(replacementIndex);
    }
}