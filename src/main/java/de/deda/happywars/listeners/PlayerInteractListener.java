package de.deda.happywars.listeners;

import de.deda.happywars.HappyWars;
import de.deda.happywars.countdown.LobbyCountdown;
import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.IngameState;
import de.deda.happywars.gamestates.LobbyState;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.InventoryManager;
import de.deda.happywars.utils.ItemManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class PlayerInteractListener implements Listener {

    private static final ConfigManager cm = new ConfigManager();
    private static int taskID;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(Variable.build.contains(event.getPlayer())) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(player.getInventory().getItemInHand() == null) return;
        if(event.getMaterial() == null || event.getItem() == null) return;

        Material teamSelectionMaterial = cm.getMaterial("JoinItems.TeamSelection.material", Variable.config);
        String teamSelectionDisplayname = cm.getString("JoinItems.TeamSelection.displayname", Variable.config);
        Material startGameMaterial = cm.getMaterial("JoinItems.StartGame.material", Variable.config);
        String startGameDisplayname = cm.getString("JoinItems.StartGame.displayname", Variable.config);
        Material leaveGameMaterial = cm.getMaterial("JoinItems.LeaveGame.material", Variable.config);
        String leaveGameDisplayname = cm.getString("JoinItems.LeaveGame.displayname", Variable.config);

        if(GameStateHandler.getCurrentState() instanceof LobbyState) {
             if(event.getMaterial() == teamSelectionMaterial && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(teamSelectionDisplayname)) {
                InventoryManager.openTeamSelectionInv(player);
            } else if(event.getMaterial() == startGameMaterial && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(startGameDisplayname)) {
                LobbyState lobbyState = (LobbyState) GameStateHandler.getCurrentState();
                LobbyCountdown lobbyCountdown = new LobbyCountdown();
                if (lobbyState.getCountdown().getSeconds() <= 10) return;
                lobbyCountdown.startDecreasedCountdown = true;

                int seconds = cm.getInt("DecreasedSeconds", Variable.config);
                lobbyState.getCountdown().setSeconds(seconds);

                Bukkit.broadcastMessage(cm.getString("DecreasedMessage", Variable.config));
            } else if(event.getMaterial() == leaveGameMaterial && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(leaveGameDisplayname)) {
                if(cm.getBoolean("JoinItems.LeaveGame.Connection.Command.enabled", Variable.config)) {
                    player.performCommand(cm.getString("JoinItems.LeaveGame.Connection.Command.commandType", Variable.config));
                    return;
                }
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                DataOutputStream output = new DataOutputStream(byteOutput);
                String lobbyServer = cm.getString("JoinItems.LeaveGame.Connection.lobbyServer", Variable.config);
                try {
                    output.writeUTF("Connect");
                    output.writeUTF(lobbyServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendPluginMessage(HappyWars.getPlugin(), "BungeeCord", byteOutput.toByteArray());
            }
        } else if(GameStateHandler.getCurrentState() instanceof IngameState) {
            if(event.getItem().getItemMeta().getDisplayName() == null) return;

            if(event.getMaterial() == Material.POTION && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aHaste")) {
                event.setCancelled(true);
                if(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) return;

                if(Variable.skillOne.containsKey(player))
                    if(Variable.skillOne.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillOne.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                Variable.skillOne.remove(player);
                Variable.skillOne.put(player, System.currentTimeMillis() + (10 * 1000));
                if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);

                player.setGameMode(GameMode.ADVENTURE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*6, 1, false, false));
                return;

            } else if(event.getMaterial() == Material.POTION && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cSpeed")) {
                event.setCancelled(true);
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), ItemManager.createClassItem(Material.POTION, (short) 16450, "§cSpeed", 1));

                if(Variable.teamSkill.containsKey(player)) {
                    if(Variable.teamSkill.get(player) == null) {
                        player.sendMessage("!!!!!XXXXXXXX §7Es sind noch keine Spieler dem Team Skill beigetreten.");
                        return;
                    }
                    for(int i = 0; i < Variable.teamSkill.get(player).length; i++) {
                        Player teamSkillPlayers = Variable.teamSkill.get(player)[i];
                        teamSkillPlayers.setWalkSpeed((float) 0.2);
                        teamSkillPlayers.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1, false, false));
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1, false, false));
                    player.setWalkSpeed((float) 0.2);

                    Variable.teamSkill.remove(player);
                    Variable.skillThree.remove(player);
                    Variable.skillThree.put(player, System.currentTimeMillis() + (45 * 1000));

                    ArmorStand stand = (ArmorStand) player.getNearbyEntities(1.5,2.5,1.5).get(0);
                    if(stand.getCustomName().equalsIgnoreCase("!!!!!XXXXXXXX §aHit me"))
                        if(stand.hasPermission(player.getName()))
                            stand.remove();
                    return;
                }
                if(Variable.skillThree.containsKey(player))
                    if(Variable.skillThree.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillThree.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                if(player.getLocation().add(0, -0.1, 0).getBlock().getType() == Material.AIR) {
                    player.sendMessage("!!!!!XXXXXXXX §cYou are not on ground!");
                    return;
                }

                Variable.teamSkill.put(player, null);
                player.setWalkSpeed(0);

                Bukkit.getScheduler().runTaskLater(HappyWars.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 2,0), EntityType.ARMOR_STAND);
                        stand.setCustomName("!!!!!XXXXXXXX §aHit me");
                        stand.addAttachment(HappyWars.getPlugin(), player.getName(), true);
                        stand.setCustomNameVisible(true);
                        stand.setVisible(false);
                        stand.setMarker(true);
                        stand.setGravity(false);
                        stand.setBasePlate(false);
                        stand.setArms(false);
                        stand.setCanPickupItems(false);
                        stand.setMaxHealth(2048);
                        stand.setHealth(2048);
                    }
                }, 5);

            } else if(event.getMaterial() == Material.POTION && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aResistance")) {
                event.setCancelled(true);
                if(Variable.skillOne.containsKey(player))
                    if(Variable.skillOne.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillOne.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                Variable.skillOne.remove(player);
                Variable.skillOne.put(player, System.currentTimeMillis() + (10 * 1000));
                if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*9, 0, false, false));
                return;

            } else if(event.getMaterial() == Material.POTION && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cStrength")) {
                event.setCancelled(true);
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), ItemManager.createClassItem(Material.POTION, (short) 16457, "§cStrength", 1));

                if(Variable.teamSkill.containsKey(player)) {
                    if(Variable.teamSkill.get(player) == null) {
                        player.sendMessage("!!!!!XXXXXXXX §7Es sind noch keine Spieler dem Team Skill beigetreten.");
                        return;
                    }
                    for(int i = 0; i < Variable.teamSkill.get(player).length; i++) {
                        Player teamSkillPlayers = Variable.teamSkill.get(player)[i];
                        teamSkillPlayers.setWalkSpeed((float) 0.2);
                        teamSkillPlayers.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 1, false, false));
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 1, false, false));
                    player.setWalkSpeed((float) 0.2);

                    Variable.teamSkill.remove(player);
                    Variable.skillThree.remove(player);
                    Variable.skillThree.put(player, System.currentTimeMillis() + (45 * 1000));

                    ArmorStand stand = (ArmorStand) player.getNearbyEntities(1.5,2.5,1.5).get(0);
                    if(stand.getCustomName().equalsIgnoreCase("!!!!!XXXXXXXX §aHit me"))
                        if(stand.hasPermission(player.getName()))
                            stand.remove();
                    return;
                }
                if(Variable.skillThree.containsKey(player))
                    if(Variable.skillThree.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillThree.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                if(player.getLocation().add(0, -0.1, 0).getBlock().getType() == Material.AIR) {
                    player.sendMessage("!!!!!XXXXXXXX §cYou are not on ground!");
                    return;
                }

                Variable.teamSkill.put(player, null);
                player.setWalkSpeed(0);

                Bukkit.getScheduler().runTaskLater(HappyWars.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 2,0), EntityType.ARMOR_STAND);
                        stand.setCustomName("!!!!!XXXXXXXX §aHit me");
                        stand.addAttachment(HappyWars.getPlugin(), player.getName(), true);
                        stand.setCustomNameVisible(true);
                        stand.setVisible(false);
                        stand.setMarker(true);
                        stand.setGravity(false);
                        stand.setBasePlate(false);
                        stand.setArms(false);
                        stand.setCanPickupItems(false);
                        stand.setMaxHealth(2048);
                        stand.setHealth(2048);
                    }
                }, 5);

            } else if(event.getMaterial() == Material.GOLDEN_APPLE && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eGolden hearts")) {
                event.setCancelled(true);
                if(Variable.skillOne.containsKey(player))
                    if(Variable.skillOne.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillOne.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                if(player.hasPotionEffect(PotionEffectType.ABSORPTION))
                    player.removePotionEffect(PotionEffectType.ABSORPTION);

                Variable.skillOne.remove(player);
                Variable.skillOne.put(player, System.currentTimeMillis() + (10 * 1000));

                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 99999, 0, false, false));
                return;

            } else if(event.getMaterial() == Material.POTION && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aHeal pool")) {
                //
                // ACHTUNG das ist der HEALING POOL und kein team skill, spieler kann sich bewegen!
                // kommt zu fehler wenn mehrere Spieler nutzen (testen) hab nur 1 acc
                //
                event.setCancelled(true);
                if(Variable.skillTwo.containsKey(player))
                    if(Variable.skillTwo.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillTwo.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                World world = player.getWorld();
                Location location = player.getLocation();
                ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

                //world.playEffect(location, Effect.HAPPY_VILLAGER, 5, 2);

                stand.setCustomName("§aHealing "+player.getScoreboard().getEntryTeam(player.getName()).getName());
                stand.setCustomNameVisible(true);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setBasePlate(false);
                stand.setArms(false);
                stand.setCanPickupItems(false);
                stand.setMaxHealth(2048);
                stand.setHealth(2048);

                Variable.skillTwo.remove(player);
                Variable.skillTwo.put(player, System.currentTimeMillis() + (35 * 1000));
                HashMap<Integer, Integer> schedulers = new HashMap<>();

                if(!Variable.healingStationDurability.containsKey(player))
                    Variable.healingStationDurability.put(player, 3);

                taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HappyWars.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        schedulers.put(stand.getEntityId(), taskID);
                        if(!Variable.healingStationDurability.containsKey(player)) return;
                        int durability = Variable.healingStationDurability.get(player);

                        durability--;
                        Variable.healingStationDurability.remove(player);
                        Variable.healingStationDurability.put(player, durability);
                        for(Entity entity : stand.getNearbyEntities(2.5, 1.5, 2.5)) {
                            if(!(entity instanceof Player)) return;
                            Player target = (Player) entity;

                            if(!player.getScoreboard().getEntryTeam(player.getName()).equals(target.getScoreboard().getEntryTeam(target.getName())))
                                return;
                            if(target.getHealth() >= 15) {
                                target.setHealth(20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                            } else {
                                target.setHealth(target.getHealth() + 5);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                            }
                            if(durability<=0) {
                                if(!schedulers.containsKey(stand.getEntityId())) return;
                                Bukkit.getScheduler().cancelTask(schedulers.get(stand.getEntityId()));
                                schedulers.remove(stand.getEntityId());
                                stand.remove();
                                Variable.healingStationDurability.remove(player);
                            }
                        }
                    }
                }, 20*3, 20*5);
                //
                // kommt zu fehler wenn mehrere Spieler nutzen (testen) hab nur 1 acc
                //

            } else if(event.getMaterial() == Material.POTION && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRegeneration")) {
                event.setCancelled(true);
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), ItemManager.createClassItem(Material.POTION, (short) 16449, "§cRegeneration", 1));

                if(Variable.teamSkill.containsKey(player)) {
                    if(Variable.teamSkill.get(player) == null) {
                        player.sendMessage("!!!!!XXXXXXXX §7Es sind noch keine Spieler dem Team Skill beigetreten.");
                        return;
                    }
                    for(int i = 0; i < Variable.teamSkill.get(player).length; i++) {
                        Player teamSkillPlayers = Variable.teamSkill.get(player)[i];
                        teamSkillPlayers.setWalkSpeed((float) 0.2);
                        teamSkillPlayers.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 1, false, false));
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 1, false, false));
                    player.setWalkSpeed((float) 0.2);

                    Variable.teamSkill.remove(player);
                    Variable.skillThree.remove(player);
                    Variable.skillThree.put(player, System.currentTimeMillis() + (45 * 1000));

                    ArmorStand stand = (ArmorStand) player.getNearbyEntities(1.5,2.5,1.5).get(0);
                    if(stand.getCustomName().equalsIgnoreCase("!!!!!XXXXXXXX §aHit me"))
                        if(stand.hasPermission(player.getName()))
                            stand.remove();
                    return;
                }
                if(Variable.skillThree.containsKey(player))
                    if(Variable.skillThree.get(player) > System.currentTimeMillis()) {
                        long timeLeft = (Variable.skillThree.get(player) - System.currentTimeMillis()) / 1000;
                        player.sendMessage("!!!!!XXXXXXXX §7Noch §c" + timeLeft + " §7Sekunde/n bis der Skill wieder benutzt werden kann.");
                        return;
                    }
                if(player.getLocation().add(0, -0.1, 0).getBlock().getType() == Material.AIR) {
                    player.sendMessage("!!!!!XXXXXXXX §cYou are not on ground!");
                    return;
                }

                Variable.teamSkill.put(player, null);
                player.setWalkSpeed(0);

                Bukkit.getScheduler().runTaskLater(HappyWars.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 2,0), EntityType.ARMOR_STAND);
                        stand.setCustomName("!!!!!XXXXXXXX §aHit me");
                        stand.addAttachment(HappyWars.getPlugin(), player.getName(), true);
                        stand.setCustomNameVisible(true);
                        stand.setVisible(false);
                        stand.setMarker(true);
                        stand.setGravity(false);
                        stand.setBasePlate(false);
                        stand.setArms(false);
                        stand.setCanPickupItems(false);
                        stand.setMaxHealth(2048);
                        stand.setHealth(2048);
                    }
                }, 5);
            }
        }


    }

}
