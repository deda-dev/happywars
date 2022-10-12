package de.deda.happywars.gamestates;

import de.deda.happywars.countdown.LobbyCountdown;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;

import java.util.Collections;

public class LobbyState extends GameState {

    private static final ConfigManager config = new ConfigManager();
    public static final int MIN_PLAYERS = config.getInt("MinPlayers", Variable.config),
                            MAX_PLAYERS = config.getInt("MaxPlayers", Variable.config);
    private LobbyCountdown countdown;

    @Override
    public void init() {
        countdown = new LobbyCountdown();
        countdown.startIdle();
        Variable.canMove = true;
    }

    @Override
    public void end() {
        Collections.shuffle(Variable.playing);
        for (int i=0; i<Variable.playing.size();i++) {
            ScoreboardManager.removeBoard("Lobby");
            ScoreboardManager.addTeam(Variable.playing.get(i), ScoreboardManager.getTeamOne());
            ScoreboardManager.addTeam(Variable.playing.get(i), ScoreboardManager.getTeamTwo());

            ScoreboardManager.setBoard(Variable.playing.get(i));
            ScoreboardManager.setScoreboard(Variable.playing.get(i));
        }
    }

    public LobbyCountdown getCountdown() {
        return countdown;
    }
}
