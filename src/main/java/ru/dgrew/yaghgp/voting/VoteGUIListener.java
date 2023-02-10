package ru.dgrew.yaghgp.voting;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.gamemap.Gamemap;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.GamemapManager;
import ru.dgrew.yaghgp.managers.PhaseManager;
import ru.dgrew.yaghgp.managers.VotingManager;
import ru.dgrew.yaghgp.phases.Lobby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteGUIListener implements Listener {
    ChatManager cm;
    PhaseManager pm;
    VotingManager vm;
    GamemapManager gm;

    public VoteGUIListener() {
        cm = Main.getCm();
        pm = Main.getPm();
        vm = Main.getVm();
        gm = Main.getGm();
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase(cm.getVoteTitle())) {
            if (!(pm.getCurrentPhase() instanceof Lobby)) {
                player.closeInventory();
                e.setCancelled(true);
                return;
            }

            switch (e.getCurrentItem().getType()) {
                case GRASS_BLOCK:
                    vm.addMapVote(gm.getRandomWorld(), player);
                    sendVoteMessage(player, gm.getRandomWorld().getTitle());
                    player.closeInventory();
                    break;
                case STONE_BRICKS:
                    player.closeInventory();
                    Inventory nextPage = customGamemapSelector(player);
                    player.openInventory(nextPage);
                    break;
            }
            e.setCancelled(true);
        } else if (e.getView().getTitle().equalsIgnoreCase(cm.getVoteCustomMapTitle())) {
            if (!(pm.getCurrentPhase() instanceof Lobby)) {
                player.closeInventory();
                e.setCancelled(true);
                return;
            }

            gm.getGamemapOptions().stream()
                    .filter(g -> g.equalsItemStack(e.getCurrentItem()))
                    .findAny()
                    .ifPresent(
                            (g) ->
                            {
                                vm.addMapVote(g, player);
                                sendVoteMessage(player, e.getCurrentItem().getItemMeta().getDisplayName());
                                player.closeInventory();
                            }
                    );
            e.setCancelled(true);
            return;
        }
    }

    private void sendVoteMessage(Player player, String mapName) {
        player.sendMessage(cm.getPrefix() + cm.getVoteMessage(mapName));
    }

    private Inventory customGamemapSelector(Player player) {
        Inventory gui = Bukkit.createInventory(player, 9, cm.getVoteCustomMapTitle());

        for (Gamemap g : gm.getGamemapOptions()) {
            ItemStack newItem = new ItemStack(g.getDisplayMaterial());
            ItemMeta newItemMeta = newItem.getItemMeta();
            newItemMeta.setDisplayName(ChatColor.GOLD + g.getTitle());
            ArrayList<String> newItemLore = new ArrayList<>();
            newItemLore.addAll(formatLore(new ArrayList<String>(Arrays.asList(g.getDescription().split("\n")))));
            newItemMeta.setLore(newItemLore);
            newItem.setItemMeta(newItemMeta);

            gui.addItem(newItem);
        }

        return gui;
    }

    private List<String> formatLore(List<String> lore) {
        var newList = new ArrayList<String>();
        for (String s : lore) {
            newList.add(ChatColor.GRAY + s);
        }
        return newList;
    }
}