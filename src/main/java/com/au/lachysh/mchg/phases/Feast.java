package com.au.lachysh.mchg.phases;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.managers.ChatManager;
import com.au.lachysh.mchg.managers.GamemapManager;
import com.au.lachysh.mchg.managers.LootManager;
import com.au.lachysh.mchg.managers.PlayerManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Feast extends Phase {
    private int timer;
    private ChatManager cm;
    private LootManager lm;
    private PlayerManager pm;
    private GamemapManager gm;
    private SharedPhaseLogic spl;
    private BukkitTask gameTimer;
    private ClipboardHolder feast;
    private Location feastLocation;

    public Feast(ClipboardHolder feast, Location feastLocation) {
        super();
        this.feast = feast;
        this.feastLocation = feastLocation;
    }

    //region Phase Methods
    @Override
    public void onEnable() {
        timer = 300;
        cm = Main.getCm();
        lm = Main.getLm();
        pm = Main.getPlm();
        gm = Main.getGm();
        spl = Main.getSpl();
        startTimer();

        try { //Pasting Operation
            // We need to adapt our world into a format that worldedit accepts. This looks like this:
            // Ensure it is using com.sk89q... otherwise we'll just be adapting a world into the same world.
            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(gm.getArenaWorld());

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);

            CuboidRegion airSelection = new CuboidRegion(adaptedWorld,
                    BlockVector3.at(feastLocation.getBlockX(), feastLocation.getBlockY()+30, feastLocation.getBlockZ()),
                    BlockVector3.at(feastLocation.getBlockX() + feast.getClipboard().getDimensions().getBlockX() - 1,
                            feastLocation.getBlockY(),
                            feastLocation.getBlockZ() + feast.getClipboard().getDimensions().getBlockZ() - 1)); // make a selection with two points
            BlockState air = BukkitAdapter.adapt(Material.AIR.createBlockData());
            editSession.setBlocks(airSelection, air);

            // Saves our operation and builds the paste - ready to be completed.
            Operation operation = feast.createPaste(editSession)
                    .to(BlockVector3.at(
                            feastLocation.getBlockX(),
                            feastLocation.getBlockY(),
                            feastLocation.getBlockZ()
                    )).ignoreAirBlocks(true).build();

            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                Main.getInstance().getLogger().warning("Warning! Could not generate feast correctly due to WorldEditException");
                e.printStackTrace();
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("Warning! Something went wrong when generating feast");
        }

        Main.getInstance().getLogger().info("Feast phase has started successfully!");

        Bukkit.broadcastMessage(cm.getPrefix() + cm.getFeastTime(spl.formatLocation(feastLocation)));
    }

    @Override
    public void onDisable() {
        gameTimer.cancel();
    }

    @Override
    public Phase next() {
        return new Deathmatch();
    }

    //endregion
    //region Phase Listeners
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        spl.inGameOnJoin(e);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        spl.inGameOnLeave(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        spl.onDeath(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    @EventHandler
    public void onWorldDeath(EntityDamageEvent e) {
        spl.onWorldDeath(e);
        timer = spl.setTimerBasedOnPlayerCount(timer);
    }

    //endregion
    //region Runnables
    void startTimer() {
        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    spl.playTimerAnnouncement(timer, cm.getPrefix() + cm.getDeathmatch(timer));
                    timer--;
                } else {
                    Main.getPm().nextPhase();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

}
