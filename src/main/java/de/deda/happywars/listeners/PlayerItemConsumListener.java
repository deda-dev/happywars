package de.deda.happywars.listeners;

import de.deda.happywars.utils.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerItemConsumListener implements Listener {

    @EventHandler
    public void onItemConsum(PlayerItemConsumeEvent event) {
        if(Variable.build.contains(event.getPlayer())) return;
        if(!Variable.canFood) event.setCancelled(true);
    }

}
