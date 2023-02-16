package ru.dgrew.yaghgp.abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.PlayerManager;

import java.util.AbstractMap;
import java.util.Comparator;

public class CompassTrack extends Ability<PlayerInteractEvent> {

    private static final int RANGE = Main.getSm().getCompassTrackRange();
    private static final String NO_NEAREST_PLAYER_TEXT = ChatColor.RED + "No player found within " + RANGE + " blocks!";
    private static final String NEAREST_PLAYER_TEXT = ChatColor.YELLOW + "Nearest player {name} is {dist} blocks away!";
    private static PlayerManager pm;
    private static ItemStack trackingCompassItem;

    public CompassTrack() {
        super("Compass tracking", PlayerInteractEvent.class, 1, false);
        if (pm == null) pm = Main.getPlm();
    }

    @Override
    public boolean precondition(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() == Material.COMPASS &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    public AbilityCallable<PlayerInteractEvent> getCallable() {
        return event -> {
            Player player = event.getPlayer();
            AbstractMap.SimpleEntry<Player, Double> nearestPlayer = getNearestTributePlayer(RANGE, player);
            if (nearestPlayer == null) {
                sendActionbar(player, NO_NEAREST_PLAYER_TEXT);
            } else {
                sendActionbar(player, formatDistanceText(nearestPlayer.getKey().getName(), Math.sqrt(nearestPlayer.getValue())));
                player.setCompassTarget(nearestPlayer.getKey().getLocation());
            }
            player.updateInventory();
            cooldown();
        };
    }

    public static ItemStack getTrackingCompass() {
        if (trackingCompassItem != null) return trackingCompassItem;

        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Tracking Compass");
        item.setItemMeta(itemMeta);

        trackingCompassItem = item;
        return trackingCompassItem;
    }

    private static AbstractMap.SimpleEntry<Player, Double> getNearestTributePlayer(int range, Player player) {
        int radius = range * range;
        return player.getWorld().getPlayers().stream()
                .filter(p -> !p.equals(player) && pm.findTribute(p).isPresent())
                .min(Comparator.comparingDouble((p) -> p.getLocation().distanceSquared(player.getLocation())))
                .filter(p -> p.getLocation().distanceSquared(player.getLocation()) < radius)
                .map(p -> new AbstractMap.SimpleEntry<>(p, p.getLocation().distanceSquared(player.getLocation())))
                .orElse(null);
    }

    private static String formatDistanceText(String playerName, Double distance) {
        Integer intDist = distance.intValue();
        return NEAREST_PLAYER_TEXT.replace("{name}", playerName).replace("{dist}", intDist.toString());
    }

    private static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
