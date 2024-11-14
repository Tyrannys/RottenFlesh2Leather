package net.tyrannys.rottenflesh2leather;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.tyrannys.rottenflesh2leather.Message;

import static net.tyrannys.rottenflesh2leather.Message.getMessage;
import static net.tyrannys.rottenflesh2leather.Message.getMessageWithColor;


public class ReloadCommand implements CommandExecutor {
    public String permission = "You do not have permission to use this command";

    private final RottenFlesh2Leather plugin;

    public ReloadCommand(RottenFlesh2Leather plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reload")) {
            this.plugin.reloadConfig();
            sender.sendMessage(getMessageWithColor("Config Reloaded!", ChatColor.YELLOW));
            return true;
        } else {
            return false;
        }
    }
}



