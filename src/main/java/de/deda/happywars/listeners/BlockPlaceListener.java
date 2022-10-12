package de.deda.happywars.listeners;

import de.deda.happywars.utils.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(Variable.build.contains(event.getPlayer())) return;
        if(!Variable.canBuild) event.setCancelled(true);
    }

}
