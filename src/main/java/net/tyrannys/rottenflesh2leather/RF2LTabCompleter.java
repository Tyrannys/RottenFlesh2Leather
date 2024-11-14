package net.tyrannys.rottenflesh2leather;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RF2LTabCompleter implements org.bukkit.command.TabCompleter {

    private static final List<String> COMMANDS = List.of("reload, help");

    public RF2LTabCompleter() {

    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            for(String commandOption : COMMANDS) {
                if (commandOption.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(commandOption);
                }
            }
            return completions;
        } else {
            return new ArrayList<>();
        }
    }

}
