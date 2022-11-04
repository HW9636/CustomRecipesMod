package io.github.hw9636.customrecipes.common.entity;

import io.github.hw9636.customrecipes.common.recipe.fluiditemconversion.FluidItemConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.itemfluidconversion.ItemFluidConversionRecipe;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class CustomItemEntity extends ItemEntity {

    // TODO: 10/26/2022 Make config
    private final int ticks = 5;
    private int tick;

    private final FluidItemConversionRecipe[] fluidRecipes;
    private final ItemFluidConversionRecipe[] itemRecipes;

    public CustomItemEntity(ItemEntity pOriginalEntity, FluidItemConversionRecipe[] fluidRecipes, ItemFluidConversionRecipe[] itemRecipes) {
        super(pOriginalEntity.level, pOriginalEntity.getX(), pOriginalEntity.getY(), pOriginalEntity.getZ(),
                pOriginalEntity.getItem(), pOriginalEntity.getDeltaMovement().x, pOriginalEntity.getDeltaMovement().y,
                pOriginalEntity.getDeltaMovement().z);

        this.fluidRecipes = fluidRecipes;
        this.itemRecipes = itemRecipes;
        this.setDefaultPickUpDelay();
        this.tick = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) return;

        if (++this.tick >= ticks) {
            this.tick = 0;

            FluidState fluidIn = this.level.getFluidState(this.blockPosition());
            if (fluidIn.is(Fluids.EMPTY)) return;

            for (FluidItemConversionRecipe recipe : fluidRecipes) {
                if (recipe.isAllowedFluid(fluidIn)) {
                    this.kill();

                    if (!recipe.consumesFluid()) {
                        int rolls = this.getItem().getCount();
                        for (int i = 0; i < rolls; i++) {
                            ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), recipe.getResultItem());
                            itemEntity.setDefaultPickUpDelay();
                            this.getCommandSenderWorld().addFreshEntity(itemEntity);
                        }
                    }
                    else {
                        this.level.setBlockAndUpdate(this.blockPosition(), Blocks.AIR.defaultBlockState());
                        ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), recipe.getResultItem());
                        itemEntity.setDefaultPickUpDelay();
                        this.getCommandSenderWorld().addFreshEntity(itemEntity);
                    }
                   return;
                }
            }

            for (ItemFluidConversionRecipe recipe : itemRecipes) {
                if (recipe.isAllowedFluid(fluidIn)) {
                    ItemStack itemStack = this.getItem();
                    if (itemStack.getCount() == 1) this.kill();
                    else itemStack.shrink(1);

                    this.getCommandSenderWorld().setBlockAndUpdate(this.blockPosition(),
                            recipe.getRandomFluid().defaultFluidState().createLegacyBlock());
                    return;
                }
            }
        }
    }
}
