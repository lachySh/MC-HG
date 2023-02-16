package ru.dgrew.yaghgp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.GamemapManager;
import ru.dgrew.yaghgp.managers.PhaseManager;
import ru.dgrew.yaghgp.phases.Lobby;

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
