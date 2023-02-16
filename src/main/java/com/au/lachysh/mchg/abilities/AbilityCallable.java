package com.au.lachysh.mchg.abilities;

import org.bukkit.event.Event;

public interface AbilityCallable<E extends Event> {
    void execute(E event);
}