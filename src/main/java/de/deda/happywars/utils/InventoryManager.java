package de.deda.happywars.utils;

import de.deda.happywars.gamestates.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private static Inventory teamSelectionInv, classSelectionInv, spawnSelectionInv;
    private static ItemStack team1Item, team2Item, warriorClassItem, archerClassItem, supporterClassItem;
    private static String teamSelectionInvTitle, classSelectionInvTitle, spawnSelectionInvTitle;
    private static final ConfigManager config = new ConfigManager();

    public static void openTeamSelectionInv(Player player) {
        teamSelectionInvTitle = config.getString("JoinItems.TeamSelection.Inventory.title", Variable.config);
        teamSelectionInv = Bukkit.createInventory(null, InventoryType.HOPPER, teamSelectionInvTitle);

        Material team1Material = config.getMaterial("JoinItems.TeamSelection.Inventory.Items.team1.material", Variable.config);
        Short team1Damage = config.getShort("JoinItems.TeamSelection.Inventory.Items.team1.durability", Variable.config);
        String team1Displayname = config.getString("JoinItems.TeamSelection.Inventory.Items.team1.displayname", Variable.config);
        List<String> team1Lore = new ArrayList<>();
        team1Lore.add("§7"+ScoreboardManager.getTeamOne().getSize()+"/"+LobbyState.MAX_PLAYERS / 2);
        team1Item = ItemManager.createItemId(team1Material, team1Damage, team1Displayname, 1, team1Lore);

        Material team2Material = config.getMaterial("JoinItems.TeamSelection.Inventory.Items.team2.material", Variable.config);
        Short team2Damage = config.getShort("JoinItems.TeamSelection.Inventory.Items.team2.durability", Variable.config);
        String team2Displayname = config.getString("JoinItems.TeamSelection.Inventory.Items.team2.displayname", Variable.config);
        List<String> team2Lore = new ArrayList<>();
        team2Lore.add("§7"+ScoreboardManager.getTeamTwo().getSize()+"/"+LobbyState.MAX_PLAYERS / 2);
        team2Item = ItemManager.createItemId(team2Material, team2Damage, team2Displayname, 1, team2Lore);

        teamSelectionInv.setItem(1, team1Item);
        teamSelectionInv.setItem(3, team2Item);

        player.openInventory(teamSelectionInv);
    }

    public static void openClassSelectionInv(Player player) {
        classSelectionInvTitle = config.getString("Inventory.classSelection.title", Variable.config);
        classSelectionInv = Bukkit.createInventory(null, InventoryType.HOPPER, classSelectionInvTitle);

        Material fillItemMaterial = config.getMaterial("Inventory.classSelection.FillItem.material", Variable.config);
        Short fillItemDamage = config.getShort("Inventory.classSelection.FillItem.durability", Variable.config);
        String fillItemDisplayname = config.getString("Inventory.classSelection.FillItem.displayname", Variable.config);

        ItemStack fillItem = ItemManager.createItemId(fillItemMaterial, fillItemDamage, fillItemDisplayname, 1, null);

        Material warriorClassMaterial = config.getMaterial("Inventory.classSelection.WarriorItem.material", Variable.config);
        Short warriorClassDamage = config.getShort("Inventory.classSelection.WarriorItem.durability", Variable.config);
        String warriorClassDisplayname = config.getString("Inventory.classSelection.WarriorItem.displayname", Variable.config);
        warriorClassItem = ItemManager.createItemId(warriorClassMaterial, warriorClassDamage, warriorClassDisplayname, 1, null);

        Material archerClassMaterial = config.getMaterial("Inventory.classSelection.ArcherItem.material", Variable.config);
        Short archerClassDamage = config.getShort("Inventory.classSelection.ArcherItem.durability", Variable.config);
        String archerClassDisplayname = config.getString("Inventory.classSelection.ArcherItem.displayname", Variable.config);
        archerClassItem = ItemManager.createItemId(archerClassMaterial, archerClassDamage, archerClassDisplayname, 1, null);

        Material supporterClassMaterial = config.getMaterial("Inventory.classSelection.SupporterItem.material", Variable.config);
        Short supporterClassDamage = config.getShort("Inventory.classSelection.SupporterItem.durability", Variable.config);
        String supporterClassDisplayname = config.getString("Inventory.classSelection.SupporterItem.displayname", Variable.config);
        supporterClassItem = ItemManager.createItemId(supporterClassMaterial, supporterClassDamage, supporterClassDisplayname, 1, null);

        classSelectionInv.setItem(0, fillItem);
        classSelectionInv.setItem(4, fillItem);
        classSelectionInv.setItem(1, warriorClassItem);
        classSelectionInv.setItem(2, archerClassItem);
        classSelectionInv.setItem(3, supporterClassItem);

        player.openInventory(classSelectionInv);
    }

    public static void openSpawnSelectionInv(Player player) {
        spawnSelectionInvTitle = config.getString("Inventory.spawnSelection.title", Variable.config);

        Material fillItemMaterial = config.getMaterial("Inventory.spawnSelection.FillItem.material", Variable.config);
        Short fillItemDamage = config.getShort("Inventory.spawnSelection.FillItem.durability", Variable.config);
        String fillItemDisplayname = config.getString("Inventory.spawnSelection.FillItem.displayname", Variable.config);
        ItemStack fillItem = ItemManager.createItemId(fillItemMaterial, fillItemDamage, fillItemDisplayname, 1, null);

        switch(config.getInt("RespawnPoints.amount", Variable.config)) {
            case 1:
                spawnSelectionInv = Bukkit.createInventory(null, InventoryType.HOPPER, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(7, getTeamItem(2));
                spawnSelectionInv.setItem(4, getRespawnPointItem(1));
                break;
            case 2:
                spawnSelectionInv = Bukkit.createInventory(null, 9, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(7, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(5, getRespawnPointItem(2));
                break;
            case 3:
                spawnSelectionInv = Bukkit.createInventory(null, 18, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(16, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(6, getRespawnPointItem(2));
                spawnSelectionInv.setItem(14, getRespawnPointItem(3));
                break;
            case 4:
                spawnSelectionInv = Bukkit.createInventory(null, 18, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(16, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(6, getRespawnPointItem(2));
                spawnSelectionInv.setItem(11, getRespawnPointItem(3));
                spawnSelectionInv.setItem(14, getRespawnPointItem(4));
                break;
            case 5:
                spawnSelectionInv = Bukkit.createInventory(null, 27, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(25, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(7, getRespawnPointItem(2));
                spawnSelectionInv.setItem(13, getRespawnPointItem(3));
                spawnSelectionInv.setItem(19, getRespawnPointItem(4));
                spawnSelectionInv.setItem(23, getRespawnPointItem(5));
                break;
            case 6:
                spawnSelectionInv = Bukkit.createInventory(null, 27, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(25, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(7, getRespawnPointItem(2));
                spawnSelectionInv.setItem(11, getRespawnPointItem(3));
                spawnSelectionInv.setItem(15, getRespawnPointItem(4));
                spawnSelectionInv.setItem(19, getRespawnPointItem(5));
                spawnSelectionInv.setItem(23, getRespawnPointItem(6));
                break;
            case 7:
                spawnSelectionInv = Bukkit.createInventory(null, 27, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(25, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(7, getRespawnPointItem(2));
                spawnSelectionInv.setItem(11, getRespawnPointItem(3));
                spawnSelectionInv.setItem(13, getRespawnPointItem(4));
                spawnSelectionInv.setItem(15, getRespawnPointItem(5));
                spawnSelectionInv.setItem(19, getRespawnPointItem(6));
                spawnSelectionInv.setItem(23, getRespawnPointItem(7));
                break;
            case 8:
                spawnSelectionInv = Bukkit.createInventory(null, 27, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(25, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(5, getRespawnPointItem(2));
                spawnSelectionInv.setItem(7, getRespawnPointItem(3));
                spawnSelectionInv.setItem(11, getRespawnPointItem(4));
                spawnSelectionInv.setItem(15, getRespawnPointItem(5));
                spawnSelectionInv.setItem(19, getRespawnPointItem(6));
                spawnSelectionInv.setItem(21, getRespawnPointItem(7));
                spawnSelectionInv.setItem(23, getRespawnPointItem(8));
                break;
            case 9:
                spawnSelectionInv = Bukkit.createInventory(null, 27, spawnSelectionInvTitle);
                for(int i=0;i<spawnSelectionInv.getSize();i++)
                    spawnSelectionInv.setItem(i, fillItem);

                spawnSelectionInv.setItem(1, getTeamItem(1));
                spawnSelectionInv.setItem(25, getTeamItem(2));
                spawnSelectionInv.setItem(3, getRespawnPointItem(1));
                spawnSelectionInv.setItem(5, getRespawnPointItem(2));
                spawnSelectionInv.setItem(7, getRespawnPointItem(3));
                spawnSelectionInv.setItem(11, getRespawnPointItem(4));
                spawnSelectionInv.setItem(13, getRespawnPointItem(5));
                spawnSelectionInv.setItem(15, getRespawnPointItem(6));
                spawnSelectionInv.setItem(19, getRespawnPointItem(7));
                spawnSelectionInv.setItem(21, getRespawnPointItem(8));
                spawnSelectionInv.setItem(23, getRespawnPointItem(9));
                break;
        }
        player.openInventory(spawnSelectionInv);
    }

    public static void setClassItems(Player player, int classId) {
        if(classId == 0) {
            ItemStack warriorWeapon = ItemManager.createClassItem(Material.IRON_SWORD, (short) 0, "", 1);
            ItemStack warriorHelmet = ItemManager.createClassItem(Material.CHAINMAIL_HELMET, (short) 0, "", 1);
            ItemStack warriorChestplate = ItemManager.createClassItem(Material.IRON_CHESTPLATE, (short) 0, "", 1);
            ItemStack warriorLeggings = ItemManager.createClassItem(Material.CHAINMAIL_LEGGINGS, (short) 0, "", 1);
            ItemStack warriorBoots = ItemManager.createClassItem(Material.CHAINMAIL_BOOTS, (short) 0, "", 1);
            ItemStack warriorSkillOne = ItemManager.createClassItem(Material.POTION, (short) 8259, "§aHaste", 1);
            ItemStack warriorSkillTwo = ItemManager.createClassItem(Material.STONE_SWORD, (short) 0, "§eStun", 1);
            ItemStack warriorSkillThree = ItemManager.createClassItem(Material.POTION, (short) 16450, "§cSpeed", 1);

            player.getInventory().setItem(0, warriorWeapon);
            player.getInventory().setItem(6, warriorSkillOne);
            player.getInventory().setItem(7, warriorSkillTwo);
            player.getInventory().setItem(8, warriorSkillThree);
            player.getInventory().setHelmet(warriorHelmet);
            player.getInventory().setChestplate(warriorChestplate);
            player.getInventory().setLeggings(warriorLeggings);
            player.getInventory().setBoots(warriorBoots);

        } else if(classId == 1) {
            ItemStack archerWeapon = ItemManager.createClassItem(Material.BOW, (short) 0, "", 1);
            ItemStack archerSecondWeapon = ItemManager.createClassItem(Material.WOOD_AXE, (short) 0, "", 1);
            ItemStack archerArrow = ItemManager.createClassItem(Material.ARROW, (short) 0, "", 1);
            ItemStack archerHelmet = ItemManager.createClassItem(Material.LEATHER_HELMET, (short) 0, "", 1);
            ItemStack archerChestplate = ItemManager.createClassItem(Material.LEATHER_CHESTPLATE, (short) 0, "", 1);
            ItemStack archerLeggings = ItemManager.createClassItem(Material.LEATHER_LEGGINGS, (short) 0, "", 1);
            ItemStack archerBoots = ItemManager.createClassItem(Material.LEATHER_BOOTS, (short) 0, "", 1);
            ItemStack archerSkillOne = ItemManager.createClassItem(Material.POTION, (short) 8259, "§aResistance", 1);
            ItemStack archerSkillTwo = ItemManager.createClassItem(Material.BOW, (short) 0, "§eLightning", 1);
            ItemStack archerSkillThree = ItemManager.createClassItem(Material.POTION, (short) 16457, "§cStrength", 1);

            player.getInventory().setItem(0, archerWeapon);
            player.getInventory().setItem(1, archerSecondWeapon);
            player.getInventory().setItem(9, archerArrow);
            player.getInventory().setItem(6, archerSkillOne);
            player.getInventory().setItem(7, archerSkillTwo);
            player.getInventory().setItem(8, archerSkillThree);
            player.getInventory().setHelmet(archerHelmet);
            player.getInventory().setChestplate(archerChestplate);
            player.getInventory().setLeggings(archerLeggings);
            player.getInventory().setBoots(archerBoots);

        } else if(classId == 2) {
            ItemStack supporterWeapon = ItemManager.createClassItem(Material.GOLD_SWORD, (short) 0, "", 1);
            ItemStack supporterHelmet = ItemManager.createClassItem(Material.GOLD_HELMET, (short) 0, "", 1);
            ItemStack supporterChestplate = ItemManager.createClassItem(Material.CHAINMAIL_CHESTPLATE, (short) 0, "", 1);
            ItemStack supporterLeggings = ItemManager.createClassItem(Material.GOLD_LEGGINGS, (short) 0, "", 1);
            ItemStack supporterBoots = ItemManager.createClassItem(Material.GOLD_BOOTS, (short) 0, "", 1);
            ItemStack supporterSkillOne = ItemManager.createClassItem(Material.GOLDEN_APPLE, (short) 0, "§eGolden hearts", 1);
            ItemStack supporterSkillTwo = ItemManager.createClassItem(Material.POTION, (short) 8203, "§aHeal pool", 1);
            ItemStack supporterSkillThree = ItemManager.createClassItem(Material.POTION, (short) 16449, "§cRegeneration", 1);

            player.getInventory().setItem(0, supporterWeapon);
            player.getInventory().setItem(6, supporterSkillOne);
            player.getInventory().setItem(7, supporterSkillTwo);
            player.getInventory().setItem(8, supporterSkillThree);
            player.getInventory().setHelmet(supporterHelmet);
            player.getInventory().setChestplate(supporterChestplate);
            player.getInventory().setLeggings(supporterLeggings);
            player.getInventory().setBoots(supporterBoots);
        }


    }

    public static String getRespawnPointStatus(int i) {
        switch(Variable.respawnPoints.get(i)) {
            case "neutral":
                return "neutral";
            case "team1":
                return "team1";
            case "team2":
                return "team2";
        }
        return "neutral";
    }

    public static ItemStack getRespawnPointItem(int respawnPointNumber) {
        String respawnPointName = config.getString("RespawnPoints."+respawnPointNumber+".name", Variable.config);

        Material respawnPoint1ItemMaterial = config.getMaterial("Inventory.spawnSelection.respawnPoints."+getRespawnPointStatus(respawnPointNumber)+".material", Variable.config);
        Short respawnPoint1ItemDamage = config.getShort("Inventory.spawnSelection.respawnPoints."+getRespawnPointStatus(respawnPointNumber)+".durability", Variable.config);
        String respawnPoint1ItemDisplayname = config.getRespawnPointString("Inventory.spawnSelection.respawnPoints."+getRespawnPointStatus(respawnPointNumber)+".displayname", respawnPointName, Variable.config);

        return ItemManager.createItemId(respawnPoint1ItemMaterial, respawnPoint1ItemDamage, respawnPoint1ItemDisplayname, 1, null);
    }

    public static ItemStack getTeamItem(int team) {
        if(team == 1) {
            Material team1ItemMaterial = config.getMaterial("Inventory.spawnSelection.Team1Spawn.material", Variable.config);
            Short team1ItemDamage = config.getShort("Inventory.spawnSelection.Team1Spawn.durability", Variable.config);
            String team1ItemDisplayname = config.getString("Inventory.spawnSelection.Team1Spawn.displayname", Variable.config);

            return ItemManager.createItemId(team1ItemMaterial, team1ItemDamage, team1ItemDisplayname, 1, null);
        }
        if(team == 2) {
            Material team2ItemMaterial = config.getMaterial("Inventory.spawnSelection.Team2Spawn.material", Variable.config);
            Short team2ItemDamage = config.getShort("Inventory.spawnSelection.Team2Spawn.durability", Variable.config);
            String team2ItemDisplayname = config.getString("Inventory.spawnSelection.Team2Spawn.displayname", Variable.config);

            return ItemManager.createItemId(team2ItemMaterial, team2ItemDamage, team2ItemDisplayname, 1, null);
        }
        return new ItemStack(Material.AIR);
    }

    public static Inventory getTeamSelectionInv() {
        return teamSelectionInv;
    }

    public static Inventory getClassSelectionInv() {
        return classSelectionInv;
    }

    public static Inventory getSpawnSelectionInv() {
        return spawnSelectionInv;
    }

    public static ItemStack getTeam1Item() {
        return team1Item;
    }

    public static ItemStack getTeam2Item() {
        return team2Item;
    }

    public static ItemStack getWarriorClassItem() {
        return warriorClassItem;
    }

    public static ItemStack getArcherClassItem() {
        return archerClassItem;
    }

    public static ItemStack getSupporterClassItem() {
        return supporterClassItem;
    }
}
