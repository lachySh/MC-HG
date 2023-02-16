package ru.dgrew.yaghgp.shared;

import org.bukkit.block.Biome;

public class BiomeChecker {
    public static boolean isOcean(Biome b) {
        return b == Biome.BEACH ||
                b == Biome.COLD_OCEAN ||
                b == Biome.DEEP_COLD_OCEAN ||
                b == Biome.DEEP_FROZEN_OCEAN ||
                b == Biome.DEEP_OCEAN ||
                b == Biome.DEEP_LUKEWARM_OCEAN ||
                b == Biome.FROZEN_OCEAN ||
                b == Biome.LUKEWARM_OCEAN ||
                b == Biome.OCEAN ||
                b == Biome.RIVER ||
                b == Biome.WARM_OCEAN ||
                b == Biome.SNOWY_BEACH ||
                b == Biome.STONY_SHORE ||
                b == Biome.FROZEN_RIVER;
    }
}
