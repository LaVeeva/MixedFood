package com.caetus.mixedfoodmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "mixedfoodmod")
public class CustomMixedFoodRenderer extends BakedModelWrapper<BakedModel> {
    public CustomMixedFoodRenderer(BakedModel originalModel) {
        super(originalModel);
    }

    public void render(ItemStack stack,
                       ItemDisplayContext transformType,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {
        if (!stack.hasTag() || !stack.getTag().contains("Ingredients", Tag.TAG_LIST)) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.render(stack, transformType, false, poseStack, bufferSource, packedLight, packedOverlay, this.originalModel);
            return;
        }

        ListTag listTag = stack.getTag().getList("Ingredients", Tag.TAG_COMPOUND);
        int n = listTag.size();
        if (n == 0) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.render(stack, transformType, false, poseStack, bufferSource, packedLight, packedOverlay, this.originalModel);
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        for (int i = 0; i < n; i++) {
            String idStr = listTag.getCompound(i).getString("id");
            ItemStack ingStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(idStr)));
            BakedModel ingModel = itemRenderer.getModel(ingStack, null, null, packedLight);
            poseStack.pushPose();
            itemRenderer.render(ingStack, transformType, false, poseStack, bufferSource, packedLight, packedOverlay, ingModel);
            poseStack.popPose();
        }
    }
}
