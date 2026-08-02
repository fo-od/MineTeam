package dev.fooduhhh.craft_team.client.actions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedSelectedOption;
import static dev.fooduhhh.craft_team.client.actions.MenuHandler.closeMenu;
import static dev.fooduhhh.craft_team.client.actions.MenuHandler.onMenuClose;
import static dev.fooduhhh.craft_team.client.actions.MenuRender.*;

public class MenuRenderHelper {
    public static RadialMenu currentMenu;

    public static class RadialMenu {
        public static class Option {
            public final String key;
            public final ItemStack item;
            public int x;
            public int y;
            public final Runnable onClick;
            public final RadialMenu nextMenu;

            public Option(String key, Item item) {
                this(key, item.getDefaultInstance(), null, null);
            }

            public Option(String key, Item item, RadialMenu nextMenu) {
                this(key, item.getDefaultInstance(), nextMenu, null);
            }

            public Option(String key, ItemStack item, RadialMenu nextMenu, Runnable onClick) {
                this.key = key;
                this.item = item;
                this.onClick = onClick;
                this.nextMenu = nextMenu;
            }

            public void draw(GuiGraphics graphics, int centerX, int centerY, boolean selected) {
                drawRectCentered(graphics, 24, 24, centerX - x, centerY - y, 0x67000000);
                drawCenteredString(graphics, Component.translatable(key), centerX - x, centerY - y + 15, 0xffffffff);
                graphics.renderFakeItem(item, centerX - x - 8, centerY - y - 8);
                if (selected) {
                    drawRectCentered(graphics, 24, 24, centerX - x, centerY - y, 0x67ffffff);
                }
            }

            public void click() {
                if (onClick != null) onClick.run();
                if (nextMenu != null) {
                    currentMenu = nextMenu;
                } else {
                    currentMenu = MenuRender.commandMenu;
                    closeMenu();
                    onMenuClose();
                }
            }
        }

        public Option[] options;
        public double angleOffset;
        public double step;

        public RadialMenu(Option[] options, int radius) {
            this(options, radius, 0);
        }

        public RadialMenu(Option[] options, int radius, double angleOffset) {
            this.options = options;

            this.step = (Math.PI * 2.0) / options.length;
            this.angleOffset = angleOffset;

            for (int i = 0; i < options.length; i++) {
                double theta = this.angleOffset + i * step;
                options[i].x = (int) Math.round((Math.cos(theta) * radius));
                options[i].y = (int) Math.round(Math.sin(theta) * radius);
            }
        }

        public void draw(GuiGraphics graphics, int centerX, int centerY) {
            for (Option option : options) {
                option.draw(graphics, centerX, centerY, cachedSelectedOption == option);
            }
        }

        public void handleSelection(double angle) {
            int n = options.length;
            if (n == 2) angle += Math.PI; // n=2 is flipped for some reason

            double bestDist = Double.POSITIVE_INFINITY;
            int bestIdx = 0;

            for (int i = 0; i < n; i++) {
                double theta = this.angleOffset - i * this.step;

                // angular distance in [0, 2π)
                double d = MenuRenderHelper.normalizeAngle(angle - theta);

                // shortest wrap-around distance in [0, π]
                d = Math.min(d, (Math.PI * 2.0) - d);

                if (d < bestDist) {
                    bestDist = d;
                    bestIdx = i;
                }
            }

            cachedSelectedOption = options[bestIdx];
        }
    }

    public static double normalizeAngle(double a) {
        a %= (Math.PI * 2.0);
        if (a < 0) a += Math.PI * 2.0;
        return a;
    }
}
