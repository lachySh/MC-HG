package ru.dgrew.yaghgp.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import ru.dgrew.yaghgp.tribute.Tribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PlayerManager {
    final private List<Tribute> tributes = new ArrayList<>();
    final private List<Player> spectators = new ArrayList<>();

    public PlayerManager() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            tributes.add(new Tribute(p));
        }
    }

    public void transferToSpectators(Tribute tribute) {
        tributes.remove(tribute);
        Player player = tribute.getPlayerObject();
        spectators.add(player);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void transferToSpectators(Player player) {
        tributes.removeIf(tribute -> tribute.getPlayerObject().equals(player));
        spectators.add(player);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public List<Tribute> getRemainingTributesList() {
        return tributes;
    }

    public Optional<Tribute> findTribute(Player player) {
        return tributes.stream().parallel()
                .filter(tribute -> tribute.getPlayerObject().equals(player))
                .findFirst();
    }

    public Optional<Tribute> findTribute(HumanEntity humanEntity) {
        return tributes.stream().parallel()
                .filter(tribute -> tribute.getPlayerObject().getName().equals(humanEntity.getName()))
                .findFirst();
    }

    public void updateTributesList() {
        spectators.clear();
        tributes.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            tributes.add(new Tribute(p));
        }
        Bukkit.getLogger().info("Tributes list after update: " + tributes.toString());
    }

    public void addTribute(Player player) {
        tributes.add(new Tribute(player));
    }

    public void removeTribute(Player player) {
        tributes.removeIf(tribute -> tribute.getPlayerObject().equals(player));
    }

    public void removeOnDC(Player p) {
        tributes.removeIf(tribute -> tribute.getPlayerObject().equals(p));
    }

    public void giveIntrinsicAbilitiesToAllTributes() {
        tributes.stream()
                .forEach(tribute -> {
                    Bukkit.getLogger().info("Giving " + tribute.getPlayerObject().getDisplayName() + " intrinsic abilities...");
                    tribute.giveIntrinsicAbilities();
                });
    }

    public void clearAllPlayerScoreboards() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
}
