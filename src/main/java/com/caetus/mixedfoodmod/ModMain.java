package com.caetus.mixedfoodmod;

import com.caetus.mixedfoodmod.init.ModClientEvents;
import com.caetus.mixedfoodmod.init.ModItems;
import com.caetus.mixedfoodmod.init.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("mixedfoodmod")
public class ModMain {
    public ModMain() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(bus);
        ModRecipes.register(bus);
        ModClientEvents.register(bus);
    }
}
