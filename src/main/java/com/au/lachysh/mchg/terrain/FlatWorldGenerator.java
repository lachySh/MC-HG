package com.au.lachysh.mchg.terrain;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This the actual terrain generator.
 *
 */
public class FlatWorldGenerator extends ChunkGenerator {

    private final FastNoiseLite terrainNoise = new FastNoiseLite();
    private final FastNoiseLite detailNoise = new FastNoiseLite();

    private static final int CHUNK_SIZE = 16;
    private static final int TERRAIN_HEIGHT = 66;

    public FlatWorldGenerator() {
        // Set frequencies, lower frequency = slower change.
        terrainNoise.SetFrequency(0.0002f);
        detailNoise.SetFrequency(0.02f);

        // Fractal pattern (optional).
        terrainNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        terrainNoise.SetFractalOctaves(5);
    }

    @Nullable
    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        BiomeProvider defaultBiomeProvider = DefaultBiomeProvider.getBiomeProvider(Bukkit.getWorld("world"));
        return new OceanlessBiomeGenerator(defaultBiomeProvider);
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for(int y = chunkData.getMinHeight(); y < 96 && y < chunkData.getMaxHeight(); y++) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    float noise2 = (terrainNoise.GetNoise(x + (chunkX * 16), z + (chunkZ * 16)) * 2) + (detailNoise.GetNoise(x + (chunkX * 16), z + (chunkZ * 16)) / 10);
                    if(65 + (30 * noise2) > y) {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }
                }
            }
        }
    }

    @Override
    public int getBaseHeight(WorldInfo world, Random random, int x, int z, HeightMap type) {
        return TERRAIN_HEIGHT;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false; // We do this ourselves
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

}
