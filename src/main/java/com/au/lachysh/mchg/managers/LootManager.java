package com.au.lachysh.mchg.managers;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.loot.EnchantmentEntry;
import com.au.lachysh.mchg.loot.LootEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
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
    private List<Location> clearedChests;
    private List<Location> feastOpenedChests;

    private Boolean lootChestsEnabled;
    private Boolean refillLootChestsEnabled;
    private Boolean feastLootChestsEnabled;

    public LootManager() {
        rand = new Random();
        gm = Main.getGm();
        playerPlacedChests = new HashMap<>();
        openedChests = new ArrayList<>();
        refillOpenedChests = new ArrayList<>();
        feastOpenedChests = new ArrayList<>();
        clearedChests = new ArrayList<>();
        lootChestsEnabled = false;
        refillLootChestsEnabled = false;
        feastLootChestsEnabled = false;
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
        Main.getInstance().getLogger().info("Enabling refill loot chests...");
        lootChestsEnabled = false;
        refillLootChestsEnabled = true;
    }

    public void enableFeastLootChestListener() {
        if (lootWeightings == null) {
            lootWeightings = Main.getGm().getArenaGamemap().getLootTable();
            Main.getInstance().getLogger().info("Loot table loaded: " + lootWeightings);
        }
        Main.getInstance().getLogger().info("Enabling feast loot chests...");
        feastLootChestsEnabled = true;
    }

    @EventHandler
    public void doNotFillPlayerPlacedChests(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getState() instanceof Chest) {
            playerPlacedChests.put(event.getBlockPlaced(), true);
        }
    }

    @EventHandler
    public void lootChestListener(PlayerInteractEvent event) {
        if (!lootChestsEnabled || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block.getType() != Material.CHEST || playerPlacedChests.getOrDefault(block, false)) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Location chestLocation = chest.getLocation();

        if (openedChests.contains(chestLocation) ||
                (chest.getCustomName() != null && (chest.getCustomName().equalsIgnoreCase("custom chest")
                        || chest.getCustomName().equalsIgnoreCase("feast chest")))) {
            return;
        }

        Inventory inv = chest.getInventory();
        if (inv instanceof DoubleChestInventory) {
            DoubleChest doubleChest = (DoubleChest) inv.getHolder();
            inv = doubleChest.getInventory();
        }

        if (gm.getArenaGamemap().getClearLootOnStart()) {
            inv.clear();
            clearedChests.add(chestLocation);
        }

        int minSlotsFilled = gm.getArenaGamemap().getMinSlotsFilled();
        int maxSlotsFilled = gm.getArenaGamemap().getMaxSlotsFilled();
        int numberOfItemStacks = inv instanceof DoubleChestInventory ?
                rand.nextInt(minSlotsFilled * 2, (maxSlotsFilled * 2) + 1) :
                rand.nextInt(minSlotsFilled, maxSlotsFilled + 1);

        List<ItemStack> items = new ArrayList<>();
        if (chest.getCustomName() != null && chest.getCustomName().toLowerCase().equals("rare chest")) {
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

        if (inv instanceof DoubleChestInventory) {
            List<Location> possibleLocations = List.of(chest.getLocation().add(-1, 0, 0),
                    chest.getLocation().add(+1, 0, 0),
                    chest.getLocation().add(0, 0, -1),
                    chest.getLocation().add(0, 0, +1));

            possibleLocations.stream()
                    .filter((loc) -> loc.getBlock().getType() == Material.CHEST)
                    .findFirst()
                    .ifPresent((loc) -> openedChests.add(loc));
        }

    }

    @EventHandler
    public void refillLootChestListener(PlayerInteractEvent event) {
        if (!refillLootChestsEnabled || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block.getType() != Material.CHEST || playerPlacedChests.getOrDefault(block, false)) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Location chestLocation = chest.getLocation();

        if (refillOpenedChests.contains(chestLocation) ||
                (chest.getCustomName() != null && (chest.getCustomName().equalsIgnoreCase("custom chest")
                        || chest.getCustomName().equalsIgnoreCase("feast chest")))) {
            return;
        }

        Inventory inv = chest.getInventory();
        if (inv instanceof DoubleChestInventory) {
            DoubleChest doubleChest = (DoubleChest) inv.getHolder();
            inv = doubleChest.getInventory();
        }

        if (gm.getArenaGamemap().getClearLootOnStart() && !clearedChests.contains(chestLocation)) {
            inv.clear();
            clearedChests.add(chestLocation);
        }

        int minSlotsFilled = gm.getArenaGamemap().getMinSlotsFilled();
        int maxSlotsFilled = gm.getArenaGamemap().getMaxSlotsFilled();
        int numberOfItemStacks = inv instanceof DoubleChestInventory ?
                rand.nextInt(minSlotsFilled * 2, (maxSlotsFilled * 2) + 1) :
                rand.nextInt(minSlotsFilled, maxSlotsFilled + 1);

        List<ItemStack> items = new ArrayList<>();
        if (chest.getCustomName() != null && chest.getCustomName().toLowerCase().equals("rare chest")) {
            // If chest wasn't opened yet, give it the initial loot
            if (!openedChests.contains(chest.getLocation())) {
                for (int i = 0; i < numberOfItemStacks; i++) {
                    items.add(nextRareItem());
                }
                numberOfItemStacks = inv instanceof DoubleChestInventory ?
                        rand.nextInt(minSlotsFilled * 2, (maxSlotsFilled * 2) + 1) :
                        rand.nextInt(minSlotsFilled, maxSlotsFilled + 1);
            }
            for (int i = 0; i < numberOfItemStacks; i++) {
                items.add(nextRefillRareItem());
            }
        } else {
            if (!openedChests.contains(chest.getLocation())) {
                for (int i = 0; i < numberOfItemStacks; i++) {
                    items.add(nextItem());
                }
                numberOfItemStacks = inv instanceof DoubleChestInventory ?
                        rand.nextInt(minSlotsFilled * 2, (maxSlotsFilled * 2) + 1) :
                        rand.nextInt(minSlotsFilled, maxSlotsFilled + 1);
            }
            for (int i = 0; i < numberOfItemStacks; i++) {
                items.add(nextRefillItem());
            }
        }

        for (ItemStack i : items) {
            inv.addItem(i);
        }

        shuffleChest(inv);
        refillOpenedChests.add(chestLocation);

        if (inv instanceof DoubleChestInventory) {
            List<Location> possibleLocations = List.of(chestLocation.add(-1, 0, 0),
                    chestLocation.add(1, 0, 0),
                    chestLocation.add(0, 0, -1),
                    chestLocation.add(0, 0, 1));

            possibleLocations.stream()
                    .filter(loc -> loc.getBlock().getType() == Material.CHEST)
                    .findFirst()
                    .ifPresent(loc -> refillOpenedChests.add(loc));
        }
    }

    @EventHandler
    public void feastLootChestListener(PlayerInteractEvent event) {
        if (!feastLootChestsEnabled || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block.getType() != Material.CHEST || playerPlacedChests.getOrDefault(block, false)) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Location chestLocation = chest.getLocation();

        if (feastOpenedChests.contains(chestLocation) ||
                (chest.getCustomName() != null && chest.getCustomName().equalsIgnoreCase("custom chest"))) {
            return;
        }

        if (chest.getCustomName() != null && chest.getCustomName().toLowerCase().equals("feast chest")) {
            Inventory inv = chest.getInventory();
            if (inv instanceof DoubleChestInventory) {
                DoubleChest doubleChest = (DoubleChest) inv.getHolder();
                inv = doubleChest.getInventory();
            }

            int minSlotsFilled = gm.getArenaGamemap().getMinSlotsFilled();
            int maxSlotsFilled = gm.getArenaGamemap().getMaxSlotsFilled();
            int numberOfItemStacks = inv instanceof DoubleChestInventory ?
                    rand.nextInt(minSlotsFilled * 2, (maxSlotsFilled * 2) + 1) :
                    rand.nextInt(minSlotsFilled, maxSlotsFilled + 1);

            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < numberOfItemStacks; i++) {
                items.add(nextFeastItem());
            }

            for (ItemStack i : items) {
                inv.addItem(i);
            }

            shuffleChest(inv);

            feastOpenedChests.add(chest.getLocation());

            if (inv instanceof DoubleChestInventory) {
                List<Location> possibleLocations = List.of(chest.getLocation().add(-1, 0, 0),
                        chest.getLocation().add(+1, 0, 0),
                        chest.getLocation().add(0, 0, -1),
                        chest.getLocation().add(0, 0, +1));

                possibleLocations.stream()
                        .filter((loc) -> loc.getBlock().getType() == Material.CHEST)
                        .findFirst()
                        .ifPresent((loc) -> feastOpenedChests.add(loc));
            }
        }
    }

    private Integer sumOfWeights(Map<LootEntry, Integer> map) {
        return map.values().stream().reduce(0, Integer::sum);
    }

    private Integer minOfWeights(Map<LootEntry, Integer> map) {
        return map.values().stream().reduce(0, Integer::min);
    }

    private Integer maxOfWeights(Map<LootEntry, Integer> map) {
        return map.values().stream().reduce(0, Integer::max);
    }

    private Double meanOfWeights(Map<LootEntry, Integer> map) {
        return map.values().stream().mapToDouble(Integer::doubleValue).average().orElse(0);
    }

    private Double continuousTranslationValue(int x, int min, int max, double a, double b) {
        return (((b - a) * (x - min)) / (double) (max - min)) + a;
    }

    private TreeMap<Double, LootEntry> weightingsToCumulative(Map<LootEntry, Integer> map, double rarityMultiplier) {
        int min = minOfWeights(map);
        int max = maxOfWeights(map);
        double a = 1 / rarityMultiplier;
        double b = rarityMultiplier;

        double cum = 0;
        TreeMap<Double, LootEntry> cumMap = new TreeMap<>();

        for (Map.Entry<LootEntry, Integer> i : map.entrySet()) {
            cum += (double) i.getValue() / continuousTranslationValue(i.getValue(), min, max, a, b);
            cumMap.put(cum, i.getKey());
        }

        return cumMap;
    }

    private ItemStack nextItem() {
        TreeMap<Double, LootEntry> cumMap = weightingsToCumulative(lootWeightings, 1);
        double randDouble = rand.nextDouble(cumMap.lastEntry().getKey());
        LootEntry lootEntry = cumMap.ceilingEntry(randDouble).getValue();
        return applyEnchantmentTable(new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax() + 1)), lootEntry.getEnchantments());
    }

    private ItemStack nextRareItem() {
        TreeMap<Double, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getRareLootMultiplier());
        double randDouble = rand.nextDouble(cumMap.lastEntry().getKey());
        LootEntry lootEntry = cumMap.ceilingEntry(randDouble).getValue();
        return applyEnchantmentTable(new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax() + 1)), lootEntry.getEnchantments());
    }

    private ItemStack nextRefillItem() {
        TreeMap<Double, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getRefillLootMultiplier());
        double randDouble = rand.nextDouble(cumMap.lastEntry().getKey());
        LootEntry lootEntry = cumMap.ceilingEntry(randDouble).getValue();
        return applyEnchantmentTable(new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax() + 1)), lootEntry.getEnchantments());
    }

    private ItemStack nextRefillRareItem() {
        TreeMap<Double, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getRefillRareLootMultiplier());
        double randDouble = rand.nextDouble(cumMap.lastEntry().getKey());
        LootEntry lootEntry = cumMap.ceilingEntry(randDouble).getValue();
        return applyEnchantmentTable(new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax() + 1)), lootEntry.getEnchantments());
    }

    private ItemStack nextFeastItem() {
        TreeMap<Double, LootEntry> cumMap = weightingsToCumulative(lootWeightings, gm.getArenaGamemap().getFeastLootMultiplier());
        double randDouble = rand.nextDouble(cumMap.lastEntry().getKey());
        LootEntry lootEntry = cumMap.ceilingEntry(randDouble).getValue();
        return applyEnchantmentTable(new ItemStack(lootEntry.getMaterial(), rand.nextInt(lootEntry.getMin(), lootEntry.getMax() + 1)), lootEntry.getEnchantments());
    }

    private ItemStack applyEnchantmentTable(ItemStack itemStack, List<EnchantmentEntry> enchantmentEntries) {
        if (enchantmentEntries == null || enchantmentEntries.isEmpty()) return itemStack;

        double chance;
        for (EnchantmentEntry e : enchantmentEntries) {
            chance = rand.nextDouble();
            if (chance < e.getChance()) {
                try {
                    itemStack.addEnchantment(e.getEnchantment(), e.getLevel());
                } catch (IllegalArgumentException ex) {
                    Main.getInstance().getLogger().warning("Could not enchant " + itemStack.getType() + " with " + e.getEnchantment().getKey() + "! Enchantment level provided was invalid, exception: " + ex);
                }
            }
        }

        return itemStack;
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

