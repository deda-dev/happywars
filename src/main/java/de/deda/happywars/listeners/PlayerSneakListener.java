package de.deda.happywars.listeners;

import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Arrays;
import java.util.List;

public class PlayerSneakListener implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if(GameStateHandler.getCurrentState() instanceof IngameState) {
            for(String allPlayerNameTeam : player.getScoreboard().getEntryTeam(player.getName()).getEntries()) {
                Player teamSkillOwner = Bukkit.getPlayer(allPlayerNameTeam);
                if(!Variable.teamSkill.containsKey(teamSkillOwner)) break;
                if(Variable.teamSkill.get(teamSkillOwner) == null) break;
                for(int i=0; i < Variable.teamSkill.get(teamSkillOwner).length; i++) {
                    if(Variable.teamSkill.get(teamSkillOwner)[i].equals(player)) {
                        List<Player> list = Arrays.asList(Variable.teamSkill.get(teamSkillOwner));
                        list.remove(player);
                        Player[] teamSkillPlayers = (Player[]) list.toArray();
                        Variable.teamSkill.remove(teamSkillOwner);
                        Variable.teamSkill.put(teamSkillOwner, teamSkillPlayers);

                        player.setWalkSpeed((float) 0.2);
                        player.sendMessage("!!!!!!XXXXXX §cDu hast das Team Skill verlassen!");
                    }
                }
            }
            if(Variable.teamSkill.containsKey(player)) {
                if(Variable.teamSkill.get(player) == null) {
                    Variable.teamSkill.remove(player);

                    Entity entity = player.getNearbyEntities(1.5,2.5,1.5).get(0);
                    if(!(entity instanceof ArmorStand)) return;
                    ArmorStand stand = (ArmorStand) player.getNearbyEntities(1.5,2.5,1.5).get(0);

                    if(stand.getCustomName().equalsIgnoreCase("!!!!!XXXXXXXX §aHit me"))
                        if(stand.hasPermission(player.getName()))
                            stand.remove();

                    player.setWalkSpeed((float) 0.2);
                    player.sendMessage("!!!!!!XXXXXX §cDu hast das Team Skill abgebrochen!");
                    return;
                }
                for(int i=0; i < Variable.teamSkill.get(player).length; i++) {
                    Player teamSkillPlayers = Variable.teamSkill.get(player)[i];

                    teamSkillPlayers.setWalkSpeed((float) 0.2);
                    teamSkillPlayers.sendMessage("!!!!!!XXXXXX §cDer Team Skill wurde abgebrochen!");
                }
            }
        }


    }

}
