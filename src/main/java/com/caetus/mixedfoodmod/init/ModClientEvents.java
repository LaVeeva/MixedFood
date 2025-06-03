package com.caetus.mixedfoodmod.init;

import com.caetus.mixedfoodmod.client.CustomMixedFoodRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod.EventBusSubscriber(modid = "mixedfoodmod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    public static void register(IEventBus bus) {
        bus.addListener(ModClientEvents::clientSetup);
    }

    private static void clientSetup(final net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        ResourceLocation itemModelRL = new ResourceLocation("mixedfoodmod", "mixed_food");
        ModelResourceLocation inventoryMRL = new ModelResourceLocation(itemModelRL, "inventory");

        BakedModel original = event.getModels().get(inventoryMRL);
        if (original == null) {
            return;
        }

        BakedModel wrapped = new CustomMixedFoodRenderer(original);
        event.getModels().put(inventoryMRL, wrapped);
    }
}
