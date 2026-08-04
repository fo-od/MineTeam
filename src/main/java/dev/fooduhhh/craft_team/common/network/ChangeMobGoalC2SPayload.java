package dev.fooduhhh.craft_team.common.network;

import dev.fooduhhh.craft_team.CraftTeam;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ChangeMobGoalC2SPayload(int entityId, int newGoal) implements CustomPacketPayload {
    public static final Type<ChangeMobGoalC2SPayload> TYPE = new Type<>(CraftTeam.asResource("change_mob_goal"));
    public static final StreamCodec<ByteBuf, ChangeMobGoalC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ChangeMobGoalC2SPayload::entityId,
            ByteBufCodecs.INT, ChangeMobGoalC2SPayload::newGoal,
            ChangeMobGoalC2SPayload::new
    );

    public static void serverHandle(final ChangeMobGoalC2SPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            Mob entity = (Mob) player.serverLevel().getEntity(data.entityId());

            if (entity == null) return;
            if (!entity.getPersistentData().contains("owner")) return;
            if (!player.getUUID().equals(entity.getPersistentData().getUUID("owner"))) return;

            entity.getPersistentData().putInt("goal", data.newGoal());
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
