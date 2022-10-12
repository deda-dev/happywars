package de.deda.happywars.listeners;

import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if(Variable.build.contains(player)) return;
        if(!Variable.canDamage) event.setCancelled(true);

        if(GameStateHandler.getCurrentState() instanceof IngameState) {
          //  if(player.getHealth() < 20 && player.hasPotionEffect(PotionEffectType.ABSORPTION))
          //      player.removePotionEffect(PotionEffectType.ABSORPTION);
            /*
            if(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                if(player.getScoreboard().getEntryTeam(player.getName()) == null) return;

                return;
            }
             */

            if(event.getCause() == EntityDamageEvent.DamageCause.FALL)
                event.setCancelled(true);
        }
    }

}
