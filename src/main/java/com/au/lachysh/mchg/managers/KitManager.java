package com.au.lachysh.mchg.managers;


import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.kits.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitManager {
    public KitManager() {
        List<Kit> kits = getAllKits();
        Main.getInstance().getLogger().info("Found " + kits.size() + " kits: " + kits.toString());
    }
    public List<Kit> getAllKits() {
        ArrayList<Kit> kits = new ArrayList<Kit>();

        kits.add(new Cultivator());
        kits.add(new Stomper());
        kits.add(new Apparition());
        kits.add(new Switcher());
        kits.add(new Endermage());
        kits.add(new Chameleon());
        kits.add(new Spy());
        kits.add(new Fisherman());
        kits.add(new Worm());

        Collections.sort(kits);

        return kits;
    }
}
