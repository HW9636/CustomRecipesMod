package io.github.hw9636.customrecipes.common.jei;

import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenRecipe;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CobbleGenRecipeCategory implements IRecipeCategory<CobbleGenRecipe> {

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public IDrawable getBackground() {
        return null;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }

    @Override
    public Class<? extends CobbleGenRecipe> getRecipeClass() {
        return null;
    }
}
