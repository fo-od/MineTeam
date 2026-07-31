package dev.fooduhhh.craft_team.client.actions;

import com.mojang.blaze3d.platform.InputConstants;
import dev.fooduhhh.craft_team.common.Constants;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class MenuHandler {
    public static final float MAX_DISTANCE = 100; // max mob distance from player squared

    public static boolean isMenuOpen = false;

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
        if (!ACTION_MENU_MAPPING.isDown()) {
            isMenuOpen = false;
        }
        while (ACTION_MENU_MAPPING.consumeClick()) {
            isMenuOpen = true;

            HitResult hitResult = Constants.MINECRAFT.hitResult;

            if (hitResult != null && hitResult.distanceTo(Constants.MINECRAFT.player) < MAX_DISTANCE) {
                Constants.LOGGER.info("Looking at entity <10 blocks away");
            } else {
                Constants.LOGGER.info("Not looking at entity / entity is too far");
            }
        }
    }
}
