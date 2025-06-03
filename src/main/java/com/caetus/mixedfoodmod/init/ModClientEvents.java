package com.caetus.mixedfoodmod.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "mixedfoodmod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    public static void register(IEventBus bus) {
        bus.addListener(ModClientEvents::clientSetup);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {}
}