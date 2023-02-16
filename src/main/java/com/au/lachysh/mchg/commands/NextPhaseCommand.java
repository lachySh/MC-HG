package com.au.lachysh.mchg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.PhaseManager;

public class NextPhaseCommand implements CommandExecutor {
    ChatManager cm;
    PhaseManager pm;
    GamemapManager gm;

    public NextPhaseCommand() {
        cm = Main.getCm();
        pm = Main.getPm();
        gm = Main.getGm();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hg.nextphase")) sender.sendMessage(cm.getPerm());
        else {
            if (gm.isMapLoaded()) {
                pm.nextPhase();
                Bukkit.getLogger().info("Next phase forced!");
            } else {
                Bukkit.getLogger().info("Tried to go next phase, but the arena has not yet loaded!");
            }
            return true;
        }
        return false;
    }
}