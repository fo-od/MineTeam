package dev.fooduhhh.craft_team.common.mixed;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.PlayerTeam;

public interface MobMixed {
    void craftTeam$setTame(PlayerTeam tame);

    PlayerTeam craftTeam$getOwnerTeam();

    boolean craftTeam$wantsToAttack(LivingEntity ownerLastHurt, PlayerTeam lastHurtTeam, PlayerTeam team);

    ServerPlayer craftTeam$getOwner();
}
