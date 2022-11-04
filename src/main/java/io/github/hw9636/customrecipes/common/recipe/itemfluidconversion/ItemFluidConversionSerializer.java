package io.github.hw9636.customrecipes.common.recipe.itemfluidconversion;

import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipeSerializer;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemFluidConversionSerializer implements CustomRecipeSerializer<ItemFluidConversionRecipe> {

    public static final ItemFluidConversionSerializer INSTANCE = new ItemFluidConversionSerializer();

    private ItemFluidConversionSerializer() {}

    @Override
    public @NotNull ItemFluidConversionRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        Ingredient item = Ingredient.fromJson(pSerializedRecipe.get("item"));
        FluidIngredient fluid = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid"));
        FluidIngredient outputs = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "outputs"));

        return new ItemFluidConversionRecipe(pRecipeId, item, fluid, outputs);
    }

    @Nullable
    @Override
    public ItemFluidConversionRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        Ingredient item = Ingredient.fromNetwork(pBuffer);
        FluidIngredient fluid = FluidIngredient.fromNetwork(pBuffer);
        FluidIngredient outputs = FluidIngredient.fromNetwork(pBuffer);

        return new ItemFluidConversionRecipe(pRecipeId, item, fluid, outputs);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull ItemFluidConversionRecipe pRecipe) {
        pRecipe.item.toNetwork(pBuffer);
        pRecipe.fluid.toNetwork(pBuffer);
        pRecipe.outputs.toNetwork(pBuffer);
    }

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "item_fluid_conversion");

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }
}
