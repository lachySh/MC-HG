package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.kangaroo.BoostedFallDetector;
import com.au.lachysh.mchg.abilities.kangaroo.JumpBoost;
import com.au.lachysh.mchg.abilities.kangaroo.OnNextFallCancelDamage;
import com.au.lachysh.mchg.abilities.portal.OnDamageCancelSpell;
import com.au.lachysh.mchg.abilities.portal.ReturnToPoint;
import com.au.lachysh.mchg.abilities.portal.SetReturnPoint;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Kangaroo extends Kit {
    public static final String itemName = ChatColor.RESET + "" + ChatColor.YELLOW + "Double Jump Rocket";

    public Kangaroo() {
        super(
            "kangaroo",
            "Kangaroo",
            "You will be given a 'Double Jump Rocket'\nupon game start. Right-click your rocket to boost yourself\ninto the air. This has a 5 second cooldown.\nYou won't take any fall damage from\nusing the rocket.",
            true,
            KitType.DEFENSIVE,
            Material.FIREWORK_ROCKET,
            List.of(rocket()),
            List.of()
        );

        JumpBoost jumpBoost = new JumpBoost();
        this.setKitAbilities(List.of(jumpBoost, new OnNextFallCancelDamage(jumpBoost), new BoostedFallDetector(jumpBoost)));
    }

    public static ItemStack rocket() {
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        item.setItemMeta(itemMeta);

        return item;
    }
}