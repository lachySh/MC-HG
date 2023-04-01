package com.au.lachysh.mchg.managers;

import com.au.lachysh.mchg.Main;
import com.au.lachysh.mchg.phases.Phase;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import com.au.lachysh.mchg.phases.Lobby;

public class PhaseManager {
    private Phase currentPhase;

    public PhaseManager() {
        setPhase(new Lobby());
    }

    public void nextPhase() {
        setPhase(currentPhase.next());
    }

    public void setPhase(Phase phase) {
        if (currentPhase != null) {
            currentPhase.onDisable();
            HandlerList.unregisterAll(currentPhase);
        }
        currentPhase = phase;
        Bukkit.getPluginManager().registerEvents(currentPhase, Main.getInstance());
        currentPhase.onEnable();
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }
}