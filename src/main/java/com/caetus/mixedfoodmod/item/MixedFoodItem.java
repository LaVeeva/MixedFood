package com.caetus.mixedfoodmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class MixedFoodItem extends Item {
    public MixedFoodItem() {
        super(new Item.Properties()
            .stacksTo(64)
            .food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Ingredients", Tag.TAG_LIST)) {
            ListTag ingredients = stack.getTag().getList("Ingredients", Tag.TAG_COMPOUND);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                CompoundTag ing = ingredients.getCompound(i);
                String id = ing.getString("id");
                if (i > 0) sb.append(", ");
                Item item = ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(id));
                if (item != null) {
                    sb.append(item.getDescription().getString());
                }
            }
            return Component.literal(sb.toString());
        }
        return super.getName(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player) || !stack.hasTag()) {
            return super.finishUsingItem(stack, level, entity);
        }

        CompoundTag tag = stack.getTag();
        if (tag.contains("Ingredients", Tag.TAG_LIST)) {
            ListTag ingredients = tag.getList("Ingredients", Tag.TAG_COMPOUND);

            for (int i = 0; i < ingredients.size(); i++) {
                CompoundTag ingTag = ingredients.getCompound(i);
                String id = ingTag.getString("id");
                Item item = ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(id));
                if (item != null && item.isEdible()) {
                    FoodProperties props = item.getFoodProperties(new ItemStack(item), entity);
                    if (props != null) {
                        player.getFoodData().eat(props.getNutrition(), props.getSaturationModifier());
                    }
                    ItemStack virt = new ItemStack(item);
                    item.finishUsingItem(virt, level, entity);

                    if (entity instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, virt);
                    }
                }
            }
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            String ingredients = tag.getString("ingredients");
            int hunger = tag.getInt("totalHunger");
            float saturation = tag.getFloat("totalSaturation");
            tooltip.add(Component.literal("Ingredients: " + ingredients).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Hunger: " + hunger).withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.literal("Saturation: " + saturation).withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
