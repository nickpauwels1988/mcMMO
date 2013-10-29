package com.gmail.nossr50.skills.repair;

import org.bukkit.Material;

import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.config.Config;

public class Repair {
    public static int    repairMasteryMaxBonusLevel = AdvancedConfig.getInstance().getRepairMasteryMaxLevel();
    public static double repairMasteryMaxBonus      = AdvancedConfig.getInstance().getRepairMasteryMaxBonus();

    public static int    superRepairMaxBonusLevel = AdvancedConfig.getInstance().getSuperRepairMaxLevel();
    public static double superRepairMaxChance     = AdvancedConfig.getInstance().getSuperRepairChanceMax();

    public static Material anvilMaterial = Config.getInstance().getRepairAnvilMaterial();
    public static boolean  anvilMessagesEnabled = Config.getInstance().getRepairAnvilMessagesEnabled();
}
