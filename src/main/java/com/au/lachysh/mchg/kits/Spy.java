package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.spy.SpyCompassTrack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Spy extends Kit {
    // NOTE: See CompassHandout for cancellation of normal compass handout
    private static ItemStack trackingCompassItem;
    public static final String itemName = ChatColor.RESET + "" + ChatColor.YELLOW + "Spy's Tracking Compass";

    public Spy() {
        super(
            "spy",
            "Spy",
            "You will be given the 'Spy's Tracking Compass' upon game start.\nIt's an upgraded version of the standard tracking compass.\nUse it to see opponent's exact distance away from you, with unlimited range.",
            true,
            KitType.UTILITY,
            Material.COMPASS,
            List.of(spyTrackingCompass()),
            List.of(new SpyCompassTrack())
        );
    }

    public static ItemStack spyTrackingCompass() {
        if (trackingCompassItem != null) return trackingCompassItem;

        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Spy's Tracking Compass");
        item.setItemMeta(itemMeta);

        trackingCompassItem = item;
        return trackingCompassItem;
    }
}