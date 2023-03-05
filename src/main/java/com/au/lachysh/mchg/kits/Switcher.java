package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.switcher.SwapEntityLocations;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Switcher extends Kit {
    public Switcher() {
        super(
                "switcher",
                "Switcher",
                "Magic snowballs??\nHitting a player or mob with a snowball will cause you\nto switch places with it.\nStart the game with 5 snowballs.",
                true,
                KitType.FIGHTER,
                Material.SNOWBALL,
                List.of(fiveSnowballs()),
                List.of(new SwapEntityLocations())
        );
    }

    private static ItemStack fiveSnowballs() {
        return new ItemStack(Material.SNOWBALL, 5);
    }
}
