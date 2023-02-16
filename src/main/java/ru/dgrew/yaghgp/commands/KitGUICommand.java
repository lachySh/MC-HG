package ru.dgrew.yaghgp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.kits.Kit;
import ru.dgrew.yaghgp.managers.*;
import ru.dgrew.yaghgp.phases.Lobby;
import ru.dgrew.yaghgp.phases.PreGame;

public class KitGUICommand implements CommandExecutor {
    private static ChatManager cm;
    private static PhaseManager pm;
    private static KitManager km;

    public KitGUICommand() {
        cm = Main.getCm();
        pm = Main.getPm();
        km = Main.getKm();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (pm.getCurrentPhase() instanceof Lobby || pm.getCurrentPhase() instanceof PreGame) {
                Player player = (Player) sender;
                openKitsMenu(player);
            } else {
                sender.sendMessage(cm.getPrefix() + cm.getKitsTooLate());
            }
        }
        return true;
    }

    private static void openKitsMenu(Player player) {
        Inventory gui = Bukkit.createInventory(player,
                km.getAllKits().size() + (9 - km.getAllKits().size() % 9),
                cm.getKitsMenuTitle());

        for (Kit k : km.getAllKits()) {
            gui.addItem(k.getKitDisplayItem());
        }

        player.openInventory(gui);
    }
}
