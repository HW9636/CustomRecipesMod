package io.github.hw9636.customrecipes.common.jei;

import io.github.hw9636.customrecipes.CustomRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CustomRecipesJEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(CustomRecipes.MODID, "jei");

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories();
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {

    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }
}
