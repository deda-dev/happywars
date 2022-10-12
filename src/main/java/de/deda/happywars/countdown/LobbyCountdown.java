package de.deda.happywars.countdown;

import de.deda.happywars.HappyWars;
import de.deda.happywars.gamestates.GameState;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.tokens.ValueToken;

public class LobbyCountdown extends Countdown {

    ConfigManager cm = new ConfigManager();

    private int seconds = cm.getInt("StartGameSeconds", Variable.config);
    private int taskID, idleID;
    public boolean isIdling = false, isRunning = false;
    public boolean startDecreasedCountdown = false;

    @Override
    public void start() {
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HappyWars.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player all : Variable.playing) all.setLevel(seconds);
                switch (seconds) {
                        case 60: case 50: case 40: case 30: case 20: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
                            Bukkit.broadcastMessage(cm.getGameStartCountdownString("CountdownMessage", seconds, Variable.config));
                            for (Player all : Variable.playing) all.playSound(all.getLocation(), Sound.ORB_PICKUP, 1, 1);
                            break;
                        case 0:
                            for (Player all : Variable.playing) all.playSound(all.getLocation(), Sound.LEVEL_UP, 1, 1);
                            GameStateHandler.setGameState(GameState.INGAME_STATE);
                            break;
                    }
                seconds--;
            }
        }, 0, 20L);
    }

    public void stopCountdown() {
        if(!isRunning) return;
        isRunning = false;
        Bukkit.getScheduler().cancelTask(taskID);
        seconds = cm.getInt("StartGameSeconds", Variable.config);
        if(startDecreasedCountdown) seconds = cm.getInt("DecreasedSeconds", Variable.config);
        for (Player all : Variable.playing) all.setLevel(0);
    }

    @Override
    public void stop() {
        seconds = cm.getInt("StartGameSeconds", Variable.config);
        if(startDecreasedCountdown) seconds = cm.getInt("DecreasedSeconds", Variable.config);
        isRunning = false;
        isIdling = false;
        Bukkit.getScheduler().cancelTask(taskID);
        Bukkit.getScheduler().cancelTask(idleID);
    }

    public void startIdle() {
        isIdling = true;
        idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HappyWars.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(cm.getIdleString("IdleMessage", Variable.config));
            }
        }, 0, 20L * cm.getInt("IdleMessageSeconds", Variable.config));
    }

    public void stopIdle() {
        if(!isIdling) return;
        isIdling = false;
        Bukkit.getScheduler().cancelTask(idleID);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
