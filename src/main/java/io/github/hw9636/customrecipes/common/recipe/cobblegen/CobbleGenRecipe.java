package io.github.hw9636.customrecipes.common.recipe.cobblegen;

import io.github.hw9636.customrecipes.common.recipe.BlockIngredient;
import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Random;

public class CobbleGenRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    final BlockIngredient block1, block2, blockUnder, blockAbove, output;

    public CobbleGenRecipe(ResourceLocation pId, BlockIngredient pBlock1, BlockIngredient pBlock2, BlockIngredient pBlockUnder, BlockIngredient pBlockAbove, BlockIngredient pOutput) {
        this.id = pId;
        this.block1 = pBlock1;
        this.block2 = pBlock2;
        this.blockUnder = pBlockUnder;
        this.blockAbove = pBlockAbove;
        this.output = pOutput;
    }

    public boolean isValid(Block pBlock1, Block pBlock2, Block pBlockUnder, Block pBlockAbove) {
        return this.block1.test(pBlock1.defaultBlockState()) && this.block2.test(pBlock2.defaultBlockState()) &&
                this.blockUnder.test(pBlockUnder.defaultBlockState()) && this.blockAbove.test(pBlockAbove.defaultBlockState());
    }

    public int getPriority() {
        return (this.blockUnder.getValidBlocks().length == 0 ? 1 : 0) + (this.blockAbove.getValidBlocks().length == 0 ? 1 : 0);
    }

    public Block[] getAllOutputs() {
        return this.output.getValidBlocks();
    }
    public Block getRandomOutput() {
        Random random = Minecraft.getInstance().font.random;
        Block[] blocks = getAllOutputs();
        return blocks[random.nextInt(blocks.length)];
    }

    public boolean hasBlockUnder() {
        return this.blockUnder.getValidBlocks().length != 0;
    }

    public boolean hasBlockAbove() {
        return this.blockAbove.getValidBlocks().length != 0;
    }


    @Override
    public boolean matches(@NotNull Container pContainer, @NotNull Level pLevel) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
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
                ", block1=" + block1 +
                ", block2=" + block2 +
                ", blockUnder=" + blockUnder +
                ", blockAbove=" + blockAbove +
                ", output=" + output +
                '}';
    }

    public static Comparator<CobbleGenRecipe> priorityComparator() {
        return Comparator.comparingInt(CobbleGenRecipe::getPriority);
    }
}
