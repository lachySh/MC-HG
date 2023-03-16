package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.worm.InstantMineDirt;
import org.bukkit.Material;

import java.util.List;

public class Worm extends Kit {
    public Worm() {
        super(
                "worm",
                "Worm",
                "Burrow beneath the ground quickly.\nLeft clicking dirt will activate\ndirt insta-mine for 7 seconds.\nThis has a 10 second cooldown.\nAlso works for sand and gravel!",
                false,
                KitType.DEFENSIVE,
                Material.DIRT,
                List.of(),
                List.of(new InstantMineDirt())
        );
    }
}
