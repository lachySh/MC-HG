package com.au.lachysh.mchg.kits;

import com.au.lachysh.mchg.abilities.forger.InstantSmelt;
import com.au.lachysh.mchg.abilities.jumper.ChargedJump;
import com.au.lachysh.mchg.abilities.jumper.ChargedJumpDetector;
import com.au.lachysh.mchg.abilities.jumper.LimitFallDamage;
import org.bukkit.Material;

import java.util.List;

public class Jumper extends Kit {
    public Jumper() {
        super(
                "jumper",
                "Jumper",
                "Hold shift to charge a mega jump.\nHold for up to 5 seconds to increase the jump height.\nFall damage is limited to 2 hearts.",
                true,
                KitType.UTILITY,
                Material.RABBIT_FOOT,
                List.of(),
                List.of(new ChargedJump(),
                        new ChargedJumpDetector(),
                        new LimitFallDamage()
                )
        );
    }
}
