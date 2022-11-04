package io.github.hw9636.customrecipes.common.recipe.cobblegen;

import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.util.BlockIngredient;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipeSerializer;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CobbleGenSerializer implements CustomRecipeSerializer<CobbleGenRecipe> {

    public static final CobbleGenSerializer INSTANCE = new CobbleGenSerializer();

    private CobbleGenSerializer() {

    }

    @Override
    public @NotNull CobbleGenRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        FluidIngredient fluid1 = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid1"));
        FluidIngredient fluid2 = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid2"));
        BlockIngredient blockUnder = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "under", EMPTY_BLOCKS_JSON));
        BlockIngredient blockAbove = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "above", EMPTY_BLOCKS_JSON));
        BlockIngredient output = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

        return new CobbleGenRecipe(pRecipeId, fluid1, fluid2, blockUnder, blockAbove, output);
    }

    @Nullable
    @Override
    public CobbleGenRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        FluidIngredient fluid1 = FluidIngredient.fromNetwork(pBuffer);
        FluidIngredient fluid2 = FluidIngredient.fromNetwork(pBuffer);
        BlockIngredient blockUnder = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient blockAbove = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient output = BlockIngredient.fromNetwork(pBuffer);

        return new CobbleGenRecipe(pRecipeId, fluid1, fluid2, blockUnder, blockAbove,output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull CobbleGenRecipe pRecipe) {
        pRecipe.fluid1.toNetwork(pBuffer);
        pRecipe.fluid2.toNetwork(pBuffer);
        pRecipe.blockUnder.toNetwork(pBuffer);
        pRecipe.blockAbove.toNetwork(pBuffer);
        pRecipe.output.toNetwork(pBuffer);
    }

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "cobble_gen");

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }
}
