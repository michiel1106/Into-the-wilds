package net.bikerboys.itw.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.bikerboys.itw.TutorialMod.MOD_ID;

public class ModRecipes {
//    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TutorialMod.MOD_ID);

  //  public static final RegistryObject<RecipeSerializer<SewingRecipe>> SEWING_SERIALIZER = SERIALIZERS.register("itw_sewing", () -> SewingRecipe.Serializer.INSTANCE);

  //  public static void register(IEventBus eventBus){
    //    SERIALIZERS.register(eventBus);
    //}

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);


    public static final RegistryObject<RecipeType<SewingRecipe>> SEWING_TYPE = RECIPE_TYPES.register(
            SewingRecipe.Type.ID,
            () -> SewingRecipe.Type.INSTANCE
    );


    public static final RegistryObject<RecipeSerializer<?>> SEWING_SERIALIZER = RECIPE_SERIALIZERS.register(
            SewingRecipe.Type.ID, // "itw_sewing"
            () -> SewingRecipe.Serializer.INSTANCE
    );

}
