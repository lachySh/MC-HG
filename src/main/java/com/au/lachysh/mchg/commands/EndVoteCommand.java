package com.au.lachysh.mchg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.PhaseManager;
import com.au.lachysh.mchg.phases.Lobby;

public class EndVoteCommand implements CommandExecutor {
    ChatManager cm;
    PhaseManager pm;

    private static boolean endVoteCalled = false;

    public EndVoteCommand() {
        cm = Main.getCm();
        pm = Main.getPm();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hg.endvote")) sender.sendMessage(cm.getPerm());
        else {
            if (pm.getCurrentPhase() instanceof Lobby && !endVoteCalled) {
                ((Lobby) pm.getCurrentPhase()).startVoteCountdown();
                Main.getInstance().getLogger().info("Vote countdown has begun!");
                endVoteCalled = true;
            } else {
                sender.sendMessage(cm.getPrefix() + cm.getAlreadyStarted());
            }
            return true;
        }
        return false;
    }
}