package dev.fooduhhh.craft_team;

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

@Mod(CraftTeam.MOD_ID)
public class CraftTeam {
    public static final String MOD_ID = "craft_team";

    public CraftTeam(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onFMLCommonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, CraftTeamConfig.CONFIG, "craft_team.toml");
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String asResourceKey(String path) {
        return MOD_ID + "." + path;
    }

    @SubscribeEvent
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        CraftTeamConfig.loadTamingMaterials();
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
