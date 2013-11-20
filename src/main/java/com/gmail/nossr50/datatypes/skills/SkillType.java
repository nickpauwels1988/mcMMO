package com.gmail.nossr50.datatypes.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.SkillManager;
import com.gmail.nossr50.skills.acrobatics.AcrobaticsManager;
import com.gmail.nossr50.skills.archery.ArcheryManager;
import com.gmail.nossr50.skills.axes.AxesManager;
import com.gmail.nossr50.skills.excavation.ExcavationManager;
import com.gmail.nossr50.skills.fishing.FishingManager;
import com.gmail.nossr50.skills.herbalism.HerbalismManager;
import com.gmail.nossr50.skills.mining.MiningManager;
import com.gmail.nossr50.skills.repair.RepairManager;
import com.gmail.nossr50.skills.smelting.SmeltingManager;
import com.gmail.nossr50.skills.swords.SwordsManager;
import com.gmail.nossr50.skills.taming.TamingManager;
import com.gmail.nossr50.skills.unarmed.UnarmedManager;
import com.gmail.nossr50.skills.woodcutting.WoodcuttingManager;
import com.gmail.nossr50.util.Permissions;
import com.gmail.nossr50.util.StringUtils;
import com.gmail.nossr50.util.skills.ParticleEffectUtils;

import com.google.common.collect.ImmutableList;

public enum SkillType {
    ACROBATICS(AcrobaticsManager.class, Color.WHITE, ImmutableList.of(SkillAbility.DODGE, SkillAbility.ROLL, SkillAbility.GRACEFUL_ROLL)),
    ARCHERY(ArcheryManager.class, Color.MAROON, ImmutableList.of(SkillAbility.RETRIEVE, SkillAbility.DAZE, SkillAbility.SKILL_SHOT)),
    AXES(AxesManager.class, Color.AQUA, AbilityType.SKULL_SPLITTER, ToolType.AXE, ImmutableList.of(SkillAbility.CRITICAL_HIT, SkillAbility.ARMOR_IMPACT, SkillAbility.GREATER_IMPACT)),
    EXCAVATION(ExcavationManager.class, Color.fromRGB(139, 69, 19), AbilityType.GIGA_DRILL_BREAKER, ToolType.SHOVEL, ImmutableList.of(SkillAbility.TREASURE_DROP)),
    FISHING(FishingManager.class, Color.NAVY, ImmutableList.of(SkillAbility.SHAKE, SkillAbility.ICE_FISHING)),
    HERBALISM(HerbalismManager.class, Color.GREEN, AbilityType.GREEN_TERRA, ToolType.HOE, ImmutableList.of(SkillAbility.GREEN_THUMB, SkillAbility.SHROOM_THUMB, SkillAbility.HYLIAN_LUCK, SkillAbility.GREEN_THUMB_BLOCK, SkillAbility.HERBALISM_DOUBLE_DROPS)),
    MINING(MiningManager.class, Color.GRAY, AbilityType.SUPER_BREAKER, ToolType.PICKAXE, ImmutableList.of(SkillAbility.MINING_DOUBLE_DROPS)),
    REPAIR(RepairManager.class, Color.SILVER, ImmutableList.of(SkillAbility.REPAIR, SkillAbility.SALVAGE, SkillAbility.SUPER_REPAIR)),
    SMELTING(SmeltingManager.class, Color.YELLOW, ImmutableList.of(SkillAbility.SECOND_SMELT, SkillAbility.FLUX_MINING)),
    SWORDS(SwordsManager.class, Color.fromRGB(178, 34, 34), AbilityType.SERRATED_STRIKES, ToolType.SWORD, ImmutableList.of(SkillAbility.BLEED, SkillAbility.COUNTER)),
    TAMING(TamingManager.class, Color.PURPLE, ImmutableList.of(SkillAbility.FAST_FOOD, SkillAbility.GORE, SkillAbility.CALL_OF_THE_WILD)),
    UNARMED(UnarmedManager.class, Color.BLACK, AbilityType.BERSERK, ToolType.FISTS, ImmutableList.of(SkillAbility.IRON_GRIP, SkillAbility.DEFLECT, SkillAbility.DISARM, SkillAbility.BLOCK_CRACKER)),
    WOODCUTTING(WoodcuttingManager.class, Color.OLIVE, AbilityType.TREE_FELLER, ToolType.AXE, ImmutableList.of(SkillAbility.WOODCUTTING_DOUBLE_DROPS, SkillAbility.LEAF_BLOWER));

    private Class<? extends SkillManager> managerClass;
    private Color runescapeColor;
    private AbilityType ability;
    private ToolType tool;
    private List<SkillAbility> skillAbilities;

    public static final List<String> SKILL_NAMES;

    public static final List<SkillType> CHILD_SKILLS;
    public static final List<SkillType> NON_CHILD_SKILLS;

    public static final List<SkillType> COMBAT_SKILLS = ImmutableList.of(ARCHERY, AXES, SWORDS, TAMING, UNARMED);
    public static final List<SkillType> GATHERING_SKILLS = ImmutableList.of(EXCAVATION, FISHING, HERBALISM, MINING, WOODCUTTING);
    public static final List<SkillType> MISC_SKILLS = ImmutableList.of(ACROBATICS, REPAIR, SMELTING);

    static {
        List<SkillType> childSkills = new ArrayList<SkillType>();
        List<SkillType> nonChildSkills = new ArrayList<SkillType>();
        ArrayList<String> names = new ArrayList<String>();

        for (SkillType skill : values()) {
            if (skill.isChildSkill()) {
                childSkills.add(skill);
            }
            else {
                nonChildSkills.add(skill);
            }

            names.add(skill.getName());
        }

        Collections.sort(names);
        SKILL_NAMES = ImmutableList.copyOf(names);

        CHILD_SKILLS = ImmutableList.copyOf(childSkills);
        NON_CHILD_SKILLS = ImmutableList.copyOf(nonChildSkills);
    }

    private SkillType(Class<? extends SkillManager> managerClass, Color runescapeColor, List<SkillAbility> skillAbilities) {
        this(managerClass, runescapeColor, null, null, skillAbilities);
    }

    private SkillType(Class<? extends SkillManager> managerClass, Color runescapeColor, AbilityType ability, ToolType tool, List<SkillAbility> skillAbilities) {
        this.managerClass = managerClass;
        this.runescapeColor = runescapeColor;
        this.ability = ability;
        this.tool = tool;
        this.skillAbilities = skillAbilities;
    }

    public Class<? extends SkillManager> getManagerClass() {
        return managerClass;
    }

    public AbilityType getAbility() {
        return ability;
    }

    /**
     * Get the max level of this skill.
     *
     * @return the max level of this skill
     */
    public int getMaxLevel() {
        return Config.getInstance().getLevelCap(this);
    }

    public boolean getPVPEnabled() {
        return Config.getInstance().getPVPEnabled(this);
    }

    public boolean getPVEEnabled() {
        return Config.getInstance().getPVEEnabled(this);
    }

    public boolean getDoubleDropsDisabled() {
        return Config.getInstance().getDoubleDropsDisabled(this);
    }

    public boolean getHardcoreStatLossEnabled() {
        return Config.getInstance().getHardcoreStatLossEnabled(this);
    }

    public void setHardcoreStatLossEnabled(boolean enable) {
        Config.getInstance().setHardcoreStatLossEnabled(this, enable);
    }

    public boolean getHardcoreVampirismEnabled() {
        return Config.getInstance().getHardcoreVampirismEnabled(this);
    }

    public void setHardcoreVampirismEnabled(boolean enable) {
        Config.getInstance().setHardcoreVampirismEnabled(this, enable);
    }

    public ToolType getTool() {
        return tool;
    }

    public List<SkillAbility> getSkillAbilities() {
        return skillAbilities;
    }

    public double getXpModifier() {
        return ExperienceConfig.getInstance().getFormulaSkillModifier(this);
    }

    public static SkillType getSkill(String skillName) {
        if (!Config.getInstance().getLocale().equalsIgnoreCase("en_US")) {
            for (SkillType type : values()) {
                if (skillName.equalsIgnoreCase(LocaleLoader.getString(StringUtils.getCapitalized(type.name()) + ".SkillName"))) {
                    return type;
                }
            }
        }

        for (SkillType type : values()) {
            if (type.name().equalsIgnoreCase(skillName)) {
                return type;
            }
        }

        if (!skillName.equalsIgnoreCase("all")) {
            mcMMO.p.getLogger().warning("Invalid mcMMO skill (" + skillName + ")"); //TODO: Localize
        }

        return null;
    }

    // TODO: This is a little "hacky", we probably need to add something to distinguish child skills in the enum, or to use another enum for them
    public boolean isChildSkill() {
        switch (this) {
            case SMELTING:
                return true;

            default:
                return false;
        }
    }

    public static SkillType bySkillAbility(SkillAbility skillAbility) {
        for (SkillType type : values()) {
            if (type.getSkillAbilities().contains(skillAbility)) {
                return type;
            }
        }
        return null;
    }

    public static SkillType byAbility(AbilityType ability) {
        for (SkillType type : values()) {
            if (type.getAbility() == ability) {
                return type;
            }
        }

        return null;
    }

    public String getName() {
        return Config.getInstance().getLocale().equalsIgnoreCase("en_US") ? StringUtils.getCapitalized(this.toString()) : StringUtils.getCapitalized(LocaleLoader.getString(StringUtils.getCapitalized(this.toString()) + ".SkillName"));
    }

    public boolean getPermissions(Player player) {
        return Permissions.skillEnabled(player, this);
    }

    public void celebrateLevelUp(Player player) {
        ParticleEffectUtils.fireworkParticleShower(player, runescapeColor);
    }

    public boolean shouldProcess(Entity target) {
        return (target instanceof Player || (target instanceof Tameable && ((Tameable) target).isTamed())) ? getPVPEnabled() : getPVEEnabled();
    }
}
