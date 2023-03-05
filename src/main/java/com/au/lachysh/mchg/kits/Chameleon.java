package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.chameleon.MobDisguise;
import com.au.lachysh.mchg.abilities.chameleon.OnDamageRemoveDisguise;
import org.bukkit.Material;

import java.util.List;

public class Chameleon extends Kit {
    public Chameleon() {
        super(
                "chameleon",
                "Chameleon",
                "Is it a bat? Is it a plane?\nHitting mobs will disguise you as them.\nTaking damage will remove your disguise.",
                true,
                KitType.DEFENSIVE,
                Material.CREEPER_SPAWN_EGG,
                List.of(),
                List.of()
        );

        var mobDisguise = new MobDisguise();
        this.setKitAbilities(List.of(mobDisguise, new OnDamageRemoveDisguise(mobDisguise)));
    }
}
