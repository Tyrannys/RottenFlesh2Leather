package net.tyrannys.rottenflesh2leather;

import org.bukkit.ChatColor;

public class Message {
    private static final String prefix = ( "[" +
            ChatColor.GREEN + "R" + ChatColor.RESET +
            ChatColor.DARK_GREEN + "F" + ChatColor.RESET +
            ChatColor.BOLD + "2" + ChatColor.RESET +
            ChatColor.YELLOW + "L" + ChatColor.RESET +
            "] ");

    public static String getMessage(String message) {


        return prefix + message;
    }

    public static String getMessageWithColor(String message, String chatColor) {

        return prefix + (chatColor + message + ChatColor.RESET );
    }

}
