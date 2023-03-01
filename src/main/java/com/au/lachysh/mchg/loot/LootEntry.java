package com.au.lachysh.mchg.loot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

@Data
@AllArgsConstructor
public class LootEntry {
    private Material material;
    private int min;
    private int max;
}
