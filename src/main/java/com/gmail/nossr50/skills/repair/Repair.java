package com.gmail.nossr50.skills.repair;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.config.Config;

public class Repair {
    public static int    repairMasteryMaxBonusLevel = AdvancedConfig.getInstance().getRepairMasteryMaxLevel();
    public static double repairMasteryMaxBonus      = AdvancedConfig.getInstance().getRepairMasteryMaxBonus();

    public static int    superRepairMaxBonusLevel = AdvancedConfig.getInstance().getSuperRepairMaxLevel();
    public static double superRepairMaxChance     = AdvancedConfig.getInstance().getSuperRepairChanceMax();

    public static Material anvilMaterial = Config.getInstance().getRepairAnvilMaterial();
    public static boolean  anvilMessagesEnabled = Config.getInstance().getRepairAnvilMessagesEnabled();

    public static int getRepairAndSalvageQuantities(ItemStack item) {
        return getRepairAndSalvageQuantities(item, null, (byte) -1);
    }

    public static int getRepairAndSalvageQuantities(ItemStack item, Material repairMaterial, byte repairMetadata) {
        int quantity = 0;
        MaterialData repairData = repairMaterial != null ? new MaterialData(repairMaterial, repairMetadata) : null;
        Recipe recipe = mcMMO.p.getServer().getRecipesFor(item).get(0);

        if (recipe instanceof ShapelessRecipe) {
            for (ItemStack ingredient : ((ShapelessRecipe) recipe).getIngredientList()) {
                if (ingredient != null && (repairMaterial == null || ingredient.getType() == repairMaterial) && (repairMetadata == -1 || ingredient.getData().equals(repairData))) {
                    quantity += ingredient.getAmount();
                }
            }
        }
        else if (recipe instanceof ShapedRecipe) {
            for (ItemStack ingredient : ((ShapedRecipe) recipe).getIngredientMap().values()) {
                if (ingredient != null && (repairMaterial == null || ingredient.getType() == repairMaterial) && (repairMetadata == -1 || ingredient.getData().equals(repairData))) {
                    quantity += ingredient.getAmount();
                }
            }
        }

        return quantity;
    }
}
