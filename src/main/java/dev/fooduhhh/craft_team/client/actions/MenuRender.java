package dev.fooduhhh.craft_team.client.actions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Items;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedScaledCenterX;
import static dev.fooduhhh.craft_team.client.actions.MenuCache.cachedScaledCenterY;
import static dev.fooduhhh.craft_team.client.actions.MenuHelper.*;

public class MenuRender {
    public static final MenuHelper.RadialMenu ownerMenu = new MenuHelper.RadialMenu(new MenuHelper.RadialMenu.Option[] {
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.change_owner.team", Items.NAME_TAG),
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.change_owner.player", Items.PLAYER_HEAD),
    }, 50);

    public static final MenuHelper.RadialMenu commandMenu = new MenuHelper.RadialMenu(new MenuHelper.RadialMenu.Option[] {
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command.attack", Items.DIAMOND_SWORD),
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command.follow", Items.LEAD),
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command.stay", Items.COBWEB),
    }, 50, Math.toRadians(90));

    public static final MenuHelper.RadialMenu mainMenu = new MenuHelper.RadialMenu(new MenuHelper.RadialMenu.Option[] {
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.change_owner", Items.NAME_TAG, ownerMenu),
            new MenuHelper.RadialMenu.Option("key.craft_team.menu.command", Items.BELL, commandMenu),
    }, 50);

    public static void render(GuiGraphics graphics) {
        currentMenu.draw(graphics, cachedScaledCenterX, cachedScaledCenterY);
    }
}
