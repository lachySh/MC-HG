package ru.dgrew.yaghgp.kits;

import org.bukkit.Material;
import ru.dgrew.yaghgp.abilities.cultivator.InstantGrow;
import ru.dgrew.yaghgp.abilities.stomper.TransferFallDamage;

import java.util.List;

public class Stomper extends Kit {
    public Stomper() {
        super(
                "stomper",
                "Stomper",
                "Falling doesn't hurt you... it hurts others\nFalling will transfer your fall damage to those below you.\nFalling only does a maximum of 2 hearts damage to you.",
                KitType.FIGHTER,
                Material.NETHERITE_BOOTS,
                List.of(),
                List.of(new TransferFallDamage())
        );
    }
}
