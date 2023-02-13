package ru.dgrew.yaghgp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.GamemapManager;
import ru.dgrew.yaghgp.managers.PhaseManager;
import ru.dgrew.yaghgp.managers.VotingManager;
import ru.dgrew.yaghgp.phases.Lobby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteGUICommand implements CommandExecutor {
    ChatManager cm;
    PhaseManager pm;
    GamemapManager gm;
    VotingManager vm;

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
                    Inventory gui = Bukkit.createInventory(player, 9, cm.getVoteTitle());

                    ItemStack randomMap = new ItemStack(Material.GRASS_BLOCK);
                    ItemStack customMap = new ItemStack(Material.STONE_BRICKS);

                    ItemMeta randomMapMeta = randomMap.getItemMeta();
                    randomMapMeta.setDisplayName(gm.getRandomWorld().getTitle());
                    ArrayList<String> randomMapLore = new ArrayList<>();
                    randomMapLore.addAll(formatLore(new ArrayList<>(Arrays.asList(gm.getRandomWorld().getDescription().split("\n")))));
                    randomMapMeta.setLore(randomMapLore);
                    randomMap.setItemMeta(randomMapMeta);

                    ItemMeta customMapMeta = customMap.getItemMeta();
                    customMapMeta.setDisplayName(cm.getVoteCustom());
                    ArrayList<String> customMapLore = new ArrayList<>();
                    customMapLore.addAll(formatLore(new ArrayList<>(Arrays.asList(cm.getVoteCustomSubtitle().split("\n")))));
                    customMapMeta.setLore(customMapLore);
                    customMap.setItemMeta(customMapMeta);

                    //Put the items in the inventory
                    ItemStack[] menuItems = {randomMap, customMap};
                    gui.setContents(menuItems);
                    player.openInventory(gui);
                } else {
                    sender.sendMessage(cm.getPrefix() + ChatColor.WHITE + "You have already voted!");
                }
            } else {
                sender.sendMessage(cm.getPrefix() + cm.getAlreadyStarted());
            }
        }
        return true;
    }

    private List<String> formatLore(List<String> lore) {
        var newList = new ArrayList<String>();
        for (String s : lore) {
            newList.add(ChatColor.GRAY + s);
        }
        return newList;
    }
}
