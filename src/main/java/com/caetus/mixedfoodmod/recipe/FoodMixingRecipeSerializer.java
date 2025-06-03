package com.caetus.mixedfoodmod.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FoodMixingRecipeSerializer implements RecipeSerializer<FoodMixingRecipe> {
    @Override
    public FoodMixingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        return new FoodMixingRecipe(recipeId);
    }

    @Override
    public FoodMixingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return new FoodMixingRecipe(recipeId);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FoodMixingRecipe recipe) {
        // No extra data needed
    }
}
