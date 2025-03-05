package net.bikerboys.itw.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;



public class CuriosTestProvider extends CuriosDataProvider {

    public CuriosTestProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {

        this.createSlot("ring");
        this.createSlot("head").useNativeGui(true);
        this.createSlot("chest");

        this.createEntities("test").addSlots("ring", "head", "chest").addPlayer();
        //this.createEntities("itw_slot").addPlayer().addSlots("ring");
    }
}
