package com.au.lachysh.mchg.managers;

import com.au.lachysh.mchg.abilities.Ability;
import com.au.lachysh.mchg.abilities.SoupCrafting;
import com.au.lachysh.mchg.abilities.SoupHeal;

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
