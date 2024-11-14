package net.tyrannys.rottenflesh2leather;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RF2LCommand implements CommandExecutor {
    private final RottenFlesh2Leather plugin;

    public RF2LCommand(RottenFlesh2Leather plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + ChatColor.YELLOW + "/rf2l <help|?|reload|fullreload>");
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "help", "?":
                    sender.sendMessage(ChatColor.GREEN + "Available commands:");
                    sender.sendMessage(ChatColor.AQUA + "/rf2l help" + ChatColor.YELLOW + " - Displays the help message.");
                    sender.sendMessage(ChatColor.AQUA + "/rf2l ?" + ChatColor.YELLOW + " - Alias for " + ChatColor.WHITE + "/rf2l help.");
                    sender.sendMessage(ChatColor.AQUA + "/rf2l reload" + ChatColor.YELLOW + " - Reloads this plugin");
                    break;
                case "reload":
                    PluginReloadCommand pluginReloadCommand = new PluginReloadCommand(this.plugin);
                    pluginReloadCommand.onCommand(sender, command, label, args);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown Command");
            }
        }

        return true;
    }

}
