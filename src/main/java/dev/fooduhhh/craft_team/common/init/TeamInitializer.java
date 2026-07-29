package dev.fooduhhh.craft_team.common.init;

import dev.fooduhhh.craft_team.CraftTeam;
import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.PlayerTeam;

import java.util.Arrays;
import java.util.Locale;

public class TeamInitializer {
    public static void initTeams(ServerLevel level) {
        ServerScoreboard scoreboard = level.getServer().getScoreboard();
        Arrays.stream(ChatFormatting.values())
                .filter(ChatFormatting::isColor)
                .map(ChatFormatting::getName)
                .filter(name -> scoreboard.getPlayerTeam(name) == null)
                .forEach(name -> {
                    PlayerTeam team = scoreboard.addPlayerTeam(name);
                    team.setColor(ChatFormatting.valueOf(name.toUpperCase(Locale.ROOT)));
                    team.setDisplayName(Component.translatable(CraftTeam.asResourceKey("team." + name)));
                    team.setAllowFriendlyFire(CraftTeamConfig.allowDamageSelf.get());
                });
    }
}
