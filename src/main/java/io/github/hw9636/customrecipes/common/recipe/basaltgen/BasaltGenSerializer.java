package io.github.hw9636.customrecipes.common.recipe.basaltgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.util.BlockIngredient;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipeSerializer;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BasaltGenSerializer implements CustomRecipeSerializer<BasaltGenRecipe> {

    public static final BasaltGenSerializer INSTANCE = new BasaltGenSerializer();

    private BasaltGenSerializer() {

    }

    @Override
    public @NotNull BasaltGenRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        FluidIngredient fluid = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid"));
        BlockIngredient block = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "block"));
        BlockIngredient blockUnder = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "under", EMPTY_BLOCKS_JSON));
        BlockIngredient blockAbove = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "above", EMPTY_BLOCKS_JSON));
        BlockIngredient output = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

        return new BasaltGenRecipe(pRecipeId, fluid, block, blockUnder, blockAbove, output);
    }

    @Nullable
    @Override
    public BasaltGenRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        FluidIngredient fluid = FluidIngredient.fromNetwork(pBuffer);
        BlockIngredient block = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient blockUnder = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient blockAbove = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient output = BlockIngredient.fromNetwork(pBuffer);

        return new BasaltGenRecipe(pRecipeId, fluid, block, blockUnder, blockAbove,output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull BasaltGenRecipe pRecipe) {
        pRecipe.fluid.toNetwork(pBuffer);
        pRecipe.block.toNetwork(pBuffer);
        pRecipe.blockUnder.toNetwork(pBuffer);
        pRecipe.blockAbove.toNetwork(pBuffer);
        pRecipe.output.toNetwork(pBuffer);
    }

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "basalt_gen");

    @Override
    public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<RecipeSerializer<?>> getRegistryType() {
        return (Class<RecipeSerializer<?>>) ((RecipeSerializer<?>)this).getClass();
    }
}
