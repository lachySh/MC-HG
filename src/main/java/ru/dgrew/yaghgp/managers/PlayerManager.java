package ru.dgrew.yaghgp.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import ru.dgrew.yaghgp.tribute.Tribute;

import java.util.ArrayList;
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
        tributes.removeIf(tribute -> tribute.getPlayerObject().getName().equals(player.getName()));
        spectators.add(player);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public List<Tribute> getRemainingTributesList() {
        return tributes;
    }

    public Optional<Tribute> findTribute(Player player) {
        return tributes.stream().parallel()
                .filter(tribute -> tribute.getPlayerObject().getName().equals(player.getName()))
                .findFirst();
    }

    public Optional<Tribute> findTribute(HumanEntity humanEntity) {
        return tributes.stream().parallel()
                .filter(tribute -> tribute.getPlayerObject().getName().equals(humanEntity.getName()))
                .findFirst();
    }

    public void updateTributesList() {
        spectators.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (findTribute(p).isEmpty()) {
                tributes.add(new Tribute(p));
            }
        }
        for (Tribute t : tributes) {
            if (!t.getPlayerObject().isOnline()) {
                tributes.remove(t);
            }
        }
        Bukkit.getLogger().info("Tributes list after refresh: " + tributes.toString());
    }

    public void addTribute(Player player) {
        tributes.add(new Tribute(player));
    }

    public void removeTribute(Player player) {
        tributes.removeIf(tribute -> tribute.getPlayerObject().getName().equals(player.getName()));
    }

    public void removeOnDC(Player p) {
        tributes.removeIf(tribute -> tribute.getPlayerObject().getName().equals(p.getName()));
    }

    public void giveIntrinsicAbilitiesToAllTributes() {
        tributes.stream()
                .forEach(tribute -> {
                    Bukkit.getLogger().info("Giving " + tribute.getPlayerObject().getDisplayName() + " intrinsic abilities...");
                    tribute.giveStartingIntrinsicAbilities();
                });
    }

    public void clearAllPlayerScoreboards() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
}
