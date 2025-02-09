package net.bikerboys.itw.item;

import net.bikerboys.itw.TutorialMod;
import net.bikerboys.itw.armor.ModArmorTiers;
import net.bikerboys.itw.item.custom.TestArmor;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.ArmorItem.Type.HELMET;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MOD_ID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new DyeableArmorItem(ArmorMaterials.LEATHER, HELMET, new Item.Properties()));

    public static final RegistryObject<Item> TEST_HELMET = ITEMS.register("red_helmet", () -> new ArmorItem(ModArmorTiers.RED, ArmorItem.Type.HELMET, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
