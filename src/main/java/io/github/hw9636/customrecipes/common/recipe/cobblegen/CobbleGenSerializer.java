package io.github.hw9636.customrecipes.common.recipe.cobblegen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.BlockIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CobbleGenSerializer implements RecipeSerializer<CobbleGenRecipe> {

    public static final CobbleGenSerializer INSTANCE = new CobbleGenSerializer();

    private CobbleGenSerializer() {

    }

    private static final JsonObject emptyBlocksJson = new JsonObject();

    static {
        emptyBlocksJson.add("blocks", new JsonArray());
    }


    @Override
    public @NotNull CobbleGenRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        BlockIngredient block1 = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "block1"));
        BlockIngredient block2 = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "block2"));
        BlockIngredient blockUnder = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "under", emptyBlocksJson));
        BlockIngredient blockAbove = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "above", emptyBlocksJson));
        BlockIngredient output = BlockIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

        return new CobbleGenRecipe(pRecipeId, block1, block2, blockUnder, blockAbove, output);
    }

    @Nullable
    @Override
    public CobbleGenRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        BlockIngredient block1 = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient block2 = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient blockUnder = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient blockAbove = BlockIngredient.fromNetwork(pBuffer);
        BlockIngredient output = BlockIngredient.fromNetwork(pBuffer);

        return new CobbleGenRecipe(pRecipeId, block1, block2, blockUnder, blockAbove,output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull CobbleGenRecipe pRecipe) {
        pRecipe.block1.toNetwork(pBuffer);
        pRecipe.block2.toNetwork(pBuffer);
        pRecipe.blockUnder.toNetwork(pBuffer);
        pRecipe.blockAbove.toNetwork(pBuffer);
        pRecipe.output.toNetwork(pBuffer);
    }

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "cobble_gen");

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
