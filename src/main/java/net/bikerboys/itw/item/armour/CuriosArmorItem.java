package net.bikerboys.itw.item.armour;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CuriosArmorItem extends ArmorItem implements ICurioItem {
    public CuriosArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
}
