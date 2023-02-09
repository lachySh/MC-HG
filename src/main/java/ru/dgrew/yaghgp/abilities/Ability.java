package ru.dgrew.yaghgp.abilities;

import org.bukkit.event.Event;
import ru.dgrew.yaghgp.abilities.cooldown.Cooldown;

public abstract class Ability<E extends Event> {

    private Cooldown cooldown;
    private String name;
    private boolean disabled;

    public Ability(String name, long standardCooldown, boolean disabled) {
        this.name = name;
        this.cooldown = new Cooldown(standardCooldown);
        this.disabled = disabled;
    }

    public Ability(String name, long standardCooldown, boolean disabled, long currentCooldown) {
        this.name = name;
        this.cooldown = new Cooldown(standardCooldown);
        this.disabled = disabled;
    }

    public abstract AbilityCallable<E> getCallable();

    public abstract boolean precondition(E event);

    public long getCurrentCooldown() {
        return cooldown.timeRemaining();
    }

    public void cooldown() {
        cooldown.cooldown();
    }

    public boolean isOnCooldown() {
        return cooldown.timeRemaining() > 0;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getName() {
        return this.name;
    }
}
