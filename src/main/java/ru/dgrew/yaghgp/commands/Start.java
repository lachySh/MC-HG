package ru.dgrew.yaghgp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.PhaseManager;
import ru.dgrew.yaghgp.phases.Lobby;

public class Start implements CommandExecutor {
    ChatManager cm;
    PhaseManager pm;

    public Start() {
        cm = Main.getCm();
        pm = Main.getPm();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hg.start")) sender.sendMessage(cm.getPerm());
        else {
            if (pm.getCurrentPhase() instanceof Lobby) {
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
