package net.bikerboys.itw.curios;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BlueDiamondChestplateCurio extends Item implements ICurioItem {
    //private static final UUID ARMOR_UUID = UUID.fromString("2c3c4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f");
    //private static final UUID TOUGHNESS_UUID = UUID.fromString("1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p");

    public BlueDiamondChestplateCurio(Properties properties) {
        super(properties.defaultDurability(528).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // Optional: Handle durability or effects over time
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        entity.getAttributes().addTransientAttributeModifiers(createAttributeModifiers(stack));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        entity.getAttributes().removeAttributeModifiers(createAttributeModifiers(stack));
    }

    private Multimap<Attribute, AttributeModifier> createAttributeModifiers(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();

        return modifiers;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canSync(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public ICurio.@NotNull SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0f, 1.0f);
    }
}