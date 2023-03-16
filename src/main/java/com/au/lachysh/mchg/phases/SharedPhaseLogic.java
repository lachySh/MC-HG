package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.PlayerManager;
import com.au.lachysh.mchg.structure.FeastStructure;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SharedPhaseLogic {
    private PlayerManager plm;
    private ChatManager cm;
    private GamemapManager gm;

    public SharedPhaseLogic() {
        this.plm = Main.getPlm();
        this.cm = Main.getCm();
        this.gm = Main.getGm();
    }

    public void inGameOnJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        e.getPlayer().kickPlayer("Game already started!");
    }

    public void inGameOnLeave(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has left!");
        plm.removeOnDC(e.getPlayer());
    }

    // TODO: Fix these when not testing
    public int setTimerBasedOnPlayerCount(int currentTimer) {
        if (plm.getRemainingTributesList().size() == 1) {
            return 120; // Should be 0
        }
        if (plm.getRemainingTributesList().size() <= 3 && currentTimer >= 120) {
            return 120;
        }
        if (plm.getRemainingTributesList().size() <= 7 && currentTimer >= 300) {
            return 300;
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

    public void playTimerAnnouncement(int timer, String message) {
        if (timer >= 600) {
            if (timer % 300 == 0) {
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        if (timer >= 300) {
            if (timer % 300 == 0) {
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        if (timer >= 60) {
            if (timer % 60 == 0) {
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        if (timer >= 30) {
            if (timer % 30 == 0) {
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        if (timer >= 15) {
            if (timer % 15 == 0) {
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        if (timer >= 10) {
            if (timer % 10 == 0) {
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        if (timer >= 5) {
            if (timer % 5 == 0) {
                for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                Bukkit.broadcastMessage(message);
            }
            return;
        }
        for(Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        Bukkit.broadcastMessage(message);
    }

    public Location calculateFeastLocation(FeastStructure feast) {
        Random rand = new Random();
        World arena = gm.getArenaWorld();
        int radius = gm.getArenaGamemap().getBorderRadius();
        Location centre = gm.getArenaCentre();

        BlockVector3 dimensions = feast.getClipboard().getDimensions();

        int x = rand.nextInt(centre.getBlockX()-radius+dimensions.getBlockX(), centre.getBlockX()+radius-dimensions.getBlockX());
        int z = rand.nextInt(centre.getBlockZ()-radius+dimensions.getBlockZ(), centre.getBlockZ()+radius-dimensions.getBlockZ());

        return new Location(arena, x, arena.getHighestBlockYAt(x, z), z);
    }

    public String formatLocation(Location feastLocation) {
        return "x: " + feastLocation.getBlockX() + ", y: " + feastLocation.getBlockY() + ", z: " + feastLocation.getBlockZ();
    }
}
