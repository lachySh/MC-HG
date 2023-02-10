package ru.dgrew.yaghgp.managers;

import ru.dgrew.yaghgp.abilities.Ability;
import ru.dgrew.yaghgp.abilities.SoupCrafting;
import ru.dgrew.yaghgp.abilities.SoupHeal;

import java.util.List;

public class AbilityManager {
    public static List<Ability> intrinsicAbilities() {
        return List.of(
                new SoupHeal(),
                new SoupCrafting()
        );
    }
}
