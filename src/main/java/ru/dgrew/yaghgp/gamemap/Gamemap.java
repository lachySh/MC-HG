package ru.dgrew.yaghgp.gamemap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Data
@Builder
@AllArgsConstructor
public class Gamemap {
    private String filename;
    private String title;
    private String description;
    private Material displayMaterial;

    public boolean equalsItemStack(ItemStack itemStack) {
        return itemStack.getItemMeta().getDisplayName().contains(title)
                && displayMaterial.equals(itemStack.getType());
    }
}
