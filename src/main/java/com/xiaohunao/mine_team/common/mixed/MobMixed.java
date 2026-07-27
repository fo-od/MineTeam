package com.xiaohunao.mine_team.common.mixed;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.PlayerTeam;

public interface MobMixed {
    void mineTeam$setTame(PlayerTeam tame);
    PlayerTeam mineTeam$getOwnerTeam();
    boolean mineTeam$wantsToAttack(LivingEntity ownerLastHurt, PlayerTeam lastHurtTeam, PlayerTeam team);
}
