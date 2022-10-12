package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class PlayerMoveListener implements Listener {

    private static final ConfigManager config = new ConfigManager();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(Variable.build.contains(player)) return;
        if(!Variable.canMove)
            event.setCancelled(true);

        if(GameStateHandler.getCurrentState() instanceof IngameState) {
            if(Variable.stuned.containsKey(player)) {
                if(Variable.stuned.get(player) > System.currentTimeMillis()) {
                    if(event.getTo().getY() != event.getFrom().getY())
                        event.setTo(event.getFrom());
                    player.setWalkSpeed(0);
                    return;
                } else {
                    player.setWalkSpeed((float) 0.2);
                    Variable.stuned.remove(player);
                    return;
                }
            }

            Material coreMaterial = config.getMaterial("RespawnPoints.Core.material", Variable.config);

            Bukkit.getScheduler().runTaskLater(HappyWars.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    //
                    // wird im InteractEvent in Adventure gesetzt, da die Klasse Warrior Eile als skill hat und nicht schneller abbauen soll (darf)
                    //
                    if(player.getGameMode().equals(GameMode.ADVENTURE))
                        if(!player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                            player.setGameMode(GameMode.SURVIVAL);
                    if(!player.getTargetBlock((Set<Material>) null, 5).getType().equals(coreMaterial)) {
                        if(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
                            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        if(Variable.supporterClass.contains(player))
                            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                    }
                    if(player.getTargetBlock((Set<Material>) null, 5).getType().equals(coreMaterial)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 0, false, false));
                        if(Variable.supporterClass.contains(player))
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 0, false, false));
                    }
                }
            }, 10);

            //
            // Das gehört zu dem unten drunter
            //
            // gete alle spieler im team vom Spieler (vom event) & check ob ein spieler (aus team) von den in teamskill drin ist &
            // wenn ja check ob einer von den getteten Spieler (spieler aus team check ob in temskill drin und daraus die player[]) der Spieler (vom event) ist
            //
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //
            // Ich glaube das unten drunter ist doppelt zu dem zwei unten drunter!!!!
            //
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //

            /*
            for(String allPlayerNameTeam : player.getScoreboard().getEntryTeam(player.getName()).getEntries()) {
                Player teamSkillOwner = Bukkit.getPlayer(allPlayerNameTeam);
                if(!Variable.teamSkill.containsKey(teamSkillOwner)) continue;
                if(Variable.teamSkill.get(teamSkillOwner) == null) continue;
                for(int i=0;i<Variable.teamSkill.get(teamSkillOwner).length;i++) {
                    if(Variable.teamSkill.get(teamSkillOwner)[i].equals(player)) {
                        if(event.getTo().getY() != event.getFrom().getY()) {
                            event.setTo(event.getFrom());
                            return;
                        }
                    }
                }
            }
             */

            if(Variable.teamSkill.containsKey(player) || Variable.teamSkill.get(player) != null)
                if(event.getTo().getY() != event.getFrom().getY()) {
                    event.setTo(event.getFrom());
                    return;
                }

            if(!Variable.playing.contains(player) || Variable.spectating.contains(player) || Variable.build.contains(player) | Variable.setup.containsKey(player) ||
                    Variable.classSelection.contains(player) || Variable.spawnSelection.contains(player) ||
                    player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) return;

            int maxHeight = config.getInt("DeathHeight.max", Variable.config),
                    minHeight = config.getInt("DeathHeight.min", Variable.config);
            if(player.getLocation().getY() < maxHeight && player.getLocation().getY() > minHeight) return;
            if(player.getHealth() <= 0) return;
            player.setHealth(0);
        }

    }
/*
    @EventHandler
    public void onDamageBlock(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material coreMaterial = config.getMaterial("RespawnPoints.Core.material", Variable.config);

        if(block.getType().equals(coreMaterial))
            for(int i=1;i<BlockBreakListener.getRespawnPoints()+1;i++)
                if(block.equals(config.getCoreLocation("Locations.RespawnCore."+i, Variable.locConfig).getBlock())) {
                    if(Variable.supporterClass.contains(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 0, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 0, false, false));
                        return;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999, 0, false, false));
                    return;
                }
    }
 */

}
