package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryClickListener implements Listener {

    private static final ConfigManager config = new ConfigManager();

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(Variable.build.contains(player)) return;
        if(!Variable.canInvClick) event.setCancelled(true);

        try {

            // Team selection Inventory

            if(event.getClickedInventory().equals(InventoryManager.getTeamSelectionInv())) {
                event.setCancelled(true);
                if(event.getCurrentItem().equals(InventoryManager.getTeam1Item())) {
                    if(ScoreboardManager.getTeamOne().hasEntry(player.getName())) {
                        player.closeInventory();
                        player.sendMessage(config.getLobbyScoreboardString("JoinedSameTeamMessage", ScoreboardManager.getTeamOne().getName(), Variable.config));
                        return;
                    }

                    ScoreboardManager.removeBoard("Lobby");
                    ScoreboardManager.removeTeam(player);
                    ScoreboardManager.joinTeam(player, ScoreboardManager.getTeamOne());
                    ScoreboardManager.setBoard(player);
                    ScoreboardManager.setScoreboard(player);
                    return;
                }
                if(event.getCurrentItem().equals(InventoryManager.getTeam2Item())) {
                    if(ScoreboardManager.getTeamTwo().hasEntry(player.getName())) {
                        player.closeInventory();
                        player.sendMessage(config.getLobbyScoreboardString("JoinedSameTeamMessage", ScoreboardManager.getTeamTwo().getName(), Variable.config));
                        return;
                    }

                    ScoreboardManager.removeBoard("Lobby");
                    ScoreboardManager.removeTeam(player);
                    ScoreboardManager.joinTeam(player, ScoreboardManager.getTeamTwo());
                    ScoreboardManager.setBoard(player);
                    ScoreboardManager.setScoreboard(player);
                    return;
                }

            }

            // Class selection Inventory

            if(event.getClickedInventory().equals(InventoryManager.getClassSelectionInv())) {
                event.setCancelled(true);
                if(event.getCurrentItem().equals(InventoryManager.getWarriorClassItem())) {
                    if(Variable.death.contains(player)) {
                        Variable.classSelection.remove(player);
                        Variable.warriorClass.add(player);
                        player.closeInventory();

                        Variable.spawnSelection.add(player);
                        InventoryManager.openSpawnSelectionInv(player);
                        return;
                    }
                    InventoryManager.setClassItems(player, 0);
                    Variable.classSelection.remove(player);

                    Variable.warriorClass.add(player);
                    player.closeInventory();
                    return;
                }
                if(event.getCurrentItem().equals(InventoryManager.getArcherClassItem())) {
                    if(Variable.death.contains(player)) {
                        Variable.classSelection.remove(player);
                        Variable.archerClass.add(player);
                        player.closeInventory();

                        Variable.spawnSelection.add(player);
                        InventoryManager.openSpawnSelectionInv(player);
                        return;
                    }
                    InventoryManager.setClassItems(player, 1);
                    Variable.classSelection.remove(player);

                    Variable.archerClass.add(player);
                    player.closeInventory();
                    return;
                }
                if(event.getCurrentItem().equals(InventoryManager.getSupporterClassItem())) {
                    if(Variable.death.contains(player)) {
                        Variable.classSelection.remove(player);
                        Variable.supporterClass.add(player);
                        player.closeInventory();

                        Variable.spawnSelection.add(player);
                        InventoryManager.openSpawnSelectionInv(player);
                        return;
                    }
                    InventoryManager.setClassItems(player, 2);
                    Variable.classSelection.remove(player);

                    Variable.supporterClass.add(player);
                    player.closeInventory();
                    return;
                }

            }

            // Spawn selection Inventory

            if(event.getClickedInventory().equals(InventoryManager.getSpawnSelectionInv())) {
                event.setCancelled(true);
                if(event.getCurrentItem().equals(InventoryManager.getTeamItem(1))) {
                    if(ScoreboardManager.getTeamTwo().hasEntry(player.getName())) {
                        player.sendMessage("XXXXXX!!!!!§cDu bist nicht in diesem Team!");
                        return;
                    }
                    InventoryManager.setClassItems(player, getPlayerClass(player));
                    Variable.spawnSelection.remove(player);
                    Variable.death.remove(player);

                    teleportPlayerTeamBase(player, "Team1");
                    player.closeInventory();
                    return;
                }
                if(event.getCurrentItem().equals(InventoryManager.getTeamItem(2))) {
                    if(ScoreboardManager.getTeamOne().hasEntry(player.getName())) {
                        player.sendMessage("XXXXXX!!!!!§cDu bist nicht in diesem Team!");
                        return;
                    }
                    InventoryManager.setClassItems(player, getPlayerClass(player));
                    Variable.spawnSelection.remove(player);
                    Variable.death.remove(player);

                    teleportPlayerTeamBase(player, "Team2");
                    player.closeInventory();
                    return;
                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(1))) {
                    switch(config.getInt("RespawnPoints.amount", Variable.config)) {
                        case 1:
                            if(event.getSlot() != 4) return;
                            if(!event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(1))) return;
                            if(InventoryManager.getRespawnPointStatus(1).equalsIgnoreCase("neutral")) return;
                            if(InventoryManager.getRespawnPointStatus(1).equalsIgnoreCase("team1") &! ScoreboardManager.getTeamOne().hasEntry(player.getName())) return;
                            if(InventoryManager.getRespawnPointStatus(1).equalsIgnoreCase("team2") &! ScoreboardManager.getTeamTwo().hasEntry(player.getName())) return;

                            //
                            // Example
                            // Hier i weg und 1 hin, weil es gibt nur den einen Respawn Point
                            //

                            for(int i=1;i<BlockBreakListener.getRespawnPoints();i++) {
                                Location respawnPointLocation = config.getCoreLocation("Locations.RespawnCore."+i, Variable.locConfig);
                                player.teleport(respawnPointLocation.getWorld().getHighestBlockAt(respawnPointLocation.add(2,0,0)).getLocation());
                            }

                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:

                            break;
                        case 5:
                            //
                            //
                            // Das ganze in eine Methode packen und nur die Zahl ändern (1)
                            //
                            //
                            if(event.getSlot() != 3) return;
                            if(!event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(1))) return;
                            if(InventoryManager.getRespawnPointStatus(1).equalsIgnoreCase("neutral")) return;
                            if(InventoryManager.getRespawnPointStatus(1).equalsIgnoreCase("team1") &! ScoreboardManager.getTeamOne().hasEntry(player.getName())) return;
                            if(InventoryManager.getRespawnPointStatus(1).equalsIgnoreCase("team2") &! ScoreboardManager.getTeamTwo().hasEntry(player.getName())) return;

                            //
                            // Location ändern, muss außen drum die bekommen und random dort tpn
                            //

                            List<Location>  respawnPointLocations = new ArrayList<>();
                            Location respawnPointLocation = config.getCoreLocation("Locations.RespawnCore."+1, Variable.locConfig);
                            World  respawnPointWorld = respawnPointLocation.getWorld();

                            int min = config.getInt("RespawnPoints.radius.min", Variable.config);
                            int max = config.getInt("RespawnPoints.radius.max", Variable.config);
                            Random random = new Random();

                            for(int x=0;x<=max-min;x++) {
                                player.sendMessage(""+respawnPointLocation);
                                respawnPointLocations.add(respawnPointWorld.getHighestBlockAt((int) respawnPointLocation.add(min+x,0,0).getX(), (int) respawnPointLocation.getZ()).getLocation());
                                player.sendMessage(""+respawnPointLocation);
                                //player.sendMessage(""+respawnPointWorld.getHighestBlockAt((int) respawnPointLocation.add(min+x,0,0).getX(), (int) respawnPointLocation.getZ()).getLocation());
                                respawnPointLocation.subtract(min+x,0,0);
                                player.sendMessage(""+respawnPointLocation);

                                respawnPointLocations.add(respawnPointLocation.add(-min-x,0,0));
                                //player.sendMessage(""+respawnPointLocation.add(-min-x,0,0));

                                respawnPointLocations.add(respawnPointLocation.add(min+x,0, max));
                                //player.sendMessage(""+respawnPointLocation.add(min+x,0, max));

                                respawnPointLocations.add(respawnPointLocation.add(min+x,0, -max));
                                //player.sendMessage(""+respawnPointLocation.add(min+x,0, -max));

                                respawnPointLocations.add(respawnPointLocation.add(-min-x,0, max));
                                //player.sendMessage(""+respawnPointLocation.add(-min-x,0, max));

                                respawnPointLocations.add(respawnPointLocation.add(-min-x,0, -max));
                                //player.sendMessage(""+respawnPointLocation.add(-min-x,0, -max));

                            }
                            for(int z=0;z<=max-min;z++) {
                                respawnPointLocations.add(respawnPointLocation.add(0,0,min+z));
                                respawnPointLocations.add(respawnPointLocation.add(0,0,-min-z));
                                respawnPointLocations.add(respawnPointLocation.add(min-1,0,min+z));
                                respawnPointLocations.add(respawnPointLocation.add(-min-1,0,min+z));
                                respawnPointLocations.add(respawnPointLocation.add(min-1,0,-min-z));
                                respawnPointLocations.add(respawnPointLocation.add(-min-1,0,-min-z));
                            }
                            int randomNumber = random.nextInt(max+min)+min;
                            player.teleport(respawnPointWorld.getHighestBlockAt(respawnPointLocations.get(randomNumber)).getLocation());
                            respawnPointLocations.clear();
                            break;
                        case 6:

                            break;
                        case 7:

                            break;
                        case 8:

                            break;
                        case 9:

                            break;
                    }


                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(2))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(3))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(4))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(5))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(6))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(7))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(8))) {



                }
                if(event.getCurrentItem().equals(InventoryManager.getRespawnPointItem(9))) {



                }
            }

        } catch(Exception exception) {
        }
    }

    private static void teleportPlayerTeamBase(Player player, String team) {
        Random random = new Random();
        int min = 1;
        int max = 8;

        int locationNumber = random.nextInt(max+min)+min;
        Location teamSpawnLocation = config.getLocation("Locations.Spawn."+team+"."+locationNumber, Variable.locConfig);
        player.teleport(teamSpawnLocation);
    }

    private static Integer getPlayerClass(Player player) {
        if(Variable.warriorClass.contains(player)) return 0;
        if(Variable.archerClass.contains(player)) return 1;
        if(Variable.supporterClass.contains(player)) return 2;
        return 0;
    }

}
