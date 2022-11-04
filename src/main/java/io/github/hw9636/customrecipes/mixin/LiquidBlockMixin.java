package io.github.hw9636.customrecipes.mixin;

import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.basaltgen.BasaltGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenRecipe;
import io.github.hw9636.customrecipes.common.recipe.sourceconversion.SourceConversionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.world.level.block.LiquidBlock.POSSIBLE_FLOW_DIRECTIONS;

@SuppressWarnings("all")
@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin extends Block{
    @Final
    @Shadow
    private FlowingFluid fluid;

    public LiquidBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Shadow
    protected abstract void fizz(LevelAccessor pLevel, BlockPos pPos);

    private boolean shouldSpreadLiquid(Level pLevel, BlockPos pPos, BlockState pState) {
        Block block1 = pState.getBlock();
        FluidState fluidState = pState.getFluidState();
        Block blockUnder = pPos.getY() > -64 ? pLevel.getBlockState(pPos.below()).getBlock() : Blocks.AIR;
        Block blockAbove = pPos.getY() > pLevel.getMaxBuildHeight() ? pLevel.getBlockState(pPos.above()).getBlock() : Blocks.AIR;

        for(Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
            BlockState blockState2 = pLevel.getBlockState(pPos.relative(direction));
            FluidState fluid2 = blockState2.getFluidState();

            Block block2 = blockState2.getBlock();
            boolean otherIsSource = fluid2.isSource();
            if (blockState2.is(block1) && otherIsSource) continue;

            if (!fluid2.is(Fluids.EMPTY)) {
                if (!otherIsSource && !fluidState.isSource()) { // Cobble Gen Recipe
                    CobbleGenRecipe[] recipes = pLevel.getRecipeManager().getAllRecipesFor(CustomRecipeTypes.COBBLE_GEN).stream()
                            .filter((r) -> r.isValid(fluidState, fluid2, blockUnder, blockAbove)).sorted(CobbleGenRecipe.priorityComparator())
                            .toArray(CobbleGenRecipe[]::new);

                    Block output;
                    if (recipes.length == 0) continue;
                    if (recipes.length == 1 || recipes[0].getPriority() > recipes[1].getPriority()) {
                        output = recipes[0].getRandomOutput();
                    } else {
                        if (!recipes[0].hasBlockUnder() && recipes[1].hasBlockUnder())
                            output = recipes[1].getRandomOutput();
                        else output = recipes[0].getRandomOutput();
                    }

                    pLevel.setBlockAndUpdate(pPos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, pPos, pPos, output.defaultBlockState()));
                    return false;
                } else { // Source Conversion Recipe
                    SourceConversionRecipe[] recipes = pLevel.getRecipeManager().getAllRecipesFor(CustomRecipeTypes.SOURCE_CONVERSION).stream()
                            .filter((r) -> r.isValid(fluid2, fluidState, blockUnder, blockAbove)).sorted(SourceConversionRecipe.priorityComparator())
                            .toArray(SourceConversionRecipe[]::new);

                    Block output;
                    if (recipes.length == 0) continue;
                    if (recipes.length == 1 || recipes[0].getPriority() > recipes[1].getPriority()) {
                        output = recipes[0].getRandomOutput();
                    } else {
                        if (!recipes[0].hasBlockUnder() && recipes[1].hasBlockUnder())
                            output = recipes[1].getRandomOutput();
                        else output = recipes[0].getRandomOutput();
                    }

                    pLevel.setBlockAndUpdate(pPos.relative(direction), net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, pPos, pPos, output.defaultBlockState()));
                    return false;
                }
            }
            else { // Basalt Gen Recipe

                BasaltGenRecipe[] recipes = pLevel.getRecipeManager().getAllRecipesFor(CustomRecipeTypes.BASALT_GEN)
                        .stream().filter((r) -> r.isValid(fluidState, block2, blockUnder, blockAbove)).sorted(BasaltGenRecipe.priorityComparator())
                        .toArray(BasaltGenRecipe[]::new);

                Block output;
                if (recipes.length == 0) continue;
                if (recipes.length == 1 || recipes[0].getPriority() > recipes[1].getPriority()) {
                    output = recipes[0].getRandomOutput();
                } else {
                    if (!recipes[0].hasBlockUnder() && recipes[1].hasBlockUnder())
                        output = recipes[1].getRandomOutput();
                    else output = recipes[0].getRandomOutput();
                }

                pLevel.setBlockAndUpdate(pPos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, pPos, pPos, output.defaultBlockState()));
                return false;
            }
        }

        return true;
    }
}
