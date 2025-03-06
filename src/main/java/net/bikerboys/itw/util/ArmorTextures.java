package net.bikerboys.itw.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ArmorTextures {
    private static final Map<Item, ResourceLocation> TEXTURES = new HashMap<>();

    public static void register(Item item, String texturePath) {
        TEXTURES.put(item, new ResourceLocation("tailory", texturePath));
    }

    public static ResourceLocation getTexture(Item item) {
        return TEXTURES.getOrDefault(item, new ResourceLocation("missing"));
    }
}
