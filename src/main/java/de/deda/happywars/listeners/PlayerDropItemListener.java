package de.deda.happywars.listeners;

import de.deda.happywars.utils.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if(Variable.build.contains(event.getPlayer())) return;
        if(!Variable.canDrop) event.setCancelled(true);

    }

}
