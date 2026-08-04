package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedScaledCenterX;
import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedScaledCenterY;
import static dev.fooduhhh.craft_team.client.actions.MenuRenderHelper.*;

public class MenuRender {
    public static final MenuRenderHelper.RadialMenu commandMenu = new MenuRenderHelper.RadialMenu(new MenuRenderHelper.RadialMenu.Option[]{
            new MenuRenderHelper.RadialMenu.Option("key.craft_team.menu.command.attack", Items.DIAMOND_SWORD, () -> MenuHandler.changeGoal(0)),
            new MenuRenderHelper.RadialMenu.Option("key.craft_team.menu.command.follow", Items.LEAD, () -> MenuHandler.changeGoal(1)),
            new MenuRenderHelper.RadialMenu.Option("key.craft_team.menu.command.stay", Items.COBWEB, () -> MenuHandler.changeGoal(2)),
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
