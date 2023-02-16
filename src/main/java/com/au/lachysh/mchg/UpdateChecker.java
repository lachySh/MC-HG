package com.au.lachysh.mchg;

import org.bukkit.Bukkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {
    int currentVersion;
    int latestVersion;
    public UpdateChecker(String currentVersion) {
        this.currentVersion = Integer.parseInt(currentVersion.replace("v", "").replace(".", ""));
        this.latestVersion = Integer.parseInt(getLatestVersion().replace("v", "").replace(".", ""));
    }
    public String getLatestVersion() {
        try {
            URLConnection urlConnection = new URL("https://api.spigotmc.org/legacy/update.php?resource=106792").openConnection();
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
        } catch (Exception exception) {
            return null;
        }
    }
    public void checkForUpdates() {
        if (currentVersion < latestVersion) Bukkit.getLogger().info("There is a new YAGHGP update available! Check it out here: https://www.spigotmc.org/resources/yaghgp-yet-another-generic-hunger-games-plugin.106792/updates");
        else if (currentVersion == latestVersion) Bukkit.getLogger().info("There are no YAGHGP updates available!");
        else Bukkit.getLogger().info("You are currently using an unreleased version of YAGHGP or the Spigot API has not updated yet!");
        Bukkit.getLogger().info("You are running "+ Main.getInstance().getDescription().getVersion() + ", latest is reported to be " + getLatestVersion() +".");
    }
}