package dev.fooduhhh.craft_team.common.init;

import dev.fooduhhh.craft_team.common.Constants;
import dev.fooduhhh.craft_team.common.network.ChangeMobGoalPayload;
import dev.fooduhhh.craft_team.common.network.MobTamingS2CPayload;
import dev.fooduhhh.craft_team.common.network.TeamColorSyncPayload;
import dev.fooduhhh.craft_team.common.network.TeamPvPSyncPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkRegister {
    public static final String VERSION = "0.0.2";

    @SubscribeEvent
    public static void registerPayload(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playBidirectional(TeamPvPSyncPayload.TYPE, TeamPvPSyncPayload.STREAM_CODEC, new DirectionalPayloadHandler<>(
                TeamPvPSyncPayload::clientHandle,
                TeamPvPSyncPayload::serverHandle
        ));
        registrar.playBidirectional(TeamColorSyncPayload.TYPE, TeamColorSyncPayload.STREAM_CODEC, new DirectionalPayloadHandler<>(
                TeamColorSyncPayload::clientHandle,
                TeamColorSyncPayload::serverHandle
        ));
        registrar.playToClient(MobTamingS2CPayload.TYPE, MobTamingS2CPayload.STREAM_CODEC, MobTamingS2CPayload::clientHandle);
        registrar.playToServer(ChangeMobGoalPayload.TYPE, ChangeMobGoalPayload.STREAM_CODEC, ChangeMobGoalPayload::serverHandle);
    }
}