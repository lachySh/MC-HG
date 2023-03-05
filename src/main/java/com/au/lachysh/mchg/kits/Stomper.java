package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.stomper.TransferFallDamage;
import org.bukkit.Material;

import java.util.List;

public class Stomper extends Kit {
    public Stomper() {
        super(
                "stomper",
                "Stomper",
                "Falling doesn't hurt you... it hurts others\nFalling will transfer your fall damage to those below you.\nFalling only does a maximum of 2 hearts damage to you.",
                true,
                KitType.FIGHTER,
                Material.NETHERITE_BOOTS,
                List.of(),
                List.of(new TransferFallDamage())
        );
    }
}
