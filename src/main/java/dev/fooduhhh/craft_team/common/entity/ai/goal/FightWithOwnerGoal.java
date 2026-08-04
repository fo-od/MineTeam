package dev.fooduhhh.craft_team.common.entity.ai.goal;

import dev.fooduhhh.craft_team.common.mixed.MobMixed;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class FightWithOwnerGoal extends TargetGoal {
    private final Mob tameLivingEntity;
    private LivingEntity ownerLastAttacked;
    private long timestamp;

    public FightWithOwnerGoal(Mob tameLivingEntity) {
        super(tameLivingEntity, false);
        this.tameLivingEntity = tameLivingEntity;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        // check if mob can attack its owner's last victim
        if (this.tameLivingEntity instanceof MobMixed mobMixed) {
            ServerPlayer owner = mobMixed.craftTeam$getOwner();
            if (owner == null) return false;
            this.ownerLastAttacked = owner.getLastHurtMob();
            long timestamp = owner.getLastHurtMobTimestamp();

            return this.mob.getPersistentData().getInt("goal") == 0 || this.mob.getPersistentData().getInt("goal") == 3 && timestamp != this.timestamp && this.canAttack(this.ownerLastAttacked, TargetingConditions.DEFAULT) &&
                    mobMixed.craftTeam$wantsToAttack(this.ownerLastAttacked, this.ownerLastAttacked.getTeam(), mobMixed.craftTeam$getOwnerTeam());
        }
        return false;
    }

    public void start() {
        this.mob.setTarget(this.ownerLastAttacked);

        // update timestamp
        if (this.tameLivingEntity instanceof MobMixed mobMixed) {
            ServerPlayer owner = mobMixed.craftTeam$getOwner();
            if (owner == null) {
                super.start();
                return;
            }
            this.timestamp = owner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}
