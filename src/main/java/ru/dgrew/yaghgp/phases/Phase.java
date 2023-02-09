package ru.dgrew.yaghgp.phases;

import org.bukkit.event.Listener;

public abstract class Phase implements Listener {
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract Phase next();
}
