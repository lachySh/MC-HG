package ru.dgrew.yaghgp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dgrew.yaghgp.abilities.apparition.Invisibility;

import java.util.List;

public class Apparition extends Kit {
    public Apparition() {
        super(
                "apparition",
                "Apparition",
                "Become a ghost...\nYou will be given a 'Invisibility Orb' upon the game starting.\nWhenever you use this, you will become invisible for 3 seconds.",
                KitType.DEFENSIVE,
                Material.FIREWORK_STAR,
                List.of(invisibilityCharge()),
                List.of(new Invisibility())
        );
    }

    private static ItemStack invisibilityCharge() {
        ItemStack item = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Invisibility Orb");
        item.setItemMeta(meta);

        return item;
    }
}