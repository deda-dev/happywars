package de.deda.happywars.listeners;

import de.deda.happywars.gamestates.EndingState;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.gamestates.LobbyState;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private static final ConfigManager cm = new ConfigManager();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(Variable.setup.containsKey(player)) {
            if(!event.getMessage().equalsIgnoreCase("done")) return;
            event.setCancelled(true);

            Location location = player.getLocation();
            switch (Variable.setup.get(player)) {
                case 1:
                    cm.setLocation("Locations.Spawn.lobby", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player, 1);
                    Variable.setup.put(player, 2);
                    player.sendMessage(Variable.prefix+" §aYou set the lobby spawn.");
                    player.sendMessage(Variable.prefix+" §aNow repeat with all positions.");
                    player.sendMessage(Variable.prefix+" §7Set the spectator spawn.");
                    break;
                case 2:
                    cm.setLocation("Locations.Spawn.spectator", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player, 2);
                    Variable.setup.put(player, 3);
                    player.sendMessage(Variable.prefix+" §aYou set the spectator spawn.");
                    player.sendMessage(Variable.prefix+" §7Now set the death spawn.");
                    break;
                case 3:
                    cm.setLocation("Locations.Spawn.death", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player, 3);
                    Variable.setup.put(player, 4);
                    player.sendMessage(Variable.prefix+" §aYou set the death spawn.");
                    player.sendMessage(Variable.prefix+" §7Now set the 1. spawn location for team 1.");
                    break;
                case 4:
                    cm.setLocation("Locations.Spawn.Team1.1", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 5);
                    player.sendMessage(Variable.prefix+" §aYou set the 1. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 2. spawn location for team 1.");
                    break;
                case 5:
                    cm.setLocation("Locations.Spawn.Team1.2", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 6);
                    player.sendMessage(Variable.prefix+" §aYou set the 2. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 3. spawn location for team 1.");
                    break;
                case 6:
                    cm.setLocation("Locations.Spawn.Team1.3", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 7);
                    player.sendMessage(Variable.prefix+" §aYou set the 3. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 4. spawn location for team 1.");
                    break;
                case 7:
                    cm.setLocation("Locations.Spawn.Team1.4", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 8);
                    player.sendMessage(Variable.prefix+" §aYou set the 4. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 5. spawn location for team 1.");
                    break;
                case 8:
                    cm.setLocation("Locations.Spawn.Team1.5", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 9);
                    player.sendMessage(Variable.prefix+" §aYou set the 5. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 6. spawn location for team 1.");
                    break;
                case 9:
                    cm.setLocation("Locations.Spawn.Team1.6", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 10);
                    player.sendMessage(Variable.prefix+" §aYou set the 6. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 7. spawn location for team 1.");
                    break;
                case 10:
                    cm.setLocation("Locations.Spawn.Team1.7", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 11);
                    player.sendMessage(Variable.prefix+" §aYou set the 7. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 8. spawn location for team 1.");
                    break;
                case 11:
                    cm.setLocation("Locations.Spawn.Team1.8", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 12);
                    player.sendMessage(Variable.prefix+" §aYou set the 8. spawn location for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 9. spawn location for team 1.");
                    break;
                case 12:
                    cm.setLocation("Locations.Spawn.Team1.9", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 13);
                    player.sendMessage(Variable.prefix+" §aYou set the all spawn locations for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now set the 1. spawn location for team 2.");
                    break;
                case 13:
                    cm.setLocation("Locations.Spawn.Team2.1", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 14);
                    player.sendMessage(Variable.prefix+" §aYou set the 1. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 2. spawn location for team 2.");
                    break;
                case 14:
                    cm.setLocation("Locations.Spawn.Team2.2", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 15);
                    player.sendMessage(Variable.prefix+" §aYou set the 2. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 3. spawn location for team 2.");
                    break;
                case 15:
                    cm.setLocation("Locations.Spawn.Team2.3", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 16);
                    player.sendMessage(Variable.prefix+" §aYou set the 3. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 4. spawn location for team 2.");
                    break;
                case 16:
                    cm.setLocation("Locations.Spawn.Team2.4", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 17);
                    player.sendMessage(Variable.prefix+" §aYou set the 4. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 5. spawn location for team 2.");
                    break;
                case 17:
                    cm.setLocation("Locations.Spawn.Team2.5", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 18);
                    player.sendMessage(Variable.prefix+" §aYou set the 5. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 6. spawn location for team 2.");
                    break;
                case 18:
                    cm.setLocation("Locations.Spawn.Team2.6", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 19);
                    player.sendMessage(Variable.prefix+" §aYou set the 6. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 7. spawn location for team 2.");
                    break;
                case 19:
                    cm.setLocation("Locations.Spawn.Team2.7", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 20);
                    player.sendMessage(Variable.prefix+" §aYou set the 7. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 8. spawn location for team 2.");
                    break;
                case 20:
                    cm.setLocation("Locations.Spawn.Team2.8", location, Variable.locFile, Variable.locConfig);
                    Variable.setup.remove(player);
                    Variable.setup.put(player, 21);
                    player.sendMessage(Variable.prefix+" §aYou set the 8. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §7Now set the 9. spawn location for team 2.");
                    break;
                case 21:
                    cm.setLocation("Locations.Spawn.Team2.9", location, Variable.locFile, Variable.locConfig);
                    player.sendMessage(Variable.prefix+" §aYou set the 9. spawn location for team 2.");
                    player.sendMessage(Variable.prefix+" §aYou set the all spawn locations for team 1.");
                    player.sendMessage(Variable.prefix+" §7Now break the respawn core from team 1.");
                    Variable.setup.remove(player, 21);
                    Variable.setup.put(player, 22);
                    break;
                case 24:
                    Variable.numberOfRespawnPoints++;
                    if(BlockBreakListener.getRespawnPoints() > Variable.numberOfRespawnPoints-1) {
                        player.sendMessage(Variable.prefix+" §7Now break the respawn core from the "+Variable.numberOfRespawnPoints+". respawn point.");
                        Variable.numberOfChangedBlocks = 1;
                        break;
                    }
                    player.sendMessage(Variable.prefix+" §a§lSetup done.");
                    Variable.setup.remove(player);
                    break;
                case 25:
                    Variable.numberOfRespawnPoints++;
                    if(BlockBreakListener.getRespawnPoints() > Variable.numberOfRespawnPoints-1) {
                        player.sendMessage(Variable.prefix+" §7Now break the respawn core from the "+Variable.numberOfRespawnPoints+". respawn point.");
                        Variable.numberOfChangedBlocks = 1;

                        Variable.setup.remove(player, 25);
                        Variable.setup.put(player, 24);
                        break;
                    }
                    player.sendMessage(Variable.prefix+" §a§lSetup done.");
                    Variable.setup.remove(player);
                    break;
            }
        }

        if(GameStateHandler.getCurrentState() instanceof LobbyState) {
            if(ScoreboardManager.getTeamOne().hasEntry(player.getName())) {
                String chatPrefix = cm.getChatString("Team1.chatPrefix", player, Variable.config);
                event.setFormat(chatPrefix+event.getMessage());

            } else if(ScoreboardManager.getTeamTwo().hasEntry(player.getName())) {
                String chatPrefix = cm.getChatString("Team2.chatPrefix", player, Variable.config);
                event.setFormat(chatPrefix+event.getMessage());

            } else if(ScoreboardManager.getTeamPlayers().hasEntry(player.getName())) {
                String chatPrefix = cm.getChatString("Players.chatPrefix", player, Variable.config);
                event.setFormat(chatPrefix+event.getMessage());
            }


        } else if(GameStateHandler.getCurrentState() instanceof IngameState) {



        } else if(GameStateHandler.getCurrentState() instanceof EndingState) {
            if(ScoreboardManager.getTeamOne().hasEntry(player.getName())) {
                String chatPrefix = cm.getChatString("Team1.chatPrefix", player, Variable.config);
                event.setFormat(chatPrefix+event.getMessage());

            } else if(ScoreboardManager.getTeamTwo().hasEntry(player.getName())) {
                String chatPrefix = cm.getChatString("Team2.chatPrefix", player, Variable.config);
                event.setFormat(chatPrefix+event.getMessage());

            } else if(ScoreboardManager.getTeamPlayers().hasEntry(player.getName())) {
                String chatPrefix = cm.getChatString("Players.chatPrefix", player, Variable.config);
                event.setFormat(chatPrefix+event.getMessage());
            }
        }

    }



}