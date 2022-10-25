package io.github.hw9636.customrecipes.mixin;

import com.sun.jna.platform.win32.NTSecApi;
import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.cobblegen.CobbleGenRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.world.level.block.LiquidBlock.POSSIBLE_FLOW_DIRECTIONS;

@SuppressWarnings("all")
@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {
    @Final
    @Shadow
    private FlowingFluid fluid;

    @Shadow
    protected abstract void fizz(LevelAccessor pLevel, BlockPos pPos);

    private boolean shouldSpreadLiquid(Level pLevel, BlockPos pPos, BlockState pState) {
        Block block1 = pState.getBlock();
        Block blockUnder = pPos.getY() > -64 ? pLevel.getBlockState(pPos.below()).getBlock() : Blocks.AIR;
        Block blockAbove = pPos.getY() > pLevel.getMaxBuildHeight() ? pLevel.getBlockState(pPos.above()).getBlock() : Blocks.AIR;

        for(Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
            BlockState blockState2 = pLevel.getBlockState(pPos.relative(direction));
            Block block2 = blockState2.getBlock();
            boolean otherIsSource = blockState2.getFluidState().isSource();
            if (blockState2.is(block1) && otherIsSource) continue;

            if (!otherIsSource) { // Cobble Gen Recipe
                CobbleGenRecipe[] recipes = pLevel.getRecipeManager().getAllRecipesFor(CustomRecipeTypes.COBBLE_GEN).stream()
                        .filter((r) -> r.isValid(block1, block2, blockUnder, blockAbove)).sorted(CobbleGenRecipe.priorityComparator())
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

                pLevel.setBlockAndUpdate(pPos, output.defaultBlockState());
                return false;
            }
            else { // Source Conversion Recipe

            }
        }

        return true;
    }
}
