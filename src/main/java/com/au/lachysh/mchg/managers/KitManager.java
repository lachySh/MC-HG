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
//        kits.add(new Chameleon()); Temp disabled waiting on LibsDisguises update
        kits.add(new Spy());
        kits.add(new Fisherman());
        kits.add(new Worm());
        kits.add(new Forger());
        kits.add(new Jumper());
        kits.add(new Thor());
        kits.add(new Viper());
        kits.add(new Hearthstone());
        kits.add(new Poseidon());
        kits.add(new Kangaroo());

        Collections.sort(kits);

        return kits;
    }
}
