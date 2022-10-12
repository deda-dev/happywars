package de.deda.happywars.listeners;

import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private static final ConfigManager config = new ConfigManager();
    private static int respawnPoints;

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws Exception {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        if(config.getInt("RespawnPoints.amount", Variable.config) <= 0) Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Set the amount of respawn points from 1 to 9");
        respawnPoints = config.getInt("RespawnPoints.amount", Variable.config);

        if(Variable.setup.containsKey(player)) {
            event.setCancelled(true);
            switch(Variable.setup.get(player)) {
                case 22:
                    config.setBlockLocation("Locations.RespawnCore.Team1", blockLocation, Variable.locFile, Variable.locConfig);
                    player.sendMessage(Variable.prefix+" §aYou set the respawn core from team 1.");
                    player.sendMessage(Variable.prefix+" §7Now break the respawn core from team 2.");
                    Variable.setup.remove(player, 22);
                    Variable.setup.put(player, 23);
                    break;
                case 23:
                    config.setBlockLocation("Locations.RespawnCore.Team2", blockLocation, Variable.locFile, Variable.locConfig);
                    player.sendMessage(Variable.prefix+" §aYou set the respawn core from team 2.");
                    player.sendMessage(Variable.prefix+" §7Now break the respawn core from the 1. respawn point.");
                    Variable.setup.remove(player, 23);
                    Variable.setup.put(player, 24);
                    break;
                case 24:
                    if(respawnPoints > Variable.numberOfRespawnPoints) {
                        config.setBlockLocation("Locations.RespawnCore."+Variable.numberOfRespawnPoints, blockLocation, Variable.locFile, Variable.locConfig);
                        player.sendMessage(Variable.prefix+" §aYou set the "+Variable.numberOfRespawnPoints+". respawn core.");
                        player.sendMessage(Variable.prefix+" §7Now break all changed blocks when a team captured the respawn point. First the neutral block than the captured block for team1 and than for team2.");
                        player.sendMessage(Variable.prefix+" §7When du doesn't want to change any blocks, than type \"done\" in the chat.");

                        Variable.setup.remove(player, 24);
                        Variable.setup.put(player, 25);
                        break;
                    }
                    config.setBlockLocation("Locations.RespawnCore."+Variable.numberOfRespawnPoints, blockLocation, Variable.locFile, Variable.locConfig);
                    player.sendMessage(Variable.prefix+" §aYou set the "+Variable.numberOfRespawnPoints+". respawn core.");
                    player.sendMessage(Variable.prefix+" §7Now break all changed blocks when a team captured the respawn point. First the neutral block than the captured block for team1 and than for team2.");
                    player.sendMessage(Variable.prefix+" §7When du doesn't want to change any blocks, than type \"done\" in the chat.");

                    Variable.setup.remove(player, 24);
                    Variable.setup.put(player, 25);
                    break;
                case 25:
                    config.setBlockLocation("Locations.RespawnCore."+Variable.numberOfRespawnPoints+".changedBlocks."+Variable.numberOfChangedBlocks, blockLocation, Variable.locFile, Variable.locConfig);
                    config.setBlocksType("Locations.RespawnCore."+Variable.numberOfRespawnPoints+".changedBlocks."+Variable.numberOfChangedBlocks+".neutral", block, Variable.locFile, Variable.locConfig);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1 ,1);

                    Variable.setup.remove(player, 25);
                    Variable.setup.put(player, 26);
                    break;
                case 26:
                    config.setBlocksType("Locations.RespawnCore."+Variable.numberOfRespawnPoints+".changedBlocks."+Variable.numberOfChangedBlocks+".captured.team1", block, Variable.locFile, Variable.locConfig);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1 ,1);

                    Variable.setup.remove(player, 26);
                    Variable.setup.put(player, 27);
                    break;
                case 27:
                    config.setBlocksType("Locations.RespawnCore."+Variable.numberOfRespawnPoints+".changedBlocks."+Variable.numberOfChangedBlocks+".captured.team2", block, Variable.locFile, Variable.locConfig);
                    player.sendMessage(Variable.prefix+" §7When you are ready with this respawn point type \"done\" in the chat.");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1 ,1);

                    Variable.setup.remove(player, 27);
                    Variable.setup.put(player, 25);
                    Variable.numberOfChangedBlocks++;
                    break;
            }
        }

        if(Variable.build.contains(event.getPlayer())) return;
        if(!Variable.canBreak) event.setCancelled(true);

        if(GameStateHandler.getCurrentState() instanceof IngameState) {
            // Abfragen vom main respawn core
            if(config.getCoreLocation("Locations.RespawnCore.Team1", Variable.locConfig) == blockLocation) {
                if(ScoreboardManager.getTeamOne().hasEntry(player.getName())) return;
                GameStateHandler.setGameState(2);
                return;
            } else if(config.getCoreLocation("Locations.RespawnCore.Team2", Variable.locConfig) == blockLocation) {
                if(ScoreboardManager.getTeamTwo().hasEntry(player.getName())) return;
                GameStateHandler.setGameState(2);
                return;
            }

            // Abfragen von respawn points
            for(int i=1;i<respawnPoints+1;i++) {
                if(config.getCoreLocation("Locations.RespawnCore."+i, Variable.locConfig) == null) return;

                Location respawnPointLocation = config.getCoreLocation("Locations.RespawnCore."+i, Variable.locConfig);
                if(!respawnPointLocation.equals(blockLocation)) continue;

                if(Variable.respawnPoints.get(i).equalsIgnoreCase("neutral")) {
                    blockLocation.getWorld().playSound(blockLocation, Sound.ANVIL_USE, 1, 1);

                    if(ScoreboardManager.getTeamOne().hasEntry(player.getName())) {
                        for(int x=1;x<Variable.locConfig.getConfigurationSection("Locations.RespawnCore."+i+".changedBlocks.").getKeys(false).size()+1;x++) {
                            Location changedBlockLocation = config.getCoreLocation("Locations.RespawnCore."+i+".changedBlocks."+x, Variable.locConfig);
                            changedBlockLocation.getBlock().setType(config.getMaterial("Locations.RespawnCore."+i+".changedBlocks."+x+".captured.team1.Material", Variable.locConfig));

                            BlockState blockState =  changedBlockLocation.getBlock().getState();
                            Byte data = (byte) config.getShort("Locations.RespawnCore."+i+".changedBlocks."+x+".captured.team1.Id", Variable.locConfig);

                            blockState.setRawData(data);
                            blockState.update();
                        }
                        ScoreboardManager.removeBoard("Ingame");

                        Variable.respawnPoints.remove(i);
                        Variable.respawnPoints.put(i, "team1");

                        for (int y = 0; y<Variable.playing.size(); y++) {
                            ScoreboardManager.setBoard(Variable.playing.get(y));
                            ScoreboardManager.setScoreboard(Variable.playing.get(y));
                        }
                        for(int z=0;z<Variable.death.size();z++) {
                            Player target = Variable.death.get(z);
                            if(!Variable.spawnSelection.contains(target)) continue;
                            target.closeInventory();
                        }
                        return;
                    }
                    if(ScoreboardManager.getTeamTwo().hasEntry(player.getName())) {
                        for(int x=1;x<Variable.locConfig.getConfigurationSection("Locations.RespawnCore."+i+".changedBlocks.").getKeys(false).size()+1;x++) {
                            Location changedBlockLocation = config.getCoreLocation("Locations.RespawnCore."+i+".changedBlocks."+x, Variable.locConfig);
                            changedBlockLocation.getBlock().setType(config.getMaterial("Locations.RespawnCore."+i+".changedBlocks."+x+".captured.team2.Material", Variable.locConfig));

                            BlockState blockState =  changedBlockLocation.getBlock().getState();
                            Byte data = (byte) config.getShort("Locations.RespawnCore."+i+".changedBlocks."+x+".captured.team2.Id", Variable.locConfig);

                            blockState.setRawData(data);
                            blockState.update();
                        }
                        ScoreboardManager.removeBoard("Ingame");

                        Variable.respawnPoints.remove(i);
                        Variable.respawnPoints.put(i, "team2");

                        for(int y = 0; y<Variable.playing.size(); y++) {
                            ScoreboardManager.setBoard(Variable.playing.get(y));
                            ScoreboardManager.setScoreboard(Variable.playing.get(y));
                        }
                        for(int z=0;z<Variable.death.size();z++) {
                            Player target = Variable.death.get(z);
                            if(!Variable.spawnSelection.contains(target)) continue;
                            target.closeInventory();
                        }
                        return;
                    }
                }
                if(Variable.respawnPoints.get(i).equalsIgnoreCase("team1") || Variable.respawnPoints.get(i).equalsIgnoreCase("team2")) {
                    //if(checkPlayerTeam(player, i)) continue;

                    blockLocation.getWorld().playEffect(blockLocation, Effect.EXPLOSION_LARGE, 5, 20);
                    blockLocation.getWorld().playSound(blockLocation, Sound.EXPLODE, 1, 1);

                    for(int x=1;x<Variable.locConfig.getConfigurationSection("Locations.RespawnCore."+i+".changedBlocks.").getKeys(false).size()+1;x++) {
                        Location changedBlockLocation = config.getCoreLocation("Locations.RespawnCore."+i+".changedBlocks."+x, Variable.locConfig);
                        changedBlockLocation.getBlock().setType(config.getMaterial("Locations.RespawnCore."+i+".changedBlocks."+x+".neutral.Material", Variable.locConfig));

                        BlockState blockState =  changedBlockLocation.getBlock().getState();
                        Byte data = (byte) config.getShort("Locations.RespawnCore."+i+".changedBlocks."+x+".neutral.Id", Variable.locConfig);

                        blockState.setRawData(data);
                        blockState.update();
                    }
                    ScoreboardManager.removeBoard("Ingame");

                    Variable.respawnPoints.remove(i);
                    Variable.respawnPoints.put(i, "neutral");

                    for (int y = 0; y<Variable.playing.size(); y++) {
                        ScoreboardManager.setBoard(Variable.playing.get(y));
                        ScoreboardManager.setScoreboard(Variable.playing.get(y));
                    }
                    for(int z=0;z<Variable.death.size();z++) {
                        Player target = Variable.death.get(z);
                        if(!Variable.spawnSelection.contains(target)) continue;
                        target.closeInventory();
                    }
                    return;
                }


                return;
            }

        }

    }

    //
    //
    // Nicht jeden ChangeBlock setzen, sonden schematic setzen
    //
    //

    private static boolean checkPlayerTeam(Player player, int i) {
        if(Variable.respawnPoints.get(i).equalsIgnoreCase("team1"))
            if(ScoreboardManager.getTeamOne().hasEntry(player.getName()))
                return true;
        if(Variable.respawnPoints.get(i).equalsIgnoreCase("team2"))
            if(ScoreboardManager.getTeamTwo().hasEntry(player.getName()))
                return true;
        return false;
    }

    public static int getRespawnPoints() {
        return respawnPoints;
    }

}
