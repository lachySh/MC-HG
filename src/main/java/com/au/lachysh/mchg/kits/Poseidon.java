package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.poseidon.WaterBonuses;
import com.au.lachysh.mchg.abilities.viper.PoisonChance;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.Material;

import java.util.List;

public class Poseidon extends Kit {
    public Poseidon() {
        super(
                "poseidon",
                "Poseidon",
                "When in water, you are granted Strength I, Speed II,\nand Dolphin's Grace. Great for luring unsuspecting\nvictims to water for your advantage.",
                true,
                KitType.FIGHTER,
                Material.TROPICAL_FISH_BUCKET,
                List.of(),
                List.of(new WaterBonuses())
        );
    }
}
