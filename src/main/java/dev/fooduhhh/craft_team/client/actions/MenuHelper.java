package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MenuHelper {
    public static RadialMenu currentMenu;

    public static class RadialMenu {
        public static class Option {
            public final String key;
            public final ItemStack item;
            public int x;
            public int y;
            public boolean selected;
            private final Runnable onClick;
            private final RadialMenu nextMenu;

            public Option(String key, Item item) {
                this(key, item.getDefaultInstance(), null, null);
            }

            public Option(String key, ItemStack item) {
                this(key, item, null, null);
            }

            public Option(String key, Item item, RadialMenu nextMenu) {
                this(key, item.getDefaultInstance(), nextMenu, null);
            }

            public Option(String key, ItemStack item, RadialMenu nextMenu) {
                this(key, item, nextMenu, null);
            }

            public Option(String key, ItemStack item, Runnable onClick) {
                this(key, item, null, onClick);
            }

            public Option(String key, ItemStack item, RadialMenu nextMenu, Runnable onClick) {
                this.key = key;
                this.item = item;
                this.onClick = onClick;
                this.nextMenu = nextMenu;
            }

            public void draw(GuiGraphics graphics, int centerX, int centerY) {
                drawRectCentered(graphics, 24, 24, centerX - x, centerY - y, 0x67000000);
                drawCenteredString(graphics, Component.translatable(key), centerX - x, centerY - y + 15, 0xffffffff);
                graphics.renderFakeItem(item, centerX - x - 8, centerY - y - 8);
                if (selected) {
                    drawRectCentered(graphics, 24, 24, centerX - x, centerY - y, 0x67ffffff);
                }
            }

            public void click() {
                if (onClick != null) onClick.run();
                if (nextMenu != null) currentMenu = nextMenu;
            }
        }

        public Option[] options;

        public RadialMenu(Option[] options, int radius) {
            this(options, radius, 0);
        }

        public RadialMenu(Option[] options, int radius, double angleOffset) {
            this.options = options;
            for (int i = 0; i < options.length; i++) {
                Point p = getRadialPoint(options.length, radius, i, angleOffset);
                options[i].x = p.x;
                options[i].y = p.y;
            }
        }

        public void draw(GuiGraphics graphics, int centerX, int centerY) {
            for (Option option : options) {
                option.draw(graphics, centerX, centerY);
            }
        }

        public boolean handleSelection(double mouseX, double mouseY) {
            // reimplement
            return false;
        }
    }

    private static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static Point getRadialPoint(int count, int radius, int index, double angleOffset) {
        double step = (Math.PI * 2.0) / count;
        double theta = angleOffset + index * step;
        return new Point((int) Math.round((Math.cos(theta) * radius)), (int) Math.round(Math.sin(theta) * radius));
    }

    private static void drawRectCentered(GuiGraphics graphics, int width, int height, int x, int y, int color) {
        int left = x - width / 2;
        int top = y - height / 2;
        graphics.fill(left, top, left + width, top + height, color);
    }

    private static void drawCenteredString(GuiGraphics graphics, Component text, int x, int y, int color) {
        graphics.drawCenteredString(Constants.MINECRAFT.font, text, x, y, color);
    }

    public static int cachedScreenWidth = -1;
    public static int cachedScreenHeight = -1;
    public static int cachedCenterX = -1;
    public static int cachedCenterY = -1;

    public static void clearCache() {
        cachedScreenWidth = -1;
        cachedScreenHeight = -1;
        cachedCenterX = -1;
        cachedCenterY = -1;
    }

    public static void initializeCache() {
        if (cachedScreenWidth == -1 && cachedScreenHeight == -1) {
            cachedScreenWidth = Constants.MINECRAFT.getWindow().getGuiScaledWidth();
            cachedScreenHeight = Constants.MINECRAFT.getWindow().getGuiScaledHeight();
            cachedCenterX = cachedScreenWidth / 2;
            cachedCenterY = cachedScreenHeight / 2;
        }
    }
}
