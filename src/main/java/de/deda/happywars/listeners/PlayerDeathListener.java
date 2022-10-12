package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setKeepInventory(true);
        event.setDroppedExp(0);
        //
        // Aus config holen
        //
        //event.setDeathMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(HappyWars.getPlugin(), 5);
        Variable.classSelection.remove(player);
        Variable.classSelection.add(player);
        Variable.death.add(player);
        Variable.warriorClass.remove(player);
        Variable.archerClass.remove(player);
        Variable.supporterClass.remove(player);
    }

}
