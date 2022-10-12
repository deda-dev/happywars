package de.deda.happywars.listeners;

import de.deda.happywars.utils.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickUpItemListener implements Listener {

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if(Variable.build.contains(event.getPlayer())) return;
        if(!Variable.canPickUp) event.setCancelled(true);

    }

}
