package de.deda.happywars.gamestates;

public abstract class GameState {

    public static final int LOBBY_STATE = 0,
                            INGAME_STATE = 1,
                            ENDING_STATE = 2;

    public abstract void init();
    public abstract void end();

}
