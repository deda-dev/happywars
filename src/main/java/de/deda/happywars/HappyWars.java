package de.deda.happywars;

import de.deda.happywars.commands.HappyWarsCommand;
import de.deda.happywars.gamestates.GameState;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.listeners.*;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.ScoreboardManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class HappyWars extends JavaPlugin {

    private static HappyWars plugin;
    private static final ConfigManager config = new ConfigManager();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        new GameStateHandler();
        GameStateHandler.setGameState(GameState.LOBBY_STATE);
        new ScoreboardManager();
        worldOptions();

        //
        // UTF-8 in der ganzen config umsetzen bzw. im ConfigManager
        //
        // Hotbar scrollen anzeigen links/rechts klick und der jeweilige Skill
        // Hotbar scrollen anzeigen wie lange bis der Skill wieder genutzt werden kann
        //
        // Player Chat (beim setup / locations setzen) dem spieler die message in der actionbar anzeigen
        //
        // Config JoinItems durability hinzufügen & fill item für team selection inventory
        //
        // Player Chat Event für IngameState machen & EndState
        // Spectator machen
        //
        // Player Move Event abchecken ob doppelt (nur mit mehreren möglich, wegen team skill)
        //
        // Ingame Klasse teleportPlayers, werden alle teleportiert??? (glaub nur einer und dann ist stuck)
        //
        // Im Block Break die kommentierte Zeile 162 ( if(checkedPlayerTeam) ) kommis weg machen
        //
        // BUG im playerSneakEvent / wenn entity != armorstand, weil ich nur den 1. aus der Liste (Liste = getNearbeEntitys) hole und nicht alle abfrage
        // Wenn Spieler teamSkill aktiviert, dann loc vom spieler speichern und wenn er sich bewegt, dann an die loc teleportieren
        //
        // BUG wenn Spieler heal pool eingesetzt hat und zu weit weg ist, dann wird der armorstand nicht gekillt
        //

        /*
        @EventHandler
        public void onTryCraftItem(PrepareItemCraftEvent event) {
            if(event.getRecipe().getResult().getType() == Material.STICK) {
                event.getInventory().setResult(new ItemStack(Material.AIR));

            }

        }

        @EventHandler
        public void onCraftItem(CraftItemEvent event) {
            if(event.getRecipe().getResult().getType() == Material.STICK) {
                event.setResult(null);

            }
        }*/

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinQuitListener(), this);
        pm.registerEvents(new PlayerChatListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new BlockPlaceListener(), this);
        pm.registerEvents(new FoodChanceListener(), this);
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new PlayerAttackListener(), this);
        pm.registerEvents(new PlayerDamageListener(), this);
        pm.registerEvents(new PlayerDropItemListener(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
        pm.registerEvents(new PlayerPickUpItemListener(), this);
        pm.registerEvents(new InventoryCloseListener(), this);
        pm.registerEvents(new PlayerShootBowListener(), this);
        pm.registerEvents(new ProjectileHitListener(), this);
        pm.registerEvents(new WeatherChangeListener(), this);
        pm.registerEvents(new PlayerItemConsumListener(), this);
        pm.registerEvents(new PlayerSneakListener(), this);
        pm.registerEvents(new PlayerDeathListener(), this);
        pm.registerEvents(new PlayerRespawnListener(), this);

        getCommand("happywars").setExecutor(new HappyWarsCommand());

        if(Variable.locFile.exists()) return;
        try {
            Variable.locFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    private void worldOptions() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getWorld(config.getString("Worlds.lobby", Variable.config)).setAutoSave(false);
        Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).setAutoSave(false);
        Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).setDifficulty(Difficulty.NORMAL);
        Bukkit.getWorld(config.getString("Worlds.lobby", Variable.config)).setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getWorld(config.getString("Worlds.lobby", Variable.config)).setWeatherDuration(0);
        Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).setWeatherDuration(0);
        Bukkit.getWorld(config.getString("Worlds.lobby", Variable.config)).setThunderDuration(0);
        Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).setThunderDuration(0);

        /*
        maybe rein machen / ist nur ingame world, müssen eh alle heal armorstand gelöscht werden & vlt auch health gate armorstand, wenn ich das so mache
        for(int i=0;i<Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).getEntities().size();i++)
            Bukkit.getWorld(config.getString("Worlds.ingame", Variable.config)).getEntities().get(i).remove();
        */
        /*
        for(int i=0;i<Bukkit.getWorld(config.getString("Worlds.lobby", Variable.config)).getEntities().size();i++)
            Bukkit.getWorld(config.getString("Worlds.lobby", Variable.config)).getEntities().get(i).remove();
         */

    }

    public static HappyWars getPlugin() {
        return plugin;
    }

}
