package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.forger.InstantSmelt;
import org.bukkit.Material;

import java.util.List;

public class Forger extends Kit {
    public Forger() {
        super(
                "forger",
                "Forger",
                "Smelt iron, copper and gold directly in the crafting table.\n1 coal / charcoal + 1 ore = 1 ingot.\nGreat for quickly gearing up.",
                false,
                KitType.UTILITY,
                Material.IRON_INGOT,
                List.of(),
                List.of(new InstantSmelt())
        );
    }
}
