package de.deda.happywars.gamestates;

import de.deda.happywars.utils.Variable;
import de.deda.happywars.doStates.Ingame;

public class IngameState extends GameState {

    @Override
    public void init() {
        Ingame.fillRespawnPoints();
        Ingame.setScoreboard();
        Ingame.teleportPlayers();
        Ingame.openClassSelectionInv();

        Variable.canMove = true;
        Variable.canAttack = true;
        Variable.canDamage = true;
        Variable.canInvClick = true;
    }

    @Override
    public void end() {

    }
}

