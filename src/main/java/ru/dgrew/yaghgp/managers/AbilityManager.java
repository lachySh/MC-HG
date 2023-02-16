package ru.dgrew.yaghgp.managers;

import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.abilities.CompassTrack;
import ru.dgrew.yaghgp.abilities.SoupCrafting;
import ru.dgrew.yaghgp.abilities.SoupHeal;

import java.util.ArrayList;
import java.util.List;

public class AbilityManager {
    public static List<Ability> startingIntrinsicAbilities() {
        ArrayList<Ability> abilities = new ArrayList<Ability>();
        abilities.add(new SoupHeal());
        abilities.add(new SoupCrafting());
        return abilities;
    }


}
