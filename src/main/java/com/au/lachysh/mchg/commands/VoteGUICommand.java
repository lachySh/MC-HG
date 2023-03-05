package com.au.lachysh.mchg.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.PhaseManager;
import com.au.lachysh.mchg.managers.VotingManager;
import com.au.lachysh.mchg.phases.Lobby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteGUICommand implements CommandExecutor {
    private static ChatManager cm;
    private static PhaseManager pm;
    private static GamemapManager gm;
    private static VotingManager vm;

    public VoteGUICommand() {
        cm = Main.getCm();
        pm = Main.getPm();
        gm = Main.getGm();
        vm = Main.getVm();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (pm.getCurrentPhase() instanceof Lobby) {
                if (vm.canPlayerVoteMap((Player) sender)) {
                    Player player = (Player) sender;
                    openMapVoteMainMenu(player);
                } else {
                    sender.sendMessage(cm.getPrefix() + ChatColor.WHITE + "You have already voted!");
                }
            } else {
                sender.sendMessage(cm.getPrefix() + cm.getAlreadyStarted());
            }
        }
        return true;
    }

    public static void openMapVoteMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(player, 9, cm.getVoteTitle());
        ItemStack[] menuItems;

        ItemStack customMap = new ItemStack(Material.STONE_BRICKS);
        ItemMeta customMapMeta = customMap.getItemMeta();
        customMapMeta.setDisplayName(cm.getVoteCustom());
        ArrayList<String> customMapLore = new ArrayList<>();
        customMapLore.addAll(formatLore(new ArrayList<>(Arrays.asList(cm.getVoteCustomSubtitle().split("\n")))));
        customMapMeta.setLore(customMapLore);
        customMap.setItemMeta(customMapMeta);

        if (gm.getRandomWorld() == null) {
            menuItems = new ItemStack[]{customMap};
        } else {
            ItemStack randomMap = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta randomMapMeta = randomMap.getItemMeta();
            randomMapMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
            randomMapMeta.setDisplayName(gm.getRandomWorld().getTitle());
            randomMapMeta.setLore(gm.getRandomWorld().getFormattedLore());
            randomMap.setItemMeta(randomMapMeta);

            menuItems = new ItemStack[]{randomMap, customMap};
        }
        //Put the items in the inventory
        gui.setContents(menuItems);
        player.openInventory(gui);
    }

    private static List<String> formatLore(List<String> lore) {
        var newList = new ArrayList<String>();
        for (String s : lore) {
            newList.add(ChatColor.GRAY + s);
        }
        return newList;
    }
}
