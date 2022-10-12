package de.deda.happywars.listeners;

import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class PlayerAttackListener implements Listener {


    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if(!(event.getEntity() instanceof Player)) return;
        Player target = (Player) event.getEntity();

        if(Variable.build.contains(player)) return;
        if(!Variable.canAttack) event.setCancelled(true);

        if(GameStateHandler.getCurrentState() instanceof IngameState) {
            if(target.getScoreboard().getEntryTeam(target.getName()).equals(player.getScoreboard().getEntryTeam(player.getName()))) {
                event.setCancelled(true);
                if(player.getItemInHand().getType() == Material.GOLDEN_APPLE && (player.getItemInHand()).getItemMeta().getDisplayName().equalsIgnoreCase("§eGolden hearts")) {
                    if(Variable.skillOne.containsKey(player))
                        if(Variable.skillOne.get(player) > System.currentTimeMillis()) {
                            long timeLeft = (Variable.skillOne.get(player) - System.currentTimeMillis()) / 1000;
                            player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                            return;
                        }
                    if(target.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                        player.sendMessage("!!!!!XXXXXXXX §cDer Spieler hat bereits goldene Herzen!");
                        return;
                    }
                    Variable.skillOne.remove(player);
                    Variable.skillOne.put(player, System.currentTimeMillis() + (10 * 1000));

                    target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 99999, 0));
                    return;
                }
                if(Variable.teamSkill.containsKey(target)) {
                    for(String allPlayerNameTeam : player.getScoreboard().getEntryTeam(player.getName()).getEntries()) {
                        Player teamSkillOwner = Bukkit.getPlayer(allPlayerNameTeam);
                        if(!Variable.teamSkill.containsKey(teamSkillOwner)) break;
                        if(Variable.teamSkill.get(teamSkillOwner) == null) break;
                        for(int i = 0; i < Variable.teamSkill.get(teamSkillOwner).length; i++) {
                            if(Variable.teamSkill.get(teamSkillOwner)[i].equals(player)) {
                                player.sendMessage("!!!!!!!!XXXXX §cDu bist bereits in einem Team Skill, sneake um diesen zu verlassen!");
                                return;
                            }
                        }
                    }

                    if(Variable.teamSkill.get(target) == null) {
                        Player[] teamSkillPlayers = new Player[] {player};
                        Variable.teamSkill.put(target, teamSkillPlayers);
                        return;
                    }
                    for(int i=0; i < Variable.teamSkill.get(target).length; i++)
                        if(Variable.teamSkill.get(target)[i].equals(player)) return;

                    List<Player> list = Arrays.asList(Variable.teamSkill.get(target));
                    list.add(player);
                    Player[] teamSkillPlayers = (Player[]) list.toArray();
                    Variable.teamSkill.remove(target);
                    Variable.teamSkill.put(target, teamSkillPlayers);
                    return;
                }
            }

            if(player.getItemInHand().getType() == Material.STONE_SWORD && (player.getItemInHand()).getItemMeta().getDisplayName().equalsIgnoreCase("§eStun")) {
                if(Variable.skillTwo.containsKey(player))
                    if(Variable.skillTwo.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillTwo.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        event.setCancelled(true);
                        return;
                    }
                Variable.skillTwo.remove(player);
                Variable.skillTwo.put(player, System.currentTimeMillis() + (20 * 1000));

                Variable.stuned.put(target, System.currentTimeMillis() + (2 * 1000));
                return;
            }



        }

    }

}
