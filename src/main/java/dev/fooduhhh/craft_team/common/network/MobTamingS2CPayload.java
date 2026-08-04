package dev.fooduhhh.craft_team.common.network;

import dev.fooduhhh.craft_team.CraftTeam;
import dev.fooduhhh.craft_team.common.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record MobTamingS2CPayload(int entityId, BlockPos pos, String ownerUUID) implements CustomPacketPayload {
    public static final Type<MobTamingS2CPayload> TYPE = new Type<>(CraftTeam.asResource("mob_taming"));
    public static final StreamCodec<ByteBuf, MobTamingS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MobTamingS2CPayload::entityId,
            BlockPos.STREAM_CODEC, MobTamingS2CPayload::pos,
            ByteBufCodecs.STRING_UTF8, MobTamingS2CPayload::ownerUUID,
            MobTamingS2CPayload::new
    );


    public static void clientHandle(final MobTamingS2CPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    ClientLevel clientLevel = Constants.MINECRAFT.level;
                    assert clientLevel != null;
                    Entity entity = clientLevel.getEntity(payload.entityId);
                    if (entity != null) {
                        entity.getPersistentData().putUUID("owner", UUID.fromString(payload.ownerUUID));
                        entity.level().playLocalSound(payload.pos.getX(), payload.pos.getY(), payload.pos.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, entity.getSoundSource(), 1.0F + entity.level().random.nextFloat(), entity.level().random.nextFloat() * 0.7F + 0.3F, false);
                    }
                }
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
