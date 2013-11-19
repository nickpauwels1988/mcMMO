package com.gmail.nossr50.datatypes.skills;

import org.bukkit.entity.Player;

import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.util.Permissions;

public enum PassiveAbility {
    /* ACROBATICS */
    DODGE,
    GRACEFUL_ROLL,
    ROLL,

    /* ARCHERY */
    TRACK_ARROWS,
    DAZE,

    /* Axes */
    CRITICAL_HIT,
    ARMOR_IMPACT,
    GREATER_IMPACT,

    /* Herbalism */
    HERBALISM_DOUBLE_DROPS,
    GREEN_THUMB,
    SHROOM_THUMB,
    HYLIAN_LUCK,
    GREEN_THUMB_BLOCK,

    /* Mining */
    MINING_DOUBLE_DROPS,

    /* Repair */
    SUPER_REPAIR,

    /* Smelting */
    SECOND_SMELT,
    FLUX_MINING,

    /* Swords */
    BLEED,
    COUNTER,

    /* Taming */
    FAST_FOOD,
    GORE,

    /* Woodcutting */
    WOODCUTTING_DOUBLE_DROPS,

    /* Excavation */
    TREASURE_DROP,

    /* Fishing */
    SHAKE,

    /* Unarmed */
    IRON_GRIP,
    DEFLECT,
    DISARM,
    ;

    public double getMaxChance() {
        return AdvancedConfig.getInstance().getMaxChance(this);
    }

    public int getMaxLevel() {
        return AdvancedConfig.getInstance().getMaxBonusLevel(this);
    }

    public boolean hasPermission(Player player) {
        return Permissions.passiveAbilityEnabled(player, this);
    }
}
