package com.au.lachysh.mchg.commands;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.PhaseManager;
import com.au.lachysh.mchg.phases.Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ItemToYamlCommand implements CommandExecutor {
    private ChatManager cm;

    public ItemToYamlCommand() {
        cm = Main.getCm();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hg.itemtoyaml")) sender.sendMessage(cm.getPerm());
        if (!(sender instanceof Player)) return false;
        else {
            var items = new YamlConfiguration();
            var file = new File(Main.getInstance().getDataFolder() + "/item.yml");

            try {
                file.createNewFile();
                items.load(file);
                items.set(UUID.randomUUID().toString(), ((Player) sender).getItemInHand());
                items.save(file);
            } catch (Exception e) {
                sender.sendMessage(cm.getPrefix() + "Something went wrong! See server logs");
            }

            sender.sendMessage(cm.getPrefix() + "Item in hand has been saved to yaml file -> plugins/MC-HG/item.yml");
            return true;
        }
    }
}