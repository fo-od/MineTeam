package dev.fooduhhh.craft_team.common.network;

import dev.fooduhhh.craft_team.CraftTeam;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestActionMenuC2SPayload(int entityId, String ownerUUID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestActionMenuC2SPayload> TYPE = new CustomPacketPayload.Type<>(CraftTeam.asResource("request_mob_menu"));
    public static final StreamCodec<ByteBuf, RequestActionMenuC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RequestActionMenuC2SPayload::entityId,
            ByteBufCodecs.STRING_UTF8, RequestActionMenuC2SPayload::ownerUUID,
            RequestActionMenuC2SPayload::new
    );

    public static void serverHandle(final RequestActionMenuC2SPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    if (!(context.player() instanceof ServerPlayer player)) return;

                    Entity entity = player.serverLevel().getEntity(payload.entityId());
                    if (!(entity instanceof Mob mob)) return;

                    if (!mob.getPersistentData().contains("owner")) return;
                    if (!player.getUUID().equals(mob.getPersistentData().getUUID("owner"))) return;

                    PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new OpenActionMenuS2CPayload(payload.entityId()));
                }
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
