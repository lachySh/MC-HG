package ru.dgrew.yaghgp.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.kits.Kit;
import ru.dgrew.yaghgp.managers.*;
import ru.dgrew.yaghgp.phases.Lobby;
import ru.dgrew.yaghgp.phases.PreGame;
import ru.dgrew.yaghgp.tribute.Tribute;

import java.util.Optional;

public class KitsGUIListener implements Listener {
    ChatManager cm;
    PhaseManager pm;
    PlayerManager plm;
    KitManager km;
    GamemapManager gm;

    public KitsGUIListener() {
        cm = Main.getCm();
        pm = Main.getPm();
        plm = Main.getPlm();
        km = Main.getKm();
        gm = Main.getGm();
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase(cm.getKitsMenuTitle())) {
            if (!(pm.getCurrentPhase() instanceof Lobby || pm.getCurrentPhase() instanceof PreGame)) {
                player.closeInventory();
                e.setCancelled(true);
                return;
            }

            for (Kit k : km.getAllKits()) {
                if (k.getKitDisplayItem().isSimilar(e.getCurrentItem())) {
                    Optional<Tribute> tribute = plm.findTribute(player);
                    tribute.ifPresent((t) -> t.setKit(k));
                    player.sendMessage(cm.getPrefix() + cm.getKitsSelection(k.getKitName()));
                    player.closeInventory();
                    e.setCancelled(true);
                    return;
                }
            }
            e.setCancelled(true);
        }
    }
}