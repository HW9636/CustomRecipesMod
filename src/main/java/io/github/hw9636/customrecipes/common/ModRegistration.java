package io.github.hw9636.customrecipes.common;

import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.recipe.basaltgen.BasaltGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.basaltgen.BasaltGenSerializer;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenSerializer;
import io.github.hw9636.customrecipes.common.recipe.fluiditemconversion.FluidItemConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.fluiditemconversion.FluidItemConversionSerializer;
import io.github.hw9636.customrecipes.common.recipe.itemfluidconversion.ItemFluidConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.itemfluidconversion.ItemFluidConversionSerializer;
import io.github.hw9636.customrecipes.common.recipe.sourceconversion.SourceConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.sourceconversion.SourceConversionSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModRegistration {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CustomRecipes.MODID);

    public static final RegistryObject<RecipeSerializer<CobbleGenRecipe>> COBBLE_GEN_SERIALIZER = RECIPE_SERIALIZERS.register("cobble_gen",
            () -> CobbleGenSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<BasaltGenRecipe>> BASALT_GEN_SERIALIZER = RECIPE_SERIALIZERS.register("basalt_gen",
            () -> BasaltGenSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<SourceConversionRecipe>> SOURCE_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("source_conversion",
            () -> SourceConversionSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FluidItemConversionRecipe>> FLUID_ITEM_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("fluid_item_conversion",
            () -> FluidItemConversionSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ItemFluidConversionRecipe>> ITEM_FLUID_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("item_fluid_conversion",
            () -> ItemFluidConversionSerializer.INSTANCE);
}
