package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(Variable.spawnSelection.contains(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    InventoryManager.openSpawnSelectionInv(player);
                }
            }.runTaskLater(HappyWars.getPlugin(), 2);
            return;
        }
        if(Variable.classSelection.contains(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    InventoryManager.openClassSelectionInv(player);
                }
            }.runTaskLater(HappyWars.getPlugin(), 2);
            return;
        }



    }
}
