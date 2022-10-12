package de.deda.happywars.gamestates;

import java.util.ArrayList;

public class GameStateHandler {

    private static GameState current;
    private static ArrayList<GameState> states = new ArrayList<>();

    public GameStateHandler() {
        states.add(new LobbyState());
        states.add(new IngameState());
        states.add(new EndingState());
    }

    public static void setGameState(int id) {
        if(current != null) current.end();
        current = states.get(id);
        current.init();
    }

    public static GameState getCurrentState() {
        return current;
    }

}
