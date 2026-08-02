package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.*;

public class MenuHelper {
    static boolean isMouseInDeadzone(double mouseX, double mouseY) {
        mouseX = projectMouseX(mouseX, cachedScaledScreenWidth, cachedScreenWidth, cachedScaledCenterX);
        mouseY = projectMouseY(mouseY, cachedScaledScreenHeight, cachedScreenHeight, cachedScaledCenterY);
        double distanceFromCenter = mouseX * mouseX + mouseY * mouseY;
        return distanceFromCenter <= (CraftTeamConfig.deadzone.get() * CraftTeamConfig.deadzone.get());
    }

    static double mouseAngle(double mouseX, double mouseY) {
        double dy = mouseY - cachedCenterY;
        double dx = mouseX - cachedCenterX;
        double angle = Math.atan2(-dy, dx);
        return MenuRenderHelper.normalizeAngle(angle);
    }

    // taken from Slice

    public static double projectMouseX(double rawX, int cachedScreenWidth, int screenWidth, int centerX) {
        return rawX * cachedScreenWidth / screenWidth - centerX;
    }

    public static double projectMouseY(double rawY, int cachedScreenHeight, int screenHeight, int centerY) {
        return rawY * cachedScreenHeight / screenHeight - centerY;
    }
}
