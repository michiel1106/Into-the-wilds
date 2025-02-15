package net.bikerboys.itw.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.bikerboys.itw.TutorialMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SewingRecipe implements Recipe<Container> {
    private final NonNullList<Ingredient> inputitems;
    private final ItemStack output;
    private final ResourceLocation id;

    public SewingRecipe(NonNullList<Ingredient> inputitems, ItemStack output, ResourceLocation id) {
        this.inputitems = inputitems;
        this.output = output;
        this.id = id;
    }


    //   private final Ingredient input1;
  //  private final Ingredient input2;



    @Override
    public boolean matches(Container container, Level level) {
        if (level.isClientSide()) return false;

        ItemStack slot0 = container.getItem(0);
        ItemStack slot1 = container.getItem(1);
        boolean match1 = inputitems.get(0).test(slot0) && inputitems.get(1).test(slot1);
        boolean match2 = inputitems.get(0).test(slot1) && inputitems.get(1).test(slot0);

        TutorialMod.LOGGER.info("Testing recipe {}: Slot0={} Slot1={} Match1={} Match2={}",
                id, slot0.getItem(), slot1.getItem(), match1, match2);

        return match1 || match2;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        TutorialMod.LOGGER.info("Assembling recipe: {}", this.id); // Add this line
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        TutorialMod.LOGGER.info("sewingecipe getresultitem: " + output.copy());
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SewingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "itw_sewing";
    }

    public static class Serializer implements RecipeSerializer<SewingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(TutorialMod.MOD_ID, "itw_sewing");

        @Override
        public SewingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            TutorialMod.LOGGER.info("Loading Sewing Recipe: {}", pRecipeId);

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SewingRecipe(inputs, output, pRecipeId);
        }

        @Override
        public @Nullable SewingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            TutorialMod.LOGGER.info("inputs: {} output: {} precipeid: {} ", inputs, output, pRecipeId);
            return new SewingRecipe(inputs, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SewingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputitems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            TutorialMod.LOGGER.info("resultitem: " + pRecipe.getResultItem(null));
        }
    }
}
