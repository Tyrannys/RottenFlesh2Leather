package net.tyrannys.rottenflesh2leather;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PluginReloadCommand implements CommandExecutor {
    private final RottenFlesh2Leather plugin;

    public PluginReloadCommand(RottenFlesh2Leather plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rf2l.reload")) {
            sender.sendMessage(Message.getMessageWithColor("You do not have permission to use this command!", ChatColor.RED));
        } else {
            sender.sendMessage(Message.getMessageWithColor("Reloading Plugin...", ChatColor.YELLOW));
            long startTime = System.currentTimeMillis();
            this.reloadPlugin(sender, startTime);
        }
        return true;
    }

    private void reloadPlugin(CommandSender sender, long startTime) {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            this.plugin.onDisable();
            this.plugin.reloadConfig();

            RottenFlesh2Leather.cookTime = this.plugin.getConfig().getInt("cooking-time");
            RottenFlesh2Leather.expGain = this.plugin.getConfig().getFloatList("experience-gain").getFirst();
            RottenFlesh2Leather.furnaceRecipe = this.plugin.getConfig().getBoolean("furnace-recipe");
            RottenFlesh2Leather.smokerRecipe = this.plugin.getConfig().getBoolean("smoker-recipe");
            RottenFlesh2Leather.blastingRecipe = this.plugin.getConfig().getBoolean("blasting-recipe");
            RottenFlesh2Leather.craftingRecipe = this.plugin.getConfig().getBoolean("crafting-recipe");

            RottenFlesh2Leather.craftingRecipeTop = this.plugin.getConfig().getString("crafting-recipe-top");
            RottenFlesh2Leather.craftingRecipeMid = this.plugin.getConfig().getString("crafting-recipe-mid");
            RottenFlesh2Leather.craftingRecipeBot = this.plugin.getConfig().getString("crafting-recipe-bot");

            this.plugin.onEnable();
            long reloadTime = System.currentTimeMillis() - startTime;
            sender.sendMessage(Message.getMessageWithColor("Plugin successfully reloaded in " + ChatColor.YELLOW + reloadTime + "ms!", ChatColor.AQUA));
        });
    }
}
