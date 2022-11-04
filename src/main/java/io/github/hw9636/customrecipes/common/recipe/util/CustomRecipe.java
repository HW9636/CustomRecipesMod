package io.github.hw9636.customrecipes.common.recipe.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface CustomRecipe extends Recipe<Container> {
    @Override
    default boolean matches(@NotNull Container pContainer, @NotNull Level pLevel) {
        return false;
    }

    @Override
    default @NotNull ItemStack assemble(@NotNull Container pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    default @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    ResourceLocation getId();

    @Override
    @NotNull
    RecipeSerializer<?> getSerializer();

    @Override
    @NotNull
    RecipeType<?> getType();
}
