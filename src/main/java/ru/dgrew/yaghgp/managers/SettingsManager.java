package ru.dgrew.yaghgp.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.dgrew.yaghgp.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SettingsManager {
    final private FileConfiguration config;

    public SettingsManager(FileConfiguration config) {
        this.config = config;
        setUpEntries();
    }

    private World worldobj;
    private String lobby;

    public World getLobbyobj() {
        worldobj = Bukkit.getWorld(lobby);
        return worldobj;
    }

    private String lobbySpawn;

    public Location fetchLobbySpawn() {
        try {
            String[] coords = lobbySpawn.split(":");
            return new Location(getLobbyobj(),
                    Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]),
                    Double.parseDouble(coords[2]),
                    Float.parseFloat(coords[3]),
                    Float.parseFloat(coords[4]));
        } catch (NumberFormatException e) {
            Bukkit.getLogger().severe("Lobby coordinates are not set up or set up incorrectly in the plugin config! Please set up the coordinates and restart the server! Related stack trace below:");
            return null;
        }
    }

    private boolean updateCheck;

    public boolean getUpdateCheck() {
        return updateCheck;
    }

    private Integer compassTrackRange;

    public Integer getCompassTrackRange() {
        return compassTrackRange;
    }

    private boolean useCustomWorldGen;

    public boolean getUseCustomWorldGen() {
        return useCustomWorldGen;
    }


    void setUpEntries() {
        lobby = config.getString("settings.lobby");
        lobbySpawn = config.getString("settings.lobby-spawn");
        compassTrackRange = config.getInt("settings.compass-track-range");
        updateCheck = config.getBoolean("settings.check-for-updates");
        useCustomWorldGen = config.getBoolean("settings.use-custom-world-generator");
    }
}
