package ru.dgrew.yaghgp.abilities.cultivator;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.abilities.AbilityCallable;

import java.util.Map;
import java.util.Optional;

public class InstantGrow extends Ability<BlockPlaceEvent> {

    private static final Map<Material, TreeType> SAPLING_TO_TREETYPE = Map.of(
            Material.OAK_SAPLING, TreeType.TREE,
            Material.SPRUCE_SAPLING, TreeType.REDWOOD,
            Material.ACACIA_SAPLING, TreeType.ACACIA,
            Material.BIRCH_SAPLING, TreeType.BIRCH,
            Material.DARK_OAK_SAPLING, TreeType.TREE,
            Material.JUNGLE_SAPLING, TreeType.SMALL_JUNGLE
    );

    public InstantGrow() {
        super("Instant grow", BlockPlaceEvent.class, 0, false);
    }

    @Override
    public boolean precondition(BlockPlaceEvent event) {
        return isAgeable(event.getBlockPlaced()) || isSapling(event.getBlockPlaced());
    }

    @Override
    public AbilityCallable<BlockPlaceEvent> getCallable() {
        return event -> {
            Location eventLoc = event.getBlockPlaced().getLocation();
            if (isAgeable(event.getBlockPlaced())) {
                World world = event.getBlockPlaced().getWorld();
                var ageable = (Ageable) event.getBlockPlaced().getBlockData();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ageable.setAge(ageable.getMaximumAge());
                        event.getBlock().setBlockData(ageable);
                        world.playEffect(eventLoc, Effect.BONE_MEAL_USE, 15, 1);
                    }
                }.runTaskLater(Main.getInstance(), 1);

            } else if (isSapling(event.getBlockPlaced())) {
                World world = event.getBlockPlaced().getWorld();
                TreeType treeType = Optional.ofNullable(SAPLING_TO_TREETYPE.get(event.getBlockPlaced().getType())).orElse(TreeType.TREE);
                Material saplingType = event.getBlockPlaced().getType();
                event.getBlockPlaced().setType(Material.AIR);
                var success = world.generateTree(
                        event.getBlock().getLocation(),
                        treeType
                );
                new BukkitRunnable() {
                    @Override
                    public void run() {
                    if (!success) {
                        world.playEffect(eventLoc, Effect.INSTANT_POTION_BREAK, 15, 2);
                        world.dropItemNaturally(eventLoc, new ItemStack(saplingType));
                    } else {
                        world.playEffect(eventLoc, Effect.BONE_MEAL_USE, 15, 3);
                    }
                    }
                }.runTaskLater(Main.getInstance(), 1);
            }
            cooldown();
        };
    }

    // Saplings
    private static boolean isSapling(Block block) {
        return block.getBlockData() instanceof Sapling;
    }

    // Plants
    private static boolean isAgeable(Block block) {
        return block.getBlockData() instanceof Ageable;
    }
}
