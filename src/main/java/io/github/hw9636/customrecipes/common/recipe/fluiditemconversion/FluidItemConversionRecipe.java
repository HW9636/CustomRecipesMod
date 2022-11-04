package io.github.hw9636.customrecipes.common.recipe.fluiditemconversion;

import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipe;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FluidItemConversionRecipe implements CustomRecipe {

    private final ResourceLocation id;
    final Ingredient ingredient, outputs;
    final FluidIngredient fluid;
    final boolean consumeFluid;

    public FluidItemConversionRecipe(ResourceLocation pId, Ingredient ingredientToThrow, FluidIngredient pFluid, Ingredient pOutputs,
                                     boolean pConsumeFluid) {
        this.id = pId;

        this.ingredient = ingredientToThrow;
        this.fluid = pFluid;
        this.outputs = pOutputs;
        this.consumeFluid = pConsumeFluid;
    }

    public boolean isValid(ItemStack pItem, FluidState pFluid) {
        return this.isIngredient(pItem) && this.isAllowedFluid(pFluid);
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        ItemStack[] items = outputs.getItems();
        Random random = new Random();
        return items[random.nextInt(items.length)];
    }

    public boolean isAllowedFluid(FluidState fluid) {
        return this.fluid.test(fluid);
    }

    public boolean isIngredient(ItemStack item) {
        return this.ingredient.test(item);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return FluidItemConversionSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CustomRecipeTypes.FLUID_ITEM_CONVERSION;
    }

    public boolean consumesFluid() {
        return consumeFluid;
    }
}
