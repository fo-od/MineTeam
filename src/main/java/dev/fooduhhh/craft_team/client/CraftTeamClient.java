package dev.fooduhhh.craft_team.client;

import dev.fooduhhh.craft_team.client.actions.MenuHandler;
import dev.fooduhhh.craft_team.client.actions.MenuRender;
import dev.fooduhhh.craft_team.common.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class CraftTeamClient {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        MenuHandler.onClientTick(event);
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (MenuHandler.isMenuOpen) {
            MenuRender.render(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClick(InputEvent.MouseButton.Pre event) {
        if (MenuHandler.isMenuOpen && event.getAction() == 1) {
            MenuHandler.handleClick();
            event.setCanceled(true);
        }
    }
}
