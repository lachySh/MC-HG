package ru.dgrew.yaghgp.tribute;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.kits.Kit;
import ru.dgrew.yaghgp.managers.AbilityManager;

import java.util.ArrayList;
import java.util.List;

public class Tribute {

    private Player player;
    private List<Ability> intrinsicAbilities;
    private Kit kit;

    public Tribute(Player player) {
        this.player = player;
    }

    public Player getPlayerObject() {
        return player;
    }

    public void giveStartingIntrinsicAbilities() {
        intrinsicAbilities = AbilityManager.startingIntrinsicAbilities();
    }

    public List<Ability> getAbilities() {
        List<Ability> abilities = new ArrayList<>();
        abilities.addAll(intrinsicAbilities);
        if (kit != null) abilities.addAll(kit.getKitAbilities());
        return abilities;
    }

    public List<ItemStack> getStartingItems() {
        if (kit == null) {
            return List.of();
        } else {
            return kit.getKitItems();
        }
    }

    public void addIntrinsicAbility(Ability ability) {
        intrinsicAbilities.add(ability);
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Kit getKit() {
        return kit;
    }
}
