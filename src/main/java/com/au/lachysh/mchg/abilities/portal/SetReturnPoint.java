package com.au.lachysh.mchg.abilities.portal;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Hearthstone;
import com.au.lachysh.mchg.kits.Thor;
import com.au.lachysh.mchg.shared.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class SetReturnPoint extends Ability<PlayerInteractEvent> {
    private Location returnPoint;
    private static final String RETURN_POINT_SET = ChatColor.GREEN + "You have successfully set your return point!";
    public SetReturnPoint() {
        super("Set return point", PlayerInteractEvent.class, 60, false);
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.LAPIS_LAZULI &&
                (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
                && Hearthstone.itemNameUnset.contains(event.getItem().getItemMeta().getDisplayName());
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player p = event.getPlayer();
            returnPoint = p.getLocation();
            ChatUtils.sendActionbar(p, RETURN_POINT_SET);

            ItemMeta meta = event.getItem().getItemMeta();
            meta.setDisplayName(Hearthstone.itemNameSet);
            event.getItem().setItemMeta(meta);
            p.updateInventory();
        };
    }

    public Location getReturnPoint() {
        return returnPoint;
    }
}
