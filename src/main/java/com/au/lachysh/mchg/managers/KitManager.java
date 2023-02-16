package com.au.lachysh.mchg.managers;


import com.au.lachysh.mchg.kits.Apparition;
import com.au.lachysh.mchg.kits.Cultivator;
import com.au.lachysh.mchg.kits.Stomper;
import com.au.lachysh.mchg.kits.Kit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitManager {
    public List<Kit> getAllKits() {
        ArrayList<Kit> kits = new ArrayList<Kit>();

        kits.add(new Cultivator());
        kits.add(new Stomper());
        kits.add(new Apparition());

        Collections.sort(kits);

        return kits;
    }
}
