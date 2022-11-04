package io.github.hw9636.customrecipes.common.recipe.basaltgen;

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

public class BasaltGenRecipe implements CustomRecipe {

    private final Random random = new Random();

    private final ResourceLocation id;
    final FluidIngredient fluid;
    final BlockIngredient block, blockUnder, blockAbove, output;

    public BasaltGenRecipe(ResourceLocation pId, FluidIngredient pFluid, BlockIngredient pBlock, BlockIngredient pBlockUnder, BlockIngredient pBlockAbove, BlockIngredient pOutput) {
        this.id = pId;
        this.fluid = pFluid;
        this.block = pBlock;
        this.blockUnder = pBlockUnder;
        this.blockAbove = pBlockAbove;
        this.output = pOutput;
    }

    public boolean isValid(FluidState pFluid, Block pBlock, Block pBlockUnder, Block pBlockAbove) {
        return this.fluid.test(pFluid) && this.block.test(pBlock) &&
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
        return BasaltGenSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CustomRecipeTypes.BASALT_GEN;
    }

    @Override
    public String toString() {
        return "BasaltGenRecipe{" +
                "id=" + id +
                ", fluid=" + fluid +
                ", block=" + block +
                ", blockUnder=" + blockUnder +
                ", blockAbove=" + blockAbove +
                ", output=" + output +
                '}';
    }

    public static Comparator<BasaltGenRecipe> priorityComparator() {
        return Comparator.comparingInt(BasaltGenRecipe::getPriority);
    }
}
