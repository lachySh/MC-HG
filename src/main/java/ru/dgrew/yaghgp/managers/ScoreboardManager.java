package ru.dgrew.yaghgp.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;
import ru.dgrew.yaghgp.gamemap.Gamemap;

import java.util.Map;

public class ScoreboardManager {
    private static Scoreboard MAP_VOTE_BOARD;

    public ScoreboardManager() {
        initMapVoteBoard();
    }

    private void initMapVoteBoard() {
        if (MAP_VOTE_BOARD != null) return;

        org.bukkit.scoreboard.ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard board = scoreboardManager.getNewScoreboard();
        Objective objective = board.registerNewObjective("MapVoteBoard", Criteria.DUMMY, ChatColor.WHITE + "Map Votes");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        MAP_VOTE_BOARD = board;
    }

    public Scoreboard getMapVoteBoard() {
        return MAP_VOTE_BOARD;
    }

    public void updateMapVoteBoard(Map<Gamemap, Integer> scores) {
        Objective objective = getMapVoteBoard().getObjective("MapVoteBoard");
        for (Map.Entry<Gamemap, Integer> entry : scores.entrySet()) {
            try {
                Score score = objective.getScore(entry.getKey().getTitle());
                score.setScore(entry.getValue());
            } catch (NullPointerException e) {
                Bukkit.getLogger().warning("Something went wrong updating map vote board!");
            }
        }
    }
}
