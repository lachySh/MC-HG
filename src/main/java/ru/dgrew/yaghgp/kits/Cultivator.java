package ru.dgrew.yaghgp.kits;

import org.bukkit.Material;
import ru.dgrew.yaghgp.abilities.cultivator.InstantGrow;

import java.util.List;

public class Cultivator extends Kit {
    public Cultivator() {
        super(
                "cultivator",
                "Cultivator",
                "Instantly grow crops and trees.\nGreat for surviving above the ground with nothing but saplings, dirt and seeds.",
                KitType.UTILITY,
                Material.OAK_SAPLING,
                List.of(),
                List.of(new InstantGrow())
        );
    }
}
