package dev.fooduhhh.craft_team.common.event;

import dev.fooduhhh.craft_team.common.Constants;
import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;
import dev.fooduhhh.craft_team.common.mixed.PlayerTeamMixed;
import dev.fooduhhh.craft_team.common.network.TeamColorSyncPayload;
import dev.fooduhhh.craft_team.common.network.TeamPvPSyncPayload;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class LivingEventSubscriber {
    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        String teamColor = player.getPersistentData().getString("teamColor");

        if (teamColor.isEmpty()) {
            teamColor = "white";
            player.getPersistentData().putString("teamColor", teamColor);
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player, new TeamColorSyncPayload(teamColor));

        boolean teamPvP = CraftTeamConfig.allowDamageSelf.get();
        PacketDistributor.sendToPlayer((ServerPlayer) player, new TeamPvPSyncPayload(teamPvP));


        ServerLevel level = (ServerLevel) player.level();
        Scoreboard scoreboard = level.getServer().getScoreboard();
        PlayerTeam team = scoreboard.getPlayersTeam(player.getScoreboardName());

        if (team == null) {
            PlayerTeam playerTeam = scoreboard.getPlayerTeam("white");

            if (playerTeam != null) {
                scoreboard.addPlayerToTeam(player.getScoreboardName(), playerTeam);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingPvP(LivingIncomingDamageEvent event) {
        LivingEntity hurtEntity = event.getEntity();
        DamageSource source = event.getSource();
        Entity attackEntity = source.getEntity();

        if (hurtEntity.level() instanceof ServerLevel serverLevel && attackEntity != null) {
            ServerScoreboard scoreboard = serverLevel.getServer().getScoreboard();
            PlayerTeam attackEntityTeam = scoreboard.getPlayersTeam(attackEntity.getScoreboardName());
            PlayerTeam hurtEntityTeam = scoreboard.getPlayersTeam(hurtEntity.getScoreboardName());

            if (attackEntity == hurtEntity && CraftTeamConfig.allowDamageSelf.get()) {
                return;
            }

            if (attackEntityTeam == null || hurtEntityTeam != attackEntityTeam) {
                return;
            }

            boolean teamPvP = hurtEntity.getPersistentData().getBoolean("teamPvP");
            boolean teamPvP1 = attackEntity.getPersistentData().getBoolean("teamPvP");
            if (!teamPvP && !teamPvP1) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        Player entity = event.getEntity();
        Player original = event.getOriginal();
        if (event.isWasDeath() && !entity.level().isClientSide()) {
            String teamColor = original.getPersistentData().getString("teamColor");
            boolean teamPvP = original.getPersistentData().getBoolean("teamPvP");

            entity.getPersistentData().putString("teamColor", teamColor);
            entity.getPersistentData().putBoolean("teamPvP", teamPvP);

            PacketDistributor.sendToPlayer((ServerPlayer) entity, new TeamColorSyncPayload(teamColor));
            PacketDistributor.sendToPlayer((ServerPlayer) entity, new TeamPvPSyncPayload(teamPvP));
        }
    }


    @SubscribeEvent
    public static void onSetTeamLastHurtMob(LivingIncomingDamageEvent event) {
        LivingEntity hurtEntity = event.getEntity();
        DamageSource source = event.getSource();
        Entity attackEntity = source.getEntity();

        if (hurtEntity.level() instanceof ServerLevel serverLevel && attackEntity != null) {
            ServerScoreboard scoreboard = serverLevel.getServer().getScoreboard();
            PlayerTeam attackEntityTeam = scoreboard.getPlayersTeam(attackEntity.getScoreboardName());
            PlayerTeam hurtEntityTeam = scoreboard.getPlayersTeam(hurtEntity.getScoreboardName());

            if (attackEntity instanceof Player && attackEntityTeam instanceof PlayerTeamMixed playerTeamMixed) {
                playerTeamMixed.craftTeam$setLastHurtMob(hurtEntity);
                playerTeamMixed.craftTeam$setLastHurtTeam(hurtEntityTeam);
            }

            if (hurtEntity instanceof Player && hurtEntityTeam instanceof PlayerTeamMixed playerTeamMixed) {
                playerTeamMixed.craftTeam$setLastHurtMob(attackEntity);
                playerTeamMixed.craftTeam$setLastHurtTeam(attackEntityTeam);
            }
        }
    }
}