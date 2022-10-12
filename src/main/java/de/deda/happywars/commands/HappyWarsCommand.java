package de.deda.happywars.commands;


import de.deda.happywars.gamestates.GameStateHandler;
import de.deda.happywars.gamestates.LobbyState;
import de.deda.happywars.utils.ConfigManager;
import de.deda.happywars.utils.ItemManager;
import de.deda.happywars.utils.Variable;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HappyWarsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        ConfigManager cm = new ConfigManager();
        String buildPermission = cm.getString("Permissions.build", Variable.config);

        if(!player.hasPermission("happywars.all")|| !player.hasPermission("happywars.*")||
                !player.hasPermission("happywars.setup") ||
                !player.hasPermission(buildPermission)) {
            player.sendMessage(Variable.noPerm);
            return false;
        }
        if(!(args.length == 1)) {
            player.sendMessage(cm.getCmdString("WrongCommandMessage","/happywars <setup/build>", Variable.config));
            return false;
        }

        switch (args[0]) {
            case "setup":
                if(!player.hasPermission("happywars.setup")) {
                    player.sendMessage(Variable.noPerm);
                    return false;
                }
                Variable.setup.remove(player);
                Variable.setup.put(player, 1);
                player.sendMessage(Variable.prefix+" §7Go to the lobbyspawn and type \"done\" in the chat.");
                return true;
            case "build":
                if(!player.hasPermission(buildPermission)) {
                    player.sendMessage(Variable.noPerm);
                    return false;
                }
                if(Variable.build.contains(player)) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    player.sendMessage(Variable.prefix+" §7You are now §cout §7of the build mode.");
                    Variable.build.remove(player);

                    if(GameStateHandler.getCurrentState() instanceof LobbyState)
                        ItemManager.setJoinItems(player);
                    return true;
                }
                Variable.build.add(player);
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().clear();
                player.sendMessage(Variable.prefix+" §7You are now §ain §7the build mode.");
                return true;
        }

        player.sendMessage(cm.getCmdString("WrongCommandMessage","/happywars <setup/build>", Variable.config));
        return false;
    }
}
