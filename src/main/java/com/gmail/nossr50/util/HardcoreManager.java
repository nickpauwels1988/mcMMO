package com.gmail.nossr50.util;

import org.bukkit.entity.Player;

import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.util.player.UserManager;

public final class HardcoreManager {
    private HardcoreManager() {}

    public static void invokeStatPenalty(Player player) {
        double statLossPercentage = Config.getInstance().getHardcoreDeathStatPenaltyPercentage();

        PlayerProfile playerProfile = UserManager.getPlayer(player).getProfile();
        int totalLevelsLost = 0;

        for (SkillType skillType : SkillType.NON_CHILD_SKILLS) {
            int playerSkillLevel = playerProfile.getSkillLevel(skillType);
            int playerSkillXpLevel = playerProfile.getSkillXpLevel(skillType);

            if (playerSkillLevel <= 0) {
                continue;
            }

            double statsLost = playerSkillLevel * (statLossPercentage * 0.01D);
            int levelsLost = (int) statsLost;
            int xpLost = (int) Math.floor(playerSkillXpLevel * (statsLost - levelsLost));

            totalLevelsLost += levelsLost;

            playerProfile.modifySkill(skillType, playerSkillLevel - levelsLost);
            playerProfile.removeXp(skillType, xpLost);

            // Negative checks (this feels inefficent...)
            playerSkillXpLevel = playerProfile.getSkillXpLevel(skillType);
            playerSkillLevel = playerProfile.getSkillLevel(skillType);
            while (playerSkillXpLevel < 0) {
                totalLevelsLost++;
                playerProfile.modifySkill(skillType, playerSkillLevel - 1);
                playerSkillLevel--;

                playerSkillXpLevel += playerProfile.getXpToLevel(skillType);
            }
            playerProfile.setSkillXpLevel(skillType, playerSkillXpLevel);
            if (playerSkillLevel < 0) {
                totalLevelsLost += playerSkillLevel; // rebalance total
                playerProfile.modifySkill(skillType, 0);
                playerProfile.setSkillXpLevel(skillType, 0);
            }
        }

        player.sendMessage(LocaleLoader.getString("Hardcore.DeathStatLoss.PlayerDeath", totalLevelsLost));
    }

    public static void invokeVampirism(Player killer, Player victim) {
        double vampirismStatLeechPercentage = Config.getInstance().getHardcoreVampirismStatLeechPercentage();

        PlayerProfile killerProfile = UserManager.getPlayer(killer).getProfile();
        PlayerProfile victimProfile = UserManager.getPlayer(victim).getProfile();
        int totalLevelsStolen = 0;

        for (SkillType skillType : SkillType.NON_CHILD_SKILLS) {
            int killerSkillLevel = killerProfile.getSkillLevel(skillType);
            int victimSkillLevel = victimProfile.getSkillLevel(skillType);

            if (victimSkillLevel <= 0 || victimSkillLevel < killerSkillLevel / 2) {
                continue;
            }

            int victimSkillXpLevel = victimProfile.getSkillXpLevel(skillType);

            double statsStolen = victimSkillLevel * (vampirismStatLeechPercentage * 0.01D);
            int levelsStolen = (int) statsStolen;
            int xpStolen = (int) Math.floor(victimSkillXpLevel * (statsStolen - levelsStolen));

            totalLevelsStolen += levelsStolen;

            killerProfile.modifySkill(skillType, killerSkillLevel + levelsStolen);
            killerProfile.addXp(skillType, xpStolen);

            victimProfile.modifySkill(skillType, victimSkillLevel - levelsStolen);
            victimProfile.removeXp(skillType, xpStolen);

            // Negative checks (this feels inefficent...)
            victimSkillXpLevel = victimProfile.getSkillXpLevel(skillType);
            victimSkillLevel = victimProfile.getSkillLevel(skillType);
            while (victimSkillXpLevel < 0) {
                totalLevelsStolen++;
                victimProfile.modifySkill(skillType, victimSkillLevel - 1);
                victimSkillLevel--;

                victimSkillXpLevel += victimProfile.getXpToLevel(skillType);
            }
            victimProfile.setSkillXpLevel(skillType, victimSkillXpLevel);
            if (victimSkillLevel < 0) {
                totalLevelsStolen += victimSkillLevel; // rebalance total
                victimProfile.modifySkill(skillType, 0);
                victimProfile.setSkillXpLevel(skillType, 0);
            }
        }

        if (totalLevelsStolen > 0) {
            killer.sendMessage(LocaleLoader.getString("Hardcore.Vampirism.Killer.Success", totalLevelsStolen, victim.getName()));
            victim.sendMessage(LocaleLoader.getString("Hardcore.Vampirism.Victim.Success", killer.getName(), totalLevelsStolen));
        }
        else {
            killer.sendMessage(LocaleLoader.getString("Hardcore.Vampirism.Killer.Failure", victim.getName()));
            victim.sendMessage(LocaleLoader.getString("Hardcore.Vampirism.Victim.Failure", killer.getName()));
        }
    }

    /**
     * Check if Hardcore Stat Loss is enabled for one or more skill types
     *
     * @return true if Stat Loss is enabled for one or more skill types
     */
    public static boolean isStatLossEnabled() {
        boolean enabled = false;

        for (SkillType skillType : SkillType.NON_CHILD_SKILLS) {
            if (skillType.getHardcoreStatLossEnabled()) {
                enabled = true;
                break;
            }
        }

        return enabled;
    }

    /**
     * Check if Hardcore Vampirism is enabled for one or more skill types
     *
     * @return true if Vampirism is enabled for one or more skill types
     */
    public static boolean isVampirismEnabled() {
        boolean enabled = false;

        for (SkillType skillType : SkillType.NON_CHILD_SKILLS) {
            if (skillType.getHardcoreVampirismEnabled()) {
                enabled = true;
                break;
            }
        }

        return enabled;
    }
}
