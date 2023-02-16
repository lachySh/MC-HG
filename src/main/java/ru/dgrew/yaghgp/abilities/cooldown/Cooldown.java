package ru.dgrew.yaghgp.abilities.cooldown;

public class Cooldown {
    private long durationMillis;
    private long lastUsedMillis;

    public Cooldown(long durationSeconds) {
        this.durationMillis = durationSeconds * 1000;
    }

    public Cooldown(long durationSeconds, long currentCooldownSeconds) {
        this.durationMillis = durationSeconds * 1000;
        this.lastUsedMillis = System.currentTimeMillis() - durationMillis + (currentCooldownSeconds * 1000);
    }

    public void cooldown() {
        lastUsedMillis = System.currentTimeMillis();
    }

    public long timeRemaining() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - durationMillis > lastUsedMillis) {
            return 0;
        } else {
            return ((long) Math.ceil((double) (lastUsedMillis + durationMillis - currentTimeMillis) / 1000));
        }
    }
}
