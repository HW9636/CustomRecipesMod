package io.github.hw9636.customrecipes.common.recipe.sourceconversion;

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

public class SourceConversionSerializer implements CustomRecipeSerializer<SourceConversionRecipe> {
    public static final SourceConversionSerializer INSTANCE = new SourceConversionSerializer();

    private SourceConversionSerializer() {

    }


    @Override
    public @NotNull SourceConversionRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        FluidIngredient source = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "source"));
        FluidIngredient converter = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "converter"));
        BlockIngredient blockUnder = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "under", EMPTY_BLOCKS_JSON));
        BlockIngredient blockAbove = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "above", EMPTY_BLOCKS_JSON));
        BlockIngredient output = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

        return new SourceConversionRecipe(pRecipeId, source, converter, blockUnder, blockAbove, output);
    }

    @Nullable
    @Override
    public SourceConversionRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        FluidIngredient source = FluidIngredient.fromNetwork(pBuffer);
        FluidIngredient converter = FluidIngredient.fromNetwork(pBuffer);
        BlockIngredient blockUnder = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient blockAbove = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient output = BlockIngredient.fromNetwork(pBuffer);

        return new SourceConversionRecipe(pRecipeId, source, converter, blockUnder, blockAbove,output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull SourceConversionRecipe pRecipe) {
        pRecipe.source.toNetwork(pBuffer);
        pRecipe.converter.toNetwork(pBuffer);
        pRecipe.blockUnder.toNetwork(pBuffer);
        pRecipe.blockAbove.toNetwork(pBuffer);
        pRecipe.output.toNetwork(pBuffer);
    }

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "source_conversion");

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }
}
