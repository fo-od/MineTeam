package dev.fooduhhh.craft_team.common.event;

import dev.fooduhhh.craft_team.CraftTeam;
import dev.fooduhhh.craft_team.common.init.TeamInitializer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = CraftTeam.MOD_ID)
public class LevelEventSubscriber {
    @SubscribeEvent
    public static void onCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        TeamInitializer.initTeams((ServerLevel) event.getLevel());
    }
}
