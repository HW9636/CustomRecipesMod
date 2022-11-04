package io.github.hw9636.customrecipes.common.recipe.cobblegen;

import io.github.hw9636.customrecipes.common.recipe.util.BlockIngredient;
import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.util.CustomRecipe;
import io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Random;

public class CobbleGenRecipe implements CustomRecipe {

    private final Random random = new Random();

    private final ResourceLocation id;
    final FluidIngredient fluid1, fluid2;
    final BlockIngredient blockUnder, blockAbove, output;

    public CobbleGenRecipe(ResourceLocation pId, FluidIngredient pFluid1, FluidIngredient pFluid2, BlockIngredient pBlockUnder, BlockIngredient pBlockAbove, BlockIngredient pOutput) {
        this.id = pId;
        this.fluid1 = pFluid1;
        this.fluid2 = pFluid2;
        this.blockUnder = pBlockUnder;
        this.blockAbove = pBlockAbove;
        this.output = pOutput;
    }

    public boolean isValid(FluidState pFluid1, FluidState pFluid2, Block pBlockUnder, Block pBlockAbove) {
        return this.fluid1.test(pFluid1) && this.fluid2.test(pFluid2) &&
                this.blockUnder.test(pBlockUnder) && this.blockAbove.test(pBlockAbove);
    }

    public int getPriority() {
        return (this.blockUnder.length() == 0 ? 1 : 0) + (this.blockAbove.length() == 0 ? 1 : 0);
    }

    public Block[] getAllOutputs() {
        return this.output.getAllBlocks().toArray(new Block[0]);
    }
    public Block getRandomOutput() {
        Block[] blocks = getAllOutputs();
        return blocks[random.nextInt(blocks.length)];
    }

    public boolean hasBlockUnder() {
        return this.blockUnder.length() != 0;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return CobbleGenSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CustomRecipeTypes.COBBLE_GEN;
    }

    @Override
    public String toString() {
        return "CobbleGenRecipe{" +
                "id=" + id +
                ", block1=" + fluid1 +
                ", block2=" + fluid2 +
                ", blockUnder=" + blockUnder +
                ", blockAbove=" + blockAbove +
                ", output=" + output +
                '}';
    }

    public static Comparator<CobbleGenRecipe> priorityComparator() {
        return Comparator.comparingInt(CobbleGenRecipe::getPriority);
    }
}
