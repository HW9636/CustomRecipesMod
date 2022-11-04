package io.github.hw9636.customrecipes.common.recipe.fluiditemconversion;

import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipeSerializer;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import io.github.hw9636.customrecipes.common.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidItemConversionSerializer implements CustomRecipeSerializer<FluidItemConversionRecipe> {

    public static final FluidItemConversionSerializer INSTANCE = new FluidItemConversionSerializer();

    private FluidItemConversionSerializer() {}

    @Override
    public @NotNull FluidItemConversionRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        Ingredient item = Ingredient.fromJson(pSerializedRecipe.get("item"));
        FluidIngredient fluid = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid"));
        Ingredient outputs = Ingredient.fromJson(pSerializedRecipe.get("outputs"));
        boolean consumesFluid = GsonHelper.getAsBoolean(pSerializedRecipe, "consumes_fluid", false);

        return new FluidItemConversionRecipe(pRecipeId, item, fluid, outputs, consumesFluid);
    }

    @Nullable
    @Override
    public FluidItemConversionRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        Ingredient item = Ingredient.fromNetwork(pBuffer);
        FluidIngredient fluid = FluidIngredient.fromNetwork(pBuffer);
        Ingredient outputs = Ingredient.fromNetwork(pBuffer);
        boolean consumesFluid = pBuffer.readBoolean();

        return new FluidItemConversionRecipe(pRecipeId, item, fluid, outputs, consumesFluid);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull FluidItemConversionRecipe pRecipe) {
        pRecipe.ingredient.toNetwork(pBuffer);
        pRecipe.fluid.toNetwork(pBuffer);
        pRecipe.outputs.toNetwork(pBuffer);
        pBuffer.writeBoolean(pRecipe.consumeFluid);
    }

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "fluid_item_conversion");

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }
}
