package com.au.lachysh.mchg.terrain;

import com.au.lachysh.mchg.Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R3.generator.CustomWorldChunkManager;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Original Author: Rutger Kok
 */
public class DefaultBiomeProvider extends BiomeProvider {
    public static BiomeProvider getBiomeProvider(WorldInfo world) throws IllegalStateException {
        CraftWorld craftWorld = getCraftWorld(world);
        ServerLevel serverLevel = craftWorld.getHandle();
        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
        return minecraftToBukkit(serverLevel, chunkGenerator);
    }

    public static BiomeProvider minecraftToBukkit(ServerLevel world, ChunkGenerator chunkGenerator) {
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
        if (biomeSource instanceof CustomWorldChunkManager worldChunkManager) {
            // Just return the BiomeProvider stored inside the CustomWorldChunkManager
            // Dig it up using reflection
            try {
                return (BiomeProvider) ReflectionUtil.getFieldOfType(worldChunkManager, BiomeProvider.class)
                        .get(worldChunkManager);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get BiomeProvider from CustomWorldChunkManager", e);
            }
        }

        // Ok, we need to wrap
        Registry<Biome> worldBiomeRegistry = world.registryAccess().registryOrThrow(Registries.BIOME);
        return new DefaultBiomeProvider(chunkGenerator.getBiomeSource(), world.getChunkSource().randomState().sampler(),
                worldBiomeRegistry);
    }

    public static BiomeSource bukkitToMinecraft(ServerLevel world, BiomeProvider provider) {
        Registry<Biome> worldBiomeRegistry = world.registryAccess().registryOrThrow(Registries.BIOME);

        if (provider instanceof DefaultBiomeProvider defaultBiomeProvider) {
            // Found an underlying Minecraft biome generator, check if it's compatible
            if (defaultBiomeProvider.registry.equals(worldBiomeRegistry)) {
                // Yes! We can directly return that, no need for two rounds of conversion
                return defaultBiomeProvider.biomeSource;
            }
        }

        return new CustomWorldChunkManager(world.getWorld(), provider, worldBiomeRegistry);
    }

    private static CraftWorld getCraftWorld(WorldInfo world) {
        if (!(world instanceof CraftWorld)) {
            world = Main.getInstance().getServer().getWorld(world.getUID());
        }

        if (world instanceof CraftWorld craftWorld) {
            return craftWorld;
        }

        throw new IllegalStateException("World not yet loaded");
    }

    private final BiomeSource biomeSource;
    private final Registry<Biome> registry;
    private final Climate.Sampler sampler;

    DefaultBiomeProvider(BiomeSource biomeSource, Climate.Sampler sampler, Registry<Biome> registry) {
        this.biomeSource = Objects.requireNonNull(biomeSource, "biomeSource");
        this.sampler = Objects.requireNonNull(sampler, "sampler");
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    @Override
    public org.bukkit.block.Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        return CraftBlock.biomeBaseToBiome(registry, biomeSource.getNoiseBiome(x >> 2, y >> 2, z >> 2, sampler));
    }

    @Override
    public List<org.bukkit.block.Biome> getBiomes(WorldInfo worldInfo) {
        List<org.bukkit.block.Biome> possibleBiomes = new ArrayList<>();
        for (Holder<Biome> biome : biomeSource.possibleBiomes()) {
            possibleBiomes.add(CraftBlock.biomeBaseToBiome(registry, biome));
        }
        return possibleBiomes;
    }
}
