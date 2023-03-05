package com.au.lachysh.mchg.loot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.enchantments.Enchantment;

@Data
@AllArgsConstructor
public class EnchantmentEntry {
    private Enchantment enchantment;
    private Integer level;
    private Double chance;
}
