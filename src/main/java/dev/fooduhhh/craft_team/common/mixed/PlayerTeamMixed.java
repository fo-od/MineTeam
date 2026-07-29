package dev.fooduhhh.craft_team.common.mixed;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

public interface PlayerTeamMixed {
    @Nullable
    PlayerTeam craftTeam$getLastHurtTeam();

    @Unique
    void craftTeam$setLastHurtTeam(@Nullable PlayerTeam lastHurtTeam);

    @Nullable
    @Unique
    LivingEntity craftTeam$getLastHurtMob();

    @Unique
    long craftTeam$getLastHurtMobTimestamp();

    @Unique
    void craftTeam$setLastHurtMob(Entity entity);
}
