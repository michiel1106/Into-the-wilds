package net.bikerboys.itw.recipes;

import net.bikerboys.itw.TutorialMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {


    public static final DeferredRegister<RecipeType<?>> TYPE =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TutorialMod.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TutorialMod.MOD_ID);



    public static final RegistryObject<RecipeType<SewingRecipe>> SEWING =
            TYPE.register("itw_sewing", () -> SewingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<SecondSewingRecipe>> SECOND_SEWING =
            TYPE.register("itw_sewing_second", () -> SecondSewingRecipe.Type.INSTANCE);



    public static final RegistryObject<RecipeSerializer<SewingRecipe>> SEWING_SERIALIZER =
            SERIALIZERS.register("itw_sewing", () -> SewingRecipe.Serializer.INSTANCE);

  //  public static final RegistryObject<RecipeSerializer<SecondSewingRecipe>> SECOND_SEWING_SERIALIZER =
   //         SERIALIZERS.register("itw_sewing_second", () -> SecondSewingRecipe.Serializer);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPE.register(eventBus);
    }

}
