package com.au.lachysh.mchg.gui;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.*;
import com.au.lachysh.mchg.phases.Lobby;
import com.au.lachysh.mchg.phases.PreGame;
import com.au.lachysh.mchg.tribute.Tribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.au.lachysh.mchg.kits.Kit;

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