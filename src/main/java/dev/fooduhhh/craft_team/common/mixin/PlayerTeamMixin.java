package dev.fooduhhh.craft_team.common.mixin;

import dev.fooduhhh.craft_team.common.mixed.PlayerTeamMixed;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(PlayerTeam.class)
public class PlayerTeamMixin implements PlayerTeamMixed {

    @Unique
    @Nullable
    private LivingEntity craftTeam$lastHurtMob;

    @Unique
    @Nullable
    private PlayerTeam craftTeam$lastHurtTeam;

    @Unique
    private long craftTeam$lastHurtMobTimestamp;

    @Nullable
    @Override
    public PlayerTeam craftTeam$getLastHurtTeam() {
        return craftTeam$lastHurtTeam;
    }

    @Unique
    @Override
    public void craftTeam$setLastHurtTeam(@Nullable PlayerTeam lastHurtTeam) {
        this.craftTeam$lastHurtTeam = lastHurtTeam;
    }

    @Nullable
    @Unique
    @Override
    public LivingEntity craftTeam$getLastHurtMob() {
        return craftTeam$lastHurtMob;
    }

    @Unique
    @Override
    public long craftTeam$getLastHurtMobTimestamp() {
        return this.craftTeam$lastHurtMobTimestamp;
    }

    @Unique
    @Override
    public void craftTeam$setLastHurtMob(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.craftTeam$lastHurtMob = (LivingEntity) entity;
        } else {
            this.craftTeam$lastHurtMob = null;
        }

        this.craftTeam$lastHurtMobTimestamp = entity.level().getGameTime();
    }

}
