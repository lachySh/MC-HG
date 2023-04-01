package com.au.lachysh.mchg.abilities.spy;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.AbilityCallable;
import com.au.lachysh.mchg.kits.Spy;
import com.au.lachysh.mchg.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.AbstractMap;
import java.util.Comparator;

import static com.au.lachysh.mchg.shared.ChatUtils.sendActionbar;

public class SpyCompassTrack extends Ability<PlayerInteractEvent> {
    private static Integer RANGE;
    private static final String NO_NEAREST_PLAYER_TEXT = ChatColor.RED + "No player found within {range} blocks!";
    private static final String NEAREST_PLAYER_TEXT = ChatColor.YELLOW + "{name} is {dist} blocks away at Y = {yval}";

    private static final String FEAST_TEXT = ChatColor.YELLOW + "Compass pointing to the feast!";
    private static PlayerManager pm;

    private static boolean feastEnabled = false;
    private static Location feastLocation;

    public SpyCompassTrack() {
        super("Spy compass tracking", PlayerInteractEvent.class, 1, false);
        if (pm == null) pm = Main.getPlm();
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.COMPASS &&
                Spy.itemName.contains(event.getItem().getItemMeta().getDisplayName());
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player player = event.getPlayer();
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (RANGE == null) {
                    RANGE = Main.getGm().getArenaGamemap().getBorderRadius() * 2;
                }
                player = event.getPlayer();
                AbstractMap.SimpleEntry<Player, Double> nearestPlayer = getNearestTributePlayer(RANGE, player);
                if (nearestPlayer == null) {
                    sendActionbar(player, formatFailText(RANGE));
                } else {
                    sendActionbar(player, formatText(nearestPlayer.getKey().getName(), Math.sqrt(nearestPlayer.getValue()), nearestPlayer.getKey().getLocation().getBlockY()));
                    player.setCompassTarget(nearestPlayer.getKey().getLocation());
                }
                player.updateInventory();
            } else if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && feastEnabled) {
                sendActionbar(player, FEAST_TEXT);
                player.setCompassTarget(feastLocation);
                player.updateInventory();
            }
            cooldown();
        };
    }

    private static AbstractMap.SimpleEntry<Player, Double> getNearestTributePlayer(int range, Player player) {
        int radius = range * range;
        return player.getWorld().getPlayers().stream()
                .filter(p -> !p.equals(player) && pm.findTribute(p).isPresent() && !(p.getLocation().distance(player.getLocation()) < 10))
                .min(Comparator.comparingDouble((p) -> p.getLocation().distanceSquared(player.getLocation())))
                .filter(p -> p.getLocation().distanceSquared(player.getLocation()) < radius)
                .map(p -> new AbstractMap.SimpleEntry<>(p, p.getLocation().distanceSquared(player.getLocation())))
                .orElse(null);
    }

    private static String formatText(String playerName, Double distance, Integer yVal) {
        Integer intDist = distance.intValue();
        return NEAREST_PLAYER_TEXT.replace("{name}", playerName)
                .replace("{dist}", intDist.toString())
                .replace("{yval}", yVal.toString());
    }

    private static String formatFailText(Integer range) {
        return NO_NEAREST_PLAYER_TEXT.replace("{range}", range.toString());
    }

    public static void setFeastEnabled(boolean enabled) {
        feastEnabled = enabled;
    }

    public static void setFeastLocation(Location location) {
        feastLocation = location;
    }
}