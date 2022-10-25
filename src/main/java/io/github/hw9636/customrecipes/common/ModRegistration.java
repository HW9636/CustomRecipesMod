package io.github.hw9636.customrecipes.common;

import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistration {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CustomRecipes.MODID);

    public static final RegistryObject<RecipeSerializer<CobbleGenRecipe>> COBBLE_GEN_SERIALIZER = RECIPE_SERIALIZERS.register("cobble_gen",
            () -> CobbleGenSerializer.INSTANCE);
}
