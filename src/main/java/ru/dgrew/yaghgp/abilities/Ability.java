package ru.dgrew.yaghgp.abilities;

import org.bukkit.event.Event;
import ru.dgrew.yaghgp.abilities.cooldown.Cooldown;

public abstract class Ability<E extends Event> {

    private Cooldown cooldown;
    private String name;
    private boolean disabled;
    private final Class<E> type;

    public Ability(String name, Class<E> type, long standardCooldown, boolean disabled) {
        this.name = name;
        this.cooldown = new Cooldown(standardCooldown);
        this.disabled = disabled;
        this.type = type;
    }

    public Ability(String name, Class<E> type, long standardCooldown, long currentCooldown, boolean disabled) {
        this.name = name;
        this.cooldown = new Cooldown(standardCooldown, currentCooldown);
        this.disabled = disabled;
        this.type = type;
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

    public Class<E> getType() {
        return type;
    }
}
