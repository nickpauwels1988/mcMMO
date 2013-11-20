package com.gmail.nossr50.events.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.gmail.nossr50.datatypes.skills.SkillAbility;

public class SkillAbilityPercentageActivationCheckEvent extends PlayerEvent {

    private double chance;
    private SkillAbility ability;

    public SkillAbilityPercentageActivationCheckEvent(Player player, SkillAbility ability, double chance) {
        super(player);
        this.ability = ability;
        this.chance = chance;
    }

    public SkillAbility getPassiveAbility() {
        return ability;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = Math.min(1D, chance);
    }

    public void setSuccessful(boolean success) {
        this.chance = success ? 1.0D : 0D;
    }

    /** Rest of file is required boilerplate for custom events **/
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
