package io.github.hw9636.customrecipes.datagen;

import io.github.hw9636.customrecipes.CustomRecipes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CustomRecipes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomRecipesDataGens {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {

    }
}
