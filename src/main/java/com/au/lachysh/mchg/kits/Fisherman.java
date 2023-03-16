package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.apparition.Invisibility;
import com.au.lachysh.mchg.abilities.fisherman.FishTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Fisherman extends Kit {
    public Fisherman() {
        super(
                "fisherman",
                "Fisherman",
                "Hook onto players and pull them directly to your location.\nGreat for pulling your opponents off heights or into lava.",
                true,
                KitType.FIGHTER,
                Material.FISHING_ROD,
                List.of(fishingRod()),
                List.of(new FishTeleport())
        );
    }

    private static ItemStack fishingRod() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Fisherman's Rod");
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        item.serialize();

        return item;
    }
}
