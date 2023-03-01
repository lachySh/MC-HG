package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.endermage.TeleportEntities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Endermage extends Kit {
    public Endermage() {
        super(
                "endermage",
                "Endermage",
                "You can run, but you can't hide.\nTeleport players (and animals) above and below you to your placed portal.\nVictims gain 3 seconds of invincibility to prepare.\nThey can't hurt you for 3 seconds.",
                KitType.UTILITY,
                Material.ENDER_EYE,
                List.of(endermagesPortal()),
                List.of(new TeleportEntities())
        );
    }

    public static ItemStack endermagesPortal() {
        ItemStack item = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Endermage's Portal");
        item.setItemMeta(meta);

        return item;
    }
}
