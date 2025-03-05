package net.bikerboys;

import net.bikerboys.itw.item.ModItems;
import net.bikerboys.itw.render.ArmorCurioRenderer;
import net.bikerboys.itw.util.ArmorTextures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = "itw", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        ModItems.ITEMS.getEntries().forEach(item -> {
            ResourceLocation texture = ArmorTextures.getTexture(item.get());
            if (!texture.getPath().equals("missing")) {
                CuriosRendererRegistry.register(
                        item.get(),
                        () -> new ArmorCurioRenderer(texture)
                );
            }
        });
    }
}
