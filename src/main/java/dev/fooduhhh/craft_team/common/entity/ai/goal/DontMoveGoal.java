package dev.fooduhhh.craft_team.common.entity.ai.goal;

import dev.fooduhhh.craft_team.common.mixed.MobMixed;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DontMoveGoal extends Goal implements CommandGoal {
    private final Mob mob;

    public DontMoveGoal(Mob mob) {
        this.mob = mob;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof MobMixed mobMixed) {
            return mobMixed.craftTeam$getOwner() != null;
        }
        return false;
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }
}
