package de.deda.happywars.utils;

import de.deda.happywars.gamestates.EndingState;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.gamestates.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {

    private static final ConfigManager config = new ConfigManager();

    private static Scoreboard board;
    private static Objective objective;
    private static Team one, two, players;

    public ScoreboardManager() {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("happywars", "dummy");
        one = board.registerNewTeam(config.getString("Team1.name", Variable.config));
        two = board.registerNewTeam(config.getString("Team2.name", Variable.config));
        players = board.registerNewTeam("Players");

        one.setPrefix(config.getString("Team1.prefix", Variable.config));
        two.setPrefix(config.getString("Team2.prefix", Variable.config));
        players.setPrefix(config.getString("Players.prefix", Variable.config));
    }

    public static void setBoard(Player player) {
        //
        // Set Scoreboard for LobbyState
        //
        if(GameStateHandler.getCurrentState() instanceof LobbyState) {
            if(!config.getBoolean("Scoreboard.Lobby.enabled", Variable.config)) return;
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(config.getString("Scoreboard.Lobby.title", Variable.config));

            for (String key : Variable.config.getConfigurationSection("Scoreboard.Lobby.lines").getKeys(false))
                objective.getScore(config.getLobbyScoreboardString("Scoreboard.Lobby.lines." + key, getTeamName(player), Variable.config)).setScore(Integer.parseInt(key));
        //
        // Set Scoreboard for IngameState
        //
        } else if(GameStateHandler.getCurrentState() instanceof IngameState) {
            if(!config.getBoolean("Scoreboard.Ingame.enabled", Variable.config)) return;
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(config.getString("Scoreboard.Ingame.title", Variable.config));

            int numbers = 1;
            for (String key : Variable.config.getConfigurationSection("Scoreboard.Ingame.lines").getKeys(false)) {
                String color = getRespawnPointColor(numbers);
                String respawnPointName = config.getString("RespawnPoints."+numbers+".name", Variable.config);
                objective.getScore(config.getIngameScoreboardString("Scoreboard.Ingame.lines." + key, one.getName(), two.getName(), color, respawnPointName, numbers, Variable.config)).setScore(Integer.parseInt(key));

                if(config.getRespawnPointNumberString("Scoreboard.Ingame.lines." + key, one.getName(), two.getName(), Variable.config).contains("%"+numbers+"-RESPAWNPOINT-COLOR%")) {
                   numbers++;
                    if(numbers >= Variable.respawnPoints.size())
                        numbers = Variable.respawnPoints.size();
                }
            }
            //
            // Setzen von spectator scoreboard & items von Spec
            //

        //
        // Set Scoreboard for EndingState
        //
        } else if(GameStateHandler.getCurrentState() instanceof EndingState) {
            if(!config.getBoolean("Scoreboard.Ending.enabled", Variable.config)) return;
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(config.getString("Scoreboard.Ending.title", Variable.config));



        }

    }

    public static void removeBoard(String gameState) {
        if(gameState.equalsIgnoreCase("ingame")) {
            int numbers = 1;
            for (String key : Variable.config.getConfigurationSection("Scoreboard.Ingame.lines").getKeys(false)) {
                String color = getRespawnPointColor(numbers);
                String respawnPointName = config.getString("RespawnPoints."+numbers+".name", Variable.config);
                board.resetScores(config.getIngameScoreboardString("Scoreboard.Ingame.lines." + key, one.getName(), two.getName(), color, respawnPointName, numbers, Variable.config));

                if(config.getRespawnPointNumberString("Scoreboard.Ingame.lines." + key, one.getName(), two.getName(), Variable.config).contains("%"+numbers+"-RESPAWNPOINT-COLOR%")) {
                    numbers++;
                    if(numbers >= Variable.respawnPoints.size())
                        numbers = Variable.respawnPoints.size();
                }
            }
            return;
        }
        for (String key : Variable.config.getConfigurationSection("Scoreboard."+gameState+".lines").getKeys(false))
            for (int i=0;i<Variable.playing.size();i++)
            board.resetScores(config.getLobbyScoreboardString("Scoreboard."+gameState+".lines." + key, getTeamName(Variable.playing.get(i)), Variable.config));
    }

    private static String getTeamName(Player player) {
        if(one.hasEntry(player.getName())) {
            return one.getName();
        } else if(two.hasEntry(player.getName())) {
            return two.getName();
        }
        return "-";
    }

    public static void setScoreboard(Player player) {
        for(Player all : Bukkit.getOnlinePlayers())
            all.setScoreboard(board);
        player.setScoreboard(board);
    }

    public static void joinTeam(Player player, Team team) {
        if(team.getSize() >= LobbyState.MAX_PLAYERS / 2) {
            player.closeInventory();
            player.sendMessage(config.getLobbyScoreboardString("JoinedFullTeamMessage", team.getName(), Variable.config));
            return;
        }
        player.closeInventory();
        team.addEntry(player.getName());
        player.sendMessage(config.getLobbyScoreboardString("JoinedTeamMessage", team.getName(), Variable.config));
    }

    public static void addTeam(Player player, Team team) {
        if(one.hasEntry(player.getName()) || two.hasEntry(player.getName())) return;

        if(team.getSize() >= LobbyState.MAX_PLAYERS / 2) {
            player.closeInventory();
            player.sendMessage(config.getLobbyScoreboardString("JoinedFullTeamMessage", team.getName(), Variable.config));
            return;
        }
        player.closeInventory();
        players.removeEntry(player.getName());
        team.addEntry(player.getName());
        player.sendMessage(config.getLobbyScoreboardString("JoinedTeamMessage", team.getName(), Variable.config));
    }

    public static void removeTeam(Player player) {
        if(player.getScoreboard().getEntryTeam(player.getName()) == null) return;
        player.getScoreboard().getEntryTeam(player.getName()).removeEntry(player.getName());
    }

    public static String getRespawnPointColor(int i) {
        switch(Variable.respawnPoints.get(i)) {
            case "neutral":
                return config.getString("RespawnPoints.neutral", Variable.config);
            case "team1":
                return config.getString("RespawnPoints.captured.team1", Variable.config);
            case "team2":
                return config.getString("RespawnPoints.captured.team2", Variable.config);
        }
        return config.getString("RespawnPoints.neutral", Variable.config);
    }

    public static Scoreboard getBoard() {
        return board;
    }

    public static Objective getObjective() {
        return objective;
    }

    public static Team getTeamOne() {
        return one;
    }

    public static Team getTeamTwo() {
        return two;
    }

    public static Team getTeamPlayers() {
        return players;
    }

}
