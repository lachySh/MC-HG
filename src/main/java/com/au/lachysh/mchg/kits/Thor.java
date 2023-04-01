package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.spy.SpyCompassTrack;
import com.au.lachysh.mchg.abilities.thor.LightningCall;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Thor extends Kit {
    // NOTE: See CompassHandout for cancellation of normal compass handout
    public static final String itemName = ChatColor.RESET + "" + ChatColor.YELLOW + "Mjölnir";

    public Thor() {
        super(
            "thor",
            "Thor",
            "You will be given the Mjölnir upon game start.\nRight click it to call down lightning upon a block.\nThis has a 15 second cooldown.",
            true,
            KitType.FIGHTER,
            Material.WOODEN_AXE,
            List.of(mjolnir()),
            List.of(new LightningCall())
        );
    }

    public static ItemStack mjolnir() {
        ItemStack item = new ItemStack(Material.WOODEN_AXE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        item.setItemMeta(itemMeta);

        return item;
    }
}