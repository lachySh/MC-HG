package com.au.lachysh.mchg.biome;

import net.minecraft.world.level.biome.BiomeSource;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.generator.CustomChunkGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BiomeProvider;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class WorldInitEventListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldInitEvent(WorldInitEvent event) {
        World world = event.getWorld();
        if (!world.getName().equals("random")) return;
        getLogger().info("Injecting custom biome provider to generation of '" + world.getName() + "'");

        // Here we construct the new biome generator
        BiomeProvider defaultBiomeProvider = DefaultBiomeProvider.getBiomeProvider(world);
        setBiomeProvider(world, new OceanlessBiomeGenerator(defaultBiomeProvider));
    }

    /**
     * Author: Rutger Kok
     */
    public static void setBiomeProvider(World world, BiomeProvider biomeProvider) {
        Objects.requireNonNull(biomeProvider, "biomeProvider");

        // First, set in CraftWorld
        CraftWorld craftWorld = (CraftWorld) world;
        try {
            ReflectionUtil.getFieldOfType(craftWorld, BiomeProvider.class).set(craftWorld, biomeProvider);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject biome provider into world", e);
        }

        // Next, set in Minecraft's ChunkGenerator
        BiomeSource biomeSource = DefaultBiomeProvider.bukkitToMinecraft(craftWorld.getHandle(), biomeProvider);
        net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator;
        chunkGenerator = craftWorld.getHandle().getChunkSource().getGenerator();

        // Bukkit's chunk generator uses a delegate field, in which the biome provider
        // is stored
        if (chunkGenerator instanceof CustomChunkGenerator) {
            try {
                chunkGenerator = (net.minecraft.world.level.chunk.ChunkGenerator) ReflectionUtil
                        .getFieldOfType(chunkGenerator, net.minecraft.world.level.chunk.ChunkGenerator.class)
                        .get(chunkGenerator);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get CustomChunkGenerator.delegate field");
            }
        }

        for (Field field : ReflectionUtil.getAllFieldsOfType(chunkGenerator.getClass(), BiomeSource.class)) {
            try {
                field.set(chunkGenerator, biomeSource);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to inject biome source into chunkGenerator." + field.getName(), e);
            }
        }

        // Test if it worked (better to catch errors early)
        if (DefaultBiomeProvider.getBiomeProvider(world) != biomeProvider) {
            throw new RuntimeException("Failed to inject biome provider; unknown reason");
        }
    }
}
