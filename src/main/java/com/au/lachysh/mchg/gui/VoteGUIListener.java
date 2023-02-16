package com.au.lachysh.mchg.gui;

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
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.commands.VoteGUICommand;
import com.au.lachysh.mchg.gamemap.Gamemap;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.PhaseManager;
import com.au.lachysh.mchg.managers.VotingManager;
import com.au.lachysh.mchg.phases.Lobby;

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

            if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE
                    && e.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) {
                player.closeInventory();
                VoteGUICommand.openMapVoteMainMenu(player);
            } else {
                gm.getCustomGamemapOptions().stream()
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
            }
            e.setCancelled(true);
            return;
        }
    }

    private void sendVoteMessage(Player player, String mapName) {
        player.sendMessage(cm.getPrefix() + cm.getVoteMessage(mapName));
    }

    private Inventory customGamemapSelector(Player player) {
        Inventory gui = Bukkit.createInventory(player,
                (gm.getCustomGamemapOptions().size()+1) + (9 - (gm.getCustomGamemapOptions().size()+1) % 9),
                cm.getVoteCustomMapTitle());

        for (Gamemap g : gm.getCustomGamemapOptions()) {
            ItemStack newItem = new ItemStack(g.getDisplayMaterial());
            ItemMeta newItemMeta = newItem.getItemMeta();
            newItemMeta.setDisplayName(g.getTitle());
            ArrayList<String> newItemLore = new ArrayList<>();
            newItemLore.addAll(formatLore(new ArrayList<String>(Arrays.asList(g.getDescription().split("\n")))));
            newItemMeta.setLore(newItemLore);
            newItem.setItemMeta(newItemMeta);

            gui.addItem(newItem);
        }

        ItemStack backButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta newItemMeta = backButton.getItemMeta();
        newItemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Back");
        backButton.setItemMeta(newItemMeta);

        gui.setItem(gui.getSize()-1, backButton);

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