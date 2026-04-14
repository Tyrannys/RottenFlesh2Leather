package net.tyrannys.rottenflesh2leather;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class RottenFlesh2Leather extends JavaPlugin implements Listener {
    private static final String ROOT_COMMAND = "rottenflesh2leather";
    private static final String RELOAD_SUBCOMMAND = "reload";
    private static final String RELOAD_PERMISSION = "rottenflesh2leather.reload";
    private static final int BSTATS_PLUGIN_ID = 23949;

    private NamespacedKey rottenLeatherFurnaceKey;
    private NamespacedKey rottenLeatherCraftingKey;
    private NamespacedKey rottenLeatherBlastingKey;
    private NamespacedKey rottenLeatherSmokerKey;
    private PluginSettings loadedSettings;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeBstats();

        rottenLeatherFurnaceKey = new NamespacedKey(this, "rottenleatherfurnace");
        rottenLeatherCraftingKey = new NamespacedKey(this, "rottenleathercrafting");
        rottenLeatherBlastingKey = new NamespacedKey(this, "rottenleatherblasting");
        rottenLeatherSmokerKey = new NamespacedKey(this, "rottenleathersmoker");

        loadedSettings = readSettings(getConfig());
        reconcileRecipes(null, loadedSettings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(ROOT_COMMAND)) {
            return false;
        }

        if (args.length != 1 || !args[0].equalsIgnoreCase(RELOAD_SUBCOMMAND)) {
            sender.sendMessage("Usage: /" + label + " reload");
            return true;
        }

        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        final PluginSettings previousSettings = loadedSettings;
        reloadConfig();
        final PluginSettings newSettings = readSettings(getConfig());
        final int changedRecipeCount = reconcileRecipes(previousSettings, newSettings);
        loadedSettings = newSettings;

        sender.sendMessage("RottenFlesh2Leather config reloaded. Updated recipes: " + changedRecipeCount + ".");
        return true;
    }

    private void initializeBstats() {
        final FileConfiguration config = getConfig();
        final boolean bstatsEnabled = config.getBoolean("bstats-enabled", true);
        if (!bstatsEnabled) {
            return;
        }

        new Metrics(this, BSTATS_PLUGIN_ID);
    }

    private PluginSettings readSettings(FileConfiguration config) {
        return new PluginSettings(
                config.getInt("cooking-time"),
                (float) config.getDouble("experience-gain"),
                config.getBoolean("furnace-recipe"),
                config.getBoolean("smoker-recipe"),
                config.getBoolean("blasting-recipe"),
                config.getBoolean("crafting-recipe"),
                config.getString("crafting-recipe-top", "XXX"),
                config.getString("crafting-recipe-mid", "XXX"),
                config.getString("crafting-recipe-bot", "XXX")
        );
    }

    private int reconcileRecipes(PluginSettings oldSettings, PluginSettings newSettings) {
        int changedRecipeCount = 0;

        final ItemStack rottenLeather = new ItemStack(Material.LEATHER, 1);
        final Material rottenFlesh = Material.ROTTEN_FLESH;

        if (didFurnaceChange(oldSettings, newSettings)) {
            Bukkit.removeRecipe(rottenLeatherFurnaceKey);
            if (newSettings.furnaceRecipeEnabled) {
                final FurnaceRecipe rottenLeatherFurnaceRecipe = new FurnaceRecipe(
                        rottenLeatherFurnaceKey,
                        rottenLeather,
                        rottenFlesh,
                        newSettings.exp,
                        newSettings.cookingTime
                );
                Bukkit.addRecipe(rottenLeatherFurnaceRecipe);
            }
            changedRecipeCount++;
        }

        if (didCraftingChange(oldSettings, newSettings)) {
            Bukkit.removeRecipe(rottenLeatherCraftingKey);
            if (newSettings.craftingRecipeEnabled) {
                final ShapedRecipe rottenLeatherCraftingRecipe = new ShapedRecipe(rottenLeatherCraftingKey, rottenLeather);
                rottenLeatherCraftingRecipe.shape(
                        newSettings.craftingRecipeTop,
                        newSettings.craftingRecipeMid,
                        newSettings.craftingRecipeBot
                );
                rottenLeatherCraftingRecipe.setIngredient('X', rottenFlesh);
                Bukkit.addRecipe(rottenLeatherCraftingRecipe);
            }
            changedRecipeCount++;
        }

        if (didBlastingChange(oldSettings, newSettings)) {
            Bukkit.removeRecipe(rottenLeatherBlastingKey);
            if (newSettings.blastingRecipeEnabled) {
                final BlastingRecipe rottenLeatherBlastingRecipe = new BlastingRecipe(
                        rottenLeatherBlastingKey,
                        rottenLeather,
                        rottenFlesh,
                        newSettings.exp,
                        newSettings.cookingTime / 2
                );
                Bukkit.addRecipe(rottenLeatherBlastingRecipe);
            }
            changedRecipeCount++;
        }

        if (didSmokerChange(oldSettings, newSettings)) {
            Bukkit.removeRecipe(rottenLeatherSmokerKey);
            if (newSettings.smokerRecipeEnabled) {
                final SmokingRecipe rottenLeatherSmokerRecipe = new SmokingRecipe(
                        rottenLeatherSmokerKey,
                        rottenLeather,
                        rottenFlesh,
                        newSettings.exp,
                        newSettings.cookingTime / 2
                );
                Bukkit.addRecipe(rottenLeatherSmokerRecipe);
            }
            changedRecipeCount++;
        }

        return changedRecipeCount;
    }

    private boolean didFurnaceChange(PluginSettings oldSettings, PluginSettings newSettings) {
        if (oldSettings == null) {
            return newSettings.furnaceRecipeEnabled;
        }
        return oldSettings.furnaceRecipeEnabled != newSettings.furnaceRecipeEnabled
                || oldSettings.cookingTime != newSettings.cookingTime
                || Float.compare(oldSettings.exp, newSettings.exp) != 0;
    }

    private boolean didCraftingChange(PluginSettings oldSettings, PluginSettings newSettings) {
        if (oldSettings == null) {
            return newSettings.craftingRecipeEnabled;
        }
        return oldSettings.craftingRecipeEnabled != newSettings.craftingRecipeEnabled
                || !oldSettings.craftingRecipeTop.equals(newSettings.craftingRecipeTop)
                || !oldSettings.craftingRecipeMid.equals(newSettings.craftingRecipeMid)
                || !oldSettings.craftingRecipeBot.equals(newSettings.craftingRecipeBot);
    }

    private boolean didBlastingChange(PluginSettings oldSettings, PluginSettings newSettings) {
        if (oldSettings == null) {
            return newSettings.blastingRecipeEnabled;
        }
        return oldSettings.blastingRecipeEnabled != newSettings.blastingRecipeEnabled
                || oldSettings.cookingTime != newSettings.cookingTime
                || Float.compare(oldSettings.exp, newSettings.exp) != 0;
    }

    private boolean didSmokerChange(PluginSettings oldSettings, PluginSettings newSettings) {
        if (oldSettings == null) {
            return newSettings.smokerRecipeEnabled;
        }
        return oldSettings.smokerRecipeEnabled != newSettings.smokerRecipeEnabled
                || oldSettings.cookingTime != newSettings.cookingTime
                || Float.compare(oldSettings.exp, newSettings.exp) != 0;
    }

    private static final class PluginSettings {
        private final int cookingTime;
        private final float exp;
        private final boolean furnaceRecipeEnabled;
        private final boolean smokerRecipeEnabled;
        private final boolean blastingRecipeEnabled;
        private final boolean craftingRecipeEnabled;
        private final String craftingRecipeTop;
        private final String craftingRecipeMid;
        private final String craftingRecipeBot;

        private PluginSettings(
                int cookingTime,
                float exp,
                boolean furnaceRecipeEnabled,
                boolean smokerRecipeEnabled,
                boolean blastingRecipeEnabled,
                boolean craftingRecipeEnabled,
                String craftingRecipeTop,
                String craftingRecipeMid,
                String craftingRecipeBot
        ) {
            this.cookingTime = cookingTime;
            this.exp = exp;
            this.furnaceRecipeEnabled = furnaceRecipeEnabled;
            this.smokerRecipeEnabled = smokerRecipeEnabled;
            this.blastingRecipeEnabled = blastingRecipeEnabled;
            this.craftingRecipeEnabled = craftingRecipeEnabled;
            this.craftingRecipeTop = craftingRecipeTop;
            this.craftingRecipeMid = craftingRecipeMid;
            this.craftingRecipeBot = craftingRecipeBot;
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
