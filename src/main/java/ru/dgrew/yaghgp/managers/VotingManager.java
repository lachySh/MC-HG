package ru.dgrew.yaghgp.managers;

import org.bukkit.entity.Player;
import ru.dgrew.yaghgp.Main;
import ru.dgrew.yaghgp.gamemap.Gamemap;

import java.util.*;

public class VotingManager {
    private ScoreboardManager sbm;
    private HashMap<Gamemap, Integer> mapVotes;
    private List<String> playersVotedMap;

    public VotingManager() {
        mapVotes = new HashMap<>();
        playersVotedMap = new ArrayList<>();
        sbm = Main.getSbm();
    }

    public void addMapVote(Gamemap gamemap, Player player) {
        if (mapVotes.get(gamemap) == null) mapVotes.putIfAbsent(gamemap, 1);
        else {
            var current = mapVotes.get(gamemap);
            mapVotes.put(gamemap, current + 1);
        }
        playersVotedMap.add(player.getName());
        sbm.updateMapVoteBoard(mapVotes);
    }

    public boolean canPlayerVoteMap(Player player) {
        return !playersVotedMap.contains(player.getName());
    }

    public Gamemap getHighestMapVote() {
        return Collections.max(mapVotes.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
