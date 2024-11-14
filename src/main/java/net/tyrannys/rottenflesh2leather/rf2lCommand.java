package net.tyrannys.rottenflesh2leather;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.tyrannys.rottenflesh2leather.Message;

import static net.tyrannys.rottenflesh2leather.Message.getMessage;


public class rf2lCommand implements CommandExecutor {
    public String permission = "You do not have permission to use this command";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                if (!sender.hasPermission("rf2l")) {
                    sender.sendMessage(permission);
                    return true;
                }
                sender.sendMessage(getMessage("if you want to reload the plugin type /rf2l reload"));
                return true;
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("rf2l.reload")) {
                        sender.sendMessage(permission);
                        return true;
                    }
                    RottenFlesh2Leather.rf2l.reloadConfig();
                    sender.sendMessage(getMessage("Config Reloaded!"));
                }
                return true;
            default:
                return false;
        }
    }
}



