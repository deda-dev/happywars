package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.ItemManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerShootBowListener implements Listener {

    private static final ConfigManager config = new ConfigManager();

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if(event.getBow().getItemMeta().getDisplayName() != null && event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("§eLightning")) {
            if(Variable.skillTwo.containsKey(player))
                if(Variable.skillTwo.get(player) > System.currentTimeMillis()) {
                    long timeLeft = (Variable.skillTwo.get(player) - System.currentTimeMillis()) / 1000;
                    player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                    setArrow(player);
                    event.setCancelled(true);
                    return;
                }
            Variable.skillTwo.remove(player);
            Variable.skillTwo.put(player, System.currentTimeMillis() + (20 * 1000));
            Variable.summonLightning.add(player);
            setArrow(player);
            return;
        }
        if(config.getBoolean("BowSpamming.enabled", Variable.config)) {
            setArrow(player);
            return;
        }
        if(event.getForce() > config.getDouble("BowPower", Variable.config)) {
            setArrow(player);
            return;
        }

        player.sendMessage(config.getString("BowSpamming.message", Variable.config));
        setArrow(player);
        event.setCancelled(true);
    }

    private void setArrow(Player player) {
        ItemStack archerArrow = ItemManager.createClassItem(Material.ARROW, (short) 0, "", 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(HappyWars.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getInventory().setItem(9, archerArrow);
            }
        }, 2);
    }

}
