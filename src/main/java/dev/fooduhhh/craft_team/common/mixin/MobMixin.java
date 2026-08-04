package dev.fooduhhh.craft_team.common.mixin;

import dev.fooduhhh.craft_team.common.entity.ai.goal.TeamOwnerHurtTargetGoal;
import dev.fooduhhh.craft_team.common.mixed.MobMixed;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MobMixin extends Entity implements MobMixed {
    @Shadow
    @Final
    public GoalSelector targetSelector;


    @Unique
    private static final EntityDataAccessor<String> DATA_FLAGS_ID = SynchedEntityData.defineId(MobMixin.class, EntityDataSerializers.STRING);

    public MobMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_FLAGS_ID, "");
    }


    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityType<? extends Mob> entityType, Level level, CallbackInfo ci) {
        this.targetSelector.addGoal(2, new TeamOwnerHurtTargetGoal((Mob) (Object) this));
    }


    @Override
    public void craftTeam$setTame(PlayerTeam tame) {
        this.entityData.set(DATA_FLAGS_ID, tame.getName());
    }

    @Override
    public boolean craftTeam$wantsToAttack(LivingEntity ownerLastHurt, PlayerTeam lastHurtTeam, PlayerTeam team) {
        return lastHurtTeam != team;
    }

    @Override
    public PlayerTeam craftTeam$getOwnerTeam() {
        MinecraftServer server = this.getServer();
        if (server != null) {
            ServerScoreboard scoreboard = server.getScoreboard();
            return scoreboard.getPlayerTeam(entityData.get(DATA_FLAGS_ID));
        }
        return null;
    }

    @Override
    @Nullable
    public ServerPlayer craftTeam$getOwner() {
        MinecraftServer server = this.getServer();
        if (server != null) {
            if (!this.getPersistentData().contains("owner")) return null;

            return server.getPlayerList().getPlayer(this.getPersistentData().getUUID("owner"));
        }
        return null;
    }
}
