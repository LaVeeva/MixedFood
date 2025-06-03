package com.caetus.mixedfoodmod.client;

import com.caetus.mixedfoodmod.util.NBTKeys;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class MixedFoodBEWLR extends BlockEntityWithoutLevelRenderer {

    public MixedFoodBEWLR(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(@javax.annotation.Nonnull ItemStack stack, @javax.annotation.Nonnull ItemDisplayContext displayContext, @javax.annotation.Nonnull PoseStack poseStack, @javax.annotation.Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel baseModel = itemRenderer.getModel(stack, null, null, 0);

        if (!stack.hasTag() || !stack.getTag().contains(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_LIST)) {
            itemRenderer.render(stack, displayContext, false, poseStack, bufferSource, packedLight, packedOverlay, baseModel);
            return;
        }

        ListTag ingredientsTag = stack.getTag().getList(NBTKeys.TAG_INGREDIENTS_LIST, Tag.TAG_COMPOUND);
        if (ingredientsTag.isEmpty()) {
            itemRenderer.render(stack, displayContext, false, poseStack, bufferSource, packedLight, packedOverlay, baseModel);
            return;
        }

        poseStack.pushPose();

        if (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.FIXED || displayContext == ItemDisplayContext.GROUND) {
            itemRenderer.render(stack, displayContext, false, poseStack, bufferSource, packedLight, packedOverlay, baseModel);

            int maxIngredientsToShow = 4; 
            float scale = 0.5f; 
            float baseOffsetIncrement = 0.03f; 

            poseStack.translate(0.5, 0.5, 0.05); 

            int ingredientsRendered = 0;
            for (int i = 0; i < ingredientsTag.size() && ingredientsRendered < maxIngredientsToShow; i++) {
                if (ingredientsTag.get(i).getId() != Tag.TAG_COMPOUND) continue;
                String idStr = ingredientsTag.getCompound(i).getString(NBTKeys.TAG_INGREDIENT_ID);
                Item ingredientItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(idStr));

                if (ingredientItem != null && ingredientItem != Items.AIR) {
                    ItemStack ingredientStack = new ItemStack(ingredientItem);
                    BakedModel ingredientModel = itemRenderer.getModel(ingredientStack, null, null, 0);

                    poseStack.pushPose();
                    
                    float xOffset = ((ingredientsRendered % 2) - 0.5f) * scale * 0.8f;
                    float yOffset = ((ingredientsRendered / 2) - 0.5f) * scale * 0.8f;
                    
                    poseStack.translate(xOffset, yOffset, ingredientsRendered * baseOffsetIncrement);
                    poseStack.scale(scale, scale, scale);
                    
                    if (ingredientItem instanceof net.minecraft.world.item.BlockItem) {
                         poseStack.mulPose(Axis.XP.rotationDegrees(30f));
                         poseStack.mulPose(Axis.YP.rotationDegrees(45f));
                    }
                    itemRenderer.render(ingredientStack, ItemDisplayContext.NONE, false, poseStack, bufferSource, packedLight, packedOverlay, ingredientModel);
                    
                    poseStack.popPose();
                    ingredientsRendered++;
                }
            }
        } else {
            itemRenderer.render(stack, displayContext, false, poseStack, bufferSource, packedLight, packedOverlay, baseModel);
        }
        poseStack.popPose();
    }
}