package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedScaledCenterX;
import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedScaledCenterY;
import static dev.fooduhhh.craft_team.client.actions.MenuHelper.*;

public class MenuRender {
    public static final MenuHelper.RadialMenu commandMenu = new MenuHelper.RadialMenu(new MenuHelper.RadialMenu.Option[] {
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command.attack", Items.DIAMOND_SWORD),
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command.follow", Items.LEAD),
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command.stay", Items.COBWEB),
    }, 50, Math.toRadians(90));

    public static void render(GuiGraphics graphics) {
        currentMenu.draw(graphics, cachedScaledCenterX, cachedScaledCenterY);
    }

    public static void drawRectCentered(GuiGraphics graphics, int width, int height, int x, int y, int color) {
        int left = x - width / 2;
        int top = y - height / 2;
        graphics.fill(left, top, left + width, top + height, color);
    }

    public static void drawCenteredString(GuiGraphics graphics, Component text, int x, int y, int color) {
        graphics.drawCenteredString(Constants.MINECRAFT.font, text, x, y, color);
    }
}
