package net.bikerboys.itw.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.momosoftworks.coldsweat.util.registries.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class CustomCurioItem extends Item implements ICurioItem {

    // Unique UUIDs for the attribute modifiers
    private static final UUID ARMOR_UUID = UUID.fromString("2c3c4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p");

    public CustomCurioItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        // Add attributes when the item is equipped
        LivingEntity entity = slotContext.entity();
        entity.getAttributes().addTransientAttributeModifiers(this.getAttributeModifiers(stack));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        // Remove attributes when the item is unequipped
        LivingEntity entity = slotContext.entity();
        entity.getAttributes().removeAttributeModifiers(this.getAttributeModifiers(stack));
    }

    // Define the attribute modifiers
    private Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        // Add armor attribute
        modifiers.put(
                ModAttributes.HEAT_DAMPENING,
                new AttributeModifier(ARMOR_UUID, "Curio Armor", 1, AttributeModifier.Operation.ADDITION)
        );

        // Add toughness attribute


        return modifiers;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true; // Allow equipping in any slot
    }
}