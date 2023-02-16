package com.au.lachysh.mchg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.PhaseManager;
import com.au.lachysh.mchg.phases.Lobby;

public class StartCommand implements CommandExecutor {
    ChatManager cm;
    GamemapManager gm;
    PhaseManager pm;

    public StartCommand() {
        cm = Main.getCm();
        pm = Main.getPm();
        gm = Main.getGm();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hg.start")) sender.sendMessage(cm.getPerm());
        else {
            if (pm.getCurrentPhase() instanceof Lobby) {
                gm.loadGamemap(gm.getRandomWorld());
                Main.getPm().nextPhase();
                sender.sendMessage(cm.getPrefix() + cm.getStarted());
            } else {
                sender.sendMessage(cm.getPrefix() + cm.getAlreadyStarted());
            }
            return true;
        }
        return false;
    }
}
