package com.au.lachysh.mchg.tribute;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.managers.AbilityManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.au.lachysh.mchg.kits.Kit;

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

    /**
     * Will return kit abilities before intrinsic ones, so intrinsic abilities with the same preconds as kit abilities are overwritten by kit abilities
     * @return List of abilities known to tribute
     */
    public List<Ability> getAbilities() {
        List<Ability> abilities = new ArrayList<>();
        if (kit != null) abilities.addAll(kit.getKitAbilities());
        abilities.addAll(intrinsicAbilities);
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
