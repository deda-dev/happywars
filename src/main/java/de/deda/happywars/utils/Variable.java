package de.deda.happywars.utils;

import de.deda.happywars.HappyWars;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Variable {

    private static final ConfigManager cm = new ConfigManager();

    public static File file = new File("plugins//HappyWars//config.yml");
    public static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    public static File locFile = new File("plugins//HappyWars//Locations.yml");
    public static YamlConfiguration locConfig = YamlConfiguration.loadConfiguration(locFile);

    public static final String prefix = cm.getString("Prefix", config),
                                noPerm = cm.getString("NoPerms", config);

    public static boolean canMove = false,
                            canAttack = false,
                            canDamage = false,
                            canBuild = false,
                            canBreak = false,
                            canInvClick = false,
                            canDrop = false,
                            canFood = false,
                            canPickUp = false;

    public static HashMap<Integer, String> respawnPoints = new HashMap<>();

    public static int numberOfRespawnPoints = 1;
    public static int numberOfChangedBlocks = 1;

    public static HashMap<Player, Player[]> teamSkill = new HashMap<>();

    public static HashMap<Player, Integer> setup = new HashMap<>(),
                                            healingStationDurability = new HashMap<>();
    public static HashMap<Player, Long> skillOne = new HashMap<>(),
                                        skillTwo = new HashMap<>(),
                                        skillThree = new HashMap<>(),
                                        stuned = new HashMap<>();

    public static ArrayList<Player> build = new ArrayList<>(),
                                    playing = new ArrayList<>(),
                                    death = new ArrayList<>(),
                                    spectating = new ArrayList<>(),
                                    classSelection = new ArrayList<>(),
                                    spawnSelection = new ArrayList<>(),
                                    summonLightning = new ArrayList<>(),
                                    warriorClass = new ArrayList<>(),
                                    archerClass = new ArrayList<>(),
                                    supporterClass = new ArrayList<>();
}
