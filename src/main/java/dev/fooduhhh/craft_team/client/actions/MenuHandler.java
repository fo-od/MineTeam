package dev.fooduhhh.craft_team.client.actions;

import com.mojang.blaze3d.platform.InputConstants;
import dev.fooduhhh.craft_team.common.Constants;
import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.*;
import static dev.fooduhhh.craft_team.client.actions.MenuHelper.*;
import static dev.fooduhhh.craft_team.client.actions.MenuRender.mainMenu;

public class MenuHandler {
    public static final int MAX_DISTANCE = 100; // max mob distance from player squared

    public static boolean isMenuOpen = false;
    private static boolean wasMenuOpen = false;

    public static final KeyMapping ACTION_MENU_MAPPING = new KeyMapping(
            "key.craft_team.menu",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE,
            "key.categories.craft_team");

    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(ACTION_MENU_MAPPING);
    }

    public static void openMenu() {
        if (isMenuOpen) return;
        isMenuOpen = true;
        Constants.MINECRAFT.mouseHandler.releaseMouse();
        initializeCache();

        HitResult hitResult = Constants.MINECRAFT.hitResult;
        if (hitResult != null && hitResult.distanceTo(Constants.MINECRAFT.player) < MAX_DISTANCE) {
            Constants.LOGGER.info("Looking at entity <10 blocks away");
        } else {
            Constants.LOGGER.info("Not looking at entity / entity is too far");
        }
    }

    public static void closeMenu() {
        if (!isMenuOpen) return;
        isMenuOpen = false;
        Constants.MINECRAFT.mouseHandler.grabMouse();
        if (cachedSelectedOption != null && cachedSelectedOption.nextMenu == null) {
            cachedSelectedOption.click();
        }
        clearCache();
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        boolean isDown = ACTION_MENU_MAPPING.isDown();
        if (!isDown && wasMenuOpen) {
            closeMenu();
        }

        if (isDown && !wasMenuOpen) {
            openMenu();
        }

        if (isMenuOpen) {
            handleMouseInput(Constants.MINECRAFT.mouseHandler.xpos(), Constants.MINECRAFT.mouseHandler.ypos());
        }

        wasMenuOpen = isDown;
    }

    public static void handleMouseInput(double mouseX, double mouseY) {
        if (isMouseInDeadzone(mouseX, mouseY)) {
            if (cachedSelectedOption != null) cachedSelectedOption = null;
            return;
        }

        double mouseAngle = mouseAngle(mouseX, mouseY);
        currentMenu.handleSelection(mouseAngle);
    }

    public static void onMenuClose() {
        currentMenu = mainMenu;
    }

    public static void handleClick() {
        if (cachedSelectedOption == null) {
            onMenuClose();
            wasMenuOpen = true;
            return;
        }

        cachedSelectedOption.click();
        clearCache();
        initializeCache();
    }

    private static boolean isMouseInDeadzone(double mouseX, double mouseY) {
        mouseX = projectMouseX(mouseX, cachedScaledScreenWidth, cachedScreenWidth, cachedScaledCenterX);
        mouseY = projectMouseY(mouseY, cachedScaledScreenHeight, cachedScreenHeight, cachedScaledCenterY);
        double distanceFromCenter = mouseX * mouseX + mouseY * mouseY;
        return distanceFromCenter <= (CraftTeamConfig.deadzone.get() * CraftTeamConfig.deadzone.get());
    }

    private static double mouseAngle(double mouseX, double mouseY) {
        double dy = mouseY - cachedCenterY;
        double dx = mouseX - cachedCenterX;
        double angle = Math.atan2(-dy, dx);
        return MenuHelper.normalizeAngle(angle);
    }

    // taken from Slice

    public static double projectMouseX(double rawX, int cachedScreenWidth, int screenWidth, int centerX) {
        return rawX * cachedScreenWidth / screenWidth - centerX;
    }

    public static double projectMouseY(double rawY, int cachedScreenHeight, int screenHeight, int centerY) {
        return rawY * cachedScreenHeight / screenHeight - centerY;
    }
}
