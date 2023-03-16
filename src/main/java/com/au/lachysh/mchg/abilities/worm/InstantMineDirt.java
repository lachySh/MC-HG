package com.au.lachysh.mchg.abilities.worm;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Endermage;
import com.au.lachysh.mchg.shared.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;

public class InstantMineDirt extends Ability<PlayerInteractEvent> {

    private static final List<Material> DIRTY_BLOCKS = List.of(
            Material.DIRT,
            Material.COARSE_DIRT,
            Material.GRASS,
            Material.GRASS_BLOCK,
            Material.PODZOL,
            Material.FARMLAND,
            Material.ROOTED_DIRT,
            Material.DIRT_PATH,
            Material.MYCELIUM,
            Material.SAND,
            Material.GRAVEL
    );

    private static final String START_MINING_TEXT = ChatColor.YELLOW + "Dirt insta-mine activated!";

    private static final String END_MINING_TEXT = ChatColor.RED + "Dirt insta-mine has worn off...";

    private boolean activated;
    private int counter;

    public InstantMineDirt() {
        super("Instant dirt mining", PlayerInteractEvent.class, 10, false);
        activated = false;
        counter = 0;
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getAction() == Action.LEFT_CLICK_BLOCK &&
                event.getClickedBlock() != null &&
                DIRTY_BLOCKS.contains(event.getClickedBlock().getType());
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player p = event.getPlayer();
            if (activated) {
                if (event.getClickedBlock() != null && DIRTY_BLOCKS.contains(event.getClickedBlock().getType())) {
                    event.setCancelled(true);
                    p.playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_CROP_BREAK, 1, 1);
                    boolean success = event.getClickedBlock().breakNaturally();
                    if (!success) {
                        Main.getInstance().getLogger().warning("A worm {" + p.getName() + "} could not break a block!");
                    }
                    return;
                }
            }
            ChatUtils.sendActionbar(p, START_MINING_TEXT);
            p.playSound(p.getLocation(), Sound.BLOCK_ROOTED_DIRT_FALL, 1, 1);
            activated = true;
            event.getClickedBlock().breakNaturally();
            p.playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_CROP_BREAK, 1, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (counter >= 7) {
                        counter = 0;
                        ChatUtils.sendActionbar(p, END_MINING_TEXT);
                        activated = false;
                        cooldown();
                        cancel();
                    }
                    if (counter >= 4) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2);
                    }
                    counter++;
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
        };
    }
}
