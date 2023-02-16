package ru.dgrew.yaghgp.phases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.managers.ChatManager;
import ru.dgrew.yaghgp.managers.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class SharedPhaseLogic {
    private PlayerManager plm;
    private ChatManager cm;

    public SharedPhaseLogic() {
        this.plm = Main.getPlm();
        this.cm = Main.getCm();
    }

    public void inGameOnJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        e.getPlayer().kickPlayer("Game already started!");
    }

    public void inGameOnLeave(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has left!");
        plm.removeOnDC(e.getPlayer());
    }

    public int setTimerBasedOnPlayerCount(int currentTimer) {
        if (plm.getRemainingTributesList().size() == 1) {
            return 60;
        }
        if (plm.getRemainingTributesList().size() <= 3 && currentTimer >= 60) {
            return 60;
        }
        if (plm.getRemainingTributesList().size() <= 7) {
            return 60;
        }
        return currentTimer;
    }

    public void onDeath(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player killed = (Player) e.getEntity();
            if (killed.getHealth() <= e.getFinalDamage()) {
                e.setCancelled(true);
                onDeath(killed, e.getDamager());
            }
        }
    }

    public void onWorldDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player killed = (Player) e.getEntity();
            if (!e.getCause().toString().startsWith("ENTITY_") && !e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                if (killed.getHealth() <= e.getFinalDamage()) {
                    e.setCancelled(true);
                    onDeath(killed, null);
                }
            }
        } else if (e.getEntity() instanceof ItemFrame) e.setCancelled(true);
    }

    private void onDeath(Player killed, Entity killerent) {
        Player killer;
        if (killerent instanceof Player) {
            killer = (Player) killerent;
            killer.sendMessage(cm.getPrefix() + cm.getKill().replace("{player}", killed.getName()));
            killed.sendMessage(cm.getPrefix() + cm.getKilled().replace("{player}", killer.getName()));
        } else if (killerent instanceof Projectile) {
            Projectile pj = (Projectile) killerent;
            killer = (Player) pj.getShooter();
            killer.sendMessage(cm.getPrefix() + cm.getKill().replace("{player}", killed.getName()));
            killed.sendMessage(cm.getPrefix() + cm.getKilled().replace("{player}", killer.getName()));
        } else killed.sendMessage(cm.getPrefix() + cm.getKillednat());
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack i : killed.getInventory()) items.add(i);
        killed.getInventory().clear();
        for (ItemStack i : items) if (i != null) killed.getWorld().dropItem(killed.getLocation(), i).setPickupDelay(20);
        killed.setHealth(20);
        killed.getWorld().strikeLightningEffect(killed.getLocation());
        plm.transferToSpectators(killed);
        Bukkit.broadcastMessage(cm.getGlobalkill().replace("{players}", String.valueOf(plm.getRemainingTributesList().size())));
    }

}
