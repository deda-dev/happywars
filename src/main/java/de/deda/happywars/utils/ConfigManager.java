package de.deda.happywars.utils;

import de.deda.happywars.gamestates.LobbyState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfigManager {


    public String testObject(String path, YamlConfiguration config) {
        String rawString = config.getString(path);
        String utf8String = new String(rawString.getBytes(), StandardCharsets.UTF_8);
        return utf8String;
    }


    public String getString(String path, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        return config.getString(path)
                .replace("&", "§")
                .replace("%PREFIX%", config.getString("Prefix").replace("&", "§"));
    }

    public String getCmdString(String path, String cmd, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        return config.getString(path)
                .replace("&", "§")
                .replace("%PREFIX%", config.getString("Prefix").replace("&", "§"))
                .replace("%COMMAND%", cmd);
    }

    public String getChatString(String path, Player player, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        return config.getString(path)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%PLAYER%", player.getName());
    }

    public String getJoinQuitString(String path, Player player, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        return config.getString(path)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%PLAYERSIZE%", ""+Variable.playing.size())
                .replace("%PLAYER%", player.getDisplayName());
    }

    public String getIdleString(String path, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;
        if(Variable.playing == null) return null;

        int missingPlayers = LobbyState.MIN_PLAYERS-Variable.playing.size();

        return config.getString(path)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%MISSING%", ""+missingPlayers);
    }

    public String getRespawnPointString(String path, String respawnPointName, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        String rawString = config.getString(path);
        String utf8String = new String(rawString.getBytes(), StandardCharsets.UTF_8)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%RESPAWNPOINT-NAME%", respawnPointName);

        return utf8String;
    }

    public String getGameStartCountdownString(String path, int seconds, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        return config.getString(path)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%SECONDS%", ""+seconds);
    }

    public String getLobbyScoreboardString(String path, String teamName, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        String rawString = config.getString(path);
        String utf8String = new String(rawString.getBytes(), StandardCharsets.UTF_8)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%TEAM%", teamName);

        return utf8String;
    }

    public String getIngameScoreboardString(String path, String team1Name, String team2Name, String capturedColor, String respawnPointName, int respawnPointNumber, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        String rawString = config.getString(path);
        String utf8String = new String(rawString.getBytes(), StandardCharsets.UTF_8)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%TEAM1-NAME%", team1Name)
                .replace("%TEAM2-NAME%", team2Name);

        if(utf8String.contains("%"+respawnPointNumber+"-RESPAWNPOINT-COLOR%") && utf8String.contains("%"+respawnPointNumber+"-RESPAWNPOINT-NAME%")) {
            return utf8String.replace("%"+respawnPointNumber+"-RESPAWNPOINT-COLOR%", capturedColor)
                            .replace("%"+respawnPointNumber+"-RESPAWNPOINT-NAME%", respawnPointName);

        } else if(utf8String.contains("%"+respawnPointNumber+"-RESPAWNPOINT-COLOR%")) {
            return utf8String.replace("%"+respawnPointNumber+"-RESPAWNPOINT-COLOR%", capturedColor);

        } else if(utf8String.contains("%"+respawnPointNumber+"-RESPAWNPOINT-NAME%")) {
            return utf8String.replace("%"+respawnPointNumber+"-RESPAWNPOINT-NAME%", respawnPointName);
        }
        return utf8String;
    }

    public String getRespawnPointNumberString(String path, String team1Name, String team2Name, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        if(config.getString("Prefix") == null) return null;

        String rawString = config.getString(path);
        String utf8String = new String(rawString.getBytes(), StandardCharsets.UTF_8)
                .replace("&", "§")
                .replace("%PREFIX%", Variable.prefix)
                .replace("%TEAM1-NAME%", team1Name)
                .replace("%TEAM2-NAME%", team2Name);

        return utf8String;
    }

    public int getInt(String path, YamlConfiguration config) {
        if(config.get(path) == null) return 0;
        return config.getInt(path);
    }

    public short getShort(String path, YamlConfiguration config) {
        if(config.get(path) == null) return 0;
        return (short) config.getInt(path);
    }

    public double getDouble(String path, YamlConfiguration config) {
        if(config.get(path) == null) return 0;
        return config.getDouble(path);
    }

    public Boolean getBoolean(String path, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        return config.getBoolean(path);
    }

    public Material getMaterial(String path, YamlConfiguration config) {
        if(config.getString(path) == null) return null;
        return Material.getMaterial(config.getString(path));
    }

    public void setLocation(String path, Location loc, File file, YamlConfiguration config) {
       if(loc == null || path == null || file == null || config == null) return;

        config.set(path+".World", loc.getWorld().getName());
        config.set(path+".X", loc.getX());
        config.set(path+".Y", loc.getY());
        config.set(path+".Z", loc.getZ());
        config.set(path+".Yaw", loc.getYaw());
        config.set(path+".Pitch", loc.getPitch());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBlockLocation(String path, Location loc, File file, YamlConfiguration config) {
        if(loc == null || path == null || file == null || config == null) return;

        config.set(path+".World", loc.getWorld().getName());
        config.set(path+".X", loc.getX());
        config.set(path+".Y", loc.getY());
        config.set(path+".Z", loc.getZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBlocksType(String path, Block block, File file, YamlConfiguration config) {
        if(block == null || path == null || file == null || config == null) return;

        config.set(path+".Material", block.getType().toString());
        config.set(path+".Id", block.getState().getRawData());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String path, YamlConfiguration config) {
        if(config.getString("Locations.Spawn.lobby") == null) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED +"Please use §a/happywars setup §cand set all spawn locations!");
            return null;
        }
        if(config.getString(path) == null) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED +"Please use §a/happywars setup §cand set all spawn locations!");
            return null;
        }

        World world = Bukkit.getWorld(config.getString(path+".World"));
        double x = config.getDouble(path+".X");
        double y = config.getDouble(path+".Y");
        double z = config.getDouble(path+".Z");
        float yaw = (float) config.getDouble(path+".Yaw");
        float pitch = (float) config.getDouble(path+".Pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location getCoreLocation(String path, YamlConfiguration config) {
        if(config.getString(path) == null) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED +"Please use §a/happywars setup §cand set all spawn locations!");
            return null;
        }

        World world = Bukkit.getWorld(config.getString(path+".World"));
        double x = config.getDouble(path+".X");
        double y = config.getDouble(path+".Y");
        double z = config.getDouble(path+".Z");

        return new Location(world, x, y, z);
    }

}
