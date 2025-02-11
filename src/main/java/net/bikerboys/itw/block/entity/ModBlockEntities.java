package net.bikerboys.itw.block.entity;

import net.bikerboys.itw.TutorialMod;
import net.bikerboys.itw.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<SewingStationBlockEntity>> SEWING_STATION_BE =
            BLOCK_ENTITIES.register("sewing_station_be", () -> BlockEntityType.Builder.of(SewingStationBlockEntity::new, ModBlocks.SEWING_STATION.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
