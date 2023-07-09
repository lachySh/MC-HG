package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.viper.PoisonChance;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Viper extends Kit {

    public Viper() {
        super(
                "viper",
                "Viper",
                "All attacks on players have a 1/3 chance of inflicting poison.\nPoisoned enemies struggle to sprint, making escape harder.\nThe poison effect lasts for 5 seconds.",
                true,
                KitType.FIGHTER,
                Material.SPIDER_EYE,
                List.of(),
                List.of(new PoisonChance())
        );
    }
}
