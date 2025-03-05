package net.bikerboys.itw.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CustomCurioItem extends Item implements ICurioItem {

    public CustomCurioItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // Optional: Add behavior when the item is equipped and ticking
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        // Optional: Add behavior when the item is equipped
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        // Optional: Add behavior when the item is unequipped
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        // Allow equipping in any slot
        return true;
    }
}