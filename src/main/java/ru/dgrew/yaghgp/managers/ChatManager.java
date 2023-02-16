package ru.dgrew.yaghgp.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ChatManager {
    final private FileConfiguration config;

    public ChatManager(FileConfiguration config) {
        this.config = config;
        setUpEntries();
    }

    private String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private List<String> formatList(List<String> l) {
        for (String s : l) l.set(l.indexOf(s), ChatColor.translateAlternateColorCodes('&', s));
        return l;
    }

    private String formatTime(String s, int timer) {
        if (timer / 60 >= 1) {
            s = s.replace("{timer}", String.valueOf(timer / 60));
            if (timer / 60 != 1) return s.replace("{m}", "minutes");
            else return s.replace("{m}", "minute");
        } else s = s.replace("{timer}", String.valueOf(timer));
        if (timer != 1) return s.replace("{m}", "seconds");
        else return s.replace("{m}", "second");
    }

    private String formatEmbeddedString(String s, String pattern, String replace) {
        return s.replace("{" + pattern + "}", replace);
    }

    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    private String perm;

    public String getPerm() {
        return perm;
    }

    private String started;

    public String getStarted() {
        return started;
    }

    private List<String> summary;

    public List<String> getSummary() {
        return summary;
    }

    private String lobbytimer;

    public String getLobbytimer(int timer) {
        return formatTime(lobbytimer, timer);
    }

    private String cancelled;

    public String getCancelled() {
        return cancelled;
    }

    private String startTimer;

    public String getStartTimer(int timer) {
        return formatTime(this.startTimer, timer);
    }

    private String invincibilityTimer;

    public String getInvincibilityTimer(int timer) {
        return formatTime(this.invincibilityTimer, timer);
    }

    private String timerend;

    public String getTimerend() {
        return timerend;
    }

    private String globalkill;

    public String getGlobalkill() {
        return globalkill;
    }

    private String kill;

    public String getKill() {
        return kill;
    }

    private String killed;

    public String getKilled() {
        return killed;
    }

    private String killednat;

    public String getKillednat() {
        return killednat;
    }

    private String deathmatch;

    public String getDeathmatch(int timer) {
        return formatTime(this.deathmatch, timer);
    }

    private String deathmatchstart;

    public String getDeathmatchstart() {
        return deathmatchstart;
    }

    private String deathmatchprep;

    public String getDeathmatchprep(int timer) {
        return formatTime(this.deathmatchprep, timer);
    }

    private String victory;

    public String getVictory() {
        return victory;
    }

    private String victorytitle;

    public String getVictorytitle() {
        return victorytitle;
    }

    private String globalvictory;

    public String getGlobalvictory() {
        return globalvictory;
    }

    private String endgame;

    public String getEndgame(int timer) {
        return formatTime(endgame, timer);
    }

    private String refill;

    public String getRefill(int timer) {
        return formatTime(refill, timer);
    }

    private String refillcommencing;

    public String getRefillcommencing() {
        return refillcommencing;
    }

    private String lobbyPlayerCounter;

    public String getLobbyPlayerCounter(int count) {
        return formatEmbeddedString(lobbyPlayerCounter, "count", String.valueOf(count));
    }

    private String alreadyStarted;

    public String getAlreadyStarted() {
        return alreadyStarted;
    }

    private String voteRandom;

    public String getVoteRandom() {
        return voteRandom;
    }

    private String voteRandomSubtitle;

    public String getVoteRandomSubtitle() {
        return voteRandomSubtitle;
    }

    private String voteCustom;

    public String getVoteCustom() {
        return voteCustom;
    }

    private String voteCustomSubtitle;

    public String getVoteCustomSubtitle() {
        return voteCustomSubtitle;
    }

    private String voteTitle;

    public String getVoteTitle() {
        return voteTitle;
    }

    private String voteCustomMapTitle;

    public String getVoteCustomMapTitle() {
        return voteCustomMapTitle;
    }

    private String voteMessage;

    public String getVoteMessage(String mapName) {
        return formatEmbeddedString(voteMessage, "map", mapName);
    }

    private String lobbyMapSelection;

    public String getLobbyMapSelection(String mapName) {
        return formatEmbeddedString(lobbyMapSelection, "map", mapName + ChatColor.RESET + ChatColor.WHITE);
    }

    private String lobbyMapLoaded;

    public String getLobbyMapLoaded() {
        return lobbyMapLoaded;
    }

    private String compassTimerNotification;

    public String getCompassTimerNotification(int timer) {
        return formatTime(this.compassTimerNotification, timer);
    }

    private String compassTime;

    public String getCompassTime() {
        return compassTime;
    }

    private String movementTimeNotification;

    public String getMovementTimeNotification(int timer) {
        return formatTime(this.movementTimeNotification, timer);
    }

    private String kitsTooLate;

    public String getKitsTooLate() {
        return kitsTooLate;
    }

    private String kitsMenuTitle;

    public String getKitsMenuTitle() {
        return kitsMenuTitle;
    }

    private String kitsSelection;

    public String getKitsSelection(String kitName) {
        return formatEmbeddedString(kitsSelection, "kit", kitName);
    }

    private void setUpEntries() {
        prefix = format(config.getString("messages.prefix"));
        perm = format(config.getString("messages.no-permission"));
        started = format(config.getString("messages.game-started"));
        summary = formatList(config.getStringList("messages.summary"));
        lobbytimer = format(config.getString("messages.lobby-timer-notifications"));
        cancelled = format(config.getString("messages.launch-cancelled"));
        startTimer = format(config.getString("messages.start-timer-notifications"));
        invincibilityTimer = format(config.getString("messages.invincibility-timer-notifications"));
        timerend = format(config.getString("messages.global-game-started"));
        globalkill = format(config.getString("messages.global-kill"));
        kill = format(config.getString("messages.kill"));
        killed = format(config.getString("messages.killed"));
        killednat = format(config.getString("messages.killed-natural"));
        deathmatch = format(config.getString("messages.deathmatch-timer-notifications"));
        deathmatchstart = format(config.getString("messages.deathmatch-started"));
        deathmatchprep = format(config.getString("messages.deathmatch-prepare"));
        victory = format(config.getString("messages.victory"));
        victorytitle = format(config.getString("messages.victory-title"));
        globalvictory = format(config.getString("messages.victory-global"));
        endgame = format(config.getString("messages.endgame"));
        refill = format(config.getString("messages.refill"));
        refillcommencing = format(config.getString("messages.refill-commencing"));
        lobbyPlayerCounter = format(config.getString("messages.lobby-player-counter"));
        alreadyStarted = format(config.getString("messages.already-started"));
        voteTitle = format(config.getString("messages.vote-title"));
        voteRandom = format(config.getString("messages.vote-random"));
        voteRandomSubtitle = format(config.getString("messages.vote-random-subtitle"));
        voteCustom = format(config.getString("messages.vote-custom"));
        voteCustomSubtitle = format(config.getString("messages.vote-custom-subtitle"));
        voteCustomMapTitle = format(config.getString("messages.vote-custom-map-title"));
        voteMessage = format(config.getString("messages.vote-message"));
        lobbyMapSelection = format(config.getString("messages.lobby-map-selection"));
        lobbyMapLoaded = format(config.getString("messages.lobby-map-loaded"));
        compassTimerNotification = format(config.getString("messages.compass-timer-notifications"));
        compassTime = format(config.getString("messages.compass-time"));
        movementTimeNotification = format(config.getString("messages.movement-timer-notifications"));
        kitsTooLate = format(config.getString("messages.kits-too-late"));
        kitsMenuTitle = format(config.getString("messages.kits-menu-title"));
        kitsSelection = format(config.getString("messages.kits-selection"));
    }
}
