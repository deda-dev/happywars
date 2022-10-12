package de.deda.happywars.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemManager {

    private static final ConfigManager cm = new ConfigManager();

    public static void setJoinItems(Player player) {
        Material teamSelectionMaterial = cm.getMaterial("JoinItems.TeamSelection.material", Variable.config);
        String teamSelectionName = cm.getString("JoinItems.TeamSelection.displayname", Variable.config);
        Material startGameMaterial = cm.getMaterial("JoinItems.StartGame.material", Variable.config);
        String startGameName = cm.getString("JoinItems.StartGame.displayname", Variable.config);
        Material leaveGameMaterial = cm.getMaterial("JoinItems.LeaveGame.material", Variable.config);
        String leaveGameName = cm.getString("JoinItems.LeaveGame.displayname", Variable.config);

        player.getInventory().setItem(0, createItem(teamSelectionMaterial, teamSelectionName, 1));
        if(player.hasPermission("happywars.all") || player.hasPermission("happywars.start"))
            player.getInventory().setItem(4, createItem(startGameMaterial, startGameName, 1));

        player.getInventory().setItem(8, createItem(leaveGameMaterial, leaveGameName, 1));
    }

    public static ItemStack createItem(Material material, String name, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItemId(Material material, short damage, String name, int amount, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, (short) damage);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createAdvancedItem(Material material, String name, int amount, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createIndividuellItem(Material material, short subId, String name, int amount) {
        ItemStack item = new ItemStack(material, amount, subId);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createClassItem(Material material, short subId, String name, int amount) {
        ItemStack item = new ItemStack(material, amount, subId);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);

        return item;
    }

}
