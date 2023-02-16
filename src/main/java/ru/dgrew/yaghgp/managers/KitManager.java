package ru.dgrew.yaghgp.managers;


import ru.dgrew.yaghgp.kits.Apparition;
import ru.dgrew.yaghgp.kits.Cultivator;
import ru.dgrew.yaghgp.kits.Kit;
import ru.dgrew.yaghgp.kits.Stomper;

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
