package io.github.hw9636.customrecipes.common.recipe.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.common.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface CustomRecipeSerializer<T extends Recipe<?>> extends RecipeSerializer<T> {

    JsonObject EMPTY_BLOCKS_JSON = JsonUtils.singleton("blocks", new JsonArray());

    @Override
    default RecipeSerializer<?> setRegistryName(ResourceLocation name) {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    default Class<RecipeSerializer<?>> getRegistryType() {
        return (Class<RecipeSerializer<?>>) ((RecipeSerializer<?>)this).getClass();
    }
}
