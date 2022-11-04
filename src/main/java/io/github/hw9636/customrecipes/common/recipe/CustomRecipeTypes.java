package io.github.hw9636.customrecipes.common.recipe;

import io.github.hw9636.customrecipes.common.recipe.basaltgen.BasaltGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.fluiditemconversion.FluidItemConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.itemfluidconversion.ItemFluidConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.sourceconversion.SourceConversionRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class CustomRecipeTypes {
    public static final RecipeType<CobbleGenRecipe> COBBLE_GEN = new RecipeType<>() {};

    public static final RecipeType<BasaltGenRecipe> BASALT_GEN = new RecipeType<>() {};

    public static final RecipeType<SourceConversionRecipe> SOURCE_CONVERSION = new RecipeType<>() {};

    public static final RecipeType<FluidItemConversionRecipe> FLUID_ITEM_CONVERSION = new RecipeType<>() {};

    public static final RecipeType<ItemFluidConversionRecipe> ITEM_FLUID_CONVERSION = new RecipeType<>() {};

}
