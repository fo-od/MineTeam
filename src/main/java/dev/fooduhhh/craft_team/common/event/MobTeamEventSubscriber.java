package dev.fooduhhh.craft_team.common.event;

import dev.fooduhhh.craft_team.common.Constants;
import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;
import dev.fooduhhh.craft_team.common.network.MobTamingS2CPayload;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class MobTeamEventSubscriber {

    @SubscribeEvent
    public static void onPlayerInteractEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();

        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) return;

        Entity target = event.getTarget();
        Ingredient tamingMaterial = CraftTeamConfig.getTamingMaterial(target.getType());
        ItemStack itemstack = player.getItemInHand(hand);

        if (!tamingMaterial.test(itemstack)) return;

        if (target instanceof LivingEntity livingEntity) {
            ServerLevel serverLevel = (ServerLevel) level;
            ServerScoreboard scoreboard = serverLevel.getServer().getScoreboard();

            PlayerTeam playersTeam = scoreboard.getPlayersTeam(player.getScoreboardName());
            PlayerTeam targetTeam = scoreboard.getPlayersTeam(livingEntity.getStringUUID());

            if (playersTeam == null || targetTeam != null) return;

            if (livingEntity.hasEffect(MobEffects.WEAKNESS)) {
                itemstack.consume(1, player);

                livingEntity.removeEffect(MobEffects.WEAKNESS);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Math.min(livingEntity.level().getDifficulty().getId() - 1, 0)));
                livingEntity.setGlowingTag(true);

                scoreboard.addPlayerToTeam(livingEntity.getStringUUID(), playersTeam);
                livingEntity.getPersistentData().putString("owner", player.getStringUUID());

                PacketDistributor.sendToPlayersInDimension(serverLevel, new MobTamingS2CPayload(target.getId(), target.blockPosition()));
            }
        }
    }
}
