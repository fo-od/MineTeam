package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.Constants;

public class MenuCache {
    public static MenuRenderHelper.RadialMenu.Option cachedSelectedOption = null;
    public static int cachedEntityId = -1;

    public static int cachedScaledScreenWidth = -1;
    public static int cachedScaledScreenHeight = -1;
    public static int cachedScaledCenterX = -1;
    public static int cachedScaledCenterY = -1;

    public static int cachedScreenWidth = -1;
    public static int cachedScreenHeight = -1;
    public static int cachedCenterX = -1;
    public static int cachedCenterY = -1;

    public static void clearCache() {
        cachedSelectedOption = null;
        cachedEntityId = -1;

        cachedScaledScreenWidth = -1;
        cachedScaledScreenHeight = -1;
        cachedScaledCenterX = -1;
        cachedScaledCenterY = -1;

        cachedScreenWidth = -1;
        cachedScreenHeight = -1;
        cachedCenterX = -1;
        cachedCenterY = -1;
    }

    public static void initializeCache() {
        if (cachedScaledScreenWidth == -1 && cachedScaledScreenHeight == -1) {
            cachedScaledScreenWidth = Constants.MINECRAFT.getWindow().getGuiScaledWidth();
            cachedScaledScreenHeight = Constants.MINECRAFT.getWindow().getGuiScaledHeight();
            cachedScaledCenterX = cachedScaledScreenWidth / 2;
            cachedScaledCenterY = cachedScaledScreenHeight / 2;

            cachedScreenWidth = Constants.MINECRAFT.getWindow().getScreenWidth();
            cachedScreenHeight = Constants.MINECRAFT.getWindow().getScreenHeight();
            cachedCenterX = cachedScreenWidth / 2;
            cachedCenterY = cachedScreenHeight / 2;
        }
    }
}
