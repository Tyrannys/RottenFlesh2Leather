package net.tyrannys.rottenflesh2leather;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class RottenFlesh2Leather extends JavaPlugin implements Listener {
    NamespacedKey RottenLeatherFurnaceKey = new NamespacedKey(this, "rottenleatherfurnace");
    NamespacedKey RottenLeatherCraftingKey = new NamespacedKey(this, "rottenleathercrafting");
    NamespacedKey RottenLeatherBlastingKey = new NamespacedKey(this, "rottenleatherblasting");
    NamespacedKey RottenLeatherSmokerKey = new NamespacedKey(this, "rottenleathersmoker");

    public static boolean furnaceRecipe;
    public static boolean smokerRecipe;
    public static boolean blastingRecipe;
    public static boolean craftingRecipe;
    public static String craftingRecipeTop;
    public static String craftingRecipeMid;
    public static String craftingRecipeBot;
    public static int cookingTime;
    public static float exp;

    @Override
    public void onEnable() {
        ItemStack RottenLeather = new ItemStack(Material.LEATHER, 1);
        Material RottenFlesh = Material.ROTTEN_FLESH;

        RottenFlesh2Leather.cookingTime = this.getConfig().getInt("cooking-time");
        RottenFlesh2Leather.exp = this.getConfig().getInt("experience-gain");
        RottenFlesh2Leather.furnaceRecipe = this.getConfig().getBoolean("furnace-recipe");
        RottenFlesh2Leather.smokerRecipe = this.getConfig().getBoolean("smoker-recipe");
        RottenFlesh2Leather.blastingRecipe = this.getConfig().getBoolean("blasting-recipe");
        RottenFlesh2Leather.craftingRecipe = this.getConfig().getBoolean("crafting-recipe");

        RottenFlesh2Leather.craftingRecipeTop = this.getConfig().getString("crafting-recipe-top");
        RottenFlesh2Leather.craftingRecipeMid = this.getConfig().getString("crafting-recipe-mid");
        RottenFlesh2Leather.craftingRecipeBot = this.getConfig().getString("crafting-recipe-bot");
        saveDefaultConfig();

        FurnaceRecipe rottenLeatherFurnaceRecipe = new FurnaceRecipe(
                RottenLeatherFurnaceKey,
                RottenLeather,
                RottenFlesh,
                exp,
                cookingTime);

        BlastingRecipe rottenLeatherBlastingRecipe = new BlastingRecipe(
                RottenLeatherBlastingKey,
                RottenLeather,
                RottenFlesh,
                exp,
                cookingTime/2
        );

        SmokingRecipe rottenLeatherSmokerRecipe = new SmokingRecipe(
                RottenLeatherSmokerKey,
                RottenLeather,
                RottenFlesh,
                exp,
                cookingTime/2
        );

        ShapedRecipe rottenLeatherCraftingRecipe = new ShapedRecipe(
                RottenLeatherCraftingKey, RottenLeather);


        rottenLeatherCraftingRecipe.shape
                (craftingRecipeTop, craftingRecipeMid, craftingRecipeBot);
        rottenLeatherCraftingRecipe.setIngredient('X', Material.ROTTEN_FLESH);

        if (furnaceRecipe) {
            Bukkit.addRecipe(rottenLeatherFurnaceRecipe);

        }

        if (craftingRecipe) {
            Bukkit.addRecipe(rottenLeatherCraftingRecipe);
        }

        if (blastingRecipe) {
            Bukkit.addRecipe(rottenLeatherBlastingRecipe);
        }

        if (smokerRecipe) {
            Bukkit.addRecipe(rottenLeatherSmokerRecipe);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
