package de.deda.happywars.gamestates;

import org.bukkit.Bukkit;

public class EndingState extends GameState {

    @Override
    public void init() {
        Bukkit.broadcastMessage("§8Die End-Phase hat begonnen.");
    }

    @Override
    public void end() {

    }
}

