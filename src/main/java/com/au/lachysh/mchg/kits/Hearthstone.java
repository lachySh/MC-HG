package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.chameleon.MobDisguise;
import com.au.lachysh.mchg.abilities.chameleon.OnDamageRemoveDisguise;
import com.au.lachysh.mchg.abilities.portal.OnDamageCancelSpell;
import com.au.lachysh.mchg.abilities.portal.ReturnToPoint;
import com.au.lachysh.mchg.abilities.portal.SetReturnPoint;
import com.au.lachysh.mchg.abilities.thor.LightningCall;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Hearthstone extends Kit {
    // NOTE: See CompassHandout for cancellation of normal compass handout
    public static final String itemNameUnset = ChatColor.RESET + "" + ChatColor.YELLOW + "Hearthstone (Unbounded)";

    public static final String itemNameSet = ChatColor.RESET + "" + ChatColor.YELLOW + "Hearthstone (Ready)";

    public Hearthstone() {
        super(
            "hearthstone",
            "Hearthstone",
            "Teleport to a prior, set location.\nRight-click your Hearthstone to set a return point.\nRight-click it again to channel a 5-second return spell.\nThe spell will slow and nauseate you, beware!\nIf damaged during this time, the spell will be cancelled.\nThis has a 60-second cooldown.",
            true,
            KitType.UTILITY,
            Material.LAPIS_LAZULI,
            List.of(hearthstone()),
            List.of()
        );

        var setReturnPoint = new SetReturnPoint();
        var returnToPoint = new ReturnToPoint(setReturnPoint);
        var onDamageCancelSpell = new OnDamageCancelSpell(returnToPoint);
        this.setKitAbilities(List.of(setReturnPoint, returnToPoint, onDamageCancelSpell));
    }

    public static ItemStack hearthstone() {
        ItemStack item = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemNameUnset);
        item.setItemMeta(itemMeta);

        return item;
    }
}