package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.gamestates.LobbyState;
import de.deda.happywars.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerJoinQuitListener implements Listener {

    private final static ConfigManager config = new ConfigManager();

    @EventHandler
    public void onPreJoin(PlayerLoginEvent event) {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        if(GameStateHandler.getCurrentState() instanceof LobbyState) {
            Variable.playing.add(player);
            event.setJoinMessage(config.getJoinQuitString("JoinMessage", player, Variable.config));

            Location location = config.getLocation("Locations.Spawn.lobby", Variable.locConfig);
            player.setMaxHealth(20);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setLevel(0);
            player.setExp(0);
            player.setWalkSpeed((float) 0.2);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(location);
            for(PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            ItemManager.setJoinItems(player);
            ScoreboardManager.removeBoard("Lobby");
            ScoreboardManager.getTeamPlayers().addEntry(player.getName());

            ScoreboardManager.setBoard(player);
            ScoreboardManager.setScoreboard(player);

            //
            // Test für title
            //
            // Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title @a subtitle {\"text\":\"Ich bin auf dem Weg!\", \"color\":\"red\", \"bold\":true}");
            // Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title @a title {\"text\":\"Aber nicht zu dir...\", \"color\":\"yellow\"}");
            if(Variable.playing.size() > LobbyState.MAX_PLAYERS) {
                String premiumPlayer = config.getString("Permissions.premium", Variable.config);
                if(player.hasPermission("happywars.all") || player.hasPermission("happywars.*") || player.hasPermission(premiumPlayer))
                    for(int i = 0;i<Variable.playing.size();i++) {
                        if(Variable.playing.get(i).hasPermission(premiumPlayer)) return;
                        connectToLobby(Variable.playing.get(i));
                    }
                connectToLobby(player);
            }

            LobbyState lobby = (LobbyState) GameStateHandler.getCurrentState();

            if(Variable.playing.size() < LobbyState.MIN_PLAYERS) return;
            if(lobby.getCountdown().isRunning) return;
            lobby.getCountdown().stopIdle();
            lobby.getCountdown().start();

        } else if(GameStateHandler.getCurrentState() instanceof IngameState) {
            Variable.spectating.add(player);

            // Setze Spec items & gamemode bzw. unsichtbar
        }


    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        if(GameStateHandler.getCurrentState() instanceof LobbyState) {
            Variable.playing.remove(player);
            event.setQuitMessage(config.getJoinQuitString("QuitMessage", player, Variable.config));

            LobbyState lobby = (LobbyState) GameStateHandler.getCurrentState();

            if(Variable.playing.size() >= LobbyState.MIN_PLAYERS) return;
            if(!lobby.getCountdown().isRunning) return;
            lobby.getCountdown().stopCountdown();
            lobby.getCountdown().startIdle();

        } else if(GameStateHandler.getCurrentState() instanceof IngameState) {
            Variable.spectating.remove(player);


        }

    }

    private static void connectToLobby(Player player) {
        if(config.getBoolean("JoinItems.LeaveGame.Connection.Command.enabled", Variable.config)) {
            player.performCommand(config.getString("JoinItems.LeaveGame.Connection.Command.commandType", Variable.config));
            return;
        }
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(byteOutput);
        String lobbyServer = config.getString("JoinItems.LeaveGame.Connection.lobbyServer", Variable.config);
        try {
            output.writeUTF("Connect");
            output.writeUTF(lobbyServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(HappyWars.getPlugin(), "BungeeCord", byteOutput.toByteArray());
    }
}
