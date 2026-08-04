package dev.fooduhhh.craft_team.common.network;

import dev.fooduhhh.craft_team.CraftTeam;
import dev.fooduhhh.craft_team.client.actions.MenuCache;
import dev.fooduhhh.craft_team.client.actions.MenuHandler;
import dev.fooduhhh.craft_team.common.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenActionMenuS2CPayload(int entityId) implements CustomPacketPayload {
    public static final Type<OpenActionMenuS2CPayload> TYPE = new Type<>(CraftTeam.asResource("open_mob_menu"));
    public static final StreamCodec<ByteBuf, OpenActionMenuS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, OpenActionMenuS2CPayload::entityId,
            OpenActionMenuS2CPayload::new
    );

    public static void clientHandle(final OpenActionMenuS2CPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    ClientLevel clientLevel = Constants.MINECRAFT.level;
                    if (clientLevel == null) return;

                    Entity entity = clientLevel.getEntity(payload.entityId());
                    if (entity == null) return;

                    MenuCache.cachedEntityId = payload.entityId();
                    MenuHandler.openMenu();
                }
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
