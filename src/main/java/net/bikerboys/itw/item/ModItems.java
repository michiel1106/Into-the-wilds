package net.bikerboys.itw.item;

import net.bikerboys.itw.TutorialMod;
import net.bikerboys.itw.util.ArmorTextures;
import net.bikerboys.itw.util.ModMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MOD_ID);




    public static final RegistryObject<Item> MASK = ITEMS.register("black_mask", () -> new ArmorItem(ModMaterial.LEATHERS, ArmorItem.Type.HELMET, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


    private static RegistryObject<Item> registerArmor(String id, String texturePath) {
        RegistryObject<Item> item = ITEMS.register(id,
                () -> new CustomCurioItem(new Item.Properties())
        );
        ArmorTextures.register(item.get(), texturePath);
        return item;
    }



}
