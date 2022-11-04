package io.github.hw9636.customrecipes.common.recipe.sourceconversion;

import io.github.hw9636.customrecipes.common.recipe.util.BlockIngredient;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipe;
import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Random;


public class SourceConversionRecipe implements CustomRecipe {

    private final Random random = new Random();

    private final ResourceLocation id;
    final FluidIngredient source, converter;
    final BlockIngredient blockUnder, blockAbove, output;

    public SourceConversionRecipe(ResourceLocation pId, FluidIngredient pSource, FluidIngredient pConverter,
                                  BlockIngredient pBlockUnder, BlockIngredient pBlockAbove, BlockIngredient pOutput) {
        this.id = pId;

        this.source = pSource;
        this.converter = pConverter;
        this.blockUnder = pBlockUnder;
        this.blockAbove = pBlockAbove;
        this.output = pOutput;
    }

    public boolean isValid(FluidState pSource, FluidState pConverter, Block pBlockUnder, Block pBlockAbove) {
        return this.source.test(pSource) && this.converter.test(pConverter) &&
                this.blockUnder.test(pBlockUnder) && this.blockAbove.test(pBlockAbove);
    }

    public int getPriority() {
        return (this.blockUnder.length() == 0 ? 1 : 0) + (this.blockAbove.length() == 0 ? 1 : 0);
    }

    public boolean hasBlockUnder() {
        return this.blockUnder.length() != 0;
    }

    public Block[] getAllOutputs() {
        return this.output.getAllBlocks().toArray(new Block[0]);
    }

    public Block getRandomOutput() {
        Block[] blocks = getAllOutputs();
        return blocks[random.nextInt(blocks.length)];
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SourceConversionSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CustomRecipeTypes.SOURCE_CONVERSION;
    }

    public static Comparator<SourceConversionRecipe> priorityComparator() {
        return Comparator.comparingInt(SourceConversionRecipe::getPriority);
    }
}
