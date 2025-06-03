package com.caetus.mixedfoodmod.recipe;

import com.caetus.mixedfoodmod.init.ModItems;
import com.caetus.mixedfoodmod.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodMixingRecipe implements Recipe<net.minecraft.world.inventory.CraftingContainer> {
    private final ResourceLocation id;

    public FoodMixingRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level world) {
        boolean foundBowl = false;
        int foodCount = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() == Items.BOWL) {
                if (foundBowl) return false;
                foundBowl = true;
            } else if (stack.isEdible()) {
                foodCount++;
            } else {
                return false;
            }
        }
        return foundBowl && foodCount >= 2 && foodCount <= 8;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, net.minecraft.core.RegistryAccess access) {
        int totalHunger = 0;
        float totalSaturation = 0f;
        ListTag listTag = new ListTag();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty() || stack.getItem() == Items.BOWL) continue;
            if (stack.isEdible()) {
                FoodProperties fp = stack.getFoodProperties((LivingEntity) null);
                if (fp != null) {
                    int hunger = fp.getNutrition();
                    float sat = fp.getSaturationModifier() * hunger;
                    totalHunger += hunger;
                    totalSaturation += sat;
                }
                String key = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();

                CompoundTag entry = new CompoundTag();
                entry.putString("id", key);
                listTag.add(entry);
            }
        }

        ItemStack result = new ItemStack(ModItems.MIXED_FOOD.get());
        CompoundTag tag = new CompoundTag();
        tag.putInt("totalHunger", totalHunger);
        tag.putFloat("totalSaturation", totalSaturation);
        tag.put("Ingredients", listTag);
        result.setTag(tag);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.RegistryAccess access) {
        return new ItemStack(ModItems.MIXED_FOOD.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FOOD_MIXING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FOOD_MIXING_TYPE;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remains = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() == Items.BOWL) {
                remains.set(i, new ItemStack(Items.BOWL));
            }
        }
        return remains;
    }
}