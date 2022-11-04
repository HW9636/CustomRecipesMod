package io.github.hw9636.customrecipes.common.recipe.itemfluidconversion;

import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipe;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ItemFluidConversionRecipe implements CustomRecipe {

    private final ResourceLocation id;

    final Ingredient item;
    final FluidIngredient fluid, outputs;

    public ItemFluidConversionRecipe(ResourceLocation pId, Ingredient pItem, FluidIngredient pFluid, FluidIngredient pOutput) {
        this.id = pId;

        this.item = pItem;
        this.fluid = pFluid;
        this.outputs = pOutput;
    }

    public boolean isAllowedFluid(FluidState fluid) {
        return this.fluid.test(fluid);
    }

    public boolean isIngredient(ItemStack item) {
        return this.item.test(item);
    }

    public Fluid[] getAllOutputs() {
        return this.outputs.getAllFluids().toArray(new Fluid[0]);
    }

    public Fluid getRandomFluid() {
        Random random = new Random();
        Fluid[] fluids = getAllOutputs();
        return fluids[random.nextInt(fluids.length)];
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ItemFluidConversionSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CustomRecipeTypes.ITEM_FLUID_CONVERSION;
    }
}
