package de.deda.happywars.listeners;

import de.deda.happywars.utils.Variable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChanceListener implements Listener {

    @EventHandler
    public void onFoodChance(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if(Variable.build.contains(player)) return;
        if(!Variable.canFood) event.setCancelled(true);

    }

}
