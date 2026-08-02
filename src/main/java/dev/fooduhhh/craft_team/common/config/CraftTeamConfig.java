package dev.fooduhhh.craft_team.common.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.regex.Pattern;

public class CraftTeamConfig {
    public static final ModConfigSpec CONFIG;

    private static final ModConfigSpec.ConfigValue<List<? extends String>> tamingMaterials;
    private static final BiMap<EntityType<?>, Ingredient> tamingMaterialMap = HashBiMap.create();
    public static final ModConfigSpec.ConfigValue<Double> deadzone;

    public static final ModConfigSpec.BooleanValue allowDamageSelf;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        // server sided stuff

        tamingMaterials = builder
                .comment("List of materials that can be used to tame entities")
                .comment("Format: entity-ingredient,'minecraft:wolf-{'item':'minecraft:bone'}'")
                .define("server.tamingMaterials", List.of(), str -> {
                    if (!(str instanceof String)) {
                        return false;
                    }
                    return Pattern.matches("\\w+:\\w+-\\{\"\\w+\":\"\\w+:\\w+\"}", (String) str);
                });

        allowDamageSelf = builder
                .comment("Whether or not to allow entities within the Team to attack themselves")
                .define("server.allowDamageSelf", true);

        // client sided stuff

        deadzone = builder
                .comment("Deadzone for menu selection in pixels (scales with GUI scale)")
                .define("client.deadzone", 12.5);

        CONFIG = builder.build();
    }

    public static void loadTamingMaterials() {
        tamingMaterialMap.clear();
        for (String material : tamingMaterials.get()) {
            String[] split = material.split("-");
            BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.parse(split[0])).ifPresent(entityType -> Ingredient.CODEC_NONEMPTY.parse(JsonOps.INSTANCE, new JsonPrimitive(split[1])).result().ifPresent(ingredient -> tamingMaterialMap.put(entityType, ingredient)));
        }
    }

    public static Ingredient getTamingMaterial(EntityType<?> entityType) {
        return tamingMaterialMap.getOrDefault(entityType, Ingredient.of(Items.GOLDEN_APPLE));
    }
}
