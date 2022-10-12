package de.deda.happywars.doStates;

import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Ingame {

    private static final ConfigManager config = new ConfigManager();
    private static final ArrayList numberCollectionTeam1 = new ArrayList();
    private static final ArrayList numberCollectionTeam2 = new ArrayList();

    public static void setScoreboard() {
        ScoreboardManager.removeBoard("Lobby");
        for (int i=0; i<Variable.playing.size(); i++) {
            ScoreboardManager.setBoard(Variable.playing.get(i));
            ScoreboardManager.setScoreboard(Variable.playing.get(i));
        }
    }

    public static void teleportPlayers() {
        Collections.shuffle(Variable.playing);
        for(int i=0;i<Variable.playing.size();i++) {
            Player player = Variable.playing.get(i);
            Random random = new Random();
            int min = 1;
            int max = 8;

            if(ScoreboardManager.getTeamOne().hasEntry(player.getName()))
                teleportTeam1Player(player, random, min, max);
            if(ScoreboardManager.getTeamTwo().hasEntry(player.getName()))
                teleportTeam2Player(player, random, min, max);
        }
    }

    public static void openClassSelectionInv() {
        for(int i=0;i<Variable.playing.size();i++) {
            Player player = Variable.playing.get(i);
            player.getInventory().clear();
            Variable.classSelection.add(player);
            InventoryManager.openClassSelectionInv(player);
        }
    }

    public static void fillRespawnPoints() {
        int respawnPoints = config.getInt("RespawnPoints.amount", Variable.config);
        for(int i=1;i<respawnPoints+1;i++) {
            Variable.respawnPoints.put(i, "neutral");
        }
    }

    private static void teleportTeam1Player(Player player, Random random, int min, int max) {
        for(int i=0; i<=max;i++) {
            int locationNumber = random.nextInt(max+min)+min;
            if(numberCollectionTeam1.contains(locationNumber)) continue;

            Location teamSpawnLocation = config.getLocation("Locations.Spawn.Team1."+locationNumber, Variable.locConfig);
            player.teleport(teamSpawnLocation);
            numberCollectionTeam1.add(locationNumber);
            return;
        }
    }

    private static void teleportTeam2Player(Player player, Random random, int min, int max) {
        for(int i=0; i<=max;i++) {
            int locationNumber = random.nextInt(max+min)+min;
            if(numberCollectionTeam2.contains(locationNumber)) continue;

            Location teamSpawnLocation = config.getLocation("Locations.Spawn.Team2."+locationNumber, Variable.locConfig);
            player.teleport(teamSpawnLocation);
            numberCollectionTeam2.add(locationNumber);
            return;
        }
    }

}
