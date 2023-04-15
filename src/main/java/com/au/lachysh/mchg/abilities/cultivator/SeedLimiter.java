package com.au.lachysh.mchg.abilities.cultivator;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class SeedLimiter extends Ability<BlockDropItemEvent> {
    public SeedLimiter() {
        super("Seed limiter", BlockDropItemEvent.class, 0, false);
    }

    @Override
    public boolean precondition(BlockDropItemEvent event) {
        return event.getBlockState().getType() == Material.WHEAT;
    }

    @Override
    public AbilityCallable<BlockDropItemEvent> getCallable() {
        return event -> {
            event.setCancelled(true);

            Item wheat = event.getItems()
                    .stream()
                    .filter(item -> item.getItemStack().getType() == Material.WHEAT)
                    .findFirst().get();

            World w = event.getBlockState().getLocation().getWorld();
            w.dropItemNaturally(event.getBlockState().getLocation(), wheat.getItemStack());
            w.dropItemNaturally(event.getBlockState().getLocation(), new ItemStack(Material.WHEAT_SEEDS, 1));

            cooldown();
        };
    }
}
