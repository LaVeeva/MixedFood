package com.caetus.mixedfoodmod.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FoodMixingRecipeSerializer implements RecipeSerializer<FoodMixingRecipe> {
    @Override
    public FoodMixingRecipe fromJson(@javax.annotation.Nonnull ResourceLocation recipeId, @javax.annotation.Nonnull JsonObject json) {
        return new FoodMixingRecipe(recipeId);
    }

    @Override
    public FoodMixingRecipe fromNetwork(@javax.annotation.Nonnull ResourceLocation recipeId, @javax.annotation.Nonnull FriendlyByteBuf buffer) {
        return new FoodMixingRecipe(recipeId);
    }

    @Override
    public void toNetwork(@javax.annotation.Nonnull FriendlyByteBuf buffer, @javax.annotation.Nonnull FoodMixingRecipe recipe) {}
    
}
