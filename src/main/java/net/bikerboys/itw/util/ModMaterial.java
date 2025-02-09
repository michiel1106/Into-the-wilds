package net.bikerboys.itw.util;

public enum ModMaterial {


    RED(1, 2, 2, 1, 0, 0, 0);

    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final float toughness;
    private final float knockbackResistance;

    ModMaterial(int bootReduce, int legReduce, int chestReduce, int headReduce, int enchantmentValue, float toughness, float knockbackResistance) {
        this.protectionAmounts = new int[]{headReduce, chestReduce, legReduce, bootReduce};
        this.enchantmentValue = enchantmentValue;
        this.knockbackResistance = knockbackResistance;
        this.toughness = toughness;
    }

    public int[] getProtectionAmounts() {
        return protectionAmounts;
    }

    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    public float getToughness() {
        return toughness;
    }

    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
