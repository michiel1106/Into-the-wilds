package net.bikerboys.itw.item;

import net.bikerboys.itw.TutorialMod;
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

    //public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new DyeableArmorItem(ModMaterial.RED, HELMET, new Item.Properties()));

 //   public static final RegistryObject<Item> TEST_HELMET = ITEMS.register("red_helmet", () -> new DyeableArmorItem(ModMaterial.LEATHERS, ArmorItem.Type.HELMET, new Item.Properties()));
 //   public static final RegistryObject<Item> TEST_CHESTPLATE = ITEMS.register("red_chestplate", () -> new DyeableArmorItem(ModMaterial.LEATHERS, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
 //   public static final RegistryObject<Item> TEST_LEGGINGS = ITEMS.register("red_leggings", () -> new DyeableArmorItem(ModMaterial.LEATHERS, ArmorItem.Type.LEGGINGS, new Item.Properties()));
 //   public static final RegistryObject<Item> TEST_BOOTS = ITEMS.register("red_boots", () -> new DyeableArmorItem(ModMaterial.LEATHERS, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> JEANS = ITEMS.register("blue_jeans", () -> new ArmorItem(ModMaterial.JEANS, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MASK = ITEMS.register("black_mask", () -> new ArmorItem(ModMaterial.LEATHERS, ArmorItem.Type.HELMET, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
