package dev.fooduhhh.craft_team;

import dev.fooduhhh.craft_team.client.actions.MenuHandler;
import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import dev.fooduhhh.craft_team.common.Constants;

@Mod(Constants.MOD_ID)
public class CraftTeam {
    public CraftTeam(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onFMLCommonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, CraftTeamConfig.CONFIG, "craft_team.toml");
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }

    public static String asResourceKey(String path) {
        return Constants.MOD_ID + "." + path;
    }

    @SubscribeEvent
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        CraftTeamConfig.loadTamingMaterials();
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onClientKeybinds(RegisterKeyMappingsEvent event) {
            MenuHandler.registerBindings(event);
        }
    }
}
