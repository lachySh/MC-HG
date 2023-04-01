package com.au.lachysh.mchg.abilities.intrinsic;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoupHeal extends Ability<PlayerInteractEvent> {

    public SoupHeal() {
        super("Soup heal", PlayerInteractEvent.class, 0, false);
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.MUSHROOM_STEW && event.getPlayer().getHealth() < 20.0 &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player player = event.getPlayer();
            player.setHealth(Math.min((player.getHealth() + 7.0), 20));
            ItemStack item = event.getItem();
            item.setType(Material.BOWL);
            item.setItemMeta(null);
            player.updateInventory();
            cooldown();
        };
    }


}
