package dev.fooduhhh.craft_team.common;

import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static final String MOD_ID = "craft_team";
    public static final String MOD_NAME = "Craft Team";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
}
