package com.xiaohunao.mine_team.common.event;

import com.xiaohunao.mine_team.MineTeam;
import com.xiaohunao.mine_team.common.config.MineTeamConfig;
import com.xiaohunao.mine_team.common.init.TeamInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.Arrays;
import java.util.Locale;

@EventBusSubscriber(modid = MineTeam.MOD_ID)
public class LevelEventSubscriber {
    @SubscribeEvent
    public static void onCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        TeamInitializer.initTeams((ServerLevel) event.getLevel());
    }
}
