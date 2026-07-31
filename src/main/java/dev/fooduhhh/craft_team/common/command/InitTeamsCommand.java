package dev.fooduhhh.craft_team.common.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.fooduhhh.craft_team.common.Constants;
import dev.fooduhhh.craft_team.common.init.TeamInitializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


@EventBusSubscriber(modid = Constants.MOD_ID)
public class InitTeamsCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("craftteam").requires(cs -> cs.hasPermission(2)) // OP-only; change as desired
                .then(Commands.literal("init").executes(ctx -> {
                    ServerLevel level = ctx.getSource().getLevel();
                    TeamInitializer.initTeams(level);
                    ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Teams initialized."), true);
                    return 1;
                })));
    }
}
