package ru.dgrew.yaghgp.tribute;

import org.bukkit.entity.Player;
import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.managers.AbilityManager;

import java.util.List;

public class Tribute {

    private Player player;
    private List<Ability> intrinsicAbilities;

    public Tribute(Player player) {
        this.player = player;
    }

    public Player getPlayerObject() {
        return player;
    }

    public void giveIntrinsicAbilities() {
        intrinsicAbilities = AbilityManager.intrinsicAbilities();
    }

    public List<Ability> getAbilities() {
        return intrinsicAbilities;
    }
}
