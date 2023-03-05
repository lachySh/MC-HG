package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.endermage.TeleportEntities;
import net.minecraft.nbt.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Endermage extends Kit {
    private static List<String> allItems;
    private static NBTTagList nbtTagList;

    public Endermage() {
        super(
                "endermage",
                "Endermage",
                "You can run, but you can't hide.\nTeleport players (and animals) above and below you to your placed portal.\nVictims gain 3 seconds of invincibility to prepare.\nThey can't hurt you for 3 seconds.\nNote: The portal is placeable even when block placing is disabled for map",
                true,
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

        net.minecraft.world.item.ItemStack mcStack = CraftItemStack.asNMSCopy(item);

        if (nbtTagList == null) {
            NBTTagList placeable = (NBTTagList) mcStack.getTag().get("CanPlaceOn");
            if (placeable == null) {
                placeable = new NBTTagList();
            }

            if (allItems == null) {
                initAllItems();
            }

            for (String s : allItems) {
                placeable.add(NBTTagString.a(s));
            }

            nbtTagList = placeable;
        }

        CompoundTag compound = mcStack.getOrCreateTag();
        Tag tag = (Tag) nbtTagList;
        compound.put("CanPlaceOn", tag);
        compound.put("HideFlags", IntTag.valueOf(16));

        return CraftItemStack.asBukkitCopy(mcStack);
    }

    private static void initAllItems() {
        allItems = new ArrayList<>();
        for (Material m : Material.values()) {
            allItems.add("minecraft:" + String.valueOf(m).toLowerCase());
        }
    }
}
