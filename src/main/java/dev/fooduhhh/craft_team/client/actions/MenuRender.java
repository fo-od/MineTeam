package dev.fooduhhh.craft_team.client.actions;

import dev.fooduhhh.craft_team.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MenuRender {
    private static int originX;
    private static int originY;

    private static final RadialMenu mainMenu = new RadialMenu(new RadialMenu.Option[] {
            new RadialMenu.Option("key.craft_team.menu.change_owner", Items.NAME_TAG),
            new RadialMenu.Option("key.craft_team.menu.command", Items.BELL),
    }, 50);

    public static void render(GuiGraphics graphics) {
        originX = (graphics.guiWidth() - 1) / 2;
        originY = (graphics.guiHeight() - 1) / 2;
        mainMenu.draw(graphics);
    }

    private static class RadialMenu {
        private static class Option {
            public final String key;
            public final Item item;
            public int x;
            public int y;

            public Option(String key, Item item) {
                this.key = key;
                this.item = item;
            }

            public void draw(GuiGraphics graphics) {
                drawRectCentered(graphics, 24, 24, originX + x, originY + y, 0x67000000);
                drawCenteredString(graphics, Component.translatable(key), originX - x, originY - y + 15, 0xffffffff);
                graphics.renderFakeItem(item.getDefaultInstance(), originX - x - 8, originY - y - 8);
            }
        }

        public Option[] options;

        public RadialMenu(Option[] options, int radius) {
            this.options = options;
            for (int i = 0; i < options.length; i++) {
                Point p = getRadialPoint(options.length, radius, i);
                options[i].x = p.x;
                options[i].y = p.y;
            }
        }

        public void draw(GuiGraphics graphics) {
            for (Option option : options) {
                option.draw(graphics);
            }
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

    private static Point getRadialPoint(int count, int radius, int index) {
        return getRadialPoint(count, radius, index, 0);
    }

    private static void drawRectCentered(GuiGraphics graphics, int width, int height, int x, int y, int color) {
        int left = x - width / 2;
        int top  = y - height / 2;
        graphics.fill(left, top, left + width, top + height, color);
    }

    private static void drawCenteredString(GuiGraphics graphics, String text, int x, int y, int color) {
        graphics.drawCenteredString(Constants.MINECRAFT.font, text, x, y, color);
    }

    private static void drawCenteredString(GuiGraphics graphics, Component text, int x, int y, int color) {
        graphics.drawCenteredString(Constants.MINECRAFT.font, text, x, y, color);
    }
}
