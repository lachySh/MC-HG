package com.au.lachysh.mchg.managers;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.loot.LootEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LootManager implements Listener {

    private Random rand;
    private GamemapManager gm;
    private Map<LootEntry, Integer> lootWeightings;
    private HashMap<Block, Boolean> playerPlacedChests;
    private List<Location> openedChests;
    private List<Location> refillOpenedChests;

    private Boolean lootChestsEnabled;
    private Boolean refillLootChestsEnabled;

    public LootManager() {
        rand = new Random();
        gm = Main.getGm();
        playerPlacedChests = new HashMap<>();
        openedChests = new ArrayList<>();
        refillOpenedChests = new ArrayList<>();
        lootChestsEnabled = false;
        refillLootChestsEnabled = false;
    }

    public void enableLootChestListener() {
        if (lootWeightings == null) {
            lootWeightings = Main.getGm().getArenaGamemap().getLootTable();
            Main.getInstance().getLogger().info("Loot table loaded: " + lootWeightings);
        }
        lootChestsEnabled = true;
        refillLootChestsEnabled = false;
    }

    public void enableRefillLootChestListener() {
        lootChestsEnabled = false;
        refillLootChestsEnabled = true;
    }

    @EventHandler
    public void doNotFillPlayerPlacedChests(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getState() instanceof Chest) {
            playerPlacedChests.put(event.getBlockPlaced(), true);
        }
    }

    @EventHandler
    public void lootChestListener(PlayerInteractEvent event) {
        if (!lootChestsEnabled) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getClickedBlock().getType() == Material.CHEST) {
            if (playerPlacedChests.getOrDefault(event.getClickedBlock(), false)) return;

            Chest chest = (Chest) event.getClickedBlock().getState();

            if (openedChests.contains(chest.getLocation())) return;

            Inventory inv = chest.getInventory();

            // Clear the loot chest before filling it with predefined loot table
            if (gm.getArenaGamemap().getClearLootOnStart()) {
                inv.clear();
            }

            Integer numberOfItemStacks = rand.nextInt(gm.getArenaGamemap().getMinSlotsFilled(), gm.getArenaGamemap().getMaxSlotsFilled()+1);
            List<ItemStack> items = new ArrayList<>();
            if (chest.getCustomName() != null && chest.equals("Rare Chest")) {
                for (int i = 0; i < numberOfItemStacks; i++) {
                    items.add(nextRareItem());
                }
            } else {
                for (int i = 0; i < numberOfItemStacks; i++) {
                    items.add(nextItem());
                }
            }

            for (ItemStack i : items) {
                inv.addItem(i);
            }

            shuffleChest(inv);

            openedChests.add(chest.getLocation());
        }
    }

    @EventHandler
    public void refillLootChestListener(PlayerInteractEvent event) {
        if (!refillLootChestsEnabled) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getClickedBlock().getType() == Material.CHEST) {
            if (playerPlacedChests.getOrDefault(event.getClickedBlock(), false)) return;

            Chest chest = (Chest) event.getClickedBlock().getState();

            if (refillOpenedChests.contains(chest.getLocation())) return;

            Inventory inv = chest.getInventory();

            Integer numberOfItemStacks = rand.nextInt(gm.getArenaGamemap().getMinSlotsFilled(), gm.getArenaGamemap().getMaxSlotsFilled()+1);
            List<ItemStack> items = new ArrayList<>();
            if (chest.getCustomName() != null && chest.equals("Rare Chest")) {
                for (int i = 0; i < numberOfItemStacks; i++) {
                    items.add(nextRefillRareItem());
                }
            } else {
                for (int i = 0; i < numberOfItemStacks; i++) {
                    items.add(nextRefillItem());
                }
            }

            for (ItemStack i : items) {
                inv.addItem(i);
            }

            shuffleChest(inv);

            refillOpenedChests.add(chest.getLocation());
        }
    }

    private Integer sumOfWeights(Map<LootEntry, Integer> map) {
        return map.values().stream().reduce(0, Integer::sum);
    }

    private Double meanOfWeights(Map<LootEntry, Integer> map) {
        return map.values().stream().mapToDouble(Integer::doubleValue).average().orElse(0);
    }

    private TreeMap<Integer, LootEntry> weightingsToCumulative(Map<LootEntry, Integer> map, double rarityMultiplier) {
        Double meanOfWeights = meanOfWeights(map);
        int cum = 0;
        TreeMap<Integer, LootEntry> cumMap = new TreeMap<>();

        for (Map.Entry<LootEntry, Integer> i : map.entrySet()) {
            cum += i.getValue() + (int) (meanOfWeights * (rarityMultiplier - 1));
            cumMap.put(cum, i.getKey());
        }

        return cumMap;
    }

    private ItemStack nextItem() {
        TreeMap<Integer, LootEntry> cumMap = weightingsToCumulative(lootWeightings, 1);
        var randInt = rand.nextInt(sumOfWeights(lootWeightings)+1);
        LootEntry lootEntry = cumMap.ceilingEntry(randInt).getValue();
        return new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax()+1));
    }

    private ItemStack nextRareItem() {
        TreeMap<Integer, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getRareLootMultiplier());
        var randInt = rand.nextInt(sumOfWeights(lootWeightings)+1);
        LootEntry lootEntry = cumMap.ceilingEntry(randInt).getValue();
        return new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax()+1));
    }

    private ItemStack nextRefillItem() {
        TreeMap<Integer, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getRefillLootMultiplier());
        var randInt = rand.nextInt(sumOfWeights(lootWeightings)+1);
        LootEntry lootEntry = cumMap.ceilingEntry(randInt).getValue();
        return new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax()+1));
    }

    private ItemStack nextRefillRareItem() {
        TreeMap<Integer, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getRefillRareLootMultiplier());
        var randInt = rand.nextInt(sumOfWeights(lootWeightings)+1);
        LootEntry lootEntry = cumMap.ceilingEntry(randInt).getValue();
        return new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax()+1));
    }

    private void shuffleChest(Inventory inv) {
        var items = inv.getContents();
        inv.clear();
        for (ItemStack i : items) {
            do {
                var index = rand.nextInt(inv.getSize());

                if (inv.getItem(index) == null) {
                    inv.setItem(index, i);
                    break;
                }
            } while (inv.firstEmpty() != -1); // avoid infinite loop
        }
    }
}

