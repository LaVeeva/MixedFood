package com.caetus.mixedfoodmod.item;

import com.caetus.mixedfoodmod.client.MixedFoodBEWLR;
import com.caetus.mixedfoodmod.util.NBTKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MixedFoodItem extends Item {
    public MixedFoodItem() {
        super(new Item.Properties()
            .stacksTo(64)
            .food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_LIST)) {
            ListTag ingredients = stack.getTag().getList(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_COMPOUND);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                CompoundTag ing = ingredients.getCompound(i);
                String id = ing.getString(NBTKeys.TAG_INGREDIENT_ID);
                if (i > 0) sb.append(", ");
                Item item = ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(id));
                if (item != null && item != Items.AIR) {
                    sb.append(item.getDescription().getString());
                } else {
                    sb.append("Unknown");
                }
            }
            if (sb.length() > 0) {
                return Component.literal(sb.toString());
            }
        }
        return super.getName(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player) || !stack.hasTag()) {
            return super.finishUsingItem(stack, level, entity);
        }

        CompoundTag tag = stack.getTag();
        if (tag.contains(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_LIST)) {
            ListTag ingredients = tag.getList(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_COMPOUND);

            for (int i = 0; i < ingredients.size(); i++) {
                CompoundTag ingTag = ingredients.getCompound(i);
                String id = ingTag.getString(NBTKeys.TAG_INGREDIENT_ID);
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

        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (stack.hasTag() && stack.getTag().contains(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_LIST)) {
            CompoundTag tag = stack.getTag();
            ListTag ingredientsNBT = tag.getList(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_COMPOUND);
            List<String> ingredientDisplayNames = new ArrayList<>();

            for (Tag ingTagElement : ingredientsNBT) {
                if (ingTagElement.getId() == Tag.TAG_COMPOUND) { 
                    CompoundTag ingCompound = (CompoundTag) ingTagElement;
                    String id = ingCompound.getString(NBTKeys.TAG_INGREDIENT_ID);
                    Item item = ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(id));
                    if (item != null && item != Items.AIR) {
                        ingredientDisplayNames.add(item.getDescription().getString());
                    } else {
                        ingredientDisplayNames.add(Component.translatable("tooltip.mixedfoodmod.unknown_item").getString());
                    }
                }
            }

            if (!ingredientDisplayNames.isEmpty()) {
                tooltip.add(Component.translatable("tooltip.mixedfoodmod.ingredients", String.join(", ", ingredientDisplayNames)).withStyle(ChatFormatting.GRAY));
            }

            int hunger = tag.getInt(NBTKeys.TAG_TOTAL_HUNGER);
            float saturation = tag.getFloat(NBTKeys.TAG_TOTAL_SATURATION);
            tooltip.add(Component.translatable("tooltip.mixedfoodmod.hunger", hunger).withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltip.mixedfoodmod.saturation", String.format("%.2f", saturation)).withStyle(ChatFormatting.AQUA));
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private MixedFoodBEWLR renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new MixedFoodBEWLR(
                        Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                        Minecraft.getInstance().getEntityModels()
                    );
                }
                return this.renderer;
            }
        });
    }
}