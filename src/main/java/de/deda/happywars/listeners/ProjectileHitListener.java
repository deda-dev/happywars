package de.deda.happywars.listeners;

import de.deda.happywars.utils.Variable;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scoreboard.Team;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Player player = (Player) event.getEntity().getShooter();
        Team playerTeam = player.getScoreboard().getEntryTeam(player.getName());
        Location location = event.getEntity().getLocation();

        if(!Variable.summonLightning.contains(player)) return;
        location.getWorld().strikeLightningEffect(location);
        Variable.summonLightning.remove(player);

        for(int i=0;i<event.getEntity().getNearbyEntities(1.5,1,1.5).size();i++) {
            Entity entity = event.getEntity().getNearbyEntities(1.5, 1, 1.5).get(i);

            if(!(entity instanceof Player)) return;
            Player target = (Player) entity;

            if(playerTeam.equals(target.getScoreboard().getEntryTeam(target.getName()))) return;
            target.playEffect(EntityEffect.HURT);
            if(target.getHealth() > 7) {
                target.setHealth(target.getHealth()-7);
                return;
            }
            target.setHealth(0);
        }

        //
        // beides kombinieren z.b. player team und loc in list speichern
        //

    }

}
