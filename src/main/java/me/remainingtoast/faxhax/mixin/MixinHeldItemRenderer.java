package me.remainingtoast.faxhax.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.impl.modules.client.MainMenu;
import me.remainingtoast.faxhax.impl.modules.render.CustomFOV;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {

    private final CustomFOV fov = (CustomFOV) ModuleManager.getModule("CustomFOV");

    @Inject(
            at = {@At(value = "TAIL", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")},
            method = {"renderFirstPersonItem"}
    )
    public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if(fov != null && fov.enabled && fov.itemFov.getValue()){
            GlStateManager.scaled(fov.scaleX.getValue(), fov.scaleY.getValue(), fov.scaleZ.getValue());
            GlStateManager.translated(fov.posX.getValue(), fov.posY.getValue(), fov.posZ.getValue());
            GlStateManager.rotatef((float) (fov.rotationY.getValue() * 360.0f), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotatef((float) -(fov.rotationX.getValue() * 360.0f), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotatef((float) (fov.rotationZ.getValue() * 360.0f), 0.0f, 0.0f, 1.0f);
        }
    }

}
