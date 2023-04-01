package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.endermage.TeleportEntities;
import com.au.lachysh.mchg.abilities.viper.PoisonChance;
import net.minecraft.nbt.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Viper extends Kit {
    private static List<String> allItems;
    private static NBTTagList nbtTagList;

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
