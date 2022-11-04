package io.github.hw9636.customrecipes.common.event;

import com.mojang.logging.LogUtils;
import io.github.hw9636.customrecipes.CustomRecipes;
import io.github.hw9636.customrecipes.common.entity.CustomItemEntity;
import io.github.hw9636.customrecipes.common.recipe.CustomRecipeTypes;
import io.github.hw9636.customrecipes.common.recipe.fluiditemconversion.FluidItemConversionRecipe;
import io.github.hw9636.customrecipes.common.recipe.itemfluidconversion.ItemFluidConversionRecipe;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = CustomRecipes.MODID)
public class WorldEvents {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (event.getWorld().isClientSide() || event.getEntity().getType() != EntityType.ITEM ||
                event.getEntity() instanceof CustomItemEntity) return;

        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack itemStack = itemEntity.getItem();

            FluidItemConversionRecipe[] fiRecipes = event.getWorld().getRecipeManager().getAllRecipesFor(CustomRecipeTypes.FLUID_ITEM_CONVERSION).stream()
                    .filter((r) -> r.isIngredient(itemStack)).toArray(FluidItemConversionRecipe[]::new);
            ItemFluidConversionRecipe[] ifRecipes = event.getWorld().getRecipeManager().getAllRecipesFor(CustomRecipeTypes.ITEM_FLUID_CONVERSION).stream()
                    .filter((r) -> r.isIngredient(itemStack)).toArray(ItemFluidConversionRecipe[]::new);

            if (fiRecipes.length != 0 || ifRecipes.length != 0) {
                event.setCanceled(true);
                CustomItemEntity newEntity = new CustomItemEntity(itemEntity, fiRecipes, ifRecipes);
                event.getWorld().addFreshEntity(newEntity);
                // TODO: 10/26/2022 BUG: Multiple entities causes entities to disappear
            }
        }
        else LOGGER.warn("Entity: {} did not pass sanity check", event.getEntity());
    }
}
