package dev.fooduhhh.craft_team.common.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;

import dev.fooduhhh.craft_team.common.mixed.MobMixed;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class FollowOwnerGoal extends Goal {
    private final Mob mob;
    @Nullable
    private ServerPlayer owner;

    private final double speedModifier;
    private final float stopDistance;

    private final PathNavigation navigation;
    private int timeToRecalcPath;

    public FollowOwnerGoal(Mob mob, double speedModifier, float stopDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.stopDistance = stopDistance;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof MobMixed mobMixed) {
            this.owner = mobMixed.craftTeam$getOwner();

            return this.mob.getPersistentData().getInt("goal") == 1 || this.mob.getPersistentData().getInt("goal") == 3 && owner != null;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getPersistentData().getInt("goal") == 1
                || this.mob.getPersistentData().getInt("goal") == 3
                && this.owner != null
                && this.mob.distanceToSqr(this.owner) > (this.stopDistance * this.stopDistance);
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
    }

    @Override
    public void tick() {
        if (this.owner == null) return;

        this.mob.getLookControl().setLookAt(this.owner, 10.0F, (float) this.mob.getMaxHeadXRot());

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);

            if (!this.mob.isLeashed() && !this.mob.isPassenger()) {
                if (this.mob.distanceToSqr(this.owner) > (this.stopDistance * this.stopDistance)) {
                    this.navigation.moveTo(this.owner, this.speedModifier);
                }
            }
        }
    }
}
