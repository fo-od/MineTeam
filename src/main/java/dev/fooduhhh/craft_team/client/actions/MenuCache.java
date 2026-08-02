package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.Constants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MenuCache {
    public static MenuHelper.RadialMenu.Option cachedSelectedOption = null;
    public static Entity cachedSelectedEntity = null;
    public static int cachedScaledScreenWidth = -1;
    public static int cachedScaledScreenHeight = -1;
    public static int cachedScaledCenterX = -1;
    public static int cachedScaledCenterY = -1;

    public static int cachedScreenWidth = -1;
    public static int cachedScreenHeight = -1;
    public static int cachedCenterX = -1;
    public static int cachedCenterY = -1;

    public static void clearCache() {
        cachedSelectedEntity = null;
        cachedSelectedOption = null;

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
            HitResult hitResult = Constants.MINECRAFT.hitResult;
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                cachedSelectedEntity = ((EntityHitResult) hitResult).getEntity();
            }

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
