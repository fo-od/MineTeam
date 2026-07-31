package dev.fooduhhh.craft_team.client.actions;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static dev.fooduhhh.craft_team.CraftTeam.LOGGER;

public class Handler {
    public static final float MAX_DISTANCE = 100; // max mob distance from player squared

    public static final KeyMapping ACTION_MENU_MAPPING = new KeyMapping(
            "key.craft_team.menu",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE,
            "key.categories.craft_team");

    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(ACTION_MENU_MAPPING);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        while (ACTION_MENU_MAPPING.consumeClick()) {
            HitResult hitResult = Minecraft.getInstance().hitResult;
            if (hitResult.distanceTo(Minecraft.getInstance().player) < MAX_DISTANCE) {
                LOGGER.info("Looking at entity <10 blocks away");
            } else {
                LOGGER.info("Not looking at entity / entity is too far");
            }
        }
    }
}
