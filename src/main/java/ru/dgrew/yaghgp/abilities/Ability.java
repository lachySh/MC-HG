package ru.dgrew.yaghgp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class Ability<E extends Event> {

    private long cooldown;
    private String name;
    private boolean disabled;

    public Ability(String name, long cooldown, boolean disabled) {
        this.name = name;
        this.cooldown = cooldown;
        this.disabled = disabled;
    }

    public abstract AbilityCallable<E> getCallable();
    public abstract boolean isPreconditionMet(E event);

    public long getCooldown(){
        return cooldown;
    };
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    public boolean isDisabled() {
        return disabled;
    }

}
