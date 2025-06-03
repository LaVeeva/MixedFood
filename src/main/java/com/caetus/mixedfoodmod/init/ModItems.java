package com.caetus.mixedfoodmod.init;

import com.caetus.mixedfoodmod.item.MixedFoodItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "mixedfoodmod");

    public static final RegistryObject<Item> MIXED_FOOD =
        ITEMS.register("mixed_food", MixedFoodItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
