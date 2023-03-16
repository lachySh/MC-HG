package com.au.lachysh.mchg.kits;

import org.bukkit.Material;
import com.au.lachysh.mchg.abilities.cultivator.InstantGrow;

import java.util.List;

public class Cultivator extends Kit {
    public Cultivator() {
        super(
                "cultivator",
                "Cultivator",
                "Instantly grow crops and trees.\nGreat for surviving with nothing but saplings, dirt and seeds.",
                false,
                KitType.UTILITY,
                Material.OAK_SAPLING,
                List.of(),
                List.of(new InstantGrow())
        );
    }
}
