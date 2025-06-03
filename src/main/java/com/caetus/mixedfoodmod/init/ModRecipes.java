package com.caetus.mixedfoodmod.init;

import com.caetus.mixedfoodmod.recipe.FoodMixingRecipe;
import com.caetus.mixedfoodmod.recipe.FoodMixingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "mixedfoodmod");

    public static final RegistryObject<RecipeSerializer<FoodMixingRecipe>> FOOD_MIXING_SERIALIZER =
        SERIALIZERS.register("food_mixing", FoodMixingRecipeSerializer::new);

    public static final RecipeType<FoodMixingRecipe> FOOD_MIXING_TYPE =
        RecipeType.register("mixedfoodmod:food_mixing");

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
