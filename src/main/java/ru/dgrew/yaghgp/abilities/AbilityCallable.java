package ru.dgrew.yaghgp.abilities;

import org.bukkit.event.Event;

public interface AbilityCallable<E extends Event> {
    void execute(E event);
}