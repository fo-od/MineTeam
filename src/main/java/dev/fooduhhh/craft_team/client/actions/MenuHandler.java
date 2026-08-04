package dev.fooduhhh.craft_team.client.actions;

import com.mojang.blaze3d.platform.InputConstants;
import dev.fooduhhh.craft_team.common.Constants;
import dev.fooduhhh.craft_team.common.network.ChangeMobGoalPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

import static dev.fooduhhh.craft_team.client.actions.MenuCache.*;
import static dev.fooduhhh.craft_team.client.actions.MenuRenderHelper.*;
import static dev.fooduhhh.craft_team.client.actions.MenuRender.commandMenu;

public class MenuHandler {
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
        currentMenu = commandMenu;
        initializeCache();
    }

    public static void closeMenu() {
        if (!isMenuOpen) return;
        isMenuOpen = false;
        Constants.MINECRAFT.mouseHandler.grabMouse();
        if (cachedSelectedOption != null) {
            cachedSelectedOption.click();
        }
        clearCache();
        currentMenu = commandMenu;
    }

    public static void onClientTick() {
        boolean isDown = ACTION_MENU_MAPPING.isDown();
        if (!isDown && wasMenuOpen) {
            closeMenu();
        }

        if (isDown && !wasMenuOpen) {
            HitResult hitResult = Constants.MINECRAFT.hitResult;

            if (hitResult == null) return;
            if (hitResult.getType() != HitResult.Type.ENTITY) return;

            Entity entity = ((EntityHitResult) hitResult).getEntity();
            cachedSelectedMob = (Mob) entity;
            if (!entity.getPersistentData().contains("owner")) return;
            UUID ownerUUID = entity.getPersistentData().getUUID("owner");
            if (Constants.MINECRAFT.player.getUUID().equals(ownerUUID)) {
                openMenu();
            }
        }

        if (isMenuOpen) {
            handleMouseInput(Constants.MINECRAFT.mouseHandler.xpos(), Constants.MINECRAFT.mouseHandler.ypos());
        }

        wasMenuOpen = isDown;
    }

    public static void handleMouseInput(double mouseX, double mouseY) {
        if (MenuHelper.isMouseInDeadzone(mouseX, mouseY)) {
            if (cachedSelectedOption != null) cachedSelectedOption = null;
            return;
        }

        double mouseAngle = MenuHelper.mouseAngle(mouseX, mouseY);
        currentMenu.handleSelection(mouseAngle);
    }

    public static void handleClick() {
        if (cachedSelectedOption == null) {
            closeMenu();
            wasMenuOpen = true;
            return;
        }

        if (isMenuOpen) {
            cachedSelectedOption.click();
            clearCache();
            closeMenu();
        }
    }

    public enum PetGoal {
        ATTACK, // attack entities that the owner attacks
        FOLLOW, // follow the owner, and attack entities that attack the owner
        STAY    // stay in place and do nothing
    }

    public static void changeGoal(PetGoal goal) {
        PacketDistributor.sendToServer(new ChangeMobGoalPayload(cachedSelectedMob.getId(), goal.ordinal()));
    }
}
