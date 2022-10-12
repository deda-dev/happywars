package de.deda.happywars.listeners;

import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerRespawnListener implements Listener {

    private static final ConfigManager config = new ConfigManager();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        event.setRespawnLocation(config.getLocation("Locations.Spawn.death", Variable.locConfig));

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, false, false));
        InventoryManager.openClassSelectionInv(player);
    }

}
